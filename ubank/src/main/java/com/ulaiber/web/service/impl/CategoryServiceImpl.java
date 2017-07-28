package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.CategoryDao;
import com.ulaiber.web.model.Category;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.CategoryService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * APP端页面的业务逻辑实现类
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午6:09:22
 * @version 1.0 
 * @since 
 */
@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {
	
	@Resource
	private CategoryDao dao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Category category) {
		category.setCreateTime(DateTimeUtil.date2Str(new Date()));
		return dao.save(category) > 0;
	}

	@Override
	public List<Category> getAllCategories(int limit, int offset, String order, String search) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("search", search);
		
		return dao.getAllCategories(params);
	}

	@Override
	public List<Category> getAllCategoriesforApi() {

		return dao.getAllCategoriesforApi();
	}

	@Override
	public int getTotalNum() {
		
		return dao.getTotalNum();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateById(Category category) {
		category.setUpdateTime(DateTimeUtil.date2Str(new Date()));
		return dao.updateById(category) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByIds(List<Integer> cids) {

		return dao.deleteByIds(cids) > 0;
	}

	@Override
	public Category getCategoryByCid(int cid) {
		
		return dao.getCategoryByCid(cid);
	}

}
