package com.hibob.anylink.netty.rpc.processor;

import java.util.Map;

public interface SystemMsgProcessor {
    void processSystemMsg(Map<String, Object> msgMap) throws Exception;
}
