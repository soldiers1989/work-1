package com.aif.rpt.biz.aml.reporting.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aif.rpt.common.server.BasFuncService;
import com.aif.rpt.common.server.BizFuncService;
import com.aif.rpt.common.server.ContextService;
import com.aif.rpt.common.server.SysParamService;
import com.allen_sauer.gwt.log.client.Log;
import com.allinfinance.yak.ubase.data.page.Page;
import com.allinfinance.yak.ubase.orm.mybatis.template.MyBatisSessionTemplate;
import com.allinfinance.yak.uface.api.core.serivce.callback.Callback;
import com.allinfinance.yak.uface.communication.utils.DateUtils;
import com.allinfinance.yak.uface.core.serivce.callback.CallbackImpl;

public class AMLReportFileService {
	
	private GenerateReportFileService generateReportFileService;
	private MyBatisSessionTemplate myBatisSessionTemplate;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * @param header
	 * @param map
	 * @return 大额查询
	 */
	public Page<Object> selectAMLBH(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectAMLBH", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}
	
	/**
	 * @param header
	 * @param map
	 * @return 大额查询
	 */
	public Page<Object> selectAMLBS(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectAMLBS", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}
	
	public Callback canGenReportFile(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback cb = new CallbackImpl();
		
		// AML第二次次补录有未审核的记录不能进行手工分析
		String departId = ContextService.getDepartId(header);
		dataMap.put("DEPART_ID", departId);
		dataMap.put("TABLE_NAME", "AML_ANALYSISRESULT");
		dataMap.put("CONDITION", "CHECK_STATUS");
		dataMap.put("CONDITION_VALUE", "'0','2'");
		List<Map> retList = myBatisSessionTemplate.selectList("com.aif.rpt.common.server.BizSql.checkMakeStatus", dataMap);
		if (retList != null && retList.size() > 0) {
			cb.setErrorCode("-1");
			cb.setErrorMessage("有未审核完成的数据,不能生成上报文件!");
			return cb;
		}
		
		String workDate = dataMap.get("WORK_DATE");
		
		// AML第二次补录审核是否确认完成
		Map<String,String> param = new HashMap<String,String>();
		param.put("fun_guid", "amlInputCheck_menu2");
		param.put("report_id", "AML");
		param.put("work_date", workDate);
		boolean isFinished = BizFuncService.isFinishedFlowNode(param);
		if (!isFinished) {
			cb.setErrorCode("-1");
			cb.setErrorMessage("AML第二次补录审核未确认完成,不能生成上报文件!");
			return cb;
		}
		
		
		int fileCount = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectReportFileByWorkDate", dataMap);
		if (fileCount > 0) {
			// cb.setErrorCode("-1");
			// cb.setErrorMessage("当日已生成过上报文件，请确认是否继续生成上报文件？");
			Map<String,String> extraMap = new HashMap<String,String>();
			extraMap.put("flag", "1");
			cb.setExtraData(extraMap);
			return cb;
		}
		
		return cb;
	}
	
	public Callback doGenReport (final Map<String, String> header, final Map<String, String> dataMap) {
		Callback cb = new CallbackImpl();
		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String reportDate = dateFormat.format(new Date());
		dataMap.put("REPORT_DATE", reportDate);
		
		// 上报机构号
		String amlCode = BizFuncService.getAmlCode(ContextService.getDepartId(header));
		dataMap.put("AML_CODE", amlCode);
		
		try {

			// 大额普通-N
			this.genNBH(header, dataMap);
			// 大额补报-A
			this.genABH(header, dataMap);
			// 大额纠错-C
			this.genCBH(header, dataMap);

			// 大额删除-D
			this.genDBH(header, dataMap);

			// 大额重发-R
			this.genRBH(header, dataMap);

			// 大额补正-I
			this.genIBH(header, dataMap);

			// 可疑普通-N
			this.genNBS(header, dataMap);

			// 可疑补报-A
			this.genABS(header, dataMap);

			// 可疑纠错-C
			this.genCBS(header, dataMap);

			// 可疑重发-R
			this.genRBS(header, dataMap);

			// 可疑补正-I
			this.genIBS(header, dataMap);
			
		} catch (Exception e) {
			Log.error(e.getMessage());
			cb.setErrorCode("-1");
			cb.setErrorMessage("生成上报文件失败");
			return cb;
		}
		return cb;
	}
	
	// 大额普通-N
	private void genNBH(final Map<String, String> header, final Map<String, String> dataMap) {
		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-NBH";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 大额普通文件N
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "H");
//		parameter.put("REPORT_TYPE", "N");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// N-普通
		parameter.put("REPORT_TYPE", "N");
		parameter.put("WORK_DATE", workDate);
		// 01-大额
		parameter.put("RULE_TYPE", "01");
		List<Map> distinctTradeDate = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctTradeDate", parameter);
		if (distinctTradeDate == null || distinctTradeDate.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "NBH" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		// 获取报表机构名称，编码和报告生成日期
		List<Map> reportDepartInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectReportDepartInfo", parameter);
		if (reportDepartInfoList == null || reportDepartInfoList.size() <= 0) {
			return;
		}
		Map reportDepartInfo = reportDepartInfoList.get(0);
		// 报表机构名称
		String unitName = (String) reportDepartInfo.get("UNIT_NAME");
		// 报表机构编码
		String unitCode = (String) reportDepartInfo.get("UNIT_CODE");
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> catiList;
		Map tradeDateMap;
		String tradeDate;
		int totalCount1 = 0;
		for (int i = 0; i < distinctTradeDate.size(); i++) {
			tradeDateMap = distinctTradeDate.get(i);
			tradeDate = (String) tradeDateMap.get("TRADE_DATE");
			parameter.put("FIELD_VALUE", tradeDate);
			parameter.put("BIZ_FIELD", "T2.TRADE_DATE");
			tempStr = "0000" + (i + 1);
			xmlFileSerialNum = tempStr.substring(tempStr.length() - 4);
			xmlFileName = "NBH" + amlCode + "-" + today + "-" + zipNo + "-" + xmlFileSerialNum + ".XML";
			
			// 获取交易主体信息
			catiList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCATIs", parameter);
			if (catiList == null || catiList.size() <= 0) {
				continue;
			}
			totalCount1 = catiList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map catiMap;
			List<Map> ruleCodes;
			int serialNum1 = 1;
			for (int j = 0; j < catiList.size(); j++) {
				// serialNum1 = j + 1;
				catiMap = catiList.get(j);
				clientId = (String) catiMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				// 获取该客户下每种规则代码和总数
				ruleCodes = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectRuleCodes", parameter);
				if (ruleCodes == null || ruleCodes.size() <= 0) {
					continue;
				}
				System.out.println("################################################## ruleCodes:" +  ruleCodes.size());
				for (Map temp : ruleCodes) {
					System.out.println(">>>>>>>>>>>>>>>>>RULE_CODE : " + temp.get("RULE_CODE"));
					System.out.println(">>>>>>>>>>>>>>>>>TOTAL_COUNT2 : " + temp.get("TOTAL_COUNT2"));
				}
				// totalCount2 = ruleCodes.size();
				int serialNum2 = 1;
				Map rulueCodeMap;
				String ruleCode;
				List<Map> tradeInfoList;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < ruleCodes.size(); k++) {
					// serialNum2 = k + 1;
					rulueCodeMap = ruleCodes.get(k);
					ruleCode = (String) rulueCodeMap.get("RULE_CODE");
					totalCount2 = (Integer) rulueCodeMap.get("TOTAL_COUNT2");
					parameter.put("RULE_CODE", ruleCode);
					
					// 获取该客户和分析规则代码下的交易信息
					tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectTradeInfos", parameter);
					if (tradeInfoList == null || tradeInfoList.size() <= 0) {
						continue;
					}
					int serialNum3 = 1;
					String refNo;
					for (int m = 0; m < tradeInfoList.size(); m++) {
						// serialNum3 = m + 1;
						refNo = (String) tradeInfoList.get(m).get("REF_NO");
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，SERIAL_NUM_3，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("REF_NO", refNo);
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("SERIAL_NUM_3", serialNum3);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER", ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateSerialNum3", parameter);
						
						serialNum3++;
					}
					serialNum2++;
				}
				serialNum1++;
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "N");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "N");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	}
	
