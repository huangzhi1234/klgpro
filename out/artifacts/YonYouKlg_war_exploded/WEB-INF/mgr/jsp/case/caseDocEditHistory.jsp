<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>项目信息管理——项目基本信息管理</title>

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI");
%>
<%@include file="../inc/import.jsp"%>

<script type="text/javascript"
	src="${ctx}/mgr/js/ux/case/caseDocEditHistory.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/mgr/css/proInfo/proInfo.css">

<style type="text/css">
	.pagination-info{
		margin:0px 38px 0px 0px;
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

		<div id="class_required">

			 <!-- 查询条件-->
			<form id="schForm">
				<ul class="seachform">
				<li><label>类型</label><input type="text" name="type" id="type" class="easyui-combobox dfinput" style="width:200px;"data-options="valueField:'id', textField:'text',panelHeight:'auto'"/></li>
				<li><label>文件名称</label> <input type="text" class="scinput" name="fileName" id="achName"/></li>
				<li><label>状态</label> <input type="text"  name="isOk"  id="pass"class="easyui-combobox dfinput" style="width:200px;"data-options="valueField:'id', textField:'text',panelHeight:'auto'"/></li>
				
					<li><input type="button" class="scbtn" value="查询"  /></li>
					</li>
				</ul>
			</form>
			
			<h2 style="height: 40px;line-height: 40px;font-size: 20px;">
			上传审批记录
				<span href="javascript:void(0);" id="up"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true"></span>
				<a href="javascript:void(0);" id="upEdit"
					class="easyui-linkbutton" style="float:right;margin-right:10px;line-height: 35px;"
					data-options="iconCls:'icon-edit',plain:true">修改</a>
			</h2>
			
			<!--数据列表-->
			<div data-options="region:'center',border:false,singleSelect:true" style="height:auto" id="upData">
				<table id="datagridOfDocUp"></table>
			</div>
			
			<h2 style="height: 40px;line-height: 40px;font-size: 20px;">下载审批记录<span href="javascript:void(0);" id="down"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true" ></span>
					<a href="javascript:void(0);" id="downEdit"
					class="easyui-linkbutton" style="float:right;margin-right:10px;line-height: 35px;"
					data-options="iconCls:'icon-edit',plain:true">修改</a></h2>
			<div data-options="region:'center',border:false,singleSelect:true" style="height:auto" id="downData">
				<table id="datagridOfDocDown"></table>
			</div>
			
		</div>

	</div>
	
	
		<!-- 修改上传审批信息 -->
		<div id="editWin2" class="easyui-dialog" title="修改上传审批信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:420px;">
			<form id="editForm2" class="formbody">
				<div class="formtitle"><span>修改上传审批信息</span></div>
					<input name="uploadId" type="hidden" id="uploadId">
					<ul class="forminfo">
						<li><label>状态</label><input id="isOk2" name="isOk" type="text" class="easyui-combobox dfinput" data-options="valueField:'id', textField:'text',panelHeight:'auto'" /></li>
						<li><label>备注</label><textarea
								rows="4" cols="40" name="remark" style="padding:2px 8px;"
								class="left easyui-validatebox" id="remark2"
								data-options="required:true,missingMessage:'不通过时的备注不能为空'"></textarea></li>
						<li><label>&nbsp;</label><input id="doc_up_updata" type="button" class="btn" value="确认更新"/></li>
					</ul>
			</form>
		</div>
	
	
		<!-- 修改下载审批信息 -->
		<div id="editWin" class="easyui-dialog" title="修改下载审批信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:420px;">
			<form id="editForm" class="formbody">
				<div class="formtitle"><span>修改下载审批信息</span></div>
					<input name="checkId" type="hidden" id="checkId">
					<ul class="forminfo">
						<li><label>状态</label><input id="isOk" name="isOk" type="text" class="easyui-combobox dfinput" data-options="valueField:'id', textField:'text',panelHeight:'auto'" /></li>
						<li><label>备注</label><textarea
								rows="4" cols="40" name="remark" style="padding:2px 8px;"
								class="left easyui-validatebox" id="remark"
								data-options="required:true,missingMessage:'备注不能为空'"></textarea></li>
						<li><label>&nbsp;</label><input id="doc_down_updata" type="button" class="btn" value="确认更新"/></li>
					</ul>
			</form>
		</div>
</body>
</html>