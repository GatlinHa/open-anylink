package com.hibob.anylink.chat.rpc;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hibob.anylink.chat.entity.AtTable;
import com.hibob.anylink.chat.entity.MsgTable;
import com.hibob.anylink.chat.entity.RefMsgId;
import com.hibob.anylink.chat.entity.Session;
import com.hibob.anylink.chat.mapper.RefMsgIdMapper;
import com.hibob.anylink.chat.mapper.SessionMapper;
import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.constants.RedisKey;
import com.hibob.anylink.common.rpc.service.ChatRpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class ChatRpcServiceImpl implements ChatRpcService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final SessionMapper sessionMapper;
    private final RefMsgIdMapper refMsgIdMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    @Value("${custom.msg-ttl-in-redis:604800}")
    private int msgTtlInRedis;

    @Value("${custom.msg-capacity-in-redis:10000}")
    private int msgCapacityInRedis;

    @Override
    public long refMsgId(String sessionId, int refMsgIdDefault) {
        RefMsgId refMsgId = selectRefMsgId(sessionId);
        if (refMsgId == null) {
            // 创建session;
            createRefMsgId(sessionId, refMsgIdDefault);
            return refMsgIdDefault;
        }

        return refMsgId.getRefMsgId();
    }

    @Override
    public long updateAndGetRefMsgId(String sessionId, int refMsgIdStep, long curRefMsgId) {
        long newRefMsgId = curRefMsgId + refMsgIdStep;
        LambdaUpdateWrapper<RefMsgId> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(RefMsgId::getSessionId, sessionId)
                .eq(RefMsgId::getRefMsgId, curRefMsgId) // 乐观锁
                .set(RefMsgId::getRefMsgId, newRefMsgId);
        refMsgIdMapper.update(updateWrapper);
        return selectRefMsgId(sessionId).getRefMsgId();
    }

    @Override
    public void asyncSaveMsg(Map<String, Object> msg) {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> saveMsg(msg) ? 1 : 0, threadPoolExecutor);
        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("asyncSaveChat execute exception: {}", throwable.getCause());
            }
        });
    }

    @Override
    public boolean saveMsg(Map<String, Object> msg) {
        String sessionId = (String) msg.get("sessionId");
        MsgTable msgTable = new MsgTable();
        msgTable.setSessionId(sessionId);
        msgTable.setFromId((String) msg.get("fromId"));
        msgTable.setFromClient((String) msg.get("fromClient"));
        msgTable.setRemoteId((String) msg.get("remoteId"));
        msgTable.setMsgId((long) msg.get("msgId"));
        msgTable.setMsgType((int) msg.get("msgType"));
        msgTable.setContent((String) msg.get("content")); //客户端负责加密内容
        msgTable.setContentType((int) msg.get("contentType"));
        msgTable.setRevoke(false);
        msgTable.setMsgTime((Date) msg.get("msgTime"));
        msgTable.setCreateTime(new Date());
        MsgTable insert = mongoTemplate.insert(msgTable);
        if (insert.getId() != null) { //如果入库成功，id会有值
            insertToRedis((long) msg.get("msgId"), sessionId, msgTable);
            return true;
        }
        else {
            return false;
        }
    }

    private void insertToRedis(long msgId, String sessionId, Object value) {
        String key1 = RedisKey.CHAT_SESSION_MSG_ID + sessionId;
        redisTemplate.opsForZSet().add(key1, msgId + Const.SPLIT_V + new Date().getTime(), msgId);
        Long card = redisTemplate.opsForZSet().zCard(key1);
        if (card > msgCapacityInRedis) {
            // 超出限制，删除10%的数据
            redisTemplate.opsForZSet().removeRangeByScore(key1, 0, msgCapacityInRedis / 10);
        }
        String key2 = RedisKey.CHAT_SESSION_MSG + sessionId + Const.SPLIT_C + msgId;
        redisTemplate.opsForValue().set(key2, JSON.toJSONString(value), Duration.ofSeconds(msgTtlInRedis));
    }

    private void createRefMsgId(String sessionId, int refMsgIdDefault) {
        RefMsgId refMsgId = new RefMsgId();
        refMsgId.setSessionId(sessionId);
        refMsgId.setRefMsgId(refMsgIdDefault);
        refMsgIdMapper.insert(refMsgId);
    }

    private RefMsgId selectRefMsgId(String sessionId) {
        LambdaQueryWrapper<RefMsgId> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(RefMsgId::getSessionId, sessionId);
        List<RefMsgId> refMsgId = refMsgIdMapper.selectList(queryWrapper);
        if (refMsgId.size() > 0) {
            return refMsgId.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean updateReadMsgId(Map<String, Object> map) {
        LambdaUpdateWrapper<Session> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Session::getAccount, map.get("account"))
                .eq(Session::getSessionId, map.get("sessionId"))
                .set(Session::getReadMsgId, map.get("readMsgId"))
                .set(Session::getReadTime, map.get("readTime"));
        return sessionMapper.update(updateWrapper) > 0;
    }

    @Override
    @Transactional
    public boolean insertGroupSessions(String groupId, List<Map<String, Object>> sessionList) {
        // 先插入
        sessionMapper.batchInsertOrUpdate(sessionList);

        // 加群后，已读消息从这一刻的群最大消息算
        long max = (long) Double.MAX_VALUE;
        String key = RedisKey.CHAT_SESSION_MSG_ID + groupId;
        //查最后一条msgId
        Set<Object> objects = redisTemplate.opsForZSet().reverseRangeByScore(key, -1, max, 0, 1);
        long lastMsgId = 0;
        if (objects.size() > 0) {
            String[] split = ((String) objects.toArray()[0]).split(Const.SPLIT_V);
            lastMsgId = Long.parseLong(split[0]); // 最后一条msg的id
        }

        // 后更新
        for (Map<String, Object> param : sessionList) {
            sessionMapper.updateForJoin((String) param.get("account"), (String) param.get("sessionId"), lastMsgId);
        }

        return true;
    }

    @Override
    public boolean updateGroupSessionsForLeave(List<Map<String, Object>> sessionList) {
        return sessionMapper.batchUpdateForLeave(sessionList) > 0;
    }

    @Override
    public void asyncSaveAt(Map<String, Object> msg) {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> saveAt(msg) ? 1 : 0, threadPoolExecutor);
        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("asyncSaveAt execute exception: {}", throwable.getCause());
            }
        });
    }

    private boolean saveAt(Map<String, Object> msg) {
        AtTable atTable = new AtTable();
        atTable.setFromId((String) msg.get("fromId"));
        atTable.setFromClient((String) msg.get("fromClient"));
        atTable.setToId((String) msg.get("toId"));
        atTable.setSessionId((String) msg.get("sessionId"));
        atTable.setGroupId((String) msg.get("groupId"));
        atTable.setMsgId((Long) msg.get("msgId"));
        atTable.setReferMsgId((Long) msg.get("referMsgId"));
        atTable.setMsgTime((Date) msg.get("msgTime"));
        atTable.setCreateTime(new Date());
        AtTable insert = mongoTemplate.insert(atTable);
        if (insert.getId() != null) { //如果入库成功，id会有值
            return true;
        }
        else {
            return false;
        }
    }

}
