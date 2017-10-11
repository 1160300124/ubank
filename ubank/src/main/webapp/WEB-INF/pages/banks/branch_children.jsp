<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<div class="page-content">
    <%--工具栏--%>
    <div id="branchs_chil_Toolbar" class="btn-group">
        <button  onclick="" type="button" class="btn btn-default">
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
    <table id="branchs_chil_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="branchs_chil_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="branchs_chil_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属总部</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control"  name="" > </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属分部</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control"  name="" > </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">支部名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="name"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">备注</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" name="remark"  rows="3"></textarea>
                                </div>
                            </div>
                        </form>
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

    });
    var flag = 0;  //标识。 0 表示新增操作，1 表示修改操作
    var id = 0; //银行ID

    var branchs_chil_fun = {
        dataLoad : function () {
            $("#branchs_chil_table").bootstrapTable({
                url : 'queryBranchsChil',
                method : 'post',// get,post
                toolbar : '#branchs_chil_Toolbar', // 工具栏
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
                    {field : 'id', title : '编号', width: 100, align : 'left'},
                    {field : 'name', title : '支行名称', width: 130, align : 'left'},
                    {field : 'count', title : '业务员数量', width: 130, align : 'left'},
                    {field : 'bankName', title : '上级部门', width: 130, align : 'left'}
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
    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>
