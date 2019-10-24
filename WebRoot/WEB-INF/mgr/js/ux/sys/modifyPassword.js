$(function(){
	
	$("#btnToUpdate").click(function(){
		if($("#confirmNewPassword").val() == $("#newPassword").val()){
			if($("#editForm").form('validate')){
				$.ajax({
					type: 'post',
					url: path + '/modifyPassword/update.json',
					async: false,
					data: $("#editForm").serialize(),
					success: function(data){
						if(data.result == true){
							alert(data.msg);
						}else{
							alert(data.msg);
						}
					}
				});
			}			
		}else{
			alert("新密码与确认新密码不一致！");
		}
		
	});
		
})