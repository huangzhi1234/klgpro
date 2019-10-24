var departN;
var userId=1;
var isOks="1";

$(function(){
	// 初始化表格
	var historyTable = {
		url : ctx + '/mgr/proScore/ListByPerson.json',
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
			
		}
		, {
			field : 'depart',
			title : '所属部门',
			align : 'center',
			width : 150,
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
			title : '积分',
			align : 'center',
			width : 150,
			sortable:true
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true,// 自适应水平，防止出现水平滚动条
		queryParams : $("#schForm").formToJson(), //查询框里的参数，将表单数据转换成json
		toolbar : "#tb_apply", //功能按钮 
		singleSelect : true,
		/*sortName:'score',*/
		onClickRow : function(rowIndex, rowData) {
			userId=rowData.userId;
			var depart;//声明一个异步请求对象
			var param={};
			param.userId=rowData.userId;
			param.scoreId=rowData.scoreId;
			//查询部门
			$.ajax({
				type:'post',//提交数据的类型
				url:path+'/proScore/getdepart.json',//提交网址
				async:false,//同步提交选项
				data:param,//提交的数据
				success:function(data){//成功返回之后调用的函数
					depart=data.dicName;
				}
			});
			//查询名称
			var person;
			$.ajax({
				type:'post',//提交数据的类型
				url:path+'/orderScore/getUserBean.json',//提交网址
				async:false,//同步提交选项
				data:param,//提交的数据
				success:function(data){//成功返回之后调用的函数
					person=data.userName;
				}
			});
			$("#person").text(person);
			$("#departN").text(depart);
			/*------查询当前排名-------*/
			var count;
			$.ajax({
				type:'post',
				url:path+'/proScore/getCount.json',
				async:false,
				data:param,
				success:function(data){
					count=data;
				}
			});
			/*---------查询个人在部门中的积分排名---------*/
			var myDepart;
			$.ajax({
				type:'post',
				url:path+'/proScore/countInDepart.json',
				async:false,
				data:param,
				success:function(data){
					myDepart=data;
				}
			});
			$("#countInDepart").text(myDepart);
			
			$("#rowIndex").text(count);
			$("#score").text(rowData.score);
			/*----------查询上传分数------------*/
			var upScore;
			$.ajax({
				type:'post',
				url:path+'/proScore/getUpScore.json',
				async:false,
				data:param,
				success:function(data){
					upScore=data;
				}
			});
			$("#up_score").text(upScore);//上传总积分
			/*--------------查询下载分数-----------------*/
			var downScore;
			$.ajax({
				type:'post',
				url:path+'/proScore/getDownScore.json',
				async:false,
				data:param,
				success:function(data){
					downScore=data;
				}
			});
			$("#down_score").text(downScore);//下载总积分
			
			
			
			
			
			
			//成果上传信息列表
			var patUpInfo={
					url : ctx + '/mgr/uploadHistory/list.json?isOk='+isOks+"&userId="+userId,
					columns : [ [ {
						field : 'uploadId',
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
						field:'patId',
						hidden:true
					},{
						field : 'patName',
						title : '成果名称',
						align : 'center',
						width : 150,
						formatter:function(value,row,index){
							var patName;
							if(row.patId!=null){
								var param={};
								param.patId=row.patId;
								$.ajax({
									type:'post',
									url:ctx + '/mgr/checkLoad/getPatInfoById.json',
									async : false,
									data : param,
									success : function(data) {
										patName = data.patName;
									}
								});
							}
							return patName;
						}
					}, {
						field : 'fileName',
						title : '文件名称',
						align : 'center',
						width : 250,
						formatter : function(value, row, index) {
							var fileName;
							if (row.patId != null) {
								var param = {};
								param.patId = row.patId;
								$.ajax({
									type : 'post',
									url : ctx + '/mgr/checkLoad/getPatInfoById.json',
									async : false,
									data : param,
									success : function(data) {
										fileName = data.fileName;
									}
								});
							}
							return fileName;
						}
					}, {
						field : 'time',
						title : '申请时间',
						align : 'center',
						width : 150
					}, {
						field : 'score',
						title : '积分',
						align : 'center',
						width : 150,
						formatter : function(value, row, index) {
							var param={};
							var score;
							param.ruleId=1;	
							$.ajax({
								type : 'post',
								url : ctx + '/mgr/proScore/getScoreRule.json',
								async : false,
								data : param,
								success : function(data) {
									score = data.score;
								}
							});
							return score;
						}
					}
					] ]
			};
			//标准文档上传列表
			var docUpInfo = {
					url : ctx + '/mgr/stUpHistory/list.json?isOk='+isOks+"&userId="+userId,
					columns : [ [ {
						field : 'uploadId',
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
						field:'stdocId',
						hidden:true
					},{
						field:'isOk',
						hidden:true
					}, {
						field : 'fileName',
						title : '文件名称',
						align : 'center',
						width : 250,
						formatter : function(value, row, index) {
							var fileName;
							if (row.stdocId != null) {
								var param = {};
								param.stdocId = row.stdocId;
								$.ajax({
									type : 'post',
									url : ctx + '/mgr/stdocDowncheck/getFileNameById.json',
									async : false,
									data : param,
									success : function(data) {
										fileName = data.fileName;
									}
								});
							}
							return fileName;
						}
					},{
						field : 'oper',
						hidden:true
					},{
						field : 'author',
						title : '作者',
						align : 'center',
						width : 150,
						formatter : function(value, row, index) {
							var author;
							if (row.stdocId != null) {
								var param = {};
								param.stdocId = row.stdocId;
								$.ajax({
									type : 'post',
									url : ctx + '/mgr/stdocDowncheck/getFileNameById.json',
									async : false,
									data : param,
									success : function(data) {
										author = data.author;
									}
								});
							}
							return author;
						}
					}, {
						field : 'time',
						title : '申请时间',
						align : 'center',
						width : 150
					} ,{
						field : 'score',
						title : '积分',
						align : 'center',
						width : 150,
						formatter : function(value, row, index) {
							var param={};
							var score;
							param.ruleId=1;	
							$.ajax({
								type : 'post',
								url : ctx + '/mgr/proScore/getScoreRule.json',
								async : false,
								data : param,
								success : function(data) {
									score = data.score;
								}
							});
							return score;
						}
					}
					] ]
				};
			//成果下载信息列表
			var patDownInfo={
					url : ctx + '/mgr/history/list.json?isOk='+isOks+"&userId="+userId,
					columns : [ [ {
						field : 'checkId',
						hidden:true
					}, {
						field : 'rowIndex',
						title : '序号',
						width : 30,
						align : 'center',
						formatter : function(value, row, index) {
							return index + 1;
						}
					},{
						field:'patId',
						hidden:true
					},{
						field : 'patName',
						title : '成果名称',
						align : 'center',
						width : 150,
						formatter:function(value,row,index){
							var patName;
							if(row.patId!=null){
								var param={};
								param.patId=row.patId;
								$.ajax({
									type:'post',
									url:ctx + '/mgr/checkLoad/getPatInfoById.json',
									async : false,
									data : param,
									success : function(data) {
										patName = data.patName;
									}
								});
							}
							return patName;
						}
					}, {
						field : 'fileName',
						title : '文件名称',
						align : 'center',
						width : 250,
						formatter : function(value, row, index) {
							var fileName;
							if (row.patId != null) {
								var param = {};
								param.patId = row.patId;
								$.ajax({
									type : 'post',
									url : ctx + '/mgr/checkLoad/getPatInfoById.json',
									async : false,
									data : param,
									success : function(data) {
										fileName = data.fileName;
									}
								});
							}
							return fileName;
						}
					},{
						field : 'time',
						title : '申请时间',
						align : 'center',
						width : 150
					},{
						field : 'score',
						title : '积分',
						align : 'center',
						width : 150,
						formatter : function(value, row, index) {
							var param={};
							var score;
							param.ruleId=2;	
							$.ajax({
								type : 'post',
								url : ctx + '/mgr/proScore/getScoreRule.json',
								async : false,
								data : param,
								success : function(data) {
									score = data.score;
								}
							});
							return score;
						}
					}
					] ]
			};
			//文档下载信息列表
			var docDownInfo={
					url : ctx + '/mgr/stHistory/list.json?isOk='+isOks+"&userId="+userId,
					columns : [ [ {
						field : 'checkId',
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
						field:'stdocId',
						hidden:true
					},{
						field:'isOk',
						hidden:true
					}, {
						field : 'fileName',
						title : '文件名称',
						align : 'center',
						width : 250,
						formatter : function(value, row, index) {
							var fileName;
							if (row.stdocId != null) {
								var param = {};
								param.stdocId = row.stdocId;
								$.ajax({
									type : 'post',
									url : ctx + '/mgr/stdocDowncheck/getFileNameById.json',
									async : false,
									data : param,
									success : function(data) {
										fileName = data.fileName;
									}
								});
							}
							return fileName;
						}
					},{
						field : 'oper',
						hidden:true
					},{
						field : 'author',
						title : '作者',
						align : 'center',
						width : 150,
						formatter : function(value, row, index) {
							var author;
							if (row.stdocId != null) {
								var param = {};
								param.stdocId = row.stdocId;
								$.ajax({
									type : 'post',
									url : ctx + '/mgr/stdocDowncheck/getFileNameById.json',
									async : false,
									data : param,
									success : function(data) {
										author = data.author;
									}
								});
							}
							return author;
						}
					}, {
						field : 'time',
						title : '申请时间',
						align : 'center',
						width : 150
					},{
						field : 'score',
						title : '积分',
						align : 'center',
						width : 150,
						formatter : function(value, row, index) {
							var param={};
							var score;
							param.ruleId=2;	
							$.ajax({
								type : 'post',
								url : ctx + '/mgr/proScore/getScoreRule.json',
								async : false,
								data : param,
								success : function(data) {
									score = data.score;
								}
							});
							return score;
						}
					}
					] ]
			};
			
			
			//上传详情列表
			$("#readUpDetail").click(function(){
				$("#up_pat").datagrid(patUpInfo);
				$("#up_doc").datagrid(docUpInfo);
				$("#uploadWin").dialog("open");
			});
			//下载详情列表
			$("#readDownDetail").click(function(){
				$("#down_pat").datagrid(patDownInfo);
				$("#down_doc").datagrid(docDownInfo);
				$("#downWin").dialog("open");
			});
			
			
			
			
			
			
			
			
			
			$('#watchWin').show();//打开对话框
		}
		
	};
	
	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagridOfScore').datagrid(historyTable);
	$(".scbtn").click(function() {
		$('#datagridOfScore').datagrid('load', $("#schForm").formToJson()); //重新加载，后边是参数
	});
	
	//我的积分
	$.ajax({
		type:'post',
		url:ctx + '/mgr/proScore/getProScore.json',
		async : false,
		success : function(data) {
			$("#myScores").html(data.score);
		}
	});
	//我的排名
	$.ajax({
		type:'post',
		url:ctx + '/mgr/proScore/getMyCount.json',
		async : false,
		success : function(data) {
			$("#myCount").html(data);
		}
	});
	
	
	
	
	//记载部门列表
	$('#depart').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/depart/combo?id=0'
	});
});

function return_main(){
	$("#watchWin").hide();
	
}

