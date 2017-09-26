package com.aif.rpt.biz.aml.processing.client;

import java.util.HashMap;
import java.util.Map;

import java_cup.internal_error;

import com.aif.rpt.common.client.BizFunClient;
import com.allinfinance.yak.uface.core.client.context.ContextHelper;
import com.allinfinance.yak.uface.core.client.utils.DateUtils;
import com.allinfinance.yak.uface.data.client.async.Callback;
import com.allinfinance.yak.uface.data.client.command.callback.CommandCallback;
import com.allinfinance.yak.uface.data.client.command.item.DataScope;
import com.allinfinance.yak.uface.data.client.dataset.action.DataAction;
import com.allinfinance.yak.uface.data.client.dataset.event.ValueChangeEvent;
import com.allinfinance.yak.uface.data.client.dataset.event.ValueChangedListener;
import com.allinfinance.yak.uface.data.client.dataset.record.DataRecord;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt.PromptCallback;
import com.allinfinance.yak.uface.ui.component.client.download.UDownloaderClient;
import com.allinfinance.yak.uface.ui.component.client.events.ClickEvent;
import com.allinfinance.yak.uface.ui.component.client.events.ClickListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FormItemClickListener;
import com.allinfinance.yak.uface.ui.component.client.form.fields.events.FormItemIconClickEvent;
import com.allinfinance.yak.uface.ui.component.client.grid.events.RecordClickEvent;
import com.allinfinance.yak.uface.ui.component.client.grid.events.RecordClickListener;
import com.allinfinance.yak.uface.ui.component.client.grid.events.RecordDoubleClickEvent;
import com.allinfinance.yak.uface.ui.component.client.grid.events.RecordDoubleClickListener;
import com.allinfinance.yak.uface.ui.component.client.upload.UploadListener;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class AMLSecondMakeupCust extends AMLSecondMakeup {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2791061803375739341L;
	// 标题名称
	private String type = "AML第二次补录";
	private String typeinner = "添加email附件";
	// 判断删除按钮的类型 0-"删除" 1-"取消删除"
	private String delNo = "0";
	// 判断操作类型 0-"新增" 1-"修改 "
	private String makeType = "0";
	//判断报文   0-新增 1-补报
	private String operationType="0";
	// 判断查询结果所赋值的字段
	private String selectName = "";
	private boolean flag = false;

	@Override
	protected void setting() {
		this.setPageID("");
	}

	
	/**
	 * 插入上传文件信息
	 */
	/*protected void insertAccList(){
		
		queryDatasetAcc.setCurrentValue("ACC_PATH",uploadEditor.getFileFullPath());//附件路径
		queryDatasetAcc.setCurrentValue("REF_NO", dsResult.getCurrentValue("REF_NO"));//当前所选数据业务流水号
		String str = queryDatasetAcc.getCurrentValue("REF_NO");
		insertCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "insertAcc");
		insertCmdAcc.executeCommand(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				
				if(callback.getCallbackCount() == 0 || !status){
					UPrompt.alert(typeinner, "上传文件数据插入失败，错误信息：" + callback.getErrorMessage());
				}
			}
		});
	}*/
	
	/**
	 * 
	 * AML第二次补录的查询操作
	 */
	protected void showList() {
		if (tableset.getSelectedTabNumber() == 0) {
			queryCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectAMLSecondMakeup");
			queryCmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (callback.getCallbackCount() == 0) {
						dsResult.sync();
						UPrompt.alert(type, "查询大额补录信息无记录");
					}
					if (!status) {
						UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
					} else {
						if (dsResult.getCurrentValue("IS_DEL") != null && "1".equals(dsResult.getCurrentValue("IS_DEL"))) {
							btnDel.setTitle("取消删除");
							delNo = "1";
						} else if (dsResult.getCurrentValue("IS_DEL") != null && "0".equals(dsResult.getCurrentValue("IS_DEL"))) {
							btnDel.setTitle("删除");
							delNo = "0";
						}
					}
				}
			});
		} else {
			query2Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectAMLSecondMakeupSuspicious");
			query2Cmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (callback.getCallbackCount() == 0) {
						dsResult1.sync();
						UPrompt.alert(type, "查询可疑补录信息无记录");
					}
					if (!status) {
						UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
					} else {
						if (dsResult1.getCurrentValue("IS_DEL") != null && "1".equals(dsResult1.getCurrentValue("IS_DEL"))) {
							btnDel.setTitle("取消删除");
							delNo = "1";
						} else if (dsResult1.getCurrentValue("IS_DEL") != null && "0".equals(dsResult1.getCurrentValue("IS_DEL"))) {
							btnDel.setTitle("删除");
							delNo = "0";
						}
					}
				}
			});
		}
		flag = true;
	}

	/**
	 * 展示已上传的附件
	 */
	protected void showListAcc(){
		//queryDatasetAcc.setCurrentValue("REF_NO", dsResult.getCurrentValue("REF_NO"));//大额
		queryDatasetAcc.setCurrentValue("REF_NO", dsResult1.getCurrentValue("REF_NO"));//可疑
		queryCommandAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "uploadedShow");
		queryCommandAcc.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "上传附件查询错误，错误信息：" + callback.getErrorMessage());
				} 
		     }
	   });	
	}
	
	/**
	 * 国家地区代码
	 */
	protected void showNationregionList() {
		qryNationregionCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectNationregion");
		qryNationregionCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsWhitherQuery.sync();
					UPrompt.alert(type, "查询无记录");
				}

			}
		});
	}

	/**
	 * 规则代码查询
	 */
	protected void showRuleCodeList() {
		qryRuleCodeCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectRulecode");
		qryRuleCodeCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsRuleCodeQuery.sync();
					UPrompt.alert(type, "查询无记录");
				}

			}
		});
	}

	/**
	 * 网点信息查询
	 */
	protected void showDepartList() {
		if (tableset.getSelectedTabNumber() == 0) {
			qryDepartCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectDepart");
			qryDepartCmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (!status) {
						UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
					}
					if (callback.getCallbackCount() == 0) {
						dsResult.sync();
						UPrompt.alert(type, "查询无记录");
					} else {
						DataRecord dataRecord = dsDepartIDList.getCurrentRecord();
						dsResult.setCurrentValue("DEPART_NAME", dataRecord.getValue("DEPART_NAME"));
						dsResult.setCurrentValue("DEPART_TYPE", dataRecord.getValue("DEPART_TYPE"));
						dsResult.setCurrentValue("DEPART_AREACODE", dataRecord.getValue("DEPART_AREACODE"));
						dsResult.sync();
					}
				}
			});
		} else {
			qryDepart2Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectDepart");
			qryDepart2Cmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (!status) {
						UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
					}
					if (callback.getCallbackCount() == 0) {
						dsResult1.sync();
						UPrompt.alert(type, "查询无记录");
					} else {
						DataRecord dataRecord = dsDepartIDList.getCurrentRecord();
						dsResult1.setCurrentValue("DEPART_NAME", dataRecord.getValue("DEPART_NAME"));
						dsResult1.setCurrentValue("DEPART_TYPE", dataRecord.getValue("DEPART_TYPE"));
						dsResult1.setCurrentValue("DEPART_AREACODE", dataRecord.getValue("DEPART_AREACODE"));
						dsResult1.sync();
					}

				}
			});

		}
	}

	/**
	 * 涉外收支交易分类与代码
	 */
	protected void showMetaList() {
		qryMetaCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectMeta");
		qryMetaCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				}
			}
		});
	}
	
	/**
     * 疑似涉罪类型查询
     */
    protected void showBsToscList() {
    	System.out.print(dsMetaQuery.getCurrentValue("META_VALUE"));
        qryBsToscCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectBsTosc");
        qryBsToscCmd.execute(new CommandCallback() {
            @Override
            public void onCallback(boolean status, Callback callback, String commandItemName) {
                if (!status) {
                    UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
                }
                if (callback.getCallbackCount() == 0) {
                    dsResult1.sync();
                    UPrompt.alert(type, "查询无记录");
                }
            }
        });
    }
    
	/**
	 * 客户信息查询
	 */
	protected void showClientList() {
		qryClientCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectClientList");
		qryClientCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				}
			}
		});
	}

	/**
	 * 账户信息
	 */
	protected void showAcctList() {
		if (tableset.getSelectedTabNumber() == 0) {
		dsAcctQuery.setCurrentValue("CLIENT_ID", dsResult.getCurrentValue("CLIENT_ID"));
		}else{
		dsAcctQuery.setCurrentValue("CLIENT_ID", dsResult1.getCurrentValue("CLIENT_ID"));		
		}
		qryAcctCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectAcctList");
		qryAcctCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				}

			}
		});
	}
	
	
	/**
	 * 单条账户信息查询
	 */
	private void selectAcctOne() {
		if (tableset.getSelectedTabNumber() == 0) {
		dsAcctQuery.setCurrentValue("ACCT_ID", dsResult.getCurrentValue("ACCT_ID"));
		dsAcctQuery.setCurrentValue("CLIENT_ID", dsResult.getCurrentValue("CLIENT_ID"));
		}else{
		dsAcctQuery.setCurrentValue("ACCT_ID", dsResult1.getCurrentValue("ACCT_ID"));		
		dsAcctQuery.setCurrentValue("CLIENT_ID", dsResult1.getCurrentValue("CLIENT_ID"));
		}
		qryAcctOneCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLFirstMakeupservice", "selectAcctOne");
		qryAcctOneCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 1) {
					setAcct();
				}else {
					if ("ActId01".equals(selectName)) {
						formAction5.setReadOnly("ACCT_TYPE",false);	
						formAction5.setReadOnly("PBOC_NUM_ACCT",false);	
					}else if("ActId02".equals(selectName)){
						formAction15.setReadOnly("ACCT_TYPE",false);	
						formAction15.setReadOnly("ACCT_OPEN_TIME",false);	
						formAction15.setReadOnly("ACCT_CLOSE_TIME",false);						
						formAction15.setReadOnly("PBOC_NUM_ACCT",false);						
					}
				}
			}
		});
	}

	/**
	 * 对方金融机构信息查询
	 */
	protected void showOrganizationList() {
		qryOrganizationCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectOrganizationList");
		qryOrganizationCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				}

			}
		});
	}

	/**
	 * 交易对手信息查询
	 */
	protected void showCounterpartyList() {
		qryCounterpartyCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectCounterpartyList");
		qryCounterpartyCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
				}
				if (callback.getCallbackCount() == 0) {
					dsResult.sync();
					UPrompt.alert(type, "查询无记录");
				}

			}
		});
	}

	/**
	 * 解锁操作
	 */
	protected void getunlock() {
		if (tableset.getSelectedTabNumber() == 0) {
			unlockCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "getunlock");
			unlockCmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (!status) {
						UPrompt.alert(type, callback.getErrorMessage());
					}

				}
			});
		} else {
			unlockCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "getunlock");
			unlockCmd1.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (!status) {
						UPrompt.alert(type, callback.getErrorMessage());
					}

				}
			});
		}

	}

	/**
	 * 检查业务唯一性
	 */
	protected void checkRefnoAndRefNo() {
		if ("1".equals(makeType)) {
			if (tableset.getSelectedTabNumber() == 0) {
				if (!(dsResult.getCurrentValue("ruleCode01")).equals(dsResult.getCurrentValue("RULE_CODE"))) {
					checkRefNoAndRuleCodeCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectRuleCodeAndRefNo");
					checkRefNoAndRuleCodeCmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {
							if (!status) {
								UPrompt.alert(type, callback.getErrorMessage());
								dsResult.setCurrentValue("RULE_CODE", "");
								dsResult.sync();
							}
						}
					});
				}

			} else {
				if (!(dsResult1.getCurrentValue("ruleCode02")).equals(dsResult.getCurrentValue("RULE_CODE"))) {
					checkRefNoAndRuleCode1Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectRuleCodeAndRefNo");
					checkRefNoAndRuleCode1Cmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {
							if (!status) {
								UPrompt.alert(type, callback.getErrorMessage());
								dsResult1.setCurrentValue("RULE_CODE", "");
								dsResult1.sync();
							}
						}
					});
				}
			}

		}
	}

	/**
	 * 添加email附件的操作
	 */
	protected void addEmail(){
		//
		
		detailWindowAcc.show();
	}
	
	/**
	 * 新增操作
	 * 
	 * @param operationType
	 *            选择新增类型 0-"新增",1-"补发"。
	 * @param addtype
	 *            选择新增类型 名称 "新增" "补发"
	 */
	protected void addNew(final String addtype) {
		// TODO Auto-generated method stub
		// 大额业务
		if (tableset.getSelectedTabNumber() == 0) {
			// 判断是否能够新增
			if (DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")).after(DateUtils.parseDate(DateUtils.formatDate(dsQuery.getCurrentValueAsDate("WORK_DATE"), "yyyyMMdd")))) {
				UPrompt.alert(type, "不能进行" + addtype + "!原因:不能对非当前工作日的大额数据进行操作!");
				return;
			}
			// 新增的时候需要在dsResult插入一条空记录,防止查询的时候出现条件传不到后台
			dsResult.addDataRecord(new DataRecord(), new DataAction() {
				@Override
				public void doAction(DataRecord record) {
					detailWindow.show();
					dsResult.setCurrentValue("operationType", operationType);
					dsResult.setCurrentValue("WORK_DATE", dsQuery.getCurrentValueAsDate("WORK_DATE"));
					dsResult.setCurrentValue("DEPART_ID", ContextHelper.getContext().getValue("departId"));
					formAction1.setReadOnly("REF_NO", false);
					if ("19".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE"))) {
						editIcTypeMemo01.setRequired(true);
						editIcTypeMemo01.redraw();
					} else {
						editIcTypeMemo01.setRequired(false);
						editIcTypeMemo01.redraw();
					}
					dsResult.sync();
					showDepartList();
					dsResult.setCurrentValue("RELATION_TYPE","00");
					dsResult.setCurrentValue("TRADE_MODE","000131");
					dsResult.sync();
					//modify by wzf 20170510 增加是否跨境标志 begin
          if ("1".equals(dsResult.getCurrentValue(
                          "IS_CROSSBORDER"))) {
              dsResult.setCurrentValue("CROSSBORDERflag", true);
              editCROSSBORDERflag01.setValue(true);
          } else {
              editCROSSBORDERflag01.setValue(false);
              dsResult.setCurrentValue("CROSSBORDERflag", false);
          }

          //modify by wzf 20170510 增加是否跨境标志 end
					makeType = "0";
				}
			});
			// 可疑业务
		} else if (tableset.getSelectedTabNumber() == 1) {
			if (DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")).after(DateUtils.parseDate(DateUtils.formatDate(dsQuery.getCurrentValueAsDate("WORK_DATE"), "yyyyMMdd")))
					&& dsResult1.getRecordCount() == 0) {
				UPrompt.alert(type, "不能进行" + addtype + "!原因:由于非当前工作日期没有可疑数据，因此不能进行" + addtype + "操作！请在当前工作日下进行补报操作！");
				return;
			} else if (DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")).after(DateUtils.parseDate(DateUtils.formatDate(dsQuery.getCurrentValueAsDate("WORK_DATE"), "yyyyMMdd")))
					&& dsResult1.getRecordCount() != 0) {
				dsResult1.setCurrentValue("PERSENT_WORK_DATE", DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")));
				dsBatchNo.setCurrentValue("BATCH_NO", dsResult1.getCurrentValue("BATCH_NO"));
				batchNoCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkBatchNo");
				batchNoCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (!status) {
							UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
						} else {
							UPrompt.confirm(type, "是否将" + addtype + "业务作为当前批次(批次号为:[画面中选择记录的BATCH_NO])" + addtype + "？", new PromptCallback() {
								@Override
								public void execute(Boolean value) {
									if (value != null && value) {
										dsResult1.setCurrentValue("BATCH_NO", dsBatchNo.getCurrentValue("BATCH_NO"));
									} else
										return;
								}
							});
						}
					}
				});
			}
			// 新增的时候需要在dsResult1插入一条空记录,防止查询的时候出现条件传不到后台
			dsResult1.addDataRecord(new DataRecord(), new DataAction() {
				@Override
				public void doAction(DataRecord record) {
					if (DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")).equals(DateUtils.parseDate(DateUtils.formatDate(dsQuery.getCurrentValueAsDate("WORK_DATE"), "yyyyMMdd")))
							&& dsResult1.getRecordCount() == 1) {
						dsResult1.setCurrentValue("PERSENT_WORK_DATE", DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")));
						batchNoCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "queryBatchNo");
						batchNoCmd.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {
								if (!status) {
									UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
								} else {
									dsResult1.setCurrentValue("BATCH_NO", Integer.parseInt(dsBatchNo.getCurrentValue("BATCH_NO")) + 1);
									dsResult1.getCurrentValue("BATCH_NO");
								}
							}
						});
					} else if (DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")).equals(DateUtils.parseDate(DateUtils.formatDate(dsQuery.getCurrentValueAsDate("WORK_DATE"), "yyyyMMdd")))
							&& dsResult1.getRecordCount() > 1) {
						dsResult1.setCurrentValue("PERSENT_WORK_DATE", DateUtils.parseDate(dsWorkDate.getCurrentValue("work_date")));
						batchNoCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "queryBatchNo");
						batchNoCmd.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {
								if (!status) {
									UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
								} else {
									UPrompt.confirm(type, "是否将" + addtype + "业务作为当前批次(批次号为:[画面中选择记录的BATCH_NO])" + addtype + "？", new PromptCallback() {
										@Override
										public void execute(Boolean value) {
											if (value != null && value) {
												dsResult1.setCurrentValue("BATCH_NO", dsBatchNo.getCurrentValue("BATCH_NO"));
											} else {
												dsResult1.setCurrentValue("BATCH_NO", Integer.parseInt(dsBatchNo.getCurrentValue("BATCH_NO")) + 1);
											}
										}
									});
								}
							}
						});
					}
					dsResult1.setCurrentValue("TRADE_MODE","000131");
					detailWindow1.show();
					dsResult1.setCurrentValue("operationType", operationType);
					dsResult1.setCurrentValue("WORK_DATE", dsQuery.getCurrentValueAsDate("WORK_DATE"));
					dsResult1.setCurrentValue("DEPART_ID", ContextHelper.getContext().getValue("departId"));
					if ("19".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE"))) {
						editIcTypeMemo02.setRequired(true);
						editIcTypeMemo02.redraw();
					} else {
						editIcTypeMemo02.setRequired(false);
						editIcTypeMemo02.redraw();
					}
					formAction11.setReadOnly("REF_NO", false);
					formAction11.setReadOnly("STATUS", false);
					dsResult1.sync();
					//modify by wzf 20170510 增加是否跨境标志 begin
          if ("1".equals(dsResult1.getCurrentValue(
                          "IS_CROSSBORDER"))) {
              dsResult1.setCurrentValue("CROSSBORDERflag", true);
              editCROSSBORDERflag02.setValue(true);
          } else {
              dsResult1.setCurrentValue("CROSSBORDERflag", false);
              editCROSSBORDERflag02.setValue(false);
          }

          //modify by wzf 20170510 增加是否跨境标志 end
					showDepartList();
					makeType = "0";
				}
			});
		}

	}

	/**
	 * 修改界面展现
	 * 
	 */
	protected void showMod() {
		if (tableset.getSelectedTabNumber() == 0) {
			checkCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "check");
			checkCmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					
					//modify by wzf 20170510 增加是否跨境标志 begin
          if ("1".equals(dsResult.getCurrentValue(
                          "IS_CROSSBORDER"))) {
              dsResult.setCurrentValue("CROSSBORDERflag", true);
          } else {
              dsResult.setCurrentValue("CROSSBORDERflag", false);
          }

          //modify by wzf 20170510 增加是否跨境标志 end
					if (!status) {
						//pw 20150921 begin(37 - 已审核纪录maker无法查看 )
						//UPrompt.alert(type, callback.getErrorMessage());
						UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){

							@Override
							public void execute(Boolean value) {
								// TODO Auto-generated method stub
								if (value != null && value) {
									// 证件类型初始化设置
									selectAcctOne();
									detailWindow.show();
									btnSave.hide();
									btncancel.setTitle("关闭");
								} else
									return;
							}
							
						} );
						//end
						dsResult.setCommand(queryCmd);

					} else {
						//add by pw 20150918 begin
						//60(对于已上报状态且未导入回执的AML数据，系统不应允许修改 )
						if ("11".equals(dsResult.getCurrentValue("RPT_STATUS"))) {
							//UPrompt.alert(type, "不能修改，导入回执后才允许修改。");
							UPrompt.confirm(type, "状态为已上报，需要导入回执后才能进行修改操作。"+"是否查看明细", new PromptCallback(){
								@Override
								public void execute(Boolean value) {
									// TODO Auto-generated method stub
									if (value != null && value) {
										// 证件类型初始化设置
										selectAcctOne();
										detailWindow.show();
										btnSave.hide();
										btncancel.setTitle("关闭");
									} else
										return;
								}
								
							} );
							return;
							
						}
						//end
						btnSave.setVisible(true);
						btncancel.setTitle("取消");
						formAction1.setReadOnly("REF_NO", true);
						dsResult.setCurrentValue("ruleCodeg01", dsResult.getCurrentValue("RULE_CODE"));
						// 大额证件类型初始化
						if ("19".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE"))) {
							editIcTypeMemo01.setRequired(true);
							editIcTypeMemo01.redraw();
						} else {
							editIcTypeMemo01.setRequired(false);
							editIcTypeMemo01.redraw();
						}
						// 大额对手证件类型初始化
						if ("19".equals(dsResult.getCurrentValue("CTPY_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CTPY_IC_TYPE"))) {
							editCtpyIcTypeMemo01.setRequired(true);
							editCtpyIcTypeMemo01.redraw();
						} else {
							editCtpyIcTypeMemo01.setRequired(false);
							editCtpyIcTypeMemo01.redraw();
						}
						// 大额代办人证件类型初始化
						if ("19".equals(dsResult.getCurrentValue("AGENT_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("AGENT_IC_TYPE"))) {
							editAgentIcTypeMemo01.setRequired(true);
							editAgentIcTypeMemo01.redraw();
						} else {
							editAgentIcTypeMemo01.setRequired(false);
							editAgentIcTypeMemo01.redraw();
						}

						if ("CHN".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "Z01".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))
								|| "Z02".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "Z03".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))
								|| "@N".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))) {
							formAction2.setReadOnly("TRADE_REGION", false);
						} else {
							formAction2.setReadOnly("TRADE_REGION", true);
						}
						if ("CHN".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z01".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))
								|| "Z02".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z03".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))
								|| "@N".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))) {
							formAction2.setReadOnly("TRADE_VENUE_REGION", false);
						} else {
							formAction2.setReadOnly("TRADE_VENUE_REGION", true);
						}
						detailWindow.show();
						if(dsResult.getCurrentValue("RELATION_TYPE") == null ||"".equals(dsResult.getCurrentValue("RELATION_TYPE"))){							
							dsResult.setCurrentValue("RELATION_TYPE","00");					
						}	
						if(dsResult.getCurrentValue("TRADE_MODE") == null ||"".equals(dsResult.getCurrentValue("TRADE_MODE"))){							
							dsResult.setCurrentValue("TRADE_MODE","000131");
						}	
						//add by wzf 20150505 begin
						//给ruleCode01(原规则代码)字段赋值
						dsResult.setCurrentValue("ruleCode01", dsResult.getCurrentValue("RULE_CODE"));
						//add by wzf 20150505 end
						dsResult.sync();					
						makeType = "1";
						selectName = "ActId01";
                        selectAcctOne();

					}
				}
			});
		} else {
			checkCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "check");
			checkCmd1.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {

					//modify by wzf 20170510 增加是否跨境标志 begin
          if ("1".equals(dsResult1.getCurrentValue(
                          "IS_CROSSBORDER"))) {
              dsResult1.setCurrentValue("CROSSBORDERflag", true);
          } else {
              dsResult1.setCurrentValue("CROSSBORDERflag", false);
          }

          //modify by wzf 20170510 增加是否跨境标志 end
          
					if (!status) {
						//pw 20150921 begin(37 - 已审核纪录maker无法查看 )
						//UPrompt.alert(type, callback.getErrorMessage());
						UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){

							@Override
							public void execute(Boolean value) {
								// TODO Auto-generated method stub
								if (value != null && value) {
									// 证件类型初始化设置
									selectAcctOne();
									detailWindow1.show();
									btnSave1.hide();
									btncancel1.setTitle("关闭");
								} else
									return;
							}
							
						} );
						//end
						dsResult1.setCommand(queryCmd);

					} else {
						//add by pw 20150918 begin
						//60(对于已上报状态且未导入回执的AML数据，系统不应允许修改 )
						if ("11".equals(dsResult1.getCurrentValue("RPT_STATUS"))) {
							//UPrompt.alert(type, "不能修改，导入回执后才允许修改。");
							UPrompt.confirm(type, "状态为已上报，需要导入回执后才能进行修改操作。"+"是否查看明细", new PromptCallback(){
								@Override
								public void execute(Boolean value) {
									// TODO Auto-generated method stub
									if (value != null && value) {
										// 证件类型初始化设置
										selectAcctOne();
										detailWindow1.show();
										btnSave1.hide();
										btncancel1.setTitle("关闭");
									} else
										return;
								}
								
							} );
							return;
							
						}
						btnSave1.setVisible(true);
						btncancel1.setTitle("取消");
						//end
						dsResult1.setCurrentValue("ruleCode02", dsResult1.getCurrentValue("RULE_CODE"));
						dsResult1.setCurrentValue("ifRpt_flag", false);
						dsResult1.sync();
						formAction11.setReadOnly("REF_NO", true);
						formAction11.setReadOnly("STATUS", true);

						// 可疑证件类型初始化
						if ("19".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE"))) {
							editIcTypeMemo02.setRequired(true);
							editIcTypeMemo02.redraw();
						} else {
							editIcTypeMemo02.setRequired(false);
							editIcTypeMemo02.redraw();
						}
						// 可疑对手证件类型
						if ("19".equals(dsResult1.getCurrentValue("CTPY_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CTPY_IC_TYPE"))) {
							editCtpyIcTypeMemo02.setRequired(true);
							editCtpyIcTypeMemo02.redraw();
						} else {
							editCtpyIcTypeMemo02.setRequired(false);
							editCtpyIcTypeMemo02.redraw();
						}
						// 可疑代办人证件类型
						if ("19".equals(dsResult1.getCurrentValue("AGENT_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("AGENT_IC_TYPE"))) {
							editAgentIcTypeMemo02.setRequired(true);
							editAgentIcTypeMemo02.redraw();
						} else {
							editAgentIcTypeMemo02.setRequired(false);
							editAgentIcTypeMemo02.redraw();
						}
						// 可疑法人证件类型
						if ("19".equals(dsResult1.getCurrentValue("REP_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("REP_IC_TYPE"))) {
							editRepIcTypeMemo02.setRequired(true);
							editRepIcTypeMemo02.redraw();
						} else {
							editRepIcTypeMemo02.setRequired(false);
							editRepIcTypeMemo02.redraw();
						}

						if ("CHN".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z01".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))
								|| "Z02".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z03".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))
								|| "@N".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))) {
							formAction12.setReadOnly("TRADE_VENUE_REGION", false);
						} else {
							formAction12.setReadOnly("TRADE_VENUE_REGION", true);
						}

						if ("CHN".equals(dsResult1.getCurrentValue("CTPY_FI_REGION_CODE")) || "Z01".equals(dsResult1.getCurrentValue("CTPY_FI_REGION_CODE"))
								|| "Z02".equals(dsResult1.getCurrentValue("CTPY_FI_REGION_CODE")) || "Z02".equals(dsResult1.getCurrentValue("CTPY_FI_REGION_CODE"))
								|| "@N".equals(dsResult.getCurrentValue("CTPY_FI_REGION_CODE"))) {
							formAction17.setReadOnly("CTPY_FI_REGION_CODE", false);
						} else {
							formAction17.setReadOnly("CTPY_FI_REGION_CODE", true);
						}
						if(dsResult1.getCurrentValue("TRADE_MODE") == null ||"".equals(dsResult1.getCurrentValue("TRADE_MODE"))){							
							dsResult1.setCurrentValue("TRADE_MODE","000131");
						}	
						//add by wzf 20150505 begin
						//给ruleCode02(原规则代码)字段赋值
						dsResult1.setCurrentValue("ruleCode02", dsResult1.getCurrentValue("RULE_CODE"));
						//add by wzf 20150505 end

						detailWindow1.show();
						selectName = "ActId02";
                        selectAcctOne();
                        
						makeType = "1";
					}
				}
			});
		}
	}

	/**
	 * 交易方向赋值
	 */
	protected void setWhither() {
		DataRecord dataRecord = dsWhither.getCurrentRecord();
		if ("TradeCountry01".equals(selectName)) {
			dsResult.setCurrentValue("TRADE_COUNTRY", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult.sync();
		} else if ("TradeRegion01".equals(selectName)) {
			dsResult.setCurrentValue("TRADE_REGION", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult.sync();
		} else if ("TradeVenueCountry01".equals(selectName)) {
			dsResult.setCurrentValue("TRADE_VENUE_COUNTRY", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult.sync();
		} else if ("TradeVenueRegion01".equals(selectName)) {
			dsResult.setCurrentValue("TRADE_VENUE_REGION", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult.sync();
		} else if ("TradeVenueCountry02".equals(selectName)) {
			dsResult1.setCurrentValue("TRADE_VENUE_COUNTRY", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult1.sync();
		} else if ("TradeVenueRegion02".equals(selectName)) {
			dsResult1.setCurrentValue("TRADE_VENUE_REGION", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult1.sync();
		} else if ("OrganizationCountry02".equals(selectName)) {
			dsResult1.setCurrentValue("CTPY_FI_COUNTRY", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult1.sync();
		} else if ("OrganizationRegion02".equals(selectName)) {
			dsResult1.setCurrentValue("CTPY_FI_REGION_CODE", dataRecord.getValue("NATIONREGION_CODE"));
			dsResult1.sync();
		}
		dsWhither.removeAllData();
		dsWhither.sync();
		whitherWindow.hide();

		if ("TradeCountry01".equals(selectName)) {

			// 大额去向国家赋值
			if ("CHN".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "Z01".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "Z02".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))
					|| "Z03".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "@N".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))) {
				formAction2.setReadOnly("TRADE_REGION", false);
				dsResult.setCurrentValue("TRADE_REGION", "");
				dsResult.sync();
			} else {
				dsResult.setCurrentValue("TRADE_REGION", "000000");
				dsResult.sync();
				formAction2.setReadOnly("TRADE_REGION", true);
			}
		} else if ("TradeVenueCountry01".equals(selectName)) {

			// 大额发生国家地区查询
			if ("CHN".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z01".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))
					|| "Z02".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z03".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))
					|| "@N".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))) {
				formAction2.setReadOnly("TRADE_VENUE_REGION", false);
				dsResult.setCurrentValue("TRADE_VENUE_REGION", "");
				dsResult.sync();
			} else {
				dsResult.setCurrentValue("TRADE_VENUE_REGION", "000000");
				dsResult.sync();
				formAction2.setReadOnly("TRADE_VENUE_REGION", true);
			}

		} else if ("TradeVenueCountry02".equals(selectName)) {

			// 可疑发生国家地区赋值
			if ("CHN".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z01".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))
					|| "Z02".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z03".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))
					|| "@N".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))) {
				dsResult1.setCurrentValue("TRADE_VENUE_REGION", "");
				dsResult1.sync();
				formAction12.setReadOnly("TRADE_VENUE_REGION", false);
			} else {
				dsResult1.setCurrentValue("TRADE_VENUE_REGION", "000000");
				dsResult1.sync();
				formAction12.setReadOnly("TRADE_VENUE_REGION", true);
			}
		} else if ("OrganizationCountry02".equals(selectName)) {
			// 可疑对方金融机构国家地区赋值
			if ("CHN".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY")) || "Z01".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY")) || "Z02".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY"))
					|| "Z03".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY")) || "@N".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY"))) {
				dsResult1.setCurrentValue("CTPY_FI_REGION_CODE", "");
				dsResult1.sync();
				formAction17.setReadOnly("CTPY_FI_REGION_CODE", false);
			} else {
				dsResult1.setCurrentValue("CTPY_FI_REGION_CODE", "000000");
				dsResult1.sync();
				formAction17.setReadOnly("CTPY_FI_REGION_CODE", true);
			}
		}

	}

	/**
	 * 
	 * 交易代码赋值
	 */
	protected void setMeta() {
		DataRecord dataRecord = dsMeta.getCurrentRecord();
		if ("TradeCode01".equals(selectName)) {
			dsResult.setCurrentValue("TRADE_CODE", dataRecord.getValue("META_VALUE"));
			dsResult.sync();
		} else if ("TradeCode02".equals(selectName)) {
			dsResult1.setCurrentValue("TRADE_CODE", dataRecord.getValue("META_VALUE"));
			dsResult1.sync();
		}
		dsMeta.removeAllData();
		dsMeta.sync();
		MetaWindow.hide();
	}
	
	/**
     * 
     * 疑似涉罪类型赋值
     */
    protected void setBsTosc() {
    	DataRecord[] selectedRecords = dsBsTosc.getSelectedRecords();
    	String metaValues = "";
    	String metas = "";
    	int length = selectedRecords.length;
    	for (int i=0;i<length;i++) {
            System.out.println("---------------------META_VALUE------------"+selectedRecords[i].getValue("META_VALUE"));
            metaValues += selectedRecords[i].getValue("META_VALUE");
            metas += selectedRecords[i].getValue("META_VALUE") + "-" + selectedRecords[i].getValue("META_NAME");
            if(i != length-1){
            	metaValues += ',';
            	metas += '\n'; 
            }
        }
    	dsResult1.setCurrentValue("BS_TOSC", metaValues);
    	dsResult1.setCurrentValue("BS_TOSCInfo", metas);
    	dsResult1.sync();
        dsBsTosc.removeAllData();
        dsBsTosc.sync();
        BsToscWindow.hide();
    }
    
	/**
	 * 规则代码赋值
	 */
	protected void setRuleCode() {
		DataRecord dataRecord = dsRuleCode.getCurrentRecord();
		if ("RuleCode01".equals(selectName)) {
			dsResult.setCurrentValue("RULE_CODE", dataRecord.getValue("RULE_CODE"));
			dsResult.sync();
		} else if ("RuleCode02".equals(selectName)) {
			dsResult1.setCurrentValue("RULE_CODE", dataRecord.getValue("RULE_CODE"));
			dsResult1.sync();
		}
		dsRuleCode.removeAllData();
		dsRuleCode.sync();
		RuleCodeWindow.hide();
		checkRefnoAndRefNo();
	}

	/**
	 * 客户信息赋值
	 * 
	 */
	protected void setClient() {
		DataRecord dataRecord = dsClient.getCurrentRecord();
		if ("ClientId01".equals(selectName)) {
			dsResult.setCurrentValue("CLIENT_ID", dataRecord.getValue("CLIENT_ID"));
			dsResult.setCurrentValue("PCODE", dataRecord.getValue("PCODE"));
			dsResult.setCurrentValue("CLIENT_TYPE", dataRecord.getValue("CLIENT_TYPE"));
			dsResult.setCurrentValue("CLIENT_IC_TYPE", dataRecord.getValue("IC_TYPE"));
			dsResult.setCurrentValue("CLIENT_IC_TYPE_MEMO", dataRecord.getValue("IC_TYPE_MEMO"));
			dsResult.setCurrentValue("CLIENT_IC_NO", dataRecord.getValue("IC_NO"));
			dsResult.setCurrentValue("CLIENT_NAME", dataRecord.getValue("NAME"));
			dsResult.setCurrentValue("CLIENT_NATIONALITY", dataRecord.getValue("NATIONALITY"));
			dsResult.setCurrentValue("PHONE", dataRecord.getValue("PHONE"));
			dsResult.setCurrentValue("ADDRESS", dataRecord.getValue("ADDRESS"));
			dsResult.setCurrentValue("OTHER_CONTACT", dataRecord.getValue("OTHER_CONTACT"));
			dsResult.setCurrentValue("INDUSTRY_TYPE", dataRecord.getValue("INDUSTRY_TYPE"));
			dsResult.setCurrentValue("REGISTERED_CAPITAL", dataRecord.getValue("REGISTERED_CAPITAL"));
			dsResult.setCurrentValue("REP_NAME", dataRecord.getValue("REP_NAME"));
			dsResult.setCurrentValue("REP_IC_TYPE", dataRecord.getValue("REP_IC_TYPE"));
			dsResult.setCurrentValue("REP_IC_TYPE_MEMO", dataRecord.getValue("REP_IC_TYPE_MEMO"));
			dsResult.setCurrentValue("REP_IC_NO", dataRecord.getValue("REP_IC_NO"));
			dsResult.setCurrentValue("CLIENT_IC_OTHER_TYPE", dataRecord.getValue("IC_OTHER_TYPE"));
			dsResult.setCurrentValue("BANK_RELATIONSHIP", dataRecord.getValue("BANK_RELATIONSHIP"));
			dsResult.setCurrentValue("REP_IC_OTHER_TYPE", dataRecord.getValue("REP_IC_OTHER_TYPE"));
			dsResult.sync();
			if ("19".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE"))) {
				editIcTypeMemo01.setRequired(true);
				editIcTypeMemo01.redraw();
			} else {
				editIcTypeMemo01.setRequired(false);
				editIcTypeMemo01.redraw();
			}
		} else if ("ClientId02".equals(selectName)) {
			dsResult1.setCurrentValue("CLIENT_ID", dataRecord.getValue("CLIENT_ID"));
			dsResult1.setCurrentValue("PCODE", dataRecord.getValue("PCODE"));
			dsResult1.setCurrentValue("CLIENT_TYPE", dataRecord.getValue("CLIENT_TYPE"));
			dsResult1.setCurrentValue("CLIENT_IC_TYPE", dataRecord.getValue("IC_TYPE"));
			dsResult1.setCurrentValue("CLIENT_IC_TYPE_MEMO", dataRecord.getValue("IC_TYPE_MEMO"));
			dsResult1.setCurrentValue("CLIENT_IC_NO", dataRecord.getValue("IC_NO"));
			dsResult1.setCurrentValue("CLIENT_NAME", dataRecord.getValue("NAME"));
			dsResult1.setCurrentValue("CLIENT_NATIONALITY", dataRecord.getValue("NATIONALITY"));
			dsResult1.setCurrentValue("PHONE", dataRecord.getValue("PHONE"));
			dsResult1.setCurrentValue("ADDRESS", dataRecord.getValue("ADDRESS"));
			dsResult1.setCurrentValue("OTHER_CONTACT", dataRecord.getValue("OTHER_CONTACT"));
			dsResult1.setCurrentValue("INDUSTRY_TYPE", dataRecord.getValue("INDUSTRY_TYPE"));
			dsResult1.setCurrentValue("REGISTERED_CAPITAL", dataRecord.getValue("REGISTERED_CAPITAL"));
			dsResult1.setCurrentValue("REP_NAME", dataRecord.getValue("REP_NAME"));
			dsResult1.setCurrentValue("REP_IC_TYPE", dataRecord.getValue("REP_IC_TYPE"));
			dsResult1.setCurrentValue("REP_IC_TYPE_MEMO", dataRecord.getValue("REP_IC_TYPE_MEMO"));
			dsResult1.setCurrentValue("REP_IC_NO", dataRecord.getValue("REP_IC_NO"));
			dsResult1.setCurrentValue("CLIENT_IC_OTHER_TYPE", dataRecord.getValue("IC_OTHER_TYPE"));
			dsResult1.setCurrentValue("BANK_RELATIONSHIP", dataRecord.getValue("BANK_RELATIONSHIP"));
			dsResult1.setCurrentValue("REP_IC_OTHER_TYPE", dataRecord.getValue("REP_IC_OTHER_TYPE"));
			dsResult1.sync();
			if ("19".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE"))) {
				editIcTypeMemo02.setRequired(true);
				editIcTypeMemo02.redraw();
			} else {
				editIcTypeMemo02.setRequired(false);
				editIcTypeMemo02.redraw();
			}
			if ("19".equals(dsResult1.getCurrentValue("REP_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("REP_IC_TYPE"))) {
				editRepIcTypeMemo02.setRequired(true);
				editRepIcTypeMemo02.redraw();
			} else {
				editRepIcTypeMemo02.setRequired(false);
				editRepIcTypeMemo02.redraw();
			}
		}
		dsClient.removeAllData();
		dsClient.sync();
		ClientWindow.hide();
	}

	/**
	 * 账号信息赋值
	 */
	protected void setAcct() {
		DataRecord dataRecord = dsAcct.getCurrentRecord();
		if ("ActId01".equals(selectName)) {	
			
//			dsResult.setCurrentValue("PBOC_NUM_ACCT", dataRecord.getValue("PBOC_NUM_ACCT"));				
			dsResult.setCurrentValue("ACCT_ID", dataRecord.getValue("ACCT_ID"));
			dsResult.setCurrentValue("ACCT_TYPE", dataRecord.getValue("ACCT_TYPE"));
			dsResult.setCurrentValue("ACCT_OPEN_TIME", dataRecord.getValue("OPEN_TIME"));
			dsResult.setCurrentValue("ACCT_CLOSE_TIME", dataRecord.getValue("CLOSE_TIME"));
			
			if(dataRecord.getValue("ACCT_ID")!=null && !"".equals(dataRecord.getValue("ACCT_ID")) && dataRecord.getValue("ACCT_ID").substring(0, 3).equals("NRA")){
				if(dataRecord.getValue("PBOC_NUM_ACCT")!=null&&!"".equals(dataRecord.getValue("PBOC_NUM_ACCT"))){
					dsResult.setCurrentValue("PBOC_NUM_ACCT", "NRA"+dataRecord.getValue("PBOC_NUM_ACCT"));
				}
				else{
					dsResult.setCurrentValue("PBOC_NUM_ACCT", dataRecord.getValue("PBOC_NUM_ACCT"));	
				}
			}else{
				dsResult.setCurrentValue("PBOC_NUM_ACCT", dataRecord.getValue("PBOC_NUM_ACCT"));	
			}
			
			formAction5.setReadOnly("ACCT_TYPE",true);	
			formAction5.setReadOnly("PBOC_NUM_ACCT",true);	
			dsResult.sync();
		} else if ("ActId02".equals(selectName)) {	
//			dsResult1.setCurrentValue("PBOC_NUM_ACCT", dataRecord.getValue("PBOC_NUM_ACCT"));				
			dsResult1.setCurrentValue("ACCT_ID", dataRecord.getValue("ACCT_ID"));
			dsResult1.setCurrentValue("ACCT_TYPE", dataRecord.getValue("ACCT_TYPE"));
			dsResult1.setCurrentValue("ACCT_OPEN_TIME", dataRecord.getValue("OPEN_TIME"));
			dsResult1.setCurrentValue("ACCT_CLOSE_TIME", dataRecord.getValue("CLOSE_TIME"));
			
			if(dataRecord.getValue("ACCT_ID")!=null && !"".equals(dataRecord.getValue("ACCT_ID")) && dataRecord.getValue("ACCT_ID").substring(0, 3).equals("NRA")){
				if(dataRecord.getValue("PBOC_NUM_ACCT")!=null&&!"".equals(dataRecord.getValue("PBOC_NUM_ACCT"))){
					dsResult1.setCurrentValue("PBOC_NUM_ACCT", "NRA"+dataRecord.getValue("PBOC_NUM_ACCT"));
				}
				else{
					dsResult1.setCurrentValue("PBOC_NUM_ACCT", dataRecord.getValue("PBOC_NUM_ACCT"));	
				}
			}else{
				dsResult1.setCurrentValue("PBOC_NUM_ACCT", dataRecord.getValue("PBOC_NUM_ACCT"));	
			}
			
			formAction15.setReadOnly("ACCT_TYPE",true);	
			formAction15.setReadOnly("PBOC_NUM_ACCT",true);	
			formAction15.setReadOnly("ACCT_OPEN_TIME",true);	
			formAction15.setReadOnly("ACCT_CLOSE_TIME",true);	
			dsResult1.sync();
		}
		dsAcct.removeAllData();
		dsAcct.sync();
		AcctWindow.hide();
	}

	/**
	 * 对方金融机构信息赋值
	 */
	protected void setOrganization() {
		DataRecord dataRecord = dsOrganization.getCurrentRecord();
		if ("OrgenizationCode01".equals(selectName)) {
			dsResult.setCurrentValue("CTPY_FI_CODE", dataRecord.getValue("CTPY_FI_CODE"));
			dsResult.setCurrentValue("CTPY_FI_NAME", dataRecord.getValue("CTPY_FI_NAME"));
			dsResult.setCurrentValue("CTPY_FI_TYPE", dataRecord.getValue("CTPY_FI_TYPE"));
			dsResult.setCurrentValue("CTPY_FI_COUNTRY", dataRecord.getValue("CTPY_FI_COUNTRY"));
			dsResult.setCurrentValue("CTPY_FI_REGION_CODE", dataRecord.getValue("CTPY_FI_REGION_CODE"));
			dsResult.setCurrentValue("CTPY_FI_PCODE", dataRecord.getValue("CTPY_FI_PCODE"));
			dsResult.sync();
		} else if ("OrgenizationCode02".equals(selectName)) {
			dsResult1.setCurrentValue("CTPY_FI_CODE", dataRecord.getValue("CTPY_FI_CODE"));
			dsResult1.setCurrentValue("CTPY_FI_NAME", dataRecord.getValue("CTPY_FI_NAME"));
			dsResult1.setCurrentValue("CTPY_FI_TYPE", dataRecord.getValue("CTPY_FI_TYPE"));
			dsResult1.setCurrentValue("CTPY_FI_COUNTRY", dataRecord.getValue("CTPY_FI_COUNTRY"));
			dsResult1.setCurrentValue("CTPY_FI_REGION_CODE", dataRecord.getValue("CTPY_FI_REGION_CODE"));
			dsResult1.setCurrentValue("CTPY_FI_PCODE", dataRecord.getValue("CTPY_FI_PCODE"));
			dsResult1.sync();
		}
		dsOrganization.removeAllData();
		dsOrganization.sync();
		OrganizationWindow.hide();
	}

	/**
	 * 交易对手信息赋值
	 */
	protected void setCounterparty() {
		DataRecord dataRecord = dsCounterparty.getCurrentRecord();
		if ("CtpyAccountId01".equals(selectName)) {
			dsResult.setCurrentValue("CTPY_NAME", dataRecord.getValue("CTPY_NAME"));
			dsResult.setCurrentValue("CTPY_TYPE", dataRecord.getValue("CTPY_TYPE"));
			dsResult.setCurrentValue("CTPY_NATIONALITY", dataRecord.getValue("CTPY_NATIONALITY"));
			dsResult.setCurrentValue("CTPY_ACCT_TYPE", dataRecord.getValue("CTPY_ACCT_TYPE"));
			//add by pw 20151019 begin
			// FS#53 - 补录和复核时要求手工输入账号
			//dsResult.setCurrentValue("CTPY_ACCT_ID", dataRecord.getValue("CTPY_ACCT_ID"));
			dsResult.setCurrentValue("CTPY_ACCT_ID", editCounterpartyAccountid1.getValue());
			//add by pw end
			dsResult.setCurrentValue("CTPY_IC_TYPE", dataRecord.getValue("CTPY_IC_TYPE"));
			dsResult.setCurrentValue("CTPY_IC_TYPE_MEMO", dataRecord.getValue("CTPY_IC_TYPE_MEMO"));
			dsResult.setCurrentValue("CTPY_IC_CODE", dataRecord.getValue("CTPY_IC_CODE"));
			dsResult.sync();
			if ("19".equals(dsResult.getCurrentValue("CTPY_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CTPY_IC_TYPE"))) {
				editCtpyIcTypeMemo01.setRequired(true);
				editCtpyIcTypeMemo01.redraw();
			} else {
				editCtpyIcTypeMemo01.setRequired(false);
				editCtpyIcTypeMemo01.redraw();
			}
		} else if ("CtpyAccountId02".equals(selectName)) {
			dsResult1.setCurrentValue("CTPY_NAME", dataRecord.getValue("CTPY_NAME"));
			dsResult1.setCurrentValue("CTPY_TYPE", dataRecord.getValue("CTPY_TYPE"));
			dsResult1.setCurrentValue("CTPY_NATIONALITY", dataRecord.getValue("CTPY_NATIONALITY"));
			//add by pw 20151019 begin
			// FS#53 - 补录和复核时要求手工输入账号
			//dsResult1.setCurrentValue("CTPY_ACCT_ID", dataRecord.getValue("CTPY_ACCT_ID"));
			dsResult.setCurrentValue("CTPY_ACCT_ID", editCounterpartyAccountid1.getValue());
			//add by pw end
			dsResult1.setCurrentValue("CTPY_ACCT_TYPE", dataRecord.getValue("CTPY_ACCT_TYPE"));
			dsResult1.setCurrentValue("CTPY_IC_TYPE", dataRecord.getValue("CTPY_IC_TYPE"));
			dsResult1.setCurrentValue("CTPY_IC_TYPE_MEMO", dataRecord.getValue("CTPY_IC_TYPE_MEMO"));
			dsResult1.setCurrentValue("CTPY_IC_CODE", dataRecord.getValue("CTPY_IC_CODE"));
			dsResult1.sync();
			if ("19".equals(dsResult1.getCurrentValue("CTPY_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CTPY_IC_TYPE"))) {
				editCtpyIcTypeMemo02.setRequired(true);
				editCtpyIcTypeMemo02.redraw();
			} else {
				editCtpyIcTypeMemo02.setRequired(false);
				editCtpyIcTypeMemo02.redraw();
			}
		}
		dsCounterparty.removeAllData();
		dsCounterparty.sync();
		CounterpartyWindow.hide();
	}

	@Override
	protected void beforeRender() {
		super.beforeRender();
		flag = false;
		queryDatasetAcc.addDataRecord(new DataRecord(),new DataAction() {
			
			@Override
			public void doAction(DataRecord record) {
				// TODO Auto-generated method stub
			}
		});
		// 币种下拉字典的加载
		qryCurrencyCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "selectCurrencyList");
		qryCurrencyCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "币种信息查询错误，错误信息：" + callback.getErrorMessage());
				}
				editCurCode01.flushData();
				editCurCode02.flushData();
				editAcctCur2.flushData();
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
				editDepartId01.flushData();
				editDepartId02.flushData();
			}
		});

		// 工作日期查询
		queryWorkDateCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "getPreWorkDate");
		queryWorkDateCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				// 进入界面开始查询
				dsQuery.addDataRecord(new DataRecord(), new DataAction() {
					@Override
					public void doAction(DataRecord record) {
						String workDate = dsWorkDate.getCurrentValue("work_date");
						dsQuery.setCurrentValue("WORK_DATE", DateUtils.parseDate(workDate));
						listQuery.editRecord(dsQuery.getCurrentRecord().getViewRecord());
						showList();
					}
				});
			}
		});
		
		/**
		 * 上传email附件
		 */
		uploadEditor.setServletPath("/ImportUploadAcc.gupld");
		
		/**
		 * 完成上传后插入所上传文件信息到数据库
		 */
		uploadEditor.setUploadListener(new UploadListener() {
			
			@Override
			public void uploadComplete(String filename) {
				if (!uploadEditor.getStatus()) {
					UPrompt.alert(typeinner, "附件上传出错，请重新上传");
					return;
				}else if (filename == null || "".equals(filename.trim())) {
					UPrompt.alert(typeinner, "附件上传出错，请重新上传");
					return;
				}else{
					UPrompt.alert(typeinner, "上传完成");
				}
				// 上传成功，插入上传日志--试一下tableResultAcc--原来是queryDatasetAcc
			
				//queryDatasetAcc.setCurrentValue("ACC_PATH","C:\\Barclays\\importfile\\emailAcc\\"+uploadEditor.getFileFullPath().substring(12));//附件路径
				//System.out.println("-----------getFile()--------"+uploadEditor.getFile()+"-----------getFileFullPath()--------"+uploadEditor.getFileFullPath()+"-------uploadFilePath-----------"+uploadFilePath);
				queryDatasetAcc.setCurrentValue("ACC_NAME",uploadEditor.getFileFullPath().substring(12));//31
				queryDatasetAcc.setCurrentValue("REF_NO", dsResult1.getCurrentValue("REF_NO"));//当前所选数据业务流水号
				String str = queryDatasetAcc.getCurrentValue("REF_NO");
				insertCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "insertAcc");
				insertCmdAcc.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (!status) {
							UPrompt.alert(typeinner, "上传附件信息插入数据库错误：" + callback.getErrorMessage());
						} else {
							UPrompt.alert(typeinner, "上传完成！ ");
							showListAcc();
					    }
				     }
			    });
			}
		});
		
		
		
		// 规则代码信息初始化查询
		dsRuleCodeQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});
		// 交易方向信息初始化查询
		dsWhitherQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});
		// 交易代码信息初始化查询
		dsMetaQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});

		// 客户信息初始化查询
		dsClientQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});

		// 账户信息初始化查询
		dsAcctQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});

		// 对方金融机构信息查询信息初始化查询
		dsOrganizationQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});

		// 对手信息初始化查询
		dsCounterpartyQuery.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});
		// 业务流水信息初始化查询
		dsRefNo.addDataRecord(new DataRecord(), new DataAction() {
			@Override
			public void doAction(DataRecord record) {
			}
		});

	}

	@Override
	protected void bindEvent() {
		// 表格改变事件
		tableset.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				// TODO Auto-generated method stub
				if (flag) {
					showList();
				}

			}
		});

		// 窗口关闭的解锁事件
		detailWindow.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				detailWindow.hide();
				getunlock();
				showList();
			}

		});
		// 窗口关闭的解锁事件
		detailWindow1.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				detailWindow1.hide();
				getunlock();
				showList();
			}
		});

		// 窗口关闭的解锁事件
		deleteWindow.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				deleteWindow.hide();
				getunlock();
				showList();
			}
		});
		// 窗口关闭的解锁事件
		deleteWindow1.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				deleteWindow1.hide();
				getunlock();
				showList();
			}
		});
		// 选中一条记录的事件
		tableResult.addRecordClickListener(new RecordClickListener() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if (dsResult.getCurrentValue("IS_DEL") != null && "1".equals(dsResult.getCurrentValue("IS_DEL"))) {
					btnDel.setTitle("取消删除");
					delNo = "1";
				} else if (dsResult.getCurrentValue("IS_DEL") != null && "0".equals(dsResult.getCurrentValue("IS_DEL"))) {
					btnDel.setTitle("删除");
					delNo = "0";
				}
			}
		});
		// 选中一条记录的事件
		tableResult1.addRecordClickListener(new RecordClickListener() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if (dsResult1.getCurrentValue("IS_DEL") != null && "1".equals(dsResult1.getCurrentValue("IS_DEL"))) {
					btnDel.setTitle("取消删除");
					delNo = "1";
				} else if (dsResult1.getCurrentValue("IS_DEL") != null && "0".equals(dsResult1.getCurrentValue("IS_DEL"))) {
					btnDel.setTitle("删除");
					delNo = "0";
				}
			}
		});

		// 双击表格修改事件
		tableResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				//add by wzf 20150406 begin
				//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改
				checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
				checkdeleteCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {							
						if (!status) {
							UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
								@Override
								public void execute(Boolean value) {
									if (value != null && value) {
										showMod();
									}
								}
							});

						}else{
							showMod();
						}
					}
				});
				//add by wzf 20150406 end
			}

		});

		// 双击表格修改事件
		tableResult1.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				//add by wzf 20150406 begin
				//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改
				checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
				checkdeleteCmd1.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {							
						if (!status) {
							UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
								@Override
								public void execute(Boolean value) {
									if (value != null && value) {
										showMod();
									}
								}
							});

						}else{
							showMod();
						}
					}
				});
				//add by wzf 20150406 end
			}

		});
		// 双击赋值交易方向
		tableWhitherResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setWhither();
			}
		});

		// 双击赋值交易代码
		tableMetaResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setMeta();
			}
		});
		
		// 双击疑似犯罪类型
        tableBsToscResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
            @Override
            public void onRecordClick(RecordDoubleClickEvent event) {
                setBsTosc();
            }
        });
        
		// 双击赋值规则代码信息事件
		tableRuleCodeResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setRuleCode();
			}
		});

		// 双击赋值客户信息事件
		tableClientResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setClient();
			}
		});

		// 双击赋值账号信息事件
		tableAcctResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setAcct();
			}

		});

		// 双击赋值对方金融机构信息事件
		tableOrganizationResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setOrganization();
			}

		});

		// 双击赋值交易对手信息事件
		tableCounterpartyResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				setCounterparty();
			}
		});
		
		// 大额的资金用途改变事件
		this.editFundUsageType01.addValueChangedListener(new ValueChangedListener("FUND_USAGE_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				dsResult.setCurrentValue("FUND_USAGE",	editFundUsageType01.getDisplayValue().substring(2));
				dsResult.sync();
			}
		});
		
		// 可疑的资金用途改变事件
		this.editFundUsageType02.addValueChangedListener(new ValueChangedListener("FUND_USAGE_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				dsResult1.setCurrentValue("FUND_USAGE",editFundUsageType02.getDisplayValue().substring(2));
				dsResult1.sync();
			}
		});

		// 大额的规则代码改变事件
		this.editRuleCode01.addValueChangedListener(new ValueChangedListener("RULE_CODE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if (!dsResult.getCurrentValue("ruleCode01").equals(dsResult.getCurrentValue("RULE_CODE"))) {
					checkRefnoAndRefNo();
				}
			}
		});

		// 可疑的规则代码改变事件
		this.editRuleCode02.addValueChangedListener(new ValueChangedListener("RULE_CODE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if (!dsResult1.getCurrentValue("ruleCode02").equals(dsResult.getCurrentValue("RULE_CODE"))) {
					checkRefnoAndRefNo();
				}
			}
		});

		// 大额补录的网点代码下拉框数值改变事件
		this.editDepartId01.addValueChangedListener(new ValueChangedListener("DEPART_ID") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				showDepartList();
			}

		});
		// 可疑补录的网点代码下拉框数值改变事件
		this.editDepartId02.addValueChangedListener(new ValueChangedListener("DEPART_ID") {

			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				showDepartList();
			}
		});
		// 大额补录交易流水数值改变事件
		this.editRefNo01.addValueChangedListener(new ValueChangedListener("REF_NO") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				dsRefNo.setCurrentValue("REF_NO", dsResult.getCurrentValue("REF_NO"));
				dsRefNo.setCurrentValue("WORK_DATE", dsResult.getCurrentValueAsDate("WORK_DATE"));
				queryRefNoCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectRefNo01");
				queryRefNoCmd01.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (callback.getCallbackCount() == 0) {
							// 新增的时候需要在dsResult插入一条空记录,防止查询的时候出现条件传不到后台
							dsResult.addDataRecord(new DataRecord(), new DataAction() {
								@Override
								public void doAction(DataRecord record) {
									dsResult.setCurrentValue("WORK_DATE", dsQuery.getCurrentValueAsDate("WORK_DATE"));
									dsResult.setCurrentValue("DEPART_ID", ContextHelper.getContext().getValue("departId"));
									dsResult.sync();
									showDepartList();
								}
							});
							UPrompt.alert(type, "无法完成操作！原因：该记录不存在于基础业务记录中，请先在基础业务记录中添加该记录！");
						}
						if (!status) {
							UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
						}
						//modify by wzf 20170510 增加是否跨境标志 begin
            if ("1".equals(dsResult.getCurrentValue(
                            "IS_CROSSBORDER"))) {
                dsResult.setCurrentValue("CROSSBORDERflag",
                    true);
            } else {
                dsResult.setCurrentValue("CROSSBORDERflag",
                    false);
            }

            //modify by wzf 20170510 增加是否跨境标志 end
            
           
            //add by wjx 20170713 增加默认地区310115 begin
            
            if (dsResult.getCurrentValueAsString("IS_CROSSBORDER") != "310115")
            {
            	dsResult.setCurrentValue("TRADE_VENUE_COUNTRY", "CHN");
            	dsResult.setCurrentValue("TRADE_VENUE_REGION", "310115");
            	
            }
            //add by wjx 20170713 增加默认地区310115 end 
            
            //add by wjx 20170606 增加自动判断新增、补录  begin
    		String TradeDate = ""; 
		    TradeDate = dsResult.getCurrentValueAsDateString("TRADE_DATE", "yyyyMMdd");
		    if(dsResult.getCurrentValue("TRADE_DATE") != null)
		    {
		    	 System.out.println("-------------------"+dsQuery.getCurrentValueAsDateString("WORK_DATE", "yyyyMMdd")+"-------------------"+TradeDate);
					checkDateCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdate");
					Map<String, String> condition = new HashMap<String, String>();
					condition.put("WORK_DATE", dsQuery.getCurrentValueAsDateString("WORK_DATE", "yyyyMMdd"));
					condition.put("TRADE_DATE", TradeDate);
					checkDateCmd.setExtraInfo(condition);
					checkDateCmd.execute(new CommandCallback() {
				
					public void onCallback(boolean status, Callback callback, String commandItemName) {
					        if (status) {
					        	 
					        	if ("0".equals(callback.getSuccessMessage()))
					        	{
					    	        operationType="0";//新增
					        	}
					        	else
					        	{
					        		operationType="1";//补报
					        		UPrompt.alert(type, "当前记录为补报交易！");
								    System.out.println("operationType-------------------1");
					        	}
								
					    	    System.out.println("getSuccessMessage-------------------"+callback.getSuccessMessage());
							} else {
								UPrompt.alert(type, "自动判断新增补报失败，错误信息：" + callback.getErrorMessage());
							}
					    }
					});
		            
		    }
		    //add by wjx 20170606 增加自动判断新增、补录  end
			       }
				});

			}
		});
		// 可疑补录的交易流水数值改变事件
		this.editRefNo02.addValueChangedListener(new ValueChangedListener("REF_NO") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				dsRefNo.setCurrentValue("REF_NO", dsResult1.getCurrentValue("REF_NO"));
				dsRefNo.setCurrentValue("WORK_DATE", dsResult1.getCurrentValueAsDate("WORK_DATE"));
				queryRefNoCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectRefNo02");
				queryRefNoCmd02.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (callback.getCallbackCount() == 0) {
							// 新增的时候需要在dsResult插入一条空记录,防止查询的时候出现条件传不到后台
							dsResult1.addDataRecord(new DataRecord(), new DataAction() {
								@Override
								public void doAction(DataRecord record) {
									dsResult1.setCurrentValue("WORK_DATE", dsQuery.getCurrentValueAsDate("WORK_DATE"));
									dsResult1.setCurrentValue("DEPART_ID", ContextHelper.getContext().getValue("departId"));
									dsResult1.sync();
									showDepartList();
								}
							});
							UPrompt.alert(type, "无法完成操作！原因：该记录不存在于基础业务记录中，请先在基础业务记录中添加该记录！");
						}
						if (!status) {
							UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
						}
						//modify by wzf 20170510 增加是否跨境标志 begin
            if ("1".equals(dsResult1.getCurrentValue(
                            "IS_CROSSBORDER"))) {
                dsResult1.setCurrentValue("CROSSBORDERflag",
                    true);
            } else {
                dsResult1.setCurrentValue("CROSSBORDERflag",
                    false);
            }

            //modify by wzf 20170510 增加是否跨境标志 end
            
            //add by wjx 20170713 增加默认地区310115 begin
            
            if (dsResult1.getCurrentValueAsString("IS_CROSSBORDER") != "310115")
            {
            	dsResult1.setCurrentValue("TRADE_VENUE_COUNTRY", "CHN");
            	dsResult1.setCurrentValue("TRADE_VENUE_REGION", "310115");
            	
            }
            //add by wjx 20170713 增加默认地区310115 end 
            
            //add by wjx 20170606 增加自动判断新增、补录  begin
             String TradeDate = ""; 
      		 TradeDate = dsResult1.getCurrentValueAsDateString("TRADE_DATE", "yyyyMMdd");
      		 if(dsResult1.getCurrentValue("TRADE_DATE") != null)
  		    {
      			checkDateCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdate");
      			Map<String, String> condition = new HashMap<String, String>();
      			condition.put("WORK_DATE", dsQuery.getCurrentValueAsDateString("WORK_DATE", "yyyyMMdd"));
      			condition.put("TRADE_DATE", TradeDate);
      			checkDateCmd.setExtraInfo(condition);
      			checkDateCmd.execute(new CommandCallback() {
      		
      			public void onCallback(boolean status, Callback callback, String commandItemName) {
                    if (status) {
			        	
                    	if ("0".equals(callback.getSuccessMessage()))
			        	{
			    	        operationType="0";//新增
			        	}
//			        	else
//			        	{
//			        		operationType="1";//补报
//			        		UPrompt.alert(type, "当前记录为补报交易！");
//						    System.out.println("operationType-------------------1");
//			        	}
						
					} else {
						UPrompt.alert(type, "自动判断新增补报失败，错误信息：" + callback.getErrorMessage());
					}
      			    }
      			});
  		    }
                  //add by wjx 20170606 增加自动判断新增、补录  end
					}
				});
			}
		});
		// 大额补录的交易去向国家数值改变事件
