package com.ulaiber.web.controller.api;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.LeaveService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
     * @return
     */
    @RequestMapping("applyForLeave")
    @ResponseBody
    public ResultInfo applyForLeave( LeaveRecord leaveRecord){
        logger.info("开始保存申请请假记录");
        int result = leaveService.saveLeaveRecord(leaveRecord); //保存申请记录
        ResultInfo resultInfo = new ResultInfo();
        if(leaveRecord.getUserid().equals("")){
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



}
