package com.ulaiber.web.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ulaiber.web.model.Bill;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;

/**
 * 钱包控制器
 * 
 * @author huangguoqing
 *
 */
@Controller
@RequestMapping("/api/v1/")
public class WalletController extends BaseController {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 查询余额
	 * @param mobile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getBalance", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getBalance(String mobile, HttpServletRequest request, HttpServletResponse response){
		
		ResultInfo retInfo = new ResultInfo();
		
		//TODO 调银行查询余额接口
		String money = "0.00";
		retInfo.setCode(IConstants.QT_CODE_OK);
		
		retInfo.setData(JSONObject.parse("{" + "\"balance\":\"" + money + "\"}"));
		return retInfo;
		
	}
	
	/**
	 * 查询账单
	 * @param mobile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "queryBills", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo queryBills(String mobile, HttpServletRequest request, HttpServletResponse response){
		
		ResultInfo retInfo = new ResultInfo();
		
		//TODO 调银行查询账单接口
		//模拟数据
		Bill bill = new Bill();
		bill.setBill_type("工资转入");
		bill.setBill_money("3467.00");
		bill.setBill_date(DateTimeUtil.date2Str(new Date()));
		
		
		Bill bill2 = new Bill();
		bill2.setBill_type("提现 工商银行(8796)");
		bill2.setBill_money("1267.00");
		bill2.setBill_date(DateTimeUtil.date2Str(new Date()));
		
		Bill bill3 = new Bill();
		bill3.setBill_type("工资转入");
		bill3.setBill_money("6666.00");
		bill3.setBill_date(DateTimeUtil.date2Str(new Date()));
		
		
		Bill bill4 = new Bill();
		bill4.setBill_type("提现 工商银行(8796)");
		bill4.setBill_money("5557.00");
		bill4.setBill_date(DateTimeUtil.date2Str(new Date()));
		
		
		List<Bill> bills = new ArrayList<Bill>();
		bills.add(bill);
		bills.add(bill2);
		bills.add(bill3);
		bills.add(bill4);
		
		retInfo.setCode(IConstants.QT_CODE_OK);
		retInfo.setData(bills);
		return retInfo;
		
	}
	
	/**
	 * 提现
	 * @param mobile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getMoney", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo getMoney(String mobile, HttpServletRequest request, HttpServletResponse response){
		
		ResultInfo retInfo = new ResultInfo();
		
		//TODO 调银行提现接口
		String money = "0.00";
		retInfo.setCode(IConstants.QT_CODE_OK);
		retInfo.setData(JSONObject.parse("{" + "\"money\":\"" + money + "\"}"));
		return retInfo;
		
	}
	
	/**
	 * 更改绑定银行卡信息
	 * @param user
	 * @param bank
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateForBankCard", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo updateForBankCard(User user, Bank bank, HttpServletRequest request, HttpServletResponse response){
		
		ResultInfo retInfo = new ResultInfo();
		
		//TODO 调银行更改绑定银行卡接口
		boolean flag = false;
		if (flag){
			
		}
		user.setBank(bank);
		userService.updateForBankCard(user);
		
		retInfo.setCode(IConstants.QT_CODE_OK);
		retInfo.setMessage("update successed.");
		return retInfo;
		
	}

}
