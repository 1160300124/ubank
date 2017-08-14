<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 集团页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="group_Toolbar" class="btn-group">
        <button  onclick="GroupFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="GroupFun.openModify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="GroupFun.gropDelete()" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="GroupFun.groupQuery()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="group_table" ></table>

        <!-- 弹出框（Modal） -->
        <div id="group_add_modal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title" ></h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-inline base-form clearfix">
                            <form id="group_form" method="post" class="form-horizontal" >
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">集团名称</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" name="name"  >
                                        <%--<input type="text" class="form-control" style="display: none;" name="groupNumber" >--%>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">法人</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" name="legalPerson"  id="" >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">注册资本</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" name="registeredCapital"  id="" >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">负责联系人</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" name="contacts"  id="" >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">联系人电话</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" name="contactsTelephone"  id="" >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">详情</label>
                                    <div class="col-md-9">
                                         <textarea class="form-control" name="details" id="group_details"  rows="3"></textarea>
                                    </div>
                                </div>

                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">备注</label>
                                    <div class="col-md-9">
                                        <textarea class="form-control" name="remark" id="group_remark" rows="3"></textarea>
                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" onclick="GroupFun.groupSave()" class="btn btn-primary">保存</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

</div>
<script type="text/javascript">

    //初始化数据
    $(function () {
        GroupFun.groupQuery();
    });
    var flag = 0; //标识。 0 表示新增操作，1 表示修改操作

    //all function
    var GroupFun = {
        //打开新增dialog
        openAdd : function () {
            flag = 0;
            $(".modal-title").html("新增");
            $("#group_add_modal").modal("show");

        },
        //新增操作
        groupSave : function () {
            var name = $("input[name=name]").val();
            var legalPerson = $("input[name=legalPerson]").val();
            var registeredCapital = $("input[name=registeredCapital]").val();
            var contacts = $("input[name=contacts]").val();
            var phone = $("input[name=contactsTelephone]").val();
            if(name == ""){
                Ewin.alert("集团名称不能为空");
                return;
            }else if(!Validate.regNumAndLetter(name)){
                Ewin.alert("集团名称格式不合法，请重新输入");
                return;
            }
            if(legalPerson == ""){
                Ewin.alert("法人不能为空");
                return;
            }else if(!Validate.regNumAndLetter(legalPerson)){
                Ewin.alert("法人格式不合法，请重新输入");
                return;
            }
            if(registeredCapital == ""){
                Ewin.alert("注册资本不能为空");
                return;
            }else if(!Validate.regNumAndLetter(registeredCapital)){
                Ewin.alert("注册资本格式不合法，请重新输入");
                return;
            }
            if(contacts == "" ){
                Ewin.alert("负责联系人不能为空");
                return;
            }else if(!Validate.regNumAndLetter(contacts)){
                Ewin.alert("负责联系人格式不合法，请重新输入");
                return;
            }
            if(phone == ""){
                Ewin.alert("联系电话不能为空");
                return;
            }else if(!Validate.regPhone(phone)){
                Ewin.alert("电话号码格式不合法，请重新输入");
                return;
            }
            $.ajax({
                url : 'addGroup?flag=' + flag ,
                dataType : 'json',
                type : 'post',
                data:  $("#group_form").serialize(),
                success : function (data) {
                    if(data.code == 300){
                        Ewin.alert(data.message);
                    }else if(data.code == 500){
                        Ewin.alert("操作异常");
                    }else{
                        Ewin.alert(data.message);
                        $("#group_form")[0].reset();
                        $("#group_add_modal").modal("hide");
                        $('#group_table').bootstrapTable('refresh');
                    }

                },
                error : function () {
                    Ewin.alert("操作异常");
                }
            })
        },
        //查询
        groupQuery : function () {
            $("#group_table").bootstrapTable({
                url : 'groupQuery',
                method : 'POST',// get,post
                toolbar : '#group_Toolbar', // 工具栏
                striped : true, // 是否显示行间隔色
                cache : false, // 是否使用缓存，默认为true
                pagination : true, // 是否显示分页
                queryParams : this.queryParams,
                contentType : "application/x-www-form-urlencoded",
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 20, // 每页的记录行数
                pageList : [20,30,40], // 可供选择的每页的行数
                sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
                showRefresh : true, //刷新按钮
                showToggle :true,   //切换试图（table/card）按钮
                search : true, //搜索框
                searchText : ' ', //初始化搜索文字
                clickToSelect : true,
               // showColumns : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : 'name', title : '集团名称', width: 130, align : 'left'},
                    {field : 'contacts', title : '负责联系人', width: 100 , align : 'left'},
                    {field : 'contactsTelephone', title : '负责联系人号码', width: 100 , align : 'left'},
                    {field : 'details', title : '详情', width: 150 , align : 'left'},
                    {field : 'groupNumber', title : '集团编号', width: 100 , align : 'left',visible : false},
                    {field : 'registeredCapital', title : '', width: 100 , align : 'left',visible : false},
                    {field : 'remark', title : '', width: 100 , align : 'left',visible : false},
                    {field : 'legalPerson', title : '', width: 100 , align : 'left',visible : false}

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
                groupNumber : GROUPNUMBER
            };
            return paramData;
        },
        //打开弹出框
        openModify : function () {
            flag = 1;
            $(".modal-title").html("修改");
            var row = $('#group_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            $('input[name=name]').val(row[0].name);
            $('input[name=legalPerson]').val(row[0].legalPerson);
            $('input[name=registeredCapital]').val(row[0].registeredCapital);
            $('input[name=contacts]').val(row[0].contacts);
            $('input[name=contactsTelephone]').val(row[0].contactsTelephone);
            $('input[name=groupNumber]').val(row[0].groupNumber);
            $('#group_details').val(row[0].details);
            $('#group_remark').val(row[0].remark);
            $("#group_add_modal").modal("show");

        },
        //删除
        gropDelete : function (e) {
            e = window.event;
            e.preventDefault();
            var row = $('#group_table').bootstrapTable('getSelections');
            if(row.length <= 0){
                Ewin.alert("请选中需要删除的数据");
                return;
            }
            Confirm.show('提示', '确定删除该集团吗？', {
                'Delete': {
                    'primary': true,
                    'callback': function() {
                        var numbers = ""; //存放多个集团编号
                        for (var i = 0 ; i < row.length ;i++){
                            if(i > 0 ){
                                numbers += "," + row[i].groupNumber;
                            }else{
                                numbers += row[i].groupNumber;
                            }
                        }
                        $.ajax({
                            url : 'deleteGroup',
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
                                    Ewin.alert("操作异常");
                                }else{
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                    $('#group_table').bootstrapTable('refresh');

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


</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>