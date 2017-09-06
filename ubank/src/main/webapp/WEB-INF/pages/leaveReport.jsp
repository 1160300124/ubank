<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>

<!-- 公司页 -->
<div class="page-content">
    <div class="panel-body" style="padding-bottom:0px;">
        <form id="LeaveFrom" class="form-horizontal"  >
            <div class="form-group">
                <div class="form-group col-md-12">
                    <label class="col-md-1" for="start_date">集团</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="leave_group" name="groupNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">公司</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="leave_company" name="companyNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">部门</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="leave_dept" name="deptNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">提交人</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="user_name" type="text"  name="username"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label" for="start_date">提交日期</label>
                <div class="col-sm-2" >
                    <div class="input-group date" id="">
                        <input class="form-control" id="leaveDate1" type="text" name="startDate" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <label class="col-md-1" for="start_date">——</label>
                <div class="col-sm-2" >
                    <div class="input-group date" id="">
                        <input class="form-control" id="leaveDate2" type="text"  name="endDate"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <label class="col-md-1" for="start_date">审批状态</label>
                <div class="col-sm-2">
                    <select class="form-control" id="leave_status" name="status">
                        <option value="">请选择</option>
                        <option value="0">待审批</option>
                        <option value="1">审批通过</option>
                        <option value="2">驳回</option>

                    </select>
                </div>
                <label class="col-md-1" for="start_date">审批结果</label>
                <div class="col-sm-2">
                    <select class="form-control" id="leave_result" name="result">
                        <option value="">请选择</option>
                        <option value="1">审批通过</option>
                        <option value="2">驳回</option>
                    </select>
                </div>
                <button onclick="LeaveFun.reset()" type="button" class="btn btn-default">
                    <span class="fa icon-search" aria-hidden="true"></span>查询
                </button>
                <button onclick="exportExcel()" type="button" class="btn btn-default">
                    <span class="fa icon-search" aria-hidden="true"></span>导出
                </button>
            </div>
        </form>
        <%--数据表格--%>
        <table id="leave_table" > </table>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.tabletojson.js" type="text/javascript"></script>
