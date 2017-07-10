<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
    <h3>个人信息</h3>
           编号:${user.id}   <br>
           姓名:${user.userName}<br>
           密码:${user.password}<br>
           年龄:${user.age}  <br>
    <input type="hidden" name="pageIndex" value="${pageIndex}"/>
    <a href="getAllUser?pageIndex=${pageIndex}">返回用户列表</a>  
</body>
</html>