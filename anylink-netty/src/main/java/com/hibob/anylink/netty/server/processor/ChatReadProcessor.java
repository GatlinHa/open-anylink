package com.hibob.anylink.netty.server.processor;

import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.netty.config.RefMsgIdConfig;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatReadProcessor extends MsgProcessor{
    private final RefMsgIdConfig refMsgIdConfig;

    @Override
    public void process(ChannelHandlerContext ctx, Msg msg)  throws Exception{
        String fromId = msg.getBody().getFromId();
        String toId = msg.getBody().getToId();
        String sessionId = msg.getBody().getSessionId();
        Long msgId = refMsgIdConfig.generateMsgId(sessionId); // 生成msgId
        syncOtherClients(msg, msgId); // 扩散给自己的其他客户端
        sendToClients(msg, toId, msgId); // 扩散给接收端的（多个）客户端
        updateReadMsgId(fromId, sessionId, msg.getBody().getContent()); // 已读消息不入库，只记录最新的已读消息
    }
}
