package com.hibob.anylink.mts.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("根据objectId查询video的url请求的参数")
public class VideoReq {

    @ApiModelProperty(value = "待查询的objectId列表")
    @NotNull
    private List<String> objectIds;
}
