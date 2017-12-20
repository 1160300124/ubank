$(function(){
	
	loadCompanys();
	function loadCompanys(){
		var select = $("#company_select").empty();
		var number = COMPANYNUMBER.split(",");
		var name = COMPANYNAME.split(",");
		for (var i in number){
			var option = "<option value='" + number[i] + "'>" + name[i] + "</option>";
			select.append(option);
		}
		
		var deptSelect = $("#dept_select").empty();
		deptSelect.append("<option value=''>全部</option>");
		var comNum = $("#company_select").val();
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
					deptSelect.append(option);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
	}
	
	$("#btn_all").unbind().bind("click", function(e){
		e.preventDefault();
		$("#btn_all").removeClass("btn-default").addClass("active btn-primary");
		$("#btn_on_the_job").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("active btn-primary").addClass("btn-default");
		loadTable();
	});
	$("#btn_on_the_job").unbind().bind("click", function(e){
		e.preventDefault();
		$("#btn_on_the_job").removeClass("btn-default").addClass("active btn-primary");
		$("#btn_all").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("active btn-primary").addClass("btn-default");
		loadTable();
	});
	$("#btn_leave_office").unbind().bind("click", function(){
		$("#btn_leave_office").removeClass("btn-default").addClass("active btn-primary");
		$("#btn_on_the_job").removeClass(" active btn-primary").addClass("btn-default");
		$("#btn_all").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("active btn-primary").addClass("btn-default");
		loadTable();
	});
	$("#btn_salary_set").unbind().bind("click", function(){
		$("#btn_salary_set").removeClass("btn-default").addClass("active btn-primary");
		$("#btn_on_the_job").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_all").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("active btn-primary").addClass("btn-default");
		loadTable();
	});
	$("#btn_salary_change").unbind().bind("click", function(){
		$("#btn_salary_change").removeClass("btn-default").addClass("active btn-primary");
		$("#btn_on_the_job").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_all").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("active btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("active btn-primary").addClass("btn-default");
		loadTable();
	});
	
	$("#company_select").unbind().bind("change", function(){
		var select = $("#dept_select").empty();
		select.append("<option value=''>全部</option>");
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
				loadTable();
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
	
	$("#dept_select").unbind().bind("change", function(){
		loadTable();
	});
	
	$("#search").unbind().bind("input propertychange", function(){
		loadTable();
	});
	
	function parseParams(param, key) {
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
	
	loadTable();
	function loadTable(){
		var params = {}; 
		var companyNum = $("#company_select").val();
		params.companyNum = companyNum;
	    var dept_number = $("#dept_select").val();
		params.deptNum = dept_number;
		var search = $("#search").val();
		params.search = search;
		var type = $(".active").val();
		params.type = type;
		$("#tb_salary_change").bootstrapTable("refresh", {
			url : "changeList?" + parseParams(params)
		});
	}

	$("#add_salary_change").unbind().bind("click", function(){
		var params = {};
		var userId = $("#user_id").val();
		var oldSalary = $("#oldSalary").text();
		var salary = $("#adjustmentSalaryText").val();
		var percent = $("#adjustmentPercent").val();
		var date = $("#effectTime").val();
		var reason = $("#change_reason").val();
		
		params.userId = userId;
		params.oldSalary = oldSalary;
		params.currentSalary = salary;
		params.changePercent = percent;
		params.changeDate = date;
		params.changeReason = reason;
		
		$.ajax({
			url : "saveChange",
			type: "post",
			contentType : 'application/json;charset=utf-8',
			data : JSON.stringify(params),
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					Ewin.alert("调薪成功。");
					$('#adjustment_salary_modal').modal('hide');
					$("#tb_salary_change").bootstrapTable("refresh");
				}else{
					Ewin.alert(data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部异常");
			}
		})	
		
	});
	
	$("#set_first_salary").unbind().bind("click", function(){
		var userId = $("#user_id").val();
		var salary = $("#fixedSalaryText").val();
		$.ajax({
			url : "setFirstSalary",
			type: "post",
			data : {
				userId : userId,
				salary : salary
			},
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					Ewin.alert("定薪成功。");
					$('#fixed_salary_modal').modal('hide');
					$("#tb_salary_change").bootstrapTable("refresh");
				}else{
					Ewin.alert(data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部异常");
			}
		})
		
	});
	
})

var salaryChangeFun = {
	openAdjustmentSalaryModal: function() {
		$('#adjustment_salary_modal').modal('show');
		$("#effectTime").datetimepicker({  
			format: 'yyyy-mm-dd',  
			language: 'zh-CN',  
			pickDate: true,  
			pickTime: true,  
			autoclose: 1,
			todayBtn:  1,
			todayHighlight: 1,
			minView: "month"
		});

		$("#adjustmentSalaryText").on('keyup', function(e) {
			if(validateMoney(e)){
				var oldSalary = parseFloat($("#oldSalary").html());

				var adjustmentPercent = ($(e.currentTarget).val() - oldSalary )/ oldSalary
				$("#adjustmentPercent").val(adjustmentPercent * 100 + '%');
			}

		})
	},
	openPositiveSalaryModal: function() {
		$('#fixed_salary_modal').modal('show');
		$("#turnPositive").datetimepicker({  
			format: 'yyyy-mm-dd',  
			language: 'zh-CN',  
			pickDate: true,  
			pickTime: true,  
			autoclose: 1,
			todayBtn:  1,
			todayHighlight: 1,
			minView: "month"
		});

		$("#fixedSalaryText").on('keyup', validateMoney)
	},
	showTurnPositive: function() {
		$(".turn-positive").show();
		$(".add-turn-positive-btn").parent().hide();
	}
}

function curDate(){
	var d = new Date(); 
	var year = d.getFullYear(); 
	var month = d.getMonth() + 1; 
	var date = d.getDate(); 
	var curDate= year;
	if(month > 9)
		curDate = curDate + "-" + month;
	else
		curDate = curDate + "-0" + month;
	if(date > 9)
		curDate = curDate + "-" + date;
	else
		curDate = curDate + "-0" + date;
	return curDate; 
}

function statusFormatter(value, row, index) {
	if ((row.entryDate != '' &&  row.entryDate != null) && (row.leaveDate == '' || row.leaveDate == null ||  row.leaveDate >= curDate())){
		return '<span style="color:#43ac34">在职</span>';
	}
	if (row.leaveDate != '' && row.leaveDate != null && row.leaveDate < curDate()){
		return '<span style="color:#e8583d">离职</span>';
	}
	return '-';
}

function operateFormatter(value, row, index) {
	var operateStr = '<a class="change"  href="javascript:void(0)" title="调薪">调薪</a>';
	if (row.salaries == 0 || row.salaries == null){
		operateStr = '<a class="set"  href="javascript:void(0)" title="定薪">定薪</a>';
	}
	return [operateStr].join('');
}

window.operateEvents = {
		'click .change': function (e, value, row, index) {
			$("#user_id").val(row.id);
			var modal = $("#adjustment_salary_modal");
			modal.find(".employee-name").text(row.userName);
			if ((row.entryDate != '' &&  row.entryDate != null) && (row.leaveDate == '' || row.leaveDate == null ||  row.leaveDate >= curDate())){
				modal.find(".employee-info .status .on-job").show();
				modal.find(".employee-info .status .no-job").hide();
			}
			if (row.leaveDate != '' && row.leaveDate != null && row.leaveDate < curDate()){
				modal.find(".employee-info .status .on-job").hide();
				modal.find(".employee-info .status .no-job").show();
			}
			modal.find(".employee-info .time .flex3").text(row.entryDate);
			modal.find(".employee-info .department .flex3").text(row.dept_name);
			modal.find("#oldSalary").text(row.salaries);
			
			$.ajax({
				url : "getSalaryChangeList",
				type: "get",
				data : {
					userId : row.id
				},
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						var records = data['data'];
						var recordDiv = $("#record").empty();
						var html = "";
						if (records == null || records.length == 0){
							html = "<br>没有记录";
						} else {
							for (var i in records){
								html += '<div class="flex align-item-center">';
								html += '<div class="paddingright20">' + records[i].changeDate + '</div>';
								html += '<div class="flex1 salary-log">';
								html += '基本工资:<br>';
								html += records[i].oldSalary + '元至' + records[i].currentSalary + '元<br>';
								html += '涨幅：' + records[i].changePercent + '<br>';
								html += records[i].changeReason;
								html += '</div>';
								html += '</div>';
							}
						}
						recordDiv.html(html);
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部异常");
				}
			})	
			
			salaryChangeFun.openAdjustmentSalaryModal();
		},
		'click .set': function (e, value, row, index) {
			$("#user_id").val(row.id);
			var modal = $("#fixed_salary_modal");
			modal.find(".employee-name").text(row.userName);
			if ((row.entryDate != '' &&  row.entryDate != null) && (row.leaveDate == '' || row.leaveDate == null ||  row.leaveDate >= curDate())){
				modal.find(".employee-info .status .on-job").show();
				modal.find(".employee-info .status .no-job").hide();
			}
			if (row.leaveDate != '' && row.leaveDate != null && row.leaveDate < curDate()){
				modal.find(".employee-info .status .on-job").hide();
				modal.find(".employee-info .status .no-job").show();
			}
			modal.find(".employee-info .time .flex3").text(row.entryDate);
			modal.find(".employee-info .department .flex3").text(row.dept_name);
			salaryChangeFun.openPositiveSalaryModal();
		}
}

function validateMoney(e) {
	if(!Validate.isMoney($(e.currentTarget).val()))
	{
		$(e.currentTarget.nextElementSibling).html('请输入合法金额');
		return false;
	}
	else {
		$(e.currentTarget.nextElementSibling).html('');
		return true;
	}
}