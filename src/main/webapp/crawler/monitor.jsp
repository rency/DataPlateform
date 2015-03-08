<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$('#crawlerTable').datagrid({
		url:'${pageContext.request.contextPath}/json/crawler!loadAll.do',
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
			{field:'crawlerId',title:'爬虫编号',width:120,formatter:function(value,rows,index){return rows.crawlerId;}},
			{field:'crawlerName',title:'爬虫名称',width:60,formatter:function(value,rows,index){return rows.crawlerName;}},
			{field:'crawlerDesc',title:'爬虫描述',width:100,formatter:function(value,rows,index){return rows.crawlerDesc;}},
			{field:'remoteAddr',title:'远程地址',width:60,formatter:function(value,rows,index){return rows.remoteAddr;}},
			{field:'remotePort',title:'远程端口',width:30,formatter:function(value,rows,index){return rows.remotePort;}},
			{field:'initAddr',title:'起始地址',width:100,formatter:function(value,rows,index){return rows.initAddr;}},
			{field:'poolSize',title:'爬虫数量',width:30,formatter:function(value,rows,index){return rows.poolSize;}},
			{field:'createTime',title:'创建时间',width:70,formatter:function(value,rows,index){
				var timeStr = rows.createTime;
				timeStr = timeStr.replace('T',' ');
				return timeStr;
			}},
			{field:'status',title:'状态',width:30,formatter:function(value,rows,index){
				if(rows.status){
					return '运行中';
				}else{
					return '未运行';
				}
			}},
			{field:'operator',title:'操作',width:40,formatter:function(value,rows,index){
				return rows.status?'<input type="button" id="stopBtn" onclick=stop("'+rows.crawlerId+'") value="停止">':'<input type="button" id="starBtn" onclick=start("'+rows.crawlerId+'") value="启动">';
			}},
			{field:'maintain',title:'爬虫数据维护',width:45,formatter:function(value,rows,index){
				return rows.status?'请先停止爬虫':'<input type="button" id="clearQueue" onclick=clearQueue("'+rows.crawlerId+'") value="清除队列">';
			}}
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
		},{
			text:'<input id="crawlerSearchBox"/>'
		}]
	});
	$('#crawlerSearchBox').searchbox({
		width:300,
		prompt:'请输入爬虫名称',
	    searcher:function(value,name){
	    	$('#crawlerTable').datagrid('options').url='${pageContext.request.contextPath}/json/crawler!queryByName.do';
			$('#crawlerTable').datagrid('load',{
				'crawlerConfig.crawlerName':value
			});
	    }
	});
});

function open(){
	openDialog('addCrawler');
}

function edit(){
	var row = $('#crawlerTable').datagrid('getSelected'); 
	if(row){
		if(row.status && row.status=='true'){
			$.messager.alert('错误','此爬虫正在运行，无法修改爬虫信息，请稍后再试！');
		}else{
			openDialog('editCrawler');
			$('#editCrawlerForm_crawlerId').val(row.crawlerId);
			$('#editCrawlerForm_crawlerName').val(row.crawlerName);
			$('#editCrawlerForm_crawlerDesc').val(row.crawlerDesc);
			$('#editCrawlerForm_remoteAddr').val(row.remoteAddr);
			$('#editCrawlerForm_remotePort').val(row.remotePort);
			$('#editCrawlerForm_poolSize').val(row.poolSize);
			$('#editCrawlerForm_initAddr').val(row.initAddr);
		}
	}else{
		$.messager.alert('警告','请选择记录');
	}
}

function start(crawlerId){
	$.messager.confirm('确认','确定要启动该爬虫吗？',function(r){
		if(r){
			httpSender('json/crawler!start.do',true,'crawlerConfig.crawlerId='+crawlerId,true,function(data){
				data = jQuery.parseJSON(data);
				if(data.key && data.key=='N'){
					showTip(data.info);
					$('#crawlerTable').datagrid('reload'); 
				}else{
					errorHandle(data);
				}
			});
		}
	});
}

function stop(crawlerId){
	$.messager.confirm('确认','确定要停止该爬虫吗？',function(r){
		if(r){
			httpSender('json/crawler!stop.do',true,'crawlerConfig.crawlerId='+crawlerId,true,function(data){
				data = jQuery.parseJSON(data);
				if(data.key && data.key=='N'){
					showTip(data.info);
					$('#crawlerTable').datagrid('reload'); 
				}else{
					errorHandle(data);
				}
			});
		}
	});
}

function clearQueue(crawlerId){
	$.messager.confirm('确认','确定要停止该爬虫任务队列吗？',function(r){
		if(r){
			httpSender('json/crawler!clearQueue.do',true,'crawlerConfig.crawlerId='+crawlerId,true,function(data){
				data = jQuery.parseJSON(data);
				if(data.key && data.key=='N'){
					showTip(data.info);
					$('#crawlerTable').datagrid('reload'); 
				}else{
					errorHandle(data);
				}
			});
		}
	});
}

