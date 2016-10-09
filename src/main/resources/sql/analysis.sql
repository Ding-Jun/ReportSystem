/*
Navicat MySQL Data Transfer

Source Server         : localhost_funtest
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : analysis

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-09-19 15:50:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_permission`;
CREATE TABLE `t_admin_permission` (
  `adminId` int(11) NOT NULL,
  `permissionId` int(11) NOT NULL,
  KEY `FK_f79f2ce5705a40a2ad07e754c84` (`permissionId`),
  KEY `FK_8d87d6dad5be48a69d0cd2bef46` (`adminId`),
  CONSTRAINT `FK_8d87d6dad5be48a69d0cd2bef46` FOREIGN KEY (`adminId`) REFERENCES `t_admin` (`id`),
  CONSTRAINT `FK_f79f2ce5705a40a2ad07e754c84` FOREIGN KEY (`permissionId`) REFERENCES `t_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_chart
-- ----------------------------
DROP TABLE IF EXISTS `t_chart`;
CREATE TABLE `t_chart` (
  `id` int(11) NOT NULL,
  `chartImg` longtext,
  `chartType` int(11) DEFAULT NULL,
  `cp` double DEFAULT NULL,
  `cpi` double DEFAULT NULL,
  `cpk` double DEFAULT NULL,
  `cpu` double DEFAULT NULL,
  `datas` longtext,
  `groupCnt` int(11) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `limitMax` double DEFAULT NULL,
  `limitMin` double DEFAULT NULL,
  `quantityMax` int(11) DEFAULT NULL,
  `rangeMax` double DEFAULT NULL,
  `rangeMin` double DEFAULT NULL,
  `realAverage` double DEFAULT NULL,
  `realMax` double DEFAULT NULL,
  `realMin` double DEFAULT NULL,
  `passStdev` double DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `totalCnt` int(11) DEFAULT NULL,
  `visible` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_chart
-- ----------------------------

-- ----------------------------
-- Table structure for t_dataconfig
-- ----------------------------
DROP TABLE IF EXISTS `t_dataconfig`;
CREATE TABLE `t_dataconfig` (
  `id` int(11) NOT NULL,
  `dutPassColumnFlag` varchar(255) DEFAULT NULL,
  `dutPassFalseString` varchar(255) DEFAULT NULL,
  `dutPassTrueString` varchar(255) DEFAULT NULL,
  `limitMaxLineFlag` varchar(255) DEFAULT NULL,
  `limitMinLineFlag` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dutNoColumnFlag` varchar(255) DEFAULT NULL,
  `siteNoColumnFlag` varchar(255) DEFAULT NULL,
  `testItemColumnFlag` varchar(255) DEFAULT NULL,
  `ignoreColumnFlag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dataconfig
-- ----------------------------
INSERT INTO `t_dataconfig` VALUES ('0', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_dataconfig` VALUES ('1', '(?i)Dut_Pass', '(?i)FALSE', '(?i)TRUE', '(?i)^(0|Max),', '(?i)^(-1|Min),', 'joulwatt', '(?i)Dut_NO', '(?i)Site_No', '(?i),(OS|PIN)', '(?i)_debug$');

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `ResourceId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_3c3d3bf1a5cb47ff9aafb7e032b` (`ResourceId`),
  CONSTRAINT `FK_3c3d3bf1a5cb47ff9aafb7e032b` FOREIGN KEY (`ResourceId`) REFERENCES `t_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_report
-- ----------------------------
DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report` (
  `id` int(11) NOT NULL,
  `chipName` varchar(255) DEFAULT NULL,
  `circuitPropty` varchar(255) DEFAULT NULL,
  `curcuitPropty` varchar(255) DEFAULT NULL,
  `failureAnalysis` varchar(255) DEFAULT NULL,
  `finalName` varchar(255) DEFAULT NULL,
  `formalProgram` tinyint(1) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `lotNo` varchar(255) DEFAULT NULL,
  `osFailCount` int(11) DEFAULT NULL,
  `osFailRate` varchar(255) DEFAULT NULL,
  `packageStyle` varchar(255) DEFAULT NULL,
  `passPercent` varchar(255) DEFAULT NULL,
  `productSolution` varchar(255) DEFAULT NULL,
  `programName` varchar(255) DEFAULT NULL,
  `programVersion` varchar(255) DEFAULT NULL,
  `reportPreparedBy` varchar(255) DEFAULT NULL,
  `reportname` varchar(255) DEFAULT NULL,
  `sealNo` varchar(255) DEFAULT NULL,
  `sufeng` varchar(255) DEFAULT NULL,
  `testCount` varchar(255) DEFAULT NULL,
  `testMan` varchar(255) DEFAULT NULL,
  `testManagerDate` varchar(255) DEFAULT NULL,
  `testManagerName` varchar(255) DEFAULT NULL,
  `testManagerOpinion` varchar(255) DEFAULT NULL,
  `testNote` varchar(255) DEFAULT NULL,
  `testPurpose` varchar(255) DEFAULT NULL,
  `testResultAnalysis` varchar(255) DEFAULT NULL,
  `testSampleAnalysis` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_f4ef0a038fb74f7fb51b9b31a6e` (`userId`),
  CONSTRAINT `FK_f4ef0a038fb74f7fb51b9b31a6e` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_report
-- ----------------------------

-- ----------------------------
-- Table structure for t_reportitem
-- ----------------------------
DROP TABLE IF EXISTS `t_reportitem`;
CREATE TABLE `t_reportitem` (
  `id` int(11) NOT NULL,
  `columnname` varchar(255) DEFAULT NULL,
  `failCount` int(11) DEFAULT NULL,
  `failRate` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `limitMax` double DEFAULT NULL,
  `limitMin` double DEFAULT NULL,
  `limitUnit` varchar(255) DEFAULT NULL,
  `testNo` int(11) DEFAULT NULL,
  `visible` tinyint(1) DEFAULT NULL,
  `failChartId` int(11) DEFAULT NULL,
  `passChartId` int(11) DEFAULT NULL,
  `reportId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_c46e18fa9f56400a8f13fd2aba5` (`failChartId`),
  KEY `FK_c5b03f94e5b54d029b135242c24` (`passChartId`),
  KEY `FK_2c4755a420ea4e0da0803a6aa9a` (`reportId`),
  CONSTRAINT `FK_2c4755a420ea4e0da0803a6aa9a` FOREIGN KEY (`reportId`) REFERENCES `t_report` (`id`),
  CONSTRAINT `FK_c46e18fa9f56400a8f13fd2aba5` FOREIGN KEY (`failChartId`) REFERENCES `t_chart` (`id`),
  CONSTRAINT `FK_c5b03f94e5b54d029b135242c24` FOREIGN KEY (`passChartId`) REFERENCES `t_chart` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_reportitem
-- ----------------------------

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` int(11) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_resource
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `roleId` int(11) NOT NULL,
  `permissionId` int(11) NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`),
  KEY `FK_d5d736f0135e40fb9da765fc4ba` (`permissionId`),
  KEY `FK_3f96b5cbc81f4d68aded6487dec` (`roleId`),
  CONSTRAINT `FK_3f96b5cbc81f4d68aded6487dec` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FK_d5d736f0135e40fb9da765fc4ba` FOREIGN KEY (`permissionId`) REFERENCES `t_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL,
  `avatar` longtext,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phoneNo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
