<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/Inc.jsp" %>
<title>页面未找到 - <s:text name="sys.subject"/></title>
</head>
<body>
<div class="main">
	<div style="float:left;background:url(images/maintain.png) no-repeat;width:500px;height:250px;padding-top:220px;">
		<p class="title3" style="color:red;">
		页面未找到&nbsp;&nbsp;&nbsp;&nbsp;   ${session.message }
		</p>
		<br/>
		<a href="javascript:history.back(-1)" style="float:left;">返回</a>
	</div>
</div>
</body>
</html>