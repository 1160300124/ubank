package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.AttendanceDao;
import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.model.Attendance;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.HttpsUtil;

/** 
 * 考勤记录业务逻辑实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月11日 上午11:08:05
 * @version 1.0 
 * @since 
 */
@Service
public class AttendanceServiceImpl extends BaseService implements AttendanceService {
	
	private static Logger logger = Logger.getLogger(AttendanceServiceImpl.class);
	
	@Resource
	private AttendanceDao dao;
	
	@Resource
	private AttendanceRuleDao ruleDao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public ResultInfo save(Attendance att) {
		ResultInfo info = new ResultInfo();
		//获取用户的考勤规则
		AttendanceRule rule = ruleDao.getRuleByUserId(att.getUserId());
		if (null == rule){
			logger.error("用户 {id=" + att.getUserId() + ",name=" + att.getUserName() + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户  " + att.getUserName() + " 没有设置考勤规则，请先设置。");
			return info;
		}

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
		String date = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		att.setClockType("0");
		att.setClockDate(date);
		//查询该用户当天有没有打上班卡
		Attendance dbAtt = dao.getRecordByCond(att);
		//type：0签到，1签退   status：0正常 ，1迟到， 2早退
		if (null == dbAtt){
			//如果加班到凌晨打卡，当天是没有打卡记录的，需判断当前打卡时间是否在最晚下班打卡时间之内，如在则为昨天的下班卡，否则为今天的上班卡
			if (time.compareTo(rule.getClockOffEndTime()) <= 0){
				att.setClockType("1");
				att.setClockStatus("0");
			}
			//当前时间早于最早上班打卡时间
			else if (time.compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOffEndTime()) > 0){
				logger.error("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				return info;
			}
			//当前时间大于最早上班打卡时间且小于上班打卡时间----正常打卡
			else if (time.compareTo(rule.getClockOnStartTime()) >= 0 && time.compareTo(rule.getClockOnTime()) <= 0)
			{
				att.setClockStatus("0");
			}
			//当前时间大于上班打卡时间且小于下班打卡时间----迟到
			else if (time.compareTo(rule.getClockOnTime()) > 0 && time.compareTo(rule.getClockOffTime()) < 0){
				att.setClockStatus("1");
			} 
		} else{
			att.setClockType("1");
			//当前时间大于下班打卡时间或小于最晚下班打卡时间(转钟后当前时间小于最晚下班打卡时间)
			if (time.compareTo(rule.getClockOffTime()) >= 0 || time.compareTo(rule.getClockOffEndTime()) <= 0){
				att.setClockStatus("0");
			}
			//当前时间大于上班打卡时间小于下班打卡时间----早退
			else if (time.compareTo(dbAtt.getClockTime()) > 0 && time.compareTo(rule.getClockOffTime()) < 0){
				att.setClockStatus("2");
			}
			//当前时间晚于最晚下班打卡时间
			else if (time.compareTo(rule.getClockOffEndTime()) > 0){
				logger.error("最晚下班打卡时间为次日 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("最晚下班打卡时间为次日 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			
		}
		
		att.setClockDate(date);
		att.setClockTime(time);
		
		if (dao.save(att) > 0){
			info.setCode(IConstants.QT_CODE_OK);
			att.setCompany(null);
			att.setDept(null);
			info.setData(att);
		} else{
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("数据库异常。");
			logger.error("数据库异常。");
		}
		
		return info;
	}

	@Override
	public List<Attendance> getRecordsByCond(Map<String, Object> params) {

		return dao.getRecordsByCond(params);
	}

	@Override
	public ResultInfo refreshLocation(String longitude, String latitude, HttpServletRequest request) {
		ResultInfo retInfo = new ResultInfo();
		//APP手机定位的坐标
		String mobileLocation = longitude + "," + latitude;
		//公司坐标
		String companyLocation = "113.941664,22.542380";
		String url = "http://restapi.amap.com/v3/distance?key=1f255ddf4f7ff1d1660b0417bee242e7&origins=" + mobileLocation + "&destination=" + companyLocation + "&type=0";
		String result = HttpsUtil.doGet(url);
		if (StringUtils.isNotEmpty(result)){
			//{"status":"1","info":"OK","infocode":"10000","results":[{"origin_id":"1","dest_id":"1","distance":"487","duration":"0"}]}
			JSONObject json = JSONObject.parseObject(result);
			if (!StringUtils.equals(json.getString("status"), "1")){
				String infocode = json.getString("infocode");
				String info = json.getString("info");
				logger.error("调用地图服务失败，code=" + infocode + ",info=" + info);
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("调用地图服务失败，code=" + infocode + ",info=" + info);
				return retInfo;
			}
			JSONArray array = json.getJSONArray("results");
			//打卡规则设置里设置的打卡距离
			long distance = 1000;
			long currentDistance = Long.parseLong(array.getJSONObject(0).getString("distance"));
			if (currentDistance > distance){
				logger.info("当前坐标离公司" + currentDistance + "m，超过设置的" + distance + "m");
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("当前坐标离公司" + currentDistance + "m，超过设置的" + distance + "m，在走进一点点就可以打卡了哟，么么哒...");
				return retInfo;
			}
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("当前坐标离公司" + currentDistance + "m，可以打卡。");
		}
		return retInfo;
	}

	@Override
	public int getCountBycond(Map<String, Object> params) {

		return dao.getCountBycond(params);
	}

	@Override
	public Attendance getRecordByCond(Attendance attend) {
		
		return dao.getRecordByCond(attend);
	}

	@Override
	public List<Attendance> getRecordsByDateAndMobile(String date, String mobile) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clockDate", date);
		params.put("mobile", mobile);
		return dao.getRecordsByDateAndMobile(params);
	}

	@Override
	public Attendance getLatestRecordByUserId(long userId) {
		
		return dao.getLatestRecordByUserId(userId);
	}

}
