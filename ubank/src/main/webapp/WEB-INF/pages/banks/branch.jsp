<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<div class="page-content">
    <%--工具栏--%>
    <div id="branchs_Toolbar" class="btn-group">
        <button  onclick="branchsFun.openNew()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="branchsFun.modify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="branchsFun.remove()" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="branchsFun.reload()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="branch_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="branch_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="branch_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">总部名称</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="branchs_headquarters"  name="headquartersNo" > </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">分部名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control"  name="name"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">备注</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" name="remark"  id="branchs_remark" rows="3"></textarea>
                                </div>
                            </div>
                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="branchsFun.add()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>

<script type="text/javascript">

    $(function () {
        branchsFun.getAllHead();
        branchsFun.query();
    });

    var flag = 0;  //标识。 0 表示新增操作，1 表示修改操作
    var id = 0; //分行ID

    var branchsFun = {
        //查询
        query : function () {
            $("#branch_table").bootstrapTable({
                url : 'queryBranchs',
                method : 'post',// get,post
                toolbar : '#branchs_Toolbar', // 工具栏
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
                    {field : 'name', title : '分行名称', width: 130, align : 'left'},
                    {field : 'count', title : '业务员数量', width: 130, align : 'left'},
                    {field : 'headquarters', title : '所属总部', width: 130, align : 'left'},
                    {field : 'headquartersNo', title : '总部ID', width: 130, align : 'left',visible : false}
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
            $("#branch_modal").modal("show");

        },
        getAllHead : function () {
            //获取所有总部
             $.ajax({
                url : 'getAllHeadquarters',
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
                        option += "<option value='"+data[i].id+"'>"+data[i].bankName+"</option>";
                    }
                    $("#branchs_headquarters").html(option);
                },
                error : function(){
                     Ewin.alert("获取总部失败");
                }
             });
        },
        //刷新
        reload : function () {
            $('#branch_table').bootstrapTable('refresh');
        },
        //新增
        add : function () {
            var head = $("#branchs_headquarters").val();
            var name = $("input[name = name]").val();
            var remark = $("#branchs_remark").val();
            if(head == ""){
                Ewin.alert("总行不能为空");
                return;
            }
            if($.trim(name) == "" ){
                Ewin.alert("名称不能为空");
                return;
            }
            if(!Validate.regWord(name)){
                Ewin.alert("名称必须是中文");
                return;
            }
            $.ajax({
                url : 'saveBranchs?flag=' + flag + "&id=" + id,
                dataType : 'json',
                type : 'post',
                data : $("#branch_form").serialize(),
                success : function (data) {
                    if(data.code != 5000){
                        Ewin.alert(data.message);
                        $("#branch_modal").modal("hide");
                        $('#branch_table').bootstrapTable('refresh');
                    }else{
                        $("#branch_form")[0].reset();
                        $("#branch_modal").modal("hide");
                        Ewin.alert(data.message);
                        $('#branch_table').bootstrapTable('refresh');
                    }
                },
                error : function () {
                    Ewin.alert("新增失败");
                }
            })
        },
        //修改
        modify : function(){
            flag = 1;
            $(".modal-title").html("修改");
            var row = $('#branch_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            id = row[0].id;
            $("#branchs_headquarters").find("option[value="+row[0].headquartersNo+"]").prop("selected","selected");
            $("input[name=name]").val(row[0].name);
            $("#branchs_remark").val(row[0].remark);
            $("#branch_modal").modal("show");
        },
        //删除
        remove : function () {
            var e = window.event;
            e.preventDefault();
            var row = $('#branch_table').bootstrapTable('getSelections');
            if(row.length <= 0){
                Ewin.alert("请选中需要删除的数据");
                return;
            }
            Confirm.show('提示', '确定删除该分行吗？', {
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
                            url : 'removeBranchs',
                            dataType : 'json',
                            type : 'post',
                            data:  {
                                "numbers" : numbers
                            },
                            success : function (data) {
                                if(data.code != 5000){
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                    $('#branch_table').bootstrapTable('refresh');
                                }else{
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                }

                            },
                            error : function () {
                                Ewin.alert("删除失败");
                            }
                        })

                    }
                }
            });
        }

    }

</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>