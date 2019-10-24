$(function(){
	// 初始化表格
	var table = {
		url : '',
		columns : [ [ {
			field : '',
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
			field : 'companyName',
			title : '企业名称',
			align : 'center',
			width : 40			
		}, {
			field : 'localeCount',
			title : '现场会议次数',
			align : 'center',
			width : 30			
		}, {
			field : 'communicationCount',
			title : '通讯方式会议次数',
			align : 'center',
			width : 100
		}, {
			field : 'meetingCount',
			title : '会议合计数',
			align : 'center',
			width : 30			
		}, {
			field : 'issueCount',
			title : '议题合计数',
			align : 'center',
			width : 30			
		}, {
			field : 'billPassCount',
			title : '议案通过数',
			align : 'center',
			width : 30			
		}, {
			field : 'billRefuseCount',
			title : '议案不通过数',
			align : 'center',
			width : 30			
		}
		] ],
		pagination : true,			// 显示分页
		pageList : [ 10, 20, 50 ],	// 每页可显示的条数，在表格左下角可选
		pageSize : 10,				// 默认显示记录数
		fitColumns : true,			// 自适应水平，防止出现水平滚动条
		queryParams : $("#schForm").formToJson()		
	};
		
	var data = new Array();
	$.ajax({
		type: 'post',
		url: ctx + '/mgr/monitStatistics/queryListCompany.json',
		success: function(result){
			var length  = result.length;
			for(var i=0;i<length;i++){
				data.push({ "text":result[i].companyName, "id":result[i].companyId });
			}
			$("#companyName").combobox("loadData", data);
			
		}
	});
	
	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagrid').datagrid(table);
	
	//一进入页面不加载表格的数据，那么就把表格初始化url设为空。然后在点击查询按钮的时候，设置查询的url
	$(".scbtn").click(function(){
//		var pp = $('#companyName').val();
		var pp = $('#companyName').combobox('getValue');
		if(pp && $.trim(pp)){		
			var opts = $("#datagrid").datagrid("options");
			opts.url = path + '/monitStatistics/list.json';		
			$('#datagrid').datagrid('load',$("#schForm").formToJson());		//重新加载，后边是参数	
		}else{
			$.messager.alert('提醒',"企业名称为必输条件");
		}
	});

});