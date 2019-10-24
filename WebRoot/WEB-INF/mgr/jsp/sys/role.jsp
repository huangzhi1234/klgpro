<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>系统管理-角色管理</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script type="text/javascript" src="${ctx}/mgr/js/ux/sys/role.js"></script>
    <style type="text/css">
    .datagrid-header-check input{
		display: none;
	} 
    </style>
  </head>

	<body>
		<!--导航栏&&权限控制-->
		<%@include file="../inc/head.jsp" %>
		
		<!--内容栏-->
		<div class="rightinfo">
			<!-- 查询条件-->
			<form id="schForm">
				<ul class="seachform">
					<li>
						<label>
							角色编码
						</label>
						<input name="roleCode" type="text" class="scinput" />
					</li>
					<li>
						<label>
							角色名称
						</label>
						<input name="roleName" type="text" class="scinput" />
					</li>
					<li>
						<label>
							&nbsp;
						</label>
						<input type="button" class="scbtn" value="查询" />
					</li>
				</ul>
			</form>
	
	
			<!--功能按钮-->
			<div id="tb">  
	    		<a href="javascript:void(0);" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>  
	    		<a href="javascript:void(0);" id="btnEdit" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>  
	    		<a href="javascript:void(0);" id="btnRemove" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>  
				<a href="javascript:void(0);" id="btnResource" class="easyui-linkbutton" data-options="iconCls:'icon-filter',plain:true">资源配置</a> 
				<a href="javascript:void(0);" id="btnGrant" class="easyui-linkbutton" data-options="iconCls:'icon-man',plain:true">授权</a> 
			</div> 
			 
			<!--	数据列表	-->
			<div data-options="region:'center',border:false" title='【搜索结果】' id="toAppGridContainerPanel" style="height: 350px">
				<table id="toGridContainer"></table>
			</div>
	
			
			<!--	新增弹窗	-->
			<div id="addWin" class="easyui-dialog" title="添加角色信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:420px;">
				<form id="addForm" class="formbody">
					<div class="formtitle"><span>角色基本信息</span></div>
						<ul class="forminfo">
							<li><label>角色编码</label><input name="roleCode" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="角色编码不能为空"/></li>
							<li><label>角色名称</label><input name="roleName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="角色名不能为空"/></li>
							<li><label>角色描述</label><textarea name="roleDesc" class="textinput"></textarea></li>
							<li><label>&nbsp;</label><input id="btnToAdd" type="button" class="btn" value="确认保存"/></li>
						</ul>
				</form>
			</div>
			
			<!--	编辑弹窗	-->
			<div id="editWin" class="easyui-dialog" title="编辑角色信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:420px;">
				<form id="editForm" class="formbody">
					<div class="formtitle"><span>角色基本信息</span></div>
						<input name="roleId" type="hidden">
						<ul class="forminfo">
							<li><label>角色编码</label><input name="roleCode" type="text" class="dfinput" readonly="readonly"/></li>
							<li><label>角色名称</label><input name="roleName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="角色名不能为空"/></li>
							<li><label>角色描述</label><textarea name="roleDesc"  class="textinput"></textarea></li>
							<li><label>&nbsp;</label><input id="btnToUpdate" type="button" class="btn" value="确认更新"/></li>
						</ul>
				</form>
			</div>
			
			<!--	权限分配弹窗	-->
			<div id="resourceWin" class="easyui-dialog" title="角色资源信息" data-options="closed:true,iconCls:'icon-filter',modal:true" style="width:600px;height:500px;">
				<form id="resourceForm" class="formbody">
					<div class="formtitle"><span>角色资源信息</span></div>
					<div class="treepanel">
						<ul id="toTreeContainer"></ul>
					</div>
					<br><br>
					<input name="roleId" type="hidden">
					<input id="btnToResource" type="button" class="btn" value="确认保存"/><span class="texttip">*重新登陆后权限生效</span>
				</form>
			</div>
			
			
			<!-- 授权部分 -->
			<div id="grantWin" class="easyui-dialog" title="用户角色授权" data-options="closed:true,iconCls:'icon-man',modal:true" style="width:600px;height:400px;">
				<form id="grantForm" class="formbody">
					<div class="formtitle"><span>用户角色授权</span></div>
						<input name="roleId" type="hidden">
						<table class="formtable">
							<tr>
								<td><input name="grantSearch" type="text" class="dfinput" placeholder="用户名/邮箱/姓名/手机号"/></li></td>
								<td><input id="btnGrantSearch" type="button" class="btn" value="搜索"/></td>
								<td></td>
							</tr>
							<tr>
								<td><label>未选用户</label></td>
								<td></td>
								<td><label>已选用户</label></td>
							</tr>
							
							<tr>
								<td>
									<select id="unselect" name="unselect" style="overflow-y: auto" multiple="multiple" size="8" class="textinput"></select>
								</td>
								<td>
									<input type="button" value="  >>  " class="tbbtn"/>
									<br/><br/><br/><br/>
									<input type="button" value="  <<  " class="tbbtn"/>
								</td>
								<td>
									<select id="select" name="select" multiple="multiple" size="8" class="textinput"></select>
								</td>
							</tr>
							<tr>
								<td colspan="3"><input id="btnToGrant" style="overflow-y: auto" type="button" class="btn" value="确认授权"/></td>
							</tr>
						</table>
				</form>
			</div>
		</div>
	</body>
</html>
