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
	public ResultInfo save(Attendance att, String device, String location, AttendanceRule rule) {
		ResultInfo info = new ResultInfo();

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String datetime = "2017-09-20 01:10";
		String date = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		String clockOnTime= date + " " + rule.getClockOnTime();
		String clockOffTime = date + " " + rule.getClockOffTime();
		String dateBegin = date + " " + rule.getClockOnStartTime();
		String dateEnd = date + " " + rule.getClockOffEndTime();
		String today = date;
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
			if (time.compareTo(rule.getClockOffEndTime()) <= 0){
				today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
				dateBegin = today + " " + rule.getClockOnStartTime();
				clockOnTime = today + " " + rule.getClockOnTime();
			} else {
				dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
				if (rule.getClockOnTime().compareTo(rule.getClockOffTime()) > 0){
					clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 1, 0, 0);
				}
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
		
		att.setClockDate(today);
		
		//是否开启弹性时间
		if (rule.getFlexibleFlag() == 0){
			clockOnTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, rule.getFlexibleTime());
			//下班是否顺延
			if (rule.getPostponeFlag() == 0){
				clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 0, 0, rule.getFlexibleTime());
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", att.getUserId());
		params.put("dateBegin", today);
		params.put("dateEnd", today);
		//查询该用户当天的打卡记录
		List<Attendance> records = dao.getRecordsByDateAndUserId(params);
		
		//新增，更新标志，默认失败
		boolean flag = false;
		// clockOnStatus：0正常 ，1迟到     clockOffStatus： 0正常 ，1早退
		if (records == null || records.size() == 0){
			//当前时间<最早上班打卡时间
			if (datetime.compareTo(dateBegin) < 0){
				logger.error("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				info.setCode(IConstants.QT_CANNOT_CLOCK_ON);
				info.setMessage("最早上班打卡时间为 " + rule.getClockOnStartTime() + ",请等待...");
				return info;
			}
			//当前时间>最晚下班打卡时间
			if (datetime.compareTo(dateEnd) > 0){
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			//当前时间大于下班打卡时间或小于最晚下班打卡时间
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <= 0){
				att.setClockOffDateTime(datetime);
				att.setClockOffDevice(device);
				att.setClockOffLocation(location);
				att.setClockOffStatus("0");
			}
			//当前时间大于最早上班打卡时间且小于上班打卡时间----正常打卡
			else if (datetime.compareTo(dateBegin) >= 0 && datetime.compareTo(clockOnTime) <= 0)
			{
				att.setClockOnDateTime(datetime);
				att.setClockOnDevice(device);
				att.setClockOnLocation(location);
				att.setClockOnStatus("0");
			}
			//当前时间大于上班打卡时间且小于下班打卡时间----迟到
			else if (datetime.compareTo(clockOnTime) > 0 && datetime.compareTo(clockOffTime) < 0){
				att.setClockOnDateTime(datetime);
				att.setClockOnDevice(device);
				att.setClockOnLocation(location);
				att.setClockOnStatus("1");
			} 
			
			flag = dao.save(att) > 0;
			
		} else {
			//当前时间等于上次打卡时间
			if (StringUtils.equals(datetime, records.get(0).getClockOffDateTime())){
				logger.error("歇一会嘛，不要太频繁打卡。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_FREQUENTLY);
				info.setMessage("歇一会嘛，不要太频繁打卡。");
				return info;
			}
			
			att.setClockOffDateTime(datetime);
			att.setClockOffDevice(device);
			att.setClockOffLocation(location);
			att.setClockOffStatus("0");
			//当前时间晚于最晚下班打卡时间
			if (datetime.compareTo(dateEnd) > 0){
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			if (datetime.compareTo(clockOnTime) < 0){
				logger.error("上班时间为 " + rule.getClockOnTime() + ",请上班后再来打卡。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("上班时间为 " + rule.getClockOnTime() + ",请上班后再来打卡。");
				return info;
			}
			String clockOffBegin = clockOnTime.compareTo(records.get(0).getClockOnDateTime()) > 0 ? clockOnTime : records.get(0).getClockOnDateTime();
			//当前时间大于下班打卡时间或小于最晚下班打卡时间
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <= 0 ){
				att.setClockOffStatus("0");
			}
			//当前时间大于上次打卡时间小于下班打卡时间----早退
			else if (datetime.compareTo(clockOffBegin) > 0 && datetime.compareTo(clockOffTime) <= 0){
				att.setClockOffStatus("1");
			}
			
			flag = dao.updateClockOffInfo(att);
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
	public List<Attendance> getRecordsByDateAndMobile(String dateBegin, String dateEnd, String mobile) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateBegin", dateBegin);
		params.put("dateEnd", dateEnd);
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
		Holiday holiday = null;
		List<String> holidays = null;
		List<String> workdays = null;
		if (rule.getHolidayFlag() == 0){
			holiday = ruleDao.getHolidaysByYear(month.split("-")[0]);
			if (holiday == null){
				return null;
			}
			holidays = Arrays.asList(holiday.getHoliday().split(","));
			workdays = Arrays.asList(holiday.getWorkday().split(","));
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		String monthBegin = month + "-01";
		//获取每个月最后一天
		String monthEnd = month + "-" + DateTimeUtil.getNumFromMonth(month);
		params.put("dateBegin", monthBegin);
		params.put("dateEnd", monthEnd);
		params.put("mobile", mobile);
		List<Attendance> list = dao.getRecordsByDateAndMobile(params);
		
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		//指定月份为空则获取当前月份
		String currentMonth = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MONTHTIME);
		//0 正常   1异常(迟到，早退，忘打卡) 2未打卡  3休息日
		int type = 3;
		
		List<String> days = DateTimeUtil.getDaysFromMonth(month);
		for (String day : days){
			data.put(day, type);
			if (month.compareTo(currentMonth) > 0){
				continue;
			}
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
				continue;
			}

			int count = 0;
			for (Attendance att : list){
				if (StringUtils.equals(att.getClockDate(), day)){
					if (StringUtils.equals(att.getClockOnStatus(), "0") && StringUtils.equals(att.getClockOffStatus(), "0")){
						type = 0;
					} else {
						type =1;
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
				data.put(day, type);
				if (isRestDay){
					data.put(day, 0);
				}
			} 

		}
		
		return data;
	}

	@Override
	public double getHoursByDateAndMobile(String startDateTime, String endDateTime, AttendanceRule rule) {
		//开始日期  yyyy-MM-dd
		String startDate = startDateTime.split(" ")[0];
		//结束日期  yyyy-MM-dd
		String endDate = endDateTime.split(" ")[0];
		double hour = 0;
		List<String> days = DateTimeUtil.getDaysFromDate(startDate, endDate);
		if (days.size() == 1){
			//上班时间
			String clockOnTime = startDate + " " + rule.getClockOnTime();
			//休息开始时间
			String restStartTime = startDate + " " + rule.getRestStartTime();
			//休息结束时间
			String restEndTime = startDate + " " + rule.getRestEndTime();
			//下班时间
			String clockOffTime = endDate + " " + rule.getClockOffTime();
			//请假开始时间
			startDateTime = startDateTime.compareTo(clockOnTime) < 0 ? clockOnTime : startDateTime;
			//请假结束时间
			endDateTime = endDateTime.compareTo(clockOffTime) > 0 ? clockOffTime : endDateTime;
			
			long start = DateTimeUtil.str2Date(startDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			long end = DateTimeUtil.str2Date(endDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			long restStart = DateTimeUtil.str2Date(restStartTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			long restEnd = DateTimeUtil.str2Date(restEndTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			
			if (startDateTime.compareTo(restStartTime) > 0 && startDateTime.compareTo(restEndTime) < 0){
				start = restEnd;
				hour = (double)(end - start) / IConstants.HOUR_MS;
			} else if (startDateTime.compareTo(restEndTime) >= 0){
				hour = (double)(end - start) / IConstants.HOUR_MS;
			} else if (endDateTime.compareTo(restStartTime) > 0 && endDateTime.compareTo(restEndTime) < 0){
				end = restStart;
				hour = (double)(end - start) / IConstants.HOUR_MS;
			} else if(endDateTime.compareTo(restStartTime) <= 0){
				hour = (double)(end - start) / IConstants.HOUR_MS;
			} else {
				hour = (double)(end - start - (restEnd - restStart)) / IConstants.HOUR_MS;
			}
			
			
		} else if (days.size() >= 2) {
			for (String day : days){
				
				boolean isRestDay = false;
				String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(day) + "");
				//节假日
				if (rule.getHolidayFlag() == 0){
					Holiday holiday = ruleDao.getHolidaysByYear(day.split("-")[0]);
					if (holiday != null){
						List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
						List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
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
					continue;
				}
				
				if (StringUtils.equals(day, startDate)){
					//上班时间
					String clockOnTime = startDate + " " + rule.getClockOnTime();
					//休息开始时间
					String restStartTime = startDate + " " + rule.getRestStartTime();
					//休息结束时间
					String restEndTime = startDate + " " + rule.getRestEndTime();
					//下班时间
					String clockOffTime = startDate + " " + rule.getClockOffTime();
					//请假开始时间
					startDateTime = startDateTime.compareTo(clockOnTime) < 0 ? clockOnTime : startDateTime;
					//请假第一天结束时间
					String firstEndDateTime = clockOffTime;
					
					long start = DateTimeUtil.str2Date(startDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long end = DateTimeUtil.str2Date(firstEndDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long restStart = DateTimeUtil.str2Date(restStartTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long restEnd = DateTimeUtil.str2Date(restEndTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					
					if (startDateTime.compareTo(restStartTime) > 0 && startDateTime.compareTo(restEndTime) < 0){
						start = restEnd;
						hour = (double)(end - start) / IConstants.HOUR_MS;
					} else if (startDateTime.compareTo(restEndTime) >= 0){
						hour = (double)(end - start) / IConstants.HOUR_MS;
					} else {
						hour = (double)(end - start - (restEnd - restStart)) / IConstants.HOUR_MS;
					}
				} else if (StringUtils.equals(day, endDate)){
					//上班时间
					String clockOnTime = endDate + " " + rule.getClockOnTime();
					//休息开始时间
					String restStartTime = endDate + " " + rule.getRestStartTime();
					//休息结束时间
					String restEndTime = endDate + " " + rule.getRestEndTime();
					//下班时间
					String clockOffTime = endDate + " " + rule.getClockOffTime();
					//请假开始时间
					String lastStartDateTime = clockOnTime;
					//请假最后一天结束时间
					endDateTime = endDateTime.compareTo(clockOffTime) > 0 ? clockOffTime : endDateTime;
					
					long start = DateTimeUtil.str2Date(lastStartDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long end = DateTimeUtil.str2Date(endDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long restStart = DateTimeUtil.str2Date(restStartTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long restEnd = DateTimeUtil.str2Date(restEndTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					
					if (endDateTime.compareTo(restStartTime) > 0 && endDateTime.compareTo(restEndTime) < 0){
						end = restStart;
						hour += (double)(end - start) / IConstants.HOUR_MS;
					} else if(endDateTime.compareTo(restStartTime) <= 0){
						hour += (double)(end - start) / IConstants.HOUR_MS;
					} else {
						hour += (double)(end - start - (restEnd - restStart)) / IConstants.HOUR_MS;
					}
				} else {
					
					hour += getHour(day, rule);
				}
				
				
			}
		}
		
		
		
		System.out.println(hour);
		return hour;
	}
	
	
	private double getHour(String date, AttendanceRule rule){
		//上班时间
		String clockOnTime = date + " " + rule.getClockOnTime();
		//休息开始时间
		String restStartTime = date + " " + rule.getRestStartTime();
		//休息结束时间
		String restEndTime = date + " " + rule.getRestEndTime();
		//下班时间
		String clockOffTime = date + " " + rule.getClockOffTime();
		
		long start = DateTimeUtil.str2Date(clockOnTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
		long end = DateTimeUtil.str2Date(clockOffTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
		long restStart = DateTimeUtil.str2Date(restStartTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
		long restEnd = DateTimeUtil.str2Date(restEndTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
		
		return (double)(end - start - (restEnd - restStart)) / IConstants.HOUR_MS;
	}
	
}
