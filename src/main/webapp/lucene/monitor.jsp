<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	
});
function start(){
	$.messager.confirm("提示","确定要启动创建索引服务吗？",function(r){
		if(r){
			httpSender('json/lucene!startIndex.do',true,null,true,function(data){
				data = jQuery.parseJSON(data);
				if(data.key && data.key=='N'){
					showTip(data.info);
				}else{
					errorHandle(data);
				}
			});
		}
	});
}
function stop(){
	$.messager.confirm("提示","确定要停止索引服务吗？",function(r){
		if(r){
			httpSender('json/lucene!startIndex.do',true,null,true,function(data){
				data = jQuery.parseJSON(data);
				if(data.key && data.key=='N'){
					showTip(data.info);
				}else{
					errorHandle(data);
				}
			});
		}
	});
}
</script>
<div class="easyui-layout">
	<div style="padding:5px 0px;">
		<a href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-start'" style="width:80px;" onclick="start()">启动</a>
		<a href="javascript:return(0)" class="easyui-linkbutton" data-options="iconCls:'icon-stop'" style="width:80px;" onclick="stop()">停止</a>
	</div>
</div>