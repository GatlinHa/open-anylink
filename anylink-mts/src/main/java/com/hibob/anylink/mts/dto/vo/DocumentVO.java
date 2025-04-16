package com.hibob.anylink.mts.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("上传视频后返回的参数")
public class DocumentVO {
    @ApiModelProperty(value = "返回值作用：上传0，下载1")
    private int scope;

    @ApiModelProperty(value = "Document对象id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    @ApiModelProperty(value = "Document类型")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String documentType;

    @ApiModelProperty(value = "下载用的url")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String downloadUrl;

    @ApiModelProperty(value = "上传用的url")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String uploadUrl;

    @ApiModelProperty(value = "文件名")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long size;

    @ApiModelProperty(value = "创建时间")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private LocalDateTime createdTime;
}
