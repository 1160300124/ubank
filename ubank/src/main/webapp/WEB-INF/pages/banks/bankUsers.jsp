<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.exedit.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-combotree.js" type="text/javascript"></script>
<div class="page-content">
    <%--工具栏--%>
    <div id="bankUser_Toolbar" class="btn-group">

        <button  onclick="bankUser.openNew()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="bankUser.modify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="bankUser.remove()" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="bankUser.reload()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
        <label class="col-md-1" for="start_date">名称</label>
        <div class="col-sm-2">
            <input class="form-control" id="bank_username" type="text"  name=""/>
        </div>
        <label class="col-md-1" for="start_date">电话</label>
        <div class="col-sm-2">
            <input class="form-control" id="bank_mobile" type="text"  name=""/>
        </div>
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
                        <div style="width: 60%;height: 100%;float: right;overflow-y: auto;">
                            <form id="bankUser_form" method="post" class="form-horizontal" >
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">所属银行</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" disabled="disabled" id="user_bank" name=""  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">名称</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name="name"  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">角色</label>
                                    <div class="col-md-9">
                                        <select class="combobox form-control " id="bankUser_role"  name="roleid" > </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">移动电话</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name="mobile"  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">办公室电话</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name="officePhone"  >
                                    </div>
                                </div>
                                <div class="form-group col-md-12">
                                    <label class="col-md-3" for="exampleInputName2">工号</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control"  name="number"  >
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
                    <button type="button" onclick="bankUser.add()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>

