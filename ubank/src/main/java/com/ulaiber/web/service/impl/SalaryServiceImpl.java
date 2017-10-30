package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.SalaryDao;
import com.ulaiber.web.dao.SalaryDetailDao;
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.SalaryService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 工资业务实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月20日 下午5:07:15
 * @version 1.0 
 * @since 
 */
@Service
public class SalaryServiceImpl extends BaseService implements SalaryService {
	
	@Resource
	private SalaryDao mapper;
	
	@Resource
	private SalaryDetailDao detailDao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Salary salary) {
		salary.setSalaryCreateTime(DateTimeUtil.date2Str(new Date()));
		salary.setStatus("0");
		if (this.mapper.save(salary) > 0){
			long sid = salary.getSid();
			for (SalaryDetail detail : salary.getDetails()){
				detail.setSid(sid);
			}
			return detailDao.saveBatch(salary.getDetails()) > 0;
		}
		
		return true;
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
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateStatusBySeqNo(Salary sa) {
		
		return this.mapper.updateStatusBySeqNo(sa) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDeleteSalaries(List<Long> sids) {
		
		return this.mapper.batchDeleteSalaries(sids) > 0;
	}

	@Override
	public List<Map<String, Object>> getSalariesByUserId(long userId, int pageSize, int pageNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		int offset = pageSize * (pageNum -1);
		params.put("limit", pageSize);
		params.put("offset", offset);
		params.put("userId", userId);
		List<Map<String, Object>> list = this.mapper.getSalariesByUserId(params);
		
//		Set<String> yearSet = new HashSet<String>();
//		for (Map<String, Object> map : list) {
//			for (Entry<String, Object> entry : map.entrySet()) {
//				if (StringUtils.equals(entry.getKey(), "salary_year")){
//					yearSet.add(entry.getValue().toString());
//				}
//			}
//		}
//		List<String> years = new ArrayList<>(yearSet);
//		Collections.reverse(years);
		
//		Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
//		for (String year : years){
//			Map<String, Object> yearData = new HashMap<String, Object>();
//			for (Map<String, Object> map : list){
//				if (StringUtils.equals(year, map.get("salary_year").toString())){
//					yearData.put(map.get("salary_month").toString(), map.get("salaries"));
//				}
//			}
//			data.put(year, yearData);
//		}
		
		return list;
	}

	
}
