-- CLIENT_IC_TYPE  Varchar(6)  客户证件类型  修改  
alter table AML_DATASOURCE alter column CLIENT_IC_TYPE varchar(6);

-- CLIENT_IC_OTHER_TYPE    Varchar(6)  客户其它证件类型    新增  
alter table AML_DATASOURCE add CLIENT_IC_OTHER_TYPE varchar(6);

-- BANK_RELATIONSHIP   Varchar(2)  客户与金融机构的关系  新增  
alter table AML_DATASOURCE add BANK_RELATIONSHIP varchar(2);

-- REP_IC_TYPE Varchar(6)  客户法定代表人身份证件/证明文件类型  修改  
alter table AML_DATASOURCE alter column REP_IC_TYPE varchar(6);

-- REP_IC_OTHER_TYPE   Varchar(6)  客户法定代表人其他身份证件/证明文件类型    新增  
alter table AML_DATASOURCE add REP_IC_OTHER_TYPE varchar(6);

-- REP_OTHER_NAME Varchar(32) 客户控股股东或实际控制人名称  新增  
alter table AML_DATASOURCE add REP_OTHER_NAME varchar(32);

-- REP_OTHER_IC_NO  Varchar(20) 客户控股股东或实际控制人身份证件/证明文件号码 新增  
alter table AML_DATASOURCE add REP_OTHER_IC_NO varchar(20);

-- REP_OTHER_IC_TYPE  Varchar(6)  客户控股股东或实际控制人身份证件/证明文件类型 新增  
alter table AML_DATASOURCE add REP_OTHER_IC_TYPE varchar(6);

-- REP_OTHER_IC_OTHER_TYPE Varchar(6)  客户控股股东或实际控制人其他身份证件/证明文件类型   新增  
alter table AML_DATASOURCE add REP_OTHER_IC_OTHER_TYPE varchar(6);

-- CARD_TYPE   Varchar(2)  客户银行卡类型 新增  
alter table AML_DATASOURCE add CARD_TYPE varchar(2);

-- CARD_OTHER_TYPE Varchar(2)  客户其他银行卡类型   新增  
alter table AML_DATASOURCE add CARD_OTHER_TYPE varchar(2);

-- CARD_NO Varchar(64) 客户银行卡号  新增  
alter table AML_DATASOURCE add CARD_NO varchar(64);

-- ACCT_TYPE   Varchar(8)  账户类型    修改  
alter table AML_DATASOURCE alter column ACCT_TYPE varchar(8);

-- CTPY_IC_TYPE    Varchar(6)  交易对手身份证件/证明文件类型 修改  
alter table AML_DATASOURCE alter column CTPY_IC_TYPE varchar(6);

-- CTPY_IC_OTHER_TYPE  Varchar(6)  交易对手其他身份证件/证明文件类型   新增  
alter table AML_DATASOURCE add CTPY_IC_OTHER_TYPE varchar(6);

-- CTPY_ACCT_TYPE  Varchar(8)  交易对手账号类型    修改  
alter table AML_DATASOURCE alter column CTPY_ACCT_TYPE varchar(8);

-- AGENT_IC_TYPE   Varchar(6)  代办人身份证件/证明文件类型  修改  
alter table AML_DATASOURCE alter column AGENT_IC_TYPE varchar(6);

-- AGENT_IC_OTHER_TYPE Varchar(6)  代办人其他身份证件/证明文件类型    新增  
alter table AML_DATASOURCE add AGENT_IC_OTHER_TYPE varchar(6);

-- REC_PAY_TYPE    Varchar(2)  收付款方匹配号类型   新增  
alter table AML_DATASOURCE add REC_PAY_TYPE varchar(2);

-- REC_PAY_NO  Varchar(120)    收付款方匹配号 新增  
alter table AML_DATASOURCE add REC_PAY_NO varchar(120);

-- TRADE_UNCOUNTER_TYPE    Varchar(2)  非柜台交易方式 新增  
alter table AML_DATASOURCE add TRADE_UNCOUNTER_TYPE varchar(2);

-- TRADE_UNCOUNTER_OTHER_TYPE  Varchar(2)  其他非柜台交易方式   新增  
alter table AML_DATASOURCE add TRADE_UNCOUNTER_OTHER_TYPE varchar(2);

-- TRADE_UNCOUNTER_NO  Varchar(120)    非柜台交易方式的设备代码    新增  
alter table AML_DATASOURCE add TRADE_UNCOUNTER_NO varchar(120);

-- TRADE_BPTC  Varchar(64) 银行与支付机构之间的业务交易编码    新增  
alter table AML_DATASOURCE add TRADE_BPTC varchar(64);

-- RMB_AMT numeric(21, 4)  交易金额（折人民币）  新增  
alter table AML_DATASOURCE add RMB_AMT numeric(21, 4);

-- REMARK1 Varchar(120)    交易信息备注 1    新增  
alter table AML_DATASOURCE add REMARK1 varchar(120);

-- REMARK2 Varchar(120)    交易信息备注 2    新增  
alter table AML_DATASOURCE add REMARK2 varchar(120);  
