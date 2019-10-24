

	$(function(){
		var stDocInfoTable = {
				url : ctx + '/mgr/user/listByName.json',
				columns : [ [ {
					field : 'rowIndex',
					title : '序号',
					width : 20,
					align : 'center',
					formatter : function(value, row, index) {
						return index + 1;
					}
				}, {
					field : 'userName',
					title : '部门成员名称',
					align : 'center',
					width : 40
				}, {
					field : 'userPhone',
					title : '手机号码',
					align : 'center',
					width : 40
				},{
					field : 'userMail',
					title : '邮箱',
					align : 'center',
					width : 40
				}
				] ],
				pagination : true, // 显示分页
				pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
				pageSize : 10, // 默认显示记录数
				fitColumns : true, // 自适应水平，防止出现水平滚动条
				toolbar : "#tb_stDocInfo" //功能按钮
			};

			//初始化页面时直接加载表格数据，数据列表的ID
			$('#datagridOfstDocInfo').datagrid(stDocInfoTable);
		
		
		
	});









