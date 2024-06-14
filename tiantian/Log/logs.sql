
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `logs2`;
CREATE TABLE `logs2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hostname` varchar(20) DEFAULT NULL COMMENT '主机名',
  `file` varchar(50) DEFAULT NULL COMMENT '日志',
  `content` varchar(10000) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;


