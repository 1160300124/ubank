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

function operateFormatter(value, row, index) {
	return [
	        '<a class="edit"  href="javascript:void(0)" title="编辑">编辑</a>'
	        ].join('');
}

window.operateEvents = {
		'click .edit': function (e, value, row, index) {
			
		}
}

