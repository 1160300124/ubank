package com.ulaiber.web.utils;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MD5Util {

	private static final String MAC_NAME = "HmacSHA1"; 
	private static final String ENCODING = "UTF-8"; 
	private static final String KEY = "ulaiber2017";

	/*** 
	 * MD5加码 生成32位md5码 
	 */ 
	public static String string2MD5(String inStr){ 
		MessageDigest md5 = null; 
		try{ 
			md5 = MessageDigest.getInstance("MD5"); 
		}catch (Exception e){ 
			System.out.println(e.toString()); 
			e.printStackTrace(); 
			return ""; 
		} 
		char[] charArray = inStr.toCharArray(); 
		byte[] byteArray = new byte[charArray.length]; 

		for (int i = 0; i < charArray.length; i++) 
			byteArray[i] = (byte) charArray[i]; 
		byte[] md5Bytes = md5.digest(byteArray); 
		StringBuffer hexValue = new StringBuffer(); 
		for (int i = 0; i < md5Bytes.length; i++){ 
			int val = ((int) md5Bytes[i]) & 0xff; 
			if (val < 16) 
				hexValue.append("0"); 
			hexValue.append(Integer.toHexString(val)); 
		} 
		return hexValue.toString(); 

	} 

	/*** 
	 * MD5加密 生成32位md5码 
	 */ 
	public static String getEncryptedPwd(String inStr){ 
		return string2MD5(convertSHA1(string2MD5(inStr))); 

	}

	/** 
	 * 加密解密算法
	 */ 
	public static String convertMD5(String inStr){ 

		char[] a = inStr.toCharArray(); 
		for (int i = 0; i < a.length; i++){ 
			a[i] = (char) (a[i] ^ 't'); 
		} 
		String s = new String(a); 
		return s; 

	} 

	/** 
	 * HMAC-SHA1 
	 * @param encryptText 
	 * @param encryptKey 
	 * @return 
	 * @throws Exception 
	 */ 
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception 
	{ 
		byte[] data = encryptKey.getBytes(ENCODING); 
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME); 
		Mac mac = Mac.getInstance(MAC_NAME); 
		mac.init(secretKey); 

		byte[] text = encryptText.getBytes(ENCODING); 
		byte[] str = mac.doFinal(text);
		// Create Hex String
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制数
		for (int i = 0; i < str.length; i++) {
			String shaHex = Integer.toHexString(str[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	} 

	public static String convertSHA1(String instr){
		try {
			return HmacSHA1Encrypt(instr, KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static boolean validatePwd(String password, String encryptedPwd){
		
		return encryptedPwd.equals(getEncryptedPwd(password));
	}


}
