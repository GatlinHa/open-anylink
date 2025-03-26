package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_video")
public class MtsVideo {
    private static final long serialVersionUID = 1L;

    @TableId(value = "video_id")
    private String videoId;

    @TableField(value = "video_type")
    private String videoType;

    @TableField(value = "video_size")
    private long videoSize;

    @TableField(value = "video_duration")
    private int videoDuration;

    @TableField(value = "file_name")
    private String fileName;

    @TableField(value = "url")
    private String url;

    @TableField(value = "expire")
    private long expire;

    @TableField(value = "created_account")
    private String createdAccount;

    @TableField(value = "created_time")
    private Date createdTime;
}
