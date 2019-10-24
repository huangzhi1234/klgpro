var dialog_width = 840;
var dialog_height = 530;
var wait_number_tabsHiddenId_ = "wait_number_tabsHiddenId_";//待办数量提醒：top.jsp中的隐藏域id，可以获取到选项卡的Text值
var sys = $.extend({}, sys);/* 定义全局对象，类似于命名空间或包的作用 */
Date.prototype.format = function(format) 
{
	var o = {
		"M+" : this.getMonth() + 1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
		"S" : this.getMilliseconds()
	//millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}
/**
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展validatebox，添加验证两次密码功能
 */
$.extend($.fn.validatebox.defaults.rules, {
	eqPwd : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '密码不一致！'
	}
});
$.extend($.fn.validatebox.defaults.rules, {
	vTotal : {
		validator : function(value, param) {
			return parseInt(value) <= parseInt($(param[0]).val());
		},
		message : '不能大于[不良数]！'
	}
});
$.extend($.fn.validatebox.defaults.rules, {
	vdate : {
		validator : function(value, param) {
			return value > new Date().format("yyyy-MM-dd");
		},
		message : '必须大于当前时间'
	}
});
$.extend($.fn.validatebox.defaults.rules, {
	vdatenow : {
		validator : function(value, param) {
			return value >= new Date().format("yyyy-MM-dd");
		},
		message : '必须大于或者等于当前时间'
	}
});
$.extend($.fn.validatebox.defaults.rules, {
	mobile : {
		validator : function(value,param) {
			//return /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/.test(value);
	        var iscross = /^(13|15|18)\d{9}$/i.test(value);
	        if(!iscross){
	        	iscross = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/i.test(value);
	        }
			return iscross;
		},
		message : '电话号码不正确'
	},
	loginName: { 
        validator: function(value, param) {
        	return /^[a-zA-Z0-9_]+$/.test(value); 
        }, 
        message: "只允许英文字母、数字及下划线" 
    },
    phoneOrMobile:{//验证手机或电话
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value) || /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message:'请填入手机或电话号码,如13688888888或020-8888888'
	},
	comboxchoose : {// 验证带请选择的下拉框
		validator : function(value) {
			return !(""==value || "--请选择--"==value);
		},
		message : '请选择有效值'
	},
	integer : {// 验证整数
		validator : function(value) {
			return /^[+]?[1-9]+\d*$/i.test(value);
		},
		message : '请输入整数'
	}
});
//$.fn.datebox.defaults.value = new Date().format("yyyy-MM-dd");  //datebox默认当前时间



//$.fn.tree.defaults.loadFilter = function(data, parent) {
//	var opt = $(this).data().tree.options;
//	var idFiled, textFiled, parentField;
//	if (opt.parentField) {
//		idFiled = opt.idFiled || 'id';
//		textFiled = opt.textFiled || 'text';
//		parentField = opt.parentField;
//		var i, l, treeData = [], tmpMap = [];
//		for (i = 0, l = data.length; i < l; i++) {
//			tmpMap[data[i][idFiled]] = data[i];
//		}
//		for (i = 0, l = data.length; i < l; i++) {
//			if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
//				if (!tmpMap[data[i][parentField]]['children'])
//					tmpMap[data[i][parentField]]['children'] = [];
//				data[i]['text'] = data[i][textFiled];
//				tmpMap[data[i][parentField]]['children'].push(data[i]);
//			} else {
//				data[i]['text'] = data[i][textFiled];
//				treeData.push(data[i]);
//			}
//		}
//		return treeData;
//	}
//	return data;
//};
$.fn.datagrid.defaults.loadFilter = function(data) {
	//},loadFilter:function(data){if(typeof data.length=="number"&&typeof data.splice=="function"){ 源码
	//就是为了解决以上源码的  data is null
	if(data==null || data==undefined){
		return {total:0,rows:[]}; 
	}else if(typeof data.length=="number" && typeof data.splice=="function"){
		return {total:data.length,rows:data};
	}else{
		return data;
	}
};

