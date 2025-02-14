package com.hibob.anylink.netty.server.processor;

import com.hibob.anylink.common.enums.ConnectStatus;
import com.hibob.anylink.common.rpc.client.RpcClient;
import com.hibob.anylink.common.utils.CommonUtil;
import com.hibob.anylink.common.protobuf.Msg;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusSyncProcessor extends MsgProcessor{

    @Autowired
    private RpcClient rpcClient;
    @Override
    public void process(ChannelHandlerContext ctx, Msg msg)  throws Exception{
        String fromId = msg.getBody().getFromId();
        String fromClient = msg.getBody().getFromClient();
        String uniqueId = CommonUtil.conUniqueId(fromId, fromClient);
        int status = Integer.parseInt(msg.getBody().getContent());
        rpcClient.getUserRpcService().updateUserStatus(fromId, uniqueId, ConnectStatus.fromValue(status));
    }

}
