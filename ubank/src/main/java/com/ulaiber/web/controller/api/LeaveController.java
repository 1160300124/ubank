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
import java.util.ArrayList;
import java.util.List;

/**
 * 申请请假Controller
 * Created by daiqingwen on 2017/8/21.
 */
@Controller
@RequestMapping("/api/v1/")
public class LeaveController extends BaseController {
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
     * @param userid
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
     * @param applyId
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
     * @param userId
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

}
