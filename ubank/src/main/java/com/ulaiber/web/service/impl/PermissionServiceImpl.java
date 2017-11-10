package com.ulaiber.web.service.impl;

import com.ulaiber.web.dao.*;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 权限管理接口实现类
 * Created by daiqingwen on 2017/7/18.
 */
@Service
public class PermissionServiceImpl extends BaseService implements PermissionService {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private RolesDao rolesDao;

    @Resource
    private RoleMenuDao roleMenuDao;

    @Resource
    private BankDao bankDao;

    @Resource
    private LevelInfoDao levelInfoDao;

    @Override
    public int addGroup(Group group) {
        int resultInfo = permissionDao.addGroup(group);
        return resultInfo;
    }

    @Override
    public Group searchGroupByName(String name) {
        Group group = permissionDao.searchGroupByName(name);
        return group;
    }

    @Override
    public List<Group> groupQuery(String search, int pageSize, int pageNum,String sysflag,String groupNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("search" ,search );
        map.put("pageSize" , pageSize);
        map.put("pageNum" , pageNum);
        map.put("sysflag" , sysflag);
        map.put("groupNumber" , groupNumber);
        List<Group> result = permissionDao.groupQuery(map);
        return result;
    }

    @Override
    public int modifyGroup(Group group) {
        int result = permissionDao.modifyGroup(group);
        return result;
    }

    @Override
    public int deleteGroup(String[] numberArr) {
        int result = permissionDao.deleteGroup(numberArr);
        return result;
    }

