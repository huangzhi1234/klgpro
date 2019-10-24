package com.ibb.common.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密工具
 * 
 * @author kin wong
 */
public class SecureUtil {
	private static final String DES_KEY = "KIN_WONG";
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static Key key;
	static{
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom(DES_KEY.getBytes()));
			key = generator.generateKey();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 单向加密
	 * 
	 * @param algorithm 加密方式 MD5 SHA
	 * @param str  明文
	 * @return 密文
	 */
	public static String encode(String algorithm, String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * DES 加密
	 * @param strMing 明文
	 * @return 密文
	 */
	public static String getEncString(String strMing) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = getDesOrEncCode(Cipher.ENCRYPT_MODE, byteMing);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * DES 解密
	 * @param strMi 密文
	 * @return 明文
	 */
	public static String getDesString(String strMi) {
		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = base64De.decodeBuffer(strMi);
			byteMing = getDesOrEncCode(Cipher.DECRYPT_MODE, byteMi);
			strMing = new String(byteMing, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	private static byte[] getDesOrEncCode(int mode, byte[] abyte){
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byteFina = cipher.doFinal(abyte);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	public static void main(String args[]){
		String ming = "cpan2016#$";
		String mi = "WEfWcSoAGDk=";
		
		System.out.println("==============单向加密=================");
		//System.out.println("MD5加密：" + ming + " --> " + encode("MD5", ming));
		System.out.println("SHA加密：" + ming + " --> " + encode("SHA", ming));
		//System.out.println("==============双向加解密=================");
		//System.out.println("DES加密：" + ming + " --> " + getEncString(ming));
		//System.out.println("DES解密：" + mi + " --> " + getDesString(mi));
	}
}
