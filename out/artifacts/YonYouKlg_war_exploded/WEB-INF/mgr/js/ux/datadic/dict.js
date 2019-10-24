/**
 * 数据字典
 */
$( function() {

});

	/**提交表单*/
	function saveMenu(){
		var upsys = sys.getInputVal("dict_form","operId");
		
	    if(upsys==null || upsys==""){
			$.messager.alert('操作提示',"请选择要修改的字典项！","info"); 
			return;
	    }
	    var chineseName = sys.getInputVal("dict_form","dicName");
	    if(chineseName==null || chineseName==""){
			$.messager.alert('操作提示',"请输入中文名称！","info");
			return; 
	    }
		
	    $.messager.progress({
			text : '提交中...'
		});
	    var dict_form = $('#dict_form');
	    dict_form.form('submit',{
			url : path+'/dictionary/addUpdate.action',
			onSubmit : function(){
				if(!dict_form.form('validate')){
					$.messager.progress('close');
					return false;
				}
			},
			success : function(d) { //此处特殊，返回的是JSON格式的字符窜 并不是JSON对象
			  var data = eval('(' + d + ')');  // change the JSON string to javascript object   
		      $.messager.alert('操作提示',data.msg,"info");   
		      dict_reload_tree(data.method);
		      dict_clear_form();
		      $.messager.progress('close');
			}
		});
	}
	// 重置表单
	function resetMenu(){
		$("#dict_form")[0].reset();
	}

	/**
	 * 右键菜单：添加子字典项
	 * @return
	 */
	function dict_addNode(){
		dict_clear_form();
		var node = $('#dict_tree').tree('getSelected');
	    $('#dict_form input[name="operId"]')[0].value=node.id;
	    $('#dict_form input[name="upSystemName"]')[0].value=node.text;
	    $('#dict_form input[name="level"]')[0].value=node.attributes.classNo;
	}
	
	/**
	 * 右键菜单：删除字典项
	 * @return
	 */
	function dict_delNode(){
		var node = $('#dict_tree').tree('getSelected');
		if(node.id=="0"){
			$.messager.alert('操作提示',"顶级节点不可删除！","warning");  
		}
		$.ajax({
            type: "POST",   
            url: path+"/dictionary/delete.action", 
            dataType: 'json',
            data: {
            	id : node.id
            },             
            success: function (data) { 
            	$.messager.alert('操作提示',data.msg,"info");  
            	dict_clear_form();
            	if(data.isleaf){//被删除节点的父节点，不存在子节点重载被删除节点的“父父节点”
            		var pNode = $("#dict_tree").tree("getParent",node.target); 
            		var ppNode = $("#dict_tree").tree("getParent",pNode.target); 
  					$("#dict_tree").tree("reload",ppNode.target);  
            	}else{ //被删除节点的父节点，存在子节点重载被删除节点的“父节点”
            		var pNode = $("#dict_tree").tree("getParent",node.target);  
  					$("#dict_tree").tree("reload",pNode.target);  
            	}
            },
            error:function(){
            	$.messager.alert('操作提示',"<font color='red'>抱歉！操作失败！</font>","error");  
            }
         });

	}
	/**
	 * 右键菜单：修改字典项
	 * @return
	 */
	function dict_updateNode(){
		var node = $('#dict_tree').tree('getSelected');
		if(node.id!="0"){
			dict_loadForm(node);
			var pNode = $("#dict_tree").tree("getParent",node.target); 
			parentNodeInfo(pNode);
		}else{
			$.messager.alert('操作提示',"顶级节点不可修改！","warning");  
		}
	}
	/**
	 * 设置表单父节点信息
	 * @param node 父节点
	 * @return
	 */
	function parentNodeInfo(node){
	    sys.setInputVal("dict_form","upSystemName",node.text);
	    sys.setInputVal("dict_form","level",node.text);
	}
	/**
	 * 加载表单数据
	 * @param node
	 * @return
	 */
	function dict_loadForm(node){
		$.ajax({
              type: "POST",   
              url: path+"/dictionary/load.action", 
              dataType: 'json',
              data: {
              	id : node.id
              },             
              success: function (data) { 
            	     
            	  	$('#dict_form').form('load',data); //加载表单数据
              },
              error:function(){
              	$.messager.alert('操作提示',"<font color='red'>抱歉！操作失败！</font>","error");  
              }
          });
	}
	
	/**
	 * 清空form
	 * @return
	 */
	function dict_clear_form(){
		$('#dict_form').form('clear');
	}
	
	/**
	 * 增删改后，重新加载相关节点
	 * @param method
	 * @return
	 */
  function dict_reload_tree(method){    
	  var node = $("#dict_tree").tree("getSelected");
	  if (node) {   
	  	if(method=="add") {   
	  		if(node.id != "0"){
		  		var bool = $('#dict_tree').tree('isLeaf',node.target);  //是否为子节点
		  		if(bool){ //刷新父节点后，打开本节点
		  			var pNode = $("#dict_tree").tree("getParent",node.target); 
		  			$("#dict_tree").tree("reload",pNode.target); //重载后已没有选中
				    time_delay_tree(node);//递归延时，直到tree加载完再打开节点
		  		}else{//非子节点，刷新本节点
		  			$("#dict_tree").tree("reload",node.target);
		  		}
	  		}else{
	  			$("#dict_tree").tree("reload"); //如果为父节点直接重载
	  		}	
		}else{   //刷新父节点
			var pNode = $("#dict_tree").tree("getParent",node.target);  
			$("#dict_tree").tree("reload",pNode.target);   
		}
	  } else {
        $("#dict_tree").tree("reload");    
      }
  }
  	/**
  	 * 递归等待节点加载完成后，打开相应节点
  	 * @param node
  	 * @return
  	 */
	function time_delay_tree(node){
		setTimeout(function () {  //递归延时，直到tree加载完再打开节点
			var n = $("#dict_tree").tree("find",node.id);
			if(n!=null){
				$("#dict_tree").tree("expand",n.target); 
			}else{
				time_delay_tree(node);
			}
		},200);
	}
	
	function app_type_menucombo(){
		$('#appMenu').combotree({   
		    url: 'menu_treeRecursive.action',   
		    onSelect:function(node){
		        var pid = sys.getInputVal("dict_form","operId");
		    	if(!sys.isNull(pid) && pid !='0104'){
					$.messager.alert('操作提示',"非流程业务不能选择菜单！","info");  
					$(this).combotree("clear");
					return;
				}
		    }
		});
	}

