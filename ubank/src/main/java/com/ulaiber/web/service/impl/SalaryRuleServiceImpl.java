package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.SalaryRuleDao;
import com.ulaiber.web.model.salary.SalaryRule;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.SalaryRuleService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 工资规则业务实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月20日 下午5:07:15
 * @version 1.0 
 * @since 
 */
@Service
public class SalaryRuleServiceImpl extends BaseService implements SalaryRuleService {
	
	@Resource
	private SalaryRuleDao dao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(SalaryRule salaryRule) {
		salaryRule.setCreateTime(DateTimeUtil.date2Str(new Date()));
		salaryRule.setUpdateTime(DateTimeUtil.date2Str(new Date()));
		return dao.save(salaryRule) > 0;
	}

	@Override
	public List<SalaryRule> getSalaryRules(int limit, int offset, String order, String search) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put(search, search);
		return dao.getSalaryRules(params);
	}

	@Override
	public int getTotal() {
		return dao.getTotalNum();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByRids(List<Long> rids) {
		return dao.deleteByRids(rids) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateSalaryRuleByRid(SalaryRule salaryRule) {
		salaryRule.setUpdateTime(DateTimeUtil.date2Str(new Date()));
		return dao.updateSalaryRuleByRid(salaryRule) > 0;
	}

	@Override
	public boolean getCountByCompanyId(String[] companyIds) {
		return dao.getCountByCompanyId(companyIds) > 0;
	}

}
