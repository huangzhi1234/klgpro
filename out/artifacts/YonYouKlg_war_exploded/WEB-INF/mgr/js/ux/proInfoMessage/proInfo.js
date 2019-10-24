var copyProNum;
var copyProName;
var loadSign = false;
var codeDiscFile;
var attachAddress;
var proFileInfo_id;
var proSourceCode_id;
var proSourceName;
var proSubInfo_id;
var parentId = 0;
var proFileInfoTable;

$(function() {
	// 初始化表格
	var proInfoTable = {
		url : ctx + '/mgr/proInfo/list.json',
		columns : [ [ {
			field : 'rowIndex',
			title : '序号',
			width : 30,
			align : 'center',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : 'proName',
			title : '项目名称',
			align : 'center',
			width : 150
		}, {
			field : 'proIndu',
			title : '项目所属行业',
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
			field : 'proProductLine',
			title : '产品线',
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
			field : 'proDis',
			title : '项目描述',
			align : 'center',
			width : 150
		}, {
			field : 'startTime',
			title : '启动时间',
			align : 'center',
			width : 80
		}, {
			field : 'finishTime',
			title : '验收时间',
			align : 'center',
			width : 80
		}, {
			field : 'custName',
			title : '客户名称',
			align : 'center',
			width : 100
		}, {
			field : 'custConnect',
			title : '客户联系人',
			align : 'center',
			width : 100
		}, {
			field : 'phone',
			title : '客户联系方式',
			align : 'center',
			width : 100
		}, {
			field : 'deliMode',
			title : '项目交付模式',
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
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		singleSelect : true,
		onClickRow : function(rowIndex, rowData) {
			copyProNum = rowData.proNum; //存储当前组织机构代码
			copyProName = rowData.proName;
			//单击一行时，根据项目编号查询项目成员信息、项目源码信息和项目文档信息，然后重新加载列表
			if (!loadSign) {
				$('#datagridOfProMember').datagrid(proMemberTable);
				$('#tb_proMember').show();
				$('#datagridOfProFileInfo').treegrid(proFileInfoTable);
				$('#tb_proFileInfo').show();
				$('#datagridOfProSubInfo').datagrid(proSubInfoTable);
				$('#tb_proSubInfo').show();
				$('#datagridOfProSourceCode').datagrid(proSourceCodeTable);
				$('#tb_proSourceCode').show();
				$('#datagridOfProCustomerMember').datagrid(proCustomerMember);
				$('#tb_proCustomerMember').show();
				loadSign = true;
			}
			/*
			 * 根据项目编号proNum来查询项目成员信息、项目源码信息和项目文档信息
			 */
			$('#datagridOfProMember').datagrid('load', {
				proNum : copyProNum
			});
			$('#datagridOfProFileInfo').treegrid('load', {
				proNum : copyProNum
			});
			$('#datagridOfProSourceCode').datagrid('load', {
				proNum : copyProNum
			});
			$('#datagridOfProSubInfo').datagrid('load', {
				proNum : copyProNum
			});
			$('#datagridOfProCustomerMember').datagrid('load', {
				proNum : copyProNum
			});

		},
		queryParams : $("#schForm").formToJson(), //查询框里的参数，将表单数据转换成json
		toolbar : "#tb_proInfo" //功能按钮
	};



	//初始化页面时直接加载表格数据，数据列表的ID
	$('#datagridOfProInfo').datagrid(proInfoTable);
	//查询按钮
	$(".scbtn").click(function() {
		$('#datagridOfProInfo').datagrid('load', $("#schForm").formToJson()); //重新加载，后边是参数
		copyProNum = "flase";
		$('#datagridOfProMember').datagrid('load', {
			proNum : copyProNum
		});
		$('#datagridOfProFileInfo').treegrid('load', {
			proNum : copyProNum
		});
		$('#datagridOfProSourceCode').datagrid('load', {
			proNum : copyProNum
		});
		$('#datagridOfProCustomerMember').datagrid('load', {
			proNum : copyProNum
		});

	});
	//新增按钮项目基本信息
	$("#btnAdd_proInfo").click(function() {
		//$('#addWin').dialog('open');	//打开对话框
		$('#addWin').show(); //打开对话框
		$('#class_required').hide(); //打开对话框
	//location.href = path + "/proInfo/add.jsp";	
	});

	//新增提交按钮(校验数据并提交表单) 项目基本信息
	$("#btnToAdd").click(function() {
		if ($("#addForm").form('validate')) {
			$.ajax({
				type : 'post',
				url : path + '/proInfo/add.json',
				data : $("#addForm").serialize(), //序列化一组表单元素，将表单内容编码为用于提交的字符串
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						//$('#datagridOfProInfo').datagrid(proInfoTable);
						//$('#addWin').dialog('close');
						$('#addWin').hide();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		} else {

			$.messager.alert('提醒',"添加失败！*为必填项！不能为空！");
			return false;
		}
	});

	//编辑按钮
	$("#btnEdit_proInfo").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#editWin').show(); //打开对话框
			$('#class_required').hide(); //打开对话框
			//把该条信息回显在编辑窗口中
			$('#editForm').find('input[name="proId"]').val(arr[0].proId);
			$('#editForm').find('input[name="proNum"]').val(arr[0].proNum);
			$('#editForm').find('input[name="proName"]').val(arr[0].proName);
			$('#editForm').find('input[name="proPactNum"]').val(arr[0].proPactNum);
			$('#editForm').find('input[name="createTime"]').val(arr[0].createTime);
			$('#editForm').find('input[name="createOper"]').val(arr[0].createOper);
			$('#proIndu3').combobox('setValue', arr[0].proIndu);
			$('#proProductLine3').combobox('setValue', arr[0].proProductLine);
			$('#currentStage3').combobox('setValue', arr[0].currentStage);
			$('#deliMode3').combobox('setValue', arr[0].deliMode);

			$('#startTime').datebox("setValue", arr[0].startTime);
			$('#finishTime').datebox("setValue", arr[0].finishTime);
			$('#finishTime2').datebox("setValue", arr[0].finishTime2);

			$('#editForm').find('input[name="custName"]').val(arr[0].custName);
			$('#editForm').find('input[name="custConnect"]').val(arr[0].custConnect);
			//$('#editForm').find('input[name="custName"]').val(arr[0].custName);
			$('#editForm').find('input[name="phone"]').val(arr[0].phone);
			$('#editForm').find('textarea[name="comment"]').val(arr[0].comment);
			$('#editForm').find('textarea[name="proDis"]').val(arr[0].proDis);

			//然后打开窗口			 			 
			$('#editWin').dialog('open');
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
				url : path + '/proInfo/update.json',
				data : $("#editForm").serialize(),
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						//$('#datagridOfProInfo').datagrid(proInfoTable);
						$('#editWin').hide();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		} else {
			$.messager.alert('提醒',"更新失败！")
			return false;
		}
	});

	$('#return').click(function() {
		$('#watchWin').hide();
		$('#class_required').show();
	});

	$('#btnWatch_proInfo').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			var proProductLine_name;
			var proIndu_name;
			var currentStage_name;
			var proIndu_name;
			var param = {};
			param.id = arr[0].proProductLine;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					proProductLine_name = data.dicName;
				}
			});
			param.id = arr[0].proIndu;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					proIndu_name = data.dicName;
				}
			});
			param.id = arr[0].currentStage;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					currentStage_name = data.dicName;
				}
			});
			$('#watchWin').show(); //打开对话框
			$('#class_required').hide(); //打开对话框
			//把该条信息回显在编辑窗口中
			$('#proNum_watch').text(arr[0].proNum);
			$('#proPactNum_watch').text(arr[0].proPactNum);
			$('#proName_watch').text(arr[0].proName);

			$('#proIndu_watch').text(proIndu_name);
			$('#proProductLine_watch').text(proProductLine_name);
			$('#proDis_watch').text(arr[0].proDis);
			$('#startTime_watch').text(arr[0].startTime);

			$('#finishTime2_watch').text(arr[0].finishTime2);
			$('#finishTime_watch').text(arr[0].finishTime);
			$('#custName_watch').text(arr[0].custName);
			$('#custConnect_watch').text(arr[0].custConnect);
			$('#currentStage_watch').text(currentStage_name);
			$('#phone_watch').text(arr[0].phone);
			$('#deliMode_watch').text(arr[0].deliMode);
			$('#comment_watch').text(arr[0].comment);
			//然后打开窗口			 			 
			$('#watchWin').dialog('open');
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');
		}
	});

	//删除按钮，项目源码信息
	$("#btnDelete_proInfo").click(function() {
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var proIdArr = 'proIdArr=';
					for (var i in arr) {
						proIdArr += (i == 0 ? '' : '#') + arr[i].proId;
					}
					$.ajax({
						type : 'post',
						url : path + '/proInfo/delete.json',
						data : proIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfProInfo').datagrid(proInfoTable);
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


	//项目成员信息
	var proMemberTable = {
		url : path + '/proMember/listByProNum.json',
		columns : [ [ {
			field : 'memberId',
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
			field : 'memberName',
			title : '姓名',
			align : 'center',
			width : 100
		}, {
			field : 'roleaName',
			title : '角色',
			align : 'center',
			width : 100,
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
			field : 'phone',
			title : '手机号码',
			align : 'center',
			width : 80
		}, {
			field : 'email',
			title : '邮件',
			align : 'center',
			width : 120
		}, {
			field : 'delyModule',
			title : '负责模块',
			align : 'center',
			width : 120
		}, {
			field : 'comment',
			title : '备注',
			align : 'center',
			width : 30,
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		toolbar : "#tb_proMember" //功能按钮
	};

	//新增按钮，项目成员信息
	$("#btnAdd_proMember").click(function() {
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#addForm_proMember').find('input[name="proNum"]').val(arr[0].proNum);
			$('#addForm_proMember').find('input[name="proName"]').val(copyProName);
			//$('#addWin_proMember').dialog('open');	//打开对话框
			$('#addWin_proMember').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据');
		} else {
			$.messager.alert('提醒', '请选择关联的项目');
		}

	});
	//新增提交按钮，项目成员信息
	$("#btnToAdd_proMember").click(function() {
		if ($("#addForm_proMember").form('validate')) {
			$.ajax({
				type : 'post',
				url : path + '/proMember/add.json',
				data : $("#addForm_proMember").serialize(), //序列化一组表单元素，将表单内容编码为用于提交的字符串
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						//$('#addWin_proMember').dialog('close');
						$('#datagridOfProMember').datagrid('load', {
							proNum : copyProNum
						});
						//$('#addForm_proMember').form('clear');
						$('#addWin_proMember').hiden();
						$('#class_required').show();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		} else {
			$.messager.alert('提醒',"添加成员信息失败！*都是必填项！");
			return false;
		}
	});


	//编辑按钮，项目成员信息
	$("#btnEdit_proMember").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProMember').datagrid("getSelections");

		if (arr.length == 1) {
			//把该条信息回显在编辑窗口中
			$('#editForm_proMember').find('input[name="memberId"]').val(arr[0].memberId);
			$('#editForm_proMember').find('input[name="proNum"]').val(arr[0].proNum);
			$('#editForm_proMember').find('input[name="createTime"]').val(arr[0].createTime);
			$('#editForm_proMember').find('input[name="createOper"]').val(arr[0].createOper);

			$('#editForm_proMember').find('input[name="memberName"]').val(arr[0].memberName);
			$('#roleaName3').combobox('setValue', arr[0].roleaName);
			$('#editForm_proMember').find('input[name="phone"]').val(arr[0].phone);
			$('#editForm_proMember').find('input[name="email"]').val(arr[0].email);
			$('#editForm_proMember').find('input[name="delyModule"]').val(arr[0].delyModule);
			//$('#editForm_proMember').find('input[name="comment"]').val(arr[0].comment);
			$('#editForm_proMember').find('textarea[name="comment"]').val(arr[0].comment);

			//然后打开窗口			 			 
			// $('#editWin_proMember').dialog('open');
			$('#editWin_proMember').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要编辑的数据');
		}
	});
	//更新提交，项目成员信息
	$("#btnToUpdate_proMember").click(function() {
		if ($("#editForm_proMember").form('validate')) {
			$.ajax({
				type : 'post',
				url : path + '/proMember/update.json',
				data : $("#editForm_proMember").serialize(),
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						//$('#editWin_proMember').dialog('close');
						$('#datagridOfProMember').datagrid('load', {
							proNum : copyProNum
						});
						//$('#editWin_proMember').form('clear');
						$('#editWin_proMember').hide();
						$('#class_required').show();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		} else {
			$.messager.alert("提醒","更新成员信息失败！*项都为必填项！");
			return false;
		}
	});

	//删除按钮，项目成员信息
	$("#btnDelete_proMember").click(function() {
		var arr = $('#datagridOfProMember').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var proMemberIdArr = 'proMemberIdArr=';
					for (var i in arr) {
						proMemberIdArr += (i == 0 ? '' : '#') + arr[i].memberId;
					}
					$.ajax({
						type : 'post',
						url : path + '/proMember/delete.json',
						data : proMemberIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfProMember').datagrid('load', {
									proNum : copyProNum
								});
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

	$('#btnWatch_proMember').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProMember').datagrid("getSelections");
		if (arr.length == 1) {
			var param = {};
			var roleaName_name;
			param.id = arr[0].roleaName;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					roleaName_name = data.dicName;
				}
			});
			//把该条信息回显在编辑窗口中
			$('#proNum_watch_proMember').text(arr[0].proNum);
			$('#memberName_watch_proMember').text(arr[0].memberName);

			$('#roleaName_watch_proMember').text(roleaName_name);
			$('#phone_watch_proMember').text(arr[0].phone);
			$('#email_watch_proMember').text(arr[0].email);
			$('#delyModule_watch_proMember').text(arr[0].delyModule);
			$('#comment_watch_proMember').text(arr[0].comment);

			//然后打开窗口			 			 
			// $('#watchWin_proMember').dialog('open');
			$('#watchWin_proMember').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');
		}
	});


	//项目文档信息表
	/*var proFileInfoTable = {
		url : path + '/proFileInfo/listByProNum.json',
		columns : [ [ {
			field : 'fileId',
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
			field : 'proStage',
			title : '项目阶段',
			align : 'center',
			width : 80,
			formatter : function(value, row, index) {
				var name;
				if (value != null) {
					var param ={};
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
			field : 'fileName',
			title : '文档名称',
			align : 'center',
			width : 120
		}, {
			field : 'createTime',
			title : '创建时间',
			align : 'center',
			width : 120
		}, {
			field : 'createOper',
			title : '创建人',
			align : 'center',
			width : 80,
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		toolbar : "#tb_proFileInfo" //功能按钮
	};
*/

	//新增按钮，项目文档信息表
	/*$("#btnAdd_proFileInfo").click(function() {
		//var arr = $('#datagridOfProInfo').datagrid("getSelections");
		var arr = $('#datagridOfProFileInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#addForm_proFileInfo').find('input[name="proNum"]').val(arr[0].proNum);
			$('#addForm_proFileInfo').find('input[name="proName"]').val(copyProName);
			$('#addForm_proFileInfo').find('input[name="proStage"]').val(arr[0].proStage);
			attachAddress = arr[0].filePath; //缓存文件在数据库的路径
			if (!attachAddress) {
				$('#proFileInfo_filePath').hide();
			}else{
				var file = new Array();
				file = attachAddress.split("/");
				$('#proFileInfo_filePath').text(file[file.length - 1]);
				$('#proFileInfo_filePath').show();
			}
			//$('#addWin_proFileInfo').dialog('open');	//打开对话框
			$('#addWin_proFileInfo').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据');
		} else {
			$.messager.alert('提醒', '请选择要上传的阶段');
		}
	});*/

	//上传附件
	/*$("#btnAdd_proAttachment").click(function(){
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if(arr.length==1){
			$("#addWin_proAttachment").show();
			$('#class_required').hide();
		}
	});*/

   proFileInfoTable = {
		idField : 'fileId',
		treeField : 'fileName',
		url : path + '/proFile/listByProNum.json?parentId=' + parentId,
		columns : [ [ {
			field : 'proNum',
			hidden : true
		}, {
			field : 'address',
			hidden : true
		}, {
			field : 'fileName',
			title : '文件',
			align : 'left',
			width : 180,
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
				if (name == null) {
					name = value;
					return name;
				}
				return '<input type="checkbox" id="' + row.fileId + '"/>' + name;
			}
		}, {
			field : 'submitOper',
			title : '提交者',
			align : 'center',
			width : 180
		}, {
			field : 'submitTime',
			title : '上传时间',
			align : 'center',
			width : 180
		}, {
			field : 'fileId',
			align : 'center',
			title : '操作',
			width : 150,
			formatter : function(value, row, index) {
				var fileName = row.fileName;
				var filePath = row.address;
				if (row.parentId == 0) {
					return null;
				} else {
					var a = '<input type="text" onclick="dl(' + value + ')" value="下载" style="background:skyblue;text-align:center;margin-right: 8px;width: 50px;"/>';
					var b = '<input type="text" onclick="readOnline(\'' + filePath + '\',\'' + fileName + '\')" value="预览" style="background:skyblue;text-align:center;margin-right:8px;width: 50px;"/>';
					var c = '<input type="text" onclick="deleteF(' + value + ')" value="删除" style="background:skyblue;text-align:center;margin-right: 8px;width: 50px;"/>';
					return b + a + c;
				}
			}
		}
		] ],
		onBeforeExpand : function(row) {
			//节点展开前进行查询的url
			var id = row.fileId;
			loadSign = false;
			$(this).treegrid('options').url = path + '/proFile/listByProNum.json?parentId=' + id;
		},
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		toolbar : "#tb_proFileInfo"
	};






	//上传按钮，项目文档信息表
	$("#btnEdit_proFileInfo").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProFileInfo').datagrid("getSelections");
		if (arr[0].parentId == 0) {
			if (arr.length == 1) {
				//把该条信息回显在编辑窗口中
				findStage();
				$('#editWin_proFileInfo').show();
				$('#class_required').hide();
			} else if (arr.length > 1) {
				$.messager.alert('提醒', '请选择单条数据记录');
			} else {
				$.messager.alert('提醒', '请选择要编辑的数据');
			}
		} else {
			$.messager.alert('提醒', "请选择相应的项目阶段,再进行上传操作！");
		}

	});

	//为上传项目文档过程添加遮罩层
	$("#upload").click(function(){
		$("#zhezhao").css("display","block");
		$.messager.alert('提醒', "上传中，请勿进行其他操作......");
	});

	//下载项目信息文档信息文件
	$('#btnWatch_proFileInfo').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProFileInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#tg').datagrid({
				title : '查看项目文档信息',
				url : path + '/proFileInfo/listByStage.json?proStage=' + arr[0].proStage + '&proNum=' + arr[0].proNum,
				width : 740,
				height : 250,
				collapsible : true, //是否可以折叠
				iconCls : 'icon-ok',
				rownumbers : true,
				idField : 'id',
				showFooter : true,
				columns : [ [
					{
						field : 'fileName',
						title : '文件',
						width : 260,
						align : 'center'
					},
					{
						field : 'createOper',
						title : '创建人',
						width : 120,
						align : 'center'
					},
					{
						field : 'attachAddress',
						title : '下载',
						width : 116,
						align : 'center',
						formatter : function(value, row, index) {
							return "<input type='button' value='下载' onclick='download(\"" + value + "\")' />";
						}
					}
				] ],
				pagination : true, // 显示分页
				pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
				pageSize : 10, // 默认显示记录数
				fitColumns : true // 自适应水平，防止出现水平滚动条
			});
			$('#watchWin_proFileInfo').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');
		}
	});

	//删除按钮，项目文档信息表
	$("#btnDelete_proFileInfo").click(function() {
		var arr = $('#datagridOfProFileInfo').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var proFileInfoIdArr = 'proFileInfoIdArr=';
					for (var i in arr) {
						proFileInfoIdArr += (i == 0 ? '' : '#') + arr[i].fileId;
					}
					$.ajax({
						type : 'post',
						url : path + '/proFileInfo/delete.json',
						data : proFileInfoIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfProFileInfo').datagrid('load', {
									proNum : copyProNum
								});
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

	//删除项目文档附件
	$('#attachAddress').click(function() {
		deleteFile_proFileInfo(attachAddress, "attachAddress");
	});


	//项目源码信息表
	var proSourceCodeTable = {
		url : path + '/proSourceCode/listByProNum.json',
		columns : [ [ {
			field : 'fileId',
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
			field : 'codeName',
			title : '源码名称',
			align : 'center',
			width : 100
		}, {
			field : 'codeType',
			title : '代码类型',
			align : 'center',
			width : 80,
			formatter : function(value, row, index) {
				var codeType;
				var param = {};
				param.id = value;
				if (value != null) {
					$.ajax({
						type : 'post',
						url : path + "/dictionary/load",
						async : false,
						data : param,
						success : function(data) {
							codeType = data.dicName;
						}
					});
				}
				return codeType;
			}
		}, {
			field : 'codeVersion',
			title : '源码版本',
			align : 'center',
			width : 80
		}, {
			field : 'codeComment',
			title : '代码描述',
			align : 'center',
			width : 150
		}, {
			field : 'author',
			title : '作者',
			align : 'center',
			width : 80,
			formatter : function(value, row, index) {
				var memberName;
				var param = {};
				param.memberId = value;
				if (value != null) {
					$.ajax({
						type : 'post',
						url : path + "/proMember/queryById.json",
						async : false,
						data : param,
						success : function(data) {
							memberName = data.memberName;
						}
					});
				}
				return memberName;
			}
		}, {
			field : 'comment',
			title : '备注',
			align : 'center',
			width : 150
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		toolbar : "#tb_proSourceCode" //功能按钮
	};


	//新增按钮，项目源码信息
	$("#btnAdd_proSourceCode").click(function() {
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			var param = {};
			param.proNum = copyProNum;
			var data = new Array();
			$.ajax({
				type : 'post',
				url : path + '/proMember/queryMember.json',
				data : param,
				success : function(result) {
					var length = result.length;
					for (var i = 0; i < length; i++) {
						data.push({
							"text" : result[i].memberName,
							"id" : result[i].memberId
						});
					}
					$("#author1").combobox("loadData", data);
				}
			});
			$('#addForm_proSourceCode').find('input[name="proNum"]').val(copyProNum);
			$('#addForm_proSourceCode').find('input[name="proName"]').val(copyProName);
			//$('#addWin_proSourceCode').dialog('open');	//打开对话框
			$('#addWin_proSourceCode').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据');
		} else {
			$.messager.alert('提醒', '请选择关联的项目');
		}
	});

	//编辑按钮，项目源码信息
	$("#btnEdit_proSourceCode").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProSourceCode').datagrid("getSelections");
		if (arr.length == 1) {
			var param = {};
			param.proNum = arr[0].proNum;
			var data = new Array();
			$.ajax({
				type : 'post',
				url : path + '/proMember/queryMember.json',
				data : param,
				success : function(result) {
					var length = result.length;
					for (var i = 0; i < length; i++) {
						data.push({
							"text" : result[i].memberName,
							"id" : result[i].memberId
						});
					}
					$("#author2").combobox("loadData", data);
				}
			});
			//把该条信息回显在编辑窗口中
			$('#editForm_proSourceCode').find('input[name="fileId"]').val(arr[0].fileId);
			$('#editForm_proSourceCode').find('input[name="submittime"]').val(arr[0].submittime);
			$('#editForm_proSourceCode').find('input[name="submitOper"]').val(arr[0].submitOper);

			$('#editForm_proSourceCode').find('input[name="proNum"]').val(arr[0].proNum);
			$('#editForm_proSourceCode').find('input[name="codeNum"]').val(arr[0].codeNum);
			$('#editForm_proSourceCode').find('input[name="codeName"]').val(arr[0].codeName);

			$('#codeType3').combobox("setValue", arr[0].codeType);
			$('#editForm_proSourceCode').find('input[name="codeVersion"]').val(arr[0].codeVersion);
			//$('#editForm_proSourceCode').find('input[name="codeComment"]').val(arr[0].codeComment);
			$('#editForm_proSourceCode').find('input[name="author"]').val(arr[0].author);
			$('#editForm_proSourceCode').find('textarea[name="codeComment"]').val(arr[0].codeComment);
			$('#editForm_proSourceCode').find('textarea[name="comment"]').val(arr[0].comment);

			proSourceCode_id = arr[0].fileId;

			codeDiscFile = arr[0].codeDiscFile; //缓存文件在数据库的路径
			var file = new Array();
			if(codeDiscFile!=null){
				file = codeDiscFile.split("/");
			}
			$('#codeDiscFile_edit').text(file[file.length - 1]);
			if (!codeDiscFile) {
				$('#codeDiscFile').hide();
			}

			//然后打开窗口			 			 
			//$('#editWin_proSourceCode').dialog('open');
			$('#editWin_proSourceCode').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要编辑的数据');
		}
	});
	//查看源码信息
	$('#btnWatch_proSourceCode').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProSourceCode').datagrid("getSelections");
		if (arr.length == 1) {
			var memberId = arr[0].author;
			var memberName;
			var param = {};
			param.memberId = memberId;
			$.ajax({
				type : 'post',
				url : path + "/proMember/queryById.json",
				async : false,
				data : param,
				success : function(data) {
					memberName = data.memberName;
				}
			});

			var codeType_name;
			var param = {};
			param.id = arr[0].codeType;
			$.ajax({
				type : 'post',
				url : path + "/dictionary/load",
				async : false,
				data : param,
				success : function(data) {
					codeType_name = data.dicName;
				}
			});

			//把该条信息回显在编辑窗口中
			$('#proNum_watch_proSourceCode').text(arr[0].proNum);
			$('#codeName_watch_proSourceCode').text(arr[0].codeNum);

			$('#codeType_watch_proSourceCode').text(codeType_name);
			$('#codeVersion_watch_proSourceCode').text(arr[0].codeVersion);
			$('#codeComment_watch_proSourceCode').text(arr[0].codeComment);
			$('#author_watch_proSourceCode').text(memberName);
			$('#comment_watch_proSourceCode').text(arr[0].comment);

			codeDiscFile = arr[0].codeDiscFile; //缓存文件在数据库的路径
			proSourceCode_id = arr[0].fileId;
			proSourceName = arr[0].fileName;
			var file = new Array();
			if(codeDiscFile!=null){
				file = codeDiscFile.split("/");
			}
			$('#codeDiscFile_watch').text(file[file.length - 1]);
			if (!codeDiscFile) {
				$('#codeDiscFileDown').hide();
				$('#codeDiscFileRead').hide();
			}

			//然后打开窗口			 			 
			//$('#watchWin_proSourceCode').dialog('open');
			$('#watchWin_proSourceCode').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');

		}
	});

	//删除按钮，项目源码信息
	$("#btnDelete_proSourceCode").click(function() {
		var arr = $('#datagridOfProSourceCode').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var proSourceCodeIdArr = 'proSourceCodeIdArr=';
					for (var i in arr) {
						proSourceCodeIdArr += (i == 0 ? '' : '#') + arr[i].fileId;
					}
					$.ajax({
						type : 'post',
						url : path + '/proSourceCode/delete.json',
						data : proSourceCodeIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfProSourceCode').datagrid('load', {
									proNum : copyProNum
								});
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

	//删除源码附件
	$('#codeDiscFile').click(function() {
		deleteFile_proSourceCode(codeDiscFile, "codeDiscFile");
	});


	//<!-- ==================================项目子合同信息================================= -->
	//项目子合同信息表
	var proSubInfoTable = {
		url : path + '/proSubInfo/listByProNum.json',
		columns : [ [ {
			field : 'subId',
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
			field : 'proSubName',
			title : '子合同名称',
			align : 'center',
			width : 80,
		}, {
			field : 'proSubNum',
			title : '子合同编号',
			align : 'center',
			width : 120
		}, {
			field : 'signTime',
			title : '签约时间',
			align : 'center',
			width : 120
		}, {
			field : 'proSubDis',
			title : '子合同描述',
			align : 'center',
			width : 80,
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		toolbar : "#tb_proSubInfo" //功能按钮
	};


	//新增按钮，项目子合同信息表
	$("#btnAdd_proSubInfo").click(function() {
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#addForm_proSubInfo').find('input[name="proNum"]').val(arr[0].proNum);
			$('#addForm_proSubInfo').find('input[name="proName"]').val(copyProName);
			$('#addWin_proSubInfo').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据');
		} else {
			$.messager.alert('提醒', '请选择关联的项目');
		}
	});

	//编辑按钮，项目子合同信息表
	$("#btnEdit_proSubInfo").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProSubInfo').datagrid("getSelections");
		if (arr.length == 1) {
			//把该条信息回显在编辑窗口中
			$('#editForm_proSubInfo').find('input[name="subId"]').val(arr[0].subId);
			$('#editForm_proSubInfo').find('input[name="proNum"]').val(arr[0].proNum);
			$('#editForm_proSubInfo').find('input[name="proSubName"]').val(arr[0].proSubName);
			$('#editForm_proSubInfo').find('input[name="proSubNum"]').val(arr[0].proSubNum);
			$('#editForm_proSubInfo').find('input[name="proName"]').val(arr[0].proName);
			$('#editForm_proSubInfo').find('textarea[name="proSubDis"]').val(arr[0].proSubDis);
			$('#signTime1').datebox("setValue", arr[0].signTime);
			proSubInfo_id = arr[0].subId;
			attachAddress = arr[0].filePath; //缓存文件在数据库的路径
			if (!attachAddress) {
				$('#proSubInfo_filePath').hide();
				$('#proSubInfo_filePath_del').hide();
			} else {
				var file = new Array();
				file = attachAddress.split("/");
				$('#proSubInfo_filePath').text(file[file.length - 1]);
				$('#proSubInfo_filePath').show();
				$('#proSubInfo_filePath_del').show();
			}
			//然后打开窗口			 			 
			$('#editWin_proSubInfo').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要编辑的数据');
		}
	});

	$('#btnWatch_proSubInfo').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProSubInfo').datagrid("getSelections");
		if (arr.length == 1) {
			//把该条信息回显在编辑窗口中

			$('#proNum_watch_proSubInfo').text(arr[0].proNum);
			$('#proName_watch_proSubInfo').text(arr[0].proName);
			$('#proSubNum_watch_proSubInfo').text(arr[0].proSubNum);
			$('#proSubName_watch_proSubInfo').text(arr[0].proSubName);
			$('#signTime_watch_proSubInfo').text(arr[0].signTime);
			$('#proSubDis_watch_proSubInfo').text(arr[0].proSubDis);


			proSubInfo_id = arr[0].subId;
			attachAddress = arr[0].filePath; //缓存文件在数据库的路径
			if (!attachAddress) {
				$('#proSubInfo_filePath_Watch').hide();
				$('#proSubInfo_filePath_Down').hide();
			} else {
				var file = new Array();
				file = attachAddress.split("/");
				$('#proSubInfo_filePath_Watch').text(file[file.length - 1]);
				$('#proSubInfo_filePath_Watch').show();
				$('#proSubInfo_filePath_Down').show();
			}

			//然后打开窗口			 			 
			$('#watchWin_proSubInfo').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');
		}
	});

	//删除按钮，项目子合同信息表
	$("#btnDelete_proSubInfo").click(function() {
		var arr = $('#datagridOfProSubInfo').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var proSubInfoIdArr = 'proSubInfoIdArr=';
					for (var i in arr) {
						proSubInfoIdArr += (i == 0 ? '' : '#') + arr[i].subId;
					}
					$.ajax({
						type : 'post',
						url : path + '/proSubInfo/delete.json',
						data : proSubInfoIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfProSubInfo').datagrid('load', {
									proNum : copyProNum
								});
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

	//删除项目子合同附件
	$('#proSubInfo_filePath_del').click(function() {
		deleteFile_proSubInfo(proSubInfo_id);
	});

	//<!-- ==================================项目客户信息================================= -->
	var proCustomerMember = {
		url : path + '/proCustomerMember/listByProNum.json',
		columns : [ [ {
			field : 'customerId',
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
			field : 'cusName',
			title : '客户名字',
			align : 'center',
			width : 50
		}, {
			field : 'cusRole',
			title : '角色',
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
			field : 'phone',
			title : '联系电话',
			align : 'center',
			width : 150
		}, {
			field : 'email',
			title : '邮箱',
			align : 'center',
			width : 150
		}, {
			field : 'responsibleArea',
			title : '责任领域',
			align : 'center',
			width : 120,
		}, {
			field : 'delyModule',
			title : '工作描述',
			align : 'center',
			width : 200
		}
		] ],
		pagination : true, // 显示分页
		pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
		pageSize : 10, // 默认显示记录数
		fitColumns : true, // 自适应水平，防止出现水平滚动条
		toolbar : "#tb_proCustomerMember" //功能按钮
	};


	//新增按钮，项目客户信息
	$("#btnAdd_proCustomerMember").click(function() {
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			var proNum = arr[0].proNum;
			$('#addForm_proCustomerMember').find('input[name="proNum"]').val(proNum);
			$('#addForm_proCustomerMember').find('input[name="proName"]').val(copyProName);
			//$('#addWin_proSourceCode').dialog('open');	//打开对话框
			$('#addWin_proCustomerMember').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据');
		} else {
			$.messager.alert('提醒', '请选择关联的项目');
		}
	});





	//编辑按钮，项目客户信息
	$("#btnEdit_proCustomerMember").click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProCustomerMember').datagrid("getSelections");
		if (arr.length == 1) {
			//把该条信息回显在编辑窗口中
			$('#editForm_proCustomerMember').find('input[name="customerId"]').val(arr[0].customerId);
			$('#editForm_proCustomerMember').find('input[name="proNum"]').val(arr[0].proNum);
			$('#editForm_proCustomerMember').find('input[name="cusName"]').val(arr[0].cusName);
			$("#cusRole_cus2").combobox('setValue', arr[0].cusRole);
			$('#editForm_proCustomerMember').find('input[name="phone"]').val(arr[0].phone);
			$('#editForm_proCustomerMember').find('input[name="email"]').val(arr[0].email);
			$('#editForm_proCustomerMember').find('input[name="responsibleArea"]').val(arr[0].responsibleArea);
			//$('#editForm_proCustomerMember').find('input[name="delyModule"]').val(arr[0].delyModule);
			$("#delyModule").val(arr[0].delyModule)
			$('#editWin_proCustomerMember').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要编辑的数据');
		}
	});
	//编辑窗口提交更新按钮
	$("#btnToUpdate_proCustomerMember").click(function() {
		if ($("#editForm_proCustomerMember").form('validate')) {
			$.ajax({
				type : 'post',
				url : path + '/proCustomerMember/update.json',
				data : $("#editForm_proCustomerMember").serialize(),
				success : function(data) {
					if (data.result == true) {
						$.messager.alert('提醒', data.msg);
						//$('#editWin_proMember').dialog('close');
						$('#datagridOfProCustomerMember').datagrid('load', {
							proNum : copyProNum
						});
						//$('#editWin_proMember').form('clear');
						$('#editWin_proMember').hide();
						$('#class_required').show();
					} else {
						$.messager.alert('提醒', data.msg);
					}
				}
			});
		} else {
			$.messager.alert('提醒',"更新失败！");
		}
	});

	//查看项目客户信息
	$('#btnWatch_proCustomerMember').click(function() {
		//取得选中的数据
		var arr = $('#datagridOfProCustomerMember').datagrid("getSelections");
		if (arr.length == 1) {
			//var customerId = arr[0].customerId;
			//把该条信息回显在编辑窗口中
			$('#proNum_watch_proCustomerMember').text(arr[0].proNum);
			$('#cusName_watch_proCustomerMember').text(arr[0].cusName);
			$('#cusRole_watch_proCustomerMember').text(arr[0].cusRole);
			$('#phone_watch_proCustomerMember').text(arr[0].phone);
			$('#email_watch_proCustomerMember').text(arr[0].email);
			$('#responsibleArea_watch_proCustomerMember').text(arr[0].responsibleArea);
			$('#delyModule_watch_proSourceCode').text(arr[0].delyModule);
			$('#watchWin_proCustomerMember').show();
			$('#class_required').hide();
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条数据记录');
		} else {
			$.messager.alert('提醒', '请选择要查看的数据');
		}
	});

	//删除按钮，项目客户信息
	$("#btnDelete_proCustomerMember").click(function() {
		var arr = $('#datagridOfProCustomerMember').datagrid("getSelections");
		if (arr.length > 0) {
			$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
				if (r) {
					var proCustomerMemberIdArr = 'proCustomerMemberIdArr=';
					for (var i in arr) {
						proCustomerMemberIdArr += (i == 0 ? '' : '#') + arr[i].customerId;
					}
					$.ajax({
						type : 'post',
						url : path + '/proCustomerMember/delete.json',
						data : proCustomerMemberIdArr,
						success : function(data) {
							if (data.result == true) {
								$.messager.alert('提醒', data.msg);
								$('#datagridOfProCustomerMember').datagrid('load', {
									proNum : copyProNum
								});
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


	//授权按钮
	$("#btnGrant").click(function() {
		var arr = $('#datagridOfProInfo').datagrid("getSelections");
		if (arr.length == 1) {
			$('#unselect').empty();
			$('#select').empty();
			$('#deptselect').empty();

			$.get(ctx + '/mgr/userPro/'+arr[0].proId+'/deptselect.json', function(result) {
				if (result.data[0].length > 0) {
					for (var i in result.data[0]) {
						$('#deptselect').append('<option value="' + result.data[0][i].departId
							+ '" onclick="getUserList(' + result.data[0][i].departId + ')">' + result.data[0][i].dicName + '</option>');
					}

					for (var i in result.data[1]) {
						$('#select').append('<option value="' + result.data[1][i].userId + '">' + result.data[1][i].userName + '</option>');
					}
				}
			});

			$('#grantForm').find('input[name="proId"]').val(arr[0].proId);
			$('#grantWin').dialog('open');
		} else if (arr.length > 1) {
			$.messager.alert('提醒', '请选择单条个项目进行授权');
		} else {
			$.messager.alert('提醒', '请选择要授权的项目');
		}
	});

	//提交后台添加项目授权的用户
	$("#btnToGrant").click(function() {
		var j = 0;
		var select = 'select=';
		var proId = 'proId=' + $('#grantForm').find('input[name="proId"]').val();

		$("#select option").each(function() {
			select += (j == 0 ? '' : '#') + $(this).val();
			j++;
		});

		$.ajax({
			type : 'post',
			url : ctx + "/mgr/userPro/grant.json",
			data : proId + '&' + select,
			success : function(data) {
				if (data.result == true) {
					$.messager.alert('提醒', data.msg);
					$('#grantWin').dialog('close');
					$('#datagridOfProInfo').datagrid(proInfoTable);
				} else {
					$.messager.alert('提醒', data.msg);
				}
			}
		});
	});

	//添加角色
	$(".tbbtn:first").click(function() {
		$('#select').append($("#unselect").find("option:selected"));
		$("#unselect").find("option:selected").remove();
	});

	//删除角色
	$(".tbbtn:last").click(function() {
		$('#unselect').append($("#select").find("option:selected"));
		$("#select").find("option:selected").remove();
	});




	/*
	 * 附件下载和预览
	 */
	$('#attachAddressDown').click(function() {
		location.href = path + "/proFileInfo/downloadFile?filePath=" + attachAddress;
	});
	$('#attachAddressRead').click(function() {
		location.href = path + "/readonline/read?filePath=" + attachAddress;
	});

	$('#codeDiscFileDown').click(function() {
		location.href = path + "/proSourceCode/downloadFile?filePath=" + codeDiscFile + "&fileId=" + proSourceCode_id;
	});
	$('#codeDiscFileRead').click(function() {
		//		location.href = path + "/readonline/read?filePath=" + codeDiscFile;
		var url = path + "/readonline/read?filePath=" + codeDiscFile;
		if (codeDiscFile) {
			addTab({
				text : proSourceName,
				url : url
			});
		} else {
			$.messager.alert('提醒', '请选择要浏览的数据');
		}
	});
	$('#proSubInfo_filePath_Down').click(function() {
		location.href = path + "/proSubInfo/downloadFile?filePath=" + attachAddress + "&subId=" + proSubInfo_id;
	});

	//加载数据字典
	$('#proProductLine').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0101'
	});

	$('#proProductLine2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0101'
	});

	$('#proProductLine3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0101'
	});

	$('#proIndu').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0102'
	});

	$('#proIndu2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0102'
	});

	$('#proIndu3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0102'
	});

	$('#currentStage2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0106'
	});

	$('#currentStage3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0106'
	});
	$('#proStage2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0106'
	});

	$('#proStage3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0106'
	});

	$('#deliMode2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0107'
	});

	$('#deliMode3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0107'
	});

	$('#roleaName2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0108'
	});

	$('#roleaName3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0108'
	});

	$('#codeType2').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0109'
	});

	$('#codeType3').combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0109'
	});
	$("#cusRole_cus").combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0103'
	});
	$("#cusRole_cus2").combobox({
		valueField : 'id',
		textField : 'text',
		panelMaxHeight : '100',
		panelHeight : 'auto',
		url : path + '/dictionary/combo?id=0103'
	});

});

