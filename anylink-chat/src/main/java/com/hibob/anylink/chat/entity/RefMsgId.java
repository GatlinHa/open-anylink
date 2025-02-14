package com.hibob.anylink.chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("anylink_chat_ref_msgId")
public class RefMsgId {
    private static final long serialVersionUID = 1L;

    @TableId(value = "session_id")
    private String sessionId;

    @TableField(value = "ref_msg_id")
    private long refMsgId;

}
