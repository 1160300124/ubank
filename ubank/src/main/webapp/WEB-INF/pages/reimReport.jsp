<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
    <div class="panel-body" style="padding-bottom:0px;">
        <form id="reimFrom" class="form-horizontal"  >
            <div class="form-group">
                <div class="form-group col-md-12">
                    <label class="col-md-1" for="start_date">集团</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="reim_group" name="groupNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">公司</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="reim_company" name="companyNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">部门</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="reim_dept" name="deptNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">提交人</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="reim_name" type="text"  name="username"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label" for="start_date">提交日期</label>
                <div class="col-sm-2" >
                    <div class="input-group date" >
                        <input class="form-control" id="reimDate1" type="text" name="startDate" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <label class="col-md-1" for="start_date">——</label>
                <div class="col-sm-2" >
                    <div class="input-group date" >
                        <input class="form-control" id="reimDate2" type="text"  name="endDate"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <label class="col-md-1" for="start_date">审批状态</label>
                <div class="col-sm-2">
                    <select class="form-control" id="reim_status" name="status">
                        <option value="">请选择</option>
                        <option value="0">待审批</option>
                        <option value="1">审批通过</option>
                        <option value="2">驳回</option>

                    </select>
                </div>
                <label class="col-md-1" for="start_date">审批结果</label>
                <div class="col-sm-2">
                    <select class="form-control" id="reim_result" name="result">
                        <option value="">请选择</option>
                        <option value="1">审批通过</option>
                        <option value="2">驳回</option>
                    </select>
                </div>
                <button onclick="" type="button" class="btn btn-default">
                    <span class="fa icon-search" aria-hidden="true"></span>查询
                </button>
                <button onclick="" type="button" class="btn btn-default">
                    <span class="fa icon-search" aria-hidden="true"></span>导出
                </button>
            </div>
        </form>
        <%--数据表格--%>
        <table id="reim_table" > </table>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        $('#reimDate1').datetimepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            pickDate: true,
            pickTime: true,
            autoclose: 1,
            todayBtn:  1,
            todayHighlight: 1,
            minView: "month"
        });

        $('#reimDate2').datetimepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            pickDate: true,
            pickTime: true,
            autoclose: 1,
            todayBtn:  1,
            todayHighlight: 1,
            minView: "month"
        });
    });
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>