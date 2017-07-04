package com.ulaiber.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ulaiber.dao.BankDao;
import com.ulaiber.model.Bank;
import com.ulaiber.service.BankService;

@Service
public class BankServiceImpl implements BankService {

	@Resource
	private BankDao mapper;
	
	public Bank getBankByBankNo(String bankNo) {
		
		return mapper.getBankByBankNo(bankNo);
	}

	public List<Bank> getAllBanks() {
		
		return mapper.getAllBanks();
	}

}
