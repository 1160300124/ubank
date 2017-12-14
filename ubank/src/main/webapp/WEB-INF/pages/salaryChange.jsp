<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-content">
		<form class="form-horizontal" role="form" id="record_form">
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
                   	  	 <option value="">全部</option>
                   	  </select>
	            </div>
	            
			</div>
			<div class="form-group">
	            
	            <label class="col-sm-1 control-label" for=""></label>
	            <div class="col-sm-8">
	            	<button type="button" class="btn btn-default" id="btn_all" >
	            		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            	</button>
	            	&nbsp;
	            	<button type="button" class="btn btn-default" id="btn_on_the_job" >
	            		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            	</button>
	            	&nbsp;
					<button type="button" class="btn btn-default" id="btn_leave_office">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;离职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</button>
					&nbsp;
					<button type="button" class="btn btn-default" id="btn_salary_set">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;未定薪&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</button>
					&nbsp;
					<button type="button" class="btn btn-default" id="btn_salary_change">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;调薪&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</button>
	            </div>
			</div>
		</form>
		
		<table id="tb_attendance_records" data-toggle="table" data-method="get" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="userName">姓名</th>
				<th data-field="clockDate"  data-width="100px">手机号</th>
				<th data-field="company.name">公司</th>
				<th data-field="dept.deptName">部门</th>
				<th data-field="clockOnDateTime" data-width="100px">入职时间</th>
				<th data-field="clockOnStatus" data-formatter="clockOnStatusFormatter">任职状态</th>
				<th data-field="clockOnLocation">基本工资</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	
</div>
<script src="<%=request.getContextPath()%>/js/salaryChange.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>