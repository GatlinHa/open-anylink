package com.hibob.anylink.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
@ApiModel("单聊历史消息查询接口的请求体参数")
public class ChatHistoryReq {

    @NotEmpty
    @ApiModelProperty(value = "会话Id")
    private String sessionId;

    @NotNull
    @Max(value = 100, message = "页大小不能大于100")
    @Min(value = 10, message = "页大小不能小于10")
    @ApiModelProperty(value = "每次拉取的消息数量")
    private int pageSize;

    @ApiModelProperty(value = "内容类型，0表示查全部")
    private List<Integer> contentTypes;

    @ApiModelProperty(value = "查询开始时间，UTC毫秒")
    private Long startTime;

    @ApiModelProperty(value = "查询结束时间，UTC毫秒")
    private Long endTime;

    @ApiModelProperty(value = "查询的结束msgId(不含)")
    private Long endMsgId;




}
