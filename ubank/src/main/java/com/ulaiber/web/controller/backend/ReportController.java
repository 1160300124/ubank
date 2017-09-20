package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.service.ReportService;
import com.ulaiber.web.utils.ExportExcel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表Controller
 * Created by daiqingwen on 2017/8/28.
 */
@Controller
@RequestMapping("/backend/")
public class ReportController extends BaseController {

    @Resource
    private ReportService reportService;

    @Resource
    private LeaveService leaveService;

    //跳转请假报表查询页面
    @RequestMapping("leaveReport")
    public String leaveReport(HttpServletRequest request){
        return "leaveReport";
    }

    //跳转请假报表查询页面
    @RequestMapping("reimReport")
    public String reimReport(HttpServletRequest request){
        return "reimReport";
    }

    /**
     * 申请记录查询
     * @param leaveReportVO 请求参数对象
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param sysflag 角色标识
     * @param groupNumber 集团编号
     * @param companyNumber 公司编号
     * @return map
     */
    @RequestMapping(value = "leaveQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> leaveQuery(LeaveReportVO leaveReportVO, @Param("pageSize") int pageSize,
                                         @Param("sysflag") String sysflag, @Param("pageNum") int pageNum,
                                         @Param("groupNumber") String groupNumber,@Param("companyNumber") String companyNumber){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<>();
        int total = reportService.getLeaveCount(sysflag,groupNumber,pageNum,pageSize); //获取申请记录总数
        if(total <= 0){
            map.put("total" , total);
            map.put("rows" , "");
            return map;
        }
        String[] comArr = companyNumber.split(",");
        List<LeaveReturnVO> list = reportService.leaveQuery(leaveReportVO,sysflag,groupNumber,comArr,pageNum,pageSize);
        //根据申请记录ID申请记录审批结果
        if(list.size() > 0){
            for (int i = 0 ; i < list.size(); i++){
                String recordNo = String.valueOf(list.get(i).getId());
                String[] ids = list.get(i).getAuditor().split(",");
                List<Map<String,Object>> user = reportService.getUserById(ids);  //根据用户ID获取用户名
                String username = "";
                for (int l = 0 ; l < user.size() ; l++){
                    username += user.get(l).get("user_name") + ",";
                }
                if(user.size() >0){
                    username = username.substring(0,(username.length() - 1));
                }
                list.get(i).setAuditor(username);
                List<AuditVO> list2 = leaveService.queryAuditorByRecord(recordNo);
                if (list2.size() > 0){
                    int index = 0;
                    for (int j = 0; j < list2.size() ; j++){
                        if( list2.get(j).getSort() > index){
                            index = list2.get(j).getSort();
                        }
                    }
                    for (int k = 0 ; k < list2.size() ; k++){
                        if(list2.get(k).getSort() == index){
                            list.get(i).setResult(list2.get(k).getStatus());
                        }
                    }
                }
            }
        }
        map.put("total" , total);
        map.put("rows" , list);
        return map;

    }

    /**
     * 请假报表导出excel
     * @param request
     * @param response
     * @throws IOException
      */
    @RequestMapping(value = "exportExcel", method = RequestMethod.POST)
    @ResponseBody
    public void Export(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String jsonStr = request.getParameter("json");  //json字符串
        String sheader = request.getParameter("header"); //表格表头
        String fileName = request.getParameter("fileName") + ".xls";  //表格文件名
        String title = request.getParameter("fileName");  // 表格标题名
        ExportExcel exportExcel = new ExportExcel();
        exportExcel.export(jsonStr,sheader,fileName,title,response,request);
    }

    /**
     * 报销报表查询
     * @param leaveReportVO 请求参数对象
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param sysflag 角色标识
     * @param groupNumber 集团编号
     * @param companyNumber 公司编号
     * @return map
     */
    @RequestMapping(value = "reimReportQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>  reimQuery(LeaveReportVO leaveReportVO, @Param("pageSize") int pageSize,
                                         @Param("sysflag") String sysflag, @Param("pageNum") int pageNum,
                                         @Param("groupNumber") String groupNumber,@Param("companyNumber") String companyNumber){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> resultMap = new HashMap<>();
        //获取报销记录总数
        int count = reportService.getReimCount(sysflag,groupNumber,pageNum,pageSize);
        if(count <= 0){
            resultMap.put("total", count);
            resultMap.put("rows", "");
            return resultMap;
        }
        String[] comArr = companyNumber.split(",");
        //报销记录查询
        List<ReimReportVO> list = reportService.reimQuery(leaveReportVO,sysflag,groupNumber,comArr,pageNum,pageSize);
        //获取报销申请记录ID
        int[] ids = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++){
            ReimReportVO re = list.get(i);
            ids[i] = re.getId();
        }
        //根据申请记录ID，获取报销详情
        List<Reimbursement> reimList = reportService.getReimRecordById(ids);
        if(reimList.size() > 0){
            //将报销详情放入对应的报销记录中
            for (int i = 0 ; i < list.size() ; i++){
                ReimReportVO record = list.get(i);
                int total = 0; //报销项数
                int totalAmount = 0; //报销总金额
                for (int j = 0 ; j < reimList.size() ; j++){
                    Reimbursement reim = reimList.get(i);
                    //如果申请记录ID等于报销详情的记录ID，则将每条记录的报销总金额汇总出来，并获取每条记录的报销项数
                    if(record.getId() == reim.getRecordNo()){
                        totalAmount += reim.getAmount();
                        total += 1;
                    }
                }
                record.setTotalAmount(totalAmount);
                record.setCount(total);
            }
        }
        resultMap.put("total" , count);
        resultMap.put("rows" , list);
        return resultMap;

    }

    /**
     * 根据申请记录ID获取报销记录
     * @param id 申请记录ID
     * @return Map<String,Object>
     */
    @RequestMapping(value = "queryReimDetails", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getReimDetails(@Param("id") int id){
        List<Reimbursement> list = reportService.getReimDetailsById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("totol",0);
        map.put("rows",list);
        return map;
    }



}
