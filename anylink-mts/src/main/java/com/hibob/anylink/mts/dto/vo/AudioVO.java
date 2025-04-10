package com.hibob.anylink.mts.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("上传音频后返回的参数")
public class AudioVO {
    @ApiModelProperty(value = "富媒体对象id")
    private String objectId;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "时长")
    private int duration;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private long size;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}
