package com.hibob.anylink.mts.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("根据objectId查询audio的url请求的参数")
public class AudioReq {

    @ApiModelProperty(value = "待查询的objectId")
    @NotNull
    private String objectId;
}
