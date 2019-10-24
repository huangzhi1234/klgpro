var copyMonitNum;
var billFile1;	//全局变量，缓存文件在数据库的路径
var billFile2;
var billFile3;
var monitid;

$(function(){
	/*var data = new Array();
	$.ajax({
		type: 'post',
		url: ctx + '/mgr/monitMeeting/queryCompany.json',
		success: function(result){
			var length  = result.length;
			for(var i=0;i<length;i++){
				data.push({ "text":result[i].companyName, "id":result[i].companyId });
			}
			$("#companyName").combobox("loadData", data);
			$("#companyName1").combobox("loadData", data);
			
		}
	});*/
	// 初始化表格
	var table = {
		url : path + '/monitMeeting/list.json',
		columns : [ [ {
			field : 'monitId',
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
			field : 'companyName',
			title : '公司名称',
			align : 'center',
			width : 40,
			formatter : function(value, row, index) {
				var params = {};
				var compayName;
				params.companyId = value;
				$.ajax({
					type: 'post',
					url: ctx + "/mgr/monitMeeting/queryCompany.json",
					async:false,
					data: params,
					success: function(data){
						if(data != null){
							companyName = data.companyName;	
						}else{
							return null;
						}
						
		     		}
				});
				return companyName;
			}
		}, {
			field : 'meetingName',
			title : '会议名称',
			align : 'center',
			width : 40			
		}, {
			field : 'meetingDate',
			title : '会议日期',
			align : 'center',
			width : 30			
		}, {
			field : 'meetingAddress',
			title : '会议地点',
			align : 'center',
			width : 100
		}, {
			field : 'meetingType',
			title : '会议方式',
			align : 'center',
			width : 30,
			formatter : function(value, row, index) {
				if(value == "xc"){
					return "现场";
				}else if(value == "dh"){
					return "电话";
				}
			}			
		}, {
			field : 'joinMember',
			title : '参会监事',
			align : 'center',
			width : 50		
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		queryParams : $("#schForm").formToJson(),
		onClickRow:function(rowIndex,rowData){
			copyMonitNum = rowData.monitNum;	//存储当前组织机构代码			
			$('#datagrid_YA').datagrid('load',{monitNum:copyMonitNum});		
		},
		toolbar : "#tb"		//功能按钮
	};
		
	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagrid').datagrid(table);
	
	//查询按钮
	$(".scbtn").click(function(){
		$('#datagrid').datagrid('load',$("#schForm").formToJson());		//重新加载，后边是参数
	});
	
	var data = new Array();
	$.ajax({
		type: 'post',
		url: ctx + '/mgr/monitMeeting/queryListCompany.json',
		success: function(result){
			var length  = result.length;
			for(var i=0;i<length;i++){
				data.push({ "text":result[i].companyName, "id":result[i].companyId });
			}
			$("#companyName").combobox("loadData", data);
			$("#companyName1").combobox("loadData", data);
			
		}
	});
	
	//新增按钮
	$("#btnAdd").click(function(){
		//$('#addForm').form('clear');	//清空表单数据
		
		$('#addWin').dialog('open');	//打开对话框
	});

	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/monitMeeting/add.json',
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
			 //把该条信息回显在编辑窗口中
			/* var data = new Array();
				$.ajax({
					type: 'post',
					url: ctx + '/mgr/monitMeeting/queryCompany.json',
					success: function(result){
						var length  = result.length;
						for(var i=0;i<length;i++){
							data.push({ "text":result[i].companyName, "id":result[i].companyId });
						}
						$("#companyName1").combobox("loadData", data);
						
					}
				});*/
			 $("#companyName1").combobox("setValue", arr[0].companyName);
			 $('#editForm').find('input[name="monitId"]').val(arr[0].monitId);
			 $('#editForm').find('input[name="meetingNum"]').val(arr[0].meetingNum);
			 $('#editForm').find('input[name="meetingName"]').val(arr[0].meetingName);
			 $('#editForm').find('input[name="meetingAddress"]').val(arr[0].meetingAddress);
			 $('#editForm').find('input[name="joinMember"]').val(arr[0].joinMember);
			 $('#editForm').find('input[name="notJoinMember1"]').val(arr[0].notJoinMember1);
			 $('#editForm').find('input[name="reason1"]').val(arr[0].reason1);
			 $('#editForm').find('input[name="notJoinMember2"]').val(arr[0].notJoinMember2);
			 $('#editForm').find('input[name="reason2"]').val(arr[0].reason2);
			 
			 if(arr[0].meetingType == "xc"){
				 $('#editForm').find("input[type=radio][name=meetingType][value=xc]").attr("checked",'checked');
			 }else{
				 $('#editForm').find("input[type=radio][name=meetingType][value=dh]").attr("checked",'checked');
			 }
			 $('#startTT').datebox("setValue", arr[0].meetingDate);
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
				url: path + '/monitMeeting/update.json',
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
		
		var params = {};
		var companyName;
		params.companyId = arr[0].companyName;
		$.ajax({
			type: 'post',
			url: ctx + "/mgr/monitMeeting/queryCompany.json",
			async:false,
			data: params,
			success: function(data){
				if(data != null){
					companyName = data.companyName;	
				}else{
					return null;
				}
				
     		}
		});
		
		 if(arr.length == 1){
			 //把该条信息回显在编辑窗口中
			 $('#watchForm').find('input[name="companyName"]').val(companyName);			
			 $('#watchForm').find('input[name="meetingName"]').val(arr[0].meetingName);
			 $('#watchForm').find('input[name="meetingDate"]').val(arr[0].meetingDate);
			 $('#watchForm').find('input[name="meetingAddress"]').val(arr[0].meetingAddress);
			 $('#watchForm').find('input[name="JoinMember"]').val(arr[0].joinMember);
			 $('#watchForm').find('input[name="notJoinMember1"]').val(arr[0].notJoinMember1);
			 $('#watchForm').find('input[name="reason1"]').val(arr[0].reason1);
			 $('#watchForm').find('input[name="notJoinMember2"]').val(arr[0].notJoinMember2);
			 $('#watchForm').find('input[name="reason2"]').val(arr[0].reason2);
			 
			 if(arr[0].meetingType == "xc"){
				 $('#watchForm').find("input[name=meettingType]").val("现场");
			 }else{
				 $('#watchForm').find("input[name=meettingType]").val("电话");
			 }
			
			 //然后打开窗口
			 $('#watchWin').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	//删除按钮
	$("#btnDelete").click(function(){
		var arr = $('#datagrid').datagrid("getSelections");
		 if(arr.length > 0){
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？',function(r){    
				 if (r){ 
					 var mettingIdArr = 'mettingIdArr=';
					 for(var i in arr){
						 mettingIdArr += (i == 0 ? '' : '#') + arr[i].monitid;
					 }					 
					 $.ajax({
							type: 'post',
							url: path + '/meetingManage/delete.json',
							data: mettingIdArr,
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
		 }else{
			 $.messager.alert('提醒','请选择要删除的数据');
		 }
	});
		
	// 初始化表格
	var table_YA = {
		url : path + '/monitBill/list.json',
		columns : [ [ {
			field : 'monitId',
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
			field : 'issue',
			title : '议题',
			align : 'center',
			width : 70			
		}, {
			field : 'resolutionresult',
			title : '决议结果',
			align : 'center',
			width : 50			
		}, {
			field : 'agreeCount',
			title : '赞成票数',
			align : 'center',
			width : 50
		}, {
			field : 'opposeCount',
			title : '反对票数',
			align : 'center',
			width : 50		
		}, {
			field : 'giveUpCount',
			title : '弃权票数',
			align : 'center',
			width : 50			
		}, {
			field : 'managerIdea',
			title : '监事意见',
			align : 'center',
			width : 150		
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		toolbar : "#tb_YA"		//功能按钮
	};
		
	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagrid_YA').datagrid(table_YA);

	//新增按钮
	$("#btnAdd_YA").click(function(){
		var arr = $('#datagrid').datagrid("getSelections");
		if(arr.length == 1){
			$('#addForm_YA').find('input[name="meetingNum"]').val(arr[0].meetingNum);			
			$('#addWin_YA').dialog('open');
		}else if(arr.length > 1){
			$.messager.alert('提醒','请选择单条会议信息');
		}else{
			$.messager.alert('提醒','请选择关联的会议信息');
		}	
	});
	
	
	//删除按钮
	$("#btnDelete_YA").click(function(){
		var arr = $('#datagrid_YA').datagrid("getSelections");
		 if(arr.length > 0){
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？',function(r){    
				 if (r){ 
					 var billIdArr = 'billIdArr=';
					 for(var i in arr){
						 billIdArr += (i == 0 ? '' : '#') + arr[i].monitId;
					 }					 
					 $.ajax({
							type: 'post',
							url: path + '/monitBill/delete.json',
							data: billIdArr,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagrid_YA').datagrid('load',{monitNum:copyMonitNum});
									}else{
										$.messager.alert('提醒',data.msg);
									}
		            			}
						});
				 }
			 }); 
		 }else{
			 $.messager.alert('提醒','请选择要删除的数据');
		 }
	});
	
	//编辑按钮
	$("#btnEdit_YA").click(function(){
		//取得选中的数据
		var arr = $('#datagrid_YA').datagrid("getSelections");
		 if(arr.length == 1){
			 monitid = arr[0].monitId;		//存储主键
			 //把该条信息回显在编辑窗口中
			 $('#editForm_YA').find('input[name="monitId"]').val(arr[0].monitId);
			 $('#editForm_YA').find('input[name="monitNum"]').val(arr[0].monitNum);			
			 billFile1 = arr[0].billFile1;	//缓存文件在数据库的路径
			 if(!billFile1){
				 $('#billFile1Del').hide();
			 }
			 
			 billFile2 = arr[0].billFile2;
			 if(!billFile2){					//如果为空，就是没有文件，那么删除按钮隐藏
				 $('#billFile2Del').hide();
			 }
			 
			 billFile3 = arr[0].billFile3;
			 if(!billFile3){
				 $('#billFile3Del').hide();
			 }
			 					 
			 $('#editForm_YA').find('input[name="issue"]').val(arr[0].issue);
			 $('#editForm_YA').find('input[name="resolutionresult"]').val(arr[0].resolutionresult);
			 $('#editForm_YA').find('input[name="agreeCount"]').val(arr[0].agreeCount);
			 $('#editForm_YA').find('input[name="opposeCount"]').val(arr[0].opposeCount);
			 $('#editForm_YA').find('input[name="giveUpCount"]').val(arr[0].giveUpCount);
			 $('#editForm_YA').find('input[name="managerIdea"]').val(arr[0].managerIdea);
			 $('#editForm_YA').find('input[name="comment"]').val(arr[0].comment);
			
			 //然后打开窗口			 			 
			 $('#editWin_YA').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	$('#billFile1Del').click(function(){
		deleteFile(billFile1,"billFile1");
	});
	
	$('#billFile2Del').click(function(){
		deleteFile(billFile2,"billFile2");
	});
	
	$('#billFile3Del').click(function(){
		deleteFile(billFile3,"billFile3");
	});
	
	
	//查看按钮
	$("#btnWatch_YA").click(function(){
		//取得选中的数据
		var arr = $('#datagrid_YA').datagrid("getSelections");
		 if(arr.length == 1){
			 //把该条信息回显在编辑窗口中
			 $('#watchForm_YA').find('input[name="issue"]').val(arr[0].issue);			
			 billFile1 = arr[0].billFile1;	//缓存文件在数据库的路径
			 if(!billFile1){
				 $('#billFile1Down').hide();
			 }
			 
			 billFile2 = arr[0].billFile2;
			 if(!billFile2){					//如果为空，就是没有文件，那么下载按钮隐藏
				 $('#billFile2Down').hide();
			 }
			 
			 billFile3 = arr[0].billFile3;
			 if(!billFile3){
				 $('#billFile3Down').hide();
			 }
			 		 		 
			 $('#watchForm_YA').find('input[name="resolutionresult"]').val(arr[0].resolutionresult);
			 $('#watchForm_YA').find('input[name="agreeCount"]').val(arr[0].agreeCount);
			 $('#watchForm_YA').find('input[name="opposeCount"]').val(arr[0].opposeCount);
			 $('#watchForm_YA').find('input[name="giveUpCount"]').val(arr[0].giveUpCount);
			 $('#watchForm_YA').find('input[name="managerIdea"]').val(arr[0].managerIdea);
			 $('#watchForm_YA').find('input[name="comment"]').val(arr[0].comment);
			 //然后打开窗口			 			 
			 $('#watchWin_YA').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要查看的数据');
		 }
	});
	
	$('#billFile1Down').click(function(){
		location.href = path + "/monitBill/downloadFile?filePath="+billFile1;
	});
	
	$('#billFile2Down').click(function(){
		location.href = path + "/monitBill/downloadFile?filePath="+billFile2;
	});
	
	$('#billFile3Down').click(function(){
		location.href = path + "/monitBill/downloadFile?filePath="+billFile3;
	});
	
	
});

//删除文件的方法
function deleteFile(filePath,field){
	var params = {};
	params.monitId = monitid;
	params.filePath = filePath;
	params.field = field;
	$.ajax({
		type: 'post',
		url: path + '/monitBill/deleteFile',
		data: params,
		success: function(data){
					if(data.result == true){
						$.messager.alert('提醒',data.msg);
						$('#editWin_YA').dialog('close');
						$('#datagrid_YA').datagrid('load',{monitNum:copyMonitNum});
						$('#editWin_YA').form('clear');
					}else{
						$.messager.alert('提醒',data.msg);
					}
    			}
	});		
}