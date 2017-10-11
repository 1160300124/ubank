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
	
	$("#btn_export").unbind().bind("click", function(){
		var params = {};
		params.companyNumber = $("#company_select").val();
		params.deptNumber = $("#dept_select").val();
		params.userName = $("#user_name").val();
		var clockStatus = $("#status").val();
		if (clockStatus === "0"){
			params.clockOnStatus = "0";
			params.clockOffStatus = "0";
		} else if (clockStatus === "1"){
			params.clockOnStatus = "1";
		} else if (clockStatus === "2"){
			params.clockOffStatus = "2";
		} else if (clockStatus === "3"){
			params.clockOnStatus = "";
			params.clockOffStatus = "";
		}
		params.startDate = $("#start_date").val();
		params.endDate = $("#end_date").val();
		
		if (params.companyNumber == "" || params.companyNumber == null) {
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
		if (params.startDate > params.endDate){
			Ewin.alert("开始日期不能大于结束日期");
			return;
		}
		if (getDays(params.startDate, params.endDate) > 30){
			Ewin.alert("开始日期与结束日期相差不能大于31天");
			return;
		}
		
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
		
		window.location = "exportRecords?" + parseParams(params);
		
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
	
	function getDays(startDate, endDate){
		var date1Str = startDate.split("-");//将日期字符串分隔为数组，数组元素分别为年、月、日      
		//根据年、月、日的值创建Date对象
		var date1Obj = new Date(date1Str[0],(date1Str[1]-1),date1Str[2]);
		var date2Str = endDate.split("-");
		var date2Obj = new Date(date2Str[0],(date2Str[1]-1),date2Str[2]);
		var t1 = date1Obj.getTime();//返回从1970-1-1开始计算到Date对象中的时间之间的毫秒数
		var t2 = date2Obj.getTime();//返回从1970-1-1开始计算到Date对象中的时间之间的毫秒数
		var datetime = 1000 * 60 * 60 * 24; //一天时间的毫秒值 
		var days = Math.abs((t2 - t1) / datetime);//计算出两个日期天数差 
		return days;
	} 

	$("#btn_search").unbind().bind("click", function(){

		var params = {};
		var company = {};
		var dept = {};
		company.companyNumber = $("#company_select").val();
		params.company = company;
	    dept.dept_number = $("#dept_select").val();
		params.dept = dept;
		params.userName = $("#user_name").val();
		var clockStatus = $("#status").val();
		if (clockStatus === "0"){
			params.clockOnStatus = "0";
			params.clockOffStatus = "0";
		} else if (clockStatus === "1"){
			params.clockOnStatus = "1";
		} else if (clockStatus === "2"){
			params.clockOffStatus = "2";
		} else if (clockStatus === "3"){
			params.clockOnStatus = "";
			params.clockOffStatus = "";
		}
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
		if (params.startDate > params.endDate){
			Ewin.alert("开始日期不能大于结束日期");
			return;
		}
		if (getDays(params.startDate, params.endDate) > 30){
			Ewin.alert("开始日期与结束日期相差不能大于31天");
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
	
	
	$("#company_select").unbind().bind("change", function(){
		
		var select = $("#dept_select").empty();
		select.append("<option value=''>请选择部门</option>");
		var comNum = $("#company_select").val();
		if (comNum == "" || comNum == null || comNum == undefined){
			return false;
		}
		
		$.ajax({
			url : "getDeptByCom",
			type: "post",
			data : {
				"comNum" : comNum
			},
			async : true, 
			dataType : "json",
			success : function(data, status) {
				if(data.length <= 0){
                    return;
                }
				for (var i in data){
					var option = "<option value='" + data[i].dept_number + "'>" + data[i].name + "</option>";
					select.append(option);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
	});

});

function clockOnStatusFormatter(value, row, index) {
	if (value == 0 || row.clockOnStatus == 0){
		return "正常";
	} else if (value == 1 || row.clockOnStatus == 1){
		return "迟到";
	}
	return "-";
}

function clockOffStatusFormatter(value, row, index) {
	if (value == 0 || row.clockOffStatus == 0){
		return "正常";
	} else if (value == 2 || row.clockOffStatus == 2){
		return "早退";
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