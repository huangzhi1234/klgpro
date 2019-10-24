// 声明一个全局对象Namespace，用来注册命名空间
NamespaceOfLanHai = {
     /**
      * 全局对象仅仅存在register函数，参数为名称空间全路径，如"Grandsoft.GEA"
      * @param {Object} fullNS 名称空间全路径，如"Grandsoft.GEA"
      */
	register:function(fullNS){
				    // 将命名空间切成N部分, 比如Grandsoft、GEA等
				    var nsArray = fullNS.split('.');
				    var sEval = "";
				    var sNS = "";
				    for (var i = 0; i < nsArray.length; i++)
				    {
				        if (i != 0) sNS += ".";
				        sNS += nsArray[i];
				        // 依次创建构造命名空间对象（假如不存在的话）的语句
				        // 比如先创建Grandsoft，然后创建Grandsoft.GEA，依次下去
				        sEval += "if (typeof(" + sNS + ") == 'undefined') " + sNS + " = new Object();"
				    }
				    if (sEval != "") eval(sEval);
	}
	
	
}; 

/**
 * 
// 全局对象仅仅存在register函数，参数为名称空间全路径，如"Grandsoft.GEA"
NamespaceOfLanHai.register = function(fullNS)
{
    // 将命名空间切成N部分, 比如Grandsoft、GEA等
    var nsArray = fullNS.split('.');
    var sEval = "";
    var sNS = "";
    for (var i = 0; i < nsArray.length; i++)
    {
        if (i != 0) sNS += ".";
        sNS += nsArray[i];
        // 依次创建构造命名空间对象（假如不存在的话）的语句
        // 比如先创建Grandsoft，然后创建Grandsoft.GEA，依次下去
        sEval += "if (typeof(" + sNS + ") == 'undefined') " + sNS + " = new Object();"
    }
    if (sEval != "") eval(sEval);
}
*/

NamespaceOfLanHai.serializeObject=function(obj){
	 var result={};
	 $.each(obj.serializeArray(), function (i, field) {  
		      result[field.name]=field.value;
      });  
	return result;
}

NamespaceOfLanHai.register("utils");
utils.datagrid={
	   /**
		 * 返回对象数组字符串，格式：[{}]
		 * @param {Object} elementId 元素Id
		 * @param {Object} index rowIndex
		 * @return {TypeName} 返回对象数组字符串，格式：[{}]
		 */
		getRowForDelete:function(elementId,index){
			var row=utils.datagrid.getRow(elementId,index);
		    var deleteRows=new Array();
		    deleteRows.push(row);
		    return JSON.stringify(deleteRows);
		},
		getTabel:function(elementId){
			 return $("#"+elementId);
		},
		load:function(elementId){
			 this.getTabel(elementId).datagrid('load');
		},
		getSelections:function(elementId){
			 return this.getTabel(elementId).datagrid('getSelections');
		},
		/**
		 * 得到列表所有记录
		 * @param {Object} elementId
		 * @memberOf {TypeName} 
		 * @return {TypeName} 
		 */
		getAll:function(elementId){
			var obj=this.getTabel(elementId).datagrid('getData');
			if(obj.rows){
				return obj.rows;
			}else{
				return obj;
			}
		},
		/**
		 * 
		 * 后台action:
		 * public void deleteProtocol() throws Exception{
				Map<String, Object> result=new HashMap<String, Object>();
				String info="";
				try {
					this.protocolService.deleteProtocol();
					info="操作成功";
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					info="操作失败："+e;
					throw e;
				}finally{
					result.put("info", info);
					this.writeJson(result);
					
				}
			}
		 
		 * 后台service:
		 * public void deleteProtocol() throws Exception{
				 List<TSysSupplierAgreement> agreements=JSON.parseArray(this.getRequest().getParameter("dataArray"), TSysSupplierAgreement.class);
				 for (TSysSupplierAgreement tSysSupplierAgreement : agreements) {
					 this.protocolDAO.deleteEntityByPK(tSysSupplierAgreement.getAgreementid());
				}
			 }
		 * 
		 * 
		 * @param {Object} elementId
		 * @param {Object} index
		 * @param {Object} url
		 */
		deleteRowForRemote:function(elementId,index,url){
			 var deleteRowArrayStr= utils.datagrid.getRowForDelete(elementId,index);
		     $.ajax({
		             type: "post",
		             url: url,
		             data: {dataArray:deleteRowArrayStr},
		             dataType: "json",
		             success: function(data){
		                      $.messager.alert("操作提示",data.info,"info");
		                      utils.datagrid.load(elementId);
		              },
		              error:function(data){
		                       $.messager.alert("操作提示",data.info,"error");
		              }
		         });
    
		},
		/**
		 * 从table删除一条记录
		 * @param {Object} elementId
		 * @param {Object} index
		 */
		deleteRow:function(elementId,index){
             var table=this.getTabel(elementId);
		     var rows=this.getRowsById(elementId);
		    
		     $.each(rows,function(rowindex,row){
				table.datagrid('refreshRow',rowindex); 
			 });
		     
			 table.datagrid('deleteRow',index); 
			
			$.each(rows,function(rowindex,row){
				table.datagrid('refreshRow',rowindex); 
			});
		},
		getRowsById:function(elementId){
	       return $("#"+elementId).datagrid('getRows');
        },
        
        /**
         * 得到行记录
         * @param {Object} elementId
         * @param {Object} index
         * @return {TypeName} 
         */
        getRow:function(elementId,index){
			 var rows=utils.datagrid.getRowsById(elementId);
		     return rows[index];
        }


	
};



utils.dictionary={
	/**
	 * 根据数据字典id得到数据字典对象
	 * @param {Object} systemid
	 * @return {TypeName} 
	 */
	getDictionaryDto:function(systemid){
	           var result={};
		      $.ajax({
	             type: "post",
	             async:false,//false:同步;true:异步;
	             url: "dictionary_load.action",
	             data: {id:systemid},
	             dataType: "json",
	             success: function(data){
	                    result=data;
	                    if(!result){
	                    	result={};
	                    }
	              },
	              error:function(data){
	            	   result={};
	              }
	         });
		     return result;
	},
	
	/**
	 * 根据数据字典id得到中文名
	 * @param {Object} systemid
	 * @return {TypeName} 
	 */
	getDictionaryChineseName:function(systemid){
	           var result='';
		      $.ajax({
	             type: "post",
	             async:false,//false:同步;true:异步;
	             url: "dictionary_load.action",
	             data: {id:systemid},
	             dataType: "json",
	             success: function(data){
	                    result=data.chineseName;
	                    if(!result){
	                    	result='';
	                    }
	                    
	              },
	              error:function(data){
	            	   
	              }
	         });
		     return result;
	}
	
	
};
