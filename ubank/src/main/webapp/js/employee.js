//arr function
$(function () {
    $('#entryDate').datetimepicker({
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        pickDate: true,
        pickTime: true,
        autoclose: 1,
        todayBtn:  1,
        todayHighlight: 1,
        minView: "month"
    }).on('hide', function(event) {
        event.preventDefault();
        event.stopPropagation();
    });


    $('#leaveDate').datetimepicker({
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        pickDate: true,
        pickTime: true,
        autoclose: 1,
        todayBtn:  1,
        todayHighlight: 1,
        minView: "month"
    }).on('hide', function(event) {
        event.preventDefault();
        event.stopPropagation();
    });

    EmployeeFun.emp_getAllGroup();
    EmployeeFun.emp_getCompany_copy();
    //EmployeeFun.emp_getDept();
    EmployeeFun.getAllBank();
    EmployeeFun.employeeQuery();
    EmployeeFun.getAllRoles();
    EmployeeFun.emp_listening();


    $("#employee_upload_file").on('change', fileUploadOnChange);

});

var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
var activetion = ""; //是否激活。 0 否 1 是
var importExcelData = null;

function fileUploadOnChange(e) {

    if($(e.currentTarget).val() != '') {
        $("#import_btn").attr('disabled', false);
        $("#import_btn").removeClass('disabled');
        $(".import-file-name").html('已选择：' + $(e.currentTarget).val());
    } else {
        $("#import_btn").attr('disabled', true);
        $("#import_btn").addClass('disabled');
        $(".import-file-name").html('');
    }
}

function fileUploadOnChange(e) {

    if($(e.currentTarget).val() != '') {
        $("#import_btn").attr('disabled', false);
        $("#import_btn").removeClass('disabled');
        $(".import-file-name").html('已选择：' + $(e.currentTarget).val());
    } else {
        $("#import_btn").attr('disabled', true);
        $("#import_btn").addClass('disabled');
        $(".import-file-name").html('');
    }
}

