-- 根据现有账户类型和原有账户类型比对，将原有账户类型代码替换为新的账户类型代码
update BI_OTHERACCOUNT set ACCT_TYPE='' where ACCT_TYPE='1203'
update BI_OTHERACCOUNT set ACCT_TYPE='' where ACCT_TYPE='1208'