//		this.editTradeCountry01.addValueChangedListener(new ValueChangedListener("TRADE_COUNTRY") {
//			@Override
//			public void onValueChange(String fieldName, ValueChangeEvent event) {
//				if ("CHN".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "Z01".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "Z02".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))
//						|| "Z03".equals(dsResult.getCurrentValue("TRADE_COUNTRY")) || "@N".equals(dsResult.getCurrentValue("TRADE_COUNTRY"))) {
//					formAction2.setReadOnly("TRADE_REGION", false);
//					dsResult.setCurrentValue("TRADE_REGION", "");
//					dsResult.sync();
//				} else {
//					dsResult.setCurrentValue("TRADE_REGION", "000000");
//					dsResult.sync();
//					formAction2.setReadOnly("TRADE_REGION", true);
//				}
//
//			}
//
//		});
		// 疑似涉罪类型改变事件
        this.editBsTosc02.addValueChangedListener(new ValueChangedListener("BS_TOSC") {
            @Override
            public void onValueChange(String fieldName, ValueChangeEvent event) {
                String temp = dsResult1.getCurrentValue("BS_TOSC");
                dsResult1.setCurrentValue("BS_TOSCInfo", "");
                dsResult1.sync();
                String[] rules = temp.split(",");
//                String bsToscInfo="";
                for(int i=0;i<rules.length;i++){
                    dsMetaQuery.setCurrentValue("META_VALUE", rules[i]);
//                    dsMetaQuery.sync();
                    System.out.print( dsMetaQuery.getCurrentValue("META_VALUE"));
                    qryBsToscCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectBsTosc");
                    qryBsToscCmd.execute(new CommandCallback() {
                        @Override
                        public void onCallback(boolean status, Callback callback, String commandItemName) {
                            System.out.print(callback.getCallbackData());
//                            dsBsTosc.sync();
                            String bsToscInfo;
                        	bsToscInfo = dsBsTosc.getCurrentValue("META_VALUE") + "-" + dsBsTosc.getCurrentValue("META_NAME");
                        	bsToscInfo = bsToscInfo + "\n" +dsResult1.getCurrentValue("BS_TOSCInfo");
                        	dsResult1.setCurrentValue("BS_TOSCInfo", bsToscInfo);
                        	dsResult1.sync();
                        }
                    });
//                    bsToscInfo += dsBsTosc.getCurrentValue("META_VALUE") + "-" + dsBsTosc.getCurrentValue("META_NAME") + "\n";
                }
//                dsResult1.setCurrentValue("BS_TOSCInfo", bsToscInfo);
//                dsResult1.sync();
            }
        });
		// 大额补录的交易发生国家数值改变事件
		this.editTradeVenueCountry01.addValueChangedListener(new ValueChangedListener("TRADE_VENUE_COUNTRY") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("CHN".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z01".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))
						|| "Z02".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z03".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))
						|| "@N".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))) {
					formAction2.setReadOnly("TRADE_VENUE_REGION", false);
					dsResult.setCurrentValue("TRADE_VENUE_REGION", "");
					dsResult.sync();
				} else {
					dsResult.setCurrentValue("TRADE_VENUE_REGION", "000000");
					dsResult.sync();
					formAction2.setReadOnly("TRADE_VENUE_REGION", true);
				}
			}
		});

		// 可疑补录交易发生国家数值改变事件
		this.editTradeVenueCountry02.addValueChangedListener(new ValueChangedListener("TRADE_VENUE_COUNTRY") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {

				if ("CHN".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z01".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))
						|| "Z02".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY")) || "Z03".equals(dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"))
						|| "@N".equals(dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"))) {
					formAction2.setReadOnly("TRADE_VENUE_REGION", false);
					dsResult1.setCurrentValue("TRADE_VENUE_REGION", "");
					dsResult1.sync();
				} else {
					dsResult1.setCurrentValue("TRADE_VENUE_REGION", "000000");
					dsResult1.sync();
					formAction12.setReadOnly("TRADE_VENUE_REGION", true);
				}
			}

		});
		// 可以补录对手金融机构国家代码数值改变事件
		this.editOrganizationCountry02.addValueChangedListener(new ValueChangedListener("CTPY_FI_COUNTRY") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("CHN".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY")) || "Z01".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY"))
						|| "Z02".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY")) || "Z03".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY"))
						|| "@N".equals(dsResult1.getCurrentValue("CTPY_FI_COUNTRY"))) {
					dsResult1.setCurrentValue("CTPY_FI_REGION_CODE", "");
					dsResult1.sync();
					formAction17.setReadOnly("CTPY_FI_REGION_CODE", false);
				} else {
					dsResult1.setCurrentValue("CTPY_FI_REGION_CODE", "000000");
					dsResult1.sync();
					formAction17.setReadOnly("CTPY_FI_REGION_CODE", true);
				}
			}
		});
		
		// 大额美元金额生成和交易金额折合成人民币金额
		this.editAmt01.addValueChangedListener(new ValueChangedListener("AMT") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				// TODO Auto-generated method stub
				String amt = dsResult.getCurrentValue("AMT");
				if (amt != null && (!"".equals(amt))) {
					qryParities01Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectParites");
					qryParities01Cmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {
							callback.getCallbackData();
							dsResult.setCurrentValue("DOLLAR_AMT", callback.getCallbackData());
							dsResult.sync();
						}
					});
					// 交易金额折合成人民币金额
					qryParities01Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectParitesRMB");
					qryParities01Cmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {
							callback.getCallbackData();
							dsResult.setCurrentValue("RMB_AMT", callback.getCallbackData());
							dsResult.sync();
						}
					});
				}
			}
		});
		// 可疑美元金额生成和交易金额折合成人民币金额
		this.editAmt02.addValueChangedListener(new ValueChangedListener("AMT") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				// TODO Auto-generated method stub
				String amt = dsResult1.getCurrentValue("AMT");
				if (amt != null && (!"".equals(amt))) {
					qryParities02Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectParites");
					qryParities02Cmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {
							callback.getCallbackData();
							dsResult1.setCurrentValue("DOLLAR_AMT", callback.getCallbackData());
							dsResult1.sync();
						}
					});
					// 交易金额折合成人民币金额
					qryParities02Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectParitesRMB");
					qryParities02Cmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {
							callback.getCallbackData();
							dsResult1.setCurrentValue("RMB_AMT", callback.getCallbackData());
							dsResult1.sync();
						}
					});
				}
			}
		});

		// 大额证件类型改变事件
		this.editIcType01.addValueChangedListener(new ValueChangedListener("CLIENT_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CLIENT_IC_TYPE"))) {
					editIcTypeMemo01.setRequired(true);
					editIcTypeMemo01.redraw();
				} else {
					editIcTypeMemo01.setRequired(false);
					editIcTypeMemo01.redraw();
				}
			}
		});

		// 可疑证件类型改变事件
		this.editIcType02.addValueChangedListener(new ValueChangedListener("CLIENT_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CLIENT_IC_TYPE"))) {
					editIcTypeMemo02.setRequired(true);
					editIcTypeMemo02.redraw();
				} else {
					editIcTypeMemo02.setRequired(false);
					editIcTypeMemo02.redraw();
				}
			}
		});
		
		//大额客户号改变事件
		this.editClientId01.addValueChangedListener(new ValueChangedListener("CLIENT_ID") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				formAction5.reset();
				dsResult.sync();
			}
		});
		
		//可疑客户号改变事件
		this.editClientId02.addValueChangedListener(new ValueChangedListener("CLIENT_ID") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				formAction15.reset();
				dsResult1.sync();
			}
		});
		
		//大额账号改变事件
		this.editActId01.addValueChangedListener(new ValueChangedListener("ACCT_ID") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				selectAcctOne();	
				selectName = "ActId01";
			}
		});
		
		//可疑账号改变事件
		this.editActId02.addValueChangedListener(new ValueChangedListener("ACCT_ID") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				selectAcctOne();
				selectName = "ActId02";
			}

		});


		// 大额对手证件类型改变事件
		this.editCtpyIcType01.addValueChangedListener(new ValueChangedListener("CTPY_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult.getCurrentValue("CTPY_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("CTPY_IC_TYPE"))) {
					editCtpyIcTypeMemo01.setRequired(true);
					editCtpyIcTypeMemo01.redraw();
				} else {
					editCtpyIcTypeMemo01.setRequired(false);
					editCtpyIcTypeMemo01.redraw();
				}
			}
		});

		// 可疑对手证件类型改变事件
		this.editCtpyIcType02.addValueChangedListener(new ValueChangedListener("CTPY_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult1.getCurrentValue("CTPY_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("CTPY_IC_TYPE"))) {
					editCtpyIcTypeMemo02.setRequired(true);
					editCtpyIcTypeMemo02.redraw();
				} else {
					editCtpyIcTypeMemo02.setRequired(false);
					editCtpyIcTypeMemo02.redraw();
				}
			}
		});

		// 大额代办人证件类型改变事件
		this.editAgentIcType01.addValueChangedListener(new ValueChangedListener("AGENT_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult.getCurrentValue("AGENT_IC_TYPE")) || "29".equals(dsResult.getCurrentValue("AGENT_IC_TYPE"))) {
					editAgentIcTypeMemo01.setRequired(true);
					editAgentIcTypeMemo01.redraw();
				} else {
					editAgentIcTypeMemo01.setRequired(false);
					editAgentIcTypeMemo01.redraw();
				}
			}
		});

		// 可疑代办人证件类型改变事件
		this.editAgentIcType02.addValueChangedListener(new ValueChangedListener("AGENT_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult1.getCurrentValue("AGENT_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("AGENT_IC_TYPE"))) {
					editAgentIcTypeMemo02.setRequired(true);
					editAgentIcTypeMemo02.redraw();
				} else {
					editAgentIcTypeMemo02.setRequired(false);
					editAgentIcTypeMemo02.redraw();
				}
			}
		});

		// 可疑法人证件类型改变事件
		this.editRepIcType02.addValueChangedListener(new ValueChangedListener("REP_IC_TYPE") {
			@Override
			public void onValueChange(String fieldName, ValueChangeEvent event) {
				if ("19".equals(dsResult1.getCurrentValue("REP_IC_TYPE")) || "29".equals(dsResult1.getCurrentValue("REP_IC_TYPE"))) {
					editRepIcTypeMemo02.setRequired(true);
					editRepIcTypeMemo02.redraw();
				} else {
					editRepIcTypeMemo02.setRequired(false);
					editRepIcTypeMemo02.redraw();
				}
			}
		});

		this.btnQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!listQuery.validate()) {
					UPrompt.alert(type, "请输入查询日期！");
					return;
				} 
				showList();
			}
		});
		
		this.btnadd.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				if (dsQuery.getCurrentValue("WORK_DATE") == null || "".equals(dsQuery.getCurrentValue("WORK_DATE"))) {
					UPrompt.alert(type, "请输入查询日期！");
					return;
				}
				operationType="0";
				addNew("新增");
			}
		});
		
		/**
		 * 【添加email附件】按钮触发
		 */
		this.btnAcc.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				//点【添加email附件】按钮时判断是不是可疑
				if (tableset.getSelectedTabNumber() == 0) {
					//if (dsResult.getRecordCount() == 0) {
						UPrompt.alert(type, "AML大额补录信息不能添加email附件！");
						return;
					//}
				} else {
					if (dsResult1.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML可疑补录信息，不能添加email附件！");
						return;
					}else{
						addEmail();
						showListAcc();
					}
				}
				
