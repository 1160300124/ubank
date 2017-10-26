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

	
	$('#tb_saraly_configs').on('load-success.bs.table',function(data){
	       console.log("load success");
	       var table = $('#tb_saraly_configs');
	       var isEdit = table.bootstrapTable('getEditStatus')
	       if (isEdit) return;
			table.bootstrapTable('prepend',{
		        'id':0,
		        'userName': '快速批量设置',
		        'cardNo': '--',
		        'pre_tax_salaries': '10000',
		        'bonuses': 0,
		        'subsidies': 0,
		        'attendance_cut_payment': '--',
		        'askForLeave_cut_payment': '--',
		        'overtime_payment': '--',
		        'socialInsurance': 0,
		        'publicAccumulationFunds': 0,
		        'taxThreshold': 3500,
		        'personalIncomeTax': '--',
		        'else_cut_payment': 0,
		        'salaries': '--',
		    });
			table.bootstrapTable('editAll');

		    var firstRow = table.find('tbody').children()[0];
		    var inputs = $(firstRow).find('input');
		    var selects = $(firstRow).find('select');
		    inputs.on('keyup', function(e){
		        var tdIndex = $(this).parent().parent().index();
		        var value = this.value;
		        table.find('tbody').children().each(function(index, row){
		            if (index !== 0){
		                $($(row).children()[tdIndex]).find('input[type="text"]').val(value);
		            }
		        })
		    });
		    selects.on('change', function(e){
		        var tdIndex = $(this).parent().index();
		        var value = this.value;
		        table.find('tbody').children().each(function(index, row){
		            if (index !== 0){
		                $($(row).children()[tdIndex]).find('select').val(value);
		            }
		        })
		    });
	});
	
	$("#btn_import_user_info").unbind().bind("click", function(){
		var select = $("#company_select").val();
		if (select == "" || select == null || select == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		
	});
	
	$("#btn_import_latest_salary").unbind().bind("click", function(){
		var select = $("#company_select").val();
		if (select == "" || select == null || select == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		
	});
	
	$("#choose_people").unbind().bind("click", function(){
		
		renderPeople("add");
		$("#people_search").val("");
		$('#peopleModal').modal("show");

		
	});

});

//选择部门与人员modal 所有员工渲染
function renderPeople(flag, search) {
	if (search == undefined){
		search == "";
	}
	$.ajax({
		url : "getDeptsAndUsers",
		type: "get",
		data : {
			search : search
		},
		async : false, 
		dataType : "json",
		success : function(data, status) {
			if (flag == "add"){
				$.fn.zTree.init($("#peoplesTree"), setting, data);
			} else if (flag == "edit") {
				$.fn.zTree.init($("#peoplesTree_edit"), setting, data);
			}
		},
		error : function(data, status, e) {
			Ewin.alert("系统内部错误！");
		}
	});
	
}

function operateFormatter(value, row, index) {
	return [
	        '<a class="detail"  href="javascript:void(0)" title="详情">详情</a>'
	        ].join('');
}


window.operateEvents = {
		'click .detail': function (e, value, row, index) {
			
			window.location.href = "salaryDetail?sid=" + row.sid;
//			$("#detail_modal").modal("show");
//			$("#tb_salary_detail").bootstrapTable("refresh", {
//				url : "details?sid=" + row.sid
//			});
		}
};


