package com.ulaiber.web.controller.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ulaiber.web.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiniu.util.Auth;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.AuditData;
import com.ulaiber.web.model.AuditVO;
import com.ulaiber.web.model.LeaveAudit;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.OvertimeVO;
import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.Remedy;
import com.ulaiber.web.model.RemindApplyVO;
import com.ulaiber.web.model.RemindAuditVO;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.SalaryRecord;
import com.ulaiber.web.model.SynchronizationData;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.WorkAuditRecordVO;
import com.ulaiber.web.model.attendance.AttendancePatchClock;
import com.ulaiber.web.utils.StringUtil;

/**
 * 申请请假Controller
 * Created by daiqingwen on 2017/8/21.
 */
@Controller
@RequestMapping("/api/v1/")
public class LeaveController extends BaseController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger logger= Logger.getLogger(LeaveController.class);

    @Resource
    private LeaveService leaveService;

    @Resource
    private SalaryAuditService salaryAuditService;

    @Resource
    private ReimbursementService reimbursementService;

    @Resource
    private AttendanceService attendanceService;
    
    @Autowired
    private AnnouncementService service;



    /**
     * 申请请假
     * @param leaveRecord
     * @return ResultInfo
     */
    @RequestMapping(value = "applyForLeave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo applyForLeave( LeaveRecord leaveRecord){
        logger.info(">>>>>>>>>>>开始保存申请请假记录");
        ResultInfo resultInfo = new ResultInfo();
        try {
            String date = sdf.format(new Date());
            leaveRecord.setCreateDate(date);
            //保存申请记录
            int result = leaveService.saveLeaveRecord(leaveRecord);
            if(StringUtil.isEmpty(leaveRecord.getUserid())){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("申请失败");
                logger.info(">>>>>>>>>>>申请请假失败");
                return resultInfo;
            }
            if (result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("申请失败");
                logger.info(">>>>>>>>>>>申请失败");
                return resultInfo;
            }
            int result2 = leaveService.saveAditor(leaveRecord);
            if(result2 <=0 ){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("申请失败");
                logger.info(">>>>>>>>>>>保存审批人失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("申请成功");
            logger.info(">>>>>>>>>>>申请请假成功");
        }catch (Exception e){
            logger.error(">>>>>>>>申请异常信息：" + e);
        }
        return resultInfo;
    }

    /**
     * 查询个人申请记录
     * @param userId 用户id
     * @return ResultInfo
     */
    @RequestMapping(value = "queryApplyRecord", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo queryApplyRecord(String userId,int pageNum,int pageSize){
        ResultInfo resultInfo = new ResultInfo();
        logger.info(">>>>>>>>>>>开始查询个人申请记录");
        try {
            if(StringUtil.isEmpty(userId)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("加载申请记录失败");
                logger.info("用户ID为空");
                return resultInfo;
            }
            pageNum = (pageNum - 1) * pageSize;
            //查询个人申请记录
            List<ApplyForVO> list = leaveService.queryApplyRecord(userId,pageNum,pageSize);
            if(list.size() <= 0){
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("加载申请记录成功");
                logger.info(">>>>>>>>>>>>>>没有数据");
                return resultInfo;
            }
            int[] ids  = new int[list.size()];
            for (int i = 0; i < list.size(); i++){
                ApplyForVO ls = list.get(i);
                ids[i] = ls.getId();
            }
            //查询审批人记录
            List<LeaveAudit> list2 = leaveService.queryAuditor(ids);
            if(list2.size() <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("查询审批人失败");
                logger.info(">>>>>>>>>>查询审批人失败");
                return resultInfo;
            }
            //将审批人放入对应的申请记录中
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++){
                Map<String,Object> resultMap = new HashMap<>();
                List<LeaveAudit> resultList = new ArrayList<>();
                ApplyForVO ls = list.get(i);
                resultMap.put("id" , ls.getId());
                resultMap.put("userid" , ls.getUserid());
                resultMap.put("startDate" , ls.getStartDate());
                resultMap.put("endDate" , ls.getEndDate());
                resultMap.put("leaveTime" , ls.getLeaveTime());
                resultMap.put("auditor" , ls.getAuditor());
                resultMap.put("reason" , ls.getReason());
                resultMap.put("disable" , ls.getDisable());
                resultMap.put("type" , ls.getType());
                resultMap.put("createDate" , ls.getCreateDate());
                resultMap.put("status" , ls.getStatus());
                resultMap.put("currentAuditor",ls.getCurrentAuditor());
                resultMap.put("username" , ls.getUsername());
                resultMap.put("image" , ls.getImage());
                if(ls.getType().equals("0")){  //请假记录
                    Map<String,Object> map = new HashMap<>();
                    map.put("leaveType" , ls.getLeaveType());
                    resultMap.put("leave" , map);
                }else if(ls.getType().equals("1")){  //加班记录
                    OvertimeVO overtimeVO = new OvertimeVO();
                    overtimeVO.setMode(ls.getMode());
                    resultMap.put("overtime" , overtimeVO);
                }else if(ls.getType().equals("2")){   //报销记录
                    //申请记录ID
                    int recordNo = ls.getId();
                    //根据申请记录ID，获取报销记录
                    List<Reimbursement> reimList = reimbursementService.queryReimbersement(recordNo);
                    Map<String,Object> map = new HashMap<>();
                    long amount = 0;  //统计金额
                    for (int k = 0 ; k < reimList.size() ; k++){
                        Reimbursement sr = reimList.get(k);
                        amount += sr.getAmount();
                    }
                    resultMap.put("reimAmount",amount);
                }else if(ls.getType().equals("3")){ //工资发放申请记录
                    //申请记录ID
                    int recordNo = ls.getId();
                    //根据申请记录ID，获取工资发放记录
                    SalaryRecord salary = salaryAuditService.querySalaryByRecordNo(recordNo);
                    resultMap.put("salaryAmount", null == salary ? 0 : salary.getTotalAmount());
                }else if(ls.getType().equals("4")){ //补卡记录
                    Remedy remedy = new Remedy();
                    remedy.setMorning(ls.getMorning());
                    remedy.setAfternoon(ls.getAfternoon());
                    remedy.setType(ls.getRemedyType());
                    remedy.setRemedyDate(ls.getRemedyDate());
                    resultMap.put("remedy" , remedy);
                }
                //将申请记录对应的审批人放入记录中
                for (int j = 0 ; j < list2.size(); j++){
                    LeaveAudit la = list2.get(j);
                    if(String.valueOf(ls.getId()).equals(la.getRecordNo()) ){
                        resultList.add(la);
                    }

                }
                resultMap.put("auditorStatus" , resultList);
                dataList.add(resultMap);
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("加载申请记录成功");
            resultInfo.setData(dataList);
            logger.info("加载申请记录成功");
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("加载申请记录失败");
            logger.error(">>>>>>>>>>加载申请记录失败",e);
        }
        return resultInfo;
    }

    /**
     * 取消申请
     * @param applyId 申请记录ID
     * @return
     */
    @RequestMapping(value = "cancelApply", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo cancelApply(String applyId,String type){
        logger.info("开始取消请假申请操作");
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(StringUtil.isEmpty(applyId)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("取消请假申请失败");
                logger.info("申请记录ID为空");
                return resultInfo;
            }
            int result = leaveService.cancelApply(applyId);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("取消请假申请失败");
                logger.info("取消请假申请失败");
                return resultInfo;
            }
            if(type.equals("4")){
                //根据记录Id查询补卡信息
                int recordNo = Integer.parseInt(applyId);
                Remedy re = leaveService.getRemedyRecordByUserId(recordNo);
                AttendancePatchClock apc =  new AttendancePatchClock();
                long userId = Long.valueOf(re.getUserId());
                apc.setUserId(userId);
                apc.setPatchClockDate(re.getRemedyDate());
                apc.setPatchClockType(Integer.parseInt(re.getType()));
                apc.setPatchClockOnTime(re.getMorning());
                apc.setPatchClockOffTime(re.getAfternoon());
                apc.setPatchClockStatus("3");
                attendanceService.patchClock(apc);
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("取消请假申请成功");
            logger.info("取消请假申请成功");
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("取消请假申请失败");
            logger.error(">>>>>>>>>>取消请假申请失败",e);
        }
        return resultInfo;
    }

    /**
     * 查询工作提醒
     * @param userId 用户id
     * @return ResultInfo
     */
    @RequestMapping(value = "getWorkRemind", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo workRemind(String userId){
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(StringUtil.isEmpty(userId)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("用户ID为空");
                logger.info("用户ID为空");
                return resultInfo;
            }
            //获取个人申请记录
            List<Map<String,Object>> list = leaveService.getLeaveRecord(userId);
            //申请记录
            RemindApplyVO remindApplyVO = new RemindApplyVO();

            if(list.size() > 0 ){
                remindApplyVO.setCount(list.size());
                remindApplyVO.setDate(list.get(0).get("createDate"));
                remindApplyVO.setType(list.get(0).get("type"));
                remindApplyVO.setApplyName(list.get(0).get("user_name"));
                remindApplyVO.setReason(list.get(0).get("reason"));
            }else{
                remindApplyVO.setCount(0);
                remindApplyVO.setType("");
                remindApplyVO.setDate("");
                remindApplyVO.setApplyName("");
                remindApplyVO.setReason("");
            }
            //获取个人审批记录
            List<AuditVO> list2 =leaveService.getLeaveAuditor(userId);
            List<AuditVO> resultList = new ArrayList<>();
            //审批记录
            RemindAuditVO remindAuditVO = new RemindAuditVO();
            if(list2.size() > 0){
//                for (int i = 0 ; i < list2.size() ; i++){
//                    AuditVO au = list2.get(i);
//                    String recordNo = au.getRecordNo();
//                    int sort = au.getSort();
//                    int sortValue = sort - 1;
//                    if(sortValue != 0){
//                        List<LeaveAudit> list3 = leaveService.getAuditorBySort(recordNo,sortValue);
//                        String status = list3.get(0).getStatus();
//                        if(status.equals("1")){
//                            resultList.add(au);
//                        }
//                    }else{
//                        if(au.getStatus().equals("0")){
//                            resultList.add(au);
//                        }
//                    }
//                }
                resultList = auditRecode(list2);
                if(resultList.size() > 0){
                    //申请记录ID
                    int id = Integer.parseInt(resultList.get(0).getRecordNo());
                    //根据申请记录ID获取申请记录
                    Map<String,Object> leaveRecord = leaveService.queryApplyRecordById(id);
                    remindAuditVO.setCount(resultList.size());
                    remindAuditVO.setDate(resultList.get(0).getAuditDate());
                    if (StringUtil.isEmpty(leaveRecord)){
                        remindAuditVO.setType("");
                        remindAuditVO.setApplyName("");
                        remindAuditVO.setReason("");
                    }else{
                        remindAuditVO.setType(leaveRecord.get("type"));
                        remindAuditVO.setApplyName(leaveRecord.get("user_name"));
                        remindAuditVO.setReason(leaveRecord.get("reason"));
                    }

                }else{
                    remindAuditVO.setCount(0);
                    remindAuditVO.setDate("");
                    remindAuditVO.setType("");
                    remindAuditVO.setApplyName("");
                    remindAuditVO.setReason("");
                }
            }else{
                remindAuditVO.setCount(0);
                remindAuditVO.setDate("");
                remindAuditVO.setType("");
                remindAuditVO.setApplyName("");
                remindAuditVO.setReason("");
            }
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("applyForData" , remindApplyVO);
            resultMap.put("auditData" , remindAuditVO);
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("查询个人工作提醒成功");
            resultInfo.setData(resultMap);
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询个人工作提醒失败");
            logger.error(">>>>>>>>>>查询个人工作提醒失败",e);
        }

        return resultInfo;
    }

    /**
     * 获取申请、审批、公告、考勤数量
     * @param userId 用户ID
     * @return ResultInfo
     */
    @RequestMapping(value = "getAllCount", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo allCount(long userId){
        ResultInfo resultInfo = new ResultInfo();
        try {
            //获取个人申请记录数量
            int apply = leaveService.getLeaveRecordCount(userId);
            //获取个人审批数量
            List<AuditVO> list =leaveService.getLeaveAuditor(String.valueOf(userId));
            List<AuditVO> resultList = resultList = auditRecode(list);
            //int audit = leaveService.getLeaveAuditorCount(userId);
            //获取个人公告数量
            int notice = service.getUnreadCountByUserId(userId);
            //获取我的考勤数量
            int attendance = attendanceService.getAbnormalCountByUserId(userId);
            Map<String,Object> map = new HashMap<>();
            map.put("apply",apply);
            map.put("audit",resultList.size());
            map.put("notice",notice);
            map.put("attendance", attendance);
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("查询数量成功");
            resultInfo.setData(map);
            logger.info("查询申请、审批、公告、考勤成功");
        }catch(Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询数量失败");
            logger.error("申请、审批、公告、考勤",e);
        }
        return resultInfo;
    }


    /**
     * 通用获取待审批记录方法
     * @param list
     * @return
     */
    public List<AuditVO> auditRecode(List<AuditVO> list) {
        List<AuditVO> resultList = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i++){
            AuditVO au = list.get(i);
            String recordNo = au.getRecordNo();
            int sort = au.getSort();
            int sortValue = sort - 1;
            if(sortValue != 0){
                List<LeaveAudit> list3 = leaveService.getAuditorBySort(recordNo,sortValue);
                String status = list3.get(0).getStatus();
                if(status.equals("1")){
                    resultList.add(au);
                }
            }else{
                resultList.add(au);
            }
        }
        return resultList;
    }

    /**
     * 查询工作审批记录
     * @param userId 用户id
     * @return ResultInfo
     */
    @RequestMapping(value = "getWorkAudit", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo workAudit(String userId){
        logger.info("开始查询工作审批记录");
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(StringUtil.isEmpty(userId)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("查询工作审批记录数量失败");
                logger.info("用户ID为空");
                return resultInfo;
            }
            //获取待审批记录数量
            // List<Map<String,Object>> list = leaveService.getPendingRecord(userId);
            List<AuditVO> list =leaveService.getLeaveAuditor(userId);
            List<AuditVO> resultList = new ArrayList<>();
            WorkAuditRecordVO workAuditRecordVO = new WorkAuditRecordVO();
            if(list.size() > 0){
//                for (int i = 0 ; i < list.size() ; i++){
//                    AuditVO au = list.get(i);
//                    String recordNo = au.getRecordNo();
//                    int sort = au.getSort();
//                    int sortValue = sort - 1;
//                    if(sortValue != 0){
//                        List<LeaveAudit> list3 = leaveService.getAuditorBySort(recordNo,sortValue);
//                        String status = list3.get(0).getStatus();
//                        if(status.equals("1")){
//                            resultList.add(au);
//                        }
//                    }else{
//                        resultList.add(au);
//                    }
//                }
                resultList = auditRecode(list);
                if(resultList.size() > 0){
                    workAuditRecordVO.setMark(IConstants.Pengding_AUDIT_MARK );
                    workAuditRecordVO.setCount(resultList.size());
                    workAuditRecordVO.setDate(resultList.get(0).getAuditDate());
                    workAuditRecordVO.setStatus(resultList.get(0).getStatus());
                }else{
                    workAuditRecordVO.setMark(IConstants.Pengding_AUDIT_MARK );
                    workAuditRecordVO.setCount(0);
                    workAuditRecordVO.setDate("");
                    workAuditRecordVO.setStatus("");
                }

            }else{
                workAuditRecordVO.setMark(IConstants.Pengding_AUDIT_MARK );
                workAuditRecordVO.setCount(0);
                workAuditRecordVO.setDate("");
                workAuditRecordVO.setStatus("");
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("查询工作审批记录数量成功");
            resultInfo.setData(workAuditRecordVO);
            logger.info("查询工作审批记录数量成功");
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询工作审批记录数量失败");
            logger.error(">>>>>>>>>>查询工作审批记录数量失败",e);
        }
        return resultInfo;
    }

    /**
     * 待审批数据查询
     * @param userId 用户id
     * @param mark   标识 0 待审批，1  已审批
     * @return ResultInfo
     */
    @RequestMapping(value = "queryAuditRecord", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo queryAuditRecord(String userId,String mark,int pageNum,int pageSize){
        logger.info("开始查询待批复数据");
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(mark)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("查询审批数据失败");
                logger.info("用户ID或标识为空");
                return resultInfo;
            }
            int count = 0;
            if(pageNum == 1){
                pageNum = 0;
            }
            if(pageSize == 0){
                pageSize = 20;
            }
            //0 待审批，1 已审批
            if(mark.equals("0")){
                List<Map<String,Object>> list = new ArrayList<>();
                Map<String,Object> resultMap = new HashMap<>();
                while (count < 10){
                    //根据申请记录编号获取待审批人
                    List<LeaveAudit> auList = leaveService.getAuditorByUserId(userId,pageNum,pageSize);
                    if(auList.size() > 0 ){
                        Map<String,Object> auditMap = Audit(userId,auList,pageNum,count);
                        List<Map<String,Object>> auditList = (List<Map<String, Object>>) auditMap.get("item");
                        pageNum = (int) auditMap.get("pageNum");
                        count  = (auditMap.get("count") != "" ? (int) auditMap.get("count") : 0);
                        list.addAll(auditList);
                    }else{
                        resultInfo.setCode(IConstants.QT_CODE_OK);
                        resultInfo.setMessage("查询待审批数据成功");
                        resultMap.put("pageNo",pageNum);
                        resultMap.put("item",list);
                        resultInfo.setData(resultMap);
                        logger.info("查询待审批数据成功");
                        return resultInfo;
                    }
                }
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("查询待审批数据成功");
                resultMap.put("pageNo",pageNum);
                resultMap.put("item",list);
                resultInfo.setData(resultMap);
                resultInfo.setData(resultMap);
                logger.info("查询待审批数据成功");
                return resultInfo;
            }else if(mark.equals("1")){
                //根据申请记录编号获取已审批人
                List<LeaveAudit> auditorList = leaveService.queryAuditorByUserId(userId,pageNum,pageSize);
                List<Map<String,Object>> list = new ArrayList<>();
                if(auditorList.size() > 0){
                    int[] ids = new int[auditorList.size()];
                    for (int i = 0; i < auditorList.size(); i++){
                        ids[i] = Integer.parseInt(auditorList.get(i).getRecordNo());
                    }
                    List<ApplyForVO> applyList = leaveService.queryAlreadRecord(ids);
                    for (int i = 0; i < applyList.size(); i++){
                        ApplyForVO applyForVO = applyList.get(i);
                        //获取已审批记录
                        if(StringUtil.isEmpty(applyForVO)){
                            resultInfo.setCode(IConstants.QT_CODE_OK);
                            resultInfo.setMessage("当前用户没有已审批记录");
                            resultInfo.setData(list);
                            logger.info("查询已审批数据成功");
                            return resultInfo;
                        }
                        Map<String ,Object> map = new HashMap<>();
                        map.put("id" , applyForVO.getId());
                        map.put("userid" , applyForVO.getUserid());
                        map.put("startDate" , applyForVO.getStartDate());
                        map.put("endDate" , applyForVO.getEndDate());
                        map.put("leaveTime" , applyForVO.getLeaveTime());
                        map.put("auditor" , applyForVO.getAuditor());
                        map.put("reason" , applyForVO.getReason());
                        map.put("createDate" , applyForVO.getCreateDate());
                        map.put("status" , applyForVO.getStatus());
                        map.put("type" , applyForVO.getType());
                        map.put("disable" , applyForVO.getDisable());
                        map.put("username",applyForVO.getUsername());
                        map.put("image",applyForVO.getImage());
                        map.put("currentAuditor",applyForVO.getCurrentAuditor());
                        // map.put("auditorStatus",new Object());
                        //判断当前数据属于哪种功能的数据
                        if(applyForVO.getType().equals("0")){  //请假
                            Map<String,Object> leaveMap = new HashMap<>();
                            leaveMap.put("leaveType",applyForVO.getLeaveType());
                            map.put("leave",leaveMap);
                        }else if(applyForVO.getType().equals("1")){  //加班
                            OvertimeVO overtimeVO = new OvertimeVO();
                            // overtimeVO.setHoliday(applyForVO.getHoliday());
                            overtimeVO.setMode(applyForVO.getMode());
                            map.put("overtime" , overtimeVO);
                        }else if(applyForVO.getType().equals("2")){   //报销记录
                            //申请记录ID
                            int recordNo = applyForVO.getId();
                            //根据申请记录ID，获取报销记录
                            List<Reimbursement> reimList = reimbursementService.queryReimbersement(recordNo);
                            Map<String,Object> Map = new HashMap<>();
                            long amount = 0;  //统计金额
                            for (int k = 0 ; k < reimList.size() ; k++){
                                Reimbursement sr = reimList.get(k);
                                amount += sr.getAmount();
                            }
                            map.put("reimAmount",amount);
                        }else if(applyForVO.getType().equals("3")){ //工资发放申请记录
                            //申请记录ID
                            int recordNo = applyForVO.getId();
                            //根据申请记录ID，获取工资发放记录
                            SalaryRecord salary = salaryAuditService.querySalaryByRecordNo(recordNo);
                            map.put("salaryAmount", null == salary ? 0 : salary.getTotalAmount());
                        }else if(applyForVO.getType().equals("4")){ //补卡记录
                            Remedy remedy = new Remedy();
                            remedy.setMorning(applyForVO.getMorning());
                            remedy.setAfternoon(applyForVO.getAfternoon());
                            remedy.setType(applyForVO.getRemedyType());
                            remedy.setRemedyDate(applyForVO.getRemedyDate());
                            map.put("remedy" , remedy);
                        }
                        String recordNo = String.valueOf(applyList.get(i).getId());
                        List<AuditVO> list2 = leaveService.queryAuditorByRecord(recordNo);
                        //根据申请记录号查询审批人记录
                        map.put("auditorStatus",list2);
                        list.add(map);
                    }
                }else{
                    resultInfo.setCode(IConstants.QT_CODE_OK);
                    resultInfo.setMessage("当前用户没有待已审批记录");
                    resultInfo.setData(list);
                    logger.info("查询已审批数据成功");
                    return resultInfo;
                }
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("查询已审批数据成功");
                resultInfo.setData(list);
                logger.info("查询已审批数据成功");
                return resultInfo;
            }
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询已审批数据失败");
            logger.error(">>>>>>>>>>查询已审批数据失败",e);
        }
        return resultInfo;
    }


    /**
     * 循环获取有用的待审批记录
     * @param userId 用户ID
     * @param auList 数据
     * @param pageNum 查询数据起始值
     * @param count 有效数据个数
     * @return  Map<String,Object>
     */
    public Map<String,Object> Audit (String userId,List<LeaveAudit> auList,int pageNum,int count){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        try {
            for (int i = 0 ; i < auList.size() ; i++){
                if (count >= 10){
                    break;
                }
                pageNum += 1;
                resultMap.put("pageNum",pageNum);
                int id = Integer.parseInt(auList.get(i).getRecordNo());
                //根据申请记录ID获取待审批记录
                ApplyForVO applyForVO = leaveService.queryPeningRecord(id,userId);
                if(StringUtil.isEmpty(applyForVO)){
                    resultMap.put("item",list);
                    return resultMap;
                }
                Map<String ,Object> map = new HashMap<>();
                map.put("id" , applyForVO.getId());
                map.put("userid" , applyForVO.getUserid());
                map.put("startDate" , applyForVO.getStartDate());
                map.put("endDate" , applyForVO.getEndDate());
                map.put("leaveTime" , applyForVO.getLeaveTime());
                map.put("auditor" , applyForVO.getAuditor());
                map.put("reason" , applyForVO.getReason());
                map.put("createDate" , applyForVO.getCreateDate());
                map.put("status" , applyForVO.getStatus());
                map.put("type" , applyForVO.getType());
                map.put("disable" , applyForVO.getDisable());
                map.put("username",applyForVO.getUsername());
                map.put("image",applyForVO.getImage());
                map.put("count",count);
                map.put("auditorStatus",new Object());
                map.put("currentAuditor",applyForVO.getCurrentAuditor());
                //判断当前数据属于哪种功能的数据
                if(applyForVO.getType().equals("0")){  //请假
                    Map<String,Object> leaveMap = new HashMap<>();
                    leaveMap.put("leaveType",applyForVO.getLeaveType());
                    map.put("leave",leaveMap);
                }else if(applyForVO.getType().equals("1")){  //加班
                    OvertimeVO overtimeVO = new OvertimeVO();
                    // overtimeVO.setHoliday(applyForVO.getHoliday());
                    overtimeVO.setMode(applyForVO.getMode());
                    map.put("overtime" , overtimeVO);
                }else if(applyForVO.getType().equals("2")){   //报销记录
//                //申请记录ID
                    int recordNo = applyForVO.getId();
                    //根据申请记录ID，获取报销记录
                    List<Reimbursement> reimList = reimbursementService.queryReimbersement(recordNo);
                    Map<String,Object> Map = new HashMap<>();
                    long amount = 0;  //统计金额
                    for (int k = 0 ; k < reimList.size() ; k++){
                        Reimbursement sr = reimList.get(k);
                        amount += sr.getAmount();
                    }
                    map.put("reimAmount",amount);
                }else if(applyForVO.getType().equals("3")){ //工资发放申请记录
                    //申请记录ID
                    int recordNo = applyForVO.getId();
                    //根据申请记录ID，获取工资发放记录
                    SalaryRecord salary = salaryAuditService.querySalaryByRecordNo(recordNo);
                    map.put("salaryAmount", null == salary ? 0 : salary.getTotalAmount());
                }else if(applyForVO.getType().equals("4")){ //补卡记录
                    Remedy remedy = new Remedy();
                    remedy.setMorning(applyForVO.getMorning());
                    remedy.setAfternoon(applyForVO.getAfternoon());
                    remedy.setType(applyForVO.getRemedyType());
                    remedy.setRemedyDate(applyForVO.getRemedyDate());
                    map.put("remedy" , remedy);
                }
                int sort = auList.get(i).getSort();
                int sortValue = sort - 1;
                String recordNo = "";
                String status = "";
                if(sortValue != 0){
                    recordNo = auList.get(i).getRecordNo();
                    //根据排序号和申请记录编号获取审批人
                    List<LeaveAudit> list2  = leaveService.getAuditorBySort(recordNo,sortValue);
                    status = list2.get(0).getStatus();
                    if(!status.equals("0") && !status.equals("2")){
                        list.add(map);
                        count += 1;
                        resultMap.put("count",count);
                        //根据申请记录号查询记录审批人
                        List<AuditVO> list3 = leaveService.queryAuditorByRecord(recordNo);
                        //将当前记录的所有审批人放入记录中
                        for (int j = 0 ; j < list.size() ; j++){
                            Map<String,Object> listMap = list.get(j);
                            if(String.valueOf(listMap.get("id")).equals(auList.get(i).getRecordNo())){
                                if(listMap.containsKey("auditorStatus")){
                                    listMap.put("auditorStatus" , list3);
                                }
                            }

                        }

                    }else{
                        resultMap.put("count",count);
                    }
                }else{
                    list.add(map);
                    count += 1;
                    resultMap.put("count",count);
                    recordNo = auList.get(i).getRecordNo();
                    //根据申请记录号查询审批人记录
                    List<AuditVO> list4 = leaveService.queryAuditorByRecord(recordNo);
                    for (int j = 0 ; j < list.size() ; j++){
                        Map<String,Object> listMap = list.get(j);
                        if(String.valueOf(listMap.get("id")).equals(auList.get(i).getRecordNo())){
                            if(listMap.containsKey("auditorStatus") ){
                                listMap.put("auditorStatus" , list4);
                            }
                        }
                    }
                }
            }
            resultMap.put("item",list);
        }catch (Exception e){
            logger.error(">>>>>>>>>>循环获取有用的待审批记录",e);
        }
        return resultMap;
    }


    /**
     * 确认审批
     * @param auditData  确认审批的数据
     * @return
     */
    @RequestMapping(value = "confirmAudit", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo confirmAudit(AuditData auditData){
        logger.info(">>>>>>>>>>>>>开始确认审批操作");
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(StringUtil.isEmpty(auditData.getUserId())){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("用户ID为空");
                logger.error(">>>>>>>>>>>>>用户ID为空");
                return resultInfo;
            }
            if(StringUtil.isEmpty(auditData.getRecordNo())){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("申请记录ID为空");
                logger.info(">>>>>>>>>>>>>申请记录ID为空");
                return resultInfo;
            }
            if(StringUtil.isEmpty(auditData.getStatus())){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("审批状态为空");
                logger.info(">>>>>>>>>>>>>审批状态为空");
                return resultInfo;
            }
            //根据审批状态获取申请记录状态
            LeaveRecord le = leaveService.queryApplyStatus(auditData.getRecordNo());
            if(le.getDisable().equals("1")){
                resultInfo.setCode(IConstants.QT_CODE_CANCEL);
                resultInfo.setMessage("申请记录已取消");
                logger.info(">>>>>>>>>>>>>申请记录已取消");
                return resultInfo;
            }
            int result = leaveService.confirmAudit(auditData);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("审批失败");
                logger.info(">>>>>>>>>>>>>审批失败");
                return resultInfo;
            }
            //更新申请记录为最新的状态
            int result2 = leaveService.updateRecord(auditData);
            if(result2 <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("审批失败");
                logger.error(">>>>>>>>>>>>>审批失败");
                return resultInfo;
            }
            //当前为工资审批
            if (auditData.getType().equals("3")){
                if (auditData.getStatus().equals("1")){
                    SalaryRecord record = salaryAuditService.querySalaryByRecordNo(Integer.parseInt(auditData.getRecordNo()));
                    //TODO
//            		salaryService.pay(record.getSalaryId());
                }
            }
            //当前为补卡审批才执行以下代码
            if(auditData.getType().equals("4")){
                //如果status不为0，则更新补卡状态
                if(!auditData.getStatus().equals("0")){
                    //根据记录Id查询补卡信息
                    int recordNo = Integer.parseInt(auditData.getRecordNo());
                    Remedy re = leaveService.getRemedyRecordByUserId(recordNo);
                    AttendancePatchClock apc =  new AttendancePatchClock();
                    long userId = Long.valueOf(re.getUserId());
                    apc.setUserId(userId);
                    apc.setPatchClockDate(re.getRemedyDate());
                    apc.setPatchClockType(Integer.parseInt(re.getType()));
                    apc.setPatchClockOnTime(re.getMorning());
                    apc.setPatchClockOffTime(re.getAfternoon());
                    apc.setPatchClockStatus(auditData.getStatus());
                    attendanceService.patchClock(apc);
                }
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("审批成功");
            logger.info(">>>>>>>>>>>>>审批成功");
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("审批失败");
            logger.error(">>>>>>>>>>确认审批失败",e);
        }
        return resultInfo;
    }

    /**
     * 获取消息总数
     * @param userId 用户ID
     * @return ResultInfo
     */
    @RequestMapping(value = "messageTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo messageTotal(String userId){
        logger.info("开始获取消息总数......");
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(StringUtil.isEmpty(userId)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("获取消息总数失败");
                logger.info("获取消息总数失败");
                return resultInfo;
            }
            //获取个人申请记录
            List<Map<String,Object>> list = leaveService.getLeaveRecord(userId);
            //获取个人审批记录
            List<AuditVO> list2 = leaveService.getLeaveAuditor(userId);
            List<AuditVO> resultList = new ArrayList<>();
            for (int i = 0 ; i < list2.size() ; i++){
                AuditVO au = list2.get(i);
                String recordNo = au.getRecordNo();
                int sort = au.getSort();
                int sortValue = sort - 1;
                if(sortValue != 0){
                    List<LeaveAudit> list3 = leaveService.getAuditorBySort(recordNo,sortValue);
                    String status = list3.get(0).getStatus();
                    if(status.equals("1")){
                        resultList.add(au);
                    }
                }else{
                    if(au.getStatus().equals("0")){
                        resultList.add(au);
                    }
                }
            }
            int total = list.size() + resultList.size();
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("获取消息总数成功");
            logger.info("获取消息总数成功");
            resultInfo.setData(total);
        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("获取消息总数失败");
            logger.error(">>>>>>>>>>获取消息总数失败",e);
        }
        return resultInfo;
    }

    /**
     * 通讯录数据同步
     * @param date 日期
     * @return ResultInfo
     */
    @RequestMapping(value = "synchronizationData", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo synchronizationData(String date,String companyNumber,int pageNum,int pageSize){
        logger.info(">>>>>>>>>>>>>开始同步数据");
        ResultInfo resultInfo = new ResultInfo();
        try {
            //根据日期查询用户总数
            int total = leaveService.getUserTotalByDate(date,companyNumber);
            if(total <= 0){
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("暂时没有可更新数据");
                logger.info(">>>>>>>>>>>>>暂时没有可更新数据");
                return resultInfo;
            }
            if(pageSize <= 0){
                pageSize = 10;
            }
            if (pageNum < 0){
                pageNum = 0;
            }
            // true 表示数据已同步完成； false 表示还有数据需要同步；
            boolean flag = false;
            // 最新的日期
            String lastDate = "";
            //根据日期分页查询用户
            List<User> userList = leaveService.getUserByDate(date,companyNumber,pageNum,pageSize);
            //存放增加或修改的数据
            List<SynchronizationData> list = new ArrayList<>();
            //存放删除的数据
            List<SynchronizationData> list2 = new ArrayList<>();
            Map<String,Object> resultMap = new HashMap<>();
            int pageTotal = userList.size();
            //如果总数大于分页查询的总数，则表示还有数据
            if((total - pageTotal) > 0){
                if(userList.size() > 0){
                    // 获取最后那条记录的创建日期
                    User us = userList.get(userList.size()-1);
                    lastDate = us.getCreateTime();
                    for (int i = 0 ; i < userList.size() ; i++){
                        User user = userList.get(i);
                        if(user.getDisabled().equals("0")){
                            SynchronizationData async = new SynchronizationData();
                            async.setId(userList.get(i).getId());
                            async.setUsername(userList.get(i).getUserName());
                            async.setDeptName(userList.get(i).getDept_name());
                            async.setMobile(userList.get(i).getMobile());
                            async.setImage(userList.get(i).getImage());
                            async.setDisabled(userList.get(i).getDisabled());
                            list.add(async);
                        }else{
                            SynchronizationData async = new SynchronizationData();
                            async.setId(userList.get(i).getId());
                            async.setUsername(userList.get(i).getUserName());
                            async.setDeptName(userList.get(i).getDept_name());
                            async.setMobile(userList.get(i).getMobile());
                            async.setImage(userList.get(i).getImage());
                            async.setDisabled(userList.get(i).getDisabled());
                            list2.add(async);

                        }
                    }
                    resultMap.put("NewAndUpdate" , list);
                    resultMap.put("Delete" , list2);
                    resultMap.put("LastDate",lastDate);
                    resultMap.put("flag",flag);
                    resultInfo.setCode(IConstants.QT_CODE_OK);
                    resultInfo.setMessage("同步数据成功");
                    resultInfo.setData(resultMap);
                    logger.info(">>>>>>>>>>>>>同步数据成功");
                    return resultInfo;
                }else{
                    flag = true;
                    resultInfo.setCode(IConstants.QT_CODE_OK);
                    resultInfo.setMessage("同步数据成功");
                    logger.info(">>>>>>>>>>>>>同步数据成功");
                    return resultInfo;
                }

            }else{
                flag = true;
                // 获取最后那条记录的创建日期
                User us = userList.get(userList.size()-1);
                lastDate = us.getCreateTime();
                for (int i = 0 ; i < userList.size() ; i++){
                    User user = userList.get(i);
                    if(user.getDisabled().equals("0")){
                        SynchronizationData async = new SynchronizationData();
                        async.setId(userList.get(i).getId());
                        async.setUsername(userList.get(i).getUserName());
                        async.setDeptName(userList.get(i).getDept_name());
                        async.setMobile(userList.get(i).getMobile());
                        async.setImage(userList.get(i).getImage());
                        async.setDisabled(userList.get(i).getDisabled());
                        list.add(async);
                    }else{
                        SynchronizationData async = new SynchronizationData();
                        async.setId(userList.get(i).getId());
                        async.setUsername(userList.get(i).getUserName());
                        async.setDeptName(userList.get(i).getDept_name());
                        async.setMobile(userList.get(i).getMobile());
                        async.setImage(userList.get(i).getImage());
                        async.setDisabled(userList.get(i).getDisabled());
                        list2.add(async);
                    }
                }
                resultMap.put("NewAndUpdate" , list);
                resultMap.put("Delete" , list2);
                resultMap.put("LastDate",lastDate);
                resultMap.put("flag",flag);
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("同步数据成功");
                resultInfo.setData(resultMap);
                logger.info(">>>>>>>>>>>>>同步数据成功");

            }
        }catch(Exception e){
            logger.error(">>>>>>>>>>>通讯录数据同步失败：",e);
        }
        return resultInfo;
    }

    /**
     * 获取用户个推CID
     * @param userId 用户ID
     * @param CID 用户个推CID
     * @return ResultInfo
     */
    @RequestMapping(value = "getClinetID", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getClinetID(String userId,String CID){
        logger.info(">>>>>>>>>>>开始插入个推CID");
        ResultInfo resultInfo = new ResultInfo();
        try {
            //修改用户个推CID
            int result = leaveService.updateUser(userId,CID);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("更新用户个推CID失败");
                logger.info(">>>>>>>>>>>>>>修改用户CID失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("更新用户个推CID成功");
            logger.info(">>>>>>>>>>>>>>修改用户CID成功");
        }catch(Exception e){
            logger.error(">>>>>>>>>>获取用户个推CID失败：",e);

        }
        return resultInfo;
    }


    /**
     * 根据类型，获取申请记录详情
     * @param type 申请记录类型
     * @param recordNo 申请记录ID
     * @return ResultInfo
     */
    @RequestMapping(value = "getApplyDetails", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getApplyDetails(String type,int recordNo){
        logger.info(">>>>>>>>>>>开始查询申请记录详情");
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(type.equals("2")){  //报销记录
                //根据申请记录ID，获取报销记录
                List<Reimbursement> reimList = reimbursementService.queryReimbersement(recordNo);
                //拼接图片地址： 域名 + 图片名称
                Map<String,Object> configMap = StringUtil.loadConfig();
                String url = (String) configMap.get("QinNiuYun");
                //String url = "http://owgz2pijp.bkt.clouddn.com/";
                for (int i = 0 ; i < reimList.size() ; i++){
                    Reimbursement re = reimList.get(i);
                    if(!StringUtil.isEmpty(re.getImages())){
                        String[] img = re.getImages().split(",");
                        String images = "";
                        for (int j = 0 ; j < img.length ; j++){
                            if(j > 0){
                                images += "," + url + img[j];
                            }else{
                                images += url + img[j] ;
                            }
                        }
                        re.setImages(images);
                    }

                }
                if(reimList.size() <= 0){
                    resultInfo.setCode(IConstants.QT_CODE_OK);
                    resultInfo.setMessage("暂时没有报销记录");
                    return resultInfo;
                }
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("查询成功");
                resultInfo.setData(reimList);
                return resultInfo;
            }else if(type.equals("3")){ //工资发放审批记录
                //根据申请记录ID，获取工资发放审批记录
            	List<Map<String, Object>> details = salaryAuditService.getSalaryDetailByRecordNo(recordNo);
                if(details == null){
                    resultInfo.setCode(IConstants.QT_CODE_OK);
                    resultInfo.setMessage("暂时没有工资发放记录");
                    logger.info(">>>>>>>>>>>>>暂时没有工资发放记录");
                    return resultInfo;
                }
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("查询成功");
                resultInfo.setData(details);
                logger.info(">>>>>>>>>>>>>>>获取工资发放记录成功");
                return resultInfo;
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取申请记录详情失败：",e);
        }
        return resultInfo;
    }

    /**
     * 获取七牛云上传文件token
     * @return ResultInfo
     */
    @RequestMapping(value = "getToken", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo createToken(){
        logger.info(">>>>>>>>>>>>>>开始获取七牛云上传文件token");
        ResultInfo re = new ResultInfo();
        try {
            //设置好账号的ACCESS_KEY和SECRET_KEY
            String accessKey = "GsEHlVlmMBEt4Swq_G-A5FttePWwi1lKwodjomoB";
            String secretKey = "y3aVnN1bDCxjWd7wuFUf-aUQ0ld-8VxjBqrJcoUg";
            //要上传的空间
            String bucket = "ubank-images1";
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            if(StringUtil.isEmpty(upToken)){
                re.setCode(IConstants.QT_CODE_ERROR);
                re.setMessage("获取七牛云token失败");
                logger.info(">>>>>>>>>>>>获取七牛云token失败");
                return re;
            }
            re.setCode(IConstants.QT_CODE_OK);
            re.setMessage("获取七牛云token成功");
            re.setData(upToken);
            logger.info(">>>>>>>>>>>>>获取七牛云token成功");
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取七牛云上传文件token：",e);
        }
        return re;
    }

    /**
     * 新增补卡记录
     * @param remedy
     * @return ResultInfo
     */
    @RequestMapping(value = "addRemedy", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addRemedy(Remedy remedy){
        logger.info(">>>>>>>>>>>开始新增补卡记录");
        ResultInfo resultInfo = new ResultInfo();
        try {
            //新增补卡记录
            int result = leaveService.insertRemedy(remedy);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("新增失败");
                logger.info(">>>>>>>>>>>>新增补卡记录失败");
                return resultInfo;
            }
            //补卡
            AttendancePatchClock apc =  new AttendancePatchClock();
            long userId = Long.valueOf(remedy.getUserId());
            apc.setUserId(userId);
            apc.setPatchClockDate(remedy.getRemedyDate());
            apc.setPatchClockType(Integer.parseInt(remedy.getType()));
            apc.setPatchClockOnTime(remedy.getMorning());
            apc.setPatchClockOffTime(remedy.getAfternoon());
            apc.setPatchClockStatus("0");
            boolean flag = attendanceService.patchClock(apc);
            System.out.print(">>>>>>>>>>补卡结果：" + flag);
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("新增成功");
            logger.info(">>>>>>>>>>>>新增补卡记录成功");
        }catch (Exception e){
            logger.error(">>>>>>>>>>新增补卡记录：",e);
        }
        return resultInfo;
    }




}
