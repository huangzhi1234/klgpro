<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>实时变动——设立企业</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script src="${ctx}/mgr/js/ux/realtime/setCompany.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
  </head>

<body >
		<!--导航栏&&权限控制-->
		<%@include file="../inc/head.jsp" %>
		
		<!--内容栏-->
		<div class="rightinfo">
			<!-- 查询条件-->
			<form id="schForm">
				<ul class="seachform">
					<li>
						<label>项目名称</label>
						<input name="projectName" type="text" class="scinput" />
					</li>
					<li>
						<label>立项时间</label>
						<input name="startTime" class="easyui-datebox scinput" data-options="sharedCalendar:'#cc',height:27"/> 
					</li>																													
					<li>					
						<input type="button" class="scbtn" value="查询" />
					</li>
				</ul>
			</form>
	
			<!--功能按钮-->
			<div id="tb">  
	    		<a id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>  
	    		<a id="btnEdit" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>
	    		<a id="btnWatch" class="easyui-linkbutton" data-options="iconCls:'icon-detail',plain:true">查看</a> 
	    		<a id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	    		<a id="btnSubmit" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true">提交</a>
			</div>
								 
			<!--数据列表-->
			<div data-options="region:'center',border:false,singleSelect:true" title='设立企业' style="height: 350px">
				<table id="datagrid"></table>
			</div> 
								
			<!--新增弹窗-->
			<div id="addWin" class="easyui-dialog" title="新增设立企业信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:570px;height:450px;">
				<form id="addForm" class="formbody">
					<div class="formtitle"><span>新增设立企业信息</span></div>
						<ul class="forminfo"> 
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input id="dept1" name="zhuBanDept" class="easyui-combotree" data-options="panelHeight:'auto',width:247,height:32" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>实施主体</label><input id="combo1" name="doBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>						    
						    <li><label>新设企业名称</label><input name="companyName" type="text" class="easyui-validatebox dfinput"   /></li>					    
						    <li><label>注册资本</label><input id="zhuceziben" name="signCapital" type="text" class="easyui-validatebox dfinput" onkeyup="calculation1()" />万元</li>
						    <li><label>我公司认缴注册资本</label><input id="renjiao" name="myPlanCapital" type="text" class="easyui-validatebox dfinput" onkeyup="calculation1()"  />万元</li>
						    <li><label>我公司实缴注册资本</label><input name="myReallyCapital" type="text" class="easyui-validatebox dfinput"   />万元</li>
						    <li><label>我公司持股比例</label><input id="chigubili" name="myRatio" type="text" class="easyui-validatebox dfinput"   />%</li>
						    <li><label>我公司实际投资额</label><input name="myReallyAmount" type="text" class="easyui-validatebox dfinput"   />万元</li>
						    <li><label>发起时间</label><input name="startTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>	
						    <li><label>预计结束时间</label><input name="endTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>						    
							<li><button id="btnToAdd" class="btn ml-180">确认保存</button></li>
						</ul>
				</form>
			</div>
			<div id="cc" class="easyui-calendar"></div>			
			<!--编辑弹窗-->
			<div id="editWin" class="easyui-dialog" title="编辑设立企业信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:550px;height:500px;">
				<form id="editForm" class="formbody">
					<div class="formtitle"><span>编辑设立企业信息</span></div>
						<input name="changeId" type="hidden">
						<input name="projectNum" type="hidden">
						<input name="createOper" type="hidden">
						<input name="cteateTime" type="hidden">			
						<ul class="forminfo">
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input id="dept2" name="zhuBanDept" class="easyui-combotree" data-options="panelHeight:'auto',width:247,height:32" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="no" />否</li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="no" />否</li>
							<li><label>实施主体</label><input id="combo2" name="doBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>					  
						    <li><label>新设企业名称</label><input name="companyName" type="text" class="easyui-validatebox dfinput"   /></li>					    
						    <li><label>注册资本</label><input id="zhuceziben1" name="signCapital" type="text" class="easyui-validatebox dfinput" onkeyup="calculation2()" />万元</li>
						    <li><label>我公司认缴注册资本</label><input id="renjiao1" name="myPlanCapital" type="text" class="easyui-validatebox dfinput" onkeyup="calculation2()"  />万元</li>
						    <li><label>我公司实缴注册资本</label><input name="myReallyCapital" type="text" class="easyui-validatebox dfinput" />万元</li>
						    <li><label>我公司持股比例</label><input id="chigubili1" name="myRatio" type="text" class="easyui-validatebox dfinput" />%</li>
						    <li><label>我公司实际投资额</label><input name="myReallyAmount" type="text" class="easyui-validatebox dfinput" />万元</li>
						    <li><label>发起时间</label><input id="startTT" name="startTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>
						    <li><label>预计结束时间</label><input id="endTT" name="endTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>						   
							<li><button id="btnToUpdate" class="btn ml-100">确认更新</button></li>
						</ul>
				</form>
			</div>
			
			<!--查看弹窗-->
			<div id="watchWin" class="easyui-dialog" title="查看设立企业信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:550px;height:500px;">
				<form id="watchForm" class="formbody">
					<div class="formtitle"><span>查看设立企业信息</span></div>
						<input name="changeId" type="hidden">			
						<ul class="forminfo">
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>主办部门</label><input name="zhuBanDept" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>实施主体</label><input name="doBody" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>						    
						    <li><label>新设企业名称</label><input name="companyName" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>					    
						    <li><label>注册资本</label><input name="signCapital" type="text" class="easyui-validatebox dfinput" readonly="readonly" />万元</li>
						    <li><label>我公司认缴注册资本</label><input name="myPlanCapital" type="text" class="easyui-validatebox dfinput" readonly="readonly" />万元</li>
						    <li><label>我公司实缴注册资本</label><input name="myReallyCapital" type="text" class="easyui-validatebox dfinput" readonly="readonly"/>万元</li>
						    <li><label>我公司持股比例</label><input name="myRatio" type="text" class="easyui-validatebox dfinput"  readonly="readonly" />%</li>
						    <li><label>我公司实际投资额</label><input name="myReallyAmount" type="text" class="easyui-validatebox dfinput" readonly="readonly"/>万元</li>
						    <li><label>发起时间</label><input name="startTime" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>预计结束时间</label><input name="endTime" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>						    
						</ul>
				</form>
			</div>
															
		</div>
	</body>
</html>