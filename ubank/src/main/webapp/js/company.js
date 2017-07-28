
$(function () {
    CompanyFun.getAllGroup();
    CompanyFun.getAllBank();
    CompanyFun.companyQuery();
});
var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
var bankInfo = ""; //全局变量，存放所有银行信息
var comNum = ""; //全局变量，存放公司编号

//all function
var CompanyFun = {
    //打开新增dialog
    openAdd : function () {
        flag = 0;
        $(".modal-title").html("新增");
        $("#company_modal").modal("show");

    },
    //获取所有集团
    getAllGroup : function () {
        $.ajax({
            url : 'getAllGroup',
            dataType : 'json',
            type : 'post',
            data:  {},
            success : function (data) {
                if(data.length <= 0){
                    Ewin.alert("没有集团数据，，请联系管理员");
                    return;
                }
                var option = "";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].groupNumber+"'>"+data[i].name+"</option>";
                }
                $("#group_select").append(option);

            },
            error : function () {
                Ewin.alert("操作异常，请联系管理员");
            }
        })
    },
    //获取所有银行信息
    getAllBank : function () {
        $.ajax({
            url : 'getAllBank',
            dataType : 'json',
            type : 'post',
            data:  {},
            success : function (data) {
                if(data.length <= 0){
                    Ewin.alert("获取银行失败，请联系管理员");
                    return;
                }
                var option = "";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].bankNo+"'>"+data[i].bankName+"</option>";
                }
                $("#company_select").append(option);
                bankInfo = data;

            },
            error : function () {
                Ewin.alert("操作异常，请联系管理员");
            }
        })
    },
    //查询
    companyQuery : function () {
        $("#company_table").bootstrapTable({
            url : 'comQuery',
            method : 'post',// get,post
            toolbar : '#company_Toolbar', // 工具栏
            striped : true, // 是否显示行间隔色
            cache : false, // 是否使用缓存，默认为true
            pagination : true, // 是否显示分页
            queryParams : this.queryParams,// 传递参数
            contentType : "application/x-www-form-urlencoded",
            sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
            search : true, //搜索框
            searchText : ' ', //初始化搜索文字
            pageNumber : 1, // 初始化加载第一页，默认第一页
            pageSize : 20, // 每页的记录行数
            pageList : [20,30,40], // 可供选择的每页的行数
            showRefresh : true, //刷新按钮
            showToggle :true,   //切换试图（table/card）按钮
            showColumns : true,
            clickToSelect : true,
            columns : [
                {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                {field : 'name', title : '公司名称', width: 130, align : 'left'},
                {field : 'account', title : '银行账户', width: 130, align : 'left',
                    formatter : function (value,row,index) {
                        var arr = [];
                        arr.push(value.split(","));
                        return ""+arr[0].length+"";
                    }
                },
                {field : 'details', title : '详情', width: 130, align : 'left'},
                {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false},
                {field : 'legalPerson', title : '公司法人', width: 130, align : 'left',visible : false},
                {field : 'group_num', title : '集团编号', width: 130, align : 'left',visible : false}


            ]
        });
    },
    //查询参数
    queryParams : function (params) {
        var paramData = {
            pageSize : params.limit,
            pageNum : params.offset,
            search : params.search
        };
        return paramData;
    },
    //新增
    companyAdd : function () {
        //获取所有账户信息
        var arr=[];
        var allBankAccount = [];
        var account = "";
        var str='';
        $('.form-box .add-form-item').each(function(){
            $(this).find('.base-request').each(function(){
                var _value=$(this).val();
                str += _value + "/";

            });
            allBankAccount.push($(this).find("input[name=accounts]").val());
            str += $("#companyName").val();
            str= str.substr(0,str.length-1);
            str+=',';
        });
        str = str.substr(0,str.length-1);
        account = allBankAccount.join(",");
        $("#allAccount").val(account);
        $.ajax({
            url : 'addCom?items= ' + str + "&flag=" + flag + "&comNum=" + comNum,
            dataType : 'json',
            type : 'post',
            data:  $("#company_form").serialize(),
            success : function (data) {
                if(data.code == 300){
                    Ewin.alert(data.message);
                }else if(data.code == 500){
                    Ewin.alert("操作异常，请联系管理员");
                }else{
                    $("#company_form")[0].reset();
                    $("#company_modal").modal("hide");
                    Ewin.alert(data.message);
                    $('#company_table').bootstrapTable('refresh');
                }

            },
            error : function () {
                Ewin.alert("操作异常，请联系管理员");
            }
        })
    },
    //打开修改框
    openEdit : function () {
        var row = $('#company_table').bootstrapTable('getSelections');
        flag = 1;
        comNum = row[0].companyNumber;
        $(".modal-title").html("修改");
        if(row.length > 1){
            Ewin.alert("不能多选，请重新选择");
            return;
        }else if(row.length <= 0){
            Ewin.alert("请选中需要修改的数据");
            return;
        }
        //根据银行账户编号获取银行账户信息
        $.ajax({
            url : 'getBankAccountByNum',
            dataType : 'json',
            type : 'post',
            data:  {
                "accountNum" : row[0].account
            },
            success : function (data) {
                $("#group_select").find("option[value="+row[0].group_num+"]").attr("selected",true);
                $("#companyName").val(row[0].name);
                $("input[name=legalPerson]").val(row[0].legalPerson);
                $("#com_area").val(row[0].details);
                //拼接元素
                var html = "";
                for (var i = 0 ; i < data.length ; i++) {
                    var option = "";
                    for (var j = 0 ; j <bankInfo.length ; j++){
                        if($.trim(bankInfo[j].bankNo) == $.trim(data[i].bankNumber)){
                            option += "<option value='"+bankInfo[j].bankNo+"' selected='selected'>"+bankInfo[j].bankName+"</option>";
                        }else{
                            option += "<option value='"+bankInfo[j].bankNo+"'>"+bankInfo[j].bankName+"</option>";
                        }

                    }
                    html += "<div class='add-form-item check-form' >" +
                        "<span class='toggle-form' data-click='toggleForm'>展开</span>" +
                        "<div class='base-right-btn'>" +
                        "<span class='fl edit-form' data-click='editForm'>编辑</span>" +
                        "<span class='fl delete-form' data-click='deleteForm'>删除</span>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>公司账户银行</span>" +
                        "<select class='combobox form-control base-form-input base-request sele"+i+"' value="+data[i].bankNumber+" id='company_select' name='bankNo' >"+option+"</select>" +
                        "<i class='base-form-select-no'></i>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>公司账户号</span>" +
                        "<input type='text' name='accounts' class='base-form-input base-request' value='" + data[i].account + "'>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>公司账户客户号</span>" +
                        "<input type='text' name='customer' class='base-form-input base-request' value='" + data[i].customer + "'>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>证书序号</span>" +
                        "<input type='text' name='certificateNumber' class='base-form-input base-request' value='" + data[i].certificateNumber + "'>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>银行数字证书授权码</span>" +
                        "<input type='text' name='authorizationCode' class='base-form-input base-request' value='" + data[i].authorizationCode + "'>" +
                        "</div>" +
                        "<div class='base-offset-label'>" +
                        "<button type='button' class='base-sure-btn' data-click='sureForm'>确定</button>" +
                        "<button type='button' class='base-cancel-btn' data-click='deleteForm'>取消</button>" +
                        "</div>" +
                        "</div>";
                    //插入银行信息
                    $(".sele"+i+"").find("option[]").attr("selected",true);

                }
                $(".form-box").html(html);
                $(".form-box .add-form-item").each(function(){
                    $(this).children('.base-form:gt(0)').slideUp();
                    $(this).find('input').attr('readonly',true);
                    $(this).find('select').attr('disabled',true);
                });
                $("#company_modal").modal("show");
            },
            error : function () {
                Ewin.alert("操作异常，请联系管理员");
            }
        })
    },
    //删除公司
    deleteCompany : function (e) {
        e = window.event;
        e.preventDefault();
        var row = $('#company_table').bootstrapTable('getSelections');
        if(row.length <= 0){
            Ewin.alert("请选中需要删除的数据");
            return;
        }
        Confirm.show('提示', '确定删除该公司吗？', {
            'Delete': {
                'primary': true,
                'callback': function() {
                    var ids = ""; //存放多个公司编号
                    for (var i = 0 ; i < row.length ;i++){
                        if(i > 0 ){
                            ids += "," + row[i].companyNumber;
                        }else{
                            ids += row[i].companyNumber;
                        }
                    }
                    $.ajax({
                        url : 'deleteCompanys',
                        dataType : 'json',
                        type : 'post',
                        data:  {
                            "ids" : ids
                        },
                        success : function (data) {
                            if(data.code == 300){
                                Ewin.alert(data.message);
                            }else if(data.code == 500){
                                Ewin.alert("操作异常，请联系管理员");
                            }else{
                                Confirm.hide();
                                Ewin.alert(data.message);
                                $('#company_table').bootstrapTable('refresh');
                            }

                        },
                        error : function () {
                            Ewin.alert("操作异常，请联系管理员");
                        }
                    })

                }
            }
        });
    }


};

