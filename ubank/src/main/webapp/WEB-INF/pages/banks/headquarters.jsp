<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<div class="page-content">
    <%--工具栏--%>
    <div id="headquarters_Toolbar" class="btn-group">
        <button  onclick="headquartersFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="headquartersFun.modify()" type="button" class="btn btn-default">
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
    <table id="headquarters_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="headquarters_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="headquarters_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">总部名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="bankName"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">备注</label>
                                <div class="col-md-9">
                                    <textarea class="form-control area" name="remark"  rows="3"></textarea>
                                </div>
                            </div>
                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="headquartersFun.save()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>

<script type="text/javascript">

    $(function () {
        headquartersFun.listening();
        headquartersFun.dataLoad();
    });

    var flag = 0;  //标识。 0 表示新增操作，1 表示修改操作
    // all function
    var headquartersFun = {
        //modal关闭监听事件
        listening : function () {
            $("#headquarters_modal").on('hide.bs.modal',function () {
                $('#headquarters_table').bootstrapTable('uncheckAll');
                $("#headquarters_form")[0].reset();
            });
        },
        dataLoad : function () {
            $("#headquarters_table").bootstrapTable({
                url : 'queryHeadquarters',
                method : 'post',// get,post
                toolbar : '#headquarters_Toolbar', // 工具栏
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
                    {field : 'bankName', title : '银行', width: 130, align : 'left'},
                    {field : 'remark', title : '备注', width: 130, align : 'left'}
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
        //打开新增dialog
        openAdd : function () {
            $(".modal-title").html("新增");
            $("#headquarters_modal").modal("show");

        },
        //新增
        save : function () {
            var bankName = $("input[name=bankName]").val();
            var reason = $(".area").val();
            if(bankName == ""){
                Ewin.alert("名称不能为空");
                return;
            }
            if(!Validate.regWord(bankName)){
                Ewin.alert("名称必须是中文");
                return;
            }
            var row = $('#headquarters_table').bootstrapTable('getSelections');
            $.ajax({
                url : 'addHeadquarters?flag=' + flag + "&bankNo= " + row[0].id,
                dataType : 'json',
                type : 'post',
                data : $("#headquarters_form").serialize(),
                success : function (data) {
                    if(data.code != 5000){
                        Ewin.alert(data.message);
                        $("#headquarters_modal").modal("hide");
                        $('#headquarters_table').bootstrapTable('refresh');
                    }else{
                        $("#headquarters_form")[0].reset();
                        $("#headquarters_modal").modal("hide");
                        Ewin.alert(data.message);
                        $('#headquarters_table').bootstrapTable('refresh');
                    }
                },
                error : function () {
                    Ewin.alert("新增失败");
                }
            })
        },
        //修改
        modify : function () {
            flag = 1;
            $(".modal-title").html("修改");
            var row = $('#headquarters_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            $("input[name=bankName]").val(row[0].bankName);
            $(".area").val(row[0].remark);
            $("#headquarters_modal").modal("show");
        },
        //删除
        remove : function () {
            var e = window.event;
            e.preventDefault();
            var row = $('#headquarters_table').bootstrapTable('getSelections');
            if(row.length <= 0){
                Ewin.alert("请选中需要删除的数据");
                return;
            }
            Confirm.show('提示', '确定删除该银行吗？', {
                'Delete': {
                    'primary': true,
                    'callback': function() {
                        var numbers = ""; //存放多个银行编号
                        for (var i = 0 ; i < row.length ;i++){
                            if(i > 0 ){
                                numbers += "," + row[i].id;
                            }else{
                                numbers += row[i].id;
                            }
                        }
                        $.ajax({
                            url : 'removeHeadquarters',
                            dataType : 'json',
                            type : 'post',
                            data:  {
                                "numbers" : numbers
                            },
                            success : function (data) {
                                if(data.code != 5000){
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                    $('#headquarters_table').bootstrapTable('refresh');
                                }else{
                                    Confirm.hide();
                                    Ewin.alert(data.message);
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
    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>