//0 新增工资表  1 修改工资表
var flag = 0;
var selectCompanyNumber = null;
var selectCompanyName = null;
var selectCalcDate = null;

$(function () {
	$("#datetimepicker_statistic").datetimepicker({  
		format: 'yyyy-mm',  
		language: 'zh-CN',  
		autoclose: 1,
		todayBtn:  1,
		todayHighlight: 1,
		startView: 'year', //这里就设置了默认视图为年视图
		minView: 'year'  //设置最小视图为年视图
	}).on('changeDate', function(ev){
		selectCalcDate =  $("#statistic_month").val();
		$('.attendance-date').html(selectCalcDate);
	});

	$("#datetimepicker_statistic_copy").datetimepicker({  
		format: 'yyyy-mm',  
		language: 'zh-CN',  
		autoclose: 1,
		todayBtn:  1,
		todayHighlight: 1,
		startView: 'year', //这里就设置了默认视图为年视图
		minView: 'year'  //设置最小视图为年视图
	}).on('changeDate', function(ev){
		selectCalcDate =  $("#statistic_month_copy").val();
		$('.attendance-date').html(selectCalcDate);
	});

	$("#datetimepicker_pay").datetimepicker({
		format: 'yyyy-mm-dd',  
		language: 'zh-CN',  
		pickDate: true,  
		pickTime: true,  
		autoclose: 1,
		todayBtn:  1,
		todayHighlight: 1,
		minView: "month"
	});

	$("#salary_upload_file").on('change', fileUploadOnChange);
	$("#salary_upload_file_rechoose").on('change', fileReUploadOnChange);
		
	loadCompanys();
	function loadCompanys(){
		var select = $(".compny-dropdown").html('');
		
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
					var option = '<li><a href="#" class="company-drop-item" data-number="' + data[i].companyNumber + '">' + data[i].name + '</a></li>';
					select.append(option);
				}
				$('.company-drop-item').on('click', function(e) {
					selectCompanyNumber = $(e.currentTarget).attr('data-number');
					selectCompanyName = $(e.currentTarget).html();
					$('.salary-company-name').html(selectCompanyName);
					$("#salary_create_modal").modal('show');
					goStep(1);
				});
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
	}

	$("#btn_remove").unbind().bind("click", function(){

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
	
	
	$("#choose_people").unbind().bind("click", function(){
		renderPeople(selectCompanyNumber, selectCompanyName);
		renderSelected("peoplesTree");
		$("#people_search").val("");
		$('#peopleModal').modal("show");
	});

	$("#people_search").unbind().bind("input propertychange", function(){
		var search = $("#people_search").val();
		renderPeople(selectCompanyNumber, selectCompanyName, search);
		
	});

	$("#people_confirm").unbind().bind("click", function(){
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
		var selectedPeoples = zTreeObj.getCheckedNodes(true);
		var count = 0;
		var nodes = "";
		var names = "";
		$(selectedPeoples).each(function(index,item){
			if (item.isParent) {
				var deptChildrenNodes = item.children;
				var deptNodes = "";
				if (deptChildrenNodes) {
					for (var i = 0; i < deptChildrenNodes.length; i++) {
						if (deptChildrenNodes[i].isParent){
							var childrenNodes = deptChildrenNodes[i].children;
							for (var j = 0; j < childrenNodes.length; j++){
								if (childrenNodes[j].checked){
									nodes += childrenNodes[j].id + ",";
									names += childrenNodes[j].name + ",";
								}
							}
							if (nodes == "" || nodes == null || names == "" || nodes == null){
								continue;
							}
							deptNodes += deptChildrenNodes[i].id + "_" + nodes.substring(0, nodes.length - 1) + "-";
							
						}
						
					}
				}
				
				if (deptNodes == "" || deptNodes == null){
					return;
				}
				
				return;
			}
			count = count + 1;
		});
		
		if (count <= 0 || count > 5){
			Ewin.alert("请选择审批人员,最多5个。");
			return false;
		}

		$('#choose_id').val(nodes.substring(0, nodes.length - 1));
		$('#choose_people').val(names.substring(0, names.length - 1));
		$('#peopleModal').modal("hide");

	});

	//选择所有员工事件
	$("#checkAll").unbind().bind("change", function(event){
		var checkAll = event.currentTarget;
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
		zTreeObj.checkAllNodes(checkAll.checked);
	    renderSelected("peoplesTree");
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



function goStep(stepNumber, secondStepNumber, width) {

	$('.step').hide();
	$('.step-' + stepNumber +(secondStepNumber ? '-' + secondStepNumber : '')).show();
	$('#salary_create_modal .modal-dialog').css('width', width ? width + 'px' : '');
}

var salaryData = null;
function calcSalary(){
	if(selectCalcDate == null || selectCalcDate == ''){
		Ewin.alert("请先选择考勤统计月份");
		return false;
	}
	goStep(1, 2)


	$.ajax({
		url : "importUserInfo",
		type: "post",
		data : {
			companyNum : selectCompanyNumber,
			salaryMonth : selectCalcDate
		},
		async : true, 
		dataType : "json",
		success : function(data, status) {
			var code = data['code'];
			goStep(1, 3, 1300);
			if (code == 1000) {
				Ewin.alert("导入员工信息成功！");
				salaryData = data['data'].details;

				$('#tb_saraly_configs_copy').on('post-body.bs.table', tableLoadSuceess);
				$('#tb_saraly_configs_copy').bootstrapTable("load", salaryData);
							
				$('#tb_saraly_configs_copy').bootstrapTable('prepend',{
					'id':0,
					'userName': '快速批量设置',
					'cardNo': '-',
					'preTaxSalaries': '-',
					'bonuses': 0,
					'subsidies': 0,
					'attendanceCutPayment': '-',
					'askForLeaveCutPayment': '-',
					'overtimePayment': '-',
					'socialInsurance': 0,
					'publicAccumulationFunds': 0,
					'taxThreshold': 0,
					'personalIncomeTax': '-',
					'elseCutPayment': 0,
					'salaries': '-',
				});
			}else{
				Ewin.alert("导入员工信息失败！" + data['message']);
			}
		},
		error : function(data, status, e) {
			Ewin.alert("系统内部错误！");
		}
	});
}

function reImportSalaryData() {
	goStep(2, 2);

	$.ajaxFileUpload({
		url : "importSalaryList",
		type: "post",
		secureuri : false,
		fileElementId : "salary_upload_file_rechoose",
		dataType : "json",
		data : {
			companyId : selectCompanyNumber
		},
		success : function(data, status) {
			$("#salary_upload_file_rechoose").val('');
			$("#salary_upload_file_rechoose").on('change', fileReUploadOnChange);
			goStep(2, 3, 1300);
			data = $.parseJSON(data.replace(/<.*?>/ig,""));
			var code = data['code'];
			if (code == 1000) {
				salaryData = data['data'].details;
				renderImportResult(data['data']);
				showImportResult();
			}else{
				Ewin.alert("上传失败！" + data['message']);
			}
		},
		error : function(data, status, e) {
			$("#salary_upload_file_rechoose").on('change', fileReUploadOnChange);
			Ewin.alert("上传发生异常");
		}
	})
}

function importSalaryData() {
	if(selectCalcDate == null || selectCalcDate == ''){
		Ewin.alert("请先选择工资发放月份");
		return false;
	}
	if($("#salary_upload_file").val() == '') {
		Ewin.alert("请先选择要上传的文件");
		return false;
	}

	goStep(2, 2);

	$.ajaxFileUpload({
		url : "importSalaryList",
		type: "post",
		secureuri : false,
		fileElementId : "salary_upload_file",
		dataType : "json",
		data : {
			companyId : selectCompanyNumber
		},
		success : function(data, status) {
			$("#salary_upload_file").val('');
			$("#salary_upload_file").on('change', fileUploadOnChange);
			goStep(2, 3, 1300);
			data = $.parseJSON(data.replace(/<.*?>/ig,""));
			var code = data['code'];
			if (code == 1000) {
				salaryData = data['data'].details;
				renderImportResult(data['data']);
				showImportResult();
			}else{
				Ewin.alert("上传失败！" + data['message']);
			}
		},
		error : function(data, status, e) {
			$("#salary_upload_file").on('change', fileUploadOnChange);
			Ewin.alert("上传发生异常");
		}
	})
}

var importError = false;
function showImportResult() {
    $("#salary_import_table").bootstrapTable({
        striped : true, // 是否显示行间隔色
        editable: true,
        cache : false, // 是否使用缓存，默认为true
        pagination : true, // 是否显示分页
        contentType : "application/x-www-form-urlencoded",
        sidePagination : "client", // 分页方式：client客户端分页，server服务端分页
        pageSize : 10, // 每页的记录行数
        pageList : [15, 20], // 可供选择的每页的行数
        clickToSelect : true,
        data: salaryData,
        columns : [
            {field : 'eid', title: '序号', width: 5, align : 'center', formatter: function(value, row, index, field){
                return parseInt(value);
            }},
            {field : 'userName', title: '姓名', width: 70, align : 'center'},
            {field : 'cardNo', title: '身份证号码', width: 80, align : 'center'},
            {field : 'preTaxSalaries', title: '税前工资', width: 10, align : 'center'},
            {field : 'bonuses', title: '奖金', width: 90, align : 'center'},
            {field : 'subsidies', title: '补贴', width: 80, align : 'center'},
            {field : 'totalCutPayment', title: '考勤扣款', width: 80, align : 'center'},
            {field : 'askForLeaveCutPayment', title: '请假扣款', width: 80, align : 'center'},
            {field : 'overtimePayment', title: '加班费', width: 80, align : 'center'},
            {field : 'socialInsurance', title: '社保缴纳', width: 80, align : 'center'},
            {field : 'publicAccumulationFunds', title: '公积金', width: 80, align : 'center'},
            {field : 'taxThreshold', title: '个税起征点', width: 80, align : 'center'},
            {field : 'personalIncomeTax', title: '个人所得税', width: 80, align : 'center'},
            {field : 'elseCutPayment', title: '其他扣款', width: 80, align : 'center'},
            {field : 'salaries', title: '应发工资', width: 80, align : 'center'},
            {field : 'remark', title: '', width: 30, align : 'left', formatter: function(value, row, index, field){
				if(value === "") return '';
				importError = true;
                return '<i class="error-info glyphicon glyphicon-alert ' + (value.indexOf('已被注册') > -1 ? 'exists-info' : '') + '" data-info="' + value + '"></i>';
            }}
        ],
        onPostBody: function(e, b, c) {
            //错误信息展示
            $(".error-info").on('mouseenter', showErrorInfo)
            //移除错误信息
            $(".error-info").on('mouseout', function(e) {
                $(".error-info-tip").remove();
            })
        }
    });
     $("#salary_import_table").bootstrapTable('load', salaryData);

}

function showErrorInfo(e) {
    var msg = $(e.currentTarget).attr('data-info');
    var msgs = msg.split(',');
    var msgsDom = '';
    $(msgs).each(function(index, item) {
        msgsDom += "<div>" + (index + 1) + ":" + item + "</div>"
    });
    $('body').append('<div class="error-info-tip ' + ($(e.currentTarget).hasClass('exists-info') ? 'error-info-exists-tip': '') + 
        '" style="left:' + 
        ($(e.currentTarget).offset().left + 40) + 
        'px;top:' + ($(e.currentTarget).offset().top + e.currentTarget.offsetHeight / 2 ) + 'px;">' + msgsDom + '</div>')
}

function renderImportResult(importData) {
    $("#importResult").html("一共识别" + (importData.successCount + importData.failCount) + "条，其中" + importData.successCount + "条正确，" + importData.failCount + "条有误" );    
}

/**
 * 通过列index获取列名
 * @param {列index} index
 */
function getFieldNameByColumnIndex(index) {
    return $($("#tb_saraly_configs_copy thead tr").children()[index]).attr('data-field');
}

function tableLoadSuceess(){
	console.log('a');
	var table = $('#tb_saraly_configs_copy');
	var isEdit = table.bootstrapTable('getEditStatus')
	if (isEdit){
		$('#tb_saraly_configs_copy').bootstrapTable('cancelEditAll');
//		    $('#tb_saraly_configs').bootstrapTable('removeRow', 0);
	}
	table.bootstrapTable('editAll');

	var firstRow = table.find('tbody').children()[0];
	var inputs = $(firstRow).find('input');
	var selects = $(firstRow).find('select');
	inputs.on('keyup', function(e){
		var tdIndex = $(this).parent().parent().index();
		var value = this.value;
		syncFieldData(getFieldNameByColumnIndex(tdIndex), value);
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
}

function syncFieldData(fieldName, data) {
	$(salaryData).each(function(index, item) {
		item[fieldName] = data;
	})
}

function cancelEdit() {
	$('#tb_saraly_configs_copy').bootstrapTable('cancelEditAll');
	$('#tb_saraly_configs_copy').bootstrapTable('removeRow', 0);
}

function commitAudit(){
	var pay_date = $("#pay_salaries_date").val();
	if (pay_date == "" || pay_date == null || pay_date == undefined){
		Ewin.alert("请先选择发放时间");
		return false;
	}
	var people = $("#choose_people").val();
	if (people == "" || people == null || people == undefined){
		Ewin.alert("请先选择审批人员。");
		return false;
	}
	
	var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
	var selectedPeoples = zTreeObj.getCheckedNodes(true);
	var count = 0;
	var nodes = "";
	var names = "";
	$(selectedPeoples).each(function(index,item){
		if (item.isParent) {
			var deptChildrenNodes = item.children;
			var deptNodes = "";
			if (deptChildrenNodes) {
				for (var i = 0; i < deptChildrenNodes.length; i++) {
					if (deptChildrenNodes[i].isParent){
						var childrenNodes = deptChildrenNodes[i].children;
						for (var j = 0; j < childrenNodes.length; j++){
							if (childrenNodes[j].checked){
								nodes += childrenNodes[j].id + ",";
								names += childrenNodes[j].name + ",";
							}
						}
						if (nodes == "" || nodes == null || names == "" || nodes == null){
							continue;
						}
						deptNodes += deptChildrenNodes[i].id + "_" + nodes.substring(0, nodes.length - 1) + "-";
						
					}
					
				}
			}
			
			if (deptNodes == "" || deptNodes == null){
				return;
			}
			
			return;
		}
		count = count + 1;
	});
	
	if (count <= 0 || count > 5){
		Ewin.alert("请选择审批人员，最多5个");
		return false;
	}
	
	var params = {};
	params.companyId = selectCompanyNumber;
	params.companyName = selectCompanyName;
	params.salaryMonth = selectCalcDate;
	params.salaryDate = pay_date;
	params.approveIds = nodes.substring(0, nodes.length - 1);
	params.approveNames = names.substring(0, names.length - 1);
	
	cancelEdit();
	var allDatas = salaryData;
	if (allDatas == null || allDatas.length <= 0){
		Ewin.alert("请先导入员工信息或导入最近工资表信息。");
		return false;
	}
	var totalAmount = 0;
	for (var i in allDatas){
		totalAmount += allDatas[i].salaries;
	}
	params.totalNumber = allDatas.length;
	params.totalAmount = totalAmount;
	params.details = allDatas;
	
	$.ajax({
		url : "saveSalary",
		type: "post",
		contentType : 'application/json;charset=utf-8',
		data : JSON.stringify(params),
		dataType : "json",
		success : function(data, status) {
			var code = data['code'];
			if (code == 1000) {
				Ewin.alert("保存成功");
				window.location.href = "salary";
			}else{
				Ewin.alert(data['message']);
			}
		},
		error : function(data, status, e) {
			Ewin.alert("系统异常");
		}
	});	
	

}


//渲染已选择的员工
function renderSelected(treeId) {
	var zTreeObj = $.fn.zTree.getZTreeObj(treeId);
    var selectedPeoples = zTreeObj.getCheckedNodes(true);
    var selectedPeoplesDom = "";
    var data = new Object();
    $(selectedPeoples).each(function(index,item){
        if (item.isParent) {
            return;
        }
        selectedPeoplesDom += '' +
            '<tr><td>' +
            '<span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp' + item.name + '</td>' +
            '</tr>';
    })
    $("#peopleModal").find('.selected-people-table').html(selectedPeoplesDom);
}


//选择部门与人员modal 所有员工渲染
function renderPeople(companyId, companyName, search) {
	if (search == undefined){
		search = "";
	}
	if (companyId == undefined || companyName == undefined){
		return;
	}
	$.ajax({
		url : "getDeptsAndUsersFromCompany",
		type: "get",
		data : {
			companyId : companyId,
			companyName : companyName,
			search : search
		},
		async : false, 
		dataType : "json",
		success : function(data, status) {
			$.fn.zTree.init($("#peoplesTree"), setting, data);
		},
		error : function(data, status, e) {
			Ewin.alert("系统内部错误！");
		}
	});
	
}

var setting = {
	    check: {
	        enable: true,
	        chkStyle: "checkbox",
        chkboxType: { "Y": "ps", "N": "ps" }
    },
    callback: {
        onCheck: function (event, treeId, treeNode) {
            
            renderSelected(treeId);
        }
    }
};

function getFileName(fullPath) {
    var paths =  fullPath.split('\\')
    return paths[paths.length -1];
}

function goToAudit() {
	if(importError) {
		Ewin.alert("上传信息有错误，请检查，重新上传！");
		return;
	}
	goStep(1, 4);
}

function fileUploadOnChange(e) {

	if($(e.currentTarget).val() != '') {
		$(".import-file-name").html('已选择：' + $(e.currentTarget).val());
		$("#importFileName").html('（' + getFileName($(e.currentTarget).val()) + '）');
	} else {
		$(".import-file-name").html('');
	}
}

function fileReUploadOnChange(e) {
	if($(e.currentTarget).val() != '') {
		reImportSalaryData();
		$("#importFileName").html('（' + getFileName($(e.currentTarget).val()) + '）');
	}
}