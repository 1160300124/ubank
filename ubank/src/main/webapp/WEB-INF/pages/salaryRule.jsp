<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
    <%--工具栏--%>
    <div id="toolbar" class="btn-group">
        <button  onclick="SalaryConfigFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_remove" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
		<button id="btn_refresh" type="button" class="btn btn-default">
			<span class="fa icon-refresh" aria-hidden="true"></span>刷新
		</button>
    </div>

    <%--数据表格--%>
    <table id="tb_salary_rules" data-toggle="table" data-url="salaryRules" data-method="get" data-toolbar="#toolbar" data-striped="true"
			   data-pagination="true" data-side-pagination="server" data-search="true" data-show-columns="true" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<th data-checkbox="true"></th>
				<th data-field="salaryRuleName">规则名称</th>
				<th data-field="companyName">适配公司</th>
				<th data-field="socialInsurance">社保缴纳金额</th>
				<th data-field="publicAccumulationFunds">公积金缴纳金额</th>
				<th data-field="taxThreshold">个税起征点</th>
				<th data-field="updateTime" data-width="150px">修改时间</th>
				<th data-field="operateName">修改人</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

	</table>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="salaryRule_config_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:40%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">新增工资规则</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="salaryRule_config_form">
						<input type="hidden" id="salarySule_id" name="salarySule_id" value="" />
						<div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">工资计算模板名称</label>
                              <div class="col-sm-6">
                                  <input type="text" class="form-control" id="salaryRuleName" name="salaryRuleName" placeholder="请输入名称" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">选择公司</label>
                              <div class="col-sm-6">
                                  <select class="selectpicker show-tick form-control" multiple data-live-search="true" data-size="20" id="company_select" >
                                  		<option value="0">苹果</option>
                                        <option value="1">菠萝</option>
                                        <option value="2">香蕉</option>
                                        <option value="3">火龙果</option>
                                        <option value="4">梨子</option>
                                        <option value="5">草莓</option>
                                        <option value="6">哈密瓜</option>
                                        <option value="7">椰子</option>
                                        <option value="8">猕猴桃</option>
                                        <option value="9">桃子</option>
                                  </select>
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">考勤规则</label>
                              <div class="col-sm-6">
                              	  <div class="control-label" style="float:left;">
                                  	<a href="javascript:SalaryConfigFun.openAttendance();">配置</a>
                                  </div>
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">请假规则</label>
                              <div class="col-sm-6">
                              	  <div class="control-label" style="float:left;">
                                  	<a href="javascript:$('#leaveConfigModal').modal('show');">配置</a>
                                  </div>
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">加班规则</label>
                              <div class="col-sm-6">
                              	  <div class="control-label" style="float:left;">
	                                  <input type="checkbox" id="overtimeRule" style="width:15px;"/>
                              	  </div>
                              	  <div class="control-label" style="float:left;">
	                              	  按加班时长增加加班费
                              	  </div>
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">社保缴纳金额</label>
                              <div class="col-sm-6">
                                  <input type="text" class="form-control" id="socialInsurance" placeholder="请输入金额" name="name" value="0" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">公积金缴纳金额</label>
                              <div class="col-sm-6">
                                  <input type="text" class="form-control" id="publicAccumulationFunds" placeholder="请输入金额" name="name" value="0" >
                              </div>
                          </div>

                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2">个税起征点</label>
                              <div class="col-sm-6 ">
                              	  <div class="control-label" style="float:left;">
	                                  <input type="radio" checked name="taxThreshold" value="3500" style="width:15px;">3500(国籍)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:$('#taxModal').modal('show');">个税表</a>
                                  </div>
                              </div>
                          </div>
                          
                          <div class="form-group">
                              <label class="col-sm-3 control-label" for="exampleInputName2"></label>
                              <div class="col-sm-6">
                                 <div  style="float:left;">
                                	 <input type="radio" name="taxThreshold" value="4500" style="width:15px;">4500(外籍)
                                 </div>
                              </div>
                          </div>
					    
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="btn_salaryRule_config" >确定</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="attendanceConfigModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:45%;min-width:780px;">
            <div class="modal-content" style="background-color: #EEEEEE;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">考勤计算规则</h4>
                </div>
                <div class="modal-body">
                    <div class="col-md-12">
                        <div class="col-md-6">
                            <div style="margin-right:10px">
                                <h5>迟到</h5>
                                <table id="late_table">
                                    <thead>
                                        <tr>
                                            <td>
                                                	迟到时间(分钟)
                                                <div>大于等于</div>
                                            </td>
                                            <td>扣除</td>
                                            <td>单位</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <button onclick="SalaryConfigFun.addSalaryConfigLate()" class="btn btn-default">添加更多迟到项</button>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div>
                                <h5>早退</h5>
                                <table id="earlyLeave_table">
                                    <thead>
                                        <tr>
                                            <td>
							                                                早退时间(分钟)<br/>
							                                                大于等于
                                            </td>
                                            <td>扣除</td>
                                            <td>单位</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        
                                    </tbody>
                                </table>
                                <button onclick="SalaryConfigFun.addSalaryConfigEarlyLeave()" class="btn btn-default">添加更多早退项</button>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-12" style="margin-top:20px;">
                        <div class="col-md-6">
                            <div style="margin-right:10px;">
                                <h5>忘打卡</h5>
                                <table id="forgetclock_table">
                                    <tr>
                                        <td>选项</td>
                                        <td>值</td>
                                        <td>单位</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            	允许忘打卡次数
                                        </td>
                                        <td>
                                            <input type="text" id="allowforgetclock_count" class="form-control">
                                        </td>
                                        <td>
                                            	次
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            	忘打卡扣除/次&nbsp;&nbsp;&nbsp;
                                        </td>
                                        <td>
                                            <input type="text" id="forgetclock_cut" class="form-control">
                                        </td>
                                        <td>
                                            <select name="" id="forgetclock_unit" class="form-control">
                                                <option value="0">元</option>
                                                <option value="1">天</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div>
                                <h5>旷工</h5>
                                <table id="noclock_table">
                                    <tr>
                                        <td>选项</td>
                                        <td>值</td>
                                        <td>单位</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            	旷工/天
                                        </td>
                                        <td>
                                            <input type="text" id="noclock_cut" class="form-control">
                                        </td>
                                        <td>
                                            <select name="" id="noclock_unit" class="form-control">
                                                <option value="0">元</option>
                                                <option value="1">天</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="btn_attendance_config">确定</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="leaveConfigModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">请假计算规则</h4>
                </div>
                <div class="modal-body">
                    <table id="leave_table" >
                        <thead>
                            <tr>
                                <td>事项</td>
                                <td>扣除项</td>
                                <td>单位</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>事假</td>
                                <td><input type="text" class="form-control" id="personalLeave" value="100" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>病假</td>
                                <td><input type="text" class="form-control" id="sickLeave" value="50" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>年假</td>
                                <td><input type="text" class="form-control" id="annualLeave" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>调休</td>
                                <td><input type="text" class="form-control" id="daysOff" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>婚假</td>
                                <td><input type="text" class="form-control" id="wdddingLeave" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>产假</td>
                                <td><input type="text" class="form-control" id="maternityLeave" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>其他</td>
                                <td><input type="text" class="form-control" id="elseLeave" value="100" /></td>
                                <td>%</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="btn_leave_config">确定</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="taxModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:60%;min-width:600px;max-width:900px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">个税表</h4>
                </div>
                <div class="modal-body" style="padding:20px;">
                    <table id="tax_table" border="1">
                        <thead>
                            <tr>
                                <td>级数</td>
                                <td>应纳税所得额（含税）</td>
                                <td>应纳税所得额（不含税）</td>
                                <td>税率（%）</td>
                                <td>速算扣除数</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>不超过1500元的</td>
                                <td>不超过1455元的</td>
                                <td>3</td>
                                <td>0</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>超过1500元至4,500的部分</td>
                                <td>超过1455元至4,155元的部分</td>
                                <td>10</td>
                                <td>105</td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>超过4,500元至9,000的部分</td>
                                <td>超过4,155元至7,755元的部分</td>
                                <td>20</td>
                                <td>555</td>
                            </tr>
                            <tr>
                                <td>4</td>
                                <td>超过9,000元至35,000的部分</td>
                                <td>超过7,755元至27,255元的部分</td>
                                <td>25</td>
                                <td>1,005</td>
                            </tr>
                            <tr>
                                <td>5</td>
                                <td>超过35,000元至55,000的部分</td>
                                <td>超过27,255元至41,255元的部分</td>
                                <td>30</td>
                                <td>2,755</td>
                            </tr>
                            <tr>
                                <td>6</td>
                                <td>超过55,000元至80,000的部分</td>
                                <td>超过41,255元至57,505元的部分</td>
                                <td>35</td>
                                <td>5,505</td>
                            </tr>
                            <tr>
                                <td>7</td>
                                <td>超过80,000的部分</td>
                                <td>超过57,505的部分</td>
                                <td>45</td>
                                <td>13,505</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
	            <div class="modal-footer">
	                 <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	            </div>
            </div>
            
        </div>
    </div>
  </div>
