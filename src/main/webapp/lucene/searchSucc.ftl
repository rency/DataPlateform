<#import "/WEB-INF/ftl/header.ftl" as app/>
<html>
<@app.head></@app.head>
<body>
<div class="main">
	<div class="search_top">
		<ul>
			<li><a href="${app.basePath}news!home">新闻</a></li>
			<li><a href="${app.basePath}maps!home">地图</a></li>
			<li><a href="javascript:return(0)" onclick="showLogin()">登录</a></li>
		</ul>
	</div>
	<div id="searchDiv" style="padding:30px 0px 0px 10px;">
		<@app.searchForm></@app.searchForm>
	</div>
	<div id="search_banner">
		<span><@s.text name="front.subject"/> 为您找到大约 ${pqr.totalCount} 条相关记录</span>
	</div>
	<div id="search_middle">
		<ul>
		<#list pqr.list as item>
			<li>
				<div class="search_middle_content">
					<div class="search_middle_content_top">
						<a target="_blank" href="${item.url}">${item.title}<a>
					</div>
					<div class="search_middle_content_txt">
						<#if item.content?length gt 150>
							<span>${item.content[0..150]}......</span>
						<#else>
							<span>${item.content}</span>
						</#if>
					</div>
					<div class="search_middle_content_middle">
						<span>${item.url}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.modifyDate?string("yyyy-MM-dd HH:mm:ss")}</span>
					</div>
				</div>
			</li>
		</#list>
		</ul>
	</div>
	<div id="search_footer">
		<ul>
			<li><div style="cursor:pointer;width:50px;" onclick="search('+(pqr.pageNo - 1)+')"><上一页</div></li>
			<#list 1..pqr.totalCount as item>
				<#if pqr.pageNo = (item_index+1)>
					<li><div>${pqr.pageNo}</div></li>
				<#else>
					<#if>
					<#else>
					</#if>
					<li><div style="cursor:pointer;" onclick="search('${item_index+1}')">${item_index+1}</div></li>
				</#if>
			</#list>
			<li><div style="cursor:pointer;width:50px;" onclick="search('+(pqr.pageNo+1)+')">下一页></div></li>
		</ul>
	</div>
</div>
<@app.loginPanel></@app.loginPanel>
</body>
</html>