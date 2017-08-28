package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.Holiday;
import com.ulaiber.web.service.AttendanceRuleService;
import com.ulaiber.web.service.BaseService;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月16日 下午12:32:21
 * @version 1.0 
 * @since 
 */
@Service
public class AttendanceRuleServiceImpl extends BaseService implements AttendanceRuleService {
	
	@Resource
	private AttendanceRuleDao dao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(AttendanceRule rule, String ids) {
		if (dao.save(rule) > 0){
			List<String> idList = Arrays.asList(ids.split(","));
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (String id : idList){
				Map<String ,Object> map = new HashMap<String, Object>();
				map.put("userId", id);
				map.put("rid", rule.getRid());
				map.put("deptId", 0);
				map.put("companyId", 0);
				list.add(map);
			}
			if (dao.batchInsertUserOfRule(list) > 0){
				return true;
			}
		}

		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(AttendanceRule rule) {

		return dao.update(rule);
	}

	@Override
	public AttendanceRule getRuleByUserId(long userId) {
		
		return dao.getRuleByUserId(userId);
	}

	@Override
	public AttendanceRule getRuleByMobile(String mobile) {
		
		return dao.getRuleByMobile(mobile);
	}

	@Override
	public List<AttendanceRule> getRules(int offset, int limit, String orderby) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("limit", limit);
		params.put("orderby", orderby);
		return dao.getRules(params);
	}

	@Override
	public int getCount() {
		
		return dao.getCount();
	}
	
	@Override
	public Holiday getHolidaysByYear(String year){

		return dao.getHolidaysByYear(year);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteRulesByRids(List<Long> rids) {
		if (dao.deleteRulesByRids(rids) > 0){
			return dao.deleteUserOfRulesByRids(rids) > 0;
		}
		return false;
	}

}
