package com.ulaiber.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.PageDao;
import com.ulaiber.web.model.Page;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.PageService;

/** 
 * APP端页面的业务逻辑实现类
 * 
 * @author  huangguoqing
 * @date 创建时间：2017年7月19日 下午6:09:22
 * @version 1.0 
 * @since 
 */
@Service
public class PageServiceImpl extends BaseService implements PageService {
	
	@Resource
	private PageDao dao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Page page) {

		return dao.save(page) > 0;
	}

	@Override
	public List<Page> getAllPages(int limit, int offset, String order, String search) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("order", order);
		params.put("search", search);
		
		return dao.getAllPages(params);
	}

	@Override
	public List<Page> getAllPagesforApi() {

		return dao.getAllPagesforApi();
	}

	@Override
	public int getTotalNum() {
		
		return dao.getTotalNum();
	}

}
