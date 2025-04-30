package com.hibob.anylink.mts.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("文件上传请求")
public class GetUploadUrlReq {

    @ApiModelProperty(value = "存储时长类型：0永久存储，1临时存储")
    @NotNull
    @Max(value = 1, message = "取值范围非法")
    @Min(value = 0, message = "取值范围非法")
    private int storeType;

    @ApiModelProperty(value = "文件md5")
    @NotEmpty
    private String md5;

    @ApiModelProperty(value = "文件名")
    @NotEmpty
    private String fileName;

    @ApiModelProperty(value = "内容类型：image/png, audio/wav...，允许为空")
    private String fileRawType;

    @ApiModelProperty(value = "文件大小")
    @NotNull
    private long size;

    @ApiModelProperty(value = "音频（语音消息）的时长")
    private int audioDuration;

    @ApiModelProperty(value = "原图的宽度")
    private int originWidth;

    @ApiModelProperty(value = "原图的高度")
    private  int originHeight;

    @ApiModelProperty(value = "缩略图的宽度")
    private int thumbWidth;

    @ApiModelProperty(value = "缩略图的高度")
    private  int thumbHeight;

    @ApiModelProperty(value = "视频的宽度")
    private int videoWidth;

    @ApiModelProperty(value = "视频的高度")
    private int videoHeight;
}
