/**
 * 加载列表数据
 */
$(document).ready(function() {
	// 初始化表格
	var table = {
		url : ctx + '/mgr/dictDef/list.json',
		columns : [ [ {
			field : 'dictId',
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
			field : 'dictCode',
			title : '字典编码',
			align : 'center',
			width : 100
		}, {
			field : 'dictName',
			title : '字典名称',
			align : 'center',
			width : 100
		}, {
			field : 'dictDesc',
			title : '字典描述',
			align : 'left',
			width : 300
		} ] ],
		pagination : true,// 显示分页
		pageList : [ 10, 20, 50 ],// 单页可以显示的记录数
		pageSize : 10,// 默认显示记录数
		fitColumns : true,// 自适应水平，防止出现水平滚动条
		queryParams : $("#schForm").formToJson(),
		toolbar : "#tb"
	};
	
	//初始化页面时直接加载表格数据
	$('#toGridContainer').datagrid(table);
	
	/*******************以下是按钮事件**************************/
	//搜索按钮
	$(".scbtn").click(function(){
		$('#toGridContainer').datagrid('load',$("#schForm").formToJson());
	});
	
	//新增按钮(弹出新增窗口)
	$("#btnAdd").click(function(){
		$('#addForm').form('clear');
		$('#addWin').dialog('open');
	});
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: ctx + "/mgr/dictDef/add.json",
				data: $("#addForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWin').dialog('close');
								$('#toGridContainer').datagrid(table);
								$('#addForm').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//删除按钮
	$("#btnRemove").click(function(){
		 var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length > 0){
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？',function(r){    
				 if (r){ 
					 var dictIdArr = 'dictIdArr=';
					 for(var i in arr){
						 dictIdArr += (i == 0 ? '' : '#') + arr[i].dictId;
					 }
					 
					 $.ajax({
							type: 'post',
							url: ctx + "/mgr/dictDef/delete.json",
							data: dictIdArr,
							success: function(data){
										if(data.result == true){
											$.messager.alert('提醒',data.msg);
											$('#toGridContainer').datagrid(table);
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
	
	//编辑按钮(弹出编辑窗口)
	$("#btnEdit").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length == 1){
			 $('#editForm').find('input[name="dictId"]').val(arr[0].dictId);
			 $('#editForm').find('input[name="dictCode"]').val(arr[0].dictCode);
			 $('#editForm').find('input[name="dictName"]').val(arr[0].dictName);
			 $('#editForm').find('textarea[name="dictDesc"]').val(arr[0].dictDesc);
			 
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
				url: ctx + "/mgr/dictDef/update.json",
				data: $("#editForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWin').dialog('close');
								$('#toGridContainer').datagrid(table);
								$('#editWin').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
	
	//维护数据字典项目
	$("#btnItem").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		
		 if(arr.length == 1){
			 window.location.href=ctx + "/mgr/dictItem/list/" + arr[0].dictCode;
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要维护的数据');
		 }
	});
	
});
