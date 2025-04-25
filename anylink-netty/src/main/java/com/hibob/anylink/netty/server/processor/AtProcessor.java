package com.hibob.anylink.netty.server.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.common.rpc.client.RpcClient;
import com.hibob.anylink.netty.config.RefMsgIdConfig;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtProcessor extends MsgProcessor{
    private final RpcClient rpcClient;
    private final RefMsgIdConfig refMsgIdConfig;

    @Override
    public void process(ChannelHandlerContext ctx, Msg msg)  throws Exception{
        String fromId = msg.getBody().getFromId();
        String content = msg.getBody().getContent();
        Map<String, Object> jsonObject = new ObjectMapper().readValue(content, Map.class);
        boolean isAtAll = (boolean) jsonObject.get("isAtAll");
        int referMsgId = (int) jsonObject.get("referMsgId"); // @消息关联的是哪条实际消息
        List<String> toIds;
        if (isAtAll) {
            String groupId = msg.getBody().getGroupId();
            List<String> members = rpcClient.getGroupMngRpcService().queryGroupMembers(groupId);
            if (!members.contains(fromId)) {
                log.error("fromId is invalid: {}", fromId);
                return;
            }
            toIds = members;
            toIds.remove(fromId);

        } else {
            List<String> atList = (List<String>) jsonObject.get("atList");
            Set<String> uniqueAtList = new LinkedHashSet<>(atList); // 去重
            toIds = new ArrayList<>(uniqueAtList);
        }

        String sessionId = msg.getBody().getSessionId();
        Long msgIdCache = getSeqMsgIdCache(msg);
        if (msgIdCache != null) {
            sendDeliveredMsg(ctx, msg, msgIdCache); //只回复已送达，不做其他动作
            return;
        }

        Long msgId = refMsgIdConfig.generateMsgId(sessionId); // 生成msgId
        for (String id : toIds) {
            saveAt(msg, msgId, referMsgId, id); //@消息入库，当前采用服务方异步入库，因此不支持等待回调结果。
        }

        setSeqMsgIdCache(msg, msgId); // 缓存seq和msgId，用于msg重复性校验
        sendDeliveredMsg(ctx, msg, msgId); //回复已送达
        sendToMembers(msg, toIds, msgId); // 发给群成员
    }
}
