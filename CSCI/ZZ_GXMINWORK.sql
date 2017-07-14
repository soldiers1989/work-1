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

Date: 2017-06-15 09:27:26
*/


-- ----------------------------
-- Table structure for ZZ_GXMINWORK
-- ----------------------------
DROP TABLE "DATA_FOUNTAIN"."ZZ_GXMINWORK";
CREATE TABLE "DATA_FOUNTAIN"."ZZ_GXMINWORK" (
"SEND_TEL_NO" VARCHAR2(20 BYTE) NULL ,
"LONGITUDE" VARCHAR2(20 BYTE) NULL ,
"LATITUDE" VARCHAR2(20 BYTE) NULL ,
"ORG_CODE" VARCHAR2(32 BYTE) NULL ,
"CUR_TIME" VARCHAR2(20 BYTE) NULL ,
"ORG_SEQ" VARCHAR2(32 BYTE) NULL ,
"SEQUENCE" VARCHAR2(8 BYTE) NULL ,
"STATUS" VARCHAR2(20 BYTE) NULL ,
"CODE" VARCHAR2(20 BYTE) NULL ,
"ERROR_DESC" VARCHAR2(20 BYTE) NULL ,
"WORK_DISTANCE" VARCHAR2(20 BYTE) NULL ,
"SID" VARCHAR2(32 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."SEND_TEL_NO" IS '手机号码';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."LONGITUDE" IS '所在经度';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."LATITUDE" IS '所在纬度';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."ORG_CODE" IS '发送机构代码';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."CUR_TIME" IS '当前时间';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."ORG_SEQ" IS '发送机构密钥序列';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."SEQUENCE" IS '新增序列号';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."STATUS" IS '状态码';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."CODE" IS '错误状态码';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."ERROR_DESC" IS '错误原因';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."WORK_DISTANCE" IS '工作地距离';
COMMENT ON COLUMN "DATA_FOUNTAIN"."ZZ_GXMINWORK"."SID" IS '主键';

-- ----------------------------
-- Records of ZZ_GXMINWORK
-- ----------------------------
INSERT INTO "DATA_FOUNTAIN"."ZZ_GXMINWORK" VALUES (null, null, null, null, null, null, null, '1', '00', null, null, '0a73dd8e2cf94745a565de7158613859');
INSERT INTO "DATA_FOUNTAIN"."ZZ_GXMINWORK" VALUES (null, null, null, null, null, null, null, '1', '00', null, null, '93ff4c30b9324bdcbc68d6142a20c890');

-- ----------------------------
-- Indexes structure for table ZZ_GXMINWORK
-- ----------------------------

-- ----------------------------
-- Checks structure for table ZZ_GXMINWORK
-- ----------------------------
ALTER TABLE "DATA_FOUNTAIN"."ZZ_GXMINWORK" ADD CHECK ("SID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table ZZ_GXMINWORK
-- ----------------------------
ALTER TABLE "DATA_FOUNTAIN"."ZZ_GXMINWORK" ADD PRIMARY KEY ("SID");
