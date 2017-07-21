<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<div id="toolbar" class="btn-group">
	 		<button id="btn_add" type="button" class="btn btn-default">
                <span class="fa icon-plus" aria-hidden="true"></span>新增
            </button>
<!--             <button id="btn_edit" type="button" class="btn btn-default">
                <span class="fa icon-edit" aria-hidden="true"></span>修改
            </button> -->
			<button id="btn_delete" type="button" class="btn btn-default">
				<span class="fa icon-remove" aria-hidden="true"></span>删除
			</button>

		</div>

		<table id="tb_managetments" data-toggle="table" data-url="modules" data-method="get" data-toolbar="#toolbar" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-search="true" data-show-refresh="true" data-show-toggle="true" data-show-columns="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="moduleName">模块名称</th>
				<th data-field="createTime">创建时间</th>
				<th data-field="updateTime">最近修改时间</th>
				<th data-field="remark">备注</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	</div>
	
</div>
<script src="<%=request.getContextPath()%>/js/url.js" type="text/javascript" ></script>
<script type="text/javascript">
	var basePath = '<%=request.getContextPath()%>';
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>