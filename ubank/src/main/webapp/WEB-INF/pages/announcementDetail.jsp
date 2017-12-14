<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>公告详情</title>
    <style>
        .title{
            font-size: 15px;
            font-weight: 700;
            color: #333;
            padding: 5px;
        }
        .time{
            padding: 5px;
            color: #999;
        }
        .content{
            padding: 5px;
            color: #666;
        }
        .files{
            padding: 5px;
            border-top: 2px solid #ededed;
            width: 100%;
        }
        .file{
            display: flex;
            display: -webkit-flex;
            display: -moz-flex;
            height: 50px;
            margin-top: 10px;
        }
        .file-icon{
            width: 50px;
        }
        .file-icon>img{
            width: 100%;
            height: 100%;
        }
        .file-info{
            flex: 1;
            -webkit-flex: 1;
            -moz-flex: 1;
        }
        .file-size{
            color: #999;
        }
    </style>
</head>
<body>
    <div class="title">${title}</div>
    <div class="time">${companyName} &nbsp;&nbsp;&nbsp; ${createTime}</div>
    <div class="content"> ${content}  </div>
    <div class="files">
        	附件：
        <table id="table_preview_attachment">
        	<c:forEach items="${attachments}" var="attachment">
	        	<tr data-down="${attachment.attachment_path}" data-preview="http://dcsapi.com/?k=262732077&url=${attachment.attachment_path}">
	                <td>
	                	<div class="file-icon">
			                <img src="<%=request.getContextPath()%>/images/pdf.png" alt="">
			            </div>
					</td>
	                <td>
	                    <div class="file-info">
			                <div class="file-name">${attachment.attachment_name}</div>
			                <div class="file-size">${attachment.attachment_size}</div>
			            </div>
	                </td>
	            </tr>
	        </c:forEach>
        </table>
    </div>
    
</body>
</html>