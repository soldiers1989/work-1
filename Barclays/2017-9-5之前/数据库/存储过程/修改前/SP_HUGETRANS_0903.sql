USE [SOAR]
GO
/****** Object:  StoredProcedure [dbo].[SP_HUGETRANS_0903]    Script Date: 2017/9/4 18:08:16 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

/***********************************************************************************************   
 * 规则描述：规则描述：自然人银行账户之间，以及自然人与法人、其他组织和个体工商户银行账户之间单笔
 *  或者当日累计人民币50万元以上或者外币等值10万美元以上的款项划转
 *  客户当日发生的交易同时涉及人民币和外币，且人民币交易和外币交易单边累计金额均未达到大额交易报告标准的，义务机构应当分别以人民币和美元折算，单边累计计算本外币交易金额，按照本外币交易报告标准“孰低原则”，合并提交大额交易报告。
客户当日发生的交易同时涉及人民币和外币，且人民币交易或外币交易任一单边累计金额达到大额交易报告标准的，义务机构应当合并提交大额交易报告。

 *  规则统计方法说明：
 *  币种：人民币和外币
 *  统计对象：客户号
 *  客户类型：02-自然人
 *  交易类型：02-转帐
 *  日期判断：当日
 *  金额判断：单笔或累计，人民币金额50万元以上，外币金额10万美元以上
 *  收付判断：收付分开
 *  特殊判断：境内交易
 *  存储过程参数定义：
 *  输出参数：
 *  错误代码：error_code(0-成功；-1-失败)
 *  错误信息：error_message
 *  作者:DAKE                                          修改日期:20170506
 *      变更要点：
        1- 增加触发条件：不分币种判断累计人民币金额50万元以上（含50万元），收付分开；
        2- 增加触发条件：不分币种判断累计美金币金额10万元以上（含10万元），收付分开；
        3- 增加“境内”触发限制；
        3- 触发大额上报条件后，抓取该客户当日所有交易，不分币种，不分收付。
************************************************************************************************/
ALTER PROCEDURE [dbo].[SP_HUGETRANS_0903]
  @DEPARTID       VARCHAR(20),
  @WORKDATE       VARCHAR(8),
  @ERR_CODE     NUMERIC OUTPUT,
  @ERR_MSG  VARCHAR(200) OUTPUT
AS
BEGIN

  --参数定义
  DECLARE @RULE_CODE     VARCHAR(15)  = '0903'    --规则代码   0903
  DECLARE @RULE_TYPE     VARCHAR(2)   = '01'      --规则类型   01-大额
  DECLARE @CLIENT_ID     VARCHAR(32)              --客户号
  DECLARE @CNY_THRESHOLD INT          = 500000
  DECLARE @FCY_THRESHOLD INT          = 100000
  DECLARE @WHERE_SQL     VARCHAR(2000)            --条件语句  

  SET @ERR_CODE = 0    
  
BEGIN TRY  
  --开始事务 
  BEGIN TRAN   

  --删除历史分析结果
  DELETE FROM AML_ANALYSISRESULT
    WHERE WORK_DATE = @WORKDATE
      AND DEPART_ID = @DEPARTID
      AND RULE_TYPE = @RULE_TYPE
      AND RULE_CODE = @RULE_CODE
       
  CREATE TABLE #CLIENT_TEMP
  (
    CLIENT_ID VARCHAR(32) NULL,
    DEBIT_CREDIT VARCHAR(2) NULL
  )
  
  INSERT INTO #CLIENT_TEMP
    --人民币交易，当日累计人民币金额超过50万
    --modify 20170516 不区分币种统计数据包含了区分币种，去掉分币种统计 begin
    /*
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND TRADE_TYPE = '02'                       --转账
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')    --02-自然人        
        AND IS_CROSSBORDER = 0                  --境内        
        AND CURRENCY = 'CNY'
        AND ISNULL(IS_DEL, '0') <> '1'              --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(AMT, 0))) >= @CNY_THRESHOLD
    UNION
    --非人民币交易，当日累计等值美金金额超过10万
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND TRADE_TYPE = '02'                       --转账
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')    --02-自然人
        AND IS_CROSSBORDER = 0                  --境内                
        AND CURRENCY <> 'CNY'
        AND ISNULL(IS_DEL, '0') <> '1'              --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(DOLLAR_AMT, 0))) >= @FCY_THRESHOLD
    UNION*/
    --modify 20170516 不区分币种统计数据包含了区分币种，去掉分币种统计 end
    
    --不分币种，当日累计等值人民币金额超过50万 
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND TRADE_TYPE = '02'                       --转账
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')    --02-自然人
        AND IS_CROSSBORDER = 0                  --境内                
        AND ISNULL(IS_DEL, '0') <> '1'              --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(AMT*dbo.GETCNY2FCYRATE(CURRENCY,TRADE_DATE), 0))) >= @CNY_THRESHOLD
    UNION
    --不分币种，当日累计等值美金金额超过10万
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND TRADE_TYPE = '02'                       --转账
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')    --02-自然人
        AND IS_CROSSBORDER = 0                  --境内                
        AND ISNULL(IS_DEL, '0') <> '1'              --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(DOLLAR_AMT, 0))) >= @FCY_THRESHOLD
    
  DECLARE CURSOR_0903 CURSOR FOR
  SELECT DISTINCT CLIENT_ID
  FROM #CLIENT_TEMP
  
  --打开游标
  OPEN CURSOR_0903 
  
  FETCH NEXT FROM CURSOR_0903 INTO @CLIENT_ID
  
  --循环遍历游标
  WHILE @@FETCH_STATUS = 0
  BEGIN
    SET @WHERE_SQL = ' WHERE WORK_DATE = ' + QUOTENAME(@WORKDATE,'''') + 
      ' AND DEPART_ID = ' + QUOTENAME(@DEPARTID,'''') + 
      ' AND ISNULL(IS_DEL, ''0'') <> ''1'' ' +
      ' AND CLIENT_ID    = ' + QUOTENAME(@CLIENT_ID,'''')
  PRINT  @WHERE_SQL
    --执行插表语句    
    EXEC SP_INSERTANALYSISRESULT @WHERE_SQL,@RULE_CODE,@RULE_TYPE,@DEPARTID,@WORKDATE,@ERR_CODE OUTPUT,@ERR_MSG OUTPUT   
               
    IF (@ERR_CODE != 0)
    BEGIN      
      --关闭游标
      CLOSE CURSOR_0903
      DEALLOCATE CURSOR_0903
      
       --回滚事务
      ROLLBACK TRAN     
      RETURN
    END  
    
    SET @CLIENT_ID = ''
    FETCH NEXT FROM CURSOR_0903 INTO @CLIENT_ID
  END
  
  --关闭游标
  CLOSE CURSOR_0903
  DEALLOCATE CURSOR_0903 
  
  COMMIT TRAN
END TRY

--异常处理
BEGIN CATCH
    SET @ERR_CODE = @@ERROR
         
    IF (@ERR_CODE != 0)
    BEGIN     
        SET @ERR_MSG = 'SP_HUGETRANS_0903:' + ERROR_MESSAGE ()
        RETURN
    END 
END  CATCH
  
END





