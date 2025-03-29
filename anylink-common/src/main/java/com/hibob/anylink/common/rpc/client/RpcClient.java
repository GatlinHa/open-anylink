package com.hibob.anylink.common.rpc.client;

import com.hibob.anylink.common.rpc.service.*;
import lombok.Data;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Data
@Component
public class RpcClient {

    @DubboReference(check = false, timeout = 3000) //关闭启动检查，否则启动会依赖RPC服务端
    private UserRpcService userRpcService;

    @DubboReference(check = false, timeout = 3000) //关闭启动检查，否则启动会依赖RPC服务端
    private ChatRpcService chatRpcService;

    @DubboReference(check = false, timeout = 3000) //关闭启动检查，否则启动会依赖RPC服务端
    private GroupMngRpcService groupMngRpcService;

    @DubboReference(check = false, timeout = 3000) //关闭启动检查，否则启动会依赖RPC服务端
    private NettyRpcService nettyRpcService;

    @DubboReference(check = false, timeout = 3000) //关闭启动检查，否则启动会依赖RPC服务端
    private MtsRpcService mtsRpcService;

}
