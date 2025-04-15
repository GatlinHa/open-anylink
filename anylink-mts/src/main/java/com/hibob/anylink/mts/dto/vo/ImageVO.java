package com.hibob.anylink.mts.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("上传文件后返回的参数")
public class ImageVO {
    @ApiModelProperty(value = "返回值作用：上传0，下载1")
    private int scope;

    @ApiModelProperty(value = "富媒体对象id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    @ApiModelProperty(value = "下载用的原图url")
    private String originUrl;

    @ApiModelProperty(value = "下载用的缩略图url")
    private String thumbUrl;

    @ApiModelProperty(value = "上传原图用的有预签名url")
    private String uploadOriginUrl;

    @ApiModelProperty(value = "上传缩略图用的有预签名url")
    private String uploadThumbUrl;

    @ApiModelProperty(value = "图片类型")
    private String imageType;

    @ApiModelProperty(value = "文件大小")
    private long size;

    @ApiModelProperty(value = "原图的宽度")
    private int originWidth;

    @ApiModelProperty(value = "原图的高度")
    private int originHeight;

    @ApiModelProperty(value = "缩略图的宽度")
    private int thumbWidth;

    @ApiModelProperty(value = "缩略图的高度")
    private int thumbHeight;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}
