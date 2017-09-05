-- 根据现有对手方账户类型和原有对手账户件类型比对，将原有对手方账户类型代码替换为新的对手方账户类型代码。
update BI_OTHERACCOUNT set CTPY_ACCT_TYPE='' where CTPY_ACCT_TYPE='1203'
update BI_OTHERACCOUNT set CTPY_ACCT_TYPE='' where CTPY_ACCT_TYPE='1208'
