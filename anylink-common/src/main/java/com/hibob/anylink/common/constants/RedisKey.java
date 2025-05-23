package com.hibob.anylink.common.constants;

public class RedisKey {

    private RedisKey() {

    }

    /**
     * 统一前缀
     */
    public static final String COMMON_PREFIX = "anylink:";

    /**
     * User服务的前缀
     */
    public static final String USER_PREFIX = COMMON_PREFIX + "user:";

    /**
     * Netty服务的前缀
     */
    public static final String NETTY_PREFIX = COMMON_PREFIX + "netty:";

    /**
     * Netty服务的前缀
     */
    public static final String CHAT_PREFIX = COMMON_PREFIX + "chat:";

    /**
     * Netty服务的前缀
     */
    public static final String MTS_PREFIX = COMMON_PREFIX + "mts:";

    /**
     * 存储注销的用户的黑名单
     */
    public static final String USER_DEREGISTER = USER_PREFIX + "deregister:";

    /**
     * 存储正在使用的用户的accessToken
     */
    public static final String USER_ACTIVE_TOKEN = USER_PREFIX + "activeAccessToken:";

    /**
     * 存储用户登录前分配的nonce
     */
    public static final String USER_NONCE =  USER_PREFIX + "nonce:";

    /**
     * 存储用户注册时要用到的验证码
     */
    public static final String USER_CAPTCHA =  USER_PREFIX + "captcha:";

    /**
     * 存储正在使用的用户的refreshToken
     */
    public static final String USER_ACTIVE_TOKEN_REFRESH = USER_PREFIX + "activeRefreshToken:";

    /**
     * 请求记录，后面接用户token+traceId
     */
    public static final String USER_REQ_RECORD = USER_PREFIX + "reqRecord:";

    /**
     * Netty在活全局路由表的Key，后面接uniqueId，value是netty的实例名
     */
    public static final String NETTY_GLOBAL_ROUTE = NETTY_PREFIX + "route:";

    /**
     * Netty在线客户端，后面接账号，value是uniqueId的集合
     */
    public static final String NETTY_ONLINE_CLIENT = NETTY_PREFIX + "online:";

    /**
     * Netty保存的参考MSG ID，后面接账号a@账号b
     */
    public static final String NETTY_REF_MSG_ID = NETTY_PREFIX + "refMsgId:";

    /**
     * 保存的seq对应的msgId, 后面接sessionId + seq，value是msgId
     */
    public static final String NETTY_SEQ_MSG_ID = NETTY_PREFIX + "sessionId:seq:";


    /**
     * 保存的SessionId下都有哪些MSG ID, 后面接sessionId
     */
    public static final String CHAT_SESSION_MSG_ID = CHAT_PREFIX + "sessionId:";

    /**
     * 保存的message消息, 后面接sessionId:msgId
     */
    public static final String CHAT_SESSION_MSG = CHAT_PREFIX + "msg:";


    /**
     * 保存的图片的原图url和缩略图url, 后面接图片的objectId
     */
    public static final String MTS_IMAGE_URL = MTS_PREFIX + "imageUrl:";

    /**
     * 保存的存储对象的url, 后面接objectId
     */
    public static final String MTS_OBJECT_URL = MTS_PREFIX + "objectUrl:";
}
