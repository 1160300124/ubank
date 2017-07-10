<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
    function del(){
    	if(confirm("您确定要删除吗？")){
    		return true;
    	}
         return false;
    }
    function changePage(){
    	var pageIndex=document.getElementById("pageIndex").value;
    }
</script>
</head>
<body>
   <h26><a href="toAddUser">添加用户</a></h2>
   <h3 align="center">用户列表</h3>
   <table width="70%" border="1" align="center">
   <tr>
      <th>编号</th>
      <th>姓名</th>
      <th>密码</th>
      <th>年龄</th>
      <th>更新</th>
      <th>删除</th>
   </tr>
   <c:forEach items="${users}" var="user">
     <tr>
      <td>${user.id}</td>
      <td><a href="getUser?id=${user.id}&pageIndex=${pageIndex}">${user.userName}</a></td>
      <td>${user.password}</td>
      <td>${user.age}</td>
      <td><a href="getUser?id=${user.id}&pageIndex=${pageIndex}">更新</a></td>
      <td><a href="delUser?id=${user.id}&pageIndex=${pageIndex}" onclick="return del()">删除</a></td>
     </tr>
    </c:forEach> 
    <a href="getAllUser?pageIndex=1">首页|</a>  
    <c:choose>
       <c:when test="${pageIndex>1}">
          <a href="getAllUser?pageIndex=${pageIndex-1}">上一页|</a>
       </c:when>
       <c:otherwise>
          <a>上一页|</a>
       </c:otherwise>
    </c:choose>
    <c:choose>
         <c:when test="${pageIndex<totalPages}">
          <a href="tofindAllPersons?pageIndex=${pageIndex+1}">下一页|</a>
       </c:when>
       <c:otherwise>
          <a>下一页|</a>
       </c:otherwise>
    </c:choose>
    
    <a href="tofindAllPersons?pageIndex=${totalPages}">尾页</a> 
       当前第 ${pageIndex}页 |共 ${totalPages}页 |共${totalRows}条数据
   </table>
</body>
</html>