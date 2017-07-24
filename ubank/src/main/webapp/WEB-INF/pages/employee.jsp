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
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="" type="button" class="btn btn-default">
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
                                    <select class="combobox form-control" id="emp_select" name="companyNumber" ></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属部门</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="emp_select_dept" name="dept_number" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">姓名</label>
                                <div class="col-md-9">
                                    <input type="text" name="name" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">身份证号码</label>
                                <div class="col-md-9">
                                    <input type="text" name="idCard" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">绑定银行</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="emp_select_bank" name="bankNo" ></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">银行卡号</label>
                                <div class="col-md-9">
                                    <input type="text" name="bankCard" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">预留手机</label>
                                <div class="col-md-9">
                                    <input type="text" name="telephone" class="base-form-input base-request" value="">
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

    $(function () {
       //EmployeeFun.employeeQuery()
        EmployeeFun.emp_getCompany();
        EmployeeFun.emp_getDept();
        EmployeeFun.getAllBank();
    });
    var flag = 0; //标识。 0 表示新增操作，1 表示修改操作


    //arr function
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
                pageSize : 20, // 每页的记录行数
                pageList : [20,30,40], // 可供选择的每页的行数
                showRefresh : true, //刷新按钮
                showToggle :true,   //切换试图（table/card）按钮
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : '', title : '', width: 130, align : 'left'},
                    {field : '', title : '', width: 130, align : 'left'},
                    {field : '', title : '', width: 130, align : 'left'},
                    {field : '', title : '', width: 130, align : 'left'},
                    {field : '', title : '', width: 130, align : 'left'},
                    {field : '', title : '', width: 130, align : 'left'},
                    {field : '', title : '', width: 130, align : 'left'}


                ]
            });
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
            $.ajax({
                url : 'addEmployee?flag=' + flag ,
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
        }


    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>