package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.ModuleDao;
import com.ulaiber.web.model.Module;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.ModuleService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * APP端模块的业务逻辑实现类
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午6:10:25
 * @version 1.0 
 * @since 
 */
@Service
public class ModueServiceImpl extends BaseService implements ModuleService {
	
	@Resource
	private ModuleDao dao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Module module) {
		module.setCreateTime(DateTimeUtil.date2Str(new Date()));
		return this.dao.save(module) > 0;
	}

	@Override
	public List<Module> getAllModules(int limit, int offset, String order, String search) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("search", search);
		
		return this.dao.getAllModules(params);
	}

	@Override
	public List<Module> getAllModulesforApi() {
		
		return this.dao.getAllModulesforApi();
	}

	@Override
	public int getTotalNum() {
		
		return dao.getTotalNum();
	}

	@Override
	public Module getModuleByMid(int mid) {
		return dao.getModuleByMid(mid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByIds(List<Integer> mids) {
		return dao.deleteByIds(mids) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateById(Module module) {
		module.setUpdateTime(DateTimeUtil.date2Str(new Date()));
		return dao.updateById(module) > 0;
	}

}
