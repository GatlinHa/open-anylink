package com.hibob.anylink.netty.rpc.processor;

import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.protobuf.Body;
import com.hibob.anylink.common.protobuf.Header;
import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.netty.config.RefMsgIdConfig;
import com.hibob.anylink.netty.server.processor.MsgProcessor;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendMsgDeleteProcessor extends MsgProcessor implements SendMsgProcessor {

    private final RefMsgIdConfig refMsgIdConfig;

    @Override
    public void process(Map<String, Object> msgMap) throws Exception {
        MsgType msgType = MsgType.forNumber((Integer) msgMap.get("msgType"));
        String fromId = (String) msgMap.get("fromId");
        String fromClient = msgMap.get("fromClient").toString();
        String sessionId = msgMap.get("sessionId").toString();
        List<Long> deleteMsgIds = (List<Long>)msgMap.get("deleteMsgIds");
        String deleteMsgIdsStr = deleteMsgIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        long msgId = refMsgIdConfig.generateMsgId(sessionId);
        Header header = Header.newBuilder()
                .setMagic(Const.MAGIC)
                .setVersion(0) //TODO 服务器版本
                .setMsgType(msgType)
                .build();
        Body body = Body.newBuilder()
                .setFromId(fromId)
                .setFromClient(fromClient)
                .setMsgId(msgId)
                .setSessionId(sessionId)
                .setContent(deleteMsgIdsStr)
                .build();
        Msg msg = Msg.newBuilder().setHeader(header).setBody(body).build();
        syncOtherClients(msg, msgId); // 扩散给自己的其他客户端
    }

    @Override
    public void process(ChannelHandlerContext ctx, Msg msg) throws Exception {
        // do nothing
    }
}
