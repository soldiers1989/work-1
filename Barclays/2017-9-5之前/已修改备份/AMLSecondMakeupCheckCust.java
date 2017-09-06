package com.aif.rpt.biz.aml.processing.client;

import java.util.Map;
import java.util.HashMap;

import com.aif.rpt.common.client.BizFunClient;
import com.allinfinance.yak.uface.core.client.context.ContextHelper;
import com.allinfinance.yak.uface.core.client.utils.DateUtils;
import com.allinfinance.yak.uface.data.client.async.Callback;
import com.allinfinance.yak.uface.data.client.command.callback.CommandCallback;
import com.allinfinance.yak.uface.data.client.command.item.DataScope;
import com.allinfinance.yak.uface.data.client.dataset.action.DataAction;

import com.allinfinance.yak.uface.data.client.dataset.record.DataRecord;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt;
import com.allinfinance.yak.uface.ui.component.client.basic.UPrompt.PromptCallback;
import com.allinfinance.yak.uface.ui.component.client.download.UDownloaderClient;
import com.allinfinance.yak.uface.ui.component.client.events.ClickEvent;
import com.allinfinance.yak.uface.ui.component.client.events.ClickListener;
import com.allinfinance.yak.uface.ui.component.client.grid.events.RecordDoubleClickEvent;
import com.allinfinance.yak.uface.ui.component.client.grid.events.RecordDoubleClickListener;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class AMLSecondMakeupCheckCust extends AMLSecondMakeupCheck {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2280395932074983298L;
	// 标题名称
	private String type = "AML第二次补录";
	private String typeinner = "email附件";
	// 判断删除按钮的类型 0-"删除" 1-"取消删除"
//	private String delNo = "0";
	// 判断操作类型 0-"新增" 1-"修改 "
//	private String makeType = "0";
	// 判断查询结果所赋值的字段
//	private String selectName = "";
	// 判断查询国家地区地址
//	private int lenth;
	private boolean flag = false;
	//用户账号
	private String CheckAccid="";

	private DataRecord currentRecord = null;
	
	private DataRecord currentRecordAcc = null;
	//对手客户账号
	private String  CTPY_ACCT_ID="";

	@Override
	protected void setting() {
		this.setPageID("");
	}

	/**
	 * email附件的操作
	 */
	protected void checkEmailAcc(){
		detailWindowCheckAcc.show();
	}
	
	
	
	/**
	 * 业务查询
	 */
	protected void showList() {
		if (tableset.getSelectedTabNumber() == 0) {
			dsResult.setCurrentValue("select_flag", false);
			queryCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "selectAMLSecondMakeupCheck");
			queryCmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (callback.getCallbackCount() == 0) {
						dsResult.sync();
						UPrompt.alert(type, "查询大额补录信息无记录");
					}
					if (!status) {
						UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
					}
				}
			});
		} else {
			dsResult1.setCurrentValue("select_flag", false);
			query2Cmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "selectAMLSecondMakeupSuspiciousCheck");
			query2Cmd.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (callback.getCallbackCount() == 0) {
						dsResult1.sync();
						UPrompt.alert(type, "查询可疑补录信息无记录");
					}
					if (!status) {
						UPrompt.alert(type, "查询失败，错误信息：" + callback.getErrorMessage());
					}
				}
			});
		}
		flag = true;
	}
	
	/**
	 * email附件查询
	 */
	protected void showListAcc(){
		//dsQueryAcc.setCurrentValue("REF_NO", dsResult.getCurrentValue("REF_NO"));//大额
		//可疑
		queryCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "uploadedShow");
		queryCmdAcc.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, "上传附件查询错误，错误信息：" + callback.getErrorMessage());
				} 
		     }
	   });	
	}
	
	/**
	 * email附件单条审核通过
	 */
//	protected void showCheckOneAcc() {
//		
//		//if (tableset.getSelectedTabNumber() == 0) {
//			dsQueryAcc.setCurrentValue("ACC_ID", dsResultTBAcc.getCurrentValue("ACC_ID"));
//			//dsQueryAcc.setCurrentValue("CHECK_INFO", dsResultTBAcc.getCurrentValue("CHECK_INFO"));
//			//System.out.println("---------------check_info-----------"+dsResultTBAcc.getCurrentValue("CHECK_INFO"));
//			
//			updateCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkAcc");
//			// checkCheckCmd01.setCommandDataScope(DataScope.SELECT);
//			updateCmdAcc.execute(new CommandCallback() {
//				@Override
//				public void onCallback(boolean status, Callback callback, String commandItemName) {
//					if (!status) {
//						UPrompt.alert(type, "email附件审核错误，错误信息：" + callback.getErrorMessage());
//						//dsResult.setCommand(queryCmd);
//					} else {
//						UPrompt.alert(type, "email附件审核通过");
//					}
//				}
//			});
//
//		//} 
//
//	}
	
	/**
	 * email附件单条审核拒绝
	 */
