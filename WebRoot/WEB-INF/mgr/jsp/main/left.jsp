<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用友项目资产管理系统</title>
<%
	request.setAttribute("import", "easyUI,jQueryForm");
%>
<%@include file="../inc/import.jsp"%>
<script type="text/javascript" src="${ctx}/mgr/js/ux/main/left.js"></script>

<script type="text/javascript">
	function IFrameResize() {
		alert(this.document.body.scrollHeight); //弹出当前页面的高度
		var obj = parent.document.getElementById("leftIframe"); //取得父页面IFrame对象
		alert(obj.height); //弹出父页面中IFrame中设置的高度
		obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度
	}
</script>
</head>

<body class="leftbody" style="width: 180px;">
	<div>
		<c:import url="/mgr/main/tree" />
	</div>
</body>
</html>