//				addEmail();
//				showListAcc();
			}
		});
		
		//上传
		this.btnUpload.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(ClickEvent event) {
				String uploadSourceFile = uploadEditor.getFileFullPath();
				if(uploadSourceFile == null || "".equals(uploadSourceFile)){
					   UPrompt.alert(typeinner, "请选择上传文件！");
					   return;
				}
				uploadEditor.submit();
				showListAcc();
			}
		});
		
		//下载
		this.btnDownload.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				DataRecord[] selectedRecords = dsResultTBAcc.getSelectedRecords();
				if (dsResultTBAcc.getRecordCount() == 0) {
					UPrompt.alert(type, "无附件可下载！");
					return;
				}else if (selectedRecords == null || selectedRecords.length < 1) {
					UPrompt.alert(type, "请选择要下载的记录！");
					return;
				}else if (selectedRecords.length > 1) {
					UPrompt.alert(type, "请选择单条记录进行下载！");
					return;
				}
				
				dsResultTBAcc.setCurrentRecord(selectedRecords[0]);
				
				UDownloaderClient client = new UDownloaderClient();
				Map<String, String> params = new HashMap<String, String>();
				
				String accPath, accName;
				StringBuilder accFile = new StringBuilder();
				
				//单条下载记录方式代码
				accPath = dsResultTBAcc.getCurrentValue("ACC_PATH");
				accName = dsResultTBAcc.getCurrentValue("ACC_NAME");

				accFile.append(accPath);

				params.put("file", accFile.toString());
				client.download(BizFunClient.getWebRoot(), "file", params);
				
				//下载成功，将数据库中的下载状态改为是
				queryDatasetAcc.setCurrentValue("ACC_ID", dsResultTBAcc.getCurrentValue("ACC_ID"));
				updateCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "updateAcc");
				updateCmdAcc.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {
						if (!status) {
							UPrompt.alert(typeinner, "下载附件错误：" + callback.getErrorMessage());
						} else {
							UPrompt.alert(typeinner, "下载附件成功！ ");
							showListAcc();
					    }
				     }
			    });
			}
		});
		
		//刷新
		this.btnRefresh.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(ClickEvent event) {
//				StringBuffer url = new StringBuffer();
//				url.append("/report/first.jsp");
//				Window.open(url.toString(), "_blank", "");
				showListAcc();
			}
		});
		
		//查看
		this.btnView.addClickListener(new ClickListener() {
					
			@Override
			public void onClick(ClickEvent event) {
				DataRecord[] selectedRecords = dsResultTBAcc.getSelectedRecords();
				System.out.println("------------selectedRecords.length-------"+selectedRecords.length);
				if (dsResultTBAcc.getRecordCount() == 0) {
					UPrompt.alert(type, "无附件可查看！");
					return;
				}else if (selectedRecords == null || selectedRecords.length < 1) {
					UPrompt.alert(type, "请选择要查看的记录！");
					return;
				}else if (selectedRecords.length > 1) {
					UPrompt.alert(type, "请选择单条记录进行查看！");
					return;
				}
				
				dsResultTBAcc.setCurrentRecord(selectedRecords[0]);
				
				//String accPath = Md5Encoder.encode(dsResultTBAcc.getCurrentValue("ACC_PATH"));
				String accPath = dsResultTBAcc.getCurrentValue("ACC_PATH");
				StringBuffer url = new StringBuffer();
				// url.append(GWT.getModuleBaseURL());
				//tomcat run
				url.append("/rpt/report/emailShow.jsp?accPath=");
				//exlipse run
				//url.append("/report/emailShow.jsp?accPath=");
				url.append(accPath);
				System.out.println("---------url------"+url.toString());
				Window.open(url.toString(), "_blank", "");
			}
		});

		/**
		 * 补报按钮触发
		 */
