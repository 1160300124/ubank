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
		var addModal = $("#add_modal");
		addModal.find("#add_form")[0].reset();
		addModal.find('.work-day').empty();
		$("#table_attendanceLocation").empty();
		$('.work-day').append('周一&nbsp;&nbsp;&nbsp;周二&nbsp;&nbsp;&nbsp;周三&nbsp;&nbsp;&nbsp;周四&nbsp;&nbsp;&nbsp;周五&nbsp;&nbsp;&nbsp;');

		var advanceSettingModal = $("#advanceSettingModal");

		addModal.find("#work_before").text(" " + 2 + " ");
		addModal.find("#work_after").text(" " + 6 + " ");
		advanceSettingModal.find('#work_on_before_hours').val("2");
		advanceSettingModal.find('#work_off_after_hours').val("6");
		advanceSettingModal.find("#openFlexible").iCheck('uncheck');
		advanceSettingModal.find("#shunyan").iCheck('uncheck');
		advanceSettingModal.find("#shunyan").iCheck('uncheck');
		

		addModal.modal("show");
	});

	$("#btn_refresh").unbind().bind("click", function(){

		$("#tb_rules").bootstrapTable("refresh");
	});


	$("#btn_add_confirm").unbind().bind("click", function(){

		var params = {};

		var addModal = $("#add_modal");
		var ruleName = addModal.find("#rule_name").val();
		var startTime = addModal.find("#start_time").val();
		var endTime = addModal.find("#end_time").val();
		if (ruleName == null || ruleName == "" || ruleName == undefined){
			Ewin.alert("请填写规则名称。");
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

		var isRestChecked = addModal.find("#input-rest").prop("checked");
		var restStartTime = addModal.find("#rest_start_time").val();
		var resteEndTime = addModal.find("#rest_end_time").val();
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

		var isWorkChecked = addModal.find("#freeDay_check").prop("checked");
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

		var workOnBeforeHours = addModal.find("#work_before").text().trim();
		var workOffAfterHours = addModal.find("#work_after").text().trim();
		params.clockOnAdvanceHours = workOnBeforeHours;
		params.clockOffDelayHours = workOffAfterHours;

		var table = document.getElementById("table_attendanceLocation");
		var clockLocation = table.rows[0].cells[0].innerHTML;
		var longit_latit = table.rows[0].cells[1].innerHTML;
		params.longit_latit = longit_latit;
		params.clockLocation = clockLocation;

		var bounds = $("#bounds").val();
		params.clockBounds = bounds;
		
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
	    var selectedPeoples = zTreeObj.getCheckedNodes(true);
	    var count = 0;
	    var result = "";
	    $(selectedPeoples).each(function(index,item){
	        if (item.isParent) {
	        	var childrenNodes = item.children;
	        	var nodes = "";
	        	if (childrenNodes) {
	                for (var i = 0; i < childrenNodes.length; i++) {
	                	if (childrenNodes[i].checked){
	                		nodes += childrenNodes[i].id + ",";
	                	}
	                }
	            }
        	result += item.id + "=" + nodes.substring(0, nodes.length - 1) + "-";
            return;
	        }
	        count = count + 1;
	    });
	    
	    if (count <= 0){
	    	Ewin.alert("请选择参与考勤人员");
	    	return false;
	    }
	    params.data = result.substring(0, result.length - 1);;
		params.counts = count;
		

		$.ajax({
			url : "addRules",
			type: "post",
			data : params,
			async : true, 
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					addModal.modal("hide");
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



	});

	
	$("#btn_edit_confirm").unbind().bind("click", function(){
		
		var params = {};

		var editModal = $("#edit_modal");
		var ruleId = editModal.find("#rule_id").val();
		var ruleName = editModal.find("#rule_name").val();
		var startTime = editModal.find("#start_time").val();
		var endTime = editModal.find("#end_time").val();
		if (ruleName == null || ruleName == "" || ruleName == undefined){
			Ewin.alert("请填写规则名称。");
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

		params.rid = ruleId;
		params.ruleName = ruleName;
		params.clockOnTime = startTime;
		params.clockOffTime = endTime;

		var isRestChecked = editModal.find("#input-rest").prop("checked");
		var restStartTime = editModal.find("#rest_start_time").val();
		var resteEndTime = editModal.find("#rest_end_time").val();
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

		var workday = editModal.find(".work-day").html().replace(/&nbsp;&nbsp;&nbsp;/g, ",");
		params.workday = workday.substring(0, workday.length-1);

		var isWorkChecked = editModal.find("#freeDay_check").prop("checked");
		params.holidayFlag = isWorkChecked ? 0 : 1;

		var settingModal = $("#advanceSettingModal_edit");
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

		var workOnBeforeHours = editModal.find("#work_before").text().trim();
		var workOffAfterHours = editModal.find("#work_after").text().trim();
		params.clockOnAdvanceHours = workOnBeforeHours;
		params.clockOffDelayHours = workOffAfterHours;

		var table = document.getElementById("table_attendanceLocation_");
		var clockLocation = table.rows[0].cells[0].innerHTML;
		var longit_latit = table.rows[0].cells[1].innerHTML;
		params.longit_latit = longit_latit;
		params.clockLocation = clockLocation;

		var bounds = editModal.find("#bounds").val();
		params.clockBounds = bounds;
		
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree_edit");
	    var selectedPeoples = zTreeObj.getCheckedNodes(true);
	    var count = 0;
	    var result = "";
	    $(selectedPeoples).each(function(index,item){
	        if (item.isParent) {
	        	var childrenNodes = item.children;
	        	var nodes = "";
	        	if (childrenNodes) {
	                for (var i = 0; i < childrenNodes.length; i++) {
	                	if (childrenNodes[i].checked){
	                		nodes += childrenNodes[i].id + ",";
	                	}
	                }
	            }
        	result += item.id + "=" + nodes.substring(0, nodes.length - 1) + "-";
            return;
	        }
	        count = count + 1;
	    });
	    
	    if (count <= 0){
	    	Ewin.alert("请选择参与考勤人员");
	    	return false;
	    }
	    params.data = result.substring(0, result.length - 1);;
		params.counts = count;
		
		$.ajax({
			url : "updateRule",
			type: "post",
			data : params,
			async : true, 
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					editModal.modal("hide");
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

	});
	
	

	$("#workDay_setting").unbind().bind("click", function(){
		var addModal = $("#add_modal");
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
	
	$("#workDay_setting_edit").unbind().bind("click", function(){
		var editModal = $("#edit_modal");
		var startTime = editModal.find("#start_time").val();
		var endTime = editModal.find("#end_time").val();
		if (startTime == null || startTime == "" || startTime == undefined){
			Ewin.alert("请选择工作开始时间。");
			return false;
		}
		if (endTime == null || endTime == "" || endTime == undefined){
			Ewin.alert("请选择工作结束时间。");
			return false;
		}

		var workDayModal = $("#workDayModal_edit");

		workDayModal.find('.work-day-list').empty();
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
	
	$("#workDayModal_edit_confirm").unbind().bind("click", function(){
		var editWorkDayModal = $("#workDayModal_edit");
		var realWorkDay = editWorkDayModal.find('.work-day-list').find('input[type="checkbox"]')
		$("#edit_modal").find('.work-day').empty();
		$(realWorkDay).each(function (index, item) {
			if (item.checked){
				$("#edit_modal").find('.work-day').append(item.value + '&nbsp;&nbsp;&nbsp;')
			}
		});

		editWorkDayModal.modal("hide");
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
		var addModal = $("#add_modal");
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

	
	$("#advanceSetting_edit").unbind().bind("click", function(){
		var editModal = $("#edit_modal");
		var startTime = editModal.find("#start_time").val();
		var endTime = editModal.find("#end_time").val();
		if (startTime == null || startTime == "" || startTime == undefined){
			Ewin.alert("请选择工作开始时间。");
			return false;
		}
		if (endTime == null || endTime == "" || endTime == undefined){
			Ewin.alert("请选择工作结束时间。");
			return false;
		}

		var advanceSettingModal = $("#advanceSettingModal_edit");

		advanceSettingModal.modal("show");
	});

	$("#advanceSetting_confirm").unbind().bind("click", function(){

		var settingModal = $("#advanceSettingModal");
		var addModal = $("#add_modal");
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
		var newStartTime = parseInt(startHours) - parseInt(workOnBeforeHours) + ":" + startMinutes;

		var endHours = endTime.split(":")[0];
		var endMinutes = endTime.split(":")[1];
		if (parseInt(endHours) + parseInt(workOffAfterHours) >= 24){
			endHours = parseInt(endHours) + parseInt(workOffAfterHours) - 24;
			var newEndTime = parseInt(endHours) + ":" + endMinutes;
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

	$("#advanceSetting_edit_confirm").unbind().bind("click", function(){

		var settingModal = $("#advanceSettingModal_edit");
		var editModal = $("#edit_modal");
		var startTime = editModal.find("#start_time").val();
		var endTime = editModal.find("#end_time").val();

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
		var newStartTime = parseInt(startHours) - parseInt(workOnBeforeHours) + ":" + startMinutes;

		var endHours = endTime.split(":")[0];
		var endMinutes = endTime.split(":")[1];
		if (parseInt(endHours) + parseInt(workOffAfterHours) >= 24){
			endHours = parseInt(endHours) + parseInt(workOffAfterHours) - 24;
			var newEndTime = parseInt(endHours) + ":" + endMinutes;
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

		editModal.find("#work_before").text(" " + workOnBeforeHours + " ");
		editModal.find("#work_after").text(" " + workOffAfterHours + " ");

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
	}
	
	
	$("#attendanceLocation_confirm").unbind().bind("click", function(){
		
//		var table = $("#table_attendanceLocation");
	    var table=document.getElementById("table_attendanceLocation");

		//添加行
	    var tr = table.insertRow(table.rows.length);
	    var rowIndex = tr.rowIndex.toString();
	    console.log(table.rows.length);
	    tr.innerHTML = "<td>" + $("#gaodeMapIframe").contents().find("#attendance_addr").val() + "</td>" + 
		"<td style='display:none'>" + $("#gaodeMapIframe").contents().find("#hidden_addr").val() + "</td>" + 
		"<td>" + 
		'<a href="javascript:;" onclick="$('+ "'#attendanceLocationModal'" + ').modal(' + "'toggle')" + '">修改</a>' +
		'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLocation(' + rowIndex + ')">删除</a>' + 
		"</td>"; 
	    
		$('#attendanceLocationModal').modal("hide")

	});
	
	$("#attendance_people_").unbind().bind("click", function(){
		
		renderPeople("add");
		$('#attendancePeopleModal').modal("show");

		
	});

	$("#attendancePeople_confirm").unbind().bind("click", function(){
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
	    var selectedPeoples = zTreeObj.getCheckedNodes(true);
	    var count = 0;
	    $(selectedPeoples).each(function(index,item){
	        if (item.parentTId === null) {
	        	return;
	        }
	        count = count + 1;
	    })
		$('#attendance_people_').val(count + '人');
		$('#attendancePeopleModal').modal("hide");

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
    } else if (treeId == "peoplesTree_edit"){
    	$("#attendancePeopleModal_edit").find('.selected-people-table').html(selectedPeoplesDom);
    }
}


//选择部门与人员modal 所有员工渲染
function renderPeople(flag) {
	
	$.ajax({
		url : "getDeptsAndUsers",
		type: "get",
		data : {},
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
	
    //renderSelected();
}

function deleteLocation(trid){
	var tab = document.getElementById("table_attendanceLocation");
//	var row = document.getElementById(trid);   
//    var index = row.rowIndex;//rowIndex属性为tr的索引值，从0开始
    tab.deleteRow(trid);  //从table中删除
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

function deleteLoc(rowIndex){
	var table = document.getElementById("table_attendanceLocation_");
	table.deleteRow(rowIndex);
}


window.operateEvents = {
		'click .edit': function (e, value, row, index) {
			
			var editModal = $("#edit_modal");
			editModal.find("#edit_form")[0].reset();
			editModal.find('.work-day').empty();
			editModal.find("#table_attendanceLocation_").empty();
			
			editModal.find("#rule_id").val(row.rid);
			editModal.find("#rule_name").val(row.ruleName);
			editModal.find("#start_time").val(row.clockOnTime);
			editModal.find("#end_time").val(row.clockOffTime);
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

			var edit_setting_modal = $("#advanceSettingModal_edit");
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
			
			var table = document.getElementById("table_attendanceLocation_");

			//添加行
		    var tr = table.insertRow(table.rows.length);
		    var rowIndex = tr.rowIndex.toString();
		    console.log(table.rows.length);
		    tr.innerHTML = "<td>" + row.clockLocation + "</td>" + 
			"<td style='display:none'>" + row.longit_latit + "</td>" + 
			"<td>" + 
			'<a href="javascript:;" onclick="$('+ "'#attendanceLocationModal'" + ').modal(' + "'toggle')" + '">修改</a>' +
			'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLoc(' + rowIndex + ')">删除</a>' + 
			"</td>"; 
		    
		    editModal.find('#attendance_people__').val(row.counts + '人');
		    
		    editModal.find("#attendance_people__").unbind().bind("click", function(){
				
				$('#attendancePeopleModal_edit').modal("show");
				
			});
		    
			$("#attendanceLocation_edit_confirm").unbind().bind("click", function(){
				
			    var table=document.getElementById("table_attendanceLocation_");

				//添加行
			    var tr = table.insertRow(table.rows.length);
			    var rowIndex = tr.rowIndex.toString();
			    console.log(table.rows.length);
			    tr.innerHTML = "<td>" + $("#gaodeMapIframe").contents().find("#attendance_addr").val() + "</td>" + 
				"<td style='display:none'>" + $("#gaodeMapIframe").contents().find("#hidden_addr").val() + "</td>" + 
				"<td>" + 
				'<a href="javascript:;" onclick="$('+ "'#attendanceLocationModal_edit'" + ').modal(' + "'toggle')" + '">修改</a>' +
				'<a href="javascript:;" style="margin-left:20px;" onclick="deleteLoc(' + rowIndex + ')">删除</a>' + 
				"</td>"; 
			    
				$('#attendanceLocationModal_edit').modal("hide")

			});
		    
		    renderPeople("edit");
		    $.ajax({
				url : "getUserIdsByRid",
				type: "get",
				data : {
					rid : row.rid
				},
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code === 1000){
						var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree_edit");
						zTreeObj.checkAllNodes(false);   //清空tree
						$(data['data']).each(function(index,item){
							zTreeObj.checkNode(zTreeObj.getNodeByParam("id", item.deptId), true);
							zTreeObj.checkNode(zTreeObj.getNodeByParam("id", item.userId), true);
						})
						renderSelected("peoplesTree_edit");
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
		    
		    //选择所有员工事件
			$("#checkAll_edit").unbind().bind("change", function(event){
				var checkAll = event.currentTarget;
				var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree_edit");
				zTreeObj.checkAllNodes(checkAll.checked);
		        renderSelected("peoplesTree_edit");
			});
			
			$("#attendancePeople_edit_confirm").unbind().bind("click", function(){
				var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree_edit");
			    var selectedPeoples = zTreeObj.getCheckedNodes(true);
			    var count = 0;
			    $(selectedPeoples).each(function(index,item){
			        if (item.parentTId === null) {
			            return;
			        }
			        count = count + 1;
			    })
				$('#attendance_people__').val(count + '人');
				$('#attendancePeopleModal_edit').modal("hide");

			});
			
			editModal.modal("show");
		}


};