/**
 * 加载列表数据
 */
$(document).ready(function() {
	// 初始化表格
	var table = {
		url : ctx + '/mgr/cust/list.json',
		columns : [ [ {
			field : 'cust_id',
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
			field : 'cust_name',
			title : '客户名称',
			align : 'center',
			width : 100,
			formatter : function(value,row,index){
				return '<a href="'+ctx+'/mgr/showFamilyMember?cust_id='+row.phone+'" >'+row.cust_name+'</a> ';
			}
		}, {
			field : 'cust_sex',
			title : '性别',
			align : 'center',
			width : 100,
			formatter : function(value,row){
				if(row.cust_sex=="1301"){
					return "男";
				}else {
					return "女";
				}
			}
		}, {
			field : 'phone',
			title : '联系电话',
			align : 'center',
			width : 100
		}
		] ],
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
		$('#_showFamilyMember').dialog('open');
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
				url: ctx + "/mgr/cust/add.json",
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
	


	
	
	
	
	//添加角色
	$(".tbbtn:first").click(function(){
		$('#select').append($("#unselect").find("option:selected"));
		$("#unselect").find("option:selected").remove();
	});
	


});
