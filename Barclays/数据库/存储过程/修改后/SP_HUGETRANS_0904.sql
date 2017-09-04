USE [SOAR]
GO
/****** Object:  StoredProcedure [dbo].[SP_HUGETRANS_0904]    Script Date: 2017/9/4 18:24:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

/*******************************************************************************    
 *  规则描述：交易一方为自然人、单笔或者当日累计等值1万美元以上的跨境交易
 *  规则统计方法说明：
 *  币种：外币
 *  客户类型：02-自然人
 *  统计对象：客户号
 *  日期判断：当日
 *  金额判断：单笔或累计，人民币金额20万元（含20万元）以上，外币金额1万美元（含1万美元）以上
 *  收付判断：收付分开
 *  特殊判断：跨境交易
 *  存储过程参数定义：
 *  输出参数：
 *  错误代码：error_code(0-成功；-1-失败)
 *  错误信息：error_message
 *  作者:DAKE                                          修改日期:20170506
 *      变更要点：
        1- 增加触发条件：不分币种判断累计人民币金额20万元以上（含20万元），收付分开；
        2- 增加触发条件：不分币种判断累计美金金额1万元以上（含1万元），收付分开；
        3- 增加触发条件：  人民币交易累计人民币金额20万元以上（含20万元），收付分开； 
        4- 改变“跨境”触发限制判定标准；
        5- 触发大额上报条件后，抓取该客户当日所有交易，不分币种，不分收付。
*******************************************************************************/
ALTER PROCEDURE [dbo].[SP_HUGETRANS_0904]
  @DEPARTID       VARCHAR(20),
  @WORKDATE       VARCHAR(8),
  @ERR_CODE       NUMERIC OUTPUT,
  @ERR_MSG        VARCHAR(200) OUTPUT
AS
BEGIN

  --参数定义
  DECLARE @RULE_CODE     VARCHAR(15)  = '0504'    --规则代码   0504
  DECLARE @RULE_TYPE     VARCHAR(2)   = '01'      --规则类型   01-大额
  DECLARE @CLIENT_ID     VARCHAR(32)              --客户号
  DECLARE @CNY_THRESHOLD INT          = 200000
  DECLARE @FCY_THRESHOLD INT          = 10000
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
    --人民币交易，当日累计人民币金额超过20万
    --modify 20170516 不区分币种统计数据包含了区分币种，去掉分币种统计 begin
    /*
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND IS_CROSSBORDER = false                              --跨境
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')                 --02-自然人
        AND CURRENCY = 'CNY'
        AND ISNULL(IS_DEL, '0') <> '1'                           --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(AMT, 0))) >= @CNY_THRESHOLD
    UNION
    --非人民币交易，当日累计美金金额超过1万   
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND IS_CROSSBORDER = false                              --跨境
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')                 --02-自然人
        AND CURRENCY <> 'CNY'
        AND ISNULL(IS_DEL, '0') <> '1'                           --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(DOLLAR_AMT, 0))) >= @FCY_THRESHOLD
    UNION*/
    --modify 20170516 不区分币种统计数据包含了区分币种，去掉分币种统计 end    
    --所有交易，当日累计人民币金额超过20万
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND IS_CROSSBORDER = '1'                                --跨境
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')                 --02-自然人
        AND ISNULL(IS_DEL, '0') <> '1'                           --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(AMT*dbo.GETCNY2FCYRATE(CURRENCY,TRADE_DATE), 0))) >= @CNY_THRESHOLD
    UNION
    --所有交易，当日累计美金金额超过1万 
    SELECT CLIENT_ID, DEBIT_CREDIT
        FROM AML_DATASOURCE
    WHERE WORK_DATE = @WORKDATE
        AND DEPART_ID = @DEPARTID
        AND IS_CROSSBORDER = '1'                                --跨境
        AND (CLIENT_TYPE='02' OR CTPY_TYPE='02')                 --02-自然人
        AND ISNULL(IS_DEL, '0') <> '1'                           --过滤删除交易
    GROUP BY CLIENT_ID, DEBIT_CREDIT
    HAVING(SUM(ISNULL(DOLLAR_AMT, 0))) >= @FCY_THRESHOLD
    
  DECLARE CURSOR_0504 CURSOR FOR
  SELECT DISTINCT CLIENT_ID
  FROM #CLIENT_TEMP
  
  --打开游标
  OPEN CURSOR_0504
  
  FETCH NEXT FROM CURSOR_0504 INTO @CLIENT_ID
   
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
      CLOSE CURSOR_0504
      DEALLOCATE CURSOR_0504
      
       --回滚事务
      ROLLBACK TRAN     
      RETURN
    END  
    
    SET @CLIENT_ID = ''
    FETCH NEXT FROM CURSOR_0504 INTO @CLIENT_ID
  END
  
  --关闭游标
  CLOSE CURSOR_0504
  DEALLOCATE CURSOR_0504 
 
  COMMIT TRAN
END TRY

--异常处理
BEGIN CATCH
    SET @ERR_CODE = @@ERROR
         
    IF (@ERR_CODE != 0)
    BEGIN     
        SET @ERR_MSG = 'SP_HUGETRANS_0904:' + ERROR_MESSAGE ()
        RETURN
    END 
END  CATCH  
END

