var nows="noUser";//使用该参数传递到后台，表示无用户过滤，使得对成果上传审批的全部操作记录显示出来
var stus="hasEdit";//使用该参数传递到后台，表示不显示正在申请的数据，即只显示已通过和未通过数据
$(function(){
	$("#type").combobox('setValue',3);
	var docUp = {
			url : ctx + '/mgr/stUpHistory/list0.json?nows='+nows+'&stus='+stus,
			columns : [ [ {
				field : 'uploadId',
				title:'序号',
				width:20,
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
			queryParams : $("#schForm").formToJson(), //查询框里的参数，将表单数据转换成json
			singleSelect : true,
			toolbar : "#tb_apply" //功能按钮
		};
	$('#datagridOfDocUp').datagrid(docUp);
	
	
	var docDown = {
			url : ctx + '/mgr/stHistory/list0.json?nows='+nows+'&stus='+stus,
			columns : [ [ {
				field : 'checkId',
				title:'序号',
				width:20,
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
				field : 'cause',
				title : '申请原因',
				width : 150,
				align : 'center'
			},{
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
			]],
			pagination : true, // 显示分页
			pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
			pageSize : 10, // 默认显示记录数
			fitColumns : true,// 自适应水平，防止出现水平滚动条
			singleSelect : true,
			queryParams : $("#schForm").formToJson(), //查询框里的参数，将表单数据转换成json
			toolbar : "#tb_apply" //功能按钮
		};
		//初始化页面时直接加载表格数据，数据列表的ID
		$('#datagridOfDocDown').datagrid(docDown);
		//加载isOk下拉框数据
		var data = new Array();
		data.push({ "text":"已通过", "id":1});
		data.push({"text":"未通过","id":2});
		$("#isOk").combobox("loadData", data);
		$("#isOk2").combobox("loadData", data);
		var doctype = new Array();
		doctype.push({ "text":"开发", "id":1});
		doctype.push({"text":"实施","id":2});
		$("#doctype").combobox("loadData", doctype);
		//
		var data1= new Array();
		data1.push({ "text":"上传审批", "id":1});
		data1.push({"text":"下载审批","id":2});
		data1.push({"text":"全部","id":3});
		$("#type").combobox("loadData", data1);
		
		//
		$(".scbtn").click(function(){
			var a=$("#type").combobox("getValue");
			var f1=true;
			var f2=true;
			if(a==1){
				f2=false;
			}
			if(a==2){
				f1=false;
			}
			if(f1){$('#datagridOfDocUp').datagrid('load', $("#schForm").formToJson());}
			if(f2){$('#datagridOfDocDown').datagrid('load', $("#schForm").formToJson());}
		});
		
		var data2= new Array();
		data2.push({ "text":"已通过", "id":1});
		data2.push({"text":"未通过","id":2});
		$("#pass").combobox("loadData", data2);
		//
		
		//隐藏、显示按钮
		$("#up").click(function(){
			var display=$("#upData").css("display");
			if(display=="none"){
				$("#upData").css("display","block");
			}else{
				$("#upData").css("display","none");
			}
		});
		//隐藏、显示按钮
		$("#down").click(function(){
			var display=$("#downData").css("display");
			if(display=="none"){
				$("#downData").css("display","block");
			}else{
				$("#downData").css("display","none");
			}
			
		});
		//点击修改，弹出成果下载审批修改框
		$("#downEdit").click(function(){
			var arr=$('#datagridOfDocDown').datagrid("getSelections");
			if(arr.length==0){
				$.messager.alert("提醒","请选择数据");
			}else if(arr.length>1){
				$.messager.alert("提醒","只能选择一条数据");
			}else{
				var checkId=arr[0].checkId;
				
				var remark;
				if(arr[0].remark==null){
					remark="";
				}else{
					remark=arr[0].remark;
				}
				
				$("#checkId").val(checkId);
				$("#isOk").combobox('setValue',arr[0].isOk);
				$("#remark").text(remark);
				$("#editWin").dialog("open");
			}
			
		});
		//下载更新提交按钮
		$("#doc_down_updata").click(function(){
			$.ajax({
				type : 'post',
				url : path + '/docEditHistory/downUpdata.json',
				data : $("#editForm").serialize(),
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						$('#datagridOfDocDown').datagrid(docDown);
						$('#editWin').dialog("close");
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		});
		
		
		//点击修改，弹出成果上传审批修改框
		$("#upEdit").click(function(){
			var arr=$('#datagridOfDocUp').datagrid("getSelections");
			if(arr.length==0){
				$.messager.alert("提醒","请选择数据");
			}else if(arr.length>1){
				$.messager.alert("提醒","只能选择一条数据");
			}else{
				var uploadId=arr[0].uploadId;
				
				var remark;
				if(arr[0].remark==null){
					remark="";
				}else{
					remark=arr[0].remark;
				}
				
				$("#uploadId").val(uploadId);
				$("#isOk2").combobox('setValue',arr[0].isOk);
				$("#remark2").text(remark);
				$("#editWin2").dialog("open");
			}
			
		});
		//上传更新提交按钮
		$("#doc_up_updata").click(function(){
			var flag=true;
			if($("#isOk2").combobox('getValue')==2){
				if ($("#editForm2").form('validate')) {
				}else{
					flag=false;
					$.messager.alert("提醒","备注不能为空");
				}
			}
			if(flag){
				$.ajax({
					type : 'post',
					url : path + '/docEditHistory/upUpdata.json',
					data : $("#editForm2").serialize(),
					success : function(data) {
						if (data.result == true) {
							$.messager.alert('提醒', data.msg);
							$('#datagridOfDocUp').datagrid(docUp);
							$('#editWin2').dialog("close");
						} else {
							$.messager.alert('提醒', data.msg);
						}
					}
				});
			}
			
			
		});


});