var EmployeeFun = {
    //查询
    employeeQuery : function () {
        $("#employee_table").bootstrapTable({
            url : 'empQuery',
            method : 'post',// get,post
            toolbar : '#employee_Toolbar', // 工具栏
            striped : true, // 是否显示行间隔色
            cache : false, // 是否使用缓存，默认为true
            pagination : true, // 是否显示分页
            queryParams : this.queryParams,// 传递参数
            contentType : "application/x-www-form-urlencoded",
            sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
            search : true, //搜索框
            searchText : ' ', //初始化搜索文字
            pageNumber : 1, // 初始化加载第一页，默认第一页
            pageSize : 10, // 每页的记录行数
            pageList : [10,20,30,40], // 可供选择的每页的行数
            showRefresh : true, //刷新按钮
            showToggle :true,   //切换试图（table/card）按钮
            //showColumns : true,
            clickToSelect : true,
            columns : [
                {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                {field : 'userName', title : '员工姓名', width: 130, align : 'left'},
                {field : 'com_name', title : '所属公司', width: 130, align : 'left'},
                {field : 'dept_name', title : '所属部门', width: 130, align : 'left'},
                {field : 'role_name', title : '所属角色', width: 130, align : 'left'},
                {field : 'cardNo', title : '身份证', width: 130, align : 'left'},
                {field : 'bankName', title : '绑定银行', width: 130, align : 'left'},
                {field : 'bankCardNo', title : '银行卡', width: 130, align : 'left'},
                {field : 'mobile', title : '手机号', width: 130, align : 'left'},
                {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false},
                {field : 'groupNumber', title : '集团编号', width: 130, align : 'left',visible : false},
                {field : 'dept_number', title : '部门编号', width: 130, align : 'left',visible : false},
                {field : 'id', title : '员工编号', width: 130, align : 'left',visible : false},
                {field : 'role_id', title : '角色id', width: 60, align : 'left',visible : false},
                {field : 'dept_name', title : '部门名称', width: 60, align : 'left',visible : false},
                {field : 'com_name', title : '公司名称', width: 60, align : 'left',visible : false},
                {field : 'entryDate', title : '入职时间', width: 60, align : 'left',visible : false},
                {field : 'leaveDate', title : '离职时间', width: 60, align : 'left',visible : false},
                {field : 'bankNo', title : '银行编号', width: 130, align : 'left',visible : false}

            ]
        });
    },
    //查询参数定义
    queryParams : function (params) {
        var paramData = {
            pageSize : params.limit,
            pageNum : params.offset,
            search : params.search,
            sysflag : SYSFLAG,
            companyNumber : COMPANYNUMBER,
            activetion : activetion
        };
        return paramData;
    },
    //打开新增dialog
    openAdd : function () {
        flag = 0;
        $(".modal-title").html("新增");
        $("#emp_select").empty();
        $("#emp_select_dept").empty();
        $("#employee_modal").modal("show");

    },
    //弹出框关闭监听事件
    emp_listening : function () {
        $("#employee_modal").on('hide.bs.modal',function () {
            $('#employee_table').bootstrapTable('uncheckAll');
            $("#employee_form")[0].reset();
        });
    },
    //获取所有公司
    emp_getCompany : function (groupNum) {
        $("#emp_select").empty();
        $("#emp_select_copy").empty();
        $.ajax({
            url : 'getComByGroup',
            dataType : 'json',
            type : 'post',
            data:  {
                "groupNum" : groupNum,
                "companyNumber" : COMPANYNUMBER,
                "sysflag" : SYSFLAG
            },
            success : function (data) {
                if(data.length <= 0){
                    // Ewin.alert("获取公司失败");
                    return;
                }

                var option = "<option value=''>请选择</option>";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].companyNumber+"'>"+data[i].name+"</option>";
                }
                $("#emp_select").html(option);
                $("#emp_select_copy").html(option);

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //获取所有公司复制
    emp_getCompany_copy : function (groupNum) {
        var companys = COMPANYNUMBER.split(',');

        $("#emp_select_copy").empty();
        var option = "";
        for (var i = 0; i < companys.length; i++){
            option += "<option value='" + companys[i] + "'>" + companys[i] + "</option>";
        }
        $("#emp_select_copy").html(option);
    },
    //获取所有部门
    emp_getDept : function (comNum) {
        $("#emp_select_dept").empty();
        $("#emp_select_dept_copy").empty();
        $.ajax({
            url : 'getDeptByCom',
            dataType : 'json',
            type : 'post',
            data:  {
                "comNum" : comNum
            },
            success : function (data) {
                if(data.length <= 0){
                    return;
                }
                var option = "";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].dept_number+"'>"+data[i].name+"</option>";
                }
                $("#emp_select_dept").html(option);
                $("#emp_select_dept_copy").html(option);

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //获取所有部门copy
    emp_getDept_copy : function (comNum) {
        $("#emp_select_dept_copy").empty();
        $.ajax({
            url : 'getDeptByCom',
            dataType : 'json',
            type : 'post',
            data:  {
                "comNum" : comNum
            },
            success : function (data) {
                if(data.length <= 0){
                    return;
                }
                var option = "";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].dept_number+"'>"+data[i].name+"</option>";
                }
                $("#emp_select_dept_copy").html(option);

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //获取所有集团
    emp_getAllGroup : function () {
        $.ajax({
            url : 'getAllGroup',
            dataType : 'json',
            type : 'post',
            data:  {
                "groupNumber" : GROUPNUMBER,
                "sysflag" : SYSFLAG
            },
            success : function (data) {
                if(data.length <= 0){
                    // Ewin.alert("没有集团数据");
                    return;
                }
                var option = "<option value=''>请选择</option>";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].groupNumber+"'>"+data[i].name+"</option>";
                }
                $("#emp_select_group").html(option);

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //获取所有角色信息
    getAllRoles : function () {
        $.ajax({
            url : 'roleAllQuery',
            dataType : 'json',
            type : 'post',
            data:  {
                "companyNumber" : COMPANYNUMBER,
                "sysflag" : SYSFLAG
            },
            success : function (data) {
                if(data.length <= 0){
                    return;
                }
                var option = "";
                option = "<option value='0'>员工</option>";
                for (var i = 0; i < data.length; i++){
                    if(data[i].role_id == 0){
                        continue;
                    }
                    option += "<option value='"+data[i].role_id+"'>"+data[i].role_name+"</option>";
                }
                $("#emp_select_role").html(option);

            },
            error : function () {
                Ewin.alert("操作异常");
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
                    Ewin.alert("获取银行失败");
                    return;
                }
                var option = "";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].bankNo+"'>"+data[i].bankName+"</option>";
                }
                $("#emp_select_bank").append(option);

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //新增
    addEmp: function () {
        $("input[name=userName]").prop("disabled","");
        $("input[name=cardNo]").prop("disabled","");
        $("input[name=mobile]").prop("disabled","");
        var mobile = $("input[name=mobile]").val();
        var userName = $("input[name=userName]").val();
        var pwd = mobile.substr(5,6);
        var cardNo = $("input[name=cardNo]").val();
        var bankCardNo = $("input[name=bankCardNo]").val();
        var com = $("#emp_select").val();
        var dept = $("#emp_select_dept").val();
        var bank = $("#emp_select_bank").val();
        var role = $("#emp_select_role").val();
        var group = $("#emp_select_group").val();
        if(com == ""){
            Ewin.alert("公司不能为空");
            return;
        }
        if(dept == ""){
            Ewin.alert("部门不能为空");
            return;
        }
        // if(bank == ""){
        //     Ewin.alert("银行不能为空");
        //     return;
        // }
        if(role == ""){
            Ewin.alert("角色不能为空");
            return;
        }
        if(group == ""){
            Ewin.alert("集团不能为空");
            return;
        }
        if(mobile == ""){
            Ewin.alert("联系电话不能为空");
            return;
        }else if(!Validate.regPhone(mobile)){
            Ewin.alert("电话号码格式不合法，请重新输入");
            return;
        }
        if(userName == ""){
            Ewin.alert("姓名不能为空");
            return;
        }else if(!Validate.regWord(userName)){
            Ewin.alert("姓名必须为中文");
            return;
        }else if(userName.length > 20){
            Ewin.alert("姓名不能超过20个字符");
            return;
        }
        if(cardNo == ""){
            Ewin.alert("身份证号不能为空");
            return;
        }else if(!Validate.isCardID(cardNo)){
            Ewin.alert("身份证号格式不合法，请重新输入");
            return;
        }
        // if(bankCardNo == ""){
        //     Ewin.alert("银行账号不能为空");
        //     return;
        // }else if(!Validate.regNumber(bankCardNo)){
        //     Ewin.alert("银行账号必须为数字");
        //     return;
        // }else if(bankCardNo.length <= 18 || bankCardNo.length > 21 ){
        //     Ewin.alert("银行账号长度在18-21位");
        //     return;
        // }
        var entryDate = $('#entryDate').val();
        var leaveDate = $('#leaveDate').val();
        var d1 = new Date(entryDate.replace(/\-/g, "\/"));
        var d2 = new Date(leaveDate.replace(/\-/g, "\/"));
        if(entryDate == "" ){
            Ewin.alert("入职时间不能为空");
            return;
        }else if(d1 > d2){
            Ewin.alert("入职时间不能大于离职时间");
            return;
        }

        $.ajax({
            url : 'addEmployee?flag=' + flag + "&pwd=" + pwd,
            dataType : 'json',
            type : 'post',
            data:  $("#employee_form").serialize(),
            success : function (data) {
                if(data.code == 300){
                    Ewin.alert(data.message);
                }else if(data.code == 500){
                    Ewin.alert("操作异常");
                }else{
                    Ewin.alert(data.message);
                    $("#employee_form")[0].reset();
                    $("#employee_modal").modal("hide");
                    $('#employee_table').bootstrapTable('refresh');
                }

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //修改操作
    openModify : function () {
        $("input[name=userName]").prop("disabled","disabled");
        $("input[name=cardNo]").prop("disabled","disabled");
        $("input[name=mobile]").prop("disabled","disabled");
        flag = 1;
        $(".modal-title").html("修改");
        var row = $('#employee_table').bootstrapTable('getSelections');
        if(row.length > 1){
            Ewin.alert("不能多选，请重新选择");
            return;
        }else if(row.length <= 0){
            Ewin.alert("请选中需要修改的数据");
            return;
        }
        this.emp_queryAllCom(); //加载公司信息
        this.emp_queryAllDept(row[0].companyNumber); //加载部门信息
        $("input[name=id]").val(row[0].id);
        $("input[name=mobile]").val(row[0].mobile);
        $("input[name=bankCardNo]").val(row[0].bankCardNo);
        $("input[name=userName]").val(row[0].userName);
        $("input[name=cardNo]").val(row[0].cardNo);
        $("#emp_select_group").find("option[value="+row[0].groupNumber+"]").prop("selected","selected");
        $("#emp_select").find("option[value="+row[0].companyNumber+"]").prop("selected","selected");
        // var option = "<option value='"+row[0].companyNumber+"'>"+row[0].com_name+"</option>";
        // $("#emp_select").html(option);
        if(row[0].dept_number != ""){
            $("#emp_select_dept").find("option[value="+row[0].dept_number+"]").prop("selected","selected");
        }
        $("#emp_select_bank").find("option[value="+row[0].bankNo+"]").prop("selected","selected");
        $("#emp_select_role").find("option[value="+row[0].role_id+"]").prop("selected","selected");
        $('#entryDate').val(row[0].entryDate);
        $('#leaveDate').val(row[0].leaveDate);
        $("#employee_modal").modal("show");

    },
    //点击修改，加载公司信息
    emp_queryAllCom : function () {
        $.ajax({
            url : 'getAllCom',
            dataType : 'json',
            type : 'post',
            async : false,
            data : {
                "groupNumber" : GROUPNUMBER,
                "companyNumber" : COMPANYNUMBER,
                "sysflag" : SYSFLAG
            },
            success : function (data) {
                if(data.length <= 0){
                    return;
                }
                var option = "<option value=''>请选择</option>";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].companyNumber+"'>"+data[i].name+"</option>";
                }
                $("#emp_select").html(option);
            },
            error : function () {
                Ewin.alert("获取公司失败");
            }
        });
    },
    //点击修改，加载部门信息
    emp_queryAllDept : function (companyNumber) {
        $.ajax({
            url : 'queryAllDept',
            dataType : 'json',
            type : 'post',
            async : false,
            data : {
                "companyNumber" : companyNumber,
                "sysflag" : SYSFLAG
            },
            success : function (data) {
                if(data.length <= 0){
                    return;
                }
                var option = "";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].dept_number+"'>"+data[i].name+"</option>";
                }
                $("#emp_select_dept").html(option);
            },
            error : function () {
                Ewin.alert("获取部门失败");
            }
        });
    },
    //删除
    emp_delete : function (e) {
        e = window.event;
        e.preventDefault();
        var row = $('#employee_table').bootstrapTable('getSelections');
        if(row.length <= 0){
            Ewin.alert("请选中需要删除的数据");
            return;
        }
        Confirm.show('提示', '确定删除该员工吗？', {
            'Delete': {
                'primary': true,
                'callback': function() {
                    var numbers = ""; //存放多个部门编号
                    for (var i = 0 ; i < row.length ;i++){
                        if(i > 0 ){
                            numbers += "," + row[i].id;
                        }else{
                            numbers += row[i].id;
                        }
                    }
                    $.ajax({
                        url : 'deleteEmployee',
                        dataType : 'json',
                        type : 'post',
                        data:  {
                            "numbers" : numbers
                        },
                        success : function (data) {
                            if(data.code == 300){
                                Ewin.alert(data.message);
                            }else if(data.code == 500){
                                Ewin.alert("操作异常");
                            }else{
                                Ewin.alert(data.message);
                                //$("#employee_form")[0].reset();
                                //$("#employee_modal").modal("hide");
                                $('#employee_table').bootstrapTable('refresh');
                                Confirm.hide();
                            }

                        },
                        error : function () {
                            Ewin.alert("操作异常");
                        }
                    })

                }
            }
        });

    },
    reload : function () {
        $('#employee_table').bootstrapTable('refresh');
    },
    showImport: function(){
        $('#employee_import_modal').modal("show");
    },
    import: function() {
        $('.import-step').hide();
        $('.import-step-two').show();

        $("#import_btn").attr('disabled', true);
        $("#import_btn").addClass('disabled');

        $.ajaxFileUpload({
            url: 'importEmployee?group='+GROUPNUMBER + '&comNum='+COMPANYNUMBER,
            type: 'POST',
            secureuri: false,
            fileElementId: "employee_upload_file",
            dataType: "json",
            data:{picParams:'1sdf '},
            // data: {
            //     name : $('#employee_upload_file').val(),
            //     group : GROUPNUMBER,
            //     comNum : COMPANYNUMBER
            // },
            success: function(data) {
                console.log('success')
                $("#employee_upload_file").on('change', fileUploadOnChange);
                $('.import-step').hide();
                $('.import-modal-btns').hide();

                var importData = JSON.parse(data);

                if (importData.code === 1000) {
                    if (importData.data && importData.data.length > 0) {

                        importExcelData = importData.data;
                        editImportData();
                    } else {
                        $('.import-step-success').show();
                        $('.import-btns').show();
                    }
                }
            },
            error: function(error, b, c) {
                $("#employee_upload_file").on('change', fileUploadOnChange);
                $('.import-step').hide();
                $('.import-step-one').show();
                console.log(error);
                console.log(b);
                console.log(c);
            }
        })
    },
    updateImport: function() {
        unEditAll();
        // if(toEditErrorData())
        // {

        // }
        $('.import-step').hide();
        $('.import-step-two').show();

        $("#update_import_btn").attr('disabled', true);
        $("#update_import_btn").addClass('disabled');

        $.ajax({
            url: 'batchImport',
            type: 'POST',
            dataType: "json",
            data: {
                rows : JSON.stringify(importExcelData),
                group : GROUPNUMBER,
                comNum : COMPANYNUMBER,
                deptNum : DEPTNUMBER
            },
            success: function(data) {
                $("#employee_upload_file").on('change', fileUploadOnChange);
                $('.import-step').hide();
                $('.import-modal-btns').hide();

                if (data.code === 1000) {
                    if (data.data && data.data.length > 0) {
                        importExcelData = data.data;
                        editImportData();
                    } else {
                        $('.import-step-success').show();
                        $('.import-btns').show();
                    }
                } else {
                    $(".import-step-edit").show();
                    $(".update-import-btns").show();
                }
            },
            error: function() {
                $("#employee_upload_file").on('change', fileUploadOnChange);
                $('.import-step').hide();
                $('.import-step-one').show();
            }
        })
    },
    initImportModal: function() {
        $('.import-step').hide();
        $('.import-step-one').show();

        $('.import-modal-btns').hide();
        $('.import-btns').show();
        $(".import-file-name").html('');
    },
    //激活
    activetion : function () {
        activetion = "1";
        this.reload();
    },
    //未激活
    inactivated : function () {
        activetion = "0";
        this.reload();
    }

};

//选择框监听事件
$("#emp_select_group").change(function(){
    var groupNum = $(this).val();
    if(groupNum != ''){
        EmployeeFun.emp_getCompany(groupNum);
    }
});

//选择框监听事件
$("#emp_select").change(function () {
    var comNum = $(this).val();
    EmployeeFun.emp_getDept(comNum);
});

$("#emp_select_copy").change(function () {
    var comNum = $(this).val();
    EmployeeFun.emp_getDept_copy(comNum);
});

function editImportData() {

    $('.import-step-edit').show();
    $('.update-import-btns').show();

    $("#update_import_btn").attr('disabled', false);
    $("#update_import_btn").removeClass('disabled');

    $("#updat_import_table").bootstrapTable({
        striped : true, // 是否显示行间隔色
        editable: true,
        cache : false, // 是否使用缓存，默认为true
        pagination : true, // 是否显示分页
        contentType : "application/x-www-form-urlencoded",
        sidePagination : "client", // 分页方式：client客户端分页，server服务端分页
        pageSize : 10, // 每页的记录行数
        pageList : [10, 20], // 可供选择的每页的行数
        clickToSelect : true,
        data: importExcelData,
        columns : [
            {field : 'id', title: '序号', width: 10, align : 'center', formatter: function(value, row, index, field){
                return parseInt(value);
            }},
            {field : 'name', title: '姓名', width: 10, align : 'center',edit:{type:'text' }},
            {field : 'idcard', title: '身份证', width: 10, align : 'center',edit:{type:'text' }},
            {field : 'mobile', title: '手机号', width: 10, align : 'center',edit:{type:'text' }},
            {field : 'entryTime', title: '入职时间', width: 10, align : 'center',edit:{type:'text' }},
            {field : 'message', title: '', width: 30, align : 'left', formatter: function(value, row, index, field){
                return '<i class="error-info glyphicon glyphicon-alert" data-info="' + value + '"></i>'
            }}
        ],
        onPostBody: function(e) {
            toEditErrorData(importExcelData);
        }
    });
    $("#updat_import_table").bootstrapTable('load', importExcelData);


    //错误信息展示
    $(".error-info").on('mouseenter', function(e) {
        var msg = $(e.currentTarget).attr('data-info');
        var msgs = msg.split(',');
        var msgsDom = '';
        $(msgs).each(function(index, item) {
            msgsDom += "<div>" + (index + 1) + ":" + item + "</div>"
        });
        $('body').append('<div class="error-info-tip" style="left:' + ($(e.currentTarget).offset().left + 40) + 'px;top:' + ($(e.currentTarget).offset().top + e.currentTarget.offsetHeight / 2 ) + 'px;">' + msgsDom + '</div>')
    })
    //移除错误信息
    $(".error-info").on('mouseout', function(e) {
        $(".error-info-tip").remove();
    })

    //单击单元格编辑
    $("#updat_import_table tbody td").on('click', function(e) {
        var tdDom = $(e.currentTarget);
        editTd(tdDom);

        tdDom
            .find('input')
            .removeClass('danger-input')
            .focus();
        setSelectionRange(tdDom.find('input')[0], tdDom.html().length, tdDom.html().length);
    });

}

//定位输入框光标到指定位置
function setSelectionRange(input, selectionStart, selectionEnd) {
    if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(selectionStart, selectionEnd);
    }
    else if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionEnd);
        range.moveStart('character', selectionStart);
        range.select();
    }
}

//把单元格变成可编辑输入框
function editTd(target) {
    if(target.index() <= 0 || target.index() >= 5){
        return;
    }
    if(!target.hasClass('td-editing')){
        target
            .html('<input type="text" class="td-input" value="' + target.text() + '" />')
            .addClass('td-editing')
            .find('input').on('blur', function(e) {
            unEdit($(e.currentTarget));
        });
    }

}

//遍历数据 把错误的数据单元格变成编辑状态
function toEditErrorData(data) {
    var haveError = false;
    $(data).each(function(index, item) {
        for(var key in item) {
            if(key === 'id' || key === 'message') continue;
            if(item[key] === '') {
                var dataTdIndex = findTdIndexByFieldName(key);
                editTd(findTdByRC(index, dataTdIndex));
                haveError = true;
                return ;
            }
            if(key === 'mobile' && item[key] && !Validate.regPhone(item[key])) {
                var dataTdIndex = findTdIndexByFieldName(key);
                editTd(findTdByRC(index, dataTdIndex));
                haveError = true;
                return ;
            }
            if(key === 'idcard' && item[key] && item[key].length < 18) {
                var dataTdIndex = findTdIndexByFieldName(key);
                editTd(findTdByRC(index, dataTdIndex));
                haveError = true;
                return ;
            }
        }
    });
    $(".td-input").addClass('danger-input');
    return haveError;
}

//通过字段名找到列的index
function findTdIndexByFieldName(fieldName) {
    return $('#updat_import_table thead th[data-field="' + fieldName + '"').index();
}

/**
 * 通过列index获取列名
 * @param {列index} index
 */
function getFieldNameByColumnIndex(index) {
    return $($("#updat_import_table thead tr").children()[index]).attr('data-field');
}

//通过行号、列号找到 td
function findTdByRC(row, column) {
    var td = $($('#updat_import_table tbody tr')[row]).children()[column];
    return $(td);
}

function unEditAll() {
    var inputs = $('#updat_import_table tbody input');
    $(inputs).each(function(index, item) {
        console.log(item);
        unEdit($(item));
    })
}

/**
 * 取消编辑状态
 * @param {输入框} input
 */
function unEdit(input) {
    var tdDom = input.parent();
    tdDom.removeClass('td-editing').html(input.val());
    var index = tdDom.parent().attr('data-index');
    var fieldName = getFieldNameByColumnIndex(tdDom.index());
    importExcelData[index][fieldName] = input.val();
}