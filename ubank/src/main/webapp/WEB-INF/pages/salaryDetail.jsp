<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
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
		<button id="editBtn" type="button" class="btn btn-default">
			<span class="fa icon-download-alt" aria-hidden="true"></span>编辑
		</button>
		<button id="saveBtn" type="button" class="btn btn-default">
			<span class="fa icon-download-alt" aria-hidden="true"></span>保存
		</button>
	</div>

	<table id="tb_saraly_details" data-toggle="table" data-url="getSalaryDetails" data-method="get" data-toolbar="#toolbar" data-striped="true"
		   data-pagination="true" data-side-pagination="server" data-search="true" data-show-columns="true"
		   data-page-size="10" data-page-list="[10,15,20]">
 		 <thead>
		<tr>
			<th data-field="userName">姓名</th>
			<th data-field="cardNo" data-edit="{false}">身份证号码</th>
			<th data-field="pre_tax_salaries">税前工资</th>
			<th data-field="bonuses">奖金</th>
			<th data-field="subsidies">补贴</th>
			<th data-field="attendance_cut_payment">考勤扣款</th>
			<th data-field="askForLeave_cut_payment">请假扣款</th>
			<th data-field="overtime_payment">加班费</th>
			<th data-field="socialInsurance">社保缴纳</th>
			<th data-field="publicAccumulationFunds">公积金</th>
			<th data-field="taxThreshold">个税起征点</th>
			<th data-field="personalIncomeTax">个人所得税</th>
			<th data-field="else_cut_payment">其他扣款</th>
			<th data-field="salaries">应发工资</th>
		</tr>
		</thead> 

	</table>

</div>
<script src="<%=request.getContextPath()%>/js/salaryConfig.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>