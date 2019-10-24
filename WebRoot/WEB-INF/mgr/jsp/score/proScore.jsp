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
	src="${ctx}/mgr/js/ux/score/proScore.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/mgr/css/proInfo/proInfo.css">
<style type="text/css">
/* .form_dd li {
	float: left;
	list-style: none;
	margin: 0px;
	padding: 5px 0 5px 0;
	width: 49.8%;
	height: 35px;
} */
#right2 li{
	border:none;
}
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
.pagination-info{
	padding-right: 32px;
}
.form_div{
	border:none;
}
.form_div ul{
	border:none;
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
				<li><label>部门</label><input type="text" name="dicNum" id="depart" class="easyui-combobox dfinput" style="width:200px;"/></li>
				<li><label>姓名</label> <input type="text" class="scinput" name="userName"/></li>
				<li><label>积分>=</label> <input type="text" class="scinput" name="score"/></li>
				<li><label>积分<</label> <input type="text" class="scinput" name="scoreId"/></li>
					<li><input type="button" class="scbtn" value="查询"  /></li>
					<li>
					<label>我的积分</label>
					<label id="myScores" style="color: red;"></label>
					<label>我的排名</label>
					<label id="myCount" style="color: red;"></label>
					</li>
				</ul>
			</form>
			
			
			
			<!--数据列表-->
			<div data-options="region:'center',border:false,singleSelect:true" style="height:auto">
				<table id="datagridOfScore"></table>
			</div>

			
		</div>



		
		
		<!-- 点击显示的界面 -->
			<div id="watchWin" title="查看成果信息"
				style="float:left;width:1200px;height:650px;;overflow-x:auto;overflow-y:auto;display: none;">
				<div class="form_div" style="height:650px;">
					<div class="formtitle">
						<span>积分详情</span>
					</div>
					<div class="btn_div">
						<ul>
							<li><a href="javascript:void(0);" class="btn ml-180"
								onclick="return_main()" style="margin-right:40%;">关闭</a></li>
						</ul>
					</div>

					
					<div id="right2">
						<ul>
							<li style="text-align:center;width:150px;font-size:20px;">姓 名：</li>
							<li id="person" style="text-align:left;width:400px;font-size:20px;"></li>
							<li style="text-align:center;width:150px;font-size:20px">所 属 部 门：</li>
							<li id="departN" style="text-align:left;width:300px;font-size:20px;"></li>

							<li style="text-align:center;width:150px;font-size:20px">总 积 分：</li>
							<li id="score" style="text-align:left;width:400px;font-size:20px;"></li>
							<li style="text-align:center;width:150px;font-size:20px">当 前 排 名：</li>
							<li id="rowIndex" style="text-align:left;width:300px;font-size:20px;"></li>
							
							
							<li style="width:300px;font-size:20px;">在部门中的排名：</li>
							<li id="countInDepart" style="text-align:left;width:400px;font-size:20px;"></li>
							<li style="width:252px;"></li>
							
							<li style="text-align:center;width:150px;font-size:20px">积 分 来 源：</li>
							<li id="phone_watch2" style="text-align:left;width:850px;"></li>
							<li style="text-align:center;width:150px;font-size:20px">上 传：</li>
							<li id="up_score" style="text-align:left;width:50px;font-size: 20px;"></li>
							<li style="width:50px;"><span href="javascript:void(0);" 
								id="readUpDetail" style="font-size:15px;height: 20px;width: 40px;background: skyblue;color:white;text-align: center;">详情</span></li>
							
							<li style="text-align:center;width:150px;font-size:20px">下载：</li>
							<li id="down_score" style="text-align:left;width:50px;font-size: 20px;"></li>
							<li style="width:50px;"><span href="javascript:void(0);" 
								id="readDownDetail" style="font-size:15px;height: 20px;width: 40px;background: skyblue;color:white;text-align: center;">详情</span></li>
							<li></li>
						</ul>
					</div>
					
			</div>

		<!--上传所获积分列表  -->			
		<div id="uploadWin" class="easyui-dialog" title="进行多个审批拒绝" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:800px;height:auto;">
			<form id="addForms" class="formbody">
				<div class="formtitle"><span>成果上传所获积分列表</span></div>
				<table id="up_pat" style="width:100%;higth:auto;">
					<!--加载批量拒绝的列表  -->
				</table>
				<div class="formtitle"><span>文档上传所获积分列表</span></div>
				<table id="up_doc" style="width:100%;higth:auto;">
					<!--加载批量拒绝的列表  -->
				</table>	
				<br>
			</form>
		</div>
		<!--下载所获积分列表  -->			
		<div id="downWin" class="easyui-dialog" title="进行多个审批拒绝" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:800px;height:auto;">
			<form id="addForms" class="formbody">
				<div class="formtitle"><span>成果下载所获积分列表</span></div>
				<table id="down_pat" style="width:100%;higth:auto;">
					<!--加载列表  -->
				</table>
				<div class="formtitle"><span>文档下载所获积分列表</span></div>
				<table id="down_doc" style="width:100%;higth:auto;">
					<!--加载列表  -->
				</table>	
				<br>
			</form>
		</div>
		





		

	</div>
</body>
</html>