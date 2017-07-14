var saveFlag = "";
Ext.define('CFrame.view.sys.roleMaintainView', {
    extend : 'Ext.container.Viewport',
    alias : 'widget.roleMaintainView',
    id : 'roleMaintain-panel',
    requires : [ 'CFrame.store.sys.roleStore',
                 'CFrame.view.sys.selectRoleMenuWinView'],
    layout : {
        type : 'border'
    },
    title : '角色信息维护',

    initComponent : function() {
        var teststore='';
        var test='loadone';
        var me = this;
        var sw_roleMenu;   //功能菜单选择窗体
        var roleStore = Ext.create('CFrame.store.sys.roleStore');
        roleStore.on("beforeload", function () {
            roleStore.proxy.extraParams = {};
            Ext.apply(roleStore.proxy.extraParams, CFControl.getQueryParams('queryform'));
        });
        //是否可用标志
        var dict0001= CFControl.getDictData('0001',Ext.create('CFrame.store.commonDictStore'));
        var pc=''; 
        var addMenu='';
        Ext.applyIf(me, {
            items : [ {
                xtype : 'form',
                region : 'north',
                id : 'queryform',
                height : 95,
                autoScroll : true,
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
                    id : 'tf_qry_ROLE_ID',
                    fieldLabel : '角色代码',
                    labelWidth : 95, // 标签宽度
                    width:250,
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1

                }, {
                    xtype : 'textfield',
                    id : 'tF_qry_ROLE_NAME',
                    fieldLabel : '角色名称',
                    labelWidth : 95, // 标签宽度
                    width:250,
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1
                } ],
                bbar : {
                    id : 'qbbar',
                    items : [ {
                        text : '查询',
                        iconCls : 'tbar_queryIcon',
                        id : 'btn_query',
                        handler : function() {
                            pc='query';
                            saveFlag = CFControl.type1QueryHandler(saveFlag, roleStore, 'queryform', 'roleManGrid', 'qbbar');
                        }
                    }, '-', {
                        text : '新增',
                        id : 'btn_add',
                        iconCls : 'tbar_addIcon',
                        handler : function() {
                            addMenu='';
                            pc='add';
                            saveFlag = CFControl.type1AddHandler(saveFlag, 'CFrame.model.sys.roleModel', 'detailPanel', 'roleManGrid', 'qbbar');
                            test='loadone';
                        }
                    }, '-', {
                        text : '修改',
                        id : 'btn_mod',
                        iconCls : 'tbar_modIcon',
                        handler : function() {
                            addMenu='';
                            pc='modify';
                            var ModCtlArray = [ 'tf_ROLE_ID'];
                            saveFlag = CFControl.type1ModHandler(saveFlag, 'detailPanel', 'roleManGrid', 'qbbar', ModCtlArray);
                            test='loadone';
                        }
                    }, '-', {
                        text : '删除',
                        id : 'btn_del',
                        iconCls : 'tbar_delIcon',
                        handler : function() {
                            var selectedModel = Ext.getCmp('roleManGrid').getSelectionModel().getSelection()[0];
                            if(!selectedModel){
                                Ext.Msg.alert('提示','请选择待删除的角色！');
                                return;
                            }
                            Ext.MessageBox.confirm('确认','请确认是否删除该角色？',function(e) {
                                if (e == 'yes') {
                                    CFControl.type1DelHandler(saveFlag, 'roleMaintainActionsave!delRole', 'detailPanel', 'roleManGrid');
                                }
                            });
                        }
                    }, '-', {
                        text : '保存',
                        id : 'btn_save',
                        iconCls : 'tbar_saveIcon',
                        handler : function() {
                            result=CFControl.type2SaveHandler(saveFlag, 'roleMaintainActionsave!saveRole', 'detailPanel', 'roleManGrid', 'qbbar',addMenu);
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
                            saveFlag = CFControl.type1CancelHandler(saveFlag, 'detailPanel', 'roleManGrid', 'qbbar');
                        }
                    }, '-', {
                        text : '刷新',
                        id : 'btn_reset',
                        iconCls : 'tbar_buildingIcon2',
                        handler : function() {
                            pc='';
                            CFControl.type1ResetHandler('queryform','roleManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }]
                }
            }, {
                xtype : 'gridpanel',
                height : 650,
                region : 'center',
                id : 'roleManGrid',
                store : roleStore,
                selModel : Ext.create('Ext.selection.RowModel', {
                    mode : "SINGLE"
                }),
                viewConfig:{  
                    enableTextSelection:true  
                }, 
                columns : [ {
                    xtype : 'gridcolumn',
                    dataIndex : 'ROLE_ID',
                    width : 150,
                    text : '角色代码'
                },  {
                    xtype : 'gridcolumn',
                    dataIndex : 'ROLE_NAME',
                    width : 150,
                    text : '角色名称'                   
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'DESCR',
                    width : 150,
                    text : '角色说明'                   
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'ENABLED',
                    width : 150,
                    text : '是否可用',
                    renderer: function rendererItem(value){
                        if(dict0001.getCount()==0){
                            dict0001.reload();
                        }
                        if(dict0001.getCount()==0){
                            Ext.MessageBox.alert("错误",'[是否可用]数据字典挂载失败');
                        }
                        var index = dict0001.find('DICT_VALUE',value,0,false,true,true); 
                        if(index == -1){
                            return "";
                        }
                        var record = dict0001.getAt(index).get('DICT_NAME'); 
                        return record; 
                    }   
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'CREATOR',
                    width : 150,
                    text : '创建人'        
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'CREATE_TIME',
                    width : 150,
                    text : '创建时间',
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                } , {
                    xtype : 'gridcolumn',
                    dataIndex : 'CHECK_TLRNO',
                    width : 150,
                    text : '审核人'        
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'CHECK_TIME',
                    width : 150,
                    text : '审核时间'   ,
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                }],
                bbar : {
                    xtype : 'pagingtoolbar',
                    region : 'south',
                    id : 'pageToolBar',
                    height : 30,
                    store : roleStore,
                    displayInfo : true,
                    displayMsg : '当前显示的记录 {0} - {1} 共计 {2}',
                    emptyMessage : '没有可显示的记录',
                    listeners : {
                        change:function(){
                            var dg = Ext.getCmp('roleManGrid');
                            dg.getSelectionModel().select(0, true);
                        }
                    }
                },
                listeners : {
                    select : function(record, index, eOpts) {// 使用select事件
                        var role = record.getSelection()[0];
                        CFControl.setFormReadOnly('detailPanel', true);
                        var df = Ext.getCmp('detailPanel');
                        df.loadRecord(role);
                    },
                    selectionchange : function(selModel, selections) {
                        if(Ext.getCmp('roleManGrid').getStore().getCount()==0){
                            CFControl.setFormFieldValue('detailPanel',Ext.create('CFrame.model.sys.roleModel'));;
                        }
                        if ("add" == saveFlag) {
                            var el = Ext.getCmp('btn_add');
                            if (el.disabled) {
                                roleStore.removeAt(0);
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
                    columns : 5
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
                    id : 'tf_ROLE_ID',
                    name : 'ROLE_ID',
                    fieldLabel : '角色代码',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1,
                    maxLength : 10,
                    maxLengthText : '角色代码长度不能大于10位',
                    allowBlank : false,
                    blankText : "角色代码不能为空"
                }, {
                    xtype : 'textfield',
                    id : 'tf_ROLE_NAME',
                    name : 'ROLE_NAME',
                    fieldLabel : '角色名称',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1,
                    maxLength : 10,
                    maxLengthText : '角色名称长度不能大于10位'
                }, {
                    xtype : 'textfield',
                    id : 'tf_DESCR',
                    name : 'DESCR',
                    fieldLabel : ' 角色说明',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1,
                    maxLength : 15,
                    maxLengthText : '角色说明长度不能大于15位'
                }, {
                    xtype : 'gridpanel',
                    id : 'test',
                    name : 'test',
                    fieldLabel : '是否可用',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    store:teststore,
                    hidden:true
                },{
                    xtype : 'checkbox',
                    id : 'ck_ENABLED',
                    name : 'ENABLED',
                    fieldLabel : '是否可用',
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1
                },{
                    xtype : 'button',
                    id : 'btn_set',
                    iconCls : 'tbar_buildingIcon',                  
                    text : '功能设置',
                    labelAlign : 'right',
                    width:90,
                    x:-60,
                    handler : function() {
                        if(pc=='query')
                            {
                                var rmg = Ext.getCmp('roleManGrid');
                                var model = rmg.getSelectionModel().getSelection()[0];
                                if(!model){
                                   Ext.Msg.alert('提示','请选择一条角色记录！');
                                   return;
                                }
                                var obj = new Object(); 
                                obj.code = this;
                                obj.model = model;
                                Ext.require('CFrame.view.sys.selectRoleMenuWinView');
                                sw_roleMenu = top.Ext.getCmp('selectMenuWin');
                                    Ext.apply(Ext.data.Connection.prototype, { async: false  });
                                    sw_roleMenu = top.Ext.create('CFrame.view.sys.selectRoleMenuWinView',
                                        {layout : 'fit',
                                        width : 640,
                                        height : 480,
                                        resizable : false,
                                        draggable : true,
                                        closeAction : 'destroy',
                                        modal : true,
                                        title : '功能设置',
                                        model : obj.model,
                                        });
                                    sw_roleMenu.show(obj.parent);
                                    var treeMenu = sw_roleMenu.child('#roleMenuForm').child('#menuTreePanel');
                                    treeMenu.expandAll();
                                sw_roleMenu.ret = '';
                                sw_roleMenu.show(obj.parent);
                                
                    }
                        if(pc=='add')
                        {
                                Ext.require('CFrame.view.sys.selectRoleAddMenuWinView');
                                sw_roleMenu = top.Ext.getCmp('selectMenuAddWin');
                                Ext.apply(Ext.data.Connection.prototype, { async: false  });
                                sw_roleMenu = top.Ext.create('CFrame.view.sys.selectRoleAddMenuWinView',
                                    {layout : 'fit',
                                    width : 640,
                                    height : 480,
                                    resizable : false,
                                    draggable : true,
                                    closeAction : 'destroy',
                                    modal : true,
                                    title : '功能设置',
                                    store : Ext.getCmp('test').store,
                                    test : test
                                    });
                                var treeMenu = sw_roleMenu.child('#roleMenuForm').child('#menuTreePanel');
                                treeMenu.expandAll();
                            sw_roleMenu.ret = '';
                            sw_roleMenu.show();
                            sw_roleMenu.on('close',fn =function(){
                            if(sw_roleMenu.ret=='confirm')
                            {   
                                addMenu=sw_roleMenu.records;
                                Ext.getCmp('test').store=sw_roleMenu.child('#roleMenuForm').child('#menuTreePanel').store;
                                test='loadtwo';
                            }
                                sw_roleMenu.un('close',fn);
                             });
                        }
                        if(pc=='modify')
                        {
                            var rmg = Ext.getCmp('roleManGrid');
                            var model = rmg.getSelectionModel().getSelection()[0];
                            if(!model){
                               Ext.Msg.alert('提示','请选择一条角色记录！');
                               return;
                            }
                            var obj = new Object(); 
                            obj.code = this;
                            obj.model = model;
                            Ext.require('CFrame.view.sys.selectRoleMenuModifyWinView');
                            sw_roleMenu = top.Ext.getCmp('selectMenuWin');
                                Ext.apply(Ext.data.Connection.prototype, { async: false  });
                                sw_roleMenu = top.Ext.create('CFrame.view.sys.selectRoleMenuModifyWinView',
                                    {layout : 'fit',
                                    width : 640,
                                    height : 480,
                                    resizable : false,
                                    draggable : true,
                                    closeAction : 'destroy',
                                    modal : true,
                                    title : '功能设置',
                                    model : obj.model,
                                    store : Ext.getCmp('test').store,
                                    test : test
                                    });
                                sw_roleMenu.show(obj.parent);
                                var treeMenu = sw_roleMenu.child('#roleMenuForm').child('#menuTreePanel');
                                treeMenu.expandAll();
                                sw_roleMenu.ret = '';
                                sw_roleMenu.show(obj.parent);
                                sw_roleMenu.on('close',fn =function(){
                                if(sw_roleMenu.ret=='confirm')
                                {
                                    addMenu=sw_roleMenu.records;
                                    Ext.getCmp('test').store=sw_roleMenu.child('#roleMenuForm').child('#menuTreePanel').store;
                                    test='loadtwo';
                                }
                                    sw_roleMenu.un('close',fn);
                                 });
                        }
                    }
                }  ]
            } ]
        });
        me.callParent(arguments);
    }
});
function PageRefresh() {
    CFControl.setFormReadOnly('detailPanel', true);
    CFControl.setToolBarStatus('qbbar', true);
    var dg = Ext.getCmp('roleManGrid');
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
    var win = Exmot.create('CFrame.view.sys.roleMaintainView');
    win.show();
    addValidation();
    CFControl.setFormReadOnly('detailPanel', true);
    CFControl.setToolBarStatus('qbbar', true);
    var dg = Ext.getCmp('roleManGrid');
    var pg = Ext.getCmp('pageToolBar');
    CFControl.setRefreshHandelerForPagebar('pageToolBar', PageRefresh);
//  dg.getStore().load({
//      params : {
//          start : 0
//      },
//      callback : function(r, options, success) {
//          if (success) {
//              dg.getSelectionModel().select(0, true);
//          }
//          ;
//      }
//  });
});
