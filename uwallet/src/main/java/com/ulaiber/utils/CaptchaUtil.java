package com.ulaiber.utils;

import java.util.HashMap;
import java.util.Map;

public class CaptchaUtil {
	
	//短信接口的api key
	public static final String API_KEY = "c5deeceadc1ee518d9220cf86ab130f1";
	
	//智能匹配模板发送接口的http地址
    private static final String URI_SEND_SMS = "https://sms.yunpian.com/v2/sms/single_send.json";
	
	
	/** 单条短信发送,智能匹配短信模板
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
	    String response = HttpsUtil.doPost(URI_SEND_SMS, params);
//	    {"code":0,"msg":"发送成功","count":1,"fee":1.0,"unit":"COUNT","mobile":"15919477086","sid":16137631166}
//	    {"http_status_code":400,"code":-3,"msg":"IP没有权限","detail":"IP 61.141.64.217 未加入白名单,可在后台‘系统设置->IP白名单设置’里添加"}
	    return response;
	}
	
	/**
	 * 生成6位数的验证码
	 * 
	 * @return
	 */
	public static String getCaptcha(){
		return String.valueOf((int)((Math.random() * 9 + 1) * 100000));
	}
}
