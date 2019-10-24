$(function(){
	//session失效时登陆页面全屏显示
	if (top.location != self.location){    
		top.location=self.location;    
	} 
	
	//登陆按钮时触发的事件
	$('.loginbtn').click(
		function(){
			return $("#loginForm").form('validate');
		});
	
	
	
});


/******************************************以下是样式控制JS************************************************/
$(function() {
	
	
	 // 计算登陆窗口的大小以确定登陆框的位置
	$('.loginbox').css( {
		'position' : 'absolute',
		'left' : ($(window).width() - 692) / 2
	});
	$(window).resize(function() {
		$('.loginbox').css( {
			'position' : 'absolute',
			'left' : ($(window).width() - 692) / 2
		});
	});
	
	 // 用户名密码输入提示
	$(".input_txt").each(function(){
		
		//输入密码时，隐藏span
		var inputObj=$(this);
		inputObj.siblings("span").bind('click',function(){
			$(this).hide();
			inputObj.focus();
		})
		
		var thisVal=$(this).val();
		//判断文本框的值是否为空，有值的情况就隐藏提示语，没有值就显示
		if(thisVal!=""){
			$(this).siblings("span").hide();
		}else{
			$(this).siblings("span").show();
		}
		//聚焦型输入框验证
		$(this).focus(function(){
			$(this).siblings("span").hide();
		}).blur(function(){
			var val=$(this).val();
			if(val!=""){
				$(this).siblings("span").hide();
			}else{
				$(this).siblings("span").show();
			}
		});
	});
});


// Cloud Float...
var $main = $cloud = mainwidth = null;
var offset1 = 450;
var offset2 = 0;

var offsetbg = 0;

$(document).ready(function() {
	$main = $("#mainBody");//两朵白云
	$body = $("body");//窗体
	$cloud1 = $("#cloud1");//白云1
	$cloud2 = $("#cloud2");//白云2

	mainwidth = $main.outerWidth();//两朵白云的宽度

});

//飘动的白云
setInterval(function flutter() {
	if (offset1 >= mainwidth) {
		offset1 = -580;
	}

	if (offset2 >= mainwidth) {
		offset2 = -580;
	}

	offset1 += 1.1;
	offset2 += 1;
	$cloud1.css("background-position", offset1 + "px 100px");
	$cloud2.css("background-position", offset2 + "px 460px");
}, 70);

//当白云消失时，重新从左边飘过来
setInterval(function bg() {
	if (offsetbg >= mainwidth) {
		offsetbg = -580;
	}

	offsetbg += 0.9;
	$body.css("background-position", -offsetbg + "px 0")
}, 90);
/*****************************************样式控制JS结束*******************************************************/