<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>优发展银行管理系统</title>
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
<div class="header clearfix">
	<a href="<%=request.getContextPath()%>/backend/index" class="logo fl"></a>
	<h1 class="fl">优发展银行</h1>
	<div class="fr header-user">
		<i class="fl icon-user-one"></i>
		<span class="fl">张三</span>
		<i class="fl icon-arrows-bottom"></i>
		<ul class="header-user-menu">
			<li>退出</li>
		</ul>
	</div>
	<span class="fr">深圳市优玩付信息技术有限公司</span>
</div>
<!-- 公共导航菜单 -->
<div class="nav">
	<ul>
		<li>
			<span class="first-menu">业务管理</span>
			<ul class="second-menu">
				<li><a href="<%=request.getContextPath()%>/backend/tomanager"><i class="icon-recond"></i>转账记录</a></li>
				<%--<li><a href=""><i class="icon-recond"></i>二级菜单</a></li>--%>
				<%--<li><a href=""><i class="icon-other"></i>二级菜单</a></li>--%>
			</ul>

		</li>
		<li>
			<span class="first-menu">权限管理</span>
			<ul class="second-menu">
				<li><a href="#"><i class="icon-recond"></i></a></li>
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