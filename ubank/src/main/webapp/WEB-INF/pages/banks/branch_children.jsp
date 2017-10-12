<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<div class="page-content">
    <%--工具栏--%>
    <div id="branchs_chil_Toolbar" class="btn-group">
        <button  onclick="branchs_chil_fun.openNew()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="branchs_chil_fun.chil_modify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="branchs_chil_fun.chil_remove()" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="branchs_chil_fun.reload()" type="button" class="btn btn-default">
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
                                    <select class="combobox form-control " id="chil_headquarters"  name="" > </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属分部</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control " id="chil_branchs" name="" > </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">支部名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control"  name="name"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">备注</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" name="remark" id="chil_remark"  rows="3"></textarea>
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
        branchs_chil_fun.dataLoad();
        branchs_chil_fun.getAllHead();
        branchs_chil_fun.getAllBranchs();
    });

    var flag = 0;  //标识。 0 表示新增操作，1 表示修改操作
    var id = 0; //支行ID

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
                    {field : 'bankName', title : '上级银行', width: 130, align : 'left'},
                    {field : 'bankNo', title : '银行编号', width: 130, align : 'left',visible : false}
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
            $("#branchs_chil_modal").modal("show");
        },
        //获取所有总部
        getAllHead : function () {
            $.ajax({
                url : 'getAllHeadquarters',
                dataType : 'json',
                type : 'post',
                data : {
                    bankNo : BANKNO
                },
                success : function (result) {
                    var data = result.data;
                    if(data.length <= 0){
                        return;
                    }
                    var option = "<option value=''>请选择</option>";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].id+"'>"+data[i].bankName+"</option>";
                    }
                    $("#chil_headquarters").html(option);
                },
                error : function(){
                    Ewin.alert("获取总部失败");
                }
            });
        },
        //获取所有分部
        getAllBranchs : function () {
            $.ajax({
                url : 'getAllBranchs',
                dataType : 'json',
                type : 'post',
                data : {
                    bankNo : BANKNO
                },
                success : function (result) {
                    var data = result.data;
                    if(data.length <= 0){
                        return;
                    }
                    var option = "<option value=''>请选择</option>";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                    $("#chil_branchs").html(option);
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
        chil_add  : function(){
            var head = $("#chil_headquarters").val();
            var branchs = $("#chil_branchs").val();
            var name = $("input[name = name]").val();
            var headTxt = $("#chil_headquarters").find("option:selected").text();
            var branchsTxt = $("#chil_branchs").find("option:selected").text();
            if(head == "" && branchs == ""){
                Ewin.alert("部门不能为空");
                return;
            }
            if(head != "" && branchs != ""){
                Ewin.alert("只能选择一个部门");
                return;
            }
            if($.trim(name) == ""){
                Ewin.alert("支部名称不能为空");
                return;
            }
            if(!Validate.regWord(name)){
                Ewin.alert("名称必须是中文");
                return;
            }
            var type = null;   //银行类型
            var bankName = "";  //银行名
            var bankNo = null;  //银行编号
            //判断当前选中的银行是总部还是分部
            if(head != ""){
                type = 0;
                bankName = headTxt;
                bankNo = head;
            }else if(branchs != ""){
                type = 1;
                bankName = branchsTxt;
                bankNo = branchs;
            }
            $.ajax({
                url : 'saveBranchsChil?flag=' + flag + "&bankName=" + bankName + "&type=" + type + "&bankNo=" + bankNo + "&id=" + id,
                dataType : 'json',
                type : 'post',
                data : $("#branchs_chil_form").serialize(),
                success : function (data) {
                    if(data.code != 5000){
                        Ewin.alert(data.message);
                        $("#branchs_chil_form")[0].reset();
                        $("#branchs_chil_modal").modal("hide");
                        $('#branchs_chil_table').bootstrapTable('refresh');
                    }else{
                        $("#branchs_chil_form")[0].reset();
                        $("#branchs_chil_modal").modal("hide");
                        Ewin.alert(data.message);
                        $('#branchs_chil_table').bootstrapTable('refresh');
                    }
                },
                error : function () {
                    Ewin.alert("新增失败");
                }
            })

        },
        //修改
        chil_modify : function(){
            flag = 1;
            $(".modal-title").html("修改");
            var row = $('#branchs_chil_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            id = row[0].id;
            $("#chil_headquarters").find("option[value="+row[0].bankNo+"]").prop("selected","selected");
            $("#chil_branchs").find("option[value="+row[0].bankNo+"]").prop("selected","selected");
            $("input[name=name]").val(row[0].name);
            $("#chil_remark").val(row[0].remark);
            $("#branchs_chil_modal").modal("show");

        },
        //删除
        chil_remove : function(){
            var e = window.event;
            e.preventDefault();
            var row = $('#branchs_chil_table').bootstrapTable('getSelections');
            if(row.length <= 0){
                Ewin.alert("请选中需要删除的数据");
                return;
            }
            Confirm.show('提示', '确定删除该支行吗？', {
                'Delete': {
                    'primary': true,
                    'callback': function() {
                        var numbers = ""; //存放多个支银编号
                        for (var i = 0 ; i < row.length ;i++){
                            if(i > 0 ){
                                numbers += "," + row[i].id;
                            }else{
                                numbers += row[i].id;
                            }
                        }
                        $.ajax({
                            url : 'removeBranchsChild',
                            dataType : 'json',
                            type : 'post',
                            data:  {
                                "numbers" : numbers
                            },
                            success : function (data) {
                                if(data.code != 5000){
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                    $('#branchs_chil_table').bootstrapTable('refresh');
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
