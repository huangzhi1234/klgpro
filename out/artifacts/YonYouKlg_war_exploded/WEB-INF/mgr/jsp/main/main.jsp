<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>用友项目资产管理系统</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI,namespace,jsUtil,commonCss");
%>
<%@include file="../inc/import.jsp"%>
<script type="text/javascript" src="${ctx}/mgr/js/ux/main/main.js"></script>
<script type="text/javascript">
	
</script>
</head>

<body id="pms_layout" class="easyui-layout">
	<div
		data-options="region:'north',href:'${pageContext.request.contextPath}/mgr/main/top'"
		style="height:90px;padding:0;overflow: hidden;"></div>
	<div data-options="region:'west'" style="width:180px;">
		<iframe id="leftIframe"
			src="${pageContext.request.contextPath}/mgr/main/left"
			scrolling="auto" frameborder="0" height="100%" style="width:175px;"></iframe>
	</div>
	<div data-options="region:'center'" id="centerDiv" style="padding:0px;">

		<div id="pms_center_tabs" class="easyui-tabs"
			style="height:$('#centerDiv').attr('height');">

			<div title="桌面" style="width: 100%;height: 100%;"
				data-options="iconCls:'icon-house',href:'${pageContext.request.contextPath}/mgr/main/right'">
			</div>
		</div>
	</div>
</body>
</html>
