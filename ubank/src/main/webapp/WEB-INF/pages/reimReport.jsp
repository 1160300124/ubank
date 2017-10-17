<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="page-content">
    <div class="panel-body" style="padding-bottom:0px;">
        <form id="reimFrom" class="form-horizontal"  >
            <div class="form-group">
                <div class="form-group col-md-12">
                    <label class="col-md-1" for="start_date">集团</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="reim_group" name="groupNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">公司</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="reim_company" name="companyNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">部门</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="reim_dept" name="deptNum"></select>
                    </div>
                    <label class="col-md-1" for="start_date">提交人</label>
                    <div class="col-sm-2">
                        <input class="form-control" id="reim_name" type="text"  name="username"/>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label" for="start_date">提交日期</label>
                <div class="col-sm-2" >
                    <div class="input-group date" >
                        <input class="form-control" id="reimDate1" type="text" name="startDate" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <label class="col-md-1" for="start_date">——</label>
                <div class="col-sm-2" >
                    <div class="input-group date" >
                        <input class="form-control" id="reimDate2" type="text"  name="endDate"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <label class="col-md-1" for="start_date">审批状态</label>
                <div class="col-sm-2">
                    <select class="form-control" id="reim_status" name="status">
                        <option value="">请选择</option>
                        <option value="0">待审批</option>
                        <option value="1">审批通过</option>
                        <option value="2">驳回</option>

                    </select>
                </div>
                <label class="col-md-1" for="start_date">审批结果</label>
                <div class="col-sm-2">
                    <select class="form-control" id="reim_result" name="result">
                        <option value="">请选择</option>
                        <option value="1">审批通过</option>
                        <option value="2">驳回</option>
                    </select>
                </div>
                <button onclick="ReimFun.reset();" type="button" class="btn btn-default">
                    <span class="fa icon-search" aria-hidden="true"></span>查询
                </button>
                <button onclick="reimExport()" type="button" class="btn btn-default">
                    <span class="fa icon-cloud-download" aria-hidden="true"></span>导出
                </button>
            </div>
        </form>

        <div id="reim_modal" class="modal fade">
            <div class="modal-dialog" style="width: 700px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title" >报销详情</h4>
                    </div>
                    <div class="modal-body">
                        <table id='detail_table'></table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <%--数据表格--%>
        <table id="reim_table" > </table>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/jquery/jquery.tabletojson.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        $('#reimDate1').datetimepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            pickDate: true,
            pickTime: true,
            autoclose: 1,
            todayBtn:  1,
            todayHighlight: 1,
            minView: "month"
        });

        $('#reimDate2').datetimepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            pickDate: true,
            pickTime: true,
            autoclose: 1,
            todayBtn:  1,
            todayHighlight: 1,
            minView: "month"
        });

        ReimFun.loadReimData();

    });

    var groupNum = $("#reim_group").val();
    var companyNum = $("#reim_company").val();
    var deptNum = $("#reim_dept").val();
    var username = $("input[name=username]").val();
    var startDate = $("input[name=startDate]").val();
    var endDate = $("input[name=endDate]").val();
    var reim_status = $("#reim_status").val();
    var reim_result = $("#reim_result").val();

    var ReimFun = {
        //加载请假申请数据
        loadReimData : function () {
            $("#reim_table").bootstrapTable({
                url : 'reimReportQuery',
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
                showRefresh : true, //刷新按钮
                showToggle : true,   //切换试图（table/card）按钮
                //showColumns : true,
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox  :true, width: 20, align : 'center'},
                    {field : 'id', title : '审批编号', width: 130, align : 'left'},
                    {field : 'createDate', title : '提交时间', width: 130, align : 'left'},
                    {field : 'username', title : '报销人', width: 130, align : 'left'},
                    {field : 'company', title : '公司', width: 130, align : 'left'},
                    {field : 'dept', title : '部门', width: 130, align : 'left'},
                    {field : 'totalAmount', title : '报销金额', width: 130, align : 'left',
                        formatter : function (value) {
                            if(value != null && value != ""){
                                return value/100 + "元";
                            }
                        }
                    },
                    {field : 'reason', title : '申请内容', width: 130, align : 'left'},
                    {field : 'count', title : '事项详情', width: 130, align : 'left',
                        formatter :function (value,row,index) {
                            return "<p><a href='javascript:void(0);' id='reimDetails"+ row.id +"' onclick='ReimFun.showDetails("+ row.id+")'>"+ value +"项" + "</a></p>";
                        }
                    },
                    {field : 'status', title : '审批状态', width: 130, align : 'left',
                        formatter : function(value,row,index){
                            if(value == "0"){
                                return "待审批";
                            }else if(value == '1'){
                                return "审批通过";
                            }else if(value == '2'){
                                return "驳回";
                            }else{
                                return "";
                            }
                        }
                    },
                    {field : 'result', title : '审批结果', width: 130, align : 'left',
                        formatter : function(value,row,index){
                            if(row.status == "1"){
                                return "审批通过";
                            }else if(row.status == "2"){
                                return "审批不通过"
                            }else if(row.status == "0"){
                                return "正在审批中";
                            }else{
                                return ''
                            }
                        }
                    }
                ]
            });
        },
        //刷新
        reset : function () {
            $('#reim_table').bootstrapTable('refresh');
        },
        //查询参数
        queryParams : function (params) {

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
                status : reim_status,
                result : reim_result

            };
            return paramData;
        },
        //弹出报销详情
        showDetails : function (id) {
            if(id == "" || id == null){
                Ewin.alert("没有获取申请记录ID");
                return;
            }
            $("#reim_modal").modal("show");
            debugger
            this.showDetailTable(id);
        },
        //加载报销详情列表
        showDetailTable : function (id) {
            $("#detail_table").bootstrapTable({
                url : 'queryReimDetails',
                method : 'post',// get,post
                toolbar : '#', // 工具栏
                striped : true, // 是否显示行间隔色
                cache : false, // 是否使用缓存，默认为true
                pagination : true, // 是否显示分页
                queryParams : {
                    id : id
                },// 传递参数
                contentType : "application/x-www-form-urlencoded",
                sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
                search : false, //搜索框
                searchText : ' ', //初始化搜索文字
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 10, // 每页的记录行数
                pageList : [10,20,30,40], // 可供选择的每页的行数
                //showColumns : true,
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox  :true, width: 20, align : 'center'},
                    {field : 'id', title : '事项', width: 100, align : 'left',
                        formatter : function (value) {
                            return "事项" + value ;
                        }
                    },
                    {field : 'type', title : '类型', width: 100, align : 'left'},
                    {field : 'amount', title : '金额', width: 100, align : 'left'},
                    {field : 'start', title : '出发地', width: 100, align : 'left'},
                    {field : 'end', title : '目的地', width: 100, align : 'left'},
                    {field : 'remark', title : '备注', width: 130, align : 'left'},
                    {field : 'images', title : '图片', width: 100, align : 'left'}

                ]
            });
        }
    };

    //报销报表导出
    function reimExport() {
        var table = $('#reim_table').tableToJSON();
        var fileName = 'ReimbursementReportExcel';
        console.info(table);
        var json = JSON.stringify(table);
        var nodes = $("#reim_table thead tr").children().children('div .th-inner');
        var header = "";
        $.each(nodes,function (i,items) {
            header += $(this).text() + ",";
        });
        header = header.substr(1,(header.length-1));
        //调用post函数
//        post('exportExcel',{fileName : fileName,header : header,json : json,sysflag : SYSFLAG,groupNum : groupNum,groupNumber : GROUPNUMBER,
//            companyNumber : COMPANYNUMBER, companyNum : companyNum,deptNum : deptNum,username : username,startDate : startDate,endDate : endDate,
//            status : leave_status,result : leave_result});
        post('reimExportExcel',{fileName : fileName,header : header,json : json,sysflag : SYSFLAG,groupNum : groupNum,groupNumber : GROUPNUMBER,
            companyNumber : COMPANYNUMBER,companyNum : companyNum,deptNum : deptNum,username : username,startDate : startDate,endDate : endDate,
            status : reim_status,result : reim_result});
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