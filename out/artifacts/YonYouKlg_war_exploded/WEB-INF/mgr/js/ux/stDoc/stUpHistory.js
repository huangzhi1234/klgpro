$(function(){
	// 初始化表格
	var stUphistoryTable = {
		url : ctx + '/mgr/stUpHistory/list0.json',
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
		}, {
			field : 'author',
			title : '作者',
			align : 'center',
			width : 250,
			formatter : function(value, row, index) {
				var author;
				if (row.stdocId != null) {
					var param = {};
					param.stdocId = row.stdocId;
					$.ajax({
						type : 'post',
						url : ctx + '/mgr/stUpHistory/getAuthorById.json',
						async : false,
						data : param,
						success : function(data) {
							author = data.author;
						}
					});
				}
				return author;
			}
		},{
			field : 'type',
			title : '类型',
			align : 'center',
			width : 250,
			formatter : function(value, row, index) {
				var type;
				if (row.stdocId != null) {
					var param = {};
					param.stdocId = row.stdocId;
					$.ajax({
						type : 'post',
						url : ctx + '/mgr/stUpHistory/getAuthorById.json',
						async : false,
						data : param,
						success : function(data) {
							if(data.type==1){
								type="开发";
							}else if(data.type==2){
								type="实施";
							}
						}
					});
				}
				return type;
			}
		},{
			field : 'oper',
			hidden:true
		}, {
			field : 'time',
			title : '申请时间',
			align : 'center',
			width : 150
		},  {
			field : 'state',
			title : '操作',
			align : 'center',
			width : 150,
			formatter : function(value, row, index) {
				var a;
				if(row.isOk==1){
					a="<label style='color:blue;'>已通过</label>";
				}else if(row.isOk==2){
					a="<label style='color:red;'>未通过</label>";
				}else{
					if(value==0){
						a="未申请";//这里可以被忽略
					}else{
						a="已申请";
					}
				}
				return a;
			}
		}, {
			field : 'remark',
			title : '备注',
			align : 'center',
			width : 150
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true,// 自适应水平，防止出现水平滚动条
		singleSelect : true,
		queryParams : $("#schForm").formToJson(), //查询框里的参数，将表单数据转换成json
		toolbar : "#tb_apply" //功能按钮
	};
	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagridOfHistory').datagrid(stUphistoryTable);
	$(".scbtn").click(function() {
		$('#datagridOfHistory').datagrid('load', $("#schForm").formToJson()); //重新加载，后边是参数
	});
	
	var doctype = new Array();
	doctype.push({ "text":"开发", "id":1});
	doctype.push({"text":"实施","id":2});
	$("#doctype").combobox("loadData", doctype);
});