package com.hibob.anylink.netty.rpc;

import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.common.rpc.service.NettyRpcService;
import com.hibob.anylink.netty.rpc.processor.SendMsgProcessor;
import com.hibob.anylink.netty.rpc.processor.SendMsgProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class NettyRpcServiceImpl implements NettyRpcService {
    private final ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void sendMsg(Map<String, Object> msg) {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> handleSendMsg(msg) ? 1 : 0, threadPoolExecutor);
        future.whenComplete((result, throwable) -> {
            log.info("NettyRpcServiceImpl::sendMsg Complete : {}", result);
            if (throwable != null) {
                log.error("exception: {}", throwable.getCause());
            }
        });
    }

    private boolean handleSendMsg(Map<String, Object> msgMap) {
        MsgType msgType = MsgType.valueOf((Integer) msgMap.get("msgType"));
        SendMsgProcessor processor = SendMsgProcessorFactory.getProcessor(msgType);
        if (processor != null) {
            try {
                processor.process(msgMap);
                return true;
            } catch (Exception e) {
                log.error("{} handleSendMsg exception: {}", msgType, e.getMessage());
                return false;
            }
        }
        else {
            return false;
        }
    }

}
