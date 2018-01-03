<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<style>
.select-people .all-people,.selected-people{
    display: inline-block;
    width: 48%;
}
.selected-people{
    float: right;
}
.people-box{
    width: 100%;
    height: 500px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #f4f6f9;
    padding: 10px;
    overflow-y: auto;
}
.search-box{
    position: relative;
    height: 55px;
}
.search-box .search-icon,.search-input{
    position: absolute;
}
.search-box .search-input{
    left: 0px;
    top: 0px;
    padding-left: 45px;
}
.search-box .search-icon{
    left: 15px;
    top: 12px;
    z-index: 10;
    color: #ccc;
}
</style>
<div class="page-content">
		<form class="form-horizontal" role="form" id="record_form">
			<div class="form-group">
				<!-- Single button -->
				<div class="btn-group">
					<button type="button button-primary" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					制作工资表 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu compny-dropdown">
					</ul>
				</div>
				
				<!-- <input class="form-control" id="choose_id" type="hidden" />
				<label class="col-sm-1 control-label" for="company">公司</label>
	            <div class="col-sm-2">
                   	  <select class="form-control" id="company_select">
                   	  	 <option value="">请选择公司</option>
                   	  </select>
	            </div>
	            
	            <div class="col-sm-4 ">
	            	<label class="col-sm-1 control-label" for="company"></label>
	                <button type="button" class="btn btn-default" id="btn_import_user_info" >
	            		<span class="fa " aria-hidden="true"></span>导入员工信息
	            	</button>
	            	<input id="lefile" name="lefile" type="file" style="display:none" onchange="return addFileChange();">  
		       		<button id="btn_salary_upload" type="button" class="btn btn-default" onclick="$('input[id=lefile]').click();">
					    <span class="fa icon-folder-open" aria-hidden="true">点击上传</span>
					</button>
	            </div> -->
	            
			</div>
			<!-- <div class="form-group">
				<label class="col-sm-1 control-label" for="start_date">考勤统计月份</label>
	            <div class="col-sm-2" >
	            	<div class="input-group date time-picker" id="datetimepicker_statistic">
		                <input class="form-control" id="statistic_month" type="text" placeholder="请选择统计月份"/>
		            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
	            	</div>
	            </div>
	            
	            <label class="col-sm-1 control-label" for="company">领导审批</label>
	            <div class="col-sm-2">
                   	  <input class="form-control" id="choose_people" type="text" placeholder="请选择审批人员" />
	            </div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" for="end_date">发放时间</label>
		        <div class="col-sm-2">
		            <div class="input-group date time-picker" id="datetimepicker_pay">
		               <input class="form-control" id="pay_salaries_date" type="text" placeholder="请选择发放时间" />
		               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		            </div>
	            </div>
	            <label class="col-sm-4 control-label" for=""></label>
	            <div class="col-sm-2">
	            	<button type="button" class="btn btn-default" id="btn_close" >
	            		关闭
	            	</button>
	            	<button type="button" class="btn btn-primary" id="btn_save" >
	            		保存
	            	</button>
	            </div>
			</div> -->
			
		</form>

	<table id="tb_saraly_configs" data-toggle="table" data-url="" data-method="get" data-striped="true"
		   data-pagination="true" data-side-pagination="client" data-editable="true"
		   data-page-size="15" data-page-list="[10,15,20]">
 		 <thead>
		<tr>
			<th data-field="userName" data-edit="false">姓名</th>
			<th data-field="cardNo" data-edit="false">身份证号码</th>
			<th data-field="preTaxSalaries" data-edit="false">税前工资</th>
			<th data-field="bonuses">奖金</th>
			<th data-field="subsidies">补贴</th>
			<th data-field="totalCutPayment" data-edit="false">考勤扣款</th>
			<th data-field="askForLeaveCutPayment" data-edit="false">请假扣款</th>
			<th data-field="overtimePayment" data-edit="false">加班费</th>
			<th data-field="socialInsurance">社保缴纳</th>
			<th data-field="publicAccumulationFunds">公积金</th>
			<th data-field="taxThreshold">个税起征点</th>
			<th data-field="personalIncomeTax" data-edit="false">个人所得税</th>
			<th data-field="elseCutPayment">其他扣款</th>
			<th data-field="salaries" data-edit="false">应发工资</th>
		</tr>
		</thead> 

	</table>
	
	 <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="peopleModal">
        <div class="modal-dialog" role="document" style="width:40%">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">选择部门与人员</h4>
                </div>
                <div class="modal-body">
                    <div class="select-people">
                        <div class="all-people">
                            选择：<br/>
                            <div class="people-box">
                                <div class="search-box">
                                    <span class="glyphicon glyphicon-search search-icon"></span>
                                    <input type="text" class="form-control search-input" id="people_search" placeholder="搜索" />
                                </div>
                                <div>
                                    <input type="checkbox"  id="checkAll" />
                                    <label for="checkAll">全选</label>
                                    <ul id="peoplesTree" class="ztree"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="selected-people">
                            已选：<br/>
                            <div class="people-box">
                                <table class="selected-people-table">
                                </table>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="people_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
