USE [SOAR]
GO
/****** Object:  StoredProcedure [dbo].[SP_HUGETRANS_0902]    Script Date: 2017/9/4 18:19:39 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO







/*******************************************************************************    
 * 规则描述：法人、其他组织和个体工商户银行账户之间单笔或者当日累计人民币200万元
 * 以上或者外币等值20万美元以上的款项划转
 *  规则统计方法说明：
 *  币种：人民币和外币
 *  客户类型：01-法人；03-个体工商户；04-其他组织
 *  统计对象：客户号
 *  交易类型：02-转帐
 *  日期判断：当日
 *  金额判断：单笔或累计，人民币金额200万元以上，外币金额20万美元以上
 *  收付判断：收付分开
 *  特殊判断：同一客户同种外币账户之间除外（账户前九位一样，币种不一样）
 *  存储过程参数定义：
 *  输出参数：
 *  错误代码：error_code(0-成功；-1-失败)
 *  错误信息：error_message
 *  作者:WUSHIYU                                          最后修改日期:20130627
*******************************************************************************/
ALTER PROCEDURE [dbo].[SP_HUGETRANS_0902]
  @DEPARTID       VARCHAR(20),
  @WORKDATE       VARCHAR(8),
  @ERR_CODE     NUMERIC OUTPUT,
  @ERR_MSG  VARCHAR(200) OUTPUT
AS
BEGIN

  --参数定义
  DECLARE @RULE_CODE     VARCHAR(15)              --规则代码   0502
  DECLARE @RULE_TYPE     VARCHAR(2)               --规则类型   01-大额
  DECLARE @CLIENT_ID     VARCHAR(32)              --客户号
  DECLARE @DEBIT_CREDIT  VARCHAR(32)              --收付标识  
  DECLARE @WHERE_SQL     VARCHAR(2000)            --条件语句  
  --参数初始化
  SET @RULE_CODE = '0502' 
  SET @RULE_TYPE = '01'
  SET @ERR_CODE = 0  
  
  /******************************************************************
   * 当日单笔或累计，人民币转帐金额200万元以上
  ******************************************************************/  
