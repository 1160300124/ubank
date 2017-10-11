<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<div id="toolbar" class="btn-group">
			<button id="btn_add" type="button" class="btn btn-default">
                <span class="fa icon-plus" aria-hidden="true"></span>新增
            </button>
            <button id="btn_edit" type="button" class="btn btn-default">
                <span class="fa icon-edit" aria-hidden="true"></span>修改
            </button>
			<button id="btn_delete" type="button" class="btn btn-default">
				<span class="fa icon-remove" aria-hidden="true"></span>删除
			</button>
			<button id="btn_import" type="button" class="btn btn-default">
				<span class="fa icon-download-alt" aria-hidden="true"></span>导出
			</button>

		</div>

		<table id="tb_saraly_records" data-toggle="table" data-url="getManagement" data-method="get" data-toolbar="#toolbar" data-striped="true"
			   data-pagination="true" data-side-pagination="server" data-search="true" data-show-columns="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="">姓名</th>
				<th data-field="">身份证号码</th>
				<th data-field="">税前工资</th>
				<th data-field="">奖金</th>
				<th data-field="">补贴</th>
				<th data-field="">考勤扣款</th>
				<th data-field="">请假扣款</th>
				<th data-field="">发放人数</th>
				<th data-field="">加班费</th>
				<th data-field="">社保缴纳</th>
				<th data-field="">公积金</th>
				<th data-field="">个税起征点</th>
				<th data-field="">个人所得税</th>
				<th data-field="">其他扣款</th>
				<th data-field="">应发工资</th>
			</tr>
			</thead>

		</table>
	</div>


</div>
<script src="<%=request.getContextPath()%>/js/management.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>