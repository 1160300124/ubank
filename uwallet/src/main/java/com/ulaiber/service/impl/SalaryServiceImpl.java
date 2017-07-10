package com.ulaiber.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.dao.SalaryDao;
import com.ulaiber.model.Salary;
import com.ulaiber.service.BaseService;
import com.ulaiber.service.SalaryService;

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

}
