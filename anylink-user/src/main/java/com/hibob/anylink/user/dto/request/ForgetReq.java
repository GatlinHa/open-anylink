package com.hibob.anylink.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel("忘记密码请求的参数")
public class ForgetReq {

    @NotEmpty(message = "账号不可为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,32}$", message = "账号必须是6-32位的字母、数字或下划线")
    @ApiModelProperty(value = "账号")
    private String account;

    @Size(max = 128, message = "客户端ID长度不能大于128字符")
    @NotEmpty(message = "客户端ID不可为空")
    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @Size(max = 16, message = "重置类型长度不能大于16字符")
    @NotEmpty(message = "重置类型不可为空")
    @ApiModelProperty(value = "重置类型：用手机号码还是邮箱重置")
    private String forgetType;

    @Size(max = 64, message = "重置Key长度不能大于64字符")
    @NotEmpty(message = "重置Key不可为空")
    @ApiModelProperty(value = "重置Key：手机号码或邮箱")
    private String forgetKey;

    @Size(min = 4, max = 4, message = "验证码长度必须为4字符")
    @NotEmpty(message = "验证码不可为空")
    @ApiModelProperty(value = "验证码")
    private String forgetCode;

    @NotEmpty(message = "iv不可为空")
    @ApiModelProperty(value = "iv")
    private String iv;

    @NotEmpty(message = "ciphertext不可为空")
    @ApiModelProperty(value = "ciphertext")
    private String ciphertext;

    @NotEmpty(message = "authTag不可为空")
    @ApiModelProperty(value = "authTag")
    private String authTag;

}
