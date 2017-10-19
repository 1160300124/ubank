<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/banks/bank_header.jsp" %>
<div class="page-content">
    <%--工具栏--%>
    <div id="expand_Toolbar" class="btn-group">
        <button onclick="expand.reload()" type="button" class="btn btn-default">
            <span class="fa icon-search" aria-hidden="true"></span>查询
        </button>
        <button  onclick="expand.openNew()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>拓展企业
        </button>
        <%--<button onclick="" type="button" class="btn btn-default">--%>
            <%--<span class="fa icon-edit" aria-hidden="true"></span>修改--%>
        <%--</button>--%>
        <%--<button onclick="" type="button" class="btn btn-default">--%>
            <%--<span class="fa icon-remove" aria-hidden="true"></span>删除--%>
        <%--</button>--%>


        <label class="col-md-1 headquarters" for="start_date">总行</label>
        <div class="col-sm-2 headquarters" >
            <input class="form-control" id="business_headquarters" type="text"  name=""/>
        </div>
        <label class="col-md-1 branchs" for="start_date">分行</label>
        <div class="col-sm-2 branchs">
            <input class="form-control" id="business_branchs" type="text"  name=""/>
        </div>
        <label class="col-md-1 child" for="start_date">支行</label>
        <div class="col-sm-2 child">
            <input class="form-control" id="business_child" type="text"  name=""/>
        </div>
        <label class="col-md-1 name" for="start_date">业务员名称</label>
        <div class="col-sm-2 name">
            <input class="form-control" id="business_name" type="text"  name=""/>
        </div>
        <label class="col-md-1 groupName" for="start_date">集团名称</label>
        <div class="col-sm-2 groupName">
            <input class="form-control" id="business_groupName" type="text"  name=""/>
        </div>
    </div>

    <%--数据表格--%>
    <table id="expand_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="expand_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" ></h4>
                </div>
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="expand_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">集团名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control"  name="groupName"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">公司名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control"  name="companyName"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">系统管理人姓名</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control"  name="userName"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">管理人电话</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control"  name="mobile"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">备注</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" name="remark" id=""  rows="3"></textarea>
                                </div>
                            </div>
                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="expand.add()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<script type="text/javascript">
    $(function(){
        expand.dataLoad();
        switch (ROLETYPE) {
            //分部管理员
            case 1 :
                $(".headquarters").css("display","none");
                $(".branchs").css("display","");
                $(".child").css("display","");
                $(".name").css("display","");
                break;
            case 2 :
                //支部管理员
                $(".headquarters").css("display","none");
                $(".branchs").css("display","none");
                $(".child").css("display","");
                $(".name").css("display","");
            case 3 :
                 //业务员
                $(".headquarters").css("display","none");
                $(".branchs").css("display","none");
                $(".child").css("display","none");
                $(".name").css("display","none");
                break;

        }

    });

    var flag = 0;  //标识。 0 表示新增操作，1 表示修改操作

    var expand = {
        //查询
        dataLoad : function () {
            $("#expand_table").bootstrapTable({
                url : 'queryBusiness',
                method : 'post',// get,post
                toolbar : '#expand_Toolbar', // 工具栏
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
                clickToSelect : true,
                columns : [
                    {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                    {field : 'number', title : '业务员编号', width: 100, align : 'left'},
                    {field : 'bankName', title : '所属银行', width: 100, align : 'left'},
                    {field : 'groupName', title : '拓展集团', width: 100, align : 'left'},
                    {field : 'count', title : '员工数量', width: 100, align : 'left'},
                    {field : 'userName', title : '集团管理员', width: 100, align : 'left'},
                    {field : 'mobile', title : '管理员电话', width: 100, align : 'left'},
                    {field : 'id', title : 'ID', width: 100, align : 'left',visible : false}
                ]
            });
        },
        //查询参数
        queryParams : function (params) {
            var paramData = {
                pageSize : params.limit,
                pageNum : params.offset,
             //   search : params.search,
                type : TYPE,
                roleType : ROLETYPE,
                bankNo : BANKNO,
                number : NUMBER,
                heaquarters : $("#business_headquarters").val(),
                branch : $("#business_branchs").val(),
                child : $("#business_child").val(),
                name : $("#business_name").val(),
                groupName : $("#business_groupName").val()

            };
            return paramData;
        },
        //刷新
        reload : function () {
            $('#expand_table').bootstrapTable('refresh');
        },
        //打开新增dialog
        openNew : function () {
            $(".modal-title").html("新增");
            $("#expand_modal").modal("show");

        },
        //弹出框关闭监听事件
        listening : function () {
            $("#expand_modal").on('hide.bs.modal',function () {
            $('#expand_table').bootstrapTable('uncheckAll');
            $("#expand_form")[0].reset();
            });
        },
        //新增
        add : function(){
            var groupName = $("input[name = groupName]").val();
            var companyName = $("input[name = companyName]").val();
            var userName = $("input[name = userName]").val();
            var mobile = $("input[name = mobile]").val();
            if(groupName == "" ){
                Ewin.alert("集团名称不能为空");
                return;
            }
            if(companyName == "" ){
                Ewin.alert("公司名称不能为空");
                return;
            }
            if(userName == "" ){
                Ewin.alert("系统管理人姓名不能为空");
                return;
            }else if(!Validate.regWord(userName)){
                Ewin.alert("系统管理人姓名必须是中文");
                return;
            }
            if(mobile == "" ){
                Ewin.alert("管理人电话不能为空");
                return;
            }
            var password = mobile.substr(5,6);
            $.ajax({
                url: 'saveBusiness?flag=' + flag + "&password=" + password + "&number=" + NUMBER + "&bankNo=" + BANKNO + "&type=" + TYPE,
                dataType : 'json',
                type : 'post',
                data : $("#expand_form").serialize(),
                success : function (data) {
                    if(data.code != 5000){
                        Ewin.alert(data.message);
                        $("#expand_form")[0].reset();
                        $("#expand_modal").modal("hide");
                        $('#expand_table').bootstrapTable('refresh');
                    }else{
                        $("#expand_form")[0].reset();
                        $("#expand_modal").modal("hide");
                        Ewin.alert(data.message);
                        $('#expand_table').bootstrapTable('refresh');
                    }
                },
                error : function () {
                    Ewin.alert("新增失败");
                }
            })

        }
    }
</script>
<%@ include file="/WEB-INF/pages/footer.jsp" %>