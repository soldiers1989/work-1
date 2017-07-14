var saveFlag = "";
Ext.define('CFrame.view.zz.merchantView', {
    extend : 'Ext.container.Viewport',
    alias: 'widget.equipInfoView',  
    id : 'equipInfo-panel',
    requires : [ 'CFrame.store.zz.managementStore3',
                 'CFrame.store.commonDictStore'],
    layout : {
        type : 'border'
    },  
    title : '合作机构',
    
    initComponent : function() {
        
        var me = this;
        var equipStore = Ext.create('CFrame.store.zz.managementStore3');
        equipStore.on("beforeload", function () {
            equipStore.proxy.extraParams = {};
            Ext.apply(equipStore.proxy.extraParams, CFControl.getQueryParams('queryform'));
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
                            saveFlag = CFControl.type1QueryHandler(saveFlag,equipStore,'queryform','equipManGrid','qbbar');
                        }
                    }, '-', {
                        text : '新增',
                        id : 'btn_add',
                        iconCls : 'tbar_addIcon',
                        handler : function() {
                            CFControl.type1ResetHandler('queryform','equipManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }, '-', {
                        text : '修改',
                        id : 'btn_mod',
                        iconCls : 'tbar_modIcon',
                        handler : function() {
                            CFControl.type1ResetHandler('queryform','equipManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }, '-', {
                        text : '删除',
                        id : 'btn_del',
                        iconCls : 'tbar_delIcon',
                        handler : function() {
                            CFControl.type1ResetHandler('queryform','equipManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }, '-', {
                        text : '重置',
                        id : 'btn_reset',
                        iconCls : 'tbar_buildingIcon2',
                        handler : function() {
                            CFControl.type1ResetHandler('queryform','equipManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }]
                    }
            }, {
                xtype : 'gridpanel',
                height : 650,
                region : 'center',
                id : 'equipManGrid',
                store : equipStore,
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
                    text : '创建时间'
                },{
                    align : 'center',
                    xtype : 'gridcolumn',
                    dataIndex : 'CREATOR',
                    width: 100,
                    text : '创建者'
                }],
                bbar:{
                    xtype : 'pagingtoolbar',
                    region : 'south',
                    id : 'pageToolBar',
                    height : 30,
                    store: equipStore,
                    displayInfo : true,
                    displayMsg: '当前显示的记录 {0} - {1} 共计 {2}',
                    emptyMessage : '没有可显示的记录',
                    listeners : {
                        change:function(){
                            var dg = Ext.getCmp('equipManGrid');
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
                        if(Ext.getCmp('equipManGrid').getStore().getCount()==0){
                            CFControl.setFormFieldValue('detailPanel',Ext.create('CFrame.model.sys.dictModel'));
                        }
                        if("add" == saveFlag){
                            var el = Ext.getCmp('btn_add');
                            if(el.disabled){
                                equipStore.removeAt(0);
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
                height : 0,
//              autoScroll : true,
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
                                id: 'tf_EQUIP_NO',
                                name: 'MERCHANT_ID',
                                fieldLabel: '合作机构编号',
                                labelWidth : 95, // 标签宽度
                                labelAlign:'right',
                                colspan:1,
                                rowspan:1,
                                readOnly:false,
                                allowBlank : false,
                                blankText : "合作机构编号",
                                maxLength : 100,
                                maxLengthText : '合作机构编号不能大于20位'
                            },
                            {
                                xtype: 'textfield',
                                id: 'tf_EQUIP_MODEL',
                                name: 'MERCHANT_NAME',
                                labelAlign:'right',
                                fieldLabel: '合作机构名称',
                                labelWidth : 95, // 标签宽度
                                colspan:1,
                                rowspan:1,
                                allowBlank : false,
                                blankText : "合作机构名称不能为空",
                                maxLength : 30,
                                maxLengthText : '合作机构名称不能大于30位'
                            },
                            {
                                xtype: 'textfield',
                                id: 'cb_ASSETS_NO',
                                name: 'CREAT_TIME',
                                fieldLabel: '创建时间',
                                labelAlign:'right',
                                labelWidth : 95, // 标签宽度
                                colspan:1,
                                rowspan:1,
                                allowBlank : false,
                                blankText : "创建时间不能为空",
                                maxLength : 10,
                                maxLengthText : '创建时间不能大于10位'
                            },
                            {
                                xtype: 'textfield',
                                id: 'tf_PRICE',
                                name: 'CREATOR',
                                fieldLabel: '创建人',
                                labelAlign:'right',
                                labelWidth : 95, // 标签宽度
                                colspan:1,
                                rowspan:1,
                                allowBlank : false,
                                blankText : "创建人不能为空",
                                maxLength : 100,
                                maxLengthText : '创建人不能大于100位'
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
     var dg = Ext.getCmp('equipManGrid');
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
    CFControl.isShowButton("qbbar","equipInfo");
    var dg = Ext.getCmp('equipManGrid');
    var pg = Ext.getCmp('pageToolBar');
    CFControl.setRefreshHandelerForPagebar('pageToolBar',PageRefresh);
});


