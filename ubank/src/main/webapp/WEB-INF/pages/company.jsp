<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="company_Toolbar" class="btn-group">
        <button  onclick="CompanyFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="CompanyFun.openEdit()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="CompanyFun.deleteCompany(window.event)" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="CompanyFun.reload()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="company_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="company_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="company_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属集团</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="group_select"  name="group_num" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">公司名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="companyName" name="name"  >
                                    <input type="text" class="form-control" style="display: none;" id="allAccount" name="account"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">公司法人</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="legalPerson"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">详情</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" id="com_area" name="details"  rows="3"></textarea>
                                </div>
                            </div>
                            <div class="form-group col-md-12">

                                <label class="col-md-3" for="exampleInputName2">公司账户</label>
                                <div class="col-md-9">
                                    <%--<button  onclick="" type="button" class="btn btn-default" data-click="addForm">--%>
                                        <%--<span class="fa icon-plus" aria-hidden="true"></span>添加账号--%>
                                    <%--</button>--%>
                                </div>
                                <div class="col-md-offset-1 col-md-11">
                                    <div class="form-box"></div>
                                    <div class="add-form-item clone-form" style="">
                                        <span class="toggle-form" data-click="toggleForm">展开</span>
                                        <div class="base-right-btn">
                                            <span class="fl edit-form" data-click="editForm">编辑</span>
                                            <span class="fl delete-form" data-click="deleteForm">删除</span>
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户银行</span>
                                            <%--<input type="text" name="com_bank" class="base-form-input" placeholder="" value="">--%>
                                            <select class="combobox form-control base-form-input base-request" id="company_select" name="bankNo" >
                                            </select>
                                            <i class="base-form-select-no"></i>
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户号</span>
                                            <input type="text" name="accounts" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户客户号</span>
                                            <input type="text" name="customer" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">证书序号</span>
                                            <input type="text" name="certificateNumber" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">银行数字证书授权码</span>
                                            <input type="text" name="authorizationCode" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-offset-label">
                                            <button type="button" class="base-sure-btn" data-click="sureForm">确定</button>
                                            <button type="button" class="base-cancel-btn" data-click="deleteForm">取消</button>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="CompanyFun.companyAdd()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script src="<%=request.getContextPath()%>/js/company.js" type="text/javascript"></script>

<%@ include file="/WEB-INF/pages/footer.jsp" %>