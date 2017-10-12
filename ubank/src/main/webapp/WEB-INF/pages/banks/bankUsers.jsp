<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<div class="page-content">
    <%--工具栏--%>
    <div id="bankUser_Toolbar" class="btn-group">
        <button  onclick="bankUser.openNew()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="bankUser.reload()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="bankUser_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="bankUser_modal" class="modal fade">
        <div class="modal-dialog" style="width: 800px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <div style="width: 40%;height: 100%;float: left;">
                            <ul class="ztree" id="bankUser_tree"></ul>
                        </div>
                        <div style="width: 60%;height: 400px;float: right;overflow-y: auto;">
                            <form id="bankUser_form" method="post" class="form-horizontal" >
                                <%--<div class="form-group col-md-12">--%>
                                    <%--<label class="col-md-3" for="exampleInputName2">所属总部</label>--%>
                                    <%--<div class="col-md-9">--%>
                                        <%--<select class="combobox form-control " id=""  name="" > </select>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">名称</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name=""  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">角色</label>
                                    <div class="col-md-9">
                                        <select class="combobox form-control " id="bankUser_role"  name="" > </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">移动电话</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name=""  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">办公室电话</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name=""  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">工号</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name=""  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">备注</label>
                                    <div class="col-md-9">
                                        <textarea class="form-control" name="remark" id="bankUser_remark"  rows="3"></textarea>
                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>

<script type="text/javascript">

    $(function(){
        bankUser.dataLoad();
    });

    var bankUser = {
        //查询
        dataLoad : function () {
            $("#bankUser_table").bootstrapTable({
                url : 'queryBankUsers',
                method : 'post',// get,post
                toolbar : '#bankUser_Toolbar', // 工具栏
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
                    {field : 'number', title : '工号', width: 100, align : 'left'},
                    {field : 'name', title : '名称', width: 130, align : 'left'},
                    {field : 'bankName', title : '所属银行', width: 130, align : 'left'},
                    {field : 'roleName', title : '角色', width: 130, align : 'left'},
                    {field : 'mobile', title : '移动电话', width: 130, align : 'left'},
                    {field : 'remark', title : '详情', width: 130, align : 'left'},
                    {field : 'id', title : '编号', width: 100, align : 'left',visible : false}
                ]
            });
        },
        //查询参数
        queryParams : function (params) {
            var paramData = {
                pageSize : params.limit,
                pageNum : params.offset,
                search : params.search,
                type : TYPE,
                bankNo : BANKNO
            };
            return paramData;
        },
        //打开新增dialog
        openNew : function () {
            $(".modal-title").html("新增");
            $("#bankUser_modal").modal("show");
        },
        //刷新
        reload : function () {
            $('#bankUser_table').bootstrapTable('refresh');
        },
        //获取所有银行角色
        BankRoles : function(){
            $.ajax({
                url : 'getBankRoles',
                dataType : 'json',
                type : 'post',
                data : {},
                success : function (result) {
                    var data = result.data;
                    if(data.length <= 0){
                        return;
                    }
                    var option = "<option value=''>请选择</option>";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].id+"'>"+data[i].roleName+"</option>";
                    }
                    $("#bankUser_role").html(option);
                },
                error : function(){
                    Ewin.alert("获取银行角色失败");
                }
            });
        },
        //新增
        add : function () {
            
        }
    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>