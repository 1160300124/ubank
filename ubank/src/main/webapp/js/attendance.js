$(function(){
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
	
	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_attendance_records").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		var data = [];
		$.each(arrselections, function(index, item){
			data.push(item.rid);
		});
		
		Ewin.confirm({ message: "确定要删除吗？" }).on(function (e) {
			
			if (!e) {
				return;
			}
			
			$.ajax({
				url : "deleteRecords",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_attendance_records").bootstrapTable("refresh");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部异常");
				}
			})	
			
		});
		
	});

	$("#btn_search").unbind().bind("click", function(){

		var params = {};
		var company = {};
		var dept = {};
		company.companyNumber = $("#company").val();
		params.company = company;
	    dept.dept_number = $("#dept").val();
		params.dept = dept;
		params.userName = $("#user_name").val();
		params.clockType = $("#type").val();
		params.clockStatus = $("#status").val();
		params.startDate = $("#start_date").val();
		params.endDate = $("#end_date").val();
		
		if (company.companyNumber == "" || company.companyNumber == null) {
			Ewin.alert("请选择公司。");
			return;
		}
		if (params.startDate == "" || params.startDate == null) {
			Ewin.alert("请选择开始日期。");
			return;
		}
		if (params.endDate == "" || params.endDate == null) {
			Ewin.alert("请选择结束日期。");
			return;
		}

		$("#btn_search").attr("disabled", true);
		
		var parseParams = function(param, key) {
		    var paramStr = "";
		    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
		        paramStr += "&" + key + "=" + encodeURIComponent(param);
		    } else {
		        $.each(param, function(i) {
		            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
		            paramStr += '&' + parseParams(this, k);
		        });
		    }
		    return paramStr.substr(1);
		}

		$("#tb_attendance_records").bootstrapTable("refresh", {
			url : "getRecords?" + parseParams(params)
		});

		$("#btn_search").attr("disabled", false);

	});

});

function operateFormatter(value, row, index) {
	return [
	        '<a class="detail"  href="javascript:void(0)" title="详情">详情</a>'
	        ].join('');
}


window.operateEvents = {
		'click .detail': function (e, value, row, index) {

			var editModal = $("#edit_modal");
			editModal.find("#edit_form")[0].reset();
			editModal.find("#url_page").empty();
			editModal.find("#url_module").empty();

			$.ajax({
				url : "getAllCategories",
				type: "get",
				data : {},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						var categories = data['data'];
						var select = editModal.find("#url_page");
						for (var i in categories){
							var option = "<option value='" + categories[i].cid + "'>" + categories[i].categoryName + "</option>";
							select.append(option);
						}
						select.find("option[value='" + row.category.cid + "']").attr("selected", true);
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
		}


};