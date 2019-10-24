package com.ibb.common.util;

public class StringBufferUtils {
	/**
	 * 方法说明：使用StringBuffer拼接字符串
	 * @param strings
	 * @return
	 * @author Ou
	 */
	public static StringBuffer getBuffer(String... strings){
		StringBuffer sb = new StringBuffer();
		for (String str : strings){
			if(str!=null){
				sb.append(str);
			}
		}
		return sb;
	}
	
	/**
	 * 方法说明：使用StringBuffer拼接字符串
	 * @param strings
	 * @return
	 * @author Ou
	 */
	public static String getString(String... strings){
		StringBuffer sb = new StringBuffer();
		for (String str : strings){
			if(str!=null){
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(StringBufferUtils.getBuffer("**","asdf","***"));
		System.out.println(StringBufferUtils.getBuffer("**","asdf"));
		System.out.println(StringBufferUtils.getBuffer("**"));
	}
}
