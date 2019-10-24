var stdocDowncheckInfo;
var stdocId;
var fileAddress;
$(function(){
	stdocDowncheckInfo={
			url : ctx + '/mgr/stdocDowncheck/list4.json',
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
				field:'stdocId',
				hidden:true
			},{
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
				field : 'cause',
				title : '申请原因',
				width : 150,
				align : 'center'
			}, {
				field : 'time',
				title : '申请时间',
				align : 'center',
				width : 150
			},  {
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
				stdocId=row.stdocId;
				var params={};
				params.stdocId=stdocId;
				$.ajax({
					type:'post',
					url:ctx + '/mgr/stdocDowncheck/getFileNameById.json',
					data:params,
					success : function(data) {
						$("#author").text(data.author);
						$("#createOper").text(data.createOper);
						$("#createTime").text(data.createTime);
						$('#file_watch2').text("文件名：" + data.fileName);
						fileAddress=data.fileAddress;
						var file2;
						if(fileAddress!=null){
							file2 = fileAddress.substring(fileAddress.indexOf(".")+1);
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
	$("#data_grant").datagrid(stdocDowncheckInfo);
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
						url : path + '/stdocDowncheck/agreeMore.json',
						data : checkIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$("#data_grant").datagrid(stdocDowncheckInfo);
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
		location.href = path + "/stDocInfo/downloadFile?fileAddress=" + fileAddress + "&stdocId=" + stdocId+"&flag="+flag;
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
						url : path + '/stdocDowncheck/refuseMore.json',
						data : checkIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$("#data_grant").datagrid(stdocDowncheckInfo);
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
//进行单个审批拒绝	
$("#btnToRefuse").click(function(){
		var param={};
		param.checkId=$("#checkId").val();
		param.remark=$("#remark").val();
		$.ajax({
			type:'post',
			url:path+'/stdocDowncheck/refuse',
			data:param,
			success:function(data){
				$.messager.alert("提醒","操作成功");
				$("#data_grant").datagrid(stdocDowncheckInfo);
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
		url : path + '/stdocDowncheck/agree',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$("#data_grant").datagrid(stdocDowncheckInfo);
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}


//拒绝申请,弹出拒绝窗
function refuse(checkId){
	$("#checkId").val(checkId);
	$("#addWin").dialog('open');
	
}



//返回
function return_main(){
	$('#watchWin').hide();
}
