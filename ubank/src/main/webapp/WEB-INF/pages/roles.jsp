<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
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
                                    <input type="text" name="userName" class="base-form-input base-request" value="">
                                </div>
                                <label class="col-md-2" for="exampleInputName2">所属公司</label>
                                <div class="col-md-4">
                                    <div id="combotree"></div>
                                    <button  onclick="" type="button" class="btn btn-default">
                                        <span class="fa icon-plus" aria-hidden="true"></span>新增
                                    </button>
                                </div>
                            </div>

                        </form>
                    </div>
                    <div style="width: 100%;height: 600px;">
                        <div style="width: 50%;height: 100%;border: 1px solid;float: left;">

                        </div>
                        <div style="width: 50%;height: 100%;float: right;border: 1px solid;">
                            <div id="role_tree"></div>
                        </div>

                    </div>


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick=" " class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>

<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-combotree.js" type="text/javascript"></script>s
<script type="text/javascript">


    $(function () {
        var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
        $("#combotree").combotree({
            defaultLable : '请选择列表',//默认按钮上的文本
            data: "",//data应符合实例的data格式
            showIcon: true,//显示图标
            showCheckbox: true,//显示复选框
            width : 400,//下拉列表宽度
            name : 'list',//combotree值得name，可以用在表单提交
            maxItemsDisplay : 3,//按钮上最多显示多少项，如果超出这个数目，将会以‘XX项已被选中代替’
            onCheck : function (node) {//树形菜单被选中是 触发事件
                debugger
            }
        });

        RoleFun.loadTree();
    });

    //arr function
    var RoleFun = {
        //打开新增框
        openAddRole : function () {
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
                    $("#role_tree").treeview({
                        data : data,
                        showCheckbox: true, //是否显示复选框
                        highlightSelected: true, //是否高亮选中
                        //nodeIcon: 'glyphicon glyphicon-globe',
                        emptyIcon: '', //没有子节点的节点图标
                        multiSelect: false, //多选
                        onNodeChecked: function (event,data) {
                            debugger;
                            alert(data.nodeId);
                        }
                    });
                }
            });
        }
    }



</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>