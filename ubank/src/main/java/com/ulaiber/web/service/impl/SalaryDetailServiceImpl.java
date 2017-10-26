package com.ulaiber.web.service.impl;

import java.util.List;

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
		return mapper.saveBatch(details) > 0;
	}

	@Override
	public List<SalaryDetail> getDetailsBySid(long sid) {
		return mapper.getDetailsBySid(sid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDeleteSalaryDetails(List<Long> sids) {
		
		return mapper.batchDeleteSalaryDetails(sids) > 0;
	}

	@Override
	public int getTotalBySid(long sid) {
		return mapper.getTotalBySid(sid);
	}


}
