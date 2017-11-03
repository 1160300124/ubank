package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.dao.SalaryDao;
import com.ulaiber.web.dao.SalaryDetailDao;
import com.ulaiber.web.dao.SalaryRuleDao;
import com.ulaiber.web.model.Salary;
import com.ulaiber.web.model.SalaryDetail;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.model.attendance.CutPayment;
import com.ulaiber.web.model.attendance.SalaryRule;
import com.ulaiber.web.service.AttendanceStatisticService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.CutPaymentService;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.service.SalaryService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.MathUtil;

/** 
 * 工资业务实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年10月20日 下午5:07:15
 * @version 1.0 
 * @since 
 */
@Service
public class SalaryServiceImpl extends BaseService implements SalaryService {
	
	private static Logger logger = Logger.getLogger(SalaryServiceImpl.class);
	
	@Resource
	private SalaryDao mapper;
	
	@Resource
	private SalaryDetailDao detailDao;
	
	@Autowired
	private UserService userService;
	
	@Resource
	private SalaryRuleDao ruleDao;
	
	@Resource
	private AttendanceRuleDao attRuleDao;
	
	@Autowired
	private CutPaymentService cutService;
	
	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private AttendanceStatisticService statisticService;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(Salary salary) {
		//获取扣款详细和总金额
		Map<String, Object> cutMap = cutService.getCutPaymentMessage(salary.getCompanyId(), salary.getSalaryMonth());
		//批量插入扣款详细
		List<CutPayment> list = (List<CutPayment>)cutMap.get("data");
		cutService.batchDelete(salary.getSalaryMonth(), list);
		cutService.batchSave(list);
		salary.setSalaryCreateTime(DateTimeUtil.date2Str(new Date()));
		salary.setStatus("0");
		salary.setTotalAmount(MathUtil.formatDouble(salary.getTotalAmount(), 1));
		if (this.mapper.save(salary) > 0){
			long sid = salary.getSid();
			for (SalaryDetail detail : salary.getDetails()){
				detail.setSid(sid);
			}
			return detailDao.batchSave(salary.getDetails()) > 0;
		}
		return false;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(Salary salary) {
		salary.setSalaryCreateTime(DateTimeUtil.date2Str(new Date()));
		if (this.mapper.update(salary) > 0){
			
			return detailDao.batchUpdate(salary.getDetails()) > 0;
		}
		return false;
	}

	@Override
	public List<Salary> getAllSalaries() {
		return this.mapper.getAllSalaries();
	}

	@Override
	public List<Salary> getSalaries(int limit, int offset, String search) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("search", search);
		return this.mapper.getSalaries(params);
	}

