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
					<button type="button" class="btn btn-default" id="btn_adjustment_salary">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;调薪&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</button>
					<button type="button" onclick="salaryChangeFun.openAdjustmentSalaryModal()" class="btn btn-default" id="btn_salary_change">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;调薪弹框&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</button>
					<button type="button" onclick="salaryChangeFun.openPositiveSalaryModal()" class="btn btn-default" id="btn_fixed_salary">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;定薪弹框&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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

<div id="adjustment_salary_modal" class="modal fade">
	<div class="modal-dialog" style="min-width:700px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" >调薪</h4>
			</div>
			<div class="modal-body flex">
				<div class="flex2">
					<div class="flex align-item-center padding10">
						<div class="flex1 employee-name">
							张晓晓
						</div>
						<div class="flex3 employee-info">
							<div class="status">
								<span class="on-job">在职</span>
								<span class="no-job">离职</span>
							</div>
							<div class="time flex">
								<div class="flex1 color999">
									入职时间
								</div>
								<div class="flex3">
									2017-02-04
								</div>
							</div>
							<div class="department flex">
								<div class="flex1 color999">
									部门
								</div>
								<div class="flex3">
									技术部
								</div>
							</div>
						</div>
					</div>
					<div class="flex align-item-center padding10">
						<div class="flex1 color999">
							调整基本工资
						</div>
						<div class="flex1">
							<span id="oldSalary">1000</span> →
						</div>
						<div class="flex2 align-right">
							<input type="text" id="adjustmentSalaryText" class="form-control" />
							<span class="error-tip"></span>
						</div>
					</div>
					<div class="flex align-item-center padding10">
						<div class="flex1 color999">
							调整幅度
						</div>
						<div class="flex1">
						</div>
						<div class="flex2 align-right">
							<input type="text" id="adjustmentPercent" class="form-control" readonly="readonly">
						</div>
					</div>
					<div class="flex align-item-center padding10">
						<div class="flex1 color999">
							调薪生效日期
						</div>
						<div class="flex1">
						</div>
						<div class="flex2 align-right">
							<input type="text" id="effectTime" class="form-control" />
						</div>
					</div>
					<div class="flex padding10">
						<div class="flex1 color999">
							调薪原因
						</div>
						<div class="flex3 align-right">
							<textarea name="" id="" cols="30" rows="10" class="form-control" style="height: 60px;"></textarea>
						</div>
					</div>
				</div>
				<div class="flex1 padding10 adjustment-log" style="overflow: auto;max-height:350px;min-width:250px;">
					<div style="padding-bottom:10px; border-bottom:1px solid #ccc;">调薪记录</div>
					<div>
						<div class="flex align-item-center">
							<div class="paddingright20">
								2017-12-15
							</div>
							<div class="flex1 salary-log">
								基本工资:<br>
								1000元至500元<br>
								涨幅：400%<br>
								adsfaf
							</div>
						</div>
						<div class="flex align-item-center">
							<div class="paddingright20">
								2017-12-15
							</div>
							<div class="flex1 salary-log">
								基本工资:<br>
								1000元至500元<br>
								涨幅：400%<br>
								adsfaf
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align: center;">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" onclick="EmployeeFun.import() " class="btn btn-primary">确认添加</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div id="fixed_salary_modal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" >定薪</h4>
			</div>
			<div class="modal-body flex">
				<div class="flex2">
					<div class="flex align-item-center padding10">
						<div class="flex1 employee-name">
							张晓晓
						</div>
						<div class="flex3 employee-info">
							<div class="status">
								<span class="on-job">在职</span>
							</div>
							<div class="time flex">
								<div class="flex1 color999">
									入职时间
								</div>
								<div class="flex3">
									2017-02-04
								</div>
							</div>
							<div class="department flex">
								<div class="flex1 color999">
									部门
								</div>
								<div class="flex3">
									技术部
								</div>
							</div>
						</div>
					</div>
					<div class="flex align-item-center padding10">
						<div class="flex1">
							调整基本工资
						</div>
						<div class="flex3 align-left">
							<input type="text" id="fixedSalaryText" class="form-control" placeholder="当前基本工资"/>
							<span class="error-tip"></span>
						</div>
					</div>
					<div class="flex align-item-center padding10">
						<div class="flex1">
							
						</div>
						<div class="flex3 align-left add-turn-positive-btn" onclick="salaryChangeFun.showTurnPositive()">
							<i class="glyphicon glyphicon-edit"></i>添加转正工资（可不填）
						</div>
					</div>
					<div class="flex align-item-center padding10 turn-positive" style="display:none;">
						<div class="flex1">
							转正薪资
						</div>
						<div class="flex3 align-right">
							<input type="text" class="form-control">
						</div>
					</div>
					<div class="flex align-item-center padding10 turn-positive" style="display:none;">
						<div class="flex1">
							转正日期
						</div>
						<div class="flex3 align-right">
							<input type="text" id="turnPositive" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align: center;">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" onclick="EmployeeFun.import() " class="btn btn-primary">确认添加</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script src="<%=request.getContextPath()%>/js/salaryChange.js" type="text/javascript" ></script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>