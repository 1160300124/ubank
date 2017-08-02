package com.ulaiber.web.controller.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Bank;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.CaptchaUtil;
import com.ulaiber.web.utils.ObjUtil;

/**
 * 
 * @author huangguoqing
 *
 */
@Controller
@RequestMapping("/api/v1/")
public class UserController extends BaseController{

	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(UserController.class);
	
	/**
	 * 缓存手机验证码
	 * key=手机号， value=验证码
	 * 暂时没做验证码时间有效时间限制，下一个版本TODO
	 */
	private Map<String, String> captchaMap = new ConcurrentHashMap<String, String>();
	
	@Autowired
	private UserService userService;
	
	/**
	 * 注册激活钱包
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo register(User user, Bank bank, HttpServletRequest request, HttpServletResponse response){
		logger.debug("register statrt...");
		ResultInfo retInfo = new ResultInfo(IConstants.QT_CODE_ERROR, "");
		if (!ObjUtil.notEmpty(user) || !ObjUtil.notEmpty(bank)){
			logger.error("register failed: user or bank is empty.");
			retInfo.setMessage("用户或银行信息不能为空。");
			return retInfo;
		}
		if (!ObjUtil.notEmpty(user.getMobile()) || !ObjUtil.notEmpty(user.getReserve_mobile()) || !ObjUtil.notEmpty(bank.getBankNo())){
			logger.error("register failed: mobile or bankNo is empty.");
			retInfo.setMessage("手机号或，预留手机号或银行卡号不能为空。");
			return retInfo;
		}
		//查询手机号是否已被注册
		User user1 = userService.findByMobile(user.getMobile());
		if (ObjUtil.notEmpty(user1)){
			retInfo.setCode(IConstants.QT_MOBILE_EXISTS);
			retInfo.setMessage("手机号已经被注册。");
			logger.error("register failed: mobile is already exists.");
			return retInfo;
		}
		
		//TODO调银行开户接口
		boolean isSuccessed = true;
		if (isSuccessed){
			user.setBank(bank);
			if (userService.save(user)){
				retInfo.setCode(IConstants.QT_CODE_OK);
				retInfo.setMessage("注册成功。");
				logger.info(user.getMobile() + " register successed.");
			}
		}
		else{
			retInfo.setCode(IConstants.QT_OPEN_ACCOUT_ERROR);
			retInfo.setMessage("银行开户失败。");
			logger.info("open an account in bank failed.");
		}
		
		logger.debug("register end...");
		return retInfo;
	}
	
	/**
	 * 获取所有用户
	 * @param request
	 * @return
	 */
	@RequestMapping("getAllUser")
	@ResponseBody
	public List<User> getAllUser(HttpServletRequest request){
		
		List<User> findAll = userService.findAll();
		
		return findAll;
	}
	
