package com.ulaiber.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ulaiber.model.Payee;

/**
 * 浦发银行接口
 * @author huangguoqing
 *
 */
public class SPDBUtil {
	
	private static final String BISAFE_IP = "10.17.1.41";
	private static final int BISAFE_SIGN_PORT = 4437;
	private static final int BISAFE_HTTP_PORT = 5777;
	
	//单位编号
	private static final String UNIT_NUM = "95018201";

	//指定授权客户号
	private static final String CLIENT_NUM = "2000040752";
	
	//付款账号
	private static final String CLIENT_USER = "6224080600234";
	
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
	
	public static boolean getPayResult(String entrustSeqNo){
		StringBuffer body = new StringBuffer();
		
		body.append("<body>");
		body.append("<entrustSeqNo>" + entrustSeqNo+ "</entrustSeqNo>");
		body.append("</body>");

		String sign = getSign(body.toString(), true);
		
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			System.out.println("sign failed.");
			return false;
		}
			
		Elements eles = doc.getElementsByTag("sign");
		String signbody = eles.get(0).text();
		System.out.println(eles);
		
		String xml = getRequestXml(getRequestHeadStr("5630", "1", "2000040752", new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), 
				getRequestSignBody(signbody));
		
//		System.out.println(xml.length());
		xml = String.format("%06d", xml.length() + 6) + xml;
//		System.out.println(xml);
		System.out.println("1------------------------------------------------------");
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
//		System.out.println(res);
		String res1 = res.substring(6);
		System.out.println("2------------------------------------------------------");
		System.out.println(res1);
		System.out.println("3------------------------------------------------------");
		
		try {
			org.dom4j.Document document = DocumentHelper.parseText(res1);
			org.dom4j.Element root = document.getRootElement();
//			System.out.println(root.element("head").elementTextTrim("returnCode"));
			String returnCode = root.element("head").elementTextTrim("returnCode");
			if (StringUtils.equals(returnCode, "AAAAAAA")){
				String returnsign = root.element("body").elementTextTrim("signature");
				System.out.println(returnsign);
				String res2 = getSign(returnsign, false);
				System.out.println("4------------------------------------------------------");
				System.out.println(res2);
				Document doc1 = Jsoup.parse(res2);
				if (!StringUtils.equals("0", doc1.getElementsByTag("result").get(0).text())){
					System.out.println("sign failed.");
					return false;
				}
				Elements eles1 = doc1.getElementsByTag("sic");
//				System.out.println(eles1)body;
				System.out.println("----------------------------------------------------------");
				System.out.println(eles1.select("transstatus"));
				System.out.println("----------------------------------------------------------");
				if (StringUtils.equals(eles1.select("transstatus").text(), "5")){
					return true;
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
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
		
		String elecChequeNo = params.get("elecChequeNo").toString();
		String authMasterID = params.get("authMasterID").toString();
		String unitNo = params.get("unitNo").toString();
		String businessType = params.get("businessType").toString();
		String acctNo = params.get("acctNo").toString();
		String bespeakDate = null == params.get("bespeakDate") ? "" : params.get("bespeakDate").toString();
		int totalNumber = (int)params.get("totalNumber");
		double totalAmount = (double)params.get("totalAmount");
		String flag = params.get("flag").toString();
		
		//拼接5652 代发工资请求body
		String body = getRequestBody(elecChequeNo, authMasterID, unitNo, businessType, acctNo, bespeakDate, totalNumber, totalAmount, flag, payees);
//		System.out.println(body);
		String sign = getSign(body.toString(), true);
//		System.out.println(sign);
		System.out.println("------------------------------------------------------");		
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			System.out.println("sign failed.");
			return "";
		}
			
		String signbody = doc.getElementsByTag("sign").get(0).text();
//		System.out.println(doc.getElementsByTag("sign").get(0).text());
		
		String xml = getRequestXml(getRequestHeadStr("5652", "1", "2000040752", new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), getRequestSignBody(signbody));
		
//		System.out.println(xml.length());
		xml = String.format("%06d", xml.length() + 6) + xml;
//		System.out.println(xml);
		System.out.println("------------------------------------------------------");
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
		String res1 = res.substring(6);
		System.out.println("2------------------------------------------------------");
		System.out.println(res1);
		System.out.println("3------------------------------------------------------");
		
		try {
			org.dom4j.Document document = DocumentHelper.parseText(res1);
			org.dom4j.Element root = document.getRootElement();
			System.out.println(root.element("head").elementTextTrim("returnCode"));
			String returnCode = root.element("head").elementTextTrim("returnCode");
			if (StringUtils.equals(returnCode, "AAAAAAA")){
				String returnsign = root.element("body").elementTextTrim("signature");
				System.out.println(returnsign);
				String res2 = getSign(returnsign, false);
				System.out.println("4------------------------------------------------------");
				System.out.println(res2);
				Document doc1 = Jsoup.parse(res2);
				if (!StringUtils.equals("0", doc1.getElementsByTag("result").get(0).text())){
					System.out.println("sign failed.");
					return "";
				}
				Elements eles1 = doc1.getElementsByTag("sic");
				System.out.println("entrustSeqNo------------------------------------------------------");
				System.out.println(eles1.select("elecChequeNo").text());
				System.out.println("entrustSeqNo------------------------------------------------------");
				return eles1.select("elecChequeNo").text();
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	} 
	
	public static void main(String[] args) throws Exception{

		
	    
//		queryBalance();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("elecChequeNo", String.valueOf(DateTimeUtil.getMillis(new Date())));
		params.put("authMasterID", "2000040752");
		params.put("unitNo", "82050095");
		params.put("businessType", "1002");
		params.put("acctNo", "001846554292003765");
		params.put("bespeakDate", DateTimeUtil.getfutureDay(1));
		params.put("totalNumber", 2);
		params.put("totalAmount", 2.22);
		params.put("flag", "1");
		
		List<Payee> payees = new ArrayList<Payee>();
		Payee payee = new Payee();
		payee.setPayeeName("张三");
		payee.setPayeeAcctNo("6224080002395");
		payee.setAmount(1.01);
		payee.setNote("代发工资测试");
		
		Payee payee1 = new Payee();
		payee1.setPayeeName("李四");
		payee1.setPayeeAcctNo("6224080600234");
		payee1.setAmount(1.21);
		payee1.setNote("代发工资测试");
		payees.add(payee);
		payees.add(payee1);
		String entrustSeqNo = paySalaries(params, payees);
		
		
		
//		Thread.sleep(8000);
//		getPayResult("19681013");
		
		
		
	}
}
