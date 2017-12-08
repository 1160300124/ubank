<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
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

	</div>

	<table id="tb_thirdUrls" data-toggle="table" data-url="thirdUrls" data-method="get" data-toolbar="#toolbar" data-striped="true" data-sort-order="desc"
		   data-pagination="true" data-side-pagination="server" data-search="true" data-show-refresh="true" data-show-toggle="true" data-show-columns="true"
		   data-click-to-select="true" data-page-size="10" data-page-list="[10,15,20]">
		<thead>
		<tr>
			<th data-checkbox="true"></th>
			<th data-field="urlName">URL名称</th>
			<th data-field="url" >URL链接</th>
			<th data-field="picPath" >图片地址</th>
			<th data-field="module.moduleName">URL模块</th>
			<th data-field="category.categoryName">URL类别</th>
			<th data-field="orderby">排序</th>
			<th data-field="createTime" data-width="160px">创建时间</th>
			<th data-field="updateTime" data-width="160px">最近修改时间</th>
			<th data-field="remark">备注</th>
			<th data-formatter="operateFormatter" data-events="operateEvents">操作栏</th>
		</tr>
		</thead>

	</table>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="add_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:45%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">新增 URL</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="add_form">
						<div class="form-group">
	                       <label class="col-sm-2 control-label" for="url_name">URL名称</label>
	                       <div class="col-sm-8">
	                          <input class="form-control" id="url_name" type="text" placeholder="请输入..."/>
	                       </div>
	                    </div>
	                    
						<div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host">URL链接</label>
	                        <div class="col-sm-8">
	                           <input class="form-control" id="url_addr" type="text" placeholder="请输入..."/>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="pic_upload">图标上传</label>
	                        <div class="col-sm-5">
	                            <img src="" class="img-thumbnail" id="img_path" alt="请上传图标" width="120" height="120">
	                            <input type="hidden" id="pic_path" name="pic_path"/>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host"></label>
	                        <div class="col-sm-5">
	                          	<input id="lefile" name="lefile" type="file" style="display:none" onchange="return addFileChange();">  
	                        	<button id="btn_add_pic_upload" type="button" class="btn btn-default" onclick="$('input[id=lefile]').click();">
					                <span class="fa icon-folder-open" aria-hidden="true">选择图标</span>
					            </button>
	                        </div>
	                     </div>
	                     
	                    <div class="form-group">
	                       <label class="col-sm-2 control-label" for="url_module">模块</label>
	                       <div class="col-sm-5">
	                       	  <select class="form-control" id="url_module">
	                       	  	 <option></option>
	                       	  </select>
	                       </div>
	                    </div>
	                    
	                    <div class="form-group">
	                       <label class="col-sm-2 control-label" for="url_page">类别</label>
	                       <div class="col-sm-5">
	                       	  <select class="form-control" id="url_page">
	                       	  	 <option></option>
	                       	  </select>
	                       </div>
	                    </div>
	                       						
						<div class="form-group">
	                        <label class="col-sm-2 control-label" for="url_sort">排序</label>
	                        <div class="col-sm-5">
	                           <input class="form-control" id="url_sort" type="text" placeholder="请输入..."/>
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
		<div class="modal-dialog" style="width:45%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel"> 编辑 </h4>
				</div>
				<div class="modal-body">

				<form class="form-horizontal" role="form" id="edit_form">
						<input type="hidden" id="url_id" name="url_id" value="" />
						<div class="form-group">
	                       <label class="col-sm-2 control-label" for="url_name">URL名称</label>
	                       <div class="col-sm-8">
	                          <input class="form-control" id="url_name" type="text" placeholder="请输入..."/>
	                       </div>
	                    </div>
	                    
						<div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host">URL链接</label>
	                        <div class="col-sm-8">
	                           <input class="form-control" id="url_addr" type="text" placeholder="请输入..."/>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="pic_upload">图标上传</label>
	                        <div class="col-sm-5">
	                            <img src="" class="img-thumbnail" id="img_path" alt="请上传图标"  width="120" height="120">
	                            <input type="hidden" id="pic_path" name="pic_path"/>
	                        </div>
	                     </div>
	                     
	                     <div class="form-group">
	                        <label class="col-sm-2 control-label" for="ds_host"></label>
	                        <div class="col-sm-5">
	                          	<input id="efile" name="efile" type="file" style="display:none" onchange="return editFileChange();">  
	                        	<button id="btn_edit_pic_upload" type="button" class="btn btn-default" onclick="$('input[id=efile]').click();">
					                <span class="fa icon-folder-open" aria-hidden="true">选择图标</span>
					            </button>
	                        </div>
	                     </div>
	                     
	                    <div class="form-group">
	                       <label class="col-sm-2 control-label" for="url_module">模块</label>
	                       <div class="col-sm-5">
	                       	  <select class="form-control" id="url_module">
	                       	  	 <option></option>
	                       	  </select>
	                       </div>
	                    </div>
	                     
	                    <div class="form-group">
	                       <label class="col-sm-2 control-label" for="url_page">类别</label>
	                       <div class="col-sm-5">
	                       	  <select class="form-control" id="url_page">
	                       	  	 <option></option>
	                       	  </select>
	                       </div>
	                    </div>
	                    
						<div class="form-group">
	                        <label class="col-sm-2 control-label" for="url_sort">排序</label>
	                        <div class="col-sm-5">
	                           <input class="form-control" id="url_sort" type="text" placeholder="请输入..."/>
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
<script src="<%=request.getContextPath()%>/js/url.js" type="text/javascript" ></script>
<script type="text/javascript">
	var basePath = '<%=request.getContextPath()%>';
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>