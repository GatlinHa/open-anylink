package com.hibob.anylink.mts.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("上传文件后返回的参数")
public class ImageVO {
    @ApiModelProperty(value = "富媒体对象id")
    private String objectId;

    @ApiModelProperty(value = "原图url")
    private String originUrl;

    @ApiModelProperty(value = "缩略图url")
    private String thumbUrl;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private long size;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}
