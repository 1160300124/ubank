package com.ulaiber.web.controller.backend;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.ExportExcel;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 权限设置Controller
 * Created by daiqingwen on 2017/7/18.
 */
@Controller
@RequestMapping("/backend/")
public class PermissionController extends BaseController {

    private static Logger logger = Logger.getLogger(PermissionController.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserService userService;

    //跳转集团管理页面
    @RequestMapping(value = "group", method = RequestMethod.GET)
    public String group(HttpServletRequest request){
        return "group";
    }

    //跳转公司管理页面
    @RequestMapping(value = "company", method = RequestMethod.GET)
    public String company(HttpServletRequest request){
        return "company";
    }

    //跳转部门管理页面
    @RequestMapping(value = "department", method = RequestMethod.GET)
    public String department(HttpServletRequest request){
        return "department";
    }

    //跳转员工管理页面
    @RequestMapping(value = "employee", method = RequestMethod.GET)
    public String employee(HttpServletRequest request){
        return "employee";
    }

    //跳转角色管理页面
    @RequestMapping(value = "roles", method = RequestMethod.GET)
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
        try {
            //0 新增操作
            if(remark.equals("0")){
                //根据集团名称查询是否数据已存在
                Group gro = permissionService.searchGroupByName(group.getName().trim());
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
        }catch (Exception e){
            logger.error(">>>>>>>>>>新增集团异常：" ,e);
        }


        return resultInfo;
    }

    /**
     * 分页查询集团
     * @param search
     * @param pageSize
     * @param pageNum
     * @param sysflag
     * @param groupNumber
     * @return
     */
    @RequestMapping(value = "groupQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> groupQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                         @Param("pageNum") int pageNum ,@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if(pageSize <= 0){
                pageSize = 10;
            }
            if (pageNum < 0){
                pageNum = 0;
            }
            //获取总数
            int pageTotal = permissionService.getTotal(sysflag,groupNumber);
            List<Group> resultGroup = permissionService.groupQuery(search,pageSize,pageNum,sysflag,groupNumber);
            map.put("total",pageTotal);
            map.put("rows",resultGroup);
        }catch (Exception e){
            logger.error(">>>>>>>>>>分页查询集团异常：" ,e);
        }

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
        try {
            String[] numberArr = numbers.split(",");
            //根据集团编号查询是否存在公司
            List<Company> comList = permissionService.queryComByGroupid(numberArr);
            if(comList.size() > 0 ){
                resultInfo.setCode(300);
                resultInfo.setMessage("该集团存在公司，请先删除该集团下的公司");
                return resultInfo;
            }
            int result = permissionService.deleteGroup(numberArr);
            if(result > 0){
                resultInfo.setCode(200);
                resultInfo.setMessage("删除成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("删除失败");
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>删除集团异常：" ,e);
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
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if(pageSize <= 0){
                pageSize = 10;
            }
            if (pageNum < 0){
                pageNum = 0;
            }
            String[] comArr = companyNumber.split(",");
            //获取部门总数
            int deptTotal = permissionService.getDeptTotal(sysflag,comArr);
            List<Departments> list = permissionService.departmentQuery(search,pageSize,pageNum,sysflag,comArr);
            map.put("total",deptTotal);
            map.put("rows",list);
        }catch (Exception e){
            logger.error(">>>>>>>>>>分页查询部门信息异常：" ,e);
        }

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
        ResultInfo resultInfo = new ResultInfo();
        try {
            String deptNum = dept.getName().trim();
            if(flag.equals("0")){
                //根据部门名称获取对应部门
                Departments departments = permissionService.getDeptByNum(deptNum);
                if(StringUtil.isEmpty(departments)){
                    //新增
                    int result = permissionService.addDept(dept);
                    if(result > 0 ){
                        resultInfo.setMessage("新增成功");
                        resultInfo.setCode(200);
                    }
                }else{
                    resultInfo.setCode(300);
                    resultInfo.setMessage("部门已存在，请重新填写！");
                }
            }else{
                //修改部门
                int edit = permissionService.editDept(dept);
                if(edit > 0 ){
                    resultInfo.setMessage("修改成功");
                    resultInfo.setCode(200);
                }else{
                    resultInfo.setCode(500);
                    resultInfo.setMessage("修改失败");
                }
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>> 新增部门异常：" ,e);
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
        try {
            String[] number = numbers.split(",");
            //根据部门id查询该部门是否存在用户
            List<User> deptList = permissionService.queryUserByDeptid(number);
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
        }catch (Exception e){
            logger.error(">>>>>>>>>> 部门删除异常：" ,e);
        }
        return resultInfo;

    }

    /**
     * 获取所有集团信息
     */
    @RequestMapping(value = "getAllGroup", method = RequestMethod.POST)
    @ResponseBody
    public List<Group> getAllGroup(@Param("groupNumber") String groupNumber,@Param("sysflag") String sysflag){
        List<Group> list = new ArrayList<>();
        try {
             list = permissionService.getAllGroup(sysflag,groupNumber);
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取所有集团信息异常：" ,e);
        }
        return list;
    }

    /**
     * 获取所有银行信息
     */
    @RequestMapping(value = "getAllBank", method = RequestMethod.POST)
    @ResponseBody
    public List<Bank> getAllBank(){
        List<Bank> list = new ArrayList<>();
        try {
            list = permissionService.getAllBank();
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取所有银行信息异常：" ,e);
        }
        return list;
    }

    /**
     * 新增公司信息
     * @param company 公司对象
     * @param items
     * @param roleid 角色ID
     * @param flag 修改/新增标识
     * @param comNum 公司编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "addCom", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addCom(Company company, @Param("items") String items, @Param("roleid") String roleid,
                             @Param("flag") String flag, @Param("comNum") String comNum,
                             HttpServletRequest request, HttpServletResponse response){
        ResultInfo resultInfo = new ResultInfo();
        try {
            String[] rows = items.split(",");
            List<Map<String,Object>> list = new ArrayList<>();
            for(int i = 0; i < rows.length ; i++){
                String[] data = rows[i].split("/");
                Map<String,Object> map = new HashMap<String,Object>();
                for (int j = 0 ; j< data.length; j++){
                    String bankNo = data[0].replaceAll(" ", "");
                    map.put("bankNo" , bankNo);
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
                //根据公司名称获取公司信息
                Company co = permissionService.getComByName(comName);
                if(StringUtil.isEmpty(co)){
                    //生成4位邀请码
                    company.setCode(StringUtil.getStringRandom(4));
                    // 插入公司基本信息
                    int com = permissionService.addCom(company);
                    String com_num = String.valueOf(company.getCompanyNumber());
                    for (int i = 0;i <list.size();i++){
                        list.get(i).put("companyNumber",com_num);
                    }
                    //插入银行账户信息
                    int acc = permissionService.addBankAccount(list);
                    if(acc == 0 && com == 0 ){
                        resultInfo.setCode(500);
                        resultInfo.setMessage("新增失败");
                        return resultInfo;
                    }
                    if(!flag.equals("0")){
                        //根据角色ID获取角色信息
                        Map<String,Object> roleMap = permissionService.queryRoleById(roleid);
                        String comNo = (String) roleMap.get("companyNumber");
                        String name = (String) roleMap.get("companyName");
                        //拼接公司编号和名称
                        comNo = comNo + "," + com_num;
                        name = name + "," + comName;
                        //更新角色所属公司
                        int ro = permissionService.updateRole(roleid,comNo,name);
                        //获取当前session对象并更新
                        User user = (User) request.getSession().getAttribute("BACKENDUSER");
                        user.setCompanyNumber(comNo);
                    }
                    resultInfo.setMessage("新增成功");
                    resultInfo.setCode(200);


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
                company.setCompanyNumber(Integer.parseInt(comNum));
                int result = permissionService.updateCompany(company); //更新银行信息表
                for (int i = 0;i <list.size();i++){
                    list.get(i).put("companyNumber",comNum);
                }
                int acc = permissionService.addBankAccount(list); //插入银行账户信息
                if(result >0 && acc > 0){
                    resultInfo.setMessage("修改成功");
                    resultInfo.setCode(200);
                }else{
                    resultInfo.setCode(500);
                    resultInfo.setMessage("修改失败");
                }

            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>新增公司信息异常：" ,e);
        }

        return resultInfo;
    }

    /**
     * 分页查询公司信息
     * @param search
     * @param pageSize
     * @param pageNum
     * @param groupNumber
     * @param sysflag
     * @return
     */
    @RequestMapping(value = "comQuery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> comQuery(@Param("search") String search,@Param("pageSize") int pageSize,
                                       @Param("pageNum") int pageNum ,@Param("groupNumber") String groupNumber,
                                       @Param("companyNumber") String companyNumber,@Param("sysflag") String sysflag){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if(pageSize <= 0){
                pageSize = 10;
            }
            if (pageNum < 0){
                pageNum = 0;
            }
            //获取公司总数
            int deptTotal = permissionService.getCompanyTotal(sysflag,groupNumber,companyNumber);
            List<Company> list = permissionService.companyQuery(search,pageSize,pageNum,sysflag,groupNumber,companyNumber);
            map.put("total",deptTotal);
            map.put("rows",list);
        }catch (Exception e){
            logger.error(">>>>>>>>>>分页查询公司信息异常：" ,e);
        }

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
        List<BankAccount> data = new ArrayList<>();
        try {
            String[] accounts = accountNum.split(",");
            data = permissionService.getBankAccountByNum(accounts);

        }catch (Exception e){
            logger.error(">>>>>>>>>>根据银行账户编号获取账户信息异常：" ,e);
        }
        return data;
    }

    /**
     * 获取所有公司信息
     * @return
     */
    @RequestMapping(value = "getAllCom", method = RequestMethod.POST)
    @ResponseBody
    public List<Company> getAllCom(@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber
                                                    ,@Param("companyNumber") String companyNumber){
        List<Company> list = new ArrayList<>();
        try {
            list = permissionService.getAllCompany(sysflag,groupNumber,companyNumber);
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取所有公司信息异常：" ,e);
        }
        return list;
    }

    /**
     * 获取所有部门信息
     * @return
     */
    @RequestMapping(value = "getAllDept", method = RequestMethod.POST)
    @ResponseBody
    public List<Departments> getAllDept(){
        List<Departments> list = new ArrayList<>();
        try {
            list = permissionService.getAllDept();
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取所有部门信息异常：" ,e);
        }
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
        try {
            if(flag.equals("0")){  //新增
                String userName = user.getUserName();
                String mobile = user.getMobile();
                //根据员工姓名查询对应的信息
                User emp = permissionService.getEmpByName(userName,mobile);
                if(!StringUtil.isEmpty(emp)){
                    if(emp.getMobile().equals(user.getMobile())){
                        resultInfo.setMessage("电话号码已存在，请重新输入");
                        resultInfo.setCode(300);
                        return resultInfo;
                    }
//                else if (emp.getUserName().equals(user.getUserName())){
//                    resultInfo.setMessage("已存在，请重新输入");
//                    resultInfo.setCode(300);
//                    return resultInfo;
//                }
                    else if(emp.getCardNo().equals(user.getCardNo())){
                        resultInfo.setMessage("身份证已存在，请重新输入");
                        resultInfo.setCode(300);
                        return resultInfo;
                    }

                }
                String password = MD5Util.getEncryptedPwd(pwd);
                user.setLogin_password(password);
                String date = sdf.format(new Date());
                user.setCreateTime(date);
                int result = permissionService.addEmployee(user);
                if(result > 0){
                    //设置用户ID
                    user.setId(user.getId());
                    //新增用户权限层级信息
                    int result2 = permissionService.addPermission(user);
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
                //修改员工信息
                int emp = permissionService.editEmp(user);
                if(emp <= 0){
                    resultInfo.setCode(500);
                    resultInfo.setMessage("修改员工失败");
                    return resultInfo;
                }
                //修改权限对应关系表
                int roots =  permissionService.editRoots(user);
                if(roots > 0 ){
                    resultInfo.setCode(200);
                    resultInfo.setMessage("修改员工成功");
                }else{
                    resultInfo.setCode(200);
                    resultInfo.setMessage("修改员工成功");
                }
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>> 新增和修改员工信息异常：" ,e);
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
    public Map<String,Object> empQuery(@Param("search") String search,@Param("pageSize") int pageSize,@Param("sysflag") String activetion,
                                       @Param("pageNum") int pageNum ,@Param("sysflag") String sysflag,@Param("companyNumber") String companyNumber){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if(pageSize <= 0){
                pageSize = 10;
            }
            if (pageNum < 0){
                pageNum = 0;
            }
            String[] comArr = companyNumber.split(",");
            //获取部门总数
            int empTotal = permissionService.getEmpTotal(sysflag,comArr);
            //分页查询
            List<User> list = permissionService.empQuery(search,pageSize,pageNum,sysflag,comArr,activetion);
            map.put("total",empTotal);
            map.put("rows",list);
        }catch (Exception e){
            logger.error(">>>>>>>>>>分页查询员工信息异常：" ,e);
        }
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
        try {
            String[] number = numbers.split(",");
            int result = permissionService.empDelete(number);
            if(result > 0){
                //删除权限层级表中的记录
                int result2 = permissionService.deleteRoots(number);
                resultInfo.setMessage("删除成功");
                resultInfo.setCode(200);
            } else {
                resultInfo.setMessage("删除失败");
                resultInfo.setCode(500);
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>根据员工编号删除对应的员工异常：" ,e);
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
        List<Map<String,Object>> list_one = new ArrayList<>();
        try {
            List<Menu> list = userService.getAllMenu(roleid,sysflag);
            Map<String ,Object> map_one = new HashMap<String,Object>();
            Map<String , Object> _map = new HashMap<String,Object>();
            for (int i = 0 ; i < list.size() ; i++){
                Menu menus = list.get(i);
                if(StringUtil.isEmpty(menus.getFather())){
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
                    if(!StringUtil.isEmpty(menus.getFather())){
                        if(menus.getFather().equals(key) ){
                            _map4.put("id" , menus.getCode());
                            _map4.put("name" , menus.getName());
                            _map3.put("isParent", false);//设置根节点为父节点
                            _map3.put("open", true); //根节点展开
                            list_two.add(_map4);
                        }
                    }

                }
                list_one.add(_map3);
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取所有菜单，以树节点形式返回异常：" ,e);
        }

        return list_one;
    }

    /**
     * 获取所有公司，以树节点形式返回
     * @return
     */
    @RequestMapping(value = "getComTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String , Object>> getComTree(@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber,
                                                 @Param("companyNumber") String companyNumber){
        List<Map<String ,Object>> list_one = new ArrayList<>();
        try {
            List<Company> list = permissionService.getAllCompanybyGroupNum(sysflag,groupNumber,companyNumber);
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
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取所有公司，以树节点形式返回异常：" ,e);
        }
        return list_one;
    }

    /**
     * 查询所有公司
     * @param sysflag
     * @param groupNumber
     * @return
     */
    @RequestMapping(value = "queryComTree", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String , Object>> queryComTree(@Param("sysflag") String sysflag,@Param("groupNumber") String groupNumber,@Param("companyNumber") String companyNumber){
        List<Map<String ,Object>> _list = new ArrayList<>();
        try {
            List<Company> list = permissionService.getAllCompany(sysflag,groupNumber, companyNumber);
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
                _list.add(map);
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>查询所有公司异常：" ,e);
        }
        return _list;
    }

    /**
     * 获取所有角色信息
     * @return
     */
    @RequestMapping(value = "roleAllQuery", method = RequestMethod.POST)
    @ResponseBody
    public List<Roles> roleAllQuery(@Param("sysflag") String sysflag,@Param("companyNumber") String companyNumber){
        List<Roles> list = new ArrayList<>();
        try {
            list = permissionService.roleAllQuery(sysflag,companyNumber);
        }catch (Exception e){
            logger.error(">>>>>>>>>>异常：" ,e);
        }
        return list;
    }

    /**
     *新增角色信息
     */
    @RequestMapping(value = "addRole", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addRole(@Param("com_numbers") String com_numbers,@Param("roleName") String roleName,@Param("groupNumber") String groupNumber
                        ,@Param("names") String names,@Param("flag") String flag,@Param("roleId") String roleId){
        ResultInfo resultInfo = new ResultInfo();
        try {
            if(flag.equals("0")){  //新增
                //根据角色名，获取角色信息
//            List<Roles> list = permissionService.getRoleByName(roleName);
//            if(list.size() >0){
//                resultInfo.setMessage("该角色已存在，请重新选择");
//                resultInfo.setCode(300);
//                return resultInfo;
//            }
                //新增角色信息
                int role = permissionService.addRole(com_numbers,roleName,names,groupNumber);
                if(role > 0){
                    resultInfo.setCode(200);
                    resultInfo.setMessage("新增成功");
                }else{
                    resultInfo.setCode(500);
                    resultInfo.setMessage("新增失败");
                }
            }else{  // 修改
                int result = permissionService.modifyRole(com_numbers,roleName,roleId,names,groupNumber);
                if(result > 0){
                    resultInfo.setCode(200);
                    resultInfo.setMessage("修改成功");
                }else{
                    resultInfo.setCode(500);
                    resultInfo.setMessage("修改失败");
                }
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>新增角色信息异常：" ,e);
        }
        return  resultInfo;
    }


    /**
     * 设置角色权限
     * @param menuId
     * @param roleId
     * @param
     * @return
     */
    @RequestMapping(value = "settingRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo settingRoleMenu(@Param("menuId") String menuId,@Param("roleId") String roleId){
        ResultInfo resultInfo = new ResultInfo();
        try {
//  if (flag.equals("0")){//新增
            // 根据角色id查询该角色是否被创建
//            List<RoleMenu> list = permissionService.getRoleMenuByRoleid(roleId);
//            if(list.size() > 0 ){
//                resultInfo.setCode(300);
//                resultInfo.setMessage("该角色已存在，请重新创建");
//                return resultInfo;
//            }
            int result1 = permissionService.setRoleMenuByRoleId(roleId,menuId);
            resultInfo.setCode(200);
            resultInfo.setMessage("设置角色权限成功");
//            int result = permissionService.settingRoleMenu(roleId,menuId);
//            if(result > 0){
//                resultInfo.setCode(200);
//                resultInfo.setMessage("新增角色权限成功");
//            }else{
//                resultInfo.setCode(500);
//                resultInfo.setMessage("新增角色权限失败");
//            }
//        }else{ //修改
//            // 根据角色id，删除对应的菜单
//            int in = permissionService.deleteRoleMenuByRoleId(roleId);
//            if(StringUtil.isEmpty(roleId)){
//                resultInfo.setCode(200);
//                resultInfo.setMessage("修改角色权限成功");
//            }
//            int result2 = permissionService.settingRoleMenu(roleId,menuId);
//            if(result2 > 0 ){
//                resultInfo.setCode(200);
//                resultInfo.setMessage("修改角色权限成功");
//            }else{
//                resultInfo.setCode(500);
//                resultInfo.setMessage("修改角色权限失败");
//            }
//        }
        }catch (Exception e){
            logger.error(">>>>>>>>>>设置角色权限异常：" ,e);
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
        List<RoleMenu> list = new ArrayList<>();
        try {
            list = permissionService.getRoleMenuByRoleid(roleId);
        }catch (Exception e){
            logger.error(">>>>>>>>>>根据角色ID，获取角色信息异常：" ,e);
        }
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
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if(pageSize <= 0){
                pageSize = 10;
            }
            if (pageNum < 0){
                pageNum = 0;
            }
            //获取角色总数
            int empTotal = permissionService.getRoleTotal(sysflag,companyNumber);
            String[] comArr = companyNumber.split(",");
            //分页查询
            List<Roles> list = permissionService.roleQuery(search,pageSize,pageNum,sysflag,comArr);
            map.put("total",empTotal);
            map.put("rows",list);
        }catch (Exception e){
            logger.error(">>>>>>>>>>分页查询角色信息异常：" ,e);
        }
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
        try {
            String[] idsArr = ids.split(",");
            //根据角色id，判断当前角色下是否有用户存在
            List<User> userList = permissionService.queryUserByRoleid(idsArr);
            if (userList.size() > 0){
                resultInfo.setCode(300);
                resultInfo.setMessage("该角色存在用户，请先删除对应的用户");
                return resultInfo;
            }
            //删除角色信息
            int result = permissionService.deleteRoles(idsArr);
            if(result <= 0){
                resultInfo.setCode(500);
                resultInfo.setMessage("删除角色失败");
                return  resultInfo;
            }
            //删除角色对应的权限菜单
            int result2 = permissionService.deleteRolesMenu(idsArr);
            if(result2 > 0){
                resultInfo.setMessage("删除成功");
                resultInfo.setCode(200);
            }else{
                resultInfo.setCode(200);
                resultInfo.setMessage("删除权限成功");
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>根据角色ID删除对应的角色异常：" ,e);
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
        try {
            String[] idsArr = ids.split(",");
            //根据公司编号查询该公司是否存在部门
            List<Departments> deptList = permissionService.queryDeptByCompanyNum(idsArr);
            if(deptList.size() > 0){
                resultInfo.setCode(300);
                resultInfo.setMessage("该公司存在部门，请先删除该公司下的部门");
                return resultInfo;
            }
            //删除公司信息
            int result = permissionService.deleteCompanys(idsArr);
            if ( result <= 0 ){
                resultInfo.setCode(500);
                resultInfo.setMessage("删除公司失败");
                return resultInfo;
            }
            //根据公司编号删除对应的银行账户
            int msg = permissionService.deleteCompanyByNum(idsArr);
            if( msg > 0 ){
                resultInfo.setCode(200);
                resultInfo.setMessage("删除成功");
            }else{
                resultInfo.setCode(500);
                resultInfo.setMessage("删除公司账户失败");
            }
        }catch (Exception e){
            logger.error(">>>>>>>>>>删除公司信息异常：" ,e);
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
    public List<Company> getComByGroup(@Param("groupNum") String groupNum,@Param("companyNumber") String companyNumber,
                                       @Param("sysflag") String sysflag){
        List<Company> list = new ArrayList<>();
        try {
             list =  permissionService.getComByGroup(groupNum,companyNumber,sysflag);
        }catch (Exception e){
            logger.error(">>>>>>>>>>根据集团获取公司名异常：" ,e);
        }
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
        List<Departments> list = new ArrayList<>();
        try {
             list = permissionService.getDeptByCom(comNum);
        }catch (Exception e){
            logger.error(">>>>>>>>>>根据公司编号获取部门异常：" ,e);
        }
        return list;
    }

    /**
     * 根据当前角色所属公司编号，查询对应的部门
     * @param sysflag
     * @param companyNumber
     * @return
     */
    @RequestMapping(value = "queryAllDept", method = RequestMethod.POST)
    @ResponseBody
    public List<Departments> queryAllDept(@Param("sysflag") String sysflag,@Param("companyNumber")String companyNumber){
        List<Departments> list  = new ArrayList<>();
        try {
            list = permissionService.queryAllDept(sysflag,companyNumber);
        }catch (Exception e){
            logger.error(">>>>>>>>>>根据当前角色所属公司编号，查询对应的部门异常：" ,e);
        }
        return list;
    }

    /**
     * 获取各个部门员工人数
     * @return
     */
    @RequestMapping(value = "getDeptEmpCount", method = RequestMethod.POST)
    @ResponseBody
    public List<Departments> getDeptEmpCount(){
        List<Departments> list  = new ArrayList<>();
        try {
            list = permissionService.getDeptEmpCount();
        }catch (Exception e){
            logger.error(">>>>>>>>>>获取各个部门员工人数异常：" ,e);
        }
        return list;
    }

    /**
     * 导入Excel
     * @param file 文件名
     * @param group 集团编号
     * @param comNum 公司编号
     * @param deptNum 部门编号
     * @return resultInfo
     */
    @RequestMapping(value = "importEmployee", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo importEmployee( @Param("file") MultipartFile file, @Param("group") String group,
                                     @Param("comNum") String comNum, @Param("deptNum") String deptNum){
        ResultInfo resultInfo = new ResultInfo();
        try {
            String name = file.getOriginalFilename();
            //导入excel
            ExportExcel excel = new ExportExcel();
            Map<String,Object> map = excel.readExcel(file);
            if(map.size() <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("导入Excel失败");
                return resultInfo;
            }
           //保存数据
            List<ExcelAO> Tlist = (List<ExcelAO>) map.get("true");
            List<ExcelAO> Flist = (List<ExcelAO>) map.get("false");
            if(Tlist.size() > 0){
                for (int i = 0 ; i < Tlist.size() ; i++){
                    ExcelAO ao = Tlist.get(i);
                    String mobile = ao.getMobile();
                    //查询当前电话号码是否已被注册
                    User user = permissionService.queryuserByMobile(mobile);
                    if(StringUtil.isEmpty(user)){
                       //新增用户
                        Map<String,Object> param = new HashMap<>();
                        param.put("gropNum",group);
                        param.put("comNum",comNum);
                        param.put("deptNum",deptNum);
                        param.put("name",ao.getName());
                        param.put("idcard",ao.getIDCard());
                        param.put("mobile",ao.getMobile());
                        param.put("entryTime",ao.getEntryTime());
                        int result = permissionService.insertUser(param);
                        if(result <= 0){
                            resultInfo.setCode(IConstants.QT_CODE_ERROR);
                            resultInfo.setMessage("导入Excel失败");
                        }
                        resultInfo.setCode(IConstants.QT_CODE_OK);
                        resultInfo.setMessage("导入成功");
                    }else{
                        //如果当前电话已存在数据库中，则返回给前台提示
                        ExcelAO ea = new ExcelAO();
                        ea.setId(ao.getId());
                        ea.setIDCard(ao.getIDCard());
                        ea.setEntryTime(ao.getEntryTime());
                        ea.setMobile(ao.getMobile());
                        ea.setName(ao.getName());
                        ea.setMessage("电话已被注册");
                        Flist.add(ea);
                    }
                }
            }
            if(Flist.size() > 0){
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("导入成功");
                resultInfo.setData(Flist);
                return resultInfo;
            }

        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("导入Excel失败");
            logger.error("导入Excel失败",e);
        }
        return resultInfo;
    }

    /**
     * 批量新增员工
     * @param group 集团编号
     * @param rows 用户信息
     * @param comNum 公司编号
     * @param deptNum 部门编号
     * @return ResultInfo
     */
    @RequestMapping(value = "batchImport", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo batchAddUser(String group,@Param("rows") String rows,
                                   @Param("comNum") String comNum, @Param("deptNum") String deptNum){
        ResultInfo resultInfo = new  ResultInfo();
        try {
            JSONArray array = JSONArray.fromObject(rows);
            List<ExcelAO> list = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsons = array.getJSONObject(i);
                ExcelAO ao = new ExcelAO();
                String mobile = jsons.get("mobile").toString();
                //查询当前电话号码是否已被注册
                User user = permissionService.queryuserByMobile(mobile);
                if(!StringUtil.isEmpty(user)){
                    ao.setName(jsons.get("name").toString());
                    ao.setId(jsons.get("id").toString());
                    ao.setMobile(jsons.get("mobile").toString());
                    ao.setEntryTime(jsons.get("entryTime").toString());
                    ao.setMessage("电话号码已存在");
                    list.add(ao);
                    break;
                }
                Map<String,Object> param = new HashMap<>();
                param.put("groupNum",group);
                param.put("comNum",comNum);
                param.put("deptNum",deptNum);
                param.put("name",jsons.get("name").toString());
                param.put("idcard",jsons.get("idcard").toString());
                param.put("mobile",jsons.get("mobile").toString());
                param.put("entryTime",jsons.get("entryTime").toString());
                int result = permissionService.insertUser(param);
                if(result <= 0){
                    resultInfo.setCode(IConstants.QT_CODE_ERROR);
                    resultInfo.setMessage("批量新增员工失败");
                    return resultInfo;
                }
            }
            if(list.size() > 0 ){
                resultInfo.setData(list);
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("批量新增员工成功");

        }catch (Exception e){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("批量新增员工失败");
            logger.error("批量新增员工失败",e);
        }

        return resultInfo;
    }

}
