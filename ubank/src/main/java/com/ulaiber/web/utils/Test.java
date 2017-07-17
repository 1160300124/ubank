package com.ulaiber.web.utils;

import java.util.HashMap;
import java.util.Map;

public class Test {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
//		UUID uuid = UUID.randomUUID();
//        System.out.println(uuid.toString().replaceAll("-", ""));
//        String s = singleSend("c5deeceadc1ee518d9220cf86ab130f1", "【应用小宝你的验证码是12314345", "15919477086");
//        System.out.println(s);
//        int n = 0 ;
//
//		while(n < 100000)
//			n = (int)(Math.random()*1000000);
//		
//		System.out.println(n);
//		System.out.println(Math.random());
//		System.out.println(CaptchaUtil.getCaptcha());
        
//		
//        
//        String tt = MD5Util.getEncryptedPwd("ubank123456!");
//        System.out.println(tt);
//
//        //MD5加密
//        String s = new String("ubank123456!"); 
//        System.out.println("原始：" + s); 
//        System.out.println("-------------：" + MD5Util.validatePwd(s, MD5Util.getEncryptedPwd(s))); 
//        System.out.println("MD5加密后：" + MD5Util.getEncryptedPwd(s));
		
//		String s = "/uwallet/backend/tomanager";
//		System.out.println(s.substring("/uwallet".length()));
        
//      -----------------------------------------------------------------
//        String apiUrl = "http://localhost:8080/uwallet/api/v1/register";
//        Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//	    params.put("userName", "lisi");
//	    params.put("login_password", "1234567");
//	    params.put("pay_password", "1234567");
//	    params.put("mobile", "15919477089");
//	    params.put("reserve_mobile", "12345678910");
//	    params.put("cardType", 1);
//	    params.put("bankNo", "305100000013");
//	    params.put("bankCardNo", "1234567890");
//	    params.put("secondBankCardNo", "6224080600234");
//	    params.put("cardNo", "12312412341234124");
//	    params.put("remark", "hahahahahahahahahaha");
//        String response = HttpsUtil.doPost(apiUrl, params);
//        System.out.println(response);
        
//      String apiUrl = "http://localhost:8080/uwallet/api/v1/login";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "18503036206");
//      params.put("login_password", "123456");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);
		
      String apiUrl = "http://localhost:8080/uwallet/api/v1/getUserInfo";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
      params.put("mobile", "18503036206");
      String response = HttpsUtil.doPost(apiUrl, params);
      System.out.println(response);
        
//      String apiUrl = "http://10.17.1.136:8080/uwallet/api/v1/sendCaptcha";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "15919477086");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);
		
//      String apiUrl = "http://localhost:8080/uwallet/api/v1/validate";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "15919477086");
//      params.put("captcha", "123456");
//      params.put("password", "123456");
//      params.put("confirm_password", "123456");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);
		
	}
	
	/* 单条短信发送,智能匹配短信模板
	 *
	 * @param apikey 成功注册后登录云片官网,进入后台可查看
	 * @param text   需要使用已审核通过的模板或者默认模板
	 * @param mobile 接收的手机号,仅支持单号码发送
	 * @return json格式字符串
	 */
	public static String singleSend(String apikey, String text, String mobile) {
	    Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
	    params.put("apikey", apikey);
	    params.put("text", text);
	    params.put("mobile", mobile);
	    return HttpsUtil.doPost("https://sms.yunpian.com/v2/sms/single_send.json", params);
//	    return post("https://sms.yunpian.com/v2/sms/single_send.json", params);//请自行使用post方式请求,可使用Apache HttpClient
	}
	 

}
