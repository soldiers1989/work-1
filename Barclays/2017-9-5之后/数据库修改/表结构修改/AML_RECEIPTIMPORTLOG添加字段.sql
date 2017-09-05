-- RECT_TIME   Varchar(8)  更正时限    新增
alter table AML_RECEIPTIMPORTLOG add  RECT_TIME varchar(8);
-- RECT_REQ    Varchar(1000)   更正要求    新增
alter table AML_RECEIPTIMPORTLOG add  RECT_REQ varchar(1000);
-- RECT_CONTENT    Varchar(1000)   更正内容    新增
alter table AML_RECEIPTIMPORTLOG add  RECT_CONTENT varchar(1000);