/**
 * 
 * @requires jQuery,EasyUI
 * 
 * 防止panel/window/dialog组件超出浏览器边界
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};
//$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
//$.fn.window.defaults.onMove = easyuiPanelOnMove;
//$.fn.panel.defaults.onMove = easyuiPanelOnMove;
/**
 * 格式：年(4位)月(2)日(2)时(2)分(2)秒(2)-毫秒(3)随机数(2)
 */
sys.getDateNo = function(){
	var date = new Date();
	
	var currentDate = "";
	//初始化时间
	var year = date.getFullYear();//年份2位
	var month = date.getMonth()+1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	var ms = date.getMilliseconds();    //毫秒
	
	currentDate += year;
	   
	if (month >= 10 ){
		currentDate += month;
	}else{
		currentDate += "0" + month;
	}
	if (day >= 10 ){
		currentDate += day ;
	}else{
		currentDate += "0" + day ;
	}
	if(hour >=10){
		currentDate = currentDate+hour ;
	}else{
		currentDate = currentDate+"0" + hour ;
	}
	if(minute >=10) {
		currentDate = currentDate + minute ;
	}else{
		currentDate = currentDate + "0" + minute ;
	}      
	if(second>=10){
		currentDate = currentDate + second + "-";
	}else{
		currentDate = currentDate + "0" + second + "-";
	}
	if(ms>=100){
		currentDate+=ms;
	}else if(ms>=10){
		currentDate=currentDate + "0" + ms;
	}else{
		currentDate=currentDate + "00" + ms;
	}
	//公式：parseInt(Math.random()*(上限-下限+1)+下限); 
	currentDate += parseInt(Math.random()*(99-10+1) + 10);  //10到99的随机数
	return currentDate;
}
/**
 * 当前日期的前d天
 */
sys.getDateBefore = function(d){
	var myDate = new Date(); 
	myDate.setDate(myDate.getDate()-d);
	return myDate.format("yyyy-MM-dd");
}
/**
 * 
 * @requires jQuery
 * 
 * 将form表单元素的值序列化成对象
 * 
 * @returns object
 */
sys.serializeJson = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};
/**
 * 根据input name获取value
 * @param formId 表单ID
 * @param name	input标签name值
 * @return $("#formId input[name='name值']").val()
 */
sys.getInputVal = function (formId,name){
	return $("#"+formId+" input[name='"+name+"']").val()
}
/**
 * 根据input name赋值value
 * @param formId 表单ID
 * @param value	value值
 * @return $("#"+formId+" input[name='"+name+"']").attr("value",value);
 */
sys.setInputVal = function (formId,name,value){
	//return $("#"+formId+" input[name='"+name+"']").attr("value",value);
	$("#"+formId+" input[name='"+name+"']")[0].value=value;
}


/**
 * 根据input name设置attr
 * @param formId 表单ID
 * @param value	value值
 */
sys.setInputAttr = function (formId,name,property,value){
	return $("#"+formId+" input[name='"+name+"']").attr(property,value);
}

/**
 * 根据根据表单id和表单元素名称获取对象
 * @param formId 表单ID
 * @param name 表单元素名称
 */
sys.getFormelement = function (formId,name){
	return $("#"+formId+" input[name='"+name+"']");
}

/**
 * 获取text值
 * @param formId 表单ID
 */
sys.getTextVal = function (formId,name){
	   var e=$("#"+formId+" [name='"+name+"']");
	   var e01=e[0];
	   if(e01&&e01.type=='textarea'){
		   return e.val();
	   }
	return $("#"+formId+" [name='"+name+"']").text();
}

/**
 * 赋text值
 * @param formId 表单ID
 */
sys.setTextVal = function (formId,name,value){
	   var e=$("#"+formId+" [name='"+name+"']");
	   var e01=e[0];
	   if(e01&&e01.type=='textarea'){
		   e.attr('value',value);
	   }
	return $("#"+formId+" [name='"+name+"']").text(value);
}

