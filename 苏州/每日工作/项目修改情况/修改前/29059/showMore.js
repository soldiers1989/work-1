var News = {};

$(function () {
    $('#auditListPanel').hide();
    $('#editOrDelPanel').hide();

    /*String去空格拓展*/
    if (!String.prototype.trim) {
        String.prototype.trim = function () {
            return this.replace(/^s+|s+$/g, '');
        };
    }

    /*公司\类型 下拉列表的隐藏*/
    var hideFlag = false;
    $("#cmyPanel,#typePanel").mouseenter(function () {
        hideFlag = false;
    });
    $("#cmyPanel,#typePanel").mouseleave(function () {
        hideFlag = true;
    });

    $("body").click(function () {
        if (hideFlag) {
            $('.ShowMoreSpan').siblings('ul').hide();
        }
    });

    //控制“置顶”操作按钮的显隐
    $('#newsListTable table tr').mouseover(function () {
        $(this).find('.operateLink').show();
    });
    $('#newsListTable table tr').mouseout(function () {
        $(this).find('.operateLink').hide();
    });
    //置顶操作
    $('.setTopBtn').click(function () {
        var nowIsTop = true;
        var setTopLink = $(this);
        if (setTopLink.text() == "置顶") {
            nowIsTop = false;
        }
        var newsId = $(this).siblings('.idColumn').first().val();
        $.post('/News/SetTop',
            {
                newsId: newsId,
                setTop: !nowIsTop
            }, function (data) {
                if (data.IsSuccess && nowIsTop) {
                    setTopLink.text('置顶');
                } else {
                    setTopLink.text('取消置顶');
                }
            });
    });


    $("#allBtn").click(function () {
        $('#auditListPanel').hide();
        $('#editOrDelPanel').hide();
        $('#contentList').show();
    });

    $("#mypublishBtn").click(function () {
        $('#auditListPanel').hide();
        $('#editOrDelPanel').hide();
        $('#contentList').show();
    });

    $("#audittingBtn").click(function () {
        $('#auditListPanel').hide();
        $('#editOrDelPanel').hide();
        $('#contentList').show();
    });

    $("#rejectBtn").click(function () {
        $('#auditListPanel').hide();
        $('#editOrDelPanel').hide();
        $('#contentList').show();
    });

    $("#FavoriteBtn").click(function () {
        $('#auditListPanel').hide();
        $('#editOrDelPanel').hide();
        $('#contentList').show();
    });

    //发布按钮-点击处理
    $("#publishTrigerBtn").click(function () {

        News.InitEditDialog();

        $('#editPop').dialog('open').dialog('move', { top: $(document).scrollTop() + 85 }); //.dialog("refresh");
    });

    //审核按钮
    $("#auditTrigerBtn").click(function () {
        $('#auditList').datagrid('reload');
        $('#auditListPanel').show();
        $('#editOrDelPanel').hide();
        $('#contentList').hide();
        $(this).css({ background: "#fbeca6" });
        $("#editTrigerBtn").css({ background: "#fff" });
        $(".NewsSearchBtn").each(function () {
            $(this).css({ background: "#fff" });
        });
        //$('#auditPop').dialog('open').dialog('move', { top: $(document).scrollTop() +150 }); //.dialog("refresh");
    });


    //点击"编辑"按钮时,将页面内的信息的Id通过参数传到弹出窗口,在其中加载这些信息
    $("#editTrigerBtn").click(function () {
        //获取页面当前显示的信息条目的Id,拼接成字符串
        var idStr = "";

        $("#newsListTable .idColumn").each(function () {
            idStr = idStr + "," + $(this).attr("value");
        });

        $('#editList').datagrid('reload');
        $('#editOrDelPanel').show();
        $('#auditListPanel').hide();
        $('#contentList').hide();
        $(this).css({ background: "#fbeca6" });
        $("#auditTrigerBtn").css({ background: "#fff" });
        $(".NewsSearchBtn").each(function () {
            $(this).css({ background: "#fff" });
        });
        //在打开的弹窗中加载这批信息
        //$('#editOrDelPop').dialog('move', { top: $(document).scrollTop() + 100 }).dialog('open').dialog('refresh', '/News/GetEditList?IdString=' + idStr);

    });

    //点击分类下拉菜单中项,执行异步查询
    $('#typeZeroList li a').click(function () {
        var desc = $(this).attr('value');
        reloadTypeList(desc, 0);
    });

    //showIndex = 0 则载三分类 （reloadTypeObj）
    function reloadTypeList(typeZero, showIndex) {
        if (showIndex == 0)
            showTypeZeroList();
        if (typeZero == "全部分类") {
            $('#typeList li a').each(function () {
                $(this).show();
                if (showIndex == 0)
                    reloadTypeObj($(this).attr('value'), $(this).text());
                showIndex++;
            });
        } else {
            $('#typeList li a').each(function () {
                if ($(this).attr('class') == typeZero) {
                    $(this).show();
                    if (showIndex == 0)
                        reloadTypeObj($(this).attr('value'), $(this).text());
                    showIndex++;
                } else {
                    $(this).hide();
                }
            });
        }
        $("#ZeroShowMoreSpan").text(typeZero);
    }

    function reloadTypeObj(typeId, nowName) {
        SearchNews("TypeId = " + typeId);
        $("#typePanel").find('.ShowMoreSpan').first().text(nowName);
        $("#typeTitle").text("▍ " + nowName);
    }

    //点击分类下拉菜单中项,执行异步查询
    $('#typeList li a').click(function () {
        SearchNews("TypeId=" + $(this).attr('value'));
        if ($(this).length > 0) {
            $("#typeTitle").text("▍ " + $(this)[0].text);
        }
    });

    //点击公司下拉选框
    $('#cmyList li a').click(function () {
        SearchNews("CompanyId=" + $(this).attr('value'));
    });

    //点击发布类型搜索按钮
    $('#searchTypeTabs li a').click(function () {
        SearchNews('PublishType=' + $(this).attr("value"));
    });


    //Enter键执行异步查询
    $(document.body).keydown(function (event) {
        if (event.keyCode == 13) {

            var searchText = $("#searchBox").val();
            SearchNews("SearchText=" + searchText);
        }
    });

    //文本检索
    $("#newsSearchBtn").click(function () {
        var searchText = $("#searchBox").val();
        SearchNews("SearchText=" + searchText);
    });

    //隐藏载入图标
    $("#loadingImg").hide();

    //初始化部门下拉选框
    InitDepartmentDropList($("#CompanyID").attr("value"));

    maskPublishType();

    //初始化3类列表，不载
    reloadTypeList($("#ZeroShowMoreSpan").text(), 1);
});                                                                                                                                                                         //--Jquery End


