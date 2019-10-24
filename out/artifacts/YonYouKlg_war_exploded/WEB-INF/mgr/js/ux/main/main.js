var pms_center_tabs;	
var centerTab={};

$(function() {
		tabinit();
  }
)
	


    /**
	 * 关闭当前选择的面板
	 * @memberOf {TypeName} 
	 * @return {TypeName} 
	 */
    function closeSelectedTab(){
	   if(pms_center_tabs&&centerTab.selectTabIndex){
		   pms_center_tabs.tabs('close', centerTab.selectTabIndex);
	   }
   };
  

		   


	
	function tabinit(){
		  
		pms_center_tabs = $('#pms_center_tabs').tabs({
			fit : true,
			border : false,
			onContextMenu : function(e, title) {
				e.preventDefault();
			}
			,tools:[{
				iconCls:'icon-accordion_collapse',
				handler:function(){
					$('#pms_layout').layout('collapse','north');  
				}
			}],
			onSelect:function(title,index){
				centerTab.selectTabIndex=index;
			 }
		});
	}
		  
	