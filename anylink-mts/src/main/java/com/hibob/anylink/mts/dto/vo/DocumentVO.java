package com.hibob.anylink.mts.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("上传视频后返回的参数")
public class DocumentVO {
    @ApiModelProperty(value = "Document对象id")
    private String objectId;

    @ApiModelProperty(value = "Document类型")
    private String documentType;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private long size;

}
