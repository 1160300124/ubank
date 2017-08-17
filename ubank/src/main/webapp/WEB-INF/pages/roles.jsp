<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.exedit.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap-combotree.js" type="text/javascript"></script>

<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="roles_Toolbar" class="btn-group">
        <button  onclick="RoleFun.openAddRole()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button  onclick="RoleFun.permissionSet()" type="button" class="btn btn-default">
            <span class="fa icon-wrench" aria-hidden="true"></span>权限设置
        </button>
        <button onclick="RoleFun.role_edit()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="RoleFun.role_delete(window.event)" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="RoleFun.role_query()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="role_table" > </table>

    <!-- 角色新增（Modal） -->
    <div id="role_modal" class="modal fade">
        <div class="modal-dialog" style="width: 400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="roles_form" method="post" class="form-horizontal" >
                            <%--<div class="form-group col-md-12">--%>
                                <%--<label class="col-md-2" for="exampleInputName2">角色名</label>--%>
                                <%--<div class="col-md-4">--%>
                                    <%--<input type="text" name="role_name" class="base-form-input base-request" value="">--%>
                                <%--</div>--%>
                                <%--<label class="col-md-2" for="exampleInputName2">所属公司</label>--%>
                                <%--<div class="col-md-4">--%>
                                    <%--<div id="combotree"></div>--%>
                                    <%--<button  onclick="RoleFun.role_add()" type="button" class="btn btn-default">--%>
                                        <%--<span class="fa icon-plus" aria-hidden="true"></span>保存--%>
                                    <%--</button>--%>
                                    <%--<button  onclick="RoleFun.clear()" type="button" class="btn btn-default">--%>
                                        <%--<span class="fa  icon-trash" aria-hidden="true"></span>清空--%>
                                    <%--</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属集团</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control"  name="groupNumber"  id="role_group"></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属公司</label>
                                <div class="col-md-9">
                                    <div id="combotree"></div>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">角色名</label>
                                <div class="col-md-9">
                                    <input type="text" name="role_name" class="base-form-input base-request" value="">
                                </div>
                            </div>

                        </form>
                    </div>
                    <%--<div style="width: 100%;height: 400px;">--%>
                        <%--<div style="width: 50%;height: 100%;border: 1px solid #cccccc;float: left;">--%>
                            <%--<table id="role_table2" > </table>--%>
                        <%--</div>--%>
                        <%--<div style="width: 50%;height: 100%;float: right;border : 1px solid #cccccc;">--%>
                            <%--<ul class="ztree" id="role_tree"></ul>--%>
                        <%--</div>--%>

                    <%--</div>--%>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="RoleFun.role_add()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <%--权限设置（Modal）--%>
    <div id="permission_modal" class="modal fade">
        <div class="modal-dialog" style="width: 700px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" >权限设置</h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <div style="width: 60%;height: 100%;float: left;">
                            <table id="role_table2" > </table>
                        </div>
                        <div style="width: 40%;height: 100%;float: right;">
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

<script src="<%=request.getContextPath()%>/js/roles.js" type="text/javascript"></script>

<%@ include file="/WEB-INF/pages/footer.jsp" %>