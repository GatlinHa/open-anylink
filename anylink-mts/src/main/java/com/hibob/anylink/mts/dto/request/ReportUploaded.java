package com.hibob.anylink.mts.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("客户端上报上传结果请求的参数")
public class ReportUploaded {
    @ApiModelProperty(value = "对象id")
    @NotNull
    private Long objectId;
}
