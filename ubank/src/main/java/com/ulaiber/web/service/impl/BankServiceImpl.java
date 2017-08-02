package com.ulaiber.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ulaiber.web.dao.BankDao;
import com.ulaiber.web.model.Bank;
import com.ulaiber.web.service.BankService;
import com.ulaiber.web.service.BaseService;

@Service
public class BankServiceImpl extends BaseService implements BankService {

	@Resource
	private BankDao mapper;
	
	@Override
	public Bank getBankByBankNo(String bankNo) {
		
		return mapper.getBankByBankNo(bankNo);
	}

	@Override
	public List<Bank> getAllBanks() {
		
		return mapper.getAllBanks();
	}

}
