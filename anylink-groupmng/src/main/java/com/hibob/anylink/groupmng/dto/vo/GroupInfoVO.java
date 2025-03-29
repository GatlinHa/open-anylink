package com.hibob.anylink.groupmng.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupInfoVO {
    @ApiModelProperty(value = "群组id")
    private String groupId;

    @ApiModelProperty(value = "群组类型")
    private Integer groupType;

    @ApiModelProperty(value = "群组名称")
    private String groupName;

    @ApiModelProperty(value = "群公告")
    private String announcement;

    @ApiModelProperty(value = "群头像Id")
    private Long avatarId;

    @ApiModelProperty(value = "群头像")
    private String avatar;

    @ApiModelProperty(value = "群头像缩略图")
    private String avatarThumb;

    @ApiModelProperty(value = "新加入成员是否允许浏览历史")
    private boolean historyBrowse;

    @ApiModelProperty(value = "是否全员静言")
    private boolean allMuted;

    @ApiModelProperty(value = "是否开启入群验证")
    private boolean joinGroupApproval;

    @ApiModelProperty(value = "我在群里的权限")
    private int myRole;
}
