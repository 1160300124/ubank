package com.ulaiber.service;

import java.util.List;

import com.ulaiber.model.Salary;
import com.ulaiber.model.SalaryDetail;

public interface ManagerService {
	
	boolean save(Salary sa, List<SalaryDetail> details);

}
