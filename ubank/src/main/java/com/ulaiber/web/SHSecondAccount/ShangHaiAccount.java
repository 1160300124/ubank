package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.utils.*;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 上海二类户工具类
 * Created by daiqingwen on 2017/10/20.
 */
public class ShangHaiAccount {

    private static final Logger logger = Logger.getLogger(ShangHaiAccount.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");

    /**
     * 注册上海二类账户
     * @param map
     * @return
     */
    public static ResultInfo register(Map<String,Object> map){
        logger.info(">>>>>>>>>>开始拼接注册XML");
        Map<String,Object> configMap = StringUtil.loadConfig();
        String privateKey = (String) configMap.get("privateKey");
        String publicKey = (String) configMap.get("publicKey");
        String postUrl = (String) configMap.get("postUrl");
        String pwd = (String) configMap.get("pwd");

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
        ResultInfo resultInfo = new ResultInfo();

        try {
            //加签
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            String random = StringUtil.getStringRandom(36);
            String date = SDF.format(new Date());
            String time = TIME.format(new Date());
            //待签名的数据
            String signDataStr = "BindCardNo="+BindCardNo+"&ChannelId=YFY&ClearDate="+date+"&CoopCustNo="+CoopCustNo
                    +"&CustName="+CustName+"&IdNo="+IdNo+"&MobllePhone="+MobllePhone+"&ProductCd="+ProductCd
                    +"&ReservedPhone="+ReservedPhone+"&RqUID="+random+"&SPName=CBIB&Sign="+Sign+"&TranDate="+date+"&TranTime="+time;
            //System.out.print(">>>>>>>>> sign:" + signDataStr);
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            //拼接xml
            String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
                    "<YFY0001Rq>" +
                    "<CommonRqHdr>" +
                    "<SPName>CBIB</SPName><RqUID>"+ random +"</RqUID>" +
                    "<ClearDate>"+ date +"</ClearDate><TranDate>"+ date +"</TranDate>" +
                    "<TranTime>"+ time +"</TranTime><ChannelId>YFY</ChannelId>" +
                    "</CommonRqHdr>" +
                    "<CoopCustNo>"+ CoopCustNo +"</CoopCustNo><ProductCd>"+ ProductCd+ "</ProductCd>" +
                    "<CustName>"+ CustName +"</CustName><IdNo>"+ IdNo +"</IdNo>" +
                    "<MobllePhone>"+ MobllePhone +"</MobllePhone><BindCardNo>"+ BindCardNo +"</BindCardNo>" +
                    "<ReservedPhone>"+ ReservedPhone +"</ReservedPhone><Sign>"+ Sign +"</Sign>" +
                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
                    "</YFY0001Rq>" +
                    "</BOSFXII>";
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 10000);
            //System.out.println(">>>>>>>>>>>>>>请求结果为 ：" + result);
            if(StringUtil.isEmpty(result)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
            //解析XML
            Map<String,String> resultXML = new HashMap<>();
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator("YFY0001Rs"); // 获取根节点下的子节点BOSFXII
            SecondAcount secondAcount = new SecondAcount();
            while (iter.hasNext()){
                Element recordEle = (Element) iter.next();
                //响应报文信息
                secondAcount.setCoopCustNo(recordEle.elementTextTrim("CoopCustNo"));//合作方客户账号
                secondAcount.setProductCd(recordEle.elementTextTrim("ProductCd"));   //理财产品参数
                secondAcount.setCustName(recordEle.elementTextTrim("CustName"));  //姓名
                secondAcount.setIdNo(recordEle.elementTextTrim("IdNo"));   //身份证
                secondAcount.setSign(recordEle.elementTextTrim("Sign")); //是否开通余额理财功能
                secondAcount.setAcctOpenResult(recordEle.elementTextTrim("AcctOpenResult")); //平台账户开户结果
                secondAcount.setEacctNo(recordEle.elementTextTrim("EacctNo"));   //E账户主账户
                secondAcount.setSubAcctNo(recordEle.elementTextTrim("SubAcctNo"));   //平台理财专属子账户
                secondAcount.setFundCode(recordEle.elementTextTrim("FundCode"));   //基金代码
                secondAcount.setFundAcctOpenResult(recordEle.elementTextTrim("FundAcctOpenResult"));   //基金账户开户结果
                secondAcount.setFundAcct(recordEle.elementTextTrim("FundAcct"));   //基金账户
                secondAcount.setFundTxnAcct(recordEle.elementTextTrim("FundTxnAcct"));   //基金交易账号
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0001Rs下的字节点Signature
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                while (iters.hasNext()){
                    Element recordEles = (Element) iters.next();
                    secondAcount.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                    secondAcount.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                    secondAcount.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                    secondAcount.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                }
            }
            signDataStr = "AcctOpenResult="+secondAcount.getAcctOpenResult()+"&CoopCustNo="+secondAcount.getCoopCustNo()+"&CustName="
                    +secondAcount.getCustName()+"&EacctNo="+secondAcount.getEacctNo()
                    +"&IdNo="+secondAcount.getIdNo()+"&ProductCd="+secondAcount.getProductCd()+"&RqUID="+secondAcount.getRqUID()
                    +"&SPRsUID="+secondAcount.getSPRsUID()+"&ServerStatusCode="+secondAcount.getServerStatusCode()+"&Sign="
                    +secondAcount.getSign()+"&StatusCode="+secondAcount.getStatusCode()+"&SubAcctNo="+secondAcount.getSubAcctNo();
           // System.out.println(">>>>>>>>>signDataStr :" + signDataStr);
           // System.out.println(">>>>>>>>>resultSign :" + resultSign);
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            System.out.println(">>>>>>>>>>>验签结果为：" + verifyRet);
            if(verifyRet != 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
            if(!secondAcount.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
            resultInfo.setData(secondAcount);
            resultInfo.setCode(IConstants.QT_CODE_OK);
        } catch (Exception e) {
            e.printStackTrace();
           // logger.error(">>>>>>>>加密签字异常：",e);
        }

        return resultInfo;
    }
}
