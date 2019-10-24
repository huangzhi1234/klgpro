<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<script type="text/javascript" charset="utf-8">
	$(function() {
		tabinit();
	});
</script>
<div id="pms_center_tabs">
	<div title="桌面"
		data-options="iconCls:'icon-house',href:'${pageContext.request.contextPath}/portal/portal.jsp'">
	</div>
</div>
<div id="sys_menu_center_tabs" style="width: 120px;display:none;">
	<div type="refresh">刷新</div>
	<div class="menu-sep"></div>
	<div type="close">关闭</div>
	<div type="closeOther">关闭其他</div>
	<div type="closeAll">关闭所有</div>
</div>