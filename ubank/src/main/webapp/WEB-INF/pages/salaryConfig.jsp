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
				<input class="form-control" id="choose_id" type="hidden" />
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
	            	<button type="button" class="btn btn-default" id="btn_import_latest_salary" >
	            		<span class="fa " aria-hidden="true"></span>导入最近工资表
	            	</button>
	            </div>
	            
			</div>
			<div class="form-group">
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
			</div>
			
		</form>

	<table id="tb_saraly_configs" data-toggle="table" data-url="" data-method="get" data-striped="true"
		   data-pagination="false" data-side-pagination="client" data-editable="true"
		   data-page-size="20" data-page-list="[10,15,20]">
 		 <thead>
		<tr>
			<th data-field="userName" data-edit="false">姓名</th>
			<th data-field="cardNo" data-edit="false">身份证号码</th>
			<th data-field="preTaxSalaries">税前工资</th>
			<th data-field="bonuses">奖金</th>
			<th data-field="subsidies">补贴</th>
			<th data-field="attendanceCutPayment" data-edit="false">考勤扣款</th>
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

</div>
<script src="<%=request.getContextPath()%>/js/bootstrap-table/bootstrap-table-editable.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/salaryConfig.js" type="text/javascript" ></script>
<script type="text/javascript">
var companyId = "${param.companyId}";
var salaryMonth = "${param.salaryMonth}";
var salaryDate = "${param.salaryDate}";
var peopleId = "${param.peopleId}";
var peopleName = "${param.peopleName}";
var sid = "${param.sid}";
$("#editBtn").bind('click',function(event){
    var table = $('#tb_saraly_configs');
    var isEdit = table.bootstrapTable('getEditStatus')
    if (isEdit) return;
		table.bootstrapTable('prepend',{
	        'id':0,
	        'userName': '快速批量设置',
	        'cardNo': '--',
	        'pre_tax_salaries': '10000',
	        'bonuses': 0,
	        'subsidies': 0,
	        'attendance_cut_payment': '--',
	        'askForLeave_cut_payment': '--',
	        'socialInsurance': 0,
	        'publicAccumulationFunds': 0,
	        'taxThreshold': 3500,
	        'personalIncomeTax': '--',
	        'else_cut_payment': 0,
	        'salaries': '--',
	    });
		table.bootstrapTable('editAll');

	    var firstRow = table.find('tbody').children()[0];
	    var inputs = $(firstRow).find('input');
	    var selects = $(firstRow).find('select');
	    inputs.on('keyup', function(e){
	        var tdIndex = $(this).parent().parent().index();
	        var value = this.value;
	        table.find('tbody').children().each(function(index, row){
	            if (index !== 0){
	                $($(row).children()[tdIndex]).find('input[type="text"]').val(value);
	            }
	        })
	    });
	    selects.on('change', function(e){
	        var tdIndex = $(this).parent().index();
	        var value = this.value;
	        table.find('tbody').children().each(function(index, row){
	            if (index !== 0){
	                $($(row).children()[tdIndex]).find('select').val(value);
	            }
	        })
	    });
})
$("#saveBtn").bind('click',function(event){
    $('#tb_saraly_configs').bootstrapTable('cancelEditAll');
    $('#tb_saraly_configs').bootstrapTable('removeRow', 0);
    console.log($('#tb_saraly_configs').bootstrapTable('getModiDatas'));
})
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>