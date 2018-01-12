<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <form class="form-horizontal" role="form" id="record_form">
        <div class="col-md-12">
            <label class="col-sm-1 control-label">公司</label>
            <div class="col-sm-2">
                <select class="combobox form-control" name="company" id="emp_select_copy"></select>
            </div>
            <button onclick="EmployeeFun.reload()" type="button" class="btn btn-default">
                <span class="fa icon-search" aria-hidden="true"></span>搜索
            </button>
            <button onclick="EmployeeFun.activetion()" type="button" class="btn btn-default">
                已激活
            </button>
            <button onclick="EmployeeFun.inactivated()" type="button" class="btn btn-default">
                未激活
            </button>

        </div>
    </form>
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
        <button onclick="EmployeeFun.reload()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
        <button onclick="EmployeeFun.showImport()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>批量导入
        </button>
    </div>

    <%--数据表格--%>
    <table id="employee_table" ></table>

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
                                    <input type="text" name="id"  style="display: none;" class="base-form-input base-request" value="1">
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">身份证号码</label>
                                <div class="col-md-9">
                                    <input type="text" name="cardNo" class="base-form-input base-request" value="">
                                </div>
                            </div>
                            <%--<div class="form-group col-md-12">--%>
                            <%--<label class="col-md-3" for="exampleInputName2">绑定银行</label>--%>
                            <%--<div class="col-md-9">--%>
                            <%--<select class="combobox form-control" name="bankNo" id="emp_select_bank"  ></select>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group col-md-12">--%>
                            <%--<label class="col-md-3" for="exampleInputName2">银行卡号</label>--%>
                            <%--<div class="col-md-9">--%>
                            <%--<input type="text" name="bankCardNo" class="base-form-input base-request" value="" >--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">预留手机</label>
                                <div class="col-md-9">
                                    <input type="text" name="mobile" class="base-form-input base-request" value="" >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">入职时间</label>
                                <div class="col-md-9">
                                    <div class="input-group date" id="">
                                        <input class="form-control" id="entryDate" type="text" name="entryDate" />
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">离职时间</label>
                                <div class="col-md-9">
                                    <div class="input-group date" id="">
                                        <input class="form-control" id="leaveDate" type="text" name="leaveDate" />
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" onclick="EmployeeFun.addEmp() " class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <!-- 弹出框（Modal） -->
    <div id="employee_import_modal" class="modal fade">
        <div class="modal-dialog" style="min-width:750px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" >批量添加员工</h4>
                </div>
                <div class="modal-body" style="padding: 20px;">
                    <div class="import-step import-step-one">
                        <h3 class="align-center">
                            <span class="import-company-name"></span>公司员工导入
                        </h3>
                        <a class="file-upload-button" href="javascript:;" target="_blank">
                            选择文件
                            <input id="employee_upload_file" type="file" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" name="file"/>
                        </a>
                        <div class="import-file-name">

                        </div>
                        <div class="import-tip" style="text-align:center;">（请<a href="../model/model.xls">下载模板</a>文件，上传说明<span class="import-help">?</span>）</div>
                    </div>
                    <div class="import-step import-step-two" style="display:none;">
                        <h3 class="align-center">
                            <span class="import-company-name"></span>公司员工导入
                        </h3>
                        <img src="../images/loading.gif" alt="">
                        <br/>
                        正在导入员工请稍等，马上好。
                    </div>
                    <div class="import-step import-step-success" style="display:none;">
                        <i class="glyphicon glyphicon-ok-circle"></i>
                    </div>
                    <div class="import-step import-step-edit" style="display:none;">
                        <div class="align-center" style="position: relative;height:40px;line-height:40px;">
                            <a class="file-upload-button" href="javascript:;" target="_blank" style="margin: 0px 0px 10px 0px;position:absolute;left:0px;top:0px;">
                                重新选择
                                <input id="employee_upload_file_rechoose" type="file" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" name="file"/>
                            </a>
                            <span style="font-size:25px;font-weight:700;"><span class="import-company-name"></span>公司员工导入</span>
                        </div>
                        <div class="align-center" style="position: relative;height:30px;line-height:30px;">
                            <div style="position:absolute;left:0px;" id="importFileName">（未知.xlsx）</div>
                            <span style="color:#666;" id="importResult">一共识别10条，其中成功8条，失败3条</span>
                        </div>
                        <div>
                            <table id="updat_import_table" class="import-result"></table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="text-align:center;">
                    <span class="import-modal-btns complete-btns" style="display:none;">
                        <button type="button" onclick="EmployeeFun.initImportModal()" class="btn btn-primary" data-dismiss="modal">完成</button>
                    </span>
                    <span class="import-modal-btns import-btns">
                        <button type="button" onclick="EmployeeFun.import()" id="import_btn" class="btn btn-primary disabled" disabled="disabled">确认添加</button>
                        <button type="button" onclick="EmployeeFun.initImportModal()" class="btn btn-default" data-dismiss="modal">取消</button>
                    </span>
                    <span class="import-modal-btns update-import-btns" style="display:none;">
                        <button type="button" onclick="EmployeeFun.updateImport()" id="update_import_btn" class="btn btn-primary">填好了，导入！</button>
                        <button type="button" onclick="EmployeeFun.initImportModal()" class="btn btn-default" data-dismiss="modal">取消</button>
                    </span>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<style>
    .exists{
        background-color: #a1a2a1 !important;
        color: #fff;
    }

    .exists-info{
        color: #a1a2a1;
    }

    .import-result td:last-child{
        background-color: #fff;
    }

    .error-info-exists-tip{
        background-color: #a1a2a1;
        color: #fff;
        padding: 10px;
        position: absolute;
        z-index: 9999;
        transform: translateY(-50%);
    }

    .error-info-exists-tip::before{
        content: '';
        height: 0px;
        width: 0px;
        border: 10px solid transparent;
        border-right: 10px solid #a1a2a1;
        position: absolute;
        left: -20px;
        top: 50%;
        transform: translateY(-50%);
    }
</style>
<script src="<%=request.getContextPath()%>/js/employee.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>