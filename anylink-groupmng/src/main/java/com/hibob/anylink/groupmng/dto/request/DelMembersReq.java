package com.hibob.anylink.groupmng.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("群组减人请求的参数")
public class DelMembersReq {

    @ApiModelProperty(value = "操作者id")
    @NotEmpty
    private String operatorId;

    @ApiModelProperty(value = "操作者昵称")
    @NotEmpty
    private String operatorNickName;

    @ApiModelProperty(value = "群组id")
    @NotEmpty
    private String groupId;

    @ApiModelProperty(value = "移除的群组成员：account, nickName")
    @NotNull
    private List<Map<String, Object>> members;
}
