$(function(){	
	//导航切换
	$(".menuson li").click(function(){
		$(".menuson li.active").removeClass("active")
		$(this).addClass("active");
	});
	
	//一级功能菜单收缩
	$('.lefttop').click(function(){
		var $dl = $(this).next('dl');
		$('dl').slideUp();//先隐藏
		if($dl.is(':visible')){
			$(this).next('dl').slideUp();
		}else{
			$(this).next('dl').slideDown();
		}
	});
	
	//二级功能菜单收缩
	$('.title').click(function(){
		var $ul = $(this).next('ul');//找查同一个当前功能菜单下的操作菜单
		$('dd').find('ul').slideUp();//隐藏所有的操作菜单
		if($ul.is(':visible')){//若当前操作菜单存在可见的
			$(this).next('ul').slideUp();//隐藏
		}else{
			$(this).next('ul').slideDown();//显示
		}
	});
});