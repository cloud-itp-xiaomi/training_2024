/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : localhost:3306
 Source Schema         : cup

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 20/06/2024 23:21:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cpu
-- ----------------------------
DROP TABLE IF EXISTS `cpu`;
CREATE TABLE `cpu`  (
  `id` int(10) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `metric` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `timestamp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `step` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cpu
-- ----------------------------
INSERT INTO `cpu` VALUES (0000000001, 'cpu.used.percent', '1715765640', '60', '60.1', 'my-computer');
INSERT INTO `cpu` VALUES (0000000002, 'mem.used.percent', '1715765640', '60', '35.0', 'my-computer');
INSERT INTO `cpu` VALUES (0000000003, 'cpu.used.percent', '1715765640', '60', '60.1', 'my-computer');
INSERT INTO `cpu` VALUES (0000000004, 'cpu.used.percent', '1715765640', '60', '60.1', 'my-computer');
INSERT INTO `cpu` VALUES (0000000005, 'mem.used.percent', '1715765640', '60', '35.0', 'my-computer');

SET FOREIGN_KEY_CHECKS = 1;
