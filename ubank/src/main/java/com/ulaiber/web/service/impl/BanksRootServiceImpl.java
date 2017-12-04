package com.ulaiber.web.service.impl;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.BanksRootDao;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.BanksRootService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 银行权限接口实现类
 * Created by daiqingwen on 2017/9/26.
 */
@Service
public class BanksRootServiceImpl extends BaseService implements BanksRootService{

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private BanksRootDao banksRootDao;
    @Override
    public List<Menu> getBankMenuByUser(String userName) {
        return banksRootDao.getBankMenuByUser(userName);
    }

    @Override
    public Headquarters queryBankByName(String bankName) {
        return banksRootDao.queryBankByName(bankName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertHeadquarters(Headquarters headquarters) {
        return banksRootDao.insertHeadquarters(headquarters);
    }

    @Override
    public int getHeadquartersCount() {
        return banksRootDao.getHeadquartersCount();
    }

    @Override
    public List<Headquarters> queryHeadquarters(String search, int pageSize, int pageNum) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        List<Headquarters> list = banksRootDao.queryHeadquarters(map);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyHeadquarters(Headquarters headquarters) {
        return banksRootDao.modifyHeadquarters(headquarters);
    }

    @Override
    public List<Branch> queryBranchByBankNo(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.queryBranchByBankNo(number);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeHeadquarters(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeHeadquarters(number);
    }

    @Override
    public List<Headquarters> getAllHeadquarters(int bankNo) {
        return banksRootDao.getAllHeadquarters(bankNo);
    }

    @Override
    public int getbranchCount(String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.getbranchCount(map);
    }

    @Override
    public List<Branch> queryBranchs(String search, int pageSize, int pageNum, String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        map.put("type" , type);
        map.put("bankNo" , bankNo);
        List<Branch> list = banksRootDao.queryBranchs(map);
        int[] ids = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++){
            Branch branch = list.get(i);
            ids[i] = (int) branch.getId();
        }
        //获取各个分行业务员的总数
        List<Map<Object , Object>> list2 = banksRootDao.getBranchSalemenCount(ids);
        for (int i = 0 ; i < list2.size() ; i++){
            Map<Object,Object> map2 = list2.get(i);
            for (int j = 0 ; j < list.size() ; j++){
                long id = (long) map2.get("bankNo");
                if(id == list.get(j).getId()){
                   Number num = (Number) map2.get("count");
                    list.get(j).setCount(num.intValue());
                }
            }
        }
        return list;
    }

    @Override
    public Branch queryBranchByName(String branchName) {
        return banksRootDao.queryBranchByName(branchName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertBranchs(Branch branch) {
        return banksRootDao.insertBranchs(branch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyBranchs(Branch branch) {
        return banksRootDao.modifyBranchs(branch);
    }

    @Override
    public List<BranchsChildren> queryBranchChildrenByBranchId(String[] numberArr,int type) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("number" , number);
        map.put("type" , type);
        return banksRootDao.queryBranchChildrenByBranchId(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeBranch(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeBranchs(number);
    }

    @Override
    public int getBranchChilCount(String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.getBranchChilCount(map);
    }

    @Override
    public List<BranchsChildren> queryBranchsChild(String search, int pageSize, int pageNum, String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        map.put("type",type);
        map.put("bankNo",bankNo);
        List<BranchsChildren> list = banksRootDao.queryBranchsChild(map);
        int[] ids = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++){
            BranchsChildren bc = list.get(i);
            ids[i] = bc.getId();
        }
        //获取各个支行业务员的总数
        List<Map<Object , Object>> list2 = banksRootDao.getBranchSalemenCount(ids);
        for (int i = 0 ; i < list2.size() ; i++){
            Map<Object,Object> map2 = list2.get(i);
            for (int j = 0 ; j < list.size() ; j++){
                long id = (long) map2.get("bankNo");
                if(id == list.get(j).getId()){
                    Number num = (Number) map2.get("count");
                    list.get(j).setCount(num.intValue());
                }
            }
        }
        return list;
    }

    @Override
    public List<Branch> getAllBranchs(int bankNo) {
        return banksRootDao.getAllBranchs(bankNo);
    }

    @Override
    public BranchsChildren queryBranchChildByName(String childName) {
        return banksRootDao.queryBranchChildByName(childName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertBranchsChild(BranchsChildren bc) {
        return banksRootDao.insertBranchsChild(bc);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int modifyBranchsChild(BranchsChildren bc) {
        return banksRootDao.modifyBranchsChild(bc);
    }

    @Override
    public List<BranchsChildren> querySalemanByBranchChildId(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.querySalemanByBranchChildId(number);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeBranchChild(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeBranchChild(number);
    }

    @Override
    public int getBankUsersCount(String type, int bankNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("bankNo",bankNo);
        return banksRootDao.getBankUsersCount(map);
    }

    @Override
    public List<BranchsChildren> queryBankUsers(String search, int pageSize, int pageNum, String type, int bankNo,String name,String mobile) {
        Map<String,Object> map = new HashMap<>();
        map.put("search",search);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        map.put("type",type);
        map.put("bankNo",bankNo);
        map.put("name",name);
        map.put("mobile",mobile);
        return banksRootDao.queryBankUsers(map);
    }

    @Override
    public List<Headquarters> getHeadquarters(int bankNo) {
        return banksRootDao.getHeadquarters(bankNo);
    }

    @Override
    public List<BranchsChildren> getBranchChild(int bankNo) {
        return banksRootDao.getBranchsChild(bankNo);
    }

    @Override
    public List<Branch> getBranchs(int bankNo) {
        return banksRootDao.getBranchs(bankNo);
    }

    @Override
    public List<BranchsChildren> getBranchChildByID(int bankNo) {
        return banksRootDao.getBranchChildByID(bankNo);
    }

    @Override
    public List<BankRoles> getRoleByType(int id, String type) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("type",type );
        return banksRootDao.getRoleByType(map);
    }

    @Override
    public BankUsers getUserByName(String name) {
        return banksRootDao.getUserByName(name);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertBankUser(BankUsers bankUsers) {
        return banksRootDao.insertBankUser(bankUsers);

    }

    @Override
    public int modifyBankUser(BankUsers bankUsers) {
        return banksRootDao.modifyBankUser(bankUsers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int removeBankUser(String[] numberArr) {
        int[] number = new int[numberArr.length];
        for (int i = 0 ; i < numberArr.length ; i++){
            number[i] = Integer.parseInt(numberArr[i]);
        }
        return banksRootDao.removeBankUser(number);
    }

    @Override
    public BankUsers getUserByMobile(String mobile) {
        return banksRootDao.getUserByMobile(mobile);
    }



    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public ResultInfo insertBusinessInfo(Business business, String password) {
        ResultInfo resultInfo = new ResultInfo();
        Group group = new Group();
        group.setName(business.getGroupName().trim());
        //新增集团
        int result = banksRootDao.insertGroup(group);
        if(result == 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增集团失败");
            return resultInfo;
        }
        Company company = new Company();
        company.setName(business.getCompanyName().trim());
        company.setGroup_num(group.getGroupNumber());
        //生成4位邀请码
        company.setCode(StringUtil.getStringRandom(4));
        //新增公司
        int result2 = banksRootDao.insertCompany(company);
        if(result2 == 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增公司失败");
            return resultInfo;
        }
        Roles roles = new Roles();
        roles.setCompanyNumber(String.valueOf(company.getCompanyNumber()));
        roles.setRole_name("管理员");
        roles.setCompanyName(business.getCompanyName().trim());
        //新增角色信息
        int result3 = banksRootDao.insertRole(roles);
        if (result3 == 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增角色失败");
            return resultInfo;
        }

        String date = sdf.format(new Date());
        User user = new User();
        String pwd= MD5Util.getEncryptedPwd(password);
        user.setLogin_password(pwd);
        user.setCreateTime(date);
        user.setGroupNumber(String.valueOf(group.getGroupNumber()));
        user.setCompanyNumber(String.valueOf(company.getCompanyNumber()));
        user.setRole_id(roles.getRole_id());
        user.setUserName(business.getUserName());
        user.setMobile(business.getMobile());
        //新增用户
        int reuslt4 =  banksRootDao.insertEmployee(user);
        if (reuslt4 == 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增用户失败");
            return resultInfo;
        }
        business.setGroupNo(group.getGroupNumber());
        business.setCompanyNo(company.getCompanyNumber());
        business.setUserid(user.getId());
        //新增业务信息
        int result5 = banksRootDao.insertBusiness(business);
        if(result5 == 0){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增业务失败");
            return resultInfo;
        }
        //新增权限层级关系
        int result6 = banksRootDao.insertPermission(user);
        if(result6 <= 0){
            resultInfo.setMessage("新增权限层级关系失败");
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            return resultInfo;
        }
        //新增角色权限菜单
        String roleId = String.valueOf(roles.getRole_id());
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> configMap = StringUtil.loadConfig();
        String menu = (String) configMap.get("menuId");
        String[] arr = menu.split(",");
        for (int i = 0 ; i <arr.length ; i++){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("menuId" , arr[i]);
            paramMap.put("roleId",roleId);
            list.add(paramMap);
        }
        int result7 = banksRootDao.insertRoleMenu(list);
        if(result7 ==0){
            resultInfo.setMessage("新增角色权限菜单失败");
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            return resultInfo;
        }
        resultInfo.setMessage("新增业务成功");
        resultInfo.setCode(IConstants.QT_CODE_OK);
        return resultInfo;

    }

    @Override
    public int getBusinessCount(String type, int roleType, int bankNo,int number) {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("roleType",roleType);
        map.put("bankNo",bankNo);
        map.put("number",number);
        return banksRootDao.getBusinessCount(map);
    }

    @Override
    public List<Business> queryBusiness(int pageSize, int pageNum, String type, int bankNo, int roleType,
                                        String heaquarters, String branch, String child, String name, String groupName, int number) {
        Map<String,Object> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
        map.put("type", type);
        map.put("bankNo", bankNo);
        map.put("roleType", roleType);
        map.put("heaquarters", heaquarters);
        map.put("branch", branch);
        map.put("child", child);
        map.put("name", name);
        map.put("groupName", groupName);
        map.put("number", number);
        return banksRootDao.queryBusiness(map);
    }

    @Override
    public int modifyPwd(String mobile, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("password",password);
        return banksRootDao.modifyPwd(map);
    }


}
