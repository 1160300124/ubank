<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="employee_Toolbar" class="btn-group">
        <button  onclick="EmployeeFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="EmployeeFun.openModify()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="EmployeeFun.emp_delete(window.event)" id="three-button" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="EmployeeFun.employeeQuery()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="employee_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="employee_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="employee_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属集团</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control"  name="groupNumber"  id="emp_select_group"></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属公司</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control"  name="companyNumber"  id="emp_select"></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属部门</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" name="dept_number"  id="emp_select_dept" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">角色</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" name="role_id" id="emp_select_role"  >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">姓名</label>
                                <div class="col-md-9">
                                    <input type="text" name="userName" class="base-form-input base-request" value="">
                                    <input type="text" name="id" style="display: none;" class="base-form-input base-request" value="1">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">身份证号码</label>
                                <div class="col-md-9">
                                    <input type="text" name="cardNo" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">绑定银行</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" name="bankNo" id="emp_select_bank"  ></select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">银行卡号</label>
                                <div class="col-md-9">
                                    <input type="text" name="bankCardNo" class="base-form-input base-request" value="" >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">预留手机</label>
                                <div class="col-md-9">
                                    <input type="text" name="mobile" class="base-form-input base-request" value="" >
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="EmployeeFun.addEmp() " class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script src="<%=request.getContextPath()%>/js/employee.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>