/**
 * 加载列表数据
 */
$(document).ready(function() {
	// 初始化表格
	var table = {
		url : ctx + '/mgr/role/list.json',
		columns : [ [ {
			field : 'roleId',
			width : 20,
			align : 'left',
			checkbox : true
		}, {
			field : 'rowIndex',
			title : '序号',
			width : 20,
			align : 'center',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : 'roleCode',
			title : '角色编码',
			align : 'center',
			width : 100
		}, {
			field : 'roleName',
			title : '角色名称',
			align : 'center',
			width : 100
		}, {
			field : 'roleDesc',
			title : '角色描述',
			align : 'left',
			width : 500
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
				url: ctx + "/mgr/role/add.json",
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
					 var roleIdArr = 'roleIdArr=';
					 for(var i in arr){
						 roleIdArr += (i == 0 ? '' : '#') + arr[i].roleId;
					 }
					 
					 $.ajax({
							type: 'post',
							url: ctx + "/mgr/role/delete.json",
							data: roleIdArr,
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
			 $('#editForm').find('input[name="roleId"]').val(arr[0].roleId);
			 $('#editForm').find('input[name="roleCode"]').val(arr[0].roleCode);
			 $('#editForm').find('input[name="roleName"]').val(arr[0].roleName);
			 $('#editForm').find('textarea[name="roleDesc"]').val(arr[0].roleDesc);
			 
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
				url: ctx + "/mgr/role/update.json",
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
	
	//打开角色资源配置窗口
	$("#btnResource").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		 if(arr.length == 1){
			 $('#resourceForm').find('input[name="roleId"]').val(arr[0].roleId);
			 $('#toTreeContainer').tree({
				 checkbox:true,
				 url:ctx + '/mgr/role/'+ arr[0].roleId +'/listResource.json'
			 });
			 
			 
			 $('#resourceWin').dialog('open');
		 }else if(arr.length > 1){
			 $.messager.alert('提醒','请选择单条数据记录');
		 }else{
			 $.messager.alert('提醒','请选择要配置的角色');
		 }
	});
	
	//保存角色资源配置
	$("#btnToResource").click(function(){
		var roleId = 'roleId=' + $('#resourceForm').find('input[name="roleId"]').val();
		var checkIdArr = 'checkIdArr=';
		
		var j = 0;
		var nodes = $('#toTreeContainer').tree('getChecked');//已选择的结点
		for(var i in nodes){
			if(nodes[i].attributes.resourceType == '4'){
				var optNode = $('#toTreeContainer').tree('getParent',nodes[i].target);
				checkIdArr += (j == 0 ? '' : '#') + + optNode.id;//添加父结点
				j++;
			}else if(nodes[i].attributes.resourceType == '3'){
				checkIdArr += (j == 0 ? '' : '#') + nodes[i].id;
				j++;
			}
		}
		
		$.ajax( {
			type : 'post',
			url : ctx + "/mgr/role/updateResource.json",
			data : roleId + '&' + checkIdArr,
			success : function(data) {
				if (data.result == true) {
					$.messager.alert('提醒', data.msg);
					$('#resourceWin').dialog('close');
					$('#toGridContainer').datagrid(table);
				} else {
					$.messager.alert('提醒', data.msg);
				}
			}
		});
	});
	
	
	//授权按钮
	$("#btnGrant").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		if(arr.length == 1){
			$('#unselect').empty();
			$('#select').empty();
			
			$.get(ctx + '/mgr/role/'+ arr[0].roleId +'/selectuser.json', function(result){
				if(result.data[0].length > 0){
					for(var i in result.data[0]){
						$('#unselect').append('<option value="'+ result.data[0][i].userId +'">'+ result.data[0][i].userName +'</option>');
					}
				}
				
				if(result.data[1].length > 0){
					for(var i in result.data[1]){
						$('#select').append('<option value="'+ result.data[1][i].userId +'">'+ result.data[1][i].userName +'</option>');
					}
				}
			});
			
			$('#grantForm').find('input[name="roleId"]').val(arr[0].roleId);
			$('#grantWin').dialog('open');
		}else if(arr.length > 1){
			$.messager.alert('提醒','请选择单条个用户进行授权');
		}else{
			$.messager.alert('提醒','请选择要授权的用户');
		}
	});
	
	
	//提交后台添加用户
	$("#btnToGrant").click(function(){
		var j = 0;
		var select = 'select=';
		var roleId = 'roleId=' + $('#grantForm').find('input[name="roleId"]').val();
		
		$("#select option").each(function(){
			select += (j == 0 ? '' : '#') + $(this).val();
			j++;
		});
		
		$.ajax({
			type: 'post',
			url: ctx + "/mgr/role/grant.json",
			data: roleId + '&' + select,
			success: function(data){
						if(data.result == true){
							$.messager.alert('提醒',data.msg);
							$('#grantWin').dialog('close');
							$('#toGridContainer').datagrid(table);
						}else{
							$.messager.alert('提醒',data.msg);
						}
        			}
		});
	});
	
	//查询用户
	$("#btnGrantSearch").click(function(){
		var j = 0;
		var select = 'select=';
		var condition = 'condition=' + $('#grantForm').find('input[name="grantSearch"]').val();
		
		$("#select option").each(function(){
			select += (j == 0 ? '' : '#') + $(this).val();
			j++;
		});
		
		$.ajax({
			type: 'post',
			url: ctx + "/mgr/role/grantSearch.json",
			data: condition + '&' + select,
			success: function(data){
				$('#unselect').empty();
				if(data.data[0].length > 0){
					for(var i in data.data[0]){
						$('#unselect').append('<option value="'+ data.data[0][i].userId +'">'+ data.data[0][i].userName +'</option>');
					}
				}
        	}
		});
	});
	
	//添加用户
	$(".tbbtn:first").click(function(){
		$('#select').append($("#unselect").find("option:selected"));
		$("#unselect").find("option:selected").remove();
	});
	
	//删除用户
	$(".tbbtn:last").click(function(){
		$('#unselect').append($("#select").find("option:selected"));
		$("#select").find("option:selected").remove();
	});
	
});
