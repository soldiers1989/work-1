-- 先更新sqlid_NBH003为sqlid_NBH007,sqlid_NBH004为sqlid_NBH008
update REPORT_FILE_DATA_RULES set SEQ_NO='7', SQL_ID='sqlid_NBH007' where APPLICATION_ID='AML-NBH' and SEQ_NO='3' and SQL_ID='sqlid_NBH003';
update REPORT_FILE_DATA_RULES set SEQ_NO='8', SQL_ID='sqlid_NBH008' where APPLICATION_ID='AML-NBH' and SEQ_NO='4' and SQL_ID='sqlid_NBH004';
-- 再插入sqlid_NBH003，sqlid_NBH004，sqlid_NBH005，sqlid_NBH006，sqlid_NBH009
insert into REPORT_FILE_DATA_RULES (APPLICATION_ID,SEQ_NO,RULE_TYPE,DATASET_NAME,SQL_ID,BEAN_ID,METHOD_NAME,DATA_RULE_DESCN,IS_SINGLE_DATA) VALUES ('AML-NBH','3','sql','CTNTs','sqlid_NBH003',NULL,NULL,NULL,NULL);
insert into REPORT_FILE_DATA_RULES (APPLICATION_ID,SEQ_NO,RULE_TYPE,DATASET_NAME,SQL_ID,BEAN_ID,METHOD_NAME,DATA_RULE_DESCN,IS_SINGLE_DATA) VALUES ('AML-NBH','4','sql','CCTLs','sqlid_NBH004',NULL,NULL,NULL,NULL);
insert into REPORT_FILE_DATA_RULES (APPLICATION_ID,SEQ_NO,RULE_TYPE,DATASET_NAME,SQL_ID,BEAN_ID,METHOD_NAME,DATA_RULE_DESCN,IS_SINGLE_DATA) VALUES ('AML-NBH','5','sql','CTARs','sqlid_NBH005',NULL,NULL,NULL,NULL);
insert into REPORT_FILE_DATA_RULES (APPLICATION_ID,SEQ_NO,RULE_TYPE,DATASET_NAME,SQL_ID,BEAN_ID,METHOD_NAME,DATA_RULE_DESCN,IS_SINGLE_DATA) VALUES ('AML-NBH','6','sql','CCEIs','sqlid_NBH006',NULL,NULL,NULL,NULL);
insert into REPORT_FILE_DATA_RULES (APPLICATION_ID,SEQ_NO,RULE_TYPE,DATASET_NAME,SQL_ID,BEAN_ID,METHOD_NAME,DATA_RULE_DESCN,IS_SINGLE_DATA) VALUES ('AML-NBH','9','sql','ROTFs','sqlid_NBH009',NULL,NULL,NULL,NULL);



