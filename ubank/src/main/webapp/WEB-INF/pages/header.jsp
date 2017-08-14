<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>优发展银行管理系统</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/login_logo.png" type="image/x-icon">
	<link href="<%=request.getContextPath()%>/css/font-awesome/font-awesome.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap-treeview.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap-datetimepicker.min.css" rel="stylesheet" />	
	<link href="<%=request.getContextPath()%>/css/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap-table/bootstrap-theme.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/zTreeStyle.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-3.1.0.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.12.3.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery/ajaxfileupload.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-datetimepicker.fr.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table-zh-CN.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/common.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/confirm.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-treeview.js" type="text/javascript"></script>



	<script type="text/javascript">
        function logout(){
            window.location = "<%=request.getContextPath()%>" + "/backend/logout";
        }
	</script>
</head>
<body >
<!-- 公共banner -->
<div class="header clearfix">
	<a href="<%=request.getContextPath()%>/backend/index" class="logo fl"></a>
	<h1 class="fl">优发展银行</h1>
	<div class="fr header-user">
		<i class="fl icon-user-one"></i>
		<span class="fl username" >${userName }</span>
		<i class="fl icon-arrows-bottom"></i>
		<ul class="header-user-menu" onclick="return logout();">
			<li>退出</li>
		</ul>
	</div>
	<span class="fr">深圳市优玩付信息技术有限公司</span>
</div>
<!-- 公共导航菜单 -->
<div class="nav">
	<ul>
		<%--<li>--%>
		<%--<span class="first-menu">业务管理</span>--%>
		<%--<ul class="second-menu">--%>
		<%--<li><a href="<%=request.getContextPath()%>/backend/tomanager"><i class="icon-recond"></i>转账记录</a></li>--%>
		<%--</ul>--%>

		<%--</li>--%>
		<%--<li>--%>
		<%--<span class="first-menu">权限管理</span>--%>
		<%--<ul class="second-menu">--%>
		<%--<li><a href="#"><i class="icon-other"></i></a></li>--%>
		<%--</ul>--%>

		<%--</li>--%>
	</ul>
</div>


<script type="text/javascript">
	var GROUPNUMBER = ${BACKENDUSER.groupNumber}; //当前用户所属集团编号
	var SYSFLAG = 0; //角色表示，0 表示管理所有集团的超级管理员；1 表示集团里的超级管理员
	var COMPANYNUMBER = ${BACKENDUSER.companyNumber};  //当前用户所属公司
    var DEPTNUMBER = ${BACKENDUSER.dept_number};		//当前用户所属部门
    var ROLEID = ${BACKENDUSER.role_id};		//当前用户角色ID
    $(function () {
        //获取系统菜单
        $.ajax({
            url : 'getAllMenu',
            type: 'post',
            dataType : 'json',
            data : {
                "userName" : $(".username").text()

			},
            success : function (data) {
                var html = "";
                var fatherMenu = "";
                var childrenMenu = "";
                var father = [];
                var children = [];
                if(data.length <= 0 ){
                    Ewin.alert("系统菜单加载异常，请联系管理员。");
                    return
                }
                for (var i = 0; i<data.length; i++){
                    if(data[i].father == "" || data[i].father == null){
                        father.push(data[i]);
                    }else{
                        children.push(data[i]);
                    }
                }
                for (var j = 0 ; j < father.length ; j++){
                    fatherMenu = "<span onclick='nav.navClick($(this))' class='first-menu'>"+father[j].name+"</span>";
                    for (var k = 0 ; k < children.length ; k++){
                        if(father[j].code == children[k].father){
                            childrenMenu += "<li><a href='<%=request.getContextPath()%>"+children[k].url+"'><i class='"+children[k].icon+"'></i>"+children[k].name+"</a></li>";
                        }
                    }
                    html += "<li> "+fatherMenu+"<ul class='second-menu'>"+childrenMenu+"</ul></li>";
                    fatherMenu = "";
                    childrenMenu = "";

                }
                $(".nav>ul").html(html);
                //根据当前页面的url，给左边菜单栏选中的样式
                var menu_url=window.location.pathname;
                $('.second-menu a').each(function(index){
                    if($(this).attr('href')==menu_url){
                        $(this).addClass('on');
                        $(this).parents('ul').show();
                    }
                });

            },
            error : function () {
                Ewin.alert("系统菜单加载异常，请联系管理员。");

            }
        });
    })
</script>