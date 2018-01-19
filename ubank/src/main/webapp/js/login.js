$(function () {
	
	function validate(){
		var mobile = $("#mobile").val();
		if(mobile == "" || mobile.length == 0){
			$(".form-error").text("手机号不能为空");
			return false;
		}
	    //检查密码
		var pwd = $("#password").val();
		if(pwd == "" || pwd.length==0){
			$(".form-error").text("密码不能为空");
			return false;
		}
	
		 return true;
	}

	function login() {
        $(".form-error").text("");
        if (validate()){

            var mobile = $("#mobile").val();
            var password = $("#password").val();

            $.ajax({
                url : "login",
                type: "post",
                data : {
                    mobile :mobile ,
                    login_password : password
                },
                async : true,
                dataType : "json",
                success : function(data, status) {
                    var code = data['code'];
                    if (code == 1000) {
                        window.location = "index";
                    }else if(code == 1010){
                        $(".form-error").text("您是普通员工没有后台管理权限");
                    }else{
                        $(".form-error").text("手机号或密码错误");
                    }
                },
                error : function(data, status, e) {
                    $(".form-error").text("系统内部错误");
                }
            });
        }
        return false;
    }
	

    //点击事件
    $("#btn_login").click(login);

    //键盘事件
    $(document).keydown(function (event) {
        e = event ? event : (window.event ? window.event : null);
        if(e.keyCode ==13){
            login();
        }
    });
})