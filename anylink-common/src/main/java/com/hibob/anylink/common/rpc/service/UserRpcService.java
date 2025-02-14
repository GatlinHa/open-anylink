package com.hibob.anylink.common.rpc.service;

import com.hibob.anylink.common.enums.ConnectStatus;

import java.util.List;
import java.util.Map;

public interface UserRpcService {
    List<String> queryOnline(String account);

    Map<String, Object> queryUserInfo(String account);

    Map<String, Map<String, Object>> queryUserInfoBatch(List<String> accountList);

    boolean updateUserStatus(String account, String uniqueId, ConnectStatus status);

    Map<String, Integer> queryUserStatus(List<String> accountList);

}