<script type="text/javascript">

    $(function(){
        bankUser.dataLoad();
        bankUser.LoadTree();
        bankUser.listening();
    });

    //初始化tree
    var setting = {
        check: {
            enable: false,
            nocheckInherit: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    var flag = 0;  //标识。 0 表示新增操作，1 表示修改操作
    var id = 0; //员工ID
    var bankNo = 0; //银行编号
    var bankName = ""; //银行名称
    var type = "";      //银行类型

    var bankUser = {
        //弹出框关闭监听事件
        listening : function () {
            $("#bankUser_modal").on('hide.bs.modal',function () {
                $('#bankUser_table').bootstrapTable('uncheckAll');
                $("#bankUser_form")[0].reset();
            });
        },
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
                search : false, //搜索框
                searchText : ' ', //初始化搜索文字
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 10, // 每页的记录行数
                pageList : [10,20,30,40], // 可供选择的每页的行数
                showRefresh : false, //刷新按钮
                showToggle :false,   //切换试图（table/card）按钮
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : 'number', title : '工号', width: 100, align : 'left'},
                    {field : 'name', title : '名称', width: 130, align : 'left'},
                    {field : 'bankName', title : '所属银行', width: 130, align : 'left'},
                    {field : 'roleName', title : '角色', width: 130, align : 'left'},
                    {field : 'mobile', title : '移动电话', width: 130, align : 'left'},
                    {field : 'remark', title : '详情', width: 130, align : 'left'},
                    {field : 'id', title : '编号', width: 100, align : 'left',visible : false},
                    {field : 'roleid', title : '角色ID', width: 100, align : 'left',visible : false},
                    {field : 'type', title : ' 所属银行类型', width: 100, align : 'left',visible : false},
                    {field : 'bankNo', title : ' 所属银行编号', width: 100, align : 'left',visible : false},
                    {field : 'officePhone', title : '办公电话', width: 100, align : 'left',visible : false}
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
                bankNo : BANKNO,
                name : $("#bank_username").val(),
                mobile : $("#bank_mobile").val()
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
        //加载树节点
        LoadTree : function () {
            $.ajax({
                url : 'bankTrees',
                dataType : 'json',
                type : 'post',
                data : {
                    type : TYPE,
                    bankNo : BANKNO
                },
                success : function (data) {
                    $.fn.zTree.init($("#bankUser_tree"), setting, data);
                }
            });
        },
        //新增
        add : function () {
            var treeObj = $.fn.zTree.getZTreeObj("bankUser_tree");
            var nodes = treeObj.getSelectedNodes();
            debugger
            if(nodes.length > 0){
                bankNo = nodes[0].id;
                bankName = nodes[0].name;
                type = nodes[0].type;
            }
            var role = $("#bankUser_role").val();
            var name = $("input[name=name]").val();
            var mobile = $("input[name=mobile]").val();
            var number = $("input[name=number]").val();
            if($.trim(name) == ""){
                Ewin.alert("名称不能为空");
                return;
            } else if(!Validate.regWord(name)){
                Ewin.alert("名称必须是中文");
                return;
            }
            if(bankNo == ""){
                Ewin.alert("所属银行不能为空");
                return;
            }
            if(role == ""){
                Ewin.alert("角色不能为空");
                return;
            }
            if(number == ""){
                Ewin.alert("工号不能为空");
                return;
            }
            if(mobile == ""){
                Ewin.alert("移动电话不能为空");
                return;
            }else if(!Validate.regPhone(mobile)){
                Ewin.alert("电话号码格式不合法，请重新输入");
                return;
            }
            var password = mobile.substr(5,6);
            $.ajax({
                url: 'saveBankUser?flag=' + flag + "&bankName=" + bankName
                            + "&bankNo=" + bankNo + "&type=" + type + "&password= " + password + "&id=" + id,
                dataType : 'json',
                type : 'post',
                data : $("#bankUser_form").serialize(),
                success : function (data) {
                    if(data.code != 5000){
                        Ewin.alert(data.message);
                        $("#bankUser_form")[0].reset();
                        $("#bankUser_modal").modal("hide");
                        $('#bankUser_table').bootstrapTable('refresh');
                    }else{
                        $("#bankUser_form")[0].reset();
                        $("#bankUser_modal").modal("hide");
                        Ewin.alert(data.message);
                        $('#bankUser_table').bootstrapTable('refresh');
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
            var row = $('#bankUser_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            id = row[0].id;
            $("input[name=name]").val(row[0].name);
            $("input[name=mobile]").val(row[0].mobile);
            $("input[name=officePhone]").val(row[0].officePhone);
            $("input[name=number]").val(row[0].number);
            $("#bankUser_remark").val(row[0].remark);
            $("#user_bank").val(row[0].bankName);
            $("#bankUser_modal").modal("show");
            getRoles(row[0].bankNo,row[0].type,row[0].roleid);
            bankNo = row[0].bankNo;
            bankName = row[0].bankName;
            type = row[0].type;
        },
        //删除
        remove : function () {
            var e = window.event;
            e.preventDefault();
            var row = $('#bankUser_table').bootstrapTable('getSelections');
            if(row.length <= 0){
                Ewin.alert("请选中需要删除的数据");
                return;
            }
            Confirm.show('提示', '确定删除该员工吗？', {
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
                            url : 'removeBankUser',
                            dataType : 'json',
                            type : 'post',
                            data:  {
                                "numbers" : numbers
                            },
                            success : function (data) {
                                if(data.code != 5000){
                                    Confirm.hide();
                                    Ewin.alert(data.message);
                                    $('#bankUser_table').bootstrapTable('refresh');
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
    };

    //树节点点击事件
    function zTreeOnClick(event, treeId, treeNode) {
        var type = treeNode.type; //标识。所属部门是总行？分行？支行？
        var id = treeNode.id;     //银行编号
        $("#user_bank").val(treeNode.name);
        var roleId = 0;
        getRoles(id,type,roleId);
    }

    //根据银行类型获取角色
    function getRoles(id,type,roleid) {
        $.ajax({
            url : 'getRoleByType',
            dataType : 'json',
            type : 'post',
            data : {
                type : type,
                id : id
            },
            success : function (data) {
                if(data.length <= 0){
                    return;
                }
                var option = "<option value=''>请选择</option>";
                for (var i = 0; i < data.length; i++){
                    option += "<option value='"+data[i].id+"'>"+data[i].roleName+"</option>";
                }
                $("#bankUser_role").html(option);
                $("#bankUser_role").find("option[value="+ roleid +"]").prop("selected",true);
            },
            error : function () {
                Ewin.alert("获取银行角色失败");
            }
        })
    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>