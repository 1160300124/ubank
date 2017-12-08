<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-content">
		<form class="form-horizontal" role="form" id="record_form">
			<div class="form-group">
	            <div class="col-sm-1">
	            	<button type="button" class="btn btn-default" id="btn_to_send" >
	            		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="fa icon-share-alt" aria-hidden="true"></span>发公告&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            	</button>
	            </div>
	            
				<label class="col-sm-1 control-label" for="company">公司</label>
	            <div class="col-sm-2">
                   	  <select class="form-control" id="company_select">
                   	  	 <option value="">请选择公司</option>
                   	  </select>
	            </div>
	            
			</div>
		</form>
		
		<table id="tb_announcement_records" data-toggle="table" data-url="getAnnouncements" data-method="get" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="announceTitle">公告标题</th>
				<th data-field="createTime" data-width="160px">发送时间</th>
				<th data-field="announceCount">发送范围</th>
				<th data-field="operateUserName">发送人</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	
</div>
<script src="<%=request.getContextPath()%>/js/announcement.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>