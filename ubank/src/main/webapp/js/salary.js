$(function () {
	$(".time-picker").datetimepicker({  
		format: 'yyyy-mm-dd',  
		language: 'zh-CN',  
		pickDate: true,  
		pickTime: true,  
		autoclose: 1,
		todayBtn:  1,
		todayHighlight: 1,
		minView: "month"
	});  
	
	loadCompanys();
	function loadCompanys(){
		var select = $("#company_select").empty();
		select.append("<option value=''>请选择公司</option>");
		
		$.ajax({
			url : "getCompanys",
			type: "get",
			data : {},
			async : true, 
			dataType : "json",
			success : function(data, status) {
				if(data.length <= 0){
                    return;
                }
				
				for (var i in data){
					var option = "<option value='" + data[i].companyNumber + "'>" + data[i].name + "</option>";
					select.append(option);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
	}

	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_saraly_records").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		var data = [];
		$.each(arrselections, function(index, item){
			data.push(item.sid);
		});
		
		Ewin.confirm({ message: "确定要删除吗？" }).on(function (e) {
			
			if (!e) {
				return;
			}
			
			$.ajax({
				url : "batchDelete",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_saraly_records").bootstrapTable("refresh");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统异常");
				}
			})	
			
		});
		
	});
	
	$("#btn_add").unbind().bind("click", function(){
		window.location.href = "salarySetting";
		
	});
	
	$("#btn_edit").unbind().bind("click", function(){
		//取表格的选中行数据
		var arrselections = $("#tb_saraly_records").bootstrapTable('getSelections');
		if (arrselections.length != 1) {
			Ewin.alert("请选择一条数据");
			return;
		}
		var companyId = arrselections[0].companyId;
		var salaryMonth = arrselections[0].salaryMonth;
		var salaryDate = arrselections[0].salaryDate;
		var peopleId = arrselections[0].approveIds;
		var peopleName = arrselections[0].approveNames;
		var sid = arrselections[0].sid;
		
		window.location.href = "salarySetting?companyId=" + companyId + "&salaryMonth=" + salaryMonth + "&salaryDate=" + salaryDate
			+ "&peopleId=" + peopleId + "&peopleName=" + peopleName + "&sid=" + sid;
		
	});

	$("#btn_import").unbind().bind("click", function(){
		$("#file").val("");
		$("#file_tmp").val("");
		$("#tb_salary").bootstrapTable("load", []);
		$("#import_modal").modal("show");
		$("#btn_pay").attr("disabled", true);
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
					Ewin.alert("上传成功！");
					$("#tb_salary").bootstrapTable("load", data["data"]);
					$("#btn_pay").attr("disabled", false);
				}else{
					Ewin.alert("上传失败！" + data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("上传发生异常");
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
						Ewin.alert("操作成功！");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统错误！");
				}
			})
			
		});
		

		return false;

	});

});

function statusFormatter(value, row, index) {
	if (value == 0 || row.clockOnStatus == 0){
		return "待发放";
	} else if (value == 1 || row.clockOnStatus == 1){
		return "已发放";
	}
	return "-";
}

function operateFormatter(value, row, index) {
	return [
	        '<a class="detail"  href="javascript:void(0)" title="详情">详情</a>'
	        ].join('');
}


window.operateEvents = {
		'click .detail': function (e, value, row, index) {
			window.location.href = "salaryDetail?sid=" + row.sid;
		}
};


