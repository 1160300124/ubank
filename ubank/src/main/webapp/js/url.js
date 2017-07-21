$(function(){
	
	$("#btn_add").unbind().bind("click", function(){
		
		var addModal = $("#add_modal");
		addModal.find("#add_form")[0].reset();
		addModal.find("#url_page").empty();
		addModal.find("#url_module").empty();
		
		$.ajax({
			url : "getAllpages",
			type: "get",
			data : {},
			async : true, 
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					var pages = data['data'];
					var select = $("#url_page");
					for (var i in pages){
						var option = "<option value='" + pages[i].pid + "'>" + pages[i].pageName + "</option>";
//						var option = new Option();
//						option.value = pages[i].pid;
//						option.text = pages[i].pageName;
						select.append(option);
					}
				}else{
					Ewin.alert(data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
		$.ajax({
			url : "getAllModules",
			type: "get",
			data : {},
			async : true, 
			dataType : "json",
			success : function(data, status) {
				var code = data['code'];
				if (code == 1000) {
					var modules = data['data'];
					var select = $("#url_module");
					for (var i in modules){
						var option = "<option value='" + modules[i].mid + "'>" + modules[i].moduleName + "</option>";
//						var option = new Option();
//						option.value = modules[i].mid;
//						option.text = modules[i].moduleName;
						select.append(option);
					}
				}else{
					Ewin.alert(data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部错误！");
			}
		});
		
		addModal.modal("show");

	});
	
	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_thirdUrls").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		var data = [];
		$.each(arrselections, function(index, item){
			data.push(item.uid);
		});
		
		Ewin.confirm({ message: "确定要删除吗？" }).on(function (e) {
			
			if (!e) {
				return;
			}
			
			$.ajax({
				url : "deleteUrl",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_thirdUrls").bootstrapTable("refresh");
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
	
	$("#btn_add_pic_upload").unbind().bind("click", function(){

		$("#btn_add_pic_upload").attr("disabled", true);
		
		$.ajaxFileUpload({
			url : "uploadUrlPic",
			type: "post",
			secureuri : false,
			fileElementId : "lefile",
			dataType : "json",
			data : {
				name : $('#lefile').val()
			},
			success : function(data, status) {
				$("#btn_add_pic_upload").attr("disabled", false);
				data = $.parseJSON(data.replace(/<.*?>/ig,""));
				var code = data['code'];
				if (code == 1000) {
					$("#img_path").attr("src", basePath + data['data']);
					$("#pic_path").val(data['data']);
				}else{
					Ewin.alert("上传失败！" + data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("上传发生异常");
			}
		})
		
		$("#btn_add_pic_upload").attr("disabled", true);

		return false;
	});
	
	$("#btn_add_confirm").unbind().bind("click", function(){
		
		$("#btn_add_confirm").attr("disabled", true);
		
		var addModal = $("#add_modal");
		var params = {};
		var page ={};
		var module ={};
		params.urlName = addModal.find("#url_name").val();
		params.url = addModal.find("#url_addr").val();
		params.picPath = addModal.find("#pic_path").val();
		page.pid = addModal.find("#url_page").val();
		params.page = page
		module.mid = addModal.find("#url_module").val();
		params.module = module;
		params.orderby = addModal.find("#url_sort").val();
		params.remark = addModal.find("#remark").val();
		
		$.ajax({
			url : "saveUrl",
			type: "post",
			data : JSON.stringify(params),
			contentType : 'application/json;charset=utf-8',
			async : true, 
			dataType : "json",
			success : function(data, status) {
				$("#btn_add_confirm").attr("disabled", false);
				var code = data['code'];
				if (code == 1000) {
					addModal.modal("hide");
					$("#tb_thirdUrls").bootstrapTable("refresh");
					Ewin.alert("新增成功！");
				}else{
					Ewin.alert(data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部异常！");
			}
		})
		
		$("#btn_add_confirm").attr("disabled", false);

	});
	
	$("#btn_edit_pic_upload").unbind().bind("click", function(){
		
		$("#btn_edit_pic_upload").attr("disabled", true);
		var editModal = $("#edit_modal");

		$.ajaxFileUpload({
			url : "uploadUrlPic",
			type: "post",
			secureuri : false,
			fileElementId : "efile",
			dataType : "json",
			data : {
				name : $('#efile').val()
			},
			success : function(data, status) {
				$("#btn_edit_pic_upload").attr("disabled", false);
				data = $.parseJSON(data.replace(/<.*?>/ig,""));
				var code = data['code'];
				if (code == 1000) {
					editModal.find("#img_path").attr("src", basePath +data['data']);
					editModal.find("#pic_path").val(data['data']);
				}else{
					Ewin.alert("上传失败！" + data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("上传发生异常");
			}
		})

		$("#btn_edit_pic_upload").attr("disabled", false);
		return false;
	});
	
	$("#btn_edit_confirm").unbind().bind("click", function(){
		
		$("#btn_edit_confirm").attr("disabled", true);
		
		var editModal = $("#edit_modal");
		var params = {};
		var page ={};
		var module ={};
		params.uid = editModal.find("#url_id").val();
		params.urlName = editModal.find("#url_name").val();
		params.url = editModal.find("#url_addr").val();
		params.picPath = editModal.find("#pic_path").val();
		page.pid = editModal.find("#url_page").val();
		params.page = page
		module.mid = editModal.find("#url_module").val();
		params.module = module;
		params.orderby = editModal.find("#url_sort").val();
		params.remark = editModal.find("#remark").val();
		
		$.ajax({
			url : "editUrl",
			type: "post",
			data : JSON.stringify(params),
			contentType : 'application/json;charset=utf-8',
			async : true, 
			dataType : "json",
			success : function(data, status) {
				$("#btn_edit_confirm").attr("disabled", false);
				var code = data['code'];
				if (code == 1000) {
					editModal.modal("hide");
					$("#tb_thirdUrls").bootstrapTable("refresh");
					Ewin.alert("修改成功！");
				}else{
					Ewin.alert(data['message']);
				}
			},
			error : function(data, status, e) {
				Ewin.alert("系统内部异常！");
			}
		})
		
		$("#btn_edit_confirm").attr("disabled", false);

	});
	
});

function operateFormatter(value, row, index) {
	return [
	        '<a class="edit"  href="javascript:void(0)" title="编辑">编辑</a>'
	        ].join('');
}


window.operateEvents = {
		'click .edit': function (e, value, row, index) {
			
			var editModal = $("#edit_modal");
			editModal.find("#edit_form")[0].reset();
			editModal.find("#url_page").empty();
			editModal.find("#url_module").empty();
			
			$.ajax({
				url : "getAllpages",
				type: "get",
				data : {},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						var pages = data['data'];
						var select = editModal.find("#url_page");
						for (var i in pages){
							var option = "<option value='" + pages[i].pid + "'>" + pages[i].pageName + "</option>";
							select.append(option);
						}
						editModal.find("#url_page").find("option[value='" + row.page.pid + "']").attr("selected", true);
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
			
			$.ajax({
				url : "getAllModules",
				type: "get",
				data : {},
				async : true, 
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						var modules = data['data'];
						var select = editModal.find("#url_module");
						for (var i in modules){
							var option = "<option value='" + modules[i].mid + "'>" + modules[i].moduleName + "</option>";
							select.append(option);
						}
						editModal.find("#url_module").find("option[value='" + row.module.mid + "']").attr("selected", true);
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
			
			editModal.find("#url_id").val(row.uid);
			editModal.find("#url_name").val(row.urlName);
			editModal.find("#url_addr").val(row.url);
			editModal.find("#img_path").attr("src", basePath + row.picPath);
			editModal.find("#pic_path").val(row.picPath);
			editModal.find("#url_sort").val(row.orderby);
			editModal.find("#remark").val(row.remark);
			editModal.modal("show");
			
		}

};