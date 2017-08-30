package com.ulaiber.web.controller.api;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.service.OvertimeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 加班Controller
 * Created by daiqingwen on 2017/8/26.
 */
@Controller
@RequestMapping("/api/v1/")
public class OvertimeController extends BaseController {

    @Resource
    private OvertimeService overtimeService;

    @RequestMapping("addOvertimeRecord")
    @ResponseBody
    public void saveOvertimeRecord(){

    }
}
