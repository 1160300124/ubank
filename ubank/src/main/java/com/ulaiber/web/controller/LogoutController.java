package com.ulaiber.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ulaiber.web.service.UserService;

@Controller
@RequestMapping("/api/v1/")
public class LogoutController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	public void logout(String mobile, HttpServletRequest request, HttpServletResponse response){
		
	}

}
