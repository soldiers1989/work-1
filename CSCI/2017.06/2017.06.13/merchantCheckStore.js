/*
 * File: CFrame/store/roleStore.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('CFrame.store.zz.merchantCheckStore', {
    extend: 'Ext.data.Store',

    requires: [
        'CFrame.model.zz.tbMerchantDetail'
    ],
    pageSize : 10,
    model: 'CFrame.model.zz.tbMerchantDetail',
    proxy : new Ext.data.HttpProxy( {
        url : 'merchantMaintainCheckAction!queryMerchant',
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