	// 大额补报-A
	private void genABH(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-ABH";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 大额补报文件N
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "H");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// A-补报
		parameter.put("REPORT_TYPE", "A");
		parameter.put("WORK_DATE", workDate);
		// 01-大额
		parameter.put("RULE_TYPE", "01");
		List<Map> distinctTradeDate = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctTradeDate", parameter);
		if (distinctTradeDate == null || distinctTradeDate.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBH" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		// 获取报表机构名称，编码和报告生成日期
		List<Map> reportDepartInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectReportDepartInfo", parameter);
		if (reportDepartInfoList == null || reportDepartInfoList.size() <= 0) {
			return;
		}
		Map reportDepartInfo = reportDepartInfoList.get(0);
		// 报表机构名称
		String unitName = (String) reportDepartInfo.get("UNIT_NAME");
		// 报表机构编码
		String unitCode = (String) reportDepartInfo.get("UNIT_CODE");
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> catiList;
		Map tradeDateMap;
		String tradeDate;
		int totalCount1 = 0;
		for (int i = 0; i < distinctTradeDate.size(); i++) {
			tradeDateMap = distinctTradeDate.get(i);
			tradeDate = (String) tradeDateMap.get("TRADE_DATE");
			parameter.put("FIELD_VALUE", tradeDate);
			parameter.put("BIZ_FIELD", "T2.TRADE_DATE");
			tempStr = "0000" + (i + 1);
			xmlFileSerialNum = tempStr.substring(tempStr.length() - 4);
			xmlFileName = "ABH" + amlCode + "-" + today + "-" + zipNo + "-" + xmlFileSerialNum + ".XML";
			
			// 获取交易主体信息
			catiList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCATIs", parameter);
			if (catiList == null || catiList.size() <= 0) {
				continue;
			}
			totalCount1 = catiList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map catiMap;
			List<Map> ruleCodes;
			int serialNum1 = 1;
			for (int j = 0; j < catiList.size(); j++) {
				// serialNum1 = j + 1;
				catiMap = catiList.get(j);
				clientId = (String) catiMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				// 获取该客户下每种规则代码和总数
				ruleCodes = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectRuleCodes", parameter);
				if (ruleCodes == null || ruleCodes.size() <= 0) {
					continue;
				}
				int serialNum2 = 1;
				Map rulueCodeMap;
				String ruleCode;
				List<Map> tradeInfoList;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < ruleCodes.size(); k++) {
					// serialNum2 = k + 1;
					rulueCodeMap = ruleCodes.get(k);
					ruleCode = (String) rulueCodeMap.get("RULE_CODE");
					totalCount2 = (Integer) rulueCodeMap.get("TOTAL_COUNT2");
					parameter.put("RULE_CODE", ruleCode);
					
					// 获取该客户和分析规则代码下的交易信息
					tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectTradeInfos", parameter);
					if (tradeInfoList == null || tradeInfoList.size() <= 0) {
						continue;
					}
					int serialNum3 = 1;
					String refNo;
					for (int m = 0; m < tradeInfoList.size(); m++) {
						// serialNum3 = m + 1;
						refNo = (String) tradeInfoList.get(m).get("REF_NO");
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，SERIAL_NUM_3，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("REF_NO", refNo);
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("SERIAL_NUM_3", serialNum3);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER", ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
						
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateSerialNum3", parameter);
						
						serialNum3++;
					}
					serialNum2++;
				}
				serialNum1++;
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "A");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 大额纠错-C
	private void genCBH(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-CBH";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 大额纠错文件C
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "H");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// A-纠错
		parameter.put("REPORT_TYPE", "C");
		parameter.put("WORK_DATE", workDate);
		// 01-大额
		parameter.put("RULE_TYPE", "01");
		List<Map> distinctTradeDate = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctTradeDate", parameter);
		if (distinctTradeDate == null || distinctTradeDate.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBH" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		// 获取报表机构名称，编码和报告生成日期
		List<Map> reportDepartInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectReportDepartInfo", parameter);
		if (reportDepartInfoList == null || reportDepartInfoList.size() <= 0) {
			return;
		}
		Map reportDepartInfo = reportDepartInfoList.get(0);
		// 报表机构名称
		String unitName = (String) reportDepartInfo.get("UNIT_NAME");
		// 报表机构编码
		String unitCode = (String) reportDepartInfo.get("UNIT_CODE");
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> catiList;
		Map tradeDateMap;
		String tradeDate;
		int totalCount1 = 0;
		for (int i = 0; i < distinctTradeDate.size(); i++) {
			tradeDateMap = distinctTradeDate.get(i);
			tradeDate = (String) tradeDateMap.get("TRADE_DATE");
			parameter.put("TRADE_DATE", tradeDate);
			parameter.put("FIELD_VALUE", tradeDate);
			parameter.put("BIZ_FIELD", "T2.TRADE_DATE");
			tempStr = "0000" + (i + 1);
			xmlFileSerialNum = tempStr.substring(tempStr.length() - 4);
			xmlFileName = "CBH" + amlCode + "-" + today + "-" + zipNo + "-" + xmlFileSerialNum + ".XML";
			
			// 获取交易主体信息
			catiList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCATIs", parameter);
			if (catiList == null || catiList.size() <= 0) {
				continue;
			}
			totalCount1 = catiList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map catiMap;
			List<Map> ruleCodes;
			int serialNum1 = 1;
			for (int j = 0; j < catiList.size(); j++) {
				// serialNum1 = j + 1;
				catiMap = catiList.get(j);
				clientId = (String) catiMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				// 获取该客户下每种规则代码和总数
				ruleCodes = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectRuleCodes", parameter);
				if (ruleCodes == null || ruleCodes.size() <= 0) {
					continue;
				}
				int serialNum2 = 1;
				Map rulueCodeMap;
				String ruleCode;
				List<Map> tradeInfoList;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < ruleCodes.size(); k++) {
					// serialNum2 = k + 1;
					rulueCodeMap = ruleCodes.get(k);
					ruleCode = (String) rulueCodeMap.get("RULE_CODE");
					totalCount2 = (Integer) rulueCodeMap.get("TOTAL_COUNT2");
					parameter.put("RULE_CODE", ruleCode);
					
					// 获取该客户和分析规则代码下的交易信息
					tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectTradeInfos", parameter);
					if (tradeInfoList == null || tradeInfoList.size() <= 0) {
						continue;
					}
					int serialNum3 = 1;
					String refNo;
					for (int m = 0; m < tradeInfoList.size(); m++) {
						// serialNum3 = m + 1;
						refNo = (String) tradeInfoList.get(m).get("REF_NO");
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，SERIAL_NUM_3，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("REF_NO", refNo);
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("SERIAL_NUM_3", serialNum3);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER", ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
						
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateSerialNum3", parameter);
						
						serialNum3++;
					}
					serialNum2++;
				}
				serialNum1++;
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "C");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 大额删除-D
	private void genDBH(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-ABH";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 大额删除文件D
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "H");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// A-纠错
		parameter.put("REPORT_TYPE", "D");
		parameter.put("WORK_DATE", workDate);
		// 01-大额
		parameter.put("RULE_TYPE", "01");
		List<Map> distinctTradeDate = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctTradeDate", parameter);
		if (distinctTradeDate == null || distinctTradeDate.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBH" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		// 获取报表机构名称，编码和报告生成日期
		List<Map> reportDepartInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectReportDepartInfo", parameter);
		if (reportDepartInfoList == null || reportDepartInfoList.size() <= 0) {
			return;
		}
		Map reportDepartInfo = reportDepartInfoList.get(0);
		// 报表机构名称
		String unitName = (String) reportDepartInfo.get("UNIT_NAME");
		// 报表机构编码
		String unitCode = (String) reportDepartInfo.get("UNIT_CODE");
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> catiList;
		Map tradeDateMap;
		String tradeDate;
		int totalCount1 = 0;
		for (int i = 0; i < distinctTradeDate.size(); i++) {
			tradeDateMap = distinctTradeDate.get(i);
			tradeDate = (String) tradeDateMap.get("TRADE_DATE");
			parameter.put("TRADE_DATE", tradeDate);
			parameter.put("FIELD_VALUE", tradeDate);
			parameter.put("BIZ_FIELD", "T2.TRADE_DATE");
			tempStr = "0000" + (i + 1);
			xmlFileSerialNum = tempStr.substring(tempStr.length() - 4);
			xmlFileName = "DBH" + amlCode + "-" + today + "-" + zipNo + "-" + xmlFileSerialNum + ".XML";
			
			// 获取交易主体信息
			catiList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCATIs", parameter);
			if (catiList == null || catiList.size() <= 0) {
				continue;
			}
			totalCount1 = catiList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map catiMap;
			List<Map> ruleCodes;
			int serialNum1 = 1;
			for (int j = 0; j < catiList.size(); j++) {
				// serialNum1 = j + 1;
				catiMap = catiList.get(j);
				clientId = (String) catiMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				// 获取该客户下每种规则代码和总数
				ruleCodes = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectRuleCodes", parameter);
				if (ruleCodes == null || ruleCodes.size() <= 0) {
					continue;
				}
				int serialNum2 = 1;
				Map rulueCodeMap;
				String ruleCode;
				List<Map> tradeInfoList;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < ruleCodes.size(); k++) {
					// serialNum2 = k + 1;
					rulueCodeMap = ruleCodes.get(k);
					ruleCode = (String) rulueCodeMap.get("RULE_CODE");
					totalCount2 = (Integer) rulueCodeMap.get("TOTAL_COUNT2");
					parameter.put("RULE_CODE", ruleCode);
					
					// 获取该客户和分析规则代码下的交易信息
					tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectTradeInfos", parameter);
					if (tradeInfoList == null || tradeInfoList.size() <= 0) {
						continue;
					}
					int serialNum3 = 1;
					String refNo;
					for (int m = 0; m < tradeInfoList.size(); m++) {
						// serialNum3 = m + 1;
						refNo = (String) tradeInfoList.get(m).get("REF_NO");
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，SERIAL_NUM_3，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("REF_NO", refNo);
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("SERIAL_NUM_3", serialNum3);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER", ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
						
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateSerialNum3", parameter);
						
						serialNum3++;
					}
					serialNum2++;
				}
				serialNum1++;
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "D");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 大额重发-R
	private void genRBH(final Map<String, String> header, final Map<String, String> dataMap) {


		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-ABH";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 大额重发文件R
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "H");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// R-重发
		parameter.put("REPORT_TYPE", "R");
		parameter.put("RPT_STATUS", "10");
		parameter.put("WORK_DATE", workDate);
		// 01-大额
		parameter.put("RULE_TYPE", "01");
		List<Map> distinctRptFiles = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctRptFiles", parameter);
		if (distinctRptFiles == null || distinctRptFiles.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBH" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> catiList;
		Map rptFileMap;
		String rptFile;
		int totalCount1 = 0;
		for (int i = 0; i < distinctRptFiles.size(); i++) {
			rptFileMap = distinctRptFiles.get(i);
			rptFile = (String) rptFileMap.get("RPT_FILE");
			parameter.put("FIELD_VALUE", rptFile);
			parameter.put("BIZ_FIELD", "T1.RPT_FILE");
			xmlFileName = "R" + rptFile.substring(1);
			
			// 获取交易主体信息
			catiList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCATIs", parameter);
			if (catiList == null || catiList.size() <= 0) {
				continue;
			}
			totalCount1 = catiList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map catiMap;
			List<Map> ruleCodes;
			int serialNum1 = 1;
			for (int j = 0; j < catiList.size(); j++) {
				// serialNum1 = j + 1;
				catiMap = catiList.get(j);
				clientId = (String) catiMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				// 获取该客户下每种规则代码和总数
				ruleCodes = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectRuleCodes", parameter);
				if (ruleCodes == null || ruleCodes.size() <= 0) {
					continue;
				}
				int serialNum2 = 1;
				Map rulueCodeMap;
				String ruleCode;
				List<Map> tradeInfoList;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < ruleCodes.size(); k++) {
					// serialNum2 = k + 1;
					rulueCodeMap = ruleCodes.get(k);
					ruleCode = (String) rulueCodeMap.get("RULE_CODE");
					totalCount2 = (Integer) rulueCodeMap.get("TOTAL_COUNT2");
					parameter.put("RULE_CODE", ruleCode);
					
					// 获取该客户和分析规则代码下的交易信息
					tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectTradeInfos", parameter);
					if (tradeInfoList == null || tradeInfoList.size() <= 0) {
						continue;
					}
					int serialNum3 = 1;
					String refNo;
					for (int m = 0; m < tradeInfoList.size(); m++) {
						// serialNum3 = m + 1;
						refNo = (String) tradeInfoList.get(m).get("REF_NO");
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，SERIAL_NUM_3，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("REF_NO", refNo);
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("SERIAL_NUM_3", serialNum3);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER", ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
						
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateSerialNum3", parameter);
						
						serialNum3++;
					}
					serialNum2++;
				}
				serialNum1++;
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "R");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	
	}
	
	// 大额补正-I
	private void genIBH(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-IBH";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 大额补正文件I
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "H");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// I-补正
		parameter.put("REPORT_TYPE", "I");
		parameter.put("RPT_STATUS", "10");
		parameter.put("WORK_DATE", workDate);
		// 01-大额
		parameter.put("RULE_TYPE", "01");
		List<Map> distinctRptFiles = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctRptFiles", parameter);
		if (distinctRptFiles == null || distinctRptFiles.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBH" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> catiList;
		Map rptFileMap;
		String rptFile;
		int totalCount1 = 0;
		for (int i = 0; i < distinctRptFiles.size(); i++) {
			rptFileMap = distinctRptFiles.get(i);
			rptFile = (String) rptFileMap.get("RPT_FILE");
			parameter.put("FIELD_VALUE", rptFile);
			parameter.put("BIZ_FIELD", "RPT_FILE");
			xmlFileName = "I" + rptFile.substring(1);
			
			// 获取大额补正上报数据
			catiList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectIBHData", parameter);
			if (catiList == null || catiList.size() <= 0) {
				continue;
			}
			totalCount1 = catiList.size();
			
			int totalCount2 = 0;
			String ruleCode;
			String refNo;
			Map catiMap;
			List<Map> ruleCodes;
			int serialNum1 = 1;
			for (int j = 0; j < catiList.size(); j++) {
				// serialNum1 = j + 1;
				catiMap = catiList.get(j);
				ruleCode = (String) catiMap.get("RULE_CODE");
				refNo = (String) catiMap.get("REF_NO");
				parameter.put("RULE_CODE", ruleCode);
				parameter.put("REF_NO", refNo);
				parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
				parameter.put("TOTAL_COUNT1", totalCount1);
				parameter.put("SERIAL_NUM_1", serialNum1);
				parameter.put("RPT_FILE", xmlFileName);
				parameter.put("RPT_ZIP", newZipFileName);
				parameter.put("REPORTER", ContextService.getUserName(header));
				parameter.put("RPT_STATUS", "11");
				
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateIBHTotalCount1AndSerialNum1", parameter);
				
				serialNum1++;
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "I");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 可疑普通-N
	private void genNBS(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-NBS";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 可疑普通文件N
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "S");
//		parameter.put("REPORT_TYPE", "N");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
					   }
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// N-普通
		parameter.put("REPORT_TYPE", "N");
		parameter.put("WORK_DATE", workDate);
		// 02-可疑
		parameter.put("RULE_TYPE", "02");
		List<Map> distinctBatchNo = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSBatchNo", parameter);
		if (distinctBatchNo == null || distinctBatchNo.size() <= 0) {
			return;
		}
		
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "NBS" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> clientList;
		Map batchNoMap;
		String batchNo;
		int totalCount1 = 0;
		for (int i = 0; i < distinctBatchNo.size(); i++) {
			batchNoMap = distinctBatchNo.get(i);
			batchNo = (String) batchNoMap.get("BATCH_NO");
			parameter.put("FIELD_VALUE", batchNo);
			parameter.put("BIZ_FIELD", "BATCH_NO");
			tempStr = "0000" + (i + 1);
			xmlFileSerialNum = tempStr.substring(tempStr.length() - 4);
			xmlFileName = "NBS" + amlCode + "-" + today + "-" + zipNo + "-" + xmlFileSerialNum + ".XML";
			
			// 查询所有交易信息
			parameter.put("BATCH_NO", batchNo);
			List<Map> tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSTradeInfo", parameter);
			if (tradeInfoList != null && tradeInfoList.size() > 0) {
				String refNo;
				for (int t = 0; t < tradeInfoList.size(); t++) {
					refNo = (String) tradeInfoList.get(t).get("REF_NO");
					parameter.put("REF_NO", refNo);
					parameter.put("SERIAL_NUM_3", t+1);
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateRPDISerialNum3", parameter);
				}
			}
			
			// 查询上报中客户信息
			clientList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSClientInfo", parameter);
			if (clientList == null || clientList.size() <= 0) {
				continue;
			}
			totalCount1 = clientList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map clientMap;
			List<Map> acctList;
			int serialNum1 = 1;
			for (int j = 0; j < clientList.size(); j++) {
				// serialNum1 = j + 1;
				clientMap = clientList.get(j);
				clientId = (String) clientMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				parameter.put("CLIENT_NAME", clientMap.get("CLIENT_NAME"));
				parameter.put("CLIENT_IC_NO", clientMap.get("CLIENT_IC_NO"));
				parameter.put("CLIENT_TYPE", clientMap.get("CLIENT_TYPE"));
				parameter.put("PHONE", clientMap.get("PHONE"));
				parameter.put("ADDRESS", clientMap.get("ADDRESS"));
				parameter.put("OTHER_CONTACT", clientMap.get("OTHER_CONTACT"));
				parameter.put("CLIENT_NATIONALITY", clientMap.get("CLIENT_NATIONALITY"));
				parameter.put("INDUSTRY_TYPE", clientMap.get("INDUSTRY_TYPE"));
				parameter.put("REGISTERED_CAPITAL", clientMap.get("REGISTERED_CAPITAL"));
				parameter.put("REP_NAME", clientMap.get("REP_NAME"));
				parameter.put("REP_IC_NO", clientMap.get("REP_IC_NO"));
				parameter.put("BATCH_NO", batchNo);
				// 查询该客户下的帐户信息
				acctList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSAcctInfo", parameter);
				if (acctList == null || acctList.size() <= 0) {
					continue;
				}
				totalCount2 = acctList.size();
				int serialNum2 = 1;
				Map acctMap;
				String acctId;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < acctList.size(); k++) {
					// serialNum2 = k + 1;
					acctMap = acctList.get(k);
					acctId = (String) acctMap.get("ACCT_ID");
					parameter.put("ACCT_ID", acctId);
					
					// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
					parameter.put("TOTAL_COUNT1", totalCount1);
					parameter.put("SERIAL_NUM_1", serialNum1);
					parameter.put("TOTAL_COUNT2", totalCount2);
					parameter.put("SERIAL_NUM_2", serialNum2);
					parameter.put("RPT_FILE", xmlFileName);
					parameter.put("RPT_ZIP", newZipFileName);
					parameter.put("REPORTER", ContextService.getUserName(header));
					parameter.put("RPT_STATUS", "11");
					parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
					
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateBSSerialNum3", parameter);
					serialNum2++;
				}
				serialNum1++;
			}
			
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "N");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "N");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 可疑补报-A
	private void genABS(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-ABS";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 可疑补报文件A
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "S");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// A-补报
		parameter.put("REPORT_TYPE", "A");
		parameter.put("WORK_DATE", workDate);
		// 02-可疑
		parameter.put("RULE_TYPE", "02");
		List<Map> distinctBatchNo = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSBatchNo", parameter);
		if (distinctBatchNo == null || distinctBatchNo.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBS" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName;
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> clientList;
		Map batchNoMap;
		String batchNo;
		int totalCount1 = 0;
		for (int i = 0; i < distinctBatchNo.size(); i++) {
			batchNoMap = distinctBatchNo.get(i);
			batchNo = (String) batchNoMap.get("BATCH_NO");
			parameter.put("FIELD_VALUE", batchNo);
			parameter.put("BIZ_FIELD", "BATCH_NO");
			tempStr = "0000" + (i + 1);
			xmlFileSerialNum = tempStr.substring(tempStr.length() - 4);
			xmlFileName = "ABS" + amlCode + "-" + today + "-" + zipNo + "-" + xmlFileSerialNum + ".XML";
			
			// 查询所有交易信息
			parameter.put("BATCH_NO", batchNo);
			List<Map> tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSTradeInfo", parameter);
			if (tradeInfoList != null && tradeInfoList.size() > 0) {
				String refNo;
				for (int t = 0; t < tradeInfoList.size(); t++) {
					refNo = (String) tradeInfoList.get(t).get("REF_NO");
					parameter.put("REF_NO", refNo);
					parameter.put("SERIAL_NUM_3", t+1);
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateRPDISerialNum3", parameter);
				}
			}
			
			// 查询上报中客户信息
			clientList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSClientInfo", parameter);
			if (clientList == null || clientList.size() <= 0) {
				continue;
			}
			totalCount1 = clientList.size();
			
			int totalCount2 = 0;
			String clientId;
			Map clientMap;
			List<Map> acctList;
			int serialNum1 = 1;
			for (int j = 0; j < clientList.size(); j++) {
				// serialNum1 = j + 1;
				clientMap = clientList.get(j);
				clientId = (String) clientMap.get("CLIENT_ID");
				parameter.put("CLIENT_ID", clientId);
				parameter.put("BATCH_NO", batchNo);
				// 查询该客户下的帐户信息
				acctList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSAcctInfo", parameter);
				if (acctList == null || acctList.size() <= 0) {
					continue;
				}
				totalCount2 = acctList.size();
				int serialNum2 = 1;
				Map acctMap;
				String acctId;
				// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
				for (int k = 0; k < acctList.size(); k++) {
					// serialNum2 = k + 1;
					acctMap = acctList.get(k);
					acctId = (String) acctMap.get("ACCT_ID");
					parameter.put("ACCT_ID", acctId);
					
					// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
					parameter.put("TOTAL_COUNT1", totalCount1);
					parameter.put("SERIAL_NUM_1", serialNum1);
					parameter.put("TOTAL_COUNT2", totalCount2);
					parameter.put("SERIAL_NUM_2", serialNum2);
					parameter.put("RPT_FILE", xmlFileName);
					parameter.put("RPT_ZIP", newZipFileName);
					parameter.put("REPORTER", ContextService.getUserName(header));
					parameter.put("RPT_STATUS", "11");
					parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));
					
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateBSSerialNum3", parameter);
					serialNum2++;
				}
				serialNum1++;
			}
			
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "A");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "ABS");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 可疑纠错-C
	private void genCBS(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-CBS";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 可疑纠错文件C
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "S");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// C-纠错
		parameter.put("REPORT_TYPE", "C");
		parameter.put("WORK_DATE", workDate);
		// 02-可疑
		parameter.put("RULE_TYPE", "02");
		List<Map> distinctBatchNo = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSBatchNo", parameter);
		if (distinctBatchNo == null || distinctBatchNo.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBS" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName = "";
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> clientList;
		Map batchNoMap;
		String batchNo;
		int totalCount1 = 0;
		for (int i = 0; i < distinctBatchNo.size(); i++) {
			batchNoMap = distinctBatchNo.get(i);
			batchNo = (String) batchNoMap.get("BATCH_NO");
			parameter.put("FIELD_VALUE", batchNo);
			parameter.put("BIZ_FIELD", "BATCH_NO");
			parameter.put("BATCH_NO", batchNo);
			List<Map> distinctRptFiles = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctCBSRptFiles", parameter);
			if (distinctRptFiles == null || distinctRptFiles.size() <= 0) {
				return;
			}
			
			Map rptFileMap;
			String rptFile;
			for (int n = 0; n < distinctRptFiles.size(); n++) {
				rptFileMap = distinctRptFiles.get(n);
				rptFile = (String) rptFileMap.get("RPT_FILE");
				parameter.put("BATCH_NO", batchNo);
				parameter.put("RPT_FILE", rptFile);
				xmlFileName = "C" + rptFile.substring(1);
				
				// 查询所有交易信息
				parameter.put("BATCH_NO", batchNo);
				List<Map> tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSTradeInfo", parameter);
				if (tradeInfoList != null && tradeInfoList.size() > 0) {
					String refNo;
					for (int t = 0; t < tradeInfoList.size(); t++) {
						refNo = (String) tradeInfoList.get(t).get("REF_NO");
						parameter.put("REF_NO", refNo);
						parameter.put("SERIAL_NUM_3", t+1);
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateRPDISerialNum3", parameter);
					}
				}
				// 查询上报中客户信息
				clientList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSClientInfo",parameter);
				if (clientList == null || clientList.size() <= 0) {
					continue;
				}
				totalCount1 = clientList.size();

				int totalCount2 = 0;
				String clientId;
				Map clientMap;
				List<Map> acctList;
				int serialNum1 = 1;
				for (int j = 0; j < clientList.size(); j++) {
					// serialNum1 = j + 1;
					clientMap = clientList.get(j);
					clientId = (String) clientMap.get("CLIENT_ID");
					parameter.put("CLIENT_ID", clientId);
					parameter.put("BATCH_NO", batchNo);
					// 查询该客户下的帐户信息
					acctList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCBSAcctInfo",parameter);
					if (acctList == null || acctList.size() <= 0) {
						continue;
					}
					totalCount2 = acctList.size();
					int serialNum2 = 1;
					Map acctMap;
					String acctId;
					// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
					for (int k = 0; k < acctList.size(); k++) {
						// serialNum2 = k + 1;
						acctMap = acctList.get(k);
						acctId = (String) acctMap.get("ACCT_ID");
						parameter.put("ACCT_ID", acctId);
						
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER",ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));

						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateBSSerialNum3",parameter);
						serialNum2++;
					}
					serialNum1++;
				}
				
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "C");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 可疑重发-R
	private void genRBS(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-RBS";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 可疑重发文件R
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "S");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// R-重发
		parameter.put("REPORT_TYPE", "R");
		parameter.put("WORK_DATE", workDate);
		// 02-可疑
		parameter.put("RULE_TYPE", "02");
		List<Map> distinctBatchNo = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSBatchNo", parameter);
		if (distinctBatchNo == null || distinctBatchNo.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBS" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName = "";
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> clientList;
		Map batchNoMap;
		String batchNo;
		int totalCount1 = 0;
		for (int i = 0; i < distinctBatchNo.size(); i++) {
			batchNoMap = distinctBatchNo.get(i);
			batchNo = (String) batchNoMap.get("BATCH_NO");
			parameter.put("BATCH_NO", batchNo);
			List<Map> distinctRptFiles = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctCBSRptFiles", parameter);
			if (distinctRptFiles == null || distinctRptFiles.size() <= 0) {
				return;
			}
			
			Map rptFileMap;
			String rptFile;
			for (int n = 0; n < distinctRptFiles.size(); n++) {
				rptFileMap = distinctRptFiles.get(n);
				rptFile = (String) rptFileMap.get("RPT_FILE");
				parameter.put("BATCH_NO", batchNo);
				parameter.put("RPT_FILE", rptFile);
				
				parameter.put("FIELD_VALUE", batchNo);
				parameter.put("BIZ_FIELD", "BATCH_NO");
				
				xmlFileName = "R" + rptFile.substring(1);

				// 查询所有交易信息
				parameter.put("BATCH_NO", batchNo);
				List<Map> tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSTradeInfo", parameter);
				if (tradeInfoList != null && tradeInfoList.size() > 0) {
					String refNo;
					for (int t = 0; t < tradeInfoList.size(); t++) {
						refNo = (String) tradeInfoList.get(t).get("REF_NO");
						parameter.put("REF_NO", refNo);
						parameter.put("SERIAL_NUM_3", t+1);
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateRPDISerialNum3", parameter);
					}
				}
				
				// 查询上报中客户信息
				clientList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSClientInfo",parameter);
				if (clientList == null || clientList.size() <= 0) {
					continue;
				}
				totalCount1 = clientList.size();

				int totalCount2 = 0;
				String clientId;
				Map clientMap;
				List<Map> acctList;
				int serialNum1 = 1;
				for (int j = 0; j < clientList.size(); j++) {
					// serialNum1 = j + 1;
					clientMap = clientList.get(j);
					clientId = (String) clientMap.get("CLIENT_ID");
					parameter.put("CLIENT_ID", clientId);
					parameter.put("BATCH_NO", batchNo);
					parameter.put("RPT_FILE", rptFile);
					// 查询该客户下的帐户信息
					acctList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCBSAcctInfo",parameter);
					if (acctList == null || acctList.size() <= 0) {
						continue;
					}
					totalCount2 = acctList.size();
					int serialNum2 = 1;
					Map acctMap;
					String acctId;
					// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
					for (int k = 0; k < acctList.size(); k++) {
						// serialNum2 = k + 1;
						acctMap = acctList.get(k);
						acctId = (String) acctMap.get("ACCT_ID");
						parameter.put("ACCT_ID", acctId);
						
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，SERIAL_NUM_3，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER",ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));

						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateBSSerialNum3",parameter);
							
						serialNum2++;
					}
					serialNum1++;
				}
				
			}
			
			
			
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "R");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	// 可疑补正-I
	private void genIBS(final Map<String, String> header, final Map<String, String> dataMap) {

		List<String> rptFileList = new ArrayList<String>();
		String workDate = dataMap.get("WORK_DATE");
		// 上报机构号
		String amlCode = dataMap.get("AML_CODE");
		// TODO:
		String applicationId = "AML-IBS";
		// 自然日
		String today = DATE_FORMAT.format(new Date());
		// 可疑补正文件I
		// 获取大额包序号起始值
		int zipFileNum = 1;
		Map parameter = new HashMap();
		parameter.put("TRADE_REPORT_TYPE", "S");
//		parameter.put("REPORT_TYPE", "S");
		parameter.put("WORK_DATE", workDate);
		List<Map> retZips = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxGenDateOfAMLZips", parameter);
		// 如果查询结果不为空截取GENERATE_DATE的1-8位与自然日期进行比较
		if (retZips != null && retZips.size() > 0) {
			Map zipMap = retZips.get(0);
			String genDateStr = (String) zipMap.get("GENERATE_DATE");
			String zipName = (String) zipMap.get("ZIP_NAME");
			if (genDateStr != null && genDateStr.length() >= 8 && zipName != null && zipName.length() >= 33) {
				String genDateStr8 = genDateStr.substring(0, 8);
				Date genDate = DateUtils.parseDate(genDateStr8, "yyyyMMdd");
				String newDateStr8 = DATE_FORMAT.format(new Date());
				Date newDate = DateUtils.parseDate(newDateStr8, "yyyyMMdd");
				if (genDate != null && newDate != null) {
					// 如果不小于自然日,从ZIP_NAME的第29位开始截取4位+1
					if (!genDate.before(newDate)) {
						int index = zipName.indexOf(".");
						if (index >= 4) {
							zipFileNum = Integer.parseInt(zipName.substring(index-4, index)) + 1;
						} else {
							zipFileNum = Integer.parseInt(zipName.substring(29, 33)) + 1;
						}
					}
				}
			}
		}
		
		// 查询出报表日不同的交易日
		parameter = new HashMap();
		// I-补正
		parameter.put("REPORT_TYPE", "I");
		parameter.put("WORK_DATE", workDate);
		// 02-可疑
		parameter.put("RULE_TYPE", "02");
		List<Map> distinctBatchNo = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSBatchNo", parameter);
		if (distinctBatchNo == null || distinctBatchNo.size() <= 0) {
			return;
		}
		// ZIP文件名
		String zipNoTemp = "0000" + zipFileNum;
		String zipNo = zipNoTemp.substring(zipNoTemp.length() - 4);
		String newZipFileName = "SBS" + amlCode + "-" + today + "-" + zipNo + ".ZIP";
		
		// 上报文件名
		String xmlFileName = "";
		// 文件序号
		String xmlFileSerialNum;
		String tempStr;
		// 交易主体信息
		List<Map> clientList;
		Map batchNoMap;
		String batchNo;
		int totalCount1 = 0;
		for (int i = 0; i < distinctBatchNo.size(); i++) {
			batchNoMap = distinctBatchNo.get(i);
			batchNo = (String) batchNoMap.get("BATCH_NO");
			parameter.put("BATCH_NO", batchNo);
			
			List<Map> distinctRptFiles = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectDistinctCBSRptFiles", parameter);
			if (distinctRptFiles == null || distinctRptFiles.size() <= 0) {
				return;
			}
			
			Map rptFileMap;
			String rptFile;
			for (int n = 0; n < distinctRptFiles.size(); n++) {
				rptFileMap = distinctRptFiles.get(n);
				rptFile = (String) rptFileMap.get("RPT_FILE");
				parameter.put("BATCH_NO", batchNo);
				parameter.put("RPT_FILE", rptFile);
				xmlFileName = "I" + rptFile.substring(1);
				
				// 查询所有交易信息
				parameter.put("BATCH_NO", batchNo);
				List<Map> tradeInfoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectBSTradeInfo", parameter);
				if (tradeInfoList != null && tradeInfoList.size() > 0) {
					String refNo;
					for (int t = 0; t < tradeInfoList.size(); t++) {
						refNo = (String) tradeInfoList.get(t).get("REF_NO");
						parameter.put("REF_NO", refNo);
						parameter.put("SERIAL_NUM_3", t+1);
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateRPDISerialNum3", parameter);
					}
				}

				// 查询上报中客户信息
				clientList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCBSClientInfo",parameter);
				if (clientList == null || clientList.size() <= 0) {
					continue;
				}
				totalCount1 = clientList.size();

				int totalCount2 = 0;
				String clientId;
				Map clientMap;
				List<Map> acctList;
				int serialNum1 = 1;
				for (int j = 0; j < clientList.size(); j++) {
					// serialNum1 = j + 1;
					clientMap = clientList.get(j);
					clientId = (String) clientMap.get("CLIENT_ID");
					parameter.put("CLIENT_ID", clientId);
					parameter.put("BATCH_NO", batchNo);
					parameter.put("RPT_FILE", rptFile);
					// 查询该客户下的帐户信息
					acctList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectCBSAcctInfo",parameter);
					if (acctList == null || acctList.size() <= 0) {
						continue;
					}
					totalCount2 = acctList.size();
					int serialNum2 = 1;
					Map acctMap;
					String acctId;
					// 回填SERIAL_NUM_2 和TOTAL_COUNT2的值
					for (int k = 0; k < acctList.size(); k++) {
						// serialNum2 = k + 1;
						acctMap = acctList.get(k);
						acctId = (String) acctMap.get("ACCT_ID");
						parameter.put("ACCT_ID", acctId);
						
						// 更新TOTAL_COUNT1，TOTAL_COUNT2，SERIAL_NUM_1，SERIAL_NUM_2，RPT_FILE=上报文件名，RPT_ZIP=上报文件包名，REPORTER=操作员，RPT_STATUS=11-已上报
						parameter.put("TOTAL_COUNT1", totalCount1);
						parameter.put("SERIAL_NUM_1", serialNum1);
						parameter.put("TOTAL_COUNT2", totalCount2);
						parameter.put("SERIAL_NUM_2", serialNum2);
						parameter.put("RPT_FILE", xmlFileName);
						parameter.put("RPT_ZIP", newZipFileName);
						parameter.put("REPORTER",ContextService.getUserName(header));
						parameter.put("RPT_STATUS", "11");
						parameter.put("REPORT_DATE", dataMap.get("REPORT_DATE"));

						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.updateBSSerialNum3",parameter);
							
						serialNum2++;
					}
					serialNum1++;
				}
				
			}
			// 获取AML_FILES的SERIAL_NO
			parameter.put("ZIP_NAME", newZipFileName);
			String serialNo = "1";
			List<Map> serialNoList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.selectMaxSerialNoOfAMLFiles", parameter);
			if (serialNoList != null && serialNoList.size() > 0) {
				serialNo = serialNoList.get(0).get("SERIAL_NO").toString();
			}
			// 在AML_FILES表中插入包信息
			Map insertAmlFileParameter = new HashMap();
			insertAmlFileParameter.put("APPLICATION_ID", applicationId);
			insertAmlFileParameter.put("WORK_DATE", workDate);
			insertAmlFileParameter.put("SERIAL_NO", serialNo);
			insertAmlFileParameter.put("ZIP_NAME", newZipFileName);
			insertAmlFileParameter.put("DATA_DATE", today);
			insertAmlFileParameter.put("FILE_NAME", xmlFileName);
			insertAmlFileParameter.put("FILE_TYPE", "I");
			insertAmlFileParameter.put("FILE_STATUS", "00");
			insertAmlFileParameter.put("RET_DATE", "");
			insertAmlFileParameter.put("RET_FILENAME", "");
			insertAmlFileParameter.put("TRANS_COUNT", "");
			insertAmlFileParameter.put("FILE_IDENTIFY", "NBH");
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlFiles", insertAmlFileParameter);
			rptFileList.add(xmlFileName);
		}
		
		String fileName;
		String filePaht = SysParamService.getGenReportFilePath();
		
		// 在AML_ZIPS表中插入包信息
		Map insertAmlZipParameter = new HashMap();
		insertAmlZipParameter.put("WORK_DATE", workDate);
		insertAmlZipParameter.put("SERIAL_NO", "1");
		insertAmlZipParameter.put("ZIP_NAME", newZipFileName);
		insertAmlZipParameter.put("DATA_DATE", today);
		insertAmlZipParameter.put("DEPART_CODE", amlCode);
		insertAmlZipParameter.put("ZIP_TYPE", "S");
		insertAmlZipParameter.put("ZIP_PATH", filePaht);
		insertAmlZipParameter.put("GENERATE_DATE", DATE_TIME_FORMAT.format(new Date()));
		insertAmlZipParameter.put("ZIP_STATUS", "00");
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.reporting.server.reportingServerSql.insertAmlZips", insertAmlZipParameter);
		
		String fileNameArr[] = new String[rptFileList.size()];
		for (int c = 0; c < rptFileList.size(); c++) {
			fileName = rptFileList.get(c);
			dataMap.put("RPT_FILE", fileName);
			fileNameArr[c] = filePaht + fileName;
			generateReportFileService.genReportFile(applicationId, fileName,null,SysParamService.getGenReportFilePath());
		}
		String zipFullPath = filePaht + newZipFileName;
		generateReportFileService.genZipFile(zipFullPath, fileNameArr);
	
	}
	
	private List<Map> processGenReportOfSql(String sqlId, Map map) {
		return myBatisSessionTemplate.selectList(sqlId, map);
	}
	
	private List<Map> processGenReportOfClass(String beanId, String methodName, Map map) {
		return null;
	}

	public MyBatisSessionTemplate getMyBatisSessionTemplate() {
		return myBatisSessionTemplate;
	}

	public void setMyBatisSessionTemplate(
			MyBatisSessionTemplate myBatisSessionTemplate) {
		this.myBatisSessionTemplate = myBatisSessionTemplate;
	}
	
	// ****************************************************************************
	// 完成确认和取消完成确认
	// ****************************************************************************
	
	// 查询待确认数据源
		/*
		 * dataMap：work_date,depart_id
		 */
		public Callback SelectUnConfirmSouce(final Map<String, String> header, final Map<String, String> dataMap) {
			String importStatus;
			Callback cb = new CallbackImpl();
			
			// TODO: 其他业务校验
			// 检查是否有FXO数据未补录完
			String departId = ContextService.getDepartId(header);
			dataMap.put("DEPART_ID", departId);
			
			/*importStatus = GetImportStatus();
			if (ImportFileVar.IMPORT_INPROGRESS.equals(importStatus)) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("系统正在导入文件,不能进行完成确认!");
				return cb;
			}*/

			String workDate = dataMap.get("WORK_DATE");
			// String departId = dataMap.get("depart_id");
			StringBuilder fieldValue = new StringBuilder("");
			// fieldValue.append("WORK_DATE='").append(workDate).append("'|DEPART_ID='").append(departId).append("'");

			Integer instanceCount = BizFuncService.SelectInstanceCount(ContextService.getCurrFuncId(header), workDate, fieldValue.toString());
			// 检查系统流程实例是否存在
			if (instanceCount == 0) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("系统报表实例未创建，不能进行完成确认!");
				return cb;
			}

			if (!BizFuncService.isFinishBeforeNodes(ContextService.getCurrFuncId(header), workDate)) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("前节点未全部完成！不能进行完成确认！");
				return cb;
			}

			List unSourceList = BizFuncService.SelectSourceList(ContextService.getCurrFuncId(header), ContextService.getUserId(header), fieldValue.toString(), "0", workDate);

			cb.setCallbackData(unSourceList);
			return cb;
		}
		
		// 查询待确认数据源
		/*
		 * dataMap：work_date,depart_id
		 */
		public Callback SelectConfirmSouce(final Map<String, String> header, final Map<String, String> dataMap) {
			String importStatus;
			Callback cb = new CallbackImpl();

			// TODO: 其他业务校验
			/*if (ImportFileVar.IMPORT_INPROGRESS.equals(importStatus)) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("系统正在导入文件,不能进行完成确认!");
				return cb;
			}*/

			String workDate = dataMap.get("WORK_DATE");
			// String departId = dataMap.get("depart_id");
			StringBuilder fieldValue = new StringBuilder("");
			// fieldValue.append("WORK_DATE='").append(workDate).append("'|DEPART_ID='").append(departId).append("'");

			Integer instanceCount = BizFuncService.SelectInstanceCount(ContextService.getCurrFuncId(header), workDate, fieldValue.toString());
			// 检查系统流程实例是否存在
			if (instanceCount == 0) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("系统报表实例未创建，不能进行完成确认!");

				return cb;
			}

			//非末尾节点，判断后节点是否完成
			if (!BizFuncService.isEndofNode(ContextService.getCurrFuncId(header))) {
				if (BizFuncService.isFinishAfterNodes(ContextService.getCurrFuncId(header), workDate)) {
					cb.setErrorCode("-1");
					cb.setErrorMessage("后节点已完成！不能进行取消确认！");
					return cb;
				}
			}

			List SourceList = BizFuncService.SelectSourceList(ContextService.getCurrFuncId(header), ContextService.getUserId(header), fieldValue.toString(), "1", workDate);

			cb.setCallbackData(SourceList);

			return cb;
		}