    @Override
    public int getCompanyTotal(String sysflag, String groupNumber, String companyNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!"0".equals(sysflag)){
            String[] comArr = companyNumber.split(",");
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("companyNumber" , arr);
            map.put("sysflag" , sysflag);
            map.put("groupNumber" , groupNumber);
        }else{
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
            map.put("groupNumber" , groupNumber);
        }
        int comTotal = permissionDao.getCompanyTotal(map);
        return comTotal;
    }

    @Override
    public List<Company> companyQuery(String search, int pageSize, int pageNum, String sysflag, String groupNumber, String companyNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!"0".equals(sysflag)){
            String[] comArr = companyNumber.split(",");
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("search" ,search );
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("groupNumber" , groupNumber);
            map.put("companyNumber" , arr);
        }else{
            map.put("search" ,search );
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("groupNumber" , groupNumber);
            map.put("companyNumber" , new int[0]);
        }
        List<Company> result = permissionDao.companyQuery(map);
        return result;
    }

    @Override
    public Departments getDeptByNum(String deptNum) {
        Departments result = permissionDao.getDeptByNum(deptNum);
        return result;
    }

    @Override
    public int addDept(Departments dept) {
        int result = permissionDao.addDept(dept);
        return result;
    }

    @Override
    public List<Departments> departmentQuery(String search, int pageSize, int pageNum,String sysflag,String[] comArr) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!sysflag.equals("0")){
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("search" ,search );
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , arr);
        }else{
            map.put("search" ,search );
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
        }

        List<Departments> list  = permissionDao.departmentQuery(map);
        return list;
    }

    @Override
    public int getTotal(String sysflag,String groupNumber) {
        Map<String,Object> map  = new HashMap<String,Object>();
        map.put("sysflag" , sysflag);
        map.put("groupNumber" , groupNumber);
        int total = permissionDao.getTotal(map);
        return total;
    }

    @Override
    public int getDeptTotal(String sysflag,String[] comArr) {
        Map<String,Object> map  = new HashMap<String,Object>();
        if(!sysflag.equals("0")){
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , arr);
        }else{
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
        }

        int result = permissionDao.getDeptTotal(map);
        return result;
    }

    @Override
    public int editDept(Departments dept) {
        int result = permissionDao.editDept(dept);
        return result;
    }

    @Override
    public int deptDelete(String[] number) {
        int result = permissionDao.deptDelete(number);
        return result;
    }

    @Override
    public List<Group> getAllGroup(String sysflag,String groupNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("sysflag" , sysflag);
        map.put("groupNumber" , groupNumber);
        List<Group> list = permissionDao.getAllGroup(map);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addCom(Company company) {
        int com = permissionDao.addCom(company);
        return com;
    }

    @Override
    public List<Bank> getAllBank() {
        List<Bank> list = permissionDao.getAllBank();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addBankAccount(List<Map<String, Object>> list) {
        int  account = companyDao.addBankAccount(list);
        return account;
    }



    @Override
    public List<BankAccount> getBankAccountByNum(String[] accounts) {
        List<BankAccount> data = companyDao.getBankAccountByNum(accounts);
        return data;
    }

    @Override
    public Company getComByName(String comName) {
        Company company = companyDao.getComByName(comName);
        return company;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteComByNum(String comNum) {
        int msg = bankDao.deleteComByNum(comNum);
        return msg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int updateCompany(Company company) {
        int result = permissionDao.updateCompany(company);
        return result;
    }

    @Override
    public List<Company> getAllCompany(String sysflag, String groupNumber, String companyNumber) {
        Map<String ,Object> map = new HashMap<String , Object>();
        if(!"0".equals(sysflag)){
            String[] comArr = companyNumber.split(",");
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("sysflag",sysflag);
            map.put("groupNumber",groupNumber);
            map.put("companyNumber",arr);
        }else{
            map.put("sysflag",sysflag);
            map.put("groupNumber",groupNumber);
            map.put("companyNumber",new int[0]);
        }
        List<Company> list = permissionDao.getAllCompany(map);
        return list;
    }

    @Override
    public List<Company> getAllCompanybyGroupNum(String sysflag, String groupNumber, String companyNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!"0".equals(sysflag)){
            String[] comArr = companyNumber.split(",");
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("sysflag",sysflag);
            map.put("groupNumber",groupNumber);
            map.put("companyNumber",arr);
        }else{
            map.put("sysflag",sysflag);
            map.put("groupNumber",groupNumber);
            map.put("companyNumber",new int[0]);
        }
     //   List<Company> list = permissionDao.getAllCompany(map);
        List<Company> list = rolesDao.getAllCompanybyGroupNum(map);
        return list;
    }

    @Override
    public List<Departments> queryAllDept(String sysflag, String companyNumber) {
        Map<String,Object> map = new HashMap<String ,Object>();
        map.put("sysflag" , sysflag);
        map.put("companyNumber" , companyNumber);
        List<Departments> list = permissionDao.queryAllDept(map);
        return list;
    }

    @Override
    public List<Departments> getDeptEmpCount() {
        List<Departments> list = permissionDao.getDeptEmpCount();
        return list;
    }

    @Override
    public int deleteRoots(String[] number) {
        return employeeDao.deleteRoots(number);
    }

    @Override
    public List<Departments> getAllDept() {
        List<Departments> list = permissionDao.getAllDept();
        return list;
    }

    @Override
    public User getEmpByName(String userName,String mobile) {
        Map<String,Object> map = new HashMap<String ,Object>();
        map.put("userName" ,userName);
        map.put("mobile" ,mobile);
        User emp = employeeDao.getEmpByName(map);
        return emp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addEmployee(User user) {
        int result = employeeDao.addEmployee(user);
        return result;
    }

    @Override
    public int getEmpTotal(String sysflag,String[] comArr) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!sysflag.equals("0")){
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , arr);
        }else{
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
        }

        int total = employeeDao.getEmpTotal(map);
        return total;
    }

    @Override
    public List<User> empQuery(String search, int pageSize, int pageNum,String sysflag,String[] comArr) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!sysflag.equals("0")){
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("search" ,search );
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , arr);
        }else{
            map.put("search" ,search );
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
        }

        List<User> result = employeeDao.empQuery(map);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int editEmp(User user) {
        String date = sdf.format(new Date());
        user.setCreateTime(date);
        int result = employeeDao.editEmp(user);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int empDelete(String[] number) {
        Map<String,Object> map = new HashMap<>();
        String date = sdf.format(new Date());
        map.put("date",date);
        map.put("number" , number);
        int result = employeeDao.empDelete(map);
        return result;
    }

    @Override
    public List<Roles> roleAllQuery(String sysflag,String companyNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        String[] comArr = companyNumber.split(",");
        map.put("sysflag" , sysflag);
        map.put("companyNumber" , comArr);
        List<Roles> list = rolesDao.roleAllQuery(map);
        return list;
    }

    @Override
    public List<Roles> getRoleByName(String roleName) {
        List<Roles> list = rolesDao.getRoleByName(roleName);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addRole(String com_numbers, String roleName,String names,String groupNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("com_numbers" , com_numbers);
        map.put("roleName" , roleName);
        map.put("names" , names);
        map.put("groupNumber" , groupNumber);
        int result = rolesDao.addRole(map);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int settingRoleMenu(String roleId, String menuId) {
        List<Map<String,Object>> list = new ArrayList<>();
        String[] arr = menuId.split(",");
        for (int i = 0 ; i <arr.length ; i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("menuId" , arr[i]);
            map.put("roleId",roleId);
            list.add(map);
        }
        int result = roleMenuDao.settingRoleMenu(list);
        return result;
    }

    @Override
    public List<RoleMenu> getRoleMenuByRoleid(String roleId) {
        List<RoleMenu> list  = roleMenuDao.getRoleMenuByRoleid(roleId);
        return list;
    }

    @Override
    public int getRoleTotal(String sysflag,String companyNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!sysflag.equals("0")){
            String[] comArr = companyNumber.split(",");
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , arr);
        }else{
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
        }

        int total = roleMenuDao.getRoleTotal(map);
        return total;
    }

    @Override
    public List<Roles> roleQuery(String search, int pageSize, int pageNum,String sysflag,String[] comArr) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!sysflag.equals("0")){
            int arr[] = new int[comArr.length];
            for (int i = 0 ; i < comArr.length ; i++){
                arr[i] = Integer.parseInt(comArr[i]);
            }
            map.put("search" , search);
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , arr);
        }else{
            map.put("search" , search);
            map.put("pageSize" , pageSize);
            map.put("pageNum" , pageNum);
            map.put("sysflag" , sysflag);
            map.put("companyNumber" , new int[0]);
        }

        List<Roles> list = rolesDao.roleQuery(map);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyRole(String com_numbers, String roleName,String roleId,String names,String groupNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("com_numbers" , com_numbers);
        map.put("roleName" , roleName);
        map.put("roleId" , roleId);
        map.put("names" , names);
        map.put("groupNumber" , groupNumber);
        int result = rolesDao.modifyRole(map);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteRoles(String[] idsArr) {
        int result = rolesDao.deleteRoles(idsArr);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteRolesMenu(String[] idsArr) {
        int result = roleMenuDao.deleteRolesMenu(idsArr);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteCompanys(String[] idsArr) {
        int result = companyDao.deleteCompanys(idsArr);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteCompanyByNum(String[] idsArr) {
        int result = bankDao.deleteCompanyByNum(idsArr);
        return result;
    }

    @Override
    public List<User> queryUserByRoleid(String[] idsArr) {
        List<User> userList = rolesDao.queryUserByRoleid(idsArr);
        return userList;
    }

    @Override
    public List<User> queryUserByDeptid(String[] number) {
        List<User> deptList = permissionDao.queryUserByDeptid(number);
        return deptList;
    }

    @Override
    public List<Departments> queryDeptByCompanyNum(String[] idsArr) {
        List<Departments> list = permissionDao.queryDeptByCompanyNum(idsArr);
        return list;
    }

    @Override
    public List<Company> queryComByGroupid(String[] numberArr) {
        List<Company> comList = companyDao.queryComByGroupid(numberArr);
        return comList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addPermission(User user) {
        int i = levelInfoDao.addPermission(user);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int setRoleMenuByRoleId(String roleId, String menuId) {
        int result = roleMenuDao.deleteRoleMenuByRoleId(roleId);
        int result2 = 0;
        if(!StringUtil.isEmpty(menuId)){
            List<Map<String,Object>> list = new ArrayList<>();
            String[] arr = menuId.split(",");
            for (int i = 0 ; i <arr.length ; i++){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("menuId" , arr[i]);
                map.put("roleId",roleId);
                list.add(map);
            }
            result2 = roleMenuDao.settingRoleMenu(list);
        }
        return result2;
    }

    @Override
    public List<Company> getComByGroup(String groupNum, String companyNumber) {
        Map<String,Object> map = new HashMap<String,Object>();
        String[] comArr = companyNumber.split(",");
        int arr[] = new int[comArr.length];
        for (int i = 0 ; i < comArr.length ; i++){
            arr[i] = Integer.parseInt(comArr[i]);
        }
        map.put("groupNum" , groupNum);
        map.put("companyNumber" , arr);
        List<Company> list = companyDao.getComByGroup(map);
        return list;
    }

    @Override
    public List<Departments> getDeptByCom(String comNum) {
        List<Departments> list = permissionDao.getDeptByCom(comNum);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int editRoots(User user) {
        return employeeDao.editRoots(user);
    }

	@Override
	public List<Company> getCompanysByNums(String[] nums) {
		return companyDao.getCompanysByNums(nums);
	}

    @Override
    public Map<String, Object> queryRoleById(String roleid) {
        return rolesDao.queryRoleById(roleid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int updateRole(String roleid, String comNo, String name) {
        Map<String,Object> map = new HashMap<>();
        map.put("roleid" , roleid);
        map.put("comNo" , comNo);
        map.put("name" , name);
        return rolesDao.updateRole(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertRole(Roles roles) {
        return permissionDao.insertRole(roles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int saveRoots(String code,int userid) {
        Company company =  permissionDao.getComAndGroupByCode(code);
        if(StringUtil.isEmpty(company)){
            return 0;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("userid" ,userid);
        map.put("companyNumber" ,company.getCompanyNumber());
        map.put("group_num" ,company.getGroup_num());
        //给注册用户分配公司和集团
        int result = permissionDao.insertRoots(map);
        return result;
    }


}
