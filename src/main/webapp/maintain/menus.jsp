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
		pagination:true,//分页控件
		pageNumber:1,
		pageSize:20,
		pageList:[10,20,30,50],
		fitColumns:true,
		singleSelect:true,
		onLoadSuccess:function(result){showEmptyInfo(result);},
		columns:[[
			//{field:'resId',formatter:function(value,rows,index){return rows.resId;}},
			{field:'resName',title:'节点名称',width:80,formatter:function(value,rows,index){return rows.resName;}},
			//{field:'parentResId',formatter:function(value,rows,index){return rows.parentResId;}},
			{field:'parentResName',title:'父节点名称',width:80,formatter:function(value,rows,index){return rows.parentResName;}},
			//{field:'userType.typeId',formatter:function(value,rows,index){return rows.userType.typeId;}},
			{field:'userType.typeName',title:'访问类别',width:60,formatter:function(value,rows,index){return rows.userType.typeName;}},
			{field:'description',title:'资源描述',width:100,formatter:function(value,rows,index){return rows.description;}},
			{field:'href',title:'资源地址',width:200,formatter:function(value,rows,index){return rows.href;}},
			
			{field:'leaf',title:'子节点',width:50,formatter:function(value,rows,index){
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
			}},
			//{field:'priority',formatter:function(value,rows,index){return rows.priority;}}
		]],
		toolbar: [{
			text:'添加',
			iconCls: 'icon-add',
			handler: function(){open();}
		},{
			text:'编辑',
			iconCls: 'icon-edit',
			handler: function(){edit();}
		},{
			text:'删除',
			iconCls: 'icon-remove',
			handler: function(){remove();}
		} ,{
			text:'<input id="menusSearchBox"/>'
		}]
	});
	$('#menusSearchBox').searchbox({
		width:300,
		prompt:'请输入节点名称',
	    searcher:function(value,name){
	    	if(value == '' || value==null){
	    		$('#menusTable').datagrid('options').url='${pageContext.request.contextPath}/json/menus!list.do';
	    	}else{
	    		$('#menusTable').datagrid('options').url='${pageContext.request.contextPath}/json/menus!queryByResName.do';
	    	}
			$('#menusTable').datagrid('load',{
				'menus.resName':value
			});
	    }
	});
});

function open(){
	console.info('open');
	$('#addMenusForm_typeId').combogrid({
		panelWidth:150,
		idField:'typeId',  
		textField:'typeName',
		url:'${pageContext.request.contextPath}/json/userT!load.do',  
		columns:[[{field:'typeName',title:'访问权限',width:130}]],
		onChange:function(oldValue,newValue){
			$('#addMenusForm_parentResId').combotree('clear');
			var g = $('#addMenusForm_typeId').combogrid('grid');
			var row = g.datagrid('getSelected');
			//加载树结构
			httpSender('json/menus!queryResConstruct.do',false,'menus.userType.typeId='+row.typeId,false,function(data){
				var menu = jQuery.parseJSON(data);
				$('#addMenusForm_parentResId').combotree('loadData',menu.info);
			});
			
			//加载优先级
			loadPriority('addMenusForm_priority','${pageContext.request.contextPath}/json/menus!queryPriority.do?menus.userType.typeId='+row.typeId);
		}
	});
	openDialog('addMenus');
}

function edit(){
	var row = $('#menusTable').datagrid('getSelected'); 
	if(row){
		$('#editMenusForm_resId').val(row.resId);
		$('#editMenusForm_typeId').combogrid({
			panelWidth:150,
			idField:'typeId',  
			textField:'typeName',
			url:'${pageContext.request.contextPath}/json/userT!load.do',  
			columns:[[{field:'typeName',title:'访问权限',width:130}]],
			onShowPanel:function(){
				$('#editMenusForm_parentResId').combotree('clear');
				var g = $('#editMenusForm_typeId').combogrid('grid');
				var row = g.datagrid('getSelected');
				//加载树结构
				httpSender('json/menus!queryResConstruct.do',false,'menus.userType.typeId='+row.typeId,false,function(data){
					var menu = jQuery.parseJSON(data);
					$('#editMenusForm_parentResId').combotree('loadData',menu.info);
				});
				//加载优先级
				loadPriority('editMenusForm_priority','${pageContext.request.contextPath}/json/menus!queryPriority.do?menus.userType.typeId='+row.typeId);
			}
		});
		openDialog('editMenus');
		//加载树结构
		httpSender('json/menus!queryResConstruct.do',false,'menus.userType.typeId='+row.userType.typeId,false,function(data){
			var menu = jQuery.parseJSON(data);
			$('#editMenusForm_parentResId').combotree('loadData',menu.info);
		});
		//加载优先级
		loadPriority('addMenusForm_priority','${pageContext.request.contextPath}/json/menus!queryPriority.do?menus.userType.typeId='+row.typeId);
		$('#editMenusForm').form('load',row);
		$('#editMenusForm_typeId').combogrid('setValue',row.userType.typeId);
		$('#editMenusForm_parentResId').combotree('setValue',row.parentResName);
		$('#editMenusForm_priority').combogrid('setValue',row.priority);
	}else{
		$.messager.alert('警告','请选择记录');
	}
}

