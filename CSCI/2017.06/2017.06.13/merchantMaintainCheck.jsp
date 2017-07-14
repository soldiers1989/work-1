<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>聚源数据平台</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<link rel="stylesheet" type="text/css"
    href="css/cframe.css">
<script type="text/javascript">
    var App = (function() {
        return {
            basePath :"<%=basePath%>"
    }
    })();
</script>
<script type="text/javascript" src="ext4full/bootstrap.js"></script>
<script type="text/javascript" src="CFrame/common/CFControl.js"></script>
<script type="text/javascript" src="CFrame/common/CFCheck.js"></script>
<script type="text/javascript" src="CFrame/common/gridprinter.js"></script>
<script type="text/javascript" src="CFrame/store/commonDictStore.js"></script>
<script type="text/javascript" src="CFrame/view/zz/merchantMaintainCheckView.js"></script> 
</head>

<body style='background-color: white;'>
</body>
</html>
