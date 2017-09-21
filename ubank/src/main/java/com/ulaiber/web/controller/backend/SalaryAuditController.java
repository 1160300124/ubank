package com.ulaiber.web.controller.backend;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.SalaryAuditVO;
import com.ulaiber.web.service.SalaryAuditService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 工资审批Controller
 * Created by daiqingwen on 2017/9/21.
 */
@Controller
@RequestMapping("/backend/")
public class SalaryAuditController extends BaseController {

    public static final Logger logger = Logger.getLogger(SalaryAuditController.class);

    @Resource
    private SalaryAuditService salaryAuditService;

    /**
     * 工资发放申请
     * @param salaryAuditVO
     * @return ResultInfo
     */
    @RequestMapping(value = "salaryAudit", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo salaryAudit(SalaryAuditVO salaryAuditVO){
        logger.info(">>>>>>>>>>>>开始工资发放申请");
        int result = salaryAuditService.salaryAudit(salaryAuditVO);
        ResultInfo resultInfo = new ResultInfo();
        if(result <= 0 ){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("工资发放申请失败");
            return resultInfo;
        }
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("工资发放申请成功");
        logger.info(">>>>>>>>>>>工资发放申请成功");
        return resultInfo;
    }

}
