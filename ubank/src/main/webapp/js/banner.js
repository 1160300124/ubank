$(function(){
	
	$("#btn_refresh").unbind().bind("click", function(){
		$("#tb_banners").bootstrapTable("refresh");
	});
	
	$("#btn_add").unbind().bind("click", function(){
		
		var addModal = $("#add_modal");
		addModal.find("#add_form")[0].reset();
		addModal.find("#lefile").val("");
		addModal.find("#img_path").attr("src", "");
		addModal.find("#banner_module").empty();
		
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
					var select = $("#banner_module");
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
		var arrselections = $("#tb_banners").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		var data = [];
		$.each(arrselections, function(index, item){
			data.push(item);
		});
		
		Ewin.confirm({ message: "确定要删除吗？" }).on(function (e) {
			
			if (!e) {
				return;
			}
			
			$.ajax({
				url : "deleteBanners",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_banners").bootstrapTable("refresh");
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
	
	$("#btn_add_confirm").unbind().bind("click", function(){
		
		var addModal = $("#add_modal");
		var params = {};
		var module ={};
		params.bannerName = addModal.find("#banner_name").val();
		params.url = addModal.find("#banner_addr").val();
		params.picPath = addModal.find("#pic_path").val();
		module.mid = addModal.find("#banner_module").val();
		params.module = module;
		params.orderby = addModal.find("#banner_sort").val();
		params.remark = addModal.find("#remark").val();
		
		if (params.bannerName == "" || params.bannerName == null) {
			Ewin.alert("Banner名称不能为空。");
			return;
		}
		if (params.picPath == "" || params.picPath == null) {
			Ewin.alert("请上传图片。");
			return;
		}
		if (params.orderby == "" || params.orderby == null) {
			Ewin.alert("排序不能为空。");
			return;
		}
		
		$("#btn_add_confirm").attr("disabled", true);
		
		$.ajax({
			url : "saveBanner",
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
					$("#tb_banners").bootstrapTable("refresh");
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
	
	$("#btn_edit_confirm").unbind().bind("click", function(){
		
		var editModal = $("#edit_modal");
		var params = {};
		var module ={};
		params.bid = editModal.find("#banner_id").val();
		params.bannerName = editModal.find("#banner_name").val();
		params.url = editModal.find("#banner_addr").val();
		params.picPath = editModal.find("#pic_path").val();
		module.mid = editModal.find("#banner_module").val();
		params.module = module;
		params.orderby = editModal.find("#banner_sort").val();
		params.remark = editModal.find("#remark").val();
		
		if (params.bannerName == "" || params.bannerName == null) {
			Ewin.alert("Banner名称不能为空。");
			return;
		}
		if (params.picPath == "" || params.picPath == null) {
			Ewin.alert("请上传图片。");
			return;
		}
		if (params.orderby == "" || params.orderby == null) {
			Ewin.alert("排序不能为空。");
			return;
		}
		
		$("#btn_edit_confirm").attr("disabled", true);
		
		$.ajax({
			url : "editBanner",
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
					$("#tb_banners").bootstrapTable("refresh");
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
			editModal.find("#banner_module").empty();
			
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
						var select = editModal.find("#banner_module");
						for (var i in modules){
							var option = "<option value='" + modules[i].mid + "'>" + modules[i].moduleName + "</option>";
							select.append(option);
						}
						select.find("option[value='" + row.module.mid + "']").attr("selected", true);
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("系统内部错误！");
				}
			});
			
			editModal.find("#banner_id").val(row.bid);
			editModal.find("#banner_name").val(row.bannerName);
			editModal.find("#banner_addr").val(row.url);
			editModal.find("#img_path").attr("src", row.picPath);
			editModal.find("#pic_path").val(row.picPath);
			editModal.find("#banner_sort").val(row.orderby);
			editModal.find("#remark").val(row.remark);
			editModal.modal("show");
			
		}

};

function addFileChange(){
	var file = $('#lefile').val();
	if (file == null || file == "" || file == undefined){
		Ewin.alert("请上传图标！");
		return false;
	}
	$("#btn_add_pic_upload").attr("disabled", true);
	
	$.ajaxFileUpload({
		url : "uploadBannerPic",
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
	
	$("#btn_add_pic_upload").attr("disabled", false);
	return false;
}

function editFileChange(){
	var file = $('#efile').val();
	if (file == null || file == "" || file == undefined){
		Ewin.alert("请上传图标！");
		return false;
	}
	
	$("#btn_edit_pic_upload").attr("disabled", true);
	var editModal = $("#edit_modal");

	$.ajaxFileUpload({
		url : "uploadBannerPic",
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
} 