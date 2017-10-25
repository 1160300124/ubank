$(function(){
	
	$("#btn_refresh").unbind().bind("click", function(){
		$("#tb_modules").bootstrapTable("refresh");
	});

	$("#btn_add").unbind().bind("click", function(){

		var addModal = $("#add_modal");
		addModal.find("#add_form")[0].reset();


		addModal.modal("show");

	});

	$("#btn_add_confirm").unbind().bind("click", function(){

		var addModal = $("#add_modal");
		var params = {};
		params.moduleName = addModal.find("#module_name").val();
		params.remark = addModal.find("#remark").val();
		if (params.moduleName == "" || params.moduleName.length == 0 || params.moduleName.length > 40){
			Ewin.alert("模块名称长度40个字符以内");
			return false;
		}
		if ( params.remark.length > 200){
			Ewin.alert("备注长度200个字符以内");
			return false;
		}

		$("#btn_edit_confirm").attr("disabled", true);
		$.ajax({
			url : "saveModule",
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
					$("#tb_modules").bootstrapTable("refresh");
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

	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_modules").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}

		var data = [];
		$.each(arrselections, function(index, item){
			data.push(item.mid);
		});

		Ewin.confirm({ message: "确定要删除吗？" }).on(function (e) {

			if (!e) {
				return;
			}

			$.ajax({
				url : "deleteModules",
				type: "post",
				contentType : 'application/json;charset=utf-8',
				data : JSON.stringify(data),
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_modules").bootstrapTable("refresh");
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


	$("#btn_edit_confirm").unbind().bind("click", function(){

		var editModal = $("#edit_modal");
		var params = {};
		params.mid = editModal.find("#module_id").val();
		params.moduleName = editModal.find("#module_name").val();
		params.remark = editModal.find("#remark").val();
		if (params.moduleName == "" || params.moduleName.length == 0 || params.moduleName.length > 40){
			Ewin.alert("模块名称长度40个字符以内");
			return false;
		}
		if (params.remark.length > 200){
			Ewin.alert("备注长度200个字符以内");
			return false;
		}

		$("#btn_edit_confirm").attr("disabled", true);
		$.ajax({
			url : "editModule",
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
					$("#tb_modules").bootstrapTable("refresh");
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

			editModal.find("#module_id").val(row.mid);
			editModal.find("#module_name").val(row.moduleName);
			editModal.find("#remark").val(row.remark);
			editModal.modal("show");

		}

};