
$(function () {
    WagesCalcFun.wagesQuery();
    WagesCalcFun.com_listening();

});
var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
var bankInfo = ""; //全局变量，存放所有银行信息
var comNum = ""; //全局变量，存放公司编号
var late=[{
    second:30,
    value:50,
    unit:1, //1元，2天
}] //迟到
var earlyLeave=[] //早退
var wagesCalcTemplateStr='<tr>'+
                '<td><input type="text" class="form-control" name="second" value="@second" /></td>'+
                '<td><input type="text" class="form-control" name="value" value="@value" /></td>'+
                '<td><select class="form-control" name="unit"><option value="1">元</option><option value="2">天</option></select></td>'+
                '<td class="del"><span class="icon-delete"></span></td>'
            '</tr>';    //考勤规则模板字符串

String.prototype.formatStr=function(data){
    var result=this;
    for(var key in data)
        result=result.replace(new RegExp('@'+key,'g'),data[key]); 
    return result;
}

//考勤计算规则
function renderWagesCalc(renderObj,data){
    renderObj.html('');
    var renderStr='';
    $(data).each(function(index,item){
        renderStr+=wagesCalcTemplateStr.formatStr({second:item.second,value:item.value})
    });
    renderObj.html(renderStr);
    bindIconDelete();
}

function bindIconDelete(){
    $(".icon-delete").bind('click',function(e){
        $(this).parent().parent().remove();
    })
}

//all function
var WagesCalcFun = {
    //打开新增dialog
    openAdd : function () {
        flag = 0;
        $(".modal-title").html("新增");
        $("#wages_calc_modal").modal("show");

    },
    //打开考勤配置
    openAttendance(){
        renderWagesCalc($('#late_table>tbody'),late);
        renderWagesCalc($('#earlyLeave_table>tbody'),earlyLeave);
        $("#attendanceConfigModal").modal("show");

    },
    //弹出框关闭监听事件
    com_listening : function () {
        $("#wages_calc_modal").on('hide.bs.modal',function () {
            
            
        });
    },
    addWagesCalcLate(){
        $('#late_table>tbody').append(wagesCalcTemplateStr.formatStr({second:'',value:''}))
        bindIconDelete();
    },
    addWagesCalcEarlyLeave(){
        $('#earlyLeave_table>tbody').append(wagesCalcTemplateStr.formatStr({second:'',value:''}))
        bindIconDelete();
    },
    //查询
    wagesQuery : function () {
        $("#wages_calc_table").bootstrapTable({
            url : 'comQuery',
            method : 'post',// get,post
            toolbar : '#wages_calc_Toolbar', // 工具栏
            striped : true, // 是否显示行间隔色
            cache : false, // 是否使用缓存，默认为true
            pagination : true, // 是否显示分页
            queryParams : this.queryParams,// 传递参数
            contentType : "application/x-www-form-urlencoded",
            sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
            search : true, //搜索框
            searchText : ' ', //初始化搜索文字
            pageNumber : 1, // 初始化加载第一页，默认第一页
            pageSize : 10, // 每页的记录行数
            pageList : [10,20,30,40], // 可供选择的每页的行数
            showRefresh : true, //刷新按钮
            showToggle :true,   //切换试图（table/card）按钮
            //showColumns : true,
            clickToSelect : true,
            columns : [
                {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                {field : 'name', title : '规则名称', width: 130, align : 'left'},
                {field : 'groupName', title : '适配公司', width: 130, align : 'left'},
                {field : 'account', title : '修改时间', width: 130, align : 'left'},
                {field : 'details', title : '修改人', width: 130, align : 'left'}
            ]
        });
    },
    //查询参数
    queryParams : function (params) {
        var paramData = {
            pageSize : params.limit,
            pageNum : params.offset,
            search : params.search,
            sysflag : SYSFLAG,
            groupNumber : GROUPNUMBER
        };
        return paramData;
    },
    //新增
    wagesAdd : function () {
        
    },
    //删除工资
    deletewages : function (e) {
        
    },
    saveWagesCalc:function(){
        var lateRules=[]    //迟到规则
        var earlyLeaveRules=[]; //早退规则
        $("#late_table tbody tr").each(function(index,item){
            lateRules.push({
                second:$(item).find('input[name="second"]').val(),
                value:$(item).find('input[name="value"]').val(),
                unit:$(item).find('select[name="unit"]').val(),
            })
        })
        $("#earlyLeave_table tbody tr").each(function(index,item){
            earlyLeaveRules.push({
                second:$(item).find('input[name="second"]').val(),
                value:$(item).find('input[name="value"]').val(),
                unit:$(item).find('select[name="unit"]').val(),
            })
        })
        console.log(lateRules);
        console.log(earlyLeaveRules);

    }


};