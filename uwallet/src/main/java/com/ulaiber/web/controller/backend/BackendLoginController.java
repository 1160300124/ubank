package com.ulaiber.web.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.model.Menu;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;

import java.util.List;

/**
 * 后台登录控制器
 * 
 * @author huangguoqing
 *
 */
@Controller
@RequestMapping("/backend/")
public class BackendLoginController extends BaseController {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(BackendLoginController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("tologin")
	public String toLogin(HttpServletRequest request, HttpServletResponse response){
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo login(User user, HttpServletRequest request, HttpServletResponse response){
		logger.info("Start to login...");
		ResultInfo retInfo = new ResultInfo();
		if (!ObjUtil.notEmpty(user.getUserName()) || !ObjUtil.notEmpty(user.getLogin_password())){
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("userName or password is empty.");
			return retInfo;
		}
		User dbuser = userService.getUserByName(user.getUserName());
		if (!ObjUtil.notEmpty(dbuser)){
			retInfo.setCode(IConstants.QT_MOBILE_NOT_EXISTS);
			retInfo.setMessage("user not exists.");
			return retInfo;
		}
		
		if (!MD5Util.validatePwd(user.getLogin_password(), dbuser.getLogin_password())){
			retInfo.setCode(IConstants.QT_NAME_OR_PWD_OEEOR);
			retInfo.setMessage("mobile or password error.");
			return retInfo;
		}
		
		//放入session
		request.getSession().setAttribute(IConstants.UBANK_BACKEND_USERSESSION, dbuser);
		
		retInfo.setCode(IConstants.QT_CODE_OK);
		retInfo.setMessage("login successed.");
		return retInfo;
	}

	/**
	 * 获取系统所有菜单
	 * @return menu
	 */
	@RequestMapping(value = "getAllMenu")
	@ResponseBody
	public List<Menu> getAllMenu(HttpServletRequest request){
		List<Menu> menu = userService.getAllMenu();
		return menu;

	};

}
