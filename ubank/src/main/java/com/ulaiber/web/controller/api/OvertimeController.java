package com.ulaiber.web.controller.api;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.OvertimeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 加班Controller
 * Created by daiqingwen on 2017/8/26.
 */
@Controller
@RequestMapping("/api/v1/")
public class OvertimeController extends BaseController {

    private static final Logger logger = Logger.getLogger(OvertimeController.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private OvertimeService overtimeService;

    /**
     * 新增加班记录信息
     * @param applyForVO 个人申请记录
     * @return ResultInfo
     */
    @RequestMapping("addOvertimeRecord")
    @ResponseBody
    public ResultInfo saveOvertimeRecord(ApplyForVO applyForVO){
        logger.debug("开始新增加班记录......");
        String date = sdf.format(new Date());
        applyForVO.setCreateDate(date);
        int result = overtimeService.addOvertimeRecord(applyForVO);  //新增申请记录
        ResultInfo resultInfo = new ResultInfo();
        if(result <= 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增失败");
            logger.debug("新增加班记录失败");
            return resultInfo;
        }
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("新增成功");
        logger.debug("新增加班记录成功");
        return resultInfo;
    }
}
