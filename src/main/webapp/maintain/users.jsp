<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$('#userTable').datagrid({
		url:'${pageContext.request.contextPath}/json/user!load.do',
		loadMsg:'数据加载中,请稍后……',
		width:'auto',
		striped:true,
		rownumbers:true,
		nowrap:true,
		fitColumns:true,
		pagination:true,//分页控件
		pageNumber:1,
		pageSize:20,
		pageList:[10,20,30,50],
		singleSelect:true,
		onLoadSuccess:function(result){showEmptyInfo(result);},
		columns:[[
			{field:'username',title:'用户名称',width:100,formatter:function(value,rows,index){return rows.username;}},
			{field:'email',title:'邮件',width:100,formatter:function(value,rows,index){return rows.email;}},
			{field:'mobilePhone',title:'移动电话',width:100,formatter:function(value,rows,index){return rows.mobilePhone;}},
			{field:'typeName',title:'用户权限',width:100,formatter:function(value,rows,index){return rows.userType.typeName;}},
			//{field:'guid',formatter:function(value,rows,index){return rows.guid}},
			{field:'lastLogin',title:'最近登录',width:100,formatter:function(value,rows,index){
				var timeStr = rows.lastLogin;
				timeStr = timeStr.replace('T',' ');
				return timeStr;
			}},
			{field:'loginIP',title:'登录地址',width:100,formatter:function(value,rows,index){return rows.loginIP;}},
			{field:'createTime',title:'创建时间',width:100,formatter:function(value,rows,index){
				var timeStr = rows.createTime;
				timeStr = timeStr.replace('T',' ');
				return timeStr;
			}}
		]],
		toolbar: [{
			text:'添加',
			iconCls: 'icon-add',
			handler: function(){open();}
		},{
			text:'删除',
			iconCls: 'icon-remove',
			handler: function(){remove();}
		}]
	});
});

function open(){
	openDialog('addUser');
	$('#addUserForm').form('clear');
	$('#addUserForm_userType').combogrid({
		panelWidth:150,
		idField:'typeId',  
		textField:'typeName',
		url:'${pageContext.request.contextPath}/json/userT!load.do',  
		columns:[[{field:'typeName',title:'用户类型',width:130}]]
	});
}

function save(){
	saveOrUpdateRow('addUser','userTable');
}

function remove(){
	var row = $('#userTable').datagrid('getSelected'); 
	if(row){
		delRow('userTable','json/user!remove.do','user.guid='+row.guid);
	}else{
		$.messager.alert('警告','请选择记录');
	}
}
</script>
<div class="easyui-layout">
	<table id="userTable" data-options="width:setWidthPX(0.60),height:setHeightPX(0.75)"></table>
	<div id="addUserDialog" class="easyui-dialog" title="添加用户" data-options="closed:true,modal:true,buttons:'#addUserDialogBtn'" style="width:400px;height:300px;">
		<form id="addUserForm" method="post" action="${pageContext.request.contextPath}/json/user!save.do" >
			<table>
				<tr>
					<td align="right">用户名称：</td>
					<td align="left"><input id="addUserForm_username" name="user.username" type="text" class="easyui-validatebox" data-options="required:true,validType:'username'"/></td>
				</tr>
				<tr>
					<td align="right">密&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
					<td align="left"><input id="addUserForm_password" name="user.password" type="password" class="easyui-validatebox" data-options="required:true"/></td>
				</tr>
				<tr>
					<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;件：</td>
					<td align="left"><input id="addUserForm_email" name="user.email" type="text" class="easyui-validatebox" data-options="required:true,validType:'email'" /></td>
				</tr>
				<tr>
					<td align="right">移动电话：</td>
					<td align="left"><input id="addUserForm_mobilePhone" name="user.mobilePhone" type="text" class="easyui-validatebox" data-options="required:true,validType:'mobile'" /></td>
				</tr>
				<tr>
					<td align="right">用户权限：</td>
					<td align="left"><select id="addUserForm_userType" align="left" name="user.userType.typeId" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="addUserDialogBtn">
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('addUser')">取消</a>
	</div>
</div>