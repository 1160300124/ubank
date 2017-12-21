$(function(){
	$("#btn_all").unbind().bind("click", function(){
		$("#btn_all").removeClass("btn-default").addClass("btn-primary");
		$("#btn_on_the_job").removeClass("btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("btn-primary").addClass("btn-default");
	});
	$("#btn_on_the_job").unbind().bind("click", function(){
		$("#btn_on_the_job").removeClass("btn-default").addClass("btn-primary");
		$("#btn_all").removeClass("btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("btn-primary").addClass("btn-default");
	});
	$("#btn_leave_office").unbind().bind("click", function(){
		$("#btn_leave_office").removeClass("btn-default").addClass("btn-primary");
		$("#btn_on_the_job").removeClass("btn-primary").addClass("btn-default");
		$("#btn_all").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("btn-primary").addClass("btn-default");
	});
	$("#btn_salary_set").unbind().bind("click", function(){
		$("#btn_salary_set").removeClass("btn-default").addClass("btn-primary");
		$("#btn_on_the_job").removeClass("btn-primary").addClass("btn-default");
		$("#btn_all").removeClass("btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_change").removeClass("btn-primary").addClass("btn-default");
	});
	$("#btn_salary_change").unbind().bind("click", function(){
		$("#btn_salary_change").removeClass("btn-default").addClass("btn-primary");
		$("#btn_on_the_job").removeClass("btn-primary").addClass("btn-default");
		$("#btn_all").removeClass("btn-primary").addClass("btn-default");
		$("#btn_leave_office").removeClass("btn-primary").addClass("btn-default");
		$("#btn_salary_set").removeClass("btn-primary").addClass("btn-default");
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

function operateFormatter(value, row, index) {
	return [
	        '<a class="edit"  href="javascript:void(0)" title="编辑">编辑</a>'
	        ].join('');
}

window.operateEvents = {
		'click .edit': function (e, value, row, index) {
			
		}
}

function validateMoney(e) {
	if(!Validate.isMoney($(e.currentTarget).val()))
	{
		$(e.currentTarget).parent().addClass('error-money');
		return false;
	}
	else {
		$(e.currentTarget).parent().removeClass('error-money');
		return true;
	}
}