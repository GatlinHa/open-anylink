package com.hibob.anylink.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("创建会话记录接口的请求体参数")
public class CreateSessionReq {

    @NotEmpty
    @ApiModelProperty(value = "会话Id")
    private String sessionId;

    @NotEmpty
    @ApiModelProperty(value = "对方ID")
    private String remoteId;

    @NotNull
    @ApiModelProperty(value = "会话类型")
    private int sessionType;
}
