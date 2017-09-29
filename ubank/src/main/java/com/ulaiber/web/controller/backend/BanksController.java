package com.ulaiber.web.controller.backend;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.*;
import com.ulaiber.web.service.BanksRootService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 跳转银行后台系统首页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("bank_index")
    public String toBank_index(HttpServletRequest request, HttpServletResponse response){
        return "banks/bank_index";
    }

    /**
     * 银行用户登录页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("toBankLogin")
    public String toBankLogin(HttpServletRequest request, HttpServletResponse response){
        return "banks/bankLogin";
    }

    /**
     * 跳转总部页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("headquarters")
    public String toHeadquarters(HttpServletRequest request, HttpServletResponse response){
        return "banks/headquarters";
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
     * 根据银行编号删除银行
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
        if(comList.size() > 0 ){
            resultInfo.setCode(300);
            resultInfo.setMessage("该集团存在公司，请先删除该集团下的公司");
            return resultInfo;
        }
        //int result = permissionService.deleteGroup(numberArr);
        if(result > 0){
            resultInfo.setCode(200);
            resultInfo.setMessage("删除成功");
        }else{
            resultInfo.setCode(500);
            resultInfo.setMessage("删除失败");
        }
        return ;
    }
}
