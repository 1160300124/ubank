package com.ulaiber.web.controller.api;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * 申请请假
     * @param leaveRecord
     * @return ResultInfo
     */
    @RequestMapping("applyForLeave")
    @ResponseBody
    public ResultInfo applyForLeave( LeaveRecord leaveRecord){
        logger.info("开始保存申请请假记录");
        String date = sdf.format(new Date());
        leaveRecord.setCreateDate(date);
        //保存申请记录
        int result = leaveService.saveLeaveRecord(leaveRecord);
        ResultInfo resultInfo = new ResultInfo();
        if(StringUtil.isEmpty(leaveRecord.getUserid())){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("申请失败");
            logger.info("申请请假失败");
            return resultInfo;
        }
        if (result <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("申请失败");
            logger.info("申请失败");
            return resultInfo;
        }
        int result2 = leaveService.saveAditor(leaveRecord);
        if(result2 <=0 ){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("申请失败");
            logger.info("保存审批人失败");
            return resultInfo;
        }
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("申请成功");
        logger.info("申请请假成功");
        return resultInfo;
    }

    /**
     * 查询个人申请记录
     * @param userid 用户id
     * @return ResultInfo
     */
    @RequestMapping("queryApplyRecord")
    @ResponseBody
    public ResultInfo queryApplyRecord(String userid,int pageNum,int pageSize){
        ResultInfo resultInfo = new ResultInfo();
        logger.info("开始查询个人申请记录");
        if(StringUtil.isEmpty(userid)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("加载申请记录失败");
            logger.info("用户ID为空");
            return resultInfo;
        }
        pageNum = (pageNum - 1) * pageSize;
        //查询个人申请记录
        List<ApplyForVO> list = leaveService.queryApplyRecord(userid,pageNum,pageSize);
        if(list.size() <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("加载申请记录失败");
            logger.info("查询申请记录失败");
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
            resultInfo.setMessage("查询申请记录失败");
            logger.info("查询申请记录失败");
            return resultInfo;
        }
        //将审批人放入对应的申请记录中
        for (int i = 0; i < list.size(); i++){
            List<LeaveAudit> resultList = new ArrayList<>();
            ApplyForVO ls = list.get(i);
            for (int j = 0 ; j < list2.size(); j++){
                LeaveAudit la = list2.get(j);
                if(String.valueOf(ls.getId()).equals(la.getRecordNo()) ){
                    resultList.add(la);
                }
                if(!la.getStatus().equals("0") && !la.getStatus().equals("2")){
                    ls.setStatus("1");
                }else{
                    ls.setStatus(la.getStatus());
                }
            }
            ls.setAuditorStatus(resultList);
        }
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("加载申请记录成功");
        resultInfo.setData(list);
        logger.info("加载申请记录成功");
        return resultInfo;
    }

    /**
     * 取消请假申请
     * @param applyId 申请记录ID
     * @return
     */
    @RequestMapping("cancelApply")
    @ResponseBody
    public ResultInfo cancelApply(String applyId){
        logger.info("开始取消请假申请操作");
        ResultInfo resultInfo = new ResultInfo();
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
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("取消请假申请成功");
        logger.info("取消请假申请");
        return resultInfo;
    }

    /**
     * 查询工作提醒
     * @param userId 用户id
     * @return ResultInfo
     */
    @RequestMapping("getWorkRemind")
    @ResponseBody
    public ResultInfo workRemind(String userId){
        ResultInfo resultInfo = new ResultInfo();
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
            remindApplyVO.setDate("");
            remindApplyVO.setType("");
            remindApplyVO.setApplyName("");
            remindApplyVO.setReason("");
        }
        //获取个人审批记录
        List<AuditVO> list2 =leaveService.getLeaveAuditor(userId);
        //审批记录
        RemindAuditVO remindAuditVO = new RemindAuditVO();
        if(list2.size() > 0){
            //申请记录ID
            int id = Integer.parseInt(list2.get(0).getRecordNo());
            //根据申请记录ID获取申请记录
            Map<String,Object> leaveRecord = leaveService.queryApplyRecordById(id);
            remindAuditVO.setCount(list2.size());
            remindAuditVO.setDate(list2.get(0).getAuditDate());
            remindAuditVO.setType(leaveRecord.get("type"));
            remindAuditVO.setApplyName(leaveRecord.get("user_name"));
            remindAuditVO.setReason(leaveRecord.get("reason"));
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
        return resultInfo;
    }

    /**
     * 查询工作审批记录
     * @param userId 用户id
     * @return ResultInfo
     */
    @RequestMapping("getWorkAudit")
    @ResponseBody
    public ResultInfo workAudit(String userId){
        logger.info("开始查询工作审批记录");
        ResultInfo resultInfo = new ResultInfo();
        if(StringUtil.isEmpty(userId)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询工作审批记录数量失败");
            logger.info("用户ID为空");
            return resultInfo;
        }
        //获取待审批记录数量
        List<Map<String,Object>> list = leaveService.getPendingRecord(userId);
        List<WorkAuditRecordVO> resultList = new ArrayList<>();
        WorkAuditRecordVO workAuditRecordVO = new WorkAuditRecordVO();
        if(list.size() >0 ){
            workAuditRecordVO.setMark(IConstants.Pengding_AUDIT_MARK );
            workAuditRecordVO.setCount(list.size());
            workAuditRecordVO.setDate(list.get(0).get("createDate"));
            workAuditRecordVO.setStatus(list.get(0).get("status"));
        }else{
            workAuditRecordVO.setMark(IConstants.Pengding_AUDIT_MARK );
            workAuditRecordVO.setCount(0);
            workAuditRecordVO.setDate("");
            workAuditRecordVO.setStatus("");
        }
       // resultList.add(workAuditRecordVO);
//        List<Map<String,Object>> list2 = leaveService.getAlreadyRecord(userId); //获取已审批记录数量
//        Map<String,Object> alreadyMap = new HashMap<>();
//        if(list2.size() > 0){
//            alreadyMap.put("mark" , IConstants.Already_APPLY_MARK);
//            alreadyMap.put("count" , list2.size());
//            alreadyMap.put("date" , list2.get(0).get("createDate"));
//            alreadyMap.put("status" , list2.get(0).get("status"));
//        }else{
//            alreadyMap.put("mark" , IConstants.Already_APPLY_MARK);
//            alreadyMap.put("count" , null);
//            alreadyMap.put("date" , "");
//            alreadyMap.put("status" ,"");
//        }
//        resultList.add(alreadyMap);
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("查询工作审批记录数量成功");
        resultInfo.setData(workAuditRecordVO);
        logger.info("查询工作审批记录数量成功");
        return resultInfo;
    }

    /**
     * 待审批数据查询
     * @param userId 用户id
     * @param mark   标识
     * @return ResultInfo
     */
    @RequestMapping("queryAuditRecord")
    @ResponseBody
    public ResultInfo queryAuditRecord(String userId,String mark,int pageNum,int pageSize){
        logger.info("开始查询待批复数据");
        ResultInfo resultInfo = new ResultInfo();
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
                for (int i = 0; i < auditorList.size(); i++){
                    int id = Integer.parseInt(auditorList.get(i).getRecordNo());
                    //获取已审批记录
                    ApplyForVO applyForVO = leaveService.queryAlreadRecord(id,userId);
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
                    map.put("auditorStatus",new Object());
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
                    }
                    list.add(map);
                    String recordNo = auditorList.get(i).getRecordNo();
                    //根据申请记录号查询审批人记录
                    List<AuditVO> list2 = leaveService.queryAuditorByRecord(recordNo);
                    for (int j = 0; j < list.size(); j++){
                        Map<String,Object> listMap = list.get(j);
                        if(String.valueOf(listMap.get("id")).equals(auditorList.get(i).getRecordNo())){
                            if(listMap.containsKey("auditorStatus") && listMap.containsKey("status")){
                                listMap.put("auditorStatus" , list2);
                                listMap.put("status",auditorList.get(i).getStatus());
                            }
                        }
                    }
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
        return resultInfo;
    }

    public Map<String,Object> Audit (String userId,List<LeaveAudit> auList,int pageNum,int count){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        for (int i = 0 ; i < auList.size() ; i++){
            if (count >= 10){
                break;
            }
            pageNum += 1;
            resultMap.put("pageNum",pageNum);
            int id = Integer.parseInt(auList.get(i).getRecordNo());
            //根据申请记录ID获取待审批记录
            ApplyForVO applyForVO = leaveService.queryPeningRecord(id,userId);
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
            }
            map.put("auditorStatus",new Object());
            // list.add(map);
            int sort = auList.get(i).getSort();
            int sortValue = sort - 1;
            String recordNo = "";
            String status = "";
            if(sortValue != 0){
                recordNo = auList.get(i).getRecordNo();
                //根据排序号和申请记录编号获取审批人
                List<LeaveAudit> list2 = leaveService.getAuditorBySort(recordNo,sortValue);
                status = list2.get(0).getStatus();
                //如果当前审批人的上一个人没有做审批处理，则移除当前记录
                if(!status.equals("0") && !status.equals("1")){
                    list.add(map);
                    count += 1;
                    resultMap.put("count",count);
                    //根据申请记录号查询审批人记录
                    List<AuditVO> list3 = leaveService.queryAuditorByRecord(recordNo);
                    for (int j = 0 ; j < list.size() ; j++){
                        Map<String,Object> listMap = list.get(j);
                        if(String.valueOf(listMap.get("id")).equals(auList.get(i).getRecordNo())){
                            if(listMap.containsKey("auditorStatus") && listMap.containsKey("status")){
                                listMap.put("auditorStatus" , list3);
                                listMap.put("status","0");
                            }
                        }
                    }

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
                        if(listMap.containsKey("auditorStatus") && listMap.containsKey("status")){
                            listMap.put("auditorStatus" , list4);
                            listMap.put("status","0");
                        }
                    }
                }
            }
        }
        resultMap.put("item",list);
        return resultMap;
    }


    /**
     * 确认审批
     * @param userId 用户id
     * @param recordNo 申请记录id
     * @param status 审批状态：1 审批通过，2 驳回
     * @return ResultInfo
     */
    @RequestMapping("confirmAudit")
    @ResponseBody
    public ResultInfo confirmAudit(String userId,String recordNo,String status,String reason){
        logger.info("开始确认审批操作");
        ResultInfo resultInfo = new ResultInfo();
        if(StringUtil.isEmpty(userId)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("用户ID为空");
            logger.info("用户ID为空");
            return resultInfo;
        }
        if(StringUtil.isEmpty(recordNo)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("申请记录ID为空");
            logger.info("申请记录ID为空");
            return resultInfo;
        }
        if(StringUtil.isEmpty(status)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("审批状态为空");
            logger.info("审批状态为空");
            return resultInfo;
        }
        int result = leaveService.confirmAudit(userId,recordNo,status,reason);
        if(result <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("审批失败");
            logger.info("审批失败");
            return resultInfo;
        }
        //更新申请记录为最新的状态
        int result2 = leaveService.updateRecord(recordNo,status);
        if(result2 <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("审批失败");
            logger.info("审批失败");
            return resultInfo;
        }
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("审批成功");
        logger.info("审批成功");
        return resultInfo;
    }

    /**
     * 获取消息总数
     * @param userId 用户ID
     * @return ResultInfo
     */
    @RequestMapping("messageTotal")
    @ResponseBody
    public ResultInfo messageTotal(String userId){
        logger.info("开始获取消息总数......");
        ResultInfo resultInfo = new ResultInfo();
        if(StringUtil.isEmpty(userId)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("获取消息总数失败");
            logger.info("获取消息总数失败");
            return resultInfo;
        }
        //获取个人申请记录
        List<Map<String,Object>> list = leaveService.getLeaveRecord(userId);
        //获取个人审批记录
        List<AuditVO> list2 =leaveService.getLeaveAuditor(userId);
        int total = list.size() + list2.size();
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("获取消息总数成功");
        logger.info("获取消息总数成功");
        resultInfo.setData(total);
        return resultInfo;
    }

    /**
     * 通讯录数据同步
     * @param date 日期
     * @return ResultInfo
     */
    @RequestMapping("synchronizationData")
    @ResponseBody
    public ResultInfo synchronizationData(String date,int pageNum,int pageSize){
        logger.info(">>>>>>>>>>>>>开始同步数据");
        //根据日期查询用户总数
        int total = leaveService.getUserTotalByDate(date);
        ResultInfo resultInfo = new ResultInfo();
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
        List<User> userList = leaveService.getUserByDate(date,pageNum,pageSize);
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
                        //list.add(userList.get(i));
                        SynchronizationData async = new SynchronizationData();
                        async.setId(userList.get(i).getId());
                        async.setUsername(userList.get(i).getUserName());
                        async.setDeptName(userList.get(i).getDept_name());
                        async.setImage(userList.get(i).getImage());
                        async.setDisabled(userList.get(i).getDisabled());
                        list.add(async);
                    }else{
                        //list2.add(userList.get(i));
                        SynchronizationData async = new SynchronizationData();
                        async.setId(userList.get(i).getId());
                        async.setUsername(userList.get(i).getUserName());
                        async.setDeptName(userList.get(i).getDept_name());
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
                    async.setImage(userList.get(i).getImage());
                    async.setDisabled(userList.get(i).getDisabled());
                    list.add(async);
                }else{
                    SynchronizationData async = new SynchronizationData();
                    async.setId(userList.get(i).getId());
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

        }
    }

    /**
     * 获取用户个推CID
     * @param userid 用户ID
     * @param CID 用户个推CID
     * @return ResultInfo
     */
    @RequestMapping("getClinetID")
    @ResponseBody
    public ResultInfo getClinetID(String userid,String CID){
        logger.info(">>>>>>>>>>>开始插入个推CID");
        ResultInfo resultInfo = new ResultInfo();
        if(StringUtil.isEmpty(userid) || StringUtil.isEmpty(CID)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("获取用户个推CID失败");
            logger.info(">>>>>>>>>>>>>>用户ID或CID为空");
            return resultInfo;
        }
        //修改用户个推CID
        int result = leaveService.updateUser(userid,CID);
        if(result <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("获取用户个推CID失败");
            logger.info(">>>>>>>>>>>>>>修改用户CID失败");
            return resultInfo;
        }
        resultInfo.setCode(IConstants.QT_CODE_ERROR);
        resultInfo.setMessage("获取用户个推CID成功");
        logger.info(">>>>>>>>>>>>>>修改用户CID成功");
        return resultInfo;
    }


}
