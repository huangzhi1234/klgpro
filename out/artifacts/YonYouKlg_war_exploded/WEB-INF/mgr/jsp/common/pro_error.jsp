<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String message = (String)request.getAttribute("message");
%>

<html>
    <head>
        <title><%=message%></title>
        <%@include file="../inc/import.jsp"%>
    </head>
    <body>
    
    <a href="javascript:void(0);" onclick="history.go(-1)">返回刚才页面</a><br/><br/>
    </body>
</html>
