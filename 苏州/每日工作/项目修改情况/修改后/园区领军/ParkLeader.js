$(function () {
    searchRegs();
});

function searchRegs() {
    var queryObj = {};
    //queryObj.CertificateType = $('#CertificateType').combobox("getValue");
    queryObj.CorpBaseInfoId = $("input[name='CorpBaseInfoId']").val();
    queryObj.CustomerRepresentative = $('#CustomerRepresentative').val();
    quickQuery(queryObj, '/PatentManage/GetPatentManageList');
}

function quickQuery(queryObj, urlstr) {
    urlstr = '/ParkLeader/GetParkLeaderList';
    $('#ParkLeaderList').datagrid({
        url: urlstr,
        fitColumns: true, //自动调整列宽度
        nowrap: false, //自动换行
        border: false, //边框
        queryParams: queryObj,
        rownumbers: true,
        singleSelect: true,
        //checkOnSelect: true, //选择checkbox则选择行  
        //selectOnCheck: true, //选择行则选择checkbox
        pagination: true,
        pageSize: 1,
        pageList: [10, 20, 30, 40],
        striped: true,
        columns: [
            [
                { field: 'CorpBaseInfoName', title: '客户名称', width: 200, align: 'center' },
                { field: 'CustomerRepresentative', title: '客户代表', width: 100, align: 'center' },
                { field: 'Area', title: '所属领域', width: 100, align: 'center' },
                { field: 'Address', title: '公司地址', width: 100, align: 'center' },
                { field: 'RegisterAccount', title: '注册资金', width: 100, align: 'center' },
                { field: 'CompanySize', title: '公司规模', width: 100, align: 'center' },
                { field: 'Remarks', title: '备注', width: 100, align: 'center' },
                { field: 'Creator', title: '创建人', width: 100, align: 'center' },
                { field: 'CreatedTime', title: '创建日期', width: 100, align: 'center' },
                { field: 'ModifyName', title: '修改人', width: 100, align: 'center' },
                { field: 'ModifyTime', title: '修改日期', width: 100, align: 'center' },
                { field: 'ParkLeaderId', hidden: true }
            ]
        ],
        onSelect: function (index, data) { //用来对最后选的Row高亮显示
            var opt = $(this).datagrid("options");
            var rows1 = opt.finder.getTr(this, "", "selected", 1);
            var rows2 = opt.finder.getTr(this, "", "selected", 2);
            if (rows1.length > 0) {
                $(rows1).each(function () {
                    var tempIndex = parseInt($(this).attr("datagrid-row-index"));
                    if (tempIndex == index) {
                        $(this).removeClass("select-noback");
                    } else {
                        $(this).addClass("select-noback");
                    }
                });
            }
            if (rows2.length > 0) {
                $(rows2).each(function () {
                    var tempIndex = parseInt($(this).attr("datagrid-row-index"));
                    if (tempIndex == index) {
                        $(this).removeClass("select-noback");
                    } else {
                        $(this).addClass("select-noback");
                    }
                });
            }
        },
        onUnselect: function (index, data) {
            var opt = $(this).datagrid("options");
            var rows1 = opt.finder.getTr(this, "", "allbody", 1);
            var rows2 = opt.finder.getTr(this, "", "allbody", 2);
            if (rows1.length > 0) {
                $(rows1).each(function () {
                    var tempIndex = parseInt($(this).attr("datagrid-row-index"));
                    if (tempIndex == index) {
                        $(this).removeClass("select-noback");
                    }
                });
            }
            if (rows2.length > 0) {
                $(rows2).each(function () {
                    var tempIndex = parseInt($(this).attr("datagrid-row-index"));
                    if (tempIndex == index) {
                        $(this).removeClass("select-noback");
                    }
                });
            }
        },
        onDblClickRow: function (index, data) {
            var id = data.ParkLeaderId;
            window.open('/Clm/ParkLeader/Edit/' + id);
        },
        onLoadSuccess: function (data) {
            if (data.message != undefined) {
                $(this).datagrid("setEmptyText", data.message);
            } else {
                $(this).datagrid("setEmptyText", "查询没有找到相关记录");
            }
        },
        onAfterEdit: function (index, row, changes) {
            //debugger;
            row.F = parseInt(changes.FDisplay);
        }
    });
}
function Reset() {
    $('#Searchcondition input').each(function () {
        $(this).val('');
    });
};

function edit(url) {
    var row = $('#ParkLeaderList').datagrid("getSelections");
    if (row.length <= 0) {
        $.messager.alert('提示', '请选择需要编辑的记录!', 'info');
    } else if (row.length > 1) {
        $.messager.alert('提示', '编辑只能是唯一记录，请重新选择!', 'info');
    } else {
        var id = row[0].ParkLeaderId.toString();
        window.open().location.href = url + "/" + id;
    }
}


function del() {
    debugger;
    var row = $('#ParkLeaderList').datagrid("getSelections");
    if (row.length <= 0) {
        $.messager.alert('提示', '请选择需要删除的记录!', 'info');
    } else if (row.length > 1) {
        $.messager.alert('提示', '编辑只能是唯一记录，请重新选择!', 'info');
    } else {
        var id = row[0].ParkLeaderId.toString();
        $.messager.confirm('确认', '是否删除专利【' + row[0].PatentName + '】？', function (r) {
            if (r) {
                $.ajax({
                    url: '/ParkLeader/Delete?id=' + id,
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        searchRegs();
                        $.QuickTips({
                            message: data.message
                        });
                    }
                });
            }
        });
    }
}