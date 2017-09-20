-- BS_SEQNO    Varchar(10) 可疑交易在上报报文中的序号   新增
alter table AML_ANALYSISRESULT add  BS_SEQNO varchar(10);
-- BS_Frequency   Varchar(2)  可疑报告上报次数    新增
alter table AML_ANALYSISRESULT add  BS_Frequency varchar(2);
-- RECT_TIME   Varchar(8)  更正时限    新增
alter table AML_ANALYSISRESULT add  RECT_TIME varchar(8);
-- RECT_REQ    Varchar(1000)   更正要求    新增
alter table AML_ANALYSISRESULT add  RECT_REQ varchar(1000);
-- RECT_CONTENT    Varchar(1000)   更正内容    新增
alter table AML_ANALYSISRESULT add  RECT_CONTENT varchar(1000);
-- SERIAL_NUM_4    Varchar(8)   SERIAL_NUM_4    新增
alter table AML_ANALYSISRESULT add  SERIAL_NUM_4 varchar(8);
