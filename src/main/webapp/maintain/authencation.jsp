<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$('#authTable').datagrid({
		url:'${pageContext.request.contextPath}/json/authority!load.do',
		loadMsg:'数据加载中,请稍后……',
		width:'auto',
		striped:true,
		pagination:true,//分页控件
		rownumbers:true,
		nowrap:true,
		pageNumber:1,
		pageSize:20,
		pageList:[10,20,30,50],
		fitColumns:true,
		singleSelect:true,
		onLoadSuccess:function(result){showEmptyInfo(result);},
		columns:[[
			{field:'id',title:'编号',width:30,formatter:function(value,rows,index){return rows.id;}},
			{field:'resources',title:'资源地址',width:200,formatter:function(value,rows,index){return rows.resources;}},
			{field:'typeName',title:'访问权限',width:100,formatter:function(value,rows,index){return rows.userType.typeName;}},
			//{field:'typeId',formatter:function(value,rows,index){return rows.userType.typeId;}},
			{field:'description',title:'描述',width:100,formatter:function(value,rows,index){return rows.description;}},
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
		},{
			text:'<input id="searchBox"/>'
		}]
	});
	
	$('#searchBox').searchbox({
		width:300,
		prompt:'请输入资源地址',
	    searcher:function(value,name){
	    	$('#authTable').datagrid('options').url='${pageContext.request.contextPath}/json/authority!queryByAddr.do';
			$('#authTable').datagrid('load',{
				'authority.resources':value
			});
	    }
	});
});

function open(){
	$('#addAuthForm_type').combogrid({
		panelWidth:150,
		mode: 'remote',
		idField:'typeId',
		textField:'typeName',
		url:'${pageContext.request.contextPath}/json/userT!load.do',  
		columns:[[{field:'typeName',title:'访问权限',width:130}]]
	});
	openDialog('addAuth');	
}

function save(){
	saveOrUpdateRow('addAuth','authTable');
}

function remove(){
	var row = $('#authTable').datagrid('getSelected');  
	if(row){
		delRow('authTable','json/authority!remove.do','authority.id='+row.id);
	}else{
		$.messager.alert('警告','请选择记录');
	}
}
</script>
<div class="easyui-layout">
	<table id="authTable" data-options="width:setWidthPX(0.60),height:setHeightPX(0.75)"></table> 
	<div id="addAuthDialog" class="easyui-dialog" title="添加权限资源" data-options="closed:true,modal:true,buttons:'#addAuthDialogBtn'" style="width:400px;height:300px;">
		<form id="addAuthForm" method="post" action="${pageContext.request.contextPath}/json/authority!add.do">
			<table>
				<tr>
					<td align="right">资源地址：</td>
					<td align="left"><input id="addAuthForm_resources" name="authority.resources" type="text" class="easyui-validatebox" data-options="required:true"/></td>
				</tr>
				<tr>
					<td align="right">访问权限：</td>
					<td align="left"><select id="addAuthForm_type" align="left" name="authority.userType.typeId" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
				</tr>
				<tr>
					<td align="right">描&nbsp;&nbsp;&nbsp;&nbsp;述：</td>
					<td align="left"><input id="addAuthForm_description" name="authority.description" type="text" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="addAuthDialogBtn">
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('addAuthDialog')">取消</a>
	</div>
</div>