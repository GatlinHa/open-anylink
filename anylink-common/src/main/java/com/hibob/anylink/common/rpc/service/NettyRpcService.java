package com.hibob.anylink.common.rpc.service;

import java.util.Map;

public interface NettyRpcService {
    /**
     * 其他服务调用RPC接口发送消息（异步）
     * @param msg xx消息
     * @return
     */
    void sendMsg(Map<String, Object> msg);
}
