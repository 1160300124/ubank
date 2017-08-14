package com.ulaiber.web.controller.backend;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @Resource
    private UserService userService;

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

    //跳转员工管理页面
    @RequestMapping("employee")
    public String employee(HttpServletRequest request){
        return "employee";
    }

    //跳转角色管理页面
    @RequestMapping("roles")
    public String roles(HttpServletRequest request){
        return "roles";
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
     * 分页查询集团
     * @param
     * @return
     */
    @RequestMapping(value = "groupQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> groupQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                         @Param("pageNum") int pageNum ,@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber) {
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int pageTotal = permissionService.getTotal(sysflag,groupNumber);  //获取总数
        List<Group> resultGroup = permissionService.groupQuery(search,pageSize,pageNum,sysflag,groupNumber);
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
        List<Company> comList = permissionService.queryComByGroupid(numberArr); //根据集团编号查询是否存在公司
        if(comList.size() > 0 ){
            resultInfo.setCode(300);
            resultInfo.setMessage("该集团存在公司，请先删除该集团下的公司");
            return resultInfo;
        }
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
     * 分页查询部门信息
     * @return
     */
    @RequestMapping(value = "departmentQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> departmentQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                              @Param("pageNum") int pageNum ,@Param("sysflag") String sysflag,@Param("companyNumber") String companyNumber){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int deptTotal = permissionService.getDeptTotal();   //获取部门总数
        List<Departments> list = permissionService.departmentQuery(search,pageSize,pageNum,sysflag,companyNumber);
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
                resultInfo.setMessage("修改失败");
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
        List<User> deptList = permissionService.queryUserByDeptid(number); //根据部门id查询该部门是否存在用户
        if(deptList.size() > 0){
            resultInfo.setCode(300);
            resultInfo.setMessage("该部门下存在员工，请先删除该部门下的员工");
            return resultInfo;
        }
        int result = permissionService.deptDelete(number);
        if(result > 0){
            resultInfo.setMessage("删除成功");
            resultInfo.setCode(200);
        }else{
            resultInfo.setCode(500);
            resultInfo.setMessage("删除失败");
        }
        return resultInfo;

    }

    /**
     * 获取所有集团信息
     */
    @RequestMapping(value = "getAllGroup", method = RequestMethod.POST)
    @ResponseBody
    public List<Group> getAllGroup(@Param("groupNumber") String groupNumber,@Param("sysflag") String sysflag){
        List<Group> list = permissionService.getAllGroup(sysflag,groupNumber);
        return list;
    }

    /**
     * 获取所有银行信息
     */
    @RequestMapping(value = "getAllBank", method = RequestMethod.POST)
    @ResponseBody
    public List<Bank> getAllBank(){
        List<Bank> list = permissionService.getAllBank();
        return list;
    }

    /**
     * 新增公司信息
     * @param company
     */
    @RequestMapping(value = "addCom", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addCom(Company company,@Param("items") String items,
                             @Param("flag") String flag,@Param("comNum") String comNum){
        ResultInfo resultInfo = new ResultInfo();
        String[] rows = items.split(",");
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i = 0; i < rows.length ; i++){
            String[] data = rows[i].split("/");
            Map<String,Object> map = new HashMap<String,Object>();
            for (int j = 0 ; j< data.length; j++){
                map.put("bankNo" , data[0]);
                map.put("accounts" , data[1]);
                map.put("customer" , data[2]);
                map.put("certificateNumber" , data[3]);
                map.put("authorizationCode" , data[4]);
                map.put("companyNumber" , "");
                list.add(map);
                break;
            }
        }
        if(flag.equals("0")){ //新增
            String comName = company.getName();
            Company co = permissionService.getComByName(comName); //根据公司名称获取公司信息
            if(StringUtil.isEmpty(co)){
                int com = permissionService.addCom(company);  // 插入公司基本信息
                String com_num = String.valueOf(company.getCompanyNumber());
                for (int i = 0;i <list.size();i++){
                    list.get(i).put("companyNumber",com_num);
                }
                int acc = permissionService.addBankAccount(list); //插入银行账户信息
                if(acc > 0 && com > 0 ){
                    resultInfo.setMessage("新增成功");
                    resultInfo.setCode(200);
                }else{
                    resultInfo.setCode(500);
                    resultInfo.setMessage("新增失败");
                }
            }else{
                resultInfo.setMessage("公司已存在，请重新添加");
                resultInfo.setCode(300);
            }

        }else{   //修改
            if(StringUtil.isEmpty(comNum)){
                resultInfo.setCode(500);
                resultInfo.setMessage("修改失败");
                return resultInfo;
            }
            int msg = permissionService.deleteComByNum(comNum); //根据公司编号删除银行账户信息表中的数据
            int result = permissionService.updateCompany(company); //更新银行信息表
            int acc = permissionService.addBankAccount(list); //插入银行账户信息
            if(result >0 && acc > 0){
                resultInfo.setMessage("修改成功");
                resultInfo.setCode(200);
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("修改失败");
            }

        }
        return resultInfo;
    }

    /**
     * 分页查询公司信息
     * @param
     * @return
     */
    @RequestMapping(value = "comQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> comQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                       @Param("pageNum") int pageNum ,@Param("groupNumber") String groupNumber,@Param("sysflag") String sysflag){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int deptTotal = permissionService.getCompanyTotal(sysflag,groupNumber);   //获取公司总数
        List<Company> list = permissionService.companyQuery(search,pageSize,pageNum,sysflag,groupNumber);
        map.put("total",deptTotal);
        map.put("rows",list);
        return map;
    }

    /**
     * 根据银行账户编号获取账户信息
     * @param accountNum
     * @return
     */
    @RequestMapping(value = "getBankAccountByNum", method = RequestMethod.POST)
    @ResponseBody
    public List<BankAccount> getBankAccountByNum(@Param("accountNum") String accountNum){
        String[] accounts = accountNum.split(",");
        List<BankAccount> data = permissionService.getBankAccountByNum(accounts);
        return data;
    }

    /**
     * 获取所有公司信息
     * @return
     */
    @RequestMapping(value = "getAllCom", method = RequestMethod.POST)
    @ResponseBody
    public List<Company> getAllCom(@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber){
        List<Company> list = permissionService.getAllCompany(sysflag,groupNumber);
        return list;
    }

    /**
     * 获取所有部门信息
     * @return
     */
    @RequestMapping(value = "getAllDept", method = RequestMethod.POST)
    @ResponseBody
    public List<Departments> getAllDept(){
        List<Departments> list = permissionService.getAllDept();
        return list;
    }

    /**
     * 新增和修改员工信息
     * @param user
     * @param flag
     * @return
     */
    @RequestMapping(value = "addEmployee", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addEmployee(User user,@Param("flag") String flag,@Param("pwd") String pwd){
        ResultInfo resultInfo = new ResultInfo();
        if(flag.equals("0")){  //新增
            String userName = user.getUserName();
            User emp = permissionService.getEmpByName(userName);  //根据员工姓名查询对应的信息
            if(!StringUtil.isEmpty(emp)){
                resultInfo.setMessage("员工已存在，请重新添加");
                resultInfo.setCode(300);
                return resultInfo;
            }
            String password = MD5Util.getEncryptedPwd(pwd);
            user.setLogin_password(password);
            int result = permissionService.addEmployee(user);
            if(result > 0){
                user.setId(user.getId()); //设置用户ID
                int result2 = permissionService.addPermission(user);  //新增用户权限层级信息
                if(result2 <= 0){
                    resultInfo.setMessage("新增失败");
                    resultInfo.setCode(500);
                    return resultInfo;
                }
                resultInfo.setMessage("新增成功");
                resultInfo.setCode(200);
            }else{
                resultInfo.setMessage("新增失败");
                resultInfo.setCode(500);
            }
        }else{
            int emp = permissionService.editEmp(user); //修改员工信息
            if(emp > 0){
                resultInfo.setCode(500);
                resultInfo.setMessage("修改员工失败");
                return resultInfo;
            }
            int roots =  permissionService.editRoots(user); //修改权限对应关系表
            if(roots > 0 ){
                resultInfo.setCode(200);
                resultInfo.setMessage("修改员工成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("修改员工失败");
            }
        }
        return resultInfo;

    }

    /**
     * 分页查询员工信息
     * @param search
     * @param pageSize
     * @param pageNum
     * @param
     * @return
     */
    @RequestMapping(value = "empQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> empQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                       @Param("pageNum") int pageNum ,@Param("sysflag") String sysflag,@Param("companyNumber") String companyNumber){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int empTotal = permissionService.getEmpTotal(sysflag,companyNumber);   //获取部门总数
        List<User> list = permissionService.empQuery(search,pageSize,pageNum,sysflag,companyNumber); //分页查询
        map.put("total",empTotal);
        map.put("rows",list);
        return map;

    }

    /**
     * 根据员工编号删除对应的员工
     * @param numbers
     * @return
     */
    @RequestMapping(value = "deleteEmployee", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteEmployee(@Param("numbers") String numbers){
        ResultInfo resultInfo = new ResultInfo();
        String[] number = numbers.split(",");
        int result = permissionService.empDelete(number);
        if(result > 0){
            resultInfo.setMessage("删除成功");
            resultInfo.setCode(200);
        } else {
            resultInfo.setMessage("删除失败");
            resultInfo.setCode(500);
        }

        return resultInfo;

    }

    /**
     * 获取所有菜单，以树节点形式返回
     * @return
     */
    @RequestMapping(value = "menuTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> menuTree(@Param("roleid") String roleid,@Param("sysflag") String sysflag){
        List<Menu> list = userService.getAllMenu(roleid,sysflag);
        Map<String ,Object> map_one = new HashMap<String,Object>();
        List<Map<String,Object>> list_one = new ArrayList<>();
        Map<String , Object> _map = new HashMap<String,Object>();
        for (int i = 0 ; i < list.size() ; i++){
            Menu menus = list.get(i);
            if(menus.getFather().equals("")){
                if(!_map.containsKey(menus.getCode())){
                    _map.put(menus.getCode(),menus);
                }
            }

        }
        for (Map.Entry<String,Object> entry : _map.entrySet()){
            String key = entry.getKey();
            Menu _map2 = (Menu) entry.getValue();
            Map<String,Object> _map3 = new HashMap<String,Object>();
            List<Map<String,Object>> list_two = new ArrayList<>();
            _map3.put("id" , key);
            _map3.put("name", _map2.getName());
            _map3.put("children" , list_two);
            _map3.put("isParent", true);//设置根节点为父节点
            _map3.put("open", true); //根节点展开
            for (int i = 0 ; i < list.size() ; i++){
                Menu menus = list.get(i);
                Map<String,Object> _map4 = new HashMap<String,Object>();
                if(menus.getFather().equals(key) ){
                    _map4.put("id" , menus.getCode());
                    _map4.put("name" , menus.getName());
                    list_two.add(_map4);
                }

            }
            list_one.add(_map3);
        }
        return list_one;
    }

    /**
     * 获取所有公司，以树节点形式返回
     * @return
     */
    @RequestMapping(value = "getComTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String , Object>> getComTree(@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber){
        List<Company> list = permissionService.getAllCompany(sysflag,groupNumber);
        List<Map<String ,Object>> list_one = new ArrayList<>();
        Map<String,Object> _map = new HashMap<String,Object>();
        for (int i = 0 ;i < list.size(); i++){
            Company com = list.get(i);
            if(!_map.containsKey(String.valueOf(com.getCompanyNumber()))){
                _map.put(String.valueOf(com.getCompanyNumber()),com);
            }
        }

        for (Map.Entry<String,Object> entry : _map.entrySet()){
            String key = entry.getKey();
            Company company = (Company) entry.getValue();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id" , key);
            map.put("text" , company.getName());
            list_one.add(map);
        }
        return list_one;
    }

    /**
     * 获取所有角色信息
     * @return
     */
    @RequestMapping(value = "roleAllQuery", method = RequestMethod.POST)
    @ResponseBody
    public List<Roles> roleAllQuery(@Param("sysflag") String sysflag,@Param("companyNumber") String companyNumber){
        List<Roles> list = permissionService.roleAllQuery(sysflag,companyNumber);
        return list;
    }

    //新增角色信息
    @RequestMapping(value = "addRole", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addRole(@Param("com_numbers") String com_numbers,@Param("roleName")
                            String roleName,@Param("flag") String flag,@Param("roleId") String roleId){
        ResultInfo resultInfo = new ResultInfo();
        if(flag.equals("0")){  //新增
            List<Roles> list = permissionService.getRoleByName(roleName); //根据角色名，获取角色信息
            if(list.size() >0){
                resultInfo.setMessage("该角色已存在，请重新选择");
                resultInfo.setCode(300);
                return resultInfo;
            }
            int role = permissionService.addRole(com_numbers,roleName); //新增角色信息
            if(role > 0){
                resultInfo.setCode(200);
                resultInfo.setMessage("新增成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("新增失败");
            }
        }else{  // 修改
            int result = permissionService.modifyRole(com_numbers,roleName,roleId);
            if(result > 0){
                resultInfo.setCode(200);
                resultInfo.setMessage("修改成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("修改失败");
            }
        }

        return  resultInfo;
    }


    /**
     * 设置角色权限
     * @param menuId
     * @param roleId
     * @param flag
     * @return
     */
    @RequestMapping(value = "settingRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo settingRoleMenu(@Param("menuId") String menuId,@Param("roleId") String roleId,@Param("flag") String flag){
        ResultInfo resultInfo = new ResultInfo();
        if (flag.equals("0")){//新增
            // 根据角色id查询该角色是否被创建
            List<RoleMenu> list = permissionService.getRoleMenuByRoleid(roleId);
            if(list.size() > 0 ){
                resultInfo.setCode(300);
                resultInfo.setMessage("该角色已存在，请重新创建");
                return resultInfo;
            }
            int result = permissionService.settingRoleMenu(roleId,menuId);
            if(result > 0){
                resultInfo.setCode(200);
                resultInfo.setMessage("新增角色权限成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("新增角色权限失败");
            }
        }else{ //修改
            int in = permissionService.deleteRoleMenuByRoleId(roleId); // 根据角色id，删除对应的菜单
            int result2 = permissionService.settingRoleMenu(roleId,menuId);
            if(result2 > 0 ){
                resultInfo.setCode(200);
                resultInfo.setMessage("修改角色权限成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("修改角色权限失败");
            }
        }
        return resultInfo;

    }

    /**
     * 根据角色ID，获取角色信息
     * @param roleId
     * @return
     */
    @RequestMapping(value = "getRoleById", method = RequestMethod.POST)
    @ResponseBody
    public List<RoleMenu>  getRoleById(@Param("roleId") String roleId){
        List<RoleMenu> list = permissionService.getRoleMenuByRoleid(roleId);
        return list;
    }

    /**
     * 分页查询角色信息
     * @param search
     * @param pageSize
     * @param pageNum
     * @param
     * @return
     */
    @RequestMapping(value = "roleQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> roleQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                        @Param("pageNum") int pageNum ,@Param("sysflag") String sysflag,@Param("companyNumber") String companyNumber){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        int empTotal = permissionService.getRoleTotal(sysflag,companyNumber);   //获取角色总数
        List<Roles> list = permissionService.roleQuery(search,pageSize,pageNum,sysflag,companyNumber); //分页查询
        map.put("total",empTotal);
        map.put("rows",list);
        return map;
    }

    /**
     * 根据角色ID删除对应的角色
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteRoles", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteRoles(@Param("ids") String ids){
        ResultInfo resultInfo = new ResultInfo();
        String[] idsArr = ids.split(",");
        List<User> userList = permissionService.queryUserByRoleid(idsArr); //根据角色id，判断当前角色下是否有用户存在
        if (userList.size() > 0){
            resultInfo.setCode(300);
            resultInfo.setMessage("该角色存在用户，请先删除对应的用户");
            return resultInfo;
        }
        int result = permissionService.deleteRoles(idsArr); //删除角色信息
        if(result <= 0){
            resultInfo.setCode(500);
            resultInfo.setMessage("删除角色失败");
            return  resultInfo;
        }
        int result2 = permissionService.deleteRolesMenu(idsArr);  //删除角色对应的权限菜单
        if(result2 > 0){
            resultInfo.setMessage("删除成功");
            resultInfo.setCode(200);
        }else{
            resultInfo.setCode(500);
            resultInfo.setMessage("删除角色菜单失败");
        }
        return resultInfo;
    }

    /**
     * 删除公司信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteCompanys", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteCompanys(@Param("ids") String ids){
        ResultInfo resultInfo = new ResultInfo();
        String[] idsArr = ids.split(",");
        List<Departments> deptList = permissionService.queryDeptByCompanyNum(idsArr); //根据公司编号查询该公司是否存在部门
        if(deptList.size() > 0){
            resultInfo.setCode(300);
            resultInfo.setMessage("该公司存在部门，请先删除该公司下的部门");
            return resultInfo;
        }
        int result = permissionService.deleteCompanys(idsArr);  //删除公司信息
        if ( result <= 0 ){
            resultInfo.setCode(500);
            resultInfo.setMessage("删除公司失败");
            return resultInfo;
        }
        int msg = permissionService.deleteCompanyByNum(idsArr); //根据公司编号删除对应的银行账户
        if( msg > 0 ){
            resultInfo.setCode(200);
            resultInfo.setMessage("删除成功");
        }else{
            resultInfo.setCode(500);
            resultInfo.setMessage("删除公司账户失败");
        }

        return resultInfo;
    }

    /**
     * 根据集团获取公司名
     * @param groupNum
     * @return
     */
    @RequestMapping(value = "getComByGroup", method = RequestMethod.POST)
    @ResponseBody
    public List<Company> getComByGroup(@Param("groupNum") String groupNum){
        List<Company> list =  permissionService.getComByGroup(groupNum);
        return list;
    }

    /**
     * 根据公司编号获取部门
     * @param comNum
     * @return
     */
    @RequestMapping(value = "getDeptByCom", method = RequestMethod.POST)
    @ResponseBody
    public List<Departments> getDeptByCom(@Param("comNum") String comNum){
        List<Departments> list = permissionService.getDeptByCom(comNum);
        return list;
    }
}
