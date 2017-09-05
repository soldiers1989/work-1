update TB_DICT 
set META_NAME = '对报告个例不适用、因报告机构自身业务系统原因暂时无法得到或因银联、支付结算系统等国家金融基础设施及国际支付清算体系原因无法得到的数据' 
WHERE GROUP_CODE = '1301' AND META_VAL = '@N'