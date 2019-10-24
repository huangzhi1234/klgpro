<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>系统管理-菜单管理</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script type="text/javascript" src="${ctx}/mgr/js/ux/sys/resource.js"></script>
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
							模块名称
						</label>
						<select	name="resourcePid" class="select_style">
						</select>
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
				<a href="javascript:void(0);" id="btnTop" class="easyui-linkbutton" data-options="iconCls:'icon-top',plain:true">置顶</a>
				<a href="javascript:void(0);" id="btnUp" class="easyui-linkbutton" data-options="iconCls:'icon-up',plain:true">向上</a>
				<a href="javascript:void(0);" id="btnDown" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true">向下</a>
				<a href="javascript:void(0);" id="btnLow" class="easyui-linkbutton" data-options="iconCls:'icon-low',plain:true">置底</a>
			</div> 
			 
			<!--	数据列表	-->
			<div data-options="region:'center',border:false" title='【搜索结果】' id="toAppGridContainerPanel" style="height: 350px">
				<table id="toGridContainer"></table>
			</div>
	
			
			<!--	新增弹窗	-->
			<div id="addWin" class="easyui-dialog" title="添加资源信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:400px;">
				<form id="addForm" class="formbody">
					<div class="formtitle"><span>资源基本信息</span></div>
						<input name="resourcePid" type="hidden">
						<ul class="forminfo">
							<li><label>父资源名称</label><input name="resourcePname" type="text" class="dfinput" disabled="disabled"></li>
							<li>
								<label>资源类型</label>
								<select id="resourceType_A" name="resourceType" class="dfinput">
								</select>
							</li>
							<li><label>资源标识</label><input name="resourceInd" type="text" class="easyui-validatebox dfinput"/></li>
							<li><label>资源名称</label><input name="resourceName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="资源名称不能为空"/></li>
							<li><label>资源地址</label><input name="resourceUrl" type="text" class="easyui-validatebox dfinput"/></li>
							<li><label>&nbsp;</label><input id="btnToAdd" type="button" class="btn" value="确认保存"/></li>
						</ul>
				</form>
			</div>
			
			<!--	编辑弹窗	-->
			<div id="editWin" class="easyui-dialog" title="编辑资源信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:370px;">
				<form id="editForm" class="formbody">
					<div class="formtitle"><span>资源基本信息</span></div>
						<input name="resourceId" type="hidden">
						<input name="resourcePid" type="hidden">
						<input name="resourceIndex" type="hidden">
						<ul class="forminfo">
							<li>
								<label>资源类型</label>
								<select id="resourceType_E" name="resourceType" class="dfinput">
								</select>
							</li>
							<li><label>资源标识</label><input name="resourceInd" type="text" class="easyui-validatebox dfinput"/></li>
							<li><label>资源名称</label><input name="resourceName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="资源名称不能为空"/></li>
							<li><label>资源地址</label><input name="resourceUrl" type="text" class="easyui-validatebox dfinput"/></li>
							<li><label>&nbsp;</label><input id="btnToUpdate" type="button" class="btn" value="确认更新"/></li>
						</ul>
				</form>
			</div>
		</div>	
	</body>
</html>
