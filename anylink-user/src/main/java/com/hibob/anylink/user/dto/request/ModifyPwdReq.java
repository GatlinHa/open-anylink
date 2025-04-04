package com.hibob.anylink.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@ApiModel("修改密码请求的参数")
public class ModifyPwdReq {

    @Size(max = 128, message = "客户端ID长度不能大于128字符")
    @NotEmpty(message = "客户端ID不可为空")
    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @NotNull(message = "旧密码对象")
    @ApiModelProperty(value = "旧密码对象")
    private Map<String, String> oldPassword;

    @NotNull(message = "新密码对象")
    @ApiModelProperty(value = "新密码对象")
    private Map<String, String> newPassword;

}
