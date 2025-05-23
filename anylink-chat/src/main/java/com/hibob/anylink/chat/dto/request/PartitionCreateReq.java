package com.hibob.anylink.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("创建分组请求的参数")
public class PartitionCreateReq {

    @NotEmpty(message = "分组名字不能为空")
    @ApiModelProperty(value = "分组名字")
    private String partitionName;

    @NotNull(message = "分组类型不能为空")
    @ApiModelProperty(value = "分组类型")
    private int partitionType;

}
