package com.ulaiber.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.UserDao;
import com.ulaiber.web.model.*;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.model.ShangHaiAcount.Withdraw;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ulaiber.web.dao.BankDao;
import com.ulaiber.web.service.BankService;
import com.ulaiber.web.service.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl extends BaseService implements BankService {

	private static Logger logger = Logger.getLogger(BankServiceImpl.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Resource
	private BankDao mapper;

	@Resource
	private UserDao userDao;

	@Resource
	private LeaveAuditDao leaveAuditDao;
	
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
    public int deleteOriginCart(Map<String, Object> map) {
		Map<String,Object> Map = new HashMap<>();
		Map.put("originCart",map.get("originCart"));
		Map.put("userid",map.get("userid"));
		Map.put("bankNo",map.get("bankNo"));
		Map.put("type",map.get("type"));
		Map.put("bankCardNo",map.get("newCardNo"));
		Map.put("newReserveMobile",map.get("newReserveMobile"));
		logger.info(">>>>>>>>>新银行卡为:" + map.get("newCardNo"));
		int result = 0;
		if(map.get("modiType").equals("00")){   //改卡
			//删除原绑定银行卡
			result = mapper.deleteOriginCart(Map);
			if(result == 0){
				logger.error(">>>>>>>>>>删除原绑定银行卡失败");
				return result;
			}
			logger.info(">>>>>>>>>>删除原绑定银行卡操作成功，开始新增改绑银行卡信息");
			//新增改绑后的银行卡信息
			 result = userDao.insertUserToBank(Map);
			if(result == 0){
				logger.error(">>>>>>>>>>新增改绑后的银行卡信息失败");
			}
			//修改用户预留手机号
			result = userDao.modifyReserveMobile(Map);
			if(result == 0){
				logger.error(">>>>>>>>>>修改用户预留手机号失败");
			}
			return result;
		}else { //改电话
			//根据用户ID修改银行卡预留电话
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("userid",map.get("userid"));
			paramMap.put("newReserveMobile",map.get("newReserveMobile"));
			result = userDao.modifyReserveMobile(paramMap);
			if(result == 0){
				logger.error(">>>>>>>>>>修改银行卡电话号码失败");
			}
			return result;
		}

    }

    @Override
    public SecondAcount querySecondAccount(String SubAcctNo) {
		Map<String,Object> map = new HashMap<>();
		map.put("SubAcctNo",SubAcctNo);
		SecondAcount sa = mapper.querySecondAccount(map);
        return sa;
    }

    @Override
    public String getCodeByuserid(long userid) {
        return mapper.getCodeByuserid(userid);
    }

    @Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int updateSecondAcc(SecondAcount sa ) {
        return mapper.updateSecondAcc(sa);
    }

    @Override
    public SecondAcount queryAccount(String subAcctNo) {
        return mapper.queryAccount(subAcctNo);
    }

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public int insertWithdraw(Withdraw withd) {
		return mapper.insertWithdraw(withd);
	}

    @Override
    public List<Bill> queryWithdraw(Map<String, Object> map) {
        return mapper.queryWithdraw(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int updateWithdraw(String OrirqUID, int tStatus, String date) {
	    Map<String,Object> map = new HashMap<>();
	    map.put("rqUID",OrirqUID);
	    map.put("status",tStatus);
	    map.put("date",date);
	    map.put("sortTime",sdf.format(new Date()));
        return mapper.updateWithdraw(map);
    }

	@Override
	public Map<String, Object> queryCIDByUserid(int userId) {
		return leaveAuditDao.queryCIDByUserid(userId);
	}

	@Override
	public BillDetail queryWithdrawByRqUID(String rqUID) {
		return mapper.queryWithdrawByRqUID(rqUID);
	}

	@Override
	public BillDetail querySalariesByRqUID(String rqUID) {
		return mapper.querySalariesByRqUID(rqUID);
	}

    @Override
    public Map<String, Object> queryCIdbySub(String subAcctNo) {
        return mapper.queryCIdbySub(subAcctNo);
    }

    @Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int updateAccFreeze(String subAcctNo, String status) {
		Map<String,Object> map = new HashMap<>();
		map.put("subAcctNo",subAcctNo);
		map.put("status",status);
        return mapper.updateAccFreeze(map);
    }

    @Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertTransfer(Transfer tran) {
        return mapper.insertTransfer(tran);
    }


}
