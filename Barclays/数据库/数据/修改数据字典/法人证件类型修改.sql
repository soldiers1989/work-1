update TB_DICT 
set META_NAME = '对报告个例不适用、因报告机构自身业务系统原因暂时无法得到或因银联、支付结算系统等国家金融基础设施及国际支付清算体系原因无法得到的数据' 
WHERE GROUP_CODE = '1304' AND META_VAL = '@N'

update TB_DICT 
set META_VAL = '110001',
	META_NAME = '居民身份证'
WHERE GROUP_CODE = '1304' AND META_VAL = '11'

update TB_DICT 
set META_VAL = '110007',
	META_NAME = '中国人民解放军军人身份证件'
WHERE GROUP_CODE = '1304' AND META_VAL = '12'

update TB_DICT 
set META_VAL = '110019' 
WHERE GROUP_CODE = '1304' AND META_VAL = '13'

update TB_DICT 
set META_VAL = '110027',
	META_NAME = '外国护照'
WHERE GROUP_CODE = '1304' AND META_VAL = '14'

update TB_DICT 
set META_VAL = '119999',
	META_NAME = '其他类境内个人身份有效证件'
WHERE GROUP_CODE = '1304' AND META_VAL = '19'