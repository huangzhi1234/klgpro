var patId;
var filePath;
var flag = true;
var userId;

$(function() {

	//获得当前用户的id
	$.ajax({
		type : 'post',
		url : path + "/user/getUserId.json",
		async : false,
		success : function(data) {
			userId = data;
		}
	});
	
	
	
	// 初始化表格
	var patchInfoTable = {
		url : ctx + '/mgr/patchInfo/list2.json',
		columns : [ [{
			field : 'patId',
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
			field : 'patName',
			title : '成果名称',
			align : 'center',
			width : 150
		}, {
			field : 'ncVersion',
			title : '对应NC版本',
			align : 'center',
			width : 80,
			formatter : function(value, row, index) {
				var name;
				if (value != null) {
					var param = {};
					param.id = value;
					$.ajax({
						type : 'post',
						url : path + "/dictionary/load",
						async : false,
						data : param,
						success : function(data) {
							name = data.dicName;
						}
					});
				}
				return name;
			}
		}, {
			field : 'busiMod',
			title : '所属业务模块',
			align : 'center',
			width : 80,
			formatter : function(value, row, index) {
				var name;
				if (value != null) {
					var param = {};
					param.id = value;
					$.ajax({
						type : 'post',
						url : path + "/dictionary/load",
						async : false,
						data : param,
						success : function(data) {
							name = data.dicName;
						}
					});
				}
				return name;
			}
		}, {
			field : 'patMan',
			title : '提供者',
			align : 'center',
			width : 80
		}, {
			field : 'customer',
			title : '客户',
			align : 'center',
			width : 80
		}, {
			field : 'email',
			title : '联系邮箱',
			align : 'center',
			width : 80
		}, {
			field : 'phone',
			title : '联系电话',
			align : 'center',
			width : 100
		}, {
			field : 'upTime',
			title : '上传时间',
			align : 'center',
			width : 100
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		onClickRow : function(rowIndex, rowData) {
			$('#watchWin2').show(); //打开对话框

			var ncVersion_name;
			var busiMod_name;
			var patMan_name;
			var param = {};
			param.id = rowData.ncVersion;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					ncVersion_name = data.dicName;
				}
			});
			param.id = rowData.busiMod;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					busiMod_name = data.dicName;
				}
			});
			
			var isOk=0;
			param.patId=rowData.patId;
			$.ajax({
				type : 'post',
				url : path + "/checkLoad/getCheckLoadById.json",
				async : false,
				data : param,
				success : function(data) {
					isOk = data.isOk;
					if(isOk==1){
						$('#fileDown2').html("下载");
						$('#fileDown2').attr('style','background:skyblue');
					}else{
						$('#fileDown2').html("申请下载");
						$('#fileDown2').attr('style','background:#cd9a5b');
					}
				}
			});
			
			
			//把该条信息回显在编辑窗口中
			$('#patName_watch2').text(rowData.patName);
			$('#receiveTime_watch2').text(rowData.receiveTime);
			$('#patMan_watch2').text(rowData.patMan);
			$('#email_watch2').text(rowData.email);
			$('#phone_watch2').text(rowData.phone);
			$('#qq_watch2').text(rowData.qq);
			$('#proName_watch2').text(rowData.proName);
			$('#testMan_watch2').text(rowData.testMan);
			$('#patDis_watch2').text(rowData.patDis);
			$('#comment_watch2').text(rowData.comment);
			$('#ncVersion_watch2').text(ncVersion_name);
			$('#busiMod_watch2').text(busiMod_name);
			$('#customer2').text(rowData.customer);

			patId = rowData.patId;
			filePath = rowData.file; //缓存文件在数据库的路径

			var file = new Array();
			if(filePath!=null){
				file = filePath.split("/");
			}
			
			$('#file_watch2').text("文件名：" + rowData.fileName);
			$('#file_watch2').show();
			$('#fileDown2').show();
			if(file!=null){
				var file2 = file[file.length - 1].split(".");
			}
			if (file2[file2.length - 1] == "doc" || file2[file2.length - 1] == "docx") {
				document.getElementById("tubiao2").src = path + "/images/doc.png";
			} else if (file2[file2.length - 1] == "xls" || file2[file2.length - 1] == "xlsx") {
				document.getElementById("tubiao2").src = path + "/images/Excel.png";
			} else if (file2[file2.length - 1] == "pdf") {
				document.getElementById("tubiao2").src = path + "/images/pdf.png";
			} else {
				document.getElementById("tubiao2").src = path + "/images/yasuo.png";
			}
		},
		queryParams : $("#schForm").formToJson(), //查询框里的参数，将表单数据转换成json
		toolbar : "#tb_patchInfo" //功能按钮
	};

	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagridOfParchInfo').datagrid(patchInfoTable);

	//查询按钮
	$("#scbtn").click(function() {
		$('#datagridOfParchInfo').datagrid('load', $("#schForm").formToJson()); //重新加载，后边是参数
		$('#watchWin2').hide();
	});
	//新增按钮  	 实施成果基本信息
	$("#btnAdd_patchInfo2").click(function() {
		var dicNum = document.getElementById("dicNum").value;
		if (dicNum == "") {
			$.messager.alert("提醒","请选择相应节点才能进行新增操作");
			return;
		}
		$('#addWin').show(); //打开对话框
		$('#class_required').hide(); //打开对话框
		$('#watchWin2').hide();
	});

	//新增提交按钮(校验数据并提交表单) 项目基本信息
	$("#btnToAdd").click(function() {
		if ($("#addForm").form('validate')) {
			var s = document.addForm.fileOne.value;
			if (s == "") {
				$.messager.alert("提醒","请添加上传文件！");
				document.addForm.fileOne.focus();
				return;
			}
			$.ajax({
				type : 'post',
				url : path + '/patchInfo/add2.json',
				data : $("#addForm").serialize(), //序列化一组表单元素，将表单内容编码为用于提交的字符串
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						$('#addWin').hide();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		}
	});

	//编辑按钮
	$("#btnEdit_patchInfo2").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfParchInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#watchWin2').hide();
			$('#editWin').show(); //打开对话框
			$('#class_required').hide(); //打开对话框
			//把该条信息回显在编辑窗口中
			$('#editForm').find('input[name="patId"]').val(arr[0].patId);
			$('#editForm').find('input[name="patName"]').val(arr[0].patName);
			//$('#editForm').find('input[name="receiveTime"]').val(arr[0].receiveTime);
			$('#editForm').find('input[name="email"]').val(arr[0].email);
			$('#editForm').find('input[name="phone"]').val(arr[0].phone);
			$('#editForm').find('input[name="customer"]').val(arr[0].customer);
			$('#patMan2').val(arr[0].patMan);
			$('#ncVersion3').combobox('setValue', arr[0].ncVersion);
			$('#busiMod3').combobox('setValue', arr[0].busiMod);
			$('#isDep2').combobox('setValue', arr[0].isDep);
			$('#isSql2').combobox('setValue', arr[0].isSql);
			$('#metadataUp2').combobox('setValue', arr[0].metadataUp);
			$('#receiveTime2').datebox("setValue", arr[0].receiveTime);
			$('#testStatus2').combobox("setValue", arr[0].testStatus);
			//$('#proName2').combobox("setValue", arr[0].proName);

			$('#editForm').find('input[name="sqlName"]').val(arr[0].sqlName);
			$('#editForm').find('input[name="metadataName"]').val(arr[0].metadataName);
			$('#editForm').find('input[name="qq"]').val(arr[0].qq);
			$('#editForm').find('input[name="proName"]').val(arr[0].proName);
			$('#editForm').find('input[name="testMan"]').val(arr[0].testMan);
			$('#editForm').find('input[name="phone"]').val(arr[0].phone);
			$('#editForm').find('input[name="dicNum"]').val(arr[0].dicNum);
			$('#editForm').find('input[name="upMan"]').val(arr[0].upMan);
			$('#editForm').find('input[name="upTime"]').val(arr[0].upTime);
			$('#editForm').find('textarea[name="patDis"]').val(arr[0].patDis);
			$('#editForm').find('textarea[name="comment"]').val(arr[0].comment);

			patId = arr[0].patId;
			filePath = arr[0].file; //缓存文件在数据库的路径
			if (!filePath) {
				$('#fileDel').hide();
				$('#fileAddress_edit').hide();
			} else {
				var file = new Array();
				file = filePath.split("/");
				$('#fileAddress_edit').text(file[file.length - 1]);
				$('#fileAddress_edit').show();
				$('#fileDel').show();
			}
			//然后打开窗口			 			 
			//$('#editWin').dialog('open');

		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要编辑的数据');
		}

	});

	//更新提交
	$("#btnToUpdate").click(function() {
		if ($("#editForm").form('validate')) {
			$.ajax({
				type : 'post',
				url : path + '/patchInfo/update.json',
				data : $("#editForm").serialize(),
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						$('#editWin').hide();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		} else {
			$.messager.alert('提醒', "更新失败");
		}
	});

	$('#return').click(function() {
		$('#watchWin').hide();
		$('#watchWin2').hide();
		$('#class_required').show();
	});

	$('#btnWatch_patchInfo2').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfParchInfo').datagrid("getSelections");
		if (arr.length == 1) {

			$('#watchWin2').hide();
			$('#watchWin').show(); //打开对话框
			$('#class_required').hide(); //打开对话框

			var ncVersion_name;
			var busiMod_name;
			var upMan_name;
			var patMan_name;
			var param = {};
			param.id = arr[0].ncVersion;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					ncVersion_name = data.dicName;
				}
			});
			param.id = arr[0].busiMod;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					busiMod_name = data.dicName;
				}
			});
			param.userId = arr[0].upMan;
			$.ajax({
				type : 'post',
				url : path + "/user/queryUserById.json",
				async : false,
				data : param,
				success : function(data) {
					upMan_name = data.userName;
				}
			});
			
			
			var isOk=0;
			var pp={};
			pp.patId=arr[0].patId;
			$.ajax({
				type : 'post',
				url : path + "/checkLoad/getCheckLoadById.json",
				async : false,
				data : pp,
				success : function(data) {
					isOk = data.isOk;
					if(isOk==1){
						$('#fileDown').html("下载");
						$('#fileDown').attr('style','background:skyblue');
					}else{
						$('#fileDown').html("申请下载");
						$('#fileDown').attr('style','background:#cd9a5b');
					}
				}
			});
			
			
			
			
			//把该条信息回显在编辑窗口中
			$('#patName_watch').text(arr[0].patName);
			$('#receiveTime_watch').text(arr[0].receiveTime);
			$('#patMan_watch').text(arr[0].patMan);
			$('#email_watch').text(arr[0].email);
			$('#phone_watch').text(arr[0].phone);
			$('#qq_watch').text(arr[0].qq);
			$('#proName_watch').text(arr[0].proName);
			$('#testMan_watch').text(arr[0].testMan);
			$('#patDis_watch').text(arr[0].patDis);
			$('#ncVersion_watch').text(ncVersion_name);
			$('#busiMod_watch').text(busiMod_name);
			$('#sqlName_watch').text(arr[0].sqlName);
			$('#metadataName_watch').text(arr[0].metadataName);
			$('#comment_watch').text(arr[0].comment);
			$('#upTime_watch').text(arr[0].upTime);
			$('#upMan_watch').text(upMan_name);
			$('#customer').text(arr[0].customer);

			if (arr[0].isDep == 1) {
				$('#isDep_watch').text("是");
			} else if (arr[0].isDep == 0) {
				$('#isDep_watch').text("否");
			}

			if (arr[0].isSql == 1) {
				$('#isSql_watch').text("是");
			} else if (arr[0].isSql == 0) {
				$('#isSql_watch').text("否");
			}
			if (arr[0].metadataUp == 1) {
				$('#metadataUp_watch').text("是");
			} else if (arr[0].metadataUp == 0) {
				$('#metadataUp_watch').text("否");
			}
			if (arr[0].testStatus == 1) {
				$('#testStatus_watch').text("通过");
			} else if (arr[0].testStatus == 0) {
				$('#testStatus_watch').text("不通过");
			}

			patId = arr[0].patId;
			filePath = arr[0].file; //缓存文件在数据库的路径
			var file = new Array();
			if(filePath!=null){
				file = filePath.split("/");
			}
			$('#file_watch').text("文件名：" + arr[0].fileName);
			$('#file_watch').show();
			$('#fileDown').show();
			//判断文件后缀
			if(file!=null){
				var file2 = file[file.length - 1].split(".");
			}
			if (file2[file2.length - 1] == "doc" || file2[file2.length - 1] == "docx") {
				document.getElementById("tubiao").src = path + "/images/doc.png";
			} else if (file2[file2.length - 1] == "xls" || file2[file2.length - 1] == "xlsx") {
				document.getElementById("tubiao").src = path + "/images/Excel.png";
			} else if (file2[file2.length - 1] == "pdf") {
				document.getElementById("tubiao").src = path + "/images/pdf.png";
			} else {
				document.getElementById("tubiao").src = path + "/images/yasuo.png";
			}
			
			
			
			
			
			
			$('#datagridOfParchDownlaod').datagrid(patchDlRcTable);
			$('#downManList').panel('close');
			$('#downManBt').text("显示");
			var demo = document.getElementById('watchWin');
			var demo2 = document.getElementById('form_divWin');
			demo.style.height = "1000px";
			demo2.style.height = "1000px";
			flag = true;
			//然后打开窗口		

		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');
		}
	});
	
	

	$("#downManBt").click(function() {
		if (flag == true) {
			$('#datagridOfParchDownlaod').datagrid('load', {
				patId : patId
			});

			var demo = document.getElementById('watchWin');
			var demo2 = document.getElementById('form_divWin');
			demo.style.height = "1350px";
			demo2.style.height = "1350px";
			$('#downManList').panel('open');
			$('#downManBt').text("隐藏");
			flag = false;
		} else {
			$('#downManList').panel('close');
			$('#downManBt').text("显示");
			var demo = document.getElementById('watchWin');
			var demo2 = document.getElementById('form_divWin');
			demo.style.height = "1000px";
			demo2.style.height = "1000px";
			flag = true;
		}
	});

	//下载人列表
	var patchDlRcTable = {
		url : ctx + '/mgr/patchDlRc/listByPatId.json',
		columns : [ [ {
			field : 'rowIndex',
			title : '序号',
			width : 300,
			align : 'center',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : 'dlMan',
			title : '下载人',
			align : 'center',
			width : 300,
			formatter : function(value, row, index) {
				var name;
				if (value != null) {
					var param = {};
					param.userId = value;
					$.ajax({
						type : 'post',
						url : path + "/user/queryUserById.json",
						async : false,
						data : param,
						success : function(data) {
							name = data.userName;
						}
					});
				}
				return name;
			}
		}, {
			field : 'dlTime',
			title : '上传时间',
			align : 'center',
			width : 300
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10 ], // 每页可显示的条数，在表格左下角可选
		singleSelect : true
	};


	//删除
	$("#btnDelete_patchInfo2").click(function() {
		var arr = $('#datagridOfParchInfo').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var patchIdArr = 'patchIdArr=';
					for (var i in arr) {
						patchIdArr += (i == 0 ? '' : '#') + arr[i].patId;
					}
					$.ajax({
						type : 'post',
						url : path + '/patchInfo/delete.json',
						data : patchIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfParchInfo').datagrid(patchInfoTable);
							} else {
								$.messager.alert('提醒', data.msg);
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提醒', '请选择要删除的数据');
		}
	});

	
	
	//批量申请
		$("#btnApply_patchInfo2").click(function() {
			var arr = $('#datagridOfParchInfo').datagrid("getSelections");
			var flag=0;
			var patIdArr = '';
			var idArr = 'idArr=';
			for (var i in arr) {
				idArr += (i == 0 ? '' : 'aaa') + arr[i].patId;
				patIdArr += (i == 0 ? '' : 'aaa') + arr[i].patId;
			}
			if (arr.length > 0) {
				//先进行判断是否包含已申请过的数据
				$.ajax({
					type : 'post',
					url : path + '/checkLoad/isApply.json',
					data : idArr,
					success : function(data) {
						flag = data;
						if(flag==1){
							//记载选中要申请下载的列表
							var causeTable={
									url : ctx + '/mgr/patchInfo/applyDownList.json?patIdArr='+patIdArr,
									columns : [ [ {
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
										field : 'patName',
										title : '成果名称',
										align : 'center',
										width : 150
									},{
										field:'cause',
										title:'申请下载的原因或用途',
										align:'center',
										width:300,
										formatter:function(value,row,index){
											return '<textarea id="cause'+row.patId+'" style="width:250" name="cause" class="easyui-validatebox" data-options="required:true"></textarea>';
										}
									}
									] ],
									pagination : true, // 显示分页
									pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
									pageSize : 10, // 默认显示记录数
									fitColumns : true,// 自适应水平，防止出现水平滚动条
									singleSelect : true
							};
							$("#data_cause").datagrid(causeTable);
							$("#getDownMore").dialog('open');
						}else{
							$.messager.alert('提醒', '所选中的数据中包含已申请过的数据！');
						}
					}
				});
			}else{
				$.messager.alert('提醒', '请选择要进行下载申请的数据！');
			}
		});
		
		//批量申请提交
		$("#sendCauseMore").click(function(){
			$('#data_cause').datagrid('selectAll');
			var arr = $('#data_cause').datagrid("getSelections");
			var patchIdArr = 'patchIdArr=';
			var ff = true;
			for (var i in arr) {
				patchIdArr += (i == 0 ? '' : '#') + arr[i].patId+'#'+$('#cause'+arr[i].patId+'').val();
				//下载申请的原因或用途
				if($('#cause'+arr[i].patId+'').val()==""||$('#cause'+arr[i].patId+'').val()==null){
					ff=false;
					break;
				}
			}
			if(ff){
					$.ajax({
						type : 'post',
						url : path + '/checkLoad/applyMore.json',
						data : patchIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
							} else {
								$.messager.alert('提醒', data.msg);
							}
						}
					});
					$("#getDownMore").dialog('close');
			}else{
				$.messager.alert('提醒', '申请下载的原因或用途不能为空！');
			}
		});
	
	
	//删除项目文档附件
	$('#fileDel').click(function() {
		deleteFile_patchInfo(filePath, patId);
		
	});


	/*
	 * 附件下载
	 */

	/*$('#fileDown').click(function() {
		location.href = path + "/patchInfo/downloadFile?filePath=" + filePath + "&patId=" + patId;
	});*/

	$('#fileDown2').click(function() {
		var a=$("#fileDown2").html();
		if(a=="下载"){
			var flag="b";
			location.href = path + "/patchInfo/downloadFile?filePath=" + filePath + "&patId=" + patId+"&flag="+flag;
		}else{
			/*var isOk=0;*/
			var param={};
			param.patId=patId;
			var state=0;
			$.ajax({
				type:'post',
				url:path+'/checkLoad/getCheckLoadById.json',
				data:param,
				success:function(data){
					state=data.state;
					var oper=data.oper;
					if(state==1&&userId==oper){
						$.messager.alert("提醒","你已经提交过申请，请耐心等候！");
					}else{
						$("#getDown").dialog('open');
						$("#patIdDown").val(patId);
						/*$.ajax({
							type:'post',
							url:path+'/checkLoad/getIsOk2',
							data:param,
							success:function(data){
								$.messager.alert("提醒","申请下载权限已提交！！！");
							}
						});*/
					}
				}
			});
			
		}
	});
	
	//提交单个下载申请
	$("#sendCause").click(function(){
		var param={};
		var cause=$("#cause").val();
		param.patId=$("#patIdDown").val();
		param.cause=cause;
		if(null==cause||''==cause){
			$.messager.alert("提醒","申请原因或用途不能为空！");
		}else{
			$.ajax({
				type:'post',
				url:path+'/checkLoad/getIsOk2',
				data:param,
				success:function(data){
					$.messager.alert("提醒","申请下载权限已提交！！！");
				}
			});
			$("#getDown").dialog('close');
		}
		
	});
	
	
	$('#fileDown').click(function() {
		var a=$("#fileDown").html();
		if(a=="下载"){
			location.href = path + "/patchInfo/downloadFile?filePath=" + filePath + "&patId=" + patId;
		}else{
			/*var isOk=0;*/
			var param={};
			param.patId=patId;
			var state=0;
			$.ajax({
				type:'post',
				url:path+'/checkLoad/getCheckLoadById.json',
				data:param,
				success:function(data){
					state=data.state;
					var oper=data.oper;
					if(state==1&&userId==oper){
						$.messager.alert("你已经提交过申请，请耐心等候！");
					}else{
						$("#getDown").dialog('open');
						$("#patIdDown").val(patId);
						/*$.ajax({
							type:'post',
							url:path+'/checkLoad/getIsOk2',
							data:param,
							success:function(data){
								$.messager.alert("提醒","申请下载权限已提交！！！");
							}
						});*/
					}
				}
			});
			
		}
	});
	

	


	//加载数据字典
	$('#ncVersion1').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0104'
	});

	$('#ncVersion2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0104'
	});

	$('#ncVersion3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0104'
	});

	$('#busiMod1').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0105'
	});

	$('#busiMod2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0105'
	});

	$('#busiMod3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0105'
	});
	

});


//删除文件的方法 	文档
function deleteFile_patchInfo(filePath, patId) {
	var params = {};
	params.patId = patId;
	$.ajax({
		type : 'post',
		url : path + '/patchInfo/deleteFile',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}
function addcheck(){
	if($("#addForm").form('validate')){
		$("#zhezhao").css("display","block");
		$.messager.alert('提醒', "上传中，请勿进行其他操作......");
		return true;
	}else{
		$.messager.alert("提醒","添加失败！");
		return false;
	}
}
function updatecheck(){
	if($("#editForm").form('validate')){
		$.messager.alert("提醒","更新成功！");
		return true;
	}else{
		$.messager.alert("提醒","更新失败！");
		return false;
	}
}






function return_main() {
	$('#addWin').hide();
	$('#editWin').hide();
	$('#watchWin').hide();
	$('#watchWin2').hide();
	$('#grantWin').hide();
	$('#tree').hide();
	$('#class_required').show();
}