function save(){
	saveOrUpdateRow('addCrawler','crawlerTable');
}

function update(){
	saveOrUpdateRow('editCrawler','crawlerTable');
}

function remove(){
	var row = $('#crawlerTable').datagrid('getSelected'); 
	if(row){
		delRow('crawlerTable','json/crawler!remove.do','crawlerConfig.crawlerId='+row.crawlerId);
	}else{
		$.messager.alert('警告','请选择记录');
	}
}

</script>
<div class="easyui-layout">
	<table id="crawlerTable" data-options="width:setWidthPX(0.60),height:setHeightPX(0.75)"></table>
	<div id="addCrawlerDialog" class="easyui-dialog" title="新建爬虫" data-options="closed:true,modal:true,buttons:'#addCrawlerDialogBtn'" style="width:400px;height:300px;">
		<form id="addCrawlerForm" method="post" action="${pageContext.request.contextPath}/json/crawler!add.do">
			<table>
				<tr>
       				<td width="35%" align="right">爬虫名称：</td>
       				<td align="left"><input align="center" type="text" id="addCrawlerForm_crawlerName" name="crawlerConfig.crawlerName" class="easyui-validatebox" style="width:150px" required="true" validType="name"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">爬虫描述：</td>
       				<td align="left"><input align="center" type="text" id="addCrawlerForm_crawlerDesc" name="crawlerConfig.crawlerDesc" class="easyui-validatebox" style="width:150px"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">远程地址：</td>
       				<td align="left"><input align="center" type="text" id="addCrawlerForm_remoteAddr" name="crawlerConfig.remoteAddr" class="easyui-validatebox" style="width:150px" required="true" validType="ip"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">远程端口：</td>
       				<td align="left"><input align="center" type="text" id="addCrawlerForm_remotePort" name="crawlerConfig.remotePort" class="easyui-validatebox" style="width:150px" required="true" validType="integer"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">爬虫数量：</td>
       				<td align="left"><input align="center" type="text" id="addCrawlerForm_poolSize" name="crawlerConfig.poolSize" class="easyui-validatebox" style="width:150px" required="true" validType="integer"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">起始地址：</td>
       				<td align="left"><input align="center" type="text" id="addCrawlerForm_initAddr" name="crawlerConfig.initAddr" class="easyui-validatebox" style="width:150px" required="true"/></td>
       			</tr>
			</table>
		</form>
	</div>
	<div id="addCrawlerDialogBtn">
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">保存</a>
		<a id="addBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('addCrawler')">取消</a>
	</div>
	<div id="editCrawlerDialog" class="easyui-dialog" title="修改爬虫" data-options="closed:true,modal:true,buttons:'#editCrawlerDialogBtn'" style="width:400px;height:300px;">
		<form id="editCrawlerForm" method="post" action="${pageContext.request.contextPath}/json/crawler!update.do">
			<table>
				<tr>
       				<td width="35%" align="right">爬虫名称：</td>
       				<td align="left"><input align="center" type="text" id="editCrawlerForm_crawlerName" name="crawlerConfig.crawlerName" class="easyui-validatebox" style="width:150px" required="true" validType="name"/><input type="hidden" id="editCrawlerForm_crawlerId" name="crawlerConfig.crawlerId" /></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">爬虫描述：</td>
       				<td align="left"><input align="center" type="text" id="editCrawlerForm_crawlerDesc" name="crawlerConfig.crawlerDesc" class="easyui-validatebox" style="width:150px" required="true" validType="name"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">远程地址：</td>
       				<td align="left"><input align="center" type="text" id="editCrawlerForm_remoteAddr" name="crawlerConfig.remoteAddr" class="easyui-validatebox" style="width:150px" required="true" validType="ip"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">远程端口：</td>
       				<td align="left"><input align="center" type="text" id="editCrawlerForm_remotePort" name="crawlerConfig.remotePort" class="easyui-validatebox" style="width:150px" required="true" validType="integer"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">爬虫数量：</td>
       				<td align="left"><input align="center" type="text" id="editCrawlerForm_poolSize" name="crawlerConfig.poolSize" class="easyui-validatebox" style="width:150px" required="true" validType="integer"/></td>
       			</tr>
       			<tr>
       				<td width="35%" align="right">起始地址：</td>
       				<td align="left"><input align="center" type="text" id="editCrawlerForm_initAddr" name="crawlerConfig.initAddr" class="easyui-validatebox" style="width:150px" required="true"/></td>
       			</tr>
			</table>
		</form>
	</div>
	<div id="editCrawlerDialogBtn">
		<a id="editBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="update()">保存</a>
		<a id="editBtn" href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog('editCrawler')">取消</a>
	</div>
</div>