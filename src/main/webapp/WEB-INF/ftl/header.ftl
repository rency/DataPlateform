<#assign s=JspTaglibs["/WEB-INF/tld/struts-tags.tld"] />
<#assign basePath=request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()?replace(",","")+ request.getContextPath() + "/">
<#macro head>
<meta http-equiv="X-UA-Compatible" content="Edge">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="data analyzer">
<meta http-equiv="description" content="data plateform,data analyzer" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${basePath}script/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${basePath}script/functions.js"></script>
<script type="text/javascript" src="${basePath}script/search.js"></script>
<link rel="stylesheet" type="text/css" href="${basePath}styles/search.css">
<link rel="SHORTCUT ICON" href="${basePath}images/favicon.ico" />
<title><#if keyword?exists>${keyword!} - </#if><@s.text name="front.subject"/></title>
</#macro>
<#macro loginPanel>
<div id="loginPanel">
	<div class="loginPanel_top">
		<table width="100%">
			<tr>
				<td><span><@s.text name="front.login"/></span></td>
				<td></td>
				<td></td>
				<td align="right"><span style="cursor:pointer;" onclick="closeLoginPanel()">关闭</span></td>
			</tr>
			<tr>
				<td></td>
			</tr>
		</table>
	</div>
	<form action="login">
		<table width="100%">
			<tr>
				<td align="right"><@s.text name="label.username" /></td>
				<td align="left"><input id="loginForm_username" name="j_username" type="text" style="width:150px"/></td>
			</tr>
			<tr>
				<td align="right"><@s.text name="label.password" /></td>
				<td align="left">
					<input id="loginForm_password" name="user.password" type="password" style="width:150px" onfocus="enterKeyPress('loginForm_password',login)" />
					<input id="password" name="j_password" type="hidden" />
				</td>
			</tr>
			<tr>
				<td align="left" colspan="2">
					<div style="padding-left:52px;font-size:12px;vertical-align:bottom;"><input id="remeberMe" type="checkbox" style=""><span>下次自动登录</span></div>
				</td>
			</tr>
			<tr align="center">
				<td colspan="2">
					<input type="submit" id="loginBtn" value="<@s.text name='btn.login' />"> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="reset" id="resetBtn" value="<@s.text name='btn.reset' />">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr align="center"><td colspan="2"><font color="red" size="3"><@s.property value="fieldErrors.message[0]"/></font></td></tr>
			<tr>
				<td colspan="2">
					<div style="font-size:12px;">可以使用以下方式登录</div>
					<div>
						<ul>
							<li></li>
						</ul>
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>
</#macro>
<#macro searchForm>
<form id="searchForm" action="${app.basePath}front/lucene!searchDo.do" method="post">
	<table>
		<tr>
			<td><span class="search_title"><h1>易&nbsp;&nbsp;搜</h1></span></td>
			<td><input id="keyword" name="keyword" type="text" class="search_input" maxlength="100" value="<#if keyword?exists>${keyword!}</#if>"/></td>
			<td><input id="searchBtn" type="button" value="搜&nbsp;&nbsp;&nbsp;&nbsp;索" class="search_btn"/></td>
		</tr>
	</table>
</form>
</#macro>