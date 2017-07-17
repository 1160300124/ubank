package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;

public interface ManagerService {
	
	boolean save(Salary sa, List<SalaryDetail> details);
	
	boolean batchDelete(List<Long> sids);

}
