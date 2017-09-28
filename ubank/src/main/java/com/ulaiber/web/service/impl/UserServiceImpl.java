package com.ulaiber.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ulaiber.web.dao.BanksRootDao;
import com.ulaiber.web.model.BankUsers;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.ulaiber.web.dao.UserDao;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.model.Message;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.HttpsUtil;
import com.ulaiber.web.utils.MD5Util;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.StringUtil;

@Service
public class UserServiceImpl extends BaseService implements UserService {
	
	@Resource
	private UserDao mapper;

	@Resource
	private BanksRootDao banksRootDao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String mobile) {
		
		return mapper.delete(mobile);
	}

	@Override
	public List<User> findAll() {
		List<User> findAllList = mapper.findAll();
		return findAllList;
	}

	@Override
	public User findByMobile(String mobile) {

		User user = mapper.getUserByMobile(mobile);
		
		return user;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(User user) {
	
		user.setRole_id(2);
		user.setCreateTime(DateTimeUtil.date2Str(new Date()));
		user.setLogin_password(MD5Util.getEncryptedPwd(user.getLogin_password()));
		user.setPay_password(MD5Util.getEncryptedPwd(user.getPay_password()));

		return mapper.save(user) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(User user) {

		user.setLoginTime(DateTimeUtil.date2Str(new Date()));
		return mapper.update(user);
	}

	@Override
	public User getUserByTicketAndToken(String ticket, String token) {
		User user = new User();
		user.setLogin_ticket(ticket);
		user.setAccess_token(token);
		return mapper.getUserByTicketAndToken(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateLoginPwd(String mobile, String password) {
		
		User user = new User();
		user.setMobile(mobile);
		user.setLogin_password(MD5Util.getEncryptedPwd(password));
		return mapper.updateLoginPwd(user);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updatePayPwd(String mobile, String password) {
		
		User user = new User();
		user.setMobile(mobile);
		user.setPay_password(MD5Util.getEncryptedPwd(password));
		return mapper.updatePayPwd(user);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateForBankCard(User user) {
		
		return mapper.updateForBankCard(user);
	}

	@Override
	public Message sendInfo(User user) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<Map<String, Object>> o_List = new ArrayList<>();
		List<Map<String, Object>> t_List = new ArrayList<>();
		List<Map<String, Object>> th_head_List = new ArrayList<>();
		List<Map<String, Object>> th_body_List = new ArrayList<>();
		//拼接json数据
		Map<String, Object> o_map = new HashMap<>();
		Map<String, Object> t_map = new HashMap<>();
		Map<String, Object> th_head_map = new HashMap<>();
		Map<String, Object> th_body_map = new HashMap<>();
		Map<String, Object> t_signature = new HashMap<>();
		Map<String, Object> head_info = new HashMap<>();
		Map<String, Object> body_field = new HashMap<>();
		//第一层
		o_map.put("IASPDB", o_List);
		resultList.add(o_map);
		//第二层
		t_map.put("PLAIN", t_List);
		t_signature.put("SIGNATURE", "");
		o_List.add(t_map);
		o_List.add(t_signature);
		//第三层
		th_head_map.put("HEAD", th_head_List);
		th_body_map.put("BODY", th_body_List);
		t_List.add(th_head_map);
		t_List.add(th_body_map);
		//第四层
		head_info.put("version", "1.0");    //协议版本号
		head_info.put("orgId", "0000011111");    //机构号
		head_info.put("subOrgId", "0002222");    //子机构号
		head_info.put("channel", "345688");    //接入机构渠道号
		String jnlno = head_info.get("subOrgId").toString() + head_info.get("channel").toString() + sdf.format(new Date()) + "90877";
		head_info.put("jnlNo", jnlno); //接入子机构号+接入机构渠道号+MMDD（日期）hhmmss（时分秒）+5位流水序号”不超过20位
		head_info.put("transId", "开户"); //交易名称
		head_info.put("timeStamp", System.currentTimeMillis());    //时间戳
		head_info.put("certVersion", "1.0");    //证书版本号
		th_head_List.add(head_info);
		body_field.put("idNo", "42212919931021053X");    //证件号
		body_field.put("idType", "1");        //证件类型 1-身份证
		body_field.put("idName", "张三");    //用户姓名
		body_field.put("acctNo", "622134578906776444");        //绑卡卡号
		body_field.put("bankName", "上海浦发银行");        //银行名称
		body_field.put("bankId", "2222244");        //行号
		body_field.put("signMobileNo", "13165601258");    //手机号码
		body_field.put("artificialNum", "");    //推荐人工号
		body_field.put("target", "345644556555");    //目标交易代码
		body_field.put("notifyUrl", "http://10.17.2.93:8080/api/v1/returnUrl");    //异步通知地址
		th_body_List.add(body_field);
		//将list转换成json
		String json = JSON.toJSONString(resultList);
		//加密。将 PLAIN 中的数据加密，并封装到SIGNATUREH中。
		//o_List.get(0).get("PLAIN");

		//发送请求
		String apiUrl = "https://10.17.2.93:8080/sendRegister";
		String response = HttpsUtil.doPost(apiUrl, json);

		return StringUtil.parserJson(response);

	}

	@Override
	public User getUserByName(String uesrName) {

		return mapper.getUserByName(uesrName);
	}

	@Override
	public List<Menu> getAllMenuByUser(String userName,String sysflag) {
		Map<String,Object> map = new HashMap<>();
		map.put("userName",userName);
		map.put("sysflag",sysflag);
		return mapper.getAllMenuByUser(map);
	}

	@Override
	public List<Menu> getAllMenu(String roleid,String sysflag) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleid" , roleid);
		map.put("sysflag" , sysflag);
		return mapper.getAllMenu(map);
	}

	@Override
	public boolean validatePayPwd(String mobile, String password) {
		User user = mapper.getUserByMobile(mobile);
		if (ObjUtil.notEmpty(user)){
			String pwd2MD5 = MD5Util.getEncryptedPwd(password);
			if (StringUtils.equals(user.getPay_password(), pwd2MD5)){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<User> getUsersByComNum(String comNum, String search) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comNum", comNum);
		params.put("search", search);
		return mapper.getUsersByComNum(params);
	}

	@Override
	public BankUsers bankUserLogin(String mobile) {
		return banksRootDao.bankUserLogin(mobile);
	}
}
