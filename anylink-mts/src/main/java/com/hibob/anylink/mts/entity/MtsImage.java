package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_image")
public class MtsImage {
    private static final long serialVersionUID = 1L;

    @TableField(value = "image_id")
    private String imageId;

    @TableField(value = "store_type")
    private int storeType;

    @TableField(value = "image_type")
    private String imageType;

    @TableField(value = "image_size")
    private long imageSize;

    @TableField(value = "origin_width")
    private int originWidth;

    @TableField(value = "origin_height")
    private int originHeight;

    @TableField(value = "thumb_width")
    private int thumbWidth;

    @TableField(value = "thumb_height")
    private int thumbHeight;

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

    @TableField(value = "uploaded")
    private boolean uploaded;

    @TableField(value = "created_account")
    private String createdAccount;

    @TableField(value = "created_time")
    private Date createdTime;
}
