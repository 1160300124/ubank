<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/summernote.css">
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
	<form class="form-horizontal required-validate" id="announcement_form">
		<div class="form-group">
			<div class="col-sm-6">
			<h3 align="center"><b>发公告</b></h3>
			</div>
		</div>
		<div class="form-group">
			<label for="company_select" class="col-sm-1 control-label">公司</label>
			<div class="col-sm-5">
           	  <select class="form-control" id="company_select">
           	  	 <option value="">请选择公司</option>
           	  </select>
            </div>
		</div>
		
        <div class="form-group">
        	<label class="col-sm-1 control-label" for="choose_people">发送范围</label>
        	<div class="col-sm-5">
           		<input class="form-control" id="choose_people" type="text" placeholder="请选择要发送的人"/>
           </div>
        </div>
        
        <div class="form-group">
        	<label class="col-sm-1 control-label" for="title">标题</label>
        	<div class="col-sm-5">
           		<input class="form-control" id="title" type="text" placeholder="请输入公告标题"/>
           	</div>
        </div>
		
		<div class="form-group">
			<label for="body" class="col-sm-1 control-label">公告正文</label>
			<div class="col-sm-5">
				<div class="summernote" id="body" name="body" placeholder="请填写公告正文"></div>
			</div>
		</div>
	</form>
	
	<form class="form-horizontal required-validate" action="#" enctype="multipart/form-data" method="post" onsubmit="return iframeCallback(this, pageAjaxDone)">
		<div class="form-group">
			<label for="" class="col-sm-1 control-label">附件</label>
			<div class="col-sm-5 tl th">
				<input type="file" name="image" class="projectfile" value="${deal.image}"/>
				<p class="help-block">支持doc、xls、ppt、pdf格式，大小不超过10M</p>
			</div>
		</div>
	</form>
	
	<div class="form-group">
		<label for="" class="col-sm-1 control-label"></label>
        <button type="button" class="btn btn-primary" id="btn_send">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发送&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
        <button type="button" class="btn btn-default" if="btn_preview">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;预览&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
    </div>
	
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


<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap/summernote.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap/summernote-zh-CN.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.core.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.ztree.excheck.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/announcement.js" type="text/javascript" ></script>
<script type="text/javascript">

</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>