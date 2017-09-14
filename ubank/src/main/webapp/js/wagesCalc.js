
$(function () {
    WagesCalcFun.wagesQuery();
    WagesCalcFun.com_listening();

});
var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
var bankInfo = ""; //全局变量，存放所有银行信息
var comNum = ""; //全局变量，存放公司编号

//all function
var WagesCalcFun = {
    //打开新增dialog
    openAdd : function () {
        flag = 0;
        $(".modal-title").html("新增");
        $("#wages_calc_modal").modal("show");

    },
    //弹出框关闭监听事件
    com_listening : function () {
        $("#wages_calc_modal").on('hide.bs.modal',function () {
            
            
        });
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
        var wagesNo = $("input[name=name]").val();
        //var comName = $("input[name=name]").text();
        var group = $("#group_select").val();
        var bankNo = $("#wages_select").val();
        var legalPerson = $("input[name=legalPerson]").val();
        var accounts = $("input[name=accounts]").val();
        var customer = $("input[name=customer]").val();
        var certificateNumber = $("input[name=certificateNumber]").val();
        var authorizationCode = $("input[name=authorizationCode]").val();
        if(group == ""){
            Ewin.alert("集团不能为空");
            return
        }
        if(bankNo == ""){
            Ewin.alert("公司银行账号不能为空");
            return
        }
        if(wagesNo == ""){
            Ewin.alert("公司名不能为空");
            return
        }else if(!Validate.regNumAndLetter(wagesNo)){
            Ewin.alert("公司名格式不合法，请重新输入");
            return
        }
        if(legalPerson == ""){
            Ewin.alert("法人不能为空");
            return;
        }else if(!Validate.regNumAndLetter(legalPerson)){
            Ewin.alert("法人格式不合法，请重新输入");
            return;
        }
        if(accounts == ""){
            Ewin.alert("公司账号不能为空");
            return;
        }else if(!Validate.regNumber(accounts)){
            Ewin.alert("公司账号格式不合法，请重新输入");
            return;
        }else if(accounts.length > 25){
            Ewin.alert("字符超出范围");
            return;
        }
        if(customer == ""){
            Ewin.alert("公司客户号不能为空");
            return;
        }else if(!Validate.regNumAndLetter(customer)){
            Ewin.alert("公司客户号格式不合法，请重新输入");
            return;
        }else if(customer.length > 25){
            Ewin.alert("字符超出范围");
            return;
        }
        if(certificateNumber == ""){
            Ewin.alert("证书编号不能为空");
            return;
        }else if(!Validate.NumberAndLetter(certificateNumber)){
            Ewin.alert("证书编号格式不合法，请重新输入");
            return;
        }else if(certificateNumber.length > 25){
            Ewin.alert("字符超出范围");
            return;
        }
        if(authorizationCode == ""){
            Ewin.alert("银行数字证书授权码不能为空");
            return;
        }else if(!Validate.NumberAndLetter(authorizationCode)){
            Ewin.alert("银行数字证书授权码格式不合法，请重新输入");
            return;
        }else if(authorizationCode.length > 25){
            Ewin.alert("字符超出范围");
            return;
        }

        //判断公司账户是否重复
        var accArray = [];
        $(".form-box>.add-form-item").find("input[name='accounts']").each(function () {
            accArray.push($(this).val());
        });
        var nary = accArray.sort();
        for (var i = 0; i <accArray.length; i++){
            if(nary[i] == nary[i+1]){
                Ewin.alert("公司账户有重复，请重新添加");
                return;
            }
        }
        //获取所有账户信息
        var arr=[];
        var allBankAccount = [];
        var account = "";
        var str='';
        $('.form-box .add-form-item').each(function(){
            $(this).find('.base-request').each(function(){
                var _value=$(this).val();
                str += _value + "/";

            });
            allBankAccount.push($(this).find("input[name=accounts]").val());
            str += $("#wagesName").val();
            str= str.substr(0,str.length-1);
            str+=',';
        });
        str = str.substr(0,str.length-1);
        account = allBankAccount.join(",");
        $("#allAccount").val(account);
        $.ajax({
            url: 'addCom?items= ' + str + "&flag=" + flag + "&comNum=" + comNum + "&roleid=" + ROLEID ,
            dataType : 'json',
            type : 'post',
            data:  $("#wages_form").serialize(),
            success : function (data) {
                if(data.code == 300){
                    Ewin.alert(data.message);
                }else if(data.code == 500){
                    Ewin.alert("操作异常");
                }else{
                    $("#wages_form")[0].reset();
                    $("#wages_modal").modal("hide");
                    Ewin.alert(data.message);
                    $('#wages_table').bootstrapTable('refresh');
                }

            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //打开修改框
    openEdit : function () {
        var row = $('#wages_table').bootstrapTable('getSelections');
        $(".modal-title").html("修改");
        if(row.length > 1){
            Ewin.alert("不能多选，请重新选择");
            return;
        }else if(row.length <= 0){
            Ewin.alert("请选中需要修改的数据");
            return;
        }
        flag = 1;
        comNum = row[0].wagesNumber;
        var groupNum = row[0].group_num;
       // wagesFun.getAllGroup(groupNum);
        //根据银行账户编号获取银行账户信息
        $.ajax({
            url : 'getBankAccountByNum',
            dataType : 'json',
            type : 'post',
            data:  {
                "accountNum" : row[0].account
            },
            success : function (data) {
                $("#group_select").find("option[value="+row[0].group_num+"]").prop("selected","selected");
                $("#wagesName").val(row[0].name);
                $("input[name=legalPerson]").val(row[0].legalPerson);
                $("#com_area").val(row[0].details);
                //拼接元素
                var html = "";
                for (var i = 0 ; i < data.length ; i++) {
                    var option = "";
                    for (var j = 0 ; j <bankInfo.length ; j++){
                        if($.trim(bankInfo[j].bankNo) == $.trim(data[i].bankNumber)){
                            option += "<option value='"+bankInfo[j].bankNo+"' selected='selected'>"+bankInfo[j].bankName+"</option>";
                        }else{
                            option += "<option value='"+bankInfo[j].bankNo+"'>"+bankInfo[j].bankName+"</option>";
                        }

                    }
                    html += "<div class='add-form-item check-form' >" +
                        "<span class='toggle-form' data-click='toggleForm'>展开</span>" +
                        "<div class='base-right-btn'>" +
                        "<span class='fl edit-form' data-click='editForm'>编辑</span>" +
                        "<span class='fl delete-form' data-click='deleteForm'>删除</span>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>公司账户银行</span>" +
                        "<select class='combobox form-control base-form-input base-request sele"+i+"' value="+data[i].bankNumber+" id='wages_select' name='bankNo' >"+option+"</select>" +
                        "<i class='base-form-select-no'></i>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>公司账户号</span>" +
                        "<input type='text' name='accounts' class='base-form-input base-request' value='" + data[i].account + "'>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>公司账户客户号</span>" +
                        "<input type='text' name='customer' class='base-form-input base-request' value='" + data[i].customer + "'>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>证书序号</span>" +
                        "<input type='text' name='certificateNumber' class='base-form-input base-request' value='" + data[i].certificateNumber + "'>" +
                        "</div>" +
                        "<div class='base-form clearfix'>" +
                        "<span class='base-form-label'>银行数字证书授权码</span>" +
                        "<input type='text' name='authorizationCode' class='base-form-input base-request' value='" + data[i].authorizationCode + "'>" +
                        "</div>" +
                        "<div class='base-offset-label'>" +
                        "<button type='button' class='base-sure-btn' data-click='sureForm'>确定</button>" +
                        "<button type='button' class='base-cancel-btn' data-click='deleteForm'>取消</button>" +
                        "</div>" +
                        "</div>";
                    //插入银行信息
                  //  $(".sele"+i+"").find("option[]").attr("selected",true);

                }
                $(".form-box").html(html);
                $(".form-box .add-form-item").each(function(){
                    $(this).children('.base-form:gt(0)').slideUp();
                    $(this).find('input').attr('readonly',true);
                    $(this).find('select').attr('disabled',true);
                });
                $("#wages_modal").modal("show");
            },
            error : function () {
                Ewin.alert("操作异常");
            }
        })
    },
    //删除工资
    deletewages : function (e) {
        e = window.event;
        e.preventDefault();
        var row = $('#wages_table').bootstrapTable('getSelections');
        if(row.length <= 0){
            Ewin.alert("请选中需要删除的数据");
            return;
        }
        Confirm.show('提示', '确定删除该公司吗？', {
            'Delete': {
                'primary': true,
                'callback': function() {
                    var ids = ""; //存放多个公司编号
                    for (var i = 0 ; i < row.length ;i++){
                        if(i > 0 ){
                            ids += "," + row[i].wagesNumber;
                        }else{
                            ids += row[i].wagesNumber;
                        }
                    }
                    $.ajax({
                        url : 'deletewagess',
                        dataType : 'json',
                        type : 'post',
                        data:  {
                            "ids" : ids
                        },
                        success : function (data) {
                            if(data.code == 300){
                                Confirm.hide();
                                Ewin.alert(data.message);
                            }else if(data.code == 500){
                                Ewin.alert("操作异常");
                            }else{
                                Confirm.hide();
                                Ewin.alert(data.message);
                                $('#wages_table').bootstrapTable('refresh');
                            }

                        },
                        error : function () {
                            Ewin.alert("操作异常");
                        }
                    })

                }
            }
        });
    }


};