		// dataList的Map中有Source_id,source_status,field_value
		public Callback SetComfirmSourceStatus(final Map<String, String> header, List<Map<String, String>> dataList) {
			Callback cb = new CallbackImpl();
			String importStatus;
			Map dataMap;
			String workDate;
			String departId;
			StringBuilder fieldValue = new StringBuilder("");

			// TODO: 其他业务校验
			/*if (ImportFileVar.IMPORT_INPROGRESS.equals(importStatus)) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("系统正在导入文件,不能进行完成确认!");
				return cb;
			}*/

			for (int i = 0; i < dataList.size(); i++) {
				dataMap = dataList.get(i);
				workDate = (String) dataMap.get("WORK_DATE");
				// departId = (String)dataMap.get("depart_id");
				// fieldValue.append("WORK_DATE='").append(workDate).append("'|DEPART_ID='").append(departId).append("'");
				dataMap.put("field_value", fieldValue.toString());
			}

			boolean setFlag = BizFuncService.SetMutiSourceStatus(dataList);

			if (setFlag) {
				cb.setSuccessMessage("数据源状态设置成功！");
			} else {
				cb.setErrorCode("-1");
				cb.setErrorMessage("数据源状态设置有误！");
			}
			return cb;
		}

		public GenerateReportFileService getGenerateReportFileService() {
			return generateReportFileService;
		}

		public void setGenerateReportFileService(
				GenerateReportFileService generateReportFileService) {
			this.generateReportFileService = generateReportFileService;
		}
}
