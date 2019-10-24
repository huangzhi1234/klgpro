<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
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
									content : '<iframe src="'
											+ url
											+ '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>'
								});

			}
		}
	}
</script>



<c:set var="ctx" value='${pageContext.request.contextPath}'
	scope="request" />
<c:forEach var="rs" items="${resource}">
	<c:if test="${rs.resourcePid == 0}">
		<div class="lefttop">
			<span></span>${rs.resourceName}
		</div>
		<c:if test="${rs.children != null && fn:length(rs.children) > 0}">
			<dl class="leftmenu">
				<c:set var="resource" value="${rs.children}" scope="request" />
				<c:import url="/mgr/main/tree" />
			</dl>
		</c:if>
	</c:if>

	<c:if test="${rs.resourcePid != 0 && rs.resourceType == 1}">
		<dd>
			<div class="title">
				<span><img src="${ctx}/mgr/images/leftico01.png" /></span>
				${rs.resourceName}
			</div>
			<c:if test="${rs.children != null && fn:length(rs.children) > 0}">
				<ul class="menuson">
					<c:set var="resource" value="${rs.children}" scope="request" />
					<c:import url="/mgr/main/tree" />
				</ul>
			</c:if>
		</dd>
	</c:if>

	<c:if test="${rs.resourceType == 2}">
		<li><cite></cite> <a
			onclick="addTab({text:'${rs.resourceName}',url:'${ctx}${rs.resourceUrl}'})">${rs.resourceName}</a>
			<i></i></li>
	</c:if>
</c:forEach>