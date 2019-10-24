<%--*****************************************--%>
<%--************  默   认   引   入    ****************--%>
<%--*****************************************--%>
<%--JSTL tag--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/show.tld" prefix="queryTag" %><!-- //自定义标签 -->
<%--服务器环境--%>
<c:set var="ctx" value='${pageContext.request.contextPath}' scope="request"/>
<script type="text/javascript">
	var ctx = '${pageContext.request.contextPath}';
	var path = ctx+'/mgr';
</script>

<%--基础JS与CSS--%>
<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/jquery.min.js"></script>
<link type="text/css"  rel="stylesheet" href="${ctx}/mgr/css/style.css" />


<%--*****************************************--%>
<%--**************    按   需   引   入    ************--%>
<%--*****************************************--%>
<%
	String importStr = (String) request.getAttribute("import");
	if (importStr != null && importStr.length() > 0) {
		String[] imports = importStr.split(",");
		for (String i : imports) {
			request.setAttribute(i, true);
		}
	}
%>
<%--用户自定义 tag--%>
<c:if test="${commonTag eq 'true'}">
	<%@ taglib prefix="common" uri="/WEB-INF/mgr/tld/commons.tld"%>
</c:if>

<%--easyUI--%>
<c:if test="${easyUI eq 'true'}">
	<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<%--<script type="text/javascript" src="${ctx}/mgr/js/common/move.js"></script>--%>
	<link type="text/css"  rel="stylesheet" href="${ctx}/mgr/js/jquery-easyui-1.4/themes/default/easyui.css" />
	<link type="text/css"  rel="stylesheet" href="${ctx}/mgr/js/jquery-easyui-1.4/themes/icon.css" />
	<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/uxplugins/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/uxplugins/jquery.form.extend.js"></script>
	<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/uxplugins/jquery.validate.extend.js"></script>
	<script type="text/javascript" src="${ctx}/mgr/js/jquery-easyui-1.4/uxplugins/jquery.datagrid.extend.js"></script>

</c:if>


<%--命名空间--%>
<c:if test="${namespace eq 'true'}">
	<script type="text/javascript" src="${ctx}/mgr/js/common/namespace/namespace.js"></script>
</c:if>
<c:if test="${jsUtil eq 'true'}">
	<script type="text/javascript" src="${ctx}/mgr/js/common/jsUtil.js"></script>
</c:if>

<c:if test="${commonCss eq 'true'}">
	<link type="text/css"  rel="stylesheet" href="${ctx}/mgr/css/common.css" />
	<link rel="stylesheet" href="${ctx}/mgr/css/icons-sys/icons-sys.css" type="text/css"></link>
	
</c:if>
