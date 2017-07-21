package com.ulaiber.web.service;

import java.util.List;

import com.ulaiber.web.model.ThirdUrl;

/**
 * 第三方URL业务逻辑接口
 * 
 * @author huangguoqing
 *
 */
public interface ThirdUrlService {
	
	/**
	 * 新增一个第三方URL
	 * @param url
	 * @return
	 */
	boolean save(ThirdUrl url);
	
	/**
	 * 删除第三方URL，支持批量删除
	 * 
	 * @param uids
	 * @return
	 */
	boolean deleteByUids(List<Long> uids);
	
	/**
	 * 根据uid修改
	 * @param uid
	 * @return
	 */
	boolean updateByUid(ThirdUrl url);
	
	/**
	 * 获取所有的第三方URL
	 * 
	 * @return List<ThirdUrl>
	 */
	List<ThirdUrl> getAllUrls(int limit, int offset, String order, String search);
	
	/**
	 * 获取总的URL数
	 * 
	 * @return
	 */
	int getTotalNum();

}
