<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	window.onload = function (){
		var bntStr = '${bntMap[resourceUrl]}';
		var noBntStr = '${noBntMap[resourceUrl]}';
		bntStr = bntStr.substring(1,bntStr.length - 1);
		noBntStr = noBntStr.substring(1,noBntStr.length - 1);
		
		var bntArr = bntStr.split(',');
		for(var i in bntArr){
			$("#" + bntArr[i].trim()).show();
		}
		
		var noBntArr = noBntStr.split(',');
		for(var i in noBntArr){
			$("#" + noBntArr[i].trim()).hide();
		}
	}
</script>
<c:if test="${!placeul eq 'true'}">
	<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li>
			<a href="#">首页</a>
		</li>
		<c:forEach var="nav" items="${navMap[resourceUrl]}">
			<li>
				<a href="#">${nav}</a>
			</li>
		</c:forEach>
	</ul>
</div>
</c:if>
