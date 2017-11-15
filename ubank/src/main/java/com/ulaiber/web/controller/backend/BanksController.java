package com.ulaiber.web.controller.backend;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.BanksRootService;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 银行权限Controller
 * Created by daiqingwen on 2017/9/26.
 */
@Controller
@RequestMapping("/backend_bank/")
public class BanksController extends BaseController {

    public static final Logger logger = Logger.getLogger(BanksController.class);


    @Resource
    private UserService userService;

    @Resource
    private BanksRootService banksRootService;

    @Resource
    private PermissionService permissionService;

    /**
     * 跳转银行后台系统首页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "bank_index", method = RequestMethod.GET)
    public String toBank_index(HttpServletRequest request, HttpServletResponse response){
        return "banks/bank_index";
    }

    /**
     * 银行用户登录页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toBankLogin", method = RequestMethod.GET)
    public String toBankLogin(HttpServletRequest request, HttpServletResponse response){
        return "banks/bankLogin";
    }

    /**
     * 跳转总部页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "headquarters", method = RequestMethod.GET)
    public String toHeadquarters(HttpServletRequest request, HttpServletResponse response){
        return "banks/headquarters";
    }

    /**
     * 跳转分行页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "branch", method = RequestMethod.GET)
    public String toBranch(HttpServletRequest request, HttpServletResponse response){
        return "banks/branch";
    }

    /**
     * 跳转支行页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "branch_children", method = RequestMethod.GET)
    public String toBranch_child(HttpServletRequest request, HttpServletResponse response){
        return "banks/branch_children";
    }

    /**
     * 跳转银行用户页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "bankUser", method = RequestMethod.GET)
    public String tobankUsers(HttpServletRequest request, HttpServletResponse response){
        return "banks/bankUsers";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "expand", method = RequestMethod.GET)
    public String toExpand(HttpServletRequest request, HttpServletResponse response){
        return "banks/expand";
    }


    /**
     * 银行后台用户登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "login_bank", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo loginBank(BankUsers user, HttpServletRequest request, HttpServletResponse response){
        logger.info(">>>>>>>>开始登录银行后台系统");
        ResultInfo resultInfo = new ResultInfo();
        if (!ObjUtil.notEmpty(user.getMobile()) || !ObjUtil.notEmpty(user.getPassword())){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("mobile or password is empty.");
            return resultInfo;
        }
        BankUsers bankUser = userService.bankUserLogin(user.getMobile());
        if (!ObjUtil.notEmpty(bankUser)){
            resultInfo.setCode(IConstants.QT_MOBILE_NOT_EXISTS);
            resultInfo.setMessage("user not exists.");
            return resultInfo;
        }

        if (!MD5Util.validatePwd(user.getPassword(), bankUser.getPassword())){
            resultInfo.setCode(IConstants.QT_NAME_OR_PWD_OEEOR);
            resultInfo.setMessage("mobile or password error.");
            return resultInfo;
        }
        String userName = bankUser.getName();
        List<Menu> menu = banksRootService.getBankMenuByUser(userName);
        String str = "";
        for (int i = 0; i < menu.size() ; i++){
            Menu me = menu.get(i);
            String url = me.getUrl();
            if(!StringUtil.isEmpty(url)){
                url = url.substring(url.lastIndexOf("/") + 1,url.length());
                if(i > 0){
                    str += "," + url ;
                }else{
                    str += url ;
                }
            }
        }
        //放入session
        request.getSession().setAttribute(IConstants.UBANK_BACKEND_USERSESSION, bankUser);
        logger.info(bankUser.getName() + " login successed.");

        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setMessage("login successed.");
        logger.debug("backedn login end...");
        return resultInfo ;
    }

    /**
     * 银行后台用户退出登录
     * @param request
     * @param response
     */
    @RequestMapping(value = "bankLogout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response){
        try {
            HttpSession session = request.getSession();
            session.removeAttribute(IConstants.UBANK_BACKEND_USERSESSION);
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/backend_bank/toBankLogin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据角色名获取银行管理页
     * @param userName 用户名
     * @return Menu
     */
    @RequestMapping(value = "getBankMenu", method = RequestMethod.POST)
    @ResponseBody
    public List<Menu> getAllMenu(@Param("userName") String userName){
        List<Menu> menu = banksRootService.getBankMenuByUser(userName);
        return menu;
    };

    /**
     * 新增总行
     * @param headquarters
     * @param flag 标识。 0 表示新增操作，1 表示修改操作
     * @return ResultInfo
     */
    @RequestMapping(value = "addHeadquarters", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addHeadquarters(Headquarters headquarters,@Param("flag") int flag,@Param("bankNo") int bankNo){
        ResultInfo resultInfo = new ResultInfo();
        if(flag == 0){ //新增
            //查询当前新增银行是否已创建
            String bankName = headquarters.getBankName();
            Headquarters bank = banksRootService.queryBankByName(bankName);
            if(!StringUtil.isEmpty(bank)){
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                resultInfo.setMessage("银行已存在");
                return resultInfo;
            }
            //新增总行
            int result = banksRootService.insertHeadquarters(headquarters);
            if (result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("新增失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("新增成功");
        }else{  //修改
            //根据银行编号修改银行信息
            headquarters.setId(bankNo);
            int result = banksRootService.modifyHeadquarters(headquarters);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("修改失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("修改成功");
        }
        return resultInfo;
    }


    /**
     * 查询总行信息
     * @param search 搜索字段
     * @param pageSize 页大小
     * @param pageNum 页码
     * @return map
     */
    @RequestMapping(value = "queryHeadquarters", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryHeadquarters(@Param("search") String search,@Param("pageSize") int pageSize, @Param("pageNum") int pageNum ){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        //获取总行数量
        int totalCount = banksRootService.getHeadquartersCount();
        if(totalCount <= 0){
            map.put("total",totalCount);
            map.put("rows","");
            return map;
        }
        //查询总行信息
        List<Headquarters> list = banksRootService.queryHeadquarters(search,pageSize,pageNum);
        map.put("total",totalCount);
        map.put("rows",list);
        return map;
    }

    /**
     * 删除总行
     * @param numbers 银行编号
     * @return ResultInfo
     */
    @RequestMapping(value = "removeHeadquarters", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo removeHeadquarters(@Param("numbers")String numbers){
        ResultInfo resultInfo = new ResultInfo();
        String[] numberArr = numbers.split(",");
        //根据总行编号查询是否存在分部
        List<Branch> comList = banksRootService.queryBranchByBankNo(numberArr);
        int type = 0; //0 总行 ， 1 分行
        List<BranchsChildren> list2 = banksRootService.queryBranchChildrenByBranchId(numberArr,type);
        if(comList.size() > 0 ){
            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
            resultInfo.setMessage("该总行下存在分行，请先删除分行");
            return resultInfo;
        }
        if(list2.size() > 0){
            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
            resultInfo.setMessage("该总行下存在支行，请先删除支行");
            return resultInfo;
        }
        //删除
        int result = banksRootService.removeHeadquarters(numberArr);
        if(result > 0){
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("删除成功");
        }else{
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("删除失败");
        }
        return resultInfo;
    }

    /**
     * 获取所有总部
     * @return ResultInfo
     */
    @RequestMapping(value = "getAllHeadquarters", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getAllHeadquarters(int bankNo){
        List<Headquarters> list = banksRootService.getAllHeadquarters(bankNo);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setData(list);
        return resultInfo;
    }

    /**
     * 分部查询
     * @param search 搜索内容
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type  标识。所属部门是总行？分行？支行？
     * @param bankNo 角色所属部门
     * @return map
     */
    @RequestMapping(value = "queryBranchs", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryBranchs(@Param("search") String search,@Param("pageSize") int pageSize,@Param("pageNum") int pageNum,
                                          @Param("type") String type,@Param("bankNo") int bankNo ){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        //获取分行数量
        int totalCount = banksRootService.getbranchCount(type,bankNo);
        if(totalCount <= 0){
            map.put("total",totalCount);
            map.put("rows","");
            return map;
        }
        List<Branch> list = banksRootService.queryBranchs(search,pageSize,pageNum,type,bankNo);
        map.put("total",totalCount);
        map.put("rows",list);
        return map;
    }

    /**
     * 新增分行
     * @param branch 分行信息
     * @param flag 标识。0 新增，1 修改
     * @return ResultInfo
     */
    @RequestMapping(value = "saveBranchs", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveBranchs(Branch branch,@Param("flag") int flag,@Param("id") int id){
        ResultInfo resultInfo = new ResultInfo();
        if(flag == 0){  //新增
            //根据名称查询分行是否已存在
            String branchName = branch.getName();
            Branch br = banksRootService.queryBranchByName(branchName);
            if(!StringUtil.isEmpty(br)){
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                resultInfo.setMessage("该分行已存在");
                return resultInfo;
            }
            //新增
            int result = banksRootService.insertBranchs(branch);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("新增失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("新增成功");
        }else{
            //根据分行编号修改分行信息
            branch.setId(id);
            int result = banksRootService.modifyBranchs(branch);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("修改失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("修改成功");
        }
        return resultInfo;
    }

    /**
     * 删除分行
     * @param numbers 分行Id
     * @return ResultInfo
     */
    @RequestMapping(value = "removeBranchs", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo removeBranchs(@Param("numbers")String numbers){
        ResultInfo resultInfo = new ResultInfo();
        String[] numberArr = numbers.split(",");
        //根据分行编号查询是否存在支行
        int type = 1; //0 总行 ， 1 分行
        List<BranchsChildren> comList = banksRootService.queryBranchChildrenByBranchId(numberArr,type);
        if(comList.size() > 0 ){
            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
            resultInfo.setMessage("该分行下存在支行，请先删除支行");
            return resultInfo;
        }
        //删除
        int result = banksRootService.removeBranch(numberArr);
        if(result > 0){
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("删除成功");
        }else{
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("删除失败");
        }
        return resultInfo;
    }

    /**
     * 查询支行
     * @param search 搜索关键字
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo  角色所属部门
     * @return map
     */
    @RequestMapping(value = "queryBranchsChil", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryBranchsChildren(@Param("search") String search,@Param("pageSize") int pageSize,@Param("pageNum") int pageNum,
                                                   @Param("type") String type,@Param("bankNo") int bankNo ){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        //获取分行数量
        int totalCount = banksRootService.getBranchChilCount(type,bankNo);
        if(totalCount <= 0){
            map.put("total",totalCount);
            map.put("rows","");
            return map;
        }
        List<BranchsChildren> list = banksRootService.queryBranchsChild(search,pageSize,pageNum,type,bankNo);
        map.put("total",totalCount);
        map.put("rows",list);
        return map;
    }

    /**
     * 获取所有分部
     * @param bankNo 银行编号
     * @return ResultInfo
     */
    @RequestMapping(value = "getAllBranchs", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo queryAllBranchs(int bankNo){
        List<Branch> list = banksRootService.getAllBranchs(bankNo);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(IConstants.QT_CODE_OK);
        resultInfo.setData(list);
        return resultInfo;
    }

    /**
     * 新增支行
     * @param bc 支行信息
     * @param flag 标识。0 新增，1 修改
     * @return ResultInfo
     */
    @RequestMapping(value = "saveBranchsChil", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveBranchsChild(HttpServletRequest request,BranchsChildren bc, @Param("flag") int flag){
        ResultInfo resultInfo = new ResultInfo();
        String bankName = request.getParameter("bankName");
        try {
            bankName = new String(bankName.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bc.setBankName(bankName);
        if(flag == 0){ //新增
            String childName = bc.getName();
            //根据支行名称查询支行是否已存在
            BranchsChildren bran = banksRootService.queryBranchChildByName(childName);
            if(!StringUtil.isEmpty(bran)){
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                resultInfo.setMessage("该支行已存在");
                return resultInfo;
            }
            //新增
            int result = banksRootService.insertBranchsChild(bc);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("新增失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("新增成功");
        }else{ //修改
            int result = banksRootService.modifyBranchsChild(bc);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("修改失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("修改成功");

        }
        return resultInfo;
    }

    /**
     * 删除支行
     * @param numbers 支行ID
     * @return ResultInfo
     */
    @RequestMapping(value = "removeBranchsChild", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo removeBranchsChild(@Param("numbers") String numbers){
        ResultInfo resultInfo = new ResultInfo();
        String[] numberArr = numbers.split(",");
        //根据支行编号查询是否存在业务员
        List<BranchsChildren> comList = banksRootService.querySalemanByBranchChildId(numberArr);
        if(comList.size() > 0 ){
            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
            resultInfo.setMessage("该支行下存在业务员，请先删除业务员");
            return resultInfo;
        }
        //删除
        int result = banksRootService.removeBranchChild(numberArr);
        if(result > 0){
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("删除成功");
        }else{
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("删除失败");
        }
        return resultInfo;
    }

    /**
     * 查询银行用户
     * @param search 搜索关键字
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo  角色所属部门
     * @return map
     */
    @RequestMapping(value = "queryBankUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryBankUsers(@Param("search") String search,@Param("pageSize") int pageSize,@Param("pageNum") int pageNum,
                                             @Param("type") String type,@Param("bankNo") int bankNo,@Param("name") String name,@Param("mobile") String mobile){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        //获取银行用户数量
        int totalCount = banksRootService.getBankUsersCount(type,bankNo);
        if(totalCount <= 0){
            map.put("total",totalCount);
            map.put("rows","");
            return map;
        }
        List<BranchsChildren> list = banksRootService.queryBankUsers(search,pageSize,pageNum,type,bankNo,name,mobile);
        map.put("total",totalCount);
        map.put("rows",list);
        return map;
    }

    /**
     * 获取银行树节点
     * @param type 标识。所属部门是总行？分行？支行？
     * @param bankNo 银行编号
     * @return Map
     */
    @RequestMapping(value = "bankTrees", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> bankTree(@Param("type") String type,@Param("bankNo") int bankNo ){
        List<Headquarters> h_list = new ArrayList<>();
        List<Branch> b_list = new ArrayList<>();
        List<BranchsChildren> c_list = new ArrayList<>();
        List<Map<String,Object>> headList = new ArrayList<>();
        List<Map<String,Object>> branList = new ArrayList<>();
        List<Map<String,Object>> resultList = new ArrayList<>();
        if(type.equals("0") ){ //总行管理员
            //根据银行编号获取总行
            h_list = banksRootService.getHeadquarters(bankNo);
            //根据总行编号获取分行
            b_list = banksRootService.getAllBranchs(bankNo);
            //根据银行编号获取支行
            c_list = banksRootService.getBranchChild(bankNo);
            //循环取出总行
            for (int i = 0;i < h_list.size(); i++){
                Map<String,Object> h_map = new HashMap<>();
                Headquarters head =  h_list.get(i);
                h_map.put("id",head.getId());
                h_map.put("name",head.getBankName());
                h_map.put("type","0");
                h_map.put("children",headList);
                h_map.put("isParent", true);
                h_map.put("open", true);
                resultList.add(h_map);
            }
            //循环取出分行
            if(b_list.size() > 0){
                for (int j = 0 ; j < b_list.size(); j++){
                    Map<String,Object> b_map = new HashMap<>();
                    Branch bra = b_list.get(j);
                    for (int i = 0;i < h_list.size(); i++){
                        Headquarters head =  h_list.get(i);
                        if(bra.getHeadquartersNo() == head.getId()){
                            b_map.put("id",bra.getId());
                            b_map.put("name",bra.getName());
                            b_map.put("type","1");
                            b_map.put("children",branList);
                            b_map.put("open",true);
                            headList.add(b_map);
                        }
                    }

                }
            }
            //循环取出支行
            if(c_list.size() > 0){
                //两种情况：一，支行属于总行；二，支行属于分行
                //如果当前支行属于分行
                for (int j = 0 ; j < c_list.size(); j++){
                    Map<String,Object> c_map = new HashMap<>();
                    BranchsChildren chil = c_list.get(j);
                    for (int t = 0 ; t < b_list.size(); t++){
                        Branch bra = b_list.get(t);
                        if(chil.getBankNo() == bra.getId()){
                            c_map.put("id",chil.getId());
                            c_map.put("name",chil.getName());
                            c_map.put("type","2");
                            branList.add(c_map);
                        }
                    }

                }
                //如果当前支行属于总行
                for (int k = 0 ; k < c_list.size(); k++){
                    Map<String,Object> cc_map = new HashMap<>();
                    BranchsChildren child = c_list.get(k);
                    for (int i = 0;i < h_list.size(); i++){
                        Headquarters head =  h_list.get(i);
                        if(child.getBankNo() == head.getId()){
                            cc_map.put("id",child.getId());
                            cc_map.put("name",child.getName());
                            cc_map.put("type","2");
                            headList.add(cc_map);
                        }
                    }

                }
            }

        }else if(type.equals("1")){ //分行管理员
            //根据分行编号获取分行
            b_list = banksRootService.getBranchs(bankNo);
            //根据银行编号获取支行
            c_list = banksRootService.getBranchChild(bankNo);
            //循环取出分行
            for (int j = 0 ; j < b_list.size(); j++){
                Map<String,Object> b_map = new HashMap<>();
                Branch bra = b_list.get(j);
                b_map.put("id",bra.getId());
                b_map.put("name",bra.getName());
                b_map.put("type","1");
                b_map.put("children",branList);
                b_map.put("isParent", true);
                b_map.put("open",true);
                headList.add(b_map);
            }

            //循环取出支行
            if(c_list.size() > 0){
                //如果当前支行属于分行
                for (int j = 0 ; j < c_list.size(); j++){
                    Map<String,Object> c_map = new HashMap<>();
                    BranchsChildren chil = c_list.get(j);
                    for (int t = 0 ; t < b_list.size(); t++){
                        Branch bra = b_list.get(t);
                        if(chil.getBankNo() == bra.getId()){
                            c_map.put("id",chil.getId());
                            c_map.put("name",chil.getName());
                            c_map.put("type","2");
                            branList.add(c_map);
                        }
                    }

                }
            }

        }else if(type.equals("2")){ //支行管理员
            //根据银行编号获取支行
            c_list = banksRootService.getBranchChildByID(bankNo);
            for (int j = 0 ; j < c_list.size(); j++){
                Map<String,Object> c_map = new HashMap<>();
                BranchsChildren chil = c_list.get(j);
                c_map.put("id",chil.getId());
                c_map.put("name",chil.getName());
                c_map.put("type","2");
                c_map.put("isParent", true);
                c_map.put("open",true);
                branList.add(c_map);
            }
        }

        return resultList;
    }


    /**
     * 根据银行类型获取角色
     * @param id 银行编号
     * @param type 标识。所属部门是总行？分行？支行？
     * @return ResultInfo
     */
    @RequestMapping(value = "getRoleByType", method = RequestMethod.POST)
    @ResponseBody
    public List<BankRoles> getBankRoles(@Param("id") int id , @Param("type") String type){
        List<BankRoles> list = banksRootService.getRoleByType(id,type);
        return list;
    }

    /**
     * 新增银行用户
     * @param bankUsers 银行用户信息
     * @param flag 标识。0 新增，1 修改
     * @return ResultInfo
     */
    @RequestMapping(value = "saveBankUser", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveBankUser(BankUsers bankUsers,@Param("flag") int flag){
        ResultInfo resultInfo = new ResultInfo();
        String bankName = bankUsers.getBankName();
        try {
            bankName = new String(bankName.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bankUsers.setBankName(bankName);
        if(flag == 0){
            String name = bankUsers.getName();
            //根据名称查询当前用户是否已存在
            BankUsers user = banksRootService.getUserByName(name);
            if(!StringUtil.isEmpty(user)){
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                resultInfo.setMessage("该员工已存在");
                return resultInfo;
            }
            //根据移动电话查询当前电话是否已存在
            BankUsers us = banksRootService.getUserByMobile(bankUsers.getMobile());
            if(!StringUtil.isEmpty(us)){
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                resultInfo.setMessage("该移动电话已存在");
                return resultInfo;
            }
            //生成6位数的密码
            String password = MD5Util.getEncryptedPwd(bankUsers.getPassword());
            bankUsers.setPassword(password);
            int result = banksRootService.insertBankUser(bankUsers);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("新增失败");
                return resultInfo;
            }
             resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("新增成功");
        }else{
            int result = banksRootService.modifyBankUser(bankUsers);
            if(result <= 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("修改失败");
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("修改成功");
        }

        return resultInfo;

    }

    /**
     * 删除银行员工
     * @param numbers 员工ID
     * @return ResultInfo
     */
    @RequestMapping(value = "removeBankUser", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo removeBankUser(@Param("numbers") String numbers){
        ResultInfo resultInfo = new ResultInfo();
        String[] numberArr = numbers.split(",");
        //根据业务员ID，查询是否存在业务（创建过集团）
//        List<Group> list = banksRootService.queryGroupBySalemanId(numberArr);
//        if(list.size() > 0){
//            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
//            resultInfo.setMessage("该业务员存在业务");
//            return resultInfo;
//        }
        int result = banksRootService.removeBankUser(numberArr);
        if(result > 0){
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage("删除成功");
        }else{
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("删除失败");
        }
        return resultInfo;
    }

    /**
     * 新增业务信息
     * @param business 业务信息
     * @param flag 标识。0 新增，1 修改
     * @param password 管理人登录初始化密码
     * @return ResultInfo
     */
    @RequestMapping(value = "saveBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveBusiness(Business business,@Param("flag") int flag,@Param("password") String password){
        ResultInfo resultInfo = new ResultInfo();
        //查询集团是否已创建
        Group gro = permissionService.searchGroupByName(business.getGroupName().trim());
        if(!StringUtil.isEmpty(gro)){
            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
            resultInfo.setMessage("该集团已存在");
            return resultInfo;
        }
        //查询公司是否已创建
        Company co = permissionService.getComByName(business.getCompanyName().trim());
        if(!StringUtil.isEmpty(co)){
            resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
            resultInfo.setMessage("该公司已存在");
            return resultInfo;
        }
        User emp = permissionService.getEmpByName(business.getUserName().trim(),business.getMobile());
        if(!StringUtil.isEmpty(emp)){
            if(emp.getMobile().equals(business.getMobile())){
                resultInfo.setMessage("电话号码已存在，请重新输入");
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                return resultInfo;
            }else if (emp.getUserName().trim().equals(business.getUserName().trim())){
                resultInfo.setMessage("用户已存在，请重新输入");
                resultInfo.setCode(IConstants.QT_ALREADY_EXISTS);
                return resultInfo;
            }
        }
        //新增业务信息
        ResultInfo result = banksRootService.insertBusinessInfo(business,password);
        return result;
    }

    /**
     * 业务查询
     * @param pageSize 页大小
     * @param pageNum 页码
     * @param type  标识。所属部门是总行？分行？支行？
     * @param bankNo 银行编号
     * @param roleType 角色类型。0 总部管理员，1 分部管理员，2 支部管理员，3业务员
     * @param heaquarters 总行
     * @param branch 分行
     * @param child 支行
     * @param name 业务员名称
     * @param groupName 集团名称
     * @return Map
     */
    @RequestMapping(value = "queryBusiness", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryBusiness(int pageSize, int pageNum, String type, int bankNo,int roleType
            ,String heaquarters,String branch,String child,String name,String groupName,int number){
        if(pageSize <= 0){
            pageSize = 10;
        }
        if (pageNum < 0){
            pageNum = 0;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        //获取业务数量
        int totalCount = banksRootService.getBusinessCount(type,roleType,bankNo,number);
        if(totalCount <= 0){
            map.put("total",totalCount);
            map.put("rows","");
            return map;
        }
        List<Business> list = banksRootService.queryBusiness(pageSize,pageNum,type,bankNo,roleType,heaquarters,branch,child,name,groupName,number);
        map.put("total",totalCount);
        map.put("rows",list);
        return map;
    }

}
