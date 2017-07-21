<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="company_Toolbar" class="btn-group">
        <button  onclick="CompanyFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
    </div>

    <%--数据表格--%>
    <table id="company_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="company_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="company_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">所属集团</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="group_select"  name="group_num" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">公司名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="companyName" name="name"  >
                                    <input type="text" class="form-control" style="display: none;" id="allAccount" name="account"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">公司法人</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="legalPerson"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">详情</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" id="com_area" name="details"  rows="3"></textarea>
                                </div>
                            </div>
                            <div class="form-group col-md-12">

                                <label class="col-md-3" for="exampleInputName2">公司账户</label>
                                <div class="col-md-9">
                                    <button  onclick="" type="button" class="btn btn-default" data-click="addForm">
                                        <span class="fa icon-plus" aria-hidden="true"></span>添加账号
                                    </button>
                                </div>
                                <div class="col-md-offset-1 col-md-11">
                                    <div class="form-box"></div>
                                    <div class="add-form-item clone-form" style="display:none;">
                                        <span class="toggle-form" data-click="toggleForm">展开</span>
                                        <div class="base-right-btn">
                                            <span class="fl edit-form" data-click="editForm">编辑</span>
                                            <span class="fl delete-form" data-click="deleteForm">删除</span>
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户银行</span>
                                            <%--<input type="text" name="com_bank" class="base-form-input" placeholder="" value="">--%>
                                            <select class="combobox form-control base-form-input base-request" id="company_select" name="bankNo" >
                                            </select>
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户号</span>
                                            <input type="text" name="accounts" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户客户号</span>
                                            <input type="text" name="customer" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">证书序号</span>
                                            <input type="text" name="certificateNumber" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">银行数字证书授权码</span>
                                            <input type="text" name="authorizationCode" class="base-form-input base-request" placeholder="" value="">
                                        </div>
                                        <div class="base-offset-label">
                                            <button type="button" class="base-sure-btn" data-click="sureForm">确定</button>
                                            <button type="button" class="base-cancel-btn" data-click="deleteForm">取消</button>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="CompanyFun.companyAdd()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script type="text/javascript">

    $(function () {
        CompanyFun.getAllGroup();
        CompanyFun.getAllBank();
        CompanyFun.companyQuery();
    });
    var flag = 0; //标识。 0 表示新增操作，1 表示修改操作

    //all function
    var CompanyFun = {
        //打开新增dialog
        openAdd : function () {
            flag = 0;
            $(".modal-title").html("新增");
            $("#company_modal").modal("show");

        },
        //获取所有集团
        getAllGroup : function () {
            $.ajax({
                url : 'getAllGroup',
                dataType : 'json',
                type : 'post',
                data:  {},
                success : function (data) {
                    if(data.length <= 0){
                        Ewin.alert("获取集团失败，请联系管理员");
                        return;
                    }
                    var option = "";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].groupNumber+"'>"+data[i].name+"</option>";
                    }
                    $("#group_select").append(option);

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //获取所有银行信息
        getAllBank : function () {
            $.ajax({
                url : 'getAllBank',
                dataType : 'json',
                type : 'post',
                data:  {},
                success : function (data) {
                    if(data.length <= 0){
                        Ewin.alert("获取银行失败，请联系管理员");
                        return;
                    }
                    var option = "";
                    for (var i = 0; i < data.length; i++){
                        option += "<option value='"+data[i].bankNo+"'>"+data[i].bankName+"</option>";
                    }
                    $("#company_select").append(option);

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //查询
        companyQuery : function () {
            $("#company_table").bootstrapTable({
                url : 'comQuery',
                method : 'post',// get,post
                toolbar : '#company_Toolbar', // 工具栏
                striped : true, // 是否显示行间隔色
                cache : false, // 是否使用缓存，默认为true
                pagination : true, // 是否显示分页
                queryParams : this.queryParams,// 传递参数
                contentType : "application/x-www-form-urlencoded",
                sidePagination : "server", // 分页方式：client客户端分页，server服务端分页
                search : true, //搜索框
                searchText : ' ', //初始化搜索文字
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 20, // 每页的记录行数
                pageList : [20,30,40], // 可供选择的每页的行数
                showRefresh : true, //刷新按钮
                showToggle :true,   //切换试图（table/card）按钮
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'left'},
                    {field : 'name', title : '公司名称', width: 130, align : 'left'},
                    {field : 'account', title : '银行账户', width: 130, align : 'left',
                        formatter : function (value,row,index) {
                            var arr = [];
                            arr.push(value.split(","));
                            return ""+arr[0].length+"";
                        }
                    },
                    {field : 'details', title : '详情', width: 130, align : 'left'},
                    {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false},
                    {field : 'legalPerson', title : '公司法人', width: 130, align : 'left',visible : false},
                    {field : 'group_num', title : '集团编号', width: 130, align : 'left',visible : false}


                ]
            });
        },
        //查询参数
        queryParams : function (params) {
            var paramData = {
                pageSize : params.limit,
                pageNum : params.offset,
                search : params.search
            };
            return paramData;
        },
        //新增
        companyAdd : function () {
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
                str= str.substr(0,str.length-1);
                str+=',';
            });
            str = str.substr(0,str.length-1);
            account = allBankAccount.join(",");
            $("#allAccount").val(account);
            $.ajax({
                url : 'addCom?items= ' + str,
                dataType : 'json',
                type : 'post',
                data:  $("#company_form").serialize(),
                success : function (data) {
                    if(data.code == 300){
                        Ewin.alert(data.message);
                    }else if(data.code == 500){
                        Ewin.alert("操作异常，请联系管理员");
                    }else{
                        Ewin.alert(data.message);
                        $("#company")[0].reset();
                        $("#company_modal").modal("hide");
                        $('#company_table').bootstrapTable('refresh');
                    }

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        },
        //打开修改框
        openEdit : function () {
            flag = 1;
            $(".modal-title").html("修改");
            var row = $('#company_table').bootstrapTable('getSelections');
            if(row.length > 1){
                Ewin.alert("不能多选，请重新选择");
                return;
            }else if(row.length <= 0){
                Ewin.alert("请选中需要修改的数据");
                return;
            }
            //根据银行账户编号获取银行账户信息
            $.ajax({
                url : 'getBankAccountByNum',
                dataType : 'json',
                type : 'post',
                data:  {
                    "accountNum" : row[0].account
                },
                success : function (data) {
                    debugger;
                    $("#department_modal").modal("show");

                },
                error : function () {
                    Ewin.alert("操作异常，请联系管理员");
                }
            })
        }

    };

    //添加账户样式
    $(document).on('click','[data-click]',function(e){
        e.stopPropagation();
        e.preventDefault();

        var self=$(this),
            name=$(this).data('click'),
            html='<div class="add-form-item">'+$('.clone-form').html()+'</div>';

        switch (name){

            case 'addForm': //确定操作
                $('.form-box').append(html);
                break;

            case 'sureForm': //确定操作
                self.parents('.add-form-item').addClass('check-form');
                self.parents('.add-form-item').children('.base-form:gt(0)').slideUp();
                self.parents('.add-form-item').find('input').attr('readonly',true);
                self.parents('.add-form-item').find('select').attr('disabled',true);
                break;

            case 'editForm': //编辑操作
                self.parents('.add-form-item').removeClass('check-form');
                self.parents('.add-form-item').children('.base-form:gt(0)').slideDown();
                self.parents('.add-form-item').find('input').removeAttr('readonly');
                self.parents('.add-form-item').find('select').removeAttr('disabled');
                break;

            case 'toggleForm': //点击展开收缩表格
                self.parents('.add-form-item').children('.base-form:gt(0)').slideToggle();
                self.find('input').attr('readonly',true);
                self.find('select').attr('disabled',true);
                break;

            case 'deleteForm': //删除操作
                self.parents('.add-form-item').remove();

                break;


        }
    });
    
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>