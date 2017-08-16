<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 部门页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="department_Toolbar" class="btn-group">
        <button  onclick="DepartmentFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="DepartmentFun.openModify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="DepartmentFun.departmentDelete(window.event)" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick=" DepartmentFun.departmentQuery()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="department_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="department_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="department_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属公司</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="dept_select" name="company_num" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">部门名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="name"   >
                                    <input type="text" class="form-control" style="display: none;" name="dept_number"   >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">备注</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" id="dept_area" name="remark"  rows="3"></textarea>
                                </div>
                            </div>

                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="DepartmentFun.departmentAdd()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script type="text/javascript">

    $(function () {
        DepartmentFun.departmentQuery();
        DepartmentFun.getAllCompany();
        DepartmentFun.dept_listening();
    });
    var flag = 0; //标识。 0 表示新增操作，1 表示修改操作

    //all function
    var DepartmentFun = {
        //查询
        departmentQuery : function () {
            $("#department_table").bootstrapTable({
                url : 'departmentQuery',
                method : 'post',// get,post
                toolbar : '#department_Toolbar', // 工具栏
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
              //  showColumns : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : 'name', title : '部门名称', width: 130, align : 'left'},
                    {field : 'dept_number', title : '部门编号', width: 130, align : 'left',visible : false},
                    {field : 'company_num', title : '公司编号', width: 130, align : 'left',visible : false},
                    {field : 'count', title : '部门人数', width: 60, align : 'left'},
                    {field : 'remark', title : '备注', width: 150, align : 'left'}
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
        //弹出框关闭监听事件
        dept_listening : function () {
            $("#department_modal").on('hide.bs.modal',function () {
                $('#department_table').bootstrapTable('uncheckAll');
                $("#department_form")[0].reset();
            });
        },
        //打开新增窗口
        openAdd : function () {
            flag = 0;
            $(".modal-title").html("新增");
            $("#department_modal").modal("show");
        },
        //获取所有公司
        getAllCompany : function () {
            $.ajax({
                url : 'getAllCom',
                dataType : 'json',
                type : 'post',
                data:  {
                    "groupNumber" : GROUPNUMBER,
                    "sysflag" : SYSFLAG
                },
                success : function (data) {
                    if(data.length <= 0){
                        Ewin.alert("获取公司失败，请联系管理员");
                        return;
                    }
                    var option = "";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].companyNumber+"'>"+data[i].name+"</option>";
                    }
                    $("#dept_select").append(option);

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //新增操作
        departmentAdd : function () {
            var name = $("input[name=name]").val();
            var com = $("#dept_select").val();
            if(name == ""){
                Ewin.alert("部门名称不能为空");
                return ;
            }else if(!Validate.regNumAndLetter(name)){
                Ewin.alert("部门名称格式不合法，请重新输入");
                return;
            }
            if(com == ""){
                Ewin.alert("公司不能为空");
                return ;
            }
            $.ajax({
                url : 'addDept?flag=' + flag ,
                dataType : 'json',
                type : 'post',
                data:  $("#department_form").serialize(),
                success : function (data) {
                    if(data.code == 300){
                        Ewin.alert(data.message);
                    }else if(data.code == 500){
                        Ewin.alert("操作异常，请联系管理员");
                    }else{
                        Ewin.alert(data.message);
                        $("#department_form")[0].reset();
                        $("#department_modal").modal("hide");
                        $('#department_table').bootstrapTable('refresh');
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
            var row = $('#department_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            debugger;
            $("#dept_select").find("option[value='"+row[0].company_num+"']").prop("selected","selected");
            $("input[name=name]").val(row[0].name);
            $("input[name=dept_number]").val(row[0].dept_number);
            $("#dept_area").val(row[0].remark);
            $("#department_modal").modal("show");

        },
        //删除
        departmentDelete : function (e) {
            e = window.event;
            e.preventDefault();
            var row = $('#department_table').bootstrapTable('getSelections');
            if(row.length <= 0){
                Ewin.alert("请选中需要删除的数据");
                return;
            }
            Confirm.show('提示', '确定删除该部门吗？', {
                'Delete': {
                    'primary': true,
                    'callback': function() {
                        var numbers = ""; //存放多个部门编号
                        for (var i = 0 ; i < row.length ;i++){
                            if(i > 0 ){
                                numbers += "," + row[i].dept_number;
                            }else{
                                numbers += row[i].dept_number;
                            }
                        }
                        $.ajax({
                            url : 'deleteDept',
                            dataType : 'json',
                            type : 'post',
                            data:  {
                                "numbers" : numbers
                            },
                            success : function (data) {
                                if(data.code == 300){
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                }else if(data.code == 500){
                                    Ewin.alert("操作异常，请联系管理员");
                                }else{
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                    $('#department_table').bootstrapTable('refresh');
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

</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>