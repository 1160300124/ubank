package com.ulaiber.web.controller.backend;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.service.UserService;

@Controller
@RequestMapping("/backend/")
public class BackendLogoutController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public void logout(String userName, HttpServletRequest request, HttpServletResponse response){
		
		try {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/backend/tologin");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
