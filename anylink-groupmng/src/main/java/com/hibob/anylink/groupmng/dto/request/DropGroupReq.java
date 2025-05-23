package com.hibob.anylink.groupmng.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("解散群组请求的参数")
public class DropGroupReq {

    @ApiModelProperty(value = "群组id")
    @NotNull
    private String groupId;

}
