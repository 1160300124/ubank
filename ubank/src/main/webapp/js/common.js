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
		};
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
		};

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

    var aCity = {
        11 : "北京",
        12 : "天津",
        13 : "河北",
        14 : "山西",
        15 : "内蒙古",
        21 : "辽宁",
        22 : "吉林",
        23 : "黑龙江",
        31 : "上海",
        32 : "江苏",
        33 : "浙江",
        34 : "安徽",
        35 : "福建",
        36 : "江西",
        37 : "山东",
        41 : "河南",
        42 : "湖北",
        43 : "湖南",
        44 : "广东",
        45 : "广西",
        46 : "海南",
        50 : "重庆",
        51 : "四川",
        52 : "贵州",
        53 : "云南",
        54 : "西藏",
        61 : "陕西",
        62 : "甘肃",
        63 : "青海",
        64 : "宁夏",
        65 : "新疆",
        71 : "台湾",
        81 : "香港",
        82 : "澳门",
        91 : "国外"
    };

	//验证
	window.Validate = {
		//判断文本框不能输入特殊字符的正则表达式  能输入英文大小写字母、数字、中文 但不能输入特殊字符
        regNumAndLetter : function (str) {
            var reg = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/;
            if(str.match(reg) == null){
                return false;
            }else{
            	return true;
			}
        },
		//验证电话
		regPhone : function (str) {
            var reg = /^1[34578]\d{9}$/; //电话
            if(str.match(reg) == null){
                return false;
            }else{
                return true;
            }
        },
		//纯数字验证
		regNumber : function (str) {
            var reg = /^[0-9]*$/;
            if(str.match(reg) == null){
                return false;
            }else{
                return true;
            }
        },
		//数字和字母验证
		NumberAndLetter : function (str) {
            var reg = /^[A-Za-z0-9]+$/; //数字和字母
            if(str.match(reg) == null){
                return false;
            }else{
                return true;
            }
        },
		//身份证验证
        isCardID : function (str) {
            var iSum = 0;
            var info = "";
            var sBirthday = "";
            if (!/^\d{17}(\d|x)$/i.test(str))
                return "你输入的身份证长度或格式错误";
            str = str.replace(/x$/i, "a");
            if (aCity[parseInt(str.substr(0, 2))] == null)
                return "你的身份证地区非法";
            sBirthday = str.substr(6, 4) + "-" + Number(str.substr(10, 2)) + "-" + Number(str.substr(12, 2));
            var d = new Date(sBirthday.replace(/-/g, "/"));
            if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate()))
                return "身份证上的出生日期非法";
            for (var i = 17; i >= 0; i--)
                iSum += (Math.pow(2, i) % 11) * parseInt(str.charAt(17 - i), 11);
            if (iSum % 11 != 1)
                return "你输入的身份证号非法";
            return true;
        },
		//中文验证
		regWord : function (str) {
            var reg = /^[\u4e00-\u9fa5]+$/; //纯中文
            if(str.match(reg) ==null){
                return false;
            }else{
                return true;
            }
        }
	}

})(jQuery);

