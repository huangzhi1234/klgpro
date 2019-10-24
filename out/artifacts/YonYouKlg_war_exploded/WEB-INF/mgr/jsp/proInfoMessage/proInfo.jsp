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
<%
	String pro = "proInfo";
%>
<script type="text/javascript"
	src="${ctx}/mgr/js/ux/proInfoMessage/proInfo.js"></script>
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

		<div id="class_required">

			<!-- 查询条件-->
			<form id="schForm">
				<ul class="seachform">
					<li><label>项目名称</label> <input id="proName" name="proName"
						type="text" class="scinput" /></li>
					<li><label>项目所属行业</label><input type="text" id="proIndu"
						name="proIndu" class="easyui-combobox dfinput"
						data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
					</li>
					<!-- <li>
						<label>产品线</label>
						<input name="proProductLine" class="easyui-combobox" data-options="editable:true,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',
						data: [{label: 'NC',value: 'NC'},{label: 'U8',value: 'U8'},{label: 'U9',value: 'U9'},
						{label: 'her',value: 'her'},{label: '原生',value: '原生'},{label: '移动',value: '移动'}]" />
					</li> -->
					<li><label>产品线</label><input type="text" id="proProductLine"
						name="proProductLine" class="easyui-combobox dfinput"
						data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
					<li><input type="button" class="scbtn" value="查询" /></li>
				</ul>
			</form>


			<!--功能按钮，项目基本信息-->
			<div id="tb_proInfo">
				<a href="javascript:void(0);" id="btnAdd_proInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">新增</a> <a
					href="javascript:void(0);" id="btnEdit_proInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">编辑</a> <a
					href="javascript:void(0);" id="btnWatch_proInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-detail',plain:true">查看</a> <a
					href="javascript:void(0);" id="btnDelete_proInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a><a
					href="javascript:void(0);" id="btnGrant" class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">授权</a>
				<!-- <a href="javascript:void(0);" id="importexcel" class="easyui-linkbutton" data-options="iconCls:'icon-up',plain:true">EXCEL导入</a> 
	    		<a href="javascript:void(0);" id="exportexcel" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true">EXCEL导出</a> -->
			</div>

			<!--功能按钮，项目成员信息-->
			<div id="tb_proMember" style="display:none;">
				<a href="javascript:void(0);" id="btnAdd_proMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">新增</a> <a
					href="javascript:void(0);" id="btnEdit_proMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">编辑</a> <a
					href="javascript:void(0);" id="btnWatch_proMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-detail',plain:true">查看</a> <a
					href="javascript:void(0);" id="btnDelete_proMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>

			<!--功能按钮，项目文档信息-->
			<div id="tb_proFileInfo" style="display:none;">
				<a href="javascript:void(0);" id="btnEdit_proFileInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">上传</a>
				<!-- <a href="javascript:void(0);" id="btnEdit_proFileInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">编辑</a>  -->
				<!-- <a href="javascript:void(0);" id="btnWatch_proFileInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-detail',plain:true">下载</a> -->
				<!-- <a href="javascript:void(0);" id="btnDelete_proFileInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a> -->
			</div>

			<!--功能按钮，项目源码信息-->
			<div id="tb_proSourceCode" style="display:none;">
				<a href="javascript:void(0);" id="btnAdd_proSourceCode"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">新增</a> <a
					href="javascript:void(0);" id="btnEdit_proSourceCode"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">编辑</a> <a
					href="javascript:void(0);" id="btnWatch_proSourceCode"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-detail',plain:true">查看</a> <a
					href="javascript:void(0);" id="btnDelete_proSourceCode"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>

			<!--功能按钮，项目子合同信息-->
			<div id="tb_proSubInfo" style="display:none;">
				<a href="javascript:void(0);" id="btnAdd_proSubInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">新增</a> <a
					href="javascript:void(0);" id="btnEdit_proSubInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">编辑</a> <a
					href="javascript:void(0);" id="btnWatch_proSubInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-detail',plain:true">查看</a> <a
					href="javascript:void(0);" id="btnDelete_proSubInfo"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>

			<!--功能按钮，项目客户信息-->
			<div id="tb_proCustomerMember" style="display:none;">
				<a href="javascript:void(0);" id="btnAdd_proCustomerMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">新增</a> <a
					href="javascript:void(0);" id="btnEdit_proCustomerMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">编辑</a> <a
					href="javascript:void(0);" id="btnWatch_proCustomerMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-detail',plain:true">查看</a> <a
					href="javascript:void(0);" id="btnDelete_proCustomerMember"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
			</div>


			<!--数据列表-->
			<div data-options="region:'center',border:false,singleSelect:true"
				title='项目基本信息' style="height:auto">
				<table id="datagridOfProInfo"></table>
			</div>

			<!-- 页签 -->
			<div id="tabs" class="easyui-tabs" style="height:380px;">
				<div data-options="region:'center',border:false" title='项目成员信息'
					style="height: 350px">
					<table id="datagridOfProMember"></table>
				</div>
				<div data-options="region:'center',border:false" title='项目子合同信息'
					style="height: 350px">
					<table id="datagridOfProSubInfo"></table>
				</div>
				<div data-options="region:'center',border:false" title='项目文档信息'
					style="height: 350px">
					<table id="datagridOfProFileInfo"></table>
				</div>
				<div data-options="region:'center',border:false" title='项目源码信息'
					style="height: 350px">
					<table id="datagridOfProSourceCode"></table>
				</div>
				<div data-options="region:'center',border:false" title='项目客户信息'
					style="height: 350px">
					<table id="datagridOfProCustomerMember"></table>
				</div>
			</div>

		</div>


		<!-- ==================================项目基本信息================================= -->
		<!--新增弹窗，项目基本信息	-->
		<div id="addWin" title="新增项目基本信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<form id="addForm" class="formbody"
				style="float:left;width:100%;higth:auto">
				<div class="form_div">
					<div class="formtitle">
						<span>新增项目基本信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目名称：</label> <input name="proName"
							class="easyui-validatebox textbox left vali"
							data-options="required:true,missingMessage:'项目名称不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>项目合同编号：</label> <input name="proPactNum"
							class="easyui-validatebox textbox left vali"
							data-options="required:true,missingMessage:'项目编号不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>项目所属行业：</label> <input type="text" id="proIndu2"
							name="proIndu" class="easyui-combobox dfinput left"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
							<span class="red" style="float: right;padding-right: 175px;">*</span>
						</li>
						<li><label>产品线：</label> <input type="text"
							id="proProductLine2" name="proProductLine"
							class="easyui-combobox dfinput "
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
							<span class="red" style="float: right;padding-right: 175px;">*</span>
						</li>
						<!-- <li><label>项目描述：</label><input name="proDis" type="text" class="easyui-validatebox dfinput"/></li> -->
						<li style="height:65px;"><label>项目描述：</label> <textarea
								rows="4" cols="40" name="proDis" style="padding:2px 8px;"
								class="left easyui-validatebox"
								data-options="required:true,missingMessage:'项目描述不能为空'"></textarea>
							<span class="red left">*</span></li>
						<li style="height:65px;"><label>启动时间：</label><input
							name="startTime" class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>验收时间：</label><input name="finishTime"
							class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>实际验收时间：</label><input name="finishTime2"
							class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>

						<li><label>当前阶段：</label><input type="text" id="currentStage2"
							name="currentStage" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li><label>客户名称：</label> <input name="custName" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'客户名称不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>客户联系人：</label> <input name="custConnect"
							type="text" class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'客户联系人不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>客户手机号码：</label><input name="phone" type="text"
							data-options="validType:'mobile'"
							class="easyui-validatebox left vali" /><span class="red left">*</span>
						</li>
						<li style="height:65px;"><label>项目交付模式：</label><input
							type="text" id="deliMode2" name="deliMode"
							class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li style="height:65px;"><label>备注：</label> <textarea
								style="padding:2px 8px;" rows="4" cols="40" name="comment"></textarea>
						</li>
					</ul>
					<div class="btn_div">
						<ul>
							<li>
								<button id="btnToAdd" class="btn ml-180">确认保存</button> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<!--	编辑弹窗，项目基本信息	-->
		<div id="editWin" title="编辑项目基本信息"
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="editForm" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>编辑项目基本信息</span>
					</div>
					<input name="proId" type="hidden"> <input name="proNum"
						type="hidden"> <!-- <input name="proPactNum" type="hidden"> -->
					<input name="createTime" type="hidden"> <input
						name="createOper" type="hidden">
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目名称：</label> <input name="proName" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'项目名称不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>项目合同编号：</label> <input name="proPactNum"
							type="text" class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'项目合同编号不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>项目所属行业：</label> <input type="text" id="proIndu3"
							name="proIndu" class="easyui-combobox vali left"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
							<span class="red" style="float: right;padding-right: 166px;">*</span>
						</li>
						<li><label>产品线：</label> <input type="text"
							id="proProductLine3" name="proProductLine"
							class="easyui-combobox vali left"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
							<span class="red" style="float: right;padding-right: 166px;">*</span>
						</li>
						<li style="height:65px;"><label>项目描述：</label> <textarea
								rows="4" cols="40" name="proDis" class="left easyui-validatebox"
								style="padding:2px 8px;"
								data-options="required:true,missingMessage:'项目描述不能为空'">
							</textarea> <span class="red left">*</span></li>
						<li style="height:65px;"><label>启动时间：</label><input
							id="startTime" name="startTime" class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>验收时间：</label><input id="finishTime"
							name="finishTime" class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>实际验收时间：</label><input name="finishTime2"
							id="finishTime2" class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>当前阶段：</label><input type="text" id="currentStage3"
							name="currentStage" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
						</li>
						<li><label>客户名称：</label> <input name="custName" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'客户名称不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>客户联系人：</label> <input name="custConnect"
							type="text" class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'客户联系人不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>客户手机号码：</label> <input name="phone" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,validType:'mobile',missingMessage:'客户联系方式不能为空'" />
							<span class="red left">*</span></li>
						<li style="height:65px;"><label>项目交付模式：</label><input
							type="text" id="deliMode3" name="deliMode"
							class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li style="height:65px;"><label>备注：</label> <textarea
								rows="4" cols="40" name="comment" style="padding:2px 8px;"></textarea>
						</li>
					</ul>
					<div class="btn_div">
						<ul>
							<li>
								<button id="btnToUpdate" class="btn ml-180">确认更新</button> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a>
							</li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	查看弹窗，项目基本信息	-->
		<div id="watchWin" title="查看项目基本信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="watchForm" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>查看项目基本信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">返回</a></li>
						</ul>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><label id="proNum_watch"
							style="text-align:left;;"></label></li>
						<li><label>项目合同编号：</label><label id="proPactNum_watch"
							style="text-align:left;;"></label></li>
						<li><label>项目名称；</label><label id="proName_watch"
							style="text-align:left;;"></label></li>
						<li><label>项目所属行业：</label><label id="proIndu_watch"
							style="text-align:left;;"></label></li>
						<li><label>产品线：</label><label id="proProductLine_watch"
							style="text-align:left;"></label></li>
						<li><label>项目描述：</label><label id="proDis_watch"
							style="text-align:left;"></label></li>
						<li><label>启动时间：</label><label id="startTime_watch"
							style="text-align:left;"></label></li>
						<li><label>验收时间：</label><label id="finishTime_watch"
							style="text-align:left;"></label></li>
						<li><label>实际验收时间：</label><label id="finishTime2_watch"
							style="text-align:left;"></label></li>
						<li><label>当前阶段：</label><label id="currentStage_watch"
							style="text-align:left;"></label></li>
						<li><label>客户名称：</label><label id="custName_watch"
							style="text-align:left;"></label></li>
						<li><label>客户联系人：</label><label id="custConnect_watch"
							style="text-align:left;"></label></li>
						<li><label>客户手机号码：</label><label id="phone_watch"
							style="text-align:left;"></label></li>
						<li><label>项目交付模式：</label><label id="deliMode_watch"
							style="text-align:left;"></label></li>
						<li><label>备注：</label><label id="comment_watch"
							style="text-align:left;"></label></li>
						<li></li>
					</ul>
				</form>
			</div>
		</div>

		<!-- ==================================项目成员信息================================= -->
		<!--	新增弹窗，项目成员信息	-->
		<div id="addWin_proMember" title="新增项目成员信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="addForm_proMember" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>新增项目成员信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><input name="proNum" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>项目名称：</label><input name="proName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>姓名：</label><input name="memberName" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'姓名不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>角色：</label><input type="text" id="roleaName2"
							name="roleaName" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
							<span class="red" style="float: right;padding-right: 183px;">*</span>
						</li>
						<li><label>手机号码：</label> <input name="phone" type="text"
							class="easyui-validatebox left vali"
							data-options="required:true,validType:'mobile'" /> <span
							class="red left">*</span></li>
						<li><label>负责领域：</label><input name="responsibleArea"
							type="text" class="easyui-validatebox dfinput" /></li>
						<li style="height:65px;"><label>邮件：</label> <input
							name="email" type="text" class="easyui-validatebox left vali"
							data-options="required:true,validType:'email'" /> <span
							class="red left">*</span></li>
						<!-- <li style="height:65px;"><label>负责模块：</label><input name="delyModule" type="text" class="easyui-validatebox dfinput" /></li> -->
						<li style="height:65px;"><label>工作描述：</label> <textarea
								rows="4" cols="40" name="delyModule"></textarea></li>
						<li style="height:65px;"><label>备注：</label> <textarea
								rows="4" cols="40" name="comment"></textarea></li>
						<li style="height:65px;"></li>
					</ul>
					<div class="btn_div">
						<ul>
							<li><button id="btnToAdd_proMember" class="btn ml-180">确认保存</button>
								<a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	编辑弹窗，项目成员信息	-->
		<div id="editWin_proMember" title="编辑项目成员信息"
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="editForm_proMember" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>编辑项目成员信息</span>
					</div>
					<input name="memberId" type="hidden"> <input
						id="proNum_proMember" name="proNum" type="hidden">
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>姓名：</label> <input name="memberName" type="text"
							data-options="required:true,missingMessage:'姓名不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>角色：</label> <input type="text" id="roleaName3"
							name="roleaName" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" />
							<span class="red" style="float: right;padding-right: 183px;">*</span>
						</li>

						<li><label>手机号码：</label> <input name="phone" type="text"
							data-options="required:true,validType:'mobile'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>邮件：</label> <input name="email" type="text"
							class="easyui-validatebox vali left "
							data-options="required:true,validType:'email'" /> <span
							class="red left">*</span></li>

						<li style="height:65px;"><label>负责模块：</label><input
							name="delyModule" type="text" class="easyui-validatebox dfinput" /></li>
						<li style="height:65px;"><label>备注：</label> <textarea
								rows="4" cols="40" name="comment" style="padding: 2px 8px;"></textarea>
						</li>
					</ul>
					<div class="btn_div">
						<ul>
							<li><button id="btnToUpdate_proMember" class="btn ml-180">确认保存</button>
								<a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	查看弹窗，项目成员信息	-->
		<div id="watchWin_proMember" title="查看项目成员信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="watchForm_proMember" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>查看项目成员信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">返回</a></li>
						</ul>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><label id="proNum_watch_proMember"
							style="text-align:left;;"></label></li>
						<li><label>姓名：</label><label id="memberName_watch_proMember"
							style="text-align:left;;"></label></li>

						<li><label>角色：</label><label id="roleaName_watch_proMember"
							style="text-align:left;;"></label></li>
						<li><label>手机号码：</label><label id="phone_watch_proMember"
							style="text-align:left;;"></label></li>

						<li><label>邮件：</label><label id="email_watch_proMember"
							style="text-align:left;;"></label></li>
						<li><label>负责模块：</label><label
							id="delyModule_watch_proMember" style="text-align:left;;"></label></li>

						<li><label>备注：</label><label id="comment_watch_proMember"
							style="text-align:left;;"></label></li>
						<li></li>
					</ul>

				</form>
			</div>
		</div>

		<!-- 时间戳 -->
		<div id="cc" class="easyui-calendar"></div>

		<!-- ==================================项目文档信息================================= -->
		<!--	上传弹窗，项目文档信息	-->
		<div id="editWin_proFileInfo" title="上传项目文档信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="editForm_proFileInfo" class="formbody"
					action="${ctx}/mgr/proFile/add.json?pro=<%=pro %>" method="post"
					enctype="multipart/form-data"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>上传项目文档信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目名称：</label><input name="proName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /> <input
							type="hidden" name="proNum" /></li>
						<li><label>项目阶段：</label><input name="fileName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /> <!-- <input type="hidden" name="proStage"/> -->
							<input name="fileId" type="hidden"></li>

						<!-- <li><label>项目文档附件：</label><label id="proFileInfo_filePath"
							style="text-align:left;width:auto;"></label><input class="easyui-filebox"
							name="fileOne" data-options="prompt:'请选择您要上传的文件'"
							style="width:320px;"></li> -->
						<li><label>项目文档附件：</label> <!-- <label id="attachAddress_edit"
						style="text-align:left;width:auto;"></label> --> <!-- <a id="attachAddress" class="atn">删除</a> -->
							<input class="easyui-filebox" name="fileOne"
							data-options="prompt:'请选择您要上传的文件'" style="width:250px;"></li>
					</ul>
					<div class="from_submit_div">
						<ul>
							<li><input type="submit" class="btn" id="upload" value="确认保存" /> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		






		
		<!--	查看弹窗，项目文档信息	-->
		<div id="watchWin_proFileInfo" title="查看项目文档信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<table id="tg"></table>
			<div
				style="width:50px;line-height: 32px;background: skyblue;text-align: center;margin-top: 20px;">
				<a href="javascript:void(0);" onclick="return_main()"
					style="color:white;font-size: 16px;">返回</a>
			</div>
		</div>



		<!-- ==================================项目源码信息================================= -->
		<!--	新增弹窗，项目源码信息	-->
		<div id="addWin_proSourceCode" title="新增项目源码信息"
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="addForm_proSourceCode" class="formbody"
					action="${ctx}/mgr/proSourceCode/add.json?pro=<%=pro %>"
					method="post" enctype="multipart/form-data"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>新增项目源码信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><input name="proNum" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>项目名称：</label><input name="proName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>源码名称：</label><input name="codeName" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'源码名称不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>源码类型：</label><input type="text" id="codeType2"
							name="codeType" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li><label>源码版本：</label><input name="codeVersion" type="text"
							data-options="required:true,missingMessage:'源码版本不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>作者：</label><input type="text" id="author1"
							name="author" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li style="height:75px;"><label>项目源码附件：</label><input
							class="easyui-filebox" name="fileThree"
							data-options="prompt:'请选择您要上传的文件'" style="width:320px;"></li>
						<!--  <li><label>代码描述：</label><input name="codeComment" type="text" class="easyui-validatebox dfinput" /></li> -->
						<li style="height:75px;"><label>代码描述：</label> <textarea
							style="padding: 8px;"	rows="4" cols="40" name="codeComment"></textarea></li>
						<!-- <li><label>备注：</label><input name="comment" type="text" class="easyui-validatebox dfinput" /></li>	 -->
						<li style="height:75px;"><label>备注：</label> <textarea
							style="padding: 8px;"	rows="4" cols="40" name="comment"></textarea></li>
						<li style="height:75px;"></li>
					</ul>
					<div class="from_submit_div">
						<ul>
							<li><input type="submit" class="btn" value="确认保存"
								onclick="return proSource_submit()" /> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	编辑弹窗，项目源码信息	-->
		<div id="editWin_proSourceCode" title="编辑项目源码信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="editForm_proSourceCode" class="formbody"
					action="${ctx}/mgr/proSourceCode/update.json?pro=<%=pro %>"
					method="post" enctype="multipart/form-data"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>编辑项目源码信息</span>
					</div>
					<input name="fileId" type="hidden"> <input
						id="proNum_proSourceCode" name="proNum" type="hidden"> <input
						id="codeNum" name="codeNum" type="hidden">
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>源码名称：</label><input name="codeName" type="text"
							data-options="required:true,missingMessage:'源码名称不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>源码类型：</label><input type="text" id="codeType3"
							name="codeType" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li><label>源码版本：</label><input name="codeVersion" type="text"
							data-options="required:true,missingMessage:'源码版本不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>作者：</label><input type="text"
							id="author2" name="author" class="easyui-combobox dfinput"
							data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li style="height:75px;"><label>项目源码文件：</label><label
							id="codeDiscFile_edit" style="text-align:left;width:auto;"></label>
							<!-- <a id="codeDiscFile" class="atn">删除</a> --> <input
							class="easyui-filebox" name="fileFour"
							data-options="prompt:'请选择您要上传的文件'" style="width:240px;"></li>
						<li style="height:75px;"><label>代码描述：</label> <textarea
							style="padding: 8px;"	rows="4" cols="40" name="codeComment"></textarea></li>
						<!-- <li><label>备注：</label><input name="comment" type="text" class="easyui-validatebox dfinput" /></li> -->
						<li style="height:75px;"><label>备注：</label> <textarea
							style="padding: 8px;"	rows="4" cols="40" name="comment"></textarea></li>
						<li style="height:75px;"></li>
					</ul>
					<div class="from_submit_div">
						<ul>
							<li><input type="submit" class="btn" value="确认更新"
								onclick="return proSource_update()" /> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	查看弹窗，项目源码信息	-->
		<div id="watchWin_proSourceCode" title="查看项目文档信息"
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="watchForm_proSourceCode" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>查看项目源码信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">返回</a></li>
						</ul>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">

						<li><label>项目编号：</label><label
							id="proNum_watch_proSourceCode" style="text-align:left;"></label></li>
						<li><label>源码名称：</label><label
							id="codeName_watch_proSourceCode" style="text-align:left;"></label></li>

						<li><label>项目阶段：</label><label
							id="codeType_watch_proSourceCode" style="text-align:left;"></label></li>
						<li><label>源码类型：</label><label
							id="codeVersion_watch_proSourceCode" style="text-align:left;"></label></li>

						<li><label>作者：</label><label id="author_watch_proSourceCode"
							style="text-align:left;"></label></li>
						<li><label>源码文件：</label><label id="codeDiscFile_watch"
							style="text-align:left;width:auto;"></label><a
							id="codeDiscFileDown" class="atn">下载</a><a id="codeDiscFileRead"
							class="atn">预览</a></li>

						<li><label>代码描述：</label><label
							id="codeComment_watch_proSourceCode" style="text-align:left;"></label></li>
						<li><label>备注：</label><label id="comment_watch_proSourceCode"
							style="text-align:left;"></label></li>
					</ul>

				</form>
			</div>
		</div>


		<!-- ==================================项目子合同信息================================= -->
		<!--	新增弹窗，项目子合同信息	-->
		<div id="addWin_proSubInfo" title="新增项目子合同信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="addForm_proSubInfo" class="formbody"
					action="${ctx}/mgr/proSubInfo/add.json?pro=<%=pro %>" method="post"
					enctype="multipart/form-data"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>新增项目子合同信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label> <input name="proNum" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>项目名称：</label> <input name="proName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>子合同编号：</label> <input name="proSubNum" type="text"
							data-options="required:true,missingMessage:'子合同编号不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>子合同名称：</label> <input name="proSubName"
							type="text"
							data-options="required:true,missingMessage:'子合同名称不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>签约时间：</label> <input name="signTime" type="text"
							class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>项目文档附件：</label><input class="easyui-filebox"
							name="fileOne6" data-options="prompt:'请选择您要上传的文件'"
							style="width:320px;"></li>
						<li style="height:65px;"><label>子合同描述：</label> <textarea
								rows="4" cols="40" name="proSubDis"></textarea></li>
						<li style="height:65px;"></li>
					</ul>
					<div class="from_submit_div">
						<ul>
							<li><input type="submit" onclick="return proSub_submit()"
								class="btn" value="确认保存" /> <a href="javascript:void(0);"
								class="btn ml-180" onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	编辑弹窗，项目子合同信息	-->
		<div id="editWin_proSubInfo" title="编辑项目子合同信息"
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="editForm_proSubInfo" class="formbody"
					action="${ctx}/mgr/proSubInfo/update.json?pro=<%=pro %>"
					method="post" enctype="multipart/form-data"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>编辑项目子合同信息</span>
					</div>
					<input name="subId" type="hidden">
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><input name="proNum" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>项目名称：</label><input name="proName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>子合同编号：</label><input name="proSubNum" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'子合同编号不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>子合同名称：</label><input name="proSubName" type="text"
							class="easyui-validatebox left vali"
							data-options="required:true,missingMessage:'子合同名称不能为空'" /> <span
							class="red left">*</span></li>
						<li><label>签约时间：</label><input name="signTime" id="signTime1"
							type="text" class="easyui-datebox dfinput"
							data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
						<li><label>项目文档附件：</label><label id="proSubInfo_filePath"
							style="text-align:left;width:auto;"></label> <!-- <a id="proSubInfo_filePath_del" class="atn">删除</a> -->
							<input class="easyui-filebox" name="fileTwo"
							data-options="prompt:'请选择您要上传的文件'" style="width:240px;"></li>
						<li style="height:65px;"><label>子合同描述：</label> <textarea
								rows="4" cols="40" name="proSubDis"></textarea></li>
						<li style="height:65px;"></li>
					</ul>
					<div class="from_submit_div">
						<ul>
							<li><input type="submit" class="btn" value="确认更新"
								onclick="return proSub_update()" /> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	查看弹窗，项目子合同信息	-->
		<div id="watchWin_proSubInfo" title="查看项目子合同信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="watchForm_proSubInfo" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>查看项目子合同信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">返回</a></li>
						</ul>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><label id="proNum_watch_proSubInfo"
							style="text-align:left;"></label></li>
						<li><label>项目名称：</label><label id="proName_watch_proSubInfo"
							style="text-align:left;"></label></li>

						<li><label>子合同编号：</label><label
							id="proSubNum_watch_proSubInfo" style="text-align:left;"></label></li>
						<li><label>子合同名称：</label><label
							id="proSubName_watch_proSubInfo" style="text-align:left;"></label></li>

						<li><label>签约时间：</label><label id="signTime_watch_proSubInfo"
							style="text-align:left;"></label></li>
						<li><label>项目文档附件：</label><label
							id="proSubInfo_filePath_Watch"
							style="text-align:left;width:auto;"></label> <a
							id="proSubInfo_filePath_Down" class="atn">下载</a></li>

						<li style="height:auto;"><label>子合同描述：</label><label
							id="proSubDis_watch_proSubInfo" style="text-align:left;"></label></li>
						<!-- <li style="height:auto;"></li> -->
					</ul>
				</form>
			</div>
		</div>

		<!-- ==================================项目客户信息================================= -->
		<!--	新增弹窗，项目客户信息	-->
		<div id="addWin_proCustomerMember" title="新增项目源码信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="addForm_proCustomerMember" class="formbody"
					action="${ctx}/mgr/proCustomerMember/add.json?pro=<%=pro %>"
					method="post" enctype="multipart/form-data"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>新增项目客户信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><input name="proNum" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>项目名称：</label><input name="proName" type="text"
							class="easyui-validatebox dfinput" readonly="readonly" /></li>
						<li><label>客户名称：</label><input name="cusName" type="text"
							data-options="required:true,missingMessage:'客户名称不能为空'"
							class="easyui-validatebox vali left" /> <span class="red left">*</span>
						</li>
						<li><label>角色：</label><input name="cusRole" id="cusRole_cus"
							data-options="editable:false,required:true,missingMessage:'角色不能为空'"
							class="easyui-combobox" /> <span class="red"
							style="float: right;padding-right: 183px;">*</span></li>
						<li><label>联系电话：</label><input name="phone" type="text"
							class="easyui-validatebox vali left"
							data-options="required:true,validType:'mobile'" /> <span
							class="red left">*</span></li>
						<li><label>邮箱：</label><input name="email" type="text"
							class="easyui-validatebox left vali"
							data-options="required:true,validType:'email'" /> <span
							class="red left">*</span></li>
						<li style="height:65px;"><label>责任领域：</label><input
							type="text" id="author" name="responsibleArea"
							class="easyui-validatebox dfinput" /></li>
						<li style="height:65px;"><label>工作描述：</label> <textarea
							style="padding:8px;"	rows="4" cols="40" name="delyModule"></textarea></li>
					</ul>
					<div class="from_submit_div">
						<ul>
							<li><input type="submit" class="btn" value="新增"
								onclick="return proCus_submit()" /> <a
								href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>

		<!--	编辑弹窗，项目客户信息	-->
		<div id="editWin_proCustomerMember" title="编辑项目客户信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="editForm_proCustomerMember" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>编辑项目客户信息</span>
					</div>
					<input name="customerId" type="hidden">
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li style="height:47px;padding-top: 23px;"><label>项目编号：</label><input
							name="proNum" type="text" readonly="readonly"
							class="easyui-validatebox dfinput" /></li>
						<li style="height:47px;padding-top: 23px;"><label>客户名称：</label><input
							name="cusName" class="easyui-validatebox vali left"
							data-options="required:true,missingMessage:'客户名称不能为空'" /> <span
							class="red left">*</span></li>
						<li style="height:47px;padding-top: 23px;"><label>角色：</label><input
							name="cusRole" type="text"
							data-options="editable:false,required:true,missingMessage:'角色不能为空'"
							class="easyui-combobox" id="cusRole_cus2" /> <span class="red"
							style="float: right;padding-right: 183px;">*</span></li>
						<li style="height:47px;padding-top: 23px;"><label>联系电话：</label><input
							name="phone" type="text" class="easyui-validatebox vali left"
							data-options="required:true,validType:'mobile'" /> <span
							class="red left">*</span></li>
						<li style="height:47px;padding-top: 23px;"><label>邮箱：</label><input
							name="email" type="text" class="easyui-validatebox left vali"
							data-options="required:true,validType:'email'" /> <span
							class="red left">*</span></li>
						<li style="height:47px;padding-top: 23px;"><label>责任领域：</label><input
							name="responsibleArea" type="text"
							class="easyui-validatebox dfinput" /></li>
						<li style="height:71px;padding-top: 23px;"><label>工作描述：</label>
							<textarea id="delyModule" style="padding:8px;" rows="4" cols="40" name="delyModule"></textarea></li>
					</ul>
					<div class="btn_div" style="margin: 0px 360px;">
						<ul>
							<li><button id="btnToUpdate_proCustomerMember"
									class="btn ml-180">确认保存</button> <a href="javascript:void(0);"
								class="btn ml-180" onclick="return_main()">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>


		<!--	查看弹窗，项目客户信息	-->
		<div id="watchWin_proCustomerMember" title="查看项目文档信息" 
			style="float:left;width:100%;higth:auto;display: none;">
			<div class="form_div">
				<form id="watchForm_proCustomerMember" class="formbody"
					style="float:left;width:100%;higth:auto">
					<div class="formtitle">
						<span>查看项目客户信息</span>
					</div>
					<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
						<li><label>项目编号：</label><label
							id="proNum_watch_proCustomerMember" style="text-align:left;"></label></li>
						<li><label>客户名字：</label><label
							id="cusName_watch_proCustomerMember" style="text-align:left;"></label></li>
						<li><label>角色：</label><label
							id="cusRole_watch_proCustomerMember" style="text-align:left;"></label></li>
						<li><label>联系电话：</label><label
							id="phone_watch_proCustomerMember" style="text-align:left;"></label></li>
						<li><label>邮箱：</label><label
							id="email_watch_proCustomerMember" style="text-align:left;"></label></li>
						<li><label>责任领域：</label><label
							id="responsibleArea_watch_proCustomerMember"
							style="text-align:left;"></label></li>
						<li><label>工作描述：</label><label
							id="delyModule_watch_proSourceCode" style="text-align:left;"></label></li>
					</ul>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">返回</a></li>
						</ul>
					</div>
				</form>
			</div>
		</div>






		<!-- ========================对用户进行查看项目的的权限进行授权 =======================================-->
		<!-- 对用户进行查看项目的的权限进行授权 -->
		<div id="grantWin" class="easyui-dialog" title="项目授权"
			data-options="closed:true,iconCls:'icon-man',modal:true"
			style="width:900px;height:400px;">
			<form id="grantForm" class="formbody">
				<input type="hidden" name="proId" id="grantProId" />
				<div class="formtitle">
					<span>项目授权</span>
				</div>
				<input name="proId" type="hidden">
				<table class="formtable">
					<tr>
						<td><label>部门</label></td>
						<td></td>
						<td><label>该部门未授权的用户</label></td>
						<td></td>
						<td><label>该项目已授权的用户</label></td>
					</tr>
					<tr>
						<td><div style=" overflow-y:auto; overflow-x:auto; width:200px; height:150px;"><select id="deptselect" name="deptselect"
							multiple="multiple" size="8" class="textinput"></select></div></td>
						<td></td>
						<td><div style=" overflow-y:auto; overflow-x:auto; width:200px; height:150px;"><select id="unselect" name="unselect" multiple="multiple"
							size="8" class="textinput"></select></div></td>
						<td><input type="button" value="  >>  " class="tbbtn" /> <br />
							<br /> <br /> <br /> <input type="button" value="  <<  "
							class="tbbtn" /></td>
						<td><div style=" overflow-y:auto; overflow-x:auto; width:200px; height:150px;"><select id="select" name="select" multiple="multiple"
							size="8" class="textinput"></select></div></td>
					</tr>
					<tr>
						<td colspan="3"><input id="btnToGrant" type="button"
							class="btn" value="确认授权" /></td>
					</tr>
				</table>
			</form>
		</div>

	</div>
	
	<!-- 创建遮罩层：作用于上传过程中，用户无法操作 -->
	<div id="zhezhao" style="background-color:#FFFAFA;position:fixed;left:0;top:0;width:100%;opacity: 0.6;height:100%;z-index:99999;display:none" >
	</div>
</body>
</html>