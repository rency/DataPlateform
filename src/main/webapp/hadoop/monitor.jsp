<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$('#menusTable').datagrid({
		url:'${pageContext.request.contextPath}/json/menus!list.do',
		loadMsg:'数据加载中,请稍后……',
		width:'auto',
		striped:true,
		rownumbers:true,
		nowrap:true,
		fitColumns:true,
		singleSelect:true,
		onLoadSuccess:function(result){showEmptyInfo(result);},
		columns:[[
			{field:'resId',title:'编号',width:100,formatter:function(value,rows,index){return rows.resId;}},
			{field:'resName',title:'节点名称',width:80,formatter:function(value,rows,index){return rows.resName;}},
			//{field:'parentResId',formatter:function(value,rows,index){return rows.parentResId;}},
			{field:'parentResName',title:'父节点名称',width:80,formatter:function(value,rows,index){return rows.parentResName;}},
			{field:'userType.typeName',title:'访问类别',width:60,formatter:function(value,rows,index){return rows.userType.typeName;}},
			{field:'description',title:'资源描述',width:100,formatter:function(value,rows,index){return rows.description;}},
			{field:'href',title:'资源地址',width:200,formatter:function(value,rows,index){return rows.href;}},
			{field:'leaf',title:'是否子节点',width:50,formatter:function(value,rows,index){
				if(rows.leaf){
					return '是';
				}else{
					return '否';
				}
			}},
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
			text:'<input id="menusSearchBox"/>'
		}]
	});
	$('#menusSearchBox').searchbox({
		width:300,
		prompt:'请输入节点名称',
	    searcher:function(value,name){
	    	$('#menusTable').datagrid('options').url='${pageContext.request.contextPath}/json/menus!queryByResName.do';
			$('#menusTable').datagrid('load',{
				'menus.resName':value
			});
	    }
	});
});

</script>
<div class="easyui-layout">
	<table id="menusTable" data-options="width:setWidthPX(0.60),height:setHeightPX(0.75)"></table>
	<%-- <div id="addMenusDialog" class="easyui-dialog" title="添加菜单" data-options="closed:true,modal:true,buttons:'#addMenusDialogBtn'" style="width:400px;height:400px;">
		<form id="addMenusForm" method="post" action="${pageContext.request.contextPath}/json/menus!save">
			<table>
				<tr>
       				<td width="35%" align="right">资源名称：</td>
       				<td><input align="center" type="text" id="addMenusForm_resName" name="menus.resName" class="easyui-validatebox" style="width:150px" required="true" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">资源描述：</td>
       				<td><input align="center" type="text" id="addMenusForm_description" name="menus.description" class="easyui-validatebox" style="width:150px" required="true" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">资源地址：</td>
       				<td><input align="center" type="text" id="aaddMenusForm_href" name="menus.href" class="easyui-validatebox" style="width:150px"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">访问权限：</td>
       				<td><select id="addMenusForm_typeId" align="center" name="menus.userType.typeId" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
				<tr>
       				<td width="35%" align="right">&nbsp;&nbsp;父节点：</td>
       				<td><select id="addMenusForm_parentResId" align="center" name="menus.parentResId" class="easyui-combotree" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">&nbsp;&nbsp;优先级：</td>
       				<td><select id="addMenusForm_priority" align="center" name="menus.priority" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
			</table>
		</form>
	</div>
	<div id="addMenusDialogBtn">
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('addMenus')">取消</a>
	</div> --%>
</div>
