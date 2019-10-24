<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>数据字典</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
     
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI,namespace,jsUtil"); %>
	<%@include file="../inc/import.jsp" %>
	<script type="text/javascript" src="${ctx}/mgr/js/ux/datadic/dict.js"></script>
	
	<script>
$( function() {
	/**
	 * 初始化字典tree 
	 */
	$('#dict_tree').tree({
		url:path+"/dictionary/tree.action",
		lines:true,
	/*	onLoadSuccess: function(node, data){
			//重载时也会触发本事件
		},*/
		//右键点击节点时，触发
		onContextMenu: function(e, node){
			e.preventDefault();
			$('#dict_tree').tree('select', node.target);
			$('#dict_menu').menu('show', {
				left: e.pageX,
				top: e.pageY
			});
		},
		//点击节点时候，触发
		onClick: function(node){
			if(node.id!="0"){//不是顶级
				dict_clear_form();
				dict_loadForm(node);
				var pNode = $("#dict_tree").tree("getParent",node.target); 
				parentNodeInfo(pNode);
			}
		},
		onDblClick : function(node){
			if(node.id=="0"){
				$.messager.alert('操作提示',"顶级节点不可修改！","warning");  
			}
		}
	});
	//app_type_menucombo();
});
</script>
	
<style type="text/css">
   /******数据字典 ******/
   .dict_editForm td{
	  text-align: left;
	}
   	
	.dic_mainTitle{
		height: 24px;
		font-size: 16px;
		font-weight: bold;
		text-align: center;
		border:1px solid #99BBE8;
		color:#15428B;
		overflow:hidden;
		padding-top :5px;
		width:95%;
	}
	.dict_editForm{
		
		font-size: 12px;border-right:1px solid #BFD2E1;border-bottom:1px solid #BFD2E1;
	}
	
    .dict_editForm tr[name='tr']> td,.dict_editForm tr> th{border-left:1px solid #BFD2E1;border-top:1px solid #BFD2E1}
	
	.dict_editForm .title{
		text-align: right;
	}
	
	.dict_button{
		margin: 5px;
		padding: 1px 6px;
	}
   .dict_input{
		  width:80%;
		  height: 35px;
		  padding: 1px 0px;
		  line-height:40px;
		  border-top:solid 1px #a7b5bc;
		  border-left:solid 1px #a7b5bc; 
		  border-right:solid 1px #ced9df; 
		  border-bottom:solid 1px #ced9df; 
		  background:url(../images/inputbg.gif) repeat-x; 
		  text-indent:10px;
   }
</style>	
  </head>
  
  <body style="overflow: auto;">
    
		
		
<div id="dict_layout" class="easyui-layout" data-options="fit:true" >  
    <div data-options="region:'west',split:true,title:'数据字典列表(右键操作)'" style="width:200px;">
		<div class="easyui-panel" data-options="fit:true,border:false">
	    	<ul id="dict_tree">
				<div id="dict_menu" class="easyui-menu" style="width:120px;">
					<div onclick="dict_addNode()" data-options="iconCls:'icon-add'">添加子级字典项</div>
					<div onclick="dict_updateNode()" data-options="iconCls:'icon-edit'">编辑字典项</div>
					<div onclick="dict_delNode()" data-options="iconCls:'icon-remove'">删除字典项</div>
				</div>
	    	</ul> 
	    </div>    
    </div>  <!-- region:west -->
    <div data-options="region:'center',title:'数据字典维护'" style="padding:5px;">
		<form id="dict_form" method="post" style='margin:0;padding:0;height: 100%;vertical-align: middle;'>
			<!-- 
			<input type=hidden name="typeFlag" id="typeFlag" value="">
			<input type=hidden name="controlFlag" id="controlFlag" value="">
			 -->
		
			 <input type="hidden" name="level" value=""><%-- 添加时：为父节点所在层次 ；修改时：为本节点 --%>
			<table class="dict_editForm"  style="width: 70%;height: 90%;margin-left: 70px;margin-bottom: 10px;margin-top: 0px;"  cellpadding=0 cellspacing=0 >
				<colgroup width="20%"></colgroup>
				<colgroup width="70%"></colgroup>  
				
				<tr name="tr" style="height: 80px;">
					<td colspan="4" class="dic_mainTitle" style="text-align: center;">数据字典维护</td>
				</tr>
				<tr name="tr">
					
					<td class="title">父节点ID：</td>
					<td>
						<input type="text" class="dict_input" value="" style="border:0px;" name="operId" readonly="readonly"/>
					</td>
				</tr>
				
				<tr name="tr">
				    <td class="title">父节点名称：</td>
					<td><input type="text" class="dict_input" value="" style="border:0px;"  name="upSystemName" readonly="readonly"/>
					</td>
				</tr>
				<tr name="tr">
					 <td class="title">字典项ID：</td>
					<td>
						<input type="text" class="dict_input" value="" style="border:0px;"  name="dicNum" readonly="readonly"/>
					</td>
				</tr>
				
				
				<tr name="tr">
				      <td class="title"><span style="color:#F00">*</span>中文名称：</td>
					<td>
						<input type="text" maxlength="40" value="" name="dicName" class="dict_input"/>
					</td>
				</tr>
				<tr>
				
				<tr name="tr">
					 <td class="title">属性值：</td>
					<td>
						<input type="text" class="dict_input" value=""   name="propertiyValue"/>
					</td>
				</tr>
				</tr>
				<tr name="tr">
					<td colspan="4" style="text-align:center;">
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk'" onclick="saveMenu()">保&nbsp;存</a>  
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cross'" onclick="resetMenu()">清&nbsp;空</a>  
					</td>
				</tr>
			</table>
		</form>
    </div>  <!-- region:center -->
</div>  <!-- dict_layout -->





		
	
  </body>
</html>
