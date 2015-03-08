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
	<div id="searchDiv" style="padding:100px 0px 0px 124px;">
		<@app.searchForm></@app.searchForm>
	</div>
</div>
<@app.loginPanel></@app.loginPanel>
</body>
</html>