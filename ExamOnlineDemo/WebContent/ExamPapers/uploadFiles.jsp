<%@ page language="java" import="java.util.*,cc.*,javax.servlet.*" contentType="text/html; charset=utf-8"%>
<html>
<head>
<title>Insert title here</title>
<link href="../css/bootstrap.css" rel='stylesheet' type='text/css' />
<link href="../css/importQuestions.css" rel='stylesheet' type='text/css' />
<style type="text/css">
	html, body {
		font-family: 'Lato', 微软雅黑;
		font-size: 100%;
		background: #fff;
	}
	body {
		padding-top: 70px;
	}
	.paperClassList{
	    padding: 5px 0;
	    margin: 0 0 0 5em; 
	} 
	#paperTypeList{
		position: relative;
	    display: block;
	    padding: 10px 15px;
	    margin-bottom: -1px;
	    background-color: #fff;
	    border: 1px solid #ddd;
	    text-indent:1.2em;
	}
</style>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header" style="margin-left: 40px;">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">首页</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">题库管理<span class="sr-only">(current)</span></a></li>
					<li><a href="#">试卷管理</a></li>
					<li><a href="#">考试管理</a></li>
					<li><a href="#">成绩管理</a></li>
					<li><a href="#">系统管理</a></li>
				</ul>
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<input type="text" class="form-control" style="margin-top: 0"
							placeholder="输入关键词">
					</div>
					<button type="submit" class="btn btn-default">搜索</button>
				</form>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#">登录</a></li>
					<li><a href="#">注册</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">个人中心<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="#">账户设置</a></li>
							<li><a href="#">成绩管理</a></li>
							<li><a href="#">考试入口</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="#">退出登录</a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container-fluid" style="padding-left: 0;">
		<div class="row-fluid">
			<div class="col-xs-12 col-md-3 col-lg-3" style="padding-left: 0;">
				<div class="user-img-div">
					<img src="../images/20160322162501.jpg" class="img-thumbnail">
					<div class="inner-text"
						style="float: right; margin-top: 20px; padding-right: 12px;">
						<label id="username">UserName</label><br /> <span
							class="glyphicon glyphicon-cog"></span><a href="#"
							class="inner-text" style="font-size: 1.2em">账户设置</a>
					</div>
				</div>
				<ul class="list-group">
					<li class="list-group-item">试卷管理</li>
					<li id="paperTypeList" class="dropdown list-group-item">		
						<a href="#" class="dropdown-toggleglyphicon" data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"><span class="glyphicon glyphicon-th-list"></span>试卷列表<span class="caret"></span></a>
						<ul class="dropdown-menu paperClassList" style="margin-bottom:0">
							<li><a href="#">全部试卷</a></li>
						</ul>	
					</li>
					<a href="" class="list-group-item"><span
						class="glyphicon glyphicon-file"></span>手动组卷</a>
					<a href="" class="list-group-item"><span
						class="glyphicon glyphicon-list-alt"></span>自动组卷</a>
					<a href="" class="list-group-item active"><span
						class="glyphicon glyphicon-cloud-upload"></span>导入试卷</a>
					<a href="" class="list-group-item"><span
						class="glyphicon glyphicon-tags"></span>试卷分类</a>
					<a href="#" class="list-group-item"><span
						class="glyphicon glyphicon-trash"></span>回收站</a>
				</ul>
			</div>
			<div class="col-xs-12 col-md-9 col-lg-9">
				<div>
					<p>正在执行上传操作，请勿离开当前页面...</p>
				</div>
			</div>
		</div>
	</div>
	<div id="footer-sec">Copyright © 2016.Company name All rights
		reserved.</div>
		
	<jsp:useBean id="sp" class="com.jspsmart.upload.SmartUpload" ></jsp:useBean>
	<%
		IPTimeStamp its= new  IPTimeStamp(request.getRemoteAddr());//获取用户的ip地址，实例化不重复文件名生成类的对象。
		sp.initialize(pageContext);//初始化上传
		sp.upload();		   //准备上传
		String name= "";
		name=its.getIPTimeStampRand()+"."+sp.getFiles().getFile(0).getFileExt();//获取一个不重复文件名+文件后缀组成文件名。
		String fileRealPathandName= getServletContext().getRealPath("/")+"file\\TempPaper\\"+name;//生成文件的物理保存地址
		sp.getFiles().getFile(0).saveAs(fileRealPathandName);//保存文件
		
		String paperClass=request.getParameter("paperClass");
	
		session.setAttribute("paperClass", paperClass);
		
	    response.setHeader("Refresh","0.1;url=../ImportPaper?filename="+name);
	%>
</body>
<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="../js/bootstrap.min.js"></script>
<script type="text/javascript" src="../js/importPaper.js"></script>
</html>