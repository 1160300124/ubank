package com.ulaiber.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.SalaryDao;
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.SalaryService;

@Service
public class SalaryServiceImpl extends BaseService implements SalaryService {
	
	@Resource
	private SalaryDao mapper;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean save(Salary sa) {
		return this.mapper.save(sa) > 0;
	}

	@Override
	public List<Salary> getAllSalaries() {
		return this.mapper.getAllSalaries();
	}

	@Override
	public List<Salary> getSalaries(int limit, int offset, String search) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("search", search);
		return this.mapper.getSalaries(params);
	}

	@Override
	public int getTotalNum() {
		
		return this.mapper.getTotalNum();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateStatusBySeqNo(Salary sa) {
		
		return this.mapper.updateStatusBySeqNo(sa);
	}

	@Override
	public boolean batchDeleteSalaries(List<Long> sids) {
		
		return this.mapper.batchDeleteSalaries(sids) > 0;
	}

	
}