/**
 * 根据id url来获取记录 下拉框
 * @param id 表单ID
 * @param url	访问的action
 * @return	
 */
function comboboxUtil(id,url){
	$('#'+id).combobox({
				url:url,
				editable:false,
				valueField:'id',
				textField:'text'
				});
}

/**
 * 根据id url来获取记录 隐藏按钮的下拉框
 * @param id 表单ID
 * @param url	访问的action
 * @return	
 */
function bcomboboxUtil(id,url){
	$('#'+id).combobox({
				url:url,
				editable:false,
				hasDownArrow:false,
				valueField:'id',
				textField:'text'
				});
}
		
/**
 * 根据id url来获取记录 复选框
 * @param id 表单ID
 * @param url	访问的action
 * @param flag true or false
 * @return	
 */
function combotreeUtil(id,url,flag){
	$('#'+id).combobox({
				url:url,
				editable:false,
				multiple:true,
				valueField:'text',
				textField:'text'
				});
}

/**
* 使panel和datagrid在加载时提示
* @requires jQuery,EasyUI
*
*/
$.fn.panel.defaults.loadingMessage = '加载中....';
$.fn.datagrid.defaults.loadMsg = '加载中....';
/**
* @requires jQuery,EasyUI
*
* 通用错误提示
*
* 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
*/
//var easyuiErrorFunction = function(XMLHttpRequest) {
//$.messager.progress('close');
//$.messager.alert('错误', XMLHttpRequest.responseText);
//};
//$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
//$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
//$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
//$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
//$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
//$.fn.form.defaults.onLoadError = easyuiErrorFunction;

/**
 * @requires jQuery,EasyUI,jQuery cookie plugin
 * 
 * 更换EasyUI主题的方法
 * 
 * @param themeName
 *            主题名称
 */
sys.changeTheme = function(themeName) {
	var $easyuiTheme = $('#easyuiTheme');
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
	$easyuiTheme.attr('href', href);

	$.cookie('easyuiThemeName', themeName, {
		expires : 7
	});
};
sys.getTheme = function(){
	var themeName = $.cookie('easyuiThemeName');
	if(themeName==null)
		themeName = 'default';
	var $easyuiTheme = $('#easyuiTheme');
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
	$easyuiTheme.attr('href', href);
};
/**
 * @requires jQuery,EasyUI
 * 
 * @param options
 */
sys.dialog = function(options) {
	var opts = $.extend({
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	}, options);
	return $('<div/>').dialog(opts);
};

sys.alert = function(arg1,arg2,arg3,arg4) {
	//alert('bb');
	$.messager.alert('警告','警告消息'); 
	
};

sys.console=function(obj){
	console.info(obj);
};
sys.showMSG=function(msg) {
	$.messager.show({
		title : "提示信息",
		msg : msg,
		timeout : 4000,
		showType : "show"
	});
};

sys.cjkEncode=function(text) {
	if (text == null) {
		return "";
	}
	var newText = "";
	for ( var i = 0; i < text.length; i++) {
		var code = text.charCodeAt(i);
		if (code >= 128 || code == 91 || code == 93) { //91 is "[", 93 is "]".         
			newText += "[" + code.toString(16) + "]";
		} else {
			newText += text.charAt(i);
		}
	}
	return newText;
}

sys.dyniframesize=function(down) { 
	var pTar = null; 
	if (document.getElementById){ 
		pTar = document.getElementById(down); 
	}else{ 
		eval('pTar = ' + down + ';'); 
	} 
	if (pTar && !window.opera){ 
		//begin resizing iframe 
		pTar.style.display="block" 
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){ 
			//ns6 syntax 
			pTar.height = pTar.contentDocument.body.offsetHeight +20; 
			pTar.width = pTar.contentDocument.body.scrollWidth; 
		}else if (pTar.Document && pTar.Document.body.scrollHeight){ 
		//ie5+ syntax 
			pTar.height = pTar.Document.body.scrollHeight; 
			pTar.width = pTar.Document.body.scrollWidth; 
		} 
	} 
} 
sys.reloadiframe=function(ifameid){
	var addurl=document.getElementById(ifameid).src
	document.getElementById(ifameid).src=addurl;
}

