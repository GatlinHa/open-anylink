package com.hibob.anylink.mts.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("文件上传请求")
public class UploadReq {

    @ApiModelProperty(value = "存储时长类型")
    @NotNull
    @Max(value = 1, message = "取值范围非法")
    @Min(value = 0, message = "取值范围非法")
    private int storeType;

    @ApiModelProperty(value = "录音的时长")
    private int duration;

    @ApiModelProperty(value = "文件")
    private MultipartFile file;
}
