<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>404 not found</title>
		<%@include file="../inc/import.jsp"%>
		<script type="text/javascript" src="${ctx}/mgr/js/ux/common/404.js"></script>
	</head>
	<body class="errorbody">
		<div id="error" class="err404">
			<h2>
				非常遗憾，您访问的页面不存在！
			</h2>
			<p>
				看到这个提示，就自认倒霉吧!
			</p>
			<div class="reindex">
				<a href="javascript:void(0);" onclick="window.history.go(-1);" target="_parent">返回</a>
			</div>
		</div>
	</body>
</html>