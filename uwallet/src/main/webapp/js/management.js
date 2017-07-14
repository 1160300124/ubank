$(function () {

	//1.初始化Table
	var Management = {
//			import : function(){
//			alert("xxxxxx");
//			}
	}

//	Management.prototype.import = function(){
//	alert("aaaaaaaaaa");

//	};


	$("#btn_delete").unbind().bind("click", function(){

		//取表格的选中行数据
		var arrselections = $("#tb_managetments").bootstrapTable('getSelections');
		if (arrselections.length <= 0) {
			Ewin.alert("请选择有效数据");
			return;
		}
		
		Ewin.confirm({ message: "请确认数据是否正确，确定要提交吗？" }).on(function (e) {
			
			$.ajax({
				url : "delete",
				type: "post",
				data : {},
				dataType : "json",
				success : function(data, status) {
					var code = data['code'];
					if (code == 1000) {
						Ewin.alert("删除成功");
						$("#tb_managetments").bootstrapTable("refresh");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("处理异常");
				}
			})	
			
		});
		alert(arrselections);
	});

	
	$("#btn_import").unbind().bind("click", function(){

		$("#import_modal").modal("show");
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
					$("#tb_salary").bootstrapTable("load", data["data"]);
				}else{
					alert("处理异常！");
				}
			},
			error : function(data, status, e) {
				alert("上传发生异常");
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
						alert("处理成功");
					}else{
						Ewin.alert(data['message']);
					}
				},
				error : function(data, status, e) {
					Ewin.alert("处理异常");
				}
			})
			
		});
		

		return false;

	});

//	var manager = new Management();
//	Management.init();
});

function operateFormatter(value, row, index) {
	return [
	        '<a class="detail"  href="javascript:void(0)" title="详情">详情</a>'
	        ].join('');
}


window.operateEvents = {
		'click .detail': function (e, value, row, index) {
			$("#detail_modal").modal("show");
			$("#tb_salary_detail").bootstrapTable("refresh", {
				url : "details?sid=" + row.sid
			});
		}
};


(function ($) {

    window.Ewin = function () {
        var html = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
            '<div class="modal-dialog modal-sm">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
            '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>[Message]</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
            '<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';


        var dialogdHtml = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
            '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';
        var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
        var generateId = function () {
            var date = new Date();
            return 'mdl' + date.valueOf();
        }
        var init = function (options) {
            options = $.extend({}, {
                title: "操作提示",
                message: "提示内容",
                btnok: "确定",
                btncl: "取消",
                height: 200,
                width: 500,
                auto: false
            }, options || {});
            var modalId = generateId();
            var content = html.replace(reg, function (node, key) {
                return {
                    Id: modalId,
                    Title: options.title,
                    Message: options.message,
                    BtnOk: options.btnok,
                    BtnCancel: options.btncl
                }[key];
            });
            $('body').append(content);
            $('#' + modalId).modal({
                width: options.width,
                backdrop: 'static'
            });
            $('#' + modalId).on('hide.bs.modal', function (e) {
                $('body').find('#' + modalId).remove();
            });
            return modalId;
        }

        return {
            alert: function (options) {
                if (typeof options == 'string') {
                    options = {
                        message: options
                    };
                }
                var id = init(options);
                var modal = $('#' + id);
                modal.find('.ok').removeClass('btn-success').addClass('btn-primary');
                modal.find('.cancel').hide();

                return {
                    id: id,
                    on: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.find('.ok').click(function () { callback(true); });
                        }
                    },
                    hide: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.on('hide.bs.modal', function (e) {
                                callback(e);
                            });
                        }
                    }
                };
            },
            confirm: function (options) {
                var id = init(options);
                var modal = $('#' + id);
                modal.find('.ok').removeClass('btn-primary').addClass('btn-success');
                modal.find('.cancel').show();
                return {
                    id: id,
                    on: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.find('.ok').click(function () { callback(true); });
                            modal.find('.cancel').click(function () { callback(false); });
                        }
                    },
                    hide: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.on('hide.bs.modal', function (e) {
                                callback(e);
                            });
                        }
                    }
                };
            },
            dialog: function (options) {
                options = $.extend({}, {
                    title: 'title',
                    url: '',
                    width: 800,
                    height: 550,
                    onReady: function () { },
                    onShown: function (e) { }
                }, options || {});
                var modalId = generateId();

                var content = dialogdHtml.replace(reg, function (node, key) {
                    return {
                        Id: modalId,
                        Title: options.title
                    }[key];
                });
                $('body').append(content);
                var target = $('#' + modalId);
                target.find('.modal-body').load(options.url);
                if (options.onReady())
                    options.onReady.call(target);
                target.modal();
                target.on('shown.bs.modal', function (e) {
                    if (options.onReady(e))
                        options.onReady.call(target, e);
                });
                target.on('hide.bs.modal', function (e) {
                    $('body').find(target).remove();
                });
            }
        }
    }();
})(jQuery);


