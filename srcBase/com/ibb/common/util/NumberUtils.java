package com.ibb.common.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

import exception.IbbMgrException;


/**
 * 数字工具类
 */
public abstract class NumberUtils {
	private static final Logger logger = Logger.getLogger(NumberUtils.class);
	/**
	 * 字符串转到长整形数据
	 * 
	 * @param str
	 *            数字字符串
	 * @param defValue
	 *            默认值,如果给定str是一个null,则返回默认值
	 * @return
	 */
	public static long toLong(String str,
			long defValue) {
		if (str == null) {
			return defValue;
		}
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException nfe) {
			logger.warn(nfe);
			return defValue;
		}
	}

	public static int toInt(String str,
			int defValue) {
		if (str == null) {
			return defValue;
		}
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			logger.warn(nfe);
			return defValue;
		}
	}

	/**
	 * 字符串转到长整形数据
	 * 
	 * @param str
	 *            数字字符串
	 * @return
	 */
	public static long toLong(String str) {
		return toLong(str, 0);
	}

	public static int toInt(String str) {
		return toInt(str, 0);
	}

	/**
	 * 查看当前字符按是否全部是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigits(String str) {
		if (StringUtils.isNull(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 转换成Double
	 * 
	 * @param str
	 * @return
	 */
	public static Double toDouble(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}

		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}
	}

	/**
	 * isNumber 方法
	 * <p>
	 * 判断给定对象是否为整数字，包含正负数。
	 * </p>
	 */
	public static boolean isNumber(Object target) {
		if (target == null) {
			return false;
		}

		if (target instanceof Integer || target instanceof Long || target instanceof Double
				|| target instanceof BigDecimal) {
			return true;
		}

		if (!(target instanceof String)) {
			return false;
		}

		String str = target.toString();
		if (str.startsWith("-")) {
			str = str.substring(1);
		}

		return isDigits(str);
	}
	
	/**
	 * 金额转换成中文大写（传入值需先做四舍五入处理）
	 * @param value
	 * @author lyl
	 * @return
	 */
	public static String changeMoney(double value){
		char[] hunit={'拾','佰','仟'};                                               //段内位置表示
	    char[] vunit={'万','亿'};                                                     //段名表示
	    char[] digit={'零','壹','贰','叁','肆','伍','陆','柒','捌','玖'};  //数字表示
	    long midVal = (long)(value*100);                                      //转化成整形
	    String valStr=String.valueOf(midVal);                                //转化成字符串
	    String head=valStr.substring(0,valStr.length()-2);               //取整数部分
	    String rail=valStr.substring(valStr.length()-2);                     //取小数部分

	    String prefix="";                                                                 //整数部分转化的结果
	    String suffix="";                                                                  //小数部分转化的结果
	    //处理小数点后面的数
	    if(rail.equals("00")){                                                            //如果小数部分为0
	        suffix="整";
	    }else{
	       suffix=digit[rail.charAt(0)-'0']+"角"+digit[rail.charAt(1)-'0']+"分";        //否则把角分转化出来
	    }
	     //处理小数点前面的数
	    char[] chDig=head.toCharArray();                                                         //把整数部分转化成字符数组
	    char zero='0';                                                                                          //标志'0'表示出现过0
	    byte zeroSerNum = 0;                                                                            //连续出现0的次数
	    for(int i=0;i<chDig.length;i++){                                                               //循环处理每个数字
	    	
	    	int idx=(chDig.length-i-1)%4;                                                                //取段内位置
	    	int vidx=(chDig.length-i-1)/4;                                                                //取段位置
	    	if(chDig[i]=='0'){                                                                                  //如果当前字符是0
	    		zeroSerNum++;                                                                                 //连续0次数递增
	    		if(zero == '0'){                                                                                    //标志
	    			zero=digit[0];
		         	} else if(idx==0 && vidx >0 &&zeroSerNum < 4){
		         		prefix += vunit[vidx-1];
		         		zero='0';
		         	}
	    		continue;
	    	}
	    	zeroSerNum = 0;                                                                                    //连续0次数清零
	    	if(zero != '0') {                                                                                        //如果标志不为0,则加上,例如万,亿什么的
	    		prefix+=zero;
	    		zero='0';
	    	}
	    	prefix+=digit[chDig[i]-'0'];                                                                        //转化该数字表示
	    	if(idx > 0) prefix += hunit[idx-1];                  
	    	if(idx==0 && vidx>0){
	    		prefix+=vunit[vidx-1];                                                                             //段结束位置应该加上段名如万,亿
	    	}
	    }
	    if(prefix.length() > 0) prefix += '元';                                                             //如果整数部分存在,则有圆的字样
	    return prefix+suffix;                                                                                     //返回正确表示
	 }
	
	/**
	 * 百分比转为小数
	 * @param value
	 * @return
	 */
	public static double percentToDouble(String value){
		double result=0.00;
		if(value.contains("%")) {  
			value = value.replaceAll("%", "");  
			result = Double.valueOf(value)/100;
	}
		return result;
	}
	/**
	 * 方法说明：四舍五入，保留digit位小数
	 * @param num 要格式化的数(47392.307692)
	 * @param digit 保留的小数，等于0时小数位会存在47393.0(2)
	 * @return (47392.31)
	 * @author Ou
	 * 2014-5-8
	 */
	public static double decimal(double num,int digit){
		double result = 0.0;
		try {
			/*此方法，当被进位满10时无法再向前近1。（如：7.595保留两位小数时，得7.59而并不是7.60）
			BigDecimal bg = new BigDecimal(num);
			result = bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
			*/
			
		    StringBuffer f = new StringBuffer("###");
		    if(digit<0){
		    	throw new Exception("保留的小数必须大于等于0。");
		    }else{
		    	for(int i=0;i<digit;i++){
		    		if(i==0){
		    			f.append(".");
		    		}
		    		f.append("#");
		    	}
		    }
		  	//DecimalFormat df_num = new DecimalFormat("###,###.##");
		    //df_num.setRoundingMode(RoundingMode.HALF_UP);
		    DecimalFormat df_num2 = new DecimalFormat(f.toString());
		    df_num2.setRoundingMode(RoundingMode.HALF_UP);  
		    result = NumberUtils.toDouble(df_num2.format(num));
		} catch (Exception e) {
			throw new RuntimeException("四舍五入出错。");
		}
		return result;
	}
	/**
	 * 方法说明：四舍五入，保留digit位小数（数字加","进行格式化）
	 * @param num 要格式化的数(47392.307692)
	 * @param digit 保留的小数(2)
	 * @return (47,392.31)
	 * @author Ou
	 * 2014-5-8
	 */
	public static String decimalFraction(double num,int digit){
		String result = null;
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(digit);
			result = nf.format(num);
		} catch (Exception e) {
			throw new RuntimeException("四舍五入出错。");
		}
		return result;
	}	
	
	public static void main(String[] args) {
		System.out.println(NumberUtils.decimal(47392.5953, 2));
		System.out.println(NumberUtils.decimalFraction(47392.395, 2));
	}
}