//Post方式查询信息(异步)
function SearchNews(keyValueStr) {

    //显示载入图标
    $("#loadingImg").show();

    var paramObj = News.GetCurrentSearchParams();

    var nameReg = /^\s*\w*\s*/;
    //  var valueReg = /\s*\w*\s*$/;

    var nameStr = String(nameReg.exec(keyValueStr)).trim();
    var tmpIndex = keyValueStr.lastIndexOf("=");
    var valueStr = keyValueStr.substring(tmpIndex + 1).trim();

    //记录上一次查询使用的这两个变量,后面需要
    var prevCompanyId = paramObj.CompanyId;
    var prevTypeId = paramObj.TypeId;

    //换公司,清部门
    if (nameStr.trim() == "CompanyId") {
        paramObj.DepartmentId = 0;
    }

    if (nameStr != "PageIndex") {
        paramObj.PageIndex = 1;
    }

    paramObj[nameStr] = valueStr;

    $("#contentList").load("/News/ShowMoreNewses #contentList>*", paramObj, function () {

        //更改选择的公司或类型后,相应下拉列表要调整
        if (nameStr.trim() == "CompanyId") {
            AdjustDropdownList("#cmyPanel", paramObj.CompanyId, prevCompanyId);
        }
        if (nameStr.trim() == "TypeId") {
            AdjustDropdownList("#typePanel", paramObj.TypeId, prevTypeId);
        }

        maskPublishType();

        InitDepartmentDropList(paramObj.CompanyId);

        //隐藏载入图标
        $("#loadingImg").hide();
    });
};

var SEARCH_PARAMS = undefined;  //搜索参数信息对象
//获取页面当前的查询参数集合对象
News.GetCurrentSearchParams = function () {
    if (SEARCH_PARAMS != undefined) {
        return SEARCH_PARAMS;
    }
    var objStr = $("#SearchParams").val();
    SEARCH_PARAMS = JSON.parse(objStr);

    return SEARCH_PARAMS;
};


//弹出公司面板
function showCmyList() {
    $('#cmyList').toggle(200);
}

//弹出分类面板
function showTypeZeroList() {
    $('#typeZeroList').toggle(200);
}

//弹出分类面板
function showTypeList() {
    $('#typeList').toggle(200);
}

//突出显示查询所点击的发布类型按钮
var maskPublishType = function () {
    var paramObj = News.GetCurrentSearchParams();

    $(".NewsSearchBtn").each(function () {
        if ($(this).attr("value") == paramObj.PublishType) {

            $(this).css({ background: "#BCD2E6" }); //.attr('class', 'publishTypeBtn');
            $("#auditTrigerBtn").css({ background: "#fff" });
            $("#editTrigerBtn").css({ background: "#fff" });
        } else {
            $(this).css({ background: "#fff" });
        }
    });
};


