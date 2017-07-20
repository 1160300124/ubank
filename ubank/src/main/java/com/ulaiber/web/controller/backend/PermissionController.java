package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限设置Controller
 * Created by daiqingwen on 2017/7/18.
 */
@Controller
@RequestMapping("/backend/")
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

    //跳转集团管理页面
    @RequestMapping("group")
    public String group(HttpServletRequest request){
        return "group";
    }

    //跳转公司管理页面
    @RequestMapping("company")
    public String company(HttpServletRequest request){
        return "company";
    }

    //跳转部门管理页面
    @RequestMapping("department")
    public String department(HttpServletRequest request){
        return "department";
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
    public Map<String,Object> groupQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                         @Param("pageNum") int pageNum ,HttpServletRequest request) {
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int pageTotal = permissionService.getTotal();  //获取总数
        List<Group> resultGroup = permissionService.groupQuery(search,pageSize,pageNum);
        map.put("total",pageTotal);
        map.put("rows",resultGroup);
        return map;

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

    /**
     * 获取所有公司信息
     * @param request
     * @return
     */
    @RequestMapping(value = "companyQuery", method = RequestMethod.POST)
    @ResponseBody
    public List<Company> companyQuery(HttpServletRequest request){
        List<Company> company = permissionService.companyQuery();
        return  company;
    }

    /**
     * 获取所有部门信息
     * @return
     */
    @RequestMapping(value = "departmentQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> departmentQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                             @Param("pageNum") int pageNum ,HttpServletRequest request){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int deptTotal = permissionService.getDeptTotal();   //获取部门总数
        List<Departments> list = permissionService.departmentQuery(search,pageSize,pageNum);
        map.put("total",deptTotal);
        map.put("rows",list);
        return map;
    }

    /**
     * 新增部门
     * @param request
     * @return
     */
    @RequestMapping(value = "addDept", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addDept(@Param("flag") String flag, Departments dept,HttpServletRequest request){
        String deptNum = dept.getName().trim();
        ResultInfo resultInfo = new ResultInfo();
        if(flag.equals("0")){ //新增部门
            Departments departments = permissionService.getDeptByNum(deptNum); //根据部门编号获取对应部门
            if(StringUtil.isEmpty(departments)){
                int result = permissionService.addDept(dept); //新增
                if(result > 0 ){
                    resultInfo.setMessage("新增成功");
                    resultInfo.setCode(200);
                }
            }else{
                resultInfo.setCode(300);
                resultInfo.setMessage("部门已存在，请重新填写！");
            }
        }else{ //修改部门
            int edit = permissionService.editDept(dept);
            if(edit > 0 ){
                resultInfo.setMessage("修改成功");
                resultInfo.setCode(200);
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("修改失败，请联系管理员");
            }
        }
        return resultInfo;
    }

    /**
     * 部门删除
     * @param numbers
     * @return
     */
    @RequestMapping(value = "deleteDept", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteDept(@Param("numbers") String numbers){
        ResultInfo resultInfo = new ResultInfo();
        String[] number = numbers.split(",");
        int result = permissionService.deptDelete(number);
        if(result > 0){
            resultInfo.setMessage("删除成功");
            resultInfo.setCode(200);
        }else{
            resultInfo.setCode(500);
            resultInfo.setMessage("删除失败，请联系管理员");
        }
        return resultInfo;

    }
}
