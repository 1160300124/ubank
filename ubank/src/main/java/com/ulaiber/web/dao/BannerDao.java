package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Banner;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 下午3:28:03
 * @version 1.0 
 * @since 
 */
public interface BannerDao {
	
	/**
	 * 根据模块id查询Banner
	 * @return
	 */
	List<Banner> getBannersByMid(int mid);
	
	/**
	 * 查询总条数
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 后台分页查询Banner
	 * @return
	 */
	List<Banner> getBanners(Map<String, Object> params);
	
	/**
	 * 新增Banner
	 * @param banner
	 * @return
	 */
	int save(Banner banner);
	
	/**
	 * 根据id查询Banner
	 * @param bid
	 * @return
	 */
	Banner getBannerByBid(long bid);
	
	/**
	 * 根据id修改
	 * @param banner
	 * @return
	 */
	int updateById(Banner banner);
	
	/**
	 * 根据id删除
	 * @param ids
	 * @return
	 */
	int deleteByIds(List<Long> ids);
	
	/**
	 * 查询有没有banner引用模块
	 * @param mids
	 * @return
	 */
	int getCountByMids(List<Integer> mids);
	
}
