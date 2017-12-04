package com.ulaiber.web.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.qiniu.util.Auth;
import com.ulaiber.web.SHSecondAccount.*;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.AccDetailVO;
import com.ulaiber.web.model.ShangHaiAcount.SHAccDetail;
import com.ulaiber.web.model.ShangHaiAcount.SHChangeCard;
import com.ulaiber.web.model.ShangHaiAcount.Withdraw;
import com.ulaiber.web.quartz.ReadFile;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public class Test {
	public static final String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKh7bxbe5dhwuZzNkeDXrpHx7o5k+uIWrbY1+8c6Oqgq2AfajnK5v10OfQW85xNUn/4TzoRcCOCaK2LZO4QJoQmgs41x45jZNvI/f8EGcJvt2oCs3S2Da98+v6VVfDXoSfgeHlNRcDSYlZF2E31KQLtdTGva9IeECx2CpPIkbVeXAgMBAAECgYA0QlUq2uigQhbQtFLTUxMq4cgFEv1es3oeUpBOM5mOH/vyM7CLlWHuE1hkNzvVmyIlRS+BjqqSQD/E4Wy8f+AbAznky5F8q5Afe5ZKxi+n2M4ZMgh9uryVMcAHCXu1RnOrtsnGjJvp23ku4wZtWCHLNAuQfI9zj6ncq4v50RKKQQJBAN8tSbcT37Mq3Y50zVnnmGxNEgUZKJXwPw/KnO6EeR/Nfpmzy40GQU8y+GGoq5cVY5NDOqYVi6nh21mLXQij9AsCQQDBQt0zU8zurSNaoRYsjhNrHJHQjBt0WuIxtluZz44CTbNQw5/3kA1jVvt1EXOE1hF+l2QVIuLgvgIeHDwvenYlAkAeiusgtAaUVZR2r4N+/1P71lxV+Eh2pKdsuNTbS6Pr90qRLGr6BNYhSZ92dgftqE61U6kOG7q+aBuF2K3FxfJbAkA1oCES4fjmbYJ23mXxvQakXQwU6xufIKzNEIXAWzhTaU4NZgrYPc+JNhSWOl5siJ3YG5f4yXJc3DxoMHt+zSNFAkEA25eGORpWtXERhGXHZR8f3nWIHTAF+EM1O8T7YXp+y7sWs8YvtNI3ZtzjzncHWk4YdScNqkXC1UJz2hokLUR7Aw==";
	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoe28W3uXYcLmczZHg166R8e6OZPriFq22NfvHOjqoKtgH2o5yub9dDn0FvOcTVJ/+E86EXAjgmiti2TuECaEJoLONceOY2TbyP3/BBnCb7dqArN0tg2vfPr+lVXw16En4Hh5TUXA0mJWRdhN9SkC7XUxr2vSHhAsdgqTyJG1XlwIDAQAB";
	private static final SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");

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
		map.put("type",4);
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
		map.put("mark","0");
		map.put("pageNum",1);
		map.put("pageSize",20);
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/queryAuditRecord",map);
		System.out.print(">>>>>>>>待审批数据为："+result);
	}

	//确认审批
	@org.junit.Test
	public void confirmAudit(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId","359");
		map.put("currentUserId", "");
		map.put("recordNo","608");
		map.put("status","1");
		map.put("currentAuditor","张三三");
		map.put("auditorStatus","1");
		map.put("type","4");
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
		r1.setEnd("郴州");
		r1.setAmount(999999999);
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
		resultMap.put("auditor" , "378,392");
		resultMap.put("reason",content);
		resultMap.put("userId","359");
		JSONObject json = JSONObject.fromObject(resultMap);
		//JSONArray json = JSONArray.fromObject(resultMap);
		System.out.println(">>>>>>>>>>>json："+ json.toString());
//		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/insertReim",json.toString());
//		System.out.print(">>>>>>>>>>>增报销记录结果为："+ result);

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
		map.put("mobile","18503036209");
		map.put("SubAcctNo","");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/getUserInfo",map);
		System.out.print(">>>>>>>>结果为："+result);
	}

	//生成上传凭证
	@org.junit.Test
	public void createToken(){
		//设置好账号的ACCESS_KEY和SECRET_KEY
		String accessKey = "GsEHlVlmMBEt4Swq_G-A5FttePWwi1lKwodjomoB";
		String secretKey = "y3aVnN1bDCxjWd7wuFUf-aUQ0ld-8VxjBqrJcoUg";
		//要上传的空间
		String bucket = "ubank-images1";

//		String key = UUID.randomUUID().toString();
//		Auth auth = Auth.create(accessKey, secretKey);
//		String upToken = auth.uploadToken(bucket, key);
//		String accessKey = "access key";
//		String secretKey = "secret key";
//		String bucket = "bucket name";
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		System.out.println(upToken);
		System.out.println(">>>>>>>>>>>生成上传凭证为:" + upToken);
	}

	//新增补卡记录
	@org.junit.Test
	public void insertRemedy(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("morning","2017-10-10 10:58");
		map.put("afternoon","");
		map.put("userId","359");
		map.put("auditor","378");
		map.put("reason","xxxx");
		map.put("type","1");
		map.put("remedyDate","2017-10-10 17:30");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/addRemedy",map);
		System.out.print(">>>>>>>>结果为："+result);
	}

	//申请二类户测试
	@org.junit.Test
	public void secondAcount(){
		Map<String,Object> param = new HashMap<>();
		param.put("CoopCustNo" , "110310018000073");			//合作方客户账号
		param.put("ProductCd" , "yfyBalFinancing");				//理财产品参数
		param.put("CustName" , "焦敏");				//姓名
		param.put("IdNo" , "110103198301019458");					//身份证号
		param.put("MobllePhone" , "18503036206");			//手机号
		param.put("BindCardNo" , "6217007200027221449");				//绑定银行卡号
		param.put("ReservedPhone" , "18503036206");	//银行卡预留手机号
		param.put("Sign" , "N");								//是否开通余额理财功能

		ResultInfo result = ShangHaiAccount.register(param);
		System.out.println(">>>>>>>>>申请二类户结果为：" + result);
	}

	//上海银行二类户改绑测试
	@org.junit.Test
	public void changeCard() throws URISyntaxException {
		SHChangeCard sh = new SHChangeCard();
		sh.setSubAcctNo("623185009300012652");
		sh.setProductCd("yfyBalFinancing");
		sh.setCustName("焦敏");
		sh.setIdNo("652101199901014414");
		sh.setBindCardNo("6217007211147229499");
		sh.setNewCardNo("6217007211143339499");
		sh.setReservedPhone("18503036209");
		sh.setNewReservedPhone("18503036209");
		sh.setModiType("00");
		JSONObject json = JSONObject.fromObject(sh);
		System.out.println(">>>>>>>>>>>json："+ json.toString());
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/SHChangeBind",json.toString());
		System.out.println(">>>>>>>>>>result is :" + result);
		//SHChangeBinding.changeBindCard(sh)
		//System.out.println(">>>>>>>>>申请二类户结果为：" + result);
	}


	//上海银行二类户查余测试
	@org.junit.Test
	public void queryBalance(){
		String str = "623185009300026322";
		SHQueryBalance.queryBalance(str);
		//System.out.println(">>>>>>>>>申请二类户结果为：" + result);
	}

	@org.junit.Test
	public void queryBanks(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code","3cz4");
		map.put("userId","");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/queryAllBanks",map);
		System.out.print(">>>>>>>>结果为："+result);
	}

	//上海银行二类户提现
	@org.junit.Test
	public void withdraw() throws URISyntaxException {
		Withdraw wd = new Withdraw();
		wd.setSubAcctNo("623185009300012892");
		wd.setProductCd("yfyBalFinancing");
		wd.setType("0");
		wd.setPurpose("测试测试");
		wd.setTheirRef("提现");
		wd.setMemoInfo("备注备注");
		wd.setAmount(9.99);
		wd.setBindCardNo("6217007299999999999");
		wd.setCurrency("156");
		SHWithdraw.withdraw(wd);
//		JSONObject json = JSONObject.fromObject(wd);
//		System.out.println(">>>>>>>>>>>json："+ json.toString());
//		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/Withdraw",json.toString());
//		System.out.println("result is :"+result);

	}


    //上海银行二类户交易状态查询
	@org.junit.Test
	public void trading() throws URISyntaxException {
//		String str = "0T28caj5678XYlhA73549258Szs497Z311yD";
//		SHTradingStatus.tradingStatus(str);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("SubAcctNo","623185009300012892");
		map.put("type","0");
		map.put("userId",482);
		map.put("pageNum","1");
		map.put("pageSize","20");
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/TradingQuery",map);
		System.out.print(result);

	}

	//上海银行二类户账户明细状态
	@org.junit.Test
	public void accDetail() throws URISyntaxException {
		AccDetailVO vo = new AccDetailVO();
		vo.setSubAcctNo("623185009300012892");
		vo.setCurrency("156");
		vo.setBeginDt("20171101");
		vo.setEndDt("20171128");
		vo.setPageSize(1000);
		vo.setSkipRecord(1);
		SHAccountDetail.queryDetail(vo);

	}

	//获取账单详情
	@org.junit.Test
	public void tradingDetails(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("SubAcctNo","623185009300012892");
		map.put("RqUID","40");
		map.put("trading",1);
		String result = HttpsUtil.doPost("http://localhost:8080/ubank/api/v1/TradingDetail",map);
		System.out.print(result);
	}

	@org.junit.Test
	public void test(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SubAcctNo","623185009300012603");
		map.put("ProductCd","yfyBalFinancing");
		map.put("Purpose","");
		map.put("TheirRef","");
		map.put("Amount", 10.0);
		map.put("BindCardNo","6217007200047229499");
		map.put("Currency","156");
		 map.put("ChannelId","YFY");
		 map.put("ClearDate",new Date());
		 map.put("RqUID","dfasdfdsfadsfdsafrg436255234");
		 map.put("SPName","CBIB");
		 map.put("TranDate","TranDate");
		 map.put("TranTime","TranTime");

		Map<String,String> ma = new HashMap<>();
		ma.put("SPName","SPName");
		ma.put("RqUID","RqUID");
		ma.put("ClearDate","ClearDate");
		ma.put("TranDate","TranDate");
		ma.put("TranTime","TranTime");
		ma.put("ChannelId","ChannelId");

		List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
		//排序方法
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				//return (o2.getValue() - o1.getValue());
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		//排序后
		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
			System.out.println("排序前"+id);
		}
		String str = "";
		StringBuffer sb = new StringBuffer();
		//排序后
		for(Map.Entry<String, Object> m : infoIds){
			boolean flag = true;
			//System.out.println("排序后"+m.getKey()+":"+ m.getValue());
			if(!StringUtil.isEmpty(m.getValue())){
				for(String key : ma.keySet()){
					if(m.getKey().equals(key)){
						flag = false;
						break;
					}
				}
				if(flag){
					sb.append("<"+m.getKey()+">"+m.getValue()+"</"+m.getKey()+">");
				}
			}
		}
		System.out.println("拼接的结果为"+sb);


	}

	@org.junit.Test
	public void test2() throws IOException {
		System.out.println(getFixLenthString(4));
//		try {
//			String indexName = "IMGDOC0001_SSSSS_yyyyMMdd_XXXX.zip";
//			//加密
//			EncryDecryUtils.makeZip("/Users/emacs/Desktop/image",
//					"/Users/emacs/Desktop/image2",indexName);
//			//解密
////			EncryDecryUtils.unZip(
////					"D:\\Users\\liao_qping\\Desktop\\temp\\IMGDOC0001_CPH_20170831_1342.zip",
////					"D:\\Users\\liao_qping\\Desktop\\temp\\aaa");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("运行完毕");
		//创建文件
//		String filePath = "/Users/emacs/Desktop/test";
//		String content = "20171107_YFY_yyyy_ddddd";
//		StringUtil.writeToTxt(filePath,content);
//		File file = new File(name+".txt");
//		try {
//			if(!file.exists()){
//				file.createNewFile();
//			}
//
//			writeToTxt(name,content);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		//获取根目录
//		String oriPath = "/Users/emacs/Desktop/image";
//		String zipPath = "/Users/emacs/Desktop/image2";
//		System.out.println(">>>>>>>>>根目录为：" + oriPath);
//		File savePath = new File(oriPath);
//		if(!savePath.exists()){
//			savePath.mkdir();
//		}
//		for (int i = 0 ; i < file.length ; i++){
//			InputStream in = file[i].getInputStream();
//			OutputStream out = new FileOutputStream(oriPath + "\\" + file[i]);
//			int r = 0;
//			byte[] by = new byte[1024];
//			while((r=in.read(by)) != 1){
//				out.write(by,0,r);
//			}
//			out.flush();
//			out.close();
//			in.close();
//		}
//		String indexName = "IMGDOC0001_SSSSS_yyyyMMdd_XXXX.zip";
//		//将上传的文件加密并压缩成zip
//		System.out.println(">>>>>>>>>>加密的目录为：" + oriPath +"--"+ zipPath +"--"+ indexName);
//		EncryDecryUtils.makeZip(oriPath,zipPath,indexName);
//		//拼接索引文件名
//		String random = StringUtil.getStringRandom(4);
//		String date = simple.format(new Date());
//		String indexFile = "IMGDOC0001_YFY_" + date + "_" + random;
//		//拼接索引文件内容
//		String content = date + "|" + indexFile + "|2|YFY|yfyBalFinancing|" + 2 + "\r\n"
//				+ "000001|" + date + "_345324534253245_000001|JPG|345324534253245|P0001|BS001|"+ "\r\n"
//				+ "000002|" + date + "_345324534253245_000002|JPG|345324534253245|P0002|BS001|";
//		//写入内容至txt文本
//		boolean re = StringUtil.writeToTxt(zipPath+"/"+indexFile,content);
//		if(!re){
//			System.out.println(">>>>>>>>>>写入内容至txt文本失败");
//		}


	}

	@org.junit.Test
	public void upload() throws IOException {
//		String zipPath = "/Users/emacs/Desktop/image2";
//		SFTPUtil sftp = new SFTPUtil();
//		try {
//			Map<String,Object> map =  sftp.connect();
//			String directory = (String) map.get("directory");
//			String zipDir = (String) map.get("zipDir");
//			File file = new File(zipPath);
//			File[] files =  file.listFiles();
//			for (int i = 0; i < files.length; i++) {
//				System.out.println(">>>>>>>>>>上传文件第"+i+"个文件:" + files[i]);
//				File mul = files[i];
//				String filename =  mul.getName(); // 获取上传文件的原名
//				InputStream ins = new FileInputStream(mul);
//				//InputStream ins = ;
//				sftp.upload(directory,filename,ins);
//			}
//			sftp.logout();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		} catch (SftpException e) {
//			e.printStackTrace();
//		}

		String path2 = "/Users/emacs/Desktop/image2";
		String path1 = "/Users/emacs/Desktop/image";
		try{
			File file = new File(path1);
			File[] files =  file.listFiles();
			for (int i = 0 ; i < files.length ; i++){
				System.out.println(">>>>>>>>>>上传文件第"+i+"个文件:" + files[i]);
				//InputStream in = files[i].getInputStream();
				File fi = files[i];
				FileOutputStream  out = new FileOutputStream(path2 + "/" + fi.getName());
				InputStream in = new FileInputStream(fi);
				int r = 0;
				while((r=in.read()) != -1){
					out.write(r);
				}
				out.flush();
				out.close();
				in.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}


	}

	public String getFixLenthString(int strLength) {

		Random rm = new Random();

		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);

		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLength + 1);
	}





	//读取文件
	@org.junit.Test
	public void readFile(){
		File file = new File("/Users/emacs/Desktop/RES_IMGDOC0001_TJS_20171101_0001.txt");
		String content = StringUtil.txt2String(file);
		System.out.println("file content is ："+content);
		String[] arr = content.split("\n");
		for (int i = 2 ; i < arr.length; i++){
			if(!StringUtil.isEmpty(arr[i])){
				//获取第一条记录的状态：S 表示处理成功  F 表示处理部分失败 E 文件或压缩包校验失败
				//System.out.println(arr[i]);
				String[] strs = arr[i].split("\\|");
				String SubAcctNo = strs[3]; //二类户账号
				String status = strs[6];  //校验状态
				String descriptions = strs[7]; //处理后的描述
				//如果校验失败，则给用户推送信息
//			if(!"S".equals(status)){
//				//根据二类户账号获取CID
//				Map<String,Object> map = new HashMap<>();
//				StringUtil.sendPictureMsg(map);
//			}
			}

		}
		//复制文件
		String downloadDir = "/Users/emacs/Desktop/image";
		String backupDir = "/Users/emacs/Desktop/image2";
//		File downloadFile = new File(downloadDir);
//		File[] dFile = downloadFile.listFiles();
//		for (int i = 0 ; i < dFile.length ; i++){
//			String oldFile = dFile[i].getPath() ;
//			System.out.println(">>>>>>>>>>第"+(i+1)+"个文件路径为--" + oldFile);
//			try {
//				StringUtil.copyFile(oldFile,backupDir+"/"+ dFile[i].getName());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		//删除目录
		StringUtil.delAllFile(backupDir);
	}

	//测试读取文件并解析
	@org.junit.Test
	public void read(){
		//ReadFile re = new ReadFile();
		//re.readFile();
//		SFTPUtil sftpUtil = new SFTPUtil();
//		try {
//			sftpUtil.connect();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		}
		String random = "20171204152748" + StringUtil.getFixLenthString(22);
		System.out.println(random);
	}

	@org.junit.Test
	public  void sort(){

	}




}
