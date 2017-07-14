package com.ulaiber.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.Bank;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BankService;

@Controller
@RequestMapping("/api/v1/")
public class BankController extends BaseController {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(BankController.class);
	
	@Autowired
	private BankService bankservice;
	
	@RequestMapping(value = "getBankById", method = RequestMethod.GET)
	@ResponseBody
	public Bank getBankById(String bankNo, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isEmpty(bankNo)){
			return null;
		}
		return bankservice.getBankByBankNo(bankNo);
	}
	
	@RequestMapping(value = "queryAllBanks", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo queryAllBanks(){
		
		ResultInfo retInfo = new ResultInfo();
		List<Bank> banks = bankservice.getAllBanks();
		retInfo.setCode(IConstants.QT_CODE_OK);
		retInfo.setData(banks);
		return retInfo;
	}

}
