package com.hibob.anylink.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel("验证图形验证码请求的参数")
public class VerifyCaptchaReq {

    @Size(max = 64, message = "验证码ID长度不能大于64字符")
    @NotEmpty(message = "验证码ID不可为空")
    @ApiModelProperty(value = "验证码ID")
    private String id;

    @Size(min = 4, max = 4, message = "验证码长度必须为4字符")
    @NotEmpty(message = "验证码不可为空")
    @ApiModelProperty(value = "验证码")
    private String code;
}
