/**
 * 格式化表格数据
 */
var ExtendFormatter = {
	//数据字典格式化
	DictFormatter: function (dictName,value) {
		if (value == undefined || value == null || value == ''){
			return "";
		}
		$.get(ctx + '/mgr/dict/getDictText/' + dictName + '/' + value, function(data){
			return data;
		});
	},
		
    //EasyUI用DataGrid用日期格式化
    TimeFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }
        /*json格式时间转js时间格式*/
        value = value.substr(1, value.length - 2);
        var obj = eval('(' + "{Date: new " + value + "}" + ')');
        var dateValue = obj["Date"];
        if (dateValue.getFullYear() < 1900) {
            return "";
        }
        var val = dateValue.format("yyyy-MM-dd HH:mm");
        return val.substr(11, 5);
    },
    
    DateTimeFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }
        /*json格式时间转js时间格式*/
        value = value.substr(1, value.length - 2);
        var obj = eval('(' + "{Date: new " + value + "}" + ')');
        var dateValue = obj["Date"];
        if (dateValue.getFullYear() < 1900) {
            return "";
        }

        return dateValue.format("yyyy-MM-dd HH:mm");
    },

    //EasyUI用DataGrid用日期格式化
    DateFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }else if(value instanceof Date){
        	return value.format("yyyy-MM-dd");
        }else if(value instanceof String){
        	value = value.substr(1, value.length - 2);
            var obj = eval('(' + "{Date: new " + value + "}" + ')');
            var dateValue = obj["Date"];
            if (dateValue.getFullYear() < 1900) {
                return "";
            }

            return dateValue.format("yyyy-MM-dd");
        }else{
        	var dateValue = new Date(value);  
            return dateValue.format("yyyy-MM-dd");
        }
    }
};

//日期格式化
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}