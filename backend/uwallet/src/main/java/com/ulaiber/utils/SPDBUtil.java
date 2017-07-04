package com.ulaiber.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 浦发银行接口
 * @author huangguoqing
 *
 */
public class SPDBUtil {
	
	private static final String BISAFE_IP = "10.17.1.41";
	private static final int BISAFE_SIGN_PORT = 4437;
	private static final int BISAFE_HTTP_PORT = 5777;

	private static final String CLIENT_NUM = "2000040752";
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
		System.out.println(signUrl);
		String res = HttpsUtil.doPost(signUrl, signBody, signFlag);
		return res;
	}
	
//	<?xml version="1.0" encoding="gb2312"?>
//	<packet>
//	<head>
//	<transCode>5652</transCode>
//	<signFlag>1</signFlag>
//	<masterID>2000000000</masterID>   
//	<packetID>1234567890</packetID>      
//	<timeStamp>2009-08-11 10:10:10</timeStamp> 
//	</head>
//	<body>
//		<elecChequeNo>1</elecChequeNo>
//		<authMasterID> 2000040752</authMasterID>
//		<unitNo>95010197</unitNo>
//		<businessType>1002</businessType>
//		<acctNo>6224080001234</acctNo>
//		<bespeakDate></bespeakDate>
//		<totalNumber>100</totalNumber>
//		<totalAmount>1000000.00</totalAmount>
//		<flag>1</flag>
//			<lists name="SalaryList">
//				<list>
//			<payeeAcctNo></payeeAcctNo>
//			<payeeName></payeeName>
//			<amount>10000.00</amount>
//			<note></note>
//				</list>
//			</lists>
//	</body>
//	</packet>


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
	 * 响应头报文
	 * 
	 * @param transCode 交易码
	 * @param signFlag  签名标志
	 * @param packetID  报文号
	 * @param timeStamp 交易时间戳  yyyy-MM-dd HH:mm:ss
	 * @param returnCode返回码
	 * @return
	 */
	private String getResponseHeadStr(String transCode, String signFlag, String packetID, String timeStamp, String returnCode){
		StringBuffer head = new StringBuffer();
		head.append("<head>");
		head.append("<transCode>" + transCode + "</transCode>");
		head.append("<signFlag>" + signFlag + "</signFlag>");
		head.append("<packetID>" + transCode + "</packetID>");
		head.append("<timeStamp>" + timeStamp + "</timeStamp>");
		head.append("<returnCode>" + returnCode + "</returnCode>");
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
	
	public static void main(String[] args) {
		StringBuffer body = new StringBuffer();
		body.append("<body>");
		body.append("<elecChequeNo>20175143132830</elecChequeNo>");
		body.append("<authMasterID>" + CLIENT_NUM + "</authMasterID>");
		body.append("<unitNo>95010197</unitNo>");
		body.append("<businessType>1002</businessType>");
		body.append("<acctNo>" + CLIENT_USER + "</acctNo>");
		body.append("<bespeakDate></bespeakDate>");
		body.append("<totalNumber>1</totalNumber>");
		body.append("<totalAmount>0.01</totalAmount>");
		body.append("<flag>1</flag>");
		body.append("<lists name='SalaryList'>");
		body.append("<list>");
		body.append("<payeeAcctNo>6235591104059580</payeeAcctNo>");
		body.append("<payeeName>李良</payeeName>");
		body.append("<amount>0.01</amount>");
		body.append("<note>代发工资测试</note>");
		body.append("</list>");
		body.append("</lists>");
		body.append("</body>");
		
		String sign = getSign(body.toString(), true);
//		System.out.println(sign);
		
		Document doc = Jsoup.parse(sign);
		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
			System.out.println("sign failed.");
		}
			
		Elements eles = doc.getElementsByTag("sign");
		String signbody = eles.get(0).text();
		System.out.println(eles.get(0).text());
		
		String xml = getRequestXml(getRequestHeadStr("5652", "1", "2000040752", new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), getRequestSignBody(signbody));
		
		System.out.println(xml.length());
		xml = "00" + (xml.length() + 6) + xml;
		System.out.println(xml);
		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
		String res = HttpsUtil.doPost(url, xml, true);
		System.out.println(res);
		
//------------------------------------------------------------------------------------------		
//		
//		StringBuffer balancebody = new StringBuffer();
//		balancebody.append("<body>");
//		balancebody.append("<lists name='acctList'>");
//		balancebody.append("<list>");
//		balancebody.append("<acctNo> " + CLIENT_USER + "</acctNo>");
//		balancebody.append("</list>");
//		balancebody.append("</lists>");
//		
//		balancebody.append("</body>");
//
//		String sign = getSign(balancebody.toString(), true);
//		
//		Document doc = Jsoup.parse(sign);
//		if (!StringUtils.equals("0", doc.getElementsByTag("result").get(0).text())){
//			System.out.println("sign failed.");
//		}
//			
//		Elements eles = doc.getElementsByTag("sign");
//		String signbody = eles.get(0).text();
//		System.out.println(eles.get(0).text());
//		
//		String xml = getRequestXml(getRequestHeadStr("4402", "1", "2000040752", new Date().getTime() + "", DateTimeUtil.date2Str(new Date())), 
//				getRequestSignBody(signbody));
//		
//		System.out.println(xml.length());
//		xml = "00" + (xml.length() + 6) + xml;
//		System.out.println(xml);
//		System.out.println("1------------------------------------------------------");
//		String url = "http://" + BISAFE_IP + ":" + BISAFE_HTTP_PORT + "/";
//		String res = HttpsUtil.doPost(url, xml, true);
//		System.out.println(res);
//		String res1 = res.substring(6);
//		System.out.println("2------------------------------------------------------");
//		System.out.println(res1);
//		System.out.println("3------------------------------------------------------");
//		
//		try {
//			org.dom4j.Document document = DocumentHelper.parseText(res1);
//			Element root = document.getRootElement();
//			System.out.println(root.element("head").elementTextTrim("returnCode"));
//			String returnCode = root.element("head").elementTextTrim("returnCode");
//			if (StringUtils.equals(returnCode, "AAAAAAA")){
//				String returnsign = root.element("body").elementTextTrim("signature");
//				System.out.println(returnsign);
//				String res2 = getSign(returnsign, false);
//				System.out.println("---------" + res2);
//			}
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
