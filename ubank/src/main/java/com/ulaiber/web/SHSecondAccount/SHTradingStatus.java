package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.utils.HttpsUtil;
import com.ulaiber.web.utils.SslTest;
import com.ulaiber.web.utils.StringUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 上海银行二类户交易状态查询
 * Created by daiqingwen on 2017/11/10.
 */
public class SHTradingStatus {
    private static final Logger logger = Logger.getLogger(SHTradingStatus.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");

    /**
     * 交易状态查询
     * @param RqUID 交易流水号
     * @return ResultInfo
     */
    public static ResultInfo tradingStatus(String RqUID){
        logger.info(">>>>>>>>>>开始上海银行二类户交易状态查询操作");
        ResultInfo resultInfo = new ResultInfo();
        try {
            Map<String,Object> map = StringUtil.loadConfig();
            String privateKey = (String) map.get("privateKey");
            String publicKey = (String) map.get("publicKey");
            String postUrl = (String) map.get("postUrl");
            String pwd = (String) map.get("pwd");
            String KoalB64Cert = "";
            String Signature = "";
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            //String random = StringUtil.getStringRandom(36);
            String random = SDF.format(new Date()) + TIME.format(new Date()) + StringUtil.getFixLenthString(22);
            String date = SDF.format(new Date());
            String time = TIME.format(new Date());
            logger.info(">>>>>>>>>流水号为'"+random+"'开始拼接待签名数据");
            //拼接待签名数据
            Map<String ,Object> rqMap = new HashMap<>();
            rqMap.put("SPName",IConstants.SPName);
            rqMap.put("ChannelId",IConstants.ChannelId);
            rqMap.put("ClearDate",date);
            rqMap.put("RqUID",random);
            rqMap.put("OriRqUID",RqUID);
            rqMap.put("TranDate",date);
            rqMap.put("TranTime",time);
            String signDataStr = StringUtil.jointSignature(rqMap);
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接查询xml");
            String joint = StringUtil.jointXML(rqMap);
            String interfaceNO = "YFY0103";  //接口编号
            //拼接xml
            String xml = StringUtil.signHeader(interfaceNO,random,date,time) + joint + StringUtil.signFooter(interfaceNO,KoalB64Cert,Signature);
            logger.info(">>>>>>>>>>流水号为"+RqUID+"开始发送请求给上海银行");
            //发送请求
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 20000);
           // String result = HttpsUtil.doPostSSL(postUrl,xml);
            logger.info(">>>>>>>>>>开始解析xml");
            Map<String,Object> resultMap = new HashMap<>();
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator(""+interfaceNO+"Rs"); // 获取根节点下的子节点BOSFXII
            Map<String , Object> rsMap = new HashMap<>();
            while (iter.hasNext()){
                Element recordEle = (Element) iter.next();
                if(StringUtil.isEmpty(recordEle.elementTextTrim("OriRqUID"))){
                    Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                    while (iters.hasNext()){
                        Element recordEles = (Element) iters.next();
                        //响应报文头信息
                        rsMap.put("StatusCode",recordEles.elementTextTrim("StatusCode")); //返回结果码
                        rsMap.put("ServerStatusCode",recordEles.elementTextTrim("ServerStatusCode"));   //返回结果信息
                        rsMap.put("SPRsUID",recordEles.elementTextTrim("SPRsUID"));         // 主机流水号
                        rsMap.put("RqUID",recordEles.elementTextTrim("RqUID"));  //请求流水号
                    }
                    resultInfo.setCode(IConstants.QT_CODE_ERROR);
                    resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
                    resultMap.put("status",rsMap.get("StatusCode"));
                    resultMap.put("tradingSta",rsMap);
                    resultInfo.setData(resultMap);
                    return resultInfo;
                }
                rsMap.put("ChannelId",recordEle.elementTextTrim("ChannelId"));    //渠道
                rsMap.put("OriRqUID",recordEle.elementTextTrim("OriRqUID"));     //原交易流水号
                rsMap.put("RespTxnNo",recordEle.elementTextTrim("RespTxnNo"));    //核心响应交易流水号
                rsMap.put("Amt",recordEle.elementTextTrim("Amt"));          //交易金额
                rsMap.put("TranDate",recordEle.elementTextTrim("TranDate"));     //交易日期
                rsMap.put("TxnStatus",recordEle.elementTextTrim("TxnStatus"));    //交易状态
                rsMap.put("TxnResult",recordEle.elementTextTrim("TxnResult"));    //交易状态描述
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0103Rs下的字节点Signature
                while (iters.hasNext()){
                    Element recordEles = (Element) iters.next();
                    //响应报文头信息
                    rsMap.put("StatusCode",recordEles.elementTextTrim("StatusCode")); //返回结果码
                    rsMap.put("ServerStatusCode",recordEles.elementTextTrim("ServerStatusCode"));   //返回结果信息
                    rsMap.put("SPRsUID",recordEles.elementTextTrim("SPRsUID"));         // 主机流水号
                    rsMap.put("RqUID",recordEles.elementTextTrim("RqUID"));  //请求流水号
                }

            }
            if(!"0000".equals(rsMap.get("StatusCode"))){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
                resultMap.put("status",rsMap.get("StatusCode"));
                resultMap.put("tradingSta",rsMap);
                resultInfo.setData(resultMap);
                return resultInfo;
            }
            signDataStr = StringUtil.jointSignature(rsMap);
            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
                resultMap.put("status",rsMap.get("StatusCode"));
                resultMap.put("tradingSta",rsMap);
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>流水号为："+RqUID+"验签失败,状态为："+verifyRet+"，返回结果为："+ rsMap.get("StatusCode") + ",信息为：" + rsMap.get("ServerStatusCode"));
                return resultInfo;
            }
            resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
            resultMap.put("status",rsMap.get("StatusCode"));
            resultMap.put("tradingSta",rsMap);
            resultInfo.setData(resultMap);
        }catch(Exception e){
            logger.error(">>>>>>>>>>上海银行二类户交易状态查询异常：",e);
        }
        return resultInfo;
    }

}
