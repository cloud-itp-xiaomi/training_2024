
CREATE DATABASE collector CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for cms_collector
-- ----------------------------
DROP TABLE IF EXISTS `cms_collector`;
CREATE TABLE `cms_collector`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `metric` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '指标名称',
  `endpoint` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '当前主机名称',
  `timestamp` int(64) NOT NULL COMMENT '采集数据时的时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `step` int(64) NOT NULL DEFAULT 60 COMMENT '指标的采集周期 默认60',
  `value` FLOAT(3,1) NOT NULL COMMENT '采集到的CPU或内存利用率的值',
  `system_type` varchar(50) NOT NULL COMMENT '系统类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统采集记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cms_collector
-- ----------------------------

-- ----------------------------
-- Table structure for collector_log
-- ----------------------------
DROP TABLE IF EXISTS `collector_log`;
CREATE TABLE `collector_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hostname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '当前主机名称',
  `file` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志文件的全路径',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `system_type` varchar(50) NOT NULL COMMENT '系统类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统采集日志表' ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Records of collector_log
-- ----------------------------

-- ----------------------------
-- Table structure for collector_detail_log
-- ----------------------------
DROP TABLE IF EXISTS `collector_detail_log`;
CREATE TABLE `collector_detail_log`  (
  `log_id` bigint(20) NOT NULL,
  `logs` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志具体内容',
  `create_time` datetime NOT NULL COMMENT '创建时间'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统采集日志详细表' ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Records of collector_detail_log
-- ----------------------------