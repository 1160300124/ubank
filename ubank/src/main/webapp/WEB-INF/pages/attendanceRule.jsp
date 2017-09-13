<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/attendance.css">
<div class="page-content">
	<div class="panel-body" style="padding-bottom:0px;">
		<div id="toolbar" class="btn-group">
	 		<button id="btn_add" type="button" class="btn btn-default">
                <span class="fa icon-plus" aria-hidden="true"></span>新增
            </button>
			<button id="btn_delete" type="button" class="btn btn-default">
				<span class="fa icon-remove" aria-hidden="true"></span>删除
			</button>
			<button id="btn_refresh" type="button" class="btn btn-default">
				<span class="fa icon-refresh" aria-hidden="true"></span>刷新
			</button>

		</div>
		
		<table id="tb_rules" data-toggle="table" data-url="getRules" data-method="get" data-toolbar="#toolbar" data-striped="true" data-sort-order="desc"
			   data-pagination="true" data-side-pagination="server" data-search="true" data-show-columns="true" data-click-to-select="true"
			   data-page-size="10" data-page-list="[10,15,20]">
			<thead>
			<tr>
				<th data-checkbox="true"></th>
				<th data-field="ruleName">名称</th>
				<th data-field="clockOnTime" >上班时间</th>
				<th data-field="clockOffTime" >下班时间</th>
				<th data-field="restStartTime">休息开始时间</th>
				<th data-field="restEndTime">休息结束时间</th>
				<th data-field="clockOnAdvanceHours">最早上班</th>
				<th data-field="clockOffDelayHours">最晚下班</th>
				<th data-field="workday">工作日</th>
				<th data-field="holidayFlag" data-formatter="holidayFormatter">遵循法定节假日</th>
				<th data-field="flexibleFlag" data-formatter="flexibleFormatter">弹性上班</th>
				<th data-field="flexibleTime">弹性时间(min)</th>
				<th data-field="postponeFlag" data-formatter="postponeFormatter">下班顺延</th>
				<th data-field="clockBounds">打卡范围(m)</th>
				<th data-field="clockLocation">考勤地点</th>
				<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
			</tr>
			</thead>

		</table>
	</div>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="add_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:60%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">新增考勤规则</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="add_form">
						<div class="form-group">
	                       <label class="col-sm-2 control-label" for="rule_name">规则名称</label>
	                       <div class="col-sm-8">
	                          <input class="form-control" id="rule_name" type="text" placeholder="请输入..."/>
	                       </div>
	                    </div>
	                    
						<div class="form-group">
	                        <label class="col-sm-2 control-label" for="type">考勤类型</label>
	                        <div class="col-sm-8">
	                        	<input type="radio" id="attendance1" checked="checked" class="icheck">
	                        	&nbsp;&nbsp;<label class="control-label" for="type">固定班制</label>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
                        	<label class="col-sm-3 control-label" style=" color: gray;">工作时段</label>
                           	<div class="col-sm-2" >
				            	<div class="input-group date time-picker" id="datetimepicker_start">
					                <input class="form-control" id="start_time" type="text" placeholder="请选择工作开始时间" style="width:160px;"/>
					            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				            	</div>
				            </div>
				            <div class="col-sm-1 control-label" >一</div>
							<div class="col-sm-2">
					            <div class="input-group date time-picker" id="datetimepicker_end">
					               <input class="form-control" id="end_time" type="text" placeholder="请选择工作结束时间"  style="width:160px;"/>
					               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					            </div>
				            </div>
	                     </div>
	                    
	                    <div class="form-group">
	                       <label class="col-sm-3 control-label"></label>
	                       <div class="col-sm-5 control-label">
		                       <div style="float:left;">
		                       	  <input tabindex="1" type="checkbox" id="input-rest" class="icheck" checked="checked">
	                              &nbsp;&nbsp;<label for="input-rest">设置休息时间段</label>
		                       </div>
	                       </div>
	                    </div>
	                    
	                    <div class="form-group">
                        	<label class="col-sm-3 control-label" ></label>
                           	<div class="col-sm-2" >
				            	<div class="input-group date time-picker" id="datetimepicker_start">
					                <input class="form-control" id="rest_start_time" type="text" placeholder="请选择休息开始时间" style="width:160px;"/>
					            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				            	</div>
				            </div>
				            <div class="col-sm-1 control-label">一</div>
							<div class="col-sm-2">
					            <div class="input-group date time-picker" id="datetimepicker_end">
					               <input class="form-control" id="rest_end_time" type="text" placeholder="请选择休息结束时间"  style="width:160px;"/>
					               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					            </div>
				            </div>
	                    </div>
	                       					
						<div class="form-group">
	                        <label class="col-sm-3 control-label" ></label>
	                        <div class="col-sm-8">
								<div style=" color: gray;">
									休息时段仅对有一个工作时段的工作日（含特殊日期）生效，设置后，工作时长会扣除休息时段部分
								</div>
							</div>
					    </div>
					    
					    <div class="form-group">
                        	<label class="col-sm-3 control-label" style=" color: gray;">工作日</label>
                        	<div class="col-sm-8 control-label">
                        		<div style="float:left;">
                           			<span class="work-day"></span> <a href="javascript:;" id="workDay_setting" >修改</a>
                        		</div>
							</div>
	                     </div>
	                     
	                     <div class="form-group">
                        	<label class="col-sm-3 control-label" ></label>
                        	<div class="col-sm-8 control-label" >
	                        	<div style="float:left;">
	                                <input type="checkbox" checked=“checked” class="icheck" id="freeDay_check">&nbsp;&nbsp;&nbsp;遵循 <a href="javascript:;" onclick="$('#freeDayModal').modal('toggle')">法定节假日</a>
									<span id="freeDay_check_mesage" style="color:red"></span>
								</div>
							</div>
	                     </div>
	                     
	                     <div class="form-group">
                        	<label class="col-sm-3 control-label" ><a href="javascript:;" id="advanceSetting"><span class="glyphicon glyphicon-cog"></span>&nbsp;&nbsp;高级设置</a></label>
                        	<div class="col-sm-8 control-label" >
	                        	<div style="float:left;">
                                    <li> 允许最早签到时间：上班前<span id="work_before"> 2 </span>小时 </li>
                                    <li> 允许最晚签到时间：下班后<span id="work_after" > 6 </span>小时 </li>
	                        	</div>
							</div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host">考勤位置</label>
	                        <div class="col-sm-8">
	                        	<label class="control-label" for="ds_host">1.&nbsp;&nbsp;&nbsp;地点考勤</label>
	                        	<span style="float:right;">有效范围 &nbsp;&nbsp;
				                    <select name="" id="bounds" class="form-control form-select">
				                        <option value="300">300米</option>
				                        <option value="500">500米</option>
				                        <option value="800">800米</option>
				                        <option value="1000">1000米</option>
				                    </select>
			                    </span>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host"></label>
	                        <div class="col-sm-8">
		                        <div class="attendance-location">
		                        	<table class="table-location" id="table_attendanceLocation">
				                        <tr>
				                            <td>优融网络科技有限公司</td>
				                            <td>
				                                <a href="javascript:;" onclick="$('#attendanceLocationModal').modal('toggle')">修改</a>
				                                <a href="javascript:;" style="margin-left:20px;">删除</a>
				                            </td>
				                        </tr>
				                        <tr>
				                            <td>优融网络科技有限公司</td>
				                            <td>
				                                <a href="javascript:;" onclick="$('#attendanceLocationModal').modal('toggle')">修改</a>
				                                <a href="javascript:;" style="margin-left:20px;">删除</a>
				                            </td>
				                        </tr>
			                    	</table>
		                    	</div>	
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host"></label>
	                        <div class="col-sm-8">
	                        <div class="attendance-location">
	                        	<label class="control-label" for="ds_host"></label>
								<div class="add-location">
									<input class="form-control" id="location_type" type="hidden"/>
			                        <a href="javascript:;" id="attendance_location">+&nbsp;&nbsp;添加考勤地点</a>                        
	                    		</div>
	                    		</div>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host">参与考勤人员</label>
	                        <div class="col-sm-8">
	                        	<input class="form-control" id="attendance_people_" type="text" placeholder="请选择参与考勤人员..."/>
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
		<div class="modal-dialog" style="width:60%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">修改考勤规则</h4>
				</div>
				<div class="modal-body" >
					<form class="form-horizontal" role="form" id="edit_form">
						<input class="form-control" id="rule_id" type="hidden"/>
						<div class="form-group">
	                       <label class="col-sm-2 control-label" for="rule_name">规则名称</label>
	                       <div class="col-sm-8">
	                          <input class="form-control" id="rule_name" type="text" placeholder="请输入..."/>
	                       </div>
	                    </div>
	                    
						<div class="form-group">
	                        <label class="col-sm-2 control-label" for="type">考勤类型</label>
	                        <div class="col-sm-8">
	                        	<input type="radio" id="attendance1" checked="checked" class="icheck">
	                        	&nbsp;&nbsp;<label class="control-label" for="type">固定班制</label>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
                        	<label class="col-sm-3 control-label" style=" color: gray;">工作时段</label>
                           	<div class="col-sm-2" >
				            	<div class="input-group date time-picker" id="datetimepicker_start">
					                <input class="form-control" id="start_time" type="text" placeholder="请选择工作开始时间" style="width:160px;"/>
					            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				            	</div>
				            </div>
				            <div class="col-sm-1 control-label">一</div>
							<div class="col-sm-2">
					            <div class="input-group date time-picker" id="datetimepicker_end">
					               <input class="form-control" id="end_time" type="text" placeholder="请选择工作结束时间"  style="width:160px;"/>
					               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					            </div>
				            </div>
	                     </div>
	                    
	                    <div class="form-group">
	                       <label class="col-sm-3 control-label"></label>
	                       <div class="col-sm-5 control-label">
		                       <div style="float:left;">
		                       	  <input tabindex="1" type="checkbox" id="input-rest" class="icheck" checked="checked">
	                              &nbsp;&nbsp;<label for="input-rest">设置休息时间段</label>
		                       </div>
	                       </div>
	                    </div>
	                    
	                    <div class="form-group">
                        	<label class="col-sm-3 control-label" ></label>
                           	<div class="col-sm-2" >
				            	<div class="input-group date time-picker" id="datetimepicker_start">
					                <input class="form-control" id="rest_start_time" type="text" placeholder="请选择休息开始时间" style="width:160px;"/>
					            	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				            	</div>
				            </div>
				            <div class="col-sm-1 control-label">一</div>
							<div class="col-sm-2">
					            <div class="input-group date time-picker" id="datetimepicker_end">
					               <input class="form-control" id="rest_end_time" type="text" placeholder="请选择休息结束时间"  style="width:160px;"/>
					               <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					            </div>
				            </div>
	                    </div>
	                       					
						<div class="form-group">
	                        <label class="col-sm-3 control-label" ></label>
	                        <div class="col-sm-8">
								<div style=" color: gray;">
									休息时段仅对有一个工作时段的工作日（含特殊日期）生效，设置后，工作时长会扣除休息时段部分
								</div>
							</div>
					    </div>
					    
					    <div class="form-group">
                        	<label class="col-sm-3 control-label" style=" color: gray;">工作日</label>
                        	<div class="col-sm-8 control-label">
                        		<div style="float:left;">
                           			<span class="work-day"></span> <a href="javascript:;" id="workDay_setting_edit" >修改</a>
                        		</div>
							</div>
	                     </div>
	                     
	                     <div class="form-group">
                        	<label class="col-sm-3 control-label" ></label>
                        	<div class="col-sm-8 control-label" >
	                        	<div style="float:left;">
	                                <input type="checkbox" checked=“checked” class="icheck" id="freeDay_check">&nbsp;&nbsp;&nbsp;遵循 <a href="javascript:;" onclick="$('#freeDayModal').modal('toggle')">法定节假日</a>
									<span id="freeDay_check_mesage" style="color:red"></span>
								</div>
							</div>
	                     </div>
	                     
	                     <div class="form-group">
                        	<label class="col-sm-3 control-label" ><a href="javascript:;" id="advanceSetting_edit"><span class="glyphicon glyphicon-cog"></span>&nbsp;&nbsp;高级设置</a></label>
                        	<div class="col-sm-8 control-label" >
	                        	<div style="float:left;">
                                    <li> 允许最早签到时间：上班前<span id="work_before"> 2 </span>小时 </li>
                                    <li> 允许最晚签到时间：下班后<span id="work_after" > 6 </span>小时 </li>
	                        	</div>
							</div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host">考勤位置</label>
	                        <div class="col-sm-8">
	                        	<label class="control-label" for="ds_host">1.&nbsp;&nbsp;&nbsp;地点考勤</label>
	                        	<span style="float:right;">有效范围 &nbsp;&nbsp;
				                    <select name="" id="bounds" class="form-control form-select">
				                        <option value="300">300米</option>
				                        <option value="500">500米</option>
				                        <option value="800">800米</option>
				                        <option value="1000">1000米</option>
				                    </select>
			                    </span>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host"></label>
	                        <div class="col-sm-8">
		                        <div class="attendance-location">
		                        	<table class="table-location" id="table_attendanceLocation_">
				                        <tr>
				                            <td>优融网络科技有限公司</td>
				                            <td>
				                                <a href="javascript:;" onclick="$('#attendanceLocationModal').modal('toggle')">修改</a>
				                                <a href="javascript:;" style="margin-left:20px;">删除</a>
				                            </td>
				                        </tr>
				                        <tr>
				                            <td>优融网络科技有限公司</td>
				                            <td>
				                                <a href="javascript:;" onclick="$('#attendanceLocationModal').modal('toggle')">修改</a>
				                                <a href="javascript:;" style="margin-left:20px;">删除</a>
				                            </td>
				                        </tr>
			                    	</table>
		                    	</div>	
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host"></label>
	                        <div class="col-sm-8">
	                        <div class="attendance-location">
	                        	<label class="control-label" for="ds_host"></label>
								<div class="add-location">
									<input class="form-control" id="location_type_" type="hidden"/>
			                        <a href="javascript:;" id="attendance_location_">+&nbsp;&nbsp;添加考勤地点</a>                        
	                    		</div>
	                    		</div>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host">参与考勤人员</label>
	                        <div class="col-sm-8">
	                        	<input class="form-control" id="attendance_people__" type="text" placeholder="请选择参与考勤人员..."/>
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
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="workDayModal" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">设置工作日</h4>
                </div>
                <div class="modal-body">
                    <table class="work-day-list">
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="workDayModal_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
    
    	<!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="workDayModal_edit" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">设置工作日</h4>
                </div>
                <div class="modal-body">
                    <table class="work-day-list">
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="workDayModal_edit_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="freeDayModal" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">法定节假日</h4>
                </div>
                <div class="modal-body">
                    <h4>2017年放假时间安排</h4>
                    <table class="free-day-table">
                        <tr>
                            <td>元旦</td>
                            <td>放假：2016年12月31日~2017年1月2日 共3天</td>
                        </tr>
                        <tr>
                            <td>春节</td>
                            <td>放假：1月27日~2月2日 共7天 调休：1月22日（星期日）上班、2月4日（星期六）上班</td>
                        </tr>
                        <tr>
                            <td>清明节</td>
                            <td>放假：4月2日~4日 共3天 调休：4月1日（星期六）上班</td>
                        </tr>
                        <tr>
                            <td>劳动节</td>
                            <td>放假：4月29日~5月1日 共3天</td>
                        </tr>
                        <tr>
                            <td>端午节</td>
                            <td>放假：5月28日~30日 共3天 调休：5月27日（星期六）上班</td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                中秋节、国庆节 放假：10月1日~10月8日 共8天 调休：9月30日（星期六）上班
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    
     <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="advanceSettingModal" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">高级设置</h4>
                </div>
                <div class="modal-body">
                    <div class="advance-setting">
                        <div class="setting">
                           <label for="">允许最早签到时间：上班前</label>
                            <select class="form-control form-select" id="work_on_before_hours">
                                <option value="1">1小时</option>
                                <option value="2" selected="selected">2小时</option>
                                <option value="3">3小时</option>
                                <option value="4">4小时</option>
                                <option value="5">5小时</option>
                                <option value="6">6小时</option>
                            </select>
                        </div>
                        <div class="setting">
                            <label for="">允许最晚签到时间：下班后</label>
                            <select class="form-control form-select" id="work_off_after_hours">
                                <option value="1">1小时</option>
                                <option value="2">2小时</option>
                                <option value="3">3小时</option>
                                <option value="4">4小时</option>
                                <option value="5">5小时</option>
                                <option value="6" selected="selected">6小时</option>
                                <option value="7">7小时</option>
                                <option value="8">8小时</option>
                                <option value="9">9小时</option>
                                <option value="10">10小时</option>
                                <option value="11">11小时</option>
                                <option value="12">12小时</option>
                            </select>
                        </div>
                        <div class="setting">
                            <span style="color:#ccc;">在选择时间的范围以外不可以打卡</span>
                        </div>

                        <div class="setting" style="padding-top:20px;margin-top:20px;border-top:1px solid #ededed;">
                            <input type="checkbox" class="icheck" id="openFlexible">
                            <label for="openFlexible">开启上班弹性时间</label>
                            <select class="form-control form-select" name="" id="flexibleTime">
                                <option value="30">30分钟</option>
                                <option value="60">60分钟</option>
                            </select>
                            
                        </div>
                        <div class="setting" style="padding-left:20px;">
                            <input type="radio" name="flexible" class="icheck" id="shunyan" value="0">
                            <label for="shunyan">弹性时间内不算迟到，下班时间自动顺延</label>
                        </div>
                        <div class="setting" style="padding-left:20px;">
                            <input type="radio" name="flexible" class="icheck" id="bushunyan" value="1">
                            <label for="bushunyan">弹性时间内不算迟到，下班时间不顺延</label>
                        </div>

                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="advanceSetting_confirm" >确定</button>
                </div>
            </div>
        </div>
    </div>
    
     <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="advanceSettingModal_edit" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">高级设置</h4>
                </div>
                <div class="modal-body">
                    <div class="advance-setting">
                        <div class="setting">
                           <label for="">允许最早签到时间：上班前</label>
                            <select class="form-control form-select" id="work_on_before_hours">
                                <option value="1">1小时</option>
                                <option value="2">2小时</option>
                                <option value="3">3小时</option>
                                <option value="4">4小时</option>
                                <option value="5">5小时</option>
                                <option value="6">6小时</option>
                            </select>
                        </div>
                        <div class="setting">
                            <label for="">允许最晚签到时间：下班后</label>
                            <select class="form-control form-select" id="work_off_after_hours">
                                <option value="1">1小时</option>
                                <option value="2">2小时</option>
                                <option value="3">3小时</option>
                                <option value="4">4小时</option>
                                <option value="5">5小时</option>
                                <option value="6">6小时</option>
                                <option value="7">7小时</option>
                                <option value="8">8小时</option>
                                <option value="9">9小时</option>
                                <option value="10">10小时</option>
                                <option value="11">11小时</option>
                                <option value="12">12小时</option>
                            </select>
                        </div>
                        <div class="setting">
                            <span style="color:#ccc;">在选择时间的范围以外不可以打卡</span>
                        </div>

                        <div class="setting" style="padding-top:20px;margin-top:20px;border-top:1px solid #ededed;">
                            <input type="checkbox" class="icheck" id="openFlexible">
                            <label for="openFlexible">开启上班弹性时间</label>
                            <select class="form-control form-select" name="" id="flexibleTime">
                                <option value="30">30分钟</option>
                                <option value="60">60分钟</option>
                            </select>
                            
                        </div>
                        <div class="setting" style="padding-left:20px;">
                            <input type="radio" name="flexible" class="icheck" id="shunyan" value="0">
                            <label for="shunyan">弹性时间内不算迟到，下班时间自动顺延</label>
                        </div>
                        <div class="setting" style="padding-left:20px;">
                            <input type="radio" name="flexible" class="icheck" id="bushunyan" value="1">
                            <label for="bushunyan">弹性时间内不算迟到，下班时间不顺延</label>
                        </div>

                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="advanceSetting_edit_confirm" >确定</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="attendanceLocationModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:50%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">添加考勤地点</h4>
                </div>
                <div class="modal-body">
                <iframe id="gaodeMapIframe" name ="gaodeMapIframeName" src="<%=request.getContextPath()%>/gaode.jsp" style="width:100%;height:600px;background-color:#ccc;border:0px">
                
                </iframe>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="attendanceLocation_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
    
        <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="attendanceLocationModal_edit" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:50%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">修改考勤地点</h4>
                </div>
                <div class="modal-body">
                <iframe id="gaodeMapIframe_edit" name ="gaodeMapIframeName_edit" src="<%=request.getContextPath()%>/gaode.jsp" style="width:100%;height:600px;background-color:#ccc;border:0px">
                
                </iframe>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="attendanceLocation_edit_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
    
     <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="attendancePeopleModal">
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
                    <button type="button" class="btn btn-primary" id="attendancePeople_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
    
         <!-- 模态框（Modal） -->
    <div class="modal fade" tabindex="-1" role="dialog" id="attendancePeopleModal_edit">
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
                                    <input type="text" class="form-control search-input" id="people_search_" placeholder="搜索" />
                                </div>
                                <div>
                                    <input type="checkbox"  id="checkAll_edit" />
                                    <label for="checkAll">全选</label>
                                    <ul id="peoplesTree_edit" class="ztree"></ul>
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
                    <button type="button" class="btn btn-primary" id="attendancePeople_edit_confirm">确定</button>
                </div>
            </div>
        </div>
    </div>
	
</div>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/rule.js" type="text/javascript" ></script>
<%-- <script src="<%=request.getContextPath()%>/js/map.js" type="text/javascript" ></script> --%>
<script type="text/javascript">
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>