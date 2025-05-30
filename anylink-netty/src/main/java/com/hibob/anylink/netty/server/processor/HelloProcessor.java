package com.hibob.anylink.netty.server.processor;

import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.constants.RedisKey;
import com.hibob.anylink.common.enums.ConnectStatus;
import com.hibob.anylink.common.rpc.client.RpcClient;
import com.hibob.anylink.common.utils.CommonUtil;
import com.hibob.anylink.netty.config.NacosConfig;
import com.hibob.anylink.common.protobuf.Header;
import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.netty.server.ws.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.hibob.anylink.common.constants.Const.SPLIT_C;
import static com.hibob.anylink.common.constants.Const.SPLIT_V;


@Component
public class HelloProcessor extends MsgProcessor{

    @Autowired
    private NacosConfig nacosConfig;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RpcClient rpcClient;

    @Override
    public void process(ChannelHandlerContext ctx, Msg msg)  throws Exception{
        writeCache(ctx);
        Header headerOut = Header.newBuilder()
                .setMagic(Const.MAGIC)
                .setVersion(0) //TODO 服务器版本
                .setMsgType(MsgType.HELLO)
                .build();
        Msg msgOut = Msg.newBuilder().setHeader(headerOut).build();
        ctx.writeAndFlush(msgOut);
    }

    private void writeCache(ChannelHandlerContext ctx) {
        String uniqueId = (String) ctx.channel().attr(AttributeKey.valueOf(Const.UNIQUE_ID)).get();
        String account = uniqueId.split(SPLIT_V)[0];
        String routeKey = RedisKey.NETTY_GLOBAL_ROUTE + uniqueId;
        String onlineKey = RedisKey.NETTY_ONLINE_CLIENT + account;
        String instance = CommonUtil.getLocalIp() + SPLIT_C + nacosConfig.getPort();
        redisTemplate.opsForValue().set(routeKey, instance, Duration.ofSeconds(Const.CHANNEL_EXPIRE)); //TODO 如果老化了，怎办么？
        redisTemplate.opsForSet().add(onlineKey, uniqueId);
        redisTemplate.expire(onlineKey, Duration.ofSeconds(Const.CACHE_ONLINE_EXPIRE)); //设置缓存过期时间，防止数据长时间不老化，导致与数据库不一致
        WebSocketServer.getLocalRoute().put(routeKey, ctx.channel());
        rpcClient.getUserRpcService().updateUserStatus(account, uniqueId, ConnectStatus.ONLINE);
    }
}
