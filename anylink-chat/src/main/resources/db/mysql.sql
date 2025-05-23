CREATE DATABASE IF NOT EXISTS `anylink`;
use `anylink`;
DROP TABLE IF EXISTS anylink_chat_ref_msgId;
CREATE TABLE anylink_chat_ref_msgId
(
    `session_id` VARCHAR(512) NOT NULL COMMENT 'session id，主键',
    `ref_msg_id` BIGINT DEFAULT 10000 COMMENT 'msgId参考值，初始值10000',
    PRIMARY KEY(`session_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '参考MsgId表';

DROP TABLE IF EXISTS `anylink_chat_session`;
CREATE TABLE `anylink_chat_session`
(
    `account` VARCHAR(255) NOT NULL COMMENT '账号，联合主键',
    `session_id` VARCHAR(512) NOT NULL COMMENT 'session id，联合主键',
    `remote_id` VARCHAR(255) NOT NULL COMMENT '会话的对方Id，单聊为对方账号，群聊为groupId',
    `session_type`  TINYINT(1) NOT NULL DEFAULT 99 COMMENT '会话类型：按照MsgType定义',
    `read_msg_id` BIGINT DEFAULT 0 COMMENT '已读消息Id，初始值0，表示没有已读的',
    `read_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '已读消息的时间',
    `top` BOOLEAN DEFAULT FALSE COMMENT '会话是否置顶，默认false',
    `dnd` BOOLEAN DEFAULT FALSE COMMENT '会话是免打扰，默认false',
    `draft` VARCHAR(3000) DEFAULT '' COMMENT '草稿',
    `mark` VARCHAR(255) DEFAULT '' COMMENT '备注',
    `partition_id` INT DEFAULT 0 COMMENT '分组id，默认0表示没有分组',
    `closed` BOOLEAN DEFAULT FALSE COMMENT '会话关闭状态',
    `del_msg_ids` JSON COMMENT '记录删除的消息id集合，数组结构',
    `join_time` JSON COMMENT '记录每一次进群的时间，数组结构',
    `leave_time` JSON COMMENT '记录每一次离群的时间，数组结构',
    PRIMARY KEY(`account`, `session_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '用户的会话信息表';

DROP TABLE IF EXISTS `anylink_chat_partition`;
CREATE TABLE `anylink_chat_partition`
(
    `account` VARCHAR(255) NOT NULL COMMENT '账号，联合主键',
    `partition_id` INT NOT NULL COMMENT '分组id，联合主键',
    `partition_name` VARCHAR(255) NOT NULL COMMENT '分组名称', -- TODO 名字也不能重复
    `partition_type` TINYINT(1) NOT NULL COMMENT '分组类型，0联系人，1群组',
    `update_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY(`account`, `partition_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '用户给会话分组的表';