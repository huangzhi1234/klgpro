<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>实时变动——增资扩股</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
    <script src="${ctx}/mgr/js/ux/realtime/addsubAsset.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/public.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/mgr/css/file.css">
    <style type="text/css">
    	.forminfo li label{width:142px;}
    </style>
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
						<input name="startTime" class="easyui-datebox scinput" data-options="sharedCalendar:'#cc',height:27" />
					</li>																													
					<li>					
						<input type="button" class="scbtn" value="查询" />
					</li>
				</ul>
			</form>
	
			<!--功能按钮-->
			<div id="tb">  
	    		<a id="btnAddPayMan" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增出资人</a>
	    		<a id="btnChangeStock" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">变更出资人股份</a> 
	    		<a id="btnEdit" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>
	    		<a id="btnWatch" class="easyui-linkbutton" data-options="iconCls:'icon-detail',plain:true">查看</a> 
	    		<a id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	    		<a id="btnSubmit" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true">提交</a>
			</div>
								 
			<!--数据列表-->
			<div data-options="region:'center',border:false,singleSelect:true" title='增资扩股' style="height: 350px">
				<table id="datagrid"></table>
			</div> 

			<div id="addWinChangeStock" class="easyui-dialog" title="新增变更出资人股份" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:570px;height:450px;">
				<form id="addFormChangeStock" class="formbody">
					<div class="formtitle"><span>新增变更出资人股份</span></div>
						<input name="datyType" type="hidden" value="0"/>
						<input name="addCap" type="hidden" />
						<input name="subCap" type="hidden" />
						<input name="addRatio" type="hidden" />
						<input name="subRatio" type="hidden" />
						<ul class="forminfo"> 
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input id="dept1" name="zhuBanDept" class="easyui-combotree" data-options="panelHeight:'auto',width:247,height:32" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>实施主体</label><input id="combo1" name="doBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>
							<li><label>标的企业</label><input id="combo5" name="beDoBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>					
						    <li><label>标的企业的现有注册资本</label><input id="biaodi1" name="signCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>选择出资人</label><input id="combo9" name="beLongMan" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>		    
						    <li><label>出资人注册资本</label><input id="zhucezibenCombo" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',width:70,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '增加',selected:true},{label: '2',value: '减少'}]" /><input id="zhuceziben" type="text" class="easyui-validatebox dfinputs" /></li>
						    <li><label>变更后出资人注册资本</label><input name="atChangeCap" type="text" class="easyui-validatebox dfinput"   /></li>			    
						    <li><label>出资人持股比例</label><input id="chigubiliCombo" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',width:70,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '增加',selected:true},{label: '2',value: '减少'}]" /><input id="chigubili" type="text" class="easyui-validatebox dfinputs" /></li>
						    <li><label>变更后出资人持股比例</label><input name="atChangeRatio" type="text" class="easyui-validatebox dfinput"   /></li>
						    <li><label>价值依据</label><input name="valueGist" type="text" class="easyui-validatebox dfinput"   /></li>
						    <li><label>发起时间</label><input name="startTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>	
						    <li><label>预计结束时间</label><input name="endTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>
							<li><button id="btnToAddChangeStock" class="btn ml-180">确认保存</button></li>
						</ul>
				</form>
			</div>
			<div id="cc" class="easyui-calendar"></div>			

			<div id="editWinChangeStock" class="easyui-dialog" title="编辑变更出资人股份" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:550px;height:500px;">
				<form id="editFormChangeStock" class="formbody">
					<div class="formtitle"><span>编辑变更出资人股份</span></div>
						<input name="changeId" type="hidden">
						<input name="createOper" type="hidden">
						<input name="cteateTime" type="hidden">
						<input name="projectNum" type="hidden">
						<input name="datyType" type="hidden" />
						<input name="addCap" type="hidden" />
						<input name="subCap" type="hidden" />
						<input name="addRatio" type="hidden" />
						<input name="subRatio" type="hidden" />			
						<ul class="forminfo">
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input id="dept2" name="zhuBanDept" class="easyui-combotree" data-options="panelHeight:'auto',width:247,height:32" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>实施主体</label><input id="combo2" name="doBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>
							<li><label>标的企业</label><input id="combo6" name="beDoBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>					
						    <li><label>标的企业的现有注册资本</label><input id="biaodi2" name="signCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>选择出资人</label><input id="combo10" name="beLongMan" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>		    
						    <li><label>出资人注册资本</label><input id="zhucezibenCombo1" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',width:70,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '增加'},{label: '2',value: '减少'}]" /><input id="zhuceziben1" type="text" class="easyui-validatebox dfinputs" /></li>
						    <li><label>变更后出资人注册资本</label><input name="atChangeCap" type="text" class="easyui-validatebox dfinput"   /></li>			    						
						    <li><label>出资人持股比例</label><input id="chigubiliCombo1" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',width:70,height:32,valueField: 'label',textField: 'value',data: [{label: '1',value: '增加'},{label: '2',value: '减少'}]" /><input id="chigubili1" type="text" class="easyui-validatebox dfinputs" /></li>
						    <li><label>变更后出资人持股比例</label><input name="atChangeRatio" type="text" class="easyui-validatebox dfinput"   /></li>
						    <li><label>价值依据</label><input name="valueGist" type="text" class="easyui-validatebox dfinput"   /></li>
						    <li><label>发起时间</label><input id="startTT" name="startTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>	
						    <li><label>预计结束时间</label><input id="endTT" name="endTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>
							<li><button id="btnToUpdateChangeStock" class="btn ml-100">确认更新</button></li>
						</ul>
				</form>
			</div>
			
			<div id="watchWinChangeStock" class="easyui-dialog" title="查看变更出资人股份" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:550px;height:500px;">
				<form id="watchFormChangeStock" class="formbody">
					<div class="formtitle"><span>查看变更出资人股份</span></div>								
						<ul class="forminfo">
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input name="zhuBanDept" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>实施主体</label><input name="doBody" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>标的企业</label><input name="beDoBody" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>标的企业的现有注册资本</label><input name="signCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>增加注册资本</label><input name="addCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>减少注册资本</label><input name="subCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>变更后出资人注册资本</label><input name="atChangeCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>						 
						    <li><label>增加持股比例</label><input name="addRatio" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>减少持股比例</label><input name="subRatio" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>变更后出资人持股比例</label><input name="atChangeRatio" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>价值依据</label><input name="valueGist" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>	
						    <li><label>发起时间</label><input name="startTime" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>预计结束时间</label><input name="endTime" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						</ul>
				</form>
			</div>
						
			<div id="addWinChuZiRen" class="easyui-dialog" title="新增出资人信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:570px;height:450px;">
				<form id="addFormChuZiRen" class="formbody">
					<div class="formtitle"><span>新增出资人信息</span></div>
						<input name="datyType" type="hidden" value="1"/>						
						<ul class="forminfo"> 
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input id="dept3" name="zhuBanDept" class="easyui-combotree" data-options="panelHeight:'auto',width:247,height:32" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>实施主体</label><input id="combo3" name="doBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>
							<li><label>标的企业</label><input id="combo7" name="beDoBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>					
						    <li><label>标的企业的现有注册资本</label><input id="biaodi3" name="signCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>					    
						    <li><label>价值依据</label><input name="valueGist" type="text" class="easyui-validatebox dfinput"   /></li>
						    <li><label>发起时间</label><input name="startTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>	
						    <li><label>预计结束时间</label><input name="endTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>
							<li><button id="btnToAddChuZiRen" class="btn ml-180">确认保存</button></li>
						</ul>
				</form>
			</div>
			
			<div id="editWinChuZiRen" class="easyui-dialog" title="编辑出资人信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:550px;height:500px;">
				<form id="editFormChuZiRen" class="formbody">
					<div class="formtitle"><span>编辑出资人信息</span></div>
						<input name="changeId" type="hidden">
						<input name="datyType" type="hidden">
						<input name="createOper" type="hidden">
						<input name="cteateTime" type="hidden">
						<input name="projectNum" type="hidden">							
						<ul class="forminfo">
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input id="dept4" name="zhuBanDept" class="easyui-combotree" data-options="panelHeight:'auto',width:247,height:32" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isAssetEval" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="yes" />是<input name="isOpenNotice" type="radio" class="easyui-validatebox userstyle" value="no" checked="checked"/>否</li>
							<li><label>实施主体</label><input id="combo4" name="doBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>
							<li><label>标的企业</label><input id="combo8" name="beDoBody" type="text" class="easyui-combobox" data-options="valueField:'id', textField:'text', panelHeight:'auto',width:247,height:32" /></li>					
						    <li><label>标的企业的现有注册资本</label><input id="biaodi4" name="signCap" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>					    
						    <li><label>价值依据</label><input name="valueGist" type="text" class="easyui-validatebox dfinput"   /></li>
						    <li><label>发起时间</label><input id="startXX" name="startTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>	
						    <li><label>预计结束时间</label><input id="endXX" name="endTime" class="easyui-datebox dfinput" data-options="sharedCalendar:'#cc',height:32" /></li>
							<li><button id="btnToUpdateChuZiRen" class="btn ml-100">确认更新</button></li>
						</ul>
				</form>
			</div>
			
			<div id="watchWinChuZiRen" class="easyui-dialog" title="查看出资人信息" data-options="closed:true,iconCls:'icon-edit',modal:true" style="width:550px;height:500px;">
				<form id="watchFormChuZiRen" class="formbody">
					<div class="formtitle"><span>查看出资人信息</span></div>								
						<ul class="forminfo">
							<li><label>项目名称</label><input name="projectName" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>项目类型</label><input name="projectType" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>主办部门</label><input name="zhuBanDept" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>是否资产评估</label><input name="isAssetEval" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>是否公开挂牌</label><input name="isOpenNotice" type="text" class="easyui-validatebox dfinput" readonly="readonly"/></li>
							<li><label>实施主体</label><input name="doBody" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
							<li><label>标的企业</label><input name="beDoBody" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>标的企业的现有注册资本</label><input name="signCap" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>						   
						    <li><label>价值依据</label><input name="valueGist" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>	
						    <li><label>发起时间</label><input name="startTime" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						    <li><label>预计结束时间</label><input name="endTime" type="text" class="easyui-validatebox dfinput" readonly="readonly" /></li>
						</ul>
				</form>
			</div>
			
			<!--出资人信息列表-->
			<div id="tabs" class="easyui-tabs" style="min-height: 230px;margin-top:20px;">
				<div data-options="region:'center',border:false,singleSelect:true" title='出资人信息' style="min-height: 200px;">
					<table id="datagridPM"></table>
				</div>
			</div>
			
			<!--功能按钮-->
			<div id="tbPM" style="display:none;">  
	    		<a id="btnAddPM" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>	    		
	    		<a id="btnEditPM" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>    		
	    		<a id="btnDeletePM" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>    		
			</div>
			
			<div id="addWinPM" class="easyui-dialog" title="新增出资人" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:570px;height:450px;">
				<form id="addFormPM" class="formbody">
					<div class="formtitle"><span>新增出资人 </span></div>
						<input name="projectNum" type="hidden">		
						<ul class="forminfo"> 
							<li><label>出资人名称</label><input name="paymanName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>出资人类型</label><input name="paymanType" type="radio" class="easyui-validatebox userstyle" value="geren" />个人<input name="paymanType" type="radio" class="easyui-validatebox userstyle" value="qiye" checked="checked"/>企业</li>						
							<li><label>出资人身份证信息</label><input name="cardNum" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>组织机构代码</label><input name="orgNum" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>出资额</label><input name="payAmount" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>币种</label><input name="currency" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '人民币',value: '人民币',selected:true},{label: '港币',value: '港币'},{label: '美元',value: '美元'}]" /></li>
							<li><label>出资后出资人注册资本为</label><input name="czhCap" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>出资后出资人持股比例为</label><input name="czhRatio" type="text" class="easyui-validatebox dfinput" /></li>
							<li><button id="btnToAddPM" class="btn ml-180">确认保存</button></li>
						</ul>
				</form>
			</div>
			
			<div id="editWinPM" class="easyui-dialog" title="编辑出资人信息" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:570px;height:450px;">
				<form id="editFormPM" class="formbody">
					<div class="formtitle"><span>编辑出资人信息 </span></div>
						<input name="paymanId" type="hidden">
						<input name="projectNum" type="hidden">
						<input name="createTime" type="hidden">
						<input name="createOper" type="hidden">
						<input name="projectType" type="hidden">	
						<ul class="forminfo"> 
							<li><label>出资人名称</label><input name="paymanName" type="text" class="easyui-validatebox dfinput"  invalidMessage="不能为空"/></li>
							<li><label>出资人类型</label><input name="paymanType" type="radio" class="easyui-validatebox userstyle" value="geren" />个人<input name="paymanType" type="radio" class="easyui-validatebox userstyle" value="qiye" checked="checked"/>企业</li>						
							<li><label>出资人身份证信息</label><input name="cardNum" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>组织机构代码</label><input name="orgNum" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>出资额</label><input name="payAmount" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>币种</label><input id="combo11" name="currency" class="easyui-combobox" data-options="editable:false,panelHeight:'auto',width:150,height:32,valueField: 'label',textField: 'value',data: [{label: '人民币',value: '人民币'},{label: '港币',value: '港币'},{label: '美元',value: '美元'}]" /></li>
							<li><label>出资后出资人注册资本为</label><input name="czhCap" type="text" class="easyui-validatebox dfinput" /></li>
							<li><label>出资后出资人持股比例为</label><input name="czhRatio" type="text" class="easyui-validatebox dfinput" /></li>
							<li><button id="btnToUpdatePM" class="btn ml-180">确认更新</button></li>
						</ul>
				</form>
			</div>
															
		</div>
	</body>
</html>