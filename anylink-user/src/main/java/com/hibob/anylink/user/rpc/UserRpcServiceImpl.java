package com.hibob.anylink.user.rpc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hibob.anylink.common.enums.ConnectStatus;
import com.hibob.anylink.common.rpc.client.RpcClient;
import com.hibob.anylink.common.rpc.service.UserRpcService;
import com.hibob.anylink.common.utils.BeanUtil;
import com.hibob.anylink.user.dto.vo.UserVO;
import com.hibob.anylink.user.entity.User;
import com.hibob.anylink.user.entity.UserStatus;
import com.hibob.anylink.user.mapper.UserMapper;
import com.hibob.anylink.user.mapper.UserStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class UserRpcServiceImpl implements UserRpcService {

    private final UserMapper userMapper;
    private final UserStatusMapper userStatusMapper;
    private final RpcClient rpcClient;

    @Override
    public List<String> queryOnline(String account) {
        log.info("UserRpcServiceImpl::queryOnline start......");
        LambdaQueryWrapper<UserStatus> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .select(UserStatus::getUniqueId)
                .eq(UserStatus::getAccount, account)
                .gt(UserStatus::getStatus, 0);

        List<String> list = new ArrayList<>();
        userStatusMapper.selectList(queryWrapper).forEach(x -> {
            list.add(x.getUniqueId());
        });
        return list;
    }

    @Override
    public Map<String, Object> queryUserInfo(String account) {
        log.info("UserRpcServiceImpl::queryUserInfo start......");
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("password"));
        queryWrapper.eq(User::getAccount, account);
        List<User> users = userMapper.selectList(queryWrapper);
        if (users.size() > 0) {
            try {
                User user = users.get(0);
                long avatarId = user.getAvatarId();
                String avatar = "";
                String avatarThumb = "";
                Map<String, Map<String, Object>> mapMap = rpcClient.getMtsRpcService().queryImageSignUrl(Arrays.asList(avatarId));
                Map<String, Object> objectMap = mapMap.get(Long.toString(avatarId));
                if (objectMap != null) {
                    avatar = objectMap.get("originUrl").toString();
                    avatarThumb = objectMap.get("thumbUrl").toString();
                }
                UserStatus userStatus = userStatusMapper.queryStatus(account);
                user.setStatus(userStatus == null ? ConnectStatus.OFFLINE.getValue() : userStatus.getStatus());
                UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
                vo.setAvatarId(avatarId);
                vo.setAvatar(avatar);
                vo.setAvatarThumb(avatarThumb);
                vo.setBirthday(null); //查询别人信息不返回生日信息
                return BeanUtil.objectToMap(vo);
            } catch (IllegalAccessException e) {
                log.error("UserRpcServiceImpl::queryUserInfo type conversion error......exception: {}", e.getMessage());
            }
        }
        return null;
    }


    @Override
    public Map<String, Map<String, Object>> queryUserInfoBatch(List<String> accountList) {
        log.info("UserRpcServiceImpl::queryUserInfoBatch start......");
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("password"));
        queryWrapper.in(User::getAccount, accountList.toArray());
        List<User> users = userMapper.selectList(queryWrapper);
        Map<String, Integer> statusMap = userStatusMapper.queryStatusByAccountList(accountList);
        Map<String, Map<String, Object>> result = new HashMap();
        try {
            List<Long> avatarIds = users.stream().map(User::getAvatarId).collect(Collectors.toList());
            Map<String, Map<String, Object>> mapMap = rpcClient.getMtsRpcService().queryImageSignUrl(avatarIds);

            for (User user : users) {
                if (statusMap == null || statusMap.get(user.getAccount()) == null) {
                    user.setStatus(ConnectStatus.OFFLINE.getValue());
                }
                else {
                    user.setStatus(statusMap.get(user.getAccount()));
                }

                long avatarId = user.getAvatarId();
                String avatar = "";
                String avatarThumb = "";
                Map<String, Object> objectMap = mapMap.get(Long.toString(avatarId));
                if (objectMap != null) {
                    avatar = objectMap.get("originUrl").toString();
                    avatarThumb = objectMap.get("thumbUrl").toString();
                }

                // 把User对象转成返回对象
                UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
                vo.setAvatarId(avatarId);
                vo.setAvatar(avatar);
                vo.setAvatarThumb(avatarThumb);
                vo.setBirthday(null); //查询别人信息不返回生日信息
                result.put(user.getAccount(), BeanUtil.objectToMap(vo));
            }
        } catch (IllegalAccessException e) {
            log.error("UserRpcServiceImpl::queryUserInfoBatch type conversion error......exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public boolean updateUserStatus(String account, String uniqueId, ConnectStatus status) {
        log.info("UserRpcServiceImpl::updateUserStatus start......");

        // 先查询，没有就插入，有则更新
        LambdaQueryWrapper<UserStatus> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserStatus::getAccount, account)
                .eq(UserStatus::getUniqueId, uniqueId);
        UserStatus userStatus = userStatusMapper.selectOne(queryWrapper);
        if (userStatus == null) {
            userStatus = new UserStatus();
            userStatus.setAccount(account);
            userStatus.setUniqueId(uniqueId);
            userStatus.setStatus(status.getValue());
            return userStatusMapper.insert(userStatus) > 0;
        }
        else {
            LambdaUpdateWrapper<UserStatus> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(UserStatus::getAccount, account)
                    .eq(UserStatus::getUniqueId, uniqueId)
                    .set(UserStatus::getStatus, status.getValue());
            return userStatusMapper.update(updateWrapper) > 0;
        }
    }

    @Override
    public Map<String, Integer> queryUserStatus(List<String> accountList) {
        log.info("UserRpcServiceImpl::queryUserStatus start......");
        return userStatusMapper.queryStatusByAccountList(accountList);
    }
}
