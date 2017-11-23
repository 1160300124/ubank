package com.ulaiber.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.SalaryDetailDao;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.SalaryDetailService;

@Service
public class SalaryDetailServiceImpl extends BaseService implements SalaryDetailService {
	
	@Resource
	private SalaryDetailDao mapper;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean saveBatch(List<SalaryDetail> details) {
		return mapper.batchSave(details) > 0;
	}

	@Override
	public List<SalaryDetail> getDetailsBySid(String sid, int limit, int offset, String orderby, String search) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("orderby", orderby);
		params.put("search", search);
		return mapper.getDetailsBySid2(params);
	}
	
	@Override
	public List<SalaryDetail> getDetailsBySid(String sid) {
		return mapper.getDetailsBySid(sid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDeleteSalaryDetails(List<String> sids) {
		
		return mapper.batchDeleteSalaryDetails(sids) > 0;
	}

	@Override
	public int getTotalBySid(String sid) {
		return mapper.getTotalBySid(sid);
	}

	@Override
	public SalaryDetail getSalaryDetailByUserIdAndMonth(long userId, String month) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("salaryMonth", month);
		return mapper.getSalaryDetailByUserIdAndMonth(params);
	}

	@Override
	public List<SalaryDetail> getLatestSalaryDetail(String companyId) {
		
		return mapper.getLatestSalaryDetail(companyId);
	}


}
