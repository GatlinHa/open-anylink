package com.hibob.anylink.chat.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hibob.anylink.chat.dto.request.*;
import com.hibob.anylink.chat.dto.vo.*;
import com.hibob.anylink.chat.entity.AtTable;
import com.hibob.anylink.chat.entity.MsgTable;
import com.hibob.anylink.chat.entity.Partition;
import com.hibob.anylink.chat.entity.Session;
import com.hibob.anylink.chat.mapper.PartitionMapper;
import com.hibob.anylink.chat.mapper.SessionMapper;
import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.constants.RedisKey;
import com.hibob.anylink.common.enums.ServiceErrorCode;
import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.common.rpc.client.RpcClient;
import com.hibob.anylink.common.session.ReqSession;
import com.hibob.anylink.common.utils.BeanUtil;
import com.hibob.anylink.common.utils.DateTimeUtil;
import com.hibob.anylink.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${custom.msg-ttl-in-redis:604800}")
    private int msgTtlInRedis;

    private final SessionMapper sessionMapper;
    private final PartitionMapper partitionMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final RpcClient rpcClient;

    public ResponseEntity<IMHttpResponse> pullMsg(PullChatMsgReq dto) {
        String account = ReqSession.getSession().getAccount();
        String sessionId = dto.getSessionId();
        int pageSize = dto.getPageSize();
        Long endMsgId = dto.getEndMsgId();
        Session session = sessionMapper.selectSession(account, sessionId);
        return ResultUtil.success(getMsgFromStore(session, pageSize, endMsgId, null));
    }

    public ResponseEntity<IMHttpResponse> history(ChatHistoryReq dto) {
        String sessionId = dto.getSessionId();
        int pageSize = dto.getPageSize();
        Session session = sessionMapper.selectSession(ReqSession.getSession().getAccount(), sessionId);
        return ResultUtil.success(getMsgFromDb(session,
                dto.getContentTypes(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getEndMsgId(),
                pageSize,
                ReqSession.getSession().getAccount()));

    }

    public ResponseEntity<IMHttpResponse> revokeMsg(RevokeMsgReq dto) {
        String redisKey = RedisKey.CHAT_SESSION_MSG + dto.getSessionId() + Const.SPLIT_C + dto.getRevokeMsgId();
        Object o = redisTemplate.opsForValue().get(redisKey);
        if (o != null) {
            MsgVO msg = JSON.parseObject( (String) o, MsgVO.class);
            if (!msg.isRevoke()) {
                msg.setRevoke(true);
                Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
                redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(msg), ttl == null ? msgTtlInRedis : ttl, TimeUnit.SECONDS);
            }
        }

        Query query = new Query();
        Criteria criteria = Criteria
                .where("sessionId").is(dto.getSessionId())
                .and("msgId").is(dto.getRevokeMsgId())
                .and("revoke").in(false, null);
        query.addCriteria(criteria);
        List<MsgTable> msgList = mongoTemplate.find(query, MsgTable.class);
        if (!msgList.isEmpty()) {
            Update update = new Update().set("revoke", true);
            mongoTemplate.updateMulti(query, update, MsgTable.class);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("msgType", MsgType.REVOKE.getNumber());
        map.put("fromId", ReqSession.getSession().getAccount());
        map.put("fromClient", ReqSession.getSession().getClientId());
        map.put("sessionId", dto.getSessionId());
        map.put("isGroupChat", dto.getIsGroupChat());
        map.put("remoteId", dto.getRemoteId());
        map.put("revokeMsgId", dto.getRevokeMsgId());
        rpcClient.getNettyRpcService().sendMsg(map);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> deleteMsg(DeleteMsgReq dto) {
        sessionMapper.updateForDelMsg(ReqSession.getSession().getAccount(), dto.getSessionId(), dto.getDeleteMsgIds());

        HashMap<String, Object> map = new HashMap<>();
        map.put("msgType", MsgType.DELETE.getNumber());
        map.put("fromId", ReqSession.getSession().getAccount());
        map.put("fromClient", ReqSession.getSession().getClientId());
        map.put("sessionId", dto.getSessionId());
        map.put("deleteMsgIds", dto.getDeleteMsgIds());
        rpcClient.getNettyRpcService().sendMsg(map);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> sessionList() {
        ReqSession reqSession = ReqSession.getSession();
        String account = reqSession.getAccount();
        List<Session> sessionList = sessionMapper.selectSessionListForChat(account);
        Map<String, ChatSessionVO> voMap = new HashMap<>();
        voMap.putAll(getChatSessionList(sessionList));
        voMap.putAll(getGroupChatSessionList(sessionList));
        return ResultUtil.success(voMap);
    }

    public ResponseEntity<IMHttpResponse> updateSession(UpdateSessionReq dto) {
        ReqSession reqSession = ReqSession.getSession();
        String account = reqSession.getAccount();
        String sessionId = dto.getSessionId();
        Boolean top = dto.getTop();
        Boolean dnd = dto.getDnd();
        String draft = dto.getDraft(); // 注意，当前端设置draft=""的意思是清空草稿
        String mark = dto.getMark();
        Integer partitionId = dto.getPartitionId();
        if (top == null && dnd == null && draft == null && mark == null && partitionId == null) {
            return ResultUtil.error(ServiceErrorCode.ERROR_CHAT_UPDATE_SESSION);
        }

        LambdaUpdateWrapper<Session> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Session::getAccount, account)
                .eq(Session::getSessionId, sessionId);
        if (top != null) updateWrapper.set(Session::isTop, top);
        if (dnd != null) updateWrapper.set(Session::isDnd, dnd);
        if (draft != null) updateWrapper.set(Session::getDraft, draft);
        if (mark != null) updateWrapper.set(Session::getMark, mark);
        if (partitionId != null) updateWrapper.set(Session::getPartitionId, partitionId.intValue());
        sessionMapper.update(updateWrapper);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> querySession(QuerySessionReq dto) {
        ChatSessionVO vo = getSessionById(ReqSession.getSession().getAccount(), dto.getSessionId());
        if (vo == null) {
            return ResultUtil.error(ServiceErrorCode.ERROR_CHAT_SESSION_NOT_EXIST);
        } else {
            return ResultUtil.success(vo);
        }
    }

    public ResponseEntity<IMHttpResponse> queryMessages(QueryMessagesReq dto) {
        String sessionId = dto.getSessionId();
        List<Long> msgIds = dto.getMsgIds();
        List<Object> resultRedis = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (Long msgId : msgIds) {
                String key2 = RedisKey.CHAT_SESSION_MSG + sessionId + Const.SPLIT_C + msgId;
                connection.get(key2.getBytes());
            }
            return null;
        });

        List<MsgVO> collect = resultRedis.stream().map(item -> JSON.parseObject((String) item, MsgVO.class)).collect(Collectors.toList());
        List<Long> msgIdInDb = new ArrayList<>();
        if (collect.size() < msgIds.size()) {
            Set<Long> msgIdInRedis = collect.stream().map(MsgVO::getMsgId).collect(Collectors.toSet());
            for (Long msgId : msgIds) {
                if (!msgIdInRedis.contains(msgId)) {
                    msgIdInDb.add(msgId);
                }
            }
        }

        if (!msgIdInDb.isEmpty()) {
            List<MsgTable> msgTables = getMsgFromDbByIn(sessionId, msgIdInDb);
            collect.addAll(msgTables.stream().map(item -> BeanUtil.copyProperties(item, MsgVO.class)).collect(Collectors.toList()));
        }

        return ResultUtil.success(collect);
    }

    public ResponseEntity<IMHttpResponse> createSession(CreateSessionReq dto) {
        ReqSession reqSession = ReqSession.getSession();
        String account = reqSession.getAccount();
        String sessionId = dto.getSessionId();
        String remoteId = dto.getRemoteId();
        int sessionType = dto.getSessionType();
        int result = sessionMapper.insertOrUpdate(account, sessionId, remoteId, sessionType);
        if (result > 0) {
            return ResultUtil.success(getSessionById(account, sessionId));
        }
        else {
            return ResultUtil.error(ServiceErrorCode.ERROR_CHAT_CREATE_SESSION);
        }
    }

    public ResponseEntity<IMHttpResponse> closeSession(CloseSessionReq dto) {
        ReqSession reqSession = ReqSession.getSession();
        String account = reqSession.getAccount();
        String sessionId = dto.getSessionId();

        // 这里采用软删除的方式
        LambdaUpdateWrapper<Session> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Session::getAccount, account)
                .eq(Session::getSessionId, sessionId)
                .set(Session::getClosed, true);
        int update = sessionMapper.update(updateWrapper);
        if (update > 0) {
            return ResultUtil.success();
        }
        else {
            return ResultUtil.error(ServiceErrorCode.ERROR_CHAT_DELETE_SESSION);
        }
    }


    public ResponseEntity<IMHttpResponse> createPartition(PartitionCreateReq dto) {
        log.info("UserService::createPartition");
        String account = ReqSession.getSession().getAccount();
        String partitionName = dto.getPartitionName();
        int partitionType = dto.getPartitionType();

        LambdaQueryWrapper<Partition> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Partition::getAccount, account);
        queryWrapper.orderByDesc(Partition::getPartitionId);
        queryWrapper.last("limit 1");
        Partition partitionLast = partitionMapper.selectOne(queryWrapper);
        int newId = 1; //0表示没有分组，1是第1个分组ID
        if (partitionLast != null) {
            newId = partitionLast.getPartitionId() + 1;
        }

        Partition partition = new Partition();
        partition.setAccount(account);
        partition.setPartitionId(newId);
        partition.setPartitionName(partitionName);
        partition.setPartitionType(partitionType);
        int insert = partitionMapper.insert(partition);
        if (insert < 1) {
            log.error("createPartition error");
            return ResultUtil.error(ServiceErrorCode.ERROR_CREATE_PARTITION);
        }

        PartitionVO vo = BeanUtil.copyProperties(partition, PartitionVO.class);
        return ResultUtil.success(vo);
    }

    public ResponseEntity<IMHttpResponse> queryPartition() {
        log.info("UserService::queryPartition");
        String account = ReqSession.getSession().getAccount();
        LambdaQueryWrapper<Partition> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Partition::getAccount, account);
        List<Partition> partitions = partitionMapper.selectList(queryWrapper);
        List<PartitionVO> voList = new ArrayList<>();
        partitions.forEach(item -> {
            PartitionVO vo = BeanUtil.copyProperties(item, PartitionVO.class);
            voList.add(vo);
        });

        return ResultUtil.success(voList);
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> delPartition(PartitionDelReq dto) {
        String account = ReqSession.getSession().getAccount();
        int partitionId = dto.getPartitionId();
        LambdaUpdateWrapper<Partition> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Partition::getAccount, account);
        updateWrapper.eq(Partition::getPartitionId, partitionId);
        int delete = partitionMapper.delete(updateWrapper);
        if (delete < 1) {
            log.error("delPartition error");
            return ResultUtil.error(ServiceErrorCode.ERROR_PARTITION_NO_EXIST);
        }

        // 还要把属于这个分区的session表中的partitionId置为默认0的状态
        LambdaUpdateWrapper<Session> updateSessionWrapper = Wrappers.lambdaUpdate();
        updateSessionWrapper.eq(Session::getAccount, account);
        updateSessionWrapper.eq(Session::getPartitionId, partitionId);
        updateSessionWrapper.set(Session::getPartitionId, 0);
        sessionMapper.update(updateSessionWrapper);
        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> updatePartition(PartitionUpdateReq dto) {
        String account = ReqSession.getSession().getAccount();
        int partitionId = dto.getPartitionId();
        String newPartitionName = dto.getNewPartitionName();
        LambdaUpdateWrapper<Partition> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Partition::getAccount, account);
        updateWrapper.eq(Partition::getPartitionId, partitionId);
        updateWrapper.set(Partition::getPartitionName, newPartitionName);
        int update = partitionMapper.update(updateWrapper);
        if (update < 1) {
            log.error("updatePartition error");
            return ResultUtil.error(ServiceErrorCode.ERROR_PARTITION_NO_EXIST);
        }

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> queryAt() {
        // 先从session表中查询群聊的sessionId和readMsgId
        String account = ReqSession.getSession().getAccount();
        LambdaQueryWrapper<Session> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Session::getAccount, account)
                .eq(Session::getSessionType, MsgType.GROUP_CHAT.getNumber())
                .eq(Session::getClosed, false)
                .select(Session::getSessionId, Session::getReadMsgId);
        List<Session> sessions = sessionMapper.selectList(queryWrapper);
        if (sessions == null || sessions.isEmpty()) {
            return ResultUtil.success();
        }

        List<Criteria> criteriaList = new ArrayList<>();
        for (Session session : sessions) {
            String sessionId = session.getSessionId();
            long readMsgId = session.getReadMsgId();
            Criteria c = Criteria.where("sessionId").is(sessionId).and("msgId").gt(readMsgId);
            criteriaList.add(c);
        }

        Criteria criteria = Criteria.where("toId").is(account).orOperator(criteriaList.toArray(new Criteria[0]));
        Query query = new Query(criteria);
        List<AtMessageVO> vos = mongoTemplate.find(query, AtTable.class)
                .stream()
                .map(item -> BeanUtil.copyProperties(item, AtMessageVO.class))
                .collect(Collectors.toList());
        return ResultUtil.success(vos);
    }

    /**
     * 从缓存或数据库获取消息
     * @param session 会话对象
     * @param pageSize pageSize
     * @param endMsgId 查询的结束msgId(不包括)
     * @param groupInfo 看调用方有没有groupInfoMap对象，有则传过来
     * @return
     */
    private ChatMessageVO getMsgFromStore(Session session, int pageSize, Long endMsgId, Map<String, Object> groupInfo) {
        ChatMessageVO result = new ChatMessageVO();
        // 获取sessionId下面的msgId集合
        String key1 = RedisKey.CHAT_SESSION_MSG_ID + session.getSessionId();
        long max = endMsgId == null ? Long.MAX_VALUE : endMsgId - 1;
        // 这里的count=1000是为了尽可能取多一点，保证去除被删除的消息，还有足够的返回给前端
        LinkedHashSet msgIds = (LinkedHashSet)redisTemplate.opsForZSet().reverseRangeByScore(key1, -1, max, 0, 1000);
        if (msgIds == null || msgIds.isEmpty()) {
            return result;
        }

        List<String> joinTime = new ArrayList<>();
        List<String> leaveTime = new ArrayList<>();
        boolean isGroupChat = session.getSessionType() == MsgType.GROUP_CHAT.getNumber();
        if (isGroupChat) {
            joinTime = session.getJoinTime() == null ? joinTime : session.getJoinTime();
            leaveTime = session.getLeaveTime() == null ? leaveTime : session.getLeaveTime();
            groupInfo = groupInfo == null ? rpcClient.getGroupMngRpcService().queryGroupInfo(session.getRemoteId()) : groupInfo;
        }

        List<Long> msgIdInRedis = new ArrayList<>();
        List<Long> msgIdInMongoDb = new ArrayList<>();
        long firstMsgId = Long.MAX_VALUE;
        long lastMsgId = 0;
        Date currentTime = new Date();
        for (Object o: msgIds) {
            String[] split = ((String)o).split(Const.SPLIT_V);
            long msgId = Long.parseLong(split[0]);
            long time = Long.parseLong(split[1]);
            if (session.getDelMsgIds().contains(msgId)) {
                continue;
            }

            if (isGroupChat && !(boolean)groupInfo.get("historyBrowse")) {
                // 判断每个msgId是否处于成员在群期间
                boolean isValid = false;
                for (int i = 0; i < joinTime.size(); i++) {
                    String format = "yyyy-MM-dd HH:mm:ss.SSSSSS";
                    long jt = DateTimeUtil.getMillisecondFromStr(joinTime.get(i), format);
                    long lt = i > leaveTime.size() - 1 ? Long.MAX_VALUE : DateTimeUtil.getMillisecondFromStr(leaveTime.get(i), format);
                    if (time > jt && time < lt) {
                        isValid = true;
                        break;
                    }
                }
                // msgId不是成员在群期间的，该成员无法获取
                if (!isValid) {
                    continue;
                }
            }

            if (msgId < firstMsgId) firstMsgId = msgId;
            if (msgId > lastMsgId) lastMsgId = msgId;
            if (currentTime.getTime() - time < msgTtlInRedis * 1000L) {
                msgIdInRedis.add(msgId);
            }
            else {
                msgIdInMongoDb.add(msgId);
            }

            if (msgIdInRedis.size() + msgIdInMongoDb.size() >= pageSize) {
                break;
            }
        }

        List<MsgVO> msgList = new ArrayList<>();
        if (!msgIdInRedis.isEmpty()) {
            // 获取每个msgId对应的msg内容
            List<Object> resultRedis = redisTemplate.executePipelined((RedisConnection connection) -> {
                for (Object msgId : msgIdInRedis) {
                    String key2 = RedisKey.CHAT_SESSION_MSG + session.getSessionId() + Const.SPLIT_C + msgId;
                    connection.get(key2.getBytes());
                }
                return null;
            });

            msgList.addAll(resultRedis.stream().map(item -> JSON.parseObject((String) item, MsgVO.class)).collect(Collectors.toList()));
        }

        if (!msgIdInMongoDb.isEmpty()) {
            List<MsgTable> msgTables = getMsgFromDbByIn(session.getSessionId(), msgIdInMongoDb);
            msgList.addAll(msgTables.stream().map(item -> BeanUtil.copyProperties(item, MsgVO.class)).collect(Collectors.toList()));
        }

        if (!msgList.isEmpty()) {
            result.setCount(msgIds.size());
            result.setFirstMsgId(firstMsgId);
            result.setLastMsgId(lastMsgId);
            result.setMsgList(msgList);
        }
        return result;
    }

    private Map<String, ChatSessionVO> getChatSessionList(List<Session> sessionList) {
        Map<String, ChatSessionVO> voMap = new HashMap<>();
        if (sessionList == null || sessionList.isEmpty()) {
            return  voMap;
        }

        List<String> remoteAccountList = sessionList.stream()
                .filter(item -> item.getSessionType() == MsgType.CHAT.getNumber())
                .map(Session::getRemoteId)
                .collect(Collectors.toList());
        if (remoteAccountList.size() == 0) {
            return voMap;
        }
        Map<String, Map<String, Object>> usersMap = rpcClient.getUserRpcService().queryUserInfoBatch(remoteAccountList);

        for (Session item : sessionList) {
            if (item.getSessionType() == MsgType.CHAT.getNumber()) {
                ChatSessionVO chatSessionvo = new ChatSessionVO();
                SessionVO sessionvo = BeanUtil.copyProperties(item, SessionVO.class);
                sessionvo.setObjectInfo(usersMap.get(item.getRemoteId()));
                sessionvo.setUnreadCount(getUnReadCount(item));
                chatSessionvo.setSession(sessionvo);
                chatSessionvo.setMsgList(getMsgFromStore(item, 30, null, null).getMsgList());
                voMap.put(item.getSessionId(), chatSessionvo);
            }
        }

        return voMap;
    }


    private Map<String, ChatSessionVO> getGroupChatSessionList(List<Session> sessionList) {
        Map<String, ChatSessionVO> voMap = new HashMap<>();
        if (sessionList == null || sessionList.isEmpty()) {
            return  voMap;
        }

        List<String> groupIdList = sessionList.stream()
                .filter(item -> item.getSessionType() == MsgType.GROUP_CHAT.getNumber())
                .map(Session::getRemoteId)
                .collect(Collectors.toList());
        if (groupIdList.size() == 0) {
            return  voMap;
        }
        Map<String, Map<String, Object>> groupInfoMap = rpcClient.getGroupMngRpcService().queryGroupInfoBatch(groupIdList);

        for (Session item : sessionList) {
            if (item.getSessionType() == MsgType.GROUP_CHAT.getNumber()) {
                ChatSessionVO chatSessionvo = new ChatSessionVO();
                SessionVO sessionvo = BeanUtil.copyProperties(item, SessionVO.class);
                sessionvo.setLeave((item.getJoinTime() == null ? 0 : item.getJoinTime().size())
                        == (item.getLeaveTime() == null ? 0 : item.getLeaveTime().size()));
                Map<String, Object> groupInfo = groupInfoMap.get(item.getRemoteId());
                sessionvo.setObjectInfo(groupInfoMap.get(item.getRemoteId()));
                sessionvo.setUnreadCount(getUnReadCount(item));
                chatSessionvo.setSession(sessionvo);
                chatSessionvo.setMsgList(getMsgFromStore(item, 30, null, groupInfo).getMsgList());
                voMap.put(item.getSessionId(), chatSessionvo);
            }
        }

        return  voMap;
    }

    private ChatSessionVO getSessionById(String account, String sessionId) {
        Session session = sessionMapper.selectSession(account, sessionId);
        if (session == null) {
            return null;
        }

        SessionVO sessionvo = BeanUtil.copyProperties(session, SessionVO.class);
        if (session.getSessionType() == MsgType.CHAT.getNumber()) {
            sessionvo.setObjectInfo(rpcClient.getUserRpcService().queryUserInfo(session.getRemoteId()));
        }
        else if(session.getSessionType() == MsgType.GROUP_CHAT.getNumber()) {
            sessionvo.setObjectInfo(rpcClient.getGroupMngRpcService().queryGroupInfo(session.getRemoteId()));
            sessionvo.setLeave((session.getJoinTime() == null ? 0 : session.getJoinTime().size())
                    == (session.getLeaveTime() == null ? 0 : session.getLeaveTime().size()));
        }

        // 如果这个session是删除状态，这里被查询到了说明要激活
        if (session.getClosed().booleanValue() == true) {
            LambdaUpdateWrapper<Session> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(Session::getAccount, account)
                    .eq(Session::getSessionId, sessionId)
                    .set(Session::getClosed, false);
            sessionMapper.update(updateWrapper);
        }

        ChatSessionVO vo = new ChatSessionVO();
        vo.setSession(sessionvo);
        return vo;
    }

    private List<MsgTable> getMsgFromDbByIn(String sessionId, List<Long> msgIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sessionId").is(sessionId).and("msgId").in(msgIds));
        query.with(Sort.by(Sort.Order.asc("msgId")));
        return mongoTemplate.find(query, MsgTable.class);
    }

    private ChatMessageVO getMsgFromDb(Session session,
                                       List<Integer> contentTypes,
                                       Long startTime,
                                       Long endTime,
                                       Long endMsgId,
                                       int pageSize,
                                       String account) {
        Query query = new Query();
        Criteria criteria = Criteria
                .where("sessionId").is(session.getSessionId());

        if (startTime != null && endTime != null) {
            criteria.and("msgTime").gte(new Date(startTime)).lte(new Date(endTime));
        }

        if (contentTypes != null && contentTypes.size() > 0) {
            if (contentTypes.contains(8)) {
                // 对于@我的消息，要特殊处理
                Criteria c = Criteria
                        .where("sessionId").is(session.getSessionId())
                        .and("toId").is(account);

                if (endMsgId != null) {
                    c.and("referMsgId").lt(endMsgId);
                }

                List<Long> msgIds = mongoTemplate.find(new Query(c), AtTable.class)
                        .stream()
                        .map(item -> item.getReferMsgId())
                        .collect(Collectors.toList());
                criteria.and("msgId").in(msgIds);
            } else {
                criteria.and("contentType").in(contentTypes);
                if (endMsgId != null) {
                    criteria.and("msgId").lt(endMsgId);
                }
            }
        } else  {
            if (endMsgId != null) {
                criteria.and("msgId").lt(endMsgId);
            }
        }

        List<Long> delMsgIds = session.getDelMsgIds();
        if (delMsgIds != null && !delMsgIds.isEmpty()) {
            criteria.andOperator(new Criteria().and("msgId").nin(delMsgIds));
        }
        query.addCriteria(criteria);

        long count = mongoTemplate.count(query, MsgTable.class);
        query.with(Sort.by(Sort.Order.desc("msgId")));
        query.limit(pageSize);
        List<MsgVO> msgList = mongoTemplate.find(query, MsgTable.class)
                .stream().map(item -> BeanUtil.copyProperties(item, MsgVO.class)).collect(Collectors.toList());
        long lastMsgId = 0;
        if (!msgList.isEmpty()) {
            lastMsgId = msgList.get(msgList.size() - 1).getMsgId();  //lastMsgId更新
        }
        ChatMessageVO chatMessageVO = new ChatMessageVO();
        chatMessageVO.setCount((int) count);
        chatMessageVO.setLastMsgId(lastMsgId);
        chatMessageVO.setMsgList(msgList);
        return chatMessageVO;
    }

    private int getUnReadCount(Session session) {
        List<String> joinTime = new ArrayList<>();
        List<String> leaveTime = new ArrayList<>();
        boolean isLeaveGroup = false;
        if (session.getSessionType() == MsgType.GROUP_CHAT.getNumber()) {
            joinTime = session.getJoinTime() == null ? joinTime : session.getJoinTime();
            leaveTime = session.getLeaveTime() == null ? leaveTime : session.getLeaveTime();
            isLeaveGroup = joinTime.size() == leaveTime.size();
        }

        Date endTime = null;
        if (isLeaveGroup) {
            endTime = DateTimeUtil.getDateFromStr(leaveTime.get(leaveTime.size() - 1), "yyyy-MM-dd HH:mm:ss.SSSSSS");
        }
        return getUnReadCount(session.getSessionId(), session.getAccount(), session.getReadMsgId(), endTime, session.getDelMsgIds());
    }

    private int getUnReadCount(String sessionId, String account, long readMsgId, Date endTime, List<Long> delMsgIds) {
        Query query = new Query();
        Criteria criteria = Criteria
            .where("sessionId").is(sessionId)
            .and("fromId").ne(account) // 不是本人发的消息才记入未读
            .and("msgType").in(MsgType.CHAT.getNumber(), MsgType.GROUP_CHAT.getNumber());

        Criteria msgIdCondition = new Criteria().and("msgId").gt(readMsgId);
        if (delMsgIds != null && !delMsgIds.isEmpty()) {
            msgIdCondition.andOperator(Criteria.where("msgId").nin(delMsgIds));
        }
        criteria.andOperator(msgIdCondition);

        if (endTime != null) {
            criteria.and("msgTime").lt(endTime);
        }
        query.addCriteria(criteria);
        return (int) mongoTemplate.count(query, MsgTable.class);
    }

}
