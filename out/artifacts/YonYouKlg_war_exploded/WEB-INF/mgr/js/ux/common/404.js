$(function() {
	/*加载页面时将内容居中*/
	$('#error').css( {
		'position' : 'absolute',
		'left' : ($(window).width() - 490) / 2
	});
	$(window).resize(function() {
		$('#error').css( {
			'position' : 'absolute',
			'left' : ($(window).width() - 490) / 2
		});
	})
});