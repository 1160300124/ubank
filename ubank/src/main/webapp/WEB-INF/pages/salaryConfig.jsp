<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	            	<div class="input-group date time-picker" id="datetimepicker_start">
		                <input class="form-control" id="start_date" type="text" placeholder="请选择开始日期"/>
		            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
	            	</div>
	            </div>
	            
	            <label class="col-sm-1 control-label" for="company">领导审批</label>
	            <div class="col-sm-2">
                   	  <input class="form-control" id="choose_people" type="text" placeholder="请选择审批人员"/>
	            </div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" for="end_date">发放时间</label>
		        <div class="col-sm-2">
		            <div class="input-group date time-picker" id="datetimepicker_end">
		               <input class="form-control" id="end_date" type="text" placeholder="请选择结束日期" />
		               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		            </div>
	            </div>
	            <label class="col-sm-4 control-label" for=""></label>
	            <div class="col-sm-2">
	            	<button type="button" class="btn btn-default" id="btn_search" >
	            		关闭
	            	</button>
	            	<button type="button" class="btn btn-primary" id="btn_export" >
	            		保存
	            	</button>
	            			<button id="editBtn" type="button" class="btn btn-default">
			<span class="fa icon-download-alt" aria-hidden="true"></span>编辑
		</button>
		<button id="saveBtn" type="button" class="btn btn-default">
			<span class="fa icon-download-alt" aria-hidden="true"></span>保存
		</button>
	            </div>
			</div>
			
		</form>

	<table id="tb_saraly_configs" data-toggle="table" data-url="getSalaryDetails" data-method="get" data-striped="true"
		   data-pagination="true" data-side-pagination="server" data-editable="true"
		   data-page-size="10" data-page-list="[10,15,20]">
 		 <thead>
		<tr>
			<th data-field="userName" data-edit="false">姓名</th>
			<th data-field="cardNo" data-edit="false">身份证号码</th>
			<th data-field="pre_tax_salaries">税前工资</th>
			<th data-field="bonuses">奖金</th>
			<th data-field="subsidies">补贴</th>
			<th data-field="attendance_cut_payment" data-edit="false">考勤扣款</th>
			<th data-field="askForLeave_cut_payment" data-edit="false">请假扣款</th>
			<th data-field="overtime_payment" data-edit="false">加班费</th>
			<th data-field="socialInsurance">社保缴纳</th>
			<th data-field="publicAccumulationFunds">公积金</th>
			<th data-field="taxThreshold">个税起征点</th>
			<th data-field="personalIncomeTax" data-edit="false">个人所得税</th>
			<th data-field="else_cut_payment">其他扣款</th>
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
<script src="<%=request.getContextPath()%>/js/salaryConfig.js" type="text/javascript" ></script>
<script type="text/javascript">
/*  $("#tb_saraly_details").bootstrapTable({
    url : 'getSalaryDetails',
    editable:true,
    columns : [
        {field : 'userName', title : '姓名', width: 130, align : 'left', edit:{type:'text' }},
        {field : 'cardNo', title : '身份证号码', width: 130, align : 'left', edit: false},
        {field : 'pre_tax_salaries', title : '税前工资', width: 130, align : 'left'},
        {field : 'bonuses', title : '奖金', width: 130, align : 'left'},
        {field : 'subsidies', title : '补贴', width: 130, align : 'left',visible : false},
        {field : 'attendance_cut_payment', title : '考勤扣款', width: 130, align : 'left',visible : false},
        {field : 'askForLeave_cut_payment', title : '请假扣款', width: 130, align : 'left',visible : false},
        {field : 'overtime_payment', title : '加班费', width: 130, align : 'left',visible : false}
    ]
}); */ 
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
    $('#tb_saraly_configs').bootstrapTable('remove',{field: 'id', values: [0]});
    console.log($('#tb_saraly_configs').bootstrapTable('getModiDatas'));
})
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>