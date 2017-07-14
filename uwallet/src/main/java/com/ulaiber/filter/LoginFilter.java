package com.ulaiber.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ulaiber.conmon.IConstants;
import com.ulaiber.model.User;
import com.ulaiber.service.UserService;
import com.ulaiber.service.impl.UserServiceImpl;
import com.ulaiber.utils.ObjUtil;

/** 
 * 登录过滤器 
 * <功能详细描述> 
 *  
 * @author  huangguoqing 
 * @version  
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */  
public class LoginFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserService userService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 不过滤的uri  
		String[] notFilter = new String[] {"/images", "/js", "/css", "/api/v1/validate", "/api/v1/login", "/api/v1/register",
						"/api/v1/sendCaptcha", "/api/v1/forgetLoginPassword", "/backend/tologin", "/backend/login"};

		// 请求的uri  
		String uri = request.getRequestURI().substring(request.getContextPath().length()); 
		// 是否过滤  
		boolean doFilter = true;  
		for (String s : notFilter) {  
			if (uri.indexOf(s) != -1) {  
				// 如果uri中包含不过滤的uri，则不进行过滤  
				doFilter = false;  
				break;  
			}  
		}  
//		boolean doFilter = false;
		if (doFilter) {
			//backend后台请求
			if (uri.contains("/backend/")){

				// 从session中获取登录者实体  
				Object obj = request.getSession().getAttribute(IConstants.UBANK_BACKEND_USERSESSION);
				if (!ObjUtil.notEmpty(obj)){
					boolean isAjaxRequest = isAjaxRequest(request);  
					if (isAjaxRequest) {  
						response.setCharacterEncoding("UTF-8");
						response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作，请刷新页面");  
						return ;  
					}  
					response.sendRedirect("../backend/tologin");  
					return;
				} else {  
//					User user = (User)obj;
//					if (user.getRole_id() == 1 || user.getRole_id() == 2){
//						
//					}
					// 如果session中存在登录者实体，则继续  
					filterChain.doFilter(request, response);
				}  
			} else if (uri.contains("/api/v1/")){
				//根据ticket和token获取用户
//				String login_ticket = request.getHeader("login_ticket");
//				String access_token = request.getHeader("access_token");
//				if (!ObjUtil.notEmpty(login_ticket) || !ObjUtil.notEmpty(access_token)) {
//					response.setCharacterEncoding("UTF-8");  
//					response.sendError(HttpStatus.UNAUTHORIZED.value(), "您还没有登录，请先登录");  
//					return ;  
//				}
//				
//				User user = userService.getUserByTicketAndToken(login_ticket, access_token);
//				if (null == user) {  
//					response.setCharacterEncoding("UTF-8");
//					response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作，请刷新页面");  
//					return ;  
//				} else {  
//					filterChain.doFilter(request, response);  
//				} 
				filterChain.doFilter(request, response);
			}  else {
				response.setCharacterEncoding("UTF-8");  
				response.sendError(HttpStatus.FORBIDDEN.value(), "您还没有登录，请先登录");
			}
			
		}  
		else {  
			// 如果不执行过滤，则继续  
			filterChain.doFilter(request, response);  
		}  
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
