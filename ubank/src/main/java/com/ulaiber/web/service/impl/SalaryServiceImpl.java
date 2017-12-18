package com.ulaiber.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.AttendanceRuleDao;
import com.ulaiber.web.dao.BankDao;
import com.ulaiber.web.dao.SalaryDao;
import com.ulaiber.web.dao.SalaryDetailDao;
import com.ulaiber.web.dao.SalaryRuleDao;
import com.ulaiber.web.model.Payee;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.SalaryAuditVO;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.model.attendance.CutPayment;
import com.ulaiber.web.model.salary.Salary;
import com.ulaiber.web.model.salary.SalaryDetail;
import com.ulaiber.web.model.salary.SalaryRule;
import com.ulaiber.web.service.AttendanceStatisticService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.CutPaymentService;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.service.SalaryAuditService;
import com.ulaiber.web.service.SalaryService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.MathUtil;
import com.ulaiber.web.utils.SPDBUtil;
import com.ulaiber.web.utils.UUIDGenerator;

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
	private SalaryDao dao;
	
	@Resource
	private SalaryDetailDao detailDao;
	
	@Autowired
	private UserService userService;
	
	@Resource
	private SalaryRuleDao ruleDao;
	
	@Resource
	private AttendanceRuleDao attRuleDao;
	
	@Resource
	private BankDao bankDao;
	
	@Autowired
	private CutPaymentService cutService;
	
	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private AttendanceStatisticService statisticService;
	
	@Autowired
    private SalaryAuditService salaryAuditService;

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
		
		//sid 17位时间戳+32位uuid
		String sid = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_SIMPLEALLTIME);
		SalaryAuditVO salaryAudit = new SalaryAuditVO(); 
		salaryAudit.setAuditor(salary.getApproveIds());
		salaryAudit.setUserId(salary.getOperateUserId() + "");
		salaryAudit.setReason(salary.getSalaryMonth() + "工资发放。");
		salaryAudit.setTotalNumber(salary.getTotalNumber());
		salaryAudit.setTotalAmount(salary.getTotalAmount());
		salaryAudit.setSalaryId(sid);
		if (salaryAuditService.salaryAudit(salaryAudit) <= 0){
			logger.info(salary.getSalaryMonth() + "工资发放申请失败。");
			return false;
		}
		
		salary.setSid(sid);
		salary.setSalaryCreateTime(DateTimeUtil.date2Str(new Date()));
		salary.setStatus("0");
		salary.setTotalAmount(MathUtil.formatDouble(salary.getTotalAmount(), 1));
		
		//获取该公司的所有二类户信息 key为userId/value为二类户账号
		Map<Long, String> map = new HashMap<Long, String>();
		List<SecondAcount> accounts = bankDao.getSubByCompanyNum(salary.getCompanyId());
		for (SecondAcount account : accounts){
			map.put(account.getUserid(), account.getSubAcctNo());
		}
		
		if (this.dao.save(salary) > 0){
			for (SalaryDetail detail : salary.getDetails()){
				detail.setDid(DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_ALLTIME) + UUIDGenerator.getUUID());
				detail.setSid(sid);
				detail.setCreateDate(DateTimeUtil.date2Str(new Date()));
				detail.setUpdateTime(DateTimeUtil.date2Str(new Date()));
				detail.setSubAcctNo(map.get(detail.getUserId()));
			}
			return detailDao.batchSave(salary.getDetails()) > 0;
		}
		return false;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean update(Salary salary) {
		salary.setSalaryCreateTime(DateTimeUtil.date2Str(new Date()));
		if (this.dao.update(salary) > 0){
			
			return detailDao.batchUpdate(salary.getDetails()) > 0;
		}
		return false;
	}

	@Override
	public List<Salary> getAllSalaries() {
		return this.dao.getAllSalaries();
	}

	@Override
	public List<Salary> getSalaries(int limit, int offset, String search) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limit", limit);
		params.put("offset", offset);
		params.put("search", search);
		return this.dao.getSalaries(params);
	}

	@Override
	public int getTotalNum() {
		
		return this.dao.getTotalNum();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateStatusBySeqNo(Salary sa) {
		
		return this.dao.updateStatusBySeqNo(sa) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean batchDeleteSalaries(List<String> sids) {
		if (this.dao.batchDeleteSalaries(sids) > 0){
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
		List<Map<String, Object>> list = this.dao.getSalariesByUserId(params);
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public List<SalaryDetail> importUserInfo(String companyNum, String salaryMonth) {
		List<SalaryDetail> details = new ArrayList<SalaryDetail>();
		//获取公司的工资规则
		SalaryRule rule = ruleDao.getSalaryRuleByCompanyId(companyNum);
		//事假，病假，年假，调休，婚假，产假，其他
		String[] leaveCutRule = rule.getLeaveCutPayment().split(",");
		//获取扣款详细和总金额
		Map<String, Object> cutMap = cutService.getCutPaymentMessage(companyNum, salaryMonth);
		//扣款总金额Map,key为userId,value为总金额
		Map<Long, Object> cutMoneyMap = (Map<Long, Object>)cutMap.get("money");
		//分组查询用户的请假扣款
		List<Map<String, Object>> leaveList = leaveService.getTotalTimeByCompanyNumAndMonth(companyNum, "0", salaryMonth);
		//分组查询用户的加班费
		List<Map<String, Object>> overTimeList = leaveService.getTotalTimeByCompanyNumAndMonth(companyNum, "1", salaryMonth);
		Map<Long, Double> overTimeMap = new HashMap<Long, Double>();
		for (Map<String, Object> map : overTimeList){
			overTimeMap.put(Long.parseLong(map.get("userId").toString()), (Double)map.get("totalTime"));
		}
		//获取公司用户
		List<User> users = userService.getUsersByComNum(companyNum, null);
		for (User user : users){
			AttendanceRule attRule = attRuleDao.getRuleByUserId(user.getId());
			if (null == attRule){
				logger.error("用户 {" + user.getId() + "}没有设置考勤规则，请先设置。");
				continue;
			}
			//应工作天数
			List<String> workdays = statisticService.getWorkdaysForDate(attRule, DateTimeUtil.getMonthBegin(salaryMonth), DateTimeUtil.getMonthEnd(salaryMonth));
			if (workdays == null){
				continue;
			}
			String day = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
			//每天工作小时数
			double workHours = DateTimeUtil.gethour(day + " " + attRule.getClockOnTime(), day + " " + attRule.getClockOffTime());
			if (attRule.getRestFlag() == 0){
				double restHours = DateTimeUtil.gethour(day + " " + attRule.getRestEndTime(), day + " " + attRule.getRestStartTime());
				workHours = MathUtil.formatDouble(MathUtil.sub(workHours, restHours), 1);
			}
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
			Map<String, Object> attCutPayment = (Map<String, Object>)cutMoneyMap.get(user.getId());
			detail.setTotalCutPayment((Double)attCutPayment.get("totalCutAmount"));
			detail.setLaterCutPayment((Double)attCutPayment.get("leaveEarlyCutPayment"));
			detail.setLeaveEarlyCutPayment((Double)attCutPayment.get("forgetClockCutPayment"));
			detail.setForgetClockCutPayment((Double)attCutPayment.get("laterCutPayment"));
			detail.setNoClockCutPayment((Double)attCutPayment.get("noClockCutPayment"));
			double leaveCutPayment = 0;
			for (Map<String, Object> map : leaveList){
				if (user.getId() != Long.parseLong(map.get("userId").toString())){
					continue;
				}
				double cutPayment = MathUtil.formatDouble(MathUtil.mul(hourSalaries, null == map.get("totalTime") ? 0 : (Double)map.get("totalTime")), 1);
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
			detail.setAskForLeaveCutPayment(leaveCutPayment);
			double overTimePayment = 0;
			//0:调休 1:发加班费
			if (rule.getOvertimeFlag() == 1){
				overTimePayment = MathUtil.formatDouble(MathUtil.mul(hourSalaries, null == overTimeMap.get(user.getId()) ? 0 : overTimeMap.get(user.getId())), 1);
			}
			detail.setOvertimePayment(overTimePayment);
			//扣除社保公积金等
			double preSalary = detail.getPreTaxSalaries() - detail.getTotalCutPayment() - detail.getAskForLeaveCutPayment() + detail.getOvertimePayment()
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

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public ResultInfo pay(String salaryId) {
		ResultInfo info = new ResultInfo();
		double amount = 0;
    	//收款人集合
		List<Payee> payees = new ArrayList<>();
		List<SalaryDetail> details = detailDao.getDetailsBySid(salaryId);
		for (SalaryDetail detail : details){
			Payee payee = new Payee();
			payee.setPayeeAcctNo(detail.getSubAcctNo());
			payee.setPayeeName(detail.getUserName());
			payee.setAmount(detail.getSalaries());
			amount += detail.getSalaries();
			payee.setNote(detail.getRemark());
			payees.add(payee);
		}
		
		//工资流水
    	Salary salary = dao.getSalaryBySid(salaryId);
    	if (salary.getTotalNumber() != details.size() || salary.getTotalAmount() != amount){
			info.setMessage("工资总笔数或总金额不相符，请检查工资表！");
			info.setCode(IConstants.QT_CODE_ERROR);
			return info;
		}
    	String bespearkDate = salary.getSalaryDate().replaceAll("-", "");
    	int totalNumber = salary.getTotalNumber();
    	double totalAmount = salary.getTotalAmount();
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("elecChequeNo", salary.getSid());
		params.put("bespeakDate", bespearkDate);
		params.put("totalNumber", totalNumber);
		params.put("totalAmount", totalAmount);
		params.put("flag", "1");
    	String entrustSeqNo = SPDBUtil.paySalaries(params, payees);
    	if (StringUtils.isNotEmpty(entrustSeqNo)){
    		salary.setEntrustSeqNo(entrustSeqNo);
    		String status = SPDBUtil.getPayResult(entrustSeqNo);
    		if (StringUtils.isNotEmpty(status)){
    			salary.setStatus(status);
    			boolean flag = dao.updateStatusBySeqNo(salary) > 0;
    			if (flag){
    				info.setCode(IConstants.QT_CODE_OK);
    				logger.info("save salary and salary detail successed.");
    			}
    		} else {
    			info.setCode(IConstants.QT_CODE_ERROR);
    			info.setMessage("getPayResult failed.");
    			logger.info("getPayResult failed.");
    		}
    		
    	} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("操作失败：代发工资失败！");
    		logger.info("paySalaries failed.");
    	}
		return info;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public boolean updateStatusBySid(Salary salary) {
		if (dao.updateStatusBySeqNo(salary) > 0){
			return detailDao.batchUpdateStatusBySid(salary.getDetails()) > 0;
		}
		return false;
	}

}