/**
 * 刷新panel
 */
sys.panel_refresh = function(panelId){
	$('#'+panelId).panel('open').panel('refresh');
}

sys.isNull = function(obj){
	if(obj == undefined || obj==null || obj == "" || obj=="undefined" || obj=="null"){
		return true;
	}
	return false;
}
/**
 * 设置A标签的悬浮title值
 * @param id
 * @param value
 * @return
 */
sys.setATitle = function(id,value){
	$("#"+id).attr("title",value);
}
/**
 * 
 * 增加formatString功能
 * 使用方法：sys.formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * @returns 格式化后的字符串
 */
sys.formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};
/**
 * 取消easyui默认开启的parser
 * 在页面加载之前，先开启一个进度条
 * 然后在页面所有easyui组件渲染完毕后，关闭进度条
 * @requires jQuery,EasyUI
 * 
 */
//$.parser.auto = false;
//$(function() {
//	$.messager.progress({
//		text : '页面加载中....',
//		interval : 100
//	});
//	$.parser.parse(window.document);
//	window.setTimeout(function() {
//		$.messager.progress('close');
//		if (self != parent) {
//			window.setTimeout(function() {
//				try {
//					parent.$.messager.progress('close');
//				} catch (e) {
//				}
//			}, 500);
//		}
//	}, 1);
//	$.parser.auto = true;
//});
var sys_iconData = [ {
	value : '',
	text : '无'
}, {
	value : 'icon-add',
	text : 'icon-add'
}, {
	value : 'icon-edit',
	text : 'icon-edit'
}, {
	value : 'icon-remove',
	text : 'icon-remove'
}, {
	value : 'icon-save',
	text : 'icon-save'
}, {
	value : 'icon-cut',
	text : 'icon-cut'
}, {
	value : 'icon-ok',
	text : 'icon-ok'
}, {
	value : 'icon-no',
	text : 'icon-no'
}, {
	value : 'icon-cancel',
	text : 'icon-cancel'
}, {
	value : 'icon-reload',
	text : 'icon-reload'
}, {
	value : 'icon-search',
	text : 'icon-search'
}, {
	value : 'icon-print',
	text : 'icon-print'
}, {
	value : 'icon-help',
	text : 'icon-help'
}, {
	value : 'icon-undo',
	text : 'icon-undo'
}, {
	value : 'icon-redo',
	text : 'icon-redo'
}, {
	value : 'icon-back',
	text : 'icon-back'
}, {
	value : 'icon-sum',
	text : 'icon-sum'
}, {
	value : 'icon-tip',
	text : 'icon-tip'
}, {
	value : 'icon-house',
	text : 'icon-house'
}, {
	value : 'icon-yc-car',
	text : 'icon-yc-car'
} ];


/**
 * 创建上传窗口 公共方法
 * @param multifile 是否多文件上传
 * @param bis 业务类型
 * @param fk 附件绑定ID
 * @param callBack 上传成功之后的回调
 */
