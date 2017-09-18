<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/header.jsp" %>
<!-- 公司页 -->
<div class="page-content">
    <%--工具栏--%>
    <div id="wages_calc_Toolbar" class="btn-group">
        <button  onclick="WagesCalcFun.openAdd()" type="button" class="btn btn-default">
            <span class="fa icon-plus" aria-hidden="true"></span>新增
        </button>
        <button onclick="WagesCalcFun.openEdit()" type="button" class="btn btn-default">
            <span class="fa icon-edit" aria-hidden="true"></span>修改
        </button>
        <button onclick="WagesCalcFun.deleteCompany(window.event)" type="button" class="btn btn-default">
            <span class="fa icon-remove" aria-hidden="true"></span>删除
        </button>
        <button onclick="WagesCalcFun.companyQuery()" type="button" class="btn btn-default">
            <span class="fa icon-upload" aria-hidden="true"></span>上传
        </button>
    </div>

    <%--数据表格--%>
    <table id="wages_calc_table" > </table>

    <!-- 弹出框（Modal） -->
    <div id="wages_calc_modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="form-inline base-form clearfix">
                        <form id="company_form" method="post" class="form-horizontal" >
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">工资计算模板名称</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="companyName" name="name"  >
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">选择公司</label>
                                <div class="col-md-9">
                                    <select class="combobox form-control" id="group_select"  name="group_num" >
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">考勤规则</label>
                                <div class="col-md-9">
                                    <a href="javascript:WagesCalcFun.openAttendance();">配置</a>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">请假规则</label>
                                <div class="col-md-9">
                                    <a href="javascript:$('#leaveConfigModal').modal('show');">配置</a>
                                </div>
                            </div>
                            <div class="form-group col-md-12">

                                <label class="col-md-3" for="exampleInputName2">加班规则</label>
                                <div class="col-md-9">
                                    <input type="checkbox" style="width:15px;"/>按加班时长增加加班费
                                </div>

                            </div>
                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">社保缴纳金额</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" id="companyName" placeholder="请输入金额" name="name"  >
                                </div>
                            </div>

                            <div class="form-group col-md-12">
                                <label class="col-md-3" for="exampleInputName2">个税起征点</label>
                                <div class="col-md-9">
                                    <input type="radio" name="tax" style="width:15px;">3500(国籍)&nbsp;&nbsp;&nbsp;<a href="javascript:$('#taxModal').modal('show');">个税表</a><br>

                                    <input type="radio" name="tax" style="width:15px;">4500(外籍)
                                </div>
                            </div>
                        </form>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="CompanyFun.wagesCalcAdd()" class="btn btn-primary">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="attendanceConfigModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="min-width:780px;">
            <div class="modal-content" style="background-color: #EEEEEE;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">考勤计算规则</h4>
                </div>
                <div class="modal-body">
                    <div class="col-md-12">
                        <div class="col-md-6">
                            <div style="margin-right:10px">
                                <h5>迟到</h5>
                                <table id="late_table">
                                    <thead>
                                        <tr>
                                            <td>
                                                迟到时间（分钟）
                                                <div class="des">大于等于</div>
                                            </td>
                                            <td>扣除</td>
                                            <td>单位</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <button onclick="WagesCalcFun.addWagesCalcLate()" class="btn btn-default">添加更多迟到项</button>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div>
                                <h5>早退</h5>
                                <table id="earlyLeave_table">
                                    <thead>
                                        <tr>
                                            <td>
                                                早退时间（分钟）<br/>
                                                大于等于
                                            </td>
                                            <td>扣除</td>
                                            <td>单位</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        
                                    </tbody>
                                </table>
                                <button onclick="WagesCalcFun.addWagesCalcEarlyLeave()" class="btn btn-default">添加更多迟到项</button>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-12" style="margin-top:20px;">
                        <div class="col-md-6">
                            <div style="margin-right:10px;">
                                <h5>忘打卡</h5>
                                <table>
                                    <tr>
                                        <td>选项</td>
                                        <td>值</td>
                                        <td>单位</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="checkbox">允许缺卡次数
                                        </td>
                                        <td>
                                            <input type="text" class="form-control">
                                        </td>
                                        </td>
                                        <td>
                                            次
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="checkbox">缺卡扣除/次
                                        </td>
                                        <td>
                                            <input type="text" class="form-control">
                                        </td>
                                        </td>
                                        <td>
                                            <select name="" id="" class="form-control">
                                                <option value="1">元</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div>
                                <h5>未打卡</h5>
                                <table>
                                    <tr>
                                        <td>选项</td>
                                        <td>值</td>
                                        <td>单位</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="checkbox">未打卡/次
                                        </td>
                                        <td>
                                            <input type="text" class="form-control">
                                        </td>
                                        </td>
                                        <td>
                                            <select name="" id="" class="form-control">
                                                <option value="1">元</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="WagesCalcFun.saveWagesCalc()">保存</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="leaveConfigModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">请假计算规则</h4>
                </div>
                <div class="modal-body">
                    <table >
                        <thead>
                            <tr>
                                <td>事项</td>
                                <td>扣除项</td>
                                <td>单位</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>事假</td>
                                <td><input type="text" class="form-control" value="100" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>病假</td>
                                <td><input type="text" class="form-control" value="50" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>年假</td>
                                <td><input type="text" class="form-control" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>调休</td>
                                <td><input type="text" class="form-control" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>婚假</td>
                                <td><input type="text" class="form-control" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>产假</td>
                                <td><input type="text" class="form-control" value="0" /></td>
                                <td>%</td>
                            </tr>
                            <tr>
                                <td>其他</td>
                                <td><input type="text" class="form-control" value="100" /></td>
                                <td>%</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="WagesCalcFun.saveWagesCalc()">保存</button>
                </div>
            </div>
        </div>
    </div><!-- 模态框（Modal） -->
	<div class="modal fade" tabindex="-1" role="dialog" id="taxModal" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width:60%;min-width:600px;max-width:900px;">
            <div class="modal-content">
                <div class="modal-body" style="padding:0px;">
                    <table id="tax_table" border="1">
                        <thead>
                            <tr>
                                <td>级数</td>
                                <td>应纳税所得额（含税）</td>
                                <td>应纳税所得额（不含税）</td>
                                <td>税率（%）</td>
                                <td>速算扣除数</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>不超过1500元的</td>
                                <td>不超过1455元的</td>
                                <td>3</td>
                                <td>0</td>
                            </tr>
                            <tr>
                                <td>7</td>
                                <td>超过1500元至4,500的部分</td>
                                <td>超过1455元至4,155元的部分</td>
                                <td>10</td>
                                <td>105</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>超过4,500元至9,000的部分</td>
                                <td>超过4,155元至7,755元的部分</td>
                                <td>20</td>
                                <td>555</td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>超过9,000元至35,000的部分</td>
                                <td>超过7,755元至27,255元的部分</td>
                                <td>25</td>
                                <td>1,005</td>
                            </tr>
                            <tr>
                                <td>4</td>
                                <td>超过35,000元至55,000的部分</td>
                                <td>超过27,255元至41,255元的部分</td>
                                <td>30</td>
                                <td>2,755</td>
                            </tr>
                            <tr>
                                <td>5</td>
                                <td>超过55,000元至80,000的部分</td>
                                <td>超过41,255元至57,505元的部分</td>
                                <td>35</td>
                                <td>5,505</td>
                            </tr>
                            <tr>
                                <td>6</td>
                                <td>超过80,000的部分</td>
                                <td>超过57,505的部分</td>
                                <td>45</td>
                                <td>13,505</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/wagesCalc.js" type="text/javascript"></script>
