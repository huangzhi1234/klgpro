var fileName='code/YonYouKlg';
$(function(){
	var svnTable = {
			title:'SVN列表',
			nowrap : false,//不要把数据显示在一行内
	        rownumbers : true,
	        collapsible : false,
	        url : path + '/svn/list.json?pid=',
			idField:'name',    
			treeField:'name',	
			
			columns : [ [{
	              field : 'url',
	              hidden:true
	           }, {
	              field : 'name',
	              title : '名称',
	              align:'left',
	              width : 250
	           }, {
	               field : 'size',
	               title : '文件大小(KB)',
	               width : 80,
	               align:'center'
	            } ,{
	                field : 'revision',
	                title : '版本号',
	                width : 80,
		            align:'center'
	             }, {
	                field : 'author',
	                title : '作者',
	                width : 100,
		            align:'center'
	             }, {
	                 field : 'date',
	                 title : '修改日期',
	                 width : 180,
		             align:'center'
	             }, {
	            	 field :'kind',
	            	 title :'操作',
	            	 width : 120,
	            	 align :'center',
	            	 formatter :function(value,row,index) {
	            		 if(value=='file'){
	            			var r="\'"+decodeURI(row.url)+"\'";
	            			var b='<input type="text" onclick="dl('+r+')" value="下载" style="cursor: pointer;color:red;width:50px"/>';
	            			var a='<input type="text" onclick="read()" value="预览" style="cursor: pointer;color:red;width:50px"/>';
	            			//var a='<a onclick="read('+row.url+')" style="cursor: pointer;color:red;margin-right:8px;">预览</a>';
	            			 //var b='<a onclick="return download('+row.url+')" style="cursor: pointer;color:red">下载</a>';
	            			 return a+b;
	            		 }
	            	 }
	            	 } ] ],
			onBeforeExpand : function(row){
			    	//节点展开前进行查询的url
			    	$(this).treegrid('options').url = path + '/svn/list.json?pid='+encodeURI(decodeURI(row.url));
			},
			fitColumns : true/*,// 自适应水平，防止出现水平滚动条
			toolbar : "#tb_proFileInfo"*/
		};
	$('#datagridOfProInfo').treegrid(svnTable);
});



//下载svn文件
function dl(url){
	alert("该功能善未开发");
	//alert("dfg");
	//location.href = path + "/svn/down?url=" + encodeURI(decodeURI(url));
}
//预览svn文件
function read(){
	alert("该功能善未开发");
}