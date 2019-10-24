var copyMonitNum;
var companyId;
var paramComp = {}
var loadSign = false;

$(function(){
	// 初始化表格
	var table = {
		url : path + '/monitInfo/list.json',
		columns : [ [ {
			field : 'monitid',
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
			field : 'name',
			title : '监事姓名',
			align : 'center',
			width : 40			
		}, {
			field : 'sex',
			title : '性别',
			align : 'center',
			width : 30			
		}, {
			field : 'phone',
			title : '手机号码',
			align : 'center',
			width : 100
		}, {
			field : 'address',
			title : '住址',
			align : 'center',
			width : 30			
		}, {
			field : 'isJoinBoard',
			title : '是否加入监事会',
			align : 'center',
			width : 50,
			formatter : function(value, row, index) {
				if(value == 0){
					return "否";
				}else if(value == 1){
					return "是";
				}				
			}
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		singleSelect: true,
		queryParams : paramComp,
		onClickRow:function(rowIndex,rowData){
			copyMonitNum = rowData.monitNum;	//存储当前组织机构代码			
			if(!loadSign){
				$('#datagridOfJob').datagrid(table_Job);
				$('#tb_Job').show();
				loadSign = true;
			}
			$('#datagridOfJob').datagrid('load',{monitNum:copyMonitNum});
		},
		toolbar : "#tb"		//功能按钮
	};
				
	//新增按钮
	$("#btnAdd").click(function(){
		if(companyId){
			$('#addForm').find('input[name="companyId"]').val(companyId);
			$('#addWin').dialog('open');	//打开对话框			
		}else{
			$.messager.alert('提醒',"请在左侧菜单中选择关联的公司");
		}
	});
	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/monitInfo/add.json',
				data: $("#addForm").serialize(),	//序列化一组表单元素，将表单内容编码为用于提交的字符串
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWin').dialog('close');
								$('#datagrid').datagrid('load',{companyId:companyId});
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
			 $('#editForm').find('input[name="monitid"]').val(arr[0].monitid);
			 $('#editForm').find('input[name="monitNum"]').val(arr[0].monitNum);
			 $('#editForm').find('input[name="isJoinBoard"]').val(arr[0].isJoinBoard);
			 $('#editForm').find('input[name="companyId"]').val(arr[0].companyId);
			 $('#editForm').find('input[name="name"]').val(arr[0].name);
			 if(arr[0].sex == "是"){
				 $('#editForm').find("input[type=radio][name=isChairman][value=是]").attr("checked",'checked');
			 }else{
				 $('#editForm').find("input[type=radio][name=isChairman][value=否]").attr("checked",'checked');
			 }
			 if(arr[0].sex == "男"){
				 $('#editForm').find("input[type=radio][name=sex][value=男]").attr("checked",'checked');
			 }else{
				 $('#editForm').find("input[type=radio][name=sex][value=女]").attr("checked",'checked');
			 }
			 $('#editForm').find('input[name="phone"]').val(arr[0].phone);
			 $('#editForm').find('input[name="officePhone"]').val(arr[0].officePhone);
			 $('#editForm').find('input[name="emaile"]').val(arr[0].emaile);
			 $('#editForm').find('input[name="address"]').val(arr[0].address);
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
				url: path + '/monitInfo/update.json',
				data: $("#editForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWin').dialog('close');
								$('#datagrid').datagrid('load',{companyId:companyId});
								$('#editWin').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//加入董事会
	$("#btnJiaRu").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		 if(arr.length == 1){
			 var param = {};
			 param.monitid = arr[0].monitid;
			 
				$.ajax({
					type: 'post',
					url: path + '/monitInfo/jiaRu.json',
					data: param,
					success: function(data){
								if(data.result == true){
									$.messager.alert('提醒',data.msg);								
									$('#datagrid').datagrid('load',{companyId:companyId});							
								}else{
									$.messager.alert('提醒',data.msg);
								}
	            			}
				});
			}else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	//退出董事会
	$("#btnTuiChu").click(function(){
		//取得选中的数据
		var arr = $('#datagrid').datagrid("getSelections");
		 if(arr.length == 1){
			 var param = {};
			 param.monitid = arr[0].monitid;
			 
				$.ajax({
					type: 'post',
					url: path + '/monitInfo/tuiChu.json',
					data: param,
					success: function(data){
								if(data.result == true){
									$.messager.alert('提醒',data.msg);								
									$('#datagrid').datagrid('load',{companyId:companyId});							
								}else{
									$.messager.alert('提醒',data.msg);
								}
	            			}
				});
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
					 var monitInfoIdArr = 'monitInfoIdArr=';
					 for(var i in arr){
						 monitInfoIdArr += (i == 0 ? '' : '#') + arr[i].monitid;
					 }					 
					 $.ajax({
							type: 'post',
							url: path + '/monitInfo/delete.json',
							data: monitInfoIdArr,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagrid').datagrid('load',{companyId:companyId});
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
	var table_Job = {
		url : path + '/monitJob/list.json',
		columns : [ [ {
			field : 'monitid',
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
			title : '单位名称',
			align : 'center',
			width : 40			
		},  {
			field : 'startTime',
			title : '开始时间',
			align : 'center',
			width : 70		
		}, {
			field : 'endTime',
			title : '结束时间',
			align : 'center',
			width : 70			
		}, {
			field : 'status',
			title : '状态',
			align : 'center',
			width : 30		
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		toolbar : "#tb_Job"		//功能按钮
	};
		
	//新增按钮
	$("#btnAdd_Job").click(function(){
		var arr = $('#datagrid').datagrid("getSelections");
		if(arr.length == 1){
			$('#addForm_Job').find('input[name="monitNum"]').val(arr[0].monitNum);			
			$('#addWin_Job').dialog('open');	//打开对话框
		}else if(arr.length > 1){
			$.messager.alert('提醒','请选择单条监事信息');
		}else{
			$.messager.alert('提醒','请选择关联的监事信息');
		}	
	});
	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd_Job").click(function(){
		if($("#addForm_Job").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/monitJob/add.json',
				data: $("#addForm_Job").serialize(),	//序列化一组表单元素，将表单内容编码为用于提交的字符串
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWin_Job').dialog('close');
								$('#datagridOfJob').datagrid('load',{managerNum:copyManagerNum});
								$('#addForm_Job').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//编辑按钮
	$("#btnEdit_Job").click(function(){
		//取得选中的数据
		var arr = $('#datagridOfJob').datagrid("getSelections");
		 if(arr.length == 1){
			 //把该条信息回显在编辑窗口中
			 $('#editForm_Job').find('input[name="monitid"]').val(arr[0].monitid);
			 $('#editForm_Job').find('input[name="monitNum"]').val(arr[0].monitNum);
			 $('#editForm_Job').find('input[name="companyName"]').val(arr[0].companyName);
			 $('#editForm_Job').find('input[name="status"]').val(arr[0].status);			 
			 $('#startTT').datebox("setValue", arr[0].startTime);		//日期框赋值必须要用ID
			 $('#endTT').datebox("setValue", arr[0].endTime);
			 //然后打开窗口			 			 
			 $('#editWin_Job').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要编辑的数据');
		 }
	});
	
	//更新提交
	$("#btnToUpdate_Job").click(function(){
		if($("#editForm_Job").form('validate')){
			$.ajax({
				type: 'post',
				url: path + '/monitJob/update.json',
				data: $("#editForm_Job").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWin_Job').dialog('close');
								$('#datagridOfJob').datagrid('load',{monitNum:copyMonitNum});
								$('#editWin_Job').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//删除按钮
	$("#btnDelete_Job").click(function(){
		var arr = $('#datagridOfJob').datagrid("getSelections");
		 if(arr.length > 0){
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？',function(r){    
				 if (r){ 
					 var jobIdArr = 'jobIdArr=';
					 for(var i in arr){
						 jobIdArr += (i == 0 ? '' : '#') + arr[i].monitid;
					 }					 
					 $.ajax({
							type: 'post',
							url: path + '/monitJob/delete.json',
							data: jobIdArr,
							success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#datagridOfJob').datagrid('load',{monitNum:copyMonitNum});
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
	
	
	$('#depttree').tree({
		url : path + "/deptInfo/list.json",
		lines : true,
		state : 'closed', 
		onClick : function(node){
			if(node.id.substr(0,1) == "c"){		//是公司
				companyId = node.id.substr(1,node.length);	//拿到公司的主键
				paramComp.companyId = companyId;
				$('#datagrid').datagrid(table);
				$('#tb').show();
			}
		},
		onBeforeExpand : function(node){	//节点展开前触发，返回false则不展开节点
			return false;
		},
		onLoadSuccess: function (node, data) {	//成功加载后，关闭所有节点
			$('#depttree').tree('collapseAll')
		}
	});
	
});