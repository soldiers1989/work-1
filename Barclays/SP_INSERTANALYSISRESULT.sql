USE [SOAR]
GO
/****** Object:  StoredProcedure [dbo].[SP_INSERTANALYSISRESULT]    Script Date: 2017/9/4 17:27:52 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

/*******************************************************************************    
 * 规则描述：插入分析结果表
 * 输入参数：
 *   业务判断条件：SQL语句
 *   规则代码
 * 输出参数：
 *   返回代码：ERR_CODE 
 *   返回信息：ERR_MSG
 *   
 * 作者:WUSHIYU                                           最后修改日期:201300626
*******************************************************************************/
ALTER PROCEDURE [dbo].[SP_INSERTANALYSISRESULT]
  @WHERE_SQL       VARCHAR(1000),
  @RULE_CODE       VARCHAR(15),
  @RULE_TYPE       VARCHAR(2),
  @DEPARTID        VARCHAR(20),
  @WORKDATE        VARCHAR(8),  
  @ERR_CODE        NUMERIC OUTPUT,
  @ERR_MSG         VARCHAR(200) OUTPUT
AS
BEGIN
  --参数定义
  DECLARE @MAKE_STATUS     VARCHAR(2)             --补录状态
  DECLARE @REPORT_TYPE     VARCHAR(2)             --上报类型  
  DECLARE @CHECK_STATUS    VARCHAR(2)             --审核标志
  DECLARE @IS_DEL          VARCHAR(1)             --删除标识  
  DECLARE @RPT_STATUS      VARCHAR(2)             --上报状态
  DECLARE @RULE_NOTES      VARCHAR(2000)          --规则描述 
  DECLARE @INSERT_SQL      NVARCHAR(2000)         --插入语句
  DECLARE @BATCH_NO        NUMERIC(10)            --批次号  
  DECLARE @IF_RPT          VARCHAR(1)              --上报标识  
  
  --参数初始化  
  SET @ERR_CODE    = 0
  SET @ERR_MSG = ''
  SET @MAKE_STATUS  = '0'     --未补录
  SET @REPORT_TYPE  = 'N'     --普通
  SET @CHECK_STATUS = '0'     --未审核
  SET @IS_DEL       = '0'     --未删除
  SET @RPT_STATUS   = '10'    --未上报
  SET @RULE_NOTES   = ''
  SET @IF_RPT       = '1'     --上报
  
  BEGIN TRY
    --获取规则描述
    SELECT @RULE_NOTES = RULE_NOTES
        FROM TB_ANALYSISRULES 
    WHERE RULE_CODE = @RULE_CODE
        AND RULE_TYPE = @RULE_TYPE
  
    --大额交易
    IF (@RULE_TYPE = '01')
    BEGIN  
        --取得批次号 
        SET @BATCH_NO = 0
        SET @INSERT_SQL = 
        'INSERT INTO AML_ANALYSISRESULT' +
        '  (WORK_DATE       ,'+
        '   DEPART_ID       ,'+     
        '   BATCH_NO        ,'+
        '   REF_NO          ,'+
        '   RULE_CODE       ,'+
        '   RULE_TYPE       ,'+
        '   UNIT_NAME       ,'+
        '   UNIT_CODE       ,'+            
        '   MAKE_STATUS     ,'+
        '   REPORT_TYPE     ,'+
        '   CHECK_STATUS    ,'+
        '   IS_DEL          ,'+      
        '   IF_RPT          ,'+         
        '   RPT_STATUS)      '+  
        ' (SELECT           ' + 
        '  '''+@WORKDATE+''','+
        '  '''+@DEPARTID+''','+         
        '  '''+ CAST(@BATCH_NO AS VARCHAR)+''','+'REF_NO,'+
        '  '''+@RULE_CODE+''','+         
        '  '''+@RULE_TYPE+''','+    
        '  '''+DBO.GETDEPARTNAME(@DEPARTID)+''','+   
        '  '''+DBO.GETUNITCODE(@DEPARTID,'1')+''','+               
        '  '''+@MAKE_STATUS+''','+      
        '  '''+@REPORT_TYPE+''','+   
        '  '''+@CHECK_STATUS+''','+    
        '  '''+@IS_DEL+''','+    
        '  '''+@IF_RPT+''','+                     
        '  '''+@RPT_STATUS+''' '+    
        '  FROM AML_DATASOURCE '+ @WHERE_SQL +' )'   
     END
     ELSE
    BEGIN 
     --可疑交易  
        IF (@RULE_TYPE = '02')   
        BEGIN
        --取得批次号
        SET @BATCH_NO = DBO.GETBATCHNO(@WORKDATE)         
        SET @INSERT_SQL = 
        'INSERT INTO AML_ANALYSISRESULT' +
        '  (WORK_DATE       ,'+
        '   DEPART_ID       ,'+     
        '   BATCH_NO        ,'+
        '   REF_NO          ,'+
        '   RULE_CODE       ,'+
        '   RULE_TYPE       ,'+
        '   UNIT_NAME       ,'+
        '   UNIT_CODE       ,'+        
        '   MAKE_STATUS     ,'+
        '   REPORT_TYPE     ,'+
        '   CHECK_STATUS    ,'+
        '   IS_DEL          ,'+ 
        '   IF_RPT          ,'+               
        '   RPT_STATUS      ,'+
        '   SHADINESS_LEVEL ,'+
        '   SHADINESS_DESC  ,'+
        '   BS_DETR         ,'+
        '   BS_MIRS         ,'+
        '   BS_ORXN         ,'+
        '   BS_DORP         ,'+
        '   BS_TPTR         ,'+
        '   BS_OTPR         ,'+
        '   BS_MIRS)         '+  
        ' (SELECT           ' + 
        '  '''+@WORKDATE+''','+
        '  '''+@DEPARTID+''','+         
        '  '''+ CAST(@BATCH_NO AS VARCHAR)+''','+'REF_NO,'+
        '  '''+@RULE_CODE+''','+         
        '  '''+@RULE_TYPE+''','+    
        '  '''+DBO.GETDEPARTNAME(@DEPARTID)+''','+   
        '  '''+DBO.GETUNITCODE(@DEPARTID,'1')+''','+        
        '  '''+@MAKE_STATUS+''','+      
        '  '''+@REPORT_TYPE+''','+   
        '  '''+@CHECK_STATUS+''','+ 
        '  '''+@IS_DEL+''','+  
        '  '''+@IF_RPT+''','+                  
        '  '''+@RPT_STATUS+''','+ '  ''01'','+       
        '  '''+@RULE_NOTES+''' '+  
        '  ''01'','+
        '  ''1'','+
        '  ''@N'','+
        '  ''01'','+
        '  ''01'','+
        '  ''@N'','+
        '  ''@N'','+             
        '  FROM AML_DATASOURCE '+ @WHERE_SQL +' )'   
        END
        ELSE
        BEGIN
            SET @ERR_CODE = @@ERROR   
            RETURN
        END
    END
    PRINT @INSERT_SQL

    EXEC  sp_executesql @INSERT_SQL
    
    SET @ERR_CODE = @@ERROR       
    RETURN  
  END TRY
  
  --异常处理
  BEGIN CATCH
    SET @ERR_CODE = @@ERROR
         
    IF (@ERR_CODE != 0)
    BEGIN     
        SET @ERR_MSG = 'SP_INSERTANALYSISRESULT:' + ERROR_MESSAGE ()
        RETURN
    END 
  END  CATCH
END


