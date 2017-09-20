package com.aif.rpt.biz.aml.receipt.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.aif.rpt.common.server.BizFuncService;
import com.aif.rpt.common.server.ContextService;
import com.allinfinance.yak.ubase.orm.mybatis.template.MyBatisSessionTemplate;
import com.allinfinance.yak.uface.api.core.serivce.callback.Callback;
import com.allinfinance.yak.uface.core.serivce.callback.CallbackImpl;

public class ReceiptImportService {
	
	private MyBatisSessionTemplate myBatisSessionTemplate;

	public MyBatisSessionTemplate getMyBatisSessionTemplate() {
		return myBatisSessionTemplate;
	}
	
	public void setMyBatisSessionTemplate(
			MyBatisSessionTemplate myBatisSessionTemplate) {
		this.myBatisSessionTemplate = myBatisSessionTemplate;
	}
	
	public Callback checkFile(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback cb = new CallbackImpl();
		String workDate = dataMap.get("WORK_DATE");
		String absolutePath = dataMap.get("FILE_PATH");
		int lastFileSeparatorIndex = absolutePath.lastIndexOf(File.separator);
		//modify by wzf 20140612 begin
		//获取文件名称将”\“号去掉	
//		String uploadFileName = absolutePath.substring(lastFileSeparatorIndex);
		String uploadFileName = absolutePath.substring(lastFileSeparatorIndex+1);
		//modify by wzf 20140612 end
		String filePath = absolutePath.substring(0, lastFileSeparatorIndex + 1);
		String amlCode = BizFuncService.getAmlCode(ContextService.getDepartId(header));
		//delete by wzf 20140612 begin
		//在上传文件时系统已经做过校验,此处不再做校验
//		String fileName = amlCode + "-" + workDate + ".ZIP";
//		if (fileName.equalsIgnoreCase(uploadFileName)) {
//			cb.setErrorCode("-1");
//			cb.setErrorMessage("回执文件不存在，请上传回执文件");
//			return cb;
//		}
		//delete by wzf 20140612 end
		// 查询上报文件表获取上报文件信息
		List<Map> reportedFileList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.selectReportedFiles", dataMap);
		if (reportedFileList == null || reportedFileList.size() <= 0) {
			cb.setErrorCode("-1");
			cb.setErrorMessage("前日期下，数据库里没有需要导入回执的数据，请核查！");
			return cb;
		}
		
		// 解压文件
		//modify by wzf 20140612 begin
		//直接使用前台界面中的文件名
//		List<String> fileList = unZip(filePath,fileName);
		List<String> fileList = unZip(filePath,uploadFileName);		
		//modify by wzf 20140612 end
		// FILE_NAME-FDSC[FILE_NAME].xml
		Map<String,String> fileMap = new HashMap<String,String>();
		String file;
		for (String tempFile : fileList) {
			file = tempFile.substring(tempFile.indexOf("[") + 1, tempFile.indexOf("]"));
			fileMap.put(file, tempFile);
		}
		StringBuffer missFileName = new StringBuffer();
		String dbFileName;
		String receiveFileName;
		String prefix;
		for (Map tempMap : reportedFileList) {
			dbFileName = (String) tempMap.get("FILE_NAME");
			dbFileName = dbFileName.substring(0, dbFileName.length() - 4);
			if (!fileMap.containsKey(dbFileName)) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("缺少回执文件[" + dbFileName + "]");
				return cb;
			}
			
			if (fileMap.containsKey(dbFileName)) {
				receiveFileName = fileMap.get(dbFileName);
				// 回执文件状态 0-不存在 1-存在
				tempMap.put("RECEIPT_FILE_STATUS", "1");
				// 回执文件名
				tempMap.put("RECEIPT_FILE_NAME", receiveFileName);
				prefix = receiveFileName.substring(0, 4);
				// FDSC-00-正确回执
				// FDCF-01-内容错误回执
				// FDRC-02 -要求补正回执
				if ("FDSC".equals(prefix)) {
					tempMap.put("RECEIPT_FILE_TYPE", "00");
				} else if ("FDCF".equals(prefix)) {
					tempMap.put("RECEIPT_FILE_TYPE", "01");
				} else if ("FDRC".equals(prefix)) {
					tempMap.put("RECEIPT_FILE_TYPE", "02");
				}
			} else {
				tempMap.put("RECEIPT_FILE_STATUS", "0");
			}
			// 文件路径(不包括文件名)
			tempMap.put("FILE_PATH", filePath);
		}
		cb.setCallbackData(reportedFileList);
		return cb;
	}
	
	public Callback importReceiveFile(final Map<String, String> header, final List<Map<String, String>> dataList) {
		Callback cb = new CallbackImpl();
		RetXmlFileImpService retXmlFileImpService = new RetXmlFileImpService();
		String departId = ContextService.getDepartId(header);
		String workDate;
		// 回执文件类型 FDSC-00-正确回执 FDCF-01-内容错误回执 FDRC-02 -要求补正回执
		String receiptFileType;
		// 回执文件名
		String receiptFileName;
		// 文件路径
		String receiptFilePath;
		// 文件名称
		String fileName;
		Map parameter;
		Map<String,String> currentRecord;
		// 回执文件导入处理service
		RetXmlFileImpService xmlFileService = new RetXmlFileImpService();
		xmlFileService.setDepartId(departId);
		boolean importStatus;
		for (int i = 0; i < dataList.size(); i++) {
			currentRecord = dataList.get(i);
			workDate = currentRecord.get("WORK_DATE");
			receiptFileType = currentRecord.get("RECEIPT_FILE_TYPE");
			receiptFileName = currentRecord.get("RECEIPT_FILE_NAME");
			receiptFilePath = currentRecord.get("FILE_PATH");
			fileName = currentRecord.get("FILE_NAME");
			
			xmlFileService.setWorkDate(workDate);
			// xmlFileService.setReceiptFileType(receiptFileType);
			xmlFileService.setReceiptFileName(receiptFileName);
			xmlFileService.setReceiptFilePath(receiptFilePath);
			xmlFileService.setFileName(fileName);
			
			importStatus = xmlFileService.impRetXmlFile();
			if (importStatus) {
				currentRecord.put("IMPORT_STATUS", "1");
			} else {
				currentRecord.put("IMPORT_STATUS", "2");
			}
		}
		return cb;
	}
	
	private List<String> unZip(String filePath,String fileName) {
		List<String> fileList = new ArrayList<String>();
		try {
    		
    		ZipFile zipFile = null;
    		// 文件后缀名zip不区分大小写
    		try {
    			zipFile = new ZipFile(filePath + fileName.toUpperCase());
    		} catch(Exception e) {
    			e.printStackTrace();
    			zipFile = new ZipFile(filePath + fileName.toLowerCase());
    		}
    		
    		Enumeration emu = zipFile.entries();

    		while(emu.hasMoreElements()){
	    		ZipEntry entry = (ZipEntry) emu.nextElement();
	    		//会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
	    		if (entry.isDirectory()){
		    		new File(filePath + entry.getName()).mkdirs();
		    		continue;
	    		}
	    
	    		BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
	    		fileList.add(entry.getName());
	    		File file = new File(filePath + entry.getName());
	    		//加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
	    		//而这个文件所在的目录还没有出现过，所以要建出目录来。
	    		File parent = file.getParentFile();
	    		if(parent != null && (!parent.exists())){
	    			parent.mkdirs();
	    		}
	    		FileOutputStream fos = new FileOutputStream(file);
	    		BufferedOutputStream bos = new BufferedOutputStream(fos,4026);
	    		int count;
	    		byte data[] = new byte[4026];
	    		while ((count = bis.read(data, 0, 4026)) != -1){
	    			bos.write(data, 0, count);
	    		}
	    		bos.flush();
	    		bos.close();
	    		bis.close();
	    	}
    		zipFile.close();
    	}catch (Exception e) {
    		e.printStackTrace();
    	} 
		return fileList;
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
			
			String departId = ContextService.getDepartId(header);
			dataMap.put("DEPART_ID", departId);

			String workDate = dataMap.get("REPORT_DATA_DATE");
			
			// 是否有数据未上报完成
			List<Map> retList = myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.selectNotFinishedReportData", dataMap);
			if (retList != null && retList.size() > 0) {
				cb.setErrorCode("-1");
				cb.setErrorMessage("有未上报完成的数据，不能进行完成确认!");
				return cb;
			}
			
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

			String workDate = dataMap.get("REPORT_DATA_DATE");
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
	
	public static void main(String[] args) {
		new ReceiptImportService().unZip("C:\\Users\\gaohw\\Desktop\\","010093100009988-20140321-0004.ZIP");
		/*Map map = new HashMap();
		map.put("key1", "20110202");
		System.out.println(map.get("KEY1"));*/
	}
}
