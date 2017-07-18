package com.ulaiber.web.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.UUIDGenerator;

/**
 * 登录控制器
 * 
 * @author huangguoqing
 *
 */
@Controller
@RequestMapping("/api/v1/")
public class LoginController extends BaseController{
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo login(User user, HttpServletRequest request, HttpServletResponse response){
		logger.info("Start to login...");
		ResultInfo retInfo = new ResultInfo();
		if (!ObjUtil.notEmpty(user.getMobile()) || !ObjUtil.notEmpty(user.getLogin_password())){
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("mobile or password is empty.");
			return retInfo;
		}
		User dbuser = userService.findByMobile(user.getMobile());
		if (!ObjUtil.notEmpty(dbuser)){
			retInfo.setCode(IConstants.QT_MOBILE_NOT_EXISTS);
			retInfo.setMessage("mobile not exists.");
			return retInfo;
		}
		
		if (!MD5Util.validatePwd(user.getLogin_password(), dbuser.getLogin_password())){
			retInfo.setCode(IConstants.QT_NAME_OR_PWD_OEEOR);
			retInfo.setMessage("mobile or password error.");
			return retInfo;
		}
		
		String login_ticket = UUIDGenerator.getUUID();
		String access_token = UUIDGenerator.getUUID();
		user.setLogin_ticket(login_ticket);
		user.setAccess_token(access_token);
		boolean flag = userService.update(user);
		if (flag){

			User tempUser = new User();
			tempUser.setId(dbuser.getId());
			tempUser.setUserName(dbuser.getUserName());
			tempUser.setMobile(dbuser.getMobile());
			tempUser.setLogin_ticket(login_ticket);
			tempUser.setAccess_token(access_token);
			tempUser.setReserve_mobile(dbuser.getReserve_mobile());
			tempUser.setCardNo(dbuser.getCardNo());
			tempUser.setBank(dbuser.getBank());
			retInfo.setData(tempUser);
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("login successed.");
		}
		return retInfo;
	}

}
