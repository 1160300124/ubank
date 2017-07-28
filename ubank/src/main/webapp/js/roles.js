$(function () {
    RoleFun.role_getComTree();
    RoleFun.role_query();
    RoleFun.loadTree();

});
var flag = 0; //标识。 0 表示新增操作，1 表示修改操作
var index = 0; // 当前选中行的下标
var roleId = ""; //角色ID
var setting = {
    check: {
        enable: true,
        nocheckInherit: true
    },
    data: {
        simpleData: {
            enable: true
        }
    }
};

//arr function
var RoleFun = {
    //打开新增框
    openAddRole : function () {
        this.rol_roleTable();
        $(".modal-title").html("新增");
        $("#role_modal").modal("show");
    },
    loadTree : function () {
        $.ajax({
            url : 'menuTree',
            dataType : 'json',
            type : 'post',
            data : {},
            success : function (data) {
                $.fn.zTree.init($("#role_tree"), setting, data);
            }
        });
    },
    //获取所有公司
    role_getComTree : function () {
        $.ajax({
            url : 'getComTree',
            dataType : 'json',
            type : 'post',
            data : {},
            success : function (data) {
                $("#combotree").bootstrapCombotree({
                    defaultLable : '请选择公司',//默认按钮上的文本
                    data: data,//data应符合实例的data格式
                    showIcon: true,//显示图标
                    showCheckbox: true,//显示复选框
                    width : 400,//下拉列表宽度
                    name : 'list',//combotree值得name，可以用在表单提交
                    maxItemsDisplay : 3,//按钮上最多显示多少项，如果超出这个数目，将会以‘XX项已被选中代替’
                    onCheck : function (node) {//树形菜单被选中是 触发事件

                    }
                });
            }
        });
    },
    //获取所有角色
    rol_roleTable : function () {
        $("#role_table2").bootstrapTable({
            url : 'roleAllQuery',
            method : 'post',// get,post
            striped : true, // 是否显示行间隔色
            cache : false, // 是否使用缓存，默认为true
            contentType : "application/x-www-form-urlencoded",
            sidePagination : "client", // 分页方式：client客户端分页，server服务端分页
            pageNumber : 1, // 初始化加载第一页，默认第一页
            pageSize : 10, // 每页的记录行数
            pageList : [10,20,30,40], // 可供选择的每页的行数
            clickToSelect : true,
            singleSelect : true,
            columns : [
                {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                {field : 'role_id', title : '角色编号', width: 130, align : 'left'},
                {field : 'role_name', title : '角色名', width: 130, align : 'left'},
                {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false}
            ],
            onCheck : function (row) {
                var treeObj = $.fn.zTree.getZTreeObj("role_tree");
                treeObj.checkAllNodes(false);   //清空tree
                $("input[name=role_name]").val(row.role_name);
                var arr = [];
                arr = row.companyNumber
                $("#combotree").bootstrapCombotree("setValue",arr);

                $.ajax({
                    url : 'getRoleById',
                    dataType : 'json',
                    type : 'post',
                    data:  {
                        "roleId" : row.role_id
                    },
                    success : function (data) {
                        //根据当前角色加载对应的菜单
                        var ids = [];
                        for (var i = 0 ; i < data.length ; i++){
                            ids.push(data[i].menuNumber);
                        }
                        for (var j = 0 ; j < ids.length ; j++){
                            treeObj.checkNode(treeObj.getNodeByParam("id",ids[j]),true);
                        }

                    },
                    error : function () {
                        Ewin.alert("操作异常，请联系管理员");
                    }
                })
            }
        });
    },
    //获取所有角色信息
    role_query : function () {
        $("#role_table").bootstrapTable({
            url : 'roleQuery',
            method : 'post',// get,post
            toolbar : '#roles_Toolbar', // 工具栏
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
            showColumns : true,
            clickToSelect : true,
            columns : [
                {field : 'checkbox',checkbox :true, width: 10, align : 'center'},
                {field : 'role_id', title : '角色编号', width: 130, align : 'left'},
                {field : 'role_name', title : '角色名', width: 130, align : 'left'},
                {field : 'companyNumber', title : '所属公司', width: 130, align : 'left',
                    formatter : function (value,row,index) {

                    }
                },
                {field : 'companyNumber', title : '公司编号', width: 130, align : 'left',visible : false}

            ],
            onClickRow : function (row, $element) {
                index = $element.data('index');
            }
        });
    },
    //查询参数定义
    queryParams : function (params) {
        var paramData = {
            pageSize : params.limit,
            pageNum : params.offset,
            search : params.search
        };
        return paramData;
    },
    //新增角色操作
    role_add : function () {
        var com_numbers = $("#combotree").bootstrapCombotree("getValue");
        var roleName = $("input[name=role_name]").val();
        var numbers = com_numbers.join(",");
        $.ajax({
            url : 'addRole?flag=' + flag + "&roleId=" + roleId,
            dataType : 'json',
            type : 'post',
            data:  {
                "com_numbers" : numbers,
                "roleName" : roleName
            },
            success : function (data) {
                if(data.code == 300){
                    Ewin.alert(data.message);
                }else if(data.code == 500){
                    Ewin.alert("操作异常，请联系管理员");
                }else{
                    Ewin.alert(data.message);
                    $("#role_modal").modal("hide");
                    $('#role_table').bootstrapTable('refresh');
                    $('#role_table2').bootstrapTable('refresh');
                    RoleFun.rol_roleTable();
                    $("input[name=role_name]").val("");
                    $("#combotree").bootstrapCombotree("setValue","");
                    flag = 0;

                }

            },
            error : function () {
                Ewin.alert("操作异常，请联系管理员");
            }
        })
    },
    //角色分配权限
    roleForMenu : function () {
        flag = 0 ;
        var treeObj = $.fn.zTree.getZTreeObj("role_tree");
        var nodes = treeObj.getCheckedNodes(true);
        var row = $('#role_table2').bootstrapTable('getSelections');
        if(nodes.length <= 0){
            Ewin.alert("请权限对应的菜单");
            return;
        }
        if(row.length <= 0){
            Ewin.alert("请选择角色");
            return;
        }
        var ids = "";
        for (var i = 0; i < nodes.length ; i++){
            if(i > 0){
                ids += "," + nodes[i].id;
            }else{
                ids += nodes[i].id ;
            }
        }
        $.ajax({
            url : 'settingRoleMenu',
            type : 'post',
            dataType : 'json',
            data :{
                "menuId" : ids ,
                "roleId" : row[0].role_id,
                "flag" : flag
            },
            success :function (data) {
                if(data.code == 300){
                    Ewin.alert(data.message);
                }else if(data.code == 500){
                    Ewin.alert("操作异常，请联系管理员");
                }else{
                    Ewin.alert(data.message);
                    treeObj.checkAllNodes(false);
                    $("#role_modal").modal("hide");
                    flag = 0;

                }

            },
            error :function () {
                Ewin.alert("添加权限失败，请联系管理员");
            }
        });
    },
    //清空输入框
    clear : function () {
        $("input[name=role_name]").val("");
        $("#combotree").bootstrapCombotree("setValue","");
    },
    //修改操作
    role_edit : function () {
        flag = 1;
        $(".modal-title").html("修改");
        var row = $('#role_table').bootstrapTable('getSelections');
        if(row.length > 1){
            Ewin.alert("不能多选，请重新选择");
            return;
        }else if(row.length <= 0){
            Ewin.alert("请选中需要修改的数据");
            return;
        }
        roleId = row[0].role_id;
        $("input[name=role_name]").val(row[0].role_name);
        var num = row[0].companyNumber;
        var arr = [];
        if(num.indexOf(",") > 0){
            arr = num.split(",");
        }else{
            arr = num;
        }
        this.rol_roleTable();
        $("#combotree").bootstrapCombotree("setValue",arr);
        $("#role_modal").modal("show");
    },
    //删除角色
    role_delete : function (e) {
        e = window.event;
        e.preventDefault();
        var row = $('#role_table').bootstrapTable('getSelections');
        if(row.length <= 0){
            Ewin.alert("请选中需要删除的数据");
            return;
        }
        Confirm.show('提示', '确定删除该角色吗？', {
            'Delete': {
                'primary': true,
                'callback': function() {
                    var ids = ""; //存放多个角色ID
                    for (var i = 0 ; i < row.length ;i++){
                        if(i > 0 ){
                            ids += "," + row[i].role_id;
                        }else{
                            ids += row[i].role_id;
                        }
                    }
                    $.ajax({
                        url : 'deleteRoles',
                        dataType : 'json',
                        type : 'post',
                        data:  {
                            "ids" : ids
                        },
                        success : function (data) {
                            if(data.code == 300){
                                Ewin.alert(data.message);
                            }else if(data.code == 500){
                                Ewin.alert("操作异常，请联系管理员");
                            }else{
                                Ewin.alert(data.message);
                                Confirm.hide();
                                $('#role_table').bootstrapTable('refresh');
                            }

                        },
                        error : function () {
                            Ewin.alert("操作异常，请联系管理员");
                        }
                    })

                }
            }
        });

    }
}