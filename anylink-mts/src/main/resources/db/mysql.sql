CREATE DATABASE IF NOT EXISTS `anylink`;
use `anylink`;
drop table IF EXISTS `anylink_mts_object`;
CREATE TABLE `anylink_mts_object`
(
    `object_id` BIGINT NOT NULL COMMENT '富媒体对象唯一ID，采用雪花算法',
    `object_type` TINYINT(1) NOT NULL COMMENT '对象类型: 0图像，1音频，2视频，3文档',
    `foreign_id` VARCHAR(255) NOT NULL COMMENT '外键id',
    `created_account` VARCHAR(255) NOT NULL COMMENT '创建者',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(`object_id`),
    INDEX `idx_foreign_id`(foreign_id)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '富媒体对象表，MTS服务的首表';

drop table IF EXISTS `anylink_mts_document`;
CREATE TABLE `anylink_mts_document`
(
    `document_id` VARCHAR(255) NOT NULL COMMENT '文档唯一ID，采用文件md5计算方式',
    `document_type` VARCHAR(255) NOT NULL COMMENT '文档类型',
    `document_size` BIGINT NOT NULL COMMENT '文档大小',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `store_source` VARCHAR(64) NOT NULL COMMENT '存储源',
    `bucket_name` VARCHAR(64) NOT NULL COMMENT '桶名',
    `full_path` VARCHAR(255) NOT NULL COMMENT '文件全路径',
    `expire` BIGINT NOT NULL COMMENT '过期时间',
    `created_account` VARCHAR(255) NOT NULL COMMENT '创建者',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(`document_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '文件对象信息表';

drop table IF EXISTS `anylink_mts_image`;
CREATE TABLE `anylink_mts_image`
(
    `image_id` VARCHAR(255) NOT NULL COMMENT '图像唯一ID，采用文件md5计算方式',
    `image_type` VARCHAR(255) NOT NULL COMMENT '图像类型',
    `image_size` BIGINT NOT NULL COMMENT '图像大小',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `store_source` VARCHAR(64) NOT NULL COMMENT '存储源',
    `bucket_name` VARCHAR(64) NOT NULL COMMENT '桶名',
    `origin_path` VARCHAR(255) NOT NULL COMMENT '原始图全路径',
    `thumb_path` VARCHAR(255) NOT NULL COMMENT '缩略图全路径',
    `expire` BIGINT NOT NULL COMMENT '过期时间',
    `created_account` VARCHAR(255) NOT NULL COMMENT '创建者',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(`image_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '图像表';

drop table IF EXISTS `anylink_mts_audio`;
CREATE TABLE `anylink_mts_audio`
(
    `audio_id` VARCHAR(255) NOT NULL COMMENT '音频唯一ID，采用文件md5计算方式',
    `audio_type` VARCHAR(255) NOT NULL COMMENT '音频类型',
    `audio_size` BIGINT NOT NULL COMMENT '音频大小',
    `audio_duration` INT NOT NULL COMMENT '录音的时长（秒），其他音频文件没有',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `store_source` VARCHAR(64) NOT NULL COMMENT '存储源',
    `bucket_name` VARCHAR(64) NOT NULL COMMENT '桶名',
    `full_path` VARCHAR(255) NOT NULL COMMENT '文件全路径',
    `expire` BIGINT NOT NULL COMMENT '过期时间',
    `created_account` VARCHAR(255) NOT NULL COMMENT '创建者',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(`audio_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '音频表';

drop table IF EXISTS `anylink_mts_video`;
CREATE TABLE `anylink_mts_video`
(
    `video_id` VARCHAR(255) NOT NULL COMMENT '视频唯一ID，采用文件md5计算方式',
    `video_type` VARCHAR(255) NOT NULL COMMENT '视频类型',
    `video_size` BIGINT NOT NULL COMMENT '视频大小',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `store_source` VARCHAR(64) NOT NULL COMMENT '存储源',
    `bucket_name` VARCHAR(64) NOT NULL COMMENT '桶名',
    `full_path` VARCHAR(255) NOT NULL COMMENT '文件全路径',
    `expire` BIGINT NOT NULL COMMENT '过期时间',
    `created_account` VARCHAR(255) NOT NULL COMMENT '创建者',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(`video_id`)
) ENGINE=INNODB CHARSET=utf8mb4 COMMENT '视频表';