//删除文件的方法 	项目源码信息
function deleteFile_proSourceCode(filePath, field) {
	var params = {};
	params.orgId = proFileInfo_id;
	params.filePath = filePath;
	params.field = field;
	$.ajax({
		type : 'post',
		url : path + '/proSourceCode/deleteFile',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$('#editWin_proSourceCode').dialog('close');
				$('#datagridOfProSourceCode').datagrid('load', {
					proNum : copyProNum
				});
				$('#editWin_proSourceCode').form('clear');
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}
//删除文件的方法 	项目文档信息
function deleteFile_proFileInfo(filePath, field) {
	var params = {};
	params.orgId = proSourceCode_id;
	params.filePath = filePath;
	params.field = field;
	$.ajax({
		type : 'post',
		url : path + '/proFileInfo/deleteFile',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$('#editWin_proFileInfo').dialog('close');
				$('#datagridOfProFileInfo').datagrid('load', {
					proNum : copyProNum
				});
				$('#editWin_proFileInfo').form('clear');
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}

//删除文件的方法 	项目文档信息
function deleteFile_proSubInfo(subId) {
	var params = {};
	params.subId = subId;
	$.ajax({
		type : 'post',
		url : path + '/proSubInfo/deleteFile',
		data : params,
		success : function(data) {
			if (data.result == true) {
				$.messager.alert('提醒', data.msg);
				$('#editWin_proSubInfo').dialog('close');
				$('#datagridOfProSubInfo').datagrid('load', {
					proNum : copyProNum
				});
				$('#editWin_proSubInfo').form('clear');
			} else {
				$.messager.alert('提醒', data.msg);
			}
		}
	});
}

/*==项目文档信息文件下载===*/
function dl(fileId) {
	if (fileId != "null" && fileId != "undefined") {
		location.href = path + "/proFile/downloadFile?fileId=" + fileId;
	} else {
		$.messager.alert("下载失败！");
	}

}
/*==项目文档信息文件预览==*/
function readOnline(filePath, fileName) {
	if (filePath != null && filePath != "undefined") {
		/*var t = {};
		t.fileId=fileId;
		$.ajax({
			type : 'post',
			url : path + "/proFile/read" ,
			data : t,
			success : function(data) {
				alert("进来了2");
				if (data.result == true) {
					var text='预览页面';
					var url= path +'/readonline';
					var pms_center_tabs = parent.pms_center_tabs;
					if (pms_center_tabs.tabs('exists', text)) {
						pms_center_tabs.tabs('select', text);
					} else {
						if (url.length > 0) {
							pms_center_tabs
									.tabs(
											'add',
											{
												tools : [ {
													iconCls : 'icon-mini-refresh',
													handler : function() {
													}
												} ],
												title : text,
												closable : true,
												content : '<iframe src="'
														+ url
														+ '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>'
											});

						}
					}
				} else {
					$.messager.alert('提醒', data.msg);
				}
			}
		});*/

		//		location.href = path + "/proFile/read?fileId=" + fileId;
		var url = path + "/readonline/read?filePath=" + filePath;
		addTab({
			text : fileName,
			url : url
		});
	} else {
		$.messager.alert('提醒',"请选择文件！");
	}
}

function addTab(Node) {
	var url = Node.url;
	var text = Node.text;
	var pms_center_tabs = parent.pms_center_tabs;
	if (pms_center_tabs.tabs('exists', text)) {
		pms_center_tabs.tabs('select', text);
	} else {
		if (url.length > 0) {

			pms_center_tabs
				.tabs(
					'add',
					{
						tools : [ {
							iconCls : 'icon-mini-refresh',
							handler : function() {
								//refreshTab(node.text);
							}
						} ],
						title : text,
						closable : true,
						//iconCls : node.iconCls,
						content : '<iframe id="centerIframe" src="'
							+ url
							+ '" scrolling="yes" frameborder="0" height="100%;" style="width:100%;"></iframe>'
					});

		}
	}
}

/*==项目文档信息文件删除==*/
function deleteF(fileId) {
	var params = {};
	params.fileId = fileId;
	$.ajax({
		type : 'post',
		url : path + '/proFile/delete',
		data : params,
		success : function(data) {
			if (data.result == true) {
				
				loadSign = false;
				$('#datagridOfProFileInfo').datagrid('load', {
					proNum : copyProNum
				});
				
				$.messager.alert('提醒', "文档成功删除");
			} else {
				$.messager.alert('提醒', "删除失败");
			}
		}
	});
}
//项目子合同新增提交验证
function proSub_submit() {
	if ($("#addForm_proSubInfo").form('validate')) {
		return true;
	} else {
		$.messager.alert('提醒',"添加失败！");
		return false;
	}

}
//项目子合同更新提交验证
function proSub_update() {
	if ($("#editForm_proSubInfo").form('validate')) {
		return true;
	} else {
		$.messager.alert('提醒',"更新失败！");
		return false;
	}
}
//项目源码新增提交验证
function proSource_submit() {
	if ($("#addForm_proSourceCode").form('validate')) {
		return true;
	} else {
		$.messager.alert('提醒',"提交失败！");
		return false;
	}
}
//项目源码更新提交验证
function proSource_update() {
	if ($("#editWin_proSourceCode").form('validate')) {
		return true;
	} else {
		$.messager.alert('提醒',"更新失败！");
		return false;
	}
}
//客户信息新增提交验证
function proCus_submit() {
	if ($("#addForm_proCustomerMember").form('validate') && $("#cusRole_cus").val() != null) {
		return true;
	} else {
		$.messager.alert("提醒","提交失败！");
		return false;
	}
}

function getUserList(departId) {
	var j = 0;
	var select = 'select=';
	$("#select option").each(function() {
		select += (j == 0 ? '' : '#') + $(this).val();
		j++;
	});

	var params = {};
	params.proId = $("#grantProId").val();
	params.departId = departId;
	params.select = select;
	$.ajax({
		type : 'post',
		url : path + '/userPro/select.json',
		data : params,
		async : false,
		success : function(data) {
			$('#unselect').empty();
			if (data[0].length > 0) {
				for (var i in data[0]) {
					$('#unselect').append('<option value="' + data[0][i].userId + '">' + data[0][i].userName + '</option>');

				}
			}
		}
	});

}

/*----查询项目阶段------*/
function findStage(){
	var arr = $('#datagridOfProFileInfo').datagrid("getSelections");
	
	
	/*--根据项目编号，查找项目名称---*/
	var param = {};
	var proName;
	param.proNum = arr[0].proNum;
	$.ajax({
		type : 'post',
		url : path + "/proInfo/queryNameByProNum.json",
		async : false,
		data : param,
		success : function(data) {
			proName = data.proName;

		}
	});
	
	
	var fileName;
	var param = {};
	param.id = arr[0].fileName;
	$.ajax({
		type : 'post',
		url : path + "/dictionary/load",
		async : false,
		data : param,
		success : function(data) {
			fileName = data.dicName;
		}
	});
	
	$('#editForm_proFileInfo').find('input[name="proNum"]').val(arr[0].proNum);
	$('#editForm_proFileInfo').find('input[name="fileId"]').val(arr[0].fileId);
	$('#editForm_proFileInfo').find('input[name="proName"]').val(proName);
	$('#editForm_proFileInfo').find('input[name="fileName"]').val(fileName);
	$('#editForm_proFileInfo').find('input[name="parentId"]').val(arr[0].parentId);
}



function return_main() {
	$('#addWin').hide();
	$('#editWin').hide();
	$('#watchWin').hide();
	$('#addWin_proMember').hide();
	$('#editWin_proMember').hide();
	$('#watchWin_proMember').hide();
	$('#addWin_proFileInfo').hide();
	$('#editWin_proFileInfo').hide();
	$('#watchWin_proFileInfo').hide();
	$('#addWin_proSubInfo').hide();
	$('#editWin_proSubInfo').hide();
	$('#watchWin_proSubInfo').hide();
	$('#addWin_proSourceCode').hide();
	$('#editWin_proSourceCode').hide();
	$('#watchWin_proSourceCode').hide();
	$('#addWin_proCustomerMember').hide();
	$('#watchWin_proCustomerMember').hide();
	$('#editWin_proCustomerMember').hide();
	$('#class_required').show();
}