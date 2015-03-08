<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$('#usertTable').edatagrid({
		url:'${pageContext.request.contextPath}/json/userT!load.do',
		saveUrl:'${pageContext.request.contextPath}/json/userT!save.do',
		updateUrl:'${pageContext.request.contextPath}/json/userT!update.do',
		destroyUrl:'${pageContext.request.contextPath}/json/userT!remove.do'
	});
});
</script>
<div class="easyui-layout">
	<table id="usertTable" toolbar="#toolbar" data-options="width:setWidthPX(0.60),height:setHeightPX(0.75),fitColumns:false,singleSelect:true,pagination:true,rownumbers:true,border:false,loadMsg:'获取数据中，请稍等...',pageList:[20,30,40]" idField="typeId">
    	<thead>
        	<tr>
            	<th field="typeId" hidden="true"></th>
            	<th field="typeName" width="200" editor="text">用户类别名称</th>
            	<th field="createTime" width="120" editor="">创建时间</th>
        	</tr>
    	</thead>
	</table>
</div>
<div id="toolbar">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:$('#usertTable').edatagrid('addRow')">新增</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#usertTable').edatagrid('destroyRow')">删除</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#usertTable').edatagrid('saveRow')">保存</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#usertTable').edatagrid('cancelRow')">取消</a>
</div>