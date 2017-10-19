package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.service.ReportService;
import com.ulaiber.web.utils.ExportExcel;
import com.ulaiber.web.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表Controller
 * Created by daiqingwen on 2017/8/28.
 */
@Controller
@RequestMapping("/backend/")
public class ReportController extends BaseController {

    //文件分隔符"\"（在 UNIX 系统中是“/”）
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

    public static final Logger logger = Logger.getLogger(ReportController.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
     * 请假报表导出excel
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "exportExcel", method = RequestMethod.POST)
    @ResponseBody
    public void Export(LeaveReportVO leaveReportVO, @Param("sysflag") String sysflag,
                       @Param("groupNumber") String groupNumber,@Param("companyNumber") String companyNumber,
                       HttpServletRequest request, HttpServletResponse response){
        OutputStream out = null;
        try {
            logger.info(">>>>>>>>开始导出请假报表");
            String jsonStr = request.getParameter("json");  //json字符串
            String sheader = request.getParameter("header"); //表格表头
            String fileName = request.getParameter("fileName")+ sdf.format(new Date()) + ".xls";  //表格文件名
            String title = request.getParameter("fileName");  // 表格标题名
            String[] comArr = companyNumber.split(",");
            //导出报表查询
            List<LeaveReturnVO> list = reportService.reportQuery(leaveReportVO,sysflag,groupNumber,comArr);
            List<LeaveReturnVO> result =  resultDealWith(list);
            //将结果放入List<List<String>>
            List<List<String>> resultList = new ArrayList<List<String>>();
            for (LeaveReturnVO lr : result){
                List<String> leave = new ArrayList<>();
                leave.add(String.valueOf(lr.getId()));
                leave.add((lr.getCompany() != "" && lr.getCompany() != null ?lr.getCompany() : ""));
                leave.add((lr.getStartDate() != "" && lr.getStartDate() != null ?lr.getStartDate() : ""));
                leave.add((lr.getEndDate() != "" && lr.getEndDate() != null ?lr.getEndDate() : ""));
                leave.add((String.valueOf(lr.getLeaveTime()) != "" && String.valueOf(lr.getLeaveTime()) != null ?String.valueOf(lr.getLeaveTime()) : ""));
                leave.add((lr.getReason() != "" && lr.getReason() != null ?lr.getReason() : ""));
                leave.add((lr.getUsername() != "" && lr.getUsername() != null ?lr.getUsername() : ""));
                leave.add(lr.getAuditor());
                String status = "";
                String Result = "";
                if(!StringUtil.isEmpty(lr.getStatus())){
                    if(lr.getStatus().equals("0")){
                        status = "待审批";
                        Result = "正在审批中";
                    }else if(lr.getStatus().equals("1")){
                        status = "审批通过";
                        Result = "审批通过";
                    }else if(lr.getStatus().equals("2")){
                        status = "驳回";
                        Result = "审批不通过";
                    }else{
                        status = "";
                        Result = "";
                    }
                }
                leave.add(status);
                leave.add(Result);
                resultList.add(leave);
            }
            ExportExcel exportExcel = new ExportExcel();
            String docsPath = request.getSession().getServletContext().getRealPath("exportExcel");
            File file = new File(docsPath);
            if(!file.exists()){
                file.mkdirs();
            }
            //文件路径
            String filePath = docsPath + FILE_SEPARATOR + fileName;
            logger.info(">>>>>>>导出路径为：/var/"+fileName+"");
            out = new FileOutputStream("/var/"+fileName+"");
            String[] headers= sheader.substring(0,sheader.length()-1).split(",");
            logger.info(sheader.substring(0,sheader.length()-1));
            exportExcel.exportExcel(title, headers, resultList, out);
            out.close();
            //JOptionPane.showMessageDialog(null, "导出成功!");
            System.out.println(">>>>>>>>>>>>>excel导出成功！");
            exportExcel.download("/var/"+ fileName +"", response);
        }catch(Exception e){
            logger.error(">>>>>>>>导出异常：" , e);
        }finally {
            try {
                if (null != out){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        //根据申请记录ID获取审批结果
        List<LeaveReturnVO> result =  resultDealWith(list);
        map.put("total" , total);
        map.put("rows" , result);
        return map;

    }

    /**
     * 根据申请记录ID获取审批结果
     * @param list
     * @return LeaveReturnVO
     */
    public List<LeaveReturnVO> resultDealWith(List<LeaveReturnVO> list){
        List<LeaveReturnVO> param = new ArrayList<>();
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
                param.add(list.get(i));
            }
        }
        return param;
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
        List<ReimReportVO> resultList =queryReimDetails(list);
//        //获取报销申请记录ID
//        int[] ids = new int[list.size()];
//        for (int i = 0 ; i < list.size() ; i++){
//            ReimReportVO re = list.get(i);
//            ids[i] = re.getId();
//        }
//        //根据申请记录ID，获取报销详情
//        List<Reimbursement> reimList = reportService.getReimRecordById(ids);
//        if(reimList.size() > 0){
//            //将报销详情放入对应的报销记录中
//            for (int i = 0 ; i < list.size() ; i++){
//                ReimReportVO record = list.get(i);
//                int total = 0; //报销项数
//                int totalAmount = 0; //报销总金额
//                for (int j = 0 ; j < reimList.size() ; j++){
//                    Reimbursement reim = reimList.get(i);
//                    //如果申请记录ID等于报销详情的记录ID，则将每条记录的报销总金额汇总出来，并获取每条记录的报销项数
//                    if(record.getId() == reim.getRecordNo()){
//                        totalAmount += reim.getAmount();
//                        total += 1;
//                    }
//                }
//                record.setTotalAmount(totalAmount);
//                record.setCount(total);
//            }
//        }
        resultMap.put("total" , count);
        resultMap.put("rows" , resultList);
        return resultMap;

    }

    /**
     * 根据申请记录ID，获取报销详情
     * @param list
     * @return ReimReportVO
     */
    public List<ReimReportVO> queryReimDetails(List<ReimReportVO> list){
        List<ReimReportVO> resultList = new ArrayList<>();
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
                    Reimbursement reim = reimList.get(j);
                    //如果申请记录ID等于报销详情的记录ID，则将每条记录的报销总金额汇总出来，并获取每条记录的报销项数
                    if(record.getId() == reim.getRecordNo()){
                        totalAmount += reim.getAmount();
                        total += 1;
                    }
                }
                record.setTotalAmount(totalAmount);
                record.setCount(total);
                resultList.add(record);
            }
        }
        return resultList;
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

    /**
     * 报销报表导出excel
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "reimExportExcel", method = RequestMethod.POST)
    @ResponseBody
    public void reimExport(LeaveReportVO leaveReportVO, @Param("sysflag") String sysflag,
                       @Param("groupNumber") String groupNumber,@Param("companyNumber") String companyNumber,
                       HttpServletRequest request, HttpServletResponse response){
        OutputStream out = null;
        try {
            logger.info(">>>>>>>>开始导出报销报表");
            String jsonStr = request.getParameter("json");  //json字符串
            String sheader = request.getParameter("header"); //表格表头
            String fileName = request.getParameter("fileName")+ sdf.format(new Date()) + ".xls";  //表格文件名
            String title = request.getParameter("fileName");  // 表格标题名
            String[] comArr = companyNumber.split(",");
            //报销记录查询
            List<ReimReportVO> list = reportService.reimReportQuery(leaveReportVO,sysflag,groupNumber,comArr);
            List<ReimReportVO> resultList = queryReimDetails(list);
            List<List<String>> result = new ArrayList<List<String>>();
            for (ReimReportVO rv : resultList){
                List<String> reim = new ArrayList<>();
                reim.add(String.valueOf(rv.getId()));
                reim.add(rv.getCreateDate());
                reim.add((rv.getUsername() != "" && rv.getUsername() != null ?rv.getUsername() : ""));
                reim.add((rv.getCompany() != "" && rv.getCompany() != null ? rv.getCompany() : ""));
                reim.add((rv.getDept() != "" && rv.getDept() != null ? rv.getDept() : ""));
                String amount = "";
                if(!StringUtil.isEmpty(rv.getTotalAmount())){
                    amount =  rv.getTotalAmount()/100 + "元";
                }
                reim.add(amount);
                reim.add((rv.getReason() != "" && rv.getReason() != null ? rv.getReason() : ""));
                reim.add(String.valueOf(rv.getCount()));
                String status = "";
                String Result = "";
                if(!StringUtil.isEmpty(rv.getStatus())){
                    if(rv.getStatus().equals("0")){
                        status = "待审批";
                        Result = "正在审批中";
                    }else if(rv.getStatus().equals("1")){
                        status = "审批通过";
                        Result = "审批通过";
                    }else if(rv.getStatus().equals("2")){
                        status = "驳回";
                        Result = "审批不通过";
                    }else{
                        status = "";
                        Result = "";
                    }
                }
                reim.add(status);
                reim.add(Result);
                result.add(reim);
            }
            ExportExcel exportExcel = new ExportExcel();
            String docsPath = request.getSession().getServletContext().getRealPath("exportExcel");
            File file = new File(docsPath);
            if(!file.exists()){
                file.mkdirs();
            }
            //文件路径
          //  String filePath = docsPath + FILE_SEPARATOR + fileName;
            logger.info(">>>>>>>导出路径为：/var/" + fileName);
            out = new FileOutputStream("/var/"+fileName+"");
            String[] headers= sheader.substring(0,sheader.length()-1).split(",");
            logger.info(sheader.substring(0,sheader.length()-1));
            exportExcel.exportExcel(title, headers, result, out);
            out.close();
            JOptionPane.showMessageDialog(null, "导出成功!");
            System.out.println(">>>>>>>>>>>>>excel导出成功！");
            exportExcel.download("/var/"+fileName+"", response);
        }catch(Exception e){
            logger.error(">>>>>>>导出异常：" , e);
        }finally {
            try {
                if (null != out){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
