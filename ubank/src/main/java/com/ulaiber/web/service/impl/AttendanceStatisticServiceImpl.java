package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.AttendanceDao;
import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.dao.AttendanceStatisticDao;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.AttendanceStatistic;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.Holiday;
import com.ulaiber.web.service.AttendanceStatisticService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 考勤统计业务逻辑实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年9月7日 下午5:15:53
 * @version 1.0 
 * @since 
 */
@Service
public class AttendanceStatisticServiceImpl extends BaseService implements AttendanceStatisticService {
	
	@Resource
	private AttendanceStatisticDao dao;
	
	@Resource
	private AttendanceRuleDao ruleDao;
	
	@Resource
	private AttendanceDao attDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(AttendanceStatistic statistic) {
		return dao.save(statistic) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(AttendanceStatistic statistic) {
		return dao.update(statistic);
	}

	@Override
	public List<AttendanceStatistic> getStatisticsByCond(Map<String, Object> params) {
		List<AttendanceStatistic> statistics = new ArrayList<AttendanceStatistic>();
		
//		List<AttendanceRule> rules = ruleDao.getRulesByCompanyId((Integer)params.get("company_num"));
//		String clockOnStartTime = rules.get(0).getClockOnStartTime();
//		String clockOffEndTime = rules.get(0).getClockOffEndTime();
//		for (AttendanceRule rule : rules){
//			if (clockOnStartTime.compareTo(rule.getClockOnStartTime()) > 0){
//				clockOnStartTime = rule.getClockOnStartTime();
//			}
//			if (clockOffEndTime.compareTo(rule.getClockOffEndTime()) < 0){
//				clockOffEndTime = rule.getClockOffEndTime();
//			}
//		}
//		String dateBegin = params.get("start_date").toString() + " " + clockOnStartTime;
//		String dateEnd = params.get("end_date").toString() + " " + clockOffEndTime;
//		if (clockOffEndTime.compareTo(clockOnStartTime) < 0){
//			dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
//		}
//		
//		params.put("dateBegin", dateBegin);
//		params.put("dateEnd", dateEnd);
		//按用户id, name, clock_on_status, clock_off_status分组查询考勤记录
		List<Map<String, Object>> list = attDao.getStatistis(params);
		
		//打卡记录里获取不重名的打卡用户
		List<Map<String, Object>> users = attDao.getUsersFromRecords(params);
		for (Map<String, Object> user : users){
			AttendanceStatistic statistic = new AttendanceStatistic();
			statistic.setUserId((Long)user.get("user_id"));
			statistic.setUserName(user.get("user_name").toString());
			Departments dept = new Departments();
			dept.setDept_number(user.get("dept_number").toString());
			dept.setDeptName(user.get("deptName").toString());
			statistic.setDept(dept);
			Company company = new Company();
			company.setCompanyNumber((Integer)user.get("companyNumber"));
			company.setName(user.get("name").toString());
			statistic.setCompany(company);
			int WorkdayCount = getWorkdayCountForDate((Long)user.get("user_id"), params.get("start_date").toString(), params.get("end_date").toString());
			statistic.setWorkdaysCount(WorkdayCount);
			int normalClockOnCount = 0;
			int normalClockOffCount = 0;
			int laterCount = 0;
			int leaveEarlyCount = 0;
			for (Map<String, Object> map : list){
				if (!StringUtils.equals(user.get("user_id").toString(), map.get("user_id").toString())){
					continue;
				}
				String clockOnStatus = null == map.get("clock_on_status") ? "" : map.get("clock_on_status").toString(); 
				String clockOffStatus = null == map.get("clock_off_status") ? "" : map.get("clock_off_status").toString();
				//正常上班
				if (StringUtils.equals(clockOnStatus, "0")){
					normalClockOnCount += Integer.parseInt(map.get("count").toString());
				}
				//迟到
				if (StringUtils.equals(clockOnStatus, "1")){
					laterCount += Integer.parseInt(map.get("count").toString());
				}
				//正常下班
				if (StringUtils.equals(clockOffStatus, "0")){
					normalClockOffCount += Integer.parseInt(map.get("count").toString());
				}
				//早退
				if (StringUtils.equals(clockOffStatus, "1")){
					leaveEarlyCount += Integer.parseInt(map.get("count").toString());
				}
			}
			statistic.setNormalClockOnCount(normalClockOnCount);
			statistic.setNormalClockOffCount(normalClockOffCount);
			statistic.setLaterCount(laterCount);
			statistic.setLeaveEarlyCount(leaveEarlyCount);
			//上班未打卡
			statistic.setNoClockOnCount(WorkdayCount - statistic.getNormalClockOnCount() - laterCount);
			//下班未打卡
			statistic.setNoClockOffCount(WorkdayCount - statistic.getNormalClockOffCount() - leaveEarlyCount);
			statistics.add(statistic);
		}
		return statistics;
	}

	@Override
	public int getCountBycond(Map<String, Object> params) {
		return attDao.getStatistisCount(params);
	}

	@Override
	public int getWorkdayCountForDate(long userId, String dateBegin, String dateEnd) {
		AttendanceRule rule = ruleDao.getRuleByUserId(userId);
		//获取指定时间段的天数
		List<String> days = DateTimeUtil.getDaysFromDate(dateBegin, dateEnd);
			
		return getWorkdayCount(rule, dateBegin, days);
	}
	
	@Override
	public int getWorkdayCountForMonth(long userId, String month) {
		AttendanceRule rule = ruleDao.getRuleByUserId(userId);
		//指定月份为空则获取当前月份
		if (StringUtils.isEmpty(month)){
			month = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MONTHTIME);
		}
		//获取指定月份的天数
		List<String> days = DateTimeUtil.getDaysFromMonth(month);
			
		return getWorkdayCount(rule, month, days);
	}
	
