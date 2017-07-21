package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Page;

/** 
 * APP端页面的数据库接口
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午5:57:13
 * @version 1.0 
 * @since 
 */
public interface PageDao {
	
	/**
	 * 新增
	 * @param page
	 * @return
	 */
	int save(Page page);
	
	/**
	 * 查询所有模块带分页
	 * @return
	 */
	List<Page> getAllPages(Map<String, Object> params);
	
	/**
	 * 查询所有模块不带分页
	 * @return
	 */
	List<Page> getAllPagesforApi();
	
	/**
	 * 获取所有条数
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 根据pid获取Page
	 * @param pid
	 * @return
	 */
	Page getPageByPid(int pid);
}
