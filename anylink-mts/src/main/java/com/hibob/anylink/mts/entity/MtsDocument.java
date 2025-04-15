package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_document")
public class MtsDocument {
    private static final long serialVersionUID = 1L;

    @TableField(value = "document_id")
    private String documentId;

    @TableField(value = "store_type")
    private int storeType;

    @TableField(value = "document_type")
    private String documentType;

    @TableField(value = "document_size")
    private long documentSize;

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