<!-- 弹出框（Modal） -->
<div id="salary_create_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<div class="step step-1">
						<h4 class="modal-title">制作方式</h4>
					</div>
					<div class="step step-1-1">
						<h4 class="modal-title">考勤统计选择</h4>
					</div>
					<div class="step step-1-2">
						<h4 class="modal-title">正在计算工资</h4>
					</div>
					<div class="step step-1-3">
						<h4 class="modal-title">快速编辑</h4>
					</div>
					<div class="step step-1-4">
						<h4 class="modal-title">工资发放</h4>
					</div>
					<div class="step step-1-5">
						<h4 class="modal-title">工资发放</h4>
					</div>
                </div>
                <div class="modal-body" style="padding: 20px;">
					<div class="step step-1">
                        <h3 class="align-center">
                            <span class="salary-company-name"></span>
                        </h3>
                        <div class="padding20">
							<div class="col-md-6 align-center">
								<button type="button" onclick="goStep(1, 1)" class="btn btn-primary">
									计算工资
								</button>
							</div>
							<div class="col-md-6 align-center">
								<button type="button" onclick="goStep(2, 1)" class="btn btn-primary">
									导入工资表
								</button>
							</div>
						</div>
					</div>
					<div class="step step-1-1">
                        <h3 class="align-center">
                            <span class="salary-company-name"></span>
						</h3>
						<div style="width: 200px;margin: 0px auto;">
							<div class="input-group date time-picker" id="datetimepicker_statistic">
								<input class="form-control" id="statistic_month" type="text" placeholder="请选择统计月份"/>
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
						</div>
					</div>
					<div class="step step-1-2 align-center">
						<img src="../images/loading.gif" alt="">
                        <br/>
                        正在计算工资，请稍等
					</div>
					<div class="step step-1-3">
						<table id="tb_saraly_configs_copy" data-toggle="table" data-url="" data-method="get" data-striped="true"
							data-pagination="true" data-side-pagination="client" data-editable="true"
							data-page-size="10" data-page-list="[10,15,20]">
							<thead>
							<tr>
								<th data-field="userName" data-edit="false">姓名</th>
								<th data-field="cardNo" data-edit="false">身份证号码</th>
								<th data-field="preTaxSalaries" data-edit="false">税前工资</th>
								<th data-field="bonuses">奖金</th>
								<th data-field="subsidies">补贴</th>
								<th data-field="totalCutPayment" data-edit="false">考勤扣款</th>
								<th data-field="askForLeaveCutPayment" data-edit="false">请假扣款</th>
								<th data-field="overtimePayment" data-edit="false">加班费</th>
								<th data-field="socialInsurance">社保缴纳</th>
								<th data-field="publicAccumulationFunds">公积金</th>
								<th data-field="taxThreshold">个税起征点</th>
								<th data-field="personalIncomeTax" data-edit="false">个人所得税</th>
								<th data-field="elseCutPayment">其他扣款</th>
								<th data-field="salaries" data-edit="false">应发工资</th>
							</tr>
							</thead> 

						</table>
					</div>
					<div class="step step-1-4">
                        <h3 class="align-center">
                            <span class="salary-company-name"></span>
						</h3>
						<table>
							<tr>
								<td>工资表名称</td>
								<td>工资表名称</td>
							</tr>
							<tr>
								<td>工资发放日期</td>
								<td>
									<div class="input-group date time-picker" id="datetimepicker_pay">
										<input class="form-control" id="pay_salaries_date" type="text" placeholder="请选择发放时间" />
										<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
									</div>
								</td>
							</tr>
							<tr>
								<td>选择工资审批人</td>
								<td>
									<input class="form-control" id="choose_people" type="text" placeholder="请选择审批人员" />
								</td>
							</tr>
						</table>
					</div>
					<div class="step step-1-5">
						已提交审核
					</div>
                </div>
                <div class="modal-footer" style="text-align:center;">
					<div class="step step-1">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
					<div class="step step-1-1">
						<button type="button" onclick="calcSalary()" class="btn btn-primary">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
					<div class="step step-1-2">
						<button type="button" onclick="goStep(1, 0)" class="btn btn-primary" data-dismiss="modal">上一步</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
					<div class="step step-1-3">
						<button type="button" onclick="goStep(1, 0)" class="btn btn-primary" data-dismiss="modal">下一步</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
					<div class="step step-1-4">
						<button type="button" onclick="commitAudit()" class="btn btn-primary" data-dismiss="modal">提交审核</button>
					</div>
					<div class="step step-1-5">
						<button type="button" class="btn btn-primary" data-dismiss="modal">完成</button>
					</div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table-editable.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/salarySetting.js" type="text/javascript" ></script>
<script type="text/javascript">
var companyId = "${param.companyId}";
var salaryMonth = "${param.salaryMonth}";
var salaryDate = "${param.salaryDate}";
var peopleId = "${param.peopleId}";
var peopleName = "${param.peopleName}";
var sid = "${param.sid}";
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>