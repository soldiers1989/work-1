Ext.define(
                'CFrame.view.zz.merchantMaintainCheckWinView',
                {
                    extend : 'top.Ext.Window',
                    alias : 'widget.merchantMaintainCheckWinView',
                    // singleton: true,
                    id : 'merchantMaintainCheckWinView',
                    operation : 'mod', // view-只读；mod-修改；add-新增；del-删除
                    type : '',
                    oldmodel : {},
                    newmodel : {},
                    resizable : false,
                    maximizable : false,
                    border : false,
                    animCollapse : true,
                    constrain : true,
                    autoScroll : true,
                    modal : true,
                    ret : '',
                    submiturl : '',
                    keyfields : {},
                    initComponent : function() {
                        var me = this;
                        Ext.applyIf(
                                        this,
                                        {
                                            layout : 'fit',
                                            width : 330,
                                            height : 250,
                                            draggable : true,
                                            closeAction : 'destroy',
                                            title : '合作机构信息审核',
                                            items : [ {
                                                xtype : 'form',
                                                layout : {
                                                    columns : 1,
                                                    type : 'table',
                                                    width : 500
                                                },
                                                id : 'detailForm',
                                                items : [ {
                                                    xtype : 'textfield',
                                                    id : 'tf_MERCHANT_ID',
                                                    name : 'MERCHANT_ID',
                                                    fieldLabel : '合作机构代码',
                                                    labelWidth : 95, // 标签宽度
                                                    labelAlign : 'right',
                                                    readOnly : true,
                                                    colspan : 2,
                                                    rowspan : 1
                                                }, {
                                                    xtype : 'textfield',
                                                    id : 'tF_MERCHANT_NAME',
                                                    name : 'MERCHANT_NAME',
                                                    fieldLabel : '合作机构名称',
                                                    labelWidth : 95, // 标签宽度
                                                    labelAlign : 'right',
                                                    readOnly : true,
                                                    colspan : 2,
                                                    rowspan : 1
                                                }, {
                                                    xtype : 'textfield',
                                                    id : 'tf_CREATOR',
                                                    name : 'CREATOR',
                                                    fieldLabel : '创建人',
                                                    labelWidth : 95, // 标签宽度
                                                    labelAlign : 'right',
                                                    readOnly : true,
                                                    colspan : 2,
                                                    rowspan : 1
                                                }, {
                                                    xtype : 'textfield',
                                                    id : 'tf_CREATE_TIME',
                                                    name : 'CREAT_TIME',
                                                    fieldLabel : '创建时间',
                                                    labelWidth : 95, // 标签宽度
                                                    labelAlign : 'right',
                                                    readOnly : true,
                                                    colspan : 2,
                                                    rowspan : 1
                                                } ,{
                                                    xtype : 'textfield',
                                                    id : 'tf_OP_FLAG',
                                                    name : 'OP_FLAG',
                                                    fieldLabel : '操作类型',
                                                    labelWidth : 95, // 标签宽度
                                                    labelAlign : 'right',
                                                    readOnly : true,
                                                    colspan : 2,
                                                    rowspan : 1
                                                } ]
                                            } ],
                                            bbar : [
                                                    '->',
                                                    {
                                                        text : '审核通过',
                                                        id : 'btn_save_win',
                                                        iconCls : 'tbar_saveIcon',                                                  
                                                        handler : function() {
                                                            var pc = top.Ext
                                                                    .getCmp("merchantMaintainCheckWinView");
                                                            pc.ret = 'save';
                                                            pc.submiturl = 'merchantMaintainCheckAction!auditSuccess';
                                                            pc.keyfields = {};
                                                            var df = top.Ext
                                                                    .getCmp('detailForm');
                                                            if (df.form
                                                                    .isValid()) {
                                                                if ('save' == pc.ret) {
                                                                    var params = top.CFControlWin
                                                                            .getSubmitParamsForWindow(
                                                                                    'detailForm',
                                                                                    pc.operation);
                                                                    top.CFControlWin
                                                                            .submitAjaxRequest2(
                                                                                    pc,
                                                                                    params,
                                                                                    "");
                                                                    Ext.getCmp("btn_save_win").setDisabled('true');
                                                                    Ext.getCmp("btn_del_win").setDisabled('true');

                                                                }
                                                            } else {
                                                                top.Ext.MessageBox
                                                                        .alert(
                                                                                "提示",
                                                                                "输入有误，无法保存！");
                                                            }
                                                        }
                                                    },
                                                    {
                                                        text : '审核拒绝',
                                                        id : 'btn_del_win',
                                                        iconCls : 'tbar_cancelIcon',
                                                        handler : function() {
                                                            var pc = top.Ext
                                                                    .getCmp("merchantMaintainCheckWinView");
                                                            pc.ret = 'save';
                                                            pc.submiturl = 'merchantMaintainCheckAction!auditFail';
                                                            pc.keyfields = {};
                                                            var df = top.Ext
                                                                    .getCmp('detailForm');
                                                            if (df.form
                                                                    .isValid()) {
                                                                if ('save' == pc.ret) {
                                                                    var params = top.CFControlWin
                                                                            .getSubmitParamsForWindow(
                                                                                    'detailForm',
                                                                                    pc.operation);
                                                                    top.CFControlWin
                                                                            .submitAjaxRequest2(
                                                                                    pc,
                                                                                    params,
                                                                                    "");

                                                                }
                                                            } else {
                                                                top.Ext.MessageBox
                                                                        .alert(
                                                                                "提示",
                                                                                "输入有误，无法保存！");
                                                            }
                                                        }
                                                    }, '->' ]

                                        });
                        this.callParent(arguments);
                    }
                });
