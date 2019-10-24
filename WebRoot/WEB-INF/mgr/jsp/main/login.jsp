<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欢迎登录用友项目资产管理系统</title>

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI");
%>
<%@include file="../inc/import.jsp"%>
<script type="text/javascript" src="${ctx}/mgr/js/ux/main/login.js"></script>
</head>

<body class="loginbody">
	<%--浮动的白云--%>
	<div id="mainBody">
		<div id="cloud1" class="cloud"></div>
		<div id="cloud2" class="cloud"></div>
	</div>

	<%--顶部条栏--%>
	<div class="logintop">
		<span>欢迎登录用友项目资产管理系统</span>
	</div>

	<%--主窗体--%>
	<div class="logincenter">
		<span class="systemlogo"></span>
		<div class="loginbox">
			<form id="loginForm" action="${ctx}/mgr/login/check" method="post">
				<ul>
					<li><c:if test="${not empty requestScope.msg}">
							<font style="font-family:微软雅黑;font-size:14px;color:#CC0000">${requestScope.msg}</font>
						</c:if> <span class="input_tips">请输入用户名</span> <input name="userAct"
						type="text" value="${cookie.userAct.value}"
						class="easyui-validatebox input_txt loginuser" required="true"
						missingMessage="用户名不能为空" /></li>
					<li><span class="input_tips">请输入密码</span> <input
						name="userPwd" type="password" value="${cookie.userPwd.value}"
						class="easyui-validatebox input_txt loginpwd" required="true"
						missingMessage="密码不能为空" /></li>
					<li><input type="submit" class="loginbtn" value="登录" /> <label>
							<input name="rmPwd" type="checkbox" value="1"
							<c:if test="${cookie.rmPwd.value eq '1'}">checked="checked"</c:if> />
							记住密码
					</label></li>
				</ul>
			</form>
		</div>
	</div>


	<%--底部条栏--%>
	<div class="loginbm">
		版权所有 2016 <a href="http://www.gzjrkg.com/">用友公司</a> V1.0
	</div>
</body>
</html>
