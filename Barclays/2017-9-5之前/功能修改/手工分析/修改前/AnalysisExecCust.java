package com.aif.rpt.analysis.client;

import java.util.Map;
import java.util.HashMap;
import com.allinfinance.yak.uface.data.client.async.Callback;
import com.allinfinance.yak.uface.data.client.command.callback.CommandCallback;
import com.allinfinance.yak.uface.data.client.command.item.DataScope;
import com.allinfinance.yak.uface.data.client.dataset.action.DataAction;

import com.allinfinance.yak.uface.data.client.dataset.record.DataRecord;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt;
import com.allinfinance.yak.uface.ui.component.client.events.ClickEvent;
import com.allinfinance.yak.uface.ui.component.client.events.ClickListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.BlurEvent;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.BlurListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.ChangeEvent;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.ChangeListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FocusEvent;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FocusListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FormItemClickListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.util.SC;

public class AnalysisExecCust extends AnalysisExec {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6847030955391411724L;
	private static String type = "手工分析";

	@Override
	protected void setting() {
		this.setPageID("");
	}

	private void selectDefaultCondition() {
		queryDefaultCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "selectDefultCondition");
		queryDefaultCmd.execute(new CommandCallback() {

			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				// TODO Auto-generated method stub
				if (!status) {
					UPrompt.alert(type, "查询默认条件数据错误！");
				} else {
					if (callback.getCallbackCount() == 0) {
						UPrompt.alert(type, "未查到默认条件数据！");
					} else {
						showList();
					}
				}
			}

		});
	}

	protected void beforeRender() {

		super.beforeRender();
		queryDepartCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "SelectDepartList");
		queryDepartCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (callback.getCallbackCount() == 0) {
					UPrompt.alert(type, "机构代码查询无记录");
				}
				if (!status) {
					UPrompt.alert(type, "机构代码查询失败，错误信息：" + callback.getErrorMessage());
				} else {
					selectDefaultCondition();
					editorDEPART_ID.flushData();
				}
			}
		});
	}

	private void showList() {
		queryRuleCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "listByParam");
		queryRuleCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				} else if (callback.getCallbackCount() == 0) {
					UPrompt.alert(type, "查询无记录");
				}
			}
		});
	}

	private void execProcedure() {
		AnalysisExecCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "execProcedure");
		AnalysisExecCmd.setCommandDataScope(DataScope.SELECT);

		AnalysisExecCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, callback.getErrorMessage());
				} else {
					UPrompt.alert(type, callback.getSuccessMessage());
				}

				showList();
			}
		});
	}

	@Override
	protected void bindEvent() {

		this.btnQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				if (!queryForm.validate()) {
					UPrompt.alert(type, "请完整输入查询条件！");
					return;
				}
				
				showList();
			}
		});

		this.btnExec.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// 判断第一次补录审核是否确认完成
				Map<String,String> extraInfo = new HashMap<String,String>();
				extraInfo.put("fun_guid", "amlInputCheck_menu");
				extraInfo.put("report_id", "AML");
				extraInfo.put("work_date", dsQuery.getCurrentValueAsDateString("WORK_DATE", "yyyyMMdd"));
				checkCmd.setExtraInfo(extraInfo);
				checkCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "isFinishedFlowNode");
				checkCmd.execute(new CommandCallback() {

					@Override
					public void onCallback(boolean status, Callback callback,
							String commandItemName) {
						if (callback.getErrorMessage() != null) {
							UPrompt.alert(type, "AML第一次补录审核未确认完成，不能执行手工分析");
							return;
						} 
						int ruleCount = dsResult.getSelectedRecords().length;
						
						if (ruleCount == 0) {
							UPrompt.alert(type, "请选择要分析的规则！");
							return;
						}
						
						execProcedure();
					}
					
				});
			}
		});
		
		this.btconfirm.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//判断后一节点是否完成确认
				
				comfirmQryCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "SelectUnConfirmSouce");
				comfirmQryCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (status) {
							if (dscomSource.getRecordCount() == 0) {
								UPrompt.alert("完成确认", "无数据源需要确认完成！");
							} else {
								comsourceWindow.show();
							}
						} else {
							UPrompt.alert("完成确认", "待确认信息查询失败，错误信息：" + callback.getErrorMessage());
						}
					}
				});
			}
		});
		this.btcancel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//判断后一节点是否完成确认
				
				uncomfirmQryCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "SelectConfirmSouce");
				uncomfirmQryCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (status) {
							if (dsuncomSource.getRecordCount() == 0) {
								UPrompt.alert("取消确认", "无数据源需要取消完成确认！");
							} else {
								uncomsourceWindow.show();
							}
						} else {
							UPrompt.alert("取消确认", "完成确认信息查询失败，错误信息：" + callback.getErrorMessage());
						}
					}
				});
			}
		});
		
		this.btOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dscomSource.getRecordCount() == 0) {
					UPrompt.alert("完成确认", "无数据源!");
					return;
				}
				
				if (dscomSource.getSelectedRecords().length == 0) {
					UPrompt.alert("完成确认", "未选择数据源!");
					return;
				}
				
				StringBuilder fieldValue = new StringBuilder();

				comfirmCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "SetComfirmSourceStatus");
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("depart_id", dsQuery.getCurrentValue("DEPART_ID"));
				condition.put("work_date", dsQuery.getCurrentValueAsDateString("WORK_DATE", "yyyyMMdd"));
				condition.put("source_status", "1");

				comfirmCmd.setExtraInfo(condition);

				comfirmCmd.setCommandDataScope(DataScope.SELECT);
				comfirmCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (status) {
							dscomSource.removeAllData();
							dscomSource.sync();
							comsourceWindow.hide();
							UPrompt.alert("完成确认", callback.getSuccessMessage());
						} else {
							UPrompt.alert("完成确认", callback.getErrorMessage());
						}
					}
				});
			}
		});
		this.btReturn.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dscomSource.removeAllData();
				dscomSource.sync();
				comsourceWindow.hide();
			}
		});
		this.btOk2.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsuncomSource.getRecordCount() == 0) {
					UPrompt.alert("取消确认", "无数据源！");
					return;
				}
	
				if (dsuncomSource.getSelectedRecords().length == 0) {
					UPrompt.alert("完成确认", "未选择数据源!");
					return;
				}		
				
				StringBuilder fieldValue = new StringBuilder();

				uncomfirmCmd.setCommandService("com.aif.rpt.analysis.server.AnalysisExecService", "SetComfirmSourceStatus");
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("depart_id", dsQuery.getCurrentValue("DEPART_ID"));
				condition.put("work_date", dsQuery.getCurrentValueAsDateString("WORK_DATE", "yyyyMMdd"));
				condition.put("source_status", "0");

				uncomfirmCmd.setExtraInfo(condition);

				uncomfirmCmd.setCommandDataScope(DataScope.SELECT);
				uncomfirmCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (status) {
							dsuncomSource.removeAllData();
							dsuncomSource.sync();
							uncomsourceWindow.hide();
							UPrompt.alert("取消确认", callback.getSuccessMessage());
						} else {
							UPrompt.alert("取消确认", callback.getErrorMessage());
						}
					}
				});
			}
		});
		this.btReturn2.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dsuncomSource.removeAllData();
				dsuncomSource.sync();
				uncomsourceWindow.hide();
			}
		});

	}
}