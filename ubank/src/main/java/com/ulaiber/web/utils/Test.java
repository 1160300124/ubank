package com.ulaiber.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.ReimbursementVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;

import javax.xml.crypto.dsig.TransformException;

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

//      String apiUrl = "http://localhost:8080/ubank/api/v1/login";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "18503036206");
//      params.put("login_password", "123456");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);

      String apiUrl = "http://localhost:8080/ubank/api/v1/getUserInfo";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
      params.put("mobile", "15919477081");
      String response = HttpsUtil.doPost(apiUrl, params);
      System.out.println(response);

//      String apiUrl = "http://10.17.1.136:8080/ubank/api/v1/sendCaptcha";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "15919477086");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);

//      String apiUrl = "http://localhost:8080/ubank/api/v1/validate";
//      Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
//      params.put("mobile", "15919477086");
//      params.put("captcha", "123456");
//      params.put("password", "123456");
//      params.put("confirm_password", "123456");
//      String response = HttpsUtil.doPost1(apiUrl, params);
//      System.out.println(response);

//      String apiUrl = "http://localhost:8080/ubank/api/v1/updateForBankCard";
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

		//随机生成n位数数字
        System.out.println(RandomStringUtils.randomNumeric(5));
        //在指定字符串中生成长度为n的随机字符串
        RandomStringUtils.random(5, "abcdefghijk");
        //指定从字符或数字中生成随机字符串
        System.out.println(RandomStringUtils.random(5, true, false));
        System.out.println(RandomStringUtils.random(5, false, true));

		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		ids.add(3L);
		ids.add(4L);
		System.out.println(ids.contains(1L));
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
		map.put("userid","428");
		map.put("leaveType","1");
		map.put("leaveTime",24);
		map.put("startDate","2017-08-24");
		map.put("endDate","2017-08-25");
		map.put("auditor","431");
		map.put("reason","请假zzzXXXXXXXXXXXXXXXXXXX");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/applyForLeave",map);
		System.out.print(result);
	}

	//查询个人申请记录
	@org.junit.Test
	public void queryApplyRecord(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","359");
		map.put("pageNum",1);
		map.put("pageSize",20);
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/queryApplyRecord",map);
		System.out.print(result);
	}

	//取消请假申请
	@org.junit.Test
	public void cancelapply(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("applyId","106");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/cancelApply",map);
		System.out.print(">>>>>>>>取消申请结果为："+result);
	}

	//查询工作提醒
	@org.junit.Test
	public void getWorkRemind(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","384");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getWorkRemind",map);
		System.out.print(">>>>>>>>工作提醒结果为："+result);
	}

	//查询工作审批
	@org.junit.Test
	public void getWorkAudit(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","384");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getWorkAudit",map);
		System.out.print(">>>>>>>>工作审批结果为："+result);
	}

	//待审批数据查询
	@org.junit.Test
	public void queryAuditRecord(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","359");
		map.put("mark","1");
		map.put("pageNum",1);
		map.put("pageSize",20);
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/queryAuditRecord",map);
		System.out.print(">>>>>>>>待审批数据为："+result);
	}

	//确认审批
	@org.junit.Test
	public void confirmAudit(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","424");
		map.put("currentUserId", "423");
		map.put("recordNo","335");
		map.put("status","0");
		map.put("currentAuditor","张三三");
		map.put("auditorStatus","1");
		map.put("reason","同意同意同意同意同意同意xxxxxxxxxxx");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/confirmAudit",map);
		System.out.print(">>>>>>>>工作审批结果为："+result);
	}

	//获取消息总数
	@org.junit.Test
	public void messageTotal(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","384");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/messageTotal",map);
		System.out.print(">>>>>>>>工作提醒结果为："+result);
	}


	/**-----------------------------------------------------------------------------------------------*/

	//新增加班记录
	@org.junit.Test
	public void addOvertimeRecord(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("leaveType","1");
		map.put("userid","359");
		map.put("startDate","2017-08-24");
		map.put("endDate","2017-08-25");
		map.put("leaveTime",8.0);
		map.put("auditor","303");
		map.put("reason","测试测试xxxxxxxx");
		map.put("mode" , '1');
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/addOvertimeRecord",map);
		System.out.print(result);
	}

	//同步数据
	@org.junit.Test
	public void synchronizationData(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("date","");
		map.put("pageNum",1);
		map.put("pageSize",50);
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/synchronizationData",map);
		System.out.print(">>>>>>>>同步数据结果为："+result);
	}

	/**-----------------------------------------------------------------------------------------------*/

	//获取clientId
	@org.junit.Test
	public void getClientId(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","359");
		map.put("CID","718d4bcec1e5c431c4b0e005e53be532");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getClinetID",map);
		System.out.print(">>>>>>>>获取clientID结果为："+result);
	}

	//新增报销记录
	@org.junit.Test
	public void insertReimbusement(){
		List<Reimbursement> list = new ArrayList<>();
		Reimbursement r1 = new Reimbursement();
		r1.setType("0");
		r1.setStart("深圳南山区深南大道腾讯大厦");
		r1.setEnd("湖北省武汉市洪山区关山大道");
		r1.setAmount(99);
		r1.setRemark("缺少报销单");
		r1.setImages("http://owgz2pijp.bkt.clouddn.com/test1,http://owgz2pijp.bkt.clouddn.com/test2");
		Reimbursement r2 = new Reimbursement();
		r2.setType("1");
		r2.setRemark("超支");
		r2.setAmount(440);
		r2.setImages("http://owgz2pijp.bkt.clouddn.com/test1,http://owgz2pijp.bkt.clouddn.com/test2");
		list.add(r1);
		list.add(r2);
		Map<String,Object> resultMap = new HashMap<>();
		String content = "报销报销报销";
		//JSONObject.fromObject(list);
		resultMap.put("data" , list);
		resultMap.put("auditor" , "364,366");
		resultMap.put("reason",content);
		resultMap.put("userId","359");
		JSONObject json = JSONObject.fromObject(resultMap);
		//JSONArray json = JSONArray.fromObject(resultMap);
		System.out.println(">>>>>>>>>>>json："+ json.toString());
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/insertReim",json.toString());
		System.out.print(">>>>>>>>>>>增报销记录结果为："+ result);

	}

	//工资发放申请
	@org.junit.Test
	public void salaryAudit(){
		Map<String,Object> map = new HashMap<>();
		map.put("userId", "378");
		map.put("auditor","359,336");
		map.put("reason","工资发放");
		map.put("salary","卜炎森:10000,焦敏:20000,杨坚:24000");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/backend/salaryAudit",map);
		System.out.print(">>>>>>>>>>>工资发放申请结果为："+result);
	}

	@org.junit.Test
	public void getDetails(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type","3");
		map.put("recordNo",468);
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getApplyDetails",map);
		System.out.print(">>>>>>>>获取clientID结果为："+result);
	}


}
