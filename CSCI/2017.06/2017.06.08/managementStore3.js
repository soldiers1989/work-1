Ext.define('CFrame.store.zz.managementStore3', {
    extend: 'Ext.data.Store',

    requires: [
        'CFrame.model.zz.tbMerchantDetail'
    ],
    pageSize : 40,
    model: 'CFrame.model.zz.tbMerchantDetail',
    proxy : new Ext.data.HttpProxy( {
        url : 'merchantDetail',
        actionMethods: {  
            read: 'POST'  
        }, 
        reader: {
            type: 'json',
            root: 'dataList',
            totalProperty: 'total'
        }
    })
});