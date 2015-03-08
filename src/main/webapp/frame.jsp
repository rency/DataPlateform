<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/Inc.jsp" %>
<title><s:text name="sys.subject"/></title>
<style type="text/css">
	.top_l .top_c .top_r{float:left;display:block;}
	.top_l{float:left;border:0px solid black;text-align:right;width:30%;padding-top:10px;}
	.top_c{float:left;border:0px solid black;margin-top:25px;text-align:center;width:40%;}
	.top_r{float:right;border:0px solid black;text-align:right;width:30%;}
	.top_r span{cursor:pointer;padding:5px;}
	.top_r a{color:#FFFFFF;padding:5px;text-decoration:none;}
	.top_r a:hover{color:#FFFFFF;padding:5px; }
</style>
<script type="text/javascript">
$(function(){
	//请求树形菜单
	httpSender('json/menus!loadMenu.do',false,'',false,loadMenu);
});

function modifyPwd(){
	openDialog('password');
}

function save(){
	var newPassword = $('#newPassword').val();
	var confirmPassword = $('#confirmPassword').val();
	if( newPassword != confirmPassword){
		$.messager.alert('错误','新密码输入不一致');
		return;
	}
	
	$('#passwordForm').form('submit',{
		success: function(data){
			data = eval('(' + data + ')');
			if(data.key == "N" ){
				$.messager.alert('提示',data.info);
				closeDialog('password');
			}else{
				$.messager.alert('错误',data.info);
			}
		}
	});
}

</script>
</head>
<body class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" style="height:69px;background:url(images/footer_bg.gif) left top repeat;color:#FFFFFF;overflow:hidden;">
    	<div class="top_l">
	  		<a href="<%=basePath%>" target="_blank"></a>
		</div>
		<div class="top_c">
			<font size="6px" color="#FFFFFF" font-family="微软雅黑"><b><%-- <s:text name="sys.subject"/> --%></b></font>
		</div>
		<div class="top_r">
		</div>
    </div>
    <%--  --%>
    <div data-options="region:'center' ">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north'" style="background: #E0ECFF;height:23px;font-weight:bold;color:#15428B;">
    			<table style="width:100%; height:100%;" class="navigate">
                    <tr>
                       <td style="width:65px;text-align:left;valign:middle;padding-left:5px;">
                          <span>当前时间：</span>
                       </td>
                       <td style="text-align:left;valign:middle;padding-left:0px;">
                          <label id="currentTime"></label>
                       </td>
                       <td style="width:50%;">
                       </td>
                       <td style="text-align:center;valign:middle;padding:0px 5px;">
                       	  <span><s:text name="label.username" />${session.user.username }</span>
                       </td>
                       <td style="text-align:center;valign:middle;padding:0px 5px;">
                       	  <span><s:text name="label.role" />${session.user.userType.typeName }</span>
                       </td>
                       <td style="text-align:center;valign:middle;padding:0px 5px;">
                       	  <span><a href="javascript:return(0)" onclick="modifyPwd()"><s:text name="label.changepwd" /></a></span>
                       </td>
                       <td style="text-align:center;valign:middle;padding:0px 5px;">
                       	  <span><a href="<%=basePath%>user!logout.do"><s:text name="label.logout" /></a></span>
                       </td>
                    </tr>
                </table>
    		</div>
    		<div data-options="region:'center' ">
    			<div class="easyui-tabs" fit="true" border="false" id="tabs">
      				<div title='<s:text name="sys.subject"/>'>
	      				<iframe scrolling="auto" frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
      				</div>
		    	</div>
    		</div>
    		<div data-options="region:'west',title:'<s:text name="sys.subject"/>'" style="width:200px;">
    			<ul id="tree"></ul>
    		</div>
    	</div>
    </div>
    <div data-options="region:'south'" style="padding:10px;height:42px;text-align: center;background:url(images/footer_bg.gif) left top repeat-x;color:#FFFFFF;overflow:hidden;">
    	<address style="font-family: 微软雅黑;font-size:14px;">
				All rights reserved©2014.&nbsp;<s:text name="sys.subject"/>&nbsp;&nbsp;<s:text name="sys.email"/><a href="mailto:user_rcy@163.com?subject=<s:text name='sys.subject'/>">user_rcy@163.com</a>	
   		</address>
    </div>
    <div id="passwordDialog" class="easyui-dialog" title="修改密码"  modal="true" closed="true" style="padding:2px;width:300px;height:200px;overflow-x:hidden;overflow-y:hidden" data-options="iconCls:'icon-save',buttons:'#passwordDialogBtn'">
   		<form id="passwordForm" method="post" action="${pageContext.request.contextPath}/json/user!modifyPassword.do">
       		<table>
				<tr>
       				<td width="35%" align="right">旧密码:</td>
       				<td><input align="center" type="password" id="password" name="j_password" class="easyui-validatebox" required="true"/></td>
       			</tr>
				<tr>
       				<td width="35%" align="right">新密码:</td>
       				<td>	<input align="center" type="password" id="newPassword" name="newPassword" class="easyui-validatebox" required="true" validType="minLength[6]"/></td>
       			</tr>
				<tr>
       				<td width="35%" align="right">新密码确认:</td>
       				<td><input align="center" type="password" id="confirmPassword" name="confirmPassword" class="easyui-validatebox" required="true" validType="minLength[6]"/></td>
       			</tr>
       		</table> 
       	</form>    
   </div>
   <div id="passwordDialogBtn">
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('password')">取消</a>
	</div>
	<div id="tabsMenu" class="easyui-menu" style="width:120px;">  
    	<div name="close">关闭</div>  
   		<div name="Other">关闭其他</div>  
    	<div name="All">关闭所有</div>
  	</div> 
</body>

</html>   
<script type='text/javascript' src='<%=basePath%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=basePath%>/dwr/interface/message.js'></script>
<script type="text/javascript">
$(document).ready(function(){
	//这个方法用来启动该页面的ReverseAjax功能
	dwr.engine.setActiveReverseAjax(true);
	//设置在页面关闭时，通知服务端销毁会话
	dwr.engine.setNotifyServerOnPageUnload(true);
});
</script>