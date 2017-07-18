<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.ulaiber.web.conmon.IConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/logo.png" type="image/x-icon">
	<script src="../js/jquery/jquery-1.12.3.min.js"></script>
    
    <link href="../css/bootstrap/bootstrap.min.css" rel="stylesheet" />
    <link href="../css/font-awesome/font-awesome.min.css" rel="stylesheet" />
    <link href="../css/login.css" rel="stylesheet" />
    
    <script src="../js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
    <script src="../js/login.js" type="text/javascript" ></script>
    <script language=JavaScript>	
	<%
		if(null != request.getSession().getAttribute(IConstants.UBANK_BACKEND_USERSESSION)){
			response.sendRedirect("index");
		}
	%>
</script>
</head>
<body>
	<div class="container">
	    <div class="row">
	        <div class="col-md-offset-4 col-md-5">
	            <form class="form-horizontal">
	                <span class="heading">用户登录</span>
	                <div class="form-group">
	                    <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名">
	                    <i class="fa icon-user"></i>
	                </div>
	                <div class="form-group help">
	                    <input type="password" class="form-control" id="password" placeholder="请输入密码">
	                    <i class="fa icon-lock"></i>
	                    <a href="#" class="fa fa-question-circle"></a>
	                </div>
	                <div class="form-group">
<!--  	                    <div class="main-checkbox">
	                        <input type="checkbox" value="None" id="checkbox1" name="check"/>
	                        <label for="checkbox1"></label>
	                    </div> -->
	                    <span class="error" id="errormsg"></span> 
	                    <button type="submit" class="btn btn-default" id="btn_login" >登录</button>
	                </div>
	            </form>
	        </div>
	    </div>
	</div>
</body>
</html>