$(function () {
	
	$("#btn_login").unbind().bind("click", function(){
		
		$("#btn_login").css("disabled", true);
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
				$("#btn_login").css("disabled", false);
				var code = data['code'];
				if (code == 1000) {
					window.location = "../";
					
				}else{
					alert("处理异常！");
				}
			},
			error : function(data, status, e) {
				alert("上传发生异常");
			}
		})
		
		return false;
		
	});
})