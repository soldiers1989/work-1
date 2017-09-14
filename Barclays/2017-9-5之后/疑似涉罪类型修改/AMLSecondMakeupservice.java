package com.aif.rpt.biz.aml.processing.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import bsh.ParseException;

import com.aif.rpt.common.server.BasFuncService;
import com.aif.rpt.common.server.BizFuncService;
import com.aif.rpt.common.server.BusiException;
import com.aif.rpt.common.server.ContextService;
import com.aif.rpt.common.server.DateUtilsC;
import com.aif.rpt.common.server.SysParamService;
import com.aif.rpt.imp.server.ImportFileVar;
import com.aif.rpt.sys.client.SysParams;
import com.allen_sauer.gwt.log.client.Log;
import com.allinfinance.yak.ubase.api.orm.mybatis.exception.UMybatisDataAccessException;
import com.allinfinance.yak.ubase.data.page.Page;
import com.allinfinance.yak.ubase.orm.mybatis.template.MyBatisSessionTemplate;
import com.allinfinance.yak.ubase.util.date.UDateUtils;
import com.allinfinance.yak.ubase.util.uuid.UUuidUtils;
import com.allinfinance.yak.uface.api.core.serivce.callback.Callback;
import com.allinfinance.yak.uface.core.client.utils.DateUtils;
import com.allinfinance.yak.uface.core.serivce.callback.CallbackImpl;
import com.allinfinance.yak.uface.data.client.dataset.record.DataRecord;
import com.allinfinance.yak.uface.service.server.datamodule.pool.DataModulePool;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt.PromptCallback;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

public class AMLSecondMakeupservice {

	private String type = "AML第二次补录";
	

	private MyBatisSessionTemplate myBatisSessionTemplate;

	public void setMyBatisSessionTemplate(MyBatisSessionTemplate myBatisSessionTemplate) {
		this.myBatisSessionTemplate = myBatisSessionTemplate;
	}


