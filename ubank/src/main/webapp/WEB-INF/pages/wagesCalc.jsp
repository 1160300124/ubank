<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="wages_calc_Toolbar" class="btn-group">
        <button  onclick="WagesCalcFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="WagesCalcFun.openEdit()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="WagesCalcFun.deleteCompany(window.event)" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="WagesCalcFun.companyQuery()" type="button" class="btn btn-default">
            <span class="fa icon-upload" aria-hidden="true"></span>上传
        </button>
    </div>

    <%--数据表格--%>
    <table id="wages_calc_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="wages_calc_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="company_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">工资计算模板名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="companyName" name="name"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">选择公司</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="group_select"  name="group_num" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">考勤规则</label>
                                <div class="col-md-9">
                                    <a href="javascript:;">配置</a>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">请假规则</label>
                                <div class="col-md-9">
                                    <a href="javascript:;">配置</a>
                                </div>
                            </div>
                            <div class="form-group col-md-12">

                                <label class="col-md-3" for="exampleInputName2">加班规则</label>
                                <div class="col-md-9">
                                    <input type="checkbox">按加班时长增加加班费
                                </div>

                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">社保缴纳金额</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="companyName" placeholder="请输入金额" name="name"  >
                                </div>
                            </div>

                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">个税起征点</label>
                                <div class="col-md-9">
                                    <input type="radio" name="tax" style="width:15px;">3500(国籍)&nbsp;&nbsp;&nbsp;<a href="javascript:;">个税表</a><br>

                                    <input type="radio" name="tax" style="width:15px;">4500(外籍)
                                </div>
                            </div>
                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="CompanyFun.wagesCalcAdd()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script src="<%=request.getContextPath()%>/js/wagesCalc.js" type="text/javascript"></script>

<%@ include file="/WEB-INF/pages/footer.jsp" %>