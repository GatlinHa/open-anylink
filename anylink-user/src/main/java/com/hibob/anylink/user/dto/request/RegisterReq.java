package com.hibob.anylink.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@ApiModel("注册请求的参数")
public class RegisterReq {

    @NotEmpty(message = "账号不可为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,32}$", message = "账号必须是6-32位的字母、数字或下划线")
    @ApiModelProperty(value = "账号")
    private String account;

    @Size(max = 128, message = "客户端ID长度不能大于128字符")
    @NotEmpty(message = "客户端ID不可为空")
    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

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
