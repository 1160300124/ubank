package com.ulaiber.web.service.impl;

import java.util.Arrays;
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
import com.ulaiber.web.model.Holiday;
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
//		String datetime = "2017-08-24 20:49";
		String today = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		boolean isRestDay = false;
		String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(today) + "");
		//节假日
		if (rule.getHolidayFlag() == 0){
			Holiday holiday = ruleDao.getHolidaysByYear(DateTimeUtil.getYear(new Date()) + "");
			if (holiday != null){
				List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
				List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
				if (holidays.contains(today) && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0
						|| time.compareTo(rule.getClockOffEndTime()) > 0 && rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0)){
					isRestDay = true;
				} else {
					if (rule.getWorkday().contains(workday) || workdays.contains(today)){
						isRestDay = false;
					} else {
						isRestDay = true;
					}
				}
			}
		} else {
			if (!rule.getWorkday().contains(workday)){
				isRestDay = true;
			} 
		}
		if (isRestDay){
			logger.info("休息日不用打卡："+ today + "-" + workday);
			info.setCode(IConstants.QT_REST_DAY);
			info.setMessage("休息日不用打卡："+ today + "-" + workday);
			return info;
		}
		
		att.setClockDate(today);
		att.setClockTime(time);
		
		String yesterday = "";
		//if最晚打卡时间<最早打卡时间，说明最晚打卡时间为转钟后的凌晨时间，否则为当天时间
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOffEndTime()) <= 0){
			//转钟后的前一天日期 yyyy-MM-dd 
			yesterday = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", att.getUserId());
		params.put("clockType", "");
		params.put("clockDate", today);
		params.put("yesterday", yesterday);
		params.put("clockOnStartTime", rule.getClockOnStartTime());
		params.put("clockOffEndTime", rule.getClockOffEndTime());
		
		//查询该用户当天的打卡记录
		List<Attendance> records = dao.getRecordsByDateAndUserId(params);
		
		String clockOnTime= rule.getClockOnTime();
		String clockOffTime = rule.getClockOffTime();
		//是否开启弹性时间
		if (rule.getFlexibleFlag() == 0){
			clockOnTime = DateTimeUtil.getfutureTime(today + " " + rule.getClockOnTime(), 0, 0, rule.getFlexibleTime()).split(" ")[1];
			//下班是否顺延
			if (rule.getPostponeFlag() == 0){
				clockOffTime = DateTimeUtil.getfutureTime(today + " " + rule.getClockOffTime(), 0, 0, rule.getFlexibleTime()).split(" ")[1];
			}
		}
		
		//新增，更新标志，默认失败
		boolean flag = false;
		//type：0签到，1签退   status：0正常 ，1迟到， 2早退
		if (null == records || records.size() == 0){
			//没有记录时说明没有打卡,先默认是打上班卡
			att.setClockType("0");
			//当前时间<最早上班打卡时间&&最晚下班打卡时间>最早上班打卡时间 || 当前时间<最早上班打卡时间&&最晚下班打卡时间<最早上班打卡时间&&当前时间>最晚下班打卡时间
			if (time.compareTo(rule.getClockOnStartTime()) < 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0
					|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOffEndTime()) > 0)){
				logger.error("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				info.setCode(IConstants.QT_CANNOT_CLOCK_ON);
				info.setMessage("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				return info;
			}
			//当前时间>最晚下班打卡时间&&最晚下班打卡时间>最早上班打卡时间 || 当前时间>最晚下班打卡时间&&最晚下班打卡时间<最早上班打卡时间&&当前时间<最早上班打卡时间
			if (time.compareTo(rule.getClockOffEndTime()) > 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0) 
					|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOnStartTime()) < 0){ 
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			//如果加班到凌晨打卡，当天是没有打卡记录的，需判断当前打卡时间是否在最晚下班打卡时间之内，如在则为昨天的下班卡，否则为今天的上班卡
			if (time.compareTo(clockOffTime) >= 0
					|| time.compareTo(rule.getClockOffEndTime()) <= 0 && rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				att.setClockType("1");
				att.setClockStatus("0");
			}
			//当前时间大于最早上班打卡时间且小于上班打卡时间----正常打卡
			else if (time.compareTo(rule.getClockOnStartTime()) >= 0 && time.compareTo(clockOnTime) <= 0)
			{
				att.setClockStatus("0");
			}
			//当前时间大于上班打卡时间且小于下班打卡时间----迟到
			else if (time.compareTo(clockOnTime) > 0 && time.compareTo(clockOffTime) < 0){
				att.setClockStatus("1");
			} 
			
			flag = dao.save(att) > 0;
			
		} else {
			att.setClockType("1");
			//当前时间晚于最晚下班打卡时间
			if (time.compareTo(rule.getClockOffEndTime()) > 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0) 
					|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOnStartTime()) < 0){ 
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			//当前时间等于上次打卡时间
			if (time.compareTo(records.get(0).getClockTime()) == 0){
				logger.error("歇一会嘛，不要太频繁打卡哟。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_FREQUENTLY);
				info.setMessage("歇一会嘛，不要太频繁打卡哟。");
				return info;
			}
			//当前时间大于下班打卡时间或小于最晚下班打卡时间(转钟后当前时间小于最晚下班打卡时间)
			if (time.compareTo(clockOffTime) >= 0
					|| time.compareTo(rule.getClockOffEndTime()) <= 0 && rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				att.setClockStatus("0");
			}
			//当前时间大于上次打卡时间小于下班打卡时间----早退
			else if (time.compareTo(records.get(0).getClockTime()) > 0 && time.compareTo(clockOffTime) <= 0){
				att.setClockStatus("2");
			}
			
			//如果有一条打卡记录,如果是上班记录则插入下班卡记录，否则更新下班卡记录
			if (records.size() == 1){
				//判断是上班记录还是下班记录,如果是上班记录则插入下班卡记录，否则更新下班卡记录
				if (StringUtils.equals(records.get(0).getClockType(), "0")){
					flag = dao.save(att) > 0;
				} else {
					//如果是下班记录则更新记录
					if (!StringUtils.equals(records.get(0).getClockDate(), today)){
						att.setYesterday(yesterday);
					} 
					flag = dao.updateClockOffInfo(att);
				}
			}
			//如果有2条记录，说明上下班记录都有，直接更新下班记录
			else if (records.size() >= 2){
				if (!StringUtils.equals(records.get(1).getClockDate(), today)){
					att.setYesterday(yesterday);
				} 
				flag = dao.updateClockOffInfo(att);
			}
		} 
		
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			att.setCompany(null);
			att.setDept(null);
			info.setData(att);
		} else{
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增或更新失败。");
			logger.error("新增或更新失败。");
		}
		
		return info;
	}

	@Override
	public List<Attendance> getRecordsByCond(Map<String, Object> params) {

		return dao.getRecordsByCond(params);
	}

	@Override
	public ResultInfo refreshLocation(String mobile, String longitude, String latitude, HttpServletRequest request) {
		ResultInfo retInfo = new ResultInfo();
		AttendanceRule rule = ruleDao.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("用户  " + mobile + " 没有设置考勤规则，请先设置。");
			return retInfo;
		}
		
		//APP手机定位的坐标
		String mobileLocation = longitude + "," + latitude;
		//公司坐标113.941664,22.542380
		String companyLocation = rule.getLongit_latit();
		String url = "http://restapi.amap.com/v3/distance?key=1f255ddf4f7ff1d1660b0417bee242e7&origins=" + companyLocation + "&destination=" + mobileLocation + "&type=0";
		String result = HttpsUtil.doGet(url);
		if (StringUtils.isNotEmpty(result)){
			//{"status":"1","info":"OK","infocode":"10000","results":[{"origin_id":"1","dest_id":"1","distance":"487","duration":"0"},{"origin_id":"2","dest_id":"1","distance":"338","duration":"0"}]}
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
			long distance = rule.getClockBounds();
			//最小的距离
			long minDistance = Long.parseLong(array.getJSONObject(0).getString("distance"));
			for (Object obj : array){
				JSONObject jsonObject = (JSONObject)obj;
				//当前距离
				long currentDistance = Long.parseLong(jsonObject.getString("distance"));
				if (minDistance > currentDistance){
					minDistance = currentDistance;
				}
			}
			if (minDistance > distance){
				logger.info("当前坐标离公司" + minDistance + "m，超过设置的" + distance + "m");
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("当前坐标离公司" + minDistance + "m，超过设置的" + distance + "m。");
				return retInfo;
			}
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("当前坐标离公司" + minDistance + "m，可以打卡。");
		}
		return retInfo;
	}

	@Override
	public int getCountBycond(Map<String, Object> params) {

		return dao.getCountBycond(params);
	}

	@Override
	public List<Attendance> getRecordsByDateAndUserId(Map<String, Object> params) {
		
		return dao.getRecordsByDateAndUserId(params);
	}

	@Override
	public List<Attendance> getRecordsByDateAndMobile(String today, String yesterday, String clockOnStartTime,  String clockOffEndTime, String mobile) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clockDate", today);
		params.put("yesterday", yesterday);
		params.put("clockOnStartTime", clockOnStartTime);
		params.put("clockOffEndTime", clockOffEndTime);
		params.put("mobile", mobile);
		return dao.getRecordsByDateAndMobile(params);
	}

	@Override
	public Attendance getLatestRecordByUserId(long userId) {
		
		return dao.getLatestRecordByUserId(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateClockOffInfo(Attendance record) {

		return dao.updateClockOffInfo(record);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteRecordsByRids(List<Long> rids) {

		return dao.deleteRecordsByRids(rids) > 0;
	}

	@Override
	public List<Attendance> getRecordsByMonthAndMobile(String month, String mobile) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("month", month);
		params.put("mobile", mobile);
		return dao.getRecordsByMonthAndMobile(params);
	}

}
