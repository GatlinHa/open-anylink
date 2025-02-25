package com.hibob.anylink.user.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hibob.anylink.common.constants.RedisKey;
import com.hibob.anylink.common.enums.ConnectStatus;
import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.user.dto.vo.TokensVO;
import com.hibob.anylink.user.dto.vo.UserVO;
import com.hibob.anylink.common.enums.ServiceErrorCode;
import com.hibob.anylink.common.session.ReqSession;
import com.hibob.anylink.common.config.JwtProperties;
import com.hibob.anylink.common.utils.BeanUtil;
import com.hibob.anylink.common.utils.CommonUtil;
import com.hibob.anylink.common.utils.JwtUtil;
import com.hibob.anylink.common.utils.ResultUtil;
import com.hibob.anylink.user.dto.request.*;
import com.hibob.anylink.user.entity.Client;
import com.hibob.anylink.user.entity.Login;
import com.hibob.anylink.user.entity.User;
import com.hibob.anylink.user.entity.UserStatus;
import com.hibob.anylink.user.mapper.ClientMapper;
import com.hibob.anylink.user.mapper.LoginMapper;
import com.hibob.anylink.user.mapper.UserMapper;
import com.hibob.anylink.user.mapper.UserStatusMapper;
import com.hibob.anylink.user.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ClientMapper clientMapper;
    private final LoginMapper loginMapper;
    private final UserStatusMapper userStatusMapper;

    public ResponseEntity<IMHttpResponse> validateAccount(ValidateAccountReq dto) {
        log.info("UserService::validateAccount");
        User user = getOneByAccount(dto.getAccount());
        if (user != null) {
            log.info("account exist");
            return ResultUtil.error(ServiceErrorCode.ERROR_ACCOUNT_EXIST);
        }
        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> register(RegisterReq dto) {
        log.info("UserService::register");
        String account = dto.getAccount();
        String clientId = dto.getClientId();
        String uniqueId = CommonUtil.conUniqueId(account, clientId);

        User user = getOneByAccount(account);
        if (user != null) {
            log.info("account exist");
            return ResultUtil.error(ServiceErrorCode.ERROR_ACCOUNT_EXIST);
        }

        // 把dto转成User对象
        user = BeanUtil.copyProperties(dto, User.class);
        if (user == null) {
            log.error("BeanUtil.copyProperties error");
            return ResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String nonceKey = RedisKey.USER_NONCE + uniqueId;
        Object obj = redisTemplate.opsForValue().get(nonceKey);
        String nonce = "";
        if (obj != null && StringUtils.hasLength((String) obj)) {
            nonce = (String) obj;
            redisTemplate.delete(nonceKey);
        } else {
            log.error("illegal login");
            return ResultUtil.error(ServiceErrorCode.ERROR_ILLEGAL_REQ);
        }

        String password = "";
        try {
            password = securityUtil.decryptPassword(nonce, dto.getIv(), dto.getCiphertext(), dto.getAuthTag());
        } catch (Exception e) {
            log.error("password decrypt Exception: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        user.setPassword(passwordEncoder.encode(password));
        this.save(user);
        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> deregister() {
        log.info("UserService::deregister");
        ReqSession session = ReqSession.getSession();
        String account = session.getAccount();
        String clientId = session.getClientId();
        String uniqueId = CommonUtil.conUniqueId(account, clientId);
        this.remove(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
        deleteClient(account);
        deleteLogin(account);

        redisTemplate.delete(RedisKey.USER_ACTIVE_TOKEN + uniqueId);
        redisTemplate.delete(RedisKey.USER_ACTIVE_TOKEN_REFRESH + uniqueId);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> getCaptcha() {
        log.info("UserService::GetCaptcha");
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 30, 4, 80);
        String uuid = UUID.randomUUID().toString();
        String redisKey = RedisKey.USER_CAPTCHA + uuid;
        redisTemplate.opsForValue().set(redisKey, captcha.getCode(), Duration.ofSeconds(300));
        String base64 = "data:image/png;base64," + captcha.getImageBase64();
        Map<String, String> result = new HashMap<>();
        result.put("base64", base64);
        result.put("id", uuid);
        return ResultUtil.success(result);
    }

    public ResponseEntity<IMHttpResponse> verifyCaptcha(VerifyCaptchaReq dto) {
        log.info("UserService::verifyCaptcha");
        String redisKey = RedisKey.USER_CAPTCHA + dto.getId();
        Object o = redisTemplate.opsForValue().get(redisKey);
        if (o == null || !dto.getCode().equals(o)) {
            log.error("verify captcha error");
            return ResultUtil.error(ServiceErrorCode.ERROR_VERIFY_CAPTCHA);
        } else {
            redisTemplate.delete(redisKey);
            return ResultUtil.success();
        }
    }

    public ResponseEntity<IMHttpResponse> nonce(NonceReq dto) {
        log.info("UserService::nonce");
        String account = dto.getAccount();
        String clientId = dto.getClientId();
        String uniqueId = CommonUtil.conUniqueId(account, clientId);
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String nonceKey = RedisKey.USER_NONCE + uniqueId;
        redisTemplate.opsForValue().set(nonceKey, nonce, Duration.ofSeconds(300));

        Map<String, String> result = new HashMap<>();
        result.put("nonce", nonce);
        return ResultUtil.success(result);
    }

    public ResponseEntity<IMHttpResponse> login(int clientType, String clientName, String clientVersion, LoginReq dto) {
        log.info("UserService::login");
        String account = dto.getAccount();
        String clientId = dto.getClientId();
        String uniqueId = CommonUtil.conUniqueId(account, clientId);
        //支持REST接口重复登录，所以这段代码不启用
//        if (redisTemplate.hasKey(key)) {
//            log.error("Repeated login");
//            return ResultUtil.error(
//                    HttpStatus.FORBIDDEN,
//                    ServiceErrorCode.ERROR_MULTI_LOGIN.code(),
//                    ServiceErrorCode.ERROR_MULTI_LOGIN.desc());
//        }

        User user = getOneByAccount(account);
        if (user == null) {
            log.error("no register");
            return ResultUtil.error(ServiceErrorCode.ERROR_LOGIN);
        }

        String nonceKey = RedisKey.USER_NONCE + uniqueId;
        Object obj = redisTemplate.opsForValue().get(nonceKey);
        String nonce = "";
        if (obj != null && StringUtils.hasLength((String) obj)) {
            nonce = (String) obj;
            redisTemplate.delete(nonceKey);
        } else {
            log.error("illegal login");
            return ResultUtil.error(ServiceErrorCode.ERROR_ILLEGAL_REQ);
        }

        String password = "";
        try {
            password = securityUtil.decryptPassword(nonce, dto.getIv(), dto.getCiphertext(), dto.getAuthTag());
        } catch (Exception e) {
            log.error("password decrypt Exception: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("password error");
            return ResultUtil.error(ServiceErrorCode.ERROR_LOGIN);
        }

        Client client = getOneByUniqueId(uniqueId);
        if (client == null) {
            log.info("client not found");
            insertClient(dto, uniqueId);
        }
        else {
            updateClient(dto, uniqueId, clientType, clientName, clientVersion);
        }
        insertLogin(account, uniqueId);

        String accessToken = JwtUtil.generateToken(
                user.getAccount(),
                clientId,
                jwtProperties.getAccessTokenExpire(),
                jwtProperties.getAccessTokenSecret());
        String refreshToken = JwtUtil.generateToken(
                user.getAccount(),
                clientId,
                jwtProperties.getRefreshTokenExpire(),
                jwtProperties.getRefreshTokenSecret());

        TokensVO accessTokenVO = new TokensVO();
        TokensVO refreshTokenVO = new TokensVO();
        accessTokenVO.setToken(accessToken);
        accessTokenVO.setSecret(JwtUtil.generateSecretKey());
        accessTokenVO.setExpire(jwtProperties.getAccessTokenExpire());
        refreshTokenVO.setToken(refreshToken);
        refreshTokenVO.setSecret(JwtUtil.generateSecretKey());
        refreshTokenVO.setExpire(jwtProperties.getRefreshTokenExpire());

        redisTemplate.opsForValue().set(
                RedisKey.USER_ACTIVE_TOKEN + uniqueId,
                JSON.toJSONString(accessTokenVO),
                Duration.ofSeconds(jwtProperties.getAccessTokenExpire()));
        redisTemplate.opsForValue().set(
                RedisKey.USER_ACTIVE_TOKEN_REFRESH + uniqueId,
                JSON.toJSONString(refreshTokenVO),
                Duration.ofSeconds(jwtProperties.getRefreshTokenExpire()));

        HashMap<String, TokensVO> map = new HashMap<>();
        map.put("accessToken", accessTokenVO);
        map.put("refreshToken", refreshTokenVO);
        return ResultUtil.success(map);
    }

    public ResponseEntity<IMHttpResponse> logout() {
        log.info("UserService::logout");
        ReqSession session = ReqSession.getSession();
        String uniqueId = CommonUtil.conUniqueId(session.getAccount(), session.getClientId());
        redisTemplate.delete(RedisKey.USER_ACTIVE_TOKEN + uniqueId);
        redisTemplate.delete(RedisKey.USER_ACTIVE_TOKEN_REFRESH + uniqueId);
        updateLogin(uniqueId, 0);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> modifyPwd(ModifyPwdReq dto) {
        log.info("UserService::modifyPwd");
        ReqSession session = ReqSession.getSession();
        String account = session.getAccount();
        String clientId = session.getClientId();
        String uniqueId = CommonUtil.conUniqueId(account, clientId);

        String nonceKey = RedisKey.USER_NONCE + uniqueId;
        Object obj = redisTemplate.opsForValue().get(nonceKey);
        String nonce = "";
        if (obj != null && StringUtils.hasLength((String) obj)) {
            nonce = (String) obj;
            redisTemplate.delete(nonceKey);
        } else {
            log.error("illegal login");
            return ResultUtil.error(ServiceErrorCode.ERROR_ILLEGAL_REQ);
        }

        String oldPasswordStr = "";
        String newPasswordStr = "";
        try {
            oldPasswordStr = securityUtil.decryptPassword(nonce,
                    dto.getOldPassword().get("iv"),
                    dto.getOldPassword().get("ciphertext"),
                    dto.getOldPassword().get("authTag"));
            newPasswordStr = securityUtil.decryptPassword(nonce,
                    dto.getNewPassword().get("iv"),
                    dto.getNewPassword().get("ciphertext"),
                    dto.getNewPassword().get("authTag"));
        } catch (Exception e) {
            log.error("password decrypt Exception: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        if (oldPasswordStr.equals(newPasswordStr)) {
            log.error("new password equals the old password");
            return ResultUtil.error(ServiceErrorCode.ERROR_NEW_PASSWORD_EQUAL_OLD);
        }

        User user = getOneByAccount(account);
        if (!passwordEncoder.matches(oldPasswordStr, user.getPassword())) {
            log.error("password error");
            return ResultUtil.error(ServiceErrorCode.ERROR_OLD_PASSWORD_ERROR);
        }
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getAccount, account)
                .set(User::getPassword, passwordEncoder.encode(newPasswordStr))
                .set(User::getUpdateTime, new Date(System.currentTimeMillis())));

        // 修改密码之后可以继续保持登录状态，理由如下
        // 1. 为了保持用户体验，减少不必要的用户操作
        // 2. 后端校验机制为token，token不需要随密码变更而刷新
//        redisTemplate.delete(RedisKey.USER_ACTIVE_TOKEN + uniqueId);
//        redisTemplate.delete(RedisKey.USER_ACTIVE_TOKEN_REFRESH + uniqueId);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> refreshToken(String refreshToken) {
        log.info("UserService::refreshToken");
        String account = JwtUtil.getAccount(refreshToken);
        String client = JwtUtil.getInfo(refreshToken);
        String accessToken = JwtUtil.generateToken(
                account,
                client,
                jwtProperties.getAccessTokenExpire(),
                jwtProperties.getAccessTokenSecret());

        TokensVO vo = new TokensVO();
        vo.setToken(accessToken);
        vo.setSecret(JwtUtil.generateSecretKey());
        vo.setExpire(jwtProperties.getAccessTokenExpire());

        String uniqueId = CommonUtil.conUniqueId(account, client);
        String key = RedisKey.USER_ACTIVE_TOKEN + uniqueId;
        redisTemplate.opsForValue().set(key, JSON.toJSONString(vo), Duration.ofSeconds(jwtProperties.getAccessTokenExpire()));
        updateLogin(uniqueId, 1);

        HashMap<String, TokensVO> map = new HashMap<>();
        map.put("accessToken", vo);
        return ResultUtil.success(map);
    }

    public ResponseEntity<IMHttpResponse> querySelf() {
        log.info("UserService::querySelf");
        ReqSession session = ReqSession.getSession();
        String account = session.getAccount();
        User user = getOneByAccount(account);
        if (user == null) {
            log.error("user not found");
            return ResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 查询用户连接状态，既然触发了这个查询，说明用户至少是在线（不可能是离线或离开）
        UserStatus userStatus = userStatusMapper.queryStatus(account);
        int status = userStatus == null || userStatus.getStatus() < ConnectStatus.ONLINE.getValue()
                ? ConnectStatus.ONLINE.getValue() : userStatus.getStatus();
        user.setStatus(status);
        // 把User对象转成返回对象
        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
        if (vo == null) {
            log.error("BeanUtil.copyProperties error");
            return ResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResultUtil.success(vo);
    }

    public ResponseEntity<IMHttpResponse> modifySelf(ModifySelfReq dto) {
        log.info("UserService::modifySelf");
        ReqSession session = ReqSession.getSession();
        String account = session.getAccount();
        User user = getOneByAccount(account);
        if (user == null) {
            log.error("user not found");
            return ResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 按需更新，dto传了参数才更新，不传不更新
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.<User>lambdaUpdate().eq(User::getAccount, account);
        if (StringUtils.hasLength(dto.getNickName())) updateWrapper.set(User::getNickName, dto.getNickName());
        if (StringUtils.hasLength(dto.getAvatar())) updateWrapper.set(User::getAvatar, dto.getAvatar());
        if (StringUtils.hasLength(dto.getAvatarThumb())) updateWrapper.set(User::getAvatarThumb, dto.getAvatarThumb());
        if (dto.getGender() != 0) updateWrapper.set(User::getGender, dto.getGender());
        if (dto.getLevel() != 0) updateWrapper.set(User::getLevel, dto.getLevel());
        if (StringUtils.hasLength(dto.getSignature())) updateWrapper.set(User::getSignature, dto.getSignature());
        if (StringUtils.hasLength(dto.getPhoneNum())) updateWrapper.set(User::getPhoneNum, dto.getPhoneNum());
        if (StringUtils.hasLength(dto.getEmail())) updateWrapper.set(User::getEmail, dto.getEmail());
        if (StringUtils.hasLength(dto.getBirthday())) updateWrapper.set(User::getBirthday, dto.getBirthday());
        updateWrapper.set(User::getUpdateTime, new Date(System.currentTimeMillis()));
        this.update(updateWrapper);

        return ResultUtil.success();
    }

    public ResponseEntity<IMHttpResponse> query(QueryReq dto) {
        log.info("UserService::query");
        User user = getOneByAccount(dto.getAccount());
        if (user == null) {
            log.info("user not found");
            return ResultUtil.success();
        }

        // 查询用户连接状态，如果查不到，这里默认为offline
        UserStatus userStatus = userStatusMapper.queryStatus(dto.getAccount());
        user.setStatus(userStatus == null ? ConnectStatus.OFFLINE.getValue() : userStatus.getStatus());

        // 把User对象转成返回对象
        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
        if (vo == null) {
            log.error("BeanUtil.copyProperties error");
            return ResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        vo.setBirthday(null); //查询别人信息不返回生日信息

        return ResultUtil.success(vo);
    }

    public ResponseEntity<IMHttpResponse> findByNick(FindByNickReq dto) {
        // TODO 这里要分页查询
        log.info("UserService::findByNick");
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(User::getNickName, dto.getKeyWords());
        List<User> lists = this.list(queryWrapper);
        List<String> accountList = lists.stream().map(User::getAccount).collect(Collectors.toList());
        Map<String, Integer> statusMap;
        if (accountList.size() > 0) {
            statusMap = userStatusMapper.queryStatusByAccountList(accountList);
        } else {
            statusMap = null;
        }

        List<UserVO> voList = new ArrayList<>();
        lists.forEach(user -> {
            if (statusMap == null || statusMap.get(user.getAccount()) == null) {
                // 如果用户连接状态查不到，这里默认为offline
                user.setStatus(ConnectStatus.OFFLINE.getValue());
            }
            else {
                user.setStatus(statusMap.get(user.getAccount()));
            }
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            vo.setBirthday(null); //查询别人信息不返回生日信息
            voList.add(vo);
        });

        return ResultUtil.success(voList);
    }


    private User getOneByAccount(String account) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getAccount, account);
        return this.getOne(queryWrapper);
    }

    private Client getOneByUniqueId(String uniqueId) {
        LambdaQueryWrapper<Client> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Client::getUniqueId, uniqueId);
        List<Client> clients = clientMapper.selectList(queryWrapper);
        if (clients.size() > 0) {
            return clients.get(0);
        }
        else {
            return null;
        }
    }

    private int insertClient(LoginReq dto, String uniqueId) {
        Client client = BeanUtil.copyProperties(dto, Client.class);
        client.setUniqueId(uniqueId);
        Date now = new Date();
        client.setCreatedTime(now);
        client.setLastLoginTime(now);
        return clientMapper.insert(client);
    }

    private int updateClient(LoginReq dto, String uniqueId, int clientType, String clientName, String clientVersion) {
        LambdaUpdateWrapper<Client> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Client::getClientType, clientType);
        updateWrapper.set(Client::getClientName, clientName);
        updateWrapper.set(Client::getClientVersion, clientVersion);
        updateWrapper.set(Client::getLastLoginTime, new Date(System.currentTimeMillis()));
        updateWrapper.eq(Client::getUniqueId, uniqueId);
        return clientMapper.update(updateWrapper);
    }

    private int deleteClient(String account) {
        LambdaUpdateWrapper<Client> deleteWrapper = Wrappers.lambdaUpdate();
        deleteWrapper.eq(Client::getAccount, account);
        return clientMapper.delete(deleteWrapper);
    }

    private int insertLogin(String account, String uniqueId) {
        Login login = new Login();
        login.setAccount(account);
        login.setUniqueId(uniqueId);
        login.setLoginTime(new Date(System.currentTimeMillis()));
        return loginMapper.insert(login);
    }

    private int updateLogin(String uniqueId, int op) {
        LambdaUpdateWrapper<Login> updateWrapper = Wrappers.lambdaUpdate();
        if (op == 0) {
            updateWrapper.set(Login::getLogoutTime, new Date(System.currentTimeMillis()));
        }
        else if (op == 1) {
            updateWrapper.set(Login::getRefreshTime, new Date(System.currentTimeMillis()));
        }
        else {
            return 0;
        }
        updateWrapper.eq(Login::getUniqueId, uniqueId);
        return loginMapper.update(updateWrapper);
    }

    private int deleteLogin(String account) {
        LambdaUpdateWrapper<Login> deleteWrapper = Wrappers.lambdaUpdate();
        deleteWrapper.eq(Login::getAccount, account);
        return loginMapper.delete(deleteWrapper);
    }
    
}
