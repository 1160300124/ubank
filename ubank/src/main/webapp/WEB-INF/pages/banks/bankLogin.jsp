<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.ulaiber.web.conmon.IConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/images/login_logo.png" type="image/x-icon">
    <link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/css/font-awesome/font-awesome.min.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/css/login.css" rel="stylesheet" />
    <script type="type/javascript">
<%
        if(null != request.getSession().getAttribute(IConstants.UBANK_BACKEND_USERSESSION)){
            response.sendRedirect("bank_index");
        }
    %>
</script>
</head>
<body>
<div class="login-header clearfix">
    <i class="login-logo fl"></i>
    <span class="fl">优发展银行后台登录xxxxx</span>
</div>
<div class="login-wrap">
    <div class="login-wrap-logo"></div>
    <div class="login-box">
        <h3>用户登录</h3>
        <form class="login-form">
            <div class="login-form-input">
                <input type="text" class="form-control" id="user_mobile" name="mobile" placeholder="请输入手机号">
                <i class="icon-user-one"></i>
            </div>
            <div class="login-form-input">
                <input type="password" class="form-control" id="user_pwd" placeholder="请输入密码">
                <i class="icon-psd"></i>
            </div>

            <div class="form-error"></div>
            <button type="submit" class="login-form-btn" id="user_login">登录</button>
        </form>
    </div>

</div>
<div class="login-footer">
    <div>
        <p><span class="m-right-30">邮箱：ulaiber@ulaiber.com</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>公司地址：广东省深圳市南山科技园麻雀岭工业区M-10</span></p>
        <p style="font-size: .8em; color: gray;"><span class="m-right-30">服务热线：400-803-0906</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>广州优融网络科技有限责任公司      版权所有</span></p>
        <p style="font-size: .8em;"><span class="m-right-30"><a href="http://www.miitbeian.gov.cn">粤ICP备16025491号-4</a></span></p>
    </div>
</div>

<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<%--<script src="<%=request.getContextPath()%>/js/login.js" type="text/javascript" ></script>--%>
<script type="text/javascript">

    function validate(){
        var mobile = $("#user_mobile").val();
        if(mobile == "" || mobile.length == 0){
            $(".form-error").text("手机号不能为空！");
            return false;
        }
        //检查密码
        var pwd = $("#user_pwd").val();
        if(pwd == "" || pwd.length==0){
            $(".form-error").text("密码不能为空！");
            return false;
        }

        return true;
    }

    $("#user_login").unbind().bind("click", function(){
        $(".form-error").text("");
        $("#user_login").attr("disabled", true);
        if (validate()){
            var mobile = $("#user_mobile").val();
            var password = $("#user_pwd").val();
            $.ajax({
                url : "login_bank",
                type: "post",
                data : {
                    mobile :mobile ,
                    password : password
                },
                async : true,
                dataType : "json",
                success : function(data, status) {
                    $("#btn_login").attr("disabled", false);
                    var code = data['code'];
                    if (code == 1000) {
                        window.location = "bank_index";

                    }else{
                        $(".form-error").text("手机号或密码错误！");
                    }
                },
                error : function(data, status, e) {
                    $(".form-error").text("系统内部错误！");
                }
            });
        }
        $("#btn_login").attr("disabled", false);
        return false;

    });
</script>
</body>
</html>