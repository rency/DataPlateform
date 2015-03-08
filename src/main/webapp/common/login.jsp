<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/Inc.jsp" %>
<title><s:text name="sys.subject"/></title>
<script type="text/javascript">
$(function(){
	$('#loginForm_username').focus();
});	
</script>
<style type="text/css">
	body{margin:0px auto;padding:0px;font-family:Arial, Lucida, Verdana, Helvetica, sans-serif;font-size:14px;text-align:center;line-height:130%;color: #263457;text-align: center;margin: 0 auto;background:url(images/login_bg.png) left top repeat-x;}
	#loginPanel table tr{padding-top:10px;line-height:30px;}
	#loginPanel table tr td{padding-left:30px;}
</style>
</head>
<body bgcolor="#D4D4D4">
<div style="text-align:center;width:985px;margin:0px auto;">
	<div style="text-align:center;width:400px;margin:0px auto;margin-top:15%;">
		<div id="loginPanel" class="easyui-panel" title="<s:text name='sys.login'/>" style="font-size:16px;width:500px;height:310px;background:#D4D4D4;">
			<form id="loginForm" method="post">
				<s:token></s:token>
				<table>
					<tr>
						<td colspan="2" align="center"><h1><s:text name="sys.subject"/></h1></td>
					</tr>
					<tr>
						<td align="right"><s:text name="label.username" /></td>
						<td align="left"><input id="loginForm_username" name="j_username" class="easyui-validatebox" type="text" data-options="required:true" style="width:150px"/></td>
					</tr>
					<tr>
						<td align="right"><s:text name="label.password" /></td>
						<td align="left">
							<input id="loginForm_password" class="easyui-validatebox" name="user.password" type="password" data-options="required:true" style="width:150px" onfocus="enterKeyPress('loginForm_password',login)" />
							<input id="password" name="j_password" type="hidden" />
						</td>
					</tr>
					<tr align="center">
						<td colspan="2">
							<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="login()"><s:text name="btn.login" /></a> 
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="reset()"><s:text name="btn.reset" /></a>
						</td>
					</tr>
					<tr><td colspan="2"></td></tr>
					<tr align="center"><td colspan="2"><font color="red" size="3"><s:property value="fieldErrors.message[0]"/></font></td></tr>
				</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>