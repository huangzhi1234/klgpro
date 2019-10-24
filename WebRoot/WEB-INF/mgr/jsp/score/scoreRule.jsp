<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>积分规则管理</title>

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI");
%>
<%@include file="../inc/import.jsp"%>
<%
	String pro = "proInfo";
%>
<script type="text/javascript"
	src="${ctx}/mgr/js/ux/score/scoreRule.js"></script>
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
/*取消全选按钮 */
.datagrid-header-check input{
	display: none;
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
			<div id="tb_Info">
			 <a href="javascript:void(0);" id="btnEdit"
					class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a> 
			</div>
		
		<!--数据列表-->
			
			<div data-options="region:'center',border:false,singleSelect:true" id="apply"
				title='申请记录表' style="height:auto">
				<table id="datagridOfHistory"></table>
			</div>
		
			<!--	编辑弹窗	-->
			<div id="editWin" class="easyui-dialog" title="编辑角色信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:420px;">
				<form id="editForm" class="formbody">
					<div class="formtitle"><span>角色基本信息</span></div>
						<input name="ruleId" type="hidden">
						<ul class="forminfo">
							<li><label>类型</label><input name="type" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>分数</label><input name="score" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="分数不能为空"/></li>
							<li><label>&nbsp;</label><input id="btnToUpdate" type="button" class="btn" value="确认更新"/></li>
						</ul>
				</form>
			</div>
	





		

	</div>
</body>
</html>