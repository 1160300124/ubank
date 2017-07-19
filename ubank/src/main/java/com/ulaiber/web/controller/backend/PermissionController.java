package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Group;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        String remark = request.getParameter("flag");
        ResultInfo resultInfo = new ResultInfo();
        //0 新增操作 ；
        if(remark.equals("0")){
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
        }else{
           // 1 修改操作
            int flag = permissionService.modifyGroup(group);
            if(flag >0){
                resultInfo.setMessage("修改成功");
                resultInfo.setCode(200);
            }else{
                resultInfo.setMessage("修改失败");
                resultInfo.setCode(500);
            }
        }

        return resultInfo;
    }

    /**
     * 查询所有集团
     * @param request
     * @return
     */
    @RequestMapping(value = "groupQuery", method = RequestMethod.POST)
    @ResponseBody
    public List<Group> groupQuery(HttpServletRequest request) {
        List<Group> resultGroup = permissionService.groupQuery();
        return resultGroup;

    }

    /**
     * 删除集团
     * @param numbers
     * @return
     */
    @RequestMapping(value = "deleteGroup", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteGroup(@RequestParam String numbers ){
        ResultInfo resultInfo = new ResultInfo();
        String[] numberArr = numbers.split(",");
        int result = permissionService.deleteGroup(numberArr);
        if(result >0){
            resultInfo.setMessage("删除成功");
            resultInfo.setCode(200);
        }else{
            resultInfo.setMessage("删除失败");
            resultInfo.setCode(500);
        }
        return resultInfo;

    }

}
