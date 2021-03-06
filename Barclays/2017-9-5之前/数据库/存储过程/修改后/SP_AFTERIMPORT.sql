USE [SOAR]
GO
/****** Object:  StoredProcedure [dbo].[SP_AFTERIMPORT]    Script Date: 2017/8/30 9:52:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[SP_AFTERIMPORT]
@DEPARTID varchar(20), @WORKDATE varchar(8), @TABLE_NAME varchar(128), @FILE_NAME varchar(128), @BATCH_NO varchar(2), @ERR_CODE int OUTPUT, @ERR_MSG varchar(200) OUTPUT
WITH EXEC AS CALLER
AS
BEGIN
   --参数定义
  DECLARE @MAX_BACKDATE_LIMIT INT;           --交易期限回退时间限额
  DECLARE @REF_NO             VARCHAR(64);   --业务标识
  DECLARE @IS_NET_SETTLE      VARCHAR(1);    --是否差额清算
  DECLARE @IS_UNWIND          VARCHAR(1);    --是否反向平仓
  DECLARE @TENOR              VARCHAR(2);    --交易期限
  DECLARE @FX_REF             VARCHAR(64);   --FX业务标识
  DECLARE @EXERCISE_AMT       NUMERIC(21,4); --FX交易金额
  DECLARE @IS_DEL             VARCHAR(1);    --删除标志
  DECLARE @DEL_INFO           VARCHAR(256);  --删除信息
  DECLARE @STATUS             VARCHAR(20);   --交易状态
  DECLARE @TRADE_DATE         VARCHAR(8);    --交易日期   
  DECLARE @VALUE_DATE         VARCHAR(8);    --起息日期     
  DECLARE @REF_LEN            INTEGER;       --原始REF长度 
  DECLARE @DATETIME           VARCHAR(14);   --系统时间
  DECLARE @YEAR               VARCHAR(4);    --年 
  DECLARE @MONTH              VARCHAR(2);    --月
  DECLARE @DAY                VARCHAR(2);    --日
  DECLARE @HOUR               VARCHAR(2);    --时  
  DECLARE @MINUTE             VARCHAR(2);    --分
  DECLARE @SECOND             VARCHAR(2);    --秒 
  DECLARE @COUNT              INT;           --记录数 
  DECLARE @CALENDARNO varchar(100) --日历套 
  
  --参数初始化
  SET @ERR_CODE = 0;                
  SET @ERR_MSG = '';
  SET @MAX_BACKDATE_LIMIT = 0;
  SET @TABLE_NAME=UPPER(@TABLE_NAME);  
  SET @YEAR    =DATEPART(YYYY,GETDATE());
  SET @MONTH   =DATEPART(MM,GETDATE());  
  SET @DAY     =DATEPART(DD,GETDATE());  
  SET @HOUR    =DATEPART(HH,GETDATE());    
  SET @MINUTE  =DATEPART(MI,GETDATE());      
  SET @SECOND  =DATEPART(SS,GETDATE());   
  
  --获取当前系统时间
  --月
  IF LEN(@MONTH)<2 
  BEGIN
	SET @MONTH='0'+@MONTH
  END
  --日
  IF LEN(@DAY)<2 
  BEGIN
	SET @DAY='0'+@DAY
  END
  --时
  IF LEN(@HOUR)<2 
  BEGIN
	SET @HOUR='0'+@HOUR
  END
  --分
  IF LEN(@MINUTE)<2 
  BEGIN
	SET @MINUTE='0'+@MINUTE
  END    
  --秒
  IF LEN(@SECOND)<2 
  BEGIN
	SET @SECOND='0'+@SECOND
  END   
  --日期
  SET @DATETIME= @YEAR+@MONTH+@DAY+@HOUR+@MINUTE+@SECOND
  
  SELECT @CALENDARNO = PARAM_VAL FROM TB_SYSPARAMS WHERE PARAMGROUP_ID = '0004' AND PARAM_ID = '0001'
  
  PRINT @DATETIME
  
  
  BEGIN TRY  
  -----------------------------FOX业务文件处理-----------------------------
	IF @TABLE_NAME='FXO_DATASOURCE' 
	BEGIN
		--开始事务
		BEGIN TRAN
  
		-------------删除标识、反平仓标识、差额清算标识、记录状态初始化----------
		UPDATE FXO_DATASOURCE
		SET IS_DEL        = '0',
			IS_UNWIND     = '0',
			IS_NET_SETTLE = '0',
			MAKE_STATUS   = '0',
			CHECK_STATUS  = '0'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
    
		-----------------------把文件中币种由CNO更新为CNY--------------------------
  
		--Currency更新
		UPDATE FXO_DATASOURCE
			SET A_CUR = 'CNY'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND A_CUR = 'CNO'
    
		--currency1G更新
		UPDATE FXO_DATASOURCE
			SET B_CUR = 'CNY'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND B_CUR = 'CNO'
    
		-------------------------反向平仓标识(Unwind Flag)赋值处理-----------------
		UPDATE FXO_DATASOURCE
			SET IS_UNWIND = '1'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND (PACKAGE_ID IS NULL OR PACKAGE_ID = '0')
			AND BUY_SELL = 'B'
	  
		-----------------------外汇期权交易期限判断--------------------------
  
		--获取交易期限回退时间限额参数值
		SELECT @MAX_BACKDATE_LIMIT = PARAM_VAL
			FROM TB_SYSPARAMS
		WHERE PARAMGROUP_ID = '1000'
			AND PARAM_ID = '0001'
     
		--期权交易期限计算更新
		UPDATE FXO_DATASOURCE SET TENOR = CASE
				WHEN (CONVERT(VARCHAR(8),
                       DATEADD(DD, 0, DATEADD(MM, 3, TRADE_DATE)),
                       112) >= EXPIRY_DATE) THEN
				'01'	--3个月（含）以下
				WHEN (CONVERT(VARCHAR(8),
                       DATEADD(DD, 0, DATEADD(MM, 3, TRADE_DATE)),
                       112) < EXPIRY_DATE) AND
				(CONVERT(VARCHAR(8),
                       DATEADD(DD, 0, DATEADD(MM, 6, TRADE_DATE)),
                       112) >= EXPIRY_DATE) THEN
				'02'	--3个月以上至6个月（含）以下
				WHEN (CONVERT(VARCHAR(8),
                       DATEADD(DD, 0, DATEADD(MM, 6, TRADE_DATE)),
                       112) < EXPIRY_DATE) AND
				(CONVERT(VARCHAR(8),
                       DATEADD(DD, 0, DATEADD(MM, 12, TRADE_DATE)),
                       112) >= EXPIRY_DATE) THEN
				'03'	--6个月以上至1年（含）以下
				WHEN (CONVERT(VARCHAR(8),
                       DATEADD(DD, 0, DATEADD(MM, 12, TRADE_DATE)),
                       112) < EXPIRY_DATE) THEN
				'04'	--1年以上
			END
			WHERE WORK_DATE=@WORKDATE  
				AND DEPART_ID=@DEPARTID    
  
		-----------------------美元金额计算赋值----------------------------------
		--外币币种为USD
		UPDATE FXO_DATASOURCE
			SET DOLLAR_AMT = CASE WHEN A_CUR = 'USD' THEN A_AMT ELSE B_AMT END
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND (A_CUR = 'USD' OR B_CUR = 'USD')
      
		--外币币种为非USD
		UPDATE FXO_DATASOURCE
			SET DOLLAR_AMT = CASE
				WHEN A_CUR = 'CNY' THEN
				B_AMT * DBO.GETMONTHRATE(B_CUR, WORK_DATE)
				ELSE
				A_AMT * DBO.GETMONTHRATE(A_CUR, WORK_DATE)
			END
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND A_CUR <> 'USD'
			AND B_CUR <> 'USD'
   
		----------------------删除业务标识处理-------------------------------
		UPDATE FXO_DATASOURCE
			SET IS_DEL = '1'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(SUCCESSDING_TRADE_REFERENCE, '') <> '0'
      
		----------------------删除Cancelled业务------------------------------
		DELETE FXO_DATASOURCE
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(STATUS, '') = 'Cancelled'
      
		---------------------继承前一工作日的补录数据------------------------
  
		--定义前以工作日业务数据查询游标
		DECLARE CUR_FXO_LASTDATA CURSOR FOR 
		SELECT T1.REF_NO,
			   T2.IS_NET_SETTLE,
			   IS_UNWIND,
               T2.TENOR,
               T2.FX_REF,
               T2.EXERCISE_AMT,
               T2.IS_DEL,
               T2.DEL_INFO,
               T1.STATUS,
               T1.TRADE_DATE,
               T1.VALUE_DATE
        FROM (SELECT REF_NO, TRADE_DATE, VALUE_DATE, STATUS
				FROM FXO_DATASOURCE
				WHERE WORK_DATE = @WORKDATE
					  AND DEPART_ID = @DEPARTID) T1
		INNER JOIN (SELECT REF_NO,
						   IS_NET_SETTLE,
                           IS_UNWIND,
                           TENOR,
                           FX_REF,
                           EXERCISE_AMT,
                           IS_DEL,
                           DEL_INFO,
                           STATUS,
                           TRADE_DATE,
                           VALUE_DATE
					FROM FXO_DATASOURCE
					WHERE WORK_DATE = (SELECT MAX(WORK_DATE)
                                   FROM FXO_DATASOURCE
                                  WHERE WORK_DATE < @WORKDATE)
					 AND DEPART_ID = @DEPARTID) T2 ON T1.REF_NO = T2.REF_NO
                                             AND T1.TRADE_DATE =
                                                 T2.TRADE_DATE
                                             AND T1.VALUE_DATE =
                                                 T2.VALUE_DATE
		
		--打开游标		
		OPEN CUR_FXO_LASTDATA
   
		--取下一条数据
		FETCH NEXT FROM CUR_FXO_LASTDATA INTO 
				@REF_NO,@IS_NET_SETTLE,@IS_UNWIND,@TENOR,@FX_REF,@EXERCISE_AMT,@IS_DEL,@DEL_INFO,@STATUS,@TRADE_DATE,@VALUE_DATE
        
		WHILE @@FETCH_STATUS = 0
		BEGIN
			IF (@STATUS='Live' OR @STATUS='Expired') OR (@STATUS='Exercised' AND ISNULL(@FX_REF,'')<>'' AND ISNULL(@EXERCISE_AMT,0)<>0)
			BEGIN
				UPDATE FXO_DATASOURCE
				SET IS_NET_SETTLE=@IS_NET_SETTLE,
					IS_UNWIND=@IS_UNWIND,
					TENOR=@TENOR,
					--modify by wzf 20150505 begin
					--fox中不再保存CNYFX编号,只在CNYFX中保留FXO编号
					--FX_REF=@FX_REF,
					--modify by wzf 20150505 end
					EXERCISE_AMT=@EXERCISE_AMT,
					IS_DEL=@IS_DEL,
					DEL_INFO=@DEL_INFO,
					MAKE_STATUS='1',
					MAKER='baradmin',
					MAKE_TIME=@DATETIME,
					CHECK_STATUS='1',
					CHECKER='baradmin',
					CHECK_TIME=@DATETIME
				WHERE WORK_DATE=@WORKDATE
	  				AND DEPART_ID = @DEPARTID
	  				AND REF_NO = @REF_NO
	  				AND TRADE_DATE= @TRADE_DATE
					AND VALUE_DATE= @VALUE_DATE
			END
			ELSE
			BEGIN
				UPDATE FXO_DATASOURCE
				SET IS_NET_SETTLE=@IS_NET_SETTLE,
					IS_UNWIND=@IS_UNWIND,
					TENOR=@TENOR,
					--modify by wzf 20150505 begin
					--fox中不再保存CNYFX编号,只在CNYFX中保留FXO编号
					--FX_REF=@FX_REF,
					--modify by wzf 20150505 end
					EXERCISE_AMT=@EXERCISE_AMT,
					IS_DEL=@IS_DEL,
					DEL_INFO=@DEL_INFO
				WHERE WORK_DATE=@WORKDATE
	  				AND DEPART_ID = @DEPARTID
	  				AND REF_NO = @REF_NO	
	  				AND TRADE_DATE= @TRADE_DATE
	  				AND VALUE_DATE= @VALUE_DATE	  		 
			END
	  	
			--错误事务处理
			SET @ERR_CODE=@@ERROR
			IF @ERR_CODE !=0 
			BEGIN
				SET @ERR_MSG= 'sp_afterimport:' + dbo.GetErrorMessage(@ERR_CODE,'S')
				ROLLBACK TRAN      --回滚事务
				RETURN	
			END  
     
			--参数初始化	 
			SET @REF_NO=''
			SET @IS_NET_SETTLE=''
			SET @IS_UNWIND=''
			SET @TENOR=''
			SET @FX_REF='' 
			SET @EXERCISE_AMT=0
			SET @IS_DEL=''
			SET @DEL_INFO=''
			SET @STATUS=''
			SET @TRADE_DATE=''
			SET @VALUE_DATE=''
	 
			--取下一条数据
			FETCH NEXT FROM CUR_FXO_LASTDATA INTO 
				@REF_NO,@IS_NET_SETTLE,@IS_UNWIND,@TENOR,@FX_REF,@EXERCISE_AMT,@IS_DEL,@DEL_INFO,@STATUS,@TRADE_DATE,@VALUE_DATE	  
		END
   
		--关闭并解除分配游标
		CLOSE CUR_FXO_LASTDATA
		DEALLOCATE CUR_FXO_LASTDATA
   
		 --提交事务
		 COMMIT TRAN
	END
   

	-----------------------------AML业务文件处理AMLSHRFI文件----------------------------- 
	IF @TABLE_NAME='AML_DATASOURCE' AND @FILE_NAME = 'AMLSHRFI'
	BEGIN
		--开始事务
		BEGIN TRAN
  
		-----------------------------记录状态初始化------------------------------   
		UPDATE AML_DATASOURCE
			SET IS_DEL        = '0',
				MAKE_STATUS   = '0',
				CHECK_STATUS  = '0'
		WHERE WORK_DATE = @WORKDATE
		  AND DEPART_ID = @DEPARTID
		  AND IMP_FILE = @FILE_NAME
		  
		---------------------------交易类型默认处理--------------------------------
		--交易类型默认为02-转账
		UPDATE AML_DATASOURCE
			SET TRADE_TYPE= '02'   --转账
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(TRADE_TYPE,'')='' 
			AND IMP_FILE = @FILE_NAME
			
		---------------------------掉期业务标识默认处理--------------------------------
		--掉期业务标识默认为0-否
		UPDATE AML_DATASOURCE
			SET IS_SWAP= '0'   --否
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(IS_SWAP,'')='' 
			AND IMP_FILE = @FILE_NAME  
			
		--add by wzf 20140616 begin
		--根据swap_code更新is_swap标志
		UPDATE AML_DATASOURCE
			SET IS_SWAP= '1'   --是
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(SWAP_CODE,'') <> ''
			AND IMP_FILE = @FILE_NAME		
		--add by wzf 20140616 end	
		
		---------交易发生地为空时默认“310115”-上海市浦东新区-----------------------
		--add by wjx 20170606 begin
		UPDATE AML_DATASOURCE
			SET TRADE_VENUE_COUNTRY= 'CHN',
			    TRADE_VENUE_REGION = '310115'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND (ISNULL(TRADE_VENUE_COUNTRY,'') = ''
			AND ISNULL(TRADE_VENUE_REGION,'') = '')
			or (ISNULL(TRADE_VENUE_COUNTRY,'') = 'CHN'
			AND ISNULL(TRADE_VENUE_REGION,'') = '')
			AND IMP_FILE = @FILE_NAME	
		--add by wjx20170607 end	
		
		---------------------------交易信息备注默认处理-------------------------------
		--add by lhb 20170830 begin
		--交易信息备注默认为@N
		UPDATE AML_DATASOURCE
			SET REMARK1 = '@N',
				REMARK2 = '@N'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND IMP_FILE = @FILE_NAME  
		--add by lhb 20170830 end
		-----------------------------AML业务收付标识更新------------------------------   
		--------如果客户账户为NOSTRO账户(账户以"NOST"开头)，其收付标志置反------------
		--------业务状态为CANCELLED，其收付标识置反-----------------------------------
		UPDATE AML_DATASOURCE
			SET DEBIT_CREDIT = CASE
				WHEN DEBIT_CREDIT = '01' THEN
				'02'
				ELSE
				'01'
			END
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND (SUBSTRING(ISNULL(ACCT_ID,''),1,4)='NOST' OR UPPER(STATUS)='CANCELLED')
			AND IMP_FILE = @FILE_NAME
    
		-----------------------------金融机构网点信息更新-----------------------------
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.DEPART_NAME=TB_BRANCH.DEPART_NAME,
				AML_DATASOURCE.DEPART_TYPE=TB_BRANCH.DEPART_TYPE,
	   			AML_DATASOURCE.DEPART_AREACODE=TB_BRANCH.AREA_CODE,	
	   			AML_DATASOURCE.TRADE_VENUE_REGION=TB_BRANCH.AREA_CODE,
	   			AML_DATASOURCE.TRADE_VENUE_COUNTRY='CHN'
			FROM TB_BRANCH
			WHERE AML_DATASOURCE.DEPART_ID=TB_BRANCH.DEPART_CODE
				AND AML_DATASOURCE.WORK_DATE = @WORKDATE
				AND AML_DATASOURCE.DEPART_ID = @DEPARTID
				AND AML_DATASOURCE.IMP_FILE = @FILE_NAME
            
		--------------------------------客户信息更新--------------------------------
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.CLIENT_NAME=BI_CUSTOMER.NAME,
				AML_DATASOURCE.CLIENT_TYPE=BI_CUSTOMER.CLIENT_TYPE,
				AML_DATASOURCE.CLIENT_IC_TYPE=BI_CUSTOMER.IC_TYPE,
				AML_DATASOURCE.CLIENT_IC_TYPE_MEMO=BI_CUSTOMER.IC_TYPE_MEMO,
				AML_DATASOURCE.CLIENT_IC_NO=BI_CUSTOMER.IC_NO,
				AML_DATASOURCE.CLIENT_NATIONALITY=BI_CUSTOMER.NATIONALITY,
				AML_DATASOURCE.PHONE=BI_CUSTOMER.PHONE,
				AML_DATASOURCE.ADDRESS=BI_CUSTOMER.ADDRESS,
				AML_DATASOURCE.OTHER_CONTACT=BI_CUSTOMER.OTHER_CONTACT,
				AML_DATASOURCE.INDUSTRY_TYPE=BI_CUSTOMER.INDUSTRY_TYPE,
				AML_DATASOURCE.REGISTERED_CAPITAL=BI_CUSTOMER.REGISTERED_CAPITAL,
				AML_DATASOURCE.REP_NAME=BI_CUSTOMER.REP_NAME,
				AML_DATASOURCE.REP_IC_TYPE=BI_CUSTOMER.REP_IC_TYPE,
				AML_DATASOURCE.REP_IC_TYPE_MEMO=BI_CUSTOMER.REP_IC_TYPE_MEMO,	       
				AML_DATASOURCE.REP_IC_NO=BI_CUSTOMER.REP_IC_NO,
				AML_DATASOURCE.PCODE=BI_CUSTOMER.PCODE,
				--------------------客户信息更新新增start-2017-8-30 lhb----------------------
				AML_DATASOURCE.CLIENT_IC_OTHER_TYPE=BI_CUSTOMER.IC_OTHER_TYPE,
				AML_DATASOURCE.REP_IC_OTHER_TYPE=BI_CUSTOMER.REP_IC_OTHER_TYPE,
				AML_DATASOURCE.REP_OTHER_NAME=BI_CUSTOMER.REP_OTHER_NAME,
				AML_DATASOURCE.REP_OTHER_IC_NO=BI_CUSTOMER.REP_OTHER_IC_NO,
				AML_DATASOURCE.REP_OTHER_IC_TYPE=BI_CUSTOMER.REP_OTHER_IC_TYPE,
				AML_DATASOURCE.REP_OTHER_IC_OTHER_TYPE=BI_CUSTOMER.REP_OTHER_IC_OTHER_TYPE,
				AML_DATASOURCE.BANK_RELATIONSHIP=BI_CUSTOMER.BANK_RELATIONSHIP
				--------------------客户信息更新新增end-------------------------------------
			FROM BI_CUSTOMER
			WHERE AML_DATASOURCE.CLIENT_ID=BI_CUSTOMER.CLIENT_ID
				AND AML_DATASOURCE.WORK_DATE = @WORKDATE
				AND AML_DATASOURCE.DEPART_ID = @DEPARTID
				AND AML_DATASOURCE.IMP_FILE = @FILE_NAME
 
		--------------------------------账户信息更新--------------------------------   
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.ACCT_TYPE=BI_ACCOUNT.ACCT_TYPE,
				AML_DATASOURCE.ACCT_OPEN_TIME=BI_ACCOUNT.OPEN_DATE,
				AML_DATASOURCE.ACCT_CLOSE_TIME=BI_ACCOUNT.CLOSE_DATE,
				AML_DATASOURCE.PBOC_NUM_ACCT = CASE     
				             WHEN isnull(BI_ACCOUNT.PBOC_NUM_ACCT,'')=''THEN
								''  
							 WHEN isnull(BI_ACCOUNT.PBOC_NUM_ACCT,'')<>'' AND 
							      subString(BI_ACCOUNT.ACCT_ID,1,3)='NRA'  THEN
								 'NRA'+BI_ACCOUNT.PBOC_NUM_ACCT  
							 WHEN isnull(BI_ACCOUNT.PBOC_NUM_ACCT,'') <> '' AND 
							      subString(BI_ACCOUNT.ACCT_ID,1,3)<>'NRA' 	 THEN
							      BI_ACCOUNT.PBOC_NUM_ACCT 
						     END
			FROM BI_ACCOUNT
		WHERE AML_DATASOURCE.ACCT_ID=BI_ACCOUNT.ACCT_ID
				AND AML_DATASOURCE.WORK_DATE = @WORKDATE
				AND AML_DATASOURCE.DEPART_ID = @DEPARTID	
				AND AML_DATASOURCE.IMP_FILE = @FILE_NAME
                          
		-----------------------------对手金融机构信息更新-----------------------------
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.CTPY_FI_NAME=BI_OTHERORGANIZATION.CTPY_FI_NAME,
				AML_DATASOURCE.CTPY_FI_TYPE=BI_OTHERORGANIZATION.CTPY_FI_TYPE,
				AML_DATASOURCE.CTPY_FI_COUNTRY=BI_OTHERORGANIZATION.CTPY_FI_COUNTRY,
				AML_DATASOURCE.CTPY_FI_REGION_CODE=BI_OTHERORGANIZATION.CTPY_FI_REGION_CODE,
				AML_DATASOURCE.CTPY_FI_PCODE=BI_OTHERORGANIZATION.CTPY_FI_PCODE
			FROM BI_OTHERORGANIZATION
		WHERE AML_DATASOURCE.CTPY_FI_CODE=BI_OTHERORGANIZATION.CTPY_FI_CODE
			AND AML_DATASOURCE.CTPY_FI_CODE <> '' AND AML_DATASOURCE.CTPY_FI_CODE IS NOT NULL
			AND AML_DATASOURCE.CTPY_FI_CODE <> '@N' AND AML_DATASOURCE.CTPY_FI_CODE <> '@E' AND AML_DATASOURCE.CTPY_FI_CODE <> '@I'   
			AND AML_DATASOURCE.WORK_DATE = @WORKDATE
			AND AML_DATASOURCE.DEPART_ID = @DEPARTID
			AND AML_DATASOURCE.IMP_FILE = @FILE_NAME	   
   
		--------------------------------美元金额计算----------------------------------
		UPDATE AML_DATASOURCE
				SET  DOLLAR_AMT=AMT*DBO.GETMONTHRATE(CURRENCY,TRADE_DATE)
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID    
			AND IMP_FILE = @FILE_NAME
			
		--------------------------------人民币金额计算-2017-8-30 lhb------------------
		UPDATE AML_DATASOURCE
				SET  RMB_AMT=AMT*DBO.GETMONTHRATE_RMB(CURRENCY,TRADE_DATE)
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID    
			AND IMP_FILE = @FILE_NAME
   
		--------------------------------REF_NO处理----------------------------------    
		--删除旧REF_NO信息数据
		DELETE FROM ID_REFNO_MAPPING    
		WHERE WORK_DATE = @WORKDATE
      AND MODULE = '1'

		--把处理后的REF_NO信息插入ID_REFNO_MAPPING表--------------------------------   
		INSERT INTO ID_REFNO_MAPPING
			(WORK_DATE, OLD_REF_NO, ORIG_REF_NO, NEW_REF_NO, MODULE)
		SELECT WORK_DATE,
				RTRIM(SUBSTRING(REF_NO, 1, 14)) AS OLD_REF_NO,
				REF_NO,
				RTRIM(SUBSTRING(REF_NO, 1, 14)) + SUBSTRING(WORK_DATE, 3, 6) +
				SUBSTRING('000' +
						CAST(ROW_NUMBER() OVER(ORDER BY REF_NO) AS VARCHAR(8)),
						LEN('000' +
							CAST(ROW_NUMBER() OVER(ORDER BY REF_NO) AS VARCHAR(8))) - 3,
						4) AS NEW_REFNO,'1' AS MODULE
		FROM AML_DATASOURCE
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID 
			AND IMP_FILE = @FILE_NAME
        
		--更新AML_DATASOURCE表的REF_NO--------------------------------------------------
  
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.REF_NO=ID_REFNO_MAPPING.NEW_REF_NO, 
				AML_DATASOURCE.ORIG_REF_NO=ID_REFNO_MAPPING.OLD_REF_NO
		FROM ID_REFNO_MAPPING
		WHERE AML_DATASOURCE.REF_NO=ID_REFNO_MAPPING.ORIG_REF_NO
			AND AML_DATASOURCE.WORK_DATE=ID_REFNO_MAPPING.WORK_DATE
			AND ID_REFNO_MAPPING.MODULE='1'
			AND AML_DATASOURCE.WORK_DATE = @WORKDATE
			AND AML_DATASOURCE.DEPART_ID = @DEPARTID
			AND AML_DATASOURCE.IMP_FILE = @FILE_NAME 
                      
		--提交事务
		COMMIT TRAN
	END
	
	
	-----------------------------AML业务文件处理AMLSHRFI_BCBS文件----------------------------- 
	IF @TABLE_NAME='AML_DATASOURCE' AND @FILE_NAME = 'AMLSHRFI_BCBS.CSV'
	BEGIN
		--开始事务
		BEGIN TRAN
  
		-----------------------------记录状态初始化------------------------------   
		UPDATE AML_DATASOURCE
			SET IS_DEL      = '0',
				MAKE_STATUS   = '0',
				CHECK_STATUS  = '0'
		WHERE WORK_DATE = @WORKDATE
		  AND DEPART_ID = @DEPARTID
		  AND IMP_FILE = @FILE_NAME
		  
		---------------------------交易类型默认处理--------------------------------
		--交易类型默认为02-转账
		UPDATE AML_DATASOURCE
			SET TRADE_TYPE= '02'   --转账
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(TRADE_TYPE,'')='' 
			AND IMP_FILE = @FILE_NAME
			
		---------------------------掉期业务标识默认处理--------------------------------
		--掉期业务标识默认为0-否
		UPDATE AML_DATASOURCE
			SET IS_SWAP= '0'   --否
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(IS_SWAP,'')=''   
			AND IMP_FILE = @FILE_NAME
			
		--add by wzf 20140616 begin
		--根据swap_code更新is_swap标志
		UPDATE AML_DATASOURCE
			SET IS_SWAP= '1'   --是
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(SWAP_CODE,'') <> ''
			AND IMP_FILE = @FILE_NAME		
		--add by wzf 20140616 end	
		
		---------交易发生地为空时默认“310115”-上海市浦东新区-----------------------
		--add by wjx 20170606 begin
		UPDATE AML_DATASOURCE
			SET TRADE_VENUE_COUNTRY= 'CHN',
			    TRADE_VENUE_REGION = '310115'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND (ISNULL(TRADE_VENUE_COUNTRY,'') = ''
			AND ISNULL(TRADE_VENUE_REGION,'') = '')
			or (ISNULL(TRADE_VENUE_COUNTRY,'') = 'CHN'
			AND ISNULL(TRADE_VENUE_REGION,'') = '')
			AND IMP_FILE = @FILE_NAME	
		
		--add by wjx20170607 end
			
		-----------------------------AML业务收付标识更新------------------------------   
		--------如果客户账户为NOSTRO账户(账户以"NOST"开头)，其收付标志置反------------
		--------业务状态为CANCELLED，其收付标识置反-----------------------------------
		UPDATE AML_DATASOURCE
			SET DEBIT_CREDIT = CASE
				WHEN DEBIT_CREDIT = '01' THEN
				'02'
				ELSE
				'01'
			END
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND (SUBSTRING(ISNULL(ACCT_ID,''),1,4)='NOST' OR UPPER(STATUS)='CANCELLED')
			AND IMP_FILE = @FILE_NAME
    
		-----------------------------金融机构网点信息更新-----------------------------
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.DEPART_NAME=TB_BRANCH.DEPART_NAME,
				AML_DATASOURCE.DEPART_TYPE=TB_BRANCH.DEPART_TYPE,
	   			AML_DATASOURCE.DEPART_AREACODE=TB_BRANCH.AREA_CODE,	
	   			AML_DATASOURCE.TRADE_VENUE_REGION=TB_BRANCH.AREA_CODE,
	   			AML_DATASOURCE.TRADE_VENUE_COUNTRY='CHN'
			FROM TB_BRANCH
			WHERE AML_DATASOURCE.DEPART_ID=TB_BRANCH.DEPART_CODE
				AND AML_DATASOURCE.WORK_DATE = @WORKDATE
				AND AML_DATASOURCE.DEPART_ID = @DEPARTID
				AND AML_DATASOURCE.IMP_FILE = @FILE_NAME
            
		--------------------------------客户信息更新--------------------------------
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.CLIENT_NAME=BI_CUSTOMER.NAME,
				AML_DATASOURCE.CLIENT_TYPE=BI_CUSTOMER.CLIENT_TYPE,
				AML_DATASOURCE.CLIENT_IC_TYPE=BI_CUSTOMER.IC_TYPE,
				AML_DATASOURCE.CLIENT_IC_TYPE_MEMO=BI_CUSTOMER.IC_TYPE_MEMO,
				AML_DATASOURCE.CLIENT_IC_NO=BI_CUSTOMER.IC_NO,
				AML_DATASOURCE.CLIENT_NATIONALITY=BI_CUSTOMER.NATIONALITY,
				AML_DATASOURCE.PHONE=BI_CUSTOMER.PHONE,
				AML_DATASOURCE.ADDRESS=BI_CUSTOMER.ADDRESS,
				AML_DATASOURCE.OTHER_CONTACT=BI_CUSTOMER.OTHER_CONTACT,
				AML_DATASOURCE.INDUSTRY_TYPE=BI_CUSTOMER.INDUSTRY_TYPE,
				AML_DATASOURCE.REGISTERED_CAPITAL=BI_CUSTOMER.REGISTERED_CAPITAL,
				AML_DATASOURCE.REP_NAME=BI_CUSTOMER.REP_NAME,
				AML_DATASOURCE.REP_IC_TYPE=BI_CUSTOMER.REP_IC_TYPE,
				AML_DATASOURCE.REP_IC_TYPE_MEMO=BI_CUSTOMER.REP_IC_TYPE_MEMO,	       
				AML_DATASOURCE.REP_IC_NO=BI_CUSTOMER.REP_IC_NO,
				AML_DATASOURCE.PCODE=BI_CUSTOMER.PCODE
			FROM BI_CUSTOMER
			WHERE AML_DATASOURCE.CLIENT_ID=BI_CUSTOMER.CLIENT_ID
				AND AML_DATASOURCE.WORK_DATE = @WORKDATE
				AND AML_DATASOURCE.DEPART_ID = @DEPARTID
				AND AML_DATASOURCE.IMP_FILE = @FILE_NAME
 
		--------------------------------账户信息更新--------------------------------   
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.ACCT_TYPE=BI_ACCOUNT.ACCT_TYPE,
				AML_DATASOURCE.ACCT_OPEN_TIME=BI_ACCOUNT.OPEN_DATE,
				AML_DATASOURCE.ACCT_CLOSE_TIME=BI_ACCOUNT.CLOSE_DATE,
				AML_DATASOURCE.PBOC_NUM_ACCT = CASE     
				             WHEN isnull(BI_ACCOUNT.PBOC_NUM_ACCT,'')=''THEN
								''  
							 WHEN isnull(BI_ACCOUNT.PBOC_NUM_ACCT,'')<>'' AND 
							      subString(BI_ACCOUNT.ACCT_ID,1,3)='NRA'  THEN
								 'NRA'+BI_ACCOUNT.PBOC_NUM_ACCT  
							 WHEN isnull(BI_ACCOUNT.PBOC_NUM_ACCT,'') <> '' AND 
							      subString(BI_ACCOUNT.ACCT_ID,1,3)<>'NRA' 	 THEN
							      BI_ACCOUNT.PBOC_NUM_ACCT 
						     END
			FROM BI_ACCOUNT
		WHERE AML_DATASOURCE.ACCT_ID=BI_ACCOUNT.ACCT_ID
				AND AML_DATASOURCE.WORK_DATE = @WORKDATE
				AND AML_DATASOURCE.DEPART_ID = @DEPARTID	
				AND AML_DATASOURCE.IMP_FILE = @FILE_NAME
                          
		-----------------------------对手金融机构信息更新-----------------------------
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.CTPY_FI_NAME=BI_OTHERORGANIZATION.CTPY_FI_NAME,
				AML_DATASOURCE.CTPY_FI_TYPE=BI_OTHERORGANIZATION.CTPY_FI_TYPE,
				AML_DATASOURCE.CTPY_FI_COUNTRY=BI_OTHERORGANIZATION.CTPY_FI_COUNTRY,
				AML_DATASOURCE.CTPY_FI_REGION_CODE=BI_OTHERORGANIZATION.CTPY_FI_REGION_CODE,
				AML_DATASOURCE.CTPY_FI_PCODE=BI_OTHERORGANIZATION.CTPY_FI_PCODE
			FROM BI_OTHERORGANIZATION
		WHERE AML_DATASOURCE.CTPY_FI_CODE=BI_OTHERORGANIZATION.CTPY_FI_CODE
			AND AML_DATASOURCE.CTPY_FI_CODE <> '' AND AML_DATASOURCE.CTPY_FI_CODE IS NOT NULL
			AND AML_DATASOURCE.CTPY_FI_CODE <> '@N' AND AML_DATASOURCE.CTPY_FI_CODE <> '@E' AND AML_DATASOURCE.CTPY_FI_CODE <> '@I'   
			AND AML_DATASOURCE.WORK_DATE = @WORKDATE
			AND AML_DATASOURCE.DEPART_ID = @DEPARTID	
			AND AML_DATASOURCE.IMP_FILE = @FILE_NAME   
   
		--------------------------------美元金额计算----------------------------------
		UPDATE AML_DATASOURCE
				SET  DOLLAR_AMT=AMT*DBO.GETMONTHRATE(CURRENCY,TRADE_DATE)
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID   
			AND IMP_FILE = @FILE_NAME 
   
		--------------------------------REF_NO处理----------------------------------    
		--删除旧REF_NO信息数据
		DELETE FROM ID_REFNO_MAPPING    
		WHERE WORK_DATE = @WORKDATE
      AND MODULE = '4'

		--把处理后的REF_NO信息插入ID_REFNO_MAPPING表--------------------------------   
		INSERT INTO ID_REFNO_MAPPING
			(WORK_DATE, OLD_REF_NO, ORIG_REF_NO, NEW_REF_NO, MODULE)
		SELECT WORK_DATE,
				RTRIM(SUBSTRING(REF_NO, 1, 16)) AS OLD_REF_NO,
				REF_NO,
				'BCBS-'+RTRIM(SUBSTRING(REF_NO, 1, 16)) + SUBSTRING(WORK_DATE, 3, 6) +
				SUBSTRING('000' +
						CAST(ROW_NUMBER() OVER(ORDER BY REF_NO) AS VARCHAR(8)),
						LEN('000' +
							CAST(ROW_NUMBER() OVER(ORDER BY REF_NO) AS VARCHAR(8))) - 3,
						4) AS NEW_REFNO,'4' AS MODULE
		FROM AML_DATASOURCE
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID 
			AND IMP_FILE = @FILE_NAME 
        
		--更新AML_DATASOURCE表的REF_NO--------------------------------------------------
  
		UPDATE AML_DATASOURCE
			SET AML_DATASOURCE.REF_NO=ID_REFNO_MAPPING.NEW_REF_NO, 
				AML_DATASOURCE.ORIG_REF_NO=ID_REFNO_MAPPING.OLD_REF_NO,
        ACCT_ID = SUBSTRING(ISNULL(ACCT_ID,''),5,7) + LEFT(ISNULL(ACCT_ID,''),4) + RIGHT(ISNULL(ACCT_ID,''),10)
		FROM ID_REFNO_MAPPING
		WHERE AML_DATASOURCE.REF_NO=ID_REFNO_MAPPING.ORIG_REF_NO
			AND AML_DATASOURCE.WORK_DATE=ID_REFNO_MAPPING.WORK_DATE
			AND ID_REFNO_MAPPING.MODULE='4'
			AND AML_DATASOURCE.WORK_DATE = @WORKDATE
			AND AML_DATASOURCE.DEPART_ID = @DEPARTID 
			AND AML_DATASOURCE.IMP_FILE = @FILE_NAME 
                      
		--提交事务
		COMMIT TRAN
	END
    
	-----------------------------CNYFX业务文件处理----------------------------- 
	IF @TABLE_NAME='FX_DATASOURCE_ORI' 
	BEGIN  
		BEGIN TRAN
		DECLARE @CNYFX_REFNO	  VARCHAR(32)
		DECLARE @SWAP_NO		  VARCHAR(32)
		DECLARE @CONTRACT_TYPE1   VARCHAR(8)
		DECLARE @BUY_CCY1		  VARCHAR(3)
		DECLARE @BUY_AMT1		  NUMERIC(21)
		DECLARE @SELL_CCY1		  VARCHAR(3)
		DECLARE @SELL_AMT1		  NUMERIC(21)	
		DECLARE @TRADE_DIRECTION1 VARCHAR(2)	
		DECLARE @TRADE_DATE1      VARCHAR(8)
		DECLARE @CONTRACT_TYPE2   VARCHAR(8)
		DECLARE @BUY_CCY2		  VARCHAR(3)
		DECLARE @BUY_AMT2		  NUMERIC(21)
		DECLARE @SELL_CCY2	      VARCHAR(3)
		DECLARE @SELL_AMT2		  NUMERIC(21)	
		DECLARE @TRADE_DIRECTION2 VARCHAR(2)
		DECLARE @TRADE_DATE2      VARCHAR(8)		
		DECLARE @SWAP_STRUCTURE_TYPE VARCHAR(2)
		DECLARE @DOLLAR_AMT       NUMERIC(21)
		DECLARE @SWAP_CATEGORY   VARCHAR(2)
		DECLARE @VALUE_DATE1 VARCHAR(8)
		DECLARE @VALUE_DATE2 VARCHAR(8)
				
		DECLARE @COUNT_FLAG  INT
		
		SET @CNYFX_REFNO=''
		SET @SWAP_NO=''
		SET @SWAP_STRUCTURE_TYPE=''
		SET @DOLLAR_AMT=0
	    ------------------把原始数据插入FX_DATASOURDE表中------------------------
	    --定义掉期业务查询游标
		DECLARE CUR_CNYFX_DATA CURSOR FOR 
		SELECT REF_NO
			FROM FX_DATASOURCE_ORI
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			
		--打开游标		
		OPEN CUR_CNYFX_DATA	 
		
		--取下一条数据
		FETCH NEXT FROM CUR_CNYFX_DATA INTO @CNYFX_REFNO
		--循环变量游标
		WHILE @@FETCH_STATUS = 0
		BEGIN
		    --判断该REFNO是否存在  
			SELECT @COUNT_FLAG = COUNT(*)
				FROM FX_DATASOURCE
		    WHERE REF_NO = @CNYFX_REFNO
		    
		    --不存在处理
		    IF @COUNT_FLAG=0 
		    BEGIN
				INSERT INTO FX_DATASOURCE
					(WORK_DATE,
					 DEPART_ID,
					 REF_NO,
					 TRADE_DATE,
					 VALUE_DATE,
					 STATUS,
					 CONTRACT_TYPE,
					 BUY_CCY,
				     BUY_AMT,
					 SELL_CCY,
					 SELL_AMT,
					 SWAP_NO,
					 CLIENT_ID,
					 PCODE,
					 IS_BANK,
					 SPOT_RATE)
				SELECT WORK_DATE,
					   DEPART_ID,
					   REF_NO,
					   TRADE_DATE,
					   VALUE_DATE,
					   STATUS,
					   CONTRACT_TYPE,
					   BUY_CCY,
					   BUY_AMT,
					   SELL_CCY,
					   SELL_AMT,
					   SWAP_NO,
					   CLIENT_ID,
					   PCODE,
					   IS_BANK,
					   cast(SPOT_RATE as numeric(13, 8))
				FROM FX_DATASOURCE_ORI
				WHERE WORK_DATE = @WORKDATE
					 AND DEPART_ID = @DEPARTID
					 AND REF_NO=@CNYFX_REFNO
            END
            ELSE
            --存在处理
            BEGIN
				UPDATE FX_DATASOURCE
				SET WORK_DATE = @WORKDATE,
					TRADE_DATE=T.TRADE_DATE,
					VALUE_DATE=T.VALUE_DATE,
					STATUS=T.STATUS,
					CONTRACT_TYPE=T.CONTRACT_TYPE,
					BUY_CCY=T.BUY_CCY,
					BUY_AMT=T.BUY_AMT,
					SELL_CCY=T.SELL_CCY,
					SELL_AMT=T.SELL_AMT,
					SWAP_NO=T.SWAP_NO,
					CLIENT_ID=T.CLIENT_ID,
					PCODE=T.PCODE,
					IS_BANK=T.IS_BANK,
					SPOT_RATE = cast(T.SPOT_RATE as numeric(13, 8))
				FROM (SELECT TOP 1 
				       REF_NO,
				       TRADE_DATE,
					   VALUE_DATE,
					   STATUS,
					   CONTRACT_TYPE,
					   BUY_CCY,
					   BUY_AMT,
					   SELL_CCY,
					   SELL_AMT,
					   SWAP_NO,
					   CLIENT_ID,
					   PCODE,
					   IS_BANK,
					   SPOT_RATE
                     FROM FX_DATASOURCE_ORI
                     WHERE WORK_DATE = @WORKDATE
                           AND DEPART_ID = @DEPARTID
                           AND REF_NO = @CNYFX_REFNO) T
				WHERE FX_DATASOURCE.REF_NO = T.REF_NO	
				    AND FX_DATASOURCE.REF_NO = @CNYFX_REFNO
					AND FX_DATASOURCE.DEPART_ID = @DEPARTID				
		    END
		    
		    PRINT @CNYFX_REFNO
			--参数初始化	
			SET @CNYFX_REFNO=''			
			--取下一条数据
			FETCH NEXT FROM CUR_CNYFX_DATA INTO @CNYFX_REFNO		    
		END
		
		--关闭并解除分配游标
		CLOSE CUR_CNYFX_DATA
		DEALLOCATE CUR_CNYFX_DATA	
			
	    -----------------------------记录状态初始化------------------------------   
		UPDATE FX_DATASOURCE
			SET IS_DEL        = '0',   --删除标识
				MAKE_STATUS   = '0',   --补录状态
				CHECK_STATUS  = '0',   --审核状态
				TRADE_DIRECTION =CASE      --结售汇标志
				             WHEN BUY_CCY='CNY'  AND SELL_CCY<>'CNY' THEN
								'02'  --售汇
							 WHEN BUY_CCY<>'CNY' AND SELL_CCY ='CNY' THEN
								'01'  --结汇
						     END							     				  
		WHERE WORK_DATE = @WORKDATE
		  AND DEPART_ID = @DEPARTID	
				  
		----------------------------------客户信息更新---------------------------------------
		UPDATE FX_DATASOURCE
			SET FX_DATASOURCE.CLIENT_NAME=BI_CUSTOMER.NAME,                  --客户名称
				FX_DATASOURCE.FX_CLIENT_TYPE=BI_CUSTOMER.FX_CLIENT_TYPE,     --结售汇客户类型
	            FX_DATASOURCE.CLIENT_ZONE =BI_CUSTOMER.BONDED_ZONE_TYPE,     --客户保税区
				FX_DATASOURCE.PCODE=BI_CUSTOMER.PCODE 
			FROM BI_CUSTOMER
			WHERE FX_DATASOURCE.CLIENT_ID=BI_CUSTOMER.CLIENT_ID
				AND FX_DATASOURCE.WORK_DATE = @WORKDATE
				AND FX_DATASOURCE.DEPART_ID = @DEPARTID		
		
	    -------------------------掉期结构类型赋值处理--------------------------
	    --定义掉期业务查询游标
		DECLARE CUR_CNYFX_SWAP CURSOR FOR 
		SELECT DISTINCT SWAP_NO
			FROM FX_DATASOURCE
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND ISNULL(SWAP_NO, '') <> ''
			AND ISNULL(STATUS,'') <>'CANCELLED'
			
		--打开游标		
		OPEN CUR_CNYFX_SWAP		
		    
		--取下一条数据
		FETCH NEXT FROM CUR_CNYFX_SWAP INTO @SWAP_NO		
		--循环变量游标
		WHILE @@FETCH_STATUS = 0
		BEGIN
			--获取掉期业务信息
			DECLARE CUR_CNYFX_SWAP_DETL CURSOR FOR 
			SELECT CONTRACT_TYPE,BUY_CCY,BUY_AMT,SELL_CCY,SELL_AMT,TRADE_DIRECTION,TRADE_DATE,VALUE_DATE
				FROM FX_DATASOURCE
			WHERE DEPART_ID = @DEPARTID
				AND SWAP_NO = @SWAP_NO
				AND ISNULL(IS_DEL,'') <>'1'
				AND ISNULL(STATUS,'') <>'CANCELLED'
				AND CONTRACT_TYPE IN ('FWRD','SPOT')
				AND ((TRADE_DIRECTION='01' AND SELL_CCY='CNY') OR (TRADE_DIRECTION='02' AND BUY_CCY='CNY'))
			ORDER BY VALUE_DATE 
			
			--打开游标		
			OPEN CUR_CNYFX_SWAP_DETL		
				
			SET @CONTRACT_TYPE1=''			
			SET @BUY_CCY1=''
			SET @BUY_AMT1=0
			SET @SELL_CCY1=''
			SET @SELL_AMT1=0	
			SET @TRADE_DIRECTION1=''	
			SET @TRADE_DATE1=''
			SET @CONTRACT_TYPE2=''
			SET @BUY_CCY2=''
			SET @BUY_AMT2=0
			SET @SELL_CCY2=''
			SET @SELL_AMT2=0
			SET @TRADE_DIRECTION2=''	
			SET @TRADE_DATE2=''
			
			SET @SWAP_CATEGORY = ''
			SET @VALUE_DATE1 = ''
			SET @VALUE_DATE1 = ''
						
			--取出掉期业务数据
			FETCH NEXT FROM CUR_CNYFX_SWAP_DETL INTO @CONTRACT_TYPE1,@BUY_CCY1,@BUY_AMT1,@SELL_CCY1,@SELL_AMT1,@TRADE_DIRECTION1,@TRADE_DATE1,@VALUE_DATE1
			FETCH NEXT FROM CUR_CNYFX_SWAP_DETL INTO @CONTRACT_TYPE2,@BUY_CCY2,@BUY_AMT2,@SELL_CCY2,@SELL_AMT2,@TRADE_DIRECTION2,@TRADE_DATE2,@VALUE_DATE2
				
			CLOSE CUR_CNYFX_SWAP_DETL
			DEALLOCATE CUR_CNYFX_SWAP_DETL			
						
			--判断掉期结构类型 		  
			IF @TRADE_DIRECTION1='01'
			BEGIN
			    --结汇对售汇
				SET @SWAP_STRUCTURE_TYPE='01'	
			END
			ELSE
			BEGIN
			    --售汇对结汇
				SET @SWAP_STRUCTURE_TYPE='02'					
			END  
			
		--add 20140717 begin	
		--判断掉期交易的期限结构
		IF @CONTRACT_TYPE1 = 'SPOT' OR @CONTRACT_TYPE2 = 'SPOT' --两条交易中有一条是即期，那么就是“即期对远期”
		BEGIN
			IF @CONTRACT_TYPE1 = 'SPOT'--如果第一条是即期,那么判断第二条的交易期限
			BEGIN
				IF dateadd(yyyy,1,@TRADE_DATE2) < cast(@VALUE_DATE2 as datetime) --交易签订日期加一年,如果小于value_date，则是一年以上
					SET @SWAP_CATEGORY = '02' --即期对一年以上
				ELSE
					SET @SWAP_CATEGORY = '01' --即期对一年(含)以下
			END
			ELSE
			IF @CONTRACT_TYPE2 = 'SPOT'--如果第二条是即期,那么判断第一条的交易期限
			BEGIN
				IF dateadd(yyyy,1,@TRADE_DATE1) < cast(@VALUE_DATE1 as datetime) --交易签订日期加一年,如果小于value_date，则是一年以上
					SET @SWAP_CATEGORY = '02' --即期对一年以上
				ELSE
					SET @SWAP_CATEGORY = '01' --即期对一年（含）以下
			END
		END
		else --如果两条都不是即期，那么就是“远期对远期”，这时应先判断哪个是近端哪个是远端
		BEGIN
			IF @VALUE_DATE1 < @VALUE_DATE2 --交易1的value_date小于交易2，那么交易1为近端
			BEGIN
				IF (dateadd(yyyy,1,@TRADE_DATE1) > cast(@VALUE_DATE1 as datetime)) and --一年以下对一年(含)以下
					(dateadd(yyyy,1,@TRADE_DATE2) >= cast(@VALUE_DATE2 as datetime))
					SET @SWAP_CATEGORY = '03'
				ELSE
				IF (dateadd(yyyy,1,@TRADE_DATE1) >= cast(@VALUE_DATE1 as datetime)) and --一年(含)以下对一年以上
					(dateadd(yyyy,1,@TRADE_DATE2) < cast(@VALUE_DATE2 as datetime)) 
					SET @SWAP_CATEGORY = '05'
				ELSE
				IF (dateadd(yyyy,1,@TRADE_DATE1) < cast(@VALUE_DATE1 as datetime)) and --一年以上对一年以上
					(dateadd(yyyy,1,@TRADE_DATE2) < cast(@VALUE_DATE2 as datetime))
					SET @SWAP_CATEGORY = '07'
			END
			ELSE
			IF @VALUE_DATE1 > @VALUE_DATE2 --交易1的value_date大于交易2，那么交易2是近端
			BEGIN
				IF (dateadd(yyyy,1,@TRADE_DATE2) > cast(@VALUE_DATE2 as datetime)) and --一年以下对一年(含)以下
					(dateadd(yyyy,1,@TRADE_DATE1) >= cast(@VALUE_DATE1 as datetime))
					SET @SWAP_CATEGORY = '03'
				ELSE
				IF (dateadd(yyyy,1,@TRADE_DATE2) >= cast(@VALUE_DATE2 as datetime)) and --一年(含)以下对一年以上
					(dateadd(yyyy,1,@TRADE_DATE1) < cast(@VALUE_DATE1 as datetime)) 
					SET @SWAP_CATEGORY = '05'
				ELSE
				IF (dateadd(yyyy,1,@TRADE_DATE2) < cast(@VALUE_DATE2 as datetime)) and --一年以上对一年以上
					(dateadd(yyyy,1,@TRADE_DATE1) < cast(@VALUE_DATE1 as datetime))
					SET @SWAP_CATEGORY = '07'
			END
		END
		--add 20140717 end	
		    
		    --计算美元金额
		    IF @BUY_AMT1=@SELL_AMT2
		    BEGIN
                --如果交易1的买入金额和交易2的卖出金额一样，说明交易1的买入金额（或者交易2的卖出金额）是基准数据		    
				SET @DOLLAR_AMT=@BUY_AMT1 * DBO.GETMONTHRATE(@BUY_CCY1,@TRADE_DATE1)
		    END 
		    ELSE IF @SELL_AMT1=@BUY_AMT2
		    BEGIN
		        --如果交易1的卖出金额和交易2的买入金额一样，说明交易1的卖出金额（或者交易2的买入金额）是基准数据					
				SET @DOLLAR_AMT=@SELL_AMT1 * DBO.GETMONTHRATE(@SELL_CCY1,@TRADE_DATE1)
		    END 
		    ELSE
		    BEGIN
				--如果没有找到基准数据，以第一条数据的买入金额计算	
				SET @DOLLAR_AMT=@BUY_AMT1 * DBO.GETMONTHRATE(@BUY_CCY1,@TRADE_DATE1)				
		    END
		    
		    --更新掉期数据信息
		    UPDATE FX_DATASOURCE
				SET SWAP_STRUCTURE_TYPE=@SWAP_STRUCTURE_TYPE,
					DOLLAR_AMT=@DOLLAR_AMT,
					SWAP_CATEGORY = @SWAP_CATEGORY
			WHERE DEPART_ID = @DEPARTID
				AND SWAP_NO = @SWAP_NO
				AND ISNULL(IS_DEL,'') <>'1'
				AND ISNULL(STATUS,'') <>'CANCELLED'
				AND CONTRACT_TYPE IN ('FWRD','SPOT')
		    
		    --更新掉期冲正金额	
		    UPDATE FX_DATASOURCE
				SET DOLLAR_AMT= CASE
				--结汇冲正
                WHEN TRADE_DIRECTION='01' THEN
					-SELL_AMT * DBO.GETMONTHRATE(SELL_CCY, TRADE_DATE)	
				WHEN TRADE_DIRECTION='02' THEN	
					-BUY_AMT * DBO.GETMONTHRATE(BUY_CCY, TRADE_DATE)					
				END	
			WHERE DEPART_ID = @DEPARTID
				AND SWAP_NO = @SWAP_NO
				AND ISNULL(IS_DEL,'') <>'1'
				AND ISNULL(STATUS,'') <>'CANCELLED'
				AND CONTRACT_TYPE IN ('FWRD','SPOT')
				AND ((TRADE_DIRECTION='01' AND BUY_CCY='CNY') OR (TRADE_DIRECTION='02' AND SELL_CCY='CNY'))		
			    	    		    
			--参数初始化	
			SET @SWAP_NO=''
			--取下一条数据
			FETCH NEXT FROM CUR_CNYFX_SWAP INTO @SWAP_NO		
		END
		--关闭并解除分配游标
		CLOSE CUR_CNYFX_SWAP
		DEALLOCATE CUR_CNYFX_SWAP		
				         
        ------------------------------远期期限分类赋值处理--------------------------     
        UPDATE FX_DATASOURCE 
			SET  TENOR =
					CASE
					WHEN DATEDIFF(DD, TRADE_DATE, VALUE_DATE) <= 7 THEN
					'01'    --7天（含）以下
					WHEN DATEDIFF(DD, TRADE_DATE, VALUE_DATE) > 7 AND
						CONVERT(VARCHAR(8), DATEADD(MM, 1, TRADE_DATE), 112) > =
						VALUE_DATE THEN
					'02'    --7天以上到1个月（含）以下
					WHEN CONVERT(VARCHAR(8), DATEADD(MM, 1, TRADE_DATE), 112) <
						VALUE_DATE AND
						CONVERT(VARCHAR(8), DATEADD(MM, 3, TRADE_DATE), 112) > =
						VALUE_DATE THEN
					'03'     --1个月以上到3个月（含）以下
					WHEN CONVERT(VARCHAR(8), DATEADD(MM, 3, TRADE_DATE), 112) <
						VALUE_DATE AND
						CONVERT(VARCHAR(8), DATEADD(MM, 6, TRADE_DATE), 112) > =
						VALUE_DATE THEN
					'04'     --3个月以上到6个月（含）以下
					 WHEN CONVERT(VARCHAR(8), DATEADD(MM, 6, TRADE_DATE), 112) <
						VALUE_DATE AND
						CONVERT(VARCHAR(8), DATEADD(MM, 9, TRADE_DATE), 112) > =
						VALUE_DATE THEN
					'05'     --6个月以上到9个月（含）以下
					WHEN CONVERT(VARCHAR(8), DATEADD(MM, 9, TRADE_DATE), 112) <
						VALUE_DATE AND
						CONVERT(VARCHAR(8), DATEADD(MM, 12, TRADE_DATE), 112) > =
						VALUE_DATE THEN
					'06'      --9个月以上到1年（含）以下
					WHEN CONVERT(VARCHAR(8), DATEADD(MM, 12, TRADE_DATE), 112) <
						VALUE_DATE THEN
					'07' END  --1年以上
        WHERE WORK_DATE = @WORKDATE
              AND DEPART_ID = @DEPARTID					
              AND CONTRACT_TYPE LIKE 'FWRD%'	 --远期交易	
              
        --------------------------------美元金额计算----------------------------------
        --非掉期业务的美元金额计算
		UPDATE FX_DATASOURCE
				SET  DOLLAR_AMT=CASE
		 --结汇		
         WHEN SELL_CCY = 'CNY' AND TRADE_DIRECTION='01' THEN
			BUY_AMT * DBO.GETMONTHRATE(BUY_CCY, TRADE_DATE)
		 --结汇冲正
         WHEN BUY_CCY  = 'CNY' AND TRADE_DIRECTION='01' THEN
			-SELL_AMT * DBO.GETMONTHRATE(SELL_CCY, TRADE_DATE)	
		 --售汇			
         WHEN BUY_CCY  = 'CNY' AND TRADE_DIRECTION='02' THEN
			SELL_AMT * DBO.GETMONTHRATE(SELL_CCY, TRADE_DATE)
	     --售汇冲正		
         WHEN SELL_CCY  = 'CNY' AND TRADE_DIRECTION='02' THEN
			-BUY_AMT * DBO.GETMONTHRATE(BUY_CCY, TRADE_DATE)			
        END
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID 
			AND ISNULL(SWAP_NO,'')=''
			AND ISNULL(STATUS,'') <>'CANCELLED'
					
		--结售汇合同类型更新-远期违约冲正
		UPDATE FX_DATASOURCE
			SET CONTRACT_TYPE='FWRDDEF'	
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID 
			AND ISNULL(SWAP_NO,'')<>''
			AND DOLLAR_AMT<0
			AND ISNULL(STATUS,'') <>'CANCELLED' 		
        
        --对于银行客户的业务标识成删除，除集团内的其他分行
        UPDATE FX_DATASOURCE
			SET IS_DEL='1', --已删除
			    DEL_INFO='interbank deals are auto-deleted',
			    MAKE_STATUS='1', --已补录
			    MAKER='system',
			    CHECK_STATUS='1', --已审核
			    CHECKER='system'
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID 
			AND IS_BANK = 'Y'         
			--除集团内其他分行
			AND (PCODE NOT IN (SELECT SUBSTRING(META_VAL,1,CHARINDEX ('|',META_VAL)-1) FROM TB_DICT   WHERE GROUP_CODE='2210')
					OR 	ISNULL(SWAP_NO,'')<>'')  
		
		--对于集团内的业务自动补充TRADE_TYPE
		UPDATE FX_DATASOURCE
			SET TRADE_PURPOSE= CASE 
			    WHEN TRADE_DIRECTION = '01' THEN
					T.B_TRADE_PURPOSE
				ELSE
					S_TRADE_PURPOSE
				END,					   
			    MAKE_STATUS='1', --已补录
			    MAKER='system',
			    CHECK_STATUS='1', --已审核
			    CHECKER='system'	
	   FROM (SELECT SUBSTRING(META_VAL,1,CHARINDEX ('|',META_VAL)-1) AS PCODE,           --PCODE
                    SUBSTRING(META_VAL,CHARINDEX ('|',META_VAL)+1,3) AS B_TRADE_PURPOSE, --结汇交易目的代码
                    SUBSTRING(META_VAL,CHARINDEX ('|',META_VAL)+5,3) AS S_TRADE_PURPOSE  --售汇交易目的代码
             FROM TB_DICT WHERE GROUP_CODE = '2210') T		
        WHERE	FX_DATASOURCE.PCODE=T.PCODE
				AND WORK_DATE = @WORKDATE
				AND DEPART_ID = @DEPARTID  
				AND IS_DEL    = '0'        --非删除业务
				AND ISNULL(SWAP_NO,'')=''  --非掉期业务    
				
		--add by wzf 20150505 begin
		--更新FXO_REF
		--有新版本的CNYFX导入,REFNO没变
		update FX_DATASOURCE SET FX_DATASOURCE.FXO_REFNO = B.FXO_REFNO 
		FROM (select REF_NO,FXO_REFNO,WORK_DATE from FX_DATASOURCE 
			   where WORK_DATE <> @WORKDATE
			     and isnull(FXO_REFNO,'') <> ''
				 and REF_NO in (select REF_NO from FX_DATASOURCE where WORK_DATE = @WORKDATE)) B
		WHERE FX_DATASOURCE.REF_NO = B.REF_NO 
		  AND FX_DATASOURCE.WORK_DATE <>  B.WORK_DATE
		  AND FX_DATASOURCE.WORK_DATE = @WORKDATE
		
		--有新版本的CNYFX导入,REFNO变动
		update FX_DATASOURCE SET FX_DATASOURCE.FXO_REFNO = C.FXO_REFNO 
		FROM (select a.REF_NO,b.FXO_REFNO
				  from FX_DATASOURCE_ORI a, FX_DATASOURCE b
				 where a.WORK_DATE = @WORKDATE
				   and isnull(a.PRECEDING_TRADE_REFERENCE,'') <> ''
				   and a.PRECEDING_TRADE_REFERENCE = b.REF_NO   
				   and ISNULL(b.FXO_REFNO,'') <> '') C
		WHERE FX_DATASOURCE.REF_NO = C.REF_NO
		--add by wzf 20150505 end		
						  
	    --提交事务
		COMMIT TRAN 	   
	END 

	-----------------------------日汇率业务文件处理-----------------------------
	IF @TABLE_NAME='BI_DAYEXCHANGERATE' 
	BEGIN
		--开始事务
		BEGIN TRAN
		    --判断汇率表中是否有CNY币种的汇率
			SELECT @COUNT = COUNT(0)
				FROM BI_DAYEXCHANGERATE
			WHERE RATE_DATE = @WORKDATE
				AND CUR_CODE = 'CNY'
			
			--如果不存在CNY币种的数据往日汇率表中插入一条CNY的汇率
			IF @COUNT=0 
			BEGIN	
				INSERT INTO BI_DAYEXCHANGERATE
					(RATE_DATE, CUR_CODE, CUR_UNIT, RATE_MIDPRICE)
				VALUES
					(@WORKDATE, 'CNY', 1, 1)
		    END
	    --提交事务
		COMMIT TRAN 		
	END	
			
	-----------------------------ABOQ账户信息文件处理-----------------------------
 	IF @TABLE_NAME='STAGING_ABOQT' 
	BEGIN
		--开始事务
		BEGIN TRAN
		DECLARE @ABOQT_TRADE_DATE       VARCHAR(8)
		DECLARE @ABOQT_ACCT_ID			VARCHAR(64)
		DECLARE @ABOQT_ACCT_CUR			VARCHAR(3)
		DECLARE @ABOQT_TRADE_DIRECTION  VARCHAR(2)
		DECLARE @ABOQT_AMOUNT           NUMERIC(21, 4)      
		
		------------------------历史收支余交易更新-----------------------------
		--初始化历史收支余额数据
		DECLARE CUR_ACCHIST_DATA CURSOR FOR 	
		SELECT DISTINCT TRADE_DATE,ACCT_ID,ACCT_CUR
			FROM STAGING_ABOQT 
		WHERE TRADE_DATE <  @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND WORK_DATE = @WORKDATE

		--打开游标		
		OPEN CUR_ACCHIST_DATA	 

		--取下一条数据
		FETCH NEXT FROM CUR_ACCHIST_DATA INTO @ABOQT_TRADE_DATE,@ABOQT_ACCT_ID,@ABOQT_ACCT_CUR
		--循环变量游标
		WHILE @@FETCH_STATUS = 0
		BEGIN	
			UPDATE ACC_CB_HIST
				SET CREDIT_AMT  = T.CREDIT_AMT,
					DEBIT_AMT   = T.DEBIT_AMT,
					BALANCE_AMT = T.BALANCE_AMT 
			FROM STAGING_ABOQA T
			WHERE ACC_CB_HIST.WORK_DATE   =  T.WORK_DATE
				AND ACC_CB_HIST.DEPART_ID =  T.DEPART_ID
				AND ACC_CB_HIST.ACCT_CUR  =  T.ACCT_CUR
				AND ACC_CB_HIST.ACCT_ID   =  T.ACCT_ID	
				AND ACC_CB_HIST.WORK_DATE >= @ABOQT_TRADE_DATE
			    AND ACC_CB_HIST.ACCT_CUR=	 @ABOQT_ACCT_CUR
			    AND ACC_CB_HIST.ACCT_ID=     @ABOQT_ACCT_ID				      
	        
	        SET @ABOQT_TRADE_DATE=''
	        SET @ABOQT_ACCT_ID=''
	        SET @ABOQT_ACCT_CUR=''
			FETCH NEXT FROM CUR_ACCHIST_DATA INTO @ABOQT_TRADE_DATE,@ABOQT_ACCT_ID,@ABOQT_ACCT_CUR		
		END		

		--关闭并解除分配游标
		CLOSE CUR_ACCHIST_DATA
		DEALLOCATE CUR_ACCHIST_DATA
		
		--参数初始化
	    SET @ABOQT_TRADE_DATE=''
	    SET @ABOQT_ACCT_ID=''
	    SET @ABOQT_ACCT_CUR=''	
	    				
		--定义历史收支余交易业务查询游标
		DECLARE CUR_ABOQT_DATA CURSOR FOR 
		SELECT TRADE_DATE,ACCT_ID,ACCT_CUR,TRADE_DIRECTION,ISNULL(AMOUNT,0) 
			FROM STAGING_ABOQT 
		WHERE TRADE_DATE <  @WORKDATE
			AND DEPART_ID = @DEPARTID
			AND WORK_DATE = @WORKDATE
			
		--打开游标		
		OPEN CUR_ABOQT_DATA	 
		
		--取下一条数据
		FETCH NEXT FROM CUR_ABOQT_DATA INTO @ABOQT_TRADE_DATE,@ABOQT_ACCT_ID,@ABOQT_ACCT_CUR,@ABOQT_TRADE_DIRECTION,@ABOQT_AMOUNT
		--循环变量游标
		WHILE @@FETCH_STATUS = 0
		BEGIN	
		    
		    --贷方发生额
			IF @ABOQT_TRADE_DIRECTION='CR' 
			BEGIN
				--对历史的收支余信息进行更新
				--发生当天贷方发生额更新
				UPDATE ACC_CB_HIST
				SET CREDIT_AMT = CREDIT_AMT + @ABOQT_AMOUNT 
				WHERE ACC_CB_HIST.ACCT_ID=@ABOQT_ACCT_ID
					AND ACC_CB_HIST.ACCT_CUR=@ABOQT_ACCT_CUR
					AND ACC_CB_HIST.WORK_DATE=@ABOQT_TRADE_DATE
					
			    --从交易发生日开始至处理日收支余额更新
				UPDATE ACC_CB_HIST
				SET BALANCE_AMT = BALANCE_AMT + @ABOQT_AMOUNT	
				WHERE  ACCT_ID=@ABOQT_ACCT_ID
					AND ACCT_CUR=@ABOQT_ACCT_CUR
					AND WORK_DATE>=@ABOQT_TRADE_DATE			      
			END
			
			--借方发生额
			IF @ABOQT_TRADE_DIRECTION='DR' 
			BEGIN
				--对历史的收支余信息进行更新
				--发生当天借方发生额更新
				UPDATE ACC_CB_HIST
				SET DEBIT_AMT = DEBIT_AMT + @ABOQT_AMOUNT 
				WHERE ACCT_ID=@ABOQT_ACCT_ID
					AND ACCT_CUR=@ABOQT_ACCT_CUR
					AND WORK_DATE=@ABOQT_TRADE_DATE	
				--从交易发生日开始至处理日收支余额更新
				UPDATE ACC_CB_HIST
				SET BALANCE_AMT = BALANCE_AMT - @ABOQT_AMOUNT
				WHERE ACCT_ID=@ABOQT_ACCT_ID
					AND ACCT_CUR=@ABOQT_ACCT_CUR
					AND WORK_DATE>=@ABOQT_TRADE_DATE							
			END
			
			--参数初始化
			SET @ABOQT_TRADE_DATE = ''	
			SET @ABOQT_ACCT_ID  = ''		
			SET @ABOQT_ACCT_CUR = ''
			SET @ABOQT_TRADE_DIRECTION=''
			SET @ABOQT_AMOUNT=0
			
			--取下一条数据
			FETCH NEXT FROM CUR_ABOQT_DATA INTO @ABOQT_TRADE_DATE,@ABOQT_ACCT_ID,@ABOQT_ACCT_CUR,@ABOQT_TRADE_DIRECTION,@ABOQT_AMOUNT
		END
		--关闭并解除分配游标
		CLOSE CUR_ABOQT_DATA
		DEALLOCATE CUR_ABOQT_DATA		
		--提交事务
		COMMIT TRAN	
	END	
	
	--因为该步更新需要用到ACC_CB_HIST的数据，所以要放到更新ACC_CB_HIST之后执行
	-----------------------------ABOQ账户信息文件处理-----------------------------
 	IF @TABLE_NAME='STAGING_ABOQA' 
	BEGIN
		--开始事务
		BEGIN TRAN
		
		--定义变量
		DECLARE @ACCT_ID		  VARCHAR(64)
		DECLARE @ACCT_CUR		  VARCHAR(3)	
		DECLARE @BALANCE_AMT_HIST NUMERIC(21, 4)
		DECLARE @CLOSE_DATE		  VARCHAR(64)
		
		
		------------------把原始数据插入BI_ACCOUNT表中------------------------
	    --定义掉期业务查询游标
		DECLARE CUR_ACC_DATA CURSOR FOR 
		SELECT ACCT_ID,ACCT_CUR
			FROM STAGING_ABOQA
		WHERE WORK_DATE = @WORKDATE
			AND DEPART_ID = @DEPARTID
			
		--打开游标		
		OPEN CUR_ACC_DATA	 
		
		--取下一条数据
		FETCH NEXT FROM CUR_ACC_DATA INTO @ACCT_ID,@ACCT_CUR
		--循环变量游标
		WHILE @@FETCH_STATUS = 0
		BEGIN
		    --判断该REFNO是否存在  
			SELECT @COUNT_FLAG = COUNT(*)
				FROM BI_ACCOUNT
		    WHERE ACCT_ID  = @ACCT_ID
			  AND ACCT_CUR = @ACCT_CUR
		
		    --获取上个工作日的账户收支余额
			SELECT @BALANCE_AMT_HIST = BALANCE_AMT
				FROM ACC_CB_HIST
			WHERE WORK_DATE IN
				(SELECT MAX(WORK_DATE) FROM ACC_CB_HIST WHERE WORK_DATE < @WORKDATE)
				AND DEPART_ID = @DEPARTID
				AND ACCT_ID   = @ACCT_ID
				AND ACCT_CUR  = @ACCT_CUR   
				   
		    --不存在处理
		    IF @COUNT_FLAG=0 
		    BEGIN
				INSERT INTO BI_ACCOUNT
					(DEPART_ID,
					 ACCT_ID,
					 ACCT_CUR,
					 CLIENT_ID,
					 ACCT_TYPE,
					 FX_ACCT_TYPE,
					 CLOSE_DATE,
					 OPEN_DATE,
				     ACCT_CATA,
					 LIMIT_TYPE,
					 FILE_NUM,
					 ACC_STATUS,
					 CREDIT_AMT,
					 DEBIT_AMT,
					 BALANCE_AMT,
					 SYS_CREDIT_AMT,
					 SYS_DEBIT_AMT,
					 SYS_BALANCE_AMT)
				SELECT DEPART_ID,
					   ACCT_ID,
					   ACCT_CUR,
					   CLIENT_ID,
					   '@N',   --因自身业务系统，无法得到
					   '1000', --经常项目-外汇结算账户
					   CASE WHEN ISNULL(CLOSE_DATE,'') = '' THEN '@N' ELSE CLOSE_DATE END,
					   OPEN_DATE,
					   '12', --现汇户
					   '11', --无限额
					   'NA',
					   case when @ACCT_CUR = 'CNY' or @ACCT_CUR = 'RMB' then '00' else '11' end, --开户(modify by wzf 20140915 如果是人民币账户开户时默认为不上报)
					   CREDIT_AMT,
					   DEBIT_AMT,
					   BALANCE_AMT,
					   CREDIT_AMT,
					   DEBIT_AMT,
					   CASE WHEN @BALANCE_AMT_HIST IS NULL THEN 
					            BALANCE_AMT
					        ELSE 
							    @BALANCE_AMT_HIST+CREDIT_AMT-DEBIT_AMT
					   END		    	        
				FROM STAGING_ABOQA
				WHERE WORK_DATE = @WORKDATE
					 AND DEPART_ID = @DEPARTID
					 AND ACCT_ID  = @ACCT_ID
					 AND ACCT_CUR = @ACCT_CUR
            END
            ELSE
            --存在处理
            BEGIN
				 --获取原先账号关户时间  
				SELECT @CLOSE_DATE = CLOSE_DATE
					FROM BI_ACCOUNT
				WHERE ACCT_ID  = @ACCT_ID
					AND ACCT_CUR = @ACCT_CUR
			
				UPDATE BI_ACCOUNT
				SET CLOSE_DATE = T.CLOSE_DATE,
					OPEN_DATE  = T.OPEN_DATE,
					ACC_STATUS = case  when T.CLOSE_DATE is not null and len(T.CLOSE_DATE)=8 and (@ACCT_CUR != 'CNY' and @ACCT_CUR != 'RMB')and len(@CLOSE_DATE)!=8 then '13'else '00' end, --add by pw 20151224 币种为外币，关闭时间不为空则闭户，否则不上报
					CREDIT_AMT = T.CREDIT_AMT,
					DEBIT_AMT  = T.DEBIT_AMT,
					BALANCE_AMT= T.BALANCE_AMT,
					SYS_CREDIT_AMT  = T.CREDIT_AMT,
					SYS_DEBIT_AMT   = T.DEBIT_AMT,
					SYS_BALANCE_AMT =CASE WHEN @BALANCE_AMT_HIST IS NULL THEN 
										 T.BALANCE_AMT
									 ELSE 
										 @BALANCE_AMT_HIST+T.CREDIT_AMT-T.DEBIT_AMT
					   END	
				FROM ( SELECT ACCT_ID,
				       ACCT_CUR,
				       CASE WHEN ISNULL(CLOSE_DATE,'') = '' THEN '@N' ELSE CLOSE_DATE END AS CLOSE_DATE,
				       OPEN_DATE,
				       CREDIT_AMT,
				       DEBIT_AMT,
				       BALANCE_AMT 
                     FROM STAGING_ABOQA
                     WHERE WORK_DATE  =  @WORKDATE
						AND DEPART_ID =  @DEPARTID
						AND ACCT_ID   =  @ACCT_ID
						AND ACCT_CUR  =  @ACCT_CUR) T
				WHERE BI_ACCOUNT.ACCT_ID = T.ACCT_ID
				    AND BI_ACCOUNT.ACCT_CUR=T.ACCT_CUR	
				    AND BI_ACCOUNT.ACCT_ID = @ACCT_ID
					AND BI_ACCOUNT.ACCT_CUR = @ACCT_CUR				
		    END
		    
			--参数初始化	
			SET @ACCT_ID  = ''		
			SET @ACCT_CUR = ''	
			
			--取下一条数据
			FETCH NEXT FROM CUR_ACC_DATA INTO @ACCT_ID,@ACCT_CUR    
		END
		
		--关闭并解除分配游标
		CLOSE CUR_ACC_DATA
		DEALLOCATE CUR_ACC_DATA
		
	    --提交事务	    
		COMMIT TRAN 
	END
  
  -----------------------------BOP文件处理-----------------------------  
  IF @TABLE_NAME='BOP_DATASOURCE' 
  BEGIN
    BEGIN TRAN
  
    DECLARE @BOPDOP VARCHAR(1)
    
    SELECT @BOPDOP = PARAM_VAL FROM TB_SYSPARAMS WHERE PARAMGROUP_ID = '1003' AND PARAM_ID = '0001'
  
    --字段处理     
    --删除旧REF_NO信息数据
		DELETE FROM ID_REFNO_MAPPING    
		WHERE WORK_DATE = @WORKDATE
      AND MODULE = '2'
    
    --把处理后的REF_NO信息插入ID_REFNO_MAPPING表--------------------------------   
		INSERT INTO ID_REFNO_MAPPING
			(WORK_DATE, OLD_REF_NO, ORIG_REF_NO, NEW_REF_NO, MODULE)
		SELECT @WORKDATE,
				RTRIM(SUBSTRING(REFERENCE_NO,1,14)) AS  OLD_REF_NO,
				REFERENCE_NO,
				RTRIM(dbo.fn_GetRefNoMap(@WORKDATE,SUBSTRING(REFERENCE_NO,1,14),'2')) AS NEW_REFNO,'2' AS MODULE
		FROM BOP_DATASOURCE
		WHERE WORK_DATE = @WORKDATE
    
    --更新BOP_DATASOURCE表的REFERENCE_NO--------------------------------------------------  
		UPDATE BOP_DATASOURCE
			SET BOP_DATASOURCE.REFERENCE_NO=ID_REFNO_MAPPING.NEW_REF_NO				
		FROM ID_REFNO_MAPPING
		WHERE BOP_DATASOURCE.REFERENCE_NO=ID_REFNO_MAPPING.ORIG_REF_NO
			AND BOP_DATASOURCE.WORK_DATE=ID_REFNO_MAPPING.WORK_DATE
			AND ID_REFNO_MAPPING.MODULE='2'
			AND BOP_DATASOURCE.WORK_DATE = @WORKDATE
          
    ---判断pay_rec设置对应的值,去掉REFERENCE_NO字段空格,将DOP字段中的-FX替换成|FX
    UPDATE BOP_DATASOURCE
       SET customer_code = CASE PAY_REC WHEN 'P' THEN REMITTER_SDSID WHEN 'R' THEN BENEFICIARY_SDSID ELSE '' END,
           opp_user1 = CASE PAY_REC WHEN 'P' THEN remitter1 WHEN 'R' THEN beneficiary1 ELSE '' END,
           opp_user2 = CASE PAY_REC WHEN 'P' THEN remitter2 WHEN 'R' THEN beneficiary2 ELSE '' END,
           opp_user3 = CASE PAY_REC WHEN 'P' THEN remitter3 WHEN 'R' THEN beneficiary3 ELSE '' END,
           opp_country = CASE WHEN charindex('CTY',upper(DOP)) > 0 THEN SUBSTRING(DOP,CHARINDEX('CTY',upper(DOP))+3,3) ELSE CASE PAY_REC WHEN 'P' THEN remitter_country WHEN 'R' THEN beneficiary_country ELSE '' END END,
           payment_flag = CASE WHEN PAY_REC = 'P' THEN '02' ELSE '01' END ,
           reference_no = replace(reference_no,' ',''),
           dop = replace(dop,'-FX','|FX'),
           opp_user = CASE PAY_REC WHEN 'P' THEN SUBSTRING(remitter1 + remitter2 + remitter3,1,120) WHEN 'R' THEN SUBSTRING(beneficiary1 + beneficiary2 + beneficiary3,1,120) ELSE '' END,           
           DEL_FLAG ='0',
           ROW_STATUS ='10',
           CHECK_FLAG ='00'
     WHERE WORK_DATE = @WORKDATE
       
    ----更新客户信息,根据pay_rec判断，如果是P则为remitter_sdsid，R则为beneficiary_sdsid--
    UPDATE BOP_DATASOURCE
       SET BOP_DATASOURCE.customer_type = CASE WHEN BI_CUSTOMER.CLIENT_TYPE IN ('01','03','04') THEN 'C' ELSE 'D' END,
           BOP_DATASOURCE.paper_code = bi_customer.IC_NO,
           BOP_DATASOURCE.customer_name = bi_customer.NAME
      FROM BI_CUSTOMER           
     WHERE BI_CUSTOMER.CLIENT_ID = BOP_DATASOURCE.customer_code
       AND BOP_DATASOURCE.work_date = @WORKDATE
       
    --bop结售汇金额默认值设置    
    declare @old_refno varchar(64)
    declare @rcount int
    
    
    DECLARE cur_bop_fxamt cursor for
    SELECT buscode,reference_no
      FROM bop_datasource
     WHERE work_date = @WORKDATE
       AND SUBSTRING(buscode,1,2) in ('FX','FT','LD','PA','RE')
       
    OPEN cur_bop_fxamt
    
    SET @REF_NO = ''
    SET @old_refno = ''
    
    FETCH NEXT FROM cur_bop_fxamt INTO @old_refno,@REF_NO
    WHILE @@FETCH_STATUS = 0
    BEGIN
      IF SUBSTRING(@old_refno,1,2) = 'FX'
      BEGIN
        --当客户卖出外币买入人民币时,结汇金额等于交易金额,否则现汇金额等于交易金额
        SELECT @rcount = count(1)
          FROM (SELECT DISTINCT buscode,case when tx_ccy = 'CNY' THEN 'CNY' ELSE 'FEX' END AS currency_code,
                                payment_flag
                  FROM bop_datasource
                 WHERE work_date = @WORKDATE
                   AND buscode = @old_refno) a
        WHERE ((currency_code <> 'CNY' AND PAYMENT_FLAG = '01') OR (currency_code = 'CNY' AND PAYMENT_FLAG = '02'))
        IF @rcount = 2 --交易笔数为2时说明该笔交易卖出外币买入人民币
        BEGIN
          UPDATE bop_datasource
             SET lcy_amount = tx_amount
           WHERE work_date = @WORKDATE
             AND reference_no = @REF_NO          
        END
        ELSE
        BEGIN
          UPDATE bop_datasource
             SET fcy_amount = tx_amount
           WHERE work_date = @WORKDATE
             AND reference_no = @REF_NO
        END
      END
      ELSE IF(SUBSTRING(@old_refno,1,2) = 'FT') OR (SUBSTRING(@old_refno,1,2)= 'PA')
         OR (SUBSTRING(@old_refno,1,2) = 'RE')
      BEGIN
        SELECT @rcount = COUNT(1)
          FROM (SELECT DISTINCT buscode,case when tx_ccy = 'CNY' THEN 'CNY' ELSE 'FEX' END AS currency_code,payment_flag
                  FROM bop_datasource
                 WHERE work_date = @WORKDATE
                   AND buscode = @old_refno)a
         WHERE ((CURRENCY_CODE <> 'CNY' AND PAYMENT_FLAG ='01') OR (CURRENCY_CODE = 'CNY' AND PAYMENT_FLAG ='02')) 
         
        IF @rcount = 2
        BEGIN
          --结汇
          UPDATE bop_datasource
             SET lcy_amount = tx_amount
           WHERE work_date = @WORKDATE
             AND reference_no = @REF_NO          
        END
        ELSE
        BEGIN
          SELECT @rcount = COUNT(1)
          FROM (SELECT DISTINCT buscode,case when tx_ccy = 'CNY' THEN 'CNY' ELSE 'FEX' END AS currency_code,payment_flag
                  FROM bop_datasource
                 WHERE work_date = @WORKDATE
                   AND buscode = @old_refno)a
         WHERE ((CURRENCY_CODE <> 'CNY' AND PAYMENT_FLAG ='02') OR (CURRENCY_CODE = 'CNY' AND PAYMENT_FLAG ='01')) 
         
         IF @rcount = 2 --售汇
         BEGIN
          UPDATE bop_datasource
             SET lcy_amount = tx_amount
           WHERE work_date = @WORKDATE
             AND reference_no = @REF_NO
         END
         ELSE
         BEGIN
           --现汇
           UPDATE bop_datasource
              SET fcy_amount = tx_amount
            WHERE work_date = @WORKDATE
              AND reference_no = @REF_NO
         END
        END
      END
      ELSE IF(SUBSTRING(@old_refno,1,2)) = 'LD' --现汇
      BEGIN
        UPDATE bop_datasource
           SET fcy_amount = tx_amount
         WHERE work_date = @WORKDATE
           AND reference_no = @REF_NO
      END
      
      SET @REF_NO = ''
      SET @old_refno = ''
      FETCH NEXT FROM cur_bop_fxamt INTO @old_refno,@REF_NO
      
    END
    
    CLOSE cur_bop_fxamt
    DEALLOCATE cur_bop_fxamt
       
    --计算BOP_DATASOURCE数据的结汇/购汇汇率及金额
    execute sp_CalcBOP @WORKDATE,@ERR_CODE,@ERR_MSG
    
    IF @ERR_CODE <> 0
    BEGIN
      ROLLBACK TRAN
      RETURN
    END
    
    --提取BOP上报数据
    execute sp_GetBOP @WORKDATE,@ERR_CODE,@ERR_MSG
    
    IF @ERR_CODE <> 0
    BEGIN
      ROLLBACK TRAN
      RETURN
    END        
    
    COMMIT TRAN 
    
  END
  
  -----------------------------CCS文件处理-----------------------------
  IF @TABLE_NAME='CCS_DATASOURCE' 
  BEGIN
    BEGIN TRAN
    
    --初始化状态位
    UPDATE CCS_DATASOURCE
       SET row_status ='10',
           del_flag ='0',
           check_status ='0',
           PRODUCT_TYPE = case when PRODUCT_TYPE = '0' then 'CCYSWAP' else PRODUCT_TYPE end
     WHERE work_date = @WORKDATE
     
    --更新操作类型operation_type
    UPDATE CCS_DATASOURCE
       SET operation_type = 
         case
					when upper(Trans_Status) = 'NEW' then '01'
					when upper(Trans_Status) = 'AMEND' then '01'
					when upper(Trans_Status) = 'RESET' then '01'
					when upper(Trans_Status) = 'TERMINATION' then '06'
				end
	  WHERE work_date = @WORKDATE
     
    UPDATE CCS_DATASOURCE
       SET operation_type = '02'
     WHERE start_date <= convert(varchar(8),getdate(),112)
       AND operation_type ='01'
       
    
    --更新删除标志
    UPDATE CCS_DATASOURCE
       SET del_flag = '1'
     WHERE work_date = @WORKDATE
       AND upper(Trans_Status) = 'TERMINATION'
         
                
    --更新汇率  
    UPDATE CCS_DATASOURCE
       SET leg1_rate_type = substring(leg1_rate_type,case when charindex('Fixed',leg1_rate_type) >0 then charindex('Fixed',leg1_rate_type) else charindex('Float',leg1_rate_type)  end ,5),
       	   leg2_rate_type =  substring(leg2_rate_type,case when charindex('Fixed',leg2_rate_type) >0 then charindex('Fixed',leg2_rate_type) else charindex('Float',leg2_rate_type)  end ,5)
     WHERE work_date = @WORKDATE  
    
    
    UPDATE CCS_DATASOURCE
       SET bought_rate_type = CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN leg1_rate_type ELSE leg2_rate_type END WHEN upper(leg2_cur) = 'CNY' THEN CASE WHEN upper(Leg2_PayOrReceive) = 'PAY' THEN leg2_rate_type ELSE leg1_rate_type END END,     
       	   sold_rate_type =  CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'RECEIVE' THEN leg1_rate_type ELSE leg2_rate_type END WHEN upper(leg2_cur) = 'CNY' THEN CASE WHEN upper(Leg2_PayOrReceive) = 'RECEIVE' THEN leg2_rate_type ELSE leg1_rate_type END END,
       	   bought_cur = CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN leg1_cur ELSE leg2_cur END WHEN upper(leg2_cur) = 'CNY' THEN CASE WHEN upper(Leg2_PayOrReceive) = 'PAY' THEN leg2_cur ELSE leg1_cur END END, 
       	   sold_cur = CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'RECEIVE' THEN leg1_cur ELSE leg2_cur END WHEN upper(leg2_cur) = 'CNY' THEN CASE WHEN upper(Leg2_PayOrReceive) = 'RECEIVE' THEN leg2_cur ELSE leg1_cur END END,
       	   bought_amt = CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN leg1_amt ELSE leg2_amt END WHEN upper(leg2_cur) = 'CNY' THEN CASE WHEN upper(Leg2_PayOrReceive) = 'PAY' THEN leg2_amt ELSE leg1_amt END END,
       	   sold_amt = CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'RECEIVE' THEN leg1_amt ELSE leg2_amt END WHEN upper(leg2_cur) = 'CNY' THEN CASE WHEN upper(Leg2_PayOrReceive) = 'RECEIVE' THEN leg2_amt ELSE leg1_amt END END
     WHERE work_date = @WORKDATE  	
        
    
    UPDATE CCS_DATASOURCE  
       SET leg1_FixedRate = CASE ISNULL(leg1_FixedRate,'') WHEN '' THEN '0' WHEN '#N/A' THEN '0' ELSE leg1_FixedRate END,
       	   leg1_DealRate = CASE ISNULL(leg1_DealRate,'') WHEN '' THEN '0' WHEN '#N/A' THEN '0' ELSE leg1_DealRate END,    
       	   leg2_FixedRate = CASE ISNULL(leg2_FixedRate,'') WHEN '' THEN '0' WHEN '#N/A' THEN '0' ELSE leg2_FixedRate END,	
       	   leg2_DealRate = CASE ISNULL(leg2_DealRate,'') WHEN '' THEN '0' WHEN '#N/A' THEN '0' ELSE leg2_DealRate END
     WHERE work_date = @WORKDATE 	   			
    
    UPDATE CCS_DATASOURCE
       SET bought_rate = 
         CASE
	   WHEN upper(leg1_cur) = 'CNY' THEN 
	     CASE WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN
	       CASE WHEN leg1_rate_type = 'Fixed' then leg1_FixedRate WHEN leg1_rate_type = 'Float' then leg1_DealRate END
	     WHEN upper(Leg1_PayOrReceive) = 'RECEIVE' THEN 
	       CASE WHEN leg2_rate_type = 'Fixed' then leg2_FixedRate WHEN leg2_rate_type = 'Float' then leg2_DealRate END
	     END
	   WHEN upper(leg2_cur) = 'CNY' THEN
	     CASE WHEN upper(Leg2_PayOrReceive) = 'PAY' THEN
	       CASE WHEN leg2_rate_type = 'Fixed' then leg2_FixedRate WHEN bought_rate_type = 'Float' then leg2_DealRate END
	     WHEN upper(Leg2_PayOrReceive) = 'RECEIVE' THEN 
	       CASE WHEN leg1_rate_type = 'Fixed' then leg1_FixedRate WHEN bought_rate_type = 'Float' then leg1_DealRate END
	     END
         END,
       sold_rate = 
         CASE
	   WHEN upper(leg1_cur) = 'CNY' THEN 
	     CASE WHEN upper(Leg1_PayOrReceive) = 'RECEIVE' THEN
	       CASE WHEN leg1_rate_type = 'Fixed' then leg1_FixedRate WHEN leg1_rate_type = 'Float' then leg1_DealRate END
	     WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN 
	       CASE WHEN leg2_rate_type = 'Fixed' then leg2_FixedRate WHEN leg2_rate_type = 'Float' then leg2_DealRate END
	     END
	   WHEN upper(leg2_cur) = 'CNY' THEN
	     CASE WHEN upper(Leg2_PayOrReceive) = 'RECEIVE' THEN
	       CASE WHEN leg2_rate_type = 'Fixed' then leg2_FixedRate WHEN bought_rate_type = 'Float' then leg2_DealRate END
	     WHEN upper(Leg2_PayOrReceive) = 'PAY' THEN 
	       CASE WHEN leg1_rate_type = 'Fixed' then leg1_FixedRate WHEN bought_rate_type = 'Float' then leg1_DealRate END
	     END
       END
     WHERE work_date = @WORKDATE
           	
    --同步客户信息
    UPDATE CCS_DATASOURCE
       SET CCS_DATASOURCE.PCODE = BI_CUSTOMER.PCODE,
           CCS_DATASOURCE.customer_type = BI_CUSTOMER.IC_TYPE,
           CCS_DATASOURCE.paper_code = BI_CUSTOMER.IC_NO,
           CCS_DATASOURCE.customer_name = BI_CUSTOMER.NAME,
           CCS_DATASOURCE.customer_address = BI_CUSTOMER.ADDRESS,
           CCS_DATASOURCE.telephone = BI_CUSTOMER.PHONE
      FROM BI_CUSTOMER
     WHERE CCS_DATASOURCE.customer_code = BI_CUSTOMER.CLIENT_ID
       AND CCS_DATASOURCE.WORK_DATE = @WORKDATE 
       
    --设置交易期限trade_period
    --人民币转入
    UPDATE CCS_DATASOURCE
       SET trade_period = 
         CASE 
           WHEN (convert(varchar(8),dateadd(mm,3,start_date),112) >= end_date) THEN '01'--3个月(含)以下
           WHEN (convert(varchar(8),dateadd(mm,3,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,6,start_date),112) >= end_date) THEN '02' --3个月以上至6个月(含)以下 
           WHEN (convert(varchar(8),dateadd(mm,6,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,12,start_date),112) >= end_date) THEN '03' --6个月以上至12个月(含)以下
           WHEN (convert(varchar(8),dateadd(mm,12,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,24,start_date),112) >= end_date) THEN '04' --1年以上至2年(含)以下
           WHEN (convert(varchar(8),dateadd(mm,24,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,36,start_date),112) >= end_date) THEN '05' --2年至3年(含)以下
           WHEN (convert(varchar(8),dateadd(mm,36,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,60,start_date),112) >= end_date) THEN '06' --3年至5年(含)以下
           WHEN (convert(varchar(8),dateadd(mm,60,start_date),112) < end_date) THEN '07' --5年以上
         END
     WHERE work_date = @WORKDATE 
       AND bought_cur = 'CNY'  	  	 
       
    --人民币转出
    UPDATE CCS_DATASOURCE
       SET trade_period = 
         CASE 
           WHEN (convert(varchar(8),dateadd(mm,6,start_date),112) >= end_date) THEN '11'--6个月(含)以下
           WHEN (convert(varchar(8),dateadd(mm,6,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,12,start_date),112) >= end_date) THEN '12' --3个月以上至6个月(含)以下 
           WHEN (convert(varchar(8),dateadd(mm,12,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,36,start_date),112) >= end_date) THEN '13' --1年以上至3年(含)以下
           WHEN (convert(varchar(8),dateadd(mm,36,start_date),112) < end_date) AND (convert(varchar(8),dateadd(mm,60,start_date),112) >= end_date) THEN '14' --3年至5年(含)以下          
           WHEN (convert(varchar(8),dateadd(mm,60,start_date),112) < end_date) THEN '15' --5年以上
         END
     WHERE work_date = @WORKDATE 
       AND sold_cur = 'CNY'   
    
    --更新结售汇标志
    UPDATE CCS_DATASOURCE
       SET trade_type='01'
     WHERE work_date = @WORKDATE 
       AND sold_cur = 'CNY'
       AND  bought_cur <> 'CNY'
    
    UPDATE CCS_DATASOURCE
       SET trade_type='02'
     WHERE work_date = @WORKDATE 
       AND sold_cur <> 'CNY'
       AND  bought_cur = 'CNY'
    
    --计算美元金额
    UPDATE CCS_DATASOURCE
       SET CCS_DATASOURCE.amt_dollar = CCS_DATASOURCE.sold_amt/tmp_tab.rate_midprice
     FROM (SELECT CUR_CODE,rate_midprice / CUR_UNIT as rate_midprice,rate_date from bi_dayexchangerate a
            WHERE a.rate_date in(select distinct deal_date from CCS_DATASOURCE WHERE work_date = @WORKDATE)
              AND a.CUR_CODE = 'USD')tmp_tab
    WHERE CCS_DATASOURCE.work_date = @WORKDATE
      AND CCS_DATASOURCE.deal_date = tmp_tab.rate_date
      AND CCS_DATASOURCE.bought_cur <> 'CNY'
      AND CCS_DATASOURCE.sold_cur = 'CNY' 
      
    UPDATE CCS_DATASOURCE
       SET CCS_DATASOURCE.amt_dollar = CCS_DATASOURCE.bought_amt/tmp_tab.rate_midprice
     FROM (SELECT CUR_CODE,rate_midprice / CUR_UNIT as rate_midprice,rate_date from bi_dayexchangerate a
            WHERE a.rate_date in(select distinct deal_date from CCS_DATASOURCE WHERE work_date = @WORKDATE)
              AND a.CUR_CODE = 'USD')tmp_tab
    WHERE CCS_DATASOURCE.work_date = @WORKDATE
      AND CCS_DATASOURCE.deal_date = tmp_tab.rate_date
      AND CCS_DATASOURCE.bought_cur = 'CNY'
      AND CCS_DATASOURCE.sold_cur <> 'CNY'  
      
    UPDATE CCS_DATASOURCE
       SET amt_dollar = sold_amt
     WHERE work_date = @WORKDATE
       AND bought_cur <> 'USD'
       AND sold_cur = 'USD'
       
    UPDATE CCS_DATASOURCE
       SET amt_dollar = bought_amt
     WHERE work_date = @WORKDATE
       AND bought_cur = 'USD'
       AND sold_cur <> 'USD'  
       
    --更新掉期结构类型
    UPDATE CCS_DATASOURCE
       SET swap_structure = 
         CASE WHEN upper(leg1_cur) = 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN '02' WHEN upper(Leg1_PayOrReceive) = 'RESERVE' THEN '01' END
         WHEN  upper(leg1_cur) <> 'CNY' THEN CASE WHEN upper(Leg1_PayOrReceive) = 'PAY' THEN '01' WHEN upper(Leg1_PayOrReceive) = 'RESERVE' THEN '02' END END
    WHERE work_date = @WORKDATE
    
    --更新即远期标志
    UPDATE CCS_DATASOURCE
       SET business_type = 
         CASE WHEN dbo.fn_addworkdate(trade_date,@CALENDARNO,2) >= start_date then '01'
         	    WHEN dbo.fn_addworkdate(trade_date,@CALENDARNO,2) < start_date then '02'
         END
    WHERE work_date = @WORKDATE
    
    --更新掉期交易期限
    UPDATE CCS_DATASOURCE
       SET swap_period = 
         CASE WHEN business_type = '01' AND convert(varchar(8),dateadd(yyyy,1,start_date),112) < end_date then '12'
         	    WHEN business_type = '01' AND convert(varchar(8),dateadd(yyyy,1,start_date),112) >= end_date then '11'
         	    WHEN business_type = '02' AND convert(varchar(8),dateadd(yyyy,1,trade_date),112) >= start_date and convert(varchar(8),dateadd(yyyy,1,start_date),112) >= end_date then '21'
         	    WHEN business_type = '02' AND convert(varchar(8),dateadd(yyyy,1,trade_date),112) >= start_date and convert(varchar(8),dateadd(yyyy,1,start_date),112) < end_date then '22'
         	    WHEN business_type = '02' AND convert(varchar(8),dateadd(yyyy,1,trade_date),112) < start_date and convert(varchar(8),dateadd(yyyy,1,start_date),112) < end_date then '23'
         END
     WHERE work_date = @WORKDATE            	    	     
          	
    COMMIT TRAN 
    
  END
  
  
  -------------------------利率互换业务数据OTHER_RATESOURCE-----------------------
  IF @TABLE_NAME='IRS_DATASOURCE'
  BEGIN
    BEGIN TRAN
    
    --为GD_PAYER和FD_PAYER字段赋值
    UPDATE IRS_DATASOURCE
       SET GD_PAYER = CASE WHEN TRADE_DIRECTION = '收取固定利率' THEN COUNTERPARTY WHEN TRADE_DIRECTION = '支付固定利率' THEN BENE END,
           FD_PAYER = CASE WHEN TRADE_DIRECTION = '收取固定利率' THEN BENE WHEN TRADE_DIRECTION = '支付固定利率' THEN COUNTERPARTY END           
     WHERE WORK_DATE = @WORKDATE
     
    --更新补录审核标志
    UPDATE IRS_DATASOURCE
       SET ROW_STATUS ='10',
           DEL_FLAG ='0',
           CHECK_STATUS ='01',
           REPAIRER = 'system',
           CHECKER = 'system'
     WHERE WORK_DATE = @WORKDATE AND (ROW_STATUS IS NULL OR ROW_STATUS = '10')
     
     --更新浮动利率
     UPDATE IRS_DATASOURCE
        SET FD_RATE=CK_RATE
      WHERE WORK_DATE = @WORKDATE
     
    COMMIT TRAN
  END
				
  END TRY		 
 ------------------------------------------------------------------------------
--异常错误事务处理  
BEGIN CATCH  
  SET @ERR_CODE=@@ERROR
  IF @ERR_CODE !=0 
  BEGIN
	SET @ERR_MSG= 'SP_AFTERIMPORT:' + ERROR_MESSAGE ()
	ROLLBACK TRAN      --回滚事务
    RETURN	
   END
END   CATCH
END ;
