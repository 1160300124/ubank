package com.ulaiber.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ulaiber.web.model.BankAccount;
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

    @Override
    public BankAccount getBankByCode(String code) {
        return mapper.getBankByCode(code);
    }

	@Override
	public List<Bank> queryBanksByNumber(String bankNumber) {
		List<Bank> banks =mapper.queryBanksByNumber(bankNumber);
		return banks;
	}

    @Override
    public int deleteOriginCart(String originCart, int userid) {
		Map<String,Object> map = new HashMap<>();
		map.put("originCart",originCart);
		map.put("userid",userid);
        return mapper.deleteOriginCart(map);
    }

}
