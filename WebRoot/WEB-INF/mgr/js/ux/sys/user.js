/**
 * 加载列表数据
 */
$(document).ready(function() {
	// 初始化表格
	var table = {
		url : ctx + '/mgr/user/list.json',
		columns : [ [ {
			field : 'userId',
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
			field : 'userAct',
			title : '账号',
			align : 'center',
			width : 100
		}, {
			field : 'userName',
			title : '用户名',
			align : 'center',
			width : 100
		}, {
			field : 'userPhone',
			title : '手机号码',
			align : 'center',
			width : 100
		}, {
			field : 'userMail',
			title : '邮箱',
			align : 'center',
			width : 100
		}, {
			field : 'departId',
			hidden:true
		}, {
			field : 'position',
			title : '岗位',
			align : 'center',
			width : 80,
			formatter : function(value, row, index) {
				var name;
				if (value != null) {
					var param = {};
					param.id = value;
					$.ajax({
						type : 'post',
						url : path + "/dictionary/load",
						async : false,
						data : param,
						success : function(data) {
							name = data.dicName;
						}
					});
				}
				return name;
			}
		}, {
			field : 'actTime',
			title : '帐户有效期',
			align : 'center',
			formatter : ExtendFormatter.DateFormatter,
			width : 100
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
	
	var data = new Array();
	$.ajax({
		type: 'post',
		url: ctx + '/mgr/meetingStatistics/queryListCompany.json',
		success: function(result){
			var length = result.length;
			for(var i = 0; i < length; i++){
				data.push({ "text":result[i].companyName, "id":result[i].companyId });
			}
			$("#combo1").combobox("loadData", data);
			$("#combo2").combobox("loadData", data);
			
		}
	});
	
	/*******************以下是按钮事件**************************/
	//搜索按钮
	$(".scbtn").click(function(){
		$('#toGridContainer').datagrid('load',$("#schForm").formToJson());
	});
	
	
	
	//新增按钮(弹出新增窗口)
	$("#btnAdd").click(function(){
		var dicNum = document.getElementById("dicNum").value;
		if(dicNum == "") {
			alert("请选择部门，才能进行新增操作");
			return;
		}
//		$('#addWin').show();
		$('#addWin').dialog('open');
			
	});
	//新增提交按钮(校验数据并提交表单)
	$("#btnToAdd").click(function(){
		if($("#addForm").form('validate')){
			$.ajax({
				type: 'post',
				url: ctx + "/mgr/user/add.json",
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
					 var userIdArr = 'userIdArr=';
					 for(var i in arr){
						 userIdArr += (i == 0 ? '' : '#') + arr[i].userId;
					 }
					 
					 $.ajax({
							type: 'post',
							url: ctx + "/mgr/user/delete.json",
							data: userIdArr,
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
			 $('#editForm').find('input[name="userId"]').val(arr[0].userId);
			 $('#editForm').find('input[name="userAct"]').val(arr[0].userAct);
			 $('#editForm').find('input[name="userPwd"]').val(arr[0].userPwd);
			 $('#editForm').find('input[name="userName"]').val(arr[0].userName);
			 $('#editForm').find('input[name="userPhone"]').val(arr[0].userPhone);
			 $('#editForm').find('input[name="userMail"]').val(arr[0].userMail);
			 $('#editForm').find('#actTime').datebox('setValue',ExtendFormatter.DateFormatter(arr[0].actTime));
			 $('#dicNum2').val(arr[0].departId);
			 $('#position2').combobox('setValue',arr[0].position);
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
				url: ctx + "/mgr/user/update.json",
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
	
	//授权按钮
	$("#btnGrant").click(function(){
		var arr = $('#toGridContainer').datagrid("getSelections");
		if(arr.length == 1){
			$('#unselect').empty();
			$('#select').empty();
			
			$.get(ctx + '/mgr/user/'+ arr[0].userId +'/select.json', function(result){
				if(result.data[0].length > 0){
					for(var i in result.data[0]){
						$('#unselect').append('<option value="'+ result.data[0][i].roleId +'">'+ result.data[0][i].roleName +'</option>');
					}
				}
				
				if(result.data[1].length > 0){
					for(var i in result.data[1]){
						$('#select').append('<option value="'+ result.data[1][i].roleId +'">'+ result.data[1][i].roleName +'</option>');
					}
				}
			});
			
			$('#grantForm').find('input[name="userId"]').val(arr[0].userId);
			$('#grantWin').dialog('open');
		}else if(arr.length > 1){
			$.messager.alert('提醒','请选择单条个用户进行授权');
		}else{
			$.messager.alert('提醒','请选择要授权的用户');
		}
	});
	
	//添加角色
	$(".tbbtn:first").click(function(){
		$('#select').append($("#unselect").find("option:selected"));
		$("#unselect").find("option:selected").remove();
	});
	
	//删除角色
	$(".tbbtn:last").click(function(){
		$('#unselect').append($("#select").find("option:selected"));
		$("#select").find("option:selected").remove();
	});
	
	//提交后台添加角色
	$("#btnToGrant").click(function(){
		var j = 0;
		var select = 'select=';
		var userId = 'userId=' + $('#grantForm').find('input[name="userId"]').val();
		
		$("#select option").each(function(){
			select += (j == 0 ? '' : '#') + $(this).val();
			j++;
		});
		
		$.ajax({
			type: 'post',
			url: ctx + "/mgr/user/grant.json",
			data: userId + '&' + select,
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
	
	$('#deptNum1').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=020a'
	});
	
	$('#deptNum2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=020a'
	});
	
	$('#position1').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=020b'
	});
	
	$('#position2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=020b'
	});
});



function return_main() {
	$('#addWin').hide();
	$('#editWin').hide();
	$('#watchWin').hide();
	$('#watchWin2').hide();
	$('#tree').hide();
	$('#class_required').show();
}
