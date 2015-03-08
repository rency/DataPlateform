/**
 * 系统域名变量
 */
var project = "DataPlateform";
var httpLocation = window.location.href;
httpLocation = httpLocation.substring(0, httpLocation.indexOf(project)+project.length)+"/";


/**发送Http请求
* @param {} actionName URL地址
* @param {} isPost请求方式
* @param {} params 请求参数
* @param {} isAsync 是否异步请求
* @param {} callBack 回调函数function(data,textStatus){}
*/
function httpSender(actionName,isPost,params,isAsync,callBack){
	$.ajax({
		type:isPost?'POST':'GET',
		url:httpLocation + actionName,
		data:params,
		//timeout:5000,
		async:isAsync,
		//dataType:'json',
		error:errorHandle,
		success:callBack,
		statusCode: {
			404: function() {
				$.messager.alert("page not found,please contact us.");
			}
		}
	});	
}

/**
 * 打开Dialog并清除表单数据
 * @param id
 */
function openDialog(id){
	$('#'+id+'Form').form('clear');
	$('#'+id+'Dialog').dialog('open');
}

/**
 * 关闭Dialog，病清除表单数据
 * @param id
 */
function closeDialog(id){
	$('#'+id+'Dialog').dialog('close');
	$('#'+id+'Form').form('clear');
}

/**
 * 鼠标提示文字
 */
function mouseTipTxt(data){
	if(data == null || data == ''){
		return;
	}
	var titleData = removeHTMLTag(data);
	return "<a href='javascript:return(0)' title="+titleData+" class='easyui-tooltip'>"+data+"</a>";
}

/**
 * 鼠标提示图片
 * @param path
 * @returns {String}
 */
function mouseTipImg(path){
	if(path == "" || path == null ){
		return;
	}
	while(path.indexOf('\\') != -1){
		path = path.replace('\\','/');
	}
	var fileName;
	if(path.indexOf('/') != -1){
		fileName = path.substring(path.lastIndexOf('/')+1,path.length);
		if(path.indexOf('\\') != -1){
			fileName = path.substring(path.lastIndexOf('\\')+1,path.length);
		}
	}else if(path.indexOf('\\') != -1){
		fileName = path.substring(path.lastIndexOf('\\')+1,path.length);
		if(path.indexOf('/') != -1){
			fileName = path.substring(path.lastIndexOf('/')+1,path.length);
		}
	}else{
		fileName = path;
	}
	 
	return "<a onmouseover=showImg('"+path+"',event) onmouseout=hideImg() href='javascript:return(0)' class='easyui-tooltip'>"+fileName+"</a>";
}

/**
 * 鼠标提示图片
 * @param path
 * @param e
 */
function showImg(path,e){
	var imgFrame ="<div id='imgTip' style='background:url(images/loading.gif)' ><img src='"+path+"' alt='加载中' width='150' height='150' /></div>";
	$("#tabs").append(imgFrame); //把它追加到文档中     
    $("#imgTip").css({"top": e.clientY-90,"left": e.clientX-175,"display":"block","position":"absolute"});
    $("#imgTip").fadeTo(600,0.9);
}

function hideImg(){
	$("#imgTip").remove();
}

function getY(id){
	return $('#'+id).position().top;
}

function getX(id){
	return $('#'+id).position().left;
}

/**
 * 判断字符串是文本还是图片
 * @param data
 * @returns {String}
 */
function opinion(data){
	var imgRegex=/([^\s]+(?=\.(jpg|gif|png|ico))\.\2)/gi;
	var reg = new RegExp(imgRegex);
	if(reg.test(data)){
		return mouseTipImg(data);
	}else{
		return mouseTipTxt(data);
	}
}

/**
 * 设置百分比宽度
 * width:setWidthPX(0.60);
 */
function setWidthPX(percent){
	return document.body.clientWidth*percent;
}
/**
 * 设置百分比高度
 * height:setHeightPX(0.60);
 * @param percent
 * @returns {Number}
 */
function setHeightPX(percent){
	return document.body.clientHeight*percent;
}

/**
 * 加载优先级下拉框
 * @param id
 * @param url
 */
function loadPriority(id,url){
	$('#'+id).combogrid({
		panelWidth:156,
		idField:'priority',
		textField:'priority',
		url:url,
		columns:[[{field:'priority',title:'可选优先级',width:130}]] 
	}); 
}

