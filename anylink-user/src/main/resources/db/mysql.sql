CREATE DATABASE IF NOT EXISTS `anylink`;
use `anylink`;
drop table IF EXISTS `anylink_user_info`;
CREATE TABLE `anylink_user_info`(
    `id` BIGINT NOT NULL COMMENT 'id，雪花算法生成',
    `account` VARCHAR(255) NOT NULL COMMENT '账号，用户指定',
    `nick_name` VARCHAR(255) NOT NULL COMMENT '用户昵称',
    `avatar_id` BIGINT DEFAULT NULL COMMENT '用户头像id',
    `password` VARCHAR(1024) NOT NULL COMMENT '密码',
    `gender`  TINYINT(1) DEFAULT 0 COMMENT '性别 0:默认值（无效） 1:男 2:女',
    `level`  TINYINT(1) DEFAULT 1 COMMENT '用户级别 1:普通用户 其他TODO',
    `signature` VARCHAR(1024) DEFAULT '' COMMENT '个性签名',
    `phone_num` VARCHAR(20) DEFAULT '' COMMENT '手机号码',
    `email` VARCHAR(255) DEFAULT '' COMMENT '邮箱',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `new_msg_tips` BOOLEAN DEFAULT TRUE COMMENT  '新消息提醒',
    `send_msg_tips` BOOLEAN DEFAULT TRUE COMMENT  '发送消息提醒',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (id),
    INDEX `idx_account`(account),
    INDEX `idx_nick_name`(nick_name)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '用户信息表';

drop table IF EXISTS `anylink_user_client`;
CREATE TABLE `anylink_user_client`(
    `unique_id` VARCHAR(255) NOT NULL COMMENT '客户端唯一ID，account+@+客户端生成的uuid',
    `account` VARCHAR(255) NOT NULL COMMENT '账号',
    `client_type` TINYINT(1) DEFAULT -1 COMMENT '客户端类型 0:android 1:ios 2:web -1:未知',
    `client_name` VARCHAR(255) DEFAULT '' COMMENT '客户端名称',
    `client_version` VARCHAR(255) DEFAULT '' COMMENT '客户端版本',
    `last_login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(`unique_id`),
    INDEX `idx_account`(account)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '客户端表';

-- TODO 需要做老化处理
drop table IF EXISTS `anylink_user_login`;
CREATE TABLE `anylink_user_login`(
    `account` VARCHAR(255) NOT NULL COMMENT '账号',
    `unique_id` VARCHAR(255) NOT NULL COMMENT '客户端唯一ID，account+@+客户端生成的uuid',
    `login_time` DATETIME DEFAULT NULL COMMENT '登录时间',
    `refresh_time` DATETIME DEFAULT NULL COMMENT '刷新token时间',
    `logout_time` DATETIME DEFAULT NULL COMMENT '登出时间',
    INDEX `idx_account`(account)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '登录记录表';


drop table IF EXISTS `anylink_user_status`;
CREATE TABLE `anylink_user_status`(
    `account` VARCHAR(255) NOT NULL COMMENT '账号',
    `unique_id` VARCHAR(255) NOT NULL COMMENT '客户端唯一ID，account+@+客户端生成的uuid',
    `status` TINYINT(1) DEFAULT 0 COMMENT '状态: 0离线，1离开，2在线，3忙碌',
    `update_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY(account, unique_id)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '用户状态表';
