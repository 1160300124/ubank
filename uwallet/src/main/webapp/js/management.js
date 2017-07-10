$(function () {

	//1.初始化Table
	var Management = {
//			import : function(){
//			alert("xxxxxx");
//			}
	}

//	Management.prototype.import = function(){
//	alert("aaaaaaaaaa");

//	};


	$("#btn_import").unbind().bind("click", function(){

//		$("#import_modal").removeClass("hide");
		$("#import_modal").modal("show");
	});


	$("#upload_file").unbind().bind("click", function(){

		$.ajaxFileUpload({
			url : "upload",
			type: "post",
			secureuri : false,
			fileElementId : "file",
			dataType : "json",
			data : {
				name : $('#file').val()
			},
			success : function(data, status) {
				data = $.parseJSON(data.replace(/<.*?>/ig,""));
				var code = data['code'];
				if (code == 1000) {
					$("#tb_salary").bootstrapTable("load", data["data"]);
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

	$("#btn_pay").unbind().bind("click", function(){
		$("#btn_pay").css("disabled", true);
		$.ajax({
			url : "pay",
			type: "post",
			data : {

			},
			async : false, 
			dataType : "json",
			success : function(data, status) {
				$("#btn_pay").css("disabled", false);
				var code = data['code'];
				if (code == 1000) {
					alert("处理成功");
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

//	var manager = new Management();
//	Management.init();
});

function operateFormatter(value, row, index) {
	return [
	        '<a class="detail"  href="javascript:void(0)" title="详情">详情</a>'
	        ].join('');
}


window.operateEvents = {
		'click .detail': function (e, value, row, index) {
			$("#detail_modal").modal("show");
			$("#tb_salary_detail").bootstrapTable("refresh", {
				url : "details?sid=" + row.sid
			});
		}
};



