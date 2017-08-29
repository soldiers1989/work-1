package com.aif.rpt.bas.client;

import java.util.HashMap;

import com.allinfinance.yak.uface.data.client.async.Callback;
import com.allinfinance.yak.uface.data.client.command.callback.CommandCallback;
import com.allinfinance.yak.uface.data.client.dataset.action.DataAction;
import com.allinfinance.yak.uface.data.client.dataset.event.ValueChangeEvent;
import com.allinfinance.yak.uface.data.client.dataset.event.ValueChangedListener;
import com.allinfinance.yak.uface.data.client.dataset.record.DataRecord;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt.PromptCallback;
import com.allinfinance.yak.uface.ui.component.client.events.ClickEvent;
import com.allinfinance.yak.uface.ui.component.client.events.ClickListener;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellMouseUpEvent;
import com.smartgwt.client.widgets.grid.events.CellMouseUpHandler;
import com.smartgwt.client.widgets.grid.events.CellOutEvent;
import com.smartgwt.client.widgets.grid.events.CellOutHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

public class AccountCust extends Account {

	private static final long serialVersionUID = -2859479517544727238L;

	private static String opStatus = "really";
	private static String type = "账户信息维护";
	private static String errorMessage = null;
	private static String successMessage = null;

	@Override
	protected void setting() {
		this.setPageID("");
	}

