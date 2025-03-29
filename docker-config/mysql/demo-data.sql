/*
SQLyog Community v13.2.0 (64 bit)
MySQL - 8.0.41 : Database - anylink
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`anylink` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `anylink`;

/*Data for the table `anylink_chat_session` */

insert  into `anylink_chat_session`(`account`,`session_id`,`remote_id`,`session_type`,`read_msg_id`,`read_time`,`top`,`dnd`,`draft`,`mark`,`partition_id`,`closed`,`join_time`,`leave_time`) values 
('a00001','158691065723879424','158691065723879424',3,0,'2025-03-13 21:41:38',0,0,'','',0,0,'[\"2025-03-13 21:41:38.000000\"]',NULL),
('a00001','158691141649170432','158691141649170432',3,0,'2025-03-13 21:41:56',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00001','158691214953021440','158691214953021440',3,0,'2025-03-13 21:42:13',0,0,'','',0,0,'[\"2025-03-13 21:42:13.000000\"]',NULL),
('a00001','a00001@a00002','a00002',2,0,'2025-03-13 21:27:01',0,0,'','',0,0,NULL,NULL),
('a00001','a00001@a00003','a00003',2,0,'2025-03-13 21:27:02',0,0,'','',0,0,NULL,NULL),
('a00001','a00001@a00004','a00004',2,0,'2025-03-13 21:27:03',0,0,'','',0,0,NULL,NULL),
('a00001','a00001@a00005','a00005',2,0,'2025-03-13 21:27:04',0,0,'','',0,0,NULL,NULL),
('a00001','a00001@a00006','a00006',2,0,'2025-03-13 21:27:07',0,0,'','',0,0,NULL,NULL),
('a00001','a00001@a00007','a00007',2,0,'2025-03-13 21:27:10',0,0,'','',0,0,NULL,NULL),
('a00001','a00001@a123456','a123456',2,0,'2025-03-13 21:27:15',0,0,'','',0,0,NULL,NULL),
('a00002','158691141649170432','158691141649170432',3,0,'2025-03-13 21:41:56',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00002','158691214953021440','158691214953021440',3,0,'2025-03-13 21:42:13',0,0,'','',0,0,'[\"2025-03-13 21:42:13.000000\"]',NULL),
('a00002','a00001@a00002','a00001',2,0,'2025-03-13 21:27:37',0,0,'','',0,0,NULL,NULL),
('a00002','a00002@a00003','a00003',2,0,'2025-03-13 21:27:41',0,0,'','',0,0,NULL,NULL),
('a00002','a00002@a00004','a00004',2,0,'2025-03-13 21:28:01',0,0,'','',0,0,NULL,NULL),
('a00002','a00002@a00005','a00005',2,0,'2025-03-13 21:27:42',0,0,'','',0,0,NULL,NULL),
('a00002','a00002@a00006','a00006',2,0,'2025-03-13 21:27:44',0,0,'','',0,0,NULL,NULL),
('a00002','a00002@a00007','a00007',2,0,'2025-03-13 21:27:45',0,0,'','',0,0,NULL,NULL),
('a00002','a00002@a123456','a123456',2,0,'2025-03-13 21:27:38',0,0,'','',0,0,NULL,NULL),
('a00003','158691108577083392','158691108577083392',3,0,'2025-03-13 21:41:48',0,0,'','',0,0,'[\"2025-03-13 21:41:48.000000\"]',NULL),
('a00003','158691141649170432','158691141649170432',3,0,'2025-03-13 21:41:56',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00003','158691195474673664','158691195474673664',3,0,'2025-03-13 21:42:09',0,0,'','',0,0,'[\"2025-03-13 21:42:09.000000\"]',NULL),
('a00003','158691214953021440','158691214953021440',3,0,'2025-03-13 21:42:13',0,0,'','',0,0,'[\"2025-03-13 21:42:13.000000\"]',NULL),
('a00003','a00001@a00003','a00001',2,0,'2025-03-13 21:28:11',0,0,'','',0,0,NULL,NULL),
('a00003','a00002@a00003','a00002',2,0,'2025-03-13 21:28:14',0,0,'','',0,0,NULL,NULL),
('a00003','a00003@a00004','a00004',2,0,'2025-03-13 21:28:17',0,0,'','',0,0,NULL,NULL),
('a00003','a00003@a00005','a00005',2,0,'2025-03-13 21:28:18',0,0,'','',0,0,NULL,NULL),
('a00003','a00003@a00006','a00006',2,0,'2025-03-13 21:28:20',0,0,'','',0,0,NULL,NULL),
('a00003','a00003@a00007','a00007',2,0,'2025-03-13 21:28:21',0,0,'','',0,0,NULL,NULL),
('a00003','a00003@a123456','a123456',2,0,'2025-03-13 21:28:13',0,0,'','',0,0,NULL,NULL),
('a00004','158691065723879424','158691065723879424',3,0,'2025-03-13 21:41:38',0,0,'','',0,0,'[\"2025-03-13 21:41:38.000000\"]',NULL),
('a00004','158691088943546368','158691088943546368',3,0,'2025-03-13 21:41:43',0,0,'','',0,0,'[\"2025-03-13 21:41:43.000000\"]',NULL),
('a00004','158691141649170432','158691141649170432',3,0,'2025-03-13 21:41:56',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00004','158691195474673664','158691195474673664',3,0,'2025-03-13 21:42:09',0,0,'','',0,0,'[\"2025-03-13 21:42:09.000000\"]',NULL),
('a00004','a00001@a00004','a00001',2,0,'2025-03-13 21:28:57',0,0,'','',0,0,NULL,NULL),
('a00004','a00002@a00004','a00002',2,0,'2025-03-13 21:28:59',0,0,'','',0,0,NULL,NULL),
('a00004','a00003@a00004','a00003',2,0,'2025-03-13 21:29:01',0,0,'','',0,0,NULL,NULL),
('a00004','a00004@a00005','a00005',2,0,'2025-03-13 21:29:02',0,0,'','',0,0,NULL,NULL),
('a00004','a00004@a00006','a00006',2,0,'2025-03-13 21:29:06',0,0,'','',0,0,NULL,NULL),
('a00004','a00004@a00007','a00007',2,0,'2025-03-13 21:29:08',0,0,'','',0,0,NULL,NULL),
('a00004','a00004@a123456','a123456',2,0,'2025-03-13 21:28:58',0,0,'','',0,0,NULL,NULL),
('a00005','158691065723879424','158691065723879424',3,0,'2025-03-13 21:41:38',0,0,'','',0,0,'[\"2025-03-13 21:41:38.000000\"]',NULL),
('a00005','158691141649170432','158691141649170432',3,0,'2025-03-13 21:41:56',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00005','a00001@a00005','a00001',2,0,'2025-03-13 21:29:20',0,0,'','',0,0,NULL,NULL),
('a00005','a00002@a00005','a00002',2,0,'2025-03-13 21:29:23',0,0,'','',0,0,NULL,NULL),
('a00005','a00003@a00005','a00003',2,0,'2025-03-13 21:29:29',0,0,'','',0,0,NULL,NULL),
('a00005','a00004@a00005','a00004',2,0,'2025-03-13 21:29:25',0,0,'','',0,0,NULL,NULL),
('a00005','a00005@a00006','a00006',2,0,'2025-03-13 21:29:30',0,0,'','',0,0,NULL,NULL),
('a00005','a00005@a00007','a00007',2,0,'2025-03-13 21:29:32',0,0,'','',0,0,NULL,NULL),
('a00005','a00005@a123456','a123456',2,0,'2025-03-13 21:29:21',0,0,'','',0,0,NULL,NULL),
('a00006','158691088943546368','158691088943546368',3,0,'2025-03-13 21:41:43',0,0,'','',0,0,'[\"2025-03-13 21:41:43.000000\"]',NULL),
('a00006','158691108577083392','158691108577083392',3,0,'2025-03-13 21:41:48',0,0,'','',0,0,'[\"2025-03-13 21:41:48.000000\"]',NULL),
('a00006','158691141649170432','158691141649170432',3,0,'2025-03-13 21:41:56',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00006','158691195474673664','158691195474673664',3,0,'2025-03-13 21:42:09',0,0,'','',0,0,'[\"2025-03-13 21:42:09.000000\"]',NULL),
('a00006','a00001@a00006','a00001',2,0,'2025-03-13 21:29:55',0,0,'','',0,0,NULL,NULL),
('a00006','a00002@a00006','a00002',2,0,'2025-03-13 21:29:58',0,0,'','',0,0,NULL,NULL),
('a00006','a00003@a00006','a00003',2,0,'2025-03-13 21:30:00',0,0,'','',0,0,NULL,NULL),
('a00006','a00004@a00006','a00004',2,0,'2025-03-13 21:29:59',0,0,'','',0,0,NULL,NULL),
('a00006','a00005@a00006','a00005',2,0,'2025-03-13 21:30:07',0,0,'','',0,0,NULL,NULL),
('a00006','a00006@a00007','a00007',2,0,'2025-03-13 21:30:04',0,0,'','',0,0,NULL,NULL),
('a00006','a00006@a123456','a123456',2,0,'2025-03-13 21:29:56',0,0,'','',0,0,NULL,NULL),
('a00007','158691088943546368','158691088943546368',3,10001,'2025-03-13 13:41:44',0,0,'','',0,0,'[\"2025-03-13 21:41:43.000000\"]',NULL),
('a00007','158691108577083392','158691108577083392',3,10001,'2025-03-13 13:42:01',0,0,'','',0,0,'[\"2025-03-13 21:41:48.000000\"]',NULL),
('a00007','158691141649170432','158691141649170432',3,10001,'2025-03-13 13:42:02',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a00007','158691195474673664','158691195474673664',3,10001,'2025-03-13 13:42:10',0,0,'','',0,0,'[\"2025-03-13 21:42:09.000000\"]',NULL),
('a00007','158691214953021440','158691214953021440',3,10001,'2025-03-13 13:42:14',0,0,'','',0,0,'[\"2025-03-13 21:42:14.000000\"]',NULL),
('a00007','a00001@a00007','a00001',2,0,'2025-03-13 21:30:19',0,0,'','',0,0,NULL,NULL),
('a00007','a00002@a00007','a00002',2,0,'2025-03-13 21:30:21',0,0,'','',0,0,NULL,NULL),
('a00007','a00003@a00007','a00003',2,0,'2025-03-13 21:30:23',0,0,'','',0,0,NULL,NULL),
('a00007','a00004@a00007','a00004',2,0,'2025-03-13 21:30:24',0,0,'','',0,0,NULL,NULL),
('a00007','a00005@a00007','a00005',2,0,'2025-03-13 21:30:25',0,0,'','',0,0,NULL,NULL),
('a00007','a00006@a00007','a00006',2,0,'2025-03-13 21:30:27',0,0,'','',0,0,NULL,NULL),
('a00007','a00007@a123456','a123456',2,10021,'2025-03-13 13:38:12',0,0,'','',0,0,NULL,NULL),
('a123456','158691065723879424','158691065723879424',3,10001,'2025-03-13 13:41:39',0,0,'','',0,0,'[\"2025-03-13 21:41:38.000000\"]',NULL),
('a123456','158691088943546368','158691088943546368',3,0,'2025-03-13 21:41:43',0,0,'','',0,0,'[\"2025-03-13 21:41:43.000000\"]',NULL),
('a123456','158691108577083392','158691108577083392',3,10001,'2025-03-13 13:41:49',0,0,'','',0,0,'[\"2025-03-13 21:41:48.000000\"]',NULL),
('a123456','158691141649170432','158691141649170432',3,10001,'2025-03-13 13:41:57',0,0,'','',0,0,'[\"2025-03-13 21:41:56.000000\"]',NULL),
('a123456','a00001@a123456','a00001',2,0,'2025-03-13 21:25:09',0,0,'','',0,0,NULL,NULL),
('a123456','a00002@a123456','a00002',2,0,'2025-03-13 21:25:12',0,0,'','',0,0,NULL,NULL),
('a123456','a00003@a123456','a00003',2,0,'2025-03-13 21:25:16',0,0,'','',0,0,NULL,NULL),
('a123456','a00004@a123456','a00004',2,0,'2025-03-13 21:25:19',0,0,'','',0,0,NULL,NULL),
('a123456','a00005@a123456','a00005',2,0,'2025-03-13 21:25:22',0,0,'','',0,0,NULL,NULL),
('a123456','a00006@a123456','a00006',2,0,'2025-03-13 21:25:25',0,0,'','',0,0,NULL,NULL),
('a123456','a00007@a123456','a00007',2,10018,'2025-03-13 13:38:09',0,0,'','',0,0,NULL,NULL);

/*Data for the table `anylink_group_info` */

insert  into `anylink_group_info`(`group_id`,`group_type`,`group_name`,`announcement`,`avatar_id`, `history_browse`,`all_muted`,`join_group_approval`,`creator`,`created_time`,`del_flag`,`del_time`) values
('158691065723879424',1,'久旱逢甘雨','',NULL,0,0,0,'a123456','2025-03-13 21:41:38',0,NULL),
('158691088943546368',1,'他乡遇故知','',NULL,0,0,0,'a00007','2025-03-13 21:41:43',0,NULL),
('158691108577083392',1,'洞房花烛夜','',NULL,0,0,0,'a123456','2025-03-13 21:41:48',0,NULL),
('158691141649170432',1,'金榜题名时','',NULL,0,0,0,'a123456','2025-03-13 21:41:56',0,NULL),
('158691195474673664',1,'海内存知己','',NULL,0,0,0,'a00007','2025-03-13 21:42:09',0,NULL),
('158691214953021440',1,'天涯若比邻','',NULL,0,0,0,'a00007','2025-03-13 21:42:13',0,NULL);

/*Data for the table `anylink_group_member` */

insert  into `anylink_group_member`(`group_id`,`account`,`nick_name`,`role`,`muted_mode`,`in_status`) values 
('158691065723879424','a00001','李白',0,0,0),
('158691065723879424','a00004','白居易',0,0,0),
('158691065723879424','a00005','唐伯虎',0,0,0),
('158691065723879424','a123456','王维',2,0,0),
('158691088943546368','a00004','白居易',0,0,0),
('158691088943546368','a00006','辛弃疾',0,0,0),
('158691088943546368','a00007','欧阳修',2,0,0),
('158691088943546368','a123456','王维',0,0,0),
('158691108577083392','a00003','苏轼',0,0,0),
('158691108577083392','a00006','辛弃疾',0,0,0),
('158691108577083392','a00007','欧阳修',0,0,0),
('158691108577083392','a123456','王维',2,0,0),
('158691141649170432','a00001','李白',0,0,0),
('158691141649170432','a00002','杜甫',0,0,0),
('158691141649170432','a00003','苏轼',0,0,0),
('158691141649170432','a00004','白居易',0,0,0),
('158691141649170432','a00005','唐伯虎',0,0,0),
('158691141649170432','a00006','辛弃疾',0,0,0),
('158691141649170432','a00007','欧阳修',0,0,0),
('158691141649170432','a123456','王维',2,0,0),
('158691195474673664','a00003','苏轼',0,0,0),
('158691195474673664','a00004','白居易',0,0,0),
('158691195474673664','a00006','辛弃疾',0,0,0),
('158691195474673664','a00007','欧阳修',2,0,0),
('158691214953021440','a00001','李白',0,0,0),
('158691214953021440','a00002','杜甫',0,0,0),
('158691214953021440','a00003','苏轼',0,0,0),
('158691214953021440','a00007','欧阳修',2,0,0);

/*Data for the table `anylink_user_info` */

insert  into `anylink_user_info`(`id`,`account`,`nick_name`,`avatar_id`,`password`,`gender`,`level`,`signature`,`phone_num`,`email`,`birthday`,`new_msg_tips`,`send_msg_tips`,`created_time`,`update_time`) values
(1900173352510787586,'a00001','李白',NULL,'$2a$10$xkDV/RerNeW.JGM1/XIdzOCe8sALfA/Wcq/GUQQ/Sg4Zw3R9iFi72',0,0,'','','','1970-01-01',1,1,'2025-03-13 21:13:25','2025-03-13 13:24:36'),
(1900175138856472577,'a123456','王维',NULL,'$2a$10$EzsZyvZYcSPSi99Y37Fo.OMfF/tG6NTQZjJoLVXMFFIj/lmVxD6JS',0,0,'','','',NULL,1,1,'2025-03-13 21:20:31','2025-03-13 21:20:31'),
(1900175304376291330,'a00002','杜甫',NULL,'$2a$10$fMHisoUiWbjSkRTB0rM7LugakprGQzTyf9wfbhCuAMbEgwx14e/nO',0,0,'','','',NULL,1,1,'2025-03-13 21:21:10','2025-03-13 21:21:10'),
(1900175379810848769,'a00003','苏轼',NULL,'$2a$10$9.Dg/MAEa3iV6/kNyrIjj.7D.OVHRSTdkLYvB47UTIUVM5XS5ld3y',0,0,'','','',NULL,1,1,'2025-03-13 21:21:28','2025-03-13 21:21:28'),
(1900175484102217730,'a00004','白居易',NULL,'$2a$10$rW0W5r5Ez1SAEDCsep3EzOCyW294ssvrd2sE5ifIqfzeg/yDICrsK',0,0,'','','',NULL,1,1,'2025-03-13 21:21:53','2025-03-13 21:21:53'),
(1900175660925685762,'a00005','唐伯虎',NULL,'$2a$10$Zv7CDJ0lzHT92e6LdxdY2uaxvidWAtaYORnQPjCHSk3rZgA.mm2EW',0,0,'','','',NULL,1,1,'2025-03-13 21:22:35','2025-03-13 21:22:35'),
(1900175802458279937,'a00006','辛弃疾',NULL,'$2a$10$qMLIWdlnSa3ztno1tEulluj56ys0TopUXr7P6.X6lhpWOui3pmlde',0,0,'','','',NULL,1,1,'2025-03-13 21:23:09','2025-03-13 21:23:09'),
(1900175894573584385,'a00007','欧阳修',NULL,'$2a$10$SkjZDhIeI.3dyjP6PZyco.SVH1gMSAII0cEHlAbRlrei/7QCqBL5K',0,0,'','','',NULL,1,1,'2025-03-13 21:23:31','2025-03-13 21:23:31');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
