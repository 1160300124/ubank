package com.ulaiber.web.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sun.deploy.net.HttpUtils;
import com.ulaiber.web.model.LeaveRecord;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import com.ulaiber.web.conmon.IConstants;

public class Test {
	public static final String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKh7bxbe5dhwuZzNkeDXrpHx7o5k+uIWrbY1+8c6Oqgq2AfajnK5v10OfQW85xNUn/4TzoRcCOCaK2LZO4QJoQmgs41x45jZNvI/f8EGcJvt2oCs3S2Da98+v6VVfDXoSfgeHlNRcDSYlZF2E31KQLtdTGva9IeECx2CpPIkbVeXAgMBAAECgYA0QlUq2uigQhbQtFLTUxMq4cgFEv1es3oeUpBOM5mOH/vyM7CLlWHuE1hkNzvVmyIlRS+BjqqSQD/E4Wy8f+AbAznky5F8q5Afe5ZKxi+n2M4ZMgh9uryVMcAHCXu1RnOrtsnGjJvp23ku4wZtWCHLNAuQfI9zj6ncq4v50RKKQQJBAN8tSbcT37Mq3Y50zVnnmGxNEgUZKJXwPw/KnO6EeR/Nfpmzy40GQU8y+GGoq5cVY5NDOqYVi6nh21mLXQij9AsCQQDBQt0zU8zurSNaoRYsjhNrHJHQjBt0WuIxtluZz44CTbNQw5/3kA1jVvt1EXOE1hF+l2QVIuLgvgIeHDwvenYlAkAeiusgtAaUVZR2r4N+/1P71lxV+Eh2pKdsuNTbS6Pr90qRLGr6BNYhSZ92dgftqE61U6kOG7q+aBuF2K3FxfJbAkA1oCES4fjmbYJ23mXxvQakXQwU6xufIKzNEIXAWzhTaU4NZgrYPc+JNhSWOl5siJ3YG5f4yXJc3DxoMHt+zSNFAkEA25eGORpWtXERhGXHZR8f3nWIHTAF+EM1O8T7YXp+y7sWs8YvtNI3ZtzjzncHWk4YdScNqkXC1UJz2hokLUR7Aw==";
	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoe28W3uXYcLmczZHg166R8e6OZPriFq22NfvHOjqoKtgH2o5yub9dDn0FvOcTVJ/+E86EXAjgmiti2TuECaEJoLONceOY2TbyP3/BBnCb7dqArN0tg2vfPr+lVXw16En4Hh5TUXA0mJWRdhN9SkC7XUxr2vSHhAsdgqTyJG1XlwIDAQAB";


