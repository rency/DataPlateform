<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<base href="<%=basePath%>">
<meta http-equiv="X-UA-Compatible" content="Edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="data analyzer">
<meta http-equiv="description" content="data plateform,data analyzer" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=basePath%>script/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>script/easyui/jquery.edatagrid.js"></script>
<script type="text/javascript" src="<%=basePath%>script/default.js"></script>
<script type="text/javascript" src="<%=basePath%>script/functions.js"></script>
<script type="text/javascript" src="<%=basePath%>script/easyuiUtil.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>styles/styles.css">
<link rel="SHORTCUT ICON" href="<%=basePath%>images/favicon.ico" />