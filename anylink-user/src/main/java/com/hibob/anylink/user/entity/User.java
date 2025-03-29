package com.hibob.anylink.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName("anylink_user_info")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "account")
    private String account;

    @TableField("nick_name")
    private String nickName;

    @TableField("avatar_id")
    private long avatarId;

    @TableField("password")
    private String password;

    @TableField("gender")
    private int gender;

    @TableField("level")
    private int level;

    @TableField("signature")
    private String signature;

    @TableField("phone_num")
    private String phoneNum;

    @TableField("email")
    private String email;

    @TableField("birthday")
    private Date birthday;

    @TableField("new_msg_tips")
    private Boolean newMsgTips;

    @TableField("send_msg_tips")
    private Boolean sendMsgTips;

    @TableField("created_time")
    private Date createdTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField(value = "status", exist = false)
    private int status;

}
