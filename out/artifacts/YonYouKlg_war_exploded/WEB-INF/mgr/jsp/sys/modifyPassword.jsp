<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>基本信息管理——修改密码</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script src="${ctx}/mgr/js/ux/sys/modifyPassword.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
  </head>


	<body>
		<!--导航栏&&权限控制-->
		<%@include file="../inc/head.jsp"%>
	
		<!--内容栏-->
		<div class="rightinfo">				
			<form id="editForm" class="formbody">
				<div class="formtitle"><span>修改密码</span></div>
					<ul class="forminfo"> 
						<li><label>原密码</label><input name="oldPassword" type="password" class="easyui-validatebox dfinput" /></li>
						<li><label>新密码</label><input id="newPassword" name="newPassword" type="password" class="easyui-validatebox dfinput" /></li>
						<li><label>确认新密码</label><input id="confirmNewPassword" name="confirmNewPassword" type="password" class="easyui-validatebox dfinput" /></li>						
						<li><button id="btnToUpdate" class="btn ml-180">确认修改</button></li>
					</ul>
			</form>		
		</div>
	</body>
</html>