<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>系统管理-数据字典管理</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script type="text/javascript" src="${ctx}/mgr/js/ux/sys/dictDef.js"></script>
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
							字典编码
						</label>
						<input name="dictCode" type="text" class="scinput" />
					</li>
					<li>
						<label>
							字典名称
						</label>
						<input name="dictName" type="text" class="scinput" />
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
	    		<a href="javascript:void(0);" id="btnItem" class="easyui-linkbutton" data-options="iconCls:'icon-detail',plain:true">维护字典项</a>   
			</div> 
			 
			<!--	数据列表	-->
			<div data-options="region:'center',border:false" title='【搜索结果】' id="toAppGridContainerPanel" style="height: 350px">
				<table id="toGridContainer"></table>
			</div>
	
			
			<!--	新增弹窗	-->
			<div id="addWin" class="easyui-dialog" title="添加字典信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:440px;">
				<form id="addForm" class="formbody">
					<div class="formtitle"><span>字典基本信息</span></div>
						<ul class="forminfo">
							<li><label>字典编码</label><input name="dictCode" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="字典编码不能为空"/></li>
							<li><label>字典名称</label><input name="dictName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="字典名称不能为空"/></li>
							<li><label>字典描述</label><textarea name="dictDesc" class="textinput"></textarea></li>
							<li><label>&nbsp;</label><input id="btnToAdd" type="button" class="btn" value="确认保存"/></li>
						</ul>
				</form>
			</div>
			
			<!--	编辑弹窗	-->
			<div id="editWin" class="easyui-dialog" title="编辑字典信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:440px;">
				<form id="editForm" class="formbody">
					<div class="formtitle"><span>字典基本信息</span></div>
						<input name="dictId" type="hidden">
						<ul class="forminfo">
							<li><label>字典编码</label><input name="dictCode" type="text" class="dfinput" readonly="readonly"/></li>
							<li><label>字典名称</label><input name="dictName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="字典名称不能为空"/></li>
							<li><label>字典描述</label><textarea name="dictDesc"  class="textinput"></textarea></li>
							<li><label>&nbsp;</label><input id="btnToUpdate" type="button" class="btn" value="确认更新"/></li>
						</ul>
				</form>
			</div>
		</div>
	</body>
</html>
