package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.model.Holiday;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.model.attendance.UserOfRule;
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
	public boolean save(AttendanceRule rule, String ruleData, String noRuleData) {
		if (dao.save(rule) > 0){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			//不参与考勤的用户集合,如参与考勤的用户id也在此集合内，过滤掉
			List<String> ids = new ArrayList<String>();
			//不参与考勤规则的用户
			if (StringUtils.isNotEmpty(noRuleData)){
				String[] companyId_deptId_userIds = noRuleData.split("\\|");
				for (String companyId_deptId_userId : companyId_deptId_userIds) {
					String companyId = companyId_deptId_userId.split("=")[0];
					String[] deptId_userIds = companyId_deptId_userId.split("=")[1].split("-");
					for (String deptId_userId : deptId_userIds){
						String deptId = deptId_userId.split("_")[0];
						String[] userIds = deptId_userId.split("_")[1].split(",");
						for (String id : userIds){
							ids.add(id);
							Map<String ,Object> map = new HashMap<String, Object>();
							map.put("userId", id);
							map.put("rid", rule.getRid());
							map.put("deptId", deptId);
							map.put("companyId", companyId);
							map.put("type", 1);
							list.add(map);
						}
					}

				}
			}
			
			//参与考勤规则的用户
			String[] companyId_deptId_userIds = ruleData.split("\\|");
			for (String companyId_deptId_userId : companyId_deptId_userIds) {
				String companyId = companyId_deptId_userId.split("=")[0];
				String[] deptId_userIds = companyId_deptId_userId.split("=")[1].split("-");
				for (String deptId_userId : deptId_userIds){
					String deptId = deptId_userId.split("_")[0];
					String[] userIds = deptId_userId.split("_")[1].split(",");
					for (String id : userIds){
						//默认不参与考勤
						int type = 0;
						//不需要考勤的用户
						if (ids.contains(id)){
							type = 2;
							for (Map<String, Object> map : list){
								//从list删除不参与考勤用户，重新添加type=2的用户
								if (StringUtils.equals(map.get("userId").toString(), id)){
									list.remove(map);
									break;
								}
							}
						}
						Map<String ,Object> map = new HashMap<String, Object>();
						map.put("userId", id);
						map.put("rid", rule.getRid());
						map.put("deptId", deptId);
						map.put("companyId", companyId);
						map.put("type", type);
						list.add(map);
					}
				}

			}
			
			return dao.batchInsertUserOfRule(list) > 0;
		}

		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(AttendanceRule rule, String ruleData, String noRuleData) {
		if (dao.update(rule)){
			List<Long> rids = new ArrayList<Long>();
			rids.add(rule.getRid());
			//先删除所有与rid绑定的userId,在批量插入
			dao.deleteUserOfRulesByRids(rids);
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			//不需要考勤的用户集合,如需要考勤的用户id也在此集合内，过滤掉
			List<String> ids = new ArrayList<String>();
			//不需要考勤规则的用户
			if (StringUtils.isNotEmpty(noRuleData)){
				String[] companyId_deptId_userIds = noRuleData.split("\\|");
				for (String companyId_deptId_userId : companyId_deptId_userIds) {
					String companyId = companyId_deptId_userId.split("=")[0];
					String[] deptId_userIds = companyId_deptId_userId.split("=")[1].split("-");
					for (String deptId_userId : deptId_userIds){
						String deptId = deptId_userId.split("_")[0];
						String[] userIds = deptId_userId.split("_")[1].split(",");
						for (String id : userIds){
							ids.add(id);
							Map<String ,Object> map = new HashMap<String, Object>();
							map.put("userId", id);
							map.put("rid", rule.getRid());
							map.put("deptId", deptId);
							map.put("companyId", companyId);
							map.put("type", 1);
							list.add(map);
						}
					}

				}
			}
			
			//需要考勤规则的用户
			String[] companyId_deptId_userIds = ruleData.split("\\|");
			for (String companyId_deptId_userId : companyId_deptId_userIds) {
				String companyId = companyId_deptId_userId.split("=")[0];
				String[] deptId_userIds = companyId_deptId_userId.split("=")[1].split("-");
				for (String deptId_userId : deptId_userIds){
					String deptId = deptId_userId.split("_")[0];
					String[] userIds = deptId_userId.split("_")[1].split(",");
					for (String id : userIds){
						//默认不参与考勤
						int type = 0;
						//不需要考勤的用户
						if (ids.contains(id)){
							type = 2;
							for (Map<String, Object> map : list){
								//从list删除不参与考勤用户，重新添加type=2的用户
								if (StringUtils.equals(map.get("userId").toString(), id)){
									list.remove(map);
									break;
								}
							}
						}
						Map<String ,Object> map = new HashMap<String, Object>();
						map.put("userId", id);
						map.put("rid", rule.getRid());
						map.put("deptId", deptId);
						map.put("companyId", companyId);
						map.put("type", type);
						list.add(map);
					}
				}

			}
			
			return dao.batchInsertUserOfRule(list) > 0;
		}

		return false;
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

	@Override
	public List<UserOfRule> getUserIdsByRid(Long rid) {

		return dao.getUserIdsByRid(rid);
	}

	@Override
	public List<UserOfRule> getUserIdsByComId(int companyId) {

		return dao.getUserIdsByComId(companyId);
	}

	@Override
	public List<AttendanceRule> getRulesByCompanyId(int companyId) {
		
		return dao.getRulesByCompanyId(companyId);
	}

}
