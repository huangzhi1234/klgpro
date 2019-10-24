<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>个人积分排行榜</title>

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI");
%>
<%@include file="../inc/import.jsp"%>
<%
	String pro = "proInfo";
%>
<script type="text/javascript"
	src="${ctx}/mgr/js/ux/score/orderScore.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/mgr/css/proInfo/proInfo.css">
<style type="text/css">
.red {
	color: red;
	font-size: 20px;
}

.left {
	float: left !important;
}

.vali {
	height: 30px !important;
	border: 1px solid #95b8e7 !important;
	border-radius: 5px 5px 5px 5px !important;
	padding-left: 8px !important;
}

.easyui-validatebox {
	background-color: white;
}
</style>
<script type="text/javascript">
</script>
</head>
<body>
	<!--导航栏&&权限控制-->
	<%@include file="../inc/head.jsp"%>

	<!--内容栏-->
	<div class="rightinfo">

		
		<!--数据列表-->
			
			<div data-options="region:'center',border:false,singleSelect:true" id="apply"
				title='申请记录表' style="height:auto">
				<table id="datagridOfHistory"></table>
			</div>
		

	





		

	</div>
</body>
</html>