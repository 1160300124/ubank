package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.Withdraw;
import com.ulaiber.web.utils.SslTest;
import com.ulaiber.web.utils.StringUtil;
import org.apache.log4j.Logger;
import org.apache.lucene.store.Lock;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 上海银行二类户提现接口
 * Created by daiqingwen on 2017/11/2.
 */
public class SHWithdraw {
    private static final Logger logger = Logger.getLogger(SHQueryBalance.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");


    /**
     * 提现
     * @rqMap wd 提现信息
     * @return
     */
    public static ResultInfo withdraw(Withdraw wd){
        logger.info(">>>>>>>>>>开始上海银行二类户提现操作");
        ResultInfo resultInfo = new ResultInfo();
        try{
            Map<String,Object> map = StringUtil.loadConfig();
            String privateKey = (String) map.get("privateKey");
            String publicKey = (String) map.get("publicKey");
            String postUrl = (String) map.get("postUrl");
            String pwd = (String) map.get("pwd");
            String KoalB64Cert = "";
            String Signature = "";
            logger.info(">>>>>>>>>>开始提现加签");
            //加签
            SvsSign signer = new SvsSign();
            signer.initSignCertAndKey(privateKey,pwd);
            //获取经过Base64处理的商户证书代码
            KoalB64Cert = signer.getEncodedSignCert();
            String random = StringUtil.getStringRandom(36);
            String date = SDF.format(new Date());
            String time = TIME.format(new Date());
            logger.info(">>>>>>>>>>请求流水号为：" + random);
            //拼接待签名数据
            Map<String ,Object> rqMap = new HashMap<>();
            rqMap.put("Amount",wd.getAmount());
            rqMap.put("ChannelId","YFY");
            rqMap.put("BindCardNo",wd.getBindCardNo());
            rqMap.put("Currency",wd.getCurrency());
            rqMap.put("ClearDate",date);
            rqMap.put("Purpose",wd.getPurpose());
            rqMap.put("RqUID",random);
            rqMap.put("SPName","CBIB");
            rqMap.put("ProductCd",wd.getProductCd());
            rqMap.put("TranDate",date);
            rqMap.put("SubAcctNo",wd.getSubAcctNo());
            rqMap.put("TheirRef",wd.getTheirRef());
            rqMap.put("MemoInfo",wd.getMemoInfo());
            rqMap.put("TranTime",time);
            String signDataStr = StringUtil.jointSignature(rqMap);
            //待签名的数据
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接查询xml");
            String joint = StringUtil.jointXML(rqMap);
            String interfaceNO = "YFY0021";  //接口编号
            String xml =  StringUtil.signHeader(interfaceNO,random,date,time) + joint + StringUtil.signFooter(interfaceNO,KoalB64Cert,Signature);
            System.out.println(">>>>>>>>>>xml is :" + xml);
            logger.info(">>>>>>>>>>拼接xml完毕");
            logger.info(">>>>>>>>>>开始发送请求给上海银行");
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 20000);
            logger.info(">>>>>>>>>>开始解析xml");
            Withdraw withdraw = new Withdraw();
            Map<String,Object> resultMap = new HashMap<>();
            //解析XML
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator("YFY0021Rs"); // 获取根节点下的子节点BOSFXII
            Map<String , Object> rsMap = new HashMap<>();
            while (iter.hasNext()){
                Element recordEle = (Element) iter.next();
                if(StringUtil.isEmpty(recordEle.elementTextTrim("SubAcctNo"))){
                    Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                    while (iters.hasNext()){
                        Element recordEles = (Element) iters.next();
                        //响应报文头信息
                        withdraw.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                        withdraw.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                        withdraw.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                        withdraw.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                    }
                    resultInfo.setCode(IConstants.QT_CODE_ERROR);
                    resultInfo.setMessage(withdraw.getServerStatusCode());
                    resultMap.put("status",withdraw.getStatusCode());
                    resultMap.put("withdraw",withdraw);
                    resultInfo.setData(resultMap);
                    return resultInfo;
                }
                withdraw.setBizDate(recordEle.elementTextTrim("BizDate"));          //交易日期
                withdraw.setSubAcctNo(recordEle.elementTextTrim("SubAcctNo"));      //余额理财子帐号
                withdraw.setBindCardNo(recordEle.elementTextTrim("BindCardNo"));    //银行卡号
                withdraw.setProductCd(recordEle.elementTextTrim("ProductCd"));      //银行卡号
                String amount = recordEle.elementTextTrim("Amount");
                withdraw.setAmount(StringUtil.round(amount));                          //交易金额
                withdraw.setCurrency(recordEle.elementTextTrim("Currency"));        //入账币种
                withdraw.setTheirRef(recordEle.elementTextTrim("TheirRef"));        //交易摘要
                withdraw.setPurpose(recordEle.elementTextTrim("Purpose"));          //用途
                withdraw.setAttach(recordEle.elementTextTrim("Attach"));            //附件信息
                withdraw.setMemoInfo(recordEle.elementTextTrim("MemoInfo"));        //交易备注
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0021Rs下的字节点Signature
                
                while (iters.hasNext()){
                    Element recordEles = (Element) iters.next();
                    //响应报文头信息
                    withdraw.setStatusCode(recordEles.elementTextTrim("StatusCode"));  //返回结果码
                    withdraw.setServerStatusCode(recordEles.elementTextTrim("ServerStatusCode")); //返回结果信息
                    withdraw.setSPRsUID(recordEles.elementTextTrim("SPRsUID"));   // 主机流水号
                    withdraw.setRqUID(recordEles.elementTextTrim("RqUID"));   //请求流水号
                }

            }
            if(!withdraw.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(withdraw.getServerStatusCode());
                resultMap.put("status",withdraw.getStatusCode());
                resultMap.put("withdraw",withdraw);
                resultInfo.setData(resultMap);
                return resultInfo;
            }
            rsMap.put("BizDate",withdraw.getBizDate());
            rsMap.put("SubAcctNo",withdraw.getSubAcctNo());
            rsMap.put("BindCardNo",withdraw.getBindCardNo());
            rsMap.put("ProductCd",withdraw.getProductCd());
            rsMap.put("Amount",withdraw.getAmount());
            rsMap.put("Currency",withdraw.getCurrency());
            rsMap.put("TheirRef",withdraw.getTheirRef());
            rsMap.put("Purpose",withdraw.getPurpose());
            rsMap.put("Attach",withdraw.getAttach());
            rsMap.put("MemoInfo",withdraw.getMemoInfo());
            rsMap.put("StatusCode",withdraw.getStatusCode());
            rsMap.put("ServerStatusCode",withdraw.getServerStatusCode());
            rsMap.put("SPRsUID",withdraw.getSPRsUID());
            rsMap.put("RqUID",withdraw.getRqUID());
            logger.info(">>>>>>>>>>解析xml完毕");
            signDataStr = StringUtil.jointSignature(rsMap);
//            signDataStr = "Amount="+withdraw.getAmount()+"&BindCardNo="+withdraw.getBindCardNo()+"&BizDate="+withdraw.getBizDate()
//                    +"&Currency="+withdraw.getCurrency()+"&ProductCd="+withdraw.getProductCd()+"&Purpose="+withdraw.getPurpose()
//                    +"&RqUID="+withdraw.getRqUID()+"&SPRsUID="+withdraw.getSPRsUID()+"&ServerStatusCode="+withdraw.getServerStatusCode()
//                    +"&StatusCode="+withdraw.getStatusCode()+"&SubAcctNo="+withdraw.getSubAcctNo()+"&TheirRef="+withdraw.getTheirRef();
            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(Integer.parseInt(withdraw.getStatusCode()));
                resultInfo.setMessage(withdraw.getServerStatusCode());
                resultMap.put("status",withdraw.getStatusCode());
                resultMap.put("withdraw",withdraw);
                resultInfo.setData(resultMap);
                System.out.println(">>>>>>>>>>验签失败,原因是返回结果为：" + verifyRet);
                 System.out.println(">>>>>>>>>>验签失败,状态为：" + withdraw.getStatusCode() + ",信息为：" + withdraw.getServerStatusCode());
                logger.error(">>>>>>>>>>验签失败,状态为：" + withdraw.getStatusCode() + ",信息为：" + withdraw.getServerStatusCode());
                return resultInfo;
            }
            if(!withdraw.getStatusCode().equals("0000")){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage(withdraw.getServerStatusCode());
                resultMap.put("status",withdraw.getStatusCode());
                resultMap.put("withdraw",withdraw);
                resultInfo.setData(resultMap);
                System.out.println(">>>>>>>>>>验签失败,状态为：" + withdraw.getStatusCode() + ",信息为：" + withdraw.getServerStatusCode());
                logger.error(">>>>>>>>>>验签失败,状态为：" + withdraw.getStatusCode() + ",信息为：" + withdraw.getServerStatusCode());
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultMap.put("status",withdraw.getStatusCode());
            resultMap.put("withdraw",withdraw);
            resultInfo.setData(resultMap);

        }catch(Exception e){
            e.printStackTrace();
            logger.error(">>>>>>>>>上海银行二类户提现操作失败" ,e);
        }
        return resultInfo;
    }
}
