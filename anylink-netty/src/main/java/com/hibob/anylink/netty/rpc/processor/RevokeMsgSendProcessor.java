package com.hibob.anylink.netty.rpc.processor;

import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.protobuf.Body;
import com.hibob.anylink.common.protobuf.Header;
import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.common.rpc.client.RpcClient;
import com.hibob.anylink.netty.config.RefMsgIdConfig;
import com.hibob.anylink.netty.server.processor.MsgProcessor;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RevokeMsgSendProcessor extends MsgProcessor implements MsgSendProcessor {

    private final RefMsgIdConfig refMsgIdConfig;
    private final RpcClient rpcClient;

    @Override
    public void process(Map<String, Object> msgMap) throws Exception {
        MsgType msgType = MsgType.forNumber((Integer) msgMap.get("msgType"));
        String fromId = (String) msgMap.get("fromId");
        String fromClient = msgMap.get("fromClient").toString();
        String sessionId = msgMap.get("sessionId").toString();
        boolean isGroupChat = (boolean) msgMap.get("isGroupChat");
        String remoteId = msgMap.get("remoteId").toString();
        String revokeMsgId = msgMap.get("revokeMsgId").toString();

        long msgId = refMsgIdConfig.generateMsgId(sessionId);
        Header header = Header.newBuilder()
                .setMagic(Const.MAGIC)
                .setVersion(0) //TODO 服务器版本
                .setMsgType(msgType)
                .build();

        if (isGroupChat) {
            Body body = Body.newBuilder()
                    .setFromId(fromId)
                    .setFromClient(fromClient)
                    .setGroupId(remoteId)
                    .setMsgId(msgId)
                    .setSessionId(sessionId)
                    .setContent(revokeMsgId)
                    .build();
            Msg msg = Msg.newBuilder().setHeader(header).setBody(body).build();
            List<String> members = rpcClient.getGroupMngRpcService().queryGroupMembers(remoteId);
            syncOtherClients(msg, msgId); // 扩散给自己的其他客户端
            sendToMembers(msg, members, msgId); // 发给群成员
        } else  {
            Body body = Body.newBuilder()
                    .setFromId(fromId)
                    .setFromClient(fromClient)
                    .setToId(remoteId)
                    .setMsgId(msgId)
                    .setSessionId(sessionId)
                    .setContent(revokeMsgId)
                    .build();
            Msg msg = Msg.newBuilder().setHeader(header).setBody(body).build();
            syncOtherClients(msg, msgId); // 扩散给自己的其他客户端
            sendToClients(msg, remoteId, msgId); // 扩散给接收端的（多个）客户端
        }
    }

    @Override
    public void process(ChannelHandlerContext ctx, Msg msg) throws Exception {
        // do nothing
    }
}
