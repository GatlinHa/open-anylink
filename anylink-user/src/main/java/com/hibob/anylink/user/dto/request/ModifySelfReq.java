package com.hibob.anylink.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("修改自己信息请求的参数")
public class ModifySelfReq {

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像Id")
    private Long avatarId;

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
    private String birthday;

    @ApiModelProperty(value = "新消息提醒")
    private Boolean newMsgTips;

    @ApiModelProperty(value = "发送消息提醒")
    private Boolean sendMsgTips;
}
