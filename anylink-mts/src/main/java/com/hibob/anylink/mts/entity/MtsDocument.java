package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_document")
public class MtsDocument {
    private static final long serialVersionUID = 1L;

    @TableId(value = "document_id")
    private String documentId;

    @TableField(value = "document_type")
    private String documentType;

    @TableField(value = "document_size")
    private long documentSize;

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