<style>
    thead td{
        vertical-align: top;
    }
    tbody td{
        height: 40px;
    }
    td:nth-child(1) {
        text-align: left;
    }
    td{
        width:120px;
        text-align: center;
        font-size: 12px;
    }
    td>.form-control{
        width:80px;
        display: inline-block;
    }
    td>select{
        width:50px;
    }
    .des{
        color:#999;
    }
    h5{
        text-align: center;
        color: #FC7334 ;
        margin-top: 0px;
    }
    .del{
        width:20px;
    }
    .col-md-6{
        padding: 0px;
    }
    .col-md-6>div{
        background: #fff;
        border-radius: 10px;
        padding: 20px;
        text-align: center;
    }
    table{
        margin-bottom: 20px
    }
    #tax_table{
        width: 100%;
        text-align: center;
        margin: 0px;
    }
    #tax_table td{
        width: auto;
    }
    #tax_table thead{
        background-color:  -webkit-gradient(linear, left top, left bottom, from(#fff), to(#90df96));
        background-image: -webkit-linear-gradient(top, #fff, #90df96);
        background-image: -moz-linear-gradient(top, #fff, #90df96);
        background-image: linear-gradient(to bottom, #fff, #90df96);
    }
</style>
<%@ include file="/WEB-INF/pages/footer.jsp" %>