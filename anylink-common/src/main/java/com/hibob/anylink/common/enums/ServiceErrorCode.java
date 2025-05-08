package com.hibob.anylink.common.enums;

import com.hibob.anylink.common.constants.Const;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ServiceErrorCode {
    ERROR_ILLEGAL_REQ(Const.SERVICE_CODE_COMMON + 1, "非法请求"),

    ERROR_ACCOUNT_EXIST(Const.SERVICE_CODE_USER + 1, "账号已存在"), //TODO 这些都没有利用起来，后面整改下

    ERROR_XSS(Const.SERVICE_CODE_USER + 2, "输入内容含有非法字符"),

    ERROR_NO_LOGIN(Const.SERVICE_CODE_USER + 3, "未登录"),

    ERROR_LOGIN(Const.SERVICE_CODE_USER + 4, "账号或密码错误"),

    ERROR_OLD_PASSWORD_ERROR(Const.SERVICE_CODE_USER + 5, "旧密码错误"),

    ERROR_NEW_PASSWORD_EQUAL_OLD(Const.SERVICE_CODE_USER + 6, "新旧密码相等"),

    ERROR_ACCESS_TOKEN_EXPIRED(Const.SERVICE_CODE_USER + 7, "AccessToken已过期"),

    ERROR_ACCESS_TOKEN_DELETED(Const.SERVICE_CODE_USER + 8, "AccessToken以删除"),

    ERROR_REFRESH_TOKEN(Const.SERVICE_CODE_USER + 9, "RefreshToken错误"),

    ERROR_IS_DEREGISTER(Const.SERVICE_CODE_USER + 10, "账号已注销"),

    ERROR_CREATE_PARTITION(Const.SERVICE_CODE_USER + 11, "创建分组失败"),

    ERROR_PARTITION_NO_EXIST(Const.SERVICE_CODE_USER + 12, "没有该分组"),

    ERROR_VERIFY_CAPTCHA(Const.SERVICE_CODE_USER + 13, "验证码校验错误"),

    ERROR_DECRYPT_PASSWORD(Const.SERVICE_CODE_USER + 14, "密码解密错误"),

    ERROR_FORGET_TYPE(Const.SERVICE_CODE_USER + 15, "忘记密码类型错误"),

    ERROR_FORGET_NO_RECORD(Const.SERVICE_CODE_USER + 16, "没有找到记录"),

    ERROR_SERVICE_EXCEPTION(Const.SERVICE_CODE_USER + 50, "服务器内部异常"),

    ERROR_CHAT_REFMSGID_EXCEPTION(Const.SERVICE_CODE_CHAT + 1, "ref msgId异常"),

    ERROR_CHAT_CREATE_SESSION(Const.SERVICE_CODE_CHAT + 2, "创建session异常"),

    ERROR_CHAT_UPDATE_SESSION(Const.SERVICE_CODE_CHAT + 3, "更新session异常"),

    ERROR_CHAT_PULL_MSG(Const.SERVICE_CODE_CHAT + 4, "拉取消息异常"),

    ERROR_CHAT_DELETE_SESSION(Const.SERVICE_CODE_CHAT + 5, "删除session异常"),

    ERROR_CHAT_SESSION_NOT_EXIST(Const.SERVICE_CODE_CHAT + 6, "会话不存在"),

    ERROR_GROUP_MNG_NOT_ENOUGH_MEMBER(Const.SERVICE_CODE_GROUP_MNG + 1, "成员数不够"),

    ERROR_GROUP_MNG_PERMISSION_DENIED(Const.SERVICE_CODE_GROUP_MNG + 2, "没有权限操作此群"),

    ERROR_GROUP_MNG_EMPTY_PARAM(Const.SERVICE_CODE_GROUP_MNG + 3, "参数为空"),

    ERROR_GROUP_MNG_HAS_NO_THIS_MEMBER(Const.SERVICE_CODE_GROUP_MNG + 4, "没有找到目标成员"),

    ERROR_GROUP_MNG_NOT_OWNER(Const.SERVICE_CODE_GROUP_MNG + 5, "非群主不能操作"),

    ERROR_GROUP_MNG_OWNER_TRANSFER(Const.SERVICE_CODE_GROUP_MNG + 6, "群主转让失败"),

    ERROR_GROUP_MNG_NOT_IN_GROUP(Const.SERVICE_CODE_GROUP_MNG + 7, "已离开该群或群已被解散"),

    ERROR_MTS_IMAGE_FORMAT_ERROR(Const.SERVICE_CODE_MTS + 1, "图片不在支持格式范围内：jpg, jpeg, png, gif, bmp, webp"),

    ERROR_MTS_AUDIO_FORMAT_ERROR(Const.SERVICE_CODE_MTS + 2, "音频不在支持的格式范围内：mp3, wav, aac, flac, ogg, webm"),

    ERROR_MTS_VIDEO_FORMAT_ERROR(Const.SERVICE_CODE_MTS + 3, "视频不在支持格式范围内：mp4, avi, mkv, mov, flv, webm, wmv"),

    ERROR_MTS_IMAGE_TOO_BIG(Const.SERVICE_CODE_MTS + 4, "图片文件过大"),

    ERROR_MTS_AUDIO_TOO_BIG(Const.SERVICE_CODE_MTS + 5, "音频文件过大"),

    ERROR_MTS_VIDEO_TOO_BIG(Const.SERVICE_CODE_MTS + 6, "视频文件过大"),

    ERROR_MTS_FILE_TOO_BIG(Const.SERVICE_CODE_MTS + 7, "文件过大"),

    ERROR_MTS_FILE_UPLOAD_ERROR(Const.SERVICE_CODE_MTS + 8, "文件上传失败"),

    ERROR_MTS_FILE_NOT_SUPPORT(Const.SERVICE_CODE_MTS + 9, "不支持该文件格式"),

    ERROR_MTS_FILE_DOWNLOAD_PRESIGN_URL(Const.SERVICE_CODE_MTS + 10, "获取下载预签名URL失败"),

    ERROR_MTS_FILE_UPLOAD_PRESIGN_URL(Const.SERVICE_CODE_MTS + 11, "获取上传预签名URL失败"),

    ERROR_DEFAULT(Const.SERVICE_CODE_DEFAULT, "未知错误");


    private int code;
    private String desc;

    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }
}
