package com.ibb.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类
 */

public class DateUtil {
	/**
	 * 默认日期格式：yyyy/MM/dd
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";

	/**
	 * 默认日期格式：yyyyMMdd
	 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	
	/**
	 * 默认时间格式：HH:mm:ss
	 */
	public static final String TIME_PATTERN = "HH:mm:ss";
	/**
	 * 默认时间格式：HHmmss
	 */
	public static final String TIME_MISS_PATTERN = "HHmmssS";

	/**
	 * 默认时间格式：yyyy/MM/dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 默认字符串格式：yyyyMMddHHmmss
	 */
	public static final String DEFAULT_STRING_PATTERN = "yyyyMMddHHmmss";

	/**
	 * 默认字符串格式：yyyyMMddHHmmssSSS
	 */
	public static final String DEFAULT_STRING_PATTERN17 = "yyyyMMddHHmmssSSS";

	/**
	 * 需要的字符串格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_STRINGFORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 需要的字符串格式：yyyyMMdd
	 */
	public static final String DEFAULT_SHORT_DATE_PATTERN = "yyyyMMdd";

	/**
	 * 默认时间戳格式，到毫秒 yyyy-MM-dd HH:mm:ss SSS
	 */
	public static final String DEFAULT_MILLISECOND_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 1天折算成毫秒数
	 * 24 * 3600 * 1000 = 86400000
	 */
	public static final long MILLIS_A_DAY = 86400000;

	/**
	 * 获取时间的毫秒数
	 * @param date 时间字符串
	 * @param pattern 格式
	 * @return 毫秒数
	 */
	public static long getMillis(String date,String pattern) {
		return getMillis(parse(date,pattern));
	}

	/**
	 * 获取时间的毫秒数
	 * @param date 时间
	 * @return 毫秒数
	 */
	public static long getMillis(Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		try {
			c.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTimeInMillis();
	}

	
	/**
	 * 获取今天时间，格式：{@link #DEFAULT_SHORT_DATE_PATTERN}
	 * @return
	 */
	public static String getToday(){
		return getToday(DEFAULT_SHORT_DATE_PATTERN);
	}
	
	/**
	 * 获取当前时间
	 * @param pattern 格式
	 * @return 时间的字符串
	 */
	public static String getToday(String pattern){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 获取当前时间，格式：{@link #DEFAULT_STRING_PATTERN}
	 * @return
	 */
	public static String getCurrFullDate() {
		return getToday(DEFAULT_STRING_PATTERN);
	}

	/**
	 * 获取当前时间，格式：{@link #DEFAULT_STRING_PATTERN17}
	 * @return
	 */
	public static String getCurrFullDateWithMillSec() {
		return getToday(DEFAULT_STRING_PATTERN17);
	}
	
	/**
	 * 获取当前时间，格式：{@link #TIME_PATTERN}
	 * @return
	 */
	public static String getCurrPartTime() {
		return getToday(TIME_PATTERN);
	}
	
	/**
	 * 获取当前时间，格式：{@link #TIME_MISS_PATTERN}
	 * @return
	 */
	public static String getCurrPartTimeMiss() {
		return getToday(TIME_MISS_PATTERN);
	}
	
	/**
	 * 转换Date为字符格式
	 * @param date 时间
	 * @param pattern 格式
	 * @return
	 */
	public static String format(Date date,String pattern) {
		if(date == null){
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	} 
	
	/**
	 * 转换Date为字符格式
	 * @param date 时间 {@link #DEFAULT_STRING_PATTERN}
	 * @return
	 */
	public static String format(Date date) {
		return format(date,DEFAULT_STRING_PATTERN);
	}

	/**
	 * 转换时间字符串为时间Date
	 * 转换出错时，返回null
	 * 格式：{@link #DATE_PATTERN}
	 * @param dateStr 时间字符串
	 * @return
	 */
	public static Date parse(String dateStr) {
		return parse(dateStr, DATE_PATTERN);
	}

	/**
	 * 转换时间字符串为时间Date
	 * 转换出错时，返回null
	 * @param dateStr 时间字符串
	 * @param pattern 格式
	 * @return
	 */
	public static Date parse(String dateStr,String pattern) {
		if(dateStr == null || "".equals(dateStr)){
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * <pre>
	 * 获取两时间相差多少天
	 * </pre>
	 * @param startday 开始时间
	 * @param endday 结束时间
	 * @return 天数
	 */
	public static int getDiffDays(Date startday,Date endday){        
        if(startday.after(endday)){
            Date cal=startday;
            startday=endday;
            endday=cal;
        }        
        long sl=startday.getTime();
        long el=endday.getTime();       
        long ei=el-sl;           
        return (int)(ei/(MILLIS_A_DAY));
    }
	
	/**
	 * 获取当前时间，增加月份
	 * @param pattern 格式
	 * @param count 月份
	 * @return 返回字符串形式
	 */
	public static String addMonth(String pattern,int count){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, count);
		return format(c.getTime(),pattern);
	}
	/**
	 * 获取当前时间，增加天数
	 * @param pattern 格式
	 * @param day 天数
	 * @return 返回字符串形式
	 */
	public static String addDay(String pattern,int day){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);
		return format(c.getTime(),pattern);
	}
	
	/**
	 * 获取半年前的时间
	 * @param dateMonth 格式yyyyMM
	 * @return 半年前的时间字符串
	 */
	public static String getLastMonth(String dateMonth){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		Date date = DateUtil.parse(dateMonth, "yyyyMM");
		Calendar c = Calendar.getInstance(); 
		c.setTime(date);
		c.add(Calendar.MONTH, -6);
		date=c.getTime();
		return formatter.format(date);
		
	} 

	/**
	 * 获取昨天时间,格式：{@link #DATE_PATTERN}
	 * @return
	 */
	public static String getYesterday(){
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.DAY_OF_MONTH, -1);
		return formatter.format(c.getTime());
	}
	
}