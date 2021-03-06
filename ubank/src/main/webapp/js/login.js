$(function () {
	
	function validate(){
		var mobile = $("#mobile").val();
		if(mobile == "" || mobile.length == 0){
			$(".form-error").text("手机号不能为空！");
			return false;
		}
	    //检查密码
		var pwd = $("#password").val();
		if(pwd == "" || pwd.length==0){
			$(".form-error").text("密码不能为空！");	
			return false;
		}
	
		 return true;
	}
	
	$("#btn_login").unbind().bind("click", function(){
		$(".form-error").text("");
		$("#btn_login").attr("disabled", true);
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
					$("#btn_login").attr("disabled", false);
					var code = data['code'];
					if (code == 1000) {
						window.location = "index";
						
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
})