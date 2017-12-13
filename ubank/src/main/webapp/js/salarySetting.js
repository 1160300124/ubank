//0 新增工资表  1 修改工资表
var flag = 0;
$(function () {
	$("#datetimepicker_statistic").datetimepicker({  
		format: 'yyyy-mm',  
		language: 'zh-CN',  
		autoclose: 1,
		todayBtn:  1,
		todayHighlight: 1,
		startView: 'year', //这里就设置了默认视图为年视图
		minView: 'year'  //设置最小视图为年视图
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
				$("#company_select").val(companyId);
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
	}
	
	load();
	function load(){
		if (sid == "" || sid == null || sid == undefined){
			//0 新增工资表  1 修改工资表
			flag = 0;
			return;
		}
		flag = 1;
		$("#statistic_month").val(salaryMonth);
		$("#pay_salaries_date").val(salaryDate);
		$("#pay_salaries_date").val(salaryDate);
		$("#choose_id").val(peopleId);
		$("#choose_people").val(peopleName);
		
		$.ajax({
			url : "getSalaryDetailsBySid",
			type: "get",
			data : {
				sid : sid
			},
			async : true, 
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					$('#tb_saraly_configs').bootstrapTable("load", data["data"]);
					tableLoadSuceess();
				}else{
					Ewin.alert("导入员工信息失败！" + data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
	}
	
	function tableLoadSuceess(){
		var table = $('#tb_saraly_configs');
		var isEdit = table.bootstrapTable('getEditStatus')
		if (isEdit){
			$('#tb_saraly_configs').bootstrapTable('cancelEditAll');
//		    $('#tb_saraly_configs').bootstrapTable('removeRow', 0);
		}
		table.bootstrapTable('prepend',{
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
	}
	
	//点击选中列，判断checkbox选中与取消选中
	$('#tb_saraly_configs').on('click-cell.bs.table', function (e, field, value, row, $element){
		
	});
	
	
	
	$("#btn_import_user_info").unbind().bind("click", function(){
		var select = $("#company_select").val();
		if (select == "" || select == null || select == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		var month = $("#statistic_month").val();
		if (month == "" || month == null || month == undefined){
			Ewin.alert("请先选择考勤统计月份");
			return false;
		}

		var allDatas = $('#tb_saraly_configs').bootstrapTable('getData');
		if (allDatas.length > 0){
			Ewin.confirm({ message: "确定要覆盖工资表信息吗？" }).on(function (e) {
				if (!e) {
					return;
				}
				$.ajax({
					url : "importUserInfo",
					type: "post",
					data : {
						companyNum : select,
						salaryMonth : month
					},
					async : true, 
					dataType : "json",
					success : function(data, status) {
						var code = data['code'];
						if (code == 1000) {
							Ewin.alert("导入员工信息成功！");
							$('#tb_saraly_configs').bootstrapTable("load", data["data"]);
							tableLoadSuceess();
						}else{
							Ewin.alert("导入员工信息失败！" + data['message']);
						}
					},
					error : function(data, status, e) {
						Ewin.alert("系统内部错误！");
					}
				});
			});
			
		} else {
			$.ajax({
				url : "importUserInfo",
				type: "post",
				data : {
					companyNum : select,
					salaryMonth : month
				},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("导入员工信息成功！");
						$('#tb_saraly_configs').bootstrapTable("load", data["data"]);
						tableLoadSuceess();
					}else{
						Ewin.alert("导入员工信息失败！" + data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
		}

	});
	
	$("#btn_import_latest_salary").unbind().bind("click", function(){
		var select = $("#company_select").val();
		if (select == "" || select == null || select == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		
		var allDatas = $('#tb_saraly_configs').bootstrapTable('getData');
		if (allDatas.length > 0){
			Ewin.confirm({ message: "确定要覆盖工资表信息吗？" }).on(function (e) {
				if (!e) {
					return;
				}
				$.ajax({
					url : "importLatestSalary",
					type: "post",
					data : {
						comNum : select
					},
					async : true, 
					dataType : "json",
					success : function(data, status) {
						var code = data['code'];
						if (code == 1000) {
							Ewin.alert("导入最近工资表成功！");
							$('#tb_saraly_configs').bootstrapTable("load", data["data"]);
							tableLoadSuceess();
						}else{
							Ewin.alert("导入最近工资表失败！" + data['message']);
						}
					},
					error : function(data, status, e) {
						Ewin.alert("系统内部错误！");
					}
				});
			});
			
		} else {
			$.ajax({
				url : "importLatestSalary",
				type: "post",
				data : {
					comNum : select
				},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("导入最近工资表成功！");
						$('#tb_saraly_configs').bootstrapTable("load", data["data"]);
						tableLoadSuceess();
					}else{
						Ewin.alert("导入最近工资表失败！" + data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
		}
		
	});
	
	$("#btn_close").unbind().bind("click", function(){
		Ewin.confirm({ message: "确定要离开工资配置页面吗？" }).on(function (e) {
			if (!e) {
				return;
			}
			
			window.location.href = "salary";
		});
	});
	
	$("#btn_save").unbind().bind("click", function(){
		var companyId = $("#company_select").val();
		var companyName = $("#company_select").find("option:selected").text();
		if (companyId == "" || companyId == null || companyId == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		var statistic_date = $("#statistic_month").val();
		var pay_date = $("#pay_salaries_date").val();
		if (statistic_date == "" || statistic_date == null || statistic_date == undefined){
			Ewin.alert("请先选择统计月份");
			return false;
		}
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
	    params.companyId = companyId;
	    params.companyName = companyName;
	    params.salaryMonth = statistic_date;
	    params.salaryDate = pay_date;
	    params.approveIds = nodes.substring(0, nodes.length - 1);
	    params.approveNames = names.substring(0, names.length - 1);
	    
	    $('#tb_saraly_configs').bootstrapTable('cancelEditAll');
	    $('#tb_saraly_configs').bootstrapTable('removeRow', 0);
	    var allDatas = $('#tb_saraly_configs').bootstrapTable('getData');
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
	    
	  //0 新增工资表  1 修改工资表
	    if (flag == 0){
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
	    } else {
	    	params.sid = sid;
	    	$.ajax({
	    		url : "updateSalary",
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

	    

	});
	
	$("#choose_people").unbind().bind("click", function(){
		var companyId = $("#company_select").val();
		var companyName = $("#company_select").find("option:selected").text();
		if (companyId == "" || companyId == null || companyId == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		renderPeople(companyId, companyName);
		if (flag == 1){
			renderPeople(companyId, companyName);
			var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
			zTreeObj.checkAllNodes(false);   //清空tree
			zTreeObj.checkNode(zTreeObj.getNodeByParam("id", companyId), true);
			var peopleArr = peopleId.split(",");
			for (var i in peopleArr){
				zTreeObj.checkNode(zTreeObj.getNodeByParam("id", peopleArr[i]), true);
			}
			renderSelected("peoplesTree");
		}
		$("#people_search").val("");
		$('#peopleModal').modal("show");

		
	});
	
	$("#people_search").unbind().bind("input propertychange", function(){
		var companyId = $("#company_select").val();
		var companyName = $("#company_select").find("option:selected").text();
		if (companyId == "" || companyId == null || companyId == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		var search = $("#people_search").val();
		renderPeople(companyId, companyName, search);
		
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


