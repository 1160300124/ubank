package com.ulaiber.web.service.impl;

import java.math.BigDecimal;
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
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.dao.UserDao;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.Holiday;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.Attendance;
import com.ulaiber.web.model.attendance.AttendancePatchClock;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.HttpsUtil;
import com.ulaiber.web.utils.MathUtil;

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
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private LeaveDao leaveDao;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public ResultInfo save(Attendance att, String device, String location, boolean isOutClock, String remark, AttendanceRule rule) {
		ResultInfo info = new ResultInfo();

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String datetime = "2017-09-20 01:10";
		String today = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		String clockOnTime= today + " " + rule.getClockOnTime();
		String clockOffTime = today + " " + rule.getClockOffTime();
		String dateBegin = today + " " + rule.getClockOnStartTime();
		String dateEnd = today + " " + rule.getClockOffEndTime();
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
			if (time.compareTo(rule.getClockOffEndTime()) <= 0){
				today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
				dateBegin = DateTimeUtil.getfutureTime(dateBegin, -1, 0, 0);
				clockOnTime = DateTimeUtil.getfutureTime(clockOnTime, -1, 0, 0);
				clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, -1, 0, 0);
			} else {
				dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
				if (rule.getClockOnTime().compareTo(rule.getClockOffTime()) > 0){
					clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 1, 0, 0);
				}
			}
		}
		
		if (isRestDay(today, rule)){
			logger.info("休息日不用打卡："+ today);
			info.setCode(IConstants.QT_REST_DAY);
			info.setMessage("休息日不用打卡："+ today);
			return info;
		}
		
		att.setClockDate(today);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", att.getUserId());
		params.put("dateBegin", today);
		params.put("dateEnd", today);
		//查询该用户当天的打卡记录
		List<Attendance> records = dao.getRecordsByDateAndUserId(params);
		
		//是否开启弹性时间
		if (rule.getFlexibleFlag() == 0){
			if (records == null || records.size() == 0){
				clockOnTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, rule.getFlexibleTime());
			} else {
				//获取当前时间>上班时间的分钟数
				int minute = records.get(0).getClockOnDateTime() == null ? 0 : DateTimeUtil.getminute(clockOnTime, records.get(0).getClockOnDateTime());
				//下班是否顺延
				if (rule.getPostponeFlag() == 0){
					//在弹性上班时间内打卡，下班时间才顺延
					if (minute > 0 && minute <= rule.getFlexibleTime()){
						clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 0, 0, minute);
					}
				}
			}
		}
		
		//查询当天是否有审批通过的请假记录
		LeaveRecord leaveRecord = leaveDao.getLeaveRecordByUserIdAndDate(att.getUserId(), today);
		
		//0:不销假打卡(上班和下班时间根据请假时间有变动)  1:销假打卡(走正常打卡流程)
		if (StringUtils.equals(att.getRevokeType(), "0")){
			if (leaveRecord != null){
				String startDay = leaveRecord.getStartDate().split(" ")[0];
				String endDay = leaveRecord.getEndDate().split(" ")[0];
				if (startDay.compareTo(endDay) == 0 || startDay.compareTo(today) == 0 || endDay.compareTo(today) == 0){
					if (rule.getRestFlag() == 0){
						String restStartTime = today + " " + rule.getRestStartTime();
						String restEndTime = today + " " + rule.getRestEndTime();
						//上午请假
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
							if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
								clockOnTime = restEndTime;
							}
							if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
						}
						//下午请假
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
							if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
								clockOffTime = restStartTime;
							}
							if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
						}
					} else {
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							clockOnTime = leaveRecord.getEndDate();
						}
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							clockOffTime = leaveRecord.getStartDate();
						}
					}
				} 
			}
		}
		
		//销假打卡时算出销假额时长
		if(StringUtils.equals(att.getRevokeType(), "1")){
			double revokeTime = 0;
			if (leaveRecord != null){
				if (leaveRecord.getStartDate().contains(today) && leaveRecord.getEndDate().contains(today)){
					revokeTime = getHoursByDate(leaveRecord.getStartDate(), leaveRecord.getEndDate(), rule);
				} 
				else if (leaveRecord.getStartDate().contains(today)){
					revokeTime = getHoursByDate(leaveRecord.getStartDate(), clockOffTime, rule);
				} 
				else if (leaveRecord.getEndDate().contains(today)){
					revokeTime = getHoursByDate(clockOnTime, leaveRecord.getEndDate(), rule);
				}
				else if (today.compareTo(leaveRecord.getStartDate()) > 0 && today.compareTo(leaveRecord.getEndDate()) < 0){
					revokeTime = getHoursByDate(clockOnTime, clockOffTime, rule);
				}
			}
			if (records == null || records.size() == 0){
				if (revokeTime > 0){
					leaveDao.updateRealLeaveTime(att.getUserId(), revokeTime, today);
				}
			} else {
				//数据库的打卡记录部位销假打卡时，避免同一天重复计算销假时长
				if (!StringUtils.equals(records.get(0).getRevokeType(), "1")){
					if (revokeTime > 0){
						leaveDao.updateRealLeaveTime(att.getUserId(), revokeTime, today);
					}
				}
			}
				
		}
		
		return clock(att, device, location, isOutClock, remark, records, datetime, today, clockOnTime, clockOffTime, dateBegin, dateEnd, rule);
		
	}
	
	/**
	 * 打卡
	 * @param att  要保存的考勤记录对象
	 * @param device 打卡设备号
	 * @param location  打卡位置
	 * @param isOutClock 是否外勤卡
	 * @param remark     外勤卡备注
	 * @param records    数据库的打卡记录
	 * @param datetime   打卡时间  yyyy-MM-dd HH:mm
	 * @param today      当天时间 yyyy-MM-dd
	 * @param clockOnTime 上班时间
	 * @param clockOffTime 下班时间
	 * @param dateBegin  最早上班时间
	 * @param dateEnd    最晚下班时间
	 * @param rule       考勤规则
	 * @return ResultInfo
	 */
	private ResultInfo clock(Attendance att, String device, String location, boolean isOutClock, String remark, List<Attendance> records, String datetime,
			String today, String clockOnTime, String clockOffTime, String dateBegin, String dateEnd, AttendanceRule rule){
		ResultInfo info = new ResultInfo();
		//新增，更新标志，默认失败
		boolean flag = false;
		// clockOnStatus：0正常  1迟到  3外勤   clockOffStatus： 0正常  2早退  3外勤
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
				att.setClockOffStatus(isOutClock ? "3" : "0");
				att.setClockOffRemark(isOutClock ? remark : null);
			}
			//当前时间大于最早上班打卡时间且小于上班打卡时间----正常打卡
			else if (datetime.compareTo(dateBegin) >= 0 && datetime.compareTo(clockOnTime) <= 0){
				att.setClockOnDateTime(datetime);
				att.setClockOnDevice(device);
				att.setClockOnLocation(location);
				att.setClockOnStatus(isOutClock ? "3" : "0");
				att.setClockOnRemark(isOutClock ? remark : null);
			}
			//当前时间大于上班打卡时间且小于下班打卡时间----迟到
			else if (datetime.compareTo(clockOnTime) > 0 && datetime.compareTo(clockOffTime) < 0){
				att.setClockOnDateTime(datetime);
				att.setClockOnDevice(device);
				att.setClockOnLocation(location);
				att.setClockOnStatus(isOutClock ? "3" : "1");
				att.setClockOnRemark(isOutClock ? remark : null);
			} 
			
			flag = dao.save(att) > 0;
			
		} else {
			//外勤打卡过滤掉频繁打卡限制
			if (!isOutClock){
				//当前时间等于上次打卡时间
				if (StringUtils.equals(datetime, records.get(0).getClockOnDateTime())
						|| StringUtils.equals(datetime, records.get(0).getClockOffDateTime())){
					logger.error("歇一会嘛，不要太频繁打卡。");
					info.setCode(IConstants.QT_CANNOT_CLOCK_FREQUENTLY);
					info.setMessage("歇一会嘛，不要太频繁打卡。");
					return info;
				}
			}
			
			att.setClockOffDateTime(datetime);
			att.setClockOffDevice(device);
			att.setClockOffLocation(location);
			att.setClockOffStatus("0");
			att.setClockOffRemark(isOutClock ? remark : null);
			//当前时间晚于最晚下班打卡时间
			if (datetime.compareTo(dateEnd) > 0){
				logger.error("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF);
				info.setMessage("最晚下班打卡时间为 " + rule.getClockOffEndTime() + ",您已经错过下班打卡时间。");
				return info;
			}
			if (datetime.compareTo(clockOnTime) < 0){
				logger.error("上班时间为 " + clockOnTime.split(" ")[1] + ",请上班后再来打卡。");
				info.setCode(IConstants.QT_CANNOT_CLOCK_OFF_BEFORE_CLOCK_ON);
				info.setMessage("上班时间为 " + clockOnTime.split(" ")[1] + ",请上班后再来打卡。");
				return info;
			}
			
			String clockOffBegin = null == records.get(0).getClockOnDateTime() ? clockOnTime 
					: clockOnTime.compareTo(records.get(0).getClockOnDateTime()) > 0 ? clockOnTime : records.get(0).getClockOnDateTime();
			//当前时间大于下班打卡时间&&小于最晚下班打卡时间
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <= 0 ){
				att.setClockOffStatus(isOutClock ? "3" : "0");
			}
			//当前时间大于上次打卡时间小于下班打卡时间----早退
			else if (datetime.compareTo(clockOffBegin) >= 0 && datetime.compareTo(clockOffTime) < 0){
				att.setClockOffStatus(isOutClock ? "3" : "2");
			}
			
			flag = dao.updateClockOffInfo(att);
		}
		
		if (flag){
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("新增或更新成功。");
			Map<String, Object> data = new HashMap<String, Object>();
			if (StringUtils.equals(att.getRevokeType(), "1")){
				data.put("clockOnTime", rule.getClockOnTime());
				data.put("clockOffTime", rule.getClockOffTime());
			} else {
				data.put("clockOnTime", "");
				data.put("clockOffTime", "");
			}
			att.setDept(null);
			att.setCompany(null);
			data.put("record", att);
			info.setData(data);
		} else{
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("新增或更新失败。");
			logger.error("新增或更新失败。");
		}
		return info;
	}
	
	@Override
	public boolean isRestDay(String day, AttendanceRule rule){
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
		return isRestDay;
	}

	@Override
	public List<Attendance> getRecordsByCond(Map<String, Object> params) {

		return dao.getRecordsByCond(params);
	}

	@Override
	public ResultInfo refreshLocation(String longitude, String latitude, AttendanceRule rule) {
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
			//打卡规则里设置的打卡距离
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
				logger.info("当前坐标离公司" + minDistance + "m，距离打卡范围还有" + (minDistance - distance) + "m");
				retInfo.setCode(IConstants.QT_N0T_IN_BOUNDS);
				retInfo.setMessage("当前坐标离公司" + minDistance + "m，距离打卡范围还有" + (minDistance - distance) + "m");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("distance", minDistance - distance);
				retInfo.setData(map);
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
	public List<Attendance> getRecordsByDateAndUserId(String dateBegin, String dateEnd, long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateBegin", dateBegin);
		params.put("dateEnd", dateEnd);
		params.put("userId", userId);
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
	public Map<String, Object> getRecordsByMonthAndUserId(String month, long userId, AttendanceRule rule) {
		//当前月份
		String currentMonth = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MONTHTIME);
		if (StringUtils.isEmpty(month)){
			month = currentMonth;
		}
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
		params.put("userId", userId);
		List<Attendance> list = dao.getRecordsByDateAndUserId(params);
		
		//key为打卡日期，value为打卡记录对象
		Map<String, Object> recordMap = new HashMap<String, Object>();
		for (Attendance att : list){
			recordMap.put(att.getClockDate(), att);
		}
		
		Map<String, Object> records = new LinkedHashMap<String, Object>();
		
		//迟到次数
		int laterCount = 0;
		//早退次数
		int leaveEarlyCount = 0;
		//未打卡次数
		int noClockCount = 0;
		
		Double totalTime = leaveDao.getTotalTimeByUserId(userId, month);
		double leaveDay = 0;
		if (totalTime != null){
			String newDay = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
			//每天工作小时数
			double workHours = DateTimeUtil.gethour(newDay + " " + rule.getClockOnTime(), newDay + " " + rule.getClockOffTime());
			if (rule.getRestFlag() == 0){
				double restHours = DateTimeUtil.gethour(newDay + " " + rule.getRestEndTime(), newDay + " " + rule.getRestStartTime());
				workHours = MathUtil.formatDouble(MathUtil.sub(workHours, restHours), 1);
			}
			leaveDay = new BigDecimal(MathUtil.div(totalTime, workHours)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		//当天
		String currentDay = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
		//0 正常   1异常(迟到，早退) 2未打卡  3休息日  4请假
		int type = 3;
		//获取指定月份的所有天数集合(如果是当月则只返回当前日期之前的天数)
		List<String> days = DateTimeUtil.getDaysFromMonth(month, true);
		for (String day : days){
//			records.put(day, type);
			//指定月份不能大于当前月份
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
			
			//休息日
			if (isRestDay){
				records.put(day, 3);
				continue;
			}
			
			//比当天大且不是休息日
			if (day.compareTo(currentDay) > 0){
				continue;
			}

			//查询当天是否有审批通过的请假记录,如果有请假记录则不为旷工
			LeaveRecord leaveRecord = leaveDao.getLeaveRecordByUserIdAndDate(userId, day);
			Attendance att = (Attendance) recordMap.get(day);
			String clockOnTime = day + " " + rule.getClockOnTime();
			String clockOffTime = day + " " + rule.getClockOffTime();
			//没有考勤记录为旷工
			if (att == null ){
				if (leaveRecord == null){
					records.put(day, 2);
					noClockCount ++;
				} else {
					if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0 && leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
						records.put(day, 4);
					} else {
						records.put(day, 2);
						noClockCount ++;
					}
				}
			} else {
				//上班，下班都没打卡
				if (StringUtils.isEmpty(att.getClockOnDateTime()) && StringUtils.isEmpty(att.getClockOffDateTime())){
					if (leaveRecord == null){
						records.put(day, 2);
						noClockCount ++;
					} else {
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0 && leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							records.put(day, 4);
						}
					}
					continue;
				}
				//打卡状态 0：正常  1：迟到  2：早退  3：外勤
				if (StringUtils.equals(att.getClockOnStatus(), "0") && StringUtils.equals(att.getClockOffStatus(), "0")
						|| StringUtils.equals(att.getClockOnStatus(), "0") && StringUtils.equals(att.getClockOffStatus(), "3")
						|| StringUtils.equals(att.getClockOnStatus(), "3") && StringUtils.equals(att.getClockOffStatus(), "0")
						|| StringUtils.equals(att.getClockOnStatus(), "3") && StringUtils.equals(att.getClockOffStatus(), "3")){
					records.put(day, 0);
					//revokeType 0：不销假打卡 1：销假打卡
					if (StringUtils.equals(att.getRevokeType(), "0")){
						records.put(day, 4);
					}
				}
				if (StringUtils.equals(att.getClockOnStatus(), "1")){
					records.put(day, 1);
					laterCount ++;
				}
				if (StringUtils.equals(att.getClockOffStatus(), "2")){
					records.put(day, 1);
					leaveEarlyCount ++;
				}
				if (StringUtils.isEmpty(att.getClockOnDateTime()) || StringUtils.isEmpty(att.getClockOffDateTime())){
					records.put(day, 2);
					noClockCount ++;
				}
			}
			
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("records", records);
		data.put("laterCount", laterCount);
		data.put("leaveEarlyCount", leaveEarlyCount);
		data.put("noClockCount", noClockCount);
		data.put("leaveDay", leaveDay);

		return data;
	}

	@Override
	public double getHoursByDate(String startDateTime, String endDateTime, AttendanceRule rule) {
		//开始日期  yyyy-MM-dd
		String startDate = startDateTime.split(" ")[0];
		//结束日期  yyyy-MM-dd
		String endDate = endDateTime.split(" ")[0];
		double hour = 0;
		List<String> days = DateTimeUtil.getDaysFromDate(startDate, endDate);
		if (days.size() == 1){
			//上班时间
			String clockOnTime = startDate + " " + rule.getClockOnTime();
			//下班时间
			String clockOffTime = endDate + " " + rule.getClockOffTime();
			//请假开始时间
			startDateTime = startDateTime.compareTo(clockOnTime) < 0 ? clockOnTime : startDateTime;
			//请假结束时间
			endDateTime = endDateTime.compareTo(clockOffTime) > 0 ? clockOffTime : endDateTime;
			
			long start = DateTimeUtil.str2Date(startDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			long end = DateTimeUtil.str2Date(endDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			
			//是否设置休息时间段
			if (rule.getRestFlag() == 0){
				//休息开始时间
				String restStartTime = startDate + " " + rule.getRestStartTime();
				//休息结束时间
				String restEndTime = startDate + " " + rule.getRestEndTime();
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
			} else {
				hour = (double)(end - start) / IConstants.HOUR_MS;
			}
			
		} else if (days.size() >= 2) {
			for (String day : days){
				if (isRestDay(day, rule)){
					continue;
				}
				
				if (StringUtils.equals(day, startDate)){
					//上班时间
					String clockOnTime = startDate + " " + rule.getClockOnTime();
					//下班时间
					String clockOffTime = startDate + " " + rule.getClockOffTime();
					//如果请假开始时间大于下班时间
					if (startDateTime.compareTo(clockOffTime) >= 0){
						continue;
					}
					//请假开始时间
					startDateTime = startDateTime.compareTo(clockOnTime) < 0 ? clockOnTime : startDateTime;
					//请假第一天结束时间
					String firstEndDateTime = clockOffTime;
					
					long start = DateTimeUtil.str2Date(startDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long end = DateTimeUtil.str2Date(firstEndDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					
					//是否设置休息时间段
					if (rule.getRestFlag() == 0){
						//休息开始时间
						String restStartTime = startDate + " " + rule.getRestStartTime();
						//休息结束时间
						String restEndTime = startDate + " " + rule.getRestEndTime();
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
					} else {
						hour = (double)(end - start) / IConstants.HOUR_MS;
					}
					
				} else if (StringUtils.equals(day, endDate)){
					//上班时间
					String clockOnTime = endDate + " " + rule.getClockOnTime();
					//如果请假结束时间小于上班时间
					if (endDateTime.compareTo(clockOnTime) <= 0){
						continue;
					}
					//下班时间
					String clockOffTime = endDate + " " + rule.getClockOffTime();
					//请假开始时间
					String lastStartDateTime = clockOnTime;
					//请假最后一天结束时间
					endDateTime = endDateTime.compareTo(clockOffTime) > 0 ? clockOffTime : endDateTime;
					
					long start = DateTimeUtil.str2Date(lastStartDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					long end = DateTimeUtil.str2Date(endDateTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
					
					//是否设置休息时间段
					if (rule.getRestFlag() == 0){
						//休息开始时间
						String restStartTime = endDate + " " + rule.getRestStartTime();
						//休息结束时间
						String restEndTime = endDate + " " + rule.getRestEndTime();
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
						hour += (double)(end - start) / IConstants.HOUR_MS;
					}
					
				} else {
					
					hour += getHour(day, rule);
				}
				
			}
		}
		
		//保留一位小数，四舍五入
		return new BigDecimal(hour).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private double getHour(String date, AttendanceRule rule){
		//上班时间
		String clockOnTime = date + " " + rule.getClockOnTime();
		//下班时间
		String clockOffTime = date + " " + rule.getClockOffTime();
		
		long start = DateTimeUtil.str2Date(clockOnTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
		long end = DateTimeUtil.str2Date(clockOffTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
		
		//是否设置休息时间段
		if (rule.getRestFlag() == 0){
			//休息开始时间
			String restStartTime = date + " " + rule.getRestStartTime();
			//休息结束时间
			String restEndTime = date + " " + rule.getRestEndTime();
			long restStart = DateTimeUtil.str2Date(restStartTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			long restEnd = DateTimeUtil.str2Date(restEndTime, DateTimeUtil.DATE_FORMAT_MINUTETIME).getTime();
			return (double)(end - start - (restEnd - restStart)) / IConstants.HOUR_MS;
			
		} else {
			return (double)(end - start) / IConstants.HOUR_MS;
		}
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean patchClock(AttendancePatchClock patchClock) { 
		User user = userDao.getUserById(patchClock.getUserId());
		if (null == user){
			logger.error("用户  " + patchClock.getUserId() + " 不存在。");
			return false;
		}
		AttendanceRule rule = ruleDao.getRuleByUserId(patchClock.getUserId());
		if (null == rule){
			logger.error("用户 {" + patchClock.getUserId() + "}没有设置考勤规则，请先设置。");
			return false;
		}
		
		boolean flag = false;
		//补卡审批状态 0：审批中  1：已通过  2：未通过  3：已取消
		if (StringUtils.equals(patchClock.getPatchClockStatus(), "0")){
			flag = updatePatchClockStatus(user, patchClock.getPatchClockDate(), patchClock.getPatchClockType(), patchClock.getPatchClockStatus());
			if (flag){
				logger.info("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "补卡状态更改为审批中。");
			} else {
				logger.error("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "更新补卡状态为审批中失败。");
			}
			
		} else if (StringUtils.equals(patchClock.getPatchClockStatus(), "1")){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", user.getId());
			params.put("clockDate", patchClock.getPatchClockDate());
			params.put("patchClockType", patchClock.getPatchClockType());
			params.put("patchClockStatus", patchClock.getPatchClockStatus());
			//补卡类型 0：全天补卡  1：上班补卡  2：下班补卡
			if (patchClock.getPatchClockType() == 0){
				String clockOnDate = patchClock.getPatchClockOnTime().split(" ")[0];
				String clockOffDate = patchClock.getPatchClockOffTime().split(" ")[0];
				params.put("patchClockOnTime", patchClock.getPatchClockOnTime());
				params.put("patchClockOffTime", patchClock.getPatchClockOffTime());
				params.put("patchClockOnStatus", "0");
				params.put("patchClockOffStatus", "0");
				if (patchClock.getPatchClockOnTime().compareTo(clockOnDate + " " + rule.getClockOnTime()) > 0){
					params.put("patchClockOnStatus", "1");
				}
				if (patchClock.getPatchClockOffTime().compareTo(clockOffDate + " " + rule.getClockOffTime()) < 0){
					params.put("patchClockOffStatus", "2");
				}
			}
			if (patchClock.getPatchClockType() == 1){
				String clockOnDate = patchClock.getPatchClockOnTime().split(" ")[0];
				params.put("patchClockOnTime", patchClock.getPatchClockOnTime());
				params.put("patchClockOnStatus", "0");
				if (patchClock.getPatchClockOnTime().compareTo(clockOnDate + " " + rule.getClockOnTime()) > 0){
					params.put("patchClockOnStatus", "1");
				}
			}
			if(patchClock.getPatchClockType() == 2){
				String clockOffDate = patchClock.getPatchClockOffTime().split(" ")[0];
				params.put("patchClockOffTime", patchClock.getPatchClockOffTime());
				params.put("patchClockOffStatus", "0");
				if (patchClock.getPatchClockOffTime().compareTo(clockOffDate + " " + rule.getClockOffTime()) < 0){
					params.put("patchClockOffStatus", "2");
				}
			}
			
			flag =  dao.patchClock(params);
			if (flag){
				logger.info("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "补卡成功。");
			} else {
				logger.error("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "补卡失败。");
			}
			
		} else if (StringUtils.equals(patchClock.getPatchClockStatus(), "2")){
			flag = updatePatchClockStatus(user, patchClock.getPatchClockDate(), patchClock.getPatchClockType(), patchClock.getPatchClockStatus());
			if (flag){
				logger.info("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "补卡状态更改为未通过。");
			} else {
				logger.error("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "更新补卡状态为未通过失败。");
			}
		} else if (StringUtils.equals(patchClock.getPatchClockStatus(), "3")){
			flag = updatePatchClockStatus(user, patchClock.getPatchClockDate(), patchClock.getPatchClockType(), patchClock.getPatchClockStatus());
			if (flag){
				logger.info("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "补卡状态更改为已取消。");
			} else {
				logger.error("用户 " + patchClock.getUserId() + " " + patchClock.getPatchClockDate() + "更新补卡状态为已取消失败。");
			}
		}
		
		return flag;
	}

	private boolean updatePatchClockStatus(User user, String clockDate, int patchClockType, String patchClockStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", user.getId());
		params.put("dateBegin", clockDate);
		params.put("dateEnd", clockDate);
		List<Attendance> records = dao.getRecordsByDateAndUserId(params);
		if (records.size() > 0){
			params.put("clockDate", clockDate);
			params.put("patchClockType", patchClockType);
			params.put("patchClockStatus", patchClockStatus);
			return dao.updatePatchClockStatus(params);
		} else {
			if (StringUtils.equals(patchClockStatus, "0")){
				Attendance att = new Attendance();
				att.setClockDate(clockDate);
				att.setPatchClockType(patchClockType + "");
				att.setPatchClockStatus(patchClockStatus);
				att.setUserId(user.getId());
				att.setUserName(user.getUserName());
				
				Company com = new Company();
				com.setCompanyNumber(user.getCompanyId());
				att.setCompany(com);
				
				Departments dept = new Departments();
				dept.setDept_number(user.getDept_number());
				att.setDept(dept);
				return dao.save(att) > 0;
			}
		}
		
		return false;
	}

	@Override
	public int getAbnormalCountByUserId(long userId) {
		//当前月份
		String currentMonth = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MONTHTIME);
		AttendanceRule rule = ruleDao.getRuleByUserId(userId);
		if (null == rule){
			logger.error("用户 {" + userId + "}没有设置考勤规则，请先设置。");
			return 0;
		}
		Map<String, Object> map = getRecordsByMonthAndUserId(currentMonth, userId, rule);
		int count = 0;
		count += (Integer)map.get("laterCount");
		count += (Integer)map.get("leaveEarlyCount");
		count += (Integer)map.get("noClockCount");

		return count;
	}
	
}
