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
            logger.info(">>>>>>>>>开始拼接待签名数据");
            //待签名的数据
            String signDataStr = "BindCardNo="+ shCard.getBindCardNo() +"&ChannelId=YFY&ClearDate="+date+"&CustName=" +
                    shCard.getCustName() + "&IdNo=" + shCard.getIdNo() +"&ModiType=" + shCard.getModiType() +"&NewCardNo="+
                    shCard.getNewCardNo() +"&NewReservedPhone="+shCard.getNewReservedPhone() +"&ProductCd="+shCard.getProductCd()+
                    "&ReservedPhone="+ shCard.getReservedPhone() +"&RqUID="+ random +
                    "&SPName=CBIB&SubAcctNo=" +shCard.getSubAcctNo()+"&TranDate="+date+"&TranTime="+time;
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接xml");
            //拼接xml
            String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
                    "<YFY0002Rq>" +
                    "<CommonRqHdr>" +
                    "<SPName>CBIB</SPName><RqUID>"+ random +"</RqUID>" +
                    "<ClearDate>"+ date +"</ClearDate><TranDate>"+ date +"</TranDate>" +
                    "<TranTime>"+ time +"</TranTime><ChannelId>YFY</ChannelId>" +
                    "</CommonRqHdr>" +
                    "<SubAcctNo>"+ shCard.getSubAcctNo() +"</SubAcctNo><ProductCd>"+ shCard.getProductCd() +"</ProductCd>" +
                    "<CustName>"+ shCard.getCustName() +"</CustName><IdNo>"+ shCard.getIdNo()+"</IdNo>" +
                    "<BindCardNo>"+ shCard.getBindCardNo() +"</BindCardNo><NewCardNo>"+ shCard.getNewCardNo()+"</NewCardNo>" +
                    "<ReservedPhone>"+ shCard.getReservedPhone() +"</ReservedPhone><NewReservedPhone>"+ shCard.getNewReservedPhone() +"</NewReservedPhone>" +
                    "<ModiType>"+ shCard.getModiType() +"</ModiType>" +
                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
                    "</YFY0002Rq>" +
                    "</BOSFXII>";
            System.out.println(">>>>>>>>>>xml is :" + xml);
            logger.info(">>>>>>>>>>拼接xml完毕");
            logger.info(">>>>>>>>>>开始发送请求给上海银行");
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 10000);
            System.out.print(">>>>>>>>>>>>>>改绑请求结果为 ：" + result);
            logger.info(">>>>>>>>>>>>>>改绑请求结果为 ："+ result);
            if(StringUtil.isEmpty(result)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
            logger.info(">>>>>>>>>>开始解析xml");
            //解析XML
            Map<String,String> resultXML = new HashMap<>();
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator("YFY0002Rs"); // 获取根节点下的子节点BOSFXII
            SHChangeCard shChangeCard = new SHChangeCard();
            while(iter.hasNext()){
                Element recordEle = (Element) iter.next();
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
                resultInfo.setCode(Integer.parseInt(shChangeCard.getStatusCode()));
                resultInfo.setMessage(shChangeCard.getServerStatusCode());
                return resultInfo;
            }
            logger.info(">>>>>>>>>>解析xml完毕");
            signDataStr = "CustName="+shChangeCard.getCustName()+"&NewCardNo="+shChangeCard.getNewCardNo()+"&NewReservedPhone="+shChangeCard.getNewReservedPhone()
                    +"&ProductCd="+shChangeCard.getProductCd()+"&RqUID="+shChangeCard.getRqUID()+"&SPRsUID="+shChangeCard.getSPRsUID()
                    +"&ServerStatusCode="+shChangeCard.getServerStatusCode()+"&StatusCode="+shChangeCard.getStatusCode()+"&SubAcctNo="+shChangeCard.getSubAcctNo()+"";

            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(Integer.parseInt(shChangeCard.getStatusCode()));
                resultInfo.setMessage(shChangeCard.getServerStatusCode());
                System.out.println(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
                System.out.println(">>>>>>>>>>验签失败,状态为：" + shChangeCard.getStatusCode() + ",信息为：" + shChangeCard.getServerStatusCode());
               // logger.error(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
               // logger.error(">>>>>>>>>>验签失败,状态为：" + shChangeCard.getStatusCode() + ",信息为：" + shChangeCard.getServerStatusCode());
                return resultInfo;
            }
            if(!shChangeCard.getStatusCode().equals("0000")){
                resultInfo.setCode(Integer.parseInt(shChangeCard.getStatusCode()));
                resultInfo.setMessage(shChangeCard.getServerStatusCode());
                System.out.println(">>>>>>>>>>验签失败,状态为：" + shChangeCard.getStatusCode() + ",信息为：" + shChangeCard.getServerStatusCode());
                //logger.error(">>>>>>>>>>验签失败,状态为：" + shChangeCard.getStatusCode() + ",信息为：" + shChangeCard.getServerStatusCode());
                return resultInfo;
            }
            resultInfo.setData(shChangeCard);
            resultInfo.setCode(Integer.parseInt(shChangeCard.getStatusCode()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(">>>>>>>>上海二类账户改绑异常：",e);
        }
        return resultInfo;
    }

}
