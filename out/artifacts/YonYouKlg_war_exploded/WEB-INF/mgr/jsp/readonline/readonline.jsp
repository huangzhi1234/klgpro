<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>在线预览</title>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
	<script src="js/FlexPaper/js/jquery.js"></script>
	<script src="js/FlexPaper/js/flexpaper_flash.js"></script>
	<script src="js/FlexPaper/js/flexpaper_flash_debug.js"></script>
<script type="text/javascript">
	$(function(){
		$("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(window).height() }).appendTo("body");  
		$("<div class=\"datagrid-mask-msg\"></div>").html("加载中，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 });
	});
	function onPageLoading(pageNumber){
	       $("#txt_eventlog").val('onPageLoading:' + pageNumber + '\n' + $("#txt_eventlog").val());
    }		

	</script>
	<script type="text/javascript">
	<%-- 加载完成后删除swf文件 --%>
		function onDocumentLoaded(totalPages){
			$("#txt_eventlog").val('onDocumentLoaded:' + totalPages + '\n' + $("#txt_eventlog").val());
			disLoad();
			$.ajax({
				type: 'get',
				url: ctx + '/mgr/readonline/delete',
				data : {fileName:'<%=(String) session.getAttribute("fileName")%>'}
			});
}

	</script>

	<script type="text/javascript">

	//取消加载层  
   function disLoad() {  
    $(".datagrid-mask").remove();  
    $(".datagrid-mask-msg").remove();  

     }
	</script>
</head>

<%--  <%=(String)session.getAttribute("fileName")%>  --%>
<body>
	<div style="position:absolute;left:10px;top:10px;">
		<%-- 指定flexPaper的宽度和高度  --%>
		<a id="viewerPlaceHolder" style="width:1200px;height:800px;display:block"></a>
		<script type="text/javascript"> 
                var fp = new FlexPaperViewer(    
                         'js/FlexPaper/swfFiles/FlexPaperViewer', 	//视图文件的位置
                         'viewerPlaceHolder',     //对应于a 标签的id
                         { config : {
	                         SwfFile : escape('../<%=(String) session.getAttribute("fileName")%>'),
	                         Scale : 0.6, 
	                         ZoomTransition : 'easeOut',
	                         ZoomTime : 0.5,
	                         ZoomInterval : 0.2,
	                         FitPageOnLoad : false,  //初始化的时候自适应页面
	                         FitWidthOnLoad : true,	//初始化的时候自适应页面宽度
	                         PrintEnabled : true,   //是否可以打印 
	                         FullScreenAsMaxWindow : false,
	                         ProgressiveLoading : false,
	                         MinZoomSize : 0.2,
	                         MaxZoomSize : 5,
	                         SearchMatchAll : false,
	                         InitViewMode : 'Portrait',
	                         
	                         ViewModeToolsVisible : true,
	                         ZoomToolsVisible : true,
	                         NavToolsVisible : true,
	                         CursorToolsVisible : true,
	                         SearchToolsVisible : true,
	                         localeChain: 'en_US'
                         }});
            </script>
	</div>
</body>
</html>