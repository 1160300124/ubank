package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询（上海银行）二类户余额
 * Created by daiqingwen on 2017/10/26.
 */
public class SHQueryBalance {
    private static final Logger logger = Logger.getLogger(SHQueryBalance.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");

    /**
     * 查询二类户余额
     * @param SubAcctNo 余额理财子账号
     * @return ResultInfo
     */
    public static ResultInfo queryBalance(String SubAcctNo){
        logger.info(">>>>>>>>>>开始查询上海银行二类户余额");
        ResultInfo resultInfo = new ResultInfo();
        try {
            Map<String,Object> map = StringUtil.loadConfig();
            String privateKey = (String) map.get("privateKey");
            String publicKey = (String) map.get("publicKey");
            String postUrl = (String) map.get("postUrl");
            String pwd = (String) map.get("pwd");
            String KoalB64Cert = "";
            String Signature = "";
            logger.info(">>>>>>>>>>开始查余额加签");
            //加签
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            String random = StringUtil.getStringRandom(36);
            String date = SDF.format(new Date());
            String time = TIME.format(new Date());
            logger.info(">>>>>>>>>流水号为'"+random+"'开始拼接待签名数据");
            //拼接待签名数据
            Map<String ,Object> rqMap = new HashMap<>();
            rqMap.put("ChannelId","YFY");
            rqMap.put("ClearDate",date);
            rqMap.put("RqUID",random);
            rqMap.put("SPName","CBIB");
            rqMap.put("SubAcctNo",SubAcctNo);
            rqMap.put("TranDate",date);
            rqMap.put("TranTime",time);
            String signDataStr = StringUtil.jointSignature(rqMap);
            //待签名的数据
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接查询xml");
            String joint = StringUtil.jointXML(rqMap);
            String interfaceNO = "YFY0101";  //接口编号
            //拼接xml
            String xml = StringUtil.signHeader(interfaceNO,random,date,time) + joint + StringUtil.signFooter(interfaceNO,KoalB64Cert,Signature);
//                    "<?xml version='1.0' encoding='UTF-8'?>" +
//                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
//                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
//                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
//                    "<YFY0101Rq>" +
//                    "<CommonRqHdr>" +
//                    "<SPName>CBIB</SPName><RqUID>"+ random +"</RqUID>" +
//                    "<ClearDate>"+ date +"</ClearDate><TranDate>"+ date +"</TranDate>" +
//                    "<TranTime>"+ time +"</TranTime><ChannelId>YFY</ChannelId>" +
//                    "</CommonRqHdr>"
//                     + joint +
//                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
//                    "</YFY0101Rq>" +
//                    "</BOSFXII>";
            logger.info(">>>>>>>>>>流水号为"+random+"开始发送请求给上海银行");
            //发送请求
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 20000);
           // String result = HttpsUtil.doPostSSL(postUrl,xml);
            logger.info(">>>>>>>>>>开始解析xml");
            SecondAcount sa = new SecondAcount();
            Map<String,Object> resultMap = new HashMap<>();
            //解析XML
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator(""+interfaceNO+"Rs"); // 获取根节点下的子节点BOSFXII
            String workingBal = "";
            String avaifundShare = "";
            String fundShare = "";
            String earningsYesterday = "";
            String avaiBal = "";
            Map<String , Object> rsMap = new HashMap<>();
            while(iter.hasNext()){
                Element recordEle = (Element) iter.next();
                if(StringUtil.isEmpty(recordEle.elementTextTrim("SubAcctNo"))){
                    Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                    while (iters.hasNext()){
                        Element recordEles = (Element) iters.next();
                        //响应报文头信息
                        sa.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                        sa.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                        sa.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                        sa.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                    }
                    resultInfo.setCode(IConstants.QT_CODE_ERROR);
                    resultInfo.setMessage(sa.getServerStatusCode());
                    resultMap.put("status",sa.getStatusCode());
                    resultMap.put("secondAccount",sa);
                    resultInfo.setData(resultMap);
                    return resultInfo;
                }
                sa.setSubAcctNo(recordEle.elementTextTrim("SubAcctNo"));
                avaiBal = recordEle.elementTextTrim("AvaiBal");
                workingBal = recordEle.elementTextTrim("WorkingBal");
                avaifundShare = recordEle.elementTextTrim("AvaiFundShare");
                fundShare =recordEle.elementTextTrim("FundShare");
                earningsYesterday = recordEle.elementTextTrim("EarningsYesterday");
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0101Rs下的字节点Signature
                while (iters.hasNext()){
                    Element recordEles = (Element) iters.next();
                    //响应报文头信息
                    sa.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                    sa.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                    sa.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                    sa.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                }
            }
            if(!sa.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(sa.getServerStatusCode());
                resultMap.put("status",sa.getStatusCode());
                resultMap.put("secondAccount",sa);
                resultInfo.setData(resultMap);
                return resultInfo;
            }
            rsMap.put("SubAcctNo",sa.getSubAcctNo());
            rsMap.put("AvaiBal",avaiBal);
            rsMap.put("WorkingBal",workingBal);
            rsMap.put("AvaiFundShare",avaifundShare);
            rsMap.put("FundShare",fundShare);
            rsMap.put("EarningsYesterday",earningsYesterday);
            rsMap.put("StatusCode",sa.getStatusCode());
            rsMap.put("ServerStatusCode",sa.getServerStatusCode());
            rsMap.put("SPRsUID",sa.getSPRsUID());
            rsMap.put("RqUID",sa.getRqUID());
            signDataStr = StringUtil.jointSignature(rsMap);
            //验签
//            signDataStr = "AvaiBal="+avaiBal+"&AvaiFundShare="+avaifundShare+"&EarningsYesterday="+earningsYesterday
//                    +"&FundShare="+fundShare+"&RqUID="+sa.getRqUID()+"&SPRsUID="+sa.getSPRsUID()+"&ServerStatusCode="+sa.getServerStatusCode()
//                    +"&StatusCode="+sa.getStatusCode()+"&SubAcctNo="+sa.getSubAcctNo()+"&WorkingBal="+workingBal+"";

            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(sa.getServerStatusCode());
                resultMap.put("secondAccount",sa);
                resultMap.put("status",sa.getStatusCode());
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
                logger.error(">>>>>>>>>>验签失败,状态为：" + sa.getStatusCode() + ",信息为：" + sa.getServerStatusCode());
                return resultInfo;
            }
            logger.info(">>>>>>>>>>验签成功");
            sa.setAvaiBal(StringUtil.round(workingBal) + StringUtil.round(avaifundShare));
            sa.setWorkingBal(StringUtil.round(workingBal));
            sa.setFundShare(StringUtil.round(fundShare));
            sa.setAvaiFundShare(StringUtil.round(avaifundShare));
            sa.setEarningsYesterday(StringUtil.round(earningsYesterday));
            if(!sa.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(sa.getServerStatusCode());
                resultMap.put("secondAccount",sa);
                resultMap.put("status",sa.getStatusCode());
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,状态为：" + sa.getStatusCode() + ",信息为：" + sa.getServerStatusCode());
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage(sa.getServerStatusCode());
            resultMap.put("secondAccount",sa);
            resultMap.put("status",sa.getStatusCode());
            resultInfo.setData(resultMap);

        } catch (Exception e) {
           // e.printStackTrace();
            logger.error(">>>>>>>>>>查询上海银行二类户余额失败，原因为：",e);
        }
        return resultInfo;
    }



}