//	protected void showRefuseOneAcc() {//showRefuseOneAcc
//		if(dsQueryAcc.getCurrentValue("CHECK_INFO") != null && !"".equals(dsQueryAcc.getCurrentValue("CHECK_INFO").trim())){
//			dsQueryAcc.setCurrentValue("ACC_ID", dsResultTBAcc.getCurrentValue("ACC_ID"));
//			//dsQueryAcc.setCurrentValue("CHECK_INFO", dsResultTBAcc.getCurrentValue("CHECK_INFO"));
//			//System.out.println("---------------check_info-----------"+dsResultTBAcc.getCurrentValue("CHECK_INFO"));
//			
//			updateCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "refuseAcc");
//			updateCmdAcc.execute(new CommandCallback() {
//				@Override
//				public void onCallback(boolean status, Callback callback, String commandItemName) {
//					if (!status) {
//						UPrompt.alert(type, "email附件审核错误，错误信息：" + callback.getErrorMessage());
//					} else {
//						UPrompt.alert(type, "email附件审核拒绝");
//					}
//				}
//			});
//		}else{
//			UPrompt.alert(type, "请填写审核拒绝信息！");
//		}
//	}

	/**
	 * 批量解锁操作大额
	 */
	protected void getunlockBatch() {
		unlockBatchCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "getunlockBatch");
		unlockBatchCmd01.setCommandDataScope(DataScope.SELECT);
		unlockBatchCmd01.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, callback.getErrorMessage());
				}

			}
		});
	}

	/**
	 * 批量解锁操作可疑
	 */
	protected void getunlockBatch1() {
		unlockBatchCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "getunlockBatch");
		unlockBatchCmd02.setCommandDataScope(DataScope.SELECT);
		unlockBatchCmd02.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, callback.getErrorMessage());
				}

			}
		});
	}
	
	private void selectWhither() {
		// 加载地区代码
		Map<String, String> whither = new HashMap<String, String>();
		String a1=dsResult.getCurrentValue("TRADE_COUNTRY");
		String a2=dsResult.getCurrentValue("TRADE_REGION");
		String a3=dsResult.getCurrentValue("TRADE_VENUE_COUNTRY");
		String a4=dsResult.getCurrentValue("TRADE_VENUE_REGION");
		String a5=dsResult1.getCurrentValue("TRADE_VENUE_COUNTRY");
		String a6=dsResult1.getCurrentValue("TRADE_VENUE_REGION");
		String a7=dsResult1.getCurrentValue("CTPY_FI_COUNTRY");
		String a8=dsResult1.getCurrentValue("CTPY_FI_REGION_CODE");
		String condition = "'"+a1+"','"+a2+"','"+a3+"','"+a4+"','"+a5+"','"+a6+"','"+a7+"','"+a8+"'";
		whither.put("condition", condition);
		qryWhitherCmd.setExtraInfo(whither);
		qryWhitherCmd.setCommandService("com.aif.rpt.common.server.BizFuncService", "SelectWhitherList");
		qryWhitherCmd.setCommandDataScope(DataScope.SELECT);
		qryWhitherCmd.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
//					UPrompt.alert(type, "地区代码查询错误，错误信息：" + callback.getErrorMessage());
					return;
				}
				dsWhitherList.getCurrentRecord().getDataMap();
