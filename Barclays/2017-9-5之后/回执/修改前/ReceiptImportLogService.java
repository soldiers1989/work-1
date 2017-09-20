package com.aif.rpt.biz.aml.receipt.server;

import java.util.HashMap;
import java.util.Map;
import com.aif.rpt.common.server.BasFuncService;
import com.aif.rpt.common.server.ContextService;
import com.aif.rpt.common.server.SysParamService;
import com.allinfinance.yak.ubase.data.page.Page;
import com.allinfinance.yak.ubase.orm.mybatis.template.MyBatisSessionTemplate;
import com.allinfinance.yak.uface.api.core.serivce.callback.Callback;
import com.allinfinance.yak.uface.core.serivce.callback.CallbackImpl;

public class ReceiptImportLogService {
	private MyBatisSessionTemplate myBatisSessionTemplate;

	public void setMyBatisSessionTemplate(MyBatisSessionTemplate myBatisSessionTemplate) {
		this.myBatisSessionTemplate = myBatisSessionTemplate;
	}

	public Page<Object> listByParam(final Map<String, String> header, Map<String, String> dataMap) {
		return myBatisSessionTemplate.selectPage("com.aif.rpt.biz.aml.receipt.server.receiptServerSql.selectRecImportLogByParam", dataMap, BasFuncService.str2Int(header.get("__PAGEINDEX__"), 1), BasFuncService.str2Int(header.get("__COUNT__"), SysParamService.pageSize));
	}
	
	public static Callback selectDefaultCondition(final Map<String, String> header, Map<String, String> dataMap) {
		String workDate = SysParamService.getWorkDate(ContextService.getDepartId(header));
		
		Map resultMap = new HashMap();
		resultMap.put("CREATED_DATE_BEGIN", workDate);
		resultMap.put("CREATED_DATE_END", workDate);
		
		Callback cb = new CallbackImpl();
		cb.setCallbackData(resultMap);
		
		return cb;
	}
}