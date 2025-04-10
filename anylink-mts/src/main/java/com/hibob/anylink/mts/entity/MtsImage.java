package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_image")
public class MtsImage {
    private static final long serialVersionUID = 1L;

    @TableId(value = "image_id")
    private String imageId;

    @TableField(value = "image_type")
    private String imageType;

    @TableField(value = "image_size")
    private long imageSize;

    @TableField(value = "file_name")
    private String fileName;

    @TableField(value = "store_source")
    private String storeSource;

    @TableField(value = "bucket_name")
    private String bucketName;

    @TableField(value = "origin_path")
    private String originPath;

    @TableField(value = "thumb_path")
    private String thumbPath;

    @TableField(value = "expire")
    private long expire;

    @TableField(value = "created_account")
    private String createdAccount;

    @TableField(value = "created_time")
    private Date createdTime;
}
