package com.aif.rpt.biz.aml.receipt.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aif.rpt.common.server.BizFuncService;
import com.allen_sauer.gwt.log.client.Log;
import com.allinfinance.yak.ubase.api.core.spring.SpringContextUtils;
import com.allinfinance.yak.ubase.orm.mybatis.template.MyBatisSessionTemplate;

/**
 * 回执文件导入处理
 * 
 * @author gaohw
 *
 */
public class RetXmlFileImpService {
	
	private MyBatisSessionTemplate myBatisSessionTemplate = (MyBatisSessionTemplate) SpringContextUtils.getBean("myBatisSessionTemplate");
	// 回执文件名
	private String receiptFileName;
	// 文件路径
	private String receiptFilePath;
	// 文件名称
	private String fileName;
	// 工作日期
	private String workDate;
	// depart id
	private String departId;
	
	private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss"); 

	private String[] lsqlArr = new String[0];
	private ArrayList<String> lsqlArrL = new ArrayList();

	private XmlFileOperate xmlOp = null;

	// 如果回执为根级的错误回执，则该值为false，不需要修改步骤控制
	// 如果回执为非根级的错误回执，或补正回执，则该值为true，需要修改步骤控制
	private Boolean bModifyStepInfo = false;

	// 回执文件类型
	public static enum RetXmlType {
		xFDSC {// 正确回执
			public String toString() {
				return "FDSC";
			}
		},
		xFDCF {// 错误回执
			public String toString() {
				return "FDCF";
			}
		},
		xFDRC {// 补正回执
			public String toString() {
				return "FDRC";
			}
		};
	}
	
	// 上报xml文件类型
	public static enum XmlType {
		xNBH {
			public String toString() {
				return "NBH";
			}
		},
		xNBS {
			public String toString() {
				return "NBS";
			}
		},
		xRBH {
			public String toString() {
				return "RBH";
			}
		},
		xRBS {
			public String toString() {
				return "RBS";
			}
		},
		xABH {
			public String toString() {
				return "ABH";
			}
		},
		xABS {
			public String toString() {
				return "ABS";
			}
		},
		xCBH {
			public String toString() {
				return "CBH";
			}
		},
		xCBS {
			public String toString() {
				return "CBS";
			}
		},
		xIBH {
			public String toString() {
				return "IBH";
			}
		},
		xIBS {
			public String toString() {
				return "IBS";
			}
		},
		xDBH {
			public String toString() {
				return "DBH";
			}
		};
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 处理文件
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 导入回执文件
	 * retFileName回执文件名，格式 FDSC[NBH011021101020001-20090306-0049-0005]20090306124530.ZIP
	 * 
	 * @return
	 */
	public boolean impRetXmlFile() {
		if (receiptFileName.indexOf(RetXmlType.xFDSC.toString()) == 0) {
			// 处理正确回执
			if (!impFDSCRetXmlFile()) {
				return false;
			}
			
			return true;
		}
		if (receiptFileName.indexOf(RetXmlType.xFDCF.toString()) == 0) {
			// 处理错误回执
			if (!impFDCFRetXmlFile()) {
				return false;
			}
			
			// 当前流程节点回退到AML一次补录状态
			BizFuncService.updateFlowNodeToAMLInput2(this.workDate);
			
			return true;
		}
		if (receiptFileName.indexOf(RetXmlType.xFDRC.toString()) == 0) {
			// 处理补正回执
			if (!impFDRCRetXmlFile()) {
				return false;
			}
			
			return true;
		}
		return false;
	}

	/**
	 *  处理正确回执
	 */
	private boolean impFDSCRetXmlFile() {
		
		// 更新回执信息到分析结果表
		Map parameter = new HashMap();
		parameter.put("RPT_STATUS", "20"); // 上报成功
		parameter.put("RET_MSE", "");
		parameter.put("RPT_STATUS2", "11"); // 已上报
		parameter.put("RPT_FILE", this.fileName);
		myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLAnalysisresult", parameter);
		// 更新上报文件表
		parameter = new HashMap();
		parameter.put("FILE_STATUS", "10"); // 正确
		parameter.put("FILE_STATUS2", "00"); // 正常
		parameter.put("FILE_NAME", this.fileName); // 已上报
		myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLFiles", parameter);
		// 插入回执日志表
		parameter = new HashMap();
		parameter.put("WORK_DATE", this.workDate);
		parameter.put("DEPART_CODE", this.departId);
		parameter.put("FILE_NAME", this.receiptFileName);
		parameter.put("FILE_TYPE", "00");
		parameter.put("ERROR_FILE_NAME", "");
		parameter.put("ERROR_POINTER", "");
		parameter.put("ERROR_INFORMATION", "上报文件正确发送");
		parameter.put("IMPORT_TIME", DATETIME_FORMAT.format(new Date()));
		myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.insertReceiptimportlog", parameter);
		
		return true;
	}

