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
        ResultInfo resultInfo = new ResultInfo();
        try {
            Map<String,Object> configMap = StringUtil.loadConfig();
            String privateKey = (String) configMap.get("privateKey");
            String publicKey = (String) configMap.get("publicKey");
            String postUrl = (String) configMap.get("postUrl");
            String pwd = (String) configMap.get("pwd");


            String KoalB64Cert = "";
            String Signature = "";
            logger.info(">>>>>>>>>>开始加签");
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
            //拼接待签名数据
            map.put("SPName","CBIB");
            map.put("RqUID",random);
            map.put("ClearDate",date);
            map.put("TranDate",date);
            map.put("TranTime",time);
            map.put("ChannelId","YFY");
            String signDataStr = StringUtil.jointSignature(map);
            //待签名的数据
            logger.info(">>>>>>>>>获取Signature");
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接xml");
            String joint = StringUtil.jointXML(map);
            String interfaceNO = "YFY0001";  //接口编号
            //拼接xml
            String xml =
//                    "<?xml version='1.0' encoding='UTF-8'?>" +
//                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
//                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
//                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
//                    "<YFY0001Rq>" +
//                    "<CommonRqHdr>" +
//                    "<SPName>CBIB</SPName><RqUID>"+ random +"</RqUID>" +
//                    "<ClearDate>"+ date +"</ClearDate><TranDate>"+ date +"</TranDate>" +
//                    "<TranTime>"+ time +"</TranTime><ChannelId>YFY</ChannelId>" +
//                    "</CommonRqHdr>"
                    StringUtil.signHeader(interfaceNO,random,date,time)  + joint + StringUtil.signFooter(interfaceNO,KoalB64Cert,Signature);
//                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
//                    "</YFY0001Rq>" +
//                    "</BOSFXII>";
            logger.info(">>>>>>>>>>拼接xml完毕");
            logger.info(">>>>>>>>>>流水号为"+random+"开始发送请求给上海银行");
            //发送请求
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 30000);
            //String result = HttpsUtil.doPostSSL(postUrl,xml);
            //logger.info(">>>>>>>>>>请求结果为：" + result);
          //  System.out.println(">>>>>>>>>>>>>>请求结果为 ：" + result);
            Map<String,Object> resultMap = new HashMap<>();
            logger.info(">>>>>>>>>>开始解析xml");
            //解析XML
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator("YFY0001Rs"); // 获取根节点下的子节点BOSFXII
            SecondAcount secondAcount = new SecondAcount();
            Map<String , Object> rsMap = new HashMap<>();
            while (iter.hasNext()){
                Element recordEle = (Element) iter.next();
                if (StringUtil.isEmpty(recordEle.elementTextTrim("SubAcctNo"))){
                    Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                    while (iters.hasNext()){
                        Element recordEles = (Element) iters.next();
                        //响应报文头信息
                        secondAcount.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                        secondAcount.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                        secondAcount.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                        secondAcount.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                    }
                    resultInfo.setCode(IConstants.QT_CODE_ERROR);
                    resultInfo.setMessage(secondAcount.getServerStatusCode());
                    resultMap.put("secondAcount",secondAcount);
                    resultMap.put("status",secondAcount.getStatusCode());
                    resultInfo.setData(resultMap);
                    return resultInfo;

                }
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
                    //响应报文头信息
                    secondAcount.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                    secondAcount.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                    secondAcount.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                    secondAcount.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                }
            }
            logger.info(">>>>>>>>>>解析xml完毕");
            logger.info(">>>>>>>>>>secondAcount is :" + secondAcount);
            if(!secondAcount.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(secondAcount.getServerStatusCode());
                resultMap.put("secondAcount",secondAcount);
                resultMap.put("status",secondAcount.getStatusCode());
                resultInfo.setData(resultMap);
                return resultInfo;
            }
            rsMap.put("CoopCustNo",secondAcount.getCoopCustNo());
            rsMap.put("ProductCd",secondAcount.getProductCd());
            rsMap.put("CustName",secondAcount.getCustName());
            rsMap.put("IdNo",secondAcount.getIdNo());
            rsMap.put("Sign",secondAcount.getSign());
            rsMap.put("AcctOpenResult",secondAcount.getAcctOpenResult());
            rsMap.put("EacctNo",secondAcount.getEacctNo());
            rsMap.put("SubAcctNo",secondAcount.getSubAcctNo());
            rsMap.put("FundCode",secondAcount.getFundCode());
            rsMap.put("FundAcctOpenResult",secondAcount.getFundAcctOpenResult());
            rsMap.put("FundAcct",secondAcount.getFundAcct());
            rsMap.put("FundTxnAcct",secondAcount.getFundTxnAcct());
            rsMap.put("StatusCode",secondAcount.getStatusCode());
            rsMap.put("ServerStatusCode",secondAcount.getServerStatusCode());
            rsMap.put("SPRsUID",secondAcount.getSPRsUID());
            rsMap.put("RqUID",secondAcount.getRqUID());
            signDataStr = StringUtil.jointSignature(rsMap);
//            signDataStr = "AcctOpenResult="+secondAcount.getAcctOpenResult()+"&CoopCustNo="+secondAcount.getCoopCustNo()+"&CustName="
//                    +secondAcount.getCustName()+"&EacctNo="+secondAcount.getEacctNo()
//                    +"&IdNo="+secondAcount.getIdNo()+"&ProductCd="+secondAcount.getProductCd()+"&RqUID="+secondAcount.getRqUID()
//                    +"&SPRsUID="+secondAcount.getSPRsUID()+"&ServerStatusCode="+secondAcount.getServerStatusCode()+"&Sign="
//                    +secondAcount.getSign()+"&StatusCode="+secondAcount.getStatusCode()+"&SubAcctNo="+secondAcount.getSubAcctNo();
            System.out.println(">>>>>>>>>signDataStr :" + signDataStr);
           // System.out.println(">>>>>>>>>resultSign :" + resultSign);
            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            //  System.out.println(">>>>>>>>>>>验签结果为：" + verifyRet);
            if(verifyRet != 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(secondAcount.getServerStatusCode());
                resultMap.put("secondAcount",secondAcount);
                resultMap.put("status",secondAcount.getStatusCode());
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
                logger.error(">>>>>>>>>>验签失败,状态为：" + secondAcount.getStatusCode() + ",信息为：" + secondAcount.getServerStatusCode());
                return resultInfo;
            }
            if(!secondAcount.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(secondAcount.getServerStatusCode());
                resultMap.put("secondAcount",secondAcount);
                resultMap.put("status",secondAcount.getStatusCode());
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,状态为：" + secondAcount.getStatusCode() + ",信息为：" + secondAcount.getServerStatusCode());
                return resultInfo;
            }
            resultMap.put("secondAcount",secondAcount);
            resultMap.put("status",secondAcount.getStatusCode());
            resultInfo.setData(resultMap);
            resultInfo.setMessage(secondAcount.getServerStatusCode());
            resultInfo.setCode(IConstants.QT_CODE_OK);

        } catch (Exception e) {
           // e.printStackTrace();
           logger.error(">>>>>>>>申请上海银行二类户异常：",e);
        }

        return resultInfo;
    }
}
