package com.ulaiber.controller.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ulaiber.conmon.IConstants;
import com.ulaiber.controller.BaseController;
import com.ulaiber.model.Payee;
import com.ulaiber.model.ResultInfo;
import com.ulaiber.model.Salary;
import com.ulaiber.model.SalaryDetail;
import com.ulaiber.model.User;
import com.ulaiber.service.ManagerService;
import com.ulaiber.service.SalaryDetailService;
import com.ulaiber.service.SalaryService;
import com.ulaiber.service.UserService;
import com.ulaiber.utils.DateTimeUtil;
import com.ulaiber.utils.ExcelUtil;
import com.ulaiber.utils.SPDBUtil;

@Controller
@RequestMapping("/backend/")
public class ManagerController extends BaseController{
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(ManagerController.class);
	
	private Map<String, Map<String, Object>> payMap = new ConcurrentHashMap<String, Map<String, Object>>();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SalaryService salaryService;
	
	@Autowired
	private SalaryDetailService salaryDetailService;
	
	@Autowired
	private ManagerService managerService;
	
	@RequestMapping("tomanager")
	public String tomanager(HttpServletRequest request, HttpServletResponse response){
		
		return "management";
	}

	@RequestMapping(value = "getManagement", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getManagement(int limit, int offset, String search, HttpServletRequest request, HttpServletResponse response){
		
		if (limit <= 0){
			limit = 10;
		}
		if (offset < 0){
			offset = 0;
		}
		
		int total = salaryService.getTotalNum();
		List<Salary> list = salaryService.getSalaries(limit, offset, search);
		
		for (Salary sa : list){
			if (StringUtils.isNotEmpty(sa.getEntrustSeqNo())){
				String oldStatus = sa.getStatus();
				if (StringUtils.isEmpty(oldStatus) || !oldStatus.equals("5")){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("authMasterID", SPDBUtil.CLIENT_MASTER_ID);
					params.put("acctNo", SPDBUtil.CLIENT_ACCT_NO);
					params.put("seqNo", sa.getEntrustSeqNo());
					params.put("beginDate", sa.getSalaryDate());
					params.put("endDate", sa.getSalaryDate());
					String status = SPDBUtil.getPayResult(params);
					sa.setStatus(status);
					//if new status != old status 更新工资流水状态
					if (StringUtils.equals(oldStatus, status)){
						salaryService.updateStatusBySeqNo(sa);
					}
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return map;
	}
	
    @SuppressWarnings("unchecked")
	@RequestMapping("upload")
    @ResponseBody
    public ResultInfo uploadFile(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response){
    	
    	ResultInfo retInfo = new ResultInfo();
    	try {
    		// 取得request中的所有文件名
        	Iterator<String> iter = multiRequest.getFileNames();
        	while (iter.hasNext()) {
        		// 取得上传文件
        		MultipartFile file = multiRequest.getFile(iter.next());
        		if (file != null) {
        			// 取得当前上传文件的文件名称
        			String myFileName = file.getOriginalFilename();
        			// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
        			if (myFileName.trim() != "") {
        				logger.info("upload file name: " + myFileName.trim());
        				Map<String, Object> emap = ExcelUtil.readExcel(file);
        				List<SalaryDetail> details = (List<SalaryDetail>)emap.get("details");
        				
        				retInfo.setData(details);
        				retInfo.setCode(IConstants.QT_CODE_OK);
        				
        				User user = getUserFromSession(request);
        				payMap.remove(String.valueOf(user.getId()));
        				payMap.put(String.valueOf(user.getId()), emap);
 
        			}
        		}
        	}
    	} catch (IOException e) {
    		logger.error("upload file failed:" , e);
			retInfo.setCode(IConstants.QT_CODE_ERROR);
		} catch (Exception e) {
			logger.error("upload file failed:" , e);
			retInfo.setCode(IConstants.QT_CODE_ERROR);
		}
    	
        return retInfo;
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "pay", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo pay(HttpServletRequest request, HttpServletResponse response){
    	
    	ResultInfo info = new ResultInfo();
    	
		Map<String, String> map = new HashMap<String, String>();
    	List<User> users = userService.findAll();
    	//有二类账户则取二类账户，否则取一类账户(银行卡)
    	for (User user : users){
    		map.put(user.getCardNo(), StringUtils.isNotEmpty(user.getSecondBankCardNo()) ? user.getSecondBankCardNo() : user.getBankCardNo());
    	}
    	
    	//从sesison获取当前用户
    	User currentUser = getUserFromSession(request);
    	Map<String, Object> emap = payMap.get(String.valueOf(currentUser.getId()));
    	
    	//收款人集合
    	double amount = 0;
		List<Payee> payees = new ArrayList<>();
		List<SalaryDetail> details = (List<SalaryDetail>)emap.get("details");
		for (SalaryDetail sd : details){
			Payee payee = new Payee();
			payee.setPayeeAcctNo(map.get(sd.getCardNo()));
			payee.setPayeeName(sd.getUserName());
			payee.setAmount(sd.getSalaries());
			amount += sd.getSalaries();
			payee.setNote(sd.getRemark());
			payees.add(payee);
		}
		
		//工资流水
    	Salary sa = (Salary)emap.get("sa");
    	
    	if (sa.getTotalNumber() != details.size() || sa.getTotalAmount() != amount){
			info.setMessage("工资总笔数或总金额不相符，请检查Excel表格！");
			info.setCode(IConstants.QT_GET_BALANCE_ERROR);
			return info;
		}
    	sa.setUserName(currentUser.getUserName());
    	sa.setSalary_createTime(DateTimeUtil.date2Str(new Date()));
    	String bespearkDate = sa.getSalaryDate();
    	int totalNumber = sa.getTotalNumber();
    	double totalAmount = sa.getTotalAmount();
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("elecChequeNo", String.valueOf(DateTimeUtil.getMillis(new Date())));
		params.put("authMasterID", SPDBUtil.CLIENT_MASTER_ID);
		params.put("unitNo", SPDBUtil.UNIT_NUM);
		params.put("businessType", "1001");
		params.put("acctNo", SPDBUtil.CLIENT_ACCT_NO);
		params.put("bespeakDate", "");
		params.put("totalNumber", totalNumber);
		params.put("totalAmount", totalAmount);
		params.put("flag", "1");
    	String entrustSeqNo = SPDBUtil.paySalaries(params, payees);
//		String entrustSeqNo = "12345678910";
    	if (StringUtils.isNotEmpty(entrustSeqNo)){
    		sa.setEntrustSeqNo(entrustSeqNo);
    		
//    		String status = SPDBUtil.getPayResult(entrustSeqNo);
//    		if (StringUtils.isNotEmpty(status)){
//    			sa.setStatus(status);
//    			boolean flag = managerService.save(sa, details);
//    			if (flag){
//    				info.setCode(IConstants.QT_CODE_OK);
//    				logger.info("save salary and salary detail successed.");
//    			}
//    		} else {
//    			info.setCode(IConstants.QT_CODE_ERROR);
//    			info.setMessage("getPayResult failed.");
//    			logger.info("getPayResult failed.");
//    		}
    		
			if (managerService.save(sa, details)){
				info.setCode(IConstants.QT_CODE_OK);
				logger.info("save salary and salary detail successed.");
			}
			
    	} else {
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("操作失败：代发工资失败！");
    		logger.info("paySalaries failed.");
    	}
    	
    	return info;
    }
    
    @RequestMapping(value = "details", method = RequestMethod.GET)
    @ResponseBody
    public List<SalaryDetail> details(String sid, HttpServletRequest request, HttpServletResponse response){
    	if (StringUtils.isEmpty(sid)){
    		logger.error("sid is null.");
    		return null;
    	}
    	
    	List<SalaryDetail> details = salaryDetailService.getDetailsBySid(Long.valueOf(sid));
    	
    	return details;
    }
    
}