<script type="text/javascript">

    $(function () {
        $('#leaveDate1').datetimepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            pickDate: true,
            pickTime: true,
            autoclose: 1,
            todayBtn:  1,
            todayHighlight: 1,
            minView: "month"
        });

        $('#leaveDate2').datetimepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            pickDate: true,
            pickTime: true,
            autoclose: 1,
            todayBtn:  1,
            todayHighlight: 1,
            minView: "month"
        });

        LeaveFun.leave_queryGroup();
        LeaveFun.leave_queryCompany();
        LeaveFun.leave_queryDepartment();
        LeaveFun.loadLeaveData();

    });

    var LeaveFun = {
        //加载请假申请数据
        loadLeaveData : function () {
            $("#leave_table").bootstrapTable({
                url : 'leaveQuery',
                method : 'post',// get,post
                toolbar : '#', // 工具栏
                striped : true, // 是否显示行间隔色
                cache : false, // 是否使用缓存，默认为true
                pagination : true, // 是否显示分页
                queryParams : this.queryParams,// 传递参数
                contentType : "application/x-www-form-urlencoded",
                sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
                search : false, //搜索框
                searchText : ' ', //初始化搜索文字
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 10, // 每页的记录行数
                pageList : [10,20,30,40], // 可供选择的每页的行数
                showRefresh : false, //刷新按钮
                showToggle :false,   //切换试图（table/card）按钮
                //showColumns : true,
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 20, align : 'center'},
                    {field : 'id', title : '审批编号', width: 130, align : 'left'},
                    {field : 'company', title : '公司', width: 130, align : 'left'},
                    {field : 'startDate', title : '开始时间', width: 130, align : 'left'},
                    {field : 'endDate', title : '结束时间', width: 130, align : 'left'},
                    {field : 'leaveTime', title : '请假时长(小时)', width: 130, align : 'left'},
                    {field : 'reason', title : '请假事由', width: 130, align : 'left'},
                    {field : 'username', title : '提交人', width: 130, align : 'left'},
                    {field : 'auditor', title : '审批人', width: 130, align : 'left'},
                    {field : 'status', title : '当前审批状态', width: 130, align : 'left',
                        formatter : function(value,index,row){
                            if(row.status == "0"){
                                return "待审批";
                            }else if(row.status == '1'){
                                return "审批通过";
                            }else if(row.status == '2'){
                                return "驳回";
                            }else{
                                return '';
                            }
                        }
                    },
                    {field : 'result', title : '审批结果', width: 130, align : 'left',
                        formatter : function(value,index,row){
                            if(row.status == "1"){
                                return "审批通过";
                            }else if(row.status == "2"){
                                return "驳回"
                            }else if(row.status == "0"){
                                return "待审批";
                            }else{
                                return "";
                            }
                        }
                    }
                ]
            });
        },
        //刷新
        reset : function () {
            $('#leave_table').bootstrapTable('refresh');
        },
        //查询参数
        queryParams : function (params) {
            var groupNum = $("#leave_group").val();
            var companyNum = $("#leave_company").val();
            var deptNum = $("#leave_dept").val();
            var username = $("input[name=username]").val();
            var startDate = $("input[name=startDate]").val();
            var endDate = $("input[name=endDate]").val();
            var leave_status = $("#leave_status").val();
            var leave_result = $("#leave_result").val();
            var paramData = {
                pageSize : params.limit,
                pageNum : params.offset,
                //search : params.search,
                sysflag : SYSFLAG,
                groupNum : groupNum,
                groupNumber : GROUPNUMBER,
                companyNumber : COMPANYNUMBER,
                companyNum : companyNum,
                deptNum : deptNum,
                username : username,
                startDate : startDate,
                endDate : endDate,
                status : leave_status,
                result : leave_result

            };
            return paramData;
        },
        //获取集团
        leave_queryGroup : function () {
            $.ajax({
                url : 'getAllGroup',
                dataType : 'json',
                type : 'post',
                data:  {
                    "groupNumber" : GROUPNUMBER,
                    "sysflag" : SYSFLAG
                },
                success : function (data) {
                    if(data.length <= 0){
                        // Ewin.alert("没有集团数据");
                        return;
                    }
                    var option = "<option value=''>请选择</option>";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].groupNumber+"'>"+data[i].name+"</option>";
                    }
                    $("#leave_group").html(option);

                },
                error : function () {
                    Ewin.alert("操作异常");
                }
            })
        },
        //获取公司
        leave_queryCompany : function () {
            $.ajax({
                url : 'getAllCom',
                dataType : 'json',
                type : 'post',
                data:  {
                    "groupNumber" : GROUPNUMBER,
                    "sysflag" : SYSFLAG
                },
                success : function (data) {
                    if(data.length <= 0){
                        // Ewin.alert("获取公司失败");
                        return;
                    }
                    var option = "<option value=''>请选择</option>";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].companyNumber+"'>"+data[i].name+"</option>";
                    }
                    $("#leave_company").html(option);

                },
                error : function () {
                    Ewin.alert("操作异常");
                }
            })
        },
        //获取部门
        leave_queryDepartment : function () {
            $.ajax({
                url : 'queryAllDept',
                dataType : 'json',
                type : 'post',
                data:  {
                    "companyNumber" : COMPANYNUMBER,
                    "sysflag" : SYSFLAG
                },
                success : function (data) {
                    if(data.length <= 0){
                        return;
                    }
                    var option = "<option value=''>请选择</option>";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].dept_number+"'>"+data[i].name+"</option>";
                    }
                    $("#leave_dept").html(option);

                },
                error : function () {
                    Ewin.alert("操作异常");
                }
            })
        }
    };

    //导出excel
    function exportExcel() {
        var table = $('#leave_table').tableToJSON();
        var fileName = 'Excel';
        console.info(table);
        var json = JSON.stringify(table);
        var nodes = $("#leave_table thead tr").children().children('div .th-inner');
        var header = "";
        $.each(nodes,function (i,items) {
            header += $(this).text() + ",";
        });
        header = header.substr(1,(header.length-1));
        //调用post函数
        post('exportExcel',{fileName : fileName,header : header,json : json});
    }

    function post(url, params) {
        var temp = document.createElement("form");
        temp.action = url;
        temp.method = "post";
        temp.style.display = "none";
        for (var x in params) {
            var opt = document.createElement("input");
            opt.name = x;
            opt.value = params[x];
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        temp.submit();
        return temp;
    }

</script>

<%@ include file="/WEB-INF/pages/footer.jsp" %>