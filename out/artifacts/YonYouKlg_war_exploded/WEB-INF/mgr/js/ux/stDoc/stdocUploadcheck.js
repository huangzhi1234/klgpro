var stdocUpcheckTable;
var fileAddress;
$(function(){
	stdocUpcheckTable={
			url : ctx + '/mgr/stdocUploadcheck/list.json',
			columns : [ [ {
				field : 'uploadId',
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
			},  {
				field : 'isOk',
				title : '操作',
				align : 'center',
				width : 150,
				formatter : function(value, row, index) {
					if(value==0){
						var a='<input value="通过审批" style="text-align:center;background:skyblue;color:white;width:60px;margin-right:8px;" onclick="return agree('+row.uploadId+')" />';
						var b='<input value="拒绝上传" style="text-align:center;background:skyblue;color:white;width:60px;" onclick="return refuse('+row.uploadId+')" />';
						return a+b;
					}
				}
			}
			] ],
			
			onClickRow : function(index,row) {
				stdocId=row.stdocId;
				var params={};
				params.stdocId=stdocId;
				$.ajax({
					type:'post',
					url:ctx + '/mgr/stdocDowncheck/getFileNameById.json',
					data:params,
					success : function(data) {
						
						$('#receiveTime_watch2').text(data.createTime);
						$('#patMan_watch2').text(data.createOper);
						$('#comment').html(data.comment);
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
				$('#watchWin').show(); //打开对话框
			},
			pagination : true, // 显示分页
			pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
			pageSize : 10, // 默认显示记录数
			fitColumns : true// 自适应水平，防止出现水平滚动条
	};
	$("#data_grant").datagrid(stdocUpcheckTable);
	$('#fileDown2').click(function() {
		var flag="a";
		location.href = path + "/stDocInfo/downloadFile?fileAddress=" + fileAddress + "&stdocId=" + stdocId+"&flag="+flag;
	});
	//进行单个审批拒绝
	$("#btnToRefuse").click(function(){
		if ($("#addForm").form('validate')) {
			var param={};
			param.uploadId=$("#uploadId").val();
			param.remark=$("#remark").val();
			$.ajax({
				type:'post',
				url:path+'/stdocUploadcheck/refuse',
				data:param,
				success:function(data){
					$.messager.alert("提醒","操作成功");
					$("#data_grant").datagrid(stdocUpcheckTable);
					$("#addWin").dialog('close');
				}
			});
		} else {
			$.messager.alert("提醒","提交失败，备注不能为空！");
			return false;
		}
	});
	//批量通过
	$("#btnGrant_patchInfo").click(function(){
		var arr = $('#data_grant').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要授权选中的数据吗？', function(r) {
				if (r) {
					var uploadIdArr = 'uploadIdArr=';
					for (var i in arr) {
						uploadIdArr += (i == 0 ? '' : '#') + arr[i].uploadId;
					}
					$.ajax({
						type : 'post',
						url : path + '/stdocUploadcheck/agreeMore.json',
						data : uploadIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$("#data_grant").datagrid(stdocUpcheckTable);
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
	//弹出多个拒绝弹窗
	$("#btnRefuse_patchInfo").click(function(){
		var arr = $('#data_grant').datagrid("getSelections");
		if (arr.length >0) {
			var uploadIdArr="";
			for (var i in arr) {
				if(i==0){
					uploadIdArr=arr[i].uploadId;
				}else{
					uploadIdArr+='aaa'+arr[i].uploadId;
				}
				/*uploadIdArr += (i == 0 ? '' : '#') + arr[i].uploadId;*/
				
			}
			//进行多个审批拒绝
			var refuseTable={
					url : ctx + '/mgr/stdocUploadcheck/list.json?uploadIdArr='+uploadIdArr,
					columns : [ [ {
						field : 'uploadId',
						hidden:true
					}, {
						field : 'rowIndex',
						title : '序号',
						width : 60,
						align : 'center',
						formatter : function(value, row, index) {
							return index + 1;
						}
					},{
						field:'patId',
						hidden:true
					}, {
						field : 'fileName',
						title : '文件名称',
						align : 'center',
						width : 150,
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
						field:'remark',
						title:'不通过原因',
						align:'center',
						width:300,
						formatter:function(value,row,index){
							return '<textarea id="remark'+row.uploadId+'" style="width:250" name="remark" class="easyui-validatebox" data-options="required:true"></textarea>';
						}
					}
					] ],
					/*onLoadSuccess:function(){
                        $('#data_refuse').datagrid('selectAll');
                    },*/
					pagination : true, // 显示分页
					pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
					pageSize : 10, // 默认显示记录数
					fitColumns : true,// 自适应水平，防止出现水平滚动条
					singleSelect : true
			};
			$("#data_refuse").datagrid(refuseTable);
			$('#refuseWin').dialog('open');
		
		}else{
			$.messager.alert("提醒","请选择数据");
		}
		
	});
	//进行多个审批拒绝
	$("#btnToMoreRefuse").click(function(){
		var ff=true;
		var arr = $('#data_grant').datagrid("getSelections");
		var uploadIdArr = 'uploadIdArr=';
		for (var i in arr) {
			uploadIdArr += (i == 0 ? '' : '#') + arr[i].uploadId+'#'+$('#remark'+arr[i].uploadId+'').val();
			if($('#remark'+arr[i].uploadId+'').val()==""||$('#remark'+arr[i].uploadId+'').val()==null){
				ff=false;
				break;
			}
		}
		if(ff){
			$.ajax({
				type : 'post',
				url : path + '/stdocUploadcheck/refuseMore.json',
				data : uploadIdArr,
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						$('#refuseWin').dialog('close');
						$("#data_grant").datagrid(stdocUpcheckTable);
					} else {
						$.messager.alert('提醒', data.msg);
						$('#refuseWin').dialog('close');
					}
				}
			});
		}else{
			$.messager.alert('提醒', "备注原因不能为空！");
		}
		
	});
	
	
});


//审批下载
function agree(uploadId){
	
	var params = {};
	params.uploadId = uploadId;
	$.ajax({
		type : 'post',
		url : path + '/stdocUploadcheck/agree',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$("#data_grant").datagrid(stdocUpcheckTable);
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}

//拒绝上传弹出窗口
function refuse(uploadId){
	$("#uploadId").val(uploadId);
	$("#addWin").dialog('open');
}


//返回
function return_main(){
	$('#watchWin').hide();
}