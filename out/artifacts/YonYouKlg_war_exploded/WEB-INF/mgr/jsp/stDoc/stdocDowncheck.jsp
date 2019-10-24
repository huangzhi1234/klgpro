<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>


<head>

<title>下载审核</title>

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
<script type="text/javascript" src="${ctx}/mgr/js/ux/stDoc/stdocDowncheck.js"></script>
<style type="text/css">.red{color:red;font-size: 20px;}.left{float: left !important;}.vali{height:30px !important;border:1px solid #95b8e7 !important;border-radius: 5px 5px 5px 5px !important;padding-left: 8px !important;}.easyui-validatebox{background-color:white;}
/*取消全选按钮 */
.datagrid-header-check input{
	display: none;
} 
</style>


</head>

<body style="overflow: auto;">

	

				<!--功能按钮，项目基本信息-->
				<div id="tb_patchInfo">
					<a href="javascript:void(0);" id="btnGrant_patchInfo" class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">批量通过</a>
					<a href="javascript:void(0);" id="btnRefuse_patchInfo" class="easyui-linkbutton"
					data-options="iconCls:'icon-clear',plain:true">批量拒绝</a>
				</div>


				<!--数据列表-->
				<div data-options="region:'center',border:false,singleSelect:true"
					title='文档基本信息' style="height:auto">
					<table id="data_grant" style="width:100%;higth:auto;">
						<!--加载申请下载的列表  -->
					</table>
				</div>

			</div>




			<!-- 点击查看详情 -->
			<div id="watchWin" title="查看文档信息" 
				style="float:left;width:100%;height:450px;;overflow-x:auto;overflow-y:auto;display: none;">
				<div class="form_div" style="height:650px;">
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
							<li id="file_watch2" style="width:350px;height:30px;text-align:center;font-size:18px;"></li>
							<li style="width:350px;height:30px;"><a id="fileDown2" class="atn">下载</a></li>
						</ul>
					</div>
					<div id="right2">
						<ul>
							<li style="text-align:center;width:150px;">作者：</li>
							<li id="author" style="text-align:left;width:200px;"></li>
							
							<li style="text-align:center;width:150px;">上传人：</li>
							<li id="createOper" style="text-align:left;width:200px;"></li>

							<li style="text-align:center;width:150px;">上传 时 间：</li>
							<li id="createTime" style="text-align:left;width:300px;"></li>
							
							<li style="font-weight: bold;font-size: 18px;">文档说明：</li>
							<li id="comment" style="text-align:left;width:500px;margin-left: 42px;"></li>
						</ul>
					</div>
					
					
					
					
				</div>
			</div>
			
			
			
			
			<!-- 单个拒绝 -->
			<div id="addWin" class="easyui-dialog" title="进行单个拒绝审批" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:420px;">
				<form id="addForm" class="formbody">
					<div class="formtitle"><span>单个审批拒绝</span></div>
						<input type="hidden" name="checkId" id="checkId" />
						<ul class="forminfo">
							<li><label>备注不通过原因(可以为空)</label>
							<textarea rows="8" cols="38" style="border:1px solid skyblue;padding:8px;" id="remark" class="easyui-validatebox"></textarea>
							</li>
							
							<li><br/><label>&nbsp;</label><input id="btnToRefuse" type="button" class="btn" value="提交"/></li>
						</ul>
				</form>
			</div>
			


			

</body>
</html>
