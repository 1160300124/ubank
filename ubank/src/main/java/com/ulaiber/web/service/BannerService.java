package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.Banner;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 下午3:29:54
 * @version 1.0 
 * @since 
 */
public interface BannerService {
	
	/**
	 * 根据模块id查询url
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
	List<Banner> getBanners(int limit, int offset, String order, String search);
	
	/**
	 * 新增Banner
	 * @param banner
	 * @return
	 */
	boolean save(Banner banner);
	
	/**
	 * 根据id查询Banner
	 * @param bid
	 * @return
	 */
	Banner getBannerByBid(long bid);
	
	/**
	 * 根据id修改
	 * @param bid
	 * @return
	 */
	boolean updateById(Banner banner);
	
	/**
	 * 根据id删除
	 * @param ids
	 * @return
	 */
	boolean deleteByIds(List<Long> ids);

}
