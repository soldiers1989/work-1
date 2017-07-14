var saveFlag = "";
Ext.define('CFrame.view.zz.merchantView', {
    extend : 'Ext.container.Viewport',
    alias: 'widget.merchantInfoView',  
    id : 'merchantInfo-panel',
    requires : [ 'CFrame.store.zz.managementStore3',
                 'CFrame.store.commonDictStore'],
    layout : {
        type : 'border'
    },  
    title : '合作机构',
    
    initComponent : function() {
        
        var me = this;
        var merchantStore = Ext.create('CFrame.store.zz.managementStore3');
        merchantStore.on("beforeload", function () {
            merchantStore.proxy.extraParams = {};
            Ext.apply(merchantStore.proxy.extraParams, CFControl.getQueryParams('queryform'));
        });
        Ext.applyIf(me, {
            items : [ {
                xtype : 'form',
                region : 'north',
                id: 'queryform',
                height : 100,
                layout : {
                    columns : 4,
                    type : 'table'
                },
                bodyPadding : 10,
                title : '查询条件',
                items: [
                    {
                        xtype: 'textfield',
                        id: 'mc_qry_MERCHANT_ID',
                        fieldLabel: '合作机构编号',
                        labelWidth : 95, // 标签宽度
                        width:250,
                        labelAlign:'right',
                        colspan:1,
                        rowspan:1
                    },
                    {
                        xtype: 'textfield',
                        id: 'mc_qry_MERCHANT_NAME',
                        fieldLabel: '合作机构名称',
                        labelWidth : 95, // 标签宽度
                        width:250,
                        labelAlign:'right',
                        colspan:1,
                        rowspan:1
                    }
                    ],
                    bbar : {
                        id:'qbbar',
                        items:[ {
                        text : '查询',
                        iconCls : 'tbar_queryIcon',
                        id: 'btn_query',
                        handler : function() {
                            saveFlag = CFControl.type1QueryHandler(saveFlag,merchantStore,'queryform','merchantManGrid','qbbar');
                        }
                    }, '-', {
                        text : '新增',
                        id : 'btn_add',
                        iconCls : 'tbar_addIcon',
                        handler : function() {
                            addMenu='';
                            pc='add';
                            saveFlag = CFControl.type1AddHandler(saveFlag, 'CFrame.model.zz.tbMerchantDetail', 'detailPanel', 'merchantManGrid', 'qbbar');
                            test='loadone';
                        }
                    }, '-', {
                        text : '修改',
                        id : 'btn_mod',
                        iconCls : 'tbar_modIcon',
                        handler : function() {
                            addMenu='';
                            pc='modify';
                            var ModCtlArray = [ 'tf_MERCHANT_ID'];
                            saveFlag = CFControl.type1ModHandler(saveFlag, 'detailPanel', 'merchantManGrid', 'qbbar', ModCtlArray);
                            test='loadone';
                        }
                    }, '-', {
                        text : '保存',
                        id : 'btn_save',
                        iconCls : 'tbar_saveIcon',
                        handler : function() {
                            result=CFControl.type2SaveHandler(saveFlag, 'merchantActionsave!saveMerchant', 'detailPanel', 'roleManGrid', 'qbbar',addMenu);
                            if(result!='-1')
                            {
                                pc='query';
                            }
                        }
                    }, '-', {
                        text : '取消',
                        id : 'btn_cancel',
                        iconCls : 'tbar_cancelIcon',
                        handler : function() {
                            pc='query';
                            var el = Ext.getCmp('btn_add');
                            if (!el.disabled) {
                                saveFlag = "";
                            }
                            saveFlag = CFControl.type1CancelHandler(saveFlag, 'detailPanel', 'merchantManGrid', 'qbbar');
                        }
                    }, '-', {
                        text : '删除',
                        id : 'btn_del',
                        iconCls : 'tbar_delIcon',
                        handler : function() {
                            var selectedModel = Ext.getCmp('merchantManGrid').getSelectionModel().getSelection()[0];
                            if(!selectedModel){
                                Ext.Msg.alert('提示','请选择待删除的合作机构！');
                                return;
                            }
                            Ext.MessageBox.confirm('确认','请确认是否删除该合作机构？',function(e) {
                                if (e == 'yes') {
                                    CFControl.type1DelHandler(saveFlag, 'merchantActionsave!delMerchant', 'detailPanel', 'merchantManGrid');
                                }
                            });
                        }
                    }, '-', {
                        text : '重置',
                        id : 'btn_reset',
                        iconCls : 'tbar_buildingIcon2',
                        handler : function() {
                            CFControl.type1ResetHandler('queryform','merchantManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }]
                    }
            }, {
                xtype : 'gridpanel',
                height : 650,
                region : 'center',
                id : 'merchantManGrid',
                store : merchantStore,
                selModel: Ext.create('Ext.selection.RowModel',{mode:"SINGLE"}),
                viewConfig:{  
                    enableTextSelection:true  
                }, 
                columns : [{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'MERCHANT_ID',
                    width : 100,
                    text : '合作机构编号'     
                },{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'MERCHANT_NAME',
                    width: 200,
                    text : '合作机构名称'
                },{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'CREAT_TIME',
                    width: 150,
                    text : '创建时间',
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                },{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'CREATOR',
                    width: 100,
                    text : '创建者'
                },{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'CHECK_TLRNO',
                    width: 100,
                    text : '审核者'
                },{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'CHECK_TIME',
                    width: 150,
                    text : '审核时间',
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                }],
                bbar:{
                    xtype : 'pagingtoolbar',
                    region : 'south',
                    id : 'pageToolBar',
                    height : 30,
                    store: merchantStore,
                    displayInfo : true,
                    displayMsg: '当前显示的记录 {0} - {1} 共计 {2}',
                    emptyMessage : '没有可显示的记录',
                    listeners : {
                        change:function(){
                            var dg = Ext.getCmp('merchantManGrid');
                            dg.getSelectionModel().select(0, true);
                        }
                    }
                },
                listeners: {
                     select : function( record, index, eOpts ){//使用select事件
                        var dict = record.getSelection()[0]; 
                        CFControl.setFormReadOnly('detailPanel',true);
                        var df = Ext.getCmp('detailPanel');
                        df.loadRecord(dict);
                    },
                    selectionchange: function(selModel, selections){
                        if(Ext.getCmp('merchantManGrid').getStore().getCount()==0){
                            CFControl.setFormFieldValue('detailPanel',Ext.create('CFrame.model.sys.dictModel'));
                        }
                        if("add" == saveFlag){
                            var el = Ext.getCmp('btn_add');
                            if(el.disabled){
                                merchantStore.removeAt(0);
                                selModel.select(0,true);
                            }
                        }
                        CFControl.setFormReadOnly('detailPanel',true);
                        CFControl.setToolBarStatus('qbbar',true);
                        saveFlag = "";
                    }
                }
            },
            {
                xtype : 'form',
                region : 'south',
                dock : 'bottom',
                height : 100,
                autoScroll : true,
                layout : {
                    type : 'table',
                    columns:4
                },
                bodyPadding : 10,
                id : 'detailPanel',
                title : '明细',
                    items: [
                            {
                                xtype: 'textfield',
                                id: 'tf_MERCHANT_ID',
                                name: 'MERCHANT_ID',
                                fieldLabel: '合作机构编号',
                                labelWidth : 95, // 标签宽度
                                labelAlign:'right',
                                colspan:1,
                                rowspan:1,
                                readOnly:false,
                                allowBlank : false,
                                blankText : "合作机构编号不能为空",
                                maxLength : 20,
                                maxLengthText : '合作机构编号不能大于20位'
                            },
                            {
                                xtype: 'textfield',
                                id: 'tf_MERCHANT_NAME',
                                name: 'MERCHANT_NAME',
                                labelAlign:'right',
                                fieldLabel: '合作机构名称',
                                labelWidth : 95, // 标签宽度
                                colspan:1,
                                rowspan:1,
                                allowBlank : false,
                                blankText : "合作机构名称不能为空",
                                maxLength : 100,
                                maxLengthText : '合作机构名称不能大于100位'
                            }
                           
                        ]
            }]
        });
        me.callParent(arguments);
    }
});
function PageRefresh(){
     CFControl.setFormReadOnly('detailPanel',true);
     CFControl.setToolBarStatus('qbbar',true);
     var dg = Ext.getCmp('merchantManGrid');
     if("add" == saveFlag){
            dg.getSelectionModel().select(1,true);
            if(dg.store.getCount()==1){
                dg.store.removeAt(0);
            }
        }
     dg.getSelectionModel().select(0,true);
     
};

/**
 * 自定义校验类示例
 */
Ext.apply(Ext.form.field.VTypes, {
    checkNull: function(str){
        return !CFCheck.checkNull(str);
    },
    checkNullText: "组号不能为空"
});

/**
 * 挂载校验
 */
function addValidation(){
};
Ext.require('CFrame.store.commonDictStore');
Ext.onReady(function() {
    Ext.require('CFrame.store.commonDictStore');

    var win = Ext.create('CFrame.view.zz.merchantView');
    win.show();
    addValidation();
    CFControl.setFormReadOnly('detailPanel',true);
    CFControl.setToolBarStatus('qbbar',true);
    var dg = Ext.getCmp('merchantManGrid');
    var pg = Ext.getCmp('pageToolBar');
    CFControl.setRefreshHandelerForPagebar('pageToolBar',PageRefresh);
});