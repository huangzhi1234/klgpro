<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>系统管理-用户管理</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script type="text/javascript" src="${ctx}/mgr/js/ux/sys/user.js"></script>
    <script type="text/javascript" src="${ctx}/mgr/js/ux/sys/depart.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/patchInfo/patchInfo.css">
	 
    <script>
	$(function() {
		/**
		 * 初始化字典tree 
		 */
		$('#dict_tree').tree({
			url : path + "/depart/tree.action",
			lines : true,
			/*	onLoadSuccess: function(node, data){
					//重载时也会触发本事件
				},*/
			onContextMenu : function(e, node) {
				e.preventDefault();
				$('#dict_tree').tree('select', node.target);
				$('#dict_menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onClick : function(node) {
				if (node.id != "0") { //不是顶级
					$('#tree').hide();
					$('#toGridContainer').datagrid('load', 
						{	dicNum:node.id	}
					 ); 
					$('#class_required').show();
				    $('#seachform').find('input[name="dicNum"]').val(node.id);
					$('#addWin').find('input[name="dicNum"]').val(node.id); 
					$('#addWin').find('input[name="deptNum"]').val(node.id);
					$('#editWin').find('input[name="deptNum"]').val(node.id);
					
				}else{
					$('#seachform').find('input[name="dicNum"]').val("");
					$('#addWin').find('input[name="dicNum"]').val(""); 
					$('#editWin').find('input[name="deptNum"]').val("");
				}
			},
			onDblClick : function(node) {
				if (node.id == "0") {
					$.messager.alert('操作提示', "顶级节点不可修改！", "warning");
				}
			}
		});
		
	
	});
</script>
    <style type="text/css">
/******数据字典 ******/
.dict_editForm td {
	text-align: left;
}

.dic_mainTitle {
	height: 24px;
	font-size: 16px;
	font-weight: bold;
	text-align: center;
	border: 1px solid #99BBE8;
	color: #15428B;
	overflow: hidden;
	padding-top: 5px;
	width: 95%;
}

.dict_editForm {
	font-size: 12px;
	border-right: 1px solid #BFD2E1;
	border-bottom: 1px solid #BFD2E1;
}

.dict_editForm tr[name='tr']>td, .dict_editForm tr>th {
	border-left: 1px solid #BFD2E1;
	border-top: 1px solid #BFD2E1
}

.dict_editForm .title {
	text-align: right;
}

.dict_button {
	margin: 5px;
	padding: 1px 6px;
}

.dict_input {
	width: 80%;
	height: 35px;
	padding: 1px 0px;
	line-height: 40px;
	border-top: solid 1px #a7b5bc;
	border-left: solid 1px #a7b5bc;
	border-right: solid 1px #ced9df;
	border-bottom: solid 1px #ced9df;
	background: url(../images/inputbg.gif) repeat-x;
	text-indent: 10px;
}
.datagrid-header-check input{
	display: none;
} 
</style>
    
    
  </head>

	<body style="overflow: auto;">
		<!--导航栏&&权限控制-->
		<%@include file="../inc/head.jsp" %>
		<div id="dict_layout" class="easyui-layout" data-options="fit:true">
<!--左边：部门列表  -->
		<div data-options="region:'west',split:true,title:'部门列表'"
			style="width:200px;">
			<div class="easyui-panel" data-options="fit:true,border:false">
				<ul id="dict_tree">
					
				</ul>
			</div>
		</div>
<!--右边：成员列表  -->
		<div data-options="region:'center',title:'部门成员列表'" style="padding:5px;">
			<div id="class_required" style="overflow-y:auto; overflow-x:auto; width:960px; height:auto;">
			<div class="rightinfo">
			<!-- 查询条件-->
			<form id="schForm">
				<ul class="seachform">
					<li>
						<label>用户帐号</label>
						<input name="userAct" type="text" class="scinput" />
						<input type="hidden" name="dicNum" />
					</li>
					<li>
						<label>用户姓名</label>
						<input name="userName" type="text" class="scinput" />
					</li>
					<li>
						<label>失效标识</label>
						<select class="select_style" name="actFlag">
							<option value="">全部</option>
							<option value="1">有效</option>
							<option value="0">无效</option>
						</select>
					</li>
					<li>
						<label>&nbsp;</label>
						<input type="button" class="scbtn" value="查询" />
					</li>
				</ul>
			</form>
			<!--功能按钮-->
			<div id="tb">  
	    		<a href="javascript:void(0);" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>  
	    		<a href="javascript:void(0);" id="btnEdit" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>  
	    		<a href="javascript:void(0);" id="btnRemove" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	    		<a href="javascript:void(0);" id="btnGrant" class="easyui-linkbutton" data-options="iconCls:'icon-man',plain:true">授权</a>   
			</div> 
			<!--	数据列表	-->
			<div data-options="region:'center',border:false" title='【搜索结果】' id="toAppGridContainerPanel" style="height: 350px">
				<table id="toGridContainer"></table>
			</div>
		</div>
			</div>
		</div>
	</div>
			<!--	新增弹窗	-->
			<div id="addWin" class="easyui-dialog" title="添加用户信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:440px;">
				<form id="addForm" class="formbody">
					<div class="formtitle"><span>用户基本信息</span></div>
						<ul class="forminfo">
							<li><label>帐号</label><input type="hidden" name="dicNum" id="dicNum" />
							<input name="userAct" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="帐号不能为空"/></li> 
							<li><label>密码</label><input name="userPwd" type="password" class="easyui-validatebox dfinput" required="true" invalidMessage="密码不能为空"/></li>
							<!-- <li><label>蓝凌帐号</label><input name="lanlingUserNum" type="text" class="easyui-validatebox dfinput"/></li> -->
							<li><label>用户姓名</label><input name="userName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="用户名不能为空"/></li>
							<li><label>手机号码</label><input name="userPhone" type="text" class="easyui-validatebox dfinput" validType="mobile"/>
								<input name="deptNum" type="hidden" />
							</li>
							<li><label>岗位</label><input id="position1" name="position" type="text" class="easyui-combobox dfinput"
								data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'"/>
							<li><label>邮箱</label><input name="userMail" type="text" class="easyui-validatebox dfinput" validType="email"/></li>
							<li><label>有效期</label><input name="actTime" type="text" class="easyui-datebox dfinput" style="height:32px" data-options="validType:['date','gtdate']"/></li>
							<!-- <li><label>所属公司</label><input id="combo1" name="companyNum" class="easyui-combobox dfinput" data-options="valueField:'id', textField:'text',panelHeight:'auto',height:27" required="true" /></li> -->
							<li><label>&nbsp;</label><input id="btnToAdd" type="button" class="btn" value="确认保存"/></li>
						</ul>
				</form>
			</div>
			
			<!-- 授权部分 -->
			<div id="grantWin" class="easyui-dialog" title="用户角色授权" data-options="closed:true,iconCls:'icon-man',modal:true" style="width:600px;height:400px;">
				<form id="grantForm" class="formbody">
					<div class="formtitle"><span>用户角色授权</span></div>
						<input name="userId" type="hidden">
						<table class="formtable">
							<tr>
								<td><label>未选角色</label></td>
								<td></td>
								<td><label>已选角色</label></td>
							</tr>
							<tr>
								<td>
									<select id="unselect" style="overflow-y: auto" name="unselect" multiple="multiple" size="8" class="textinput"></select>
								</td>
								<td>
									<input type="button" value="  >>  " class="tbbtn"/>
									<br/><br/><br/><br/>
									<input type="button" value="  <<  " class="tbbtn"/>
								</td>
								<td>
									<select id="select" style="overflow-y: auto"  name="select" multiple="multiple" size="8" class="textinput"></select>
								</td>
							</tr>
							<tr>
								<td colspan="3"><input id="btnToGrant" type="button" class="btn" value="确认授权"/></td>
							</tr>
						</table>
				</form>
			</div>
			
			<!--	编辑弹窗	-->
			<div id="editWin" class="easyui-dialog" title="编辑用户信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:500px;height:440px;">
				<form id="editForm" class="formbody">
					<div class="formtitle"><span>用户基本信息</span></div>
						<input name="userId" type="hidden">
						<input type="hidden" name="departId" id="dicNum2" />
						<ul class="forminfo">
							<li><label>帐号</label><input id="act" name="userAct" type="text" readonly="readonly" class="dfinput"/></li>
							<li><label>密码</label><input id="pass" name="userPwd" type="password"  class="dfinput"/></li>
							<li><label>用户姓名</label><input name="userName" type="text" class="easyui-validatebox dfinput" required="true" invalidMessage="用户名不能为空"/></li>
							<li><label>手机号码</label><input name="userPhone" type="text" class="easyui-validatebox dfinput" validType="mobile"/></li>
							<li><label>岗位</label><input id="position2" name="position" type="text" class="easyui-combobox dfinput" data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'"/>
							<li><label>邮箱</label><input name="userMail" type="text" class="easyui-validatebox dfinput" validType="email"/></li>
							<li><label>有效期</label><input id="actTime" name="actTime" type="text" class="easyui-datebox dfinput" style="height:32px" data-options="validType:['date','gtdate']"/></li>
							<li><label>&nbsp;</label><input id="btnToUpdate" type="button" class="btn" value="确认更新"/></li>
						</ul>
				</form>
			</div>
	</body>
</html>
