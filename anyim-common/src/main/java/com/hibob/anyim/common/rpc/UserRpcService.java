package com.hibob.anyim.common.rpc;

import java.util.List;
import java.util.Map;

public interface UserRpcService {
    List<String> queryOnline(String account);

    Map<String, Object> queryUserInfo(String account);

}
