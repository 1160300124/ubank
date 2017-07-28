package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Module;

/** 
 * APP端模块的业务逻辑接口
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午6:08:03
 * @version 1.0 
 * @since 
 */
public interface ModuleService {
	
	/**
	 * 新增
	 * 
	 * @param module
	 * @return
	 */
	boolean save(Module module);
	
	/**
	 * 查询所有模块带分页
	 * @return
	 */
	List<Module> getAllModules(int limit, int offset, String order, String search);
	
	/**
	 * 查询所有模块不带分页
	 * @return
	 */
	List<Module> getAllModulesforApi();

	/**
	 * 获取所有条数
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 根据mid获取模块
	 * @param mid
	 * @return
	 */
	Module getModuleByMid(int mid);
	
	/**
	 * 根据id删除
	 * @param mids
	 * @return
	 */
	boolean deleteByIds(List<Integer> mids);
	
	/**
	 * 根据id修改
	 * @param module
	 * @return
	 */
	boolean updateById(Module module);
}
