var saveFlag = "";
Ext.define('CFrame.view.zz.merchantMaintainCheckView', {
    extend : 'Ext.container.Viewport',
    alias: 'widzz.merchantMaintainCheckView',  
    id : 'merchantMaintainCheck-panel',
    requires : [ 'CFrame.store.zz.merchantCheckStore',
                 'CFrame.store.commonDictStore'],
    layout : {
        type : 'border'
    },
    title : '角色信息审核',
    model :{},
    ret :'',
    initComponent : function() {
        
        var me = this;
        var merchantCheckStore = Ext.create('CFrame.store.zz.merchantCheckStore');
        merchantCheckStore.on("beforeload", function () {
            merchantCheckStore.proxy.extraParams={};
            Ext.apply(merchantCheckStore.proxy.extraParams, CFControl.getQueryParams('queryform'));
        });
        //是否可用标志
        var dict0001= CFControl.getDictData('0001',Ext.create('CFrame.store.commonDictStore'));
        
        Ext.applyIf(me, {
            items : [ {
                xtype : 'form',
                region : 'north',
                id: 'queryform',
                autoScroll: true,
                height : 95,
                layout : {
                    columns : 4,
                    type : 'table'
                },
                bodyPadding : 10,
                title : '查询条件',
                defaults:{
                     width:260
                    },
                    items : [ {
                        xtype : 'textfield',
                        id : 'mc_qry_MERCHANT_ID',
                        fieldLabel : '合作机构编号',
                        labelWidth : 95, // 标签宽度
                        width:250,
                        labelAlign : 'right',
                        colspan : 1,
                        rowspan : 1

                    }, {
                        xtype : 'textfield',
                        id : 'mc_qry_MERCHANT_NAME',
                        fieldLabel : '合作机构名称',
                        labelWidth : 95, // 标签宽度
                        width:250,
                        labelAlign : 'right',
                        colspan : 1,
                        rowspan : 1
                    } ],
                    bbar : {
                        id:'qbbar',
                        items:[ {
                        text : '查询',
                        iconCls : 'tbar_queryIcon',
                        id: 'btn_query',
                        
                        handler : function() {
                            var from = Ext.getCmp('queryform');
                            if(from.isValid()){
                                saveFlag = CFControl.type1QueryHandler( saveFlag,merchantCheckStore, 'queryform', 'merchantManGrid', 'qbbar');  
                            }else{
                                Ext.MessageBox.alert('提示','您输入的参数不正确！');
                                return;
                            }
                            }
                    }, '-', {
                        text: '审核',
                        iconCls: 'tbar_modIcon',
                        id: 'btn_check',
                        handler : function(){
                            //必须选中一条未审核的角色
                            var auditModel = Ext.getCmp('merchantManGrid').getSelectionModel().getSelection()[0];
                            if(!auditModel){
                                Ext.MessageBox.alert('提示','请选择一条数据进行审核！');
                                return;
                            }
                            detailWin = top.Ext.getCmp('merchantMaintainCheckWinView');
                            if(!detailWin){
                                detailWin =top.Ext.create('CFrame.view.zz.merchantMaintainCheckWinView',{
                                    modal : true,
                                    operation:'add'
                                });
                            }    
                            var tf_MERCHANT_ID = detailWin.child('#detailForm').child('#tf_MERCHANT_ID');
                            tf_MERCHANT_ID.setValue(auditModel.get('MERCHANT_ID')); 
                            var tF_MERCHANT_NAME = detailWin.child('#detailForm').child('#tF_MERCHANT_NAME');
                            tF_MERCHANT_NAME.setValue(auditModel.get('MERCHANT_NAME')); 
        
                            var tf_CREATOR = detailWin.child('#detailForm').child('#tf_CREATOR');
                            tf_CREATOR.setValue(auditModel.get('CREATOR')); 
                            var tf_CREATE_TIME = detailWin.child('#detailForm').child('#tf_CREATE_TIME');
                            tf_CREATE_TIME.setValue(CFControl.formatDate(auditModel.get('CREAT_TIME'))); 
                            var tf_OP_FLAG = detailWin.child('#detailForm').child('#tf_OP_FLAG');
                            tf_OP_FLAG.setValue(auditModel.get('OP_FLAG')); 
                            detailWin.show();
                            detailWin.on('close',fn =function(){                                  
                                CFControl.type1RefreshHandler(merchantCheckStore,'queryform','merchantManGrid','qbbar');
                                detailWin.un('close',fn);
                                
                            });
                        }
                    },'-', {
                        text: '刷新',
                        iconCls: 'tbar_buildingIcon2',
                        id: 'btn_reset',
                        handler : function(){
                            CFControl.type1ResetHandler('queryform','merchantManGrid','qbbar','pageToolBar',null);
                            saveFlag = CFControl.type1QueryHandler( saveFlag,merchantCheckStore, 'queryform', 'merchantManGrid', 'qbbar');
                        }
                    }]
                }
            }, {
                xtype : 'gridpanel',
                height : 650,
                region : 'center',
                id : 'merchantManGrid',
                store : merchantCheckStore,
                selModel : Ext.create('Ext.selection.RowModel', {
                    mode : "SINGLE"
                }),
                viewConfig:{  
                    enableTextSelection:true  
                }, 
                columns : [ {
                    xtype : 'gridcolumn',
                    dataIndex : 'MERCHANT_ID',
                    width : 150,
                    text : '合作机构代码'
                },  {
                    xtype : 'gridcolumn',
                    dataIndex : 'MERCHANT_NAME',
                    width : 150,
                    text : '合作机构名称'                   
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'CREATOR',
                    width : 150,
                    text : '创建人'        
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'CREAT_TIME',
                    width : 150,
                    text : '创建时间',
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                },{
                    xtype : 'gridcolumn',
                    dataIndex : 'OP_FLAG',
                    width : 150,
                    text : '操作类型'       
                }],
                bbar : {
                    xtype : 'pagingtoolbar',
                    region : 'south',
                    id : 'pageToolBar',
                    height : 30,
                    store : merchantCheckStore,
                    displayInfo : true,
                    displayMsg : '当前显示的记录 {0} - {1} 共计 {2}',
                    emptyMessage : '没有可显示的记录',
                    listeners : {
                        change:function(){
                            var dg = Ext.getCmp('merchantManGrid');
                            dg.getSelectionModel().select(0, true);
                        }
                    }
                },
                listeners : {
                    select : function(record, index, eOpts) {// 使用select事件
                        var dept = record.getSelection()[0];
                        CFControl.setFormReadOnly('detailPanel', true);
                        var df = Ext.getCmp('detailPanel');
                        df.loadRecord(dept);
                    },
                    selectionchange : function(selModel, selections) {
                        if(Ext.getCmp('merchantManGrid').getStore().getCount()==0){
                            CFControl.setFormFieldValue('detailPanel',Ext.create('CFrame.model.zz.tbMerchantDetail'));;
                        }
                        if ("add" == saveFlag) {
                            var el = Ext.getCmp('btn_add');
                            if (el.disabled) {
                                merchantCheckStore.removeAt(0);
                                selModel.select(0, true);
                            }
                        }
                        CFControl.setFormReadOnly('detailPanel', true);
                        CFControl.setToolBarStatus('qbbar', true);
                        saveFlag = "";
                    }
                }
            }, {
                xtype : 'form',
                region : 'south',
                dock : 'bottom',
                height : 100,
                autoScroll : true,
                layout : {
                    type : 'table',
                    columns : 4
                },
                bodyPadding : 10,
                id : 'detailPanel',
                title : '明细',
                defaults:{
                 width:260
                },
                items : [
                {
                xtype : 'textfield',
                id : 'tf_MERCHANT_ID',
                name : 'MERCHANT_ID',
                fieldLabel : '合作机构编号',
                labelWidth : 95, // 标签宽度
                labelAlign : 'right',
                colspan : 1,
                rowspan : 1,
                maxLength : 20,
                maxLengthText : '合作机构编号不能大于20位',
                allowBlank : false,
                blankText : "合作机构编号不能为空"
                }, {
                xtype : 'textfield',
                id : 'tf_MERCHANT_NAME',
                name : 'MERCHANT_NAME',
                fieldLabel : '合作机构名称',
                labelWidth : 95, // 标签宽度
                labelAlign : 'right',
                colspan : 1,
                rowspan : 1,
                maxLength : 100,
                maxLengthText : '合作机构名称不能大于100位'
                }  ]
                } ]
                });
                me.callParent(arguments);
    }
});
function PageRefresh() {
    CFControl.setFormReadOnly('detailPanel', true);
    CFControl.setToolBarStatus('qbbar', true);
    var dg = Ext.getCmp('merchantManGrid');
    if("add" == saveFlag){
        dg.getSelectionModel().select(1,true);
        if(dg.store.getCount()==1){
            dg.store.removeAt(0);
        }
    }
    dg.getSelectionModel().select(0, true);
};

/**
 * 自定义校验类示例
 */
/*Ext.apply(Ext.form.field.VTypes, {
    checkRoleCode: function(str){
        return CFCheck.cheRoletyCode(str);
    },
    checkRoleCodeText: "国家或地区代码不符合要求"
});*/

/**
 * 挂载校验
 */
function addValidation() {

};

Ext.onReady(function() {
    var win = Ext.create('CFrame.view.zz.merchantMaintainCheckView');
    win.show();
    addValidation();
    CFControl.setFormReadOnly('detailPanel', true);
    CFControl.setToolBarStatus('qbbar', true);
    var dg = Ext.getCmp('merchantManGrid');
    var pg = Ext.getCmp('pageToolBar');
    CFControl.setRefreshHandelerForPagebar('pageToolBar', PageRefresh);
/*  dg.getStore().load({
        params : {
            start : 0
        },
        callback : function(r, options, success) {
            if (success) {
            dg.getSelectionModel().select(0, true);
            }
            ;
        }
    });*/
});