	/**
	 * 查询已上传文件uploadedShow
	 * @param header
	 * @param dataMap
	 * @return
	 */
	public Page<Object> uploadedShow(final Map<String, String> header, Map<String, String> dataMap) {
		dataMap.put("ORDERBY", "LOAD_DATE DESC");
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLAccSql.query_AMLAcc", dataMap, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1), BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
	}
//	public List<Object> uploadedShow(final Map<String, String> header, Map<String, String> dataMap) {
//		//dataMap.put("ORDERBY", "UPLOAD_TIME DESC");
//		System.out.println("-----------前面！！");
//		return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLAccSql.query_AMLAcc", dataMap);
//	}
	/**
	 * 在数据库中插入已上传文件
	 * @param header
	 * @param dataMap
	 * @return
	 */
	public Callback insertAcc(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback cb = new CallbackImpl();
		
		String uploadFilePath = "";
		uploadFilePath = SysParamService.sysParamMap.get("0003|0004").get("PARAM_VAL");
		uploadFilePath = uploadFilePath+"\\"+dataMap.get("ACC_NAME");
		
		//获取当前所选数据业务流水号--前台已获取
		//插入已上传附件到附件表
		//dataMap.put("REF_NO", "RF-FX3066693X1501290025");//业务流水
		//dataMap.put("ACC_NAME", "11");//附件名称
		dataMap.put("ACC_PATH", uploadFilePath);//附件路径
		dataMap.put("LOAD_DATE", DateUtilsC.timeToNumber(new Date()));//上传日期
		dataMap.put("LOADER", ContextService.getUserName(header));//上传人
		dataMap.put("CHECK_STATUS", "0");//审核状态
		//dataMap.put("CHECK_TIME", "11");//审核时间
		//dataMap.put("CHECKER", "11");//审核人--审核页面上是当前用户
		dataMap.put("IS_DOWNLOAD", "否");//是否下载
		dataMap.put("IS_UPLOAD", "是");//是否上传
		try{
			myBatisSessionTemplate.insert("com.aif.rpt.biz.aml.processing.server.AMLAccSql.insert_AMLAcc", dataMap);
		}catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			cb.setErrorCode("-1");
			cb.setErrorMessage("附件表数据插入错误：" + e.getMessage());
			return cb;
		}
		return cb;
	}
	
	/**
	 * 解析msg文件，并获取msg文件的内容
	 * @param dataMap
	 * @return Message
	 */
	public Message getMessage(Map<String, String> dataMap) {
		
		Message msg = null;
		String accPath = dataMap.get("accPath");
		MsgParser msgp = new MsgParser();
		System.out.println("--------------------accPath----------"+accPath);
		try {
			msg = msgp.parseMsg(accPath);
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}
	
	/**
	 * 在数据库中更新已下载文件
	 * @param header
	 * @param dataMap
	 * @return
	 */
	public Callback updateAcc(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback cb = new CallbackImpl();
		dataMap.put("IS_DOWNLOAD", "是");//是否下载
		try{
			myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLAccSql.update_AMLAcc", dataMap);
		}catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			cb.setErrorCode("-1");
			cb.setErrorMessage("附件表数据更新错误：" + e.getMessage());
			return cb;
		}
		return cb;
	}
	
	/**
	 * @param header
	 * @param map
	 * @return AML的第二次补录大额补录查询
	 */
	public Page<Object> selectAMLSecondMakeup(final Map<String, String> header, Map<String, String> map) {
		map.put("ORDERBY", "P.BATCH_NO");
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectAMLSecondMakeup", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}

	/**
	 * @param header
	 * @param map
	 * @return AML的第二次补录可疑补录查询
	 */
	public Page<Object> selectAMLSecondMakeupSuspicious(final Map<String, String> header, Map<String, String> map) {
		map.put("ORDERBY", "P.BATCH_NO");
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectAMLSecondMakeupSuspicious", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}

	/**
	 * @param header
	 * @param map
	 * @return 大额交易REF_NO的条件查询
	 */
	public List<Object> selectRefNo01(final Map<String, String> header, Map<String, String> map) {
	//add by pw	
		/*return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectRefNo01", map);
		*/    
		return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectRefNo00", map);
	
	}

	/**
	 * @param header
	 * @param map
	 * @return 大额交易REF_NO的条件查询
	 */
	public List<Object> selectRefNo02(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectRefNo03", map);
	}

	/**
	 * @param header
	 * @param map
	 * @return 规则代码查询
	 */
	public Page<Object> selectRulecode(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectRulecode", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}

	/**
	 * @param header
	 * @param map
	 * @return 国家地区代码查询
	 */
	public Page<Object> selectNationregion(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectNationregion2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}

	/**
	 * @param header
	 * @param map
	 * @return 机构代码查询
	 */
	public List<Object> selectDepart(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.showDepartList2", map);
	}

	/**
	 * @param header
	 * @param map
	 * @return 涉外收支交易分类与代码
	 */
	public Page<Object> selectMeta(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectMeta2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}
	
	/**
	 * @param header
	 * @param map
	 * @return 疑似涉罪类型
	 */
	public Page<Object> selectBsTosc(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectBsTosc2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));

	}
	
	/**
	 * @param header
	 * @param map
	 * @return 客户信息查询
	 */
	public Page<Object> selectClientList(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectClientList2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
	}

	/**
	 * @param header
	 * @param map
	 * @return 账户信息查询
	 */
	public Page<Object> selectAcctList(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectAcctList2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
	}

	/**
	 * @param header
	 * @param map
	 * @return 对方金融机构信息查询
	 */
	public Page<Object> selectOrganizationList(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectOrganizationList2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
	}

	/**
	 * @param header
	 * @param map
	 * @return 交易对手信息查询
	 */
	public Page<Object> selectCounterpartyList(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.selectCounterpartyList2", map, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
				BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
	}

	/**
	 * @param header
	 * @param map
	 * @return 报告机构代码
	 */
	public List<Object> selectUnitCode(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeupUnitCode", map);
	}
	
	/**
	 * @param header
	 * @param map
	 * @return 唯一标识
	 */
	public Callback selectRuleCodeAndRefNo(final Map<String, String> header, Map<String, String> map) {
		Callback rb = new CallbackImpl();

		Integer total = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeupTotal", map);
	    
		if(total.intValue() != 0){
			rb.setErrorCode("-1");
			rb.setErrorMessage("业务流水或规则代码输入错误!该笔业务违反该规则的记录已经存在于数据库中!");
			return rb;
		}
		return rb;
	}

	
	/**
	 * @param header
	 * @param map
	 * @return 进行汇率查询
	 */
	public String selectParites(final Map<String, String> header, Map<String, String> map) {
		String currency = map.get("CURRENCY");
		String amount = map.get("AMT");
		String work_date = map.get("WORK_DATE");
		return String.valueOf(BizFuncService.getParities(currency, amount, work_date));
	}
	
	/**
	 * @param header
	 * @param map
	 * @return 进行相对人民币汇率查询
	 */
	public String selectParitesRMB(final Map<String, String> header, Map<String, String> map) {
		String currency = map.get("CURRENCY");
		String amount = map.get("AMT");
		String work_date = map.get("WORK_DATE");
		return String.valueOf(BizFuncService.getParitiesRMB(currency, amount, work_date));
	}
	
	/**
	 * @param header
	 * @param dataMap
	 * @return 取消锁
	 */
	public Callback getunlock(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();

		String tableName = "AML_ANALYSISRESULT";
		String workDate = dataMap.get("WORK_DATE");
		String refNO = dataMap.get("REF_NO");
		String ruleCode = dataMap.get("RULE_CODE");
		String locker = ContextService.getUserName(header);
		try {

			BizFuncService.unLock(tableName, workDate, refNO+ruleCode, locker);
		} catch (BusiException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("AML第二次补录信息解锁失败，错误信息：" + e.getMessage());
			return rb;
		}
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return 业务修改检查
	 */
	public Callback check(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();

		String tableName = "AML_ANALYSISRESULT";
		String workDate = dataMap.get("WORK_DATE");
		String refNO = dataMap.get("REF_NO");
		String ruleCode = dataMap.get("RULE_CODE");
		String locker = ContextService.getUserName(header);

		try {

			String checkStatus = BizFuncService.getCheckStatus(tableName, workDate, refNO, ruleCode);
			if ("0".equals(checkStatus)||"2".equals(checkStatus)) {
				String deleteStatus = BizFuncService.getDeleteStatus(tableName, workDate, refNO ,ruleCode);
				if ("0".equals(deleteStatus)) {
					String lock = BizFuncService.isLock(tableName, workDate, refNO+ruleCode, locker);
					if ("0".equals(lock)) {
						BizFuncService.getLock(tableName, workDate, refNO+ruleCode, locker);
					}else if ("2".equals(lock)) {
						return rb;
					} else {
						rb.setErrorMessage("AML第二次补录信息已经被锁定，不能修改！");
						return rb;
					}
				} else {
					rb.setErrorMessage("AML第二次补录信息已经删除，不能修改！");
				}
			} else {
				rb.setErrorMessage("AML第二次补录信息已经审核，不能修改！");

			}
		} catch (BusiException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("AML第二次补录信息检查失败，错误信息：" + e.getMessage());
			return rb;
		}
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return 业务删除检查
	 */
	public Callback checkdelete(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();

		String tableName = "AML_ANALYSISRESULT";
		String workDate = dataMap.get("WORK_DATE");
		String refNO = dataMap.get("REF_NO");
		String ruleCode = dataMap.get("RULE_CODE");
		String locker = ContextService.getUserName(header);
		try {

			String checkStatus = BizFuncService.getCheckStatus(tableName, workDate, refNO,ruleCode);
			if ("0".equals(checkStatus)||"2".equals(checkStatus)) {
				String lock = BizFuncService.isLock(tableName, workDate, refNO+ruleCode, locker);
				if ("0".equals(lock)) {
					BizFuncService.getLock(tableName, workDate, refNO+ruleCode, locker);
				}else if ("2".equals(lock)) {
					return rb;
				} else {
					rb.setErrorMessage("AML第二次补录信息已经被锁定，不能删除！");
					return rb;
				}
			} else {
				rb.setErrorMessage("AML第二次补录信息已经审核，不能删除！");
				return rb;
			}
		} catch (BusiException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("AML第二次补录信息检查失败，错误信息：" + e.getMessage());
			return rb;
		}
		return rb;
	}
	
	/**
	 * @param header
	 * @param dataMap
	 * @return 业务延迟上报检查
	 */
	public Callback checkdelay(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();

		String tableName = "AML_ANALYSISRESULT";
		String workDate = dataMap.get("WORK_DATE");
		String refNO = dataMap.get("REF_NO");
		String ruleCode = dataMap.get("RULE_CODE");
		String locker = ContextService.getUserName(header);
		try {

			String checkStatus = BizFuncService.getCheckStatus(tableName, workDate, refNO,ruleCode);
			if ("0".equals(checkStatus)||"2".equals(checkStatus)) {
				String makeStatus = BizFuncService.getMakeStatus(tableName, workDate, refNO,ruleCode);
				if("1".equals(makeStatus)||"2".equals(makeStatus)){
					String lock = BizFuncService.isLock(tableName, workDate, refNO+ruleCode, locker);
					if ("0".equals(lock)) {
						BizFuncService.getLock(tableName, workDate, refNO+ruleCode, locker);
					}else if ("2".equals(lock)) {
						return rb;
					} else {
						rb.setErrorMessage("AML第二次补录信息已经被锁定，不能延迟上报！");
						return rb;
					}
				} else {
					rb.setErrorMessage("AML第二次补录信息未补录，不能延迟上报！");
					return rb;
				}
				
			} else {
				rb.setErrorMessage("AML第二次补录信息已经审核，不能延迟上报！");
				return rb;
			}
		} catch (BusiException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("AML第二次补录信息检查失败，错误信息：" + e.getMessage());
			return rb;
		}
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录大额手工新增
	 */
	public Callback addAMLSecond01(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		String secondLargeValidatorservice = AMLBizValidatorService.secondLargeValidatorservice(dataMap);
		if (secondLargeValidatorservice != null) {
			rb.setErrorCode("-1");
			rb.setErrorMessage(secondLargeValidatorservice);
			return rb;
		}
		if(dataMap.get("ACCT_OPEN_TIME").length() == 8){
			dataMap.put("ACCT_OPEN_TIME", dataMap.get("ACCT_OPEN_TIME")+"tt");	
		}
		if(dataMap.get("ACCT_CLOSE_TIME").length() == 8){
		dataMap.put("ACCT_CLOSE_TIME", dataMap.get("ACCT_CLOSE_TIME")+"tt");
		}
		dataMap.put("Maker", ContextService.getUserName(header));
		dataMap.put("MakeTime", DateUtilsC.timeToNumber(new Date()));
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		String operationType = dataMap.get("operationType");
		if (operationType != null && !"".equals(operationType)) {
			if ("0".equals(operationType)) {
				/**
				 * 新增时上报状态等信息的添加
				 */
				dataMap.put("BATCH_NO", "0");
				dataMap.put("MAKE_STATUS", "2");
				dataMap.put("CHECK_STATUS", "0");
				dataMap.put("IS_DEL", "0");
				dataMap.put("REPORT_TYPE", "N");
				dataMap.put("RPT_STATUS", "10");
				//add by pw begin 
				//超过5天为补报
				String departId = ContextService.getDepartId(header);
				String calendarNo = BizFuncService.getCalendarNoByDepart(departId);
				Date tradedate = UDateUtils.numberToDate(BizFuncService.getSpecialWorkDate(calendarNo,dataMap.get("TRADE_DATE"),0));
				String work_date=SysParamService.getWorkDate(ContextService.getDepartId(header));			
				//String work_date=dataMap.get("WORK_DATE");
				String beginDate = BizFuncService.getSpecialWorkDate(calendarNo, work_date, -5);
				Date nowdate=UDateUtils.numberToDate(beginDate);
				//Date nowdate = new Date();
				int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
				/*if(usedDays > 30) {
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易已超过30天请确认是否已申请补报,并获批准?");
					return rb;
				}*/
//				if(usedDays>0){
//					rb.setErrorCode("-1");
//					rb.setErrorMessage("交易日期不在规定时间内，请选择补报");
//					return rb;
//				}
				//end
				
			} else {
				/**
				 * 补报时上报状态等信息的添加
				 */
				dataMap.put("BATCH_NO", "0");
				dataMap.put("MAKE_STATUS", "2");
				dataMap.put("CHECK_STATUS", "0");
				dataMap.put("IS_DEL", "0");
				dataMap.put("REPORT_TYPE", "A");
				dataMap.put("RPT_STATUS", "10");
				//add by pw begin
				String departId = ContextService.getDepartId(header);
				String calendarNo = BizFuncService.getCalendarNoByDepart(departId);
				Date tradedate = UDateUtils.numberToDate(BizFuncService.getSpecialWorkDate(calendarNo,dataMap.get("TRADE_DATE"),0));
				String work_date=SysParamService.getWorkDate(ContextService.getDepartId(header));			
				String beginDate = BizFuncService.getSpecialWorkDate(calendarNo, work_date, -5);
				Date nowdate=UDateUtils.numberToDate(beginDate);
				//Date nowdate = new Date();
				int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
				if(usedDays<0){
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易日期不在规定时间内，请选择新增");
					return rb;
				}
				/*int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
				if(usedDays > 30) {
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易已超过30天请确认是否已申请补报,并获批准?");
					return rb;
				}*/
				//end
			}
		}
		try {
			// 业务流水的检查
			Integer total = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeupTotal", dataMap);
			if (total.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeup011", dataMap);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup01", dataMap);
				// 业务流水的保存
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupRefNo", dataMap);
			} else {
				rb.setErrorCode("-1");
				rb.setErrorMessage("业务流水或规则代码输入错误!该笔业务违反该规则的记录已经存在于数据库中!");
				return rb;
			}
				// 判断对方金融机构信息是否存在数据库
				Integer total1 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupOrganization", dataMap);
				if (total1.intValue() == 0) {
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupOrganization", dataMap);
				}else if(total1.intValue() == 1){
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupOrganization", dataMap);	
				}
				// 判断交易对手信息是否存在数据库
				Integer total2 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupAccont", dataMap);
				if (total2.intValue() == 0) {
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupAccont", dataMap);
				}else if(total2.intValue() == 1){
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupAccont", dataMap);						
				}
		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("更新AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息保存成功！");
		return rb;
	}
	

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录大额新增删除记录
	 */
	public Callback addAMLSecond03(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		/*String secondLargeValidatorservice = AMLBizValidatorService.secondLargeValidatorservice(dataMap);
		if (secondLargeValidatorservice != null) {
			rb.setErrorCode("-1");
			rb.setErrorMessage(secondLargeValidatorservice);
			return rb;
		}*/
		if(dataMap.get("ACCT_OPEN_TIME").length() == 8){
			dataMap.put("ACCT_OPEN_TIME", dataMap.get("ACCT_OPEN_TIME")+"tt");	
		}
		if(dataMap.get("ACCT_CLOSE_TIME").length() == 8){
		dataMap.put("ACCT_CLOSE_TIME", dataMap.get("ACCT_CLOSE_TIME")+"tt");
		}
		dataMap.put("Maker", ContextService.getUserName(header));
		dataMap.put("MakeTime", DateUtilsC.timeToNumber(new Date()));
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		
		/**
		 * 新增时上报状态等信息的添加
		 */
		dataMap.put("BATCH_NO", "0");
		dataMap.put("MAKE_STATUS", "2");
		dataMap.put("CHECK_STATUS", "0");
		dataMap.put("IS_DEL", "0");
		dataMap.put("REPORT_TYPE", "D");
		dataMap.put("RPT_STATUS", "10");
		String operationType = dataMap.get("operationType");
		/*if (operationType != null && !"".equals(operationType)) {
			*//**
			 * 新增时上报状态等信息的添加
			 *//*
			dataMap.put("BATCH_NO", "0");
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("CHECK_STATUS", "0");
			dataMap.put("IS_DEL", "0");
			dataMap.put("REPORT_TYPE", "D");
			dataMap.put("RPT_STATUS", "10");*/
			/*if (operationType == "0") {
				*//**
				 * 新增时上报状态等信息的添加
				 *//*
				dataMap.put("BATCH_NO", "0");
				dataMap.put("MAKE_STATUS", "2");
				dataMap.put("CHECK_STATUS", "0");
				dataMap.put("IS_DEL", "0");
				dataMap.put("REPORT_TYPE", "N");
				dataMap.put("RPT_STATUS", "10");
			} else {
				*//**
				 * 补报时上报状态等信息的添加
				 *//*
				dataMap.put("BATCH_NO", "0");
				dataMap.put("MAKE_STATUS", "2");
				dataMap.put("CHECK_STATUS", "0");
				dataMap.put("IS_DEL", "0");
				dataMap.put("REPORT_TYPE", "A");
				dataMap.put("RPT_STATUS", "10");
				Date tradedate = UDateUtils.numberToDate(dataMap.get("TRADE_DATE"));		
				Date nowdate = new Date();
				int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
				if(usedDays > 30) {
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易已超过30天请确认是否已申请补报,并获批准?");
					return rb;
				}
			}*/
		//}
		try {
			// 业务流水的检查
			Integer total =(Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeupTotal", dataMap);
			if (total.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeup013", dataMap);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup01", dataMap);
				// 业务流水的保存
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupRefNo", dataMap);
			} else {
				rb.setErrorCode("-1");
				rb.setErrorMessage("删除失败，该笔业务已删除!");
				return rb;
			}
				// 判断对方金融机构信息是否存在数据库
				Integer total1 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupOrganization", dataMap);
				if (total1.intValue() == 0) {
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupOrganization", dataMap);
				}else if(total1.intValue() == 1){
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupOrganization", dataMap);	
				}
				// 判断交易对手信息是否存在数据库
				Integer total2 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupAccont", dataMap);
				if (total2.intValue() == 0) {
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupAccont", dataMap);
				}else if(total2.intValue() == 1){
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupAccont", dataMap);						
				}
		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("更新AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息保存成功！");
		return rb;
	}


	/**
	 * @param header
	 * @param dataMap
	 * @return 可疑业务批次号检查
	 */
	public Callback checkBatchNo(final Map<String, String> header, final Map<String, String> dataMap) throws BusiException {
		Callback rb = new CallbackImpl();

		Integer total = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkBatchNo", dataMap);
		if (total > 0) {
			rb.setErrorCode("-1");
			rb.setErrorMessage("该批次下的数据已经全部作为纠错报文新增到当前工作日，请到当前工作日数据中进行新增!");
			return rb;
		}
		rb.setSuccessMessage("该批次下数据可以进行手工新增!");
		return rb;
	}

	/**
	 * @param header
	 * @param map
	 * @return 可疑业务批次号查询
	 */
	public List<Object> queryBatchNo(final Map<String, String> header, Map<String, String> map) {
		return myBatisSessionTemplate.selectList("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.queryBatchNo", map);
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录可疑手工新增
	 */
	public Callback addAMLSecond02(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		String secondSuspiciousValidatorService = AMLBizValidatorService.secondSuspiciousValidatorService(dataMap);
		if (secondSuspiciousValidatorService != null) {
			rb.setErrorCode("-1");
			rb.setErrorMessage(secondSuspiciousValidatorService);
			return rb;
		}

		dataMap.put("Maker", ContextService.getUserName(header));
		dataMap.put("MakeTime", DateUtilsC.timeToNumber(new Date()));
		if(dataMap.get("ACCT_OPEN_TIME").length() == 8){
			dataMap.put("ACCT_OPEN_TIME", dataMap.get("ACCT_OPEN_TIME")+"tt");	
		}
		if(dataMap.get("ACCT_CLOSE_TIME").length() == 8){
		dataMap.put("ACCT_CLOSE_TIME", dataMap.get("ACCT_CLOSE_TIME")+"tt");
		}
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		String operationType = dataMap.get("operationType");
		if (operationType != null && !"".equals(operationType)) {
			if (operationType.equals("0")) {
				/**
				 * 新增时上报状态等信息的添加
				 */
				dataMap.put("BATCH_NO", "0");
				dataMap.put("MAKE_STATUS", "2");
				dataMap.put("CHECK_STATUS", "0");
				dataMap.put("IS_DEL", "0");
				dataMap.put("REPORT_TYPE", "N");
				dataMap.put("RPT_STATUS", "10");
				//add by pw begin 
				//超过5天为补报
				String departId = ContextService.getDepartId(header);
				String calendarNo = BizFuncService.getCalendarNoByDepart(departId);
				Date tradedate = UDateUtils.numberToDate(BizFuncService.getSpecialWorkDate(calendarNo,dataMap.get("TRADE_DATE"),0));
				String work_date=SysParamService.getWorkDate(ContextService.getDepartId(header));			
				//String work_date=dataMap.get("WORK_DATE");
				String beginDate = BizFuncService.getSpecialWorkDate(calendarNo, work_date, -10);
				Date nowdate=UDateUtils.numberToDate(beginDate);
				//Date nowdate = new Date();
				int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
				/*if(usedDays > 30) {
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易已超过30天请确认是否已申请补报,并获批准?");
					return rb;
				}*/
				if(usedDays>0){
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易日期不在规定时间内，请选择补报");
					return rb;
				}
				//end
			} else {
				/**
				 * 补报时上报状态等信息的添加
				 */
				dataMap.put("BATCH_NO", "0");
				dataMap.put("MAKE_STATUS", "2");
				dataMap.put("CHECK_STATUS", "0");
				dataMap.put("IS_DEL", "0");
				dataMap.put("REPORT_TYPE", "A");
				dataMap.put("RPT_STATUS", "10");
				//add by pw begin
				String departId = ContextService.getDepartId(header);
				String calendarNo = BizFuncService.getCalendarNoByDepart(departId);
				Date tradedate = UDateUtils.numberToDate(BizFuncService.getSpecialWorkDate(calendarNo,dataMap.get("TRADE_DATE"),0));
				String work_date=SysParamService.getWorkDate(ContextService.getDepartId(header));			
				String beginDate = BizFuncService.getSpecialWorkDate(calendarNo, work_date, -10);
				Date nowdate=UDateUtils.numberToDate(beginDate);
				//Date nowdate = new Date();
				int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
				if(usedDays<0){
					rb.setErrorCode("-1");
					rb.setErrorMessage("交易日期不在规定时间内，请选择新增");
					return rb;
				}
				//end
			}
		}
		try {
			Integer total = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeupTotal1", dataMap);
			if (total.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.addAMLSecondMakeup021", dataMap);
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup02", dataMap);
				if (dataMap.get("ifRpt_flag") != null) {
					myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateIfRptList", dataMap);
				}
				// 业务流水的保存
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupRefNo", dataMap);
			} else {
				rb.setErrorCode("-1");
				rb.setErrorMessage("业务流水或规则代码输入错误!该笔业务违反该规则的记录已经存在于数据库中!");
				return rb;
			}

			// 判断对方金融机构信息是否存在数据库
			Integer total1 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupOrganization", dataMap);
			if (total1.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupOrganization", dataMap);
			}else if(total1.intValue() == 1){
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupOrganization", dataMap);	
			}
			// 判断交易对手信息是否存在数据库
			Integer total2 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupAccont", dataMap);
			if (total2.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupAccont", dataMap);
			}else if(total2.intValue() == 1){
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupAccont", dataMap);						
			}

		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("更新AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息保存成功！");
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录大额修改
	 */
	public Callback updateAMLSecond01(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		String secondLargeValidatorservice = AMLBizValidatorService.secondLargeValidatorservice(dataMap);
		if (secondLargeValidatorservice != null) {
			rb.setErrorCode("-1");
			rb.setErrorMessage(secondLargeValidatorservice);
			return rb;
		}
		dataMap.put("Maker", ContextService.getUserName(header));
		dataMap.put("MakeTime", DateUtilsC.timeToNumber(new Date()));
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		if(dataMap.get("ACCT_OPEN_TIME").length() == 8){
			dataMap.put("ACCT_OPEN_TIME", dataMap.get("ACCT_OPEN_TIME")+"tt");	
		}
		if(dataMap.get("ACCT_CLOSE_TIME").length() == 8){
		dataMap.put("ACCT_CLOSE_TIME", dataMap.get("ACCT_CLOSE_TIME")+"tt");
		}
		String makeStatus = dataMap.get("MAKE_STATUS");
		String rptStatus = dataMap.get("RPT_STATUS");
		String reportType = dataMap.get("REPORT_TYPE");
		if ("0".equals(makeStatus) && "10".equals(rptStatus) && "N".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于0-未补录，上报状态等于10-未上报，上报类型为N-普通时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "N");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "A".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为A-补报时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "A");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "21".equals(rptStatus) && "A".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于21-上报错误，上报类型为A-补报时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "A");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "N".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为N-普通时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "N");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "C".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为C-纠错时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "22".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于21-上报错误，上报类型为R-重发时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "N".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为N-普通时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "N");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "C".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为C-纠错时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "21".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于21-上报错误，上报类型为R-重发时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "21".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于21-上报错误，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为R-重发时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为R-重发时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "22".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于22-要求补正，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "22".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于22-要求补正，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "21".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于21-上报错误，上报类型为I-补正时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "22".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于22-要求补正，上报类型为I-补正时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "21".equals(rptStatus) && "C".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于21-上报错误，上报类型为C-纠错时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		} else if (("1".equals(makeStatus) || "2".equals(makeStatus)) && "20".equals(rptStatus)
				&& ("N".equals(reportType) || "R".equals(reportType) || "I".equals(reportType) || "C".equals(reportType) || "A".equals(reportType))) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成或2-手工新增，上报状态等于20-上报成功，上报类型为N-普通或R-重发或I-补正或C
			 * -纠错或A-补报时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", makeStatus);
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		}
	/*	//add by pw 20151118
				根据规范，需要改正＂修改＂逻辑为：
				若收到错误回执（初级校验发现格式错误），则发重发报文．
				若收到补正要求（内容校验发现内容错误），则发补正报文．
				若已收到正确回执，报告机构发现原报文错误后主动进行校正，则发纠错报文．
				
				//begin
				if ("21".equals(rptStatus)) {
					dataMap.put("REPORT_TYPE", "R");
					dataMap.put("RPT_STATUS", "10");
				}else if("22".equals(rptStatus)){
					dataMap.put("REPORT_TYPE", "I");
					dataMap.put("RPT_STATUS", "10");
				}else if("20".equals(rptStatus)){
					dataMap.put("REPORT_TYPE", "C");
					dataMap.put("RPT_STATUS", "10");
				}
				//end
*/		try {
			myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup01", dataMap);
			myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup011", dataMap);
			// 判断对方金融机构信息是否存在数据库
			Integer total1 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupOrganization", dataMap);
			if (total1.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupOrganization", dataMap);
			}else if(total1.intValue() == 1){
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupOrganization", dataMap);	
			}
			// 判断交易对手信息是否存在数据库
			Integer total2 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupAccont", dataMap);
			if (total2.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupAccont", dataMap);
			}else if(total2.intValue() == 1){
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupAccont", dataMap);						
			}
		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("更新AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息保存成功！");
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录可疑修改
	 */
	public Callback updateAMLSecond02(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();

		String secondSuspiciousValidatorService = AMLBizValidatorService.secondSuspiciousValidatorService(dataMap);
		if (secondSuspiciousValidatorService != null) {
			rb.setErrorCode("-1");
			rb.setErrorMessage(secondSuspiciousValidatorService);
			return rb;
		}

		dataMap.put("Maker", ContextService.getUserName(header));
		dataMap.put("MakeTime", DateUtilsC.timeToNumber(new Date()));
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		if(dataMap.get("ACCT_OPEN_TIME").length() == 8){
			dataMap.put("ACCT_OPEN_TIME", dataMap.get("ACCT_OPEN_TIME")+"tt");	
		}
		if(dataMap.get("ACCT_CLOSE_TIME").length() == 8){
		dataMap.put("ACCT_CLOSE_TIME", dataMap.get("ACCT_CLOSE_TIME")+"tt");
		}
		
		String makeStatus = dataMap.get("MAKE_STATUS");
		String rptStatus = dataMap.get("RPT_STATUS");
		String reportType = dataMap.get("REPORT_TYPE");
		if ("0".equals(makeStatus) && "10".equals(rptStatus) && "N".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于0-未补录，上报状态等于10-未上报，上报类型为N-普通时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "N");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "A".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为A-补报时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "A");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "21".equals(rptStatus) && "A".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于21-上报错误，上报类型为A-补报时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "A");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "N".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为N-普通时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "N");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "C".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为C-纠错时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "21".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于21-上报错误，上报类型为R-重发时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "N".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为N-普通时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "N");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "C".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为C-纠错时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "21".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于21-上报错误，上报类型为R-重发时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "21".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于21-上报错误，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "10".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于10-未上报，上报类型为R-重发时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "R".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为R-重发时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "10".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于10-未上报，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "22".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于22-要求补正，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "22".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于22-要求补正，上报类型为I-补正时，保存后上报状态等信息赋值
			 */

			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "21".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于21-上报错误，上报类型为I-补正时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("2".equals(makeStatus) && "22".equals(rptStatus) && "I".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于2-手工新增，上报状态等于22-要求补正，上报类型为I-补正时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "2");
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		} else if ("1".equals(makeStatus) && "21".equals(rptStatus) && "C".equals(reportType)) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成，上报状态等于21-上报错误，上报类型为C-纠错时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", "1");
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		} else if (("1".equals(makeStatus) || "2".equals(makeStatus)) && "20".equals(rptStatus)
				&& ("N".equals(reportType) || "R".equals(reportType) || "I".equals(reportType) || "C".equals(reportType) || "A".equals(reportType))) {
			/**
			 * 修改业务，业务当前的补录状态等于1-补录完成或2-手工新增，上报状态等于20-上报成功，上报类型为N-普通或R-重发或I-补正或C
			 * -纠错或A-补报时，保存后上报状态等信息赋值
			 */
			dataMap.put("MAKE_STATUS", makeStatus);
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		}
	/*	//add by pw 20151118
		根据规范，需要改正＂修改＂逻辑为：
		若收到错误回执（初级校验发现格式错误），则发重发报文．
		若收到补正要求（内容校验发现内容错误），则发补正报文．
		若已收到正确回执，报告机构发现原报文错误后主动进行校正，则发纠错报文．
		
		//begin
		if ("21".equals(rptStatus)) {
			dataMap.put("REPORT_TYPE", "R");
			dataMap.put("RPT_STATUS", "10");
		}else if("22".equals(rptStatus)){
			dataMap.put("REPORT_TYPE", "I");
			dataMap.put("RPT_STATUS", "10");
		}else if("20".equals(rptStatus)){
			dataMap.put("REPORT_TYPE", "C");
			dataMap.put("RPT_STATUS", "10");
		}
		//end
*/
		try {
			myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup02", dataMap);
			myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeup021", dataMap);
			if ("true".equals(dataMap.get("ifRpt_flag"))) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateIfRptList", dataMap);
			}

			// 判断对方金融机构信息是否存在数据库
			Integer total1 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupOrganization", dataMap);
			if (total1.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupOrganization", dataMap);
			}else if(total1.intValue() == 1){
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupOrganization", dataMap);	
			}
			// 判断交易对手信息是否存在数据库
			Integer total2 = (Integer) myBatisSessionTemplate.selectOne("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.checkAMLSecondMakeupAccont", dataMap);
			if (total2.intValue() == 0) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.saveAMLSecondMakeupAccont", dataMap);
			}else if(total2.intValue() == 1){
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.updateAMLSecondMakeupAccont", dataMap);						
			}
		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("更新AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息保存成功！");
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录删除
	 */
	public Callback deleteAMLList(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		dataMap.put("delUser", ContextService.getUserName(header));
		dataMap.put("delTime", DateUtilsC.timeToNumber(new Date()));
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		dataMap.put("ruleCode", dataMap.get("RULE_CODE"));
		dataMap.put("departId", dataMap.get("DEPART_ID"));
		dataMap.put("batchNo", dataMap.get("BATCH_NO"));

		try {
			if ("2".equals(dataMap.get("MAKE_STATUS"))) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.deleteAMLList21", dataMap);
			} else {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.deleteAMLList2", dataMap);
			}

		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("删除AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息删除成功！");
		return rb;
	}

	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录取消删除
	 */
	public Callback undeleteAMLList(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		dataMap.put("delUser", ContextService.getUserName(header));
		dataMap.put("delTime", DateUtilsC.timeToNumber(new Date()));
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("RefNo", dataMap.get("REF_NO"));
		dataMap.put("ruleCode", dataMap.get("RULE_CODE"));
		dataMap.put("departId", dataMap.get("DEPART_ID"));
		dataMap.put("batchNo", dataMap.get("BATCH_NO"));


		try {
			if ("2".equals(dataMap.get("MAKE_STATUS"))) {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.undeleteAMLList21", dataMap);
			} else {
				myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.undeleteAMLList2", dataMap);
			}

		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("取消删除AML第二次补录信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录信息取消删除成功！");

		return rb;
	}
	
	/**
	 * @param header
	 * @param dataMap
	 * @return AML第二次补录可疑延迟上报
	 */
	public Callback delayAMLList(final Map<String, String> header, final Map<String, String> dataMap) {
		Callback rb = new CallbackImpl();
		
		String departId = dataMap.get("DEPART_ID");
		//String currentDay = SysParamService.getWorkDate(departId);
		String currentDay = dataMap.get("WORK_DATE");
		String calendarNo = BizFuncService.getCalendarNoByDepart(departId);
		String nextDay = BizFuncService.getSpecialWorkDate(calendarNo, currentDay, 1);
		dataMap.put("delayDate", nextDay);
		
		dataMap.put("WorkDate", dataMap.get("WORK_DATE"));
		dataMap.put("ruleCode", dataMap.get("RULE_CODE"));
		dataMap.put("batchNo", dataMap.get("BATCH_NO"));

		try {
			myBatisSessionTemplate.update("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupSql.delayAMLList2", dataMap);
		} catch (UMybatisDataAccessException e) {
			Log.error(e.getMessage(), e);
			rb.setErrorCode("-1");
			rb.setErrorMessage("延迟上报AML第二次补录可疑信息失败，错误信息：" + e.getMessage());
			return rb;
		}
		rb.setSuccessMessage("AML第二次补录可疑信息延迟上报成功！");
		return rb;
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
					dataMap.put("TABLE_NAME", "AML_ANALYSISRESULT");
					dataMap.put("CONDITION", "MAKE_STATUS");
					dataMap.put("CONDITION_VALUE", "'0'");
					List<Map> retList = myBatisSessionTemplate.selectList("com.aif.rpt.common.server.BizSql.checkMakeStatus", dataMap);
					if (retList != null && retList.size() > 0) {
						cb.setErrorCode("-1");
						cb.setErrorMessage("有未补录完成的数据,不能进行完成确认!");
						return cb;
					}
					
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
				
				/**
				 * 检查是否可以纠错C或者删除D
				 * @param header
				 * @param dataMap
				 * @return
				 */
				public Callback checkjiucuo(final Map<String, String> header, final Map<String, String> dataMap) {
					Callback rb = new CallbackImpl();
					String operationtype = dataMap.get("operationType");
					String makeStatus = dataMap.get("MAKE_STATUS");
					String rptStatus = dataMap.get("RPT_STATUS");
					String reportType = dataMap.get("REPORT_TYPE");
					//Date tradedate = UDateUtils.numberToDate(dataMap.get("TRADE_DATE"));		
					//Date nowdate = new Date();
					//int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
					
					//add by pw begin
					//usedDays（系统前30个工作日日期减交易日期）
					String departId = ContextService.getDepartId(header);
					String calendarNo = BizFuncService.getCalendarNoByDepart(departId);
					Date tradedate = UDateUtils.numberToDate(BizFuncService.getSpecialWorkDate(calendarNo,dataMap.get("TRADE_DATE"),0));
					String work_date=SysParamService.getWorkDate(ContextService.getDepartId(header));			
					String beginDate = BizFuncService.getSpecialWorkDate(calendarNo, work_date, -30);
					Date nowdate=UDateUtils.numberToDate(beginDate);
					int usedDays = (int) ((nowdate.getTime() - tradedate.getTime()) / 86400000);
					//end
					//补发
					if(operationtype !=null &&!"".equals(operationtype)&&"1".equals(operationtype)){
						if(usedDays >0){
							rb.setErrorCode("-1");
							rb.setErrorMessage("交易已超过30天请确认是否已申请补报,并获批准?");
							return rb;
						}
					}else{
						//修改、删除
						if(("1".equals(makeStatus) || "2".equals(makeStatus)) && "20".equals(rptStatus)
								&& ("N".equals(reportType) || "R".equals(reportType) || "I".equals(reportType) || "C".equals(reportType) || "A".equals(reportType))
								&& usedDays >0) {
							rb.setErrorCode("-1");
							rb.setErrorMessage("交易已超过30天请确认是否已申请纠错/删除,并获批准?");
							return rb;
						}
					}
					return rb;
				}

				//add 20150918 pw 
				// (AML补录画面帮助按钮服务)begin
				/**
				 * 获取帮助信息
				 * @param header
				 * @param dataMap
				 * @return
				 */
				public Page<Object> selectHelpInfo(final Map<String, String> header, Map<String, String> map) {
						map.put("menu_id", ContextService.getCurrFuncId(header));
						return myBatisSessionTemplate.selectPage("com.aif.rpt.common.server.BizSql.selectHelpInfo", map,BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1),
								BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
				}
				//end
				
			    //add 20170604 by wjx
				/**
				 * 判断交易日期与工作日期是否大于5天
				 * @param header
				 * @param dataMap
				 * @return 是否超过5天 大于5补录/小于等于5
				 * @throws ParseException 
				 */
				public Callback checkdate(final Map<String, String> header, final Map<String, String> dataMap) throws ParseException {
					Callback rb = new CallbackImpl();

					String workDate = dataMap.get("WORK_DATE");
					String TradeDate  = dataMap.get("TRADE_DATE");
				   
				
						  if (workDate == "") 
						  {
							 rb.setErrorCode("-1");
							 rb.setErrorMessage("工作日期不能为空！");  
							 return rb;
						  }
						  
						  if (TradeDate == "") 
						  {
							  rb.setErrorCode("-1");
							  rb.setErrorMessage("交易日期不能为空！"); 
							  return rb;
						  }
						  
						   String departId = ContextService.getDepartId(header);
						   String calendarNo = BizFuncService.getCalendarNoByDepart(departId);	
						   String beginDate = BizFuncService.getSpecialWorkDate(calendarNo, workDate, -5);
						   Date nowdate = UDateUtils.numberToDate(beginDate);
						   Date tradedateN = UDateUtils.numberToDate(BizFuncService.getSpecialWorkDate(calendarNo,dataMap.get("TRADE_DATE"),0));
							
						   int usedDays = (int) ((nowdate.getTime() - tradedateN.getTime()) / 86400000);
						   if (usedDays > 0){
						    
//						    	rb.setSuccessMessage("1");//补报
							   rb.setSuccessMessage("0");//新增
						    }
						    else
						    {
						    	rb.setSuccessMessage("0");//新增
						    }
						    
                       return rb;
				} 
				
}