//				editTradeCountry01.flushData();
//				editTradeRegion01.flushData();
				editTradeVenueCountry01.flushData();
				editTradeVenueRegion01.flushData();
				editTradeVenueCountry02.flushData();
				editTradeVenueRegion02.flushData();
				editOrganizationCountry02.flushData();
				editOrganizationRegion02.flushData();
			}
		});
	}
	
	
	/**
	 *单笔解锁操作大额
	 */
	protected void getunlock() {
		unlockCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "getunlock");
		unlockCmd01.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, callback.getErrorMessage());
				}

			}
		});
	}
	
	/**
	 * 单笔解锁操作可疑
	 */
	protected void getunlock1() {
		unlockCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "getunlock");
		unlockCmd02.execute(new CommandCallback() {
			@Override
			public void onCallback(boolean status, Callback callback, String commandItemName) {
				if (!status) {
					UPrompt.alert(type, callback.getErrorMessage());
				}

			}
		});
	}

	protected void showCheckOne() {
		
		if (tableset.getSelectedTabNumber() == 0) {
			checkCheckCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "check");
			// checkCheckCmd01.setCommandDataScope(DataScope.SELECT);
			checkCheckCmd01.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (!status) {
						UPrompt.alert(type, callback.getErrorMessage());
						dsResult.setCommand(queryCmd);

					} else {
						//modify by wzf 20170510 增加是否跨境标志 begin
            if ("1".equals(dsResult.getCurrentValue(
                            "IS_CROSSBORDER"))) {
                editCROSSBORDERflag01.setValue(true);
            } else {
                editCROSSBORDERflag01.setValue(false);
            }

            //modify by wzf 20170510 增加是否跨境标志 end
						// 单条记录的审核界面
						formAction91.setReadOnly("editCheckInfo", false);
						selectWhither();
						btnOk01.show();
						btnRefuse01.show();
						btnCancel01.setTitle("取消");
						editCheckInfo01.setLength(256);
						detailWindow.show();
						if(dsResult.getCurrentRecord().getValue("IS_DEL").endsWith("0")){
							editCtpyAccountId01.setRequired(true);
							formAction8.setReadOnly("CTPY_ACCT_ID", false);
							editCtpyAccountId01.setTitle("重新输入对手账号");
							formAction8.focusInItem(editCtpyAccountId01);
							CTPY_ACCT_ID=editCtpyAccountId01.getValue().toString();
							editCtpyAccountId01.setValue("");
						}else{
							editCtpyAccountId01.setTitle("对手账号");
							formAction8.setReadOnly("CTPY_ACCT_ID", true);
						}
					}
				}
			});

		} else {
			checkCheckCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "check");
			//checkCheckCmd02.setCommandDataScope(DataScope.SELECT);
			checkCheckCmd02.execute(new CommandCallback() {
				@Override
				public void onCallback(boolean status, Callback callback, String commandItemName) {
					if (!status) {
						UPrompt.alert(type, callback.getErrorMessage());
						dsResult1.setCommand(queryCmd);

					} else {
						//modify by wzf 20170510 增加是否跨境标志 begin
            if ("1".equals(dsResult1.getCurrentValue(
                            "IS_CROSSBORDER"))) {
                editCROSSBORDERflag02.setValue(true);
            } else {
                editCROSSBORDERflag02.setValue(false);
            }

            //modify by wzf 20170510 增加是否跨境标志 end
						// 单条记录的审核界面
						if(dsResult1.getCurrentRecord().getValue("IS_DEL").endsWith("0")){
							editCtpyAccountId02.setRequired(true);
							formAction18.setReadOnly("CTPY_ACCT_ID", false);
							editCtpyAccountId02.setTitle("重新输入对手账号");
							formAction18.focusInItem(editCtpyAccountId02);
							CTPY_ACCT_ID=editCtpyAccountId02.getValue().toString();
							editCtpyAccountId02.setValue("");
						}else{
							editCtpyAccountId02.setTitle("对手账号");
							formAction18.setReadOnly("CTPY_ACCT_ID", true);
						}
						formAction92.setReadOnly("editCheckInfo", false);
						btnOk02.show();
						btnRefuse02.show();
						btnCancel02.setTitle("取消");
						editCheckInfo02.setLength(256);
						detailWindow1.show();
					}
				}
			});


		}

	}

	@Override
	protected void beforeRender() {
		flag = false;
		
		dsQueryAcc.addDataRecord(new DataRecord(),new DataAction() {
			
			@Override
			public void doAction(DataRecord record) {
				// TODO Auto-generated method stub
			}
		});
		dsResultTBAcc.addDataRecord(new DataRecord(),new DataAction() {
			
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
		// 审核窗口关闭的解锁事件
		detailWindow.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				detailWindow.hide();
				getunlock();
				showList();
			}
		});
		detailWindow1.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				detailWindow1.hide();
				getunlock1();
				showList();
			}

		});

		// 批量审核窗口关闭的解锁事件
		checkBatchWindow01.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				checkBatchWindow01.hide();
				getunlockBatch();
				showList();

			}

		});

		// 批量审核窗口关闭的解锁事件
		checkBatchWindow02.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				checkBatchWindow02.hide();
				getunlockBatch1();
				showList();

			}

		});

		// 双击表格审核事件
		tableResult.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				CheckAccid_Accid2.setValue("");
				
				if (!BizFunClient.isCanCheck(dsResult.getCurrentRecord().getDataMap())) {
					return;
				}
				showCheckOne();
			}

		});

		// 双击表格审核事件
		tableResult1.addRecordDoubleClickListener(new RecordDoubleClickListener() {
			@Override
			public void onRecordClick(RecordDoubleClickEvent event) {
				CheckAccid_Accid2.setValue("");
				if (!BizFunClient.isCanCheck(dsResult1.getCurrentRecord().getDataMap())) {
					return;
				}
				
				showCheckOne();
				
			}

		});
		
		/**
		 * 点【email附件】按钮
		 */
		this.btnCheckAcc.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//点【email附件】按钮时判断是不是可疑
				if (tableset.getSelectedTabNumber() == 0) {
					//if (dsResult.getRecordCount() == 0) {
						UPrompt.alert(type, "AML大额补录信息无email附件！");
						return;
					//}
				} else {
					if (dsResult1.getRecordCount() == 0) {
						UPrompt.alert(type, "无AML可疑补录信息，不能查看email附件！");
						return;
					}else{
						
						DataRecord[] selectedRecords = dsResult1.getSelectedRecords();
						if (dsResult1.getRecordCount() == 0) {
							UPrompt.alert(type, "无记录可查看email附件！");
							return;
						}else if (selectedRecords == null || selectedRecords.length < 1) {
							UPrompt.alert(type, "请选择要查看email附件的记录！");
							return;
						}else if (selectedRecords.length > 1) {
							UPrompt.alert(type, "请选择单条记录进行查看email附件！");
							return;
						}
						
						dsQueryAcc.setCurrentValue("REF_NO", selectedRecords[0].getValue("REF_NO"));
						checkEmailAcc();
						showListAcc();
					}
				}
				