</div>
<script src="<%=request.getContextPath()%>/js/salaryRule.js" type="text/javascript"></script>
<style>
/*     thead td{
        vertical-align: top;
    } */
    #leave_table table{
        margin-bottom: 20px
    }
    #leave_table td{
    	height: 40px;
        width: 120px;
        text-align: center;
    }
    #attendanceConfigModal td{
    	height: 40px;
        width: 120px;
        text-align: center;
    }
    td>.form-control{
        width: 80px;
        display: inline-block;
    }
    #attendanceConfigModal table{
        margin-bottom: 20px
    }
    #attendanceConfigModal h5{
        text-align: center;
        color: #FC7334 ;
        margin-top: 0px;
    }
    .col-md-6{
        padding: 0px;
    }
    .col-md-6>div{
        background: #fff;
        border-radius: 10px;
        padding: 20px;
        text-align: center;
    }
    #tax_table{
        width: 100%;
        text-align: center;
        margin: 0px;
    }
    #tax_table td{
        width: auto;
        height: 40px;
        text-align: center;
    }
    #tax_table thead{
        background-color:  -webkit-gradient(linear, left top, left bottom, from(#fff), to(#90df96));
        background-image: -webkit-linear-gradient(top, #fff, #90df96);
        background-image: -moz-linear-gradient(top, #fff, #90df96);
        background-image: linear-gradient(to bottom, #fff, #90df96);
    }
</style>
<%@ include file="/WEB-INF/pages/footer.jsp" %>