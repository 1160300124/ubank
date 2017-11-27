package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.SHChangeCard;
import com.ulaiber.web.utils.HttpsUtil;
import com.ulaiber.web.utils.SslTest;
import com.ulaiber.web.utils.StringUtil;
import com.ulaiber.web.utils.SysConf;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 上海银行二类户更改绑定银行卡
 * Created by daiqingwen on 2017/10/25.
 */
public class SHChangeBinding {

    private static final Logger logger = Logger.getLogger(SHChangeBinding.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");

    public static ResultInfo changeBindCard(SHChangeCard shCard){
        logger.info(">>>>>>>>>>开始拼接改绑XML");
        Map<String,Object> configMap = StringUtil.loadConfig();
        String privateKey = (String) configMap.get("privateKey");
        String publicKey = (String) configMap.get("publicKey");
        String postUrl = (String) configMap.get("postUrl");
        String pwd = (String) configMap.get("pwd");

        String KoalB64Cert = "";
        String Signature = "";
        ResultInfo resultInfo = new ResultInfo();
        try {
            logger.info(">>>>>>>>>>开始改绑加签");
            //加签
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            logger.info(">>>>>>>>>>加签成功");
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            String random = StringUtil.getStringRandom(36);
            String date = SDF.format(new Date());
            String time = TIME.format(new Date());
            logger.info(">>>>>>>>>流水号为"+random+"开始拼接待签名数据");
            logger.info(">>>>>>>>>>请求流水号为：" + random);
            //拼接待签名数据
            Map<String ,Object> rqMap = new HashMap<>();
            rqMap.put("BindCardNo",shCard.getBindCardNo());
            rqMap.put("ChannelId","YFY");
            rqMap.put("ClearDate",date);
            rqMap.put("CustName",shCard.getCustName());
            rqMap.put("IdNo",shCard.getIdNo());
            rqMap.put("ModiType",shCard.getModiType());
            rqMap.put("NewCardNo",shCard.getNewCardNo());
            rqMap.put("NewReservedPhone",shCard.getNewReservedPhone());
            rqMap.put("ProductCd",shCard.getProductCd());
            rqMap.put("ReservedPhone",shCard.getReservedPhone());
            rqMap.put("RqUID",random);
            rqMap.put("SPName","CBIB");
            rqMap.put("SubAcctNo",shCard.getSubAcctNo());
            rqMap.put("TranDate",date);
            rqMap.put("TranTime",time);
            String signDataStr = StringUtil.jointSignature(rqMap);
            //待签名的数据
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接xml");
            String joint = StringUtil.jointXML(rqMap);
            String interfaceNO = "YFY0002";  //接口编号
            //拼接xml
            String xml =
//                    "<?xml version='1.0' encoding='UTF-8'?>" +
//                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
//                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
//                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
//                    "<YFY0002Rq>" +
//                    "<CommonRqHdr>" +
//                    "<SPName>CBIB</SPName><RqUID>"+ random +"</RqUID>" +
//                    "<ClearDate>"+ date +"</ClearDate><TranDate>"+ date +"</TranDate>" +
//                    "<TranTime>"+ time +"</TranTime><ChannelId>YFY</ChannelId>" +
//                    "</CommonRqHdr>"
                    StringUtil.signHeader(interfaceNO,random,date,time)  + joint + StringUtil.signFooter(interfaceNO,KoalB64Cert,Signature);
//                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
//                    "</YFY0002Rq>" +
//                    "</BOSFXII>";
           // System.out.println(">>>>>>>>>>xml is :" + xml);
            logger.info(">>>>>>>>>>拼接xml完毕");
            logger.info(">>>>>>>>>>流水号为"+random+"开始发送请求给上海银行");
            //发送请求
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 20000);
            //String result = HttpsUtil.doPostSSL(postUrl,xml);
            logger.info(">>>>>>>>>>开始解析xml");
            Map<String,Object> resultMap = new HashMap<>();
            //解析XML
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator("YFY0002Rs"); // 获取根节点下的子节点BOSFXII
            SHChangeCard shChangeCard = new SHChangeCard();
            Map<String , Object> rsMap = new HashMap<>();
            while(iter.hasNext()){
                Element recordEle = (Element) iter.next();
                if (StringUtil.isEmpty(recordEle.elementTextTrim("SubAcctNo"))){
                    Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                    while (iters.hasNext()){
                        Element recordEles = (Element) iters.next();
                        //响应报文头信息
                        shChangeCard.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                        shChangeCard.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                        shChangeCard.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                        shChangeCard.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                    }

                }
                shChangeCard.setSubAcctNo(recordEle.elementTextTrim("SubAcctNo"));
                shChangeCard.setProductCd(recordEle.elementTextTrim("ProductCd"));
                shChangeCard.setCustName(recordEle.elementTextTrim("CustName"));
                shChangeCard.setNewCardNo(recordEle.elementTextTrim("NewCardNo"));
                shChangeCard.setNewReservedPhone(recordEle.elementTextTrim("NewReservedPhone"));
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0001Rs下的字节点Signature
                while (iters.hasNext()){
                    Element recordEles = (Element) iters.next();
                    //响应报文头信息
                    shChangeCard.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                    shChangeCard.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                    shChangeCard.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                    shChangeCard.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                }
            }
            if(!shChangeCard.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(shChangeCard.getServerStatusCode());
                resultMap.put("status",shChangeCard.getStatusCode());
                resultMap.put("SHChangeCard",shChangeCard);
                resultInfo.setData(resultMap);
                return resultInfo;
            }
            rsMap.put("SubAcctNo",shChangeCard.getSubAcctNo());
            rsMap.put("ProductCd",shChangeCard.getProductCd());
            rsMap.put("CustName",shChangeCard.getCustName());
            rsMap.put("NewCardNo",shChangeCard.getNewCardNo());
            rsMap.put("NewReservedPhone",shChangeCard.getNewReservedPhone());
            rsMap.put("StatusCode",shChangeCard.getStatusCode());
            rsMap.put("ServerStatusCode",shChangeCard.getServerStatusCode());
            rsMap.put("SPRsUID",shChangeCard.getSPRsUID());
            rsMap.put("RqUID",shChangeCard.getRqUID());
            logger.info(">>>>>>>>>>解析xml完毕");
            signDataStr = StringUtil.jointSignature(rsMap);
//            signDataStr = "CustName="+shChangeCard.getCustName()+"&NewCardNo="+shChangeCard.getNewCardNo()+"&NewReservedPhone="+shChangeCard.getNewReservedPhone()
//                    +"&ProductCd="+shChangeCard.getProductCd()+"&RqUID="+shChangeCard.getRqUID()+"&SPRsUID="+shChangeCard.getSPRsUID()
//                    +"&ServerStatusCode="+shChangeCard.getServerStatusCode()+"&StatusCode="+shChangeCard.getStatusCode()+"&SubAcctNo="+shChangeCard.getSubAcctNo()+"";

            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(Integer.parseInt(shChangeCard.getStatusCode()));
                resultInfo.setMessage(shChangeCard.getServerStatusCode());
                resultMap.put("status",shChangeCard.getStatusCode());
                resultMap.put("SHChangeCard",shChangeCard);
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,状态为：" + shChangeCard.getStatusCode() + ",信息为：" + shChangeCard.getServerStatusCode());
                return resultInfo;
            }
            if(!shChangeCard.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(shChangeCard.getServerStatusCode());
                resultMap.put("status",shChangeCard.getStatusCode());
                resultMap.put("SHChangeCard",shChangeCard);
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,状态为：" + shChangeCard.getStatusCode() + ",信息为：" + shChangeCard.getServerStatusCode());
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultMap.put("status",shChangeCard.getStatusCode());
            resultMap.put("SHChangeCard",shChangeCard);
            resultInfo.setData(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(">>>>>>>>上海二类账户改绑异常：",e);
        }
        return resultInfo;
    }

}
