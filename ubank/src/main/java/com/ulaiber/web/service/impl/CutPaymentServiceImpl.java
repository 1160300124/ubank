package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.AttendanceDao;
import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.dao.CutPaymentDao;
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.dao.SalaryRuleDao;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.Attendance;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.model.attendance.CutPayment;
import com.ulaiber.web.model.attendance.CutPaymentRule;
import com.ulaiber.web.model.salary.SalaryRule;
import com.ulaiber.web.service.AttendanceStatisticService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.CutPaymentService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.MathUtil;
import com.ulaiber.web.utils.ObjUtil;

/** 
 * 扣款业务逻辑处理
 *
 * @author  huangguoqing
 * @date 创建时间：2017年11月1日 上午11:57:20
 * @version 1.0 
 * @since 
 */
@Service
public class CutPaymentServiceImpl extends BaseService implements CutPaymentService {
	
	private static Logger logger = Logger.getLogger(CutPaymentServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AttendanceStatisticService statisticService;
	
	@Resource
	private AttendanceDao attDao;
	
	@Resource
	private AttendanceRuleDao ruleDao;
	
	@Resource
	private SalaryRuleDao salaryRuleDao;
	
	@Resource
	private CutPaymentDao dao;
	
	@Resource
	private LeaveDao leaveDao;
	
	@Override
	public Map<String, Object> getCutPaymentMessage(String companyId, String salaryMonth) {
		Map<String, Object> data = new HashMap<String, Object>();
		//扣款记录列表
		List<CutPayment> cutPayments = new ArrayList<CutPayment>();
		//扣款Map  key为用户ID,value为用户扣款总金额
		Map<Long, Object> cutMap = new HashMap<Long, Object>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("company_num", companyId);
		params.put("start_date", DateTimeUtil.getMonthBegin(salaryMonth));
		params.put("end_date", DateTimeUtil.getMonthEnd(salaryMonth));
		//获取考勤记录
		List<Attendance> list = attDao.getStatistis(params);
		//获取公司的工资规则
		SalaryRule salaryRule = salaryRuleDao.getSalaryRuleByCompanyId(companyId);
		//事假，病假，年假，调休，婚假，产假，其他
		String[] leaveCutRule = salaryRule.getLeaveCutPayment().split(",");
		//分组查询用户的请假扣款
		List<Map<String, Object>> leaveList = leaveDao.getTotalTimeByCompanyNumAndMonth(companyId, "0", salaryMonth);
		//分组查询用户的加班费
		List<Map<String, Object>> overTimeList = leaveDao.getTotalTimeByCompanyNumAndMonth(companyId, "1", salaryMonth);
		Map<Long, Double> overTimeMap = new HashMap<Long, Double>();
		for (Map<String, Object> map : overTimeList){
			overTimeMap.put(Long.parseLong(map.get("userId").toString()), (Double)map.get("totalTime"));
		}
		//无考勤规则人员ID集合
		List<Long> userIds = new ArrayList<Long>();
		//获取公司的所有用户
		List<User> users = userService.getUsersByComNum(companyId, null);
		for (User user : users){
			//考勤规则
			AttendanceRule attRule = ruleDao.getRuleByUserId(user.getId());
			if (null == attRule){
				userIds.add(user.getId());
				logger.error("用户 {" + user.getId() + "}没有设置考勤规则，请先设置。");
				continue;
			}
			
			//应工作天数
			List<String> workdays = statisticService.getWorkdaysForDate(attRule, DateTimeUtil.getMonthBegin(salaryMonth), DateTimeUtil.getMonthEnd(salaryMonth));
			if (workdays == null){
				logger.error("用户 {" + user.getId() + "}应工作天数为null。");
				continue;
			}
			//天工资
			double daySalaries = MathUtil.div(user.getSalaries(), workdays.size());
			double leaveCutPayment = 0;
			for (Map<String, Object> map : leaveList){
				if (user.getId() != Long.parseLong(map.get("userId").toString())){
					continue;
				}
				double cutPayment = MathUtil.formatDouble(MathUtil.mul(daySalaries, null == map.get("totalTime") ? 0 : (Double)map.get("totalTime")), 1);
				//请假类型. 0 年假，1 事假，2 病假，3 调休，4 婚假，5 产假 ，6 其他
				String leaveType = map.get("leaveType").toString();
				switch(leaveType){
					case "0" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[2]) / 100), 1);
						break;
					case "1" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[0]) / 100), 1);
						break;
					case "2" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[1]) / 100), 1);
						break;
					case "3" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[3]) / 100), 1);
						break;
					case "4" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[4]) / 100), 1);
						break;
					case "5" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[5]) / 100), 1);
						break;
					case "6" : 
						cutPayment =  MathUtil.formatDouble(MathUtil.mul(cutPayment, Double.parseDouble(leaveCutRule[6]) / 100), 1);
						break;
				}
				leaveCutPayment += cutPayment;
				
			}
			double overTimePayment = 0;
			//0:调休 1:发加班费
			if (salaryRule.getOvertimeFlag() == 1){
				overTimePayment = MathUtil.formatDouble(MathUtil.mul(daySalaries, null == overTimeMap.get(user.getId()) ? 0 : overTimeMap.get(user.getId())), 1);
			}
			if (attRule.getType() != 0){
				Map<String, Object> attCutMap = new HashMap<String, Object>();
				attCutMap.put("laterCutPayment", 0.0);
				attCutMap.put("leaveEarlyCutPayment", 0.0);
				attCutMap.put("forgetClockCutPayment", 0.0);
				attCutMap.put("noClockCutPayment", 0.0);
				attCutMap.put("totalCutAmount", 0.0);
				attCutMap.put("leaveCutPayment", leaveCutPayment);
				attCutMap.put("overTimePayment", overTimePayment);
				cutMap.put(user.getId(), attCutMap);
				logger.info("用户 {" + user.getId() + "}不参与考勤规则。");
				continue;
			}

			//扣款总金额
			double totalCutAmount = 0;
			//忘打卡扣款
			double forgetClockCutAmount = 0;
			//迟到扣款
			double laterCutAmount = 0;
			//早退扣款
			double leaveEarlyCutAmount = 0;
			//旷工扣款
			double noClockCutAmount = 0;
			//实际工作天数
			List<String> realWorkdays = new ArrayList<String>();
			//忘打卡次数
			int forgetClockCount = 0;
			//cutType扣款类型 0：迟到  1：早退  2：未打卡  3：旷1天工  4：旷半天工  5：请假
			for (Attendance att : list){
				//内层循环过滤掉id不同的数据 ||过滤掉上班，下班都没打卡或打卡日期不在应工作天之内的数据
				if (user.getId() != att.getUserId() || !workdays.contains(att.getClockDate())
						|| StringUtils.isEmpty(att.getClockOnDateTime()) && StringUtils.isEmpty(att.getClockOffDateTime())){
					continue;
				}
				if (StringUtils.isNotEmpty(att.getClockOnDateTime()) || StringUtils.isNotEmpty(att.getClockOffDateTime())){
					realWorkdays.add(att.getClockDate());
				}
				//上班或下班忘打卡
				if (StringUtils.isEmpty(att.getClockOnDateTime()) || StringUtils.isEmpty(att.getClockOffDateTime())){
					forgetClockCount ++;
					CutPayment cut = new CutPayment();
					cut.setUserId(user.getId());
					cut.setUserName(user.getUserName());
					Departments dept = new Departments();
					dept.setDept_number(user.getDept_number());
					dept.setDeptName(user.getDept_name());
					cut.setDept(dept);
					Company company = new Company();
					company.setCompanyNumber(Integer.parseInt(user.getCompanyNumber()));
					company.setName(user.getCom_name());
					cut.setCompany(company);
					cut.setCutDate(att.getClockDate());
					cut.setCutType("2");
					cut.setCutReason(att.getClockDate() + "：缺卡" + forgetClockCount + "次");
					double money = 0;
					if (forgetClockCount > salaryRule.getAllowForgetClockCount()){
						money = salaryRule.getForgetClockCutPayment();
						if (salaryRule.getForgetClockCutUnit() == 1){
							money = MathUtil.formatDouble(MathUtil.mul(money, daySalaries) , 1);
						}
					}
					cut.setCutMoney(money);
					forgetClockCutAmount += money;
					totalCutAmount += money;
					cutPayments.add(cut);
				}
				String clockOnStatus = StringUtils.isEmpty(att.getClockOnStatus()) ? "" : att.getClockOnStatus(); 
				String clockOffStatus = StringUtils.isEmpty(att.getClockOffStatus()) ? "" : att.getClockOffStatus(); 
				//迟到
				if (StringUtils.equals(clockOnStatus, "1")){
					//获取迟到分钟数
					int minute = DateTimeUtil.getminute(att.getClockOnDateTime(), att.getClockDate() + " " + attRule.getClockOnTime());
					double money = getCutPayment(salaryRule.getLaterCutPayment(), daySalaries, minute);
					CutPayment cut = new CutPayment();
					cut.setUserId(user.getId());
					cut.setUserName(user.getUserName());
					Departments dept = new Departments();
					dept.setDept_number(user.getDept_number());
					dept.setDeptName(user.getDept_name());
					cut.setDept(dept);
					Company company = new Company();
					company.setCompanyNumber(Integer.parseInt(user.getCompanyNumber()));
					company.setName(user.getCom_name());
					cut.setCompany(company);
					cut.setCutDate(att.getClockDate());
					cut.setCutType("0");
					cut.setCutReason(att.getClockDate() + "：迟到" + minute + "分钟");
					cut.setCutMoney(money);	
					laterCutAmount += money;
					totalCutAmount += money;
					cutPayments.add(cut);
				}
				//早退
				if (StringUtils.equals(clockOffStatus, "2")){
					//获取早退分钟数
					int minute = DateTimeUtil.getminute(att.getClockOffDateTime(), att.getClockDate() + " " + attRule.getClockOffTime());
					double money = getCutPayment(salaryRule.getLeaveEarlyCutPayment(), daySalaries, minute);
					CutPayment cut = new CutPayment();
					cut.setUserId(user.getId());
					cut.setUserName(user.getUserName());
					Departments dept = new Departments();
					dept.setDept_number(user.getDept_number());
					dept.setDeptName(user.getDept_name());
					cut.setDept(dept);
					Company company = new Company();
					company.setCompanyNumber(Integer.parseInt(user.getCompanyNumber()));
					company.setName(user.getCom_name());
					cut.setCompany(company);
					cut.setCutDate(att.getClockDate());
					cut.setCutType("1");
					cut.setCutReason(att.getClockDate() + "：早退" + minute + "分钟");
					cut.setCutMoney(money);
					leaveEarlyCutAmount += money;
					totalCutAmount += money;
					cutPayments.add(cut);
				}
			}

			//旷工
			List<String> noClockWorkdays = ObjUtil.getDiffrent(workdays, realWorkdays);
			//旷工1天扣款
			double money = salaryRule.getNoClockCutPayment();
			if (salaryRule.getNoClockCutUnit() == 1){
				money =  MathUtil.formatDouble(MathUtil.mul(money, daySalaries) , 1);
			}
			//旷工半天扣款
			double halfMoney = salaryRule.getHalf_noClockCutPayment();
			if (salaryRule.getHalf_noClockCutUnit() == 1){
				halfMoney = MathUtil.formatDouble(MathUtil.mul(halfMoney, daySalaries), 1);
			}
			for (String noClockDay : noClockWorkdays){
				//在入职日期之前不算旷工或离职日期之后不算旷工
				if (StringUtils.isNotEmpty(user.getEntryDate()) && noClockDay.compareTo(user.getEntryDate()) < 0 
						|| StringUtils.isNotEmpty(user.getLeaveDate()) && noClockDay.compareTo(user.getLeaveDate()) > 0){
					continue;
				}
				
				// 0:旷一天工   1：旷半天工
				int flag = 0;
				//查询当天是否有审批通过的请假记录,如果有请假记录则不为旷工
				List<LeaveRecord> leaveRecords = leaveDao.getLeaveRecordByUserIdAndDate(user.getId(), noClockDay);
				if (leaveRecords != null && leaveRecords.size() > 0){
					if (leaveRecords.size() == 1){
						LeaveRecord leaveRecord = leaveRecords.get(0);
						String startDay = leaveRecord.getStartDate();
						String endDay = leaveRecord.getEndDate();
						//请假在同一天
						if (StringUtils.equals(startDay, noClockDay) && StringUtils.equals(endDay, noClockDay)){
							//上半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
								flag = 1;
							}
							//下半天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								flag = 1;
							}
							//全天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								continue;
							}
							//请假开始时间为今天
						} else if (StringUtils.equals(startDay, noClockDay)){
							//全天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "0")){
								continue;
							}
							//下半天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "1")){
								flag = 1;
							}
							//请假结束时间为今天
						} else if (StringUtils.equals(endDay, noClockDay)){
							//上半天请假
							if (StringUtils.equals(leaveRecord.getEndType(), "0")){
								flag = 1;
							}
							//全天请假
							if (StringUtils.equals(leaveRecord.getEndType(), "1")){
								continue;
							}
							//今天在请假开始与结束之间
						} else if (noClockDay.compareTo(startDay) > 0 && noClockDay.compareTo(endDay) < 0){
							continue;
						}
					}
					if (leaveRecords.size() == 2){
						continue;
					}
				}
				CutPayment cut = new CutPayment();
				cut.setUserId(user.getId());
				cut.setUserName(user.getUserName());
				Departments dept = new Departments();
				dept.setDept_number(user.getDept_number());
				dept.setDeptName(user.getDept_name());
				cut.setDept(dept);
				Company company = new Company();
				company.setCompanyNumber(Integer.parseInt(user.getCompanyNumber()));
				company.setName(user.getCom_name());
				cut.setCompany(company);
				cut.setCutDate(noClockDay);
				if (flag == 0){
					cut.setCutType("3");
					cut.setCutReason(noClockDay + "：旷工1天");
					cut.setCutMoney(money);	
				} else if (flag == 1){
					cut.setCutType("4");
					cut.setCutReason(noClockDay + "：旷工半天");
					cut.setCutMoney(halfMoney);	
				}
				noClockCutAmount += money;
				totalCutAmount += money;
				cutPayments.add(cut);
			}
			
			Map<String, Object> attCutMap = new HashMap<String, Object>();
			attCutMap.put("laterCutPayment", MathUtil.formatDouble(laterCutAmount, 1));
			attCutMap.put("leaveEarlyCutPayment", MathUtil.formatDouble(leaveEarlyCutAmount, 1));
			attCutMap.put("forgetClockCutPayment", MathUtil.formatDouble(forgetClockCutAmount, 1));
			attCutMap.put("noClockCutPayment", MathUtil.formatDouble(noClockCutAmount, 1));
			attCutMap.put("totalCutAmount", MathUtil.formatDouble(totalCutAmount, 1));
			attCutMap.put("leaveCutPayment", leaveCutPayment);
			attCutMap.put("overTimePayment", overTimePayment);
			cutMap.put(user.getId(), attCutMap);
		}
		
		data.put("data", cutPayments);
		data.put("money", cutMap);
		data.put("noRuleUserIds", userIds);
		return data;
	}
	
	/**
	 * 获取迟到或早退的每次扣款
	 * @param cutPayment 迟到或早退的扣款规则
	 * @param daySalaries 天工资
	 * @param minute 迟到或早退的分钟数
	 * @return double 扣款金额
	 */
	private double getCutPayment(String cutPayment, double daySalaries, int minute){
		String[] CutArr = cutPayment.split("\\|");
		List<CutPaymentRule> rules = new ArrayList<CutPaymentRule>();
		for (String later : CutArr){
			CutPaymentRule cutRule = new CutPaymentRule();
			cutRule.setMinute(Integer.parseInt(later.split(",")[0]));
			cutRule.setMoney(Double.parseDouble(later.split(",")[1]));
			cutRule.setUnit(Integer.parseInt(later.split(",")[2]));
			rules.add(cutRule);
		}
		Collections.sort(rules, new Comparator<CutPaymentRule>() {
			@Override  
			public int compare(CutPaymentRule o1, CutPaymentRule o2) {
				return o1.getMinute() - o2.getMinute();
			}
		});
		double money = 0;
		for (CutPaymentRule rule : rules){
			if (minute >= rule.getMinute()){
				money = rule.getMoney();
				if (rule.getUnit() == 1){
					money = MathUtil.formatDouble(MathUtil.mul(money, daySalaries) , 1);
				}
			}
		}
		return money;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(CutPayment cut) {
		return dao.save(cut) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchSave(List<CutPayment> list) {
		return dao.batchSave(list) > 0;
	}

	@Override
	public List<CutPayment> getCutPaymentByMonthAndUserId(long userId, String month) {
		Map<String ,Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("month", month);
		return dao.getCutPaymentByMonthAndUserId(params);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDelete(String month, List<CutPayment> list) {
		return dao.batchDelete(month, list) > 0;
	}

	@Override
	public List<CutPayment> getCutPaymentList(Map<String, Object> params) {
		return dao.getCutPaymentList(params);
	}

	@Override
	public int getTotalNum(Map<String, Object> params) {
		return dao.getTotalNum(params);
	}
	

}
