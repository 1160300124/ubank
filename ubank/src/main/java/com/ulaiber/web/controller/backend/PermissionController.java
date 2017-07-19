package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Group;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限设置Controller
 * Created by daiqingwen on 2017/7/18.
 */
@Controller
@RequestMapping("/backend/")
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

    //跳转首页
    @RequestMapping("group")
    public String index(HttpServletRequest request){
        return "group";
    }

    /**
     * 新增集团
     * @param group
     * @param request
     * @return
     */
    @RequestMapping(value = "addGroup", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addGroup(Group group ,HttpServletRequest request ){
        ResultInfo resultInfo = new ResultInfo();
        Group gro = permissionService.searchGroupByName(group.getName().trim()); //根据集团名称查询是否数据已存在
        if(StringUtil.isEmpty(gro)){
            int flag = permissionService.addGroup(group);
            if(flag > 0 ){
                resultInfo.setMessage("新增成功");
                resultInfo.setCode(200);
            }
        } else {
            resultInfo.setCode(300);
            resultInfo.setMessage("集团已存在，请重新填写！");
        }

        return resultInfo;
    }

}
