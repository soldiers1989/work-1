-- BS_DETR Varchar(2)  可疑交易报告紧急程度  新增  
alter table AML_ANALYSISRESULT add BS_DETR varchar(2);

-- BS_TORP Varchar(6)  报送次数标志  新增  
alter table AML_ANALYSISRESULT add BS_TORP varchar(6);

-- BS_ORXN Varchar(64) 初次报送的可疑交易报告报文名称 新增  
alter table AML_ANALYSISRESULT add BS_ORXN varchar(64);

-- BS_DORP Varchar(2)  报送方向    新增  
alter table AML_ANALYSISRESULT add BS_DORP varchar(2);

-- BS_ODRP Varchar(6)  其他报送方向  新增  
alter table AML_ANALYSISRESULT add BS_ODRP varchar(6);

-- BS_TPTR    Varchar(32) 可疑交易报告触发点   新增  
alter table AML_ANALYSISRESULT add BS_TPTR varchar(32);

-- BS_OTPR    Varchar(100)    其他可疑交易报告触发点 新增  
alter table AML_ANALYSISRESULT add BS_OTPR varchar(100);

-- BS_STCB Varchar(1000)   资金交易及客户行为情况 新增  
alter table AML_ANALYSISRESULT add BS_STCB varchar(1000);

-- BS_AOSP Varchar(1000)   疑点分析    新增  
alter table AML_ANALYSISRESULT add BS_AOSP varchar(1000);

-- BS_TOSC Varchar(4)  疑似涉罪类型  新增  
alter table AML_ANALYSISRESULT add BS_TOSC varchar(4);

-- BS_MIRS Varchar(64) 人工补正标识  新增  
alter table AML_ANALYSISRESULT add BS_MIRS varchar(64);

