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
                   	  	 <option value="">请选择部门</option>
                   	  </select>
	            </div>
	            
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" for="start_date">开始日期</label>
	            <div class="col-sm-2" >
	            	<div class="input-group date time-picker" id="datetimepicker_start">
		                <input class="form-control" id="start_date" type="text" placeholder="请选择开始日期"/>
		            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
	            	</div>
	            </div>
	            
	            <label class="col-sm-1 control-label" for="end_date">结束日期</label>
		        <div class="col-sm-2">
		            <div class="input-group date time-picker" id="datetimepicker_end">
		               <input class="form-control" id="end_date" type="text" placeholder="请选择结束日期" />
		               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		            </div>
	            </div>
	            
	            <label class="col-sm-1 control-label" for=""></label>
	            <div class="col-sm-2">
	            	<button type="button" class="btn btn-default" id="btn_search" >
	            		<span class="fa icon-search" aria-hidden="true"></span>查询
	            	</button>
	            	<button type="button" class="btn btn-default" id="btn_export" >
	            		<span class="fa icon-download-alt" aria-hidden="true"></span>导出
	            	</button>
					<button id="btn_delete" type="button" class="btn btn-default">
						<span class="fa icon-remove" aria-hidden="true"></span>删除
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
				<th data-field="company.name">公司</th>
				<th data-field="dept.deptName">部门</th>
				<th data-field="clockDate"  data-width="100px">打卡日期</th>
				<th data-field="clockOnDateTime" data-width="140px">上班时间</th>
				<th data-field="clockOnStatus" data-formatter="clockOnStatusFormatter">上班状态</th>
				<th data-field="clockOnLocation">上班打卡位置</th>
				<th data-field="clockOnDevice">上班打卡设备号</th>
				<th data-field="clockOffDateTime"  data-width="140px">下班时间</th>
				<th data-field="clockOffStatus" data-formatter="clockOffStatusFormatter">下班状态</th>
				<th data-field="clockOffLocation">下班打卡位置</th>
				<th data-field="clockOffDevice">下班打卡设备号</th>
			</tr>
			</thead>

		</table>
	
</div>
<script src="<%=request.getContextPath()%>/js/attendance.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>