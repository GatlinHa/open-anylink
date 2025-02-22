package com.hibob.anylink.common.constants;

public class Const {
    private Const() {
    }

    /**
     * 服务编码：公共编码
     */
    public static final int SERVICE_CODE_COMMON = 0;

    /**
     * 服务编码：anylink-awg服务
     */
    public static final int SERVICE_CODE_AGW = 100;

    /**
     * 服务编码：anylink-user服务
     */
    public static final int SERVICE_CODE_USER = 200;

    /**
     * 服务编码：anylink-netty服务
     */
    public static final int SERVICE_CODE_NETTY = 300;

    /**
     * 服务编码：anylink-chat服务
     */
    public static final int SERVICE_CODE_CHAT = 400;

    /**
     * 服务编码：anylink-groupmng服务
     */
    public static final int SERVICE_CODE_GROUP_MNG = 500;

    /**
     * 服务编码：anylink-mts服务
     */
    public static final int SERVICE_CODE_MTS = 600;

    /**
     * 服务编码：default占位
     */
    public static final int SERVICE_CODE_DEFAULT = 5000;


    /**
     * 分隔符：@
     */
    public static final String SPLIT_V = "@";

    /**
     * 分隔符：冒号
     */
    public static final String SPLIT_C = ":";


    /**
     * 请求sign到期时间，单位秒
     */
    public static final int REQUEST_SIGN_EXPIRE = 300;

    /**
     * Msg消息头中的magic固定值
     */
    public static final int MAGIC = 0x8E110B0B;

    /**
     * 全局唯一客户端ID
     */
    public static final String UNIQUE_ID = "uniqueId";

    /**
     * channel的过期时间
     */
    public static final int CHANNEL_EXPIRE = 1800;

    /**
     * msg的seq重复性校验缓存过期时间
     */
    public static final int SEQ_EXPIRE = 1800;

    /**
     * 在线客户端缓存过期时间
     */
    public static final int CACHE_ONLINE_EXPIRE = 3600;

    public static final int MSG_TTL_IN_MONGODB = 365 * 24 * 60 * 60;

    public static final int MSG_CONTENT_LIMIT = 3000;

}
