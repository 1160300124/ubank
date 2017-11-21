<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>U钱包</title>
<link href="<%=request.getContextPath()%>/css/error.css" rel="stylesheet" type="text/css" />


</head>
<body style="background-image:url(<%=request.getContextPath()%>/images/bodyBg.jpg);background-size:cover;">

<div class="body">
    <div class="zBodyK" style="box-shadow:2px 2px 10px rgba(0,0,0,.3);margin-top:30px">
		<div class="error">
        	<div class="pic error404" style="background-image:url(<%=request.getContextPath()%>/images/error404.gif)"></div>
            <div class="text">
            	<div class="text1">很遗憾！没找到您所访问的页面！</div>
                <div class="text2">错误类型：404，文件没有找到。</div>
                <div class="text3">您可以重新输入正确地址或访问其他栏目页面。</div>
                <div class="link">
                	<ul>
                    	<li></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
</div>
</body>
</html>

