$(function(){
	// 禁止页面右键菜单
	$(document).bind("contextmenu", function() {
		return false;
	});
	showTime('currentTime');
});

function loadMenu(menu){
	var data = jQuery.parseJSON(menu);
	if(data.key && data.key=="E"){
		errorHandle(data);
		return;
	}
	$("#tree").tree({
		data:data.info,
		lines:true,
		animate:true,
		method:'GET',
		onClick:function(node) {
			 if(node.attributes) {
				 Open(node.id,node.text, node.attributes.url);
			} 
		}
	});
	
	function Open(tabId,text, url) {
		if($("#tabs").tabs('exists',text)){
			$('#tabs').tabs('select', text);
			tab = $('#tabs').tabs('getSelected');
			tab.panel('refresh');
		}else{
			if(null == url || "" == url){
				return;
			}
			$('#tabs').tabs('add',{
				title : text,
				closable : true,
                href : url,
				cache : false 
			});
		}
	}
	//绑定tabs的右键菜单
	$("#tabs").tabs({
		onContextMenu : function(e, title) {
			e.preventDefault();
			$('#tabsMenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			}).data("tabTitle", title);
		}
	});

	//实例化menu的onClick事件
	$("#tabsMenu").menu({
		onClick:function(item){
			CloseTab(this, item.name);
		}
	});

	//几个关闭事件的实现
	function CloseTab(menu, type) {
		var curTabTitle = $(menu).data("tabTitle");
		var tabs = $("#tabs");

		if (type === "close") {
			tabs.tabs("close", curTabTitle);
			return;
		}

		var allTabs = tabs.tabs("tabs");
		var closeTabsTitle = [];

		$.each(allTabs, function() {
			var opt = $(this).panel("options");
			if (opt.closable && opt.title != curTabTitle
					&& type === "Other") {
				closeTabsTitle.push(opt.title);
			} else if (opt.closable && type === "All") {
				closeTabsTitle.push(opt.title);
			}
		});

		for ( var i = 0; i < closeTabsTitle.length; i++) {
			tabs.tabs("close", closeTabsTitle[i]);
		}
	}

}

/**
 * 监听键盘回车事件
 * @param id
 * @param formId
 */
function enterKeyPress(id,callback){
	$('#'+id).keydown(function(e){
		if(e.keyCode==13){
			login();
		}
	});
}

function login(){
	var username = $('#loginForm_username').val();
	var pwd = $('#loginForm_password').val();
	if(username.length <= 0 && pwd.length <= 0){
		$.messager.alert("错误","请输入用户名、密码","error");
		$('#loginForm_username').focus();
		return;
	}
	
	$('#password').val(pwd);
	$('#loginForm').attr('action',httpLocation+'user!login.do');
	$('#loginForm').submit();
}

function reset(){
	$('#loginForm').reset();
}
/**
 * 解析服务器短推送的消息
 * @param event
 */
function pushMessage(message){
	showTip(message);
}