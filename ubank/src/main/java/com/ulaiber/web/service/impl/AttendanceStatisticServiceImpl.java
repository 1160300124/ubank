package com.ulaiber.web.service.impl;

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
import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.dao.AttendanceStatisticDao;
import com.ulaiber.web.model.AttendanceRule;
import com.ulaiber.web.model.AttendanceStatistic;
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
		return dao.getStatisticsByCond(params);
	}

	@Override
	public int getCountBycond(Map<String, Object> params) {
		return dao.getCountBycond(params);
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
		//指定月份应工作天数
		int count = 0;
		//节假日
		if (rule.getHolidayFlag() == 0){
			Holiday holiday = ruleDao.getHolidaysByYear(month.split("-")[0]);
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
		List<String> days = DateTimeUtil.getDaysFromMonth(month);
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