//				checkEmailAcc();
//				showListAcc();
			}
		});
		
		//刷新
		this.btnRefresh.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(ClickEvent event) {
				showListAcc();
			}
		});
		
		//查看
		this.btnView.addClickListener(new ClickListener() {
							
			@Override
			public void onClick(ClickEvent event) {
//				if (dsResultTBAcc.getRecordCount() == 0) {
//					UPrompt.alert(type, "请选择一条信息查看！");
//					return;
//				}
				
				DataRecord[] selectedRecords = dsResultTBAcc.getSelectedRecords();
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
				//url.append("/rpt/report/emailShow.jsp?accPath=");
				//exlipse run
				url.append("/report/emailShow.jsp?accPath=");
				url.append(accPath);
				//System.out.println("---------url------"+url.toString());
				Window.open(url.toString(), "_blank", "");
			}
		});
		
		//下载
		this.btnDownload.addClickListener(new ClickListener() {
					
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsResultTBAcc.getRecordCount() == 0) {
					UPrompt.alert(type, "无附件可下载！");
					return;
				}
				
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
				//System.out.println("------------------ACC_ID------------------"+dsResultTBAcc.getCurrentValue("ACC_ID"));
				dsQueryAcc.setCurrentValue("ACC_ID", dsResultTBAcc.getCurrentValue("ACC_ID"));
				//System.out.println("------------------ACC_ID------------------"+dsQueryAcc.getCurrentValue("ACC_ID"));
				updateCmdAcc.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "updateAcc");
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
		
		//email附件的审核
