<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="topbody" style="width: 100%;height: 100%;">
	<!--logo-->
	<div>
		<img src="${pageContext.request.contextPath}/mgr/images/logo3.png"
			title="系统首页" />
	</div>

	<div class="topright" style="position:relative;top:-86px;">
		<div class="user">
			<span>${user.userAct}【${user.userName}】</span>
		</div>
		<ul style="position:absolute;top:34px;right:42px;">
			<li><a href="${pageContext.request.contextPath}/mgr/logout"
				target="_parent">退出登录</a>|<button id="changepassword" >修改密码</button></li>
		</ul>
	</div>
<div id="changepwd" class="easyui-dialog" title="修改密码" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:400px;height:320px;">
		<form id="causeForm" class="formbody" action="${ctx}/mgr/stDocInfo/add.json?stDoc=stDocInfo">
			<div class="formtitle"><span>申请下载</span></div>
				<ul class="forminfo">
					<li><label>原密码：</label>
					<input name="pwd" id="pwd" type="password" style="height: 24px" class="easyui-validatebox" data-options="required:true,missingMessage:'原密码不能为空'">
					</li>
					<li><label>新密码：</label>
					<input name="newpwd" id="newpwd" type="password" style="height: 24px" class="easyui-validatebox" data-options="required:true,missingMessage:'新密码不能为空'">
					</li>
					<li><label>确认密码：</label>
					<input name="newpwd2" id="newpwd2" type="password" style="height: 24px" class="easyui-validatebox" data-options="required:true,missingMessage:'确认密码不能为空'">
					</li>
					
					<li><br/><label>&nbsp;</label><input id="sendChange" type="button" class="btn" value="提交"/>
				</ul>
		</form>
	</div>
</div>

<script>
$(function(){
	$("#changepassword").click(function() {
		$("#changepwd").dialog('open');
	});
	
	$("#sendChange").click(function(){
		var password = $("#pwd").val();
		var newpassword = $("#newpwd").val();
		var newpassword2 = $("#newpwd2").val();
		var param={};
		param.password=password;
		param.newpassword=newpassword;
		param.newpassword2=newpassword2;
		$.ajax({
			type:'post',
			url:path+'/user/changePwd.json',
			async:true,
			data:param,
			success:function(data){
				if(data==1){
					$.messager.alert("提醒","密码修改成功");
					$("#changepwd").dialog('close');
					return;
				}else if(data==2){
					$.messager.alert("提醒","密码修改失败");
					return;
				}else{
					$.messager.alert("提醒","两次输入的密码不一样，请重新输入");
				}
			}
		});
				
	});
})
</script>
