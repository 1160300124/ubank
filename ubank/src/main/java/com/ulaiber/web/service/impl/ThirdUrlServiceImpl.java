package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.ThirdUrlDao;
import com.ulaiber.web.model.ThirdUrl;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.ThirdUrlService;
import com.ulaiber.web.utils.DateTimeUtil;

/**
 * 第三方URL业务逻辑接口
 * 
 * @author huangguoqing
 *
 */
@Service
public class ThirdUrlServiceImpl extends BaseService implements ThirdUrlService {
	
	@Resource
	private ThirdUrlDao dao;
	
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ThirdUrl url){
		url.setCreateTime(DateTimeUtil.date2Str(new Date()));
		return dao.save(url) > 0;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ThirdUrl> getAllUrls(int limit, int offset, String order, String search) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("search", search);
		
		return this.dao.getAllUrls(params);
	}

	@Override
	@Transactional(readOnly = true)
	public int getTotalNum() {
		
		return this.dao.getTotalNum();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteByUids(List<Long> uids) {
		
		return this.dao.deleteByUids(uids) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateByUid(ThirdUrl url) {

		url.setUpdateTime(DateTimeUtil.date2Str(new Date()));
		return this.dao.updateByUid(url) > 0;
	}

}
