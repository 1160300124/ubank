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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

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
        int result = leaveService.saveLeaveRecord(leaveRecord); //保存申请记录
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
            logger.info("申请请假失败");
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
    public ResultInfo queryApplyRecord(String userid){
        ResultInfo resultInfo = new ResultInfo();
        logger.info("开始查询个人申请记录");
        if(StringUtil.isEmpty(userid)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("加载申请记录失败");
            logger.info("用户ID为空");
            return resultInfo;
        }
        List<LeaveRecord> list = leaveService.queryApplyRecord(userid); //查询个人申请记录
        if(list.size() <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("加载申请记录失败");
            logger.info("查询申请记录失败");
            return resultInfo;
        }
        String[] ids  = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            LeaveRecord ls = list.get(i);
            ids[i] = ls.getUserid();
        }
        List<LeaveAudit> list2 = leaveService.queryAuditor(ids); //查询审批人记录
        if(list2.size() <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询申请记录失败");
            logger.info("查询申请记录失败");
            return resultInfo;
        }
        //将审批人放入对应的申请记录中
        List<LeaveAudit> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            LeaveRecord ls = list.get(i);
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
        int result2 = leaveService.cancelApplyAudit(applyId); //取消请假审批人
        if(result2 <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("取消请假申请失败");
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
     * @return ReturnData
     */
    @RequestMapping("getWorkRemind")
    @ResponseBody
    public ReturnData workRemind(String userId){
        ReturnData returnData = new ReturnData();
        if(StringUtil.isEmpty(userId)){
            returnData.setCode(IConstants.QT_CODE_ERROR);
            returnData.setMessage("查询工作提醒失败");
            logger.info("用户ID为空");
            return returnData;
        }
        List<ApplyForVO> list = leaveService.getLeaveRecord(userId); //获取个人申请记录
        if(list.size() <= 0 ){
            returnData.setCode(IConstants.QT_CODE_ERROR);
            returnData.setMessage("查询工作提醒失败");
            logger.info("查询个人申请记录失败");
            return returnData;
        }
        List<AuditVO> list2 =leaveService.getLeaveAuditor(userId); //获取个人审批记录
        if(list2.size() <= 0){
            returnData.setCode(IConstants.QT_CODE_ERROR);
            returnData.setMessage("查询工作提醒失败");
            logger.info("查询个人审批记录失败");
            return returnData;
        }
        returnData.setCode(IConstants.QT_CODE_OK);
        returnData.setMessage("查询个人审批记录成功");
        returnData.setApplyForData(list);
        returnData.setAuditData(list2);
        return returnData;
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
        List<Map<String,Object>> list = leaveService.getPendingRecord(userId); //获取待审批记录数量
        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String,Object> pendingMap = new HashMap<>();
        if(list.size() >0 ){
            pendingMap.put("mark" ,IConstants.Pengding_AUDIT_MARK );
            pendingMap.put("count" , list.size());
            pendingMap.put("date" , list.get(0).get("createDate"));
            pendingMap.put("status" , list.get(0).get("status"));
        }else{
            pendingMap.put("mark" ,IConstants.Pengding_AUDIT_MARK );
            pendingMap.put("count" , null);
            pendingMap.put("date" , "");
            pendingMap.put("status" , "");
        }
        resultList.add(pendingMap);
        List<Map<String,Object>> list2 = leaveService.getAlreadyRecord(userId); //获取已审批记录数量
        Map<String,Object> alreadyMap = new HashMap<>();
        if(list2.size() > 0){
            alreadyMap.put("mark" , IConstants.Already_APPLY_MARK);
            alreadyMap.put("count" , list2.size());
            alreadyMap.put("date" , list2.get(0).get("createDate"));
            alreadyMap.put("status" , list2.get(0).get("status"));
        }else{
            alreadyMap.put("mark" , IConstants.Already_APPLY_MARK);
            alreadyMap.put("count" , null);
            alreadyMap.put("date" , "");
            alreadyMap.put("status" ,"");
        }
        resultList.add(alreadyMap);
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("查询工作审批记录数量成功");
        resultInfo.setData(resultList);
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
    public ResultInfo queryAuditRecord(String userId,String mark){
        logger.info("开始查询待批复数据");
        ResultInfo resultInfo = new ResultInfo();
        if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(mark)){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("查询审批数据失败");
            logger.info("用户ID或标识为空");
            return resultInfo;
        }
        //0 待审批，1 已审批
        if(mark.equals("0")){
            List<LeaveAudit> auList = leaveService.getAuditorByUserId(userId);  //根据申请记录编号获取审批人
            List<ApplyForVO> list = new ArrayList<>();
            if(auList.size() > 0){
                for (int i = 0 ; i < auList.size() ; i++){
                    int id = Integer.parseInt(auList.get(i).getRecordNo());
                    ApplyForVO applyForVO = leaveService.queryPeningRecord(id); //获取待审批记录
                    list.add(applyForVO);
                    /**
                     * 获取当前审批人的排序号，判断之前是否有审批人，
                     * 如果没有，则判断当前审批人做判断是否做过审批操作,如果已有审批操作，则移除当前记录；如果没有审批操作，则保留;
                     * 如果有，则获取之前的审批人，判断是否已审批，如果已有审批操作，则移除当前记录；如果没有审批操作，则保留；
                     */
                    int sort = auList.get(i).getSort();
                    int sortValue = sort - 1;
                    String recordNo = "";
                    if(sortValue != 0){
                        recordNo = auList.get(i).getRecordNo();
                        List<LeaveAudit> list2 = leaveService.getAuditorBySort(recordNo,sortValue);  //根据排序号和申请记录编号获取审批人
                        String status = list2.get(0).getStatus();
                        //如果当前审批人的上一个人没有做审批处理，则移除当前记录
                        if(!status.equals("0")){
                            list.remove(i);
                        }
                    }
                    //获取当前记录所有审批人后，放入对应的记录中
                    List<LeaveAudit> list2 = leaveService.queryAuditorByRecord(recordNo); //根据申请记录号查询审批人记录
                    for (int j = 0 ; j < list.size() ; j++){
                        if(String.valueOf(list.get(j).getId()).equals(auList.get(i).getRecordNo())){
                            list.get(j).setAuditorStatus(list2);
                            list.get(j).setStatus("0");
                        }
                    }
                }
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("查询待审批数据成功");
            resultInfo.setData(list);
            logger.info("查询待审批数据成功");
            return resultInfo;
        }else if(mark.equals("1")){
            List<LeaveRecord> list2 = leaveService.queryAlreadRecord(userId); //获取已审批记录
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("查询已审批数据成功");
            resultInfo.setData(list2);
            logger.info("查询已审批数据成功");
            return resultInfo;
        }
        return resultInfo;
    }


}
