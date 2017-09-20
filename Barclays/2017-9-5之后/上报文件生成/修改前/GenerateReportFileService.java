package com.aif.rpt.biz.aml.reporting.server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aif.rpt.common.server.SysParamService;
import com.allen_sauer.gwt.log.client.Log;
import com.allinfinance.yak.ubace.util.zip.ZipUtils;
import com.allinfinance.yak.ubase.api.print.template.engine.XmlTemplateEngine;
import com.allinfinance.yak.ubase.api.print.template.engine.exception.TemplateException;
import com.allinfinance.yak.ubase.orm.mybatis.template.MyBatisSessionTemplate;
import com.allinfinance.yak.ubase.print.template.engine.XmlTemplateEngineImpl;

public class GenerateReportFileService {
	
	private MyBatisSessionTemplate myBatisSessionTemplate;;
	
	/**
	 * 生成xml文件
	 * 目前只支持当个文件
	 * 
	 * @param applicationId
	 * @param fileName
	 * @return
	 */
	public boolean genReportFile(String applicationId, String fileName, String workDate, String filePaht) {
		Map parameter = new HashMap();
		Map bopparam = new HashMap();
		parameter.put("APPLICATION_ID", applicationId);
		parameter.put("RPT_FILE", fileName);
		parameter.put("WORK_DATE", workDate);
		if(applicationId.indexOf("BOP") >= 0){
			parameter.put("boptype", applicationId.substring(applicationId.indexOf("-")+1, applicationId.length()));
		}
		// 获取上报文件模版信息
		List<Map> reportFileInfoList =  myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.receipt.server.reportingServerSql.selectReportFileTemplateInfo", parameter);
		if (reportFileInfoList == null || reportFileInfoList.size() <= 0) {
			return false;
		}
		// templateName
		String templateName = "";
		// 规则类型 sql/class
		String ruleType;
		// dataset name
		String datasetName;
		// sql id
		String sqlId;
		// bean id
		String beanId;
		// method name
		String methodName;
		// 是否是单条记录 0-单条，1-多条
		String isSingleData;
		Map reportFileDataRule;
		Map<String,Object> data = new HashMap<String,Object>();
		for (int i = 0; i < reportFileInfoList.size(); i++) {
			templateName = (String) reportFileInfoList.get(i).get("TEMPLETE_NAME");
			
			// 获取上报文件数据规则
			List<Map> reportFileDataRuleList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.receipt.server.reportingServerSql.selectReportFileDataRules", parameter);
			if (reportFileDataRuleList == null || reportFileDataRuleList.size() <= 0) {
				continue;
			}
			for (int j = 0; j < reportFileDataRuleList.size(); j++) {
				reportFileDataRule = reportFileDataRuleList.get(j);
				ruleType = (String) reportFileDataRule.get("RULE_TYPE");
				datasetName = (String) reportFileDataRule.get("DATASET_NAME");
				sqlId = (String) reportFileDataRule.get("SQL_ID");
				beanId = (String) reportFileDataRule.get("BEAN_ID");
				methodName = (String) reportFileDataRule.get("METHOD_NAME");
				isSingleData = (String) reportFileDataRule.get("IS_SINGLE_DATA");
				if ("sql".equalsIgnoreCase(ruleType.trim())) {
					List<Map> dataList = this.executeGenReportSql(sqlId, parameter);
					if(applicationId.indexOf("BOP") >= 0 || applicationId.indexOf("JSH") >= 0){//bop和JSH不生成空文件
						if ((dataList == null || dataList.size() <= 0)&&(beanId == null || "".equals(beanId))) {
							return false;
						}
					}else{
						if (dataList == null && dataList.size() <= 0 && (beanId == null || "".equals(beanId))) {
							return false;
						}
					}
					if ("0".equals(isSingleData)) {
						data.put(datasetName, dataList.get(0));
						
						//BOP更新对应上报文件的总笔数
						if(applicationId.indexOf("BOP") >= 0){
							bopparam.clear();
							bopparam.put("reportfile", fileName);
							bopparam.put("totalcount", dataList.get(0).get("TOTALRECORDS"));
							myBatisSessionTemplate.update("com.aif.rpt.biz.bop.server.BOPGenReportServiceSql.updateRptTotalCount", bopparam);
						}
					} else {
						data.put(datasetName, dataList);
					}
				}
			}
		}
		XmlTemplateEngine engine = new XmlTemplateEngineImpl();
		try {
			engine.setTemplateHome(SysParamService.getGenReportFileTemplatePath());
			// 声明模板目录
			engine.setEncoding("GB18030");
			// 设置文件编码
			engine.initialize();
			String xmlTemplateDataFile = engine.parse(templateName, filePaht + fileName, data);
		} catch (IOException e) {
			Log.error(e.getMessage());
			return false;
		} catch (TemplateException e) {
			Log.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 多个文件打包成zip
	 * 
	 * @param zipFile
	 * @param srcFiles
	 * @return
	 */
	public boolean genZipFile(String zipFile, String srcFiles[]) {
		try {
			// 设置ZIP的文件，要求全路径
			ZipUtils zipUtils = new ZipUtils(zipFile);
			// 添加要压缩的文件。这里必须写全路径，可以同时支持多个文件和多个文件夹的混合压缩
			zipUtils.zip(srcFiles);
			
			// 删除XML文件
			if (srcFiles != null && srcFiles.length > 0) {
				String tempFilePath;
				File tempFile;
				for (int i = 0; i < srcFiles.length; i++) {
					tempFilePath = srcFiles[i];
					try {
						tempFile = new File(tempFilePath);
						tempFile.delete();
					} catch (Exception error) {
						Log.error(error.getMessage());
						continue;
					}
				}
			}
			
		} catch (Exception e) {
			Log.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	private List<Map> executeGenReportSql(String sqlId, Map map) {
		return myBatisSessionTemplate.selectList(sqlId, map);
	}

	public MyBatisSessionTemplate getMyBatisSessionTemplate() {
		return myBatisSessionTemplate;
	}

	public void setMyBatisSessionTemplate(
			MyBatisSessionTemplate myBatisSessionTemplate) {
		this.myBatisSessionTemplate = myBatisSessionTemplate;
	}
}
