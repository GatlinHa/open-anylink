package com.hibob.anylink.mts.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("上传音频后返回的参数")
public class AudioVO {
    @ApiModelProperty(value = "富媒体对象id")
    private String objectId;

    @ApiModelProperty(value = "url")
    private String url;

}