//		this.btnCheckAcc2.addClickListener(new ClickListener() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				
//				DataRecord[] selectedRecords = dsResultTBAcc.getSelectedRecords();
//				if (selectedRecords == null || selectedRecords.length < 1) {
//					UPrompt.alert(type, "请选择要审核的记录！");
//					return;
//				}
//				int i = 0;
//				for (DataRecord dataRecord : selectedRecords) {
//					//System.out.println("---------------------CHECK_STATUS------------"+dataRecord.getValue("CHECK_STATUS"));
//					if (dataRecord.getValue("CHECK_STATUS") != null && "0".equals(dataRecord.getValue("CHECK_STATUS"))) {
//						currentRecordAcc = dataRecord;
//						i++;
//					}
//				}
//				if (i == 1) {//eamil附件单条审核
//					
//					dsResultTBAcc.setCurrentRecord(currentRecordAcc);
//					String check_info1 = dsResultTBAcc.getCurrentValue("CHECK_INFO");
//					dsQueryAcc.setCurrentValue("CHECK_INFO", check_info1);
//					showCheckOneAcc();
//					UPrompt.alert(type, "eamil附件单条审核通过！");
//				} else if (i > 1) {//eamil附件单条审核
//					String check_info2 = dsResultTBAcc.getCurrentValue("CHECK_INFO");//审核信息只存一条，需调试
//					
//					for (DataRecord dataRecord : selectedRecords) {
//						currentRecordAcc = dataRecord;
//						dsResultTBAcc.setCurrentRecord(currentRecordAcc);
//						
//						dsQueryAcc.setCurrentValue("CHECK_INFO", check_info2);
//						showCheckOneAcc();
//					}
//					UPrompt.alert(type, "eamil附件多条审核通过！");
//				} else {
//					UPrompt.alert(type, "eamil附件信息都已经审核，不能审核！");
//				}
//				showListAcc();
//			}
//		});
//		
//		//email附件的审核拒绝//btnRefuseAcc2
//		this.btnRefuseAcc2.addClickListener(new ClickListener() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				
//				DataRecord[] selectedRecords = dsResultTBAcc.getSelectedRecords();
//				if (selectedRecords == null || selectedRecords.length < 1) {
//					UPrompt.alert(type, "请选择要拒绝的记录！");
//					return;
//				}
//				int i = 0;
//				for (DataRecord dataRecord : selectedRecords) {
//					//System.out.println("---------------------CHECK_STATUS------------"+dataRecord.getValue("CHECK_STATUS"));
//					if (dataRecord.getValue("CHECK_STATUS") != null && "0".equals(dataRecord.getValue("CHECK_STATUS"))) {
//						currentRecordAcc = dataRecord;
//						i++;
//					}
//				}
////				if(dsResultTBAcc.getCurrentValue("CHECK_INFO") == null && "".equals(dsResultTBAcc.getCurrentValue("CHECK_INFO").trim())){
////					UPrompt.alert(type, "请填写审核拒绝信息！");
////					return;
////				}
//				if (i == 1) {//eamil附件单条审核拒绝
//					dsResultTBAcc.setCurrentRecord(currentRecordAcc);
//					String check_info1 = dsResultTBAcc.getCurrentValue("CHECK_INFO");
//					dsQueryAcc.setCurrentValue("CHECK_INFO", check_info1);
//					showRefuseOneAcc();
//					//UPrompt.alert(type, "eamil附件单条审核拒绝！");
//				} else if (i > 1) {//eamil附件多条审核拒绝
//					String check_info2 = dsResultTBAcc.getCurrentValue("CHECK_INFO");//审核信息只存一条，需调试
//					for (DataRecord dataRecord : selectedRecords) {
//						currentRecordAcc = dataRecord;
//						dsResultTBAcc.setCurrentRecord(currentRecordAcc);
//						dsQueryAcc.setCurrentValue("CHECK_INFO", check_info2);
//						showRefuseOneAcc();
//					}
//					//UPrompt.alert(type, "eamil附件多条审核拒绝！");
//				} else {
//					UPrompt.alert(type, "eamil附件信息都已经审核，不能审核！");
//				}
//				showListAcc();
//			}
//		});
		

		// 点击查询按钮
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
		this.btnCheck.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// 大额审核
				CheckAccid_Accid2.setValue("");
				if (tableset.getSelectedTabNumber() == 0) {
					// TODO Auto-generated method stub
					DataRecord[] selectedRecords = dsResult.getSelectedRecords();
					if (selectedRecords == null || selectedRecords.length < 1) {
						UPrompt.alert(type, "请选择要审核的记录！");
						return;
					}
					int i = 0;
					for (DataRecord dataRecord : selectedRecords) {
						if (dataRecord.getValue("CHECK_STATUS") != null && "0".equals(dataRecord.getValue("CHECK_STATUS"))) {
							currentRecord = dataRecord;
							i++;
						}
					}
					if (i == 1) {
						// 审核检查
						for (DataRecord dataRecord : selectedRecords) {
							if (!BizFunClient.isCanCheck(dataRecord.getDataMap())) {
								return;
							}
						}

						dsResult.setCurrentRecord(currentRecord);
						
						showCheckOne();
						
					} else if (i > 1) {
						// TODO Auto-generated method stub
						// 审核检查
						for (DataRecord dataRecord : selectedRecords) {
							if (!BizFunClient.isCanCheck(dataRecord.getDataMap())) {
								return;
							}
						}
						checkCheckBatchCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkCheckBath");
						checkCheckBatchCmd01.setCommandDataScope(DataScope.SELECT);
						checkCheckBatchCmd01.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {
								if (!status) {
									UPrompt.alert(type, callback.getErrorMessage());
									dsResult.setCommand(queryCmd);

								} else {
									// 批量审核界面
									//add by pw
									//begin
									boolean isdelete=true;
									for (DataRecord dataRecord : dsResult.getSelectedRecords()) {
										if(dataRecord.getValue("IS_DEL").endsWith("0")){
											isdelete=false;
													break;
										}
									}
									//end
									if(!isdelete){
										UPrompt.alert(type, "要求上报的记录不能进行多条审核");
									}
									else{
									//UPrompt.alert(type, "不能选中多条审核");
									editBatchCheckInfo01.setLength(256);
									dsResult.setCurrentValue("CHECK_INFO", "");
									dsResult.sync();
									checkBatchWindow01.show();
									}
								}
							}
						});
					} else {
						UPrompt.alert(type, "AML信息都已经审核，不能审核！");
					}
					// 可疑审核
				} else {
					// TODO Auto-generated method stub
					DataRecord[] selectedRecords = dsResult1.getSelectedRecords();
					if (selectedRecords == null || selectedRecords.length < 1) {
						UPrompt.alert(type, "请选择要审核的记录！");
						return;
					}
					int i = 0;
					for (DataRecord dataRecord : selectedRecords) {
						if (dataRecord.getValue("CHECK_STATUS") != null && !"1".equals(dataRecord.getValue("CHECK_STATUS"))) {
							currentRecord = dataRecord;
							i++;
						}
					}
					if (i == 1) {
						// 审核检查
						for (DataRecord dataRecord : selectedRecords) {
							if (!BizFunClient.isCanCheck(dataRecord.getDataMap())) {
								return;
							}
						}
						dsResult.setCurrentRecord(currentRecord);
						
						showCheckOne();
						
					} else if (i > 1) {
						// TODO Auto-generated method stub
						// 审核检查
						for (DataRecord dataRecord : selectedRecords) {
							if (!BizFunClient.isCanCheck(dataRecord.getDataMap())) {
								return;
							}
						}
						checkCheckBatchCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkCheckBath");
						checkCheckBatchCmd02.setCommandDataScope(DataScope.SELECT);
						checkCheckBatchCmd02.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {
								if (!status) {
									UPrompt.alert(type, callback.getErrorMessage());
									dsResult1.setCommand(queryCmd);

								} else {
									// 批量审核界面
									//add by pw
									//begin
									boolean isdelete=true;
									for (DataRecord dataRecord : dsResult1.getSelectedRecords()) {
										if(dataRecord.getValue("IS_DEL").endsWith("0")){
											isdelete=false;
													break;
										}
									}
									//end
									if(!isdelete){
										UPrompt.alert(type, "要求上报的记录不能进行多条审核");
									}
									else{
									editBatchCheckInfo02.setLength(256);
									dsResult1.setCurrentValue("CHECK_INFO", "");
									dsResult1.sync();
									checkBatchWindow02.show();
									}
								}
							}
						});
					} else {
						UPrompt.alert(type, "AML信息都已经审核，不能审核！");
					}

				}
			}
		});
		
		
		this.btnCancelCheck.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {

				if (tableset.getSelectedTabNumber() == 0) {
					DataRecord[] selectedRecords = dsResult.getSelectedRecords();
					if (selectedRecords == null || selectedRecords.length < 1) {
						UPrompt.alert(type, "请选择要取消审核的记录！");
						return;
					} else {
						// TODO Auto-generated method stub
						// 取消审核检查
						unCheckCheckCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "uncheckCheck");
						unCheckCheckCmd01.setCommandDataScope(DataScope.SELECT);
						unCheckCheckCmd01.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {
								if (!status) {
									UPrompt.alert(type, callback.getErrorMessage());
									dsResult.setCommand(queryCmd);
								} else {
									UPrompt.confirm("审核", "您确定要取消审核选中的记录吗？", new PromptCallback() {
										@Override
										public void execute(Boolean value) {
											if (value != null && value) {
												// TODO Auto-generated method
												// stub
												unCheckCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "unCheckAML");
												unCheckCmd01.setCommandDataScope(DataScope.SELECT);
												unCheckCmd01.execute(new CommandCallback() {
													@Override
													public void onCallback(boolean status, Callback callback, String commandItemName) {
														if (!status) {
															UPrompt.alert(type, callback.getErrorMessage());
														} else {
															UPrompt.alert(type, callback.getSuccessMessage());
															getunlockBatch();
															showList();
														}
													}
												});
											} else {
												getunlockBatch();
												showList();
											}

										}
									});
								}
							}
						});
					}
				} else {
					DataRecord[] selectedRecords = dsResult1.getSelectedRecords();
					if (selectedRecords == null || selectedRecords.length < 1) {
						UPrompt.alert(type, "请选择要取消审核的记录！");
						return;
					} else {
						// TODO Auto-generated method stub
						// 取消审核检查
						unCheckCheckCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "uncheckCheck");
						unCheckCheckCmd02.setCommandDataScope(DataScope.SELECT);
						unCheckCheckCmd02.execute(new CommandCallback() {
							@Override
							public void onCallback(boolean status, Callback callback, String commandItemName) {
								if (!status) {
									UPrompt.alert(type, callback.getErrorMessage());
									dsResult1.setCommand(queryCmd);

								} else {
									UPrompt.confirm("审核", "您确定要取消审核选中的记录吗？", new PromptCallback() {
										@Override
										public void execute(Boolean value) {
											if (value != null && value) {
												// TODO Auto-generated method
												// stub
												unCheckCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "unCheckAML");
												unCheckCmd02.setCommandDataScope(DataScope.SELECT);
												unCheckCmd02.execute(new CommandCallback() {
													@Override
													public void onCallback(boolean status, Callback callback, String commandItemName) {
														if (!status) {
															UPrompt.alert(type, callback.getErrorMessage());
														} else {
															UPrompt.alert(type, callback.getSuccessMessage());
															getunlockBatch1();
															showList();
														}
													}
												});
											} else {
												getunlockBatch1();
												showList();
											}

										}
									});
								}
							}
						});
					}

				}

			}

		});
		this.btnShow.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				editCtpyAccountId01.setTitle("对手账号");
				formAction8.setReadOnly("CTPY_ACCT_ID", true);
				editCtpyAccountId02.setTitle("对手账号");
				formAction18.setReadOnly("CTPY_ACCT_ID", true);
				if (tableset.getSelectedTabNumber() == 0) {
					DataRecord[] selectedRecords = dsResult.getSelectedRecords();
					if (selectedRecords == null || selectedRecords.length < 1) {
						UPrompt.alert(type, "请选择要查看的记录！");
						return;
					}
					if (selectedRecords.length == 1) {
						dsResult.setCurrentRecord(selectedRecords[0]);
						formAction91.setReadOnly("CHECK_INFO", true);
						selectWhither();
						btnOk01.hide();
						btnRefuse01.hide();
						btnCancel01.setTitle("关闭");						
						detailWindow.show();
						//modify by wzf 20170510 增加是否跨境标志 begin
            if ("1".equals(dsResult.getCurrentValue(
                            "IS_CROSSBORDER"))) {
                editCROSSBORDERflag01.setValue(true);
            } else {
                editCROSSBORDERflag01.setValue(false);
            }

            //modify by wzf 20170510 增加是否跨境标志 end
					} else {
						UPrompt.alert(type, "只能查看一条记录的明细信息！");
					}
				} else {
					DataRecord[] selectedRecords = dsResult1.getSelectedRecords();
					if (selectedRecords == null || selectedRecords.length < 1) {
						UPrompt.alert(type, "请选择要审核的记录！");
						return;
					}
					if (selectedRecords.length == 1) {
						dsResult1.setCurrentRecord(selectedRecords[0]);
						formAction92.setReadOnly("editCheckInfo", false);
						selectWhither();
						btnOk02.hide();
						btnRefuse02.hide();
						btnCancel02.setTitle("关闭");
						detailWindow1.show();
						//modify by wzf 20170510 增加是否跨境标志 begin
            if ("1".equals(dsResult1.getCurrentValue(
                            "IS_CROSSBORDER"))) {
                editCROSSBORDERflag02.setValue(true);
            } else {
                editCROSSBORDERflag02.setValue(false);
            }

            //modify by wzf 20170510 增加是否跨境标志 end
					} else {
						UPrompt.alert(type, "只能查看一条记录的明细信息！");
					}

				}
			}
		});

		// 大额补录审核确定提交
		this.btnOk01.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//add by pw 20150105 手工验证客户账号
				//begin
				
				if(dsResult.getCurrentRecord().getValue("IS_DEL").endsWith("0")&&!CTPY_ACCT_ID.equals(editCtpyAccountId01.getValue())){
					
					if("".equals(editCtpyAccountId01.getValue())){
						UPrompt.alert(type, "请输入对手账号");
					}else{
						UPrompt.alert(type, "输入的对手账号错误，请重新输入。");
					}
				}else{
				//end
					formAction8.setReadOnly("CTPY_ACCT_ID", true);
					formAction18.setReadOnly("CTPY_ACCT_ID", true);
				CheckCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkAML");
				CheckCmd01.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							detailWindow.hide();
							UPrompt.alert(type, "AML大额补录信息审核成功！");
							getunlock();
							showList();
						}
					}
				});
			}
			}
		});
		// 可疑补录审核确定提交
		this.btnOk02.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//add by pw 20150105 手工验证客户账号
				//begin
				
				if(dsResult1.getCurrentRecord().getValue("IS_DEL").endsWith("0")&&!CTPY_ACCT_ID.equals(editCtpyAccountId02.getValue())){
					if("".equals(editCtpyAccountId02.getValue())){
						UPrompt.alert(type, "请输入对手账号");
					}else{
						UPrompt.alert(type, "输入的对手账号错误，请重新输入。");
					}
					
				}else{
					formAction8.setReadOnly("CTPY_ACCT_ID", true);
					formAction18.setReadOnly("CTPY_ACCT_ID", true);
				//end
				CheckCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkAML");
				CheckCmd02.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							detailWindow1.hide();
							UPrompt.alert(type, "AML可疑补录信息审核成功！");
							getunlock1();
							showList();
						}
					}
				});
			}
			}
		});

		// 大额审核拒绝
		this.btnRefuse01.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsResult.getCurrentValue("CHECK_INFO") == null || "".equals(dsResult.getCurrentValue("CHECK_INFO").trim())) {
					UPrompt.alert(type, "请输入AML大额补录的审核拒绝信息！");
					return;
				}
				checkRefuseCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkRefuseAML");
				checkRefuseCmd01.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, "AML大额补录信息审核拒绝成功！");
							detailWindow.hide();
							getunlock();
							showList();
						}
					}
				});

			}
		});

		// 可疑审核拒绝
		this.btnRefuse02.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsResult1.getCurrentValue("CHECK_INFO") == null || "".equals(dsResult1.getCurrentValue("CHECK_INFO").trim())) {
					UPrompt.alert(type, "请输入AML可疑补录的审核拒绝信息！");
					return;
				}
				checkRefuseCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkRefuseAML");
				checkRefuseCmd02.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, "AML可疑补录信息审核拒绝成功！");
							detailWindow1.hide();
							getunlock1();
							showList();
						}
					}
				});
			}
		});
		// 大额审核界面取消操作
		this.btnCancel01.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				detailWindow.hide();
				getunlock();
			}
		});
		// 可疑审核界面取消操作
		this.btnCancel02.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				detailWindow1.hide();
				getunlock1();
			}
		});

		// 大额业务批量审核通过
		this.btnOkBatch01.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Map<String, String> checkinfo = new HashMap<String, String>();
				checkinfo.put("CHECK_INFO", dsResult.getCurrentValue("CHECK_INFO"));
				checkBatchCmd01.setExtraInfo(checkinfo);
				checkBatchCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkAMLs");
				checkBatchCmd01.setCommandDataScope(DataScope.SELECT);
				checkBatchCmd01.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, callback.getSuccessMessage());
							checkBatchWindow01.hide();
							getunlockBatch();
							showList();
						}
					}
				});
			}
		});

		// 可疑业务批量审核通过
		this.btnOkBatch02.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			
				Map<String, String> checkinfo = new HashMap<String, String>();
				checkinfo.put("CHECK_INFO", dsResult1.getCurrentValue("CHECK_INFO"));
				checkBatchCmd02.setExtraInfo(checkinfo);
				checkBatchCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkAMLs");
				checkBatchCmd02.setCommandDataScope(DataScope.SELECT);
				checkBatchCmd02.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, callback.getSuccessMessage());
							checkBatchWindow02.hide();
							getunlock1();
							showList();
						}
					}
				});
			}
		});
		// 大额批量审核拒绝
		this.btnRefuseBatch01.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsResult.getCurrentValue("CHECK_INFO") == null || "".equals(dsResult.getCurrentValue("CHECK_INFO").trim())) {
					UPrompt.alert(type, "请输入AML补录的审核拒绝信息！");
					return;
				}
				Map<String, String> checkinfo = new HashMap<String, String>();
				checkinfo.put("CHECK_INFO", dsResult.getCurrentValue("CHECK_INFO"));
				checkBatchCmd01.setExtraInfo(checkinfo);
				checkBatchCmd01.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkRefuseAMLs");
				checkBatchCmd01.setCommandDataScope(DataScope.SELECT);
				checkBatchCmd01.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, callback.getSuccessMessage());
							checkBatchWindow01.hide();
							getunlockBatch();
							showList();
						}
					}
				});

			}
		});
		// 可疑批量审核拒绝
		this.btnRefuseBatch02.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (dsResult1.getCurrentValue("CHECK_INFO") == null || "".equals(dsResult1.getCurrentValue("CHECK_INFO").trim())) {
					UPrompt.alert(type, "请输入AML补录的审核拒绝信息！");
					return;
				}

				Map<String, String> checkinfo = new HashMap<String, String>();
				checkinfo.put("CHECK_INFO", dsResult1.getCurrentValue("CHECK_INFO"));
				checkBatchCmd02.setExtraInfo(checkinfo);
				checkBatchCmd02.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "checkRefuseAMLs");
				checkBatchCmd02.setCommandDataScope(DataScope.SELECT);
				checkBatchCmd02.execute(new CommandCallback() {
					@Override
					public void onCallback(boolean status, Callback callback, String commandItemName) {

						if (!status) {
							UPrompt.alert(type, callback.getErrorMessage());
						} else {
							UPrompt.alert(type, callback.getSuccessMessage());
							checkBatchWindow02.hide();
							getunlock1();
							showList();
						}
					}
				});

			}
		});
		// 批量审核界面取消操作
		this.btnCancelBath01.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				checkBatchWindow01.hide();
				getunlockBatch();
			}
		});
		// 批量审核界面取消操作
		this.btnCancelBath02.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				checkBatchWindow02.hide();
				getunlockBatch1();
			}
		});

		this.btconfirm.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		this.btcancel.addClickListener(new ClickListener() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
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

				comfirmQryCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "SelectUnConfirmSouce");
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

				uncomfirmQryCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "SelectConfirmSouce");
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

				comfirmCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "SetComfirmSourceStatus");
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

				uncomfirmCmd.setCommandService("com.aif.rpt.biz.aml.processing.server.AMLSecondMakeupCheckservice", "SetComfirmSourceStatus");
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
		
	}
}