package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_audio")
public class MtsAudio {
    private static final long serialVersionUID = 1L;

    @TableField(value = "audio_id")
    private String audioId;

    @TableField(value = "store_type")
    private int storeType;

    @TableField(value = "audio_type")
    private String audioType;

    @TableField(value = "audio_size")
    private long audioSize;

    @TableField(value = "audio_duration")
    private int audioDuration;

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
