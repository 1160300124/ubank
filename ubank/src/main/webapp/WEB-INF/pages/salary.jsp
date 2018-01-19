<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
	<!-- Single button -->
	<div class="btn-group">
		<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		制作工资表 <span class="caret"></span>
		</button>
		<ul class="dropdown-menu compny-dropdown">
		</ul>
		<button id="btn_remove" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
	</div>

	<table id="tb_saraly_records" data-toggle="table" data-url="getSalaries" data-method="get" data-toolbar="#toolbar" data-striped="true"
		   data-pagination="true" data-side-pagination="server" data-search="true" data-show-refresh="true" data-show-toggle="true" data-show-columns="true" 
		   data-click-to-select="true" data-page-size="10" data-page-list="[10,15,20]">
		<thead>
		<tr>
			<th data-checkbox="true"></th>
			<th data-field="companyName">公司名称</th>
			<th data-field="salaryMonth">工资月份</th>
			<th data-field="salaryDate">发放时间</th>
			<th data-field="totalNumber">发放人数</th>
			<th data-field="totalAmount">发放金额</th>
			<th data-field="status">发放状态</th>
			<th data-field="approveNames">审批人</th>
			<th data-field="operateUserName">操作人</th>
			<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
		</tr>
		</thead>

	</table>

	<!-- 弹出框（Modal） -->
	<div id="salary_create_modal" class="modal fade" style="">
        <div class="modal-dialog"  style="z-index: 901;">
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
					<div class="step step-2-1 step-2-2 step-2-3">
						<h4 class="modal-title">工资表导入</h4>
					</div>
                </div>
                <div class="modal-body" style="padding: 20px;">
					<div class="step step-1">
                        <h3 class="font-bold align-center margin-bottom-60">
							<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name"></span>
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
                        <h3 class="font-bold align-center margin-bottom-60">
							<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name"></span>
						</h3>
						<div class="col-md-10" style="margin: 0px auto;">
							<div class="col-md-5 align-right font-bold">考勤统计月份</div>
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
						<div class="font-bold height-highest margin-bottom-20">
							<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name color-666" style="margin-right: 20px;"></span>
							<i class="icon-calendar" aria-hidden="true"></i>考勤计算日期 <span class="attendance-date color-666"></span>
							
						</div>
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
                        <h3 class="font-bold align-center">
							<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name" style="color:#999;font-weight:300;"></span>
						</h3>
						<div class="col-md-12 height-high">
							<div class="col-md-5 align-right font-bold">工资表名称</div>
							<div class="col-md-6 align-left font-bold color-666"><span class="attendance-date"></span>月工资表</div>

						</div>
						<div class="font-bold col-md-12 height-high">
							<div class="col-md-5 align-right font-bold">工资发放日期</div>
							<div class="col-md-6 align-left">
								<div class="input-group date time-picker" id="datetimepicker_pay">
									<input class="form-control" id="pay_salaries_date" type="text" placeholder="请选择发放时间" />
									<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
								</div>
							</div>
						</div>
						<div class="font-bold col-md-12 height-high">
							<div class="col-md-5 align-right font-bold">选择工资审批人</div>
							<div class="col-md-6 align-left">
								<input class="form-control" id="choose_people" type="text" placeholder="请选择审批人员" />
							</div>
						</div>
					</div>
					<div class="step step-1-5 font-largest align-center">
						<i class="glyphicon glyphicon-time" style="color: #f56b43; font-size: 40px; transform: translateY(8px);"></i>已提交审核
					</div>
					<div class="step step-2-1">
						<h3 class="font-bold align-center margin-bottom-60">
							<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name"></span>
						</h3>
						<div style="width: 200px;margin: 20px auto;">
							<div class="input-group date time-picker" id="datetimepicker_statistic_copy">
								<input class="form-control" id="statistic_month_copy" type="text" placeholder="请选择发放月份"/>
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
						</div>
						<a class="file-upload-button margin-bottom-20" href="javascript:;" target="_blank">
                            选择文件
                            <input id="salary_upload_file" type="file" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" name="file"/>
                        </a>
                        <div class="import-file-name">

						</div>
                        <div class="import-tip" style="text-align:center;">（请<a href="../model/model.xls">下载模板</a>文件，上传说明<span class="import-help">?</span>）</div>
					</div>
					<div class="step step-2-2 align-center">
						<h3 class="font-bold align-center margin-bottom-60">
							<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name"></span>
						</h3>
                        <img src="../images/loading.gif" alt="">
                        <br/>
                        正在导入工资表请稍等，马上好。
					</div>
					<div class="step step-2-3">
						<div class="align-center" style="position: relative;height:40px;line-height:40px;">
                            <a class="file-upload-button" href="javascript:;" target="_blank" style="margin: 0px 0px 10px 0px;position:absolute;left:0px;top:0px;">
                                重新导入
                                <input id="salary_upload_file_rechoose" type="file" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" name="file"/>
                            </a>
                            <h3 class="font-bold align-center margin-bottom-60">
								<i class="icon-building" aria-hidden="true"></i><span class="salary-company-name"></span>
							</h3>
                        </div>
                        <div class="align-center" style="position: relative;height:30px;line-height:30px;">
                            <div style="position:absolute;left:0px;" id="importFileName">（未知.xlsx）</div>
                            <span style="color:#666;" id="importResult">一共识别10条，其中成功8条，失败3条</span>
                        </div>
                        <div>
                            <table id="salary_import_table" class="import-result"></table>
                        </div>
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
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
					<div class="step step-1-3">
						<button type="button" onclick="goStep(1, 4)" class="btn btn-primary">下一步</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
					<div class="step step-1-4">
						<button type="button" onclick="goStep(1, 3, 1300)" class="btn btn-primary">上一步</button>
						<button type="button" onclick="commitAudit()" class="btn btn-primary">提交审核</button>
					</div>
					<div class="step step-1-5">
						<button type="button" class="btn btn-primary" data-dismiss="modal">完成</button>
					</div>
					<div class="step step-2-1">
						<button type="button" onclick="importSalaryData()" class="btn btn-primary">下一步</button>
					</div>
					<div class="step step-2-2">
						
					</div>
					<div class="step step-2-3">
						<button type="button" onclick="goToAudit()" class="btn btn-primary">下一步</button>
					</div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	 <!-- 模态框（Modal） -->
	 <div class="modal fade" tabindex="-1" role="dialog" id="peopleModal">
			<div class="modal-dialog" role="document" style="width:40%;z-index:888;">
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
</div>
<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table-editable.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/salary.js" type="text/javascript" ></script>
<style>
	i{
		margin-right: 20px;
	}
</style>
<%@ include file="/WEB-INF/pages/footer.jsp" %>