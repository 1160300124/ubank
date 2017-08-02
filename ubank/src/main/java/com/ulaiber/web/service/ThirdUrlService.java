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
	
	/**
	 * 根据模块id和类别id查询url
	 * @param mid
	 * @param cid
	 * @return
	 */
	List<ThirdUrl> getUrlsByMidAndCid(int mid, int cid);
	
	/**
	 * 查询有没有url引用模块
	 * @param mids
	 * @return
	 */
	int getCountByMids(List<Integer> mids);
	
	/**
	 * 查询有没有url引用类别
	 * @param cids
	 * @return
	 */
	int getCountByCids(List<Integer> cids);

}