//		this.btnRepay.addClickListener(new ClickListener() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if (dsQuery.getCurrentValue("WORK_DATE") == null || "".equals(dsQuery.getCurrentValue("WORK_DATE"))) {
//					UPrompt.alert(type, "请输入查询日期！");
//					return;
//				} 
//				
//				addNew("手工新增");
//				
//			}
//		});
				
		this.btnMod.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (tableset.getSelectedTabNumber() == 0) {
					if (dsResult.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML大额补录信息，不能修改！");
						return;
					}
					//add by wzf 20150406 begin
					//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改				
					checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
					checkdeleteCmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {							
							if (!status) {
								UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
									@Override
									public void execute(Boolean value) {
										if (value != null && value) {
											showMod();
										}
									}
								});

							}else{
								showMod();
							}
						}
					});
					//add by wzf 20150406 end
				} else {
					if (dsResult1.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML可疑补录信息，不能修改！");
						return;
					}
					//add by wzf 20150406 begin
					//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改				
					checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
					checkdeleteCmd1.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {							
							if (!status) {
								UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
									@Override
									public void execute(Boolean value) {
										if (value != null && value) {
											showMod();
										}
									}
								});

							}else{
								showMod();
							}
						}
					});
					//add by wzf 20150406 end
				}
				
			}
		});
		this.btnDel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (tableset.getSelectedTabNumber() == 0) {
					if (dsResult.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML大额补录信息，不能删除！");
						return;
					}
				} else {
					if (dsResult1.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML可疑补录信息，不能删除！");
						return;
					}
				}
				if ("0".equals(delNo)) {
					deleteWindow.setTitle("AML第二次补录删除");
					if (tableset.getSelectedTabNumber() == 0) {
						//add by pw 20150918 begin
						//60(对于已上报状态且未导入回执的AML数据，系统不应允许修改 )
						if ("11".equals(dsResult.getCurrentValue("RPT_STATUS"))) {
							//UPrompt.alert(type, "不能修改，导入回执后才允许修改。");
							UPrompt.confirm(type, "状态为已上报，需要导入回执后才能进行修改操作。"+"是否查看明细", new PromptCallback(){
								@Override
								public void execute(Boolean value) {
									// TODO Auto-generated method stub
									if (value != null && value) {
										// 证件类型初始化设置
										selectAcctOne();
										detailWindow.show();
										//modify by wzf 20170510 增加是否跨境标志 begin
                    if ("1".equals(
                                dsResult.getCurrentValue(
                                    "IS_CROSSBORDER"))) {
                        dsResult.setCurrentValue("CROSSBORDERflag",
                            true);
                        editCROSSBORDERflag01.setValue(true);
                    } else {
                        dsResult.setCurrentValue("CROSSBORDERflag",
                            false);
                        editCROSSBORDERflag01.setValue(false);
                    }

                    //modify by wzf 20170510 增加是否跨境标志 end
										btnSave.hide();
										btncancel.setTitle("关闭");
									} else
										return;
								}
								
							} );
							return;
							
						}
						//end
						//add by wzf 20150406 begin
						//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改				
						checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
						checkdeleteCmd.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {							
								if (!status) {
									UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
										@Override
										public void execute(Boolean value) {
											if (value == null || !value) {
												return;
											}else{
												checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelete");
												checkdeleteCmd.execute(new CommandCallback() {
													@Override
													public void onCallback(boolean status, Callback callback, String commandItemName) {

														if (!status) {
															UPrompt.alert(type, callback.getErrorMessage());
															dsResult.setCommand(queryCmd);

														} else {
															deleteDelInfo.setLength(256);
															deleteWindow.show();
															//modify by wzf 20170510 增加是否跨境标志 begin
                              if ("1".equals(
                                          dsResult.getCurrentValue(
                                              "IS_CROSSBORDER"))) {
                                  dsResult.setCurrentValue("CROSSBORDERflag",
                                      true);
                                  editCROSSBORDERflag01.setValue(true);
                              } else {
                                  dsResult.setCurrentValue("CROSSBORDERflag",
                                      false);
                                  editCROSSBORDERflag01.setValue(false);
                              }

                              //modify by wzf 20170510 增加是否跨境标志 end
														}
													}
												});
											}
										}
									});

								}else{
									checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelete");
									checkdeleteCmd.execute(new CommandCallback() {
										@Override
										public void onCallback(boolean status, Callback callback, String commandItemName) {											
											if (!status) {
												//pw 20150921 begin
													UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){
														@Override
														public void execute(Boolean value) {
															// TODO Auto-generated method stub
															if (value != null && value) {
																// 证件类型初始化设置
																selectAcctOne();
																detailWindow.show();
																//modify by wzf 20170510 增加是否跨境标志 begin
                                if ("1".equals(
                                            dsResult.getCurrentValue(
                                                "IS_CROSSBORDER"))) {
                                    dsResult.setCurrentValue("CROSSBORDERflag",
                                        true);
                                    editCROSSBORDERflag01.setValue(true);
                                } else {
                                    dsResult.setCurrentValue("CROSSBORDERflag",
                                        false);
                                    editCROSSBORDERflag01.setValue(false);
                                }

                                //modify by wzf 20170510 增加是否跨境标志 end
																btnSave.hide();
																btncancel.setTitle("关闭");
															} else
																return;
														}
														
													} );
												//UPrompt.alert(type, callback.getErrorMessage());
												//end
												dsResult.setCommand(queryCmd);

											} else {
												deleteDelInfo.setLength(256);
												deleteWindow.show();
											}
										}
									});
								}
							}
						});
						//add by wzf 20150406 end
						
					} else {
						//add by pw 20150918 begin
						//60(对于已上报状态且未导入回执的AML数据，系统不应允许修改 )
						if ("11".equals(dsResult1.getCurrentValue("RPT_STATUS"))) {
							//UPrompt.alert(type, "不能修改，导入回执后才允许修改。");
							UPrompt.confirm(type, "状态为已上报，需要导入回执后才能进行修改操作。"+"是否查看明细", new PromptCallback(){
								@Override
								public void execute(Boolean value) {
									// TODO Auto-generated method stub
									if (value != null && value) {
										// 证件类型初始化设置
										selectAcctOne();
										detailWindow1.show();
										//modify by wzf 20170510 增加是否跨境标志 begin
                    if ("1".equals(
                                dsResult1.getCurrentValue(
                                    "IS_CROSSBORDER"))) {
                        dsResult1.setCurrentValue("CROSSBORDERflag",
                            true);
                        editCROSSBORDERflag02.setValue(true);
                    } else {
                        dsResult1.setCurrentValue("CROSSBORDERflag",
                            false);
                        editCROSSBORDERflag02.setValue(false);
                    }

                    //modify by wzf 20170510 增加是否跨境标志 end
										btnSave1.hide();
										btncancel1.setTitle("关闭");
									} else
										return;
								}
								
							} );
							return;
							
						}
						//end
						//add by wzf 20150406 begin
						//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改				
						checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
						checkdeleteCmd1.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {							
								if (!status) {
									UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
										@Override
										public void execute(Boolean value) {
											if (value == null || !value) {
												return;
											}else{
												checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelete");
												checkdeleteCmd1.execute(new CommandCallback() {
													@Override
													public void onCallback(boolean status, Callback callback, String commandItemName) {

														if (!status) {
															//pw 20150921 begin
															UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){
																@Override
																public void execute(Boolean value) {
																	// TODO Auto-generated method stub
																	if (value != null && value) {
																		// 证件类型初始化设置
																		selectAcctOne();
																		detailWindow1.show();
																		//modify by wzf 20170510 增加是否跨境标志 begin
                                    if ("1".equals(
                                                dsResult1.getCurrentValue(
                                                    "IS_CROSSBORDER"))) {
                                        dsResult1.setCurrentValue("CROSSBORDERflag",
                                            true);
                                        editCROSSBORDERflag02.setValue(true);
                                    } else {
                                        dsResult1.setCurrentValue("CROSSBORDERflag",
                                            false);
                                        editCROSSBORDERflag02.setValue(false);
                                    }

                                    //modify by wzf 20170510 增加是否跨境标志 end
																		btnSave1.hide();
																		btncancel1.setTitle("关闭");
																	} else
																		return;
																}
																
															} );
															//UPrompt.alert(type, callback.getErrorMessage());
															//end
															dsResult1.setCommand(queryCmd);

														} else {
															deleteDelInfo1.setLength(256);
															deleteWindow1.show();
														}
													}
												});
											}
										}
									});

								}else{
									checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelete");
									checkdeleteCmd1.execute(new CommandCallback() {
										@Override
										public void onCallback(boolean status, Callback callback, String commandItemName) {

											if (!status) {
												UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){
													@Override
													public void execute(Boolean value) {
														// TODO Auto-generated method stub
														if (value != null && value) {
															// 证件类型初始化设置
															selectAcctOne();
															detailWindow1.show();
															//modify by wzf 20170510 增加是否跨境标志 begin
                              if ("1".equals(
                                          dsResult1.getCurrentValue(
                                              "IS_CROSSBORDER"))) {
                                  dsResult1.setCurrentValue("CROSSBORDERflag",
                                      true);
                                  editCROSSBORDERflag02.setValue(true);
                              } else {
                                  dsResult1.setCurrentValue("CROSSBORDERflag",
                                      false);
                                  editCROSSBORDERflag02.setValue(false);
                              }

                              //modify by wzf 20170510 增加是否跨境标志 end
															btnSave1.hide();
															btncancel1.setTitle("关闭");
														} else
															return;
													}
													
												} );
												//UPrompt.alert(type, callback.getErrorMessage());
												dsResult1.setCommand(queryCmd);

											} else {
												deleteDelInfo1.setLength(256);
												deleteWindow1.show();
											}
										}
									});
								}
							}
						});
						//add by wzf 20150406 end
					}
				} else if ("1".equals(delNo)) {
					deleteWindow.setTitle("AML第二次补录取消删除");
					if (tableset.getSelectedTabNumber() == 0) {
						checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelete");
						checkdeleteCmd.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {							
								if (!status) {
									UPrompt.alert(type, callback.getErrorMessage());
									dsResult.setCommand(queryCmd);

								} else {
									deleteDelInfo.setLength(256);
									deleteWindow.show();
								}
							}
						});
					} else {
						checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelete");
						checkdeleteCmd1.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {

								if (!status) {
									UPrompt.alert(type, callback.getErrorMessage());
									dsResult1.setCommand(queryCmd);

								} else {
									deleteDelInfo1.setLength(256);
									deleteWindow1.show();
								}
							}
						});

					}
				}

			}
		});
		// 大额删除确认操作
		this.btnConfirm.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				UPrompt.confirm(type, "您确定删除/取消删除该条记录？", new PromptCallback() {
					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							if ("0".equals(delNo)) {
								if ("20".equals(dsResult.getCurrentValue("RPT_STATUS"))) {
									// 工作日期查询
									queryWorkDateCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "getPreWorkDate");
									queryWorkDateCmd.execute(new CommandCallback() {
										@Override
										public void onCallback(boolean status, Callback callback, String commandItemName) {
											// 进入界面开始查询
											String workDate = dsWorkDate.getCurrentValue("work_date");
											dsResult.setCurrentValue("WORK_DATE", DateUtils.parseDate(workDate));
											saveCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "addAMLSecond03");
											saveCmd.execute(new CommandCallback() {
												@Override
												public void onCallback(boolean status, Callback callback, String commandItemName) {

													if (!status) {
														UPrompt.alert(type, callback.getErrorMessage());
													} else {
														UPrompt.alert(type, callback.getSuccessMessage());
														deleteWindow.hide();
														getunlock();
														showList();
													}
												}
											});
										}
									});
									
									return;
								}
								deleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "deleteAMLList");
							} else {
								deleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "undeleteAMLList");
							}
							deleteCmd.execute(new CommandCallback() {
								@Override
								public void onCallback(boolean status, Callback callback, String commandItemName) {
									if (!status) {
										if ("0".equals(delNo)) {
											UPrompt.alert(type, "删除AML第二次补录信息失败，错误信息：" + callback.getErrorMessage());
										} else {
											UPrompt.alert(type, "取消删除AML第二次补录信息失败，错误信息：" + callback.getErrorMessage());
										}
										dsResult.setCommand(queryCmd);
									} else {
										if ("0".equals(delNo)) {
											UPrompt.alert(type, "删除AML第二次补录信息成功！");
										} else {
											UPrompt.alert(type, "取消删除AML第二次补录信息成功！");
										}
										deleteWindow.hide();
										getunlock();
										showList();
										if ("0".equals(delNo)) {
											dsResult.logSys("del");
										} else {
											dsResult.logSys("mod");
										}
									}
								}
							});
						}
					}

				});
			}
		});

		// 可疑删除确认操作
		this.btnConfirm1.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				UPrompt.confirm(type, "您确定删除/取消删除该条记录？", new PromptCallback() {
					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							if ("0".equals(delNo)) {
								deleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "deleteAMLList");
							} else {
								deleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "undeleteAMLList");
							}
							dsResult1.setCommand(queryCmd);
							deleteCmd1.execute(new CommandCallback() {
								@Override
								public void onCallback(boolean status, Callback callback, String commandItemName) {
									if (!status) {
										if ("0".equals(delNo)) {
											UPrompt.alert(type, "删除AML第二次补录信息失败，错误信息：" + callback.getErrorMessage());
										} else {
											UPrompt.alert(type, "取消删除AML第二次补录信息失败，错误信息：" + callback.getErrorMessage());
										}
									} else {
										if ("0".equals(delNo)) {
											UPrompt.alert(type, "删除AML第二次补录信息成功！");
										} else {
											UPrompt.alert(type, "取消删除AML第二次补录信息成功！");
										}
										deleteWindow1.hide();
										getunlock();
										showList();
										if ("0".equals(delNo)) {
											dsResult1.logSys("del");
										} else {
											dsResult1.logSys("mod");
										}
									}
								}
							});
						}
					}

				});

			}
		});
		// 大额删除取消操作
		this.btncancel2.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				deleteWindow.hide();
				getunlock();
				showList();
			}
		});

		// 可疑删除取消操作
		this.btncancel21.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				deleteWindow1.hide();
				getunlock();
				showList();
			}
		});
		
		//延迟上报按钮的点击事件
		this.btnDelay.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (tableset.getSelectedTabNumber() == 0) {
					//if (dsResult.getRecordCount() == 0) {
						UPrompt.alert(type, "AML大额补录信息不能延迟上报！");
						return;
					//}
				} else {
					if (dsResult1.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML可疑补录信息，不能延迟上报！");
						return;
					}
				}
					deleteWindow.setTitle("AML第二次补录延迟上报");//这个window要改为delay？不改也能用
					if (tableset.getSelectedTabNumber() == 0) {
						UPrompt.alert(type, "AML大额补录信息不能延迟上报！");
						return;
					} else {
						//add by pw 20150918 begin
						//60(对于已上报状态且未导入回执的AML数据，系统不应允许修改 )
						if ("11".equals(dsResult1.getCurrentValue("RPT_STATUS"))) {
							//UPrompt.alert(type, "不能修改，导入回执后才允许修改。");
							UPrompt.confirm(type, "状态为已上报，需要导入回执后才能进行修改操作。"+"是否查看明细", new PromptCallback(){
								@Override
								public void execute(Boolean value) {
									// TODO Auto-generated method stub
									if (value != null && value) {
										// 证件类型初始化设置
										selectAcctOne();
										detailWindow1.show();
										btnSave1.hide();
										btncancel1.setTitle("关闭");
									} else
										return;
								}
								
							} );
							return;
							
						}
						//end
						//add by wzf 20150406 begin
						//如果对上报成功数据进行纠错或者删除时，则判断是否已超期30天，如果是则提示是否需要修改				
						checkdelayCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
						checkdelayCmd.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {							
								if (!status) {
									UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
										@Override
										public void execute(Boolean value) {
											if (value == null || !value) {
												return;
											}else{
												checkdelayCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelay");
												checkdelayCmd.execute(new CommandCallback() {
													@Override
													public void onCallback(boolean status, Callback callback, String commandItemName) {

														if (!status) {
															//pw 20150921 begin
															UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){
																@Override
																public void execute(Boolean value) {
																	// TODO Auto-generated method stub
																	if (value != null && value) {
																		// 证件类型初始化设置
																		selectAcctOne();
																		detailWindow1.show();
																		btnSave1.hide();
																		btncancel1.setTitle("关闭");
																	} else
																		return;
																}
																
															} );
															//UPrompt.alert(type, callback.getErrorMessage());
															//end
															dsResult1.setCommand(queryCmd);

														} else {
															deleteDelInfo1.setLength(256);
															//deleteWindow1.show();
															
															UPrompt.confirm(type, "您确定延迟上报该批次号下的所有记录？", new PromptCallback() {
																@Override
																public void execute(Boolean value) {
																	if (value != null && value) {
																		delayCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "delayAMLList");
																		dsResult1.setCommand(queryCmd);
																		delayCmd.execute(new CommandCallback() {
																			@Override
																			public void onCallback(boolean status, Callback callback, String commandItemName) {
																				if (!status) {
																					UPrompt.alert(type, "延迟上报AML第二次补录信息失败，错误信息：" + callback.getErrorMessage());
																				} else {
																					UPrompt.alert(type, "延迟上报AML第二次补录信息成功！");
																					//deleteWindow1.hide();
																					getunlock();
																					showList();
																					dsResult1.logSys("del");//这里要改delay？不知道什么意思，不过不改也能用
																				}
																			}
																		});
																	}
																}

															});
														}
													}
												});
											}
										}
									});

								}else{
									checkdelayCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkdelay");
									checkdelayCmd.execute(new CommandCallback() {
										@Override
										public void onCallback(boolean status, Callback callback, String commandItemName) {

											if (!status) {
												UPrompt.confirm(type, callback.getErrorMessage()+"是否查看明细", new PromptCallback(){
													@Override
													public void execute(Boolean value) {
														// TODO Auto-generated method stub
														if (value != null && value) {
															// 证件类型初始化设置
															selectAcctOne();
															detailWindow1.show();
															btnSave1.hide();
															btncancel1.setTitle("关闭");
														} else
															return;
													}
													
												} );
												//UPrompt.alert(type, callback.getErrorMessage());
												dsResult1.setCommand(queryCmd);

											} else {
												deleteDelInfo1.setLength(256);
												//deleteWindow1.show();
												
												UPrompt.confirm(type, "您确定延迟上报该批次号下的所有记录？", new PromptCallback() {
													@Override
													public void execute(Boolean value) {
														if (value != null && value) {
															delayCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "delayAMLList");
															dsResult1.setCommand(queryCmd);
															delayCmd.execute(new CommandCallback() {
																@Override
																public void onCallback(boolean status, Callback callback, String commandItemName) {
																	if (!status) {
																		UPrompt.alert(type, "延迟上报AML第二次补录信息失败，错误信息：" + callback.getErrorMessage());
																	} else {
																		UPrompt.alert(type, "延迟上报AML第二次补录信息成功！");
																		//deleteWindow1.hide();
																		getunlock();
																		showList();
																		dsResult1.logSys("del");//这里要改delay？不知道什么意思，不过不改也能用
																	}
																}
															});
														}
													}

												});
												
											}
										}
									});
								}
							}
						});
						//add by wzf 20150406 end
					}
			}
		});
		
		
		// 完成确认
		this.btconfirm.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		// 取消完成确认
		this.btcancel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});

		// 大额规则代码查询
		this.editRuleCode01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsRuleCodeQuery.setCurrentValue("RuleType", "01");
				dsRuleCodeQuery.setCurrentValue("RULE_CODE", dsResult.getCurrentValue("RULE_CODE"));
				dsRuleCodeQuery.sync();
				showRuleCodeList();
				RuleCodeWindow.show();
				selectName = "RuleCode01";
			}
		});

		// 大额去向国家查询
