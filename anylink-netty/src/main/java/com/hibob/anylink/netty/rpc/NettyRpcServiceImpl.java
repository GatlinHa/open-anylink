package com.hibob.anylink.netty.rpc;

import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.common.rpc.service.NettyRpcService;
import com.hibob.anylink.netty.rpc.processor.MsgSendProcessor;
import com.hibob.anylink.netty.rpc.processor.MsgSendProcessorFactory;
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
    public void sendSysMsg(Map<String, Object> msgMap) {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> handleSendSysMsg(msgMap) ? 1 : 0, threadPoolExecutor);
        future.whenComplete((result, throwable) -> {
            log.info("NettyRpcServiceImpl::sendSysMsg Complete : {}", result);
            if (throwable != null) {
                log.error("exception: {}", throwable.getCause());
            }
        });
    }

    @Override
    public void sendRevokeMsg(Map<String, Object> msgMap) {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> handleSendRevokeMsg(msgMap) ? 1 : 0, threadPoolExecutor);
        future.whenComplete((result, throwable) -> {
            log.info("NettyRpcServiceImpl::sendRevokeMsg Complete : {}", result);
            if (throwable != null) {
                log.error("exception: {}", throwable.getCause());
            }
        });
    }

    private boolean handleSendSysMsg(Map<String, Object> msgMap) {
        MsgType msgType = MsgType.valueOf((Integer) msgMap.get("msgType"));
        MsgSendProcessor processor = MsgSendProcessorFactory.getProcessor(msgType);
        if (processor != null) {
            try {
                processor.process(msgMap);
                return true;
            } catch (Exception e) {
                log.error("system msg {} handle exception: {}", msgType, e.getMessage());
                return false;
            }
        }
        else {
            return false;
        }
    }

    private boolean handleSendRevokeMsg(Map<String, Object> msgMap) {
        MsgType msgType = MsgType.valueOf((Integer) msgMap.get("msgType"));
        MsgSendProcessor processor = MsgSendProcessorFactory.getProcessor(msgType);
        if (processor != null) {
            try {
                processor.process(msgMap);
                return true;
            } catch (Exception e) {
                log.error("revoke msg {} handle exception: {}", msgType, e.getMessage());
                return false;
            }
        }
        else {
            return false;
        }
    }
}
