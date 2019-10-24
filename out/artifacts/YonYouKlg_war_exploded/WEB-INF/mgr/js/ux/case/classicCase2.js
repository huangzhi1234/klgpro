var stdocId;
var fileAddress;
var userId;
$(function(){

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
		var stDocInfoTable = {
			url : ctx + '/mgr/classicCase/list.json',
			columns : [ [ {
				field : 'stdocId',
				width : 20,
				align : 'left',
				checkbox : true
			},{
				field : 'rowIndex',
				title : '序号',
				width : 20,
				align : 'center',
				formatter : function(value, row, index) {
					return index + 1;
				}
			}, {
				field : 'fileName',
				title : '文档名称',
				align : 'center',
				width :40
			},{
				field : 'author',
				title : '文档作者',
				align : 'center',
				width :40
			}, {
				field : 'proname',
				title : '项目名称',
				align : 'center',
				width :40
			}, {
				field : 'industry',
				title : '所属行业',
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
			},{
				field : 'createOper',
				title : '上传人',
				align : 'center',
				width : 40
			}, {
				field : 'createTime',
				title : '上传时间',
				align : 'center',
				width : 40
			}, {
				field : 'comment',
				hidden:true
			}
			] ],
			pagination : true, // 显示分页
			pageList : [ 10, 20, 50 ], // 每页可显示的条数，在表格左下角可选
			pageSize : 10, // 默认显示记录数
			fitColumns : true, // 自适应水平，防止出现水平滚动条
			/*singleSelect : true,*/
			onClickRow : function(rowIndex, rowData) {
				$('#watchWin2').show(); //打开对话框
				$('#fileDown2').html("下载");
				$('#fileDown2').attr('style','background:skyblue');
				
				//把该条信息回显在编辑窗口中
				$('#createOper_watch2').text(rowData.createOper);
				$('#createTime_watch2').text(rowData.createTime);
				var param = {};
				var proIndu_name;
				param.id = rowData.industry;
				$.ajax({
					type : 'post',
					url : path + "/dictionary/load",
					async : false,
					data : param,
					success : function(data) {
						proIndu_name = data.dicName;
					}
				});
				
				$('#proname2').text(rowData.proname);
				$('#industry2').text(proIndu_name);
				$('#comment').text(rowData.comment);
				$('#author2').text(rowData.author);
				stdocId = rowData.stdocId;
				fileAddress = rowData.fileAddress; //缓存文件在数据库的路径

				var file = new Array();
				file = fileAddress.split("/");
				$('#file_watch2').text("文件名：" + rowData.fileName);
				$('#file_watch2').show();
				$('#fileDown2').show();
				var file2 = file[file.length - 1].split(".");
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
			toolbar : "#tb_stDocInfo" //功能按钮
		};

		//初始化页面时直接加载表格数据，数据列表的ID
		$('#datagridOfstDocInfo').datagrid(stDocInfoTable);
		//查询按钮
		$("#scbtn").click(function() {
			$('#datagridOfstDocInfo').datagrid('load', $("#schForm").formToJson()); //重新加载，后边是参数
			$('#watchWin2').hide();
		});
		//新增按钮  	 项目基本信息
		$("#btnAdd_stDocInfo").click(function() {
			var dicNum = document.getElementById("dicNum").value;
			if (dicNum == "") {
				$.messager.alert("提醒","请选择相应节点才能进行新增操作");
				return;
			}
			$('#addWin').show(); //打开对话框
			$('#class_required').hide(); //打开对话框
			$('#watchWin2').hide();
		});
		$("#btnDelete_stDocInfo").click(function() {
			var arr = $('#datagridOfstDocInfo').datagrid("getSelections");
			if (arr.length > 0) {
				$.messager.confirm('确认', '您确认想要删除选中的记录吗？', function(r) {
					if (r) {
						var stdocIdArr = 'stdocIdArr=';
						for (var i in arr) {
							stdocIdArr += (i == 0 ? '' : '#') + arr[i].stdocId;
						}
						$.ajax({
							type : 'post',
							url : path + '/classicCase/delete.json',
							data : stdocIdArr,
							success : function(data) {
								if (data.result == true) {
									$.messager.alert('提醒', data.msg);
									$('#datagridOfstDocInfo').datagrid(stDocInfoTable);
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
			return_main();
		});
		$("#btnWatch_stDocInfo").click(function() {
			var arr = $('#datagridOfstDocInfo').datagrid("getSelections");
			if(arr.length==0){
				$.messager.alert('提醒', '请选择要浏览的数据');
			}else if(arr.length>1){
				$.messager.alert('提醒','请选择单行数据');
			}else{
				//查看是否被允许在线预览
				/*var param={};
				var isOk=0;
				param.stdocId=arr[0].stdocId;
				$.ajax({
					type : 'post',
					url : path + "/stdocDowncheck/getStateByPatId.json",
					async : false,
					data : param,
					success : function(data) {
						isOk = data.isOk;
					}
				});
				if(isOk==1){
					var fileName = arr[0].fileName;
					var url = path + "/readonline/read?filePath="+fileAddress;
					if (fileAddress) {
						addTab({text:fileName,url:url});
					} else {
						$.messager.alert('提醒', '请选择要浏览的数据');
					}
				}else{
					$.messager.alert("提醒","未经申请得到允许，不得预览文档信息");
				}*/
				
				var fileName = arr[0].fileName;
				var url = path + "/readonline/read?filePath="+fileName;
				if (fileName) {
					addTab({text:fileName,url:url});
				} else {
					$.messager.alert('提醒', '请选择要浏览的数据');
				}
			}
		});
		
		$('#fileDown2').click(function() {
			var a=$("#fileDown2").html();
			if(a=="下载"){
				var flag="b";
				location.href = path + "/classicCase/downloadFile?fileAddress=" + fileAddress + "&stdocId=" + stdocId+"&flag="+flag;
			}
		});
		
		
		$('#industry').combobox({
			valueField : 'id',
			textField : 'text',
			panelMaxHeight : '100',
			panelHeight : 'auto',
			url : path + '/dictionary/combo?id=0102'
		});

	
});

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

//上传文件是否为空
function upload_check(){
	if($("#addForm").form('validate')){
		$("#zhezhao").css("display","block");
		$.messager.alert('提醒', "上传中，请勿进行其他操作......");
		return true;
	}else{
		alert("添加失败！");
		return false;
	}
}


function return_main() {
	$('#addWin').hide();
	$('#editWin').hide();
	$('#watchWin').hide();
	$('#watchWin2').hide();
	$('#tree').hide();
	$('#class_required').show();
}