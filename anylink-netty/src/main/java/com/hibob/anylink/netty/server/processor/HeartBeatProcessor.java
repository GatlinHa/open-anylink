package com.hibob.anylink.netty.server.processor;

import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.protobuf.Header;
import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.common.protobuf.MsgType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public class HeartBeatProcessor extends MsgProcessor{

    @Override
    public void process(ChannelHandlerContext ctx, Msg msg)  throws Exception{
        Header headerOut = Header.newBuilder()
                .setMagic(Const.MAGIC)
                .setVersion(0)
                .setMsgType(MsgType.HEART_BEAT)
                .build();
        Msg msgOut = Msg.newBuilder().setHeader(headerOut).build();
        ctx.writeAndFlush(msgOut);
    }
}