	private int getWorkdayCount(AttendanceRule rule, String date, List<String> days){
		//指定月份应工作天数
		int count = 0;
		//节假日
		if (rule.getHolidayFlag() == 0){
			Holiday holiday = ruleDao.getHolidaysByYear(date.split("-")[0]);
			if (holiday != null){
				List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
				List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
				for (String day : days){
					if (holidays.contains(day)){
						continue;
					}
					String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(day) + "");
					if (rule.getWorkday().contains(workday) || workdays.contains(day)){
						count ++;
					}
				} 
			}
		} else {
			for (String day : days){
				String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(day) + "");
				if (rule.getWorkday().contains(workday)){
					count ++;
				}
			}
		}
		
		return count;
	}
	
	
	public static void main(String[] args) {
		AttendanceRule rule = new AttendanceRule();
		rule.setHolidayFlag(0);
		rule.setWorkday("周一,周二,周三,周四,周五");
		String month = "2017-09";
		//指定月份为空则获取当前月份
		if (StringUtils.isEmpty(month)){
			month = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MONTHTIME);
		}
		List<String> days = DateTimeUtil.getDaysFromDate("2017-08-15", "2017-09-15");
		//指定月份应工作天数
		int count = 0;
		//节假日
		Holiday holiday = new Holiday();
		holiday.setHoliday("2017-01-27,2017-01-28,2017-01-29,2017-01-30,2017-01-31,2017-02-01,2017-02-02,2017-04-02,2017-04-03,2017-04-04,2017-04-29,2017-04-30,2017-05-01,2017-05-28,2017-05-29,2017-05-30,2017-10-01,2017-10-02,2017-10-03,2017-10-04,2017-10-05,2017-10-06,2017-10-07,2017-10-08");
		holiday.setWorkday("2017-01-22,2017-02-04,2017-04-01,2017-05-27,2017-09-30");
		if (rule.getHolidayFlag() == 0){
			if (holiday != null){
				List<String> holidays = Arrays.asList(holiday.getHoliday().split(","));
				List<String> workdays = Arrays.asList(holiday.getWorkday().split(","));
				for (String day : days){
					if (holidays.contains(day)){
						continue;
					}
					String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(day) + "");
					if (rule.getWorkday().contains(workday) || workdays.contains(day)){
						count ++;
					}
				} 
			}
		} else {
			for (String day : days){
				String workday = IConstants.WORK_DAY.get(DateTimeUtil.getWeekday(day) + "");
				if (rule.getWorkday().contains(workday)){
					count ++;
				}
			}
		}
		System.out.println(count);
	}

}
