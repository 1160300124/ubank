//0 新增公告  1 修改公告
var flag = 0;
$(function(){
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

	$('div.summernote').each(function() {
		var $this = $(this);
		var placeholder = $this.attr("placeholder") || '';
		var url = $this.attr("action") || '';
		$this.summernote({
			lang : 'zh-CN',
			placeholder : placeholder,
			minHeight : 300,
			dialogsFade : true,// Add fade effect on dialogs
			dialogsInBody : true,// Dialogs can be placed in body, not in
			//summernote.
			disableDragAndDrop : false,// default false You can disable drag
			//and drop
			callbacks : {
				onImageUpload : function(files) {
					var $files = $(files);
					$files.each(function() {
						var file = this;
						var data = new FormData();
						data.append("file", file);
						$.ajax({
							data : data,
							type : "POST",
							url : "uploadPicOfAnnounce",
							cache : false,
							contentType : false,
							processData : false,
							dataType : "json",
							success : function(data, status) {
								var code = data['code'];
								if (code == 1000) {
									var imageUrl = data['data'];
									$this.summernote('insertImage', imageUrl, function($image) {

									});
								}else{
									Ewin.alert(data['message']);
								}
							},
							error :  function(data, status, e) {
								Ewin.alert("系统内部异常");
							}
						});
					});
				}
			}
		});
	});
	
	
	$("#btn_to_send").unbind().bind("click", function(){
		window.location.href = "addAnnouncement";
	});

	$("#btn_send").unbind().bind("click", function(){
		var companyId = $("#company_select").val();
		var companyName = $("#company_select").find("option:selected").text();
		if (companyId == "" || companyId == null || companyId == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		var title = $("#title").val();
		if (title == "" || title == null || title == undefined || title.length > 40){
			Ewin.alert("公告标题不能为空且长度不大于40。");
			return false;
		}
		var body = $('.summernote').summernote('code');
		if (body == "<p><br></p>"){
			Ewin.alert("请填写公告正文");
			return false;
		}
		var people = $("#choose_people").val();
		if (people == "" || people == null || people == undefined){
			Ewin.alert("请选择要发送的人。");
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
	    
	    if (count <= 0){
	    	Ewin.alert("请选择要发送的人");
	    	return false;
	    }
	    
	    var params = {};
	    params.companyId = companyId;
	    params.companyName = companyName;
	    params.announceTitle = title;
	    params.announceBody = body;
	    params.announceCount = count;
	    params.userIds = nodes.substring(0, nodes.length - 1).split(",");
	    
	    var table = document.getElementById("table_attachment");
	    var attachments = new Array();
	    var attachment = {};
		for (var i = 0; i < table.rows.length; i++){
			attachment.attachment_name = table.rows[i].cells[0].innerHTML;
			attachment.attachment_type = table.rows[i].cells[1].innerHTML;
			attachment.attachment_size = table.rows[i].cells[2].innerHTML;
			attachment.attachment_path = table.rows[i].cells[3].innerHTML;
			attachments.push(attachment);
		}
		params.attachments = attachments;
	    
	  //0 新增工资表  1 修改工资表
	    if (flag == 0){
	    	$.ajax({
	    		url : "saveAnnouncement",
	    		type: "post",
	    		contentType : 'application/json;charset=utf-8',
	    		data : JSON.stringify(params),
	    		dataType : "json",
	    		success : function(data, status) {
	    			var code = data['code'];
	    			if (code == 1000) {
	    				window.location.href = "announcement";
	    				Ewin.alert("发送成功");
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
	
	function curDateTime(){
		var d = new Date(); 
		var year = d.getFullYear(); 
		var month = d.getMonth() + 1; 
		var date = d.getDate(); 
		var day = d.getDay(); 
		var hours = d.getHours(); 
		var minutes = d.getMinutes(); 
		var seconds = d.getSeconds(); 
		var ms = d.getMilliseconds(); 
		var curDateTime= year;
		if(month > 9)
			curDateTime = curDateTime + "-" + month;
		else
			curDateTime = curDateTime + "-0" + month;
		if(date > 9)
			curDateTime = curDateTime + "-" + date;
		else
			curDateTime = curDateTime + "-0" + date;
		if(hours > 9)
			curDateTime = curDateTime + " " + hours;
		else
			curDateTime = curDateTime + " 0" + hours;
		if(minutes > 9)
			curDateTime = curDateTime + ":" + minutes;
		else
			curDateTime = curDateTime + ":0" + minutes;
		if(seconds > 9)
			curDateTime = curDateTime + ":" + seconds;
		else
			curDateTime = curDateTime + ":0" + seconds;
		return curDateTime; 
	}
	
	$("#btn_preview").unbind().bind("click", function(){
		var companyId = $("#company_select").val();
		var companyName = $("#company_select").find("option:selected").text();
		if (companyId == "" || companyId == null || companyId == undefined){
			Ewin.alert("请先选择公司");
			return false;
		}
		var title = $("#title").val();
		var content = $('.summernote').summernote('code');
		if (title == "" || title == null || title == undefined){
			Ewin.alert("请填写公告标题");
			return false;
		}
		if (content == "<p><br></p>"){
			Ewin.alert("请填写公告正文");
			return false;
		}
		$("#previewModal").find(".title").html(title);
		$("#previewModal").find(".time").html(companyName + "&nbsp;&nbsp;&nbsp;" + curDateTime());
		$("#previewModal").find(".content").html(content);
		$("#table_preview_attachment").empty();
		var table = document.getElementById("table_attachment");
		var previewtable = document.getElementById("table_preview_attachment");
		for (var i = 0; i < table.rows.length; i++){
			var tr = previewtable.insertRow(previewtable.rows.length);
			tr.innerHTML = "<td><div class='file-icon'> <img src='../images/pdf.png'></div></td>" + 
			"<td><div class='file-info'>" + table.rows[i].cells[0].innerHTML + "</div></td>";
		}
		
		$("#previewModal").modal("show");
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
	    
	    $('#choose_id').val(nodes.substring(0, nodes.length - 1));
//		$('#choose_people').val(names.substring(0, names.length - 1))
	    $('#choose_people').val(count + "人");
		$('#peopleModal').modal("hide");

	});
	
    //选择所有员工事件
	$("#checkAll").unbind().bind("change", function(event){
		var checkAll = event.currentTarget;
		var zTreeObj = $.fn.zTree.getZTreeObj("peoplesTree");
		zTreeObj.checkAllNodes(checkAll.checked);
        renderSelected("peoplesTree");
	});
	
})	

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

function addFileChange(){
	var file = $('#lefile').val();
	if (file == null || file == "" || file == undefined){
		Ewin.alert("请上传图标！");
		return false;
	}
	var objFile = document.getElementById("lefile");
	var fileName = objFile.files[0].name;
	var table = document.getElementById("table_attachment");
	for (var i = 0; i < table.rows.length; i++){
		if (fileName == table.rows[i].cells[0].innerHTML){
			Ewin.alert("您已经上传了'" + fileName + "'，请不要重复上传。");
			return false;
		}
	}
	$("#btn_attachment_upload").attr("disabled", true);
	
	$.ajaxFileUpload({
		url : "uploadAttachment",
		type: "post",
		secureuri : false,
		fileElementId : "lefile",
		dataType : "json",
		data : {
			name : $('#lefile').val()
		},
		success : function(data, status) {
			$("#btn_attachment_upload").attr("disabled", false);
			data = $.parseJSON(data.replace(/<.*?>/ig,""));
			var code = data['code'];
			if (code == 1000) {
				var table = document.getElementById("table_attachment");
				var tr = table.insertRow(table.rows.length);
				var rowIndex = tr.rowIndex.toString();
				tr.innerHTML = "<td>" + data['data'].attachment_name + "</td>" + 
				"<td style='display:none'>" + data['data'].attachment_type + "</td>" + 
				"<td style='display:none'>" + data['data'].attachment_size + "</td>" + 
				"<td style='display:none'>" + data['data'].attachment_path + "</td>" + 
				"<td>" + 
				'<a href="javascript:;" style="margin-left:20px;" onclick="deleteAttachment(' + rowIndex + ',\'table_attachment\')"><span class="fa icon-remove" aria-hidden="true"></span></a>' + 
				"</td>"; 
			}else{
				Ewin.alert("上传失败！" + data['message']);
			}
		},
		error : function(data, status, e) {
			Ewin.alert("上传发生异常");
		}
	})
	
	$('#lefile').val("");
	$("#btn_attachment_upload").attr("disabled", false);
	return false;
}

function deleteAttachment(trid, tableId){
	var table = document.getElementById(tableId);
	table.deleteRow(trid);  //从table中删除
    for(i = trid; i < table.rows.length; i++){
    	table.rows[i].innerHTML = "<td>" + table.rows[i].cells[0].innerHTML + "</td>" + 
		"<td style='display:none'>" + table.rows[i].cells[1].innerHTML + "</td>" + 
		"<td style='display:none'>" + table.rows[i].cells[2].innerHTML + "</td>" + 
		"<td style='display:none'>" + table.rows[i].cells[3].innerHTML + "</td>" + 
		"<td>" + 
		'<a href="javascript:;" style="margin-left:20px;" onclick="deleteAttachment(' +  i + ',\'' + tableId + '\')"><span class="fa icon-remove" aria-hidden="true"></span></a>' + 
		"</td>"; 
    }
}

function operateFormatter(value, row, index) {
	return [
	        '<a class="view"  href="javascript:void(0)" title="查看">查看</a>&nbsp;&nbsp;&nbsp;',
	        '<a class="remove"  href="javascript:void(0)" title="撤回">撤回</a>'
	        ].join('');
}

window.operateEvents = {
		'click .view': function (e, value, row, index) {
			
			$("#previewModal").find(".title").html(row.announceTitle);
			$("#previewModal").find(".time").html(row.companyName + "&nbsp;&nbsp;&nbsp;" + row.createTime);
			$("#previewModal").find(".content").html(row.announceBody);
			$("#table_preview_attachment").empty();
			$.ajax({
				url : "getAttachments",
				type: "get",
				data : {
					aid : row.aid
				},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						var rows = data['data'];
						var previewtable = document.getElementById("table_preview_attachment");
						for (var i = 0; i < rows.length; i++){
							var tr = previewtable.insertRow(previewtable.rows.length);
							tr.innerHTML = "<td><div class='file-icon'> <img src='../images/pdf.png'></div></td>" + 
							"<td><div class='file-info'>" + 
							'<div class="file-name">' + rows[i].attachment_name + '</div>' + 
                            '<div class="file-size">'  + rows[i].attachment_size + '</div>' + 
							"</div></td>";
						}
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
			
			$("#previewModal").modal("show");
		},
		'click .remove': function (e, value, row, index) {
			
			Ewin.confirm({ message: "确定要撤回本条公告吗？" }).on(function (e) {
				if (!e) {
					return;
				}
				
				$.ajax({
					url : "deleteAnnouncement",
					type: "post",
					data : {
						aid : row.aid
					},
					async : true, 
					dataType : "json",
					success : function(data, status) {
						var code = data['code'];
						if (code == 1000) {
							Ewin.alert("撤回成功。");
							$("#tb_announcement_records").bootstrapTable("refresh");
						}else{
							Ewin.alert(data['message']);
						}
					},
					error : function(data, status, e) {
						Ewin.alert("系统内部错误！");
					}
				});
				
			});
		}
}

