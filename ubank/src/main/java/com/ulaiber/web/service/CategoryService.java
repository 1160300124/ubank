package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Category;

/** 
 * APP端页面的业务逻辑接口
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午6:06:41
 * @version 1.0 
 * @since 
 */
public interface CategoryService {
	
	/**
	 * 新增
	 * 
	 * @param category
	 * @return
	 */
	boolean save(Category category);
	
	/**
	 * 查询所有模块带分页
	 * @return
	 */
	List<Category> getAllCategories(int limit, int offset, String order, String search);
	
	/**
	 * 查询所有模块不带分页
	 * @return
	 */
	List<Category> getAllCategoriesforApi();
	
	/**
	 * 获取所有条数
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 根据pid获取category
	 * @param pid
	 * @return
	 */
	Category getCategoryByCid(int cid);
	
	/**
	 * 根据id修改
	 * @param category
	 * @return
	 */
	boolean updateById(Category category);
	
	/**
	 * 根据id删除
	 * @param pid
	 * @return
	 */
	boolean deleteByIds(List<Integer> cids);
}
