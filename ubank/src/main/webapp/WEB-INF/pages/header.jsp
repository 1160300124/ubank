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
	<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap-select.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap-table/bootstrap-theme.min.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/bootstrap/daterangepicker.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/zTreeStyle.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/icheck/skins/all.css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/css/metroStyle/metroStyle.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-3.1.0.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.12.3.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery/ajaxfileupload.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-datetimepicker.fr.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-select.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/defaults-zh_CN.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table-zh-CN.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/icheck/icheck.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/common.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/confirm.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-treeview.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/jquery/moment.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/daterangepicker.js" type="text/javascript" ></script>



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
		<ul class="header-user-menu" >
			<li class="line" onclick="openModPwd()">修改密码</li>
			<li class="line" onclick="logout()">退出</li>
		</ul>
	</div>
	<span class="fr">${BACKENDUSER.com_name}</span>



</div>
<%--修改密码弹出框--%>
<div id="modiPwd_modal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" >修改密码</h4>
			</div>
			<div class="modal-body">
				<div class="form-inline base-form clearfix">
					<form id="modiPwd_form" method="post" class="form-horizontal" >
						<div class="form-group col-md-12">
							<label class="col-md-3" for="exampleInputName2">旧密码</label>
							<div class="col-md-9">
								<input type="password" class="form-control" id="oldPwd" name=""   placeholder="请输入旧密码">
							</div>
						</div>
						<div class="form-group col-md-12">
							<label class="col-md-3" for="exampleInputName2">新密码</label>
							<div class="col-md-9">
								<input type="password" class="form-control" id="newPwd" name=""   placeholder="请输入新密码">
							</div>
						</div>
						<div class="form-group col-md-12">
							<label class="col-md-3" for="exampleInputName2">确认密码</label>
							<div class="col-md-9">
								<input type="password" class="form-control" id="confirmPwd" name=""  placeholder="请再次输入密码" >
							</div>
						</div>
					</form>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" onclick="modifyPwd()" class="btn btn-primary">保存</button>
			</div>
		</div>
	</div>
</div>
<!-- 公共导航菜单 -->
<div class="nav">
	<ul></ul>
</div>


<script type="text/javascript">
    var GROUPNUMBER = "${BACKENDUSER.groupNumber}"; //当前用户所属集团编号
    var SYSFLAG = "${BACKENDUSER.sysflag}"; //角色标识。为 0 表示为根节点，为 1 表示非根节点
    var COMPANYNUMBER = "${BACKENDUSER.companyNumber}";  //当前用户所属公司编号
    var COMPANYNAME = "${BACKENDUSER.com_name}";  //当前用户所属公司名称
    var DEPTNUMBER = "${BACKENDUSER.dept_number}";		//当前用户所属部门
    var ROLEID = "${BACKENDUSER.role_id}";		//当前用户角色ID
    $(function () {
        //获取系统菜单
        $.ajax({
            url : 'getAllMenu',
            type: 'post',
            dataType : 'json',
            data : {
                "userName" : $(".username").text(),
                "sysflag" : SYSFLAG

            },
            success : function (data) {
                var html = "";
                var fatherMenu = "";
                var childrenMenu = "";
                var father = [];
                var children = [];
                if(data.length <= 0 ){
                    Ewin.alert("系统菜单加载异常");
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
                Ewin.alert("系统菜单加载异常");

            }
        });
    });

    /**
	 * 打开修改密码框
	 */
    function openModPwd() {
        $("#modiPwd_modal").modal("show");
    }

    /**
	 * 修改密码
     */
    function modifyPwd() {
        var old = $("#oldPwd").val();
        var newPwd = $("#newPwd").val();
        var confirm = $("#confirmPwd").val();
        var mobile = ${BACKENDUSER.mobile};
        if(old == "" ){
            Ewin.alert("旧密码不能为空");
            return;
        }
        if( newPwd == "" ){
            Ewin.alert("新密码不能为空");
            return;
        }
        if(newPwd.length < 6 || newPwd.length > 20){
            Ewin.alert("密码长度为6~20之间");
            return;
        }
        if( confirm == ""){
            Ewin.alert("确认密码不能为空");
            return;
        }
        if(confirm.length < 6 || confirm.length > 20){
            Ewin.alert("密码长度为6~20之间");
            return;
        }
        if(newPwd != confirm){
            Ewin.alert("密码不一致");
            return;
        }
        if(!Validate.NumberAndLetter(newPwd) || !Validate.NumberAndLetter(confirm)){
            Ewin.alert("密码格式为数字和字母");
            return;
        }
        $.ajax({
            url : "modifyPassword",
			type : "POST",
			dataType : "json",
			data : {
                "mobile" : $.trim(mobile),
                "newPwd" : $.trim(newPwd),
                "oldPwd" : $.trim(old)
			},
			success : function (data) {
				if(data.code == 1010){
                    Ewin.alert(data.message);
				}else{
                    Ewin.alert(data.message);
                    $("#modiPwd_modal").modal("hide");
                    $("#modiPwd_form")[0].reset();
				}
            },
			error : function () {
                Ewin.alert("修改密码失败");
            }
		});


    }

</script>