	/**
	 * 处理错误回执
	 */
	private boolean impFDCFRetXmlFile() {
		try {
			String expression;
			String exprERLC; // 错误定位xpath
			String sERLC; // 错误定位信息
			String exprERRS; // 错误原因xpath
			String sERRS; // 错误原因
			String sNodeName; // 错误节点名
			xmlOp = new XmlFileOperate();
			if (!xmlOp.transFormXmlFile(receiptFilePath + receiptFileName, receiptFilePath)) {
				return false;
			}
			if (!xmlOp.LoadXmlFile(receiptFilePath + receiptFileName, receiptFilePath)) {
				return false;
			}
			// 读错误文件总数
			String exprERTN = "/FDBK/ERTN";
			String preExpr = "/FDBK/FCERs/FCER[";
			String affExpr = "]";
			// String tmp = xmlOp.getNodeValue(exprERTN);
			StringBuffer lsql = new StringBuffer("");
			int iErtn = Integer.valueOf(xmlOp.getNodeValue(exprERTN));
			for (int i = 1; i <= iErtn; i++) {
				expression = preExpr + String.valueOf(i) + affExpr;
				exprERLC = expression + "/ERLC";
				sERLC = xmlOp.getNodeValue(exprERLC);
				exprERRS = expression + "/ERRS";
				sERRS = xmlOp.getNodeValue(exprERRS);
				
				
				
				// 更新回执信息到分析结果表
				Map parameter = new HashMap();
				parameter.put("RPT_STATUS", "20"); 
				parameter.put("REPORT_TYPE", "N"); // 重发
				parameter.put("RPT_STATUS2", "11"); // 已上报
				parameter.put("REPORT_TYPE2", "N"); // 普通
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLAnalysisresult", parameter);
				// 更新上报文件表
				parameter = new HashMap();
				parameter.put("FILE_STATUS", "11"); // 错误
				parameter.put("FILE_STATUS2", "00"); // 正常
				parameter.put("FILE_NAME", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLFiles", parameter);
				// 插入回执日志表
				parameter = new HashMap();
				parameter.put("WORK_DATE", this.workDate);
				parameter.put("DEPART_CODE", this.departId);
				parameter.put("FILE_NAME", this.receiptFileName);
				parameter.put("FILE_TYPE", "01"); // 内容错误回执
				parameter.put("ERROR_FILE_NAME", this.fileName);
				parameter.put("ERROR_POINTER", sERLC);
				parameter.put("ERROR_INFORMATION", sERRS);
				parameter.put("IMPORT_TIME", DATETIME_FORMAT.format(new Date()));
				myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.insertReceiptimportlog", parameter);

				if ((sERLC != null) && (sERLC.length() > 4)) {
					sNodeName = sERLC.substring(sERLC.length() - 4);
				} else {
					sNodeName = "";
				}
				// 解析错误
				if (!parseLocation(receiptFileName, sERLC, sERRS, RetXmlType.xFDCF,
						sNodeName)) {
					Log.error(receiptFileName + "：" + sERLC + ":" + sERRS + "解析失败");
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 处理补正回执
	 */
	private boolean impFDRCRetXmlFile() {
		try {
			String expression;
			String exprRCLC; // 补正定位xpath
			String sRCLC; // 补正定位信息
			String exprRCSG; // 补正提示xpath
			String sRCSG; // 补正提示
			String sNodeName; // 错误节点名
			xmlOp = new XmlFileOperate();
			if (!xmlOp.transFormXmlFile(receiptFilePath + receiptFileName, receiptFilePath)) {
				return false;
			}
			if (!xmlOp.LoadXmlFile(receiptFilePath + receiptFileName, receiptFilePath)) {
				return false;
			}
			// 读补正要求总数
			String exprRCTN = "/FDBK/RCTN";
			String preExpr = "/FDBK/FCRCs/FCRC[";
			String affExpr = "]";

			int iRCTN = Integer.valueOf(xmlOp.getNodeValue(exprRCTN));

			if (receiptFileName.indexOf("BH") > 0) {
				// 大额
				for (int i = 1; i <= iRCTN; i++) {
					expression = preExpr + String.valueOf(i) + affExpr
							+ "/RCIFs/RCIF[";
					sRCLC = "test";
					int j = 0;
					while (sRCLC != null && sRCLC.length() > 0) {
						j++;
						exprRCLC = expression + String.valueOf(j) + affExpr;
						exprRCLC = exprRCLC + "/RCLC";
						sRCLC = xmlOp.getNodeValue(exprRCLC);
						exprRCSG = expression + String.valueOf(j) + affExpr
								+ "/RCSG";
						sRCSG = xmlOp.getNodeValue(exprRCSG);
						
						// 更新回执信息到分析结果表
						Map parameter = new HashMap();
						parameter.put("RPT_STATUS", "20"); // 上报成功 解析补正定位时需要设置为22(要求补正)
						parameter.put("REPORT_TYPE", "N"); // 普通 解析补正定位时需要设置为I(补正) 
						parameter.put("RPT_STATUS2", "11"); // 已上报
						parameter.put("RPT_FILE", this.fileName);
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLAnalysisresult", parameter);
						// 更新上报文件表
						parameter = new HashMap();
						parameter.put("FILE_STATUS", "12"); // 补正
						parameter.put("FILE_STATUS2", "00"); // 正常
						parameter.put("FILE_NAME", this.fileName);
						myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLFiles", parameter);
						// 插入回执日志表
						parameter = new HashMap();
						parameter.put("WORK_DATE", this.workDate);
						parameter.put("DEPART_CODE", this.departId);
						parameter.put("FILE_NAME", this.receiptFileName);
						parameter.put("FILE_TYPE", "02"); // 要求补正回执
						parameter.put("ERROR_FILE_NAME", this.fileName);
						parameter.put("ERROR_POINTER", sRCLC);
						parameter.put("ERROR_INFORMATION", sRCSG);
						parameter.put("IMPORT_TIME", DATETIME_FORMAT.format(new Date()));
						myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.insertReceiptimportlog", parameter);

						if ((sRCLC != null) && (sRCLC.length() > 4)) {
							sNodeName = sRCLC.substring(sRCLC.length() - 4);
						} else {
							sNodeName = "";
						}
						if (sRCLC != null && sRCLC.length() > 0) {
							// 解析补正定位
							if (!parseLocation(receiptFileName, sRCLC, sRCSG,
									RetXmlType.xFDRC, sNodeName)) {
								Log.error(receiptFileName + "：" + sRCLC + ":"
										+ sRCSG + "解析失败");
								return false;
							}
						}
					}
				}
			} else {
				// 可疑
				for (int i = 1; i <= iRCTN; i++) {
					expression = preExpr + String.valueOf(i) + affExpr;
					exprRCLC = expression + "/RCLC";
					sRCLC = xmlOp.getNodeValue(exprRCLC);
					exprRCSG = expression + "/RCSG";
					sRCSG = xmlOp.getNodeValue(exprRCSG);
					
					// 更新回执信息到分析结果表
					Map parameter = new HashMap();
					parameter.put("RPT_STATUS", "20"); // 上报成功 解析补正定位时需要设置为22(要求补正)
					parameter.put("REPORT_TYPE", "N"); // 普通 解析补正定位时需要设置为I(补正) 
					parameter.put("RPT_STATUS2", "11"); // 已上报
					parameter.put("RPT_FILE", this.fileName);
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLAnalysisresult", parameter);
					// 更新上报文件表
					parameter = new HashMap();
					parameter.put("FILE_STATUS", "12"); // 补正
					parameter.put("FILE_STATUS2", "00"); // 正常
					parameter.put("FILE_NAME", this.fileName);
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateAMLFiles", parameter);
					// 插入回执日志表
					parameter = new HashMap();
					parameter.put("WORK_DATE", this.workDate);
					parameter.put("DEPART_CODE", this.departId);
					parameter.put("FILE_NAME", this.receiptFileName);
					parameter.put("FILE_TYPE", "02"); // 要求补正回执
					parameter.put("ERROR_FILE_NAME", this.fileName);
					parameter.put("ERROR_POINTER", sRCLC);
					parameter.put("ERROR_INFORMATION", sRCSG);
					parameter.put("IMPORT_TIME", DATETIME_FORMAT.format(new Date()));
					myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.insertReceiptimportlog", parameter);

					if ((sRCLC != null) && (sRCLC.length() > 4)) {
						sNodeName = sRCLC.substring(sRCLC.length() - 4);
					} else {
						sNodeName = "";
					}
					// 解析补正定位
					if (!parseLocation(receiptFileName, sRCLC, sRCSG,
							RetXmlType.xFDRC, sNodeName)) {
						Log.error(receiptFileName + "：" + sRCLC + ":" + sRCSG
								+ "解析失败");
						return false;
					}
				}
			}

			return true;
		} catch (Exception e) {
			Log.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 解析定位
	 * 
	 * @param retFileName 回执文件名称
	 * @param sLC 定位信息
	 * @param sInfo 信息
	 * @param type 回执类型
	 * @param sNodeName
	 * @return
	 */
	private boolean parseLocation(String retFileName, String sLC, String sInfo,
			RetXmlType type, String sNodeName) {
		if (type == RetXmlType.xFDCF) {
			// 错误报文
			if (retFileName.indexOf(XmlType.xNBH
					.toString()) != -1) {
				// 大额正常上报报文
				if (!parseNBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xRBH
					.toString()) != -1) {
				// 大额重发上报报文
				if (!parseNBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xABH
					.toString()) != -1) {
				// 大额补报报文
				if (!parseNBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xCBH
					.toString()) != -1) {
				// 大额纠错报文
				if (!parseCBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xDBH
					.toString()) != -1) {
				// 大额删除报文
				if (!parseDBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xIBH
					.toString()) != -1) {
				// 大额补正报文
				if (!parseCBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xNBS
					.toString()) != -1) {
				// 可疑正常上报报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xRBS
					.toString()) != -1) {
				// 可疑重发上报报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xABS
					.toString()) != -1) {
				// 可疑补报报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xCBS
					.toString()) != -1) {
				// 可疑纠错报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xIBS
					.toString()) != -1) {
				// 可疑补正报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
		}
		if (type == RetXmlType.xFDRC) {
			// 补正
			if (retFileName.indexOf(XmlType.xNBH
					.toString()) != -1) {
				// 大额正常上报报文
				if (!parseNBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xRBH
					.toString()) != -1) {
				// 大额重发报文
				if (!parseNBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xABH
					.toString()) != -1) {
				// 大额补报报文
				if (!parseNBHLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xNBS
					.toString()) != -1) {
				// 可疑正常上报报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xRBS
					.toString()) != -1) {
				// 可疑重发报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
			if (retFileName.indexOf(XmlType.xABS
					.toString()) != -1) {
				// 可疑补报报文
				if (!parseNBSLocation(retFileName, sLC, sInfo, type, sNodeName)) {
					return false;
				} else {
					return true;
				}
			}
		}
		Log.error("无法识别文件格式");
		return false;
	}
	
	/**
	 * 解析大额报文定位
	 * 
	 * @param retFileName 回执文件名称
	 * @param sLC 定位信息
	 * @param sInfo 信息
	 * @param type 回执类型
	 * @param sNodeName
	 * @return
	 */
	private boolean parseNBHLocation(String retFileName, String sLC,
			String sInfo, RetXmlType type, String sNodeName) {
		
		Map parameter = new HashMap();
		
		StringBuffer lsql = new StringBuffer("");
		StringBuffer lwhereclause = new StringBuffer("");

		bModifyStepInfo = true;
		// 解析大额正常、重发、补报报文回执
		try {
			int iCATI = sLC.indexOf("CATI[@seqno");
			int iHTCR = sLC.indexOf("HTCR[@seqno");
			int iTSDT = sLC.indexOf("TSDT[@seqno");
			if (iCATI > 0) {
				if (iHTCR > 0) {
					iCATI = Integer.valueOf(sLC
							.substring(iCATI + 13, iHTCR - 9));
				} else {
					iCATI = Integer.valueOf(sLC.substring(iCATI + 13, sLC
							.indexOf("\"]")));
				}
			} else {
				
				// 交易基本信息错误
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R"); // 重发
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
				return true;
			}
			String tmp = "";
			if (iHTCR > 0) {
				tmp = sLC.substring(sLC.indexOf("HTCR[@seqno"));
				iHTCR = tmp.indexOf("HTCR[@seqno");
				iTSDT = tmp.indexOf("TSDT[@seqno");
				if (iTSDT > 0) {
					iHTCR = Integer.valueOf(tmp
							.substring(iHTCR + 13, iTSDT - 9));
				} else {
					iHTCR = Integer.valueOf(tmp.substring(iHTCR + 13, tmp
							.indexOf("\"]")));
				}
			}
			if (iTSDT > 0) {
				tmp = tmp.substring(tmp.indexOf("TSDT[@seqno"));
				iTSDT = tmp.indexOf("TSDT[@seqno");
				iTSDT = Integer.valueOf(tmp.substring(iTSDT + 13, tmp
						.indexOf("\"]")));
			}

			// 更新
			if (iHTCR == -1) { // 客户信息出错

				// 交易主体信息错误更新SERIAL_NUM_1
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R"); // 重发
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_1", String.valueOf(iCATI));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
				return true;
			}

			if (iTSDT == -1) { // 大额代码错

				
				// 更新SERIAL_NUM_2
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R"); // 重发
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_1", String.valueOf(iCATI));
				parameter.put("SERIAL_NUM_2", String.valueOf(iHTCR));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
				return true;
			}

			if (iTSDT > 0) { // 大额交易错
				
				// 更新SERIAL_NUM_3
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 上报错误
				parameter.put("REPORT_TYPE", "I"); // 重发
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_1", String.valueOf(iCATI));
				parameter.put("SERIAL_NUM_2", String.valueOf(iHTCR));
				parameter.put("SERIAL_NUM_3", String.valueOf(iTSDT));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
				return true;
			}
			return true;
			// lSqlArr
		} catch (Exception e) {
			Log.error(sLC + "解析失败");
			return false;
		}

	}
	
	/**
	 * 解析大额删除报文定位
	 * 
	 * @param retFileName 回执文件名称
	 * @param sLC 定位信息
	 * @param sInfo 信息
	 * @param type 回执类型
	 * @param sNodeName
	 * @return
	 */
	private boolean parseDBHLocation(String retFileName, String sLC,
			String sInfo, RetXmlType type, String sNodeName) {
		
		Map parameter = new HashMap();
		
		StringBuffer lsql = new StringBuffer("");
		StringBuffer lwhereclause = new StringBuffer("");
		bModifyStepInfo = true;
		// 解析大额删除报文回执
		try {
			int iDTDT = sLC.indexOf("DTDT[@seqno");
			if (iDTDT > 0) {
				iDTDT = Integer.valueOf(sLC.substring(iDTDT + 13, sLC
						.indexOf("\"]")));
			} else {
				
				// 交易基本信息错误
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 要求补正
				parameter.put("REPORT_TYPE", "I");
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
				return true;
			}

			if (iDTDT == -1) {
				
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 要求补正
				parameter.put("REPORT_TYPE", "I");// 删除
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
			} else {
				
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 要求补正
				parameter.put("REPORT_TYPE", "I");// 删除
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_3", String.valueOf(iDTDT));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
			}

		} catch (Exception e) {
			Log.error(sLC + "解析失败");
			return false;
		}
		return true;
	}
	
	/**
	 * 解析大额纠错、补正定位
	 * 
	 * @param retFileName 回执文件名称
	 * @param sLC 定位信息
	 * @param sInfo 信息
	 * @param type 回执类型
	 * @param sNodeName
	 * @return
	 */
	private boolean parseCBHLocation(String retFileName, String sLC,
			String sInfo, RetXmlType type, String sNodeName) {
		
		Map parameter = new HashMap();
		
		StringBuffer lsql = new StringBuffer("");
		StringBuffer lwhereclause = new StringBuffer("");
		bModifyStepInfo = true;
		// 解析大额纠错、补正报文回执
		try {
			int iTSDT = sLC.indexOf("TSDT[@seqno");
			if (iTSDT > 0) {
				iTSDT = Integer.valueOf(sLC.substring(iTSDT + 13, sLC
						.indexOf("\"]")));
			} else {
				
				// 交易基本信息错误
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 要求补正
				parameter.put("REPORT_TYPE", "I"); // 要求补正
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
				return true;
			}

			if (iTSDT == -1) {
				
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 要求补正
				parameter.put("REPORT_TYPE", "I"); // 要求补正
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
			} else {
				
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "22"); // 要求补正
				parameter.put("REPORT_TYPE", "I"); // 要求补正
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_3", String.valueOf(iTSDT));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
			}

		} catch (Exception e) {
			Log.error(sLC + "解析失败");
			return false;
		}
		return true;
	}
	
	/**
	 * 解析可疑正常报文定位
	 * 
	 * @param retFileName 回执文件名称
	 * @param sLC 定位信息
	 * @param sInfo 信息
	 * @param type 回执类型
	 * @param sNodeName
	 * @return
	 */
	private boolean parseNBSLocation(String retFileName, String sLC,
			String sInfo, RetXmlType type, String sNodeName) {
		
		Map parameter = new HashMap();
		
		StringBuffer lsql = new StringBuffer("");
		StringBuffer lwhereclause = new StringBuffer("");
		bModifyStepInfo = true;
		// 解析可疑正常报文错误回执
		try {
			int iCTIF = sLC.indexOf("CTIF[@seqno");
			int iATIF = sLC.indexOf("ATIF[@seqno");
			int iRPDI = sLC.indexOf("RPDI[@seqno");

			if (iCTIF > 0) {
				if (iATIF > 0) {
					iCTIF = Integer.valueOf(sLC
							.substring(iCTIF + 13, iATIF - 9));
				} else {
					iCTIF = Integer.valueOf(sLC.substring(iCTIF + 13, sLC
							.indexOf("\"]")));
				}

			}
			String tmp = "";
			if (iATIF > 0) {
				tmp = sLC.substring(iATIF);
				iATIF = tmp.indexOf("ATIF[@seqno");
				iATIF = Integer.valueOf(tmp.substring(iATIF + 13, tmp
						.indexOf("\"]")));
			}

			if (iRPDI > 0) {
				tmp = sLC.substring(iRPDI);
				iRPDI = tmp.indexOf("RPDI[@seqno");
				iRPDI = Integer.valueOf(tmp.substring(iRPDI + 13, tmp
						.indexOf("\"]")));
			}

			if ((iCTIF != -1) && (iATIF == -1)) { // 客户出错
				
				// 交易主体信息错误更新SERIAL_NUM_1
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R");
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_1", String.valueOf(iCTIF));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
				
			} else if (iATIF > 0) { // 账户出错
				
				// 更新SERIAL_NUM_2
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R");
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_1", String.valueOf(iCTIF));
				parameter.put("SERIAL_NUM_2", String.valueOf(iATIF));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
			} else if (iRPDI > 0) { // 交易出错
				
				// 更新SERIAL_NUM_3
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R");
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				parameter.put("SERIAL_NUM_3", String.valueOf(iRPDI));
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
			} else {
				
				// 交易基本信息错误
				parameter = new HashMap();
				parameter.put("RPT_STATUS", "21"); // 上报错误
				parameter.put("REPORT_TYPE", "R");
				parameter.put("RET_MSE", sInfo);
				parameter.put("RPT_FILE", this.fileName);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.updateReceiveError", parameter);
			}

		} catch (Exception e) {
			Log.error(sLC + "解析失败");
			return false;
		}
		return true;
	}

	private String formatDateTime(String format, Date date) {
		SimpleDateFormat simpleDateFormat = null;
		String result = null;
		try {
			simpleDateFormat = new SimpleDateFormat(format);
			result = simpleDateFormat.format(date);
			return result;
		} finally {
			simpleDateFormat = null;
			result = null;
		}
	}
	
	public MyBatisSessionTemplate getMyBatisSessionTemplate() {
		return myBatisSessionTemplate;
	}
	
	public void setMyBatisSessionTemplate(
			MyBatisSessionTemplate myBatisSessionTemplate) {
		this.myBatisSessionTemplate = myBatisSessionTemplate;
	}

	public String getReceiptFileName() {
		return receiptFileName;
	}

	public void setReceiptFileName(String receiptFileName) {
		this.receiptFileName = receiptFileName;
	}

	public String getReceiptFilePath() {
		return receiptFilePath;
	}

	public void setReceiptFilePath(String receiptFilePath) {
		this.receiptFilePath = receiptFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}
}
