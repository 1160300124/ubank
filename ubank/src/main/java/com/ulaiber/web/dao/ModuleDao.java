package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Module;

/** 
 * APP端模块的数据库接口
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午5:55:52
 * @version 1.0 
 * @since 
 */
public interface ModuleDao {
	
	/**
	 * 新增
	 * @param module
	 * @return
	 */
	int save(Module module);
	
	/**
	 * 查询所有模块带分页
	 * @return
	 */
	List<Module> getAllModules(Map<String, Object> params);
	
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

}
