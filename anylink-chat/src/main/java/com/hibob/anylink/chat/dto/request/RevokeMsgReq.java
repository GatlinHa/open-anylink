package com.hibob.anylink.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("撤销某条消息请求的参数")
public class RevokeMsgReq {

    @NotEmpty
    @ApiModelProperty(value = "会话id")
    private String sessionId;

    @NotNull
    @ApiModelProperty(value = "撤销的msgId")
    private Long revokeMsgId;

    @NotNull
    @ApiModelProperty(value = "是否是群聊")
    private Boolean isGroupChat;

    @NotEmpty
    @ApiModelProperty(value = "对方的ID，如果是群聊就是groupId")
    private String remoteId;
}
