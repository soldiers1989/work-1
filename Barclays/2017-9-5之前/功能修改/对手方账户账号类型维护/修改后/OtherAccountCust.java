package com.aif.rpt.bas.client;

import java.util.HashMap;

import com.allinfinance.yak.uface.data.client.async.Callback;
import com.allinfinance.yak.uface.data.client.command.callback.CommandCallback;
import com.allinfinance.yak.uface.data.client.command.item.DataScope;
import com.allinfinance.yak.uface.data.client.dataset.action.DataAction;
import com.allinfinance.yak.uface.data.client.dataset.record.DataRecord;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt.PromptCallback;
import com.allinfinance.yak.uface.ui.component.client.events.ClickEvent;
import com.allinfinance.yak.uface.ui.component.client.events.ClickListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FormItemClickListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FormItemIconClickEvent;

public class OtherAccountCust extends OtherAccount {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7663044251209509525L;
	
	private static String opStatus = "really";
	private static String type = "对手方账户信息维护";
	private static String errorMessage = null;
	private static String successMessage = null;
	
	
	@Override
	protected void setting() {
	this.setPageID("");
	}
	
	
	protected void beforeRender() {

		super.beforeRender();

		// 为查询条件的dataset添加一条记录
		this.dsQuery.addDataRecord(new DataRecord(), new DataAction() {

			@Override
			public void doAction(DataRecord record) {
				queryForm.editRecord(dsQuery.getCurrentRecord().getViewRecord());
				showList();
			}

		});

		this.dsCountryQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});
	}

	protected void butAddOrMod() {
		btnSave.setDisabled(false);
		btnCancel.setDisabled(false);
		btnAdd.setDisabled(true);
		btnMod.setDisabled(true);
		btnDel.setDisabled(true);
	}

	protected void butSaveOrCancel() {
		btnSave.setDisabled(true);
		btnCancel.setDisabled(true);
		btnAdd.setDisabled(false);
		btnMod.setDisabled(false);
		btnDel.setDisabled(false);
	}
	
	/**
	 * 查询并显示客户信息
	 */
	protected void showList() {

		queryCmd.setCommandService("com.aif.rpt.bas.server.OtherAccountService", "listByParam");
		queryCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (callback.getCallbackCount() == 0) {
					//formAction.setReadOnly(false);
					btnMod.setDisabled(true);
					btnDel.setDisabled(true);
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				} else {
					butSaveOrCancel();
				}
				formAction.setReadOnly(true);

				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
			}
		});
	}

	protected void showCountryList() {

		qryCountryCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "selectCountryList");
		qryCountryCmd.setCommandDataScope(DataScope.CURRENT);
		qryCountryCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (callback.getCallbackCount() == 0) {
					UPrompt.alert("国籍查询", "查询无记录");
				}

				if (!status) {
					UPrompt.alert("国籍查询", "查询失败，错误信息：" + callback.getErrorMessage());
				}
			}
		});
	}
	
	@Override
	protected void bindEvent() {
		
		this.btnQuery.addClickListener(new ClickListener(){
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
						dsResult.setCurrentValue("CTPY_IC_OTHER_TYPE", "@N");
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
				formAction.setReadOnly("CTPY_ACCT_ID", true);
			}
		});
		this.btnDel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				UPrompt.confirm(type, "您确定删除该条记录？", new PromptCallback() {
					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							saveCmd.setCommandService("com.aif.rpt.bas.server.OtherAccountService", "deleteOtherAccount");
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
		this.btnSave.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {

                //对方账户信息非空校验	
				if (!formAction.validate()) {
					UPrompt.alert(type, "请完整输入对方账户信息！");
					return;
				}
				//对方账户信息的详细校验
				String validate =OtherAccountValidator.otherAccountValidator(dsResult.getCurrentRecord().getDataMap());
				if (validate != null) {
					UPrompt.alert(type, validate);
					return;
				}
				
				if ("Add".equals(opStatus)) {
					saveCmd.setCommandService("com.aif.rpt.bas.server.OtherAccountService", "addOtherAccount");

				} else if ("Mod".equals(opStatus)) {
					saveCmd.setCommandService("com.aif.rpt.bas.server.OtherAccountService", "updateOtherAccount");

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
		
		this.editCTPY_NATIONALITY.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				showCountryWindow();
			}

		});

		this.btnCountryQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				showCountryList();

			}
		});

		this.btnCountryOk.addClickListener(new ClickListener() {

			@Override
			public void onClick(ClickEvent event) {
				if (dsCountryResult.getCurrentRecord() == null) {
					UPrompt.alert("国籍查询", "请选择国家");
					return;
				}
				String countryCode = dsCountryResult.getCurrentValue("countryCode");
				String countryName = dsCountryResult.getCurrentValue("countryName");
				dsResult.setCurrentValue("CTPY_NATIONALITY", countryCode);
				dsResult.sync();
				dsCountryResult.removeAllData();
				dsCountryResult.sync();
				countryWindow.hide();
				dsCountryQuery.setCurrentValue("countryCode", "");
				dsCountryQuery.setCurrentValue("countryName", "");
				dsCountryQuery.sync();
			}

		});

		this.btnCountryBack.addClickListener(new ClickListener() {

			@Override
			public void onClick(ClickEvent event) {
				dsCountryResult.removeAllData();
				dsCountryResult.sync();
				countryWindow.hide();
				dsCountryQuery.setCurrentValue("countryCode", "");
				dsCountryQuery.setCurrentValue("countryName", "");
			}

		});

	}

	private void showCountryWindow() {

		String countryCode = dsResult.getCurrentValue("CTPY_NATIONALITY");
		if (countryCode != null && !"".equals(countryCode.trim())) {
			dsCountryQuery.setCurrentValue("countryCode", countryCode);
			dsCountryQuery.sync();
		} else {
			dsCountryQuery.setCurrentValue("countryCode", "");
			dsCountryQuery.sync();
		}

		qryCountryCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "selectCountryList");
		qryCountryCmd.setCommandDataScope(DataScope.CURRENT);
		qryCountryCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "国籍查询失败，错误信息：" + callback.getErrorMessage());
				} else {
					countryWindow.show();
				}
			}
		});
	}
}