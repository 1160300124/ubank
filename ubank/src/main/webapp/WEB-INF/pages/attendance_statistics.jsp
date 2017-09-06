<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<form class="form-horizontal" role="form" id="add22_form">
			<div class="form-group">
				<label class="col-sm-1 control-label" for="url_name">公司</label>
	            <div class="col-sm-2">
                   	  <select class="form-control" id="url_module">
                   	  	 <option></option>
                   	  </select>
	            </div>
	            <label class="col-sm-1 control-label" for="url_name">部门</label>
	            <div class="col-sm-2 ">
	                  <select class="form-control" id="url_module">
                   	  	 <option></option>
                   	  </select>
	            </div>
	            <label class="col-sm-1 control-label" for="url_name">员工</label>
	            <div class="col-sm-2">
	               <input class="form-control" id="url_name" type="text" placeholder="请输入..."/>
	            </div>
			</div>
			<div class="form-group">
			
				<label class="col-sm-1 control-label" for="url_name">开始日期</label>
	            <div class="col-sm-2" >
	            	<div class="input-group date" id="datetimepicker1">
		                <input class="form-control" id="url_name" type="text" placeholder="请输入..."/>
		            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
	            	</div>
	            </div>
	            <label class="col-sm-1 control-label" for="url_name">结束日期</label>
		            <div class="col-sm-2">
		            <div class="input-group date" id="datetimepicker2">
		               <input class="form-control" id="url_name" type="text" placeholder="请输入..." />
		               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		            </div>
	            </div>
	            <label class="col-sm-1 control-label" for="url_name"></label>
	            <div class="col-sm-2">
	            	<button type="button" class="btn btn-default" id="btn_edit_confirm" >
	            		<span class="fa icon-search" aria-hidden="true"></span>查询
	            	</button>
	            </div>
			</div>
		</form>
		
		<table id="tb_thirdUrls" data-toggle="table" data-url="" data-method="get" data-toolbar="#toolbar" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="">员工</th>
				<th data-field="">公司</th>
				<th data-field="">部门</th>
				<th data-field="">正确打卡（次）</th>
				<th data-field="">迟到（次）</th>
				<th data-field="">未签到（次）</th>
				<th data-field="createTime" data-width="150px">正常签退（次）</th>
				<th data-field="updateTime" data-width="150px">早退（次）</th>
				<th data-field="remark">未签退（次）</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	</div>
	
	
	
</div>
<script src="<%=request.getContextPath()%>/js/url.js" type="text/javascript" ></script>
<script type="text/javascript">
	var basePath = '<%=request.getContextPath()%>';
	$('#datetimepicker1').datetimepicker({  
	    format: 'yyyy-mm-dd',  
	    language: 'zh-CN',  
	    pickDate: true,  
	    pickTime: true,  
	    autoclose: 1,
	    todayBtn:  1,
	    todayHighlight: 1,
	    minView: "month"
	  });  
	
	$('#datetimepicker2').datetimepicker({  
	    format: 'yyyy-mm-dd',  
	    language: 'zh-CN',  
	    pickDate: true,  
	    pickTime: true,  
	    autoclose: 1,
	    todayBtn:  1,
	    todayHighlight: 1,
	    minView: "month"
	  }); 
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>