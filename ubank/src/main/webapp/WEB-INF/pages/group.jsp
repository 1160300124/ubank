<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 集团页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="group_Toolbar" class="btn-group">
        <button id="group_add" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="group_edit" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button id="group_delete" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="group_search" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="group_table" >
        <%--<thead>--%>
            <%--<tr>--%>
                <%--<th data-checkbox="true"></th>--%>
                <%--<th data-field="userName">操作人</th>--%>
                <%--<th data-field="totalNumber">总笔数</th>--%>
                <%--<th data-field="totalAmount">总金额</th>--%>
                <%--<th data-field="company">企业名称</th>--%>
                <%--<th data-field="salaryDate">工资发放时间</th>--%>
                <%--<th data-field="salary_createTime">操作时间</th>--%>
                <%--<th data-field="entrustSeqNo">业务委托编号</th>--%>
                <%--<th data-field="status">状态</th>--%>
                <%--<th data-field="remark">备注</th>--%>
                <%--<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>--%>
            <%--</tr>--%>
        <%--</thead>--%>

    </table>

        <!-- 模态框（Modal） -->
        <div id="group_add_modal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">新增集团</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-inline base-form clearfix">
                            <form id="group_form" method="post" class="form-horizontal" >
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">集团名称</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" name="name" id="" >
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
                                        <textarea class="form-control" name="details"  rows="3"></textarea>
                                    </div>
                                </div>

                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">备注</label>
                                    <div class="col-md-9">
                                        <textarea class="form-control" name="remark" rows="3"></textarea>
                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" id="group_save" class="btn btn-primary">保存</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

</div>
<script type="text/javascript">
    $("#group_table").bootstrapTable({
            url : '',
            method : 'post',// get,post
            toolbar : '#group_Toolbar', // 工具栏
            striped : true, // 是否显示行间隔色
            cache : false, // 是否使用缓存，默认为true
            pagination : true, // 是否显示分页
            queryParams : {},// 传递参数
            pageNumber : 1, // 初始化加载第一页，默认第一页
            pageSize : 20, // 每页的记录行数
            pageList : [20,30,40], // 可供选择的每页的行数
            columns : [
                {field : 'name', title : '集团名称', width: 130, align : 'center'},
                {field : 'contacts', title : '负责联系人', width: 100 , align : 'center'},
                {field : 'contactsTelephone', title : '负责联系人号码', width: 100 , align : 'center'},
                {field : 'details', title : '详情', width: 150 , align : 'center'},
                {field : 'groupNumber', title : '集团编号', width: 100 , align : 'center',visible : false}

            ]
    });

    //打开新增dialog
    $("#group_add").click(function(){
        $("#group_add_modal").modal("show");
    });

    //新增操作
    $("#group_save").click(function () {
        $.ajax({
            url : 'addGroup',
            dataType : 'json',
            type : 'post',
            data:  $("#group_form").serialize(),
            success : function (data) {
                debugger;
                if(data.code == 300){
                    Ewin.alert(data.msg);
                }else if(data.code == 500){
                    Ewin.alert("操作异常，请联系管理员");
                }else{
                    Ewin.alert(data.msg);
                    $("#group_add_modal").modal("hide");
                }

            },
            error : function () {
                Ewin.alert("操作异常，请联系管理员");
            }
        })
    })

</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>