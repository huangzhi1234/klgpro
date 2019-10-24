<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>


<head>

<title>乐学空间</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
<%
	request.setAttribute("import", "easyUI,namespace,jsUtil");
%>
<%@include file="../inc/import.jsp"%>
<script type="text/javascript" src="${ctx}/mgr/js/ux/study/studydic.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/mgr/css/patchInfo/patchInfo.css">
<script type="text/javascript" src="${ctx}/mgr/js/ux/study/studyspace.js"></script>
<style type="text/css">
/*取消全选按钮 */
.datagrid-header-check input{
	display: none;
} 
.border1{
	border:1px solid skyblue;
}
</style>
<script>
	$(function() {
	/*用来提醒上传是否成功  */
		var a=<%=request.getParameter("a")%>;
		if(a=="1"){
			$.messager.alert("提醒","操作成功，请耐心等候结果");
		}
		if(a=="2"){
			$.messager.alert("提醒","操作失败");
		}
		/**
		 * 初始化字典tree 
		 */
		$('#dict_tree').tree({
			url : path + "/docDicInfo/tree3.action",
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
				if (node.id != "2") { //不是顶级
					/* dict_clear_form();
					dict_loadForm(node); 
					var pNode = $("#dict_tree").tree("getParent", node.target);
					parentNodeInfo(pNode);*/
					$('#addWin').hide();
					$('#editWin').hide();
					$('#watchWin').hide();
					$('#watchWin2').hide();
					$('#tree').hide();
					$('#class_required').show();
					$('#schForm').find('input[name="dicNum"]').val(node.id);
					$('#addForm').find('input[name="dicNum"]').val(node.id);
					$.ajax({
						type : "POST",
						url : path + "/docDicInfo/load.action",
						dataType : 'json',
						data : {
							id : node.id
						},
						success : function(data) {
							if(data.dicType == '0'){
								$('#datagridOfstDocInfo').datagrid('load', $("#schForm").formToJson());
							}else{
								window.open(data.propertiyValue);
							}
						},
						error : function() {
							$.messager.alert('操作提示', "<font color='red'>抱歉！操作失败！</font>", "error");
						}
					});
					
				}else{
					$('#schForm').find('input[name="dicNum"]').val("");
					$('#addForm').find('input[name="dicNum"]').val("");
				}
				$('#schForm').find('input[name="dicNum"]').val(node.id);
				$('#addForm').find('input[name="dicNum"]').val(node.id);
				$('#datagridOfstDocInfo').datagrid('load', $("#schForm").formToJson());
			},
			onDblClick : function(node) {
				if (node.id == "0") {
					$.messager.alert('操作提示', "顶级节点不可修改！", "warning");
				}
			}
		});
	//app_type_menucombo();
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
	border-top: 1px solid #BFD2E1;
	border-right: 1px solid #BFD2E1;
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
</style>
</head>

<body style="overflow: auto;">
<!--导航栏&&权限控制-->
	<%@include file="../inc/head.jsp"%>
	<div id="dict_layout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',split:true,title:'标准文档列表(右键操作)'"
			style="width:200px;">
			<div class="easyui-panel" data-options="fit:true,border:false">
				<ul id="dict_tree">
					<div id="dict_menu" class="easyui-menu" style="width:120px;">
						<div id="add_menu" onclick="dict_addNode()" data-options="iconCls:'icon-add'">新增项目节点</div>
						<div id="update_menu" onclick="dict_updateNode()"
							data-options="iconCls:'icon-edit'">编辑项目节点</div>
						<div id="delete_menu" onclick="dict_delNode()" data-options="iconCls:'icon-remove'">删除项目节点</div>
					</div>
				</ul>
			</div>
		</div>
		<!-- region:west -->
		<div data-options="region:'center',title:'文档列表'" style="padding:5px;">
			<div id="class_required"
				style="overflow-y:auto; overflow-x:auto; width:1200px; height:auto;">
				<!-- 查询条件-->
				<form id="schForm">
					<input type="hidden" name="dicNum" />
					<ul class="seachform">
						<li><label>文档名称</label> <input id="fileName" name="fileName"
							type="text" class="scinput" /></li>
						<li><label>文档作者</label> <input id="author" name="author"
							type="text" class="scinput" /></li>
						<li><label>上传人</label> <input id="createOper" name="createOper"
							type="text" class="scinput" /></li>
						<li><input type="button" class="scbtn" id="scbtn" value="查询" /></li>
					</ul>
				</form>


				<!--功能按钮，项目基本信息-->
				<div id="tb_stDocInfo">
					<a href="javascript:void(0);" id="btnAdd_stDocInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-add',plain:true">上传</a> 
					<a href="javascript:void(0);" id="btnDelete_stDocInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">删除</a>
					<a href="javascript:void(0);" id="btnWatch_stDocInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-detail',plain:true">在线浏览</a>
					<!-- <a href="javascript:void(0);" id="btnApply_stdocInfo" class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">批量申请</a> -->
				</div>


				<!--数据列表-->
				<div data-options="region:'center',border:false,singleSelect:true"
					title='文档基本信息' style="height:auto">
					<table id="datagridOfstDocInfo"></table>
				</div>

			</div>

			
			<!--	新增弹窗，文档信息	-->
			<div id="addWin" title="上传文档信息文件" 
				style="float:left;width:95%;higth:auto;display: none;">
				<form id="addForm" name="addForm" class="formbody"
					enctype="multipart/form-data"
					action="${ctx}/mgr/studyspace/add.json?stDoc=studyspace"
					method="post" style="float:left;width:95%;higth:auto">
					<input type="hidden" name="dicNum" id="dicNum" />
					<div class="form_div">
						<div class="formtitle" style="width:70%">
							<span>上传文档信息文件</span>
						</div>
						<div class="btn_div">
							<ul>
								<!-- id="btnToAdd" -->
								<li><button type="submit" class="btn ml-180" onclick="return upload_check()">确认保存</button>
									<a href="javascript:void(0);" class="btn ml-180"
									onclick="return_main()">返回</a></li>
							</ul>
						</div>
						<ul style="border-top:1px;border-color:#006C9C;border-style:solid;">
							<li>
								<label>上传文档：</label>
								<input class="easyui-filebox"
								name="fileOne" data-options="prompt:'请选择您要上传的文件',required:true,missingMessage:'必须有上传文件才能进行添加'""
								style="width:320px;">
							</li>
							
							<li>
								<label>文档作者：</label>
								<input name="author" type="text" style="height: 24px" class="easyui-validatebox" data-options="required:true,missingMessage:'文档作者不能为空'">
							</li>
							
							
							<li style="width: 100%;height: auto;">
								<label>文档说明：</label>
								<textarea rows="4" cols="76" style="padding: 8px;" name="comment"></textarea>
							</li>
						</ul>

					</div>
				</form>
			</div>

			
			

			<!-- 点击显示的界面 -->
			<div id="watchWin2" title="查看文档信息" 
				style="float:left;width:1200px;height:450px;;overflow-x:auto;overflow-y:auto;display: none;">
				<div class="form_div" style="height:500px;">
					<div class="formtitle">
						<span>文档信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">关闭</a>
							</li>
						</ul>
					</div>
					<div id="left-top2">
						<ul>
							<li style="width:350px;height:100px;"><img id="tubiao2" style="margin-top:10px;" /></li>
							<li id="file_watch2" style="width:350px;height:58px;text-align:center;font-size:18px;"></li>
							<li id="fileDownli" style="width:350px;height:30px;"><a id="fileDown2" class="atn">下载</a></li>
						</ul>
					</div>
					<div id="right2">
						<ul>
							<li style="text-align:center;width:150px;">作者：</li>
							<li id="author2" style="text-align:left;width:200px;"></li>
							
							<li style="text-align:center;width:150px;">上传人：</li>
							<li id="createOper_watch2" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:150px;">上传 时 间：</li>
							<li id="createTime_watch2" style="text-align:left;width:300px;"></li>
							
							<!-- <li style="font-weight: bold;font-size: 18px;">文档说明：</li>
							<li id="comment" style="text-align:left;width:500px;margin-left: 42px;"></li> -->
						</ul>
					</div>
					<div class="formtitle" style="height:50px;">
						<span style="font-size:20px;">文档说明</span>
					</div>
					<div id="left-bottom2">
						<ul>
							<li><label style="width:10%;"></label><label
								id="comment"
								style="width:80%;text-align:left;font-size:18px;"></label></li>
						</ul>
					</div>
					
				</div>
			</div>

			<!-- 数节点维护 -->
			<div id="tree" title="项目节点" 
				style="float:left;width:100%;higth:auto;display: none;">
				<form id="dict_form" method="post"
					style='margin:0;padding:0;height: 100%;vertical-align: middle;'>

					<input type="hidden" name="level" value="">
					<%-- 添加时：为父节点所在层次 ；修改时：为本节点 --%>
					<table class="dict_editForm"
						style="width: 70%;height: 90%;margin-left: 70px;margin-bottom: 10px;margin-top: 0px;"
						cellpadding=0 cellspacing=0>
						<colgroup width="20%"></colgroup>
						<colgroup width="70%"></colgroup>

						<tr name="tr" style="height: 80px;">
							<td colspan="4" class="dic_mainTitle" style="text-align: center;">数据字典维护</td>
						</tr>
						<tr name="tr">

							<td class="title">父节点ID：</td>
							<td><input type="text" class="dict_input" value=""
								style="border:0px;" name="operId" readonly="readonly" /></td>
						</tr>

						<tr name="tr">
							<td class="title">父节点名称：</td>
							<td><input type="text" class="dict_input" value=""
								style="border:0px;" name="upSystemName" readonly="readonly" /></td>
						</tr>
						<tr name="tr">
							<td class="title">字典项ID：</td>
							<td><input type="text" class="dict_input" value=""
								style="border:0px;" name="dicNum" readonly="readonly" /></td>
						</tr>


						<tr name="tr">
							<td class="title"><span style="color:#F00">*</span>中文名称：</td>
							<td><input type="text" maxlength="40" value=""
								name="dicName" class="dict_input" /></td>
						</tr>
						<tr name="tr">
							<td class="title">类型：</td>
							<td>
							<input name="dicType" class="easyui-combobox" id="dicType"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '0',value: '本系统'},{label: '1',value: '乐学网'}]" />
							
							</td>
						</tr>
						<tr name="tr">
							<td class="title">链接：</td>
							<td><input type="text" class="dict_input" value=""
								name="propertiyValue" /></td>
						</tr>
						<tr name="tr">
							<td colspan="4" style="text-align:center;"><a
								href="javascript:;" class="easyui-linkbutton"
								data-options="iconCls:'icon-disk'" onclick="saveMenu()">保&nbsp;存</a>
								<a href="javascript:;" class="easyui-linkbutton"
								data-options="iconCls:'icon-cross'" onclick="resetMenu()">清&nbsp;空</a>
								<a href="javascript:;" class="easyui-linkbutton"
								data-options="iconCls:'icon-cross'" onclick="back()">返&nbsp;回</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<!-- dict_layout -->
	<!-- 创建遮罩层：作用于上传过程中，用户无法操作 -->
	<div id="zhezhao" style="background-color:#FFFAFA;position:fixed;left:0;top:0;width:100%;opacity: 0.6;height:100%;z-index:99999;display:none" >
	</div>

</body>
</html>
