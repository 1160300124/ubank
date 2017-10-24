package com.ulaiber.web.secondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.sun.deploy.net.HttpUtils;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.utils.HttpsUtil;
import com.ulaiber.web.utils.StringUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上海二类户工具类
 * Created by daiqingwen on 2017/10/20.
 */
public class ShangHaiAccount {

    private static final Logger logger = Logger.getLogger(ShangHaiAccount.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");

    private static String privateKey = "/Users/emacs/Documents/文档/private.pfx";
    private static String publicKey = "/Users/emacs/Documents/文档/bankofshanghai_test.cer";

    private static String postUrl = "http://203.156.238.218:30278/ib-rest/gateway";

    private static String pwd = "123123";

    /**
     * 注册上海二类账户
     * @param map
     * @return
     */
    public static SecondAcount register(Map<String,Object> map){
        logger.info(">>>>>>>>>>开始拼接XML");
        boolean flag = false;
        String CoopCustNo = (String) map.get("CoopCustNo");
        String ProductCd = (String) map.get("ProductCd");
        String CustName = (String) map.get("CustName");
        String IdNo = (String) map.get("IdNo");
        String MobllePhone = (String) map.get("MobllePhone");
        String BindCardNo = (String) map.get("BindCardNo");
        String ReservedPhone = (String) map.get("ReservedPhone");
        String Sign = (String) map.get("Sign");
        String KoalB64Cert = "";
        String Signature = "";
        //加签
        SvsSign signer = new SvsSign();
        SecondAcount secondAcount = new SecondAcount();
        try {
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            //待签名的数据
            String signDataStr = "BindCardNo="+BindCardNo+"&CoopCustNo="+CoopCustNo+"&CustName="+CustName+"&IdNo="+IdNo+"&MobllePhone="+MobllePhone+"" +
                    "&ProductCd="+ProductCd+"&ReservedPhone="+ReservedPhone+"&Sign="+Sign+"";
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            //拼接xml
            String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
                    "<YFY0001Rq>" +
                    "<CommonRqHdr>" +
                    "<SPName>CBIB</SPName><RqUID>"+ StringUtil.getStringRandom(36) +"</RqUID>" +
                    "<ClearDate>"+ SDF.format(new Date()) +"</ClearDate><TranDate>"+ SDF.format(new Date()) +"</TranDate>" +
                    "<TranTime>"+ TIME.format(new Date()) +"</TranTime><ChannelId>YFY</ChannelId>" +
                    "</CommonRqHdr>" +
                    "<CoopCustNo>"+ CoopCustNo +"</CoopCustNo><ProductCd>"+ ProductCd+ "</ProductCd>" +
                    "<CustName>"+ CustName +"</CustName><IdNo>"+ IdNo +"</IdNo>" +
                    "<MobllePhone>"+ MobllePhone +"</MobllePhone><BindCardNo>"+ BindCardNo +"</BindCardNo>" +
                    "<ReservedPhone>"+ ReservedPhone +"</ReservedPhone><Sign>"+ Sign +"</Sign>" +
                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
                    "</YFY0001Rq>" +
                    "</BOSFXII>";
            System.out.print(">>>>>>>>>>>>>>XML is：" + xml);

            String result = HttpsUtil.doPost(postUrl,xml);
            System.out.print(">>>>>>>>>>>>>>请求结果为 ：" + result);
            if(!StringUtil.isEmpty(result)){
                //验签
                int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),result,publicKey);
                if(verifyRet == 0){
                    return null;
                }

                //解析XML
                Document document = DocumentHelper.parseText(result);
                Element root = document.getRootElement();
                String StatusCode = root.elementText("StatusCode");  //返回结果码
                String ServerStatusCode = root.elementText("ServerStatusCode"); //返回结果信息
                String RqUID = root.elementText("RqUID");   //请求流水号
                secondAcount.setStatusCode(StatusCode);
                secondAcount.setServerStatusCode(ServerStatusCode);
                if(!StatusCode.equals("0000")){
                    return secondAcount;
                }
                //响应报文信息
                String coopCustNo = root.elementText("CoopCustNo"); //合作方客户账号
                String productCd = root.elementText("ProductCd");   //理财产品参数
                String sign = root.elementText("Sign"); //是否开通余额理财功能
                String FundCode = root.elementText("FundCode");   //基金代码
                String AcctOpenResult = root.elementText("AcctOpenResult"); //平台账户开户结果
                String AcctOpenDesc = root.elementText("AcctOpenDesc"); //平台账户开户结果描述
                String EacctNo = root.elementText("EacctNo");   //E账户主账户
                String SubAcctNo = root.elementText("SubAcctNo");   //平台理财专属子账户
                String FundAcctOpenResult = root.elementText("FundAcctOpenResult");   //基金账户开户结果
                String FundAcctOpenDesc = root.elementText("FundAcctOpenDesc");   //基金账户开户结果描述
                String FundAcct = root.elementText("FundAcct");   //基金账户
                String FundTxnAcct = root.elementText("FundTxnAcct");   //基金交易账号
                secondAcount.setCoopCustNo(coopCustNo);
                secondAcount.setProductCd(productCd);
                secondAcount.setSign(sign);
                secondAcount.setFundCode(FundCode);
                secondAcount.setAcctOpenResult(AcctOpenResult);
                secondAcount.setAcctOpenDesc(AcctOpenDesc);
                secondAcount.setEacctNo(EacctNo);
                secondAcount.setSubAcctNo(SubAcctNo);
                secondAcount.setFundAcctOpenResult(FundAcctOpenResult);
                secondAcount.setFundAcctOpenDesc(FundAcctOpenDesc);
                secondAcount.setFundAcct(FundAcct);
                secondAcount.setFundTxnAcct(FundTxnAcct);
                return secondAcount;

            }
        } catch (Exception e) {
            e.printStackTrace();
           // logger.error(">>>>>>>>加密签字异常：",e);
        }

        return secondAcount;
    }
}
