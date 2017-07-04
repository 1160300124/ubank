package com.ulaiber.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.model.SalaryInfo;
import com.ulaiber.utils.DateTimeUtil;

@Controller
@RequestMapping("/manager/")
public class ManagerController {
	
	@RequestMapping("tomanager")
	public String tomanager(HttpServletRequest request, HttpServletResponse response){
		
		return "management";
	}

	@RequestMapping(value = "getManagement", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getManagement(int limit, int offset, String search, HttpServletRequest request, HttpServletResponse response){
		
		List<SalaryInfo> list = new ArrayList<SalaryInfo>();
		for (int i =1; i<11; i++){
			SalaryInfo salary = new SalaryInfo();
			salary.setSid(i);
			salary.setUserName("张三" + i );
			salary.setCardNo("100000000" + i );
			salary.setSalaries("9999.99");
			salary.setSalaryDate(DateTimeUtil.date2Str(new Date()));
			salary.setRemark("暂无备注");
			list.add(salary);
		}
		
		List<SalaryInfo> list2 = new ArrayList<SalaryInfo>();
		for (int i =11; i<21; i++){
			SalaryInfo salary = new SalaryInfo();
			salary.setSid(i);
			salary.setUserName("张三" + i );
			salary.setCardNo("100000000" + i );
			salary.setSalaries("9999.99");
			salary.setSalaryDate(DateTimeUtil.date2Str(new Date()));
			salary.setRemark("暂无备注");
			list2.add(salary);
		}
		
		List<SalaryInfo> list3 = new ArrayList<SalaryInfo>();
		for (int i =21; i<31; i++){
			SalaryInfo salary = new SalaryInfo();
			salary.setSid(i);
			salary.setUserName("张三" + i );
			salary.setCardNo("100000000" + i );
			salary.setSalaries("9999.99");
			salary.setSalaryDate(DateTimeUtil.date2Str(new Date()));
			salary.setRemark("暂无备注");
			list3.add(salary);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total",30);
		if (offset == 0){
			map.put("rows", list);
		}
		else if (offset == 10) {
			map.put("rows", list2);
		}
		else if(offset == 20) {
			map.put("rows", list3);
		}
		return map;
	}
}
