package com.hibob.anylink.user.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("查询别人信息返回的参数")
public class UserVO {

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "头像缩略图")
    private String avatarThumb;

    @ApiModelProperty(value = "性别")
    private int gender;

    @ApiModelProperty(value = "级别")
    private int level;

    @ApiModelProperty(value = "个性签名")
    private String signature;

    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "状态")
    private int status;

    @ApiModelProperty(value = "新消息提醒")
    private Boolean newMsgTips;

    @ApiModelProperty(value = "发送消息提醒")
    private Boolean sendMsgTips;
}
