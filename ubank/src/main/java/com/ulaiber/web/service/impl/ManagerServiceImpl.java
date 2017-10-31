package com.ulaiber.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.SalaryDao;
import com.ulaiber.web.dao.SalaryDetailDao;
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.ManagerService;

@Service
public class ManagerServiceImpl extends BaseService implements ManagerService {

	@Resource
	private SalaryDao salaryDao;
	
	@Resource
	private SalaryDetailDao detailDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean save(Salary sa, List<SalaryDetail> details) {
		if (salaryDao.save(sa) > 0){
			long sid = sa.getSid();
			for (SalaryDetail detail : details){
				detail.setSid(sid);
			}
			return detailDao.batchSave(details) > 0;
		}
		
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean batchDelete(List<Long> sids) {
		if (salaryDao.batchDeleteSalaries(sids) > 0){
			
			return detailDao.batchDeleteSalaryDetails(sids) > 0;
		}
		return false;
	}

}
