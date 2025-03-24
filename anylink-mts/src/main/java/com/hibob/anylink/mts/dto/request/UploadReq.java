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
    @Max(value = 1, message = "临时存储")
    @Min(value = 0, message = "永久存储")
    private int storeType;

    @ApiModelProperty(value = "文件")
    private MultipartFile file;
}
