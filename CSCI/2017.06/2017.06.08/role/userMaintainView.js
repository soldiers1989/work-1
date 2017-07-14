var saveFlag = "";
Ext.define('CFrame.view.sys.userMaintainView', {
    extend : 'Ext.container.Viewport',
    alias : 'widget.userMaintainView',
    id : 'userMaintain-panel',
    requires : [ 'CFrame.store.sys.userStore',
                 'CFrame.view.sys.selectUserRoleWinView'],
    layout : {
        type : 'border'
    },
    title : '用户信息维护',

    initComponent : function() {
        var me = this;
        var teststore='';
        var test='loadone';
        var pc=''; 
        var sw_userrole;   //角色设置选择窗体
        var userStore = Ext.create('CFrame.store.sys.userStore');
        userStore.on("beforeload", function () {
            userStore.proxy.extraParams = {};
            Ext.apply(userStore.proxy.extraParams, CFControl.getQueryParams('queryform'));
        });
        //管理员状态
        var dict0002= CFControl.getDictData('0002',Ext.create('CFrame.store.commonDictStore'));
        var dictRole= Ext.create('CFrame.store.sys.roleStore');
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
                    id : 'tf_qry_USER_ID',
                    fieldLabel : '用户代码',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1

                }, {
                    xtype : 'textfield',
                    id : 'tf_qry_USER_NAME',
                    fieldLabel : '用户名称',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1
                }, {
                    xtype : 'combo',
                    id : 'tf_qry_STATUS',
                    fieldLabel : '用户状态',
                    labelWidth : 95, // 标签宽度
                    labelAlign:'right',
                    colspan:1,
                    rowspan:1,
                    store: dict0002, 
                    displayField : 'DICT_NAME',   
                    valueField : 'DICT_VALUE',
                    queryMode:'local',
                    width:250,
                    forceSelection:true,
                    listeners:{  
                       afterrender:function(cb){  
//                        if(dict0002&&dict0002.getAt(0)){
//                              cb.setRawValue(dict0002.getAt(0).get('DICT_NAME'));
//                              cb.setValue(dict0002.getAt(0).get('DICT_VALUE'));
//                        }
                       },
                       change : function(field,newValue,oldValue){
                           if(newValue!=null&& Ext.String.trim(newValue)==""){
                               field.setValue("");
                           }
                       }
                    }  
                }, {
                    xtype : 'combo',
                    id : 'cb_qry_ROLE_ID',
                    fieldLabel : '角色名称',
                    labelWidth : 95, // 标签宽度
                    labelAlign:'right',
                    colspan:1,
                    rowspan:1,
                    store: dictRole, 
                    displayField : 'ROLE_NAME',   
                    valueField : 'ROLE_ID',
                    queryMode:'remote',
                    queryParam:false,
                    pageSize: 10,
                    matchFieldWidth:false,
                    width:250,
                    forceSelection:true,
                    listeners:{  
                        afterrender:function(cb){  
//                        if(dictRole&&dictRole.getAt(0)){
//                               cb.setRawValue(dictRole.getAt(0).get('ROLE_NAME'));  
//                               cb.setValue(dictRole.getAt(0).get('ROLE_ID'));
//                        }
                        },
                        change : function(field,newValue,oldValue){
                            if(newValue!=null&& Ext.String.trim(newValue)==""){
                                field.setValue("");
                            }
                        },
                        expand:function(cb){
                            cb.getPicker().setWidth(260);
                        }
                     }  
                } ],
                bbar : {
                    id : 'qbbar',
                    items : [ {
                        text : '查询',
                        iconCls : 'tbar_queryIcon',
                        id : 'btn_query',
                        handler : function() {
                            pc='query';
                            saveFlag = CFControl.type1QueryHandler(saveFlag, userStore, 'queryform', 'userManGrid', 'qbbar');
                        }
                    }, '-', {
                        text : '新增',
                        id : 'btn_add',
                        iconCls : 'tbar_addIcon',
                        handler : function() {
                            addMenu='';
                            pc='add';
                            saveFlag = CFControl.type1AddHandler(saveFlag, 'CFrame.model.sys.userModel', 'detailPanel', 'userManGrid', 'qbbar');
                            test='loadone';                     
                        }
                    }, '-', {
                        text : '修改',
                        id : 'btn_mod',
                        iconCls : 'tbar_modIcon',
                        handler : function() {
                            addMenu='';
                            pc='modify';
                            var ModCtlArray = [ 'tf_USER_ID'];
                            saveFlag = CFControl.type1ModHandler(saveFlag, 'detailPanel', 'userManGrid', 'qbbar', ModCtlArray);
                            Ext.getCmp('tf_PASSWD').setVisible(false);
                            test='loadone';
                        }
                    }, '-', {
                        text : '保存',
                        id : 'btn_save',
                        iconCls : 'tbar_saveIcon',
                        handler : function() {
                            result=CFControl.type3SaveHandler(saveFlag, 'userMaintainActionsave!saveUser', 'detailPanel', 'userManGrid', 'qbbar',addMenu);
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
                            saveFlag = CFControl.type1CancelHandler(saveFlag, 'detailPanel', 'userManGrid', 'qbbar');
                        }
                    }   ,'-', {
                        text : '密码重置',
                        id : 'btn_resetpass',
                        iconCls : 'tbar_buildingIcon1',
                        handler : function() {
                            var rmg = Ext.getCmp('userManGrid');
                            var model = rmg.getSelectionModel().getSelection()[0];
                            if(!model){
                               Ext.Msg.alert('提示','请选择一个需要重置密码的用户！');
                               return;
                            }
                            Ext.MessageBox.confirm('确认','请确认是否重置用户密码？',function(e) {
                                if (e == 'yes') {
                                    //CFControl.submitAjaxRequestCommon('userMaintainAction!resetPassword', CFControl.getSubmitParams('detailPanel','resetPasswd'));
                                    Ext.Ajax.request({
                                         url:'userMaintainAction!resetPassword',
                                         method:'POST',
                                         params:CFControl.getSubmitParams('detailPanel','resetPasswd'), // 提交参数
                                         success: function(response, options){
                                             var obj = Ext.JSON.decode(response.responseText);
                                             if(obj.success){
                                                 Ext.getCmp('tf_PASSWD').setValue(obj.storeList[0].PASSWORD);
                                                 Ext.getCmp('tf_PASSWD').show();
                                                 Ext.MessageBox.alert("提示", obj.retMessage);
                                             }else{
                                                 Ext.MessageBox.alert("提示", obj.retMessage);
                                             }
                                         }, 
                                         failure:function(response, options){
                                             var obj = Ext.JSON.decode(response.responseText);
                                             Ext.MessageBox.alert("错误", obj.retMessage);
                                         }
                                     });
                                }
                            });
                        }
                    },'-', {
                        text : '用户激活',
                        id : 'btn_setactive',
                        iconCls : 'tbar_buildingIcon2',
                        handler : function() {
                            var rmg = Ext.getCmp('userManGrid');
                            var model = rmg.getSelectionModel().getSelection()[0];
                            if(!model){
                               Ext.Msg.alert('提示','请选择一个需要激活的用户！');
                               return;
                            }
                            Ext.MessageBox.confirm('确认','请确认是否激活用户？',function(e) {
                                if (e == 'yes') {
                                    CFControl.submitAjaxRequestCommon('userMaintainActionsave!activeUser', CFControl.getSubmitParams('detailPanel','userActive'));
                                    CFControl.type1RefreshHandler(userStore, 'queryform', 'userManGrid', 'qbbar');
                                }
                            });
                        }
                    },'-', {
                        text : '用户注销',
                        id : 'btn_del',
                        iconCls : 'tbar_delIcon',
                        handler : function() {
                            var rmg = Ext.getCmp('userManGrid');
                            var model = rmg.getSelectionModel().getSelection()[0];
                            if(!model){
                               Ext.Msg.alert('提示','请选择一个需要注销的用户！');
                               return;
                            }
                            Ext.MessageBox.confirm('确认','请确认是否注销用户？',function(e) {
                                if (e == 'yes') {
                                    CFControl.submitAjaxRequestCommon('userMaintainActionsave!delUser', CFControl.getSubmitParams('detailPanel','userCancel'));
                                    CFControl.type1RefreshHandler(userStore, 'queryform', 'userManGrid', 'qbbar');
                                }
                            });
                        }
                    }, '-', {
                        text : '刷新',
                        id : 'btn_reset',
                        iconCls : 'tbar_buildingIcon2',
                        handler : function() {
                            CFControl.type1ResetHandler('queryform','userManGrid', 'qbbar','pageToolBar','detailPanel');
                        }
                    }]
                }
            }, {
                xtype : 'gridpanel',
                height : 650,
                region : 'center',
                id : 'userManGrid',
                store : userStore,
                selModel : Ext.create('Ext.selection.RowModel', {
                    mode : "SINGLE"
                }),
                viewConfig:{  
                    enableTextSelection:true  
                }, 
                columns : [ {
                    xtype : 'gridcolumn',
                    dataIndex : 'USER_ID',
                    width : 150,
                    text : '用户代码'
                },  {
                    xtype : 'gridcolumn',
                    dataIndex : 'USER_NAME',
                    width : 150,
                    text : '用户名称'                   
                },{
                    xtype : 'gridcolumn',
                    dataIndex : 'STATUS',
                    width : 150,
                    text : '用户状态',
                    renderer: function rendererItem(value){
                        dict0002.clearFilter();
                        if(dict0002.getCount()==0){
                            dict0002.reload();
                        }
                        if(dict0002.getCount()==0){
                            Ext.MessageBox.alert("错误",'[用户状态]数据字典挂载失败');
                        }
                        var index = dict0002.find('DICT_VALUE',value,0,false,true,true); 
                        if(index == -1){
                            return "";
                        }
                        var record = dict0002.getAt(index).get('DICT_NAME'); 
                        
                        return record; 
                    }   
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'LAST_DATE',
                    width : 150,
                    text : '最新登录时间',
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                },{
                    xtype : 'gridcolumn',
                    dataIndex : 'LAST_IP',
                    width : 150,
                    text : '最新登录IP'     
                },{
                    xtype : 'gridcolumn',
                    dataIndex : 'WRONG_PWD_COUNT',
                    width : 150,
                    text : '登录失败次数',
                    hidden: true
                },{
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
                }, {
                    xtype : 'gridcolumn',
                    dataIndex : 'CHECKER',
                    width : 150,
                    text : '审核人'        
                },{
                    xtype : 'gridcolumn',
                    dataIndex : 'CHECK_TIME',
                    width : 150,
                    text : '审核时间'   ,
                    renderer : function rendererItem(value){
                        return CFControl.formatDate(value);
                    }
                } ],
                bbar : {
                    xtype : 'pagingtoolbar',
                    region : 'south',
                    id : 'pageToolBar',
                    height : 30,
                    store : userStore,
                    displayInfo : true,
                    displayMsg : '当前显示的记录 {0} - {1} 共计 {2}',
                    emptyMessage : '没有可显示的记录',
                    listeners : {
                        change:function(){
                            var dg = Ext.getCmp('userManGrid');
                            dg.getSelectionModel().select(0, true);
                        }
                    }
                },
                listeners : {
                    select : function(record, index, eOpts) {// 使用select事件
                        var user = record.getSelection()[0];
                        CFControl.setFormReadOnly('detailPanel', true);
                        var df = Ext.getCmp('detailPanel');
                        df.loadRecord(user);
                    },
                    selectionchange : function(selModel, selections) {
                        if(Ext.getCmp('userManGrid').getStore().getCount()==0){
                            CFControl.setFormFieldValue('detailPanel',Ext.create('CFrame.model.sys.userModel'));;
                        }
                        if ("add" == saveFlag) {
                            var el = Ext.getCmp('btn_add');
                            if (el.disabled) {
                                userStore.removeAt(0);
                                selModel.select(0, true);
                            }
                        }
                        Ext.getCmp('tf_PASSWD').setValue('');
                        Ext.getCmp('tf_PASSWD').hide();
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
                    id : 'tf_USER_ID',
                    name : 'USER_ID',
                    fieldLabel : '用户代码',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1,
                    MaxLength : 32,
                    allowBlank : false,
                    blankText : "用户代码不能为空",
                    maxLength : 20,
                    maxLengthText : '用户代码长度不能大于10位'
                }, {
                    xtype : 'textfield',
                    id : 'tf_USER_NAME',
                    name : 'USER_NAME',
                    fieldLabel : '用户名称',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1,
                    maxLength : 20,
                    maxLengthText : '用户名称长度不能大于10位'
                },{
                    xtype : 'gridpanel',
                    id : 'test',
                    name : 'test',
                    fieldLabel : '是否可用',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    store:teststore,
                    hidden:true
                },{
                    xtype : 'textfield',
                    id : 'tf_PASSWD',
                    name : 'PASSWD',
                    fieldLabel : '新密码',
                    labelWidth : 95, // 标签宽度
                    labelAlign : 'right',
                    colspan : 1,
                    rowspan : 1
                },{
                    xtype : 'button',
                    id : 'btn_set',
                    iconCls : 'tbar_buildingIcon',                  
                    text : '角色设置',
                    labelAlign : 'right',
                    width:90,
                    x:30,
                    handler : function() {
                        if(pc=='query')
                            {
                                var rmg = Ext.getCmp('userManGrid');
                                var model = rmg.getSelectionModel().getSelection()[0];
                                if(!model){
                                   Ext.Msg.alert('提示','请选择一条角色记录！');
                                   return;
                                }
                                var obj = new Object(); 
                                obj.code = this;
                                obj.model = model;
                                Ext.require('CFrame.view.sys.selectUserRoleWinView');
                                sw_userrole = top.Ext.getCmp('selectQueryUserRoleWin');
                                if(!sw_userrole){
                                    //Ext.apply(Ext.data.Connection.prototype, { async: false  });
                                    sw_userrole = top.Ext.create('CFrame.view.sys.selectUserRoleWinView',{layout : 'fit',
                                        width : 640,
                                        height : 480,
                                        resizable : false,
                                        draggable : true,
                                        closeAction : 'destroy',
                                        modal : true,
                                        title : '角色设置',
                                        model : obj.model
                                        });
                                }
                                sw_userrole.ret = '';
                                sw_userrole.show();
                                sw_userrole.child('#userRoleForm').child('#userRoleGrid').getStore().reload();
                                                        
                    }
                        if(pc=='add')
                        {
                            Ext.require('CFrame.view.sys.selectUserAddRoleWinView');
                            sw_userrole = top.Ext.getCmp('selectUserAddRoleWin');
                            if(!sw_userrole){
                                sw_userrole = top.Ext.create('CFrame.view.sys.selectUserAddRoleWinView',{layout : 'fit',
                                    width : 640,
                                    height : 480,
                                    resizable : false,
                                    draggable : true,
                                    closeAction : 'destroy',
                                    modal : true,
                                    title : '角色设置',
                                    store : Ext.getCmp('test').store,
                                    test : test
                                    });
                            }
                            sw_userrole.ret = '';
                            sw_userrole.show();
                            if(test=='loadone')
                                {
                                    sw_userrole.child('#userRoleForm').child('#userRoleGrid').getStore().reload();
                                }
                            sw_userrole.on('close',fn =function(){
                                if(sw_userrole.ret=='confirm')
                                {   
                                    addMenu=sw_userrole.child('#userRoleForm').child('#userRoleGrid').store;
                                    Ext.getCmp('test').store=addMenu;
                                    test='loadtwo';
                                }
                                sw_userrole.un('close',fn);
                                 });
                        }
                        if(pc=='modify')
                        {
                            var rmg = Ext.getCmp('userManGrid');
                            var model = rmg.getSelectionModel().getSelection()[0];
                            if(!model){
                               Ext.Msg.alert('提示','请选择一条角色记录！');
                               return;
                            }
                            var obj = new Object(); 
                            obj.code = this;
                            obj.model = model;
                            Ext.require('CFrame.view.sys.selectUserModifyRoleWinView');
                            sw_userrole = top.Ext.getCmp('selectUserModifyRoleWin');
                            if(!sw_userrole){
                                sw_userrole = top.Ext.create('CFrame.view.sys.selectUserModifyRoleWinView',{layout : 'fit',
                                    width : 640,
                                    height : 480,
                                    resizable : false,
                                    draggable : true,
                                    closeAction : 'destroy',
                                    modal : true,
                                    title : '角色设置',
                                    model : obj.model,
                                    store : Ext.getCmp('test').store,
                                    test : test
                                    });
                            }
                            sw_userrole.ret = '';
                            sw_userrole.show();
                            //sw_userrole.child('#userRoleForm').child('#userRoleGrid').getStore().reload();
                            if(test=='loadone')
                            {
                                sw_userrole.child('#userRoleForm').child('#userRoleGrid').getStore().reload();
                            }
                            sw_userrole.on('close',fn =function(){
                                if(sw_userrole.ret=='confirm')
                                {   
                                    addMenu=sw_userrole.child('#userRoleForm').child('#userRoleGrid').getStore();
                                    Ext.getCmp('test').store=sw_userrole.child('#userRoleForm').child('#userRoleGrid').store;
                                    test='loadtwo';
                                }
                                sw_userrole.un('close',fn); 
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
    var dg = Ext.getCmp('userManGrid');
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
    checkUserCode: function(str){
        return CFCheck.cheUsertyCode(str);
    },
    checkUserCodeText: "国家或地区代码不符合要求"
});*/

/**
 * 挂载校验
 */
function addValidation() {
//  var tf = Ext.getCmp('tf_USER_ID');
//  tf.allowBlank = false;
//  tf.blankText = "用户ID不能为空";
//  
//  var tf = Ext.getCmp('cb_BRANCH_NO');
//  tf.allowBlank = false;
//  tf.blankText = "用户部门不能为空";
};

Ext.onReady(function() {
    var win = Ext.create('CFrame.view.sys.userMaintainView');
    win.show();
    addValidation();
    CFControl.setFormReadOnly('detailPanel', true);
    CFControl.setToolBarStatus('qbbar', true);
    var dg = Ext.getCmp('userManGrid');
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
    Ext.getCmp('tf_PASSWD').hide();
    Ext.getCmp('tf_PASSWD').disable(true);
});
