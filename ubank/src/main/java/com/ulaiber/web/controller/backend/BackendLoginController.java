package com.ulaiber.web.controller.backend;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.model.Company;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.StringUtil;

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

	/**
	 * 集团用户登录页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("tologin")
	public String toLogin(HttpServletRequest request, HttpServletResponse response){
		return "login";
	}

	/**
	 * 跳转集团系统后台首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "index";
	}
	
	@RequestMapping("test")
	public String test(HttpServletRequest request, HttpServletResponse response){
		return "test";
	}

	/**
	 * 集团后台用户登录
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo login(User user, HttpServletRequest request, HttpServletResponse response){
		logger.debug("backend login start...");
		ResultInfo retInfo = new ResultInfo();
		if (!ObjUtil.notEmpty(user.getMobile()) || !ObjUtil.notEmpty(user.getLogin_password())){
			retInfo.setMessage("mobile or password is empty.");
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			logger.error("用户名或密码不能为空。");
			return retInfo;
		}
		try {
			User dbuser = userService.findByMobile(user.getMobile());
			if (!ObjUtil.notEmpty(dbuser)){
				logger.error("用户名不存在。");
				retInfo.setCode(IConstants.QT_MOBILE_NOT_EXISTS);
				retInfo.setMessage("user not exists.");
				return retInfo;
			}
			
			if (!MD5Util.validatePwd(user.getLogin_password(), dbuser.getLogin_password())){
				logger.error("用户名或密码错误。");
				retInfo.setCode(IConstants.QT_NAME_OR_PWD_OEEOR);
				retInfo.setMessage("mobile or password error.");
				return retInfo;
			}
			String userName = dbuser.getUserName();
			String sysflag = dbuser.getSysflag();
			//获取当前用户所有权限菜单
			List<Menu> menu = userService.getAllMenuByUser(userName,sysflag);
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
			String number = "";
			String name = "";
			//获取当前用用户所属公司
			if("1".equals(sysflag)){
				String[] comNum = dbuser.getCompanyNumber().split(",");
				int numbers[] = new int[comNum.length];
				for (int i = 0 ; i < comNum.length ; i++){
					numbers[i] = Integer.parseInt(comNum[i]);
				}
				List<Company> comList = userService.getCompanyByNum(numbers);
				if(comList.size() > 0){
					for (int i = 0 ; i < comList.size(); i++){
						Company com = comList.get(i);
						if(i > 0 ){
							number += "," + com.getCompanyNumber();
							name += "," + com.getName();
						}else{
							number += com.getCompanyNumber();
							name += com.getName();
						}
					}
				}
				dbuser.setCompanyNumber(number);
				dbuser.setCom_name(name);
			}
			//放入session
			request.getSession().setAttribute(IConstants.UBANK_BACKEND_USERSESSION, dbuser);
			request.getSession().setAttribute(IConstants.UBANK_BACKEND_USERMENU, str);
			request.getSession().setAttribute("userName", dbuser.getUserName());
			logger.info(dbuser.getUserName() + " login successed.");
			
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage(dbuser.getUserName() + "登录成功。");
		} catch (Exception e) {
			logger.error("login exception: " , e);
		}
		logger.debug("backedn login end...");
		return retInfo;
	}



	/**
	 * 根据用户名获取系统所有菜单
	 * @return menu
	 */
	@RequestMapping(value = "getAllMenu")
	@ResponseBody
	public List<Menu> getAllMenu(@Param("userName") String userName,@Param("sysflag") String sysflag){
		List<Menu> menu = userService.getAllMenuByUser(userName,sysflag);
		return menu;
	};

	/**
	 * 修改密码
	 * @param mobile 电话号码
	 * @param newPwd 新密码
	 * @param oldPwd 旧密码
	 * @return
	 */
	@RequestMapping(value = "modifyPassword")
	@ResponseBody
	public ResultInfo modifyPWD(@Param("mobile") String mobile,@Param("newPwd") String newPwd,@Param("oldPwd") String oldPwd){
		logger.info(">>>>>>>>>>开始修改登录密码");
		ResultInfo resultInfo = new ResultInfo();
		try {
			//根据当前用户电话查询登录密码
			User user = userService.findByMobile(mobile);
			oldPwd = MD5Util.getEncryptedPwd(oldPwd);
			if (!user.getLogin_password().equals(oldPwd)){
				logger.error(">>>>>>>>>>旧密码错误");
				resultInfo.setCode(IConstants.QT_CODE_ERROR);
				resultInfo.setMessage("旧密码错误");
				return resultInfo;
			}
			//加密
			String password = MD5Util.getEncryptedPwd(newPwd);
			//修改密码
			int result = userService.modifyPwd(mobile,password);
			if(result <= 0){
				logger.error(">>>>>>>>>>手机号为："+mobile+"的用户修改密码失败");
				resultInfo.setCode(IConstants.QT_CODE_ERROR);
				resultInfo.setMessage("修改密码失败");
				return resultInfo;
			}
			logger.error(">>>>>>>>>>手机号为："+mobile+"的用户修改密码成功");
			resultInfo.setCode(IConstants.QT_CODE_OK);
			resultInfo.setMessage("修改密码成功");
		}catch (Exception e){
			logger.error(">>>>>>>>>>修改登录密码失败",e);
		}
		return resultInfo;
	}

}
