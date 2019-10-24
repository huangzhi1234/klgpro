/**
 * 加载列表数据
 */
$(document).ready(function() {
	// 初始化表格
	var qryData = '{"dictCode": "'+ dictCode +'"}'; 
	var table = {
		url : ctx + '/mgr/dictItem/list.json',
		columns : [ [ {
			field : 'itemId',
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
			field : 'itemCode',
			title : '项目编码',
			align : 'center',
			width : 100
		}, {
			field : 'itemName',
			title : '项目名称',
			align : 'center',
			width : 100
		}, {
			field : 'itemDesc',
			title : '项目描述',
			align : 'left',
			width : 300
		} ] ],
		pagination : true,// 显示分页
		pageList : [ 10, 20, 50 ],// 单页可以显示的记录数
		pageSize : 10,// 默认显示记录数
		fitColumns : true,// 自适应水平，防止出现水平滚动条
		queryParams : eval('(' + qryData + ')'),
		toolbar : "#tb"
	};
	
	//初始化页面时直接加载表格数据
	$('#toGridContainer').datagrid(table);
	
	/*******************以下是按钮事件**************************/
	//返回按钮(返回字典维护页面)
	$(".scbtn").click(function(){
		 history.go(-1);
	});
	
	//新增按钮(弹出新增窗口)
	$("#btnAdd").click(function(){
		$('#addForm').form('clear');
		$('#addForm').find('input[name="dictCode"]').val(dictCode);
		$('#addWin').dialog('open');
	});
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: ctx + "/mgr/dictItem/add.json",
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
					 var itemIdArr = 'itemIdArr=';
					 for(var i in arr){
						 itemIdArr += (i == 0 ? '' : '#') + arr[i].itemId;
					 }
					 
					 $.ajax({
							type: 'post',
							url: ctx + "/mgr/dictItem/delete.json",
							data: itemIdArr,
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
			 $('#editForm').find('input[name="itemId"]').val(arr[0].itemId);
			 $('#editForm').find('input[name="dictCode"]').val(arr[0].dictCode);
			 $('#editForm').find('input[name="itemCode"]').val(arr[0].itemCode);
			 $('#editForm').find('input[name="itemName"]').val(arr[0].itemName);
			 $('#editForm').find('textarea[name="itemDesc"]').val(arr[0].itemDesc);
			 
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
				url: ctx + "/mgr/dictItem/update.json",
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
	
	//置顶
	$("#btnTop").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length == 1){
			 $.ajax({
					type: 'post',
					url: ctx + "/mgr/dictItem/top.json",
					data: 'itemId=' + arr[0].itemId,
					success: function(data){
								if(data.result == true){
									$.messager.alert('提醒',data.msg);
									$('#toGridContainer').datagrid(table);
								}else{
									$.messager.alert('提醒',data.msg);
								}
	            			}
				});
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要调整排序的数据');
		 }
	});
	
	//向上
	$("#btnUp").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length == 1){
			 $.ajax({
					type: 'post',
					url: ctx + "/mgr/dictItem/up.json",
					data: 'itemId=' + arr[0].itemId,
					success: function(data){
								if(data.result == true){
									$.messager.alert('提醒',data.msg);
									$('#toGridContainer').datagrid(table);
								}else{
									$.messager.alert('提醒',data.msg);
								}
	            			}
				});
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要调整排序的数据');
		 }
	});
	
	//向下
	$("#btnDown").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length == 1){
			 $.ajax({
					type: 'post',
					url: ctx + "/mgr/dictItem/down.json",
					data: 'itemId=' + arr[0].itemId,
					success: function(data){
								if(data.result == true){
									$.messager.alert('提醒',data.msg);
									$('#toGridContainer').datagrid(table);
								}else{
									$.messager.alert('提醒',data.msg);
								}
	            			}
				});
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要调整排序的数据');
		 }
	});
	
	//置底
	$("#btnLow").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length == 1){
			 $.ajax({
					type: 'post',
					url: ctx + "/mgr/dictItem/low.json",
					data: 'itemId=' + arr[0].itemId,
					success: function(data){
								if(data.result == true){
									$.messager.alert('提醒',data.msg);
									$('#toGridContainer').datagrid(table);
								}else{
									$.messager.alert('提醒',data.msg);
								}
	            			}
				});
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要调整排序的数据');
		 }
	});
});
