//0：新增   1：修改
var flag = 0;
$(function(){

	$(".time-picker").datetimepicker({  
		format: 'hh:ii',
		language: 'zh-CN',  
		pickDate: true,  
		pickTime: true,  
		autoclose: 1,
		todayBtn:  1,
		todayHighlight: 1,
		startView: 1,
		minView: 0
	});  

	$('.icheck').iCheck({
		checkboxClass: 'icheckbox_square-blue',
		radioClass: 'iradio_square-blue',
		increaseArea: '20%'
	});
	
	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_rules").bootstrapTable('getSelections');
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
				url : "deleteRules",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_rules").bootstrapTable("refresh");
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

	//工作日，假数据
	var workDay = [
	   {
		   name: '周一',
		   isWork: true,
		   workTime: '09:00-18:00'
	   },
	   {
		   name: '周二',
		   isWork: true,
		   workTime: '09:00-18:00'
	   },
	   {
		   name: '周三',
		   isWork: true,
		   workTime: '09:00-18:00'
	   },
	   {
		   name: '周四',
		   isWork: true,
		   workTime: '09:00-18:00'
	   },
	   {
		   name: '周五',
		   isWork: true,
		   workTime: '09:00-18:00'
	   },
	   {
		   name: '周六',
		   isWork: false,
		   workTime: '休息'
	   },
	   {
		   name: '周日',
		   isWork: false,
		   workTime: '休息'
	   }
	]

	$("#btn_add").unbind().bind("click", function(){
		//0：新增  1：修改
		flag = 0;
		$("#myModalLabel").text("新增考勤规则");attendance_people_
		$("#form")[0].reset();
		$('.work-day').empty();
		$("#table_attendanceLocation").empty();
		$('.work-day').append('周一&nbsp;&nbsp;&nbsp;周二&nbsp;&nbsp;&nbsp;周三&nbsp;&nbsp;&nbsp;周四&nbsp;&nbsp;&nbsp;周五&nbsp;&nbsp;&nbsp;');

		var advanceSettingModal = $("#advanceSettingModal");

		$("#work_before").text(" " + 2 + " ");
		$("#work_after").text(" " + 6 + " ");
		advanceSettingModal.find('#work_on_before_hours').val("2");
		advanceSettingModal.find('#work_off_after_hours').val("6");
		advanceSettingModal.find("#openFlexible").iCheck('uncheck');
		advanceSettingModal.find("#shunyan").iCheck('uncheck');
		advanceSettingModal.find("#shunyan").iCheck('uncheck');
		
		renderPeople();
		$("#attendancePeopleModal").find('.selected-people-table').html("");
		$("#attendancePeopleModal_no").find('.selected-people-table').html("");
		
		$("#modal").modal("show");
	});

	$("#btn_add_confirm").unbind().bind("click", function(){

		var params = {};

		var modal = $("#modal");
		var ruleName = modal.find("#rule_name").val();
		var startTime = modal.find("#start_time").val();
		var endTime = modal.find("#end_time").val();
		if (ruleName == null || ruleName == "" || ruleName == undefined || ruleName.length > 40){
			Ewin.alert("规则名称长度40个字符以内。");
			return false;
		}
		if (startTime == null || startTime == "" || startTime == undefined){
			Ewin.alert("请选择工作开始时间。");
			return false;
		}
		if (endTime == null || endTime == "" || endTime == undefined){
			Ewin.alert("请选择工作结束时间。");
			return false;
		}

		params.ruleName = ruleName;
		params.clockOnTime = startTime;
		params.clockOffTime = endTime;

		var isRestChecked = modal.find("#input-rest").prop("checked");
		var restStartTime = modal.find("#rest_start_time").val();
		var resteEndTime = modal.find("#rest_end_time").val();
		params.restFlag = isRestChecked ? 0 : 1;
		if (isRestChecked){
			if (restStartTime == null || restStartTime == "" || restStartTime == undefined){
				Ewin.alert("请选择休息开始时间。");
				return false;
			}
			if (resteEndTime == null || resteEndTime == "" || resteEndTime == undefined){
				Ewin.alert("请选择休息结束时间。");
				return false;
			}
			params.restStartTime = restStartTime;
			params.restEndTime = resteEndTime;
		}

		var workday = $(".work-day").html().replace(/&nbsp;&nbsp;&nbsp;/g, ",");
		params.workday = workday.substring(0, workday.length-1);

		var isWorkChecked = modal.find("#freeDay_check").prop("checked");
		params.holidayFlag = isWorkChecked ? 0 : 1;

		var settingModal = $("#advanceSettingModal");
		var isOpenFlexible = settingModal.find("#openFlexible").prop("checked");
		var flexibleTime = 0;
		var flexibleFlag = isOpenFlexible ? 0 : 1;
		if (isOpenFlexible){
			flexibleTime = settingModal.find("#flexibleTime").val();
			postponeFlag = settingModal.find("input[type='radio']:checked").val();
			params.flexibleTime = flexibleTime;
			params.postponeFlag = postponeFlag;
		}
		params.flexibleFlag = flexibleFlag;

		var workOnBeforeHours = modal.find("#work_before").text().trim();
		var workOffAfterHours = modal.find("#work_after").text().trim();
		params.clockOnAdvanceHours = workOnBeforeHours;
		params.clockOffDelayHours = workOffAfterHours;

		var table = document.getElementById("table_attendanceLocation");
		if (table.rows.length <= 0){
			Ewin.alert("请添加考勤地点。");
			return false;
		}
		var clockLocation = "";
		var longit_latit = "";
		for (var i = 0; i < table.rows.length; i++){
			clockLocation += table.rows[i].cells[0].innerHTML + ",";
			longit_latit += table.rows[i].cells[1].innerHTML + "|";
		}
		if (clockLocation == "" || longit_latit == ""){
			Ewin.alert("请添加考勤地点。");
			return false;
		}
		params.longit_latit = longit_latit.substring(0, longit_latit.length - 1);
		params.clockLocation = clockLocation.substring(0, clockLocation.length - 1);
		
		var bounds = $("#bounds").val();
		params.clockBounds = bounds;
		
		var tree = getDataFormTree("peoplesTree");
		if (!tree){
			return false;
		} else {
			params.ruleData = tree.data;
			params.ruleCount = tree.count;
		}
		var tree_no = getDataFormTree("peoplesTree_no");
		params.noRuleData = tree_no.data;
		params.noRuleCount = tree_no.count;
		//0 新增  1 修改
		if (flag == 0){
			$.ajax({
				url : "addRule",
				type: "post",
				data : params,
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						modal.modal("hide");
						$("#tb_rules").bootstrapTable("refresh");
						Ewin.alert("新增成功。");
						
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
			
		} else if (flag == 1){
			var rid = $("#rule_id").val();
			params.rid = rid;
			$.ajax({
				url : "updateRule",
				type: "post",
				data : params,
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						modal.modal("hide");
						$("#tb_rules").bootstrapTable("refresh");
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
	
	function getDataFormTree(treeId){
		var tree = {};
		var zTreeObj = $.fn.zTree.getZTreeObj(treeId);
	    var selectedPeoples = zTreeObj.getCheckedNodes(true);
	    var count = 0;
	    var result = "";
	    $(selectedPeoples).each(function(index,item){
	        if (item.isParent) {
	        	var deptChildrenNodes = item.children;
	        	var deptNodes = "";
	        	if (deptChildrenNodes) {
	                for (var i = 0; i < deptChildrenNodes.length; i++) {
	                	var nodes = "";
	                	if (deptChildrenNodes[i].isParent){
	                		var childrenNodes = deptChildrenNodes[i].children;
	                		for (var j = 0; j < childrenNodes.length; j++){
	                			if (childrenNodes[j].checked){
	                				nodes += childrenNodes[j].id + ",";
	                			}
	                		}
	                		if (nodes == "" || nodes == null){
	                			continue;
	                		}
	                		deptNodes += deptChildrenNodes[i].id + "_" + nodes.substring(0, nodes.length - 1) + "-";
	                		
	                	}
	                	
	                }
	            }
	        	
	        	if (deptNodes == "" || deptNodes == null){
	        		return;
	        	}
	        	
		    	result += item.id + "=" + deptNodes.substring(0, deptNodes.length - 1) + "|";
		        return;
	        }
	        count = count + 1;
	    });
	    
	    if (treeId == "peoplesTree"){
	    	if (count <= 0){
	    		Ewin.alert("请选择参与考勤人员");
	    		return false;
	    	}
	    }
	    if (result == ""){
	    	tree.data = "";
	    } else {
	    	tree.data = result.substring(0, result.length - 1);;
	    }
	    tree.count = count;
		return tree;
	}

	
	$("#workDay_setting").unbind().bind("click", function(){
		var addModal = $("#modal");
		var startTime = addModal.find("#start_time").val();
		var endTime = addModal.find("#end_time").val();
		if (startTime == null || startTime == "" || startTime == undefined){
			Ewin.alert("请选择工作开始时间。");
			return false;
		}
		if (endTime == null || endTime == "" || endTime == undefined){
			Ewin.alert("请选择工作结束时间。");
			return false;
		}

		var workDayModal = $("#workDayModal");

		$('.work-day-list').empty();
		renderWorkDay(startTime, endTime);

		workDayModal.modal("show");
	});
	
	$("#workDayModal_confirm").unbind().bind("click", function(){
		var workDayModal = $("#workDayModal");
		var realWorkDay = workDayModal.find('.work-day-list').find('input[type="checkbox"]')
		$('.work-day').empty();
		$(realWorkDay).each(function (index, item) {
			if (item.checked){
				$('.work-day').append(item.value + '&nbsp;&nbsp;&nbsp;')
			}
		});

		workDayModal.modal("hide");
	});
	
	$("#freeDay_check").on('ifChecked', function(event){
		$("#freeDay_check_mesage").html("")
	}).on('ifUnchecked', function(event){
		$("#freeDay_check_mesage").html("&nbsp;&nbsp;&nbsp;不遵循法定节假日将按照工作日进行考勤计算");
	});

	$("#openFlexible").on('ifChecked', function(event){
		$("#advanceSettingModal").find("#shunyan").iCheck('check')

	}).on('ifUnchecked', function(event){
		$("#advanceSettingModal").find("#shunyan").iCheck('uncheck');
		$("#advanceSettingModal").find("#bushunyan").iCheck('uncheck');
	});
	
	$("#advanceSetting").unbind().bind("click", function(){
		var addModal = $("#modal");
		var startTime = addModal.find("#start_time").val();
		var endTime = addModal.find("#end_time").val();
		if (startTime == null || startTime == "" || startTime == undefined){
			Ewin.alert("请选择工作开始时间。");
			return false;
		}
		if (endTime == null || endTime == "" || endTime == undefined){
			Ewin.alert("请选择工作结束时间。");
			return false;
		}

		var advanceSettingModal = $("#advanceSettingModal");

		advanceSettingModal.modal("show");
	});

	$("#people_search").unbind().bind("input propertychange", function(){
		var rid = $("#rule_id").val();
		var search = $("#people_search").val();
		if (flag == 0){
			renderPeople("peoplesTree", "", search);
		}
		if (flag == 1){
			renderPeople("peoplesTree_no", rid, search);
		}
		
	});
	
	$("#advanceSetting_confirm").unbind().bind("click", function(){

		var settingModal = $("#advanceSettingModal");
		var addModal = $("#modal");
		var startTime = addModal.find("#start_time").val();
		var endTime = addModal.find("#end_time").val();

		if (startTime == null || startTime == "" || startTime == undefined){
			Ewin.alert("请选择工作开始时间。");
			settingModal.modal("hide");
			return false;
		}
		if (endTime == null || endTime == "" || endTime == undefined){
			Ewin.alert("请选择工作结束时间。");
			settingModal.modal("hide");
			return false;
		}

		var workOnBeforeHours = settingModal.find("#work_on_before_hours").val();
		var workOffAfterHours = settingModal.find("#work_off_after_hours").val();

		var startHours = startTime.split(":")[0];
		var startMinutes = startTime.split(":")[1];
		if (parseInt(startHours) - parseInt(workOnBeforeHours) < 0){
			Ewin.alert("最早签到时间不能大于开始上班时间。");
			return false;
		}
		var hour = parseInt(startHours) - parseInt(workOnBeforeHours);
		var newStartTime = hour < 10 ? "0" + hour + ":" + startMinutes : hour + ":" + startMinutes;

		var endHours = endTime.split(":")[0];
		var endMinutes = endTime.split(":")[1];
		if (parseInt(endHours) + parseInt(workOffAfterHours) >= 24){
			endHours = parseInt(endHours) + parseInt(workOffAfterHours) - 24;
			var newEndTime = parseInt(endHours) < 10 ? "0" + parseInt(endHours) + ":" + endMinutes : parseInt(endHours) + ":" + endMinutes;
			if (newStartTime <= newEndTime){
				Ewin.alert("最早签到时间和最晚下班时间不能有重叠。");
				return false;
			}
		} 

		var isOpenFlexible = settingModal.find("#openFlexible").prop("checked");
		var flexibleTime = null;
		var flexibleFlag = 0;
		if (isOpenFlexible){
			flexibleTime = settingModal.find("#flexibleTime").val();
			flexibleFlag = settingModal.find("input[type='radio']:checked").val();
		}

		$("#work_before").text(" " + workOnBeforeHours + " ");
		$("#work_after").text(" " + workOffAfterHours + " ");

		settingModal.modal("hide");
	});

	//设置工作日modal的渲染
	function renderWorkDay(startTime, endTime) {
		$(workDay).each(function (index, item) {
			$('.work-day-list').append('' +
					'<tr>' +
					'<td><input type="checkbox" ' + (item.isWork ? 'checked="checked"' : '') + ' class="isWork" value="' + item.name + '"></td>' +
					'<td id="workday_name">' + item.name + '</td>' +
					'<td class="work-time">' + (item.isWork ? startTime + "-" + endTime : '休息') + '</td>' +
					'<td class="set-work-time"><a href="javascript:;">设置</a></td>' +
					'</tr>' +
			'');

		});

		//设置工作日checkbox初始化，状态改变的事件绑定
		$('.work-day-list input').iCheck({
			checkboxClass: 'icheckbox_square-blue',
			radioClass: 'iradio_square-blue',
			increaseArea: '20%'
		}).on('ifChecked', function(event){
			var currentTr=$(event.currentTarget).parent().parent().parent();
			workDay[$(currentTr).index()].isWork=true;
			currentTr.find('.work-time').html(startTime + "-" + endTime);

		}).on('ifUnchecked', function(event){
			var currentTr=$(event.currentTarget).parent().parent().parent();
			workDay[$(currentTr).index()].isWork = false;
			currentTr.find('.work-time').html('休息');
			currentTr.attr('edit','false');
		});

		$('.set-work-time').bind('click', function(event){
			var currentTr = $(this).parent();
			//当前行是不是选中状态，不是选中状态不能设置
			if(!currentTr.find('input[type="checkbox"]').prop("checked")){
				Ewin.alert("当前是休息日，不能设置时间。");
				return false;
			}
			var edit = currentTr.attr('edit');
			if(edit === 'true'){
				currentTr.attr('edit','false');
				var currentRow = currentTr.find('.work-time');
				var currentWorkTimeStart = currentRow.find('#workTimeStart').val();
				var currentWorkTimeEnd = currentRow.find('#workTimeEnd').val();
				currentRow.html(currentWorkTimeStart + "-" + currentWorkTimeEnd);
				return;
			}

			//改变其他正在编辑行的状态
			var editRow = currentTr.parent().find('tr[edit="true"]');
			editRow.attr('edit','false');
			var editWorkTimeStart = editRow.find('.work-time').find('#workTimeStart').val();
			var editWorkTimeEnd = editRow.find('.work-time').find('#workTimeEnd').val();
			editRow.find('.work-time').html(editWorkTimeStart + "-" + editWorkTimeEnd);

			//设置行属性edit=true 表示正在编辑
			currentTr.attr('edit','true')
			var workTime = currentTr.find('.work-time');
			var workTimeHtml = workTime.html();
			var workTimes = workTimeHtml.split('-');
			var workTimeStart = workTimes[0];
			var workTimeEnd = workTimes[1];
			var workTimeDom = ''+
			'<div class="input-time">'+
			'<input type="text" class="form-control input-addon time-picker" id="workTimeStart" value="'+workTimeStart+'" style="width:120px;">'+
			'<span class="glyphicon glyphicon-time"></span>'+
			'</div>'+
			'&nbsp;&nbsp;一&nbsp;&nbsp;'+
			'<div class="input-time">'+
			'<input type="text" class="form-control input-addon time-picker" id="workTimeEnd" value="'+workTimeEnd+'" style="width:120px;">'+
			'<span class="glyphicon glyphicon-time"></span>'+
			'</div>'
			;
			workTime.html(workTimeDom);
			$('.time-picker').datetimepicker({
				language: 'zh-CN',
				format: 'hh:ii',
				startView: 1,
				autoclose: true,
				minView: 0
			})

		}) 
	};
	
	$("#attendance_location").unbind().bind("click", function(){
		var table = document.getElementById("table_attendanceLocation");
		if (table.rows.length >= 3){
    		Ewin.alert("考勤地点最多只能添加3个。");
    		return false;
    	}
		clearMap();
		$("#location_type").val("-1");
		$('#attendanceLocationModal').modal("show");
	});
	
	$("#attendanceLocation_confirm").unbind().bind("click", function(){
		
	    var table = document.getElementById("table_attendanceLocation");
	    var type = $("#location_type").val();
	    var location = $("#gaodeMapIframe").contents().find("#attendance_addr").val();
	    var longit_latit = $("#gaodeMapIframe").contents().find("#hidden_addr").val();
	    if (type == "-1"){
	    	if (table.rows.length >= 3){
	    		Ewin.alert("考勤地点最多只能添加3个。");
	    		return false;
	    	}
	    	for (var i = 0; i < table.rows.length; i++){
	    		if (location == table.rows[i].cells[0].innerHTML){
	    			Ewin.alert("您已经添加了 '" + location + "' ,请 不要重复添加。");
		    		return false;
	    		}
	    	}
	    	//添加行
	    	var tr = table.insertRow(table.rows.length);
	    	var rowIndex = tr.rowIndex.toString();
	    	tr.innerHTML = "<td>" + location + "</td>" + 
	    	"<td style='display:none'>" + longit_latit + "</td>" + 
	    	"<td>" + 
	    	'<a href="javascript:;" onclick="updateLocation(' + rowIndex + ',\'attendanceLocationModal\')">修改</a>' +
	    	'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLocation(' + rowIndex + ',\'attendanceLocationModal\',\'table_attendanceLocation\')">删除</a>' + 
	    	"</td>"; 
	    } else {
	    	var tr = table.rows[type];
			tr.innerHTML = "<td>" + location + "</td>" + 
			"<td style='display:none'>" + longit_latit + "</td>" + 
			"<td>" + 
			'<a href="javascript:;" onclick="updateLocation('  + tr.rowIndex + ',\'attendanceLocationModal\')">修改</a>' +
			'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLocation(' + tr.rowIndex + ',\'attendanceLocationModal\',\'table_attendanceLocation\')">删除</a>' + 
			"</td>"; 
	    }
	    
		$('#attendanceLocationModal').modal("hide");

	});
	
	$("#attendance_people_").unbind().bind("click", function(){
		
		var rid = $("#rule_id").val();
		if (flag == 0){
//			renderPeople();
		} else if (flag == 1){
//			renderPeople(rid);
			renderSelected("peoplesTree");
		}
		$("#people_search").val("");
		$('#attendancePeopleModal').modal("show");
	});
	
	$("#attendance_no_people_").unbind().bind("click", function(){
		
		var rid = $("#rule_id").val();
		if (flag == 0){
//			renderPeople();
		} else if (flag == 1){
//			renderPeople(rid);
			renderSelected("peoplesTree_no");
		}
		
		$("#people_search_").unbind().bind("input propertychange", function(){
			var rid = $("#rule_id").val();
			var search = $("#people_search_").val();
			if (flag == 0){
				renderPeople("peoplesTree", "", search);
			}
			if (flag == 1){
				renderPeople("peoplesTree_no", rid, search);
			}
			
		});
		
		$("#people_search_").val("");
		$('#attendancePeopleModal_no').modal("show");
	});

	$("#attendancePeople_confirm").unbind().bind("click", function(){
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
	    var selectedPeoples = zTreeObj.getCheckedNodes(true);
	    var count = 0;
	    $(selectedPeoples).each(function(index,item){
	        if (item.isParent) {
	        	return;
	        }
	        count = count + 1;
	    })
	    if (count <= 0){
	    	Ewin.alert("请选择考勤人员。");
	    	return false;
	    }
		$('#attendance_people_').val(count + '人');
		$('#attendancePeopleModal').modal("hide");

	});
	
	$("#attendancePeople_no_confirm").unbind().bind("click", function(){
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree_no");
	    var selectedPeoples = zTreeObj.getCheckedNodes(true);
	    var count = 0;
	    $(selectedPeoples).each(function(index,item){
	        if (item.isParent) {
	        	return;
	        }
	        count = count + 1;
	    })
	    if (count <= 0){
	    	Ewin.alert("请选择考勤人员。");
	    	return false;
	    }
		$('#attendance_no_people_').val(count + '人');
		$('#attendancePeopleModal_no').modal("hide");

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
    if (treeId == "peoplesTree"){
        $("#attendancePeopleModal").find('.selected-people-table').html(selectedPeoplesDom);
    } else if(treeId == "peoplesTree_no"){
    	 $("#attendancePeopleModal_no").find('.selected-people-table').html(selectedPeoplesDom);
    }
}


//选择部门与人员modal 所有员工渲染
function renderPeople(treeId, rid, search) {
	if (rid == undefined){
		rid = "";
	}
	if (search == undefined){
		search = "";
	}
	var type = 0;
	//0：参与考勤规则  1：不参与考勤规则
	if (treeId == "peoplesTree"){
		type = 0;
	} else if (treeId == "peoplesTree_no"){
		type = 1;
	} 
	
	$.ajax({
		url : "getDeptsAndUsers",
		type: "get",
		data : {
			rid : rid,
			search : search,
			type : type
		},
		async : true, 
		dataType : "json",
		success : function(data, status) {
			if (treeId == "peoplesTree"){
				$.fn.zTree.init($("#peoplesTree"), setting, data);
			} else if (treeId == "peoplesTree_no"){
				$.fn.zTree.init($("#peoplesTree_no"), setting, data);
			} else {
				$.fn.zTree.init($("#peoplesTree"), setting, data);
				$.fn.zTree.init($("#peoplesTree_no"), setting, data);
			}
		},
		error : function(data, status, e) {
			Ewin.alert("系统内部错误！");
		}
	});
	
}

function clearMap(){
	$("#gaodeMapIframe").contents().find("#attendance_addr").val("");
	$("#gaodeMapIframe").contents().find("#hidden_addr").val("");
	document.gaodeMapIframeName.map.clearMap();
}

function updateLocation(trid, modalId){
	clearMap();
	if (modalId == "attendanceLocationModal"){
		$("#location_type").val(trid);
	} else {
		$("#location_type_").val(trid);
	}
	$("#" + modalId).modal("show");
}

function deleteLocation(trid, modalId, tableId){
	var table = document.getElementById(tableId);
	table.deleteRow(trid);  //从table中删除
    for(i = trid; i < table.rows.length; i++){
    	table.rows[i].innerHTML = "<td>" + table.rows[i].cells[0].innerHTML + "</td>" + 
		"<td style='display:none'>" + table.rows[i].cells[1].innerHTML + "</td>" + 
		"<td>" + 
		'<a href="javascript:;" onclick="updateLocation(' + i + ',\'' +  modalId + '\')">修改</a>' +
		'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLocation(' +  i + ',\'' +  modalId + '\',\'' + tableId + '\')">删除</a>' + 
		"</td>"; 
    }
}

function holidayFormatter(value, row, index) {
	if (value == 0 || row.holidayFlag == 0){
		return "是";
	}
	return "否";
}

function flexibleFormatter(value, row, index) {
	if (value == 0 || row.flexibleFlag == 0){
		return "是";
	}
	return "否";
}

function postponeFormatter(value, row, index) {
	if (value == 0 || row.postponeFlag == 0){
		return "是";
	}
	return "否";
}

function operateFormatter(value, row, index) {
	return [
	        '<a class="edit"  href="javascript:void(0)" title="修改">修改</a>'
	        ].join('');
}

window.operateEvents = {
		'click .edit': function (e, value, row, index) {
			flag = 1;
			var editModal = $("#modal");
			editModal.find(".modal-title").text("修改考勤规则");
			editModal.find("#form")[0].reset();
			editModal.find('.work-day').empty();
			editModal.find("#table_attendanceLocation").empty();
			
			editModal.find("#rule_id").val(row.rid);
			editModal.find("#rule_name").val(row.ruleName);
			editModal.find("#start_time").val(row.clockOnTime);
			editModal.find("#end_time").val(row.clockOffTime);
			if (row.restFlag == 0){
				editModal.find("#input-rest").iCheck('check');
			} else {
				editModal.find("#input-rest").iCheck('uncheck');
			}
			editModal.find("#rest_start_time").val(row.restStartTime);
			editModal.find("#rest_end_time").val(row.restEndTime);
			
			var workday = row.workday.split(",");
			$(workday).each(function (index, item) {
				editModal.find('.work-day').append(item + '&nbsp;&nbsp;&nbsp;')
			});
			
			if (row.holidayFlag == 0){
				editModal.find("#freeDay_check").iCheck('check');
			} else {
				editModal.find("#freeDay_check").iCheck('uncheck');
				editModal.find("#freeDay_check_mesage").html("&nbsp;&nbsp;&nbsp;不遵循法定节假日将按照工作日进行考勤计算");
			}
			editModal.find("#freeDay_check").on('ifChecked', function(event){
				editModal.find("#freeDay_check_mesage").html("")
			}).on('ifUnchecked', function(event){
				editModal.find("#freeDay_check_mesage").html("&nbsp;&nbsp;&nbsp;不遵循法定节假日将按照工作日进行考勤计算");
			});
			
			editModal.find("#work_before").html(" " + row.clockOnAdvanceHours + " ");
			editModal.find("#work_after").html(" " + row.clockOffDelayHours + " ");

			var edit_setting_modal = $("#advanceSettingModal");
			edit_setting_modal.find("#work_on_before_hours").val(row.clockOnAdvanceHours);
			edit_setting_modal.find("#work_off_after_hours").val(row.clockOffDelayHours);
			
			edit_setting_modal.find("#openFlexible").on('ifChecked', function(event){
				edit_setting_modal.find("#shunyan").iCheck('check')

			}).on('ifUnchecked', function(event){
				edit_setting_modal.find("#shunyan").iCheck('uncheck');
				edit_setting_modal.find("#bushunyan").iCheck('uncheck');
			});
			
			if (row.flexibleFlag == 0){
				edit_setting_modal.find("#openFlexible").iCheck('check');
				edit_setting_modal.find("#flexibleTime").val(row.flexibleTime);
				if (row.postponeFlag == 0){
					edit_setting_modal.find("#shunyan").iCheck('check');
				} else {
					edit_setting_modal.find("#bushunyan").iCheck('check');
				}
			} else {
				edit_setting_modal.find("#openFlexible").iCheck('uncheck');
				edit_setting_modal.find("#shunyan").iCheck('uncheck');
				edit_setting_modal.find("#shunyan").iCheck('uncheck');
			}
			
			editModal.find("#bounds").val(row.clockBounds);
			
			var table = document.getElementById("table_attendanceLocation");
			var locations = row.clockLocation.split(",");
			var longit_latits = row.longit_latit.split("|");
			for (var i in  locations){
				var tr = table.insertRow(table.rows.length);
				var rowIndex = tr.rowIndex.toString();
				tr.innerHTML = "<td>" + locations[i] + "</td>" + 
				"<td style='display:none'>" + longit_latits[i] + "</td>" + 
				"<td>" + 
				'<a href="javascript:;" onclick="updateLocation(' + rowIndex + ',\'attendanceLocationModal\')">修改</a>' +
				'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLocation(' + rowIndex + ',\'attendanceLocationModal\',\'table_attendanceLocation\')">删除</a>' + 
				"</td>"; 
			}
		    
		    editModal.find('#attendance_people_').val(row.ruleCount + '人');
		    editModal.find('#attendance_no_people_').val(row.noRuleCount + '人');
		    renderPeople("peoplesTree", row.rid);
		    renderPeople("peoplesTree_no", row.rid);
		    
			editModal.modal("show");
		}


};