
$(function () {
    WagesDetailsFun.wagesDetailsQuery();

});


//all function
var WagesDetailsFun = {
    //查询
    wagesDetailsQuery : function () {
        $("#wages_details_table").bootstrapTable({
            url : 'comQuery',
            method : 'post',// get,post
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
                {field : 'name', title :'姓名', width: 130, align : 'left'},
                {field : 'groupName', title : '身份证号码', width: 130, align : 'left'},
                {field : 'account', title : '税前工资', width: 130, align : 'left'},
                {field : 'details', title : '奖金', width: 130, align : 'left'},
                {field : 'companyNumber', title : '补贴', width: 130, align : 'left'},
                {field : 'legalPerson', title : '考勤扣款', width: 130, align : 'left'},
                {field : 'legalPerson', title : '请假扣款', width: 130, align : 'left'},
                {field : 'legalPerson', title : '加班', width: 130, align : 'left'},
                {field : 'legalPerson', title : '社保缴纳', width: 130, align : 'left'},
                {field : 'legalPerson', title : '公积金', width: 130, align : 'left'},
                {field : 'legalPerson', title : '个税起征点', width: 130, align : 'left'},
                {field : 'legalPerson', title : '个人所得税', width: 130, align : 'left'},
                {field : 'legalPerson', title : '其他扣款', width: 130, align : 'left'},
                {field : 'legalPerson', title : '应发工资', width: 130, align : 'left'}
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
    }
};

