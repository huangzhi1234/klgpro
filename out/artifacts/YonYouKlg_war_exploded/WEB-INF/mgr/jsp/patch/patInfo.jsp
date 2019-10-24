<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>


<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>研发成果管理</title>

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
<script type="text/javascript" src="${ctx}/mgr/js/ux/patch/dict.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/mgr/css/patchInfo/patchInfo.css">
<script type="text/javascript" src="${ctx}/mgr/js/ux/patch/patchInfo.js"></script>
<style type="text/css">.red{color:red;font-size: 20px;}.left{float: left !important;}.vali{height:30px !important;border:1px solid #95b8e7 !important;border-radius: 5px 5px 5px 5px !important;padding-left: 8px !important;}.easyui-validatebox{background-color:white;}
	/*取消全选按钮 */
.datagrid-header-check input{
	display: none;
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
			url : path + "/proDicInfo/tree.action",
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
				}else{
					$('#schForm').find('input[name="dicNum"]').val("");
					$('#addForm').find('input[name="dicNum"]').val("");
				}
				$('#schForm').find('input[name="dicNum"]').val(node.id);
				$('#addForm').find('input[name="dicNum"]').val(node.id);
				$('#datagridOfParchInfo').datagrid('load', $("#schForm").formToJson());
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
</style>
</head>

<body style="overflow: auto;">

	<div id="dict_layout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',split:true,title:'成果信息列表(右键操作)'"
			style="width:200px;">
			<div class="easyui-panel" data-options="fit:true,border:false">
				<ul id="dict_tree">
					<div id="dict_menu" class="easyui-menu" style="width:120px;">
						<div id="add_pat" onclick="dict_addNode()" data-options="iconCls:'icon-add'">新增项目节点</div>
						<div id="edit_pat" onclick="dict_updateNode()"
							data-options="iconCls:'icon-edit'">编辑项目节点</div>
						<div id="del_pat" onclick="dict_delNode()" data-options="iconCls:'icon-remove'">删除项目节点</div>
					</div>
				</ul>
			</div>
		</div>
		<!-- region:west -->
		<div data-options="region:'center',title:'成果列表'" style="padding:5px;">
			<div id="class_required"
				style="overflow-y:auto; overflow-x:auto; width:1200px; height:auto;">
				<!-- 查询条件-->
				<form id="schForm">
					<input type="hidden" name="dicNum" />
					<ul class="seachform">
						<li><label>成果名称</label> <input id="patName" name="patName"
							type="text" class="scinput" /></li>
						<li><label>对应NC版本</label><input type="text" id="ncVersion1"
							name="ncVersion" class="easyui-combobox dfinput"
							style="width:200px;"
							data-options="valueField:'id', textField:'text', panelHeight:'auto'" />
						</li>
						<li><label>所属业务模块</label><input type="text" id="busiMod1"
							name="busiMod" class="easyui-combobox dfinput"
							style="width:200px;"
							data-options="valueField:'id', textField:'text', panelHeight:'auto'" /></li>
						<li><label>提供者</label> <input id="patMan" name="patMan"
							type="text" class="scinput" /></li>
						<li><input type="button" class="scbtn" id="scbtn" value="查询" /></li>
					</ul>
				</form>


				<!--功能按钮，项目基本信息-->
				<div id="tb_patchInfo">
					<a href="javascript:void(0);" id="btnAdd_patchInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-add',plain:true">新增</a> <a
						href="javascript:void(0);" id="btnEdit_patchInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-edit',plain:true">编辑</a> <a
						href="javascript:void(0);" id="btnWatch_patchInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-detail',plain:true">查看详细信息</a> <a
						href="javascript:void(0);" id="btnDelete_patchInfo"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">删除</a><a
						href="javascript:void(0);" id="btnApply_patchInfo" class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">批量申请</a>
				</div>


				<!--数据列表-->
				<div data-options="region:'center',border:false,singleSelect:true"
					title='成果基本信息' style="height:auto">
					<table id="datagridOfParchInfo"></table>
				</div>

			</div>

			<!-- 时间戳 -->
			<div id="cc" class="easyui-calendar"></div>
			
			<!--单个申请下载  -->
			<div id="getDown" class="easyui-dialog" title="申请下载" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:420px;">
				<form id="causeForm" class="formbody">
					<div class="formtitle"><span>申请下载</span></div>
						<input type="hidden" name="patIdDown" id="patIdDown" /> 
						<ul class="forminfo">
							<li><label>申请原因或用途：</label>
							<textarea rows="8" cols="38" style="border:1px solid skyblue;padding:8px;" id="cause" class="left easyui-validatebox"
								data-options="required:true,missingMessage:'不能为空'"></textarea>
							</li>
							
							<li><br/><label>&nbsp;</label><input id="sendCause" type="button" class="btn" value="提交申请"/></li>
						</ul>
				</form>
			</div>
			
			<!--批量申请下载  -->
			<div id="getDownMore" class="easyui-dialog" title="进行多个下载申请" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:420px;">
				<form id="causeFormMore" class="formbody">
					<div class="formtitle"><span>多个下载申请</span></div>
					<table id="data_cause" style="width:100%;higth:auto;">
						<!--加载批量拒绝的列表  -->
					</table>	
					<br>
					<input id="sendCauseMore" type="button"  class="btn" value="提交"/>	
				</form>
			</div>
			
			<!--	新增弹窗，成果信息	-->
			<div id="addWin" title="新增成果信息"
				style="float:left;width:100%;higth:auto;display: none;">
				<form id="addForm" name="addForm" class="formbody"
					enctype="multipart/form-data"
					action="${ctx}/mgr/patchInfo/add.json?patch=patchInfo"
					method="post" style="float:left;width:100%;higth:auto">
					<input type="hidden" name="dicNum" id="dicNum" />
					<div class="form_div">
						<div class="formtitle" style="width: 50%;">
							<span>新增成果信息</span>
						</div>
						<div class="btn_div">
							<ul>
								<!-- id="btnToAdd" -->
								<li><button type="submit" class="btn ml-180" onclick="return addcheck()">确认保存</button>
									<a href="javascript:void(0);" class="btn ml-180"
									onclick="return_main()">返回</a></li>
							</ul>
						</div>
						<ul
							style="border-top:1px;border-color:#006C9C;border-style:solid;">
							<li><label>成果名称：</label><input name="patName" type="text"
								class="easyui-validatebox vali left" data-options="required:true,missingMessage:'成果名称不能为空'" />
								<span class="red left">*</span>
							</li>
							<li><label>提供者：</label><input type="text" 
								name="patMan" class="easyui-validatebox vali left" data-options="required:true,missingMessage:'提供者名称不能为空'"
								/><span class="red left">*</span></li>

							<li><label>接收时间：</label><input name="receiveTime"
								class="easyui-datebox dfinput"
								data-options="editable:false,sharedCalendar:'#cc',height:32" /></li>
							<li><label>联系人邮箱：</label><input type="text" id="email"
								name="email" class="easyui-validatebox dfinput" data-options="validType:'email'"/></li>

							<li><label>联系人电话：</label><input type="text" name="phone"
								class="easyui-validatebox dfinput" data-options="validType:'mobile'"/></li>
							<li><label>联系人QQ：</label> <input name="qq" type="text"
								class="easyui-validatebox dfinput" /></li>

							<li><label>所属项目名称：</label><input name="proName" type="text"
								class="easyui-validatebox dfinput" /></li>
							<li><label>测试情况：</label><input id="testStatus"
								name="testStatus" class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '通过'},{label: '0',value: '不通过'}]" /></li>

							<li><label>测试人：</label><input name="testMan" type="text"
								class="easyui-validatebox dfinput" /></li>
							<li><label>对应NC版本：</label>
								<input type="text" id="ncVersion2"
								name="ncVersion" class="easyui-combobox vali left" 
								data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto',required:true,missingMessage:'对应的NC版本不能为空'" />
								<span class="red" style="float:right;margin-right: 100px;">*</span>
							</li>

							<li><label>所属业务模块：</label><input type="text" id="busiMod2"
								name="busiMod" class="easyui-combobox dfinput"
								data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
							<li><label>是否有SQL脚本：</label><input id="isSql" name="isSql"
								class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '是'},{label: '0',value: '否'}]" /></li>

							<li><label>SQL脚本名称：</label><input name="sqlName" type="text"
								class="easyui-validatebox dfinput" /></li>
							<li><label>是否需元数据升级：</label><input id="metadataUp"
								name="metadataUp" class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '是'},{label: '0',value: '否'}]" /></li>

							<li><label>元数据名称：</label><input name="metadataName"
								type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>是否需要部署：</label><input name="isDep"
								class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '是'},{label: '0',value: '否'}]" /></li>

							<li><label>附件：</label><input class="easyui-filebox"
								name="fileOne" data-options="prompt:'请选择您要上传的文件',required:true,missingMessage:'必须添加上传文件'"
								style="width:320px;">
								<span class="red" style="float:right">*</span>
							</li>
							<li><label>客户名称：</label><input name="customer"
								type="text" class="easyui-validatebox dfinput" /></li>

							<li style="height:65px;"><label>成果说明：</label> <textarea data-options="required:true,missingMessage:'成果说明不能为空'" style="padding: 8px;"
									rows="4" cols="40" name="patDis" class="left easyui-validatebox"></textarea>
								<span class="red left">*</span>
							</li>
							<li style="height:65px;"><label>备注：</label> <textarea style="padding: 8px;"
									rows="4" cols="40" name="comment"></textarea></li>
						</ul>

					</div>
				</form>
			</div>

			<!--	编辑弹窗，成果信息	-->
			<div id="editWin" title="编辑成果信息" 
				style="float:left;width:100%;higth:auto;display: none;">
				<div class="form_div">
					<form id="editForm" class="formbody" enctype="multipart/form-data"
						action="${ctx}/mgr/patchInfo/update.json?patch=patchInfo"
						method="post" style="float:left;width:100%;higth:auto">
						<input type="hidden" name="dicNum" /> <input name="patId"
							type="hidden"> <input name="upTime" type="hidden">
						<input name="upMan" type="hidden">
						<div class="formtitle" style="width: 50%;">
							<span>编辑成果信息</span>
						</div>
						<div class="btn_div">
							<ul>
								<li><button type="submit" class="btn ml-180" onclick="return updatecheck()">确认更新</button>
									<a href="javascript:void(0);" class="btn ml-180"
									onclick="return_main()">返回</a></li>
							</ul>
						</div>
						<ul
							style="border-top:1px;border-color:#006C9C;border-style:solid;">
							<li><label>成果名称：</label><input name="patName" type="text" data-options="required:true,missingMessage:'成果名称不能为空'"
								class="easyui-validatebox vali left" /><span class="red left">*</span></li>
							<li><label>提供者：</label><input type="text" id="patMan2"
								name="patMan" class="easyui-validatebox vali left"
								data-options="required:true,missingMessage:'提供者名称不能为空'" /><span class="red left">*</span></li>

							<li><label>接收时间：</label><input name="receiveTime"
								id="receiveTime2" class="easyui-datebox dfinput"
								data-options="sharedCalendar:'#cc',height:32" /></li>
							<li><label>联系人邮箱：</label><input type="text" id="email"
								name="email" class="easyui-validatebox dfinput" data-options="validType:'email'"/></li>

							<li><label>联系人电话：</label><input type="text" name="phone"
								class="easyui-validatebox dfinput" data-options="validType:'mobile'" /></li>
							<li><label>联系人QQ：</label> <input name="qq" id="qq2"
								type="text" class="easyui-validatebox dfinput" /></li>

							<li><label>所属项目名称：</label><input name="proName"
								id="proName2" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>测试情况：</label><input id="testStatus2"
								name="testStatus" class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '通过'},{label: '0',value: '不通过'}]" /></li>

							<li><label>测试人：</label><input name="testMan" type="text"
								class="easyui-validatebox dfinput" /></li>
							<li><label>对应NC版本：</label><input type="text" id="ncVersion3"
								name="ncVersion" class="easyui-combobox dfinput" 
								data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto',required:true,missingMessage:'对应NC版本不能为空'" />
								<span class="red" style="float:right;margin-right: 100px;">*</span>
							</li>

							<li><label>所属业务模块：</label><input type="text" id="busiMod3"
								name="busiMod" class="easyui-combobox dfinput"
								data-options="editable:false,valueField:'id', textField:'text', panelHeight:'auto'" /></li>
							<li><label>是否有SQL脚本：</label><input id="isSql2" name="isSql"
								class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '是'},{label: '0',value: '否'}]" /></li>

							<li><label>SQL脚本名称：</label><input name="sqlName" type="text"
								class="easyui-validatebox dfinput" /></li>
							<li><label>是否需元数据升级：</label><input id="metadataUp2"
								name="metadataUp" class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '是'},{label: '0',value: '否'}]" /></li>

							<li><label>元数据名称：</label><input name="metadataName"
								type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>是否需要部署：</label><input name="isDep" id="isDep2"
								class="easyui-combobox"
								data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '是'},{label: '0',value: '否'}]" /></li>
							<li><label>客户名称：</label><input name="customer"
								type="text" class="easyui-validatebox dfinput" /></li>
							<li style="width:100%; height:40px;"><label>附件：</label><label
								id="fileAddress_edit" style="text-align:left;width:auto;"></label>
								<!-- <a id="fileDel" class="atn">删除</a> -->
								<input class="easyui-filebox"
								name="fileTwo" data-options="prompt:'请选择您要上传的文件'" id="filecheck"
								style="width:320px;">
								<span class="red" style="float:right;margin-right:280px;">*</span>
							</li>

							<li style="height:65px;"><label>成果说明：</label> <textarea class="left easyui-validatebox" data-options="required:true,missingMessage:'成果说明不能为空'"
									rows="4" cols="40" name="patDis" style="padding:8px;"></textarea>
								<span class="red left">*</span>
							</li>
							<li style="height:65px;"><label>备注：</label> <textarea
									rows="4" cols="40" name="comment"></textarea></li>
						</ul>

					</form>
				</div>
			</div>

			<!--	查看弹窗，成果信息	-->
			<div id="watchWin" title="查看成果信息"
				style="float:left;width:1200px;height:1350px;overflow-x:auto;overflow-y:auto;display: none;">
				<div id="form_divWin" class="form_div" style="height:1350px;">
					<div class="formtitle">
						<span>查看成果信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">返回</a></li>
						</ul>
					</div>

					<div id="left-top">
						<ul>
							<li style="width:350px;height:100px;"><img id="tubiao"
								style="margin-top:10px;" /></li>
							<li id="file_watch"
								style="width:350px;height:30px;text-align:center;font-size:18px;"></li>
							<li id="f1" style="width:350px;height:30px;"><a id="fileDown"
								class="atn">下载</a>
							</li>
						</ul>
					</div>
					<div id="right">
						<ul>
							<li style="text-align:center;width:150px;">成 果 名 称：</li>
							<li id="patName_watch" style="text-align:left;width:300px;"></li>
							<li style="text-align:center;width:150px;">提 供 者：</li>
							<li id="patMan_watch" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:150px;">接 收 时 间：</li>
							<li id="receiveTime_watch" style="text-align:left;width:300px;"></li>
							<li style="text-align:center;width:150px;">提供者 邮箱：</li>
							<li id="email_watch" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:150px;">提供者 电话：</li>
							<li id="phone_watch" style="text-align:left;width:300px;"></li>
							<li style="text-align:center;width:150px;">提供者 QQ：</li>
							<li id="qq_watch" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:150px;">对应NC版本：</li>
							<li id="ncVersion_watch" style="text-align:left;width:300px;"></li>
							<li style="text-align:center;width:150px;">所属业务模块：</li>
							<li id="busiMod_watch" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:150px;">所属项目名称：</li>
							<li id="proName_watch" style="text-align:left;width:300px;"></li>
							<li style="text-align:center;width:150px;">客户名称：</li>
							<li id="customer" style="text-align:left;width:300px;"></li>
						</ul>
					</div>

					<div class="formtitle" style="height:50px;">
						<span style="font-size:20px;">成果说明</span>
					</div>
					<div class="btn_div" style="height:50px;"></div>
					<div id="left-bottom">
						<ul>
							<li><label style="width:10%;"></label> <label
								id="patDis_watch"
								style="width:80%;text-align:left;font-size:18px;"></label></li>
						</ul>
					</div>

					<div class="formtitle" style="height:50px;">
						<span style="font-size:20px;">备注</span>
					</div>
					<div class="btn_div" style="height:50px;"></div>
					<div id="left-bottom">
						<ul>
							<li><label style="width:10%;"></label> <label
								id="comment_watch"
								style="width:80%;text-align:left;font-size:18px;"></label></li>
						</ul>
					</div>

					<div class="formtitle" style="height:50px;">
						<span style="font-size:20px;">成果详细信息</span>
					</div>
					<div class="btn_div" style="height:50px;"></div>
					<div id="bottom">
						<ul>
							<li style="height:180px;"><label
								style="width:200px;text-align:center;"></label> <label
								style="width:200px;text-align:center;">测 试 人：</label> <label
								style="width:200px;text-align:left;" id="testMan_watch"></label>
								<label style="width:200px;text-align:center;">测 试 情 况：</label> <label
								style="width:200px;text-align:left;" id="testStatus_watch"></label>
								<label style="width:200px;text-align:center;font-size:18px;"></label>
								<label style="width:200px;text-align:center;">是 否 有 SQL
									脚 本：</label> <label style="width:200px;text-align:left;"
								id="isSql_watch"></label> <label
								style="width:200px;text-align:center;">SQL 脚 本 名 称：</label> <label
								style="width:200px;text-align:left;" id="sqlName_watch"></label>

								<label style="width:200px;text-align:center;font-size:18px;"></label>
								<label style="width:200px;text-align:center;">是否需要元数据升级：</label>
								<label style="width:200px;text-align:left;"
								id="metadataUp_watch"></label> <label
								style="width:200px;text-align:center;">元 数 据 名 称：</label> <label
								style="width:200px;text-align:left;" id="metadataName_watch"></label>

								<label style="width:200px;text-align:center;font-size:18px;"></label>
								<label style="width:200px;text-align:center;">上 传 人：</label> <label
								style="width:200px;text-align:left;" id="upMan_watch"></label> <label
								style="width:200px;text-align:center;">上 传 时 间：</label> <label
								style="width:200px;text-align:left;" id="upTime_watch"></label>
							</li>
						</ul>
						<div class="formtitle" style="height:50px;margin-top:100px;">
							<span style="font-size:20px;">下载人列表</span>
							<button id="downManBt">显示</button>
						</div>
						<div class="btn_div" style="height:50px;margin-top:100px;"></div>
						<div id="downManList" closed='true' class="easyui-panel"
							style="height:325px;width:1160px;">
							<table id="datagridOfParchDownlaod" style="height:320px;width:1155px;"></table>
						</div>
					</div>
				</div>
			</div>



			<!-- 点击显示的界面 -->
			<div id="watchWin2" title="查看成果信息"
				style="float:left;width:1200px;height:650px;display: none;">
				<div class="form_div" style="height:650px;">
					<div class="formtitle">
						<span>成果信息</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">关闭</a></li>
						</ul>
					</div>

					<div id="left-top2">
						<ul>
							<li style="width:350px;height:100px;"><img id="tubiao2"
								style="margin-top:10px;" class="tupiao2" /></li>
							<li id="file_watch2"
								style="width:350px;height:58px;text-align:center;font-size:18px;"></li>
							<li id="f2" style="width:350px;height:30px;"><a id="fileDown2"
								class="atn">下载</a></li>
						</ul>
					</div>
					<div id="right2">
						<ul>
							<li style="text-align:center;width:120px;">成 果 名 称：</li>
							<li id="patName_watch2"  style="display:inline;width:360px;line-height: 20px;"></li>
							<li style="text-align:center;width:120px;">提 供 者：</li>
							<li id="patMan_watch2" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:120px;">接 收 时 间：</li>
							<li id="receiveTime_watch2" style="text-align:left;width:360px;"></li>
							<li style="text-align:center;width:120px;">提供者 邮箱：</li>
							<li id="email_watch2" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:120px;">提供者 电话：</li>
							<li id="phone_watch2" style="text-align:left;width:360px;"></li>
							<li style="text-align:center;width:120px;">提供者 QQ：</li>
							<li id="qq_watch2" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:120px;">对应NC版本：</li>
							<li id="ncVersion_watch2" style="text-align:left;width:360px;"></li>
							<li style="text-align:center;width:120px;">所属业务模块：</li>
							<li id="busiMod_watch2" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:120px;">所属项目名称：</li>
							<li id="proName_watch2" style="text-align:left;width:360px;"></li>
							
							<li style="text-align:center;width:120px;">客户名称：</li>
							<li id="customer2" style="text-align:left;width:100px;"></li>
						</ul>
					</div>
					<div class="formtitle" style="height:50px;">
						<span style="font-size:20px;">成果说明</span>
					</div>
					<div class="btn_div" style="height:50px;"></div>
					<div id="left-bottom2">
						<ul>
							<li><label style="width:10%;"></label><label
								id="patDis_watch2"
								style="width:80%;text-align:left;font-size:18px;"></label></li>
						</ul>
					</div>
					<div class="formtitle" style="height:50px;">
						<span style="font-size:20px;">备注</span>
					</div>
					<div class="btn_div" style="height:50px;"></div>
					<div id="left-bottom2">
						<ul>
							<li><label style="width:10%;"></label> <label
								id="comment_watch2"
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
						<tr>
						<tr name="tr">
							<td class="title">属性值：</td>
							<td><input type="text" class="dict_input" value=""
								name="propertiyValue" /></td>
						</tr>
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
