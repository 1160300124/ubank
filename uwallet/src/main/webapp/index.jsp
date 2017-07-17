<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.ulaiber.web.conmon.IConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script language=JavaScript>	
	<%
		if(null != request.getSession().getAttribute(IConstants.UBANK_BACKEND_USERSESSION)){
			response.sendRedirect("backend/index");
		}else{
			response.sendRedirect("backend/tologin");
		}
	%>
</script>
</head>
<body>

</body>
</html>