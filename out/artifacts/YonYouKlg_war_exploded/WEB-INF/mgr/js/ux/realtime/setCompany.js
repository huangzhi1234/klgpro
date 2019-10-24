var dataOfCombo = new Array();

$(function(){
	// 初始化表格
	var table = {
		url : path + '/projectSetcomp/list.json',
		columns : [ [ {
			field : 'changeId',
			width : 20,
			align : 'left',
			checkbox : true
		}, {
			field : 'rowIndex',
			title : '序号',
			width : 30,
			align : 'center',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : 'projectName',
			title : '项目名称',
			align : 'center',
			width : 150			
		}, {
			field : 'projectType',
			title : '项目类型',
			align : 'center',
			width : 30
		}, {
			field : 'zhuBanDept',
			title : '主办部门',
			align : 'center',
			width : 120,
			formatter : function(value, row, index) {
				var param = {};
				param.deptId = value.substring(1);
				var temp;
				$.ajax({
					type: 'post',
					data: param,
					async: false,		//不是同步就不能显示部门名称
					url: path + '/addsubAsset/queryDeptInfo.json',
					success: function(result){
						temp = result.deptName;
					}
				});
				return temp;
			}
		}, {
			field : 'projectNum',
			title : '当前流程',
			align : 'center',
			width : 40,
			formatter : function(value, row, index) {
				var paramFlow = {};
				paramFlow.projectNum = value;
				var flowString;
				$.ajax({
					type: 'post',
					data: paramFlow,
					async: false,
					url: path + '/flowControl/queryFlow.json',
					success: function(result){
						flowString = result.currentFlow;
					}
				});
				return flowString;
			}
		}, {
			field : 'doBody',
			title : '实施主体',
			align : 'center',
			width : 120
		}, {
			field : 'startTime',
			title : '发起时间',
			align : 'center',
			width : 50
		}, {
			field : 'endTime',
			title : '预计结束时间',
			align : 'center',
			width : 50
		}, {
			field : 'dataStatus',
			title : '数据状态',
			width : 30,
			align : 'center',
			formatter : function(value, row, index) {
				if(value == "1"){
					return "已提交";
				}else{
					return "未提交";
				}				
			}
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		singleSelect: true,
		queryParams : $("#schForm").formToJson(),	//查询框里的参数，将表单数据转换成json
		toolbar : "#tb"		//功能按钮
	};
		
	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagrid').datagrid(table);

	//查询按钮
	$(".scbtn").click(function(){
		$('#datagrid').datagrid('load',$("#schForm").formToJson());	//重新加载，后边是参数
	});
	
	//新增按钮
	$("#btnAdd").click(function(){
		$('#addWin').dialog('open');	//打开对话框
		$('#addForm').find('input[name="projectType"]').val("设立企业");	//为了设置默认值
	});
				
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/projectSetcomp/add.json',
				data: $("#addForm").serialize(),	//序列化一组表单元素，将表单内容编码为用于提交的字符串
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWin').dialog('close');
								$('#datagrid').datagrid(table);
								$('#addForm').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
		
	//编辑按钮
	$("#btnEdit").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		 if(arr.length == 1){
			 if(arr[0].dataStatus == "1"){
				 alert("这项数据已经进入下一个流程，无法编辑");
				 return;
			 }
			 //把该条信息回显在编辑窗口中
			 $('#editForm').find('input[name="changeId"]').val(arr[0].changeId);
			 $('#editForm').find('input[name="projectNum"]').val(arr[0].projectNum);
			 $('#editForm').find('input[name="createOper"]').val(arr[0].createOper);
			 $('#editForm').find('input[name="cteateTime"]').val(arr[0].cteateTime);
			 
			 $('#editForm').find('input[name="projectName"]').val(arr[0].projectName);
			 $('#editForm').find('input[name="projectType"]').val(arr[0].projectType);
			 			 
			 $('#dept2').combotree('setValue',arr[0].zhuBanDept);
			 $('#combo2').combobox('setValue',arr[0].doBody);
		 
			 if(arr[0].isAssetEval == "yes"){
				 $('#editForm').find("input[type=radio][name=isAssetEval][value=yes]").attr("checked",'checked');
			 }else{
				 $('#editForm').find("input[type=radio][name=isAssetEval][value=no]").attr("checked",'checked');
			 }
			 
			 if(arr[0].isOpenNotice == "yes"){
				 $('#editForm').find("input[type=radio][name=isOpenNotice][value=yes]").attr("checked",'checked');
			 }else{
				 $('#editForm').find("input[type=radio][name=isOpenNotice][value=no]").attr("checked",'checked');
			 }
			 
			 $('#editForm').find('input[name="companyName"]').val(arr[0].companyName);
			 $('#editForm').find('input[name="signCapital"]').val(arr[0].signCapital);
			 $('#editForm').find('input[name="myPlanCapital"]').val(arr[0].myPlanCapital);
			 $('#editForm').find('input[name="myReallyCapital"]').val(arr[0].myReallyCapital);
			 $('#editForm').find('input[name="myRatio"]').val(arr[0].myRatio);
			 $('#editForm').find('input[name="myReallyAmount"]').val(arr[0].myReallyAmount);
			 $('#startTT').datebox("setValue", arr[0].startTime);	//日期框赋值必须要用ID
			 $('#endTT').datebox("setValue", arr[0].endTime);	 	//日期框赋值必须要用ID			 
			 //然后打开窗口			 			 
			 $('#editWin').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	//更新提交
	$("#btnToUpdate").click(function(){
		if($("#editForm").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/projectSetcomp/update.json',
				data: $("#editForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWin').dialog('close');
								$('#datagrid').datagrid(table);
								$('#editWin').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//查看按钮
	$("#btnWatch").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		 if(arr.length == 1){
			 //把该条信息回显在编辑窗口中
			 $('#watchForm').find('input[name="projectName"]').val(arr[0].projectName);
			 $('#watchForm').find('input[name="projectType"]').val(arr[0].projectType);
			 var param = {};
			 param.deptId = arr[0].zhuBanDept.substring(1);
			 $.ajax({
					type: 'post',
					data: param,
					url: path + '/addsubAsset/queryDeptInfo.json',
					success: function(result){
						$('#watchForm').find('input[name="zhuBanDept"]').val(result.deptName);
					}
				});
			 
			 if(arr[0].isAssetEval == "yes"){
				 $('#watchForm').find("input[name=isAssetEval]").val("是");
			 }else{
				 $('#watchForm').find("input[name=isAssetEval]").val("否");
			 }
			 
			 if(arr[0].isOpenNotice == "yes"){
				 $('#watchForm').find("input[name=isOpenNotice]").val("是");
			 }else{
				 $('#watchForm').find("input[name=isOpenNotice]").val("否");
			 }
			 			 
			 $('#watchForm').find('input[name="doBody"]').val(arr[0].doBody);
			 $('#watchForm').find('input[name="level"]').val(arr[0].level);
			 $('#watchForm').find('input[name="companyName"]').val(arr[0].companyName);
			 $('#watchForm').find('input[name="signCapital"]').val(arr[0].signCapital);
			 $('#watchForm').find('input[name="myPlanCapital"]').val(arr[0].myPlanCapital);
			 $('#watchForm').find('input[name="myReallyCapital"]').val(arr[0].myReallyCapital);
			 $('#watchForm').find('input[name="myRatio"]').val(arr[0].myRatio);
			 $('#watchForm').find('input[name="myReallyAmount"]').val(arr[0].myReallyAmount);
			 $('#watchForm').find('input[name="startTime"]').val(arr[0].startTime);
			 $('#watchForm').find('input[name="endTime"]').val(arr[0].endTime);			
			 //然后打开窗口			 			 
			 $('#watchWin').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要查看的数据');
		 }
	});
	
	//删除按钮
	$("#btnDelete").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		
		 if(arr.length == 1){
			 if(arr[0].dataStatus == "1"){
				 alert("这项数据已经进入下一个流程，无法删除");
				 return;
			 }
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？',function(r){    
				 if (r){ 
					 var changeIdArr = 'changeIdArr=';
					 for(var i in arr){
						 changeIdArr += (i == 0 ? '' : '#') + arr[i].changeId;
					 }					 
					 $.ajax({
							type: 'post',
							url: path + '/projectSetcomp/delete.json',
							data: changeIdArr,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagrid').datagrid(table);;
									}else{
										$.messager.alert('提醒',data.msg);
									}
		            			}
						});
				 }
			 });
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要删除的数据');
		 }		 				
	});
	
	//立项提交
	$("#btnSubmit").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		
		 if(arr.length == 1){
			 if(arr[0].dataStatus == "1"){
				 alert("这项数据已经提交过");
				 return;
			 }
			 $.messager.confirm('确认','您确认要提交到内部决策吗？',function(r){    
				 if (r){ 
					 var param = {};
					 param.projectId = arr[0].projectNum;
					 param.projectName = arr[0].projectName;
					 param.isAssetEval = arr[0].isAssetEval;
					 param.isOpenNotice = arr[0].isOpenNotice;
					 
					 $.ajax({
							type: 'post',
							url: path + '/projectSetcomp/submit.json',
							data: param,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagrid').datagrid(table);;
									}else{
										$.messager.alert('提醒',data.msg);
									}
		            			}
						});
				 }
			 });
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要提交的数据');
		 }		 				
	});
	
	//生成部门的下拉树形列表
	$('#dept1').combotree({    
		url : path + '/deptInfo/list.json',   
	    required : true   
	});
	
	$('#dept2').combotree({    
		url : path + '/deptInfo/list.json'
	});
	
	//生成公司的下拉列表
	$.ajax({
		type: 'post',
		url: path + '/projectSetcomp/queryCompany.json',
		success: function(result){
			for(var i = 0; i < result.length; i++){
				dataOfCombo.push({ "text":result[i].companyName, "id":result[i].companyName });
			}
			$("#combo1").combobox("loadData", dataOfCombo);			
			$("#combo2").combobox("loadData", dataOfCombo);
		}
	});
	
});

//新增时计算持股比例
function calculation1(){
	var num1 = $('#zhuceziben').val();
	var num2 = $('#renjiao').val();
	var num3;
	if((num1)&&(num2)){
		num3 = parseFloat(num2)/parseFloat(num1)*100;
		$('#chigubili').val(num3 + "%");
	}	
}

//编辑时计算持股比例
function calculation2(){
	var num1 = $('#zhuceziben1').val();
	var num2 = $('#renjiao1').val();
	var num3;
	if((num1)&&(num2)){
		num3 = parseFloat(num2)/parseFloat(num1)*100;
		$('#chigubili1').val(num3 + "%");
	}	
}