	public static void main(String[] args) {
//		UUID uuid = UUID.randomUUID();
//        System.out.println(uuid.toString().replaceAll("-", ""));
//        String s = singleSend("c5deeceadc1ee518d9220cf86ab130f1", IConstants.SMS_TEMPLATE.replace("#code#", CaptchaUtil.getCaptcha()), "15919477086");
//        System.out.println(s);
		
//        int n = 0 ;
//		while(n < 10000)
//			n = (int)(Math.random()*100000);
//		
//		System.out.println(n);
//		System.out.println(Math.random());
//		System.out.println(CaptchaUtil.getCaptcha());
        
//		
//        6700e28167adc46e61422ddbabea87e9
//        String tt = MD5Util.getEncryptedPwd("123456");
//        System.out.println(tt);
//
//        //MD5加密
//        String s = new String("123456"); 
//        System.out.println("原始：" + s); 
//        System.out.println("-------------：" + MD5Util.validatePwd(s, MD5Util.getEncryptedPwd(s))); 
//        System.out.println("MD5加密后：" + MD5Util.getEncryptedPwd(s));
		
//		String s = "/uwallet/backend/tomanager";
//		System.out.println(s.substring("/uwallet".length()));
        
//      -----------------------------------------------------------------
//        String apiUrl = "http://localhost:8080/ubank/api/v1/register";
//        Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//	    params.put("userName", "黄国清");
//	    params.put("login_password", "123456");
//	    params.put("pay_password", "123456");
//	    params.put("mobile", "15919477086");
//	    params.put("reserve_mobile", "15919477086");
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
		
//      String apiUrl = "http://localhost:8080/uwallet/api/v1/getUserInfo";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "18503036206");
//      String response = HttpsUtil.doPost(apiUrl, params);
//      System.out.println(response);
        
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
		
//      String apiUrl = "http://localhost:8080/uwallet/api/v1/updateForBankCard";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "15919477086");
//      params.put("bankCardNo", "123456");
//      params.put("bankNo", "123456");
//      params.put("reserve_mobile", "123456");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);
		
		
//		FileUtil.copy(new File("C:\\tools\\test\\haha\\body.jpg"), new File("C:\\tools\\test\\"));
//		FileUtil.deleteAllFilesOfDir("C:\\tools\\test\\");
		
//		String filename = "fileaaaaa.aaaa.jpg";
//		String uuid = UUIDGenerator.getUUID();
//        String newFilename = uuid + filename.substring(filename.lastIndexOf("."), filename.length());
//        System.out.println(uuid);
//        System.out.println(newFilename);
		
//		String clock_on_time = "09:30";
//		String clock_off_time = "18:30";
//		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String date = datetime.split(" ")[0];
//		String time = datetime.split(" ")[1];
//		System.out.println("--------------" + date + "-------------" + time);
        
//		String time = DateTimeUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm");
//        System.out.println(time);
//        System.out.println(DateTimeUtil.getfutureTime(time, 0, 12, 0));
//        System.out.println(DateTimeUtil.getfutureTime(time, -1, 0, 0).split(" ")[0]);
//		System.out.println(DateTimeUtil.getWeekday("2017-08-26"));
		String[] holidays = {"2017-08-26", "2017-08-27" ,"2017-08-28"};
		System.out.println(Arrays.asList(holidays));
		System.out.println(DateTimeUtil.getYear(new Date()));
		
		String daytime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_DAYTIME);
		String clockOnStartTime = DateTimeUtil.getfutureTime(daytime + " " + "10:30", 0, -2, 0).split(" ")[1];
		String clockOffEndTime = DateTimeUtil.getfutureTime(daytime + " " + "19:00", 0, 6, 0).split(" ")[1];
		System.out.println(clockOnStartTime + "--------------------" + clockOffEndTime);
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


	/**----------------------------------------------------------------------------------------------*/
	//申请请假test
    @org.junit.Test
	public void apply(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid","365");
		map.put("leaveType","2");
		map.put("leaveTime",24);
		map.put("startDate","2017-08-24");
		map.put("endDate","2017-08-25");
		map.put("auditor","364,366");
		map.put("reason","生病，去一医院看病");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/applyForLeave",map);
		System.out.print(result);
	}

	//查询个人申请记录
	@org.junit.Test
	public void queryApplyRecord(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid","369");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/queryApplyRecord",map);
		System.out.print(result);
	}

	//取消请假申请
	@org.junit.Test
	public void cancelapply(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("applyId","106");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/cancelApply",map);
		System.out.print("~~~~~~~~取消申请结果为："+result);
	}

	//查询工作提醒
	@org.junit.Test
	public void getWorkRemind(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","365");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getWorkRemind",map);
		System.out.print("~~~~~~~~工作提醒结果为："+result);
	}

	//查询工作审批
	@org.junit.Test
	public void getWorkAudit(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","365");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getWorkAudit",map);
		System.out.print("~~~~~~~~工作审批结果为："+result);
	}

	//待审批数据查询
	@org.junit.Test
	public void queryAuditRecord(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","365");
		map.put("mark","0");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/queryAuditRecord",map);
		System.out.print("~~~~~~~~工作审批结果为："+result);
	}

	//确认审批
	@org.junit.Test
	public void confirmAudit(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","365");
		map.put("recordNo","106");
		map.put("status","1");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/confirmAudit",map);
		System.out.print("~~~~~~~~工作审批结果为："+result);
	}

}