//初始化部门下拉框
function InitDepartmentDropList() {
    var objStr = $("#SearchParams").val();
    var paramObj = eval("(" + objStr + ")");
    $('#deptComboBox').combobox({
        url: '/News/GetDepartmentData?companyID=' + paramObj.CompanyId,
        valueField: 'id',
        textField: 'text',
        width: 200,
        onSelect: function (rec) {
            SearchNews("DepartmentId=" + rec.id);
        },
        onLoadSuccess: function () {
            $('#deptComboBox').combobox('setValue', paramObj.DepartmentId);
        }
    });

    //禁用或者启用部门下拉选框
    if (paramObj.CompanyId == 0) {
        $('#deptComboBox').combobox("disable");
    } else {
        $('#deptComboBox').combobox("enable");
    }
}

//更新"公司"、"信息类型"下拉列表
function AdjustDropdownList(containerId, nowId, prevObjId) {

    $(containerId).find('ul').first().hide();

    //上一次查询的类型名称(也就是目前仍显示的名称)
    var prevName = $(containerId).find('.ShowMoreSpan').first().text();

    //当前查询条件的名称
    var nowName = "";

    var dicArray = new Array(); //类型键值对数组

    $(containerId).find("ul li a").each(function () {

        dicArray.push([$(this).attr('value'), $(this).text()]);

        if ($(this).attr('value') == nowId) {
            nowName = $(this).text();
        }
    });

    dicArray.push([prevObjId, prevName]);

    //将当前类型名称更新到显示标签
    $(containerId).find('.ShowMoreSpan').first().text(nowName);

    //数组处理(移除当前所选,排序,重新绑定到控件)
    for (var i = 0; i < dicArray.length; i++) {
        if (dicArray[i][1] == nowName) {
            dicArray.splice(i, 1);
            break;
        }
    }
    function sortFunc(a, b) {
        return a[0] - b[0];
    }
    dicArray.sort(sortFunc);

    //重置列表的节点
    $(containerId).find("ul li a").each(function (index) {
        $(this).attr('value', dicArray[index][0]);
        $(this).text(dicArray[index][1]);
    });

}


//上传进行时将发布按钮禁用
var UploadBeginDeal = function () {
    $('#publishBtn').linkbutton('disable');
};
//上传结束将发布按钮启用
var UploadCompleteDeal = function () {
    $('#publishBtn').linkbutton('enable');
};

////////**********////////////

News.EditDialogHasInitialized = false; //编辑弹窗是否已经初始化
//初始化编辑弹窗
News.InitEditDialog = function () {
    if (this.EditDialogHasInitialized) {
        //如果弹窗已经初始化,则清空相应控件
        News.ResetCreateDialog();
        FlashUpload.ClearAllUploadFiles();
        return;
    }

    //初始化信息发布弹窗
    $('#editPop').dialog({
        title: '发布信息',
        href: '/News/Create',
        width: 900,
        //        height: 570,
        dragable: true,
        cache: false,
        closed: true,
        modal: false,
        onLoad: function () {

            News.InitRichEditor();

            News.InitEditComponent();

            News.InitFlashUploader();

            News.EditDialogHasInitialized = true;

            //“发表”按钮的点击事件处理
            $("#publishBtn").click(News.PublishNews);

            //预览
            $("#previewBtn").click(News.PreviewNews);


        }
    }); //   $('#editPop').dialog(  End!!!

};
 
//初始化编辑弹窗里的富文本编辑器
News.InitRichEditor = function () {

    //初始化多媒体编辑器 
   var ue = UE.getEditor('newsEditor', {zIndex:9000});
};


function ShowSimpleLayout() {
    document.getElementById('contentDiv').style.display = 'none';
    $('.window-shadow').height($('#editPop').height());
    $('#previewBtn').css('visibility', 'hidden');
}

function ShowStandardLayout() {
    document.getElementById('contentDiv').style.display = 'block';
    $('.window-shadow').height($('#editPop').height());
    $('#previewBtn').css('visibility', 'visible');
}

News.UserTreeData = null;
//初始化编辑弹窗的其它组件
News.InitEditComponent = function () {
    //初始化发布方式选择框
    $("#PublishTypeId").combobox({
        editable: false,
        onSelect: function (record) {
            if (record.value == 1) {
                ShowSimpleLayout();
            } else {
                ShowStandardLayout();
            }
        }
    });
    $("#PublishTypeId").combobox('clear');

    //初始化信息类型选择框
    $('#type').combobox({
        url: '/News/GetNewsTypeList?id=1',
        valueField: 'id',
        textField: 'text',
        onSelect: function () {
            $('#assignAudittor').combotree('clear');
            getAudittorsTree();
        }
    });

    //初始化指定查看人列表树
    $('#assignUser').combotree({});
    $.post('/User/GetTreeUsers', function (treeData) {
        News.UserTreeData = eval(treeData);
        $('#assignUser').combotree('loadData', News.UserTreeData);
    });

    $('#assignUserGroup').combotree({ multiple: true });
    //查看群组选择框
    $.post('/SubGroup/GetSubGroupByModule',
        { moduleName: 'News', dontAddGroup: true },
        function (treeData) {
            var tData = eval(treeData);
            $('#assignUserGroup').combotree('loadData', tData);

        });
    //初始化指定审核人列表树
    getAudittorsTree();
};

