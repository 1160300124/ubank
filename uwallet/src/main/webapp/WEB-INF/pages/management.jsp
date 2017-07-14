<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<div id="toolbar" class="btn-group">
			<!-- 		<button id="btn_add" type="button" class="btn btn-default">
                        <span class="fa icon-plus" aria-hidden="true"></span>新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="fa icon-edit" aria-hidden="true"></span>修改
                    </button>-->
			<button id="btn_delete" type="button" class="btn btn-default">
				<span class="fa icon-remove" aria-hidden="true"></span>删除
			</button>
			<button id="btn_import" type="button" class="btn btn-default">
				<span class="fa icon-upload" aria-hidden="true"></span>上传
			</button>

		</div>

		<table id="tb_managetments" data-toggle="table" data-url="getManagement" data-method="get" data-toolbar="#toolbar" data-striped="true"
			   data-pagination="true" data-side-pagination="server" data-search="true" data-show-refresh="true" data-show-toggle="true" data-show-columns="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="userName">操作人</th>
				<th data-field="totalNumber">总笔数</th>
				<th data-field="totalAmount">总金额</th>
				<th data-field="company">企业名称</th>
				<th data-field="salaryDate">工资发放时间</th>
				<th data-field="salary_createTime">操作时间</th>
				<th data-field="entrustSeqNo">业务委托编号</th>
				<th data-field="status">状态</th>
				<th data-field="remark">备注</th>
				<th 	data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="import_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:50%;height:50%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel"> 上传 </h4>
				</div>
				<div class="modal-body">
					<div class="form-group" style="padding-bottom: 30px;">
						<label for="upload_file" class="col-sm-2 control-label">上传文件</label>
						<div class="col-sm-6">
							<input type="file" class="form-control" id="file" name="file"
								   style="display: none;" onchange="file_tmp.value=this.value;">
							<input type="text" class="form-control" id="file_tmp" name="file_tmp"
								   readonly="readonly" onclick="file.click(); ">
						</div>

						<div class="col-sm-1">
							<button type="button" class="btn btn-primary" id="select_file" onclick="file.click();">浏览   </button>
						</div>
						<div class="col-sm-1">
							<button type="button" class="btn btn-primary" id="upload_file">上传 </button>
						</div>

					</div>

					<table id="tb_salary" data-toggle="table" data-striped="true"
						   data-pagination="true" data-side-pagination="client" data-page-size="10" data-page-list="[10,15,20]">
						<thead>
						<tr>
							<th data-checkbox="true"></th>
							<th data-field="eid">编号</th>
							<th data-field="userName">姓名</th>
							<th data-field="cardNo">身份证号码</th>
							<th data-field="salaries">金额</th>
							<th data-field="remark">备注</th>
						</tr>
						</thead>
					</table>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="btn_pay" >确定</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="detail_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:50%;height:50%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel"> 详情 </h4>
				</div>
				<div class="modal-body">
					<table id="tb_salary_detail" data-toggle="table" data-striped="true"
						   data-pagination="true" data-side-pagination="client" data-page-size="10" data-page-list="[10,15,20]">
						<thead>
						<tr>
							<th data-checkbox="true"></th>
							<th data-field="userName">姓名</th>
							<th data-field="cardNo">身份证号码</th>
							<th data-field="salaries">金额</th>
							<th data-field="remark">备注</th>
						</tr>
						</thead>
					</table>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
</div>
<script src="<%=request.getContextPath()%>/js/management.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>