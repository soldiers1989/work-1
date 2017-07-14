Ext.define('CFrame.model.zz.tbMerchantDetail', {
    extend: 'Ext.data.Model',

    fields: [
        {//合作机构编号
            name: 'MERCHANT_ID',
            type : 'string'
        },
        {//合作机构名称
            name: 'MERCHANT_NAME',
            type : 'string'
        },       
        {//创建时间
            name: 'CREAT_TIME',
            type : 'string'
        },
        {//创建者
            name: 'CREATOR',
            type : 'string'
        },
        {//审核者
            name: 'CHECK_TLRNO',
            type : 'string'
        },
        {//审核时间
            name: 'CHECK_TIME',
            type : 'string'
        }
    ]
});