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


	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_managetments").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		Ewin.confirm({ message: "请确认数据是否正确，确定要提交吗？" }).on(function (e) {
			
			$.ajax({
				url : "delete",
				type: "post",
				data : {},
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_managetments").bootstrapTable("refresh");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("处理异常");
				}
			})	
			
		});
		alert(arrselections);
	});

	
	$("#btn_import").unbind().bind("click", function(){

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
		
		Ewin.confirm({ message: "请确认数据是否正确，确定要提交吗？" }).on(function (e) {
			$("#btn_pay").attr("disabled", true);
			if (!e) {
				$("#btn_pay").attr("disabled", false);
				return;
			}
			$.ajax({
				url : "pay",
				type: "post",
				data : {},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					$("#btn_pay").attr("disabled", false);
					var code = data['code'];
					if (code == 1000) {
						$("#import_modal").modal("hide");
						$("#tb_managetments").bootstrapTable("refresh");
						alert("处理成功");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("处理异常");
				}
			})
			
		});
		

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