sys.Uploader = function(multifile,bis,fk,filters,maxSize,callBack){
	var addWin = $('<div style="overflow: hidden;"/>');
	var upladoer = $('<iframe/>');
	var chunk = false;//（系统不实现切割文件上传）
	upladoer.attr({'src':path+'/plugins/plupload/uploader.jsp?bis='+bis+'&fk='+fk+'&filters='+filters+'&multifile='+multifile+'&maxSize='+maxSize,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
	addWin.window({
		title:"上传文件",
		height:350,
		width:550,
		minimizable:false,
		modal:true,
		collapsible:false,
		maximizable:false,
		resizable:false,
		content:upladoer,
		onClose:function(){
			var fw = GetFrameWindow(upladoer[0]);
			var files = fw.files;
			$(this).window('destroy');
			callBack.call(this,files);
		},
		onOpen:function(){
			var target = $(this);
			setTimeout(function(){
				var fw = GetFrameWindow(upladoer[0]);
				fw.target = target;
			},100);
		}
	});
};




/**
 * 创建上传窗口 公共方法
 * @param multifile 是否多文件上传
 * @param bis 业务类型
 * @param fk 附件绑定ID
 * @param callBack 上传成功之后的回调
 */
sys.Uploader_01 = function(multifile,bis,fk,filters,maxSize,param,callBack){
	var addWin = $('<div style="overflow: hidden;"/>');
	var upladoer = $('<iframe/>');
	var chunk = false;//（系统不实现切割文件上传）
	upladoer.attr({'src':path+'/plugins/plupload/uploader.jsp?filters='+filters+'&multifile='+multifile+'&maxSize='+maxSize+'&otherUrlAction='+param.otherUrlAction+'&bis='+bis+'&fk='+fk,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
	addWin.window({
		title:"上传文件",
		height:350,
		width:550,
		minimizable:false,
		modal:true,
		collapsible:false,
		maximizable:false,
		resizable:false,
		content:upladoer,
		onClose:function(){
			var fw = GetFrameWindow(upladoer[0]);
			var files = fw.files;
			$(this).window('destroy');
			callBack.call(this,files);
		},
		onOpen:function(){
			var target = $(this);
			setTimeout(function(){
				var fw = GetFrameWindow(upladoer[0]);
				fw.target = target;
			},100);
		}
	});
};


/**
 * 根据iframe对象获取iframe的window对象
 * @param frame
 * @returns {Boolean}
 */
function GetFrameWindow(frame){
	return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentWindow;
}
/**
 * 业务调用
 */
sys.uploadTest = function(bis,fk){
	var filters = "zip,rar,doc,docx,xls,xlsx,ppt,pptx,jpg,gif,png,pdf";
	var maxSize = '5mb';
	var multifile = true;//是否允许多附件上传
	sys.Uploader(multifile,bis,fk,filters,maxSize,function(files){
		 if(files && files.length>0){
			 $.messager.alert("上传完成","成功上传："+files.join(","));
		 }
	});
};
/**
 * 显示Office、Pdf文件预览打印
 * @param uuidName 附件存放在磁盘的真实名字
 * @param type 数据字典：0p
 */
sys.viewOfficePdfPrint = function(uuidName,type){
	window.open(path+'/readOnline.action?uuidName='+uuidName+'&type='+type,'newwindow','top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no, status=no'); 
}
/**
 * 显示Office、Pdf文件预览打印（连续打印多个文档）
 * @param uuidName 附件存放在磁盘的真实名字
 * @param type 数据字典：0p
 * @param name 预览窗口名称
 */
sys.viewMultOfficePdfPrint = function(uuidName,type,name){
	window.open(path+'/readOnline.action?uuidName='+uuidName+'&type='+type,name,'top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no, status=no'); 
}
//人民币金额转大写程序 JavaScript版
sys.numToCny = function (num){
	if(num == null || num == undefined || num == 0 || num == "")
		return "零";
	var capUnit = ['万','亿','万','圆','']; 
	var capDigit = { 2:['角','分',''], 4:['仟','佰','拾','']}; 
	var capNum=['零','壹','贰','叁','肆','伍','陆','柒','捌','玖']; 
	if (((num.toString()).indexOf('.') > 16)||(isNaN(num))) 
		return ''; 
	num = ((Math.round(num*100)).toString()).split('.');
	num = (num[0]).substring(0, (num[0]).length-2)+'.'+ (num[0]).substring((num[0]).length-2,(num[0]).length);
	num =((Math.pow(10,19-num.length)).toString()).substring(1)+num; 
	var i,ret,j,nodeNum,k,subret,len,subChr,CurChr=[]; 
	for (i=0,ret='';i<5;i++,j=i*4+Math.floor(i/4)){
		nodeNum=num.substring(j,j+4); 
		for(k=0,subret='',len=nodeNum.length;((k<len) && (parseInt(nodeNum.substring(k),10)!=0));k++){ 
			CurChr[k%2] = capNum[nodeNum.charAt(k)]+((nodeNum.charAt(k)==0)?'':capDigit[len][k]); 
			if (!((CurChr[0]==CurChr[1]) && (CurChr[0]==capNum[0]))) 
				if(!((CurChr[k%2] == capNum[0]) && (subret=='') && (ret==''))) 
					subret += CurChr[k%2]; 
		} 
		subChr = subret + ((subret=='')?'':capUnit[i]); 
		if(!((subChr == capNum[0]) && (ret==''))) 
			ret += subChr; 
	}
	ret = (ret=='')? capNum[0]+capUnit[3]: ret; 
	return ret; 
};
/**
 * 结束编辑
 */
sys.endEditGrid=function(grid){
    var rows = grid.datagrid('getRows');
    for ( var i = 0; i < rows.length; i++) {
        grid.datagrid('endEdit', i);
    }
};
/**
 * 验证编辑
 * @param grid
 * @return
 */
sys.validateRowGrid=function(grid){
	var rows = grid.datagrid('getRows');
	var r = rows.length;
    for ( var i = 0; i < r; i++) {
        var a = grid.datagrid('validateRow', i);
        if(!a){
        	if(r>1)
        		grid.datagrid('selectRow', i);//选中添加行
            return false;
        }
    }
    return true;
};
/**
 * reload Datagrid
 * @param grid
 * @return
 */
sys.reloadGrid=function(grid){
	grid.datagrid('reload');
};

/********
 * 进度框
 * title:标题
 * msg:信息
 * isShow:true|false
 * ****/
sys.showProcess=function(isShow, title, msg){
	if (!isShow){
		$.messager.progress('close');
		return;
	}
	var win = $.messager.progress({
		title: title,
		msg: msg
	});
};
/***********
 * 加载带下拉选择的combox
 * comboxId 要添加的下拉Id
 * dicId 数据字典对应Id
 * defaultId 默认显示Id
 * **********/
sys.loadcombox=function(comboxId,dicId,defaultId){
	$('#'+comboxId).combobox({   
		url:'dictionary_combov.action?id='+dicId+'&choose=100',   
		valueField:'id',   
		textField:'text',
		value:defaultId
	});
	$('#'+comboxId).combobox('disableTextbox',{stoptype:'readOnly',activeTextArrow:true,stopArrowFocus:true});
};

/***********
 * 加载带下拉选择的comboxgrid
 * comboxId 要添加的下拉Id
 * dicId 数据字典对应Id
 * defaultId 默认值
 * **********/
sys.loadcomboxgrid=function(comboxId,dicId,defaultId){
	$('#'+comboxId).combogrid({   
		url:'dictionary_combovByParam.action?systemId='+dicId+"&time="+new Date(),   
		valueField:'systemId',
		mode: 'remote',
		idField:'systemId',
		panelWidth:350,
		textField:'chineseName',
		value:defaultId,
	    pagination:true,
		rownumbers:true, 
		columns: [[   
		           {field:'chineseName',width:'300',title:'名称'}   
		       ]]
	});
};

/*************
 * 根据给定的comboxId设置disableTextbox
 * ***/
sys.setComboboxDisableText=function(comboxId){
	$('#'+comboxId).combobox('disableTextbox',{stoptype:'readOnly',activeTextArrow:true,stopArrowFocus:true});
};
/*******
 * 确认窗口
 * title:标题
 * msg:信息
 * callback:回调函数
 * ***/
sys.showConfirm=function(title,msg,callback){
	$.messager.confirm(title,msg,function(r){
		if(r){
			if (jQuery.isFunction(callback)){
				callback.call();
			}
		}
	});
};

/****************
 * 设置radio的默认选中值
 * radioName  radio的name名
 * defaultValue 要设置默认选中的值
 * *****/
sys.setDefaultRadioChecked=function(radioName,defaultValue){
	var radioGroup=$("input[name="+radioName+"]");
	if(radioGroup!=null && !(radioGroup==undefined) && radioGroup.length>0){
		for(var v=0;v<radioGroup.length;v++){
			if(defaultValue==radioGroup[v].value){
				radioGroup[v].checked = "checked";
				break;
			}
		}
	}
};

/****************
 * 根据给定的值设置要禁用的radio
 * radioName  radio的name名
 * disvalue 要禁用的radio
 * *****/
sys.setDisableRadio=function(radioName,disvalue){
	var radioGroup=$("input[name="+radioName+"]");
	if(radioGroup!=null && !(radioGroup==undefined) && radioGroup.length>0){
		for(var v=0;v<radioGroup.length;v++){
			if(disvalue==radioGroup[v].value){
				radioGroup[v].disabled = "disabled";
				break;
			}
		}
	}
};

/****************
 * 根据给定的值设置要隐藏的radio
 * radioName  radio的name名
 * disvalue 要隐藏的radio
 * *****/
sys.setNoDisplayRadio=function(radioName,disvalue){
	var radioGroup=$("input[name="+radioName+"]");
	if(radioGroup!=null && !(radioGroup==undefined) && radioGroup.length>0){
		for(var v=0;v<radioGroup.length;v++){
			if(disvalue==radioGroup[v].value){
				radioGroup[v].style.display  = "none";
				$("#"+disvalue+"_id").display="none";
				break;
			}
		}
	}
};

/****************
 * 隐藏除给定值之外的所有radio
 * radioName  radio的name名
 * disvalue 不要隐藏的radio
 * *****/
sys.setNoDisplayRadioExceptCurrent=function(radioName,disvalue){
	var radioGroup=$("input[name="+radioName+"]");
	if(radioGroup!=null && !(radioGroup==undefined) && radioGroup.length>0){
		for(var v=0;v<radioGroup.length;v++){
			if(disvalue!=radioGroup[v].value){
				radioGroup[v].style.display  = "none";
				document.getElementById(radioGroup[v].value+'_id').style.display='none';
			}
		}
	}
};

/*************
 * 根据raido的name得到选中的值
 * radioName  radio的name名
 * ***/
sys.getCheckedRaidoValue=function(radioName){
	return $('input[name='+radioName+']:checked').val();
};

/***************
 * 结束所有行的编辑
 * return true所有行验证成功|false存在行验证失败
 * **/
sys.endAllRowsEdit=function(gridId){
	var $dg=$("#"+gridId);
	var rows = $dg.datagrid('getRows');
	var flg=true;
	if(rows){
		for(var r in rows){
			var vindex=$dg.datagrid('getRowIndex', rows[r]);
			$dg.datagrid('beginEdit', vindex);
			if($dg.datagrid('validateRow',vindex)){
				$dg.datagrid('endEdit', vindex);
			}else{
				flg=false;
			}
		}
	}
	return flg;
};

/**
 * 创建Map对象
 * @returns {___map_0}
 * @since 初始化map_，给map_对象增加方法，使map_像个Map
 */
sys.newMap = function(){
	var map_= new Object();
	//属性加个特殊字符，以区别方法名，统一加下划线_
	map_.put=function(key,value){    map_[key]=value;} 
	map_.get=function(key){    return map_[key];}
	map_.remove=function(key){    delete map_[key];}
	map_.keyset=function(){
		var ret="";
		for(var p in map_){
			if(typeof p == "string" && p.substr(0,1)=="#"){ 
			    ret+=",";
				ret+=p;
			}
		}
		if(ret==""){
			return ret.split(","); //empty array
		}else{
			return ret.substring(1).split(","); 
		}
	}
	map_.removeall=function(){
		var keys = map_.keyset();
		$.each(keys,function(ix,key){
			delete map_[key];
		});
		return;
	}
	return map_;
};
/**
 * 拓展 grid>tr 鼠标进入事件
 */
sys.gridTrMouseEnter=function(gridId){
   var s= $("#"+gridId).datagrid('getPanel');
   var rows = s.find('tr.datagrid-row');
   var rowindex ;
   rows.bind("mouseenter",function(e){
    if((inputFlags.isAltDown)){//如果按住alt键，触发tr点击事件
     $(this).trigger("click");
     }
   });
}

/**
 * 格式化datetimebox
 */
sys.formatDateText=function(date){
	var rainType= 0 ;
	  switch (rainType) {  
	      case '0':  
	              return date.format("yyyy-MM-dd hh:mm");  
	          break;  
	      case '1':  
	              return date.format("yyyy-MM-dd hh");  
	          break;  
	      case '2':  
	              return date.format("yyyy-MM-dd");  
	          break;  
	      case '3':  
	              return date.format("yyyy-MM");  
	          break;  
	      case '4':  
	              return date.format("yyyy-MM");  
	          break;  
	      case '5':  
	              return date.format("yyyy");  
	          break;  
	      default:  
              return date.format("yyyy-MM-dd hh:mm");  
	          break;  
	  }  
	}
/**
 * 查询条件fieldset的显示隐藏
 * @param ths fieldset的legend对象
 * @param toggleId 包含所有查询输入表单的table或div的ID
 * @param layoutId 查询条件所在layout的ID
 * @param showHeight 显示查询条件时，layout的north的高度
 * @param hideHeight 隐藏查询条件时，layout的north的高度（建议为28，不合适可以适当增减）
 */
sys.searchToggle = function(ths,toggleId,layoutId,showHeight,hideHeight){
	var span = $(ths).children("span");
	var cls = span.attr("class");
	if("search_resultset_up_icon"==cls){
		span.attr("class","search_resultset_down_icon");
		$("#"+layoutId).layout("panel","north").panel('resize',{
			height: hideHeight
		});
	}else{
		span.attr("class","search_resultset_up_icon");
		$("#"+layoutId).layout("panel","north").panel('resize',{
			height: showHeight
		});
	}
	$("#"+toggleId).toggle();
	$("#"+layoutId).layout("resize");
}
/*扩展Editors的datetimebox方法*/
 $.extend($.fn.datagrid.defaults.editors, {
        datetimebox: {//为方法取名
            init: function (container, options) {
                var editor = $('<input />').appendTo(container);
                options.editable = false;//设置其不能手动输入
                editor.datetimebox(options);
                return editor;
            },
            getValue: function (target) {//取值
                return $(target).datetimebox('getValue');
            },
            setValue: function (target, value) {//设置值
                $(target).datetimebox('setValue', value);
            },
            resize: function (target, width) {
                $(target).datetimebox('resize', width);
            },
            destroy: function (target) {
                $(target).datetimebox('destroy');//销毁生成的panel
            }
        }
    });
 
 /**  
  * @author 
  */  
 $.extend($.fn.tabs.methods, {   
     /**
      * tabs组件每个tab panel对应的小工具条tools绑定的事件没有传递事件参数  
      * 本函数修正这个问题  
      * @param {[type]} jq [description]  
      */  
     addEventParam: function(jq) {   
         return jq.each(function() {   
             var that = this;   
             var headers = $(this).find('>div.tabs-header>div.tabs-wrap>ul.tabs>li');   
             headers.each(function(i) {   
                 var tools = $(that).tabs('getTab', i).panel('options').tools;   
                 if (typeof tools != "string") {   
                     $(this).find('>span.tabs-p-tool a').each(function(j) {   
                         $(this).unbind('click').bind("click", {   
                             handler: tools[j].handler   
                         }, function(e) {   
                             if ($(this).parents("li").hasClass("tabs-disabled")) {   
                                 return;   
                             }   
                             e.data.handler.call(this, e);   
                         });   
                     });   
                 }   
             })   
         });   
     }   
 }); 
