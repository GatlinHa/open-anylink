package com.hibob.anylink.chat.entity;

import com.hibob.anylink.common.constants.Const;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document("anylink_chat_msg") //项目启动后会自动创建这张表，以及索引（索引需要打开auto-index-creation开关）
public class MsgTable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("session_id")
    @Indexed
    private String sessionId;

    @Field("from_id")
    private String fromId;

    @Field("from_client")
    private String fromClient;

    /**
     * 消息接收方，如果是单聊就是toId，如果是群聊就是groupId
     */
    @Field("remote_id")
    private String remoteId;

    @Field("msg_id")
    @Indexed
    private long msgId;

    @Field("msg_type")
    private int msgType;

    @Field("content")
    private String content;

    @Field("content_type")
    @Indexed
    private int contentType;

    @Field("revoke")
    @Indexed
    private boolean revoke;

    @Field("msg_time")
    @Indexed
    private Date msgTime;

    @Field("create_time")
    @Indexed(expireAfterSeconds = Const.MSG_TTL_IN_MONGODB)
    private Date createTime;
}
