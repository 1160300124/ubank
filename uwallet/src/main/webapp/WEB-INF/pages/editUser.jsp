<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'updatePerson.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
      <h3>更新用户信息</h3>
      <form action="updateUser" method="post">
         username:<input type="text" name="username" value="${user.userName}" readonly="true" /><br>
         password:<input type="password" name="password" value="${user.password}" /><br>
         age:<input type="text" name="age" value="${user.age}" /><br>
         <input type="hidden" name="id" value="${user.id}" />
         <input type="hidden" name="pageIndex" value="${pageIndex}"/>
         <input type="submit" value="提交" />
      </form>
  </body>
</html>
