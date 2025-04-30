package com.hibob.anylink.netty.rpc.processor;

import java.util.Map;

public interface SendMsgProcessor {
    void process(Map<String, Object> msgMap) throws Exception;
}
