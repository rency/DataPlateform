$(function(){
	$('#keyword').focus();
	$('#keyword').keydown(function(e){
		if(e.keyCode==13){
			search($('#pageNo').val());
		}
	});
	//输入框变化事件
	/*$('#keyword').on('input',function(e){
		search($('#pageNo').val());
	});	*/
	$('#searchBtn').click(function(){
		search($('#pageNo').val());
	});
	/*if($('#keyword').val().length > 0){
		search($('#pageNo').val());
	}*/
	
	$('#searchFooter td div').hover(function(){
		$(this).css('border','1px solid #38F');
	},function(){
		$(this).css('border','1px solid white');
	});
});

function showLogin(){
	if($('#loginPanel').is(':visible')){
		return;
	}
	var top = $(window).height()/2;
	var left = ($(window).width()/2)-($('#loginPanel').width()/2);
	$('body').css('background','#ccc');
	$('#loginPanel').css({top:top,left:left,background:'#FFF'});
	$('#loginPanel').show();
}

function closeLoginPanel(){
	$('#loginPanel').hide();
	$('body').css('background','#FFF');
	$('#loginPanel form')[0].reset();
}

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

/**
 * 提交搜索
 * @param pageNo
 */
function search(pageNo){
	if(pageNo > 0){
		$('#pageNo').val(pageNo);
	}
	$('#searchResult').empty();
	var keyword = $('#keyword').val();
	if(keyword.length == 0){
		return;
	}
	$('#searchForm').submit();
}



/**
 * 显示页脚分页
 * @param data
 */
function showFooter(data){
	$('#searchFooter table tr').empty();
	//清空原有值
	$('#pageNo').val('');
	//赋值
	$('#pageNo').val(data.pageNo);
	
	var totalPage = parseInt((data.totalCount % data.pageSize) == 0 ? parseInt(data.totalCount / data.pageSize):parseInt(data.totalCount / data.pageSize)+1);
	//console.info("pageNo:"+data.pageNo+"-pageSize:"+data.pageSize+"-totalCount:"+data.totalCount+"-totalPage:"+totalPage);
	var htmlTemplate = '';
	
	if(data.pageNo > 1){
		htmlTemplate += '<td><div style="cursor:pointer;" onclick="search('+(data.pageNo - 1)+')">上一页</div></td>';
	}
	
	for(var index=0;index<totalPage;index++){
		var page = index+1;
		if(page == data.pageNo){
			htmlTemplate += '<td><div>'+page+'</div></td>';
		}else{
			htmlTemplate += '<td><div style="cursor:pointer;" onclick="search('+page+')">'+page+'</div></td>';
		}
	}
	
	if(data.pageNo < totalPage){
		htmlTemplate += '<td><div style="cursor:pointer;" onclick="search('+(data.pageNo+1)+')">下一页</div></td>';
	}
	$('#searchFooter table tr').append(htmlTemplate);
}