BEGIN TRY  
  --开始事务 
  BEGIN TRAN   
 
  --删除历史分析结果
  DELETE FROM AML_ANALYSISRESULT
    WHERE WORK_DATE = @WORKDATE
      AND DEPART_ID = @DEPARTID
      AND RULE_TYPE = @RULE_TYPE
      AND RULE_CODE = @RULE_CODE
       
  --建立分析统计游标      
  DECLARE CURSOR_0502_CNY CURSOR FOR
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND TRADE_TYPE = '02'                  --转账
        AND CLIENT_TYPE IN ('01','03','04')    --01-法人；03-个体工商户；04-其他组织
        AND CURRENCY = 'CNY'                   --人民币
        AND ISNULL(IS_DEL, '0') <> '1'         --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(AMT, 0))) >= 2000000
  
  --打开游标
  OPEN CURSOR_0502_CNY 
  
  --初始化参数
  SET @CLIENT_ID = ''
  SET @DEBIT_CREDIT = ''
  
  FETCH NEXT FROM CURSOR_0502_CNY INTO @CLIENT_ID,@DEBIT_CREDIT
  
  --循环遍历游标
  WHILE @@FETCH_STATUS = 0
  BEGIN
    SET @WHERE_SQL = ' WHERE WORK_DATE = ' + QUOTENAME(@WORKDATE,'''') + 
      ' AND DEPART_ID = ' + QUOTENAME(@DEPARTID,'''') + 
      ' AND TRADE_TYPE = ''02''  ' +
      ' AND CURRENCY   = ''CNY'' ' +
      ' AND CLIENT_TYPE IN (''01'',''03'',''04'')' +
      ' AND ISNULL(IS_DEL, ''0'') <> ''1'' ' +
      ' AND DEBIT_CREDIT = ' + QUOTENAME(@DEBIT_CREDIT,'''') + 
      ' AND CLIENT_ID    = ' + QUOTENAME(@CLIENT_ID,'''')
  PRINT  @WHERE_SQL
    --执行插表语句    
    EXEC SP_INSERTANALYSISRESULT @WHERE_SQL,@RULE_CODE,@RULE_TYPE,@DEPARTID,@WORKDATE,@ERR_CODE OUTPUT,@ERR_MSG OUTPUT   
               
    IF (@ERR_CODE != 0)
    BEGIN      
      --关闭游标
      CLOSE CURSOR_0502_CNY
      DEALLOCATE CURSOR_0502_CNY
      
       --回滚事务
      ROLLBACK TRAN     
      RETURN
    END  
    
    SET @CLIENT_ID = ''
    SET @DEBIT_CREDIT = ''
    FETCH NEXT FROM CURSOR_0502_CNY INTO @CLIENT_ID,@DEBIT_CREDIT
  END
  
  --关闭游标
  CLOSE CURSOR_0502_CNY
  DEALLOCATE CURSOR_0502_CNY 
 
  /******************************************************************
   * 当日单笔或累计，外币转帐金额20万美元以上
  ******************************************************************/  
  --建立分析统计游标      
  DECLARE CURSOR_0502_USD CURSOR FOR
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND TRADE_TYPE = '02'                  --转账
        AND CLIENT_TYPE IN ('01','03','04')    --01-法人；03-个体工商户；04-其他组织
        AND CURRENCY <> 'CNY'                  --外币
        AND ISNULL(IS_DEL, '0') <> '1'         --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(DOLLAR_AMT, 0))) >= 200000
  
  --打开游标
  OPEN CURSOR_0502_USD 
  
  --初始化参数
  SET @CLIENT_ID = ''
  SET @DEBIT_CREDIT = ''
  
  FETCH NEXT FROM CURSOR_0502_USD INTO @CLIENT_ID,@DEBIT_CREDIT
  
  --循环遍历游标
  WHILE @@FETCH_STATUS = 0
  BEGIN
    SET @WHERE_SQL = ' WHERE WORK_DATE = ' + QUOTENAME(@WORKDATE,'''') + 
      ' AND DEPART_ID = ' + QUOTENAME(@DEPARTID,'''') + 
      ' AND TRADE_TYPE = ''02''  ' +
      ' AND CURRENCY   <> ''CNY'' ' +
      ' AND CLIENT_TYPE IN (''01'',''03'',''04'')' +
      ' AND ISNULL(IS_DEL, ''0'') <> ''1'' ' +
      ' AND DEBIT_CREDIT = ' + QUOTENAME(@DEBIT_CREDIT,'''') + 
      ' AND CLIENT_ID    = ' + QUOTENAME(@CLIENT_ID,'''')
  PRINT  @WHERE_SQL
    --执行插表语句    
    EXEC SP_INSERTANALYSISRESULT @WHERE_SQL,@RULE_CODE,@RULE_TYPE,@DEPARTID,@WORKDATE,@ERR_CODE OUTPUT,@ERR_MSG OUTPUT   
               
    IF (@ERR_CODE != 0)
    BEGIN      
      --关闭游标
      CLOSE CURSOR_0502_USD
      DEALLOCATE CURSOR_0502_USD
      
       --回滚事务
      ROLLBACK TRAN     
      RETURN
    END  
    
    SET @CLIENT_ID = ''
    SET @DEBIT_CREDIT = ''
    FETCH NEXT FROM CURSOR_0502_USD INTO @CLIENT_ID,@DEBIT_CREDIT
  END
  
  --关闭游标
  CLOSE CURSOR_0502_USD
  DEALLOCATE CURSOR_0502_USD 
 
  COMMIT TRAN
END TRY

--异常处理
BEGIN CATCH
    SET @ERR_CODE = @@ERROR
         
    IF (@ERR_CODE != 0)
    BEGIN     
        SET @ERR_MSG = 'SP_HUGETRANS_0902:' + ERROR_MESSAGE ()
        RETURN
    END 
END  CATCH
END