function getAudittorsTree() {
    var paramObj = News.GetCurrentSearchParams();
    $.post('/User/GetNewsAudittorUsersTree', { companyId: paramObj.CurrentUserCompanyId, typeName: $('#type').combobox('getText') },
                function (treeData) {
                    var tData = eval(treeData);
                    $('#assignAudittor').combotree('loadData', tData);
                    var treeObj = $('#assignAudittor').combotree('tree');
                    treeObj.tree('expandAll');
                });
}

//初始化flash版的文件上传插件
News.InitFlashUploader = function () {

    //加载flash版的文件上传处理脚本
    if ($('#SWFUpload_0').length < 1) {
        $.getScript("/jb/FileUpload/FileUpload_Flash.js", function () {

            setTimeout(function () {
                FlashUpload.CreateUploader('', UploadBeginDeal, UploadCompleteDeal);
            }, 1000);
        });
    } else {
        //控件已初始化,则清空已上传
        FlashUpload.ClearAllUploadFiles();
    }
};

//重置发表页面的所有输入控件和隐藏信息
News.ResetCreateDialog = function () {
    $('#CurrentEditNewsId').val(0);
    $("#PublishTypeId").combobox('clear');
    $('#type').combobox('clear');
    $('#assignUser').combotree('clear');
    $('#assignAudittor').combotree('clear');
    $("#newsPublishContainer :input").val('');
    UE.getEditor('newsEditor').setContent('');
    ShowStandardLayout();
};

//发布信息
News.PublishNews = function () {

    var publishObj = new Object();
    publishObj.NewsId = $('#CurrentEditNewsId').val();
    publishObj.Title = $("#newsTitle").val();
    publishObj.TypeID = $('#type').combobox('getValue');
    publishObj.PublishTypeId = $("#PublishTypeId").combobox('getValue');
    publishObj.CustomType = $("#customType").val();
    publishObj.Content = UE.getEditor('newsEditor').getContent();
    publishObj.BrowserIdArray = $("#assignUser").combotree("getValues");
    publishObj.BrowserIdArray = News.TrimArray(publishObj.BrowserIdArray);

    publishObj.BrowserGroupIdArray = $("#assignUserGroup").combotree("getValues");
    publishObj.BrowserGroupIdArray = News.TrimArray(publishObj.BrowserGroupIdArray);

    publishObj.ConfirmAuditor = $('#ComfirmAudit').attr('checked') == 'checked';
    publishObj.AuditUserArray = $("#assignAudittor").combotree("getValues");
    publishObj.CurrentFilesGuidSting = GetCurrentFiles();

    $.ajax({
        url: '/News/SaveNews',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(publishObj),
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            alert(data.Message);
            if (data.Success) {
                $('#editPop').dialog("close");
                $('#auditList').datagrid('reload');
            }
        },
        error: function (event) {//, jqXHR, ajaxSettings, thrownError
            if (event != undefined && event.responseText != undefined) {
                alert("信息发布失败!" + $(event.responseText).text());
            }
        }
    });
};

News.PreviewNews = function () {
    debugger;
    var a = encodeURIComponent(UE.getEditor('newsEditor').getContent());
//    $.ajax({
//        url: '/News/PreviewNews',
//        type: 'POST',
//        dataType: 'json',
//        data: { PublishTypeId: 0, Title: $("#newsTitle").val(), Content: encodeURIComponent(UE.getEditor('newsEditor').getContent()) },
//        contentType: 'application/json; charset=utf-8',
//        success: function (data) {
//            
//        }
//    });
//    $.post('/News/PreviewNews', { PublishTypeId: 0, Title: $("#newsTitle").val(), Content: encodeURIComponent(UE.getEditor('newsEditor').getContent()) },
//                function (a) {
//                    window.open(a);
//                });
    window.open('/News/PreviewNews?PublishTypeId=0&Title=' + $("#newsTitle").val() + '&Content=' +encodeURIComponent( UE.getEditor('newsEditor').getContent()));
};

//去除数组的空格成员
News.TrimArray = function (strArray) {

    if (strArray == undefined) {
        return null;
    }
    for (var i = 0; i < strArray.length; i++) {
        if (strArray[i].trim() == "") {
            strArray.splice(i, 1);
        }
    }
    return strArray;
};