/**
 * 添加、更新选中记录
 * @param id
 * @param tableId
 */
function saveOrUpdateRow(id,tableId){
	var formId = id+'Form';
	var valid = $('#'+formId).form('validate');
	if(!valid){
		$.messager.alert('警告','请填写必要信息','info');
		return;
	}
	$('#'+formId).form('submit',{
		success: function(data){
			data = jQuery.parseJSON(data);
			if(data.key == "N" ){
				$.messager.alert('提示',data.info,'info');
				$('#'+tableId).datagrid('reload');
				closeDialog(id);
			}else{
				errorHandle(data);
			}
		}
	});
}

/**
 * 删除选中记录
 * @param tableId
 * @param url
 * @param param
 */
function delRow(tableId,url,param){
	$.messager.confirm('确认','确定删除？',function(r){
	    if (r){
	    	$.messager.progress();
	    	httpSender(url,true,param,true,function(data){
	    		data = jQuery.parseJSON(data);
	    		$.messager.progress('close');
				if(data.key=='N'){
					$.messager.alert('提示','删除成功','info');
					$('#'+tableId).datagrid('reload');
				}else{
					errorHandle(data);
				}
			});
	    }
	});
}

/**
 * 右下角弹出系统提示
 * @param message 提示信息
 */
function showTip(message){
	$.messager.show({title:'系统提示',msg:message,timeout:5000,height:'auto',showType:'fade'});
}

/**
 * 系统错误处理
 * @param errorThrown
 * @param data json
 */
function errorHandle(data){
	$.messager.progress('close');
	if(data.key && data.key=='E'){
		if(data.info == "timeout"){
			alert("登录超时，请重新登陆");
			self.location.href=httpLocation+'user!loginInput.do';
		}else if(data.info == "denied"){
			$.messager.alert("警告","权限不足！",'error');
		}else{
			$.messager.alert('错误',data.info,'error');
		}
	}else{
		$.messager.alert('错误',"系统未知错误，请稍后重试或者联系管理员!",'error');
	}
}

/**
 * datagrid加载数据为空时显示
 * @param data
 */
function showEmptyInfo(data){
	if(data.key){
		if(data.key=='N'){
			if(data.rows.length ==0){
				showTip('无数据显示');
			}
		}else{
			errorHandle(data);
		}
	}
	
}

function getJSessionId(){
	var c_name = 'JSESSIONID';
	if(document.cookie.length>0){
	  c_start=document.cookie.indexOf(c_name + "=");
	  if(c_start!=-1){ 
	    c_start=c_start + c_name.length+1;
	    c_end=document.cookie.indexOf(";",c_start);
	    if(c_end==-1) c_end=document.cookie.length;
	    return unescape(document.cookie.substring(c_start,c_end));
	  }
	}
}

/**
 * 显示当前时间
 * @param id 标签ID
 */
function showTime(id) {
	var today, hour, second, minute, year, month, date;
	var strDate = '';
	today = new Date();
	var n_day = today.getDay();
	switch (n_day) {
			case 0 : {
		      strDate = "星期日";
		    }
		      break;
		    case 1 : {
		      strDate = "星期一";
		    }
		      break;
		    case 2 : {
		      strDate = "星期二";
		    }
		      break;
		    case 3 : {
		      strDate = "星期三";
		    }
		      break;
		    case 4 : {
		      strDate = "星期四";
		    }
		      break;
		    case 5 : {
		      strDate = "星期五";
		    }
		      break;
		    case 6 : {
		      strDate = "星期六";
		    }
		      break;
		    case 7 : {
		      strDate = "星期日";
		    }
		      break;
	}
	year = today.getFullYear();
	month = today.getMonth() + 1;
	date = today.getDate();
	hour = today.getHours();
	minute = today.getMinutes();
	second = today.getSeconds();
	if(minute <= 9){
		minute = "0" + minute;
	}
	if(second <= 9){
		second = "0" + second;
	}
	$('#'+id).html(year + "年" + month + "月" + date + "日 " + strDate + " " + hour + ":" + minute + ":" + second); // 显示时间
	setTimeout("showTime('"+id+"');", 1000); // 设定函数自动执行时间为 1000 ms(1 s)
}

