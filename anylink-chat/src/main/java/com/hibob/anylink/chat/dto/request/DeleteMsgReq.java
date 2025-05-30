package com.hibob.anylink.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("删除消息请求的参数")
public class DeleteMsgReq {

    @NotEmpty
    @ApiModelProperty(value = "会话id")
    private String sessionId;

    @NotNull
    @ApiModelProperty(value = "删除的msgId列表")
    private List<Long> deleteMsgIds;
}
