package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
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
        Map<String,Object> map = StringUtil.loadConfig();
        String privateKey = (String) map.get("privateKey");
        String publicKey = (String) map.get("publicKey");
        String postUrl = (String) map.get("postUrl");
        String pwd = (String) map.get("pwd");
        ResultInfo resultInfo = new ResultInfo();
        String KoalB64Cert = "";
        String Signature = "";
        try {
            logger.info(">>>>>>>>>>开始加签");
            //加签
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            String random = StringUtil.getStringRandom(36);
            String date = SDF.format(new Date());
            String time = TIME.format(new Date());
            //待签名的数据
            String signDataStr = "ChannelId=YFY&ClearDate="+date+"&RqUID="+random+"&SPName=CBIB&SubAcctNo="+SubAcctNo+"&TranDate="+date+"&TranTime="+time+"";
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接查询xml");
            //拼接xml
            String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
                    "<YFY0101Rq>" +
                    "<CommonRqHdr>" +
                    "<SPName>CBIB</SPName><RqUID>"+ random +"</RqUID>" +
                    "<ClearDate>"+ date +"</ClearDate><TranDate>"+ date +"</TranDate>" +
                    "<TranTime>"+ time +"</TranTime><ChannelId>YFY</ChannelId>" +
                    "</CommonRqHdr>" +
                    "<SubAcctNo>"+ SubAcctNo +"</SubAcctNo>" +
                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
                    "</YFY0101Rq>" +
                    "</BOSFXII>";
            System.out.println(">>>>>>>>>>xml is :" + xml);
            logger.info(">>>>>>>>>>拼接xml完毕");
            logger.info(">>>>>>>>>>开始发送请求给上海银行");
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 8000);
            System.out.print(">>>>>>>>>>>>>>查询上海银行二类户余额结果为 ：" + result);
            logger.info(">>>>>>>>>>>>>>查询上海银行二类户余额结果为 ："+ result);
            logger.info(">>>>>>>>>>开始解析xml");
            SecondAcount sa = new SecondAcount();
            Map<String,Object> resultMap = new HashMap<>();
            //解析XML
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator("YFY0101Rs"); // 获取根节点下的子节点BOSFXII
            while(iter.hasNext()){
                Element recordEle = (Element) iter.next();
                sa.setSubAcctNo(recordEle.elementTextTrim("SubAcctNo"));
                double bal = Double.parseDouble(recordEle.elementTextTrim("WorkingBal"));
                double avaifun = Double.parseDouble(recordEle.elementTextTrim("AvaiFundShare"));
                sa.setAvaiBal(bal + avaifun);
                sa.setWorkingBal(bal);
                sa.setFundShare(Double.parseDouble(recordEle.elementTextTrim("FundShare")));
                sa.setAvaiFundShare(Double.parseDouble(recordEle.elementTextTrim("AvaiFundShare")));
                sa.setEarningsYesterday(Double.parseDouble(recordEle.elementTextTrim("EarningsYesterday")));
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0001Rs下的字节点Signature
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
                resultInfo.setCode(Integer.parseInt(sa.getStatusCode()));
                resultInfo.setMessage(sa.getServerStatusCode());
                return resultInfo;
            }
            //验签
            signDataStr = "AvaiBal="+sa.getAvaiBal()+"&AvaiFundShare="+sa.getAvaiFundShare()+"&EarningsYesterday="+sa.getEarningsYesterday()
                    +"&FundShare="+sa.getFundShare()+"&RqUID="+sa.getRqUID()+"&SPRsUID="+sa.getSPRsUID()+"&ServerStatusCode="+sa.getServerStatusCode()
                    +"&StatusCode="+sa.getStatusCode()+"&SubAcctNo="+sa.getSubAcctNo()+"&WorkingBal="+sa.getWorkingBal()+"";

            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(Integer.parseInt(sa.getStatusCode()));
                resultInfo.setMessage(sa.getServerStatusCode());
                System.out.println(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
                System.out.println(">>>>>>>>>>验签失败,状态为：" + sa.getStatusCode() + ",信息为：" + sa.getServerStatusCode());
                logger.error(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
                logger.error(">>>>>>>>>>验签失败,状态为：" + sa.getStatusCode() + ",信息为：" + sa.getServerStatusCode());
                return resultInfo;
            }
            if(!sa.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(sa.getServerStatusCode());
                resultMap.put("secondAcount",sa);
                resultMap.put("status",sa.getStatusCode());
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>验签失败,状态为：" + sa.getStatusCode() + ",信息为：" + sa.getServerStatusCode());
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultMap.put("status",sa.getStatusCode());
            resultMap.put("secondAcount",sa);
            resultInfo.setData(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
