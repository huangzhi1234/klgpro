<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>标准文档下载历史记录</title>

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI");
%>
<%@include file="../inc/import.jsp"%>
<%
	String pro = "proInfo";
%>
<script type="text/javascript"
	src="${ctx}/mgr/js/ux/stDoc/stHistory2.js"></script>
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

		<div id="class_required">

			<!-- 查询条件-->
			<form id="schForm">
				<ul class="seachform" style="width:100%;">
					<li><label>文件名称</label>
					<input type="text" name="fileName" class="scinput" />
					</li>
					<li><label>状态</label>
						<input type="text" id="isOk"
						name="isOk" class="easyui-combobox dfinput"
						data-options="editable:false,panelHeight:'auto',
						valueField: 'label',textField: 'value',data: [{label: '0',value: '申请中'},{label: '1',value: '已通过'},{label: '2',value: '未通过'}]"
						 />
					</li>
					<li>
						<label>申请日期</label>
						<input name="time" class="easyui-datebox dfinput" 
							data-options="editable:false,sharedCalendar:'#cc',height:32" />
					</li>
					<!-- <li>
						<label>截止日期</label>
						此处为了使name不冲突，借用了agreeTime字段 
						<input name="agreeTime" class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" />
					</li> -->
					<li><input type="button" class="scbtn" value="查询"  /></li>
				</ul>
			</form>
			
			
			
			
		</div>
		<!--数据列表-->
			<br><br>
			<div data-options="region:'center',border:false,singleSelect:true" id="apply"
				title='申请记录表' style="height:auto">
				<table id="datagridOfHistory"></table>
			</div>
		

	<!-- 时间戳 -->
		<div id="cc" class="easyui-calendar"></div>

	




		

	</div>
</body>
</html>