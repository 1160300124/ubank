package com.ulaiber.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.ObjUtil;

public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(SessionInterceptor.class);
	
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.debug("getRequestURI----------" + request.getRequestURI());

		String uri = request.getRequestURI().substring(request.getContextPath().length()); 
		
		if (uri.contains("/backend/")){

			// 从session中获取登录者实体  
			Object obj = request.getSession().getAttribute(IConstants.UBANK_BACKEND_USERSESSION);
			if (!ObjUtil.notEmpty(obj)){
				boolean isAjaxRequest = isAjaxRequest(request);  
				if (isAjaxRequest) {  
					response.setCharacterEncoding("UTF-8");
					response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作，请刷新页面");  
				}  
				response.sendRedirect(request.getContextPath() + "/backend/tologin");  
				return false;
			} else {  
//				User user = (User)obj;
//				if (user.getRole_id() == 1 || user.getRole_id() == 2){
//					
//				}
				// 如果session中存在登录者实体，则继续  
				
			}  
		} else if (uri.contains("/api/v1/")){
			//根据ticket和token获取用户
//			String login_ticket = request.getHeader("login_ticket");
//			String access_token = request.getHeader("access_token");
//			if (!ObjUtil.notEmpty(login_ticket) || !ObjUtil.notEmpty(access_token)) {
//				response.setCharacterEncoding("UTF-8");  
//				response.sendError(HttpStatus.UNAUTHORIZED.value());
//				return false;  
//			}
//			
//			User user = userService.getUserByTicketAndToken(login_ticket, access_token);
//			if (null == user) {  
//				response.setCharacterEncoding("UTF-8");
//				response.sendError(HttpStatus.UNAUTHORIZED.value());
//				return false;  
//			}
//			if (StringUtils.isNotEmpty(request.getParameter("mobile"))){
//				if (!StringUtils.equals(user.getMobile(), request.getParameter("mobile"))){
//					response.setCharacterEncoding("UTF-8");
//					response.sendError(HttpStatus.UNAUTHORIZED.value(), "手机号不正确。");
//					return false;
//				}
//			}
		}
		
		
		return true;
	}
	
	/** 判断是否为Ajax请求  
	 * <功能详细描述> 
	 * @param request 
	 * @return 是true, 否false  
	 * @see [类、类#方法、类#成员] 
	 */  
	public static boolean isAjaxRequest(HttpServletRequest request)  
	{  
		String header = request.getHeader("X-Requested-With");   
		if (header != null && "XMLHttpRequest".equals(header))   
			return true;   
		else   
			return false;    
	} 
}
