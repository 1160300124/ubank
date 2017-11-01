package com.ulaiber.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ulaiber.web.dao.UserDao;
import com.ulaiber.web.model.BankAccount;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import org.springframework.stereotype.Service;

import com.ulaiber.web.dao.BankDao;
import com.ulaiber.web.model.Bank;
import com.ulaiber.web.service.BankService;
import com.ulaiber.web.service.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl extends BaseService implements BankService {

	@Resource
	private BankDao mapper;

	@Resource
	private UserDao userDao;
	
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
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteOriginCart(String originCart, long userid, long bankNo, String newCardNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("originCart",originCart);
		map.put("userid",userid);
		map.put("bankNo",bankNo);
		map.put("bankCardNo",newCardNo);
		//删除原绑定银行卡
		int result = mapper.deleteOriginCart(map);
		if(result == 0){
			return result;
		}
		//新增改绑后的银行卡信息
		int result2 = userDao.insertUserToBank(map);
        return result2;
    }

    @Override
    public SecondAcount querySecondAccount(String SubAcctNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("SubAcctNo",SubAcctNo);
		SecondAcount sa = mapper.querySecondAccount(map);
        return sa;
    }

}
