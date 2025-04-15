package com.hibob.anylink.mts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_mts_object")
public class MtsObject {
    private static final long serialVersionUID = 1L;

    @TableId(value = "object_id")
    private Long objectId;

    @TableField(value = "object_type")
    private int objectType;

    @TableField(value = "store_type")
    private int storeType;

    @TableField(value = "foreign_id")
    private String foreignId;

    @TableField(value = "created_account")
    private String createdAccount;

    @TableField(value = "created_time")
    private Date createdTime;
}
