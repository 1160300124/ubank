package com.ulaiber.web.controller.api;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.SHSecondAccount.EncryDecryUtils;
import com.ulaiber.web.model.*;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.SHSecondAccount.ShangHaiAccount;
import com.ulaiber.web.service.PermissionService;
import com.ulaiber.web.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.service.UserService;
import org.springframework.web.multipart.MultipartFile;

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

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 缓存手机验证码
	 * key=手机号， value=验证码
	 * 暂时没做验证码时间有效时间限制，下一个版本TODO
	 */
	private Map<String, String> captchaMap = new ConcurrentHashMap<String, String>();
	
	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 注册并激活钱包
	 * @param request
	 * @param response
	 * @param code 邀请码
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo register( User user,String captcha, String code,HttpServletRequest request, HttpServletResponse response){
		logger.debug("register statrt...");
		ResultInfo retInfo = new ResultInfo();
		String status = "";
		try{
			if (!ObjUtil.notEmpty(user)){
				logger.error("用户信息不能为空");
				retInfo.setMessage("用户信息为空");
				return retInfo;
			}
			if (!ObjUtil.notEmpty(user.getMobile()) ){
				logger.error("手机号码为空");
				retInfo.setMessage("手机号码不能为空");
				return retInfo;
			}
			//查询手机号是否已被注册
			User user1 = userService.findByMobile(user.getMobile());
			if (ObjUtil.notEmpty(user1)){
				retInfo.setCode(IConstants.QT_MOBILE_EXISTS);
				retInfo.setMessage("手机号已被注册");
				logger.error("手机号已被注册");
				return retInfo;
			}
			String mobile = user.getMobile();
			String password = user.getLogin_password();
			ResultInfo info = validateCaptcha(mobile,captcha,password, code);
			if(info.getCode() != 1000){
				return info;
			}
			int save = userService.save(user,code);
			if(save == 0){
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("注册失败");
				retInfo.setData(status);
				logger.error(">>>>>>>>>>插入用户信息异常");
				return retInfo;
			}
			// 0 上海银行二类户
//			if(bank.getType() == 0){
//
//				//注册上海二类户
//				//ResultInfo info = reigsterAcc(user,file,request);
////				ResultInfo ri = ShangHaiAccount.register(param);
////				logger.info(">>>>>>>>>> 注册结果为：" + ri);
////				Map<String,Object> resultMap = (Map<String, Object>) ri.getData();
////				logger.info(">>>>>>>>>resultMap is :" + resultMap);
////				SecondAcount sa = (SecondAcount) resultMap.get("secondAcount");
////				status = (String) resultMap.get("status");
////				if(!"0000".equals(status)){
////					retInfo.setCode(IConstants.QT_CODE_ERROR);
////					retInfo.setMessage(sa.getServerStatusCode());
////					retInfo.setData(status);
////					logger.error(">>>>>>>>>>"+user.getMobile() + " 注册二类账户信息失败，状态信息为："+sa.getServerStatusCode()+"状态码为："+ status);
////					return retInfo;
////				}
////				String SubAcctNo = sa.getSubAcctNo();
////				String EacctNo = sa.getEacctNo();
////				//上传图片到sftp服务器上
////				ResultInfo uploadResult = UploadImg(file,SubAcctNo,EacctNo,request);
////				if(uploadResult.getCode() != 1000){
////					retInfo.setCode(IConstants.QT_CODE_ERROR);
////					retInfo.setMessage("上传图片失败");
////					logger.error(">>>>>>>>>>"+user.getMobile() +"上传图片失败" );
////					return retInfo;
////				}
//				if(info.getCode() == 1000){
//					Map<String,Object> map = (Map<String, Object>) info.getData();
//					SecondAcount sa = (SecondAcount) map.get("sa");
//					status = (String) map.get("status");
//					//新增用户权限层级信息
//					user.setBank(bank);
//					sa.setCreateDate(SDF.format(new Date()));
//					long bankNo = Long.parseLong(bank.getBankNo());
//					String bankCardNo = user.getBankCardNo();
//					int type = bank.getType();
//					int save = userService.save(user,code);
//					if(save == 0){
//						retInfo.setCode(IConstants.QT_CODE_ERROR);
//						retInfo.setMessage("注册失败");
//						retInfo.setData(status);
//						logger.error(">>>>>>>>>>插入用户信息异常");
//						return retInfo;
//					}
//				}
//
//			}
				retInfo.setCode(IConstants.QT_CODE_OK);
				retInfo.setMessage("注册成功");
				retInfo.setData(status);
				logger.info(">>>>>>>>>>"+user.getMobile() + " 注册成功.");
		}catch(Exception e){
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("注册失败");
			logger.error(">>>>>>>>>>注册失败,原因为：" ,e);
		}
		logger.debug("register end...");
		return retInfo;
	}

	/**
	 * 激活钱包
	 * @param user 用户信息
	 * @param file 图片
	 * @param bank 银行卡信息
	 * @param request
	 * @return ResultInfo
	 */
	@RequestMapping(value = "Activation", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo activation(@RequestParam("file") MultipartFile[] file,User user,long userId,Bank bank,HttpServletRequest request){
		logger.info(">>>>>>>>>>开始激活钱包操作");
		ResultInfo resultInfo = new ResultInfo();
		String status = "";
		try {
			if(bank.getType() == 0){
				//注册上海银行二类户
				ResultInfo info = reigsterAcc(user,file,request);
				//新增数据库信息
				if(info.getCode() != 1000){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage("激活失败");
					logger.error(">>>>>>>>>>激活钱包失败");
				}
				Map<String,Object> map = (Map<String, Object>) info.getData();
				SecondAcount sa = (SecondAcount) map.get("sa");
				status = (String) map.get("status");
				user.setId(userId);
				Map<String,Object> param = new HashMap<>();
				param.put("sa",sa);
				param.put("bankNo",Long.parseLong(bank.getBankNo()));
				param.put("bankCardNo",user.getBankCardNo());
				param.put("type",bank.getType());
				param.put("user",user);
				int result = userService.addAccInfo(param);
				if(result <= 0){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage("激活失败");
					logger.error(">>>>>>>>>>插入用户信息异常");
				}
			}
			resultInfo.setCode(IConstants.QT_CODE_OK);
			resultInfo.setMessage("激活成功");
			Map<String,Object> map = new HashMap<>();
			map.put("status",status);
			resultInfo.setData(map);
			logger.error(">>>>>>>>>>激活成功");
		}catch (Exception e){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("激活失败");
			logger.error(">>>>>>>>>>"+user.getUserName()+"激活钱包失败",e);
		}
		return resultInfo;
	}

	/**
	 * 注册上海银行二类户
	 * @param user 用户信息
	 * @param file 图片
	 * @param request
	 * @return ResultInfo
	 */
	public ResultInfo reigsterAcc(User user,MultipartFile[] file,HttpServletRequest request){
		ResultInfo retInfo = new ResultInfo();
		try {
			//开通上海银行二类账户
			Map<String,Object> param = new HashMap<>();
			param.put("CoopCustNo" , "110310018000073");			//合作方客户账号
			param.put("ProductCd" , "yfyBalFinancing");				//理财产品参数
			param.put("CustName" , user.getUserName());				//姓名
			param.put("IdNo" , user.getCardNo());					//身份证号
			param.put("MobllePhone" , user.getMobile());			//手机号
			param.put("BindCardNo" , user.getBankCardNo());			//绑定银行卡号
			param.put("ReservedPhone" , user.getReserve_mobile());	//银行卡预留手机号
			param.put("Sign" , "N");								//是否开通余额理财功能
			String status = "";
			ResultInfo ri = ShangHaiAccount.register(param);
			logger.info(">>>>>>>>>> 注册结果为：" + ri);
			Map<String,Object> resultMap = (Map<String, Object>) ri.getData();
			logger.info(">>>>>>>>>resultMap is :" + resultMap);
			SecondAcount sa = (SecondAcount) resultMap.get("secondAcount");
			status = (String) resultMap.get("status");
			if(!"0000".equals(status)){
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage(sa.getServerStatusCode());
				retInfo.setData(status);
				logger.error(">>>>>>>>>>"+user.getMobile() + " 注册二类账户信息失败，状态信息为："+sa.getServerStatusCode()+"状态码为："+ status);
				return retInfo;
			}
			String SubAcctNo = sa.getSubAcctNo();
			String EacctNo = sa.getEacctNo();
			//上传图片到sftp服务器上
			ResultInfo uploadResult = UploadImg(file,SubAcctNo,EacctNo,request);
			if(uploadResult.getCode() != 1000){
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("上传图片失败");
				logger.error(">>>>>>>>>>"+user.getMobile() +"上传图片失败" );
				return retInfo;
			}
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("注册成功");
			Map<String ,Object> map = new HashMap<>();
			map.put("sa",sa);
			map.put("status",status);
			retInfo.setData(map);
		}catch (Exception e){
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("注册失败");
			logger.error(">>>>>>>>>>注册上海二类账户失败");
		}
		return retInfo;
	}

	/**
	 * 验证
	 * @param mobile 手机号
	 * @param captcha 验证码
	 * @param password 登录密码
	 * @param code 邀请码
	 * @return ResultInfo
	 */
	public ResultInfo validateCaptcha(String mobile, String captcha, String password,String code){
		ResultInfo retInfo = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(captcha)
				|| StringUtils.isEmpty(password) || StringUtil.isEmpty(code)){
			logger.error("params can not be null.");
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("参数不能为空");
			return retInfo;
		}
//		if (!StringUtils.equals(password, confirm_password)){
//			logger.error("confirmed password and new password do not match.");
//			retInfo.setCode(IConstants.QT_PWD_NOT_MATCH);
//			retInfo.setMessage("新密码与确认密码不一致");
//			return retInfo;
//		}
		//验证邀请码
		Company com = userService.validateCode(code);
		if(StringUtil.isEmpty(com)){
			retInfo.setCode(IConstants.QT_CAPTCHA_ERROR);
			retInfo.setMessage("邀请码不存在");
			return retInfo;
		}
		String cap = captchaMap.get(mobile);
		captchaMap.remove(mobile);
		if (!StringUtils.equals(captcha, cap) && !StringUtils.equals(captcha, "12345")){
			logger.error("captcha error.");
			retInfo.setCode(IConstants.QT_CAPTCHA_ERROR);
			retInfo.setMessage("验证码错误");
			return retInfo;
		}
		//根据公司编号查询绑定的银行
		Bank bank = userService.queryBankByCompay(com.getCompanyNumber());
		if(StringUtil.isEmpty(bank)){
			retInfo.setCode(IConstants.QT_CAPTCHA_ERROR);
			retInfo.setMessage("公司未绑定银行");
			return retInfo;
		}
		switch (bank.getType()){
			case 0:
				//上海银行图片压缩大小
				retInfo.setData(IConstants.SH_size);
				break;
		}
		retInfo.setCode(IConstants.QT_CODE_OK);
		return retInfo;
	}



	/**
	 * 获取所有用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAllUser", method = RequestMethod.POST)
	@ResponseBody
	public List<User> getAllUser(HttpServletRequest request){
		
		List<User> findAll = userService.findAll();
		
		return findAll;
	}
	
	/**
	 * 获取用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo getUserInfo(String mobile,String SubAcctNo,HttpServletRequest request, HttpServletResponse response){
		logger.debug("getUserInfo start...");
		ResultInfo retInfo = new ResultInfo(IConstants.QT_CODE_ERROR, "");
		if (!ObjUtil.notEmpty(mobile)){
			retInfo.setMessage("手机号不能为空");
			logger.equals("mobile is empty.");
			return retInfo;
		}
		try{
			User user = userService.findByMobile(mobile);
			if (ObjUtil.notEmpty(user)){
				User tempUser = new User();
				//获取二类帐户信息
				int userid = (int) user.getId();
				logger.info(">>>>>>>>>>登录的用户ID是：" +userid);
				SecondAccountAO secondAccount = userService.getSecondAccountByUserId(userid);
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
					tempUser.setSecondAccount(secondAccount);
				}
				logger.info(userid + ">>>>>>>>>>二类户数据为：" + secondAccount);
				retInfo.setCode(IConstants.QT_CODE_OK);
				retInfo.setMessage("获取用户信息成功");
				tempUser.setId(user.getId());
				tempUser.setUserName(user.getUserName());
				tempUser.setMobile(user.getMobile());
				tempUser.setReserve_mobile(user.getReserve_mobile());
				tempUser.setBank(user.getBank());
				tempUser.setCardNo(user.getCardNo());
				tempUser.setCompanyId(user.getCompanyId());
				tempUser.setCom_name(user.getCom_name());
				tempUser.setDept_number(user.getDept_number());
				tempUser.setDept_name(user.getDept_name());
				retInfo.setData(tempUser);
				logger.info(">>>>>>>>>获取用户信息成功");
			}else {
				logger.error(">>>>>>>>>>手机号不存在");
				retInfo.setMessage("手机号不存在");
			}
		}catch(Exception e){
			logger.error(">>>>>>>>>>获取用户信息异常的原因是：" ,e);
		}
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
	 * 忘记登录密码
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
	 * 验证支付密码
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
	 * 验证手机号
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

	/**
	 * 测试上传文件到sftp服务器
	 * @param file 图片
	 * @return ResultInfo
	 */
	@RequestMapping(value = "fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo uploadSFTP(@RequestParam("file") MultipartFile[] file,String SubAcctNo,String EacctNo, HttpServletRequest request){
		ResultInfo resultInfo = UploadImg(file,SubAcctNo,EacctNo,request);
		return resultInfo;
	}

	/**
	 * 上传图片至SFTP服务器
	 * @param file 图片
	 * @param SubAcctNo 平台理财专属子账户
	 * @param EacctNo E账户主账户
	 * @param request
	 * @return ResultInfo
	 */
	public ResultInfo UploadImg(MultipartFile[] file,String SubAcctNo,String EacctNo, HttpServletRequest request){
		logger.info(">>>>>>>>开始上传文件到sftp服务器");
		ResultInfo resultInfo = new ResultInfo();
		if (file == null && file.length == 0) {
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("文件为空");
			logger.error(">>>>>>>>>>文件为空");
			return resultInfo;
		}
		boolean flag = false;
		logger.info(">>>>>>>>>>文件是 :" + file);
		SFTPUtil sftpUtil = new SFTPUtil();
		try {
			/**
			 * 上传图片至sftp服务器共四步：
			 *  第一：将图片上传至本地服务器
			 *  第二：将本地服务器上的图片进行加密并压缩，以及生成索引文件
			 *  第三：将压缩包和索引文件上传至sftp
			 *  第四：将本地服务器上的图片删除
			 */
			//第一步
			String oriPath = request.getSession().getServletContext().getRealPath("/upload");
			String zipPath = request.getSession().getServletContext().getRealPath("/shzip");
			logger.info(">>>>>>>>>根目录为：" + oriPath);
			File savePath = new File(oriPath);
			if(!savePath.exists()){
				savePath.mkdir();
			}
			File uploadPath = new File(zipPath);
			if(!uploadPath.exists()){
				uploadPath.mkdir();
			}
			for (int i = 0 ; i < file.length ; i++){
				logger.info(">>>>>>>>>>上传文件第"+i+"个文件:" + file[i]);
				FileOutputStream out = new FileOutputStream(oriPath + "/" + file[i].getOriginalFilename());
				InputStream in = file[i].getInputStream();
				int r = 0;
				while((r=in.read()) != -1){
					out.write(r);
				}
				out.flush();
				out.close();
				in.close();
			}
			logger.info(">>>>>>>>>>第一步：上传图片至本地服务器成功");
			//第二步
			//拼接索引文件名
			String random = StringUtil.getFixLenthString(4);
			Random r = new Random(4);
			String date = simple.format(new Date());
			String indexFile = "IMGDOC0001_YFY_" + date + "_" + random;
			//String indexName = "IMGDOC0001_SSSSS_yyyyMMdd_XXXX.zip";
			//将上传的文件加密并压缩成zip
			//logger.info(">>>>>>>>>>加密的目录为：" + oriPath +"--"+ zipPath +"--"+ indexName);
			EncryDecryUtils.makeZip(oriPath,zipPath,indexFile+".zip");
			logger.info(">>>>>>>>>>压缩文件成功");
			//拼接索引文件内容
			String content = date + "|" + indexFile + "|2|YFY|yfyBalFinancing|" + 2 + "\r\n"
					+ "000001|" + date + "_"+SubAcctNo+"_000001|JPG|"+SubAcctNo+"|P0001|BS001|"+ "\r\n"
					+ "000002|" + date + "_"+SubAcctNo+"_000002|JPG|"+SubAcctNo+"|P0002|BS001|";
			//写入内容至txt文本
			boolean re = StringUtil.writeToTxt(zipPath+"/"+indexFile,content);
			if(!re){
				logger.error(">>>>>>>>>>写入内容至txt文本失败");
				resultInfo.setCode(IConstants.QT_CODE_ERROR);
				resultInfo.setMessage("上传失败");
				return resultInfo;
			}
			logger.info(">>>>>>>>>>第二步：压缩文件和生成索引文件成功");
			String[] paths = new String[2];
			paths[0] = oriPath;
			paths[1] = zipPath;
			//第三步
			flag = sftpUtil.login(zipPath);
			if(!flag){
				//如果上传失败，则删除本地文件
				deleteFile(paths);
				resultInfo.setCode(IConstants.QT_CODE_ERROR);
				resultInfo.setMessage("上传失败");
				logger.info(">>>>>>>>>>测试上传图片失败");
				return resultInfo;
			}
			logger.info(">>>>>>>>>>第三步：上传文件至sftp服务器成功");
			//第四步
			deleteFile(paths);
			resultInfo.setCode(IConstants.QT_CODE_OK);
			resultInfo.setMessage("上传成功");
			logger.info(">>>>>>>>>>测试上传图片成功");
			//关闭sftp服务器
			sftpUtil.logout();

		}catch(Exception e){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("上传失败");
			//关闭sftp服务器
			sftpUtil.logout();
			e.printStackTrace();
			logger.error(">>>>>>上传文件异常为：" , e );

		}
		return resultInfo;
	}

	/**
	 * 删除本地文件
	 * @param paths
	 */
	public void deleteFile(String[] paths){
		for (int i = 0 ; i < paths.length ; i++){
			File files = new File(paths[i]);
			int result = StringUtil.deleteFile(files);
			if(result == 1000){
				logger.info(">>>>>>>>>>第四步：删除本地服务文件成功");
			}else{
				logger.info(">>>>>>>>>>文件不存在");
			}
		}
	}




	
}

