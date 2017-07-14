<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>U钱包管理系统</title>
    <link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/font-awesome/font-awesome.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap-table/bootstrap-theme.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" />
    <script src="<%=request.getContextPath()%>/js/jquery/jquery-1.12.3.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery/ajaxfileupload.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table-zh-CN.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/common.js" type="text/javascript"></script>

</head>
<body >
	<!-- 公共banner -->
	<div class="header">
		<div ><a class="index_title" href="<%=request.getContextPath()%>/backend/index"><h3>uboss钱包</h3></a></div>
	</div>
	<!-- 公共导航菜单 -->
	<div class="nav">
	    <ul>
	        <li>
	            <span class="first-menu">业务管理</span>
	            <ul class="second-menu">
	                <li><a href="<%=request.getContextPath()%>/backend/tomanager">转账记录</a></li>
	            </ul>
	        </li>
	    </ul>
	</div>

<script type="text/javascript">

	//根据当前页面的url，给左边菜单栏选中的样式
	var menu_url=window.location.pathname;
	$('.second-menu a').each(function(){
		if($(this).attr('href')==menu_url){
            $(this).addClass('on');
			$(this).parents('ul').show();
		}
	});

</script>