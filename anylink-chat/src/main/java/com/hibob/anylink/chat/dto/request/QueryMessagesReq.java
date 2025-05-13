package com.hibob.anylink.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("查询某个会话下指定msgId的消息接口的请求体参数")
public class QueryMessagesReq {

    @NotEmpty
    @ApiModelProperty(value = "会话Id")
    private String sessionId;

    @NotNull
    @ApiModelProperty(value = "指定msgId列表")
    private List<Long> msgIds;
}
