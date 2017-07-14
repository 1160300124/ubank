$(function () {
	
	function validate(){
		var name = $("#username").val();
		if(name == "" || name.length == 0){
			$("#errormsg").text("用户名不能为空！");
			return false;
		}
	    //检查密码
		var pwd = $("#password").val();
		if(pwd == "" || pwd.length==0){
			$("#errormsg").text("密码不能为空！");	
			return false;
		}
	
		 return true;
	}
	
	$("#btn_login").unbind().bind("click", function(){
		$("#errormsg").text("");
		$("#btn_login").attr("disabled", true);
		if (validate()){
			
			var name = $("#username").val();
			var password = $("#password").val();
			
			$.ajax({
				url : "login",
				type: "post",
				data : {
					userName :name ,
					login_password : password
				},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					$("#btn_login").attr("disabled", false);
					var code = data['code'];
					if (code == 1000) {
						window.location = "../backend/index";
						
					}else{
						$("#errormsg").text("用户名或密码错误！");
					}
				},
				error : function(data, status, e) {
					$("#errormsg").text("系统内部错误！");
				}
			});
		}
		$("#btn_login").attr("disabled", false);
		return false;
		
	});
})