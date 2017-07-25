package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.BannerDao;
import com.ulaiber.web.model.Banner;
import com.ulaiber.web.service.BannerService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月24日 下午3:30:47
 * @version 1.0 
 * @since 
 */
@Service
public class BannerServiceImpl extends BaseService implements BannerService {
	
	@Resource
	private BannerDao dao;

	@Override
	public List<Banner> getBannersByMid(int mid) {
		return dao.getBannersByMid(mid);
	}
	
	@Override
	public int getTotalNum() {

		return dao.getTotalNum();
	}

	@Override
	public List<Banner> getBanners(int limit, int offset, String order, String search) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("search", search);
		return dao.getBanners(params);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Banner banner) {
		banner.setCreateTime(DateTimeUtil.date2Str(new Date()));
		return dao.save(banner) > 0;
	}

	@Override
	public Banner getBannerByBid(long bid) {

		return dao.getBannerByBid(bid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateById(Banner banner) {
		
		return dao.updateById(banner) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByIds(List<Long> ids) {

		return dao.deleteByIds(ids) > 0;
	}

}