	@Override
	public int getTotalNum() {
		
		return this.mapper.getTotalNum();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateStatusBySeqNo(Salary sa) {
		
		return this.mapper.updateStatusBySeqNo(sa) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDeleteSalaries(List<Long> sids) {
		if (this.mapper.batchDeleteSalaries(sids) > 0){
			return detailDao.batchDeleteSalaryDetails(sids) > 0;
		}
		return false;
		
	}

	@Override
	public List<Map<String, Object>> getSalariesByUserId(long userId, int pageSize, int pageNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		int offset = pageSize * (pageNum -1);
		params.put("limit", pageSize);
		params.put("offset", offset);
		params.put("userId", userId);
		List<Map<String, Object>> list = this.mapper.getSalariesByUserId(params);
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public List<SalaryDetail> importUserInfo(String companyNum, String salaryMonth) {
		List<SalaryDetail> details = new ArrayList<SalaryDetail>();
		//获取公司的工资规则
		SalaryRule rule = ruleDao.getSalaryRuleByCompanyId(companyNum);
		//获取扣款详细和总金额
		Map<String, Object> cutMap = cutService.getCutPaymentMessage(companyNum, salaryMonth);
		//扣款总金额Map,key为userId,value为总金额
		Map<Long, Double> cutMoneyMap = (Map<Long, Double>)cutMap.get("money");
		//分组查询用户的请假扣款
		List<Map<String, Object>> leaveList = leaveService.getTotalTimeByCompanyNumAndMonth(companyNum, "0", salaryMonth);
		Map<Long, Double> leaveMap = new HashMap<Long, Double>();
		for (Map<String, Object> map : leaveList){
			leaveMap.put(Long.parseLong(map.get("userId").toString()), (Double)map.get("totalTime"));
		}
		//分组查询用户的加班费
		List<Map<String, Object>> overTimeList = leaveService.getTotalTimeByCompanyNumAndMonth(companyNum, "1", salaryMonth);
		Map<Long, Double> overTimeMap = new HashMap<Long, Double>();
		for (Map<String, Object> map : overTimeList){
			overTimeMap.put(Long.parseLong(map.get("userId").toString()), (Double)map.get("totalTime"));
		}
		//获取公司用户
		List<User> users = userService.getUsersByComNum(companyNum, null);
		for (User user : users){
			//应工作天数
			List<String> workdays = statisticService.getWorkdaysForDate(user.getId(), DateTimeUtil.getMonthBegin(salaryMonth), DateTimeUtil.getMonthEnd(salaryMonth));
			if (workdays == null){
				continue;
			}
			AttendanceRule attRule = attRuleDao.getRuleByUserId(user.getId());
			if (null == attRule){
				logger.error("用户 {" + user.getId() + "}没有设置考勤规则，请先设置。");
				continue;
			}
			String day = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
			//每天工作小时数
			double workHours = DateTimeUtil.gethour(day + " " + attRule.getClockOnTime(), day + " " + attRule.getClockOffTime());
			//小时工资
			double hourSalaries = MathUtil.div(user.getSalaries(), workdays.size() * workHours);
			SalaryDetail detail = new SalaryDetail();
			detail.setUserId(user.getId());
			detail.setUserName(user.getUserName());
			detail.setCardNo(user.getCardNo());
			detail.setPreTaxSalaries(user.getSalaries());
			detail.setSocialInsurance(rule.getSocialInsurance());
			detail.setPublicAccumulationFunds(rule.getPublicAccumulationFunds());
			detail.setTaxThreshold(rule.getTaxThreshold());
			detail.setAttendanceCutPayment(null == cutMoneyMap.get(user.getId()) ? 0 : cutMoneyMap.get(user.getId()));
			double leaveCutPayment = MathUtil.formatDouble(MathUtil.mul(hourSalaries, null == leaveMap.get(user.getId()) ? 0 : leaveMap.get(user.getId())), 1);
			detail.setAskForLeaveCutPayment(leaveCutPayment);
			double overTimePayment = MathUtil.formatDouble(MathUtil.mul(hourSalaries, null == overTimeMap.get(user.getId()) ? 0 : overTimeMap.get(user.getId())), 1);
			detail.setOvertimePayment(overTimePayment);
			//扣除社保公积金等
			double preSalary = detail.getPreTaxSalaries() - detail.getAttendanceCutPayment() - detail.getAskForLeaveCutPayment() + detail.getOvertimePayment()
							- detail.getSocialInsurance() - detail.getPublicAccumulationFunds();
			double personalIncomeTax = getPersonalIncomeTax(preSalary, detail.getTaxThreshold());
			detail.setPersonalIncomeTax(personalIncomeTax);
			double salaries = MathUtil.formatDouble(MathUtil.sub(preSalary, personalIncomeTax), 1);
			detail.setSalaries(salaries < 0 ? 0 : salaries);
			details.add(detail);
		}
		return details;
	}
	
	/**
	 * 个人所得税
	 * @param preSalary 扣除社保公积金等
	 * @param taxThreshold 个税起征点
	 * @return
	 */
	private double getPersonalIncomeTax(double preSalary, double taxThreshold){
		//应纳税所得额(不含税)
		double salary = preSalary - taxThreshold;
		double personalIncomeTax = 0;
		if (salary <= 1500){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.mul(salary,  0.03), 1);
		} else if (salary > 1500 && salary <= 4500){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.sub(MathUtil.formatDouble(MathUtil.mul(salary,  0.10), 1), 105), 1);
		} else if (salary > 4500 && salary <= 9000){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.sub(MathUtil.formatDouble(MathUtil.mul(salary,  0.20), 1), 555), 1);
		} else if (salary > 9000 && salary <= 35000){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.sub(MathUtil.formatDouble(MathUtil.mul(salary,  0.25), 1), 1005), 1);
		} else if (salary > 35000 && salary <= 55000){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.sub(MathUtil.formatDouble(MathUtil.mul(salary,  0.30), 1), 2775), 1);
		} else if (salary > 55000 && salary <= 80000){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.sub(MathUtil.formatDouble(MathUtil.mul(salary,  0.35), 1), 5505), 1);
		} else if (salary > 80000){
			personalIncomeTax = MathUtil.formatDouble(MathUtil.sub(MathUtil.formatDouble(MathUtil.mul(salary,  0.45), 1), 13505), 1);
		}   
		return personalIncomeTax < 0 ? 0 : personalIncomeTax;
	}

}
