var copyProjectNum;
var loadSign = false;

$(function(){
	// 初始化表格
	var table = {
		url : path + '/addsubAsset/list.json',
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
			width : 130			
		}, {
			field : 'datyType',
			title : '项目类型',
			align : 'center',
			width : 90,
			formatter : function(value, row, index) {
				if(value == 0){
					return "增资扩股-变更出资人股份";
				}else if(value == 1){
					return "增资扩股-新增出资人";
				}
			}
		}, {
			field : 'zhuBanDept',
			title : '主办部门',
			align : 'center',
			width : 90,
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
		onClickRow:function(rowIndex,rowData){
			copyProjectNum = rowData.projectNum;	//存储当前项目编号
			if(!loadSign){
				$('#datagridPM').datagrid(tablePM);
				$('#tbPM').show();
				loadSign = true;
			}
			//单击一行时，根据项目编号查询出资人信息，然后重新加载列表
			$('#datagridPM').datagrid('load',{projectNum:copyProjectNum});		
		},
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
	$("#btnChangeStock").click(function(){
		$('#addWinChangeStock').dialog('open');	//打开对话框
		$('#addFormChangeStock').find('input[name="projectType"]').val("增资扩股");	//为了设置默认值
	});
	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAddChangeStock").click(function(){
		if($('#zhucezibenCombo').combobox('getValue') == "1"){	//注册资本增加
			$('#addFormChangeStock').find('input[name="addCap"]').val($('#zhuceziben').val());		
		}else{
			$('#addFormChangeStock').find('input[name="subCap"]').val($('#zhuceziben').val());
		}
		
		if($('#chigubiliCombo').combobox('getValue') == "1"){	//持股比例增加
			$('#addFormChangeStock').find('input[name="addRatio"]').val($('#chigubili').val());		
		}else{
			$('#addFormChangeStock').find('input[name="subRatio"]').val($('#chigubili').val());
		}
		
		if($("#addFormChangeStock").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/addsubAsset/addTwo.json',
				data: $("#addFormChangeStock").serialize(),	//序列化一组表单元素，将表单内容编码为用于提交的字符串
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWinChangeStock').dialog('close');
								$('#datagrid').datagrid(table);
								$('#addFormChangeStock').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//新增按钮
	$("#btnAddPayMan").click(function(){
		$('#addWinChuZiRen').dialog('open');	//打开对话框
		$('#addFormChuZiRen').find('input[name="projectType"]').val("增资扩股");	//为了设置默认值
	});
	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAddChuZiRen").click(function(){
		if($("#addFormChuZiRen").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/addsubAsset/addOne.json',
				data: $("#addFormChuZiRen").serialize(),	//序列化一组表单元素，将表单内容编码为用于提交的字符串
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWinChuZiRen').dialog('close');
								$('#datagrid').datagrid(table);
								$('#addWinChuZiRen').form('clear');
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
			 
			 if(arr[0].datyType == "0"){		//变更出资人股份				 
				 //把该条信息回显在编辑窗口中
				 $('#editFormChangeStock').find('input[name="changeId"]').val(arr[0].changeId);
				 $('#editFormChangeStock').find('input[name="datyType"]').val(arr[0].datyType);
				 $('#editFormChangeStock').find('input[name="createOper"]').val(arr[0].createOper);
				 $('#editFormChangeStock').find('input[name="cteateTime"]').val(arr[0].cteateTime);
				 $('#editFormChangeStock').find('input[name="projectNum"]').val(arr[0].projectNum);
				 
				 $('#editFormChangeStock').find('input[name="projectName"]').val(arr[0].projectName);
				 $('#editFormChangeStock').find('input[name="projectType"]').val(arr[0].projectType);
				 $('#dept2').combotree('setValue',arr[0].zhuBanDept);
				 $('#combo2').combobox('setValue',arr[0].doBody);
				 $('#combo6').combobox('setValue',arr[0].beDoBody);
				 $('#combo10').combobox('setValue',arr[0].beLongMan);
				 
				 if(arr[0].isAssetEval == "yes"){
					 $('#editFormChangeStock').find("input[type=radio][name=isAssetEval][value=yes]").attr("checked",'checked');
				 }else{
					 $('#editFormChangeStock').find("input[type=radio][name=isAssetEval][value=no]").attr("checked",'checked');
				 }
				 
				 if(arr[0].isOpenNotice == "yes"){
					 $('#editFormChangeStock').find("input[type=radio][name=isOpenNotice][value=yes]").attr("checked",'checked');
				 }else{
					 $('#editFormChangeStock').find("input[type=radio][name=isOpenNotice][value=no]").attr("checked",'checked');
				 }
				 
				 $('#startTT').datebox("setValue", arr[0].startTime);	//日期框赋值必须要用ID
				 $('#endTT').datebox("setValue", arr[0].endTime);	 	//日期框赋值必须要用ID
				 $('#editFormChangeStock').find('input[name="signCap"]').val(arr[0].signCap);
				 
				 if(arr[0].addCap){		//增加
					 $('#zhucezibenCombo1').combobox('setValue','1');
					 $('#zhuceziben1').val(arr[0].addCap);				 
				 }
				 
				 if(arr[0].subCap){		//减少
					 $('#zhucezibenCombo1').combobox('setValue','2');
					 $('#zhuceziben1').val(arr[0].subCap);
				 }
				 
				 if(arr[0].addRatio){		//增加持股
					 $('#chigubiliCombo1').combobox('setValue','1');
					 $('#chigubili1').val(arr[0].addRatio);
				 }
				 
				 if(arr[0].subRatio){		//减少持股
					 $('#chigubiliCombo1').combobox('setValue','2');
					 $('#chigubili1').val(arr[0].subRatio);
				 }			 
				 $('#editFormChangeStock').find('input[name="valueGist"]').val(arr[0].valueGist);
				 $('#editFormChangeStock').find('input[name="atChangeRatio"]').val(arr[0].atChangeRatio);
				 $('#editFormChangeStock').find('input[name="atChangeCap"]').val(arr[0].atChangeCap);
				 //然后打开窗口			 			 
				 $('#editWinChangeStock').dialog('open');
								 				 				 		 			 
			 }else{			//增加出资人
				 
				 $('#editFormChuZiRen').find('input[name="changeId"]').val(arr[0].changeId);
				 $('#editFormChuZiRen').find('input[name="projectName"]').val(arr[0].projectName);
				 $('#editFormChuZiRen').find('input[name="projectType"]').val(arr[0].projectType);
				 $('#editFormChuZiRen').find('input[name="datyType"]').val(arr[0].datyType);
				 $('#editFormChuZiRen').find('input[name="createOper"]').val(arr[0].createOper);
				 $('#editFormChuZiRen').find('input[name="cteateTime"]').val(arr[0].cteateTime);
				 $('#editFormChuZiRen').find('input[name="projectNum"]').val(arr[0].projectNum);
				 $('#dept4').combotree('setValue',arr[0].zhuBanDept);
				 $('#combo4').combobox('setValue',arr[0].doBody);
				 $('#combo8').combobox('setValue',arr[0].beDoBody);
				 
				 if(arr[0].isAssetEval == "yes"){
					 $('#editFormChuZiRen').find("input[type=radio][name=isAssetEval][value=yes]").attr("checked",'checked');
				 }else{
					 $('#editFormChuZiRen').find("input[type=radio][name=isAssetEval][value=no]").attr("checked",'checked');
				 }
				 
				 if(arr[0].isOpenNotice == "yes"){
					 $('#editFormChuZiRen').find("input[type=radio][name=isOpenNotice][value=yes]").attr("checked",'checked');
				 }else{
					 $('#editFormChuZiRen').find("input[type=radio][name=isOpenNotice][value=no]").attr("checked",'checked');
				 }
				 
				 $('#startXX').datebox("setValue", arr[0].startTime);	//日期框赋值必须要用ID
				 $('#endXX').datebox("setValue", arr[0].endTime);	 	//日期框赋值必须要用ID
				 $('#editFormChuZiRen').find('input[name="signCap"]').val(arr[0].signCap);
				 $('#editFormChuZiRen').find('input[name="valueGist"]').val(arr[0].valueGist);
				 
				 //然后打开窗口			 			 
				 $('#editWinChuZiRen').dialog('open');			 
			 }
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	//更新提交
	$("#btnToUpdateChangeStock").click(function(){
		
		if($('#zhucezibenCombo1').combobox('getValue') == "1"){	//注册资本增加
			$('#editFormChangeStock').find('input[name="addCap"]').val($('#zhuceziben1').val());		
		}else{
			$('#editFormChangeStock').find('input[name="subCap"]').val($('#zhuceziben1').val());
		}
		
		if($('#chigubiliCombo1').combobox('getValue') == "1"){	//持股比例增加
			$('#editFormChangeStock').find('input[name="addRatio"]').val($('#chigubili1').val());		
		}else{
			$('#editFormChangeStock').find('input[name="subRatio"]').val($('#chigubili1').val());
		}
		
		if($("#editFormChangeStock").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/addsubAsset/update.json',
				async: false,
				data: $("#editFormChangeStock").serialize(),
				success: function(data){
							if(data.result == true){
								alert(data.msg);
								$('#editWinChangeStock').dialog('close');
								$('#datagrid').datagrid(table);
								$('#editWinChangeStock').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//更新提交
	$("#btnToUpdateChuZiRen").click(function(){
		if($("#editFormChuZiRen").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/addsubAsset/update.json',
				data: $("#editFormChuZiRen").serialize(),
				success: function(data){
							if(data.result == true){
								alert(data.msg);
								$('#editWinChuZiRen').dialog('close');
								$('#datagrid').datagrid(table);
								$('#editWinChuZiRen').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
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
							url: path + '/addsubAsset/delete.json',
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
	
	//查看按钮
	$("#btnWatch").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		 if(arr.length == 1){		 
			 if(arr[0].datyType == "0"){	//变更出资人							 
				 //把该条信息回显在查看窗口中
				 $('#watchFormChangeStock').find('input[name="projectName"]').val(arr[0].projectName);
				 $('#watchFormChangeStock').find('input[name="projectType"]').val(arr[0].projectType);
				 
				 var param = {};
				 param.deptId = arr[0].zhuBanDept.substring(1);
				 $.ajax({
					 type: 'post',
					 data: param,
					 url: path + '/addsubAsset/queryDeptInfo.json',
					 success: function(result){
						 $('#watchFormChangeStock').find('input[name="zhuBanDept"]').val(result.deptName);
					 }
				 });
				 
				 if(arr[0].isAssetEval == "yes"){
					 $('#watchFormChangeStock').find("input[name=isAssetEval]").val("是");
				 }else{
					 $('#watchFormChangeStock').find("input[name=isAssetEval]").val("否");
				 }
				 
				 if(arr[0].isOpenNotice == "yes"){
					 $('#watchFormChangeStock').find("input[name=isOpenNotice]").val("是");
				 }else{
					 $('#watchFormChangeStock').find("input[name=isOpenNotice]").val("否");
				 }
				 
				 $('#watchFormChangeStock').find('input[name="doBody"]').val(arr[0].doBody);
				 $('#watchFormChangeStock').find('input[name="startTime"]').val(arr[0].startTime);
				 $('#watchFormChangeStock').find('input[name="endTime"]').val(arr[0].endTime);
				 $('#watchFormChangeStock').find('input[name="signCap"]').val(arr[0].signCap);
				 if(arr[0].addCap){
					 $('#watchFormChangeStock').find('input[name="addCap"]').val(arr[0].addCap);
					 $('#watchFormChangeStock').find('input[name="subCap"]').parent().hide();
				 }
				 if(arr[0].subCap){
					 $('#watchFormChangeStock').find('input[name="subCap"]').val(arr[0].subCap);
					 $('#watchFormChangeStock').find('input[name="addCap"]').parent().hide();
				 }
				 
				 $('#watchFormChangeStock').find('input[name="atChangeCap"]').val(arr[0].atChangeCap);		 
				 
				 if(arr[0].addRatio){
					 $('#watchFormChangeStock').find('input[name="addRatio"]').val(arr[0].addRatio);
					 $('#watchFormChangeStock').find('input[name="subRatio"]').parent().hide();
				 }
				 if(arr[0].subRatio){
					 $('#watchFormChangeStock').find('input[name="subRatio"]').val(arr[0].subRatio);
					 $('#watchFormChangeStock').find('input[name="addRatio"]').parent().hide();
				 }
				 
				 $('#watchFormChangeStock').find('input[name="atChangeRatio"]').val(arr[0].atChangeRatio);
				 $('#watchFormChangeStock').find('input[name="valueGist"]').val(arr[0].valueGist);
				 $('#watchFormChangeStock').find('input[name="beDoBody"]').val(arr[0].beDoBody);
				 //然后打开窗口			 			 
				 $('#watchWinChangeStock').dialog('open');				 
			 }else{		 				
				 $('#watchFormChuZiRen').find('input[name="projectName"]').val(arr[0].projectName);
				 $('#watchFormChuZiRen').find('input[name="projectType"]').val(arr[0].projectType);
				 var param = {};
				 param.deptId = arr[0].zhuBanDept.substring(1);
				 $.ajax({
					 type: 'post',
					 data: param,
					 url: path + '/addsubAsset/queryDeptInfo.json',
					 success: function(result){
						 $('#watchFormChuZiRen').find('input[name="zhuBanDept"]').val(result.deptName);
					 }
				 });
				 $('#watchFormChuZiRen').find('input[name="doBody"]').val(arr[0].doBody);
				 
				 if(arr[0].isAssetEval == "yes"){
					 $('#watchFormChuZiRen').find("input[name=isAssetEval]").val("是");
				 }else{
					 $('#watchFormChuZiRen').find("input[name=isAssetEval]").val("否");
				 }
				 
				 if(arr[0].isOpenNotice == "yes"){
					 $('#watchFormChuZiRen').find("input[name=isOpenNotice]").val("是");
				 }else{
					 $('#watchFormChuZiRen').find("input[name=isOpenNotice]").val("否");
				 }
				 
				 $('#watchFormChuZiRen').find('input[name="startTime"]').val(arr[0].startTime);
				 $('#watchFormChuZiRen').find('input[name="endTime"]').val(arr[0].endTime);
				 $('#watchFormChuZiRen').find('input[name="signCap"]').val(arr[0].signCap);
				 $('#watchFormChuZiRen').find('input[name="beDoBody"]').val(arr[0].beDoBody);
				 $('#watchFormChuZiRen').find('input[name="valueGist"]').val(arr[0].valueGist);
				 //然后打开窗口			 			 
				 $('#watchWinChangeStock').dialog('open');
			 }		 
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要查看的数据');
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
							url: path + '/addsubAsset/submit.json',
							data: param,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagrid').datagrid(table);
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
	
	$('#dept3').combotree({    
		url : path + '/deptInfo/list.json'
	});
	
	$('#dept4').combotree({    
		url : path + '/deptInfo/list.json'
	});
	
	//生成公司的下拉列表
	$.ajax({
		type: 'post',
		url: path + '/projectSetcomp/queryCompany.json',
		success: function(result){
			var dataOfCombo = new Array();
			for(var i = 0; i < result.length; i++){
				dataOfCombo.push({ "text":result[i].companyName, "id":result[i].companyName });
			}
			$("#combo1").combobox("loadData", dataOfCombo);
			$("#combo2").combobox("loadData", dataOfCombo);
			$("#combo3").combobox("loadData", dataOfCombo);
			$("#combo4").combobox("loadData", dataOfCombo);
			$("#combo5").combobox("loadData", dataOfCombo);
			$("#combo6").combobox("loadData", dataOfCombo);
			$("#combo7").combobox("loadData", dataOfCombo);
			$("#combo8").combobox("loadData", dataOfCombo);
		}
	});
	

	
	var tablePM = {
		url : path + '/addsubAsset/listPM.json',
		columns : [ [ {
			field : 'paymanId',
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
			field : 'paymanName',
			title : '出资人名称',
			align : 'center',
			width : 150			
		}, {
			field : 'paymanType',
			title : '出资人类型',
			align : 'center',
			width : 30,
			formatter : function(value, row, index) {
				if(value == "geren"){
					return "个人";
				}else if(value == "qiye"){
					return "企业";
				}
			}
		}, {
			field : 'payAmount',
			title : '出资额',
			align : 'center',
			width : 60
			
		}, {
			field : 'currency',
			title : '币种',
			align : 'center',
			width : 20
		}, {
			field : 'czhCap',
			title : '出资后出资人的注册资本为',
			align : 'center',
			width : 80
		}, {
			field : 'czhRatio',
			title : '出资后出资人占持股比例为',
			align : 'center',
			width : 80
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		toolbar : "#tbPM"		//功能按钮
	};
		
	
	//新增按钮
	$("#btnAddPM").click(function(){		
		var arr = $('#datagrid').datagrid("getSelections");
		if(arr.length == 1){
			$('#addFormPM').find('input[name="projectNum"]').val(arr[0].projectNum);
			$('#addWinPM').dialog('open');	//打开对话框
		}else if(arr.length > 1){
			$.messager.alert('提醒','请选择单条的增资扩股信息');
		}else{
			$.messager.alert('提醒','请选择关联的增资扩股信息');
		}
		
	});
	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAddPM").click(function(){
		if($("#addFormPM").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/addsubAsset/addPM.json',
				data: $("#addFormPM").serialize(),	//序列化一组表单元素，将表单内容编码为用于提交的字符串
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWinPM').dialog('close');
								$('#datagridPM').datagrid('load',{projectNum:copyProjectNum});
								$('#addFormPM').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//编辑按钮
	$("#btnEditPM").click(function(){
		//取得选中的数据
		var arr = $('#datagridPM').datagrid("getSelections");
		 if(arr.length == 1){		
			 //把该条信息回显在编辑窗口中
			 $('#editFormPM').find('input[name="paymanId"]').val(arr[0].paymanId);
			 $('#editFormPM').find('input[name="paymanName"]').val(arr[0].paymanName);			
			 $('#editFormPM').find('input[name="projectNum"]').val(arr[0].projectNum);
			 $('#editFormPM').find('input[name="createTime"]').val(arr[0].createTime);
			 $('#editFormPM').find('input[name="createOper"]').val(arr[0].createOper);
			 $('#editFormPM').find('input[name="projectType"]').val(arr[0].projectType);
			 if(arr[0].paymanType == "geren"){
				 $('#editFormPM').find("input[type=radio][name=paymanType][value=geren]").attr("checked",'checked');
			 }else{
				 $('#editFormPM').find("input[type=radio][name=paymanType][value=qiye]").attr("checked",'checked');
			 }
						 			 			 				 
			 $('#editFormPM').find('input[name="cardNum"]').val(arr[0].cardNum);
			 $('#editFormPM').find('input[name="orgNum"]').val(arr[0].orgNum);
			 $('#editFormPM').find('input[name="payAmount"]').val(arr[0].payAmount);
			 $('#combo11').combobox('setValue',arr[0].currency);
			 $('#editFormPM').find('input[name="czhCap"]').val(arr[0].czhCap);
			 $('#editFormPM').find('input[name="czhRatio"]').val(arr[0].czhRatio);
			 
			 //然后打开窗口			 			 
			 $('#editWinPM').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	//更新提交
	$("#btnToUpdatePM").click(function(){
		if($("#editFormPM").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/addsubAsset/updatePM.json',
				data: $("#editFormPM").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWinPM').dialog('close');
								$('#datagridPM').datagrid('load',{projectNum:copyProjectNum});
								$('#editWinPM').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//删除按钮
	$("#btnDeletePM").click(function(){
		//取得选中的数据
		var arr = $('#datagridPM').datagrid("getSelections");
		
		 if(arr.length == 1){
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？',function(r){    
				 if (r){ 
					 var paymanIdArr = 'paymanIdArr=';
					 for(var i in arr){
						 paymanIdArr += (i == 0 ? '' : '#') + arr[i].paymanId;
					 }					 
					 $.ajax({
							type: 'post',
							url: path + '/addsubAsset/deletePM.json',
							data: paymanIdArr,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagridPM').datagrid('load',{projectNum:copyProjectNum});
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
	
	$('#combo5').combobox({  
        onChange: function (newValue, oldValue) {  
        	var param = {};
			param.companyName = newValue;
			$.ajax({
				type: 'post',
				data: param,
				async: false,
				url: path + '/addsubAsset/querySignCapital.json',
				success: function(result){
					$('#biaodi1').val(result.signCapital);
				}
			});
			queryPayMan(newValue);
        }  
    });
	
	$('#combo6').combobox({  
        onChange: function (newValue, oldValue) {  
        	var param = {};
			param.companyName = newValue;
			$.ajax({
				type: 'post',
				data: param,
				async: false,
				url: path + '/addsubAsset/querySignCapital.json',
				success: function(result){
					$('#biaodi2').val(result.signCapital);
				}
			});
			queryPayMan(newValue);
        }  
    });
	
	$('#combo7').combobox({  
        onChange: function (newValue, oldValue) {  
        	var param = {};
			param.companyName = newValue;
			$.ajax({
				type: 'post',
				data: param,
				async: false,
				url: path + '/addsubAsset/querySignCapital.json',
				success: function(result){
					$('#biaodi3').val(result.signCapital);
				}
			});		
        }  
    });
	
	$('#combo8').combobox({  
        onChange: function (newValue, oldValue) {  
        	var param = {};
			param.companyName = newValue;
			$.ajax({
				type: 'post',
				data: param,
				async: false,
				url: path + '/addsubAsset/querySignCapital.json',
				success: function(result){
					$('#biaodi4').val(result.signCapital);
				}
			});
        }  
    });
	
});

function queryPayMan(companyName){
	//生成出资人下拉列表
	var param = {};
	param.companyName = companyName;
	$.ajax({
		type: 'post',
		data: param,
		async: false,
		url: path + '/addsubAsset/queryPayMan.json',
		success: function(result){
			var payManOfCombo = new Array();
			for(var i = 0; i < result.length; i++){
				payManOfCombo.push({ "text":result[i].paymanName, "id":result[i].paymanName });
			}
			$("#combo9").combobox("loadData", payManOfCombo);
			$("#combo10").combobox("loadData", payManOfCombo);
		}
	});
}