//		this.editTradeCountry01.addSearchPickerClickListener(new FormItemClickListener() {
//			@Override
//			public void onFormItemClick(FormItemIconClickEvent event) {
//				// TODO Auto-generated method stub
//				dsWhitherQuery.setCurrentValue("queryLenth", 3);
//				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult.getCurrentValue("TRADE_COUNTRY"));
//				dsWhitherQuery.sync();
//				showNationregionList();
//				whitherWindow.show();
//				selectName = "TradeCountry01";
//			}
//		});
		// 大额去向地区查询
//		this.editTradeRegion01.addSearchPickerClickListener(new FormItemClickListener() {
//			@Override
//			public void onFormItemClick(FormItemIconClickEvent event) {
//				// TODO Auto-generated method stub
//				dsWhitherQuery.setCurrentValue("queryLenth", 6);
//				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult.getCurrentValue("TRADE_REGION"));
//				dsWhitherQuery.sync();
//				showNationregionList();
//				whitherWindow.show();
//				selectName = "TradeRegion01";
//			}
//		});
		// 大额交易代码查询
		this.editTradeCode01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				dsMetaQuery.setCurrentValue("META_VALUE", dsResult.getCurrentValue("TRADE_CODE"));
				dsMetaQuery.sync();
				showMetaList();
				MetaWindow.show();
				selectName = "TradeCode01";
			}
		});
		// 大额交易发生国家查询
		this.editTradeVenueCountry01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsWhitherQuery.setCurrentValue("queryLenth", 3);
				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult.getCurrentValue("TRADE_VENUE_COUNTRY"));
				dsWhitherQuery.sync();
				showNationregionList();
				whitherWindow.show();
				selectName = "TradeVenueCountry01";
			}
		});
		// 大额交易发生地区查询
		this.editTradeVenueRegion01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsWhitherQuery.setCurrentValue("queryLenth", 6);
				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult.getCurrentValue("TRADE_VENUE_REGION"));
				dsWhitherQuery.sync();
				showNationregionList();
				whitherWindow.show();
				selectName = "TradeVenueRegion01";
			}
		});
		// 大额对方金融机构代码查询
		this.editOrganizationCode01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsOrganizationQuery.setCurrentValue("CTPY_FI_CODE", dsResult.getCurrentValue("CTPY_FI_CODE"));
				dsOrganizationQuery.sync();
				showOrganizationList();
				OrganizationWindow.show();
				selectName = "OrgenizationCode01";
			}
		});
		// 大额交易对手信息查询
		this.editCtpyAccountId01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsCounterpartyQuery.setCurrentValue("CTPY_ACCT_ID", dsResult.getCurrentValue("CTPY_ACCT_ID"));
				dsCounterpartyQuery.sync();
				showCounterpartyList();
				CounterpartyWindow.show();
				selectName = "CtpyAccountId01";

			}
		});
		// 大额客户信息查询
		this.editClientId01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsClientQuery.setCurrentValue("CLIENT_ID", dsResult.getCurrentValue("CLIENT_ID"));
				dsClientQuery.sync();
				showClientList();
				ClientWindow.show();
				selectName = "ClientId01";
			}
		});
		// 大额账户信息查询
		this.editActId01.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsAcctQuery.setCurrentValue("ACCT_ID", dsResult.getCurrentValue("ACCT_ID"));
				dsAcctQuery.sync();
				showAcctList();
				AcctWindow.show();
				selectName = "ActId01";
			}
		});
		// 大额补录确认
		this.btnSave.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!formAction1.validate() || !formAction2.validate() || !formAction3.validate() || !formAction4.validate() || !formAction5.validate() || !formAction7.validate()
						|| !formAction8.validate() || !formAction9.validate()) {
					UPrompt.alert(type, "请完整输入AML第二次补录大额补录信息！");
					return;
				}
				//add by wzf 20170509 新增是否跨境字段 begin
        if (editCROSSBORDERflag01.getValueAsBoolean()) {
            dsResult.setCurrentValue("IS_CROSSBORDER", "1");
        } else {
            dsResult.setCurrentValue("IS_CROSSBORDER", "0");
        }

        //add by wzf 20170509 新增是否跨境字段 end
				String validate = AMLBizValidator.secondLargeValidator(dsResult.getCurrentRecord().getDataMap());
				if (validate != null) {
					UPrompt.alert(type, validate);
					return;
				}
				if ("0".equals(makeType)) {
					dsResult.setCurrentValue("operationType", operationType);
					saveCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "addAMLSecond01");
					//add by wzf 20150406 begin
					//补报时，则判断是否已超期30天，如果是则提示是否需要修改
					checkdeleteCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
					checkdeleteCmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {							
							if (!status) {
								UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
									@Override
									public void execute(Boolean value) {
										if (value == null || !value) {
											return;
										}else{
											saveCmd.execute(new CommandCallback() {
												@Override
												public void onCallback(boolean status, Callback callback, String commandItemName) {

													if (!status) {
														UPrompt.alert(type, callback.getErrorMessage());
													} else {
														UPrompt.alert(type, callback.getSuccessMessage());
														detailWindow.hide();
														getunlock();
														showList();
													}
												}
											});
										}
									}
								});

							}else{
								saveCmd.execute(new CommandCallback() {
									@Override
									public void onCallback(boolean status, Callback callback, String commandItemName) {

										if (!status) {
											UPrompt.alert(type, callback.getErrorMessage());
										} else {
											UPrompt.alert(type, callback.getSuccessMessage());
											detailWindow.hide();
											getunlock();
											showList();
										}
									}
								});
							}
						}
					});
					//add by wzf 20150406 end
				} else if ("1".equals(makeType)) {
					saveCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "updateAMLSecond01");
					saveCmd.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {

							if (!status) {
								UPrompt.alert(type, callback.getErrorMessage());
							} else {
								UPrompt.alert(type, callback.getSuccessMessage());
								detailWindow.hide();
								getunlock();
								showList();
							}
						}
					});
				}
				/*saveCmd.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, callback.getSuccessMessage());
							detailWindow.hide();
							getunlock();
							showList();
						}
					}
				});
*/
			}
		});
		// 大额补录取消
		this.btncancel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				detailWindow.hide();
				getunlock();
				showList();
			}
		});

		// 可疑交易代码查询
		this.editTradeCode02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				dsMetaQuery.setCurrentValue("META_VALUE", dsResult1.getCurrentValue("TRADE_CODE"));
				dsMetaQuery.sync();
				showMetaList();
				MetaWindow.show();
				selectName = "TradeCode02";
			}
		});
		// 疑似涉罪类型查询
        this.editBsTosc02.addSearchPickerClickListener(new FormItemClickListener() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
            	dsMetaQuery.setCurrentValue("META_VALUE", "");
            	dsMetaQuery.sync();
                showBsToscList();
                BsToscWindow.show();
                selectName = "BsTosc02";
            }
        });
		// 可疑规则代码查询
		this.editRuleCode02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsRuleCodeQuery.setCurrentValue("RuleType", "02");
				dsRuleCodeQuery.setCurrentValue("RULE_CODE", dsResult1.getCurrentValue("RULE_CODE"));
				dsRuleCodeQuery.sync();
				showRuleCodeList();
				RuleCodeWindow.show();
				selectName = "RuleCode02";
			}
		});
		// 可疑交易发起国家
		this.editTradeVenueCountry02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsWhitherQuery.setCurrentValue("queryLenth", 3);
				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY"));
				dsWhitherQuery.sync();
				showNationregionList();
				whitherWindow.show();
				selectName = "TradeVenueCountry02";
			}
		});
		// 可疑交易发生地区
		this.editTradeVenueRegion02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsWhitherQuery.setCurrentValue("queryLenth", 6);
				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult1.getCurrentValue("TRADE_VENUE_REGION"));
				dsWhitherQuery.sync();
				showNationregionList();
				whitherWindow.show();
				selectName = "TradeVenueRegion02";
			}
		});
		// 可疑交易对方金融机构查询
		this.editOrganizationCode02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsOrganizationQuery.setCurrentValue("CTPY_FI_CODE", dsResult1.getCurrentValue("CTPY_FI_CODE"));
				dsOrganizationQuery.sync();
				showOrganizationList();
				OrganizationWindow.show();
				selectName = "OrgenizationCode02";
			}
		});

		// 可疑对方金融机构国家代码
		this.editOrganizationCountry02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsWhitherQuery.setCurrentValue("queryLenth", 3);
				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult1.getCurrentValue("CTPY_FI_COUNTRY"));
				dsWhitherQuery.sync();
				showNationregionList();
				whitherWindow.show();
				selectName = "OrganizationCountry02";
			}
		});
		// 可疑对方金融机构地区代码
		this.editOrganizationRegion02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsWhitherQuery.setCurrentValue("queryLenth", 6);
				dsWhitherQuery.setCurrentValue("NATIONREGION_CODE", dsResult1.getCurrentValue("CTPY_FI_REGION_CODE"));
				dsWhitherQuery.sync();
				showNationregionList();
				whitherWindow.show();
				selectName = "OrganizationRegion02";
			}
		});
		// 可疑交易对手信息查询
		this.editCtpyAccountId02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsCounterpartyQuery.setCurrentValue("CTPY_ACCT_ID", dsResult1.getCurrentValue("CTPY_ACCT_ID"));
				dsCounterpartyQuery.sync();
				showCounterpartyList();
				CounterpartyWindow.show();
				selectName = "CtpyAccountId02";
			}
		});
		// 可疑客户信息查询
		this.editClientId02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsClientQuery.setCurrentValue("CLIENT_ID", dsResult1.getCurrentValue("CLIENT_ID"));
				dsClientQuery.sync();
				showClientList();
				ClientWindow.show();
				selectName = "ClientId02";
			}
		});
		// 可疑账户信息查询
		this.editActId02.addSearchPickerClickListener(new FormItemClickListener() {
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				// TODO Auto-generated method stub
				dsAcctQuery.setCurrentValue("ACCT_ID", dsResult1.getCurrentValue("ACCT_ID"));
				dsAcctQuery.sync();
				showAcctList();
				AcctWindow.show();
				selectName = "ActId02";
			}
		});
		// 查询国家地区信息
		this.btnWhitherQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showNationregionList();
			}
		});
		// 国家地区完成确认
		this.btnWhitherOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				if (dsWhither.getRecordCount() == 0) {
					UPrompt.alert(type, "国家地区信息查询无记录，不能进行确认！");
					return;
				}
				setWhither();
			}
		});
		// 国家地区查询取消
		this.btnWhitherBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				whitherWindow.hide();
			}
		});

		// 查询交易代码信息
		this.btnMetaQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showMetaList();
			}
		});
		// 交易代码完成确认
		this.btnMetaOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsMeta.getRecordCount() == 0) {
					UPrompt.alert(type, "交易代码信息查询无记录，不能进行确认！");
					return;
				}
				setMeta();
			}
		});
		// 交易代码取消
		this.btnMetaBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				MetaWindow.hide();
			}
		});
		// 查询疑似涉罪类型信息
        this.btnBsToscQuery.addClickListener(new ClickListener() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                showBsToscList();
            }
        });
     // 疑似涉罪类型完成确认
        this.btnBsToscOk.addClickListener(new ClickListener() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
            	DataRecord[] selectedRecords = dsBsTosc.getSelectedRecords();
				if (selectedRecords == null || selectedRecords.length < 1) {
					UPrompt.alert(type, "请选择疑似涉罪类型信息！");
					return;
				}
                setBsTosc();
            }
        });
     // 疑似涉罪类型取消
        this.btnBsToscBack.addClickListener(new ClickListener() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                BsToscWindow.hide();
            }
        });
		
		// 查询规则代码信息
		this.btnRuleCodeQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showRuleCodeList();
			}
		});
		// 规则代码完成确认
		this.btnRuleCodeOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsRuleCode.getRecordCount() == 0) {
					UPrompt.alert(type, "规则代码信息查询无记录，不能进行确认！");
					return;
				}
				setRuleCode();
			}
		});
		// 规则代码查询取消
		this.btnRuleCodeBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				RuleCodeWindow.hide();
			}
		});

		// 查询客户信息
		this.btnClientQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showClientList();
			}
		});
		// 客户信息完成确认
		this.btnClientOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsClient.getRecordCount() == 0) {
					UPrompt.alert(type, "客户信息查询无记录，不能进行确认！");
					return;
				}
				setClient();
			}
		});

		// 客户信息取消
		this.btnClientBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				ClientWindow.hide();
			}
		});

		// 查询账户信息
		this.btnAcctQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showAcctList();
			}
		});
		// 账户信息完成确认
		this.btnAcctOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsAcct.getRecordCount() == 0) {
					UPrompt.alert(type, "账户信息查询无记录，不能进行确认！");
					return;
				}
				setAcct();
			}
		});
		// 账户信息取消
		this.btnAcctBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AcctWindow.hide();
			}
		});

		// 查询对方金融机构信息
		this.btnOrganizationQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showOrganizationList();
			}
		});
		// 对方金融机构信息完成确认
		this.btnOrganizationOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsOrganization.getRecordCount() == 0) {
					UPrompt.alert(type, "对方金融机构信息查询无记录，不能进行确认！");
					return;
				}
				setOrganization();
			}
		});
		// 对方金融机构信息取消
		this.btnOrganizationBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				OrganizationWindow.hide();
			}
		});

		// 查询交易对手信息
		this.btnCounterpartyQuery.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showCounterpartyList();
			}
		});

		// 交易对手信息完成确认
		this.btnCounterpartyOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsCounterparty.getRecordCount() == 0) {
					UPrompt.alert(type, "交易对手信息查询无记录，不能进行确认！");
					return;
				}
				setCounterparty();
			}
		});
		// 交易对手信息取消
		this.btnCounterpartyBack.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CounterpartyWindow.hide();
			}
		});

		// 可疑数据补录通过
		this.btnSave1.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!formAction11.validate() || !formAction12.validate() || !formAction13.validate() || !formAction14.validate() || !formAction15.validate() || !formAction17.validate()
						|| !formAction18.validate() || !formAction19.validate() || !formAction20.validate()) {
					UPrompt.alert(type, "请完整输入AML第二次补录可疑补录信息！");
					return;
				}
				//add by wzf 20170509 新增是否跨境字段 begin
        if (editCROSSBORDERflag02.getValueAsBoolean()) {
            dsResult1.setCurrentValue("IS_CROSSBORDER", "1");
        } else {
            dsResult1.setCurrentValue("IS_CROSSBORDER", "0");
        }

        //add by wzf 20170509 新增是否跨境字段 end
				if (AMLBizValidator.secondSuspiciousValidator(dsResult1.getCurrentRecord().getDataMap()) != null) {
					UPrompt.alert(type, AMLBizValidator.secondSuspiciousValidator(dsResult1.getCurrentRecord().getDataMap()));
					return;
				}
				
				//add by wzf 20150406 begin
				//补报时判断是否已超期30天，如果是则提示是否需要修改
				//add by kay 20170309 begin
				//if("0".equals(makeType)){
				//add by kay 20170309 end
					checkdeleteCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "checkjiucuo");
					checkdeleteCmd1.execute(new CommandCallback() {
						@Override
						public void onCallback(boolean status, Callback callback, String commandItemName) {							
							if (!status) {
								UPrompt.confirm(type, callback.getErrorMessage(), new PromptCallback() {
									@Override
									public void execute(Boolean value) {
										if (value == null || !value) {
											return;
										}
										queryUnitCodeCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectUnitCode");
										queryUnitCodeCmd.execute(new CommandCallback() {
											@Override
											public void onCallback(boolean status, Callback callback, String commandItemName) {
												if (!status) {
													UPrompt.alert(type, "报告机构代码查询无记录！");
												} else {
													if ("0".equals(makeType)) {
														dsResult1.setCurrentValue("operationType", operationType);
														dsResult1.setCurrentValue("UNIT_CODE", dsUnitCode.getCurrentValue("UNIT_CODE"));
														saveCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "addAMLSecond02");
													} else if ("1".equals(makeType)) {
														saveCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "updateAMLSecond02");
													}
													saveCmd1.execute(new CommandCallback() {
														@Override
														public void onCallback(boolean status, Callback callback, String commandItemName) {
															if (!status) {
																UPrompt.alert(type, callback.getErrorMessage());
															} else {
																UPrompt.alert(type, callback.getSuccessMessage());
																detailWindow1.hide();
																getunlock();
																showList();
															}
														}
													});
												}
											}
										});
									}
								});

							}else{
								queryUnitCodeCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectUnitCode");
								queryUnitCodeCmd.execute(new CommandCallback() {
									@Override
									public void onCallback(boolean status, Callback callback, String commandItemName) {
										if (!status) {
											UPrompt.alert(type, "报告机构代码查询无记录！");
										} else {
											if ("0".equals(makeType)) {
												dsResult1.setCurrentValue("operationType", operationType);
												dsResult1.setCurrentValue("UNIT_CODE", dsUnitCode.getCurrentValue("UNIT_CODE"));
												saveCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "addAMLSecond02");
											} else if ("1".equals(makeType)) {
												saveCmd1.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "updateAMLSecond02");
											}
											saveCmd1.execute(new CommandCallback() {
												@Override
												public void onCallback(boolean status, Callback callback, String commandItemName) {
													if (!status) {
														UPrompt.alert(type, callback.getErrorMessage());
													} else {
														UPrompt.alert(type, callback.getSuccessMessage());
														detailWindow1.hide();
														getunlock();
														showList();
													}
												}
											});
										}
									}
								});
							}
						}
					});
				//}
				//add by wzf 20150406 end
				
			}
		});
		// 可以数据补录取消
		this.btncancel1.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				detailWindow1.hide();
				getunlock();
				showList();
			}
		});

		this.btOk.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		this.btReturn.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		this.btOk2.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		this.btReturn2.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});

		// ****************************************************************************
		// 完成确认和取消完成确认
		// ****************************************************************************
		this.btconfirm.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// 判断后一节点是否完成确认

				comfirmQryCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "SelectUnConfirmSouce");
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
				// 判断后一节点是否完成确认

				uncomfirmQryCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "SelectConfirmSouce");
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
				if (dscomSource.getRecordCount() == 0) {
					UPrompt.alert("完成确认", "无数据源!");
					return;
				}

				if (dscomSource.getSelectedRecords().length == 0) {
					UPrompt.alert("完成确认", "未选择数据源!");
					return;
				}

				comfirmCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "SetComfirmSourceStatus");
				Map<String, String> condition = new HashMap<String, String>();
				String departId = ContextHelper.getContext().getValue("departId");
				condition.put("depart_id", departId);
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
				dscomSource.removeAllData();
				dscomSource.sync();
				comsourceWindow.hide();
			}
		});
		this.btOk2.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				if (dsuncomSource.getRecordCount() == 0) {
					UPrompt.alert("取消确认", "无数据源！");
					return;
				}

				if (dsuncomSource.getSelectedRecords().length == 0) {
					UPrompt.alert("完成确认", "未选择数据源!");
					return;
				}

				uncomfirmCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "SetComfirmSourceStatus");
				Map<String, String> condition = new HashMap<String, String>();
				String departId = ContextHelper.getContext().getValue("departId");
				condition.put("depart_id", departId);
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
				dsuncomSource.removeAllData();
				dsuncomSource.sync();
				uncomsourceWindow.hide();
			}
		});
		//add 20150918 pw 
				//44 (FXO补录画面增加快捷键弹出帮助信息)begin
				//帮助
						this.btnHelp.addClickListener(new ClickListener() {
							@Override
							public void onClick(ClickEvent event) {
								helpCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupservice", "selectHelpInfo");
								helpCmd.execute(new CommandCallback() {
									@Override
									public void onCallback(boolean status, Callback callback, String commandItemName) {
										if (!status) {
											UPrompt.alert(type, "查询帮助信息失败!");
											return;
										}
										helpForm.setReadOnly(true);
										helpWindow.show();
									}
								});
							}
						});
						//帮助页面的关闭按钮
						this.btnclose.addClickListener(new ClickListener() {
							@Override
							public void onClick(ClickEvent event) {
								helpWindow.hide();
							}
						});
				//end	
	}
}


