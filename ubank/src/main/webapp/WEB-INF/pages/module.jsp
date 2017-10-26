<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
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
		<button id="btn_refresh" type="button" class="btn btn-default">
			<span class="fa icon-refresh" aria-hidden="true"></span>刷新
		</button>

	</div>

	<table id="tb_modules" data-toggle="table" data-url="modules" data-method="get" data-toolbar="#toolbar" data-striped="true" data-sort-order="desc"
		   data-pagination="true" data-side-pagination="server" data-search="true" data-show-columns="true" data-click-to-select="true"
		   data-page-size="10" data-page-list="[10,15,20]">
		<thead>
		<tr>
			<th data-checkbox="true"></th>
			<th data-field="mid">模块号</th>
			<th data-field="moduleName">模块名称</th>
			<th data-field="createTime">创建时间</th>
			<th data-field="updateTime">最近修改时间</th>
			<th data-field="remark">备注</th>
			<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
		</tr>
		</thead>

	</table>
	
		<!-- 模态框（Modal） -->
	<div class="modal fade" id="add_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:40%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">新增页面</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="add_form">
						<div class="form-group">
	                       <label class="col-sm-2 control-label" for="module_name">模块名称</label>
	                       <div class="col-sm-8">
	                          <input class="form-control" id="module_name" type="text" placeholder="请输入..."/>
	                       </div>
	                    </div>
	                    
					    <div class="form-group">
	                        <label class="col-sm-2 control-label" for="remark">备注</label>
	                        <div class="col-sm-8">
	                        	<textarea class="form-control" id="remark" rows="4" placeholder="请输入..."></textarea> 
	                        </div>
					    </div>
					    
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="btn_add_confirm" >确定</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="edit_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:40%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">编辑分页</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="edit_form">
						<input type="hidden" id="module_id" name="module_id" value="" />
						<div class="form-group">
	                       <label class="col-sm-2 control-label" for="module_name">页面名称</label>
	                       <div class="col-sm-8">
	                          <input class="form-control" id="module_name" type="text" placeholder="请输入..."/>
	                       </div>
	                    </div>
	                    
					    <div class="form-group">
	                        <label class="col-sm-2 control-label" for="remark">备注</label>
	                        <div class="col-sm-8">
	                        	<textarea class="form-control" id="remark" rows="4" placeholder="请输入..."></textarea> 
	                        </div>
					    </div>
					    
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="btn_edit_confirm" >确定</button>
				</div>
			</div>
		</div>
	</div>
	
	
</div>
<script src="<%=request.getContextPath()%>/js/module.js" type="text/javascript" ></script>
<script type="text/javascript">
	var basePath = '<%=request.getContextPath()%>';
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>