//加载上次登录时间
$(function(){
	var loginTime;
	$.ajax({
		type : 'post',
		url : path + "/login/getLoginInfo.json",
		async : false,
		success : function(data) {
			loginTime = data.loginTime;
		}
	});
	$("#loginTime").text(loginTime);
});