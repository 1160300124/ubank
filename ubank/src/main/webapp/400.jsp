<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>U钱包</title>
<link href="../css/error.css" rel="stylesheet" type="text/css" />


</head>
<body style="background-image:url(<% request.getContextPath(); %>/images/bodyBg.jpg);background-size:cover;">
<div class="body">
    <div class="zBodyK" style="box-shadow:2px 2px 10px rgba(0,0,0,.3);margin-top:30px">
		<div class="error">
        	<div class="pic error400" style="background-image:url(<%=request.getContextPath()%>/images/error400.gif)"></div>
            <div class="text">
            	<div class="text1">很遗憾！网页无法显示，请稍后再试！！</div>
                <div class="text2">错误类型：400，服务器繁忙。</div>
                <div class="text3">您可以刷新页面或重新输入地址。</div>
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
