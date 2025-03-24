package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_audio")
public class MtsAudio {
    private static final long serialVersionUID = 1L;

    @TableId(value = "audio_id")
    private String audioId;

    @TableField(value = "audio_type")
    private String audioType;

    @TableField(value = "audio_size")
    private long audioSize;

    @TableField(value = "audio_duration")
    private long audioDuration;

    @TableField(value = "url")
    private String url;

    @TableField(value = "expire")
    private long expire;

    @TableField(value = "created_account")
    private String createdAccount;

    @TableField(value = "created_time")
    private Date createdTime;
}
