$(function(){
	var orderTable = {
			url : ctx + '/mgr/orderScore/list.json',
			columns : [ [ {
				field : 'scoreId',
				hidden : true
			}, {
				field : 'rowIndex',
				title : '序号',
				width : 30,
				align : 'center',
				formatter : function(value, row, index) {
					return index + 1;
				}
			},{
				field : 'userId',
				title : '姓名',
				align : 'center',
				width : 150,
				formatter : function(value, row, index) {
					var name;
					var param={};
					param.userId=value;
					$.ajax({
						type:'post',//提交数据的类型
						url:path+'/orderScore/getUserBean.json',//提交网址
						async:false,//同步提交选项
						data:param,//提交的数据
						success:function(data){//成功返回之后调用的函数
							name=data.userName;
						}
					});
					return name;
				}
			}, {
				field : 'depart',
				title : '所属部门',
				align : 'center',
				width : 250,
				formatter : function(value, row, index) {
					var name;//声明一个异步请求对象
					var param={};
					param.userId=row.userId;
					
					$.ajax({
						type:'post',//提交数据的类型
						url:path+'/proScore/getdepart.json',//提交网址
						async:false,//同步提交选项
						data:param,//提交的数据
						success:function(data){//成功返回之后调用的函数
							name=data.dicName;
						}
					});
					return name;
				}
			}, {
				field : 'score',
				title : '分数',
				align : 'center',
				width : 150
			} 
			] ],
			fitColumns : true,// 自适应水平，防止出现水平滚动条
			singleSelect : true
		};
	$('#datagridOfHistory').datagrid(orderTable);
});