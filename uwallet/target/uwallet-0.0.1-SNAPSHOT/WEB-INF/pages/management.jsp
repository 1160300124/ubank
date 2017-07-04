<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>U钱包后台管理系统</title>
    <script src="js/jquery-1.10.2.js"></script>
    
    <link href="css/Content/bootstrap/bootstrap.min.css" rel="stylesheet" />
    <link href="css/Content/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" />
    <link href="css/Content/bootstrap-table/bootstrap-theme.min.css" rel="stylesheet" />

    <script src="js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
    <script src="js/bootstrap-table/bootstrap-table.min.js" type="text/javascript"></script>
    <script src="js/bootstrap-table/locale/bootstrap-table-zh-CN.min.js" type="text/javascript"></script>
</head>
<body>
<h1>U钱包后台管理系统</h1>
<div class="panel-body" style="padding-bottom:0px;">
	<div id="toolbar" class="btn-group">
		<button id="btn_add" type="button" class="btn btn-default">
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
		</button>
		<button id="btn_edit" type="button" class="btn btn-default">
			<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
		</button>
		<button id="btn_delete" type="button" class="btn btn-default">
			<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
		</button>
	</div>
	<table id="tb_departments"></table>
	</div>
</body>
</html>