//添加账户样式
$(document).on('click','[data-click]',function(e){
    e.stopPropagation();
    e.preventDefault();

    var self=$(this),
        name=$(this).data('click'),
        html='<div class="add-form-item">'+$('.clone-form').html()+'</div>';

    switch (name){

        case 'addForm': //确定操作
            $('.form-box').append(html);
            break;

        case 'sureForm': //确定操作
            self.parents('.add-form-item').addClass('check-form');
            self.parents('.add-form-item').children('.base-form:gt(0)').slideUp();
            self.parents('.add-form-item').find('input').attr('readonly',true);
            self.parents('.add-form-item').find('select').attr('disabled',true);
            break;

        case 'editForm': //编辑操作
            self.parents('.add-form-item').removeClass('check-form');
            self.parents('.add-form-item').children('.base-form:gt(0)').slideDown();
            self.parents('.add-form-item').find('input').removeAttr('readonly');
            self.parents('.add-form-item').find('select').removeAttr('disabled');
            break;

        case 'toggleForm': //点击展开收缩表格
            self.parents('.add-form-item').children('.base-form:gt(0)').slideToggle();
            self.find('input').attr('readonly',true);
            self.find('select').attr('disabled',true);
            break;

        case 'deleteForm': //删除操作
            self.parents('.add-form-item').remove();

            break;
    }
});
