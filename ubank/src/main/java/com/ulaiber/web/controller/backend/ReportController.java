package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.service.ReportService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * 申请记录查询
     * @param leaveReportVO
     * @param pageSize
     * @param sysflag
     * @param pageNum
     * @param groupNumber
     * @return map
     */
    @RequestMapping("leaveQuery")
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
        int total = reportService.getLeaveCount(sysflag,groupNumber); //获取申请记录总数
        String[] comArr = companyNumber.split(",");
        List<LeaveReturnVO> list = reportService.leaveQuery(leaveReportVO,sysflag,groupNumber,comArr);
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
                username = username.substring(0,(username.length() - 1));
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
        Map<String,Object> map = new HashMap<>();
        map.put("total" , total);
        map.put("rows" , list);
        return map;

    }


}
