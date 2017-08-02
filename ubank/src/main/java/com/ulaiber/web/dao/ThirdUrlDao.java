package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.ThirdUrl;

/**
 * 第三方URL数据库接口
 * 
 * @author huangguoqing
 *
 */
public interface ThirdUrlDao {
	
	/**
	 * 新增一个第三方URL
	 * @param url
	 * @return
	 */
	int save(ThirdUrl url);
	
	/**
	 * 删除第三方URL，支持批量删除
	 * 
	 * @param uids
	 * @return
	 */
	int deleteByUids(List<Long> uids);
	
	/**
	 * 根据uid修改
	 * @param uid
	 * @return
	 */
	int updateByUid(ThirdUrl url);

	/**
	 * 获取所有的第三方URL
	 * 
	 * @return List<ThirdUrl>
	 */
	List<ThirdUrl> getAllUrls(Map<String, Object> params);
	
	/**
	 * 获取总的URL数
	 * 
	 * @return
	 */
	int getTotalNum();
	
	/**
	 * 根据模块id和类别id查询url
	 * @return
	 */
	List<ThirdUrl> getUrlsByMidAndCid(Map<String, Object> params);
	
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
