package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Page;

/** 
 * APP端页面的业务逻辑接口
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午6:06:41
 * @version 1.0 
 * @since 
 */
public interface PageService {
	
	/**
	 * 新增
	 * 
	 * @param page
	 * @return
	 */
	boolean save(Page page);
	
	/**
	 * 查询所有模块带分页
	 * @return
	 */
	List<Page> getAllPages(int limit, int offset, String order, String search);
	
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
}