	/**
	 * 获取用户信息
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserInfo")
	@ResponseBody
	public ResultInfo getUserInfo(String mobile,HttpServletRequest request, HttpServletResponse response){
		logger.debug("getUserInfo start...");
		ResultInfo retInfo = new ResultInfo(IConstants.QT_CODE_ERROR, "");
		if (!ObjUtil.notEmpty(mobile)){
			retInfo.setMessage("手机号不能为空。");
			logger.equals("mobile is empty.");
			return retInfo;
		}
		User user = userService.findByMobile(mobile);
		if (ObjUtil.notEmpty(user)){
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("获取用户信息成功。");
			logger.info("get user information successed.");
			User tempUser = new User();
			tempUser.setId(user.getId());
			tempUser.setUserName(user.getUserName());
			tempUser.setMobile(user.getMobile());
			tempUser.setReserve_mobile(user.getReserve_mobile());
			tempUser.setBankCardNo(user.getBankCardNo());
			tempUser.setBank(user.getBank());
			tempUser.setCardNo(user.getCardNo());
			//TODO 调二类户接口查询余额
			double balance = 0.00;
			tempUser.setBalance(balance);
			retInfo.setData(tempUser);
		}
		else {
			logger.error("get user information failed: mobile not exists.");
			retInfo.setMessage("手机号不存在。");
		}
		
		logger.debug("getUserInfo end...");
		return retInfo;
	}
	
	/**
	 * 发送验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "sendCaptcha", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo sendCaptcha(String mobile, HttpServletRequest request, HttpServletResponse response){
		
		ResultInfo retInfo = new ResultInfo();
		retInfo.setCode(IConstants.QT_CODE_ERROR);
		
		if (StringUtils.isEmpty(mobile)){
			retInfo.setMessage("手机号不能为空。");
			logger.error("mobile can not be empty.");
			return retInfo;
		}
		logger.info("Start to send captcha...");
		String captcha = CaptchaUtil.getCaptcha();
		captchaMap.put(mobile, captcha);
		String text = IConstants.SMS_TEMPLATE.replace("#code#", captcha);
		String result = CaptchaUtil.singleSend(CaptchaUtil.API_KEY, text, mobile);
		logger.info("send captcha info: " + result);
		Map<String, Object> resultMap = (Map<String, Object>)JSONObject.parse(result);
		if (resultMap.get("code").toString().equals("0")){
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("验证码发送成功。");
			logger.info("send captcha successed.");
		}
		else {
			retInfo.setCode(IConstants.QT_CAPTCHA_SEND_ERROR);
			retInfo.setMessage("验证码发送失败：" + result);
			logger.error("send captcha failed: " + result);
		}
		return retInfo;
	}
	
	/**
	 * validate
	 * 
	 * @param mobile
	 * @param captcha
	 * @param password
	 * @param confirm_password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "validate", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo validateCaptcha(String mobile, String captcha, String password, String confirm_password,
			HttpServletRequest request, HttpServletResponse response){
		
		ResultInfo retInfo = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(captcha) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirm_password)){
			logger.error("params can not be null.");
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("参数不能为空。");
			return retInfo;
		}
		if (!StringUtils.equals(password, confirm_password)){
			logger.error("confirmed password and new password do not match.");
			retInfo.setCode(IConstants.QT_PWD_NOT_MATCH);
			retInfo.setMessage("新密码与确认密码不一致。");
			return retInfo;
		}
		String cap = captchaMap.get(mobile);
		captchaMap.remove(mobile);
		if (!StringUtils.equals(captcha, cap)){
			logger.error("captcha error.");
			retInfo.setCode(IConstants.QT_CAPTCHA_ERROR);
			retInfo.setMessage("验证码错误。");
			return retInfo;
		}
		
		retInfo.setCode(IConstants.QT_CODE_OK);
		return retInfo;
	}
	
	
	/**
	 * 忘记登录密码
	 * 
	 * @param mobile
	 * @param captcha
	 * @param password
	 * @param confirm_password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "forgetLoginPassword", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo forgetPassword(String mobile, String captcha, String password, String confirm_password,
			HttpServletRequest request, HttpServletResponse response){
		logger.debug("forgetLoginPassword start...");
		ResultInfo retInfo = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(captcha) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirm_password)){
			logger.error("params can not be null.");
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("参数不能为空。");
			return retInfo;
		}
		if (!StringUtils.equals(password, confirm_password)){
			logger.error("confirmed password and new password do not match.");
			retInfo.setCode(IConstants.QT_PWD_NOT_MATCH);
			retInfo.setMessage("新密码与确认密码不一致。");
			return retInfo;
		}
		String cap = captchaMap.get(mobile);
		if (!StringUtils.equals(captcha, cap)){
			logger.error("captcha error.");
			retInfo.setCode(IConstants.QT_CAPTCHA_ERROR);
			retInfo.setMessage("验证码错误。");
			return retInfo;
		}
		
		boolean flag = userService.updateLoginPwd(mobile, password);
		if (flag){
			captchaMap.remove(mobile);
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("找回登录密码成功。");
			logger.info(mobile + "find login password successed.");
		}
		else {
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("找回登录密码失败。");
			logger.error(mobile + "find login password failed.");
		}
		
		logger.debug("forgetLoginPassword end...");
		return retInfo;
	}
	
	/**
	 * 忘记支付密码
	 * 
	 * @param mobile
	 * @param captcha
	 * @param password
	 * @param confirm_password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "forgetPayPassword", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo forgetPayPassword(String mobile, String captcha, String password, String confirm_password,
			HttpServletRequest request, HttpServletResponse response){
		logger.debug("forgetPayPassword start...");
		ResultInfo retInfo = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(captcha) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirm_password)){
			logger.error("params can not be null.");
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("参数不能为空。");
			return retInfo;
		}
		if (!StringUtils.equals(password, confirm_password)){
			logger.error("confirmed password and new password do not match.");
			retInfo.setCode(IConstants.QT_PWD_NOT_MATCH);
			retInfo.setMessage("新密码与确认密码不一致。");
			return retInfo;
		}
		String cap = captchaMap.get(mobile);
		if (!StringUtils.equals(captcha, cap)){
			logger.error("captcha error.");
			retInfo.setCode(IConstants.QT_CAPTCHA_ERROR);
			retInfo.setMessage("验证码错误。");
			return retInfo;
		}
		
		boolean flag = userService.updatePayPwd(mobile, password);
		if (flag){
			captchaMap.remove(mobile);
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("找回支付成功。");
			logger.info(mobile + "find pay password successed.");
		}
		else {
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("找回支付密码失败。");
			logger.error(mobile +  "find pay password failed.");
		}
		
		logger.debug("forgetPayPassword end...");
		return retInfo;
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateUser")
	@ResponseBody
	public String updateUser(User user,HttpServletRequest request, HttpServletResponse response){
		
		
		if(userService.update(user)){
			
		}
		return "/error";
	}

	/**
	 * 删除用户
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping("delUser")
	@ResponseBody
	public String delUser(String mobile,HttpServletRequest request, HttpServletResponse response){
		
		if(userService.delete(mobile)){
			
		}
		return "";
		
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "validatePayPwd", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo validatePayPwd(String mobile, String password, HttpServletRequest request, HttpServletResponse response){
		logger.debug("validatePayPwd start...");
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
			logger.error("mobile or password can not be null.");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数不能为空。");
			return info;
		}
		if(userService.validatePayPwd(mobile, password)){
			logger.info(mobile + " validate pay password successed.");
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("支付密码校验成功。");
		} else {
			logger.error(mobile + " validate pay password failed.");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("支付密码错误。");
		}
		logger.debug("validatePayPwd end...");
		return info;
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "validateMobile", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo validateMobile(String mobile, HttpServletRequest request, HttpServletResponse response){
		logger.debug("validateMobile start...");
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile)){
			logger.error("mobile can not be null.");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数不能为空。");
			return info;
		}
		User user = userService.findByMobile(mobile);
		if (ObjUtil.notEmpty(user)){
			logger.error("mobile is already exists.");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号已被注册。");
		} else {
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("手机号没有被注册。");
		}
		logger.debug("validateMobile end...");
		return info;
	}
	
}
