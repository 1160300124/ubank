package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限设置Controller
 * Created by daiqingwen on 2017/7/18.
 */
@Controller
@RequestMapping("/backend/")
public class PermissionController extends BaseController {

    //跳转首页
    @RequestMapping("group")
    public String index(HttpServletRequest request){
        return "group";
    }

}
