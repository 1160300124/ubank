package com.ulaiber.web.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ulaiber.web.model.Payee;

/**
 * 浦发银行接口
 * @author huangguoqing
 *
 */
public class SPDBUtil {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(SPDBUtil.class);
	
	private static String BISAFE_IP = "192.168.1.242";
	private static String BISAFE_SIGN_PORT = "4437";
	private static String BISAFE_HTTP_PORT = "5777";
	
	//单位编号
	public static String UNIT_NUM = "19630031";

	//指定授权客户号
	public static String CLIENT_MASTER_ID = "2013632264";
	
	//付款账号
	public static String CLIENT_ACCT_NO = "19630155200001772";
	
	//代发工资的交易码
	private static String PAY_SALARY_TRANS_CODE= "5652";
	
	//查询代发工资信息交易码
	private static String PAY_RESULT_TRANS_CODE = "5635";
	
	private static String BUSINESS_TYPE= "1001";
	
	private static Properties props = null;   
	
	static {
		loadProps();
	}
	
	private synchronized static void loadProps(){
		props = new Properties();
		InputStream is = null;
		try {
			is = SPDBUtil.class.getClassLoader().getResourceAsStream("config/spdb.properties");
			props.load(is);
		} catch (FileNotFoundException e) {
			logger.error("cannot find spdb.properties", e);
		} catch (IOException e) {
			logger.error("read spdb.properties error.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	public static String getProperty(String key){
		if(null == props) {
			loadProps();
		}
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		if(null == props) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}
	
	/**
	 * 获取签名报文
	 * 
	 * @param signBody 需要的签名的请求body
	 * @param boolean 是否需要签名  
	 * 
	 * @return
	 */
	private static String getSign(String signBody, boolean signFlag){
		String signUrl = "http://" + BISAFE_IP + ":" + BISAFE_SIGN_PORT + "/";
		String res = HttpsUtil.doPost(signUrl, signBody, signFlag);
		return res;
	}
	
	/**
	 * 请求头报文
	 * 
	 * @param transCode 交易码
	 * @param signFlag  签名标志      0：表示数据没有签名  1：表示数据经过签名 (企业提交浦发的所有报文都必须签名，浦发返回的结果报文除9001，9003外，其它都经过签名)
	 * @param masterID  企业客户号
	 * @param packetID  报文号
	 * @param timeStamp 交易时间戳  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	private static String getRequestHeadStr(String transCode, String signFlag, String masterID, String packetID, String timeStamp){
		StringBuffer head = new StringBuffer();
		head.append("<head>");
		head.append("<transCode>" + transCode + "</transCode>");
		head.append("<signFlag>"  + signFlag  + "</signFlag>");
		head.append("<masterID>"  + masterID  + "</masterID>");
		head.append("<packetID>"  + packetID + "</packetID>");
		head.append("<timeStamp>" + timeStamp + "</timeStamp>");
		head.append("</head>");

		return head.toString();
	}
	
	/**
	 * 签名后的body
	 * 
	 * @param sign
	 * @return
	 */
	private static String getRequestSignBody(String sign){
		
		StringBuffer signBody = new StringBuffer();
		signBody.append("<body><signature>");
		signBody.append(sign);
		signBody.append("</signature></body>");
		
		return signBody.toString();
	}
	
	/**
	 * 请求xml
	 * 
	 * @param requestHeadStr
	 * @param requestBody
	 * @return
	 */
	private static String getRequestXml(String requestHeadStr, String requestBody){
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='GB2312'?>");
		xml.append("<packet>");
		xml.append(requestHeadStr);
		xml.append(requestBody);
		xml.append("</packet>");
		return xml.toString();
	} 
	
	/**
	 * 代发工资请求body
	 * 
	 * @param elecChequeNo 电子凭证号
	 * @param masterID 指定授权客户号
	 * @param unitNo 单位编号
	 * @param businessType 1001-代发其他  1002-代发工资  1003-代发奖金
	 * @param bespeakDate 付款日期  不输入时表示实时，输入日期为当天时也表示实时。日期只能为提交日期的后一个月内，否则报错。
	 * @param acctNo 付款账号
	 * @param totalNumber 总笔数 该批交易的总笔数（提交时，需校验笔数）
	 * @param totalAmount 总金额 该批交易的总金额（提交时，需校验金额）
	 * @param flag 预校验/代发标志 0=预校验文件 1=代发文件
	 * @param payees 收款人集合
	 * 
	 * @return String
	 */
	private static String getRequestBody(String elecChequeNo, String masterID, String unitNo, String businessType, String acctNo, String bespeakDate, 
			int totalNumber, double totalAmount, String flag, List<Payee> payees){
		StringBuffer body = new StringBuffer();
		body.append("<body>");
		body.append("<elecChequeNo>" + elecChequeNo + "</elecChequeNo>");
		body.append("<authMasterID>" + masterID + "</authMasterID>");
		body.append("<unitNo>" + unitNo + "</unitNo>");
		body.append("<businessType>" + businessType + "</businessType>");
		body.append("<acctNo>" + acctNo + "</acctNo>");
		body.append("<bespeakDate>" + bespeakDate + "</bespeakDate>");
		body.append("<totalNumber>" + totalNumber + "</totalNumber>");
		body.append("<totalAmount>" + totalAmount + "</totalAmount>");
		body.append("<flag>" + flag + "</flag>");
		body.append("<lists name='SalaryList'>");
		for (Payee payee : payees){
			body.append("<list>");
			body.append("<payeeAcctNo>" + payee.getPayeeAcctNo() + "</payeeAcctNo>");
			body.append("<payeeName>" + payee.getPayeeName() + "</payeeName>");
			body.append("<amount>" + payee.getAmount() + "</amount>");
			body.append("<note>" + payee.getNote() + "</note>");
			body.append("</list>");
		}
		body.append("</lists>");
		body.append("</body>");
		
		return body.toString();
	}
	
	
	/**
	 * 网银授权代发工资交易结果信息批量查询 body
	 * 
	 * @param authMasterID 指定授权客户号
	 * @param acctNo 付款账号
	 * @param seqNo 业务编号
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 *  
	 * @return String
	 */
	private static String getRequestBody(String authMasterID, String acctNo, String seqNo, String beginDate, String endDate){
		StringBuffer body = new StringBuffer();
		body.append("<body>");
		body.append("<authMasterID>" + authMasterID + "</authMasterID>");
		body.append("<acctNo>" + acctNo + "</acctNo>");
		body.append("<seqNo>" + seqNo + "</seqNo>");
		body.append("<beginDate>" + beginDate + "</beginDate>");
		body.append("<endDate>" + endDate + "</endDate>");
		body.append("</body>");
		
		return body.toString();
	}
	
	public static void queryBalance(){
		StringBuffer balancebody = new StringBuffer();

		balancebody.append("<body>");
		balancebody.append("<lists name='acctList'>");
		balancebody.append("<list>");
		balancebody.append("<acctNo>001846554292003765</acctNo>");
		balancebody.append("</list>");
		balancebody.append("</lists>");
		balancebody.append("</body>");

		String sign = getSign(balancebody.toString(), true);
		
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			System.out.println("sign failed.");
		}
			
		Elements eles = doc.getElementsByTag("sign");
		String signbody = eles.get(0).text();
//		System.out.println(eles.get(0).text());
		
		String xml = getRequestXml(getRequestHeadStr("4402", "1", "2000040752", new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), 
				getRequestSignBody(signbody));
		
		xml = "00" + (xml.length() + 6) + xml;
//		System.out.println(xml);
		System.out.println("1------------------------------------------------------");
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
//		System.out.println(res);
		String res1 = res.substring(6);
		System.out.println("2------------------------------------------------------");
//		System.out.println(res1);
		System.out.println("3------------------------------------------------------");
		
		try {
			org.dom4j.Document document = DocumentHelper.parseText(res1);
			org.dom4j.Element root = document.getRootElement();
//			System.out.println(root.element("head").elementTextTrim("returnCode"));
			String returnCode = root.element("head").elementTextTrim("returnCode");
			if (StringUtils.equals(returnCode, "AAAAAAA")){
				String returnsign = root.element("body").elementTextTrim("signature");
//				System.out.println(returnsign);
				String res2 = getSign(returnsign, false);
				System.out.println("4------------------------------------------------------");
//				System.out.println(res2);
				Document doc1 = Jsoup.parse(res2);
				if (!StringUtils.equals("0", doc1.getElementsByTag("result").get(0).text())){
					System.out.println("sign failed.");
				}
				Elements eles1 = doc1.getElementsByTag("sic");
				System.out.println(eles1.get(0));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询代发工资结果信息 5630
	 * 
	 * @param entrustSeqNo 业务委托编号
	 * @return String 处理状态
	 * 0-未处理数据文件
	 * 1-正在处理数据文件
	 * 2-数据文件处理成功
     * 3-数据文件有误，不能进行
	 * 4-正在处理业务数据
	 * 5-业务数据处理成功
     * 6-业务数据处理失败
	 * 7-撤销
	 */
	public static String getPayResult(String entrustSeqNo){
		logger.info("getPayResult start...");
		
		StringBuffer body = new StringBuffer();
		body.append("<body>");
		body.append("<entrustSeqNo>" + entrustSeqNo+ "</entrustSeqNo>");
		body.append("</body>");

		logger.info("getPayResult request body: " + body);
		String sign = getSign(body.toString(), true);
		
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			logger.error("getPayResult()--request body sign failed.");
			return null;
		}
			
		Elements eles = doc.getElementsByTag("sign");
		String signbody = eles.get(0).text();
		
		String xml = getRequestXml(getRequestHeadStr(PAY_RESULT_TRANS_CODE, "1", CLIENT_MASTER_ID, new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), 
				getRequestSignBody(signbody));
		
		//接口规范  请求报文前面必须加上报文长度+6 固定长度6位s
		xml = String.format("%06d", xml.length() + 6) + xml;
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
		logger.debug("[getPayResult] response: " + res);
		String res1 = res.substring(6);
		
		try {
			org.dom4j.Document document = DocumentHelper.parseText(res1);
			org.dom4j.Element root = document.getRootElement();
			String returnCode = root.element("head").elementTextTrim("returnCode");
			logger.info("[getPayResult] response head: " + root.element("head"));
			if (StringUtils.equals(returnCode, "AAAAAAA")){
				String returnsign = root.element("body").elementTextTrim("signature");
				String res2 = getSign(returnsign, false);
				Document doc1 = Jsoup.parse(res2);
				if (!StringUtils.equals("0", doc1.getElementsByTag("result").get(0).text())){
					logger.error("[getPayResult] response body sign failed, result code: " + doc1.getElementsByTag("result").get(0).text());
					return null;
				}
				Elements eles1 = doc1.getElementsByTag("sic");
				return eles1.select("transstatus").text();
			}
		} catch (DocumentException e) {
			logger.error("parse response failed.", e);

		}
		logger.info("getPayResult end...");
		return null;
	}
	
	/**
	 * 查询在代发工资交易时走授权路线的情况下，代发工资交易结果信息。 5635
	 * 
	 * @param params
	 * @return String 处理状态
	 * 0-未处理数据文件
	 * 1-正在处理数据文件
	 * 2-数据文件处理成功
     * 3-数据文件有误，不能进行
	 * 4-正在处理业务数据
	 * 5-业务数据处理成功
     * 6-业务数据处理失败
	 * 7-撤销
	 */
	public static String getPayResult(Map<String, Object> params){
		logger.info("getPayResult start...");
//		BISAFE_IP = getProperty("BISAFE_IP", BISAFE_IP); 
//		BISAFE_SIGN_PORT = getProperty("BISAFE_SIGN_PORT", BISAFE_SIGN_PORT);
//		BISAFE_HTTP_PORT = getProperty("BISAFE_HTTP_PORT", BISAFE_HTTP_PORT);
//		UNIT_NUM = getProperty("UNIT_NUM", UNIT_NUM);
//		CLIENT_MASTER_ID = getProperty("CLIENT_MASTER_ID", CLIENT_MASTER_ID);
//		CLIENT_ACCT_NO = getProperty("CLIENT_ACCT_NO", CLIENT_ACCT_NO);
		String authMasterID = getProperty("CLIENT_MASTER_ID", CLIENT_MASTER_ID); 
		String acctNo = getProperty("CLIENT_ACCT_NO", CLIENT_ACCT_NO); 
		String seqNo = params.get("seqNo").toString();
		String beginDate = params.get("beginDate").toString();
		String endDate = params.get("endDate").toString();
		
		String body = getRequestBody(authMasterID, acctNo, seqNo, beginDate, endDate);		
		logger.info("getPayResult request body: " + body);
		String sign = getSign(body.toString(), true);
		
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			logger.error("getPayResult()--request body sign failed.");
			return null;
		}
			
		Elements eles = doc.getElementsByTag("sign");
		String signbody = eles.get(0).text();
		
		String xml = getRequestXml(getRequestHeadStr(PAY_RESULT_TRANS_CODE, "1", authMasterID, new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), 
				getRequestSignBody(signbody));
		
		//接口规范  请求报文前面必须加上报文长度+6 固定长度6位s
		xml = String.format("%06d", xml.length() + 6) + xml;
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
		logger.debug("[getPayResult] response: " + res);
		String res1 = res.substring(6);
		
		try {
			org.dom4j.Document document = DocumentHelper.parseText(res1);
			org.dom4j.Element root = document.getRootElement();
			String returnCode = root.element("head").elementTextTrim("returnCode");
			logger.info("[getPayResult] response head: " + root.element("head"));
			if (StringUtils.equals(returnCode, "AAAAAAA")){
				String returnsign = root.element("body").elementTextTrim("signature");
				String res2 = getSign(returnsign, false);
				logger.debug("[getPayResult] response body: " + res2);
				Document doc1 = Jsoup.parse(res2);
				if (!StringUtils.equals("0", doc1.getElementsByTag("result").get(0).text())){
					logger.error("[getPayResult] response body sign failed, result code: " + doc1.getElementsByTag("result").get(0).text());
					return null;
				}
				Elements eles1 = doc1.getElementsByTag("sic");
				logger.info("[getPayResult] sic body: " + doc1.getElementsByTag("sic"));
				return eles1.select("transstatus").text();
			}
		} catch (DocumentException e) {
			logger.error("parse response failed.", e);

		}
		logger.info("getPayResult end...");
		return null;
	}
	
	/**
	 * 代发工资 5652
	 * 
	 * @param params 参数，详情见接口文档
	 * @param payees 收款人集合
	 * @return String 业务委托编号(entrustSeqNo)
	 */
	public static String paySalaries(Map<String, Object> params, List<Payee> payees){
//		StringBuffer body = new StringBuffer();
//		body.append("<body>");
//		body.append("<elecChequeNo>20175143132831</elecChequeNo>");
//		body.append("<authMasterID>2000040752</authMasterID>");
//		body.append("<unitNo>82050095</unitNo>");
//		body.append("<businessType>1002</businessType>");
//		body.append("<acctNo>001846554292003765</acctNo>");
//		body.append("<bespeakDate>" + DateTimeUtil.getfutureDay(7) + "</bespeakDate>");
//		body.append("<totalNumber>2</totalNumber>");
//		body.append("<totalAmount>2.22</totalAmount>");
//		body.append("<flag>1</flag>");
//		body.append("<lists name='SalaryList'>");
//		body.append("<list>");
//		body.append("<payeeAcctNo>6224080002395</payeeAcctNo>");
//		body.append("<payeeName>浦发</payeeName>");
//		body.append("<amount>1.01</amount>");
//		body.append("<note>代发工资测试</note>");
//		body.append("</list>");
//		body.append("<list>");
//		body.append("<payeeAcctNo>6224080600234</payeeAcctNo>");
//		body.append("<payeeName>浦发1</payeeName>");
//		body.append("<amount>1.21</amount>");
//		body.append("<note>代发工资测试</note>");
//		body.append("</list>");
//		body.append("</lists>");
//		body.append("</body>");

		logger.info("paySalaries start...");
		String authMasterID = getProperty("CLIENT_MASTER_ID", CLIENT_MASTER_ID); 
		String unitNo = getProperty("UNIT_NUM", UNIT_NUM); 
		String acctNo = getProperty("CLIENT_ACCT_NO", CLIENT_ACCT_NO); 
		String businessType = getProperty("BUSINESS_TYPE", BUSINESS_TYPE); 
		String elecChequeNo = params.get("elecChequeNo").toString();
		String bespeakDate = null == params.get("bespeakDate") ? "" : params.get("bespeakDate").toString();
		int totalNumber = (int)params.get("totalNumber");
		double totalAmount = (double)params.get("totalAmount");
		String flag = params.get("flag").toString();

		//拼接5652 代发工资请求body
		String body = getRequestBody(elecChequeNo, authMasterID, unitNo, businessType, acctNo, bespeakDate, totalNumber, totalAmount, flag, payees);
		logger.info("paySalaries request body: " + body);
		String sign = getSign(body.toString(), true);
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			logger.error("[paySalaries] request body sign failed.");
			return null;
		}
			
		String signbody = doc.getElementsByTag("sign").get(0).text();
		String xml = getRequestXml(getRequestHeadStr(PAY_SALARY_TRANS_CODE, "1", authMasterID, new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), getRequestSignBody(signbody));
		
		//接口规范  请求报文前面必须加上报文长度+6 固定长度6位
		xml = String.format("%06d", xml.length() + 6) + xml;
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
		logger.debug("[paySalaries] response: " + res);
		String res1 = res.substring(6);
		
		try {
			org.dom4j.Document document = DocumentHelper.parseText(res1);
			org.dom4j.Element root = document.getRootElement();
			String returnCode = root.element("head").elementTextTrim("returnCode");
			logger.info("[paySalaries] response head: " + root.element("head"));
			if (StringUtils.equals(returnCode, "AAAAAAA")){
				String returnsign = root.element("body").elementTextTrim("signature");
				//解签
				String res2 = getSign(returnsign, false);
				logger.debug("[paySalaries] response body: " + res2);
				Document doc1 = Jsoup.parse(res2);
				if (!StringUtils.equals("0", doc1.getElementsByTag("result").get(0).text())){
					logger.error("[paySalaries] response body sign failed, result code: " + doc1.getElementsByTag("result").get(0).text());
					return null;
				}
				Elements eles1 = doc1.getElementsByTag("sic");
				logger.info("[getPayResult] sic body: " + doc1.getElementsByTag("sic"));
				return eles1.select("entrustSeqNo").text();
			}
		} catch (DocumentException e) {
			
			logger.error("parse response failed.", e);
		}
		logger.info("paySalaries end...");
		return null;
	} 
	
	public static void main(String[] args) throws Exception{

		
	    
//		queryBalance();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("elecChequeNo", String.valueOf(DateTimeUtil.getMillis(new Date())));
		params.put("authMasterID", CLIENT_MASTER_ID);
		params.put("unitNo", UNIT_NUM);
		params.put("businessType", "1002");
		params.put("acctNo", CLIENT_ACCT_NO);
		params.put("bespeakDate", "");
		params.put("totalNumber", 1);
		params.put("totalAmount", 0.01);
		params.put("flag", "1");
		
		List<Payee> payees = new ArrayList<Payee>();
		Payee payee = new Payee();
		payee.setPayeeName("和俊杰");
		payee.setPayeeAcctNo("6217921102903120");
		payee.setAmount(0.01);
		payee.setNote("代发工资");
		
		Payee payee1 = new Payee();
		payee1.setPayeeName("李良");
		payee1.setPayeeAcctNo("6235591104059580");
		payee1.setAmount(0.01);
		payee1.setNote("代发工资");
		payees.add(payee1);
		payees.add(payee);
//		String entrustSeqNo = paySalaries(params, payees);
//		System.out.println("-----------------------------------------------" + entrustSeqNo);
		
		
		
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		params1.put("authMasterID", CLIENT_MASTER_ID);
//		params1.put("acctNo", CLIENT_ACCT_NO);
//		params1.put("seqNo", "5235133004");
//		params1.put("beginDate", "20170713");
//		params1.put("endDate", "20170713");
//		String transStatus = getPayResult(params1);
		
//		System.out.println("-----------------------------------------------" + transStatus);
		
		System.err.println(getProperty("BISAFE_IP"));
		
	}
}
