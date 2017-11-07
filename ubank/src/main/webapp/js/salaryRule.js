var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
$(function(){
    SalaryConfigFun.com_listening();
    
    $("#btn_refresh").unbind().bind("click", function(){
    	$("#tb_salary_rules").bootstrapTable("refresh");
    });

    $("#btn_remove").unbind().bind("click", function(){
    	//取表格的选中行数据
		var selections = $("#tb_salary_rules").bootstrapTable('getSelections');
		if (selections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		var data = [];
		$.each(selections, function(index, item){
			data.push(item.rid);
		});
		
		Ewin.confirm({ message: "确定要删除吗？" }).on(function (e) {
			
			if (!e) {
				return;
			}
			
			$.ajax({
				url : "deleteSalaryRules",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_salary_rules").bootstrapTable("refresh");
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
    
    $("#btn_salaryRule_config").unbind().bind("click", function(){
    	var params = {};
    	params.salaryRuleName = $("#salaryRuleName").val();
    	if (params.salaryRuleName == "" || params.salaryRuleName == null || params.salaryRuleName == undefined 
    			|| params.salaryRuleName.length > 40){
    		Ewin.alert("请输入长度20以内的名称。");
    		return false;
    	}
    	var company = $("#company_select").val();
    	if (company == null || company == undefined){
    		Ewin.alert("请选择公司。");
    		return false;
    	}
    	var companyId = "";
    	var companyName = "";
    	$("#company_select").find("option:selected").each(function(index,item){
    		companyId += $(item).val() + ",";
    		companyName += $(item).text() + ",";
    	});
    	
    	if (!validate()){
    		return false;
    	}
    	
    	params.companyId = companyId == "" ? "" : companyId.substring(0, companyId.length - 1);
    	params.companyName = companyName == "" ? "" : companyName.substring(0, companyName.length - 1);
    	params.overtimeFlag = $("#overtimeRule").prop("checked") ? 1 : 0;
    	params.socialInsurance = $("#socialInsurance").val();
    	params.publicAccumulationFunds = $("#publicAccumulationFunds").val();
    	
    	params.taxThreshold = $("input[name='taxThreshold']:checked").val();

    	params.allowForgetClockCount = $("#allowforgetclock_count").val();
    	params.forgetClockCutPayment = $("#forgetclock_cut").val();
    	params.forgetClockCutUnit = $("#forgetclock_unit").val();
    	params.noClockCutPayment = $("#noclock_cut").val();
    	params.noClockCutUnit = $("#noclock_unit").val();
    	var lateRules = "";   //迟到规则
    	var earlyLeaveRules = ""; //早退规则
    	$("#late_table tbody tr").each(function(index,item){
    		lateRules += $(item).find('input[name="minute"]').val() + ","
    				   + $(item).find('input[name="value"]').val()  + ","
    				   + $(item).find('select[name="unit"]').val()  + "|";
    	});
    	$("#earlyLeave_table tbody tr").each(function(index,item){
    		earlyLeaveRules += $(item).find('input[name="minute"]').val() + ","
    						 + $(item).find('input[name="value"]').val()  + ","
    						 + $(item).find('select[name="unit"]').val()  + "|";
    	});
    	params.laterCutPayment = lateRules == "" ? "" : lateRules.substring(0, lateRules.length - 1);
    	params.leaveEarlyCutPayment = earlyLeaveRules == "" ? "" : earlyLeaveRules.substring(0, earlyLeaveRules.length - 1);

    	var leaveCutPayment = "";
    	leaveCutPayment += $("#personalLeave").val() + ","
    	+ $("#sickLeave").val() + ","
    	+ $("#annualLeave").val() + ","
    	+ $("#daysOff").val() + ","
    	+ $("#wdddingLeave").val() + ","
    	+ $("#maternityLeave").val() + ","
    	+ $("#elseLeave").val();
    	params.leaveCutPayment = leaveCutPayment;
    	
    	//标识。 0 表示新增操作，1 表示修改操作
    	if (flag == 0){
    		$.ajax({
    			url : "saveSalaryRule",
    			type: "post",
    			contentType : 'application/json;charset=utf-8',
    			data : JSON.stringify(params),
    			async : true, 
    			dataType : "json",
    			success : function(data, status) {
    				var code = data['code'];
    				if (code == 1000) {
    					$("#salaryRule_config_modal").modal("hide");
    					$("#tb_salary_rules").bootstrapTable("refresh");
    					Ewin.alert("新增成功。");
    					
    				}else{
    					Ewin.alert(data['message']);
    				}
    				
    			},
    			error : function(data, status, e) {
    				Ewin.alert("系统内部错误！");
    			}
    		});
    	}
    	if (flag == 1){
    		params.rid = $("#salarySule_id").val();
    		$.ajax({
    			url : "updateSalaryRule",
    			type: "post",
    			contentType : 'application/json;charset=utf-8',
    			data : JSON.stringify(params),
    			async : true, 
    			dataType : "json",
    			success : function(data, status) {
    				var code = data['code'];
    				if (code == 1000) {
    					$("#salaryRule_config_modal").modal("hide");
    					$("#tb_salary_rules").bootstrapTable("refresh");
    					Ewin.alert("修改成功。");
    					
    				}else{
    					Ewin.alert(data['message']);
    				}
    				
    			},
    			error : function(data, status, e) {
    				Ewin.alert("系统内部错误！");
    			}
    		});
    	}

    });
    
    $("#btn_attendance_config").unbind().bind("click", function(){
    	var lateObj = $("#late_table tbody tr");
    	var leaveEarlyObj = $("#earlyLeave_table tbody tr")
    	if (!validateAttendance(lateObj, 0) || !validateAttendance(leaveEarlyObj, 1)){
    		return false;
    	}
    	
    	$("#attendanceConfigModal").modal("hide");
    		
    });
    
    
    $("#btn_leave_config").unbind().bind("click", function(){
    	if (!validateLeave()){
    		return false
    	}
    	
    	$("#leaveConfigModal").modal("hide");
    	
    });

});

var late=[{
    minute:"",
    value:"",
    unit:0, //0元，1天
}] //迟到
var earlyLeave=[{
	minute:"",
    value:"",
    unit:0, //0元，1天
}] //早退
var salaryConfigTemplateStr = '<tr>'+
                '<td><input type="text" class="form-control" name="minute" value="@minute" /></td>'+
                '<td><input type="text" class="form-control" name="value" value="@value" /></td>'+
                '<td><select class="form-control" name="unit"><option value="0">元</option><option value="1">天</option></select></td>'+
                '<td class="del"><span class="icon-delete"></span></td>'
            '</tr>';    //考勤规则模板字符串

String.prototype.formatStr = function(data){
    var result=this;
    for(var key in data)
        result=result.replace(new RegExp('@'+key,'g'),data[key]); 
    return result;
}

function formatStr(item){
	var str = salaryConfigTemplateStr.formatStr({
    	minute : item.minute,
    	value : item.value,
    	unit : item.unit
    });
	if (item.unit == "0"){
		return str.replace('<select class="form-control" name="unit"><option value="0">元</option><option value="1">天</option></select>',
				'<select class="form-control" name="unit"><option value="0" selected>元</option><option value="1">天</option></select>');
	}
	if (item.unit == "1"){
		return str.replace('<select class="form-control" name="unit"><option value="0">元</option><option value="1">天</option></select>',
		'<select class="form-control" name="unit"><option value="0">元</option><option value="1" selected>天</option></select>');
	}
	
}

function validate(){
	var socialInsurance = $("#socialInsurance").val();
	var publicAccumulationFunds = $("#publicAccumulationFunds").val();
	if (socialInsurance == "" || socialInsurance == null || socialInsurance == undefined){
		Ewin.alert("请输入社保缴纳金额。");
		return false;
	}
	if (publicAccumulationFunds == "" || publicAccumulationFunds == null || publicAccumulationFunds == undefined){
		Ewin.alert("请输入公积金缴纳金额。");
		return false;
	}
	if (!/^([1-9]\d{0,6}|0)(\.\d{1,2})?$/.test(socialInsurance)){
		Ewin.alert("社保缴纳金额格式不正确,范围0~9999999.99");
		return false;
	}
	if (!/^([1-9]\d{0,6}|0)(\.\d{1,2})?$/.test(publicAccumulationFunds)){
		Ewin.alert("公积金缴纳金额格式不正确,范围0~9999999.99");
		return false;
	}
	
	var lateObj = $("#late_table tbody tr");
	var leaveEarlyObj = $("#earlyLeave_table tbody tr")
	if (!validateAttendance(lateObj, 0) || !validateAttendance(leaveEarlyObj, 1)){
		return false;
	}
	
	if (!validateLeave()){
		return false
	}
	return true;
}

function validateAttendance(obj, flag){
	var mflag = false;
	var vflag = false;
	obj.each(function(index,item){
		var minute = $(item).find('input[name="minute"]').val();
		var value = $(item).find('input[name="value"]').val();
		if (minute == "" && value != ""){
			mflag = true;
			return;
		}
		if(minute != "" && value == ""){
			vflag = true;
			return;
		}
		if (minute != ""){
			if (!/^[1-9]\d{0,3}$/.test(minute)){
				mflag = true;
				return;
			}
		}
		if (value != ""){
			if (!/^([1-9]\d{0,4}|0)(\.\d{0,1})?$/.test(value)){
				vflag = true;
				return;
			}
		}
	});
	
	var type = flag == 0 ? "迟到" : "早退";
	if (mflag){
		Ewin.alert(type + "时间格式不正确，范围0~9999");
		return false;
	}
	
	if (vflag){
		Ewin.alert(type + "扣除格式不正确，范围0~99999.9");
		return false;
	}
	
	if ($("#allowforgetclock_count").val() != "" && $("#allowforgetclock_count").val() != 0 ){
		if (!/^[1-9]\d{0,1}$/.test($("#allowforgetclock_count").val())){
			Ewin.alert("允许忘打卡次数格式不正确，范围0~99");
			return false;
		}
	}
	if ($("#forgetclock_cut").val() != "" ){
		if (!/^([1-9]\d{0,3}|0)(\.\d{0,1})?$/.test($("#forgetclock_cut").val())){
			Ewin.alert("忘打卡扣除格式不正确，范围0~9999.9");
			return false;
		}
	}
	if ($("#noclock_cut").val() != "" ){
		if (!/^([1-9]\d{0,3}|0)(\.\d{0,1})?$/.test($("#noclock_cut").val())){
			Ewin.alert("旷工扣除格式不正确，范围0~9999.9");
			return false;
		}
	}
	
	return true;
}

function validateLeave(){
	if (!leave($("#personalLeave").val())){
		return false
	}
	if (!leave($("#sickLeave").val())){
		return false
	}
	if (!leave($("#annualLeave").val())){
		return false
	}
	if (!leave($("#daysOff").val())){
		return false
	}
	if (!leave($("#wdddingLeave").val())){
		return false
	}
	if (!leave($("#maternityLeave").val())){
		return false
	}
	if (!leave($("#elseLeave").val())){
		return false
	}
	
	return true;
}

function leave(value){
	if (value == 0 || value == 100){
		return true;
	}
	if (!/^[1-9]\d{0,1}$/.test(value)){
		Ewin.alert("扣除格式不正确，范围0~100");
		return false;
	}
	return true;
}

//考勤计算规则
function renderSalaryConfig(renderObj,data){
    renderObj.html('');
    var renderStr = '';
    $(data).each(function(index,item){
        renderStr += formatStr(item);
    });
    renderObj.html(renderStr);
    bindIconDelete();
}

function bindIconDelete(){
    $(".icon-delete").bind('click',function(e){
        $(this).parent().parent().remove();
    })
}
//加载公司列表  companyArr为选择的公司数组
function loadCompanys(companyArr){
	var select = $("#company_select").empty();
	
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
			if (companyArr != undefined){
				select.selectpicker('val', companyArr);//默认选中
			}
			$('#company_select').selectpicker('refresh');
		},
		error : function(data, status, e) {
			Ewin.alert("系统内部错误！");
		}
	});
	
}

function operateFormatter(value, row, index) {
	return [
	        '<a class="edit"  href="javascript:void(0)" title="修改">修改</a>'
	        ].join('');
}

window.operateEvents = {
		'click .edit': function (e, value, row, index) {
			//标识。 0 表示新增操作，1 表示修改操作
			console.log(row)
	        flag = 1;
	        var editmodal = $("#salaryRule_config_modal");
	        editmodal.find("#salaryRuleName").val(row.salaryRuleName);
	        //加载公司列表
	        var companyArr = row.companyId.split(",");
	        loadCompanys(companyArr);
	        editmodal.find("#overtimeRule").prop("checked", row.overtimeFlag == 0 ? false : true);
	        editmodal.find("#socialInsurance").val(row.socialInsurance);
	        editmodal.find("#publicAccumulationFunds").val(row.publicAccumulationFunds);
	        editmodal.find("input[name='taxThreshold'][value='" + row.taxThreshold + "']").prop("checked", true);
	        
	        var laterCuts = row.laterCutPayment.split("|");
	        var latercutRules = [];
	        for (var i in laterCuts){
	        	latercutRules.push({
	                minute : laterCuts[i].split(",")[0],
	                value : laterCuts[i].split(",")[1],
	                unit : laterCuts[i].split(",")[2],
	            })
	        }
	        renderSalaryConfig($('#late_table>tbody'), latercutRules);
	        
	        var earlyLeaveCuts =  row.leaveEarlyCutPayment.split("|");
	        var earlyLeavcutRules = [];
	        for (var i in earlyLeaveCuts){
	        	earlyLeavcutRules.push({
	                minute : earlyLeaveCuts[i].split(",")[0],
	                value : earlyLeaveCuts[i].split(",")[1],
	                unit : earlyLeaveCuts[i].split(",")[2],
	            })
	        }
	        renderSalaryConfig($('#earlyLeave_table>tbody'), earlyLeavcutRules);
	        
	        $("#allowforgetclock_count").val(row.allowForgetClockCount);
	        $("#forgetclock_cut").val(row.forgetClockCutPayment);
	        $("#forgetclock_unit").val(row.forgetClockCutUnit);
	        $("#noclock_cut").val(row.noClockCutPayment);
	        $("#noclock_unit").val(row.noClockCutUnit);
	        
	        var leaveCuts = row.leaveCutPayment.split(",");
	    	$("#personalLeave").val(leaveCuts[0]);
	    	$("#sickLeave").val(leaveCuts[1]);
	    	$("#annualLeave").val(leaveCuts[2]);
	    	$("#daysOff").val(leaveCuts[3]);
	    	$("#wdddingLeave").val(leaveCuts[4]);
	    	$("#maternityLeave").val(leaveCuts[5]);
	    	$("#elseLeave").val(leaveCuts[6]);
	    	
	    	editmodal.find("#salarySule_id").val(row.rid);
	        
	    	editmodal.find(".modal-title").text("修改工资规则");
	        editmodal.modal("show");
		}
}

//all function
var SalaryConfigFun = {
    //打开新增dialog
    openAdd : function(){
    	//标识。 0 表示新增操作，1 表示修改操作
        flag = 0;
        var addModal = $("#salaryRule_config_modal");
		addModal.find("#salaryRule_config_form")[0].reset();
        loadCompanys();
        renderSalaryConfig($('#late_table>tbody'), late);
        renderSalaryConfig($('#earlyLeave_table>tbody'), earlyLeave);
        $(".selectpicker").selectpicker({
        	noneSelectedText : '请选择公司'
        });
        
        $("#allowforgetclock_count").val("");
        $("#forgetclock_cut").val("");
        $("#forgetclock_unit").val("0");
        $("#noclock_cut").val("");
        $("#noclock_unit").val("0");
    	$("#personalLeave").val("100");
    	$("#sickLeave").val("50");
    	$("#annualLeave").val("0");
    	$("#daysOff").val("0");
    	$("#wdddingLeave").val("0");
    	$("#maternityLeave").val("0");
    	$("#elseLeave").val("100");
        
        addModal.find(".modal-title").text("新增工资规则");
        addModal.modal("show");

    },
    //打开考勤配置
    openAttendance : function(){

        $("#attendanceConfigModal").modal("show");

    },
    //弹出框关闭监听事件
    com_listening : function(){
        $("#salaryRule_config_modal").on('hide.bs.modal',function () {
            
            
        });
    },
    addSalaryConfigLate : function(){
    	if ($("#late_table tbody tr").length >= 5){
    		Ewin.alert("迟到最多只能添加5项");
    		return;
    	}
        $('#late_table>tbody').append(salaryConfigTemplateStr.formatStr({minute:'', value:''}))
        bindIconDelete();
    },
    addSalaryConfigEarlyLeave : function(){
    	if ($("#earlyLeave_table tbody tr").length >= 5){
    		Ewin.alert("早退最多只能添加5项");
    		return;
    	}
        $('#earlyLeave_table>tbody').append(salaryConfigTemplateStr.formatStr({minute:'', value:''}))
        bindIconDelete();
    },
    //新增
    wagesAdd : function(){
        
    },
    //删除工资
    deletewages : function(e){
        
    },
    saveSalaryConfig:function(){
        var lateRules=[]    //迟到规则
        var earlyLeaveRules=[]; //早退规则
        $("#late_table tbody tr").each(function(index,item){
            lateRules.push({
                minute:$(item).find('input[name="minute"]').val(),
                value:$(item).find('input[name="value"]').val(),
                unit:$(item).find('select[name="unit"]').val(),
            })
        })
        $("#earlyLeave_table tbody tr").each(function(index,item){
            earlyLeaveRules.push({
                minute:$(item).find('input[name="minute"]').val(),
                value:$(item).find('input[name="value"]').val(),
                unit:$(item).find('select[name="unit"]').val(),
            })
        })
        console.log(lateRules);
        console.log(earlyLeaveRules);

    }


};