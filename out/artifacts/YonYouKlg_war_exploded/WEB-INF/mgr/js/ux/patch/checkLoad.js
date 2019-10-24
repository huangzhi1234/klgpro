var checkLoadInfo;
var patId;
var file;
$(function(){
	 checkLoadInfo={
			url : ctx + '/mgr/checkLoad/list.json',
			columns : [ [ {
				field : 'checkId',
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
				field : 'oper',
				title : '申请人',
				align : 'center',
				width : 150,
				formatter : function(value, row, index) {
					var userName;
					var param = {};
					param.userId = value;
					$.ajax({
						type : 'post',
						url : ctx + '/mgr/user/queryUserById.json',
						async : false,
						data : param,
						success : function(data) {
							userName = data.userName;
						}
					});
					return userName;
				}
			}, {
				field : 'time',
				title : '申请时间',
				align : 'center',
				width : 150
			},{
				field : 'type',
				title : '类型',
				align : 'center',
				width : 150,
				formatter : function(value, row, index) {
					if(value==1){
						return "研发";
					}else if(value==2){
						return "实施";
					}
				}
			}, {
				field : 'cause',
				title : '申请理由',
				align : 'center',
				width : 150
			}, {
				field : 'isOk',
				title : '操作',
				align : 'center',
				width : 150,
				formatter : function(value, row, index) {
					if(value==0){
						var a='<input value="通过审批" style="text-align:center;background:skyblue;color:white;width:60px;margin-right:8px;" onclick="return agree('+row.checkId+')" />';
						var b='<input value="拒绝申请" style="text-align:center;background:skyblue;color:white;width:60px;" onclick="return refuse('+row.checkId+')" />';
						return a+b;
					}
				}
			}
			] ],
			onClickRow : function(index,row) {
				$('#watchWin').show(); //打开对话框
				patId=row.patId;
				var params={};
				params.patId=patId;
				$.ajax({
					type:'post',
					url:ctx + '/mgr/checkLoad/getPatInfoById.json',
					data:params,
					success : function(data) {
						var ncVersion_name;
						var busiMod_name; 
						var param={};
						param.id=data.ncVersion;
						
						$.ajax({
							type : 'post',
							url : path + "/dictionary/load",
							async : false,
							data : param,
							success : function(data) {
								ncVersion_name = data.dicName;
							}
						});
						param.id = data.busiMod;
						$.ajax({
							type : 'post',
							url : path + "/dictionary/load",
							async : false,
							data : param,
							success : function(data) {
								busiMod_name = data.dicName;
							}
						});
						$('#patName_watch2').text(data.patName);
						$('#receiveTime_watch2').text(data.receiveTime);
						$('#patMan_watch2').text(data.patMan);
						$('#email_watch2').text(data.email);
						$('#phone_watch2').text(data.phone);
						$('#qq_watch2').text(data.qq);
						$('#proName_watch2').text(data.proName);
						$('#testMan_watch2').text(data.testMan);
						$('#patDis_watch2').text(data.patDis);
						$('#comment_watch2').text(data.comment);
						$('#ncVersion_watch2').text(ncVersion_name);
						$('#busiMod_watch2').text(busiMod_name);
						$('#file_watch2').text("文件名：" + data.fileName);
						file=data.file;
						var file2;
						if(file!=null){
							file2 = file.substring(file.indexOf(".")+1);
						}
						if (file2 == "doc" || file2== "docx") {
							$("#tubiao2").attr("src",path + "/images/doc.png");
						} else if (file2== "xls" || file2== "xlsx") {
							$("#tubiao2").attr("src",path + "/images/Excel.png");
						} else if (file2== "pdf") {
							$("#tubiao2").attr("src",path + "/images/pdf.png");
						} else {
							$("#tubiao2").attr("src", path + "/images/yasuo.png");
						}
					}
				});
			},
				
			pagination : true, // 显示分页
			pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
			pageSize : 10, // 默认显示记录数
			fitColumns : true// 自适应水平，防止出现水平滚动条
	};
	$("#data_grant").datagrid(checkLoadInfo);
	$("#btnGrant_patchInfo").click(function(){
		var arr = $('#data_grant').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要授权选中的数据吗？', function(r) {
				if (r) {
					var checkIdArr = 'checkIdArr=';
					for (var i in arr) {
						checkIdArr += (i == 0 ? '' : '#') + arr[i].checkId;
					}
					$.ajax({
						type : 'post',
						url : path + '/checkLoad/agreeMore.json',
						data : checkIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$("#data_grant").datagrid(checkLoadInfo);
							} else {
								$.messager.alert('提醒', data.msg);
							}
						}
					});
				}
			});
		}else{
			$.messager.alert('提醒', '请选择数据！！！');
		}
	});
	$('#fileDown2').click(function() {
		var flag="a";
		location.href = path + "/patchInfo/downloadFile?filePath=" + file + "&patId=" + patId+"&flag="+flag;
	});
	$("#btnRefuse_patchInfo").click(function(){
		var arr = $('#data_grant').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要拒绝选中的数据吗？', function(r) {
				if (r) {
					var checkIdArr = 'checkIdArr=';
					for (var i in arr) {
						checkIdArr += (i == 0 ? '' : '#') + arr[i].checkId;
					}
					$.ajax({
						type : 'post',
						url : path + '/checkLoad/refuseMore.json',
						data : checkIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$("#data_grant").datagrid(checkLoadInfo);
							} else {
								$.messager.alert('提醒', data.msg);
							}
						}
					});
				}
			});
		}else{
			$.messager.alert('提醒', '请选择数据！！！');
		}
	});
	
	$("#btnToRefuse").click(function(){
		var param={};
		param.checkId=$("#checkId").val();
		param.remark=$("#remark").val();
		$.ajax({
			type:'post',
			url:path+'/checkLoad/refuse',
			data:param,
			success:function(data){
				$.messager.alert("提醒","操作成功");
				$("#data_grant").datagrid(checkLoadInfo);
				$("#addWin").dialog('close');
			}
		});
	});
	
	
});
//审批下载
function agree(checkId){
	var params = {};
	params.checkId = checkId;
	$.ajax({
		type : 'post',
		url : path + '/checkLoad/agree',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$("#data_grant").datagrid(checkLoadInfo);
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}


//拒绝申请
function refuse(checkId){
	$("#checkId").val(checkId);
	$("#addWin").dialog('open');
	/*var params = {};
	params.checkId = checkId;
	$.ajax({
		type : 'post',
		url : path + '/checkLoad/refuse',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$("#data_grant").datagrid(checkLoadInfo);
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});*/
}



//返回
function return_main(){
	$('#watchWin').hide();
}
