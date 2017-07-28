package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Category;

/** 
 * APP端类别的数据库接口
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午5:57:13
 * @version 1.0 
 * @since 
 */
public interface CategoryDao {
	
	/**
	 * 新增
	 * @param category
	 * @return
	 */
	int save(Category category);
	
	/**
	 * 查询所有类别带分页
	 * @return
	 */
	List<Category> getAllCategories(Map<String, Object> params);
	
	/**
	 * 查询所有类别不带分页
	 * @return
	 */
	List<Category> getAllCategoriesforApi();
	
	/**
	 * 获取所有条数
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 根据cid获取category
	 * @param pid
	 * @return
	 */
	Category getCategoryByCid(int cid);
	
	/**
	 * 根据id修改
	 * @param category
	 * @return
	 */
	int updateById(Category category);
	
	/**
	 * 根据id删除
	 * @param cids
	 * @return
	 */
	int deleteByIds(List<Integer> cids);
}