function save(){
	saveOrUpdateRow('addMenus','menusTable');
}

function update(){
	saveOrUpdateRow('editMenus','menusTable');
}
	
function cancel(id){
	$('#'+id).dialog('close');
	$('#addMenusForm').form('clear');
}

function remove(){
	var row = $('#menusTable').datagrid('getSelected'); 
	if(row){
		delRow('menusTable','json/menus!remove.do','menus.resId='+row.resId);
	}else{
		$.messager.alert('警告','请选择记录');
	}
}

</script>
<div class="easyui-layout">
	<table id="menusTable" data-options="width:setWidthPX(0.60),height:setHeightPX(0.75)"></table>
	<div id="addMenusDialog" class="easyui-dialog" title="添加菜单" data-options="closed:true,modal:true,buttons:'#addMenusDialogBtn'" style="width:400px;height:400px;">
		<form id="addMenusForm" method="post" action="${pageContext.request.contextPath}/json/menus!save.do">
			<table>
				<tr>
       				<td width="35%" align="right">资源名称：</td>
       				<td><input align="center" type="text" id="addMenusForm_resName" name="resName" class="easyui-validatebox" style="width:155px" required="true" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">资源描述：</td>
       				<td><input align="center" type="text" id="addMenusForm_description" name="description" class="easyui-validatebox" style="width:155px" required="true" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">资源地址：</td>
       				<td><input align="center" type="text" id="addMenusForm_href" name="href" class="easyui-validatebox" style="width:155px"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">访问权限：</td>
       				<td><select id="addMenusForm_typeId" align="center" name="userType.typeId" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
				<tr>
       				<td width="35%" align="right">&nbsp;&nbsp;父节点：</td>
       				<td><select id="addMenusForm_parentResId" align="center" name="parentResId" class="easyui-combotree" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">&nbsp;&nbsp;优先级：</td>
       				<td><select id="addMenusForm_priority" align="center" name="priority" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
			</table>
		</form>
	</div>
	<div id="addMenusDialogBtn">
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('addMenus')">取消</a>
	</div>
	
	<!-- 编辑 -->
	<div id="editMenusDialog" class="easyui-dialog" title="编辑菜单" data-options="closed:true,modal:true,buttons:'#editMenusDialogBtn'" style="width:400px;height:400px;">
		<form id="editMenusForm" method="post" action="${pageContext.request.contextPath}/json/menus!update.do">
			<input id="editMenusForm_resId" name="resId" type="hidden">
			<table>
				<tr>
       				<td width="35%" align="right">资源名称：</td>
       				<td><input align="left" type="text" id="editMenusForm_resName" name="resName" class="easyui-validatebox" style="width:155px" required="true" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">资源描述：</td>
       				<td><input align="center" type="text" id="editMenusForm_description" name="description" class="easyui-validatebox" style="width:155px" required="true" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">资源地址：</td>
       				<td><input align="center" type="text" id="editMenusForm_href" name="href" class="easyui-validatebox" style="width:155px"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">访问权限：</td>
       				<td><select id="editMenusForm_typeId" align="center" name="userType.typeId" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
				<tr>
       				<td width="35%" align="right">&nbsp;&nbsp;父节点：</td>
       				<td><select id="editMenusForm_parentResId" align="center" name="parentResId" class="easyui-combotree" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">&nbsp;&nbsp;优先级：</td>
       				<td><select id="editMenusForm_priority" align="center" name="priority" class="easyui-combogrid" editable="false" style="width:156px" required="true"></select></td>
       			</tr>
			</table>
		</form>
	</div>
	<div id="editMenusDialogBtn">
		<a id="editBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="update()">更新</a>
		<a id="editBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('editMenus')">取消</a>
	</div>
</div>