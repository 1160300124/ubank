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
                                    <input type="text" class="form-control" name="name"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">公司法人</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="legalPerson"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <%--用于多个银行数据 start--%>
                                    <div id="" style="display: none;" name="com_bank" ></div>
                                    <div id="" style="display: none;" name="com_account" ></div>
                                    <div id="" style="display: none;" name="com_customer" ></div>
                                    <div id="" style="display: none;" name="certificateNumber" ></div>
                                    <div id="" style="display: none;" name="authorizationCode" ></div>
                                <%--用于多个银行数据 end--%>

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
                                            <input type="text" name="user" class="base-form-input" placeholder="请输入公司账户银行" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户号</span>
                                            <input type="text" name="email" class="base-form-input" placeholder="请输入公司账户号" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">公司账户客户号</span>
                                            <input type="text" name="phone" class="base-form-input" placeholder="请输入公司账户客户号" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">证书序号</span>
                                            <input type="text" name="phone" class="base-form-input" placeholder="请输入证书序号" value="">
                                        </div>
                                        <div class="base-form clearfix">
                                            <span class="base-form-label">银行数字证书授权码</span>
                                            <input type="text" name="phone" class="base-form-input" placeholder="请输入银行数字证书授权码" value="">
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
                    <button type="button" onclick="" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script type="text/javascript">

    $(function () {
        CompanyFun.getAllGroup();
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
                url : 'groupQuery',
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
        //查询
        companyQuery : function () {
            $("#company_table").bootstrapTable({
                url : '',
                method : 'post',// get,post
                toolbar : '#company_Toolbar', // 工具栏
                striped : true, // 是否显示行间隔色
                cache : false, // 是否使用缓存，默认为true
                pagination : true, // 是否显示分页
                queryParams : {},// 传递参数
                pageNumber : 1, // 初始化加载第一页，默认第一页
                pageSize : 20, // 每页的记录行数
                pageList : [20,30,40], // 可供选择的每页的行数
                showRefresh : true, //刷新按钮
                showToggle :true,   //切换试图（table/card）按钮
                search : true, //搜索框
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : '', title : '', width: 130, align : 'center'},



                ]
            });
        }

    };

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
                break;

            case 'editForm': //编辑操作
                self.parents('.add-form-item').removeClass('check-form');
                self.parents('.add-form-item').children('.base-form:gt(0)').slideDown();
                self.parents('.add-form-item').find('input').removeAttr('readonly');
                break;

            case 'toggleForm': //点击展开收缩表格
                self.parents('.add-form-item').children('.base-form:gt(0)').slideToggle();
                self.find('input').attr('readonly',true);
                break;

            case 'deleteForm': //删除操作
                self.parents('.add-form-item').remove();
                break;


        }
    });
    
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>