<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<form class="form-horizontal" role="form" id="statistics_form">
			<div class="form-group">
				<label class="col-sm-1 control-label" for="company">公司</label>
	            <div class="col-sm-2">
                   	  <select class="form-control" id="company_select">
                   	  	 <option value="">请选择公司</option>
                   	  </select>
	            </div>
	            <label class="col-sm-1 control-label" for="dept">部门</label>
	            <div class="col-sm-2 ">
	                  <select class="form-control" id="dept_select">
                   	  	 <option value="">请选择部门</option>
                   	  </select>
	            </div>
	            <label class="col-sm-1 control-label" for="user">员工</label>
	            <div class="col-sm-2">
	               <input class="form-control" id="user_name" type="text" placeholder="请输入员工名称"/>
	            </div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" for="url_name">开始日期</label>
	            <div class="col-sm-2" >
	            	<div class="input-group time-picker date" id="datetimepicker1">
		                <input class="form-control" id="start_date" type="text" placeholder="请输入开始日期"/>
		            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
	            	</div>
	            </div>
	            <label class="col-sm-1 control-label" for="url_name">结束日期</label>
		            <div class="col-sm-2">
		            <div class="input-group time-picker date" id="datetimepicker2">
		               <input class="form-control" id="end_date" type="text" placeholder="请输入结束日期" />
		               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		            </div>
	            </div>
	            <label class="col-sm-1 control-label" for="url_name"></label>
	            <div class="col-sm-2">
	            	<button type="button" class="btn btn-default" id="btn_search" >
	            		<span class="fa icon-search" aria-hidden="true"></span>查询
	            	</button>
	            	<button type="button" class="btn btn-default" id="btn_export" >
	            		<span class="fa icon-download-alt" aria-hidden="true"></span>导出
	            	</button>
	            </div>
			</div>
		</form>
		
		<table id="tb_statistics" data-toggle="table" data-url="" data-method="get" data-toolbar="#toolbar" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="userName">员工</th>
				<th data-field="company.name">公司</th>
				<th data-field="dept.deptName">部门</th>
				<th data-field="workdaysCount">应出勤天数</th>
				<th data-field="normalClockOnCount">正常上班打卡(次)</th>
				<th data-field="laterCount">迟到(次)</th>
				<th data-field="noClockOnCount">上班未打卡(次)</th>
				<th data-field="normalClockOffCount">正常下班打卡(次)</th>
				<th data-field="leaveEarlyCount">早退(次)</th>
				<th data-field="noClockOffCount">下班未打卡(次)</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	</div>
	
	
	
</div>
<script src="<%=request.getContextPath()%>/js/statistics.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>