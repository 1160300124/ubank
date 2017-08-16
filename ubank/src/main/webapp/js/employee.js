//arr function
$(function () {
    EmployeeFun.emp_getAllGroup();
   // EmployeeFun.emp_getCompany();
    //EmployeeFun.emp_getDept();
    EmployeeFun.getAllBank();
    EmployeeFun.employeeQuery();
    EmployeeFun.getAllRoles();
    EmployeeFun.emp_listening();

    var flag = 0; //标识。 0 表示新增操作，1 表示修改操作

});
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
                {field : 'dept_name', title : '部门', width: 130, align : 'left'},
                {field : 'com_name', title : '公司', width: 130, align : 'left'},
                {field : 'cardNo', title : '身份证', width: 130, align : 'left'},
                {field : 'bankName', title : '绑定银行', width: 130, align : 'left'},
                {field : 'bankCardNo', title : '银行卡', width: 130, align : 'left'},
                {field : 'mobile', title : '预留手机号', width: 130, align : 'left'},
                {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false},
                {field : 'groupNumber', title : '集团编号', width: 130, align : 'left',visible : false},
                {field : 'dept_number', title : '部门编号', width: 130, align : 'left',visible : false},
                {field : 'id', title : '员工编号', width: 130, align : 'left',visible : false},
                {field : 'role_id', title : '角色id', width: 60, align : 'left',visible : false},
                {field : 'dept_name', title : '部门名称', width: 60, align : 'left',visible : false},
                {field : 'com_name', title : '公司名称', width: 60, align : 'left',visible : false},
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
            companyNumber : COMPANYNUMBER
        };
        return paramData;
    },
    //打开新增dialog
    openAdd : function () {
        flag = 0;
        $(".modal-title").html("新增");
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
        $.ajax({
            url : 'getComByGroup',
            dataType : 'json',
            type : 'post',
            data:  {
                "groupNum" : groupNum
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

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //获取所有部门
    emp_getDept : function (comNum) {
        $("#emp_select_dept").empty();
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
        var mobile = $("input[name=mobile]").val();
        var userName = $("input[name=userName]").val();
        var pwd = mobile.substr(7,4);
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
        if(bank == ""){
            Ewin.alert("银行不能为空");
            return;
        }
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
        }else if(!Validate.regNumAndLetter(userName)){
            Ewin.alert("性名格式不合法，请重新输入");
            return;
        }
        if(cardNo == ""){
            Ewin.alert("身份证号不能为空");
            return;
        }else if(!Validate.isCardID(cardNo)){
            Ewin.alert("身份证号格式不合法，请重新输入");
            return;
        }
        if(bankCardNo == ""){
            Ewin.alert("银行账号不能为空");
            return;
        }else if(!Validate.regNumber(bankCardNo)){
            Ewin.alert("银行账号格式不合法，请重新输入");
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
        $("input[name=id]").val(row[0].id);
        $("input[name=mobile]").val(row[0].mobile);
        $("input[name=bankCardNo]").val(row[0].bankCardNo);
        $("input[name=userName]").val(row[0].userName);
        $("input[name=cardNo]").val(row[0].cardNo);
        $("#emp_select_group").find("option[value="+row[0].groupNumber+"]").prop("selected","selected");
        $("#emp_select").html("<option value='" + row[0].companyNumber + "'>"+(row[0].com_name != undefined ? row[0].com_name : '')+"</option>");
        $("#emp_select_dept").html("<option value='" + row[0].dept_number + "'>"+row[0].dept_name+"</option>");
        $("#emp_select_bank").find("option[value="+row[0].bankNo+"]").attr("selected","selected");
        $("#emp_select_role").find("option[value="+row[0].role_id+"]").attr("selected","selected");
        $("#employee_modal").modal("show");

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