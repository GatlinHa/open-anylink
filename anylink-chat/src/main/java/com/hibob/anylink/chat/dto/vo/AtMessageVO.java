package com.hibob.anylink.chat.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("返回前端@消息的元素")
public class AtMessageVO {

    @ApiModelProperty(value = "@消息的msgId")
    private long msgId;

    @ApiModelProperty(value = "@消息所在消息的msgId")
    private long referMsgId;

    @ApiModelProperty(value = "会话Id")
    private String sessionId;

    @ApiModelProperty(value = "群组Id")
    private String groupId;

    @ApiModelProperty(value = "@发起人")
    private String fromId;

    @ApiModelProperty(value = "消息时间")
    private Date msgTime;
}
