package com.ulaiber.web.controller.backend;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.BankUsers;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.BanksRootService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;
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
import java.util.List;

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
//        request.getSession().setAttribute("userName", bankUser.getName());
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
//            session.removeAttribute("userName");
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

}
