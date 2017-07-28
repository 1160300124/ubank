<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="employee_Toolbar" class="btn-group">
        <button  onclick="EmployeeFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="EmployeeFun.openModify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="EmployeeFun.emp_delete(window.event)" id="three-button" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="EmployeeFun.employeeQuery()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="employee_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="employee_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="employee_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属公司</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control"  name="companyNumber"  id="emp_select"></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属部门</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" name="dept_number"  id="emp_select_dept" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">角色</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" name="role_id" id="emp_select_role"  >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">姓名</label>
                                <div class="col-md-9">
                                    <input type="text" name="userName" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">身份证号码</label>
                                <div class="col-md-9">
                                    <input type="text" name="cardNo" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">绑定银行</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" name="bankNo" id="emp_select_bank"  ></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">银行卡号</label>
                                <div class="col-md-9">
                                    <input type="text" name="bankCardNo" class="base-form-input base-request" value="" >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">预留手机</label>
                                <div class="col-md-9">
                                    <input type="text" name="mobile" class="base-form-input base-request" value="" >
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="EmployeeFun.addEmp() " class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script type="text/javascript">


    //arr function


    $(function () {
        EmployeeFun.emp_getCompany();
        EmployeeFun.emp_getDept();
        EmployeeFun.getAllBank();
        EmployeeFun.employeeQuery();
        EmployeeFun.getAllRoles();

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
                showColumns : true,
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false},
                    {field : 'dept_number', title : '部门编号', width: 130, align : 'left',visible : false},
                    {field : 'id', title : '员工编号', width: 130, align : 'left',visible : false},
                    {field : 'role_id', title : '角色id', width: 60, align : 'left',visible : false},
                    {field : 'userName', title : '员工姓名', width: 130, align : 'left'},
                    {field : 'dept_name', title : '部门姓名', width: 130, align : 'left'},
                    {field : 'com_name', title : '公司', width: 130, align : 'left'},
                    {field : 'cardNo', title : '身份证', width: 130, align : 'left'},
                    {field : 'bankNo', title : '银行编号', width: 130, align : 'left',visible : false},
                    {field : 'bank_name', title : '绑定银行', width: 130, align : 'left'},
                    {field : 'bankCardNo', title : '银行卡', width: 130, align : 'left'},
                    {field : 'mobile', title : '预留手机号', width: 130, align : 'left'}

                ]
            });
        },
        //查询参数定义
        queryParams : function (params) {
            var paramData = {
                pageSize : params.limit,
                pageNum : params.offset,
                search : params.search
            };
            return paramData;
        },
        //打开新增dialog
        openAdd : function () {
            flag = 0;
            $(".modal-title").html("新增");
            $("#employee_modal").modal("show");


        },
        //获取所有公司
        emp_getCompany : function () {
            $.ajax({
                url : 'getAllCom',
                dataType : 'json',
                type : 'post',
                data:  {},
                success : function (data) {
                    if(data.length <= 0){
                        Ewin.alert("获取公司失败，请联系管理员");
                        return;
                    }
                    var option = "";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].companyNumber+"'>"+data[i].name+"</option>";
                    }
                    $("#emp_select").append(option);

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //获取所有部门
        emp_getDept : function () {
            $.ajax({
                url : 'getAllDept',
                dataType : 'json',
                type : 'post',
                data:  {},
                success : function (data) {
                    if(data.length <= 0){
                        Ewin.alert("获取部门失败，请联系管理员");
                        return;
                    }
                    var option = "";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].dept_number+"'>"+data[i].name+"</option>";
                    }
                    $("#emp_select_dept").append(option);

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //获取所有角色信息
        getAllRoles : function () {
            $.ajax({
                url : 'roleAllQuery',
                dataType : 'json',
                type : 'post',
                data:  {},
                success : function (data) {
                    debugger;
                    if(data.length <= 0){
                        return;
                    }
                    var option = "";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].role_id+"'>"+data[i].role_name+"</option>";
                    }
                    $("#emp_select_role").html(option);

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
                    $("#emp_select_bank").append(option);

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //新增
        addEmp: function () {
            var mobile = $("input[name=mobile]").val();
            var pwd = mobile.substr(7,4);
            $.ajax({
                url : 'addEmployee?flag=' + flag + "&pwd=" + pwd,
                dataType : 'json',
                type : 'post',
                data:  $("#employee_form").serialize(),
                success : function (data) {
                    if(data.code == 300){
                        Ewin.alert(data.message);
                    }else if(data.code == 500){
                        Ewin.alert("操作异常，请联系管理员");
                    }else{
                        Ewin.alert(data.message);
                        $("#employee_form")[0].reset();
                        $("#employee_modal").modal("hide");
                        $('#employee_table').bootstrapTable('refresh');
                    }

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
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
            $("input[name=bankCardNo]").val(row[0].bankCardNo);
            $("input[name=mobile]").val(row[0].mobile);
            $("input[name=userName]").val(row[0].userName);
            $("input[name=cardNo]").val(row[0].cardNo);
            $("#emp_select").find("option[value="+row[0].companyNumber+"]").attr("selected","selected");
            $("#emp_select_dept").find("option[value="+row[0].dept_number+"]").attr("selected","selected");
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
                                    Ewin.alert("操作异常，请联系管理员");
                                }else{
                                    Ewin.alert(data.message);
                                    //$("#employee_form")[0].reset();
                                    //$("#employee_modal").modal("hide");
                                    $('#employee_table').bootstrapTable('refresh');
                                    Confirm.hide();
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


    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>