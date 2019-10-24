/**
 * 加载列表数据
 */
$(document).ready(function() {
	// 初始化表格
	var table = {
		idField:'resourceId',    
		treeField:'resourceName',	
		url : ctx + '/mgr/resource/list.json',
		columns : [ [ {
			field : 'resourceId',
			hidden : true
		}, {
			field : 'resourcePid',
			hidden : true
		}, {
			field : 'resourceName',
			title : '资源名称',
			align : 'left',
			width : 150,
			formatter:function(value,rowData,rowIndex){
						return '<input type="checkbox" id="'+ rowData.resourceId +'"/>' + value;
					  }
		}, {
			field : 'resourceType',
			title : '资源类型',
			align : 'center',
			width : 100,
			formatter : function(value,row,index){
							if(value == '1')
								return '功能菜单';
							else if(value == '2')
								return '操作菜单';
							else if(value == '3')
								return '操作按钮';
							else{
								return '';
							}
						}
		}, {
			field : 'resourceInd',
			title : '资源标识',
			align : 'center',
			width : 100
		}, {
			field : 'resourceUrl',
			title : '资源地址',
			align : 'left',
			width : 200
		} ] ],
		fitColumns : true,// 自适应水平，防止出现水平滚动条
		queryParams : $("#schForm").formToJson(),
		toolbar : "#tb"
	};
	
	//初始化页面时直接加载表格数据
	initSelector();
	$('#toGridContainer').treegrid(table);
	
	/*******************以下是按钮事件**************************/
	//搜索按钮
	$(".scbtn").click(function(){
		$('#toGridContainer').treegrid('load',$("#schForm").formToJson());
	});
	
	//新增按钮(弹出新增窗口)
	$("#btnAdd").click(function(){
		if($("input:checked").length > 1){
			$.messager.alert('提醒','最多只能选一个作为父结点添加');
			return;
		} else {
			initAddForm();
			if($("input:checked").length == 1){
				var pId = $("input:checked:first").attr("id");
				var selectData = $('#toGridContainer').treegrid('find',pId);
				$('#addForm').find('input[name="resourcePid"]').val(pId);
				$('#addForm').find('input[name="resourcePname"]').val(selectData.resourceName);
				if(selectData.resourceType == '3'){
					$.messager.alert('提醒','操作按钮下不能添加资源');
					return;
				}else if(selectData.resourceType == '2'){
					$("#resourceType_A option[value='1']").remove();
					$("#resourceType_A option[value='2']").remove();
				}else{
					//支持二级菜单布局，此处先 注释不用，改为三级菜单布局。				
					$("#resourceType_A option[value='3']").remove();
					$('#addForm').find('ul li:eq(2)').hide();
					if($('#toGridContainer').treegrid('getParent',pId)){//如果已经在第二级菜单了
						$("#resourceType_A option[value='1']").remove();
					}else{
						$("#resourceType_A option[value='2']").remove();
						$('#addForm').find('ul li:eq(4)').hide();
					}
				}
			}else{
				$('#addForm').find('input[name="resourcePid"]').val(0);
				$('#addForm').find('input[name="resourcePname"]').val('(无)');
				$('#addForm').find('ul li:eq(2)').hide();
				$('#addForm').find('ul li:eq(4)').hide();
				$("#resourceType_A option[value='2']").remove();
				$("#resourceType_A option[value='3']").remove();
			}
		}
		
		$('#addWin').dialog('open');
	});
	
	//新增页面中select改变事件
	$('#resourceType_A').change(function(){
		if($(this).children('option:selected').val() == 1){
			$('#addForm').find('ul li:eq(4)').hide();
		}else if($(this).children('option:selected').val() == 2){
			$('#addForm').find('ul li:eq(4)').show();
		}
	});
	
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: ctx + "/mgr/resource/add.json",
				data: $("#addForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#addWin').dialog('close');
								initSelector();
								$('#toGridContainer').treegrid(table);
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
		 if($("input:checked").length > 0){
			 //先选中
			 $("input:checked").each(function(){
				 var pId = $(this).attr("id");
				 var children = $('#toGridContainer').treegrid('getChildren',pId);
				 for(var a in children)
					 $('#' + children[a].resourceId).attr("checked","checked");
			 });
			 
			 $.messager.confirm('确认','您确认想要删除选中的记录吗？<br><br>注意：删除结点的同时也会删除子结点！',function(r){    
				 if (r){ 
					 var i = 0;
					 var resourceIdArr = 'resourceIdArr=';
					 $("input:checked").each(function(){
						 resourceIdArr += (i == 0 ? '' : '#') + $(this).attr("id");
						 i++;
					 });
					 
					 $.ajax({
							type: 'post',
							url: ctx + "/mgr/resource/delete.json",
							data: resourceIdArr,
							success: function(data){
										if(data.result == true){
											$.messager.alert('提醒',data.msg);
											initSelector();
											$('#toGridContainer').treegrid(table);
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
		 if($("input:checked").length == 1){
			initEditForm();
			var pId = $("input:checked:first").attr("id");
			var selectData = $('#toGridContainer').treegrid('find',pId);
			$('#editForm').find('input[name="resourceId"]').val(selectData.resourceId);
			$('#editForm').find('input[name="resourcePid"]').val(selectData.resourcePid);
			$('#editForm').find('input[name="resourceInd"]').val(selectData.resourceInd);
			$('#editForm').find('input[name="resourceName"]').val(selectData.resourceName);
			$('#editForm').find('input[name="resourceIndex"]').val(selectData.resourceIndex);
			$('#editForm').find('input[name="resourceUrl"]').val(selectData.resourceUrl);
			if(selectData.resourceType == '1'){
				$("#resourceType_E").append('<option value="1">功能菜单</option>');
				$('#editForm').find('ul li:eq(1)').hide();
				$('#editForm').find('ul li:eq(3)').hide();
			}else if(selectData.resourceType == '2'){
				$('#editForm').find('ul li:eq(1)').hide();
				$("#resourceType_E").append('<option value="2">操作菜单</option>');
			}else{
				$("#resourceType_E").append('<option value="3">操作按钮</option>');
			}
			
			$('#editWin').dialog('open');
		 }else if($("input:checked").length > 1){
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
				url: ctx + "/mgr/resource/update.json",
				data: $("#editForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWin').dialog('close');
								initSelector();
								$('#toGridContainer').treegrid(table);
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
			 if(arr[0].resourceType == '3'){
				 $.messager.alert('提醒','操作按钮不能调整排序');
			 }else{
				 $.ajax({
						type: 'post',
						url: ctx + "/mgr/resource/top.json",
						data: 'resourceId=' + arr[0].resourceId,
						success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#toGridContainer').treegrid(table);
									}else{
										$.messager.alert('提醒',data.msg);
									}
		            			}
					});
			 }
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
			 if(arr[0].resourceType == '3'){
				 $.messager.alert('提醒','操作按钮不能调整排序');
			 }else{
				 $.ajax({
						type: 'post',
						url: ctx + "/mgr/resource/up.json",
						data: 'resourceId=' + arr[0].resourceId,
						success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#toGridContainer').treegrid(table);
									}else{
										$.messager.alert('提醒',data.msg);
									}
		            			}
					});
			 }
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
			 if(arr[0].resourceType == '3'){
				 $.messager.alert('提醒','操作按钮不能调整排序');
			 }else{
				 $.ajax({
						type: 'post',
						url: ctx + "/mgr/resource/down.json",
						data: 'resourceId=' + arr[0].resourceId,
						success: function(data){
									if(data.result == true){
										$.messager.alert('提醒',data.msg);
										$('#toGridContainer').treegrid(table);
									}else{
										$.messager.alert('提醒',data.msg);
									}
		            			}
					});
			 }
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
			 if(arr[0].resourceType == '3'){
				 $.messager.alert('提醒','操作按钮不能调整排序');
			 }else{
				 if(arr[0].resourceType == '3'){
					 $.messager.alert('提醒','操作按钮不能调整排序');
				 }else{
					 $.ajax({
							type: 'post',
							url: ctx + "/mgr/resource/low.json",
							data: 'resourceId=' + arr[0].resourceId,
							success: function(data){
										if(data.result == true){
											$.messager.alert('提醒',data.msg);
											$('#toGridContainer').treegrid(table);
										}else{
											$.messager.alert('提醒',data.msg);
										}
			            			}
						});
				 }
			 }
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要调整排序的数据');
		 }
	});
});

//初始化下拉栏
function initSelector(){
	$('.select_style').empty();
	$('.select_style').append('<option value="0">全部</option>');
	$.get(ctx + '/mgr/resource/0/select.json', function(result){
		if(result.data.length > 0){
			for(var i in result.data){
				$('.select_style').append('<option value="'+ result.data[i].resourceId +'">'+ result.data[i].resourceName +'</option>');
			}
		}
	});
}


//将增加窗口的中的组件重新显示
function initAddForm(){
	$('#addForm').find('ul li:eq(2)').show();
	$('#addForm').find('ul li:eq(4)').show();
	
	$("#resourceType_A").empty();//先清空，再添加
	$("#resourceType_A").append('<option value="1">功能菜单</option>');
	$("#resourceType_A").append('<option value="2">操作菜单</option>');
	$("#resourceType_A").append('<option value="3">操作按钮</option>');
	
	$('#addForm').form('clear');
}

//将编辑窗口的中的组件重新显示
function initEditForm(){
	$('#editForm').find('ul li:eq(1)').show();
	$('#editForm').find('ul li:eq(3)').show();
	
	$("#resourceType_E").empty();//先清空，再添加
	
	$('#editForm').form('clear');
}