<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">

	<table id="tb_saraly_details" data-toggle="table" data-url="getSalaryDetails?sid=${param.sid}" data-method="get" data-toolbar="#toolbar" data-striped="true"
		   data-pagination="true" data-side-pagination="server" data-search="true" data-show-columns="true"
		   data-page-size="10" data-page-list="[10,15,20]">
 		 <thead>
		<tr>
			<th data-field="userName">姓名</th>
			<th data-field="cardNo">身份证号码</th>
			<th data-field="preTaxSalaries">税前工资</th>
			<th data-field="bonuses">奖金</th>
			<th data-field="subsidies">补贴</th>
			<th data-field="totalCutPayment">考勤扣款</th>
			<th data-field="askForLeaveCutPayment">请假扣款</th>
			<th data-field="overtimePayment">加班费</th>
			<th data-field="socialInsurance">社保缴纳</th>
			<th data-field="publicAccumulationFunds">公积金</th>
			<th data-field="taxThreshold">个税起征点</th>
			<th data-field="personalIncomeTax">个人所得税</th>
			<th data-field="elseCutPayment">其他扣款</th>
			<th data-field="salaries">应发工资</th>
		</tr>
		</thead> 

	</table>

</div>
<%@ include file="/WEB-INF/pages/footer.jsp" %>