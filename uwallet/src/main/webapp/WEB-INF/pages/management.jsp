<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>U钱包后台管理系统</title>
    <script src="../js/jquery/jquery-1.12.3.min.js"></script>
    <script src="../js/jquery/ajaxfileupload.js"></script>
    
    <link href="../css/bootstrap/bootstrap.min.css" rel="stylesheet" />
    <link href="../css/font-awesome/font-awesome.min.css" rel="stylesheet" />
    <link href="../css/bootstrap-table/bootstrap-table.min.css" rel="stylesheet" />
    <link href="../css/bootstrap-table/bootstrap-theme.min.css" rel="stylesheet" />

    <script src="../js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
    <script src="../js/bootstrap-table/bootstrap-table.min.js" type="text/javascript"></script>
    <script src="../js/bootstrap-table/bootstrap-table-zh-CN.min.js" type="text/javascript"></script>
    <script src="../js/management.js" type="text/javascript"></script>
</head>
<body>
<h1 align="center">U钱包后台管理系统</h1>
<div class="panel-body" style="padding-bottom:0px;">
	<div id="toolbar" class="btn-group">
		<button id="btn_add" type="button" class="btn btn-default">
			<span class="fa icon-plus" aria-hidden="true"></span>新增
		</button>
		<button id="btn_edit" type="button" class="btn btn-default">
			<span class="fa icon-edit" aria-hidden="true"></span>修改
		</button>
		<button id="btn_delete" type="button" class="btn btn-default">
			<span class="fa icon-remove" aria-hidden="true"></span>删除
		</button>
		<button id="btn_import" type="button" class="btn btn-default">
			<span class="fa icon-upload" aria-hidden="true"></span>上传
		</button>
 
	</div>

	<table id="tb_managetments" data-toggle="table" data-url="../manager/getManagement" data-method="get" data-toolbar="#toolbar"
           data-pagination="true" data-side-pagination="server" data-search="true" data-show-refresh="true" data-show-toggle="true" data-show-columns="true" 
           data-page-size="10" data-page-list="[10,15,20]">
           <thead>
           <tr>
           		<th data-checkbox="true"></th>
               <th data-field="userName">工资发放人</th>
               <th data-field="totalNumber">工资总笔数</th>
               <th data-field="totalAmount">工资总金额</th>
               <th data-field="salaryDate">工资发放时间</th>
               <th data-field="salary_createTime">操作时间</th>
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
	                哈哈哈哈哈
	                <table id="tb_salary" data-toggle="table" data-url="" data-method="get"
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
	哈哈哈哈
				
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
	               <table id="tb_salary_detail" data-toggle="table" data-url="" data-method="get"
			           data-pagination="true" data-side-pagination="client" data-page-size="10" data-page-list="[10,15,20]">
			           <thead>
			           <tr>
			           	   <th data-checkbox="true"></th>
			               <th data-field="did">编号</th>
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
</body>
</html>