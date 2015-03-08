<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/Inc.jsp" %>
<title><s:text name="sys.subject"/>-跑到火星啦~~~~</title>
</head>
<body>
<div class="main">
<h2 style="color:red;padding:20px;">
	${session.message }
</h2>
<s:debug></s:debug>
</div>
</body>
</html>