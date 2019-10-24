$(function(){
	var ruleTable = {
			url : ctx + '/mgr/scoreRule/list.json',
			columns : [ [ {
				field : 'ruleId',
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
			},{
				field : 'type',
				title : '类型',
				align : 'center',
				width : 150
			}, {
				field : 'score',
				title : '分数',
				align : 'center',
				width : 150
			} 
			] ],
			fitColumns : true,// 自适应水平，防止出现水平滚动条
			singleSelect : true,
			toolbar : "#tb_Info" //功能按钮
		};
	$('#datagridOfHistory').datagrid(ruleTable);
	$('#btnEdit').click(function(){
		var arr=$("#datagridOfHistory").datagrid("getSelections");
		if(arr.length==1){
			$('#editWin').find('input[name="ruleId"]').val(arr[0].ruleId);
			$('#editWin').find('input[name="score"]').val(arr[0].score);
			$('#editWin').find('input[name="type"]').val(arr[0].type);
			$("#editWin").dialog("open");
		}else if(arr.length==0){
			$.messager.alert("提醒","请选择数据");
		}else{
			$.messager.alert("提醒","只能选择一行数据进行编辑");
		}
		
	});
	//更新提交
	$("#btnToUpdate").click(function(){
		if($("#editForm").form('validate')){
			$.ajax({
				type: 'post',
				url: ctx + "/mgr/scoreRule/update.json",
				data: $("#editForm").serialize(),
				success: function(data){
							if(data.result == true){
								$.messager.alert('提醒',data.msg);
								$('#editWin').dialog('close');
								$('#datagridOfHistory').datagrid(ruleTable);
								$('#editWin').form('clear');
							}else{
								$.messager.alert('提醒',data.msg);
							}
            			}
			});
		}
	});
});