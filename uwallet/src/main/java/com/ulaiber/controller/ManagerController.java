package com.ulaiber.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ulaiber.conmon.IConstants;
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

@Controller
@RequestMapping("/manager/")
public class ManagerController extends BaseController{
	
	private Map<String, Object> payMap = new ConcurrentHashMap<String, Object>();
	
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
		
		List<Salary> list = salaryService.getAllSalaries();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", list.size());
		map.put("rows", list);
		return map;
	}
	
    @RequestMapping("upload")
    @ResponseBody
    public ResultInfo uploadFile(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response){
    	
    	ResultInfo retInfo = new ResultInfo(1000, "");
    	try {
    		// 取得request中的所有文件名
        	Iterator<String> iter = multiRequest.getFileNames();
        	while (iter.hasNext()) {
        		// 记录上传过程起始时的时间，用来计算上传时间
        		int pre = (int) System.currentTimeMillis();
        		// 取得上传文件
        		MultipartFile file = multiRequest.getFile(iter.next());
        		if (file != null) {
        			// 取得当前上传文件的文件名称
        			String myFileName = file.getOriginalFilename();
        			// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
        			if (myFileName.trim() != "") {
        				System.out.println(myFileName);
        				Map<String, Object> emap = ExcelUtil.readExcel(file);
        				List<SalaryDetail> details = (List<SalaryDetail>)emap.get("details");
        				retInfo.setData(details);
        				payMap.clear();
        				payMap = emap;
        				// 重命名上传后的文件名
//        				String fileName = "demoUpload"
//        						+ file.getOriginalFilename();
//        				// 定义上传路径
//        				String path = "E:/" + fileName;
//        				File localFile = new File(path);
//        				file.transferTo(localFile);
        			}
        		}
        		// 记录上传该文件后的时间
        		int finaltime = (int) System.currentTimeMillis();
        		System.out.println(finaltime - pre);
        	}
    	} catch (IOException e) {
			 e.printStackTrace();
			 retInfo.setCode(IConstants.QT_CODE_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			retInfo.setCode(IConstants.QT_CODE_ERROR);
		}
    	
        return retInfo;
    }
    
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo pay(HttpServletRequest request, HttpServletResponse response){
    	ResultInfo info = new ResultInfo();
    	
		List<SalaryDetail> details = (List<SalaryDetail>)payMap.get("details");
		Map<String, String> map = new HashMap<String, String>();
    	List<User> users = userService.findAll();
    	for (User user : users){
    		map.put(user.getCardNo(), user.getSecondBankCardNo());
    	}
		List<Payee> payees = new ArrayList<>();
		for (SalaryDetail sd : details){
			Payee payee = new Payee();
			payee.setPayeeAcctNo(map.get(sd.getCardNo()));
			payee.setPayeeName(sd.getUserName());
			payee.setAmount(sd.getSalaries());
			payee.setNote(sd.getRemark());
			payees.add(payee);
		}
		
    	Salary sa = (Salary)payMap.get("sa");
    	sa.setSalary_createTime(DateTimeUtil.date2Str(new Date()));
    	Map<String, Object> params = new HashMap<String, Object>();
//    	String returnCode = SPDBUtil.paySalaries(params, payees);
//    	if (StringUtils.isNotEmpty(returnCode)){
//    		boolean flag = SPDBUtil.getPayResult(returnCode);
//    	}
    	
    	
//    	salaryService.save(sa);
//    	salaryDetailService.saveBatch(details);
    	managerService.save(sa, details);
    	
    	return info;
    }
    
    @RequestMapping(value = "details", method = RequestMethod.GET)
    @ResponseBody
    public List<SalaryDetail> details(String sid, HttpServletRequest request, HttpServletResponse response){
    	if (StringUtils.isEmpty(sid)){
    		return null;
    	}
    	
    	List<SalaryDetail> details = salaryDetailService.getDetailsBySid(Long.valueOf(sid));
    	
    	return details;
    }
    
}
