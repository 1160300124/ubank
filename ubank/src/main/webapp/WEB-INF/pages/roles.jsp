<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.exedit.js" type="text/javascript"></script>

<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="roles_Toolbar" class="btn-group">
        <button  onclick="RoleFun.openAddRole()" type="button" class="btn btn-default">
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
    <table id="role_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="role_modal" class="modal fade">
        <div class="modal-dialog" style="width: 800px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="roles_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-2" for="exampleInputName2">角色名</label>
                                <div class="col-md-4">
                                    <input type="text" name="role_name" class="base-form-input base-request" value="">
                                </div>
                                <label class="col-md-2" for="exampleInputName2">所属公司</label>
                                <div class="col-md-4">
                                    <div id="combotree"></div>
                                    <button  onclick="RoleFun.role_add()" type="button" class="btn btn-default">
                                        <span class="fa icon-plus" aria-hidden="true"></span>新增
                                    </button>
                                </div>
                            </div>

                        </form>
                    </div>
                    <div style="width: 100%;height: 400px;">
                        <div style="width: 50%;height: 100%;border: 1px solid #cccccc;float: left;">
                            <table id="role_table2" > </table>
                        </div>
                        <div style="width: 50%;height: 100%;float: right;border : 1px solid #cccccc;">
                            <ul class="ztree" id="role_tree"></ul>
                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="RoleFun.roleForMenu()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-combotree.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        RoleFun.loadTree();
        RoleFun.role_getComTree();

    });
    var flag = 0; //标识。 0 表示新增操作，1 表示修改操作

    var setting = {
        check: {
            enable: true,
            nocheckInherit: true
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    //arr function
    var RoleFun = {
        //打开新增框
        openAddRole : function () {
            this.rol_roleTable();
            $(".modal-title").html("新增");
            $("#role_modal").modal("show");
        },
        loadTree : function () {
            $.ajax({
                url : 'menuTree',
                dataType : 'json',
                type : 'post',
                data : {},
                success : function (data) {
                    $.fn.zTree.init($("#role_tree"), setting, data);
                }
            });
        },
        //获取所有公司
        role_getComTree : function () {
            $.ajax({
                url : 'getComTree',
                dataType : 'json',
                type : 'post',
                data : {},
                success : function (data) {
                    $("#combotree").bootstrapCombotree({
                        defaultLable : '请选择公司',//默认按钮上的文本
                        data: data,//data应符合实例的data格式
                        showIcon: true,//显示图标
                        showCheckbox: true,//显示复选框
                        width : 400,//下拉列表宽度
                        name : 'list',//combotree值得name，可以用在表单提交
                        maxItemsDisplay : 3,//按钮上最多显示多少项，如果超出这个数目，将会以‘XX项已被选中代替’
                        onCheck : function (node) {//树形菜单被选中是 触发事件

                        }
                    });
                }
            });
        },
        //获取所有角色
        rol_roleTable : function () {
            $("#role_table2").bootstrapTable({
                url : 'roleAllQuery',
                method : 'post',// get,post
                striped : true, // 是否显示行间隔色
                cache : false, // 是否使用缓存，默认为true
                contentType : "application/x-www-form-urlencoded",
                sidePagination : "client", // 分页方式：client客户端分页，server服务端分页
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 10, // 每页的记录行数
                pageList : [10,20,30,40], // 可供选择的每页的行数
                clickToSelect : true,
                singleSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : 'role_id', title : '角色编号', width: 130, align : 'left'},
                    {field : 'role_name', title : '角色名', width: 130, align : 'left'},
                    {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false}
                ],
                onCheck : function (row) {

                }
            });
        },
        //新增角色操作
        role_add : function () {
            var com_numbers = $("#combotree").bootstrapCombotree("getValue");
            var roleName = $("input[name=role_name]").val();
            var numbers = com_numbers.join(",");
            $.ajax({
                url : 'addRole',
                dataType : 'json',
                type : 'post',
                data:  {
                    "com_numbers" : numbers,
                    "roleName" : roleName
                },
                success : function (data) {
                    if(data.code == 300){
                        Ewin.alert(data.message);
                    }else if(data.code == 500){
                        Ewin.alert("操作异常，请联系管理员");
                    }else{
                        Ewin.alert(data.message);
                        RoleFun.rol_roleTable();
                        $("input[name=role_name]").val("");
                        $("#combotree").bootstrapCombotree("setValue","");
                        $('#role_table2').bootstrapTable('refresh');

                    }

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //给角色分配权限
        roleForMenu : function () {
            flag = 0 ;
            var treeObj = $.fn.zTree.getZTreeObj("role_tree");
            var nodes = treeObj.getCheckedNodes(true);
            var row = $('#role_table2').bootstrapTable('getSelections');
            if(nodes.length <= 0){
                Ewin.alert("请权限对应的菜单");
                return;
            }
            if(row.length <= 0){
                Ewin.alert("请选择角色");
                return;
            }
            var ids = "";
            for (var i = 0; i < nodes.length ; i++){
                if(i > 0){
                    ids += "," + nodes[i].id;
                }else{
                    ids += nodes[i].id ;
                }
            }
            $.ajax({
                url : 'settingRoleMenu',
                type : 'post',
                dataType : 'json',
                data :{
                    "menuId" : ids ,
                    "roleId" : row[0].role_id,
                    "flag" : flag
                },
                success :function (data) {
                    if(data.code == 300){
                        Ewin.alert(data.message);
                    }else if(data.code == 500){
                        Ewin.alert("操作异常，请联系管理员");
                    }else{
                        Ewin.alert(data.message);
                        treeObj.checkAllNodes(false);
                        $("#role_modal").modal("hide");

                    }

                },
                error :function () {
                    Ewin.alert("添加权限失败，请联系管理员");
                }
            });
        }
    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>