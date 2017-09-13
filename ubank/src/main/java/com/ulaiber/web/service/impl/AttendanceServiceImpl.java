package com.ulaiber.web.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
	public ResultInfo save(Attendance att, AttendanceRule rule) {
		ResultInfo info = new ResultInfo();

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String datetime = "2017-09-12 02:10";
		String date = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		String dateBegin = date + " " + rule.getClockOnStartTime();
		String dateEnd = date + " " + rule.getClockOffEndTime();
		String today = date;
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
			if (time.compareTo(rule.getClockOffEndTime()) <= 0){
				today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
				dateBegin = today + " " + rule.getClockOnStartTime();
			} else {
				dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
			}
		}
		
		boolean isRestDay = false;
		String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(today) + "");
		//节假日
		if (rule.getHolidayFlag() == 0){
			Holiday holiday = ruleDao.getHolidaysByYear(today.split("-")[0]);
			if (holiday != null){
				List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
				List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
				if (holidays.contains(today) && datetime.compareTo(dateBegin) >= 0 && datetime.compareTo(dateEnd) <= 0){
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
		
		att.setClockDate(date);
		att.setClockTime(time);
		att.setClockDateTime(datetime);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", att.getUserId());
		params.put("clockType", "");
		params.put("dateBegin", dateBegin);
		params.put("dateEnd", dateEnd);
		
		//查询该用户当天的打卡记录
		List<Attendance> records = dao.getRecordsByDateAndUserId(params);
		
		String clockOnTime= today + " " + rule.getClockOnTime();
		String clockOffTime = today + " " + rule.getClockOffTime();
		//是否开启弹性时间
		if (rule.getFlexibleFlag() == 0){
			clockOnTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, rule.getFlexibleTime());
			//下班是否顺延
			if (rule.getPostponeFlag() == 0){
				clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 0, 0, rule.getFlexibleTime());
			}
		}
		
		//新增，更新标志，默认失败
		boolean flag = false;
		//type：0签到，1签退   status：0正常 ，1迟到， 2早退
		if (null == records || records.size() == 0){
			//没有记录时说明没有打卡,先默认是打上班卡
			att.setClockType("0");
			//当前时间<最早上班打卡时间&&最晚下班打卡时间>最早上班打卡时间 || 当前时间<最早上班打卡时间&&最晚下班打卡时间<最早上班打卡时间&&当前时间>最晚下班打卡时间
			if (datetime.compareTo(dateBegin) < 0){
//			if (time.compareTo(rule.getClockOnStartTime()) < 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0
//					|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOffEndTime()) > 0)){
				logger.error("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				info.setCode(IConstants.QT_CANNOT_CLOCK_ON);
				info.setMessage("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				return info;
			}
			//当前时间>最晚下班打卡时间&&最晚下班打卡时间>最早上班打卡时间 || 当前时间>最晚下班打卡时间&&最晚下班打卡时间<最早上班打卡时间&&当前时间<最早上班打卡时间
			if (datetime.compareTo(dateEnd) > 0){
//			if (time.compareTo(rule.getClockOffEndTime()) > 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0) 
//					|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOnStartTime()) < 0){ 
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			//如果加班到凌晨打卡，当天是没有打卡记录的，需判断当前打卡时间是否在最晚下班打卡时间之内，如在则为昨天的下班卡，否则为今天的上班卡
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <= 0){
				att.setClockType("1");
				att.setClockStatus("0");
			}
			//当前时间大于最早上班打卡时间且小于上班打卡时间----正常打卡
			else if (datetime.compareTo(dateBegin) >= 0 && datetime.compareTo(clockOnTime) <= 0)
			{
				att.setClockStatus("0");
			}
			//当前时间大于上班打卡时间且小于下班打卡时间----迟到
			else if (datetime.compareTo(clockOnTime) > 0 && datetime.compareTo(clockOffTime) < 0){
				att.setClockStatus("1");
			} 
			
			flag = dao.save(att) > 0;
			
		} else {
			att.setClockType("1");
			//当前时间晚于最晚下班打卡时间
			if (datetime.compareTo(dateEnd) > 0){
//			if (time.compareTo(rule.getClockOffEndTime()) > 0 && (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) > 0) 
//					|| rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0 && time.compareTo(rule.getClockOnStartTime()) < 0){ 
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}

			//当前时间大于下班打卡时间或小于最晚下班打卡时间(转钟后当前时间小于最晚下班打卡时间)
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <=0 ){
//			if (time.compareTo(clockOffTime) >= 0
//					|| time.compareTo(rule.getClockOffEndTime()) <= 0 && rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				att.setClockStatus("0");
			}
			//当前时间大于上次打卡时间小于下班打卡时间----早退
			else if (datetime.compareTo(records.get(0).getClockDateTime()) > 0 && datetime.compareTo(clockOffTime) <= 0){
				att.setClockStatus("2");
			}
			
			//如果有一条打卡记录,如果是上班记录则插入下班卡记录，否则更新下班卡记录
			if (records.size() == 1){
				//当前时间等于上次打卡时间
				if (StringUtils.equals(time, records.get(0).getClockTime())){
					logger.error("歇一会嘛，不要太频繁打卡。");
					info.setCode(IConstants.QT_CANNOT_CLOCK_FREQUENTLY);
					info.setMessage("歇一会嘛，不要太频繁打卡。");
					return info;
				}
				//判断是上班记录还是下班记录,如果是上班记录则插入下班卡记录，否则更新下班卡记录
				if (StringUtils.equals(records.get(0).getClockType(), "0")){
					flag = dao.save(att) > 0;
				} else {
					//如果是下班记录则更新记录
					if (!StringUtils.equals(records.get(0).getClockDate(), date)){
						att.setYesterday(today);
					} 
					flag = dao.updateClockOffInfo(att);
				}
			}
			//如果有2条记录，说明上下班记录都有，直接更新下班记录
			else if (records.size() >= 2){
				//当前时间等于上次打卡时间
				if (StringUtils.equals(time, records.get(1).getClockTime())){
					logger.error("歇一会嘛，不要太频繁打卡。");
					info.setCode(IConstants.QT_CANNOT_CLOCK_FREQUENTLY);
					info.setMessage("歇一会嘛，不要太频繁打卡。");
					return info;
				}
				if (!StringUtils.equals(records.get(1).getClockDate(), date)){
					att.setYesterday(today);
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
	public ResultInfo refreshLocation(String mobile, String longitude, String latitude, AttendanceRule rule) {
		ResultInfo retInfo = new ResultInfo();
		
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
	public List<Attendance> getRecordsByDateAndMobile(String dateBegin, String dateEnd, String mobile, AttendanceRule rule) {
		Map<String, Object> params = new HashMap<String, Object>();
		String dayBegin = dateBegin + " " + rule.getClockOnStartTime();
		String dayEnd = dateEnd + " " + rule.getClockOffEndTime();
		//if最晚打卡时间<最早打卡时间，说明最晚打卡时间为转钟后的凌晨时间，否则为当天时间
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
			//转钟后的前一天日期 yyyy-MM-dd 
			dayEnd = DateTimeUtil.getfutureTime(dayEnd, 1, 0, 0);
		}
		params.put("dateBegin", dayBegin);
		params.put("dateEnd", dayEnd);
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
	public Map<String, Object> getRecordsByMonthAndMobile(String month, String mobile, AttendanceRule rule) {
		Map<String, Object> params = new HashMap<String, Object>();
		//每个月的考勤时间，1号的最早打卡时间---31号最晚打卡时间(如果最晚打卡时间为凌晨，则为下个月的1号的凌晨时间)
		String monthBegin = month + "-01 "+ rule.getClockOnStartTime();
		int num = DateTimeUtil.getNumFromMonth(month);
		String monthEnd = month + "-" + num + " " + rule.getClockOffEndTime();
		//if最晚打卡时间<最早打卡时间，说明最晚打卡时间为转钟后的凌晨时间，否则为当天时间
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
			//月份的最后一天日期 yyyy-MM-dd HH:mm
			monthEnd = DateTimeUtil.getfutureTime(monthEnd, 1, 0, 0);
		}

		params.put("dateBegin", monthBegin);
		params.put("dateEnd", monthEnd);
		params.put("mobile", mobile);
		List<Attendance> list = dao.getRecordsByDateAndMobile(params);

		Holiday holiday = ruleDao.getHolidaysByYear(month.split("-")[0]);
		List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
		List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		List<String> days = DateTimeUtil.getDaysFromMonth(month);
		//0 正常   1异常(迟到，早退，忘打卡) 2未打卡  3休息日
		int type = 2;
		for (String day : days){
			data.put(day, type);
			boolean isRestDay = false;
			String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(day) + "");
			//节假日
			if (rule.getHolidayFlag() == 0){
				if (holiday != null){
					if (holidays.contains(day)){
						isRestDay = true;
					} else {
						if (rule.getWorkday().contains(workday) || workdays.contains(day)){
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
				data.put(day, 3);
			}

			String dayBegin = day + " " + rule.getClockOnStartTime();
			String dayEnd = day + " " + rule.getClockOffEndTime();
			//if最晚打卡时间<最早打卡时间，说明最晚打卡时间为转钟后的凌晨时间，否则为当天时间
			if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				//转钟后的前一天日期 yyyy-MM-dd 
				dayEnd = DateTimeUtil.getfutureTime(dayEnd, 1, 0, 0);
			}
			int count = 0;
			for (Attendance att : list){
				if (att.getClockDateTime().compareTo(dayBegin) >= 0 && att.getClockDateTime().compareTo(dayEnd) <= 0){
					if (StringUtils.equals(att.getClockType(), "0") && StringUtils.equals(att.getClockStatus(), "0")){
						type = 1;
					} else if (StringUtils.equals(att.getClockType(), "1") && StringUtils.equals(att.getClockStatus(), "0")){
						type = 0;
					}
					count ++;
				}
			}
			
			if (count == 0){
				data.put(day, 2);
				if (isRestDay){
					data.put(day, 3);
				}
			} else if (count == 1){
				data.put(day, 1);
				if (isRestDay){
					data.put(day, 0);
				}
			} else if (count == 2){
				data.put(day, type);
				if (isRestDay){
					data.put(day, 0);
				}
			}

		}
		
		return data;
	}

}
