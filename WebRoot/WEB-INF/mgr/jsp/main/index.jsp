<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
	Calendar calendars = Calendar.getInstance(Locale.CHINA);
	int hours = calendars.get(Calendar.HOUR_OF_DAY);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用友项目资产管理系统</title>
<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI,jQueryForm");
%>
<%@include file="../inc/import.jsp"%>
<script type="text/javascript"
	src="${ctx}/mgr/js/ux/main/index.js"></script>
<script>
$(function(){
	alert("hjkhj");
});
</script>
</head>

<body>
	<!--导航栏&&权限控制-->
	<%@include file="../inc/head.jsp"%>

	<div class="mainindex">
		<div class="welinfo">
			<span><img src="${ctx}/mgr/images/sun.png" alt="天气" /> </span> <b>${user.userAct}【${user.userName}】,
				<c:set var="hours" value="<%=hours%>" /> <c:choose>
					<c:when test="${hours >= 0 && hours < 5}">凌晨好，</c:when>
					<c:when test="${hours >= 5 && hours < 11}">早上好，</c:when>
					<c:when test="${hours >= 11 && hours < 13}">中午好，</c:when>
					<c:when test="${hours >= 13 && hours < 17}">下午好，</c:when>
					<c:when test="${hours >= 17 && hours < 19}">傍晚好，</c:when>
					<c:when test="${hours >= 19 && hours < 24}">晚上好，</c:when>
					<c:otherwise>您好，</c:otherwise>
				</c:choose> 欢迎使用广州用友资产管理系统
			</b>
		</div>

		<div class="welinfo">
			<span><img src="${ctx}/mgr/images/time.png" alt="时间" /> </span> <label id="loginTime">您上次登录的时间：
				</label>
		</div>

		<div class="xline"></div>
	</div>
</body>
<script>
$(function(){
	alert("hjkhj");
});
</script>
</html>