package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
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
import java.util.Date;
import java.util.Map;
import java.util.Properties;

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
    public ResultInfo queryBalance(String SubAcctNo){
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
            //加签
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            //待签名的数据
            String signDataStr = "";
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            //拼接xml
            String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<BOSFXII xmlns='http://www.bankofshanghai.com/BOSFX/2010/08' " +
                    "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                    "xsi:schemaLocation='http://www.bankofshanghai.com/BOSFX/2010/08 BOSFX2.0.xsd'>" +
                    "<YFY0002Rq>" +
                    "<CommonRqHdr>" +
                    "<SPName>CBIB</SPName><RqUID>"+ StringUtil.getStringRandom(36) +"</RqUID>" +
                    "<ClearDate>"+ SDF.format(new Date()) +"</ClearDate><TranDate>"+ SDF.format(new Date()) +"</TranDate>" +
                    "<TranTime>"+ TIME.format(new Date()) +"</TranTime><ChannelId>YFY</ChannelId>" +
                    "</CommonRqHdr>" +
                    "<KoalB64Cert>"+ KoalB64Cert +"</KoalB64Cert><Signature>"+ Signature +"</Signature>" +
                    "</YFY0002Rq>" +
                    "</BOSFXII>";

            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 8000);
            System.out.print(">>>>>>>>>>>>>>改绑请求结果为 ：" + result);
            if(StringUtil.isEmpty(result)){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
            //验签
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),result,publicKey);
            if(verifyRet == 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
            //解析XML
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String StatusCode = root.elementText("StatusCode");  //返回结果码
            String ServerStatusCode = root.elementText("ServerStatusCode"); //返回结果信息
            String RqUID = root.elementText("RqUID");   //请求流水号
//            shChangeCard.setStatusCode(StatusCode);
//            shChangeCard.setServerStatusCode(ServerStatusCode);
            if(!StatusCode.equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                return resultInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
