/*
Navicat Oracle Data Transfer
Oracle Client Version : 11.2.0.1.0

Source Server         : ACC
Source Server Version : 120100
Source Host           : 106.14.30.97:1521
Source Schema         : DATA_FOUNTAIN

Target Server Type    : ORACLE
Target Server Version : 120100
File Encoding         : 65001

Date: 2017-06-08 14:50:32
*/


-- ----------------------------
-- Table structure for TB_MERCHANTID
-- ----------------------------
DROP TABLE "DATA_FOUNTAIN"."TB_MERCHANTID";
CREATE TABLE "DATA_FOUNTAIN"."TB_MERCHANTID" (
"MERCHANT_ID" VARCHAR2(20 BYTE) NOT NULL ,
"MERCHANT_NAME" VARCHAR2(100 BYTE) NULL ,
"CREAT_TIME" VARCHAR2(30 BYTE) NULL ,
"CREATOR" VARCHAR2(30 BYTE) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of TB_MERCHANTID
-- ----------------------------
INSERT INTO "DATA_FOUNTAIN"."TB_MERCHANTID" VALUES ('AXJJ170510', '安信基金', null, null);
INSERT INTO "DATA_FOUNTAIN"."TB_MERCHANTID" VALUES ('AXJR170505', '安信金融', null, null);
INSERT INTO "DATA_FOUNTAIN"."TB_MERCHANTID" VALUES ('ZZTEST0516', 'test', null, null);
INSERT INTO "DATA_FOUNTAIN"."TB_MERCHANTID" VALUES ('ZZDAILYREPORT', '日报', null, null);

-- ----------------------------
-- Indexes structure for table TB_MERCHANTID
-- ----------------------------

-- ----------------------------
-- Checks structure for table TB_MERCHANTID
-- ----------------------------
ALTER TABLE "DATA_FOUNTAIN"."TB_MERCHANTID" ADD CHECK ("MERCHANT_ID" IS NOT NULL);
ALTER TABLE "DATA_FOUNTAIN"."TB_MERCHANTID" ADD CHECK ("MERCHANT_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table TB_MERCHANTID
-- ----------------------------
ALTER TABLE "DATA_FOUNTAIN"."TB_MERCHANTID" ADD PRIMARY KEY ("MERCHANT_ID");
