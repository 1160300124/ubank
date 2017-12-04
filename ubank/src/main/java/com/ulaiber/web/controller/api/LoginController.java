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
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.SecondAccountAO;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.StringUtil;
import com.ulaiber.web.utils.UUIDGenerator;

import java.util.Map;

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
		try{
			if (!ObjUtil.notEmpty(user.getMobile()) || !ObjUtil.notEmpty(user.getLogin_password())){
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("账号或密码不能为空。");
				logger.error("mobile or password is empty.");
				return retInfo;
			}
			User dbuser = userService.findByMobile(user.getMobile());
			if (!ObjUtil.notEmpty(dbuser)){
				retInfo.setCode(IConstants.QT_MOBILE_NOT_EXISTS);
				retInfo.setMessage("该账号不存在。");
				logger.error("mobile not exists.");
				return retInfo;
			}

			if (!MD5Util.validatePwd(user.getLogin_password(), dbuser.getLogin_password())){
				retInfo.setCode(IConstants.QT_NAME_OR_PWD_OEEOR);
				retInfo.setMessage("请输入正确密码。");
				logger.error("mobile or password error.");
				return retInfo;
			}

			String login_ticket = UUIDGenerator.getUUID();
			String access_token = UUIDGenerator.getUUID();
			user.setLogin_ticket(login_ticket);
			user.setAccess_token(access_token);
			boolean flag = userService.update(user);
			if (flag){
				//获取二类帐户信息
				String mobile =  user.getMobile();
				logger.info(">>>>>>>>>>登录的用户ID是：" +mobile);
				//SecondAccountAO secondAccount = userService.getSecondAccountByUserId(userid);
				SecondAccountAO secondAccount = userService.getSecondAccountByMobile(mobile);
				logger.info(mobile + ">>>>>>>>>>二类户secondAccount数据为：" + secondAccount);
				User tempUser = new User();
				if(!StringUtil.isEmpty(secondAccount)){
					String type = secondAccount.getType(); //银行类型
					int size = 0;
					switch (type){
						case "0":
							//上海银行图片压缩大小
							size = IConstants.SH_size;
							break;
					}
					secondAccount.setSize(size);
					tempUser.setBankCardNo(secondAccount.getBankCardNo());
//					tempUser.setBankNo(String.valueOf(secondAccount.getBankNo()));
//					tempUser.setBankName(secondAccount.getBankName());
					tempUser.setSecondAccount(secondAccount);
				}
				//拼接图片地址： 域名 + 图片名称
				Map<String,Object> configMap = StringUtil.loadConfig();
				String url = (String) configMap.get("QinNiuYun");
				if(StringUtil.isEmpty(dbuser.getPay_password())){
					tempUser.setPay_password("false");
				}else{
					tempUser.setPay_password("true");
				}
				tempUser.setImage(url + dbuser.getImage());
				tempUser.setId(dbuser.getId());
				tempUser.setUserName(dbuser.getUserName());
				tempUser.setMobile(dbuser.getMobile());
				tempUser.setLogin_ticket(login_ticket);
				tempUser.setAccess_token(access_token);
				tempUser.setReserve_mobile(dbuser.getReserve_mobile());
				tempUser.setCardNo(dbuser.getCardNo());
				tempUser.setBank(dbuser.getBank());
				tempUser.setCompanyId(dbuser.getCompanyId());
				tempUser.setCom_name(dbuser.getCom_name());
				tempUser.setDept_number(dbuser.getDept_number());
				tempUser.setDept_name(dbuser.getDept_name());
				retInfo.setData(tempUser);
				retInfo.setCode(IConstants.QT_CODE_OK);
				retInfo.setMessage("登录成功");
				logger.info(dbuser.getUserName() + " login successed.");
			} else {
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("登录失败");
				logger.error(dbuser.getUserName() + " login failed.");
			}
		}catch(Exception e){
			logger.error(">>>>>>>>>>登录异常",e);
		}
		return retInfo;
	}

}
