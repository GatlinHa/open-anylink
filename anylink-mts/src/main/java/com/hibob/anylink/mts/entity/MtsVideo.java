package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_video")
public class MtsVideo {
    private static final long serialVersionUID = 1L;

    @TableField(value = "video_id")
    private String videoId;

    @TableField(value = "store_type")
    private int storeType;

    @TableField(value = "video_type")
    private String videoType;

    @TableField(value = "video_size")
    private long videoSize;

    @TableField(value = "video_width")
    private int videoWidth;

    @TableField(value = "video_height")
    private int videoHeight;

    @TableField(value = "file_name")
    private String fileName;

    @TableField(value = "store_source")
    private String storeSource;

    @TableField(value = "bucket_name")
    private String bucketName;

    @TableField(value = "full_path")
    private String fullPath;

    @TableField(value = "expire")
    private long expire;

    @TableField(value = "uploaded")
    private boolean uploaded;

    @TableField(value = "created_account")
    private String createdAccount;

    @TableField(value = "created_time")
    private Date createdTime;
}