	protected void beforeRender() {

		super.beforeRender();

		qryCurrencyCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "selectCurrencyList");
		qryCurrencyCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "币种信息查询错误，错误信息：" + callback.getErrorMessage());
				}
				editACCT_CUR.flushData();
			}
		});
		
		// 加载网点代码
		qryDepCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "SelectDepartList");
		qryDepCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "机构列表查询错误，错误信息：" + callback.getErrorMessage());
				}
				editDEPART_ID.flushData();
			}
		});

		// 为查询条件的dataset添加一条记录
		this.dsQuery.addDataRecord(new DataRecord(), new DataAction() {

			@Override
			public void doAction(DataRecord record) {
				queryForm.editRecord(dsQuery.getCurrentRecord().getViewRecord());
				showList();
			}

		});
		
		
	}

	protected void butAddOrMod() {
		btnSave.setDisabled(false);
		btnCancel.setDisabled(false);
		btnAdd.setDisabled(true);
		btnMod.setDisabled(true);
		btnDel.setDisabled(true);	
		btnClose.setDisabled(true);			
	}

	protected void butSaveOrCancel() {
		btnSave.setDisabled(true);
		btnCancel.setDisabled(true);
		btnAdd.setDisabled(false);
		btnMod.setDisabled(false);
		btnDel.setDisabled(false);
		btnClose.setDisabled(false);
	}

	protected void showList() {

		queryCmd.setCommandService("com.aif.rpt.bas.server.AccountService", "listByParam");
		queryCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				DataRecord[] dataRecords = dsResult.getDataRecords();
				for (DataRecord record : dataRecords) {
					System.out.println(record.getDataMap());
				}
				if (callback.getCallbackCount() == 0) {
					// formAction.setReadOnly(false);
					btnMod.setDisabled(true);
					btnDel.setDisabled(true);
					btnClose.setDisabled(true);				
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				} else {
					butSaveOrCancel();
				}
				formAction.setReadOnly(true);
				
				//91(ACC外汇账户类型-当账户为外币账户时，为必填项；当账户为人民币账户时，为非必填项。目前无论币种都是必填)2015/9/18 begin
				if(!"CNY".equals(dsResult.getCurrentValue("ACCT_CUR"))){
					editFX_ACCT_TYPE.setRequired(true);
				}else{
					editFX_ACCT_TYPE.setRequired(false);
				}
				//end
				
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
			}
		});
	}

	@Override
	protected void bindEvent() {

		this.btnQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				showList();
			}
		});

		this.btnAdd.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {

				opStatus = "Add";

				butAddOrMod();
				queryForm.setReadOnly(true);
				tableResult.setDisabled(true);
				formAction.setReadOnly(false);
				btnQuery.setDisabled(true);
				HashMap<String, String> map = new HashMap<String, String>();
				dsResult.addDataRecord(new DataRecord(map), new DataAction() {
					@Override
					public void doAction(DataRecord record) {
						dsResult.setCurrentValue("ACCT_CATA", "12");
						dsResult.setCurrentValue("LIMIT_TYPE", "11");
						dsResult.setCurrentValue("CARD_TYPE", "90");
						dsResult.setCurrentValue("CARD_OTHER_TYPE", "@N");
						dsResult.setCurrentValue("CARD_NO", "@N");
						dsResult.setCurrentValue("DEPART_ID","310000103201");
						formAction.synchronize();
					}
				});
			}
		});

		this.btnMod.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				opStatus = "Mod";
				butAddOrMod();		
				btnQuery.setDisabled(true);
				queryForm.setReadOnly(true);
				formAction.setReadOnly(false);
				tableResult.setDisabled(true);
				formAction.setReadOnly("ACCT_ID", true);
				formAction.setReadOnly("DEPART_ID", true);
				formAction.setReadOnly("ACCT_CUR", true);
				
			}
		});

		this.btnDel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				UPrompt.confirm(type, "您确定删除该条记录？", new PromptCallback() {
					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							saveCmd.setCommandService("com.aif.rpt.bas.server.AccountService", "deleteAccount");
							saveCmd.execute(new CommandCallback() {
								@Override
								public void onCallback(boolean status, Callback callback, String commandItemName) {
									
									if (!status) {
										UPrompt.alert(type, callback.getErrorMessage());
									} else {
										UPrompt.alert(type, callback.getSuccessMessage());
										showList();
										dsResult.logSys("del");
									}
								}
							});
						}
					}

				});
			}
		});

		this.btnClose.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				if ((!"@I".equals(dsResult.getCurrentValue("CLOSE_TIME").trim())
					   && !"@N".equals(dsResult.getCurrentValue("CLOSE_TIME").trim()) 
					   && !"@E".equals(dsResult.getCurrentValue("CLOSE_TIME").trim()))
					|| (dsResult.getCurrentValue("ACC_STATUS") != null 
						&& "13".equals(dsResult.getCurrentValue("ACC_STATUS").trim()))) {
					UPrompt.alert(type, "该账户已经关户！");
					return;
				}
				else
				{	
					UPrompt.confirm(type, "您确定要对该账户进行关户？", new PromptCallback() {
					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							saveCmd.setCommandService("com.aif.rpt.bas.server.AccountService", "closeAccount");
							saveCmd.execute(new CommandCallback() {
								@Override
								public void onCallback(boolean status, Callback callback, String commandItemName) {
									
									if (!status) {
										UPrompt.alert(type, callback.getErrorMessage());
									} else {
										UPrompt.alert(type, callback.getSuccessMessage());
										showList();
										dsResult.logSys("Close");
									}
								}
							});
						}
					}

				});
			}
		  }
		});		
		this.btnSave.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {

				formAction.blur();
                //账户基础信息非空校验	
				if (!formAction.validate()) {
					UPrompt.alert(type, "请完整输入账户基础信息！");
					return;
				}
				//账户基础信息的详细校验
				String validate =AccountValidator.accountValidator(dsResult.getCurrentRecord().getDataMap());
				if (validate != null) {
					UPrompt.alert(type, validate);
					return;
				}

				if ("Add".equals(opStatus)) {
					saveCmd.setCommandService("com.aif.rpt.bas.server.AccountService", "addAccount");

				} else if ("Mod".equals(opStatus)) {
					saveCmd.setCommandService("com.aif.rpt.bas.server.AccountService", "updateAccount");
				}
				saveCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (callback.getErrorMessage() != null) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							if ("Mod".equals(opStatus)) {
								dsResult.logSys("mod");
							} else if ("Add".equals(opStatus)) {
								dsResult.logSys("new");
							}

							showList();
							UPrompt.alert(type, callback.getSuccessMessage());
							butSaveOrCancel();
							
						}
					}
				});

				tableResult.setDisabled(false);
				queryForm.setReadOnly(false);
				btnQuery.setDisabled(false);
			}
		});
		this.btnCancel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				butSaveOrCancel();
				showList();
				tableResult.setDisabled(false);
				queryForm.setReadOnly(false);
				btnQuery.setDisabled(false);
			}
		});
		
		//91(ACC外汇账户类型-当账户为外币账户时，为必填项；当账户为人民币账户时，为非必填项。目前无论币种都是必填)2015/9/18 begin
		
		this.editACCT_CUR.addValueChangedListener(new ValueChangedListener("dataset") {
			
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				// TODO Auto-generated method stub
				if(!"CNY".equals(dsResult.getCurrentValue("ACCT_CUR"))){
					editFX_ACCT_TYPE.setRequired(true);
				}else{
					editFX_ACCT_TYPE.setRequired(false);
				}
			}
		});
		this.tableResult.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				// TODO Auto-generated method stub
				if(!"CNY".equals(event.getRecord().getAttribute("ACCT_CUR"))){
					editFX_ACCT_TYPE.setRequired(true);
				}else{
					editFX_ACCT_TYPE.setRequired(false);
				}
			}
		
		});

	}

}