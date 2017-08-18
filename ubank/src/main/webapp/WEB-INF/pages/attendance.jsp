<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<form class="form-horizontal" role="form" id="record_form">
			<div class="form-group">
				<label class="col-sm-1 control-label" for="company">公司</label>
	            <div class="col-sm-2">
                   	  <select class="form-control" id="company">
                   	  	 <option value=""></option>
                   	  	 <option value="20047">深圳市优融网络科技有限公司</option>
                   	  </select>
	            </div>
	            
	            <label class="col-sm-1 control-label" for="dept">部门</label>
	            <div class="col-sm-2 ">
	                  <select class="form-control" id="dept">
                   	  	 <option value=""></option>
                   	  	 <option value="40025">技术部</option>
                   	  	 <option value="40026">财务部</option>
                   	  </select>
	            </div>
	            
	            <label class="col-sm-1 control-label" for="user_name">员工</label>
	            <div class="col-sm-2">
	               <input class="form-control" id="user_name" type="text" placeholder="请输入员工姓名"/>
	            </div>
	            
	           <label class="col-sm-1 control-label" for="type">打卡类型</label>
	            <div class="col-sm-2 ">
	                  <select class="form-control" id="type">
                   	  	 <option value=""></option>
                   	  	 <option value="0">签到</option>
                   	  	 <option value="1">签退</option>
                   	  </select>
	            </div>
			</div>
			<div class="form-group">
	            <label class="col-sm-1 control-label" for="status">状态</label>
	            <div class="col-sm-2 ">
	                  <select class="form-control" id="status">
                   	  	 <option value=""></option>
                   	  	 <option value="0">正常</option>
                   	  	 <option value="1">迟到</option>
                   	  	 <option value="2">早退</option>
                   	  </select>
	            </div>
			
				<label class="col-sm-1 control-label" for="start_date">开始日期</label>
	            <div class="col-sm-2" >
	            	<div class="input-group date" id="datetimepicker_start">
		                <input class="form-control" id="start_date" type="text" placeholder="请选择开始日期"/>
		            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
	            	</div>
	            </div>
	            
	            <label class="col-sm-1 control-label" for="end_date">结束日期</label>
		            <div class="col-sm-2">
		            <div class="input-group date" id="datetimepicker_end">
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
	            </div>
			</div>
		</form>
		
		<table id="tb_attendance_records" data-toggle="table" data-method="get" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="userId">工号</th>
				<th data-field="userName">姓名</th>
				<th data-field="company.name">公司</th>
				<th data-field="dept.deptName">部门</th>
				<th data-field="clockDate">日期</th>
				<th data-field="clockType">打卡类型</th>
				<th data-field="clockTime">打卡时间</th>
				<th data-field="clockStatus">状态</th>
				<th data-field="clockLocation">打卡位置</th>
				<th data-field="clockDevice">打卡设备号</th>
			</tr>
			</thead>

		</table>
	</div>
	
	
	
</div>
<script src="<%=request.getContextPath()%>/js/attendance.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>