package com.ulaiber.web.SHSecondAccount;

import com.koalii.svs.SvsSign;
import com.koalii.svs.SvsVerify;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.ShangHaiAcount.AccDetailVO;
import com.ulaiber.web.model.ShangHaiAcount.SHAccDetail;
import com.ulaiber.web.utils.SslTest;
import com.ulaiber.web.utils.StringUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 上海银行二类户账户明细查询
 * Created by daiqingwen on 2017/11/28.
 */
public class SHAccountDetail {
    private static final Logger logger = Logger.getLogger(SHAccountDetail.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HHmmss");

    public static ResultInfo queryDetail(AccDetailVO vo){
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
            Map<String ,Object> rqMap = new HashMap<>();
            rqMap.put("ChannelId","YFY");
            rqMap.put("ClearDate",date);
            rqMap.put("RqUID",random);
            rqMap.put("SPName","CBIB");
            rqMap.put("TranDate",date);
            rqMap.put("TranTime",time);
            rqMap.put("SubAcctNo",vo.getSubAcctNo());
            rqMap.put("Currency",vo.getCurrency());
            rqMap.put("BeginDt",vo.getBeginDt());
            rqMap.put("EndDt",vo.getEndDt());
            rqMap.put("PageSize",vo.getPageSize());
            rqMap.put("SkipRecord",vo.getSkipRecord());
            String signDataStr = StringUtil.jointSignature(rqMap);
            //获取签名数据，其中signDataStr为待签名字符串
            Signature  =  signer.signData(signDataStr.getBytes("GBK"));
            logger.info(">>>>>>>>>>开始拼接查询xml");
            String joint = StringUtil.jointXML(rqMap);
            String interfaceNO = "YFY0102";  //接口编号
            //拼接xml
            String xml = StringUtil.signHeader(interfaceNO,random,date,time) + joint + StringUtil.signFooter(interfaceNO,KoalB64Cert,Signature);
            logger.info(">>>>>>>>>>二类户账号为"+vo.getSubAcctNo()+"开始发送请求查询账户明细");
            //发送请求
            SslTest st = new SslTest();
            String result = st.postRequest(postUrl,xml, 20000);
            logger.info(">>>>>>>>>>开始解析xml");
            Map<String,Object> resultMap = new HashMap<>();
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            String resultSign = "";
            Iterator iter = root.elementIterator(""+interfaceNO+"Rs"); // 获取根节点下的子节点BOSFXII
            Map<String , Object> rsMap = new HashMap<>();
            List<String> sortList = new ArrayList<>();
            while (iter.hasNext()){
                Element recordEle = (Element) iter.next();
                if(StringUtil.isEmpty(recordEle.elementTextTrim("RecordNum"))){
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
                    resultMap.put("accDetail",rsMap);
                    resultInfo.setData(resultMap);
                    return resultInfo;
                }
                rsMap.put("RecordNum",recordEle.elementTextTrim("RecordNum"));
                rsMap.put("PageCount",recordEle.elementTextTrim("PageCount"));
                sortList.add("RecordNum=" + recordEle.elementTextTrim("RecordNum"));
                sortList.add("PageCount=" + recordEle.elementTextTrim("PageCount"));
                Iterator info = recordEle.elementIterator("TxnInfo");
                List<SHAccDetail> list = new ArrayList<>();
                while (info.hasNext()){
                    Element recordEles = (Element) info.next();
                    SHAccDetail accDetail = new SHAccDetail();
                    accDetail.setTheirRef(recordEles.elementTextTrim("TheirRef"));      //主机流水号
                    accDetail.setFlowCode(recordEles.elementTextTrim("FlowCode"));    //交易代码
                    accDetail.setTxnAmt(recordEles.elementTextTrim("TxnAmt"));        //交易金额
                    accDetail.setTxnBsnId(recordEles.elementTextTrim("TxnBsnId"));    //业务流水号
                    accDetail.setTxnDate(recordEles.elementTextTrim("TxnDate"));      //交易日期
                    accDetail.setTxnRef(recordEles.elementTextTrim("TxnRef"));      //交易摘要
                    accDetail.setTxnTime(recordEles.elementTextTrim("TxnTime"));      //交易时间
                    sortList.add("TheirRef=" + recordEles.elementTextTrim("TheirRef"));
                    sortList.add("FlowCode=" + recordEles.elementTextTrim("FlowCode"));
                    sortList.add("TxnAmt=" + recordEles.elementTextTrim("TxnAmt"));
                    sortList.add("TxnBsnId=" + recordEles.elementTextTrim("TxnBsnId"));
                    sortList.add("TxnDate=" + recordEles.elementTextTrim("TxnDate"));
                    sortList.add("TxnRef=" + recordEles.elementTextTrim("TxnRef"));
                    sortList.add("TxnTime=" + recordEles.elementTextTrim("TxnTime"));
                    list.add(accDetail);
                }
                rsMap.put("TxnInfo",list);
                resultSign = recordEle.elementTextTrim("Signature"); // 拿到YFY0102Rs下的字节点Signature
                Iterator iters = recordEle.elementIterator("CommonRsHdr"); // 获取节点下的子节点CommonRsHdr
                while (iters.hasNext()){
                    Element recordEles = (Element) iters.next();
                    //响应报文头信息
                    rsMap.put("StatusCode",recordEles.elementTextTrim("StatusCode")); //返回结果码
                    rsMap.put("ServerStatusCode",recordEles.elementTextTrim("ServerStatusCode"));   //返回结果信息
                    rsMap.put("SPRsUID",recordEles.elementTextTrim("SPRsUID"));         // 主机流水号
                    rsMap.put("RqUID",recordEles.elementTextTrim("RqUID"));  //请求流水号
                    sortList.add("StatusCode=" + recordEles.elementTextTrim("StatusCode"));
                    sortList.add("ServerStatusCode=" + recordEles.elementTextTrim("ServerStatusCode"));
                    sortList.add("SPRsUID=" + recordEles.elementTextTrim("SPRsUID"));
                    sortList.add("RqUID=" + recordEles.elementTextTrim("RqUID"));
                }
            }
            if(!"0000".equals(rsMap.get("StatusCode"))){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
                resultMap.put("status",rsMap.get("StatusCode"));
                resultMap.put("accDetail",rsMap);
                resultInfo.setData(resultMap);
                return resultInfo;
            }
           signDataStr = StringUtil.jointRepeat(sortList);
            logger.info(">>>>>>>>>>开始验签");
            //验签Signature
            int verifyRet = SvsVerify.verify(signDataStr.getBytes("GBK"),resultSign,publicKey);
            if(verifyRet != 0){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
                resultMap.put("status",rsMap.get("StatusCode"));
                resultMap.put("accDetail",rsMap);
                resultInfo.setData(resultMap);
                logger.error(">>>>>>>>>>二类户账号为"+vo.getSubAcctNo()+"验签失败,状态为："+verifyRet+"，返回结果为："+ rsMap.get("StatusCode") + ",信息为：" + rsMap.get("ServerStatusCode"));
                return resultInfo;
            }
            resultInfo.setCode(IConstants.QT_CODE_OK);
            resultInfo.setMessage((String) rsMap.get("ServerStatusCode"));
            resultMap.put("status",rsMap.get("StatusCode"));
            resultMap.put("accDetail",rsMap);
            resultInfo.setData(resultMap);
            logger.info(">>>>>>>>>>验签成功");
        }catch (Exception e){
            logger.error(">>>>>>>>>>查询账户明细失败",e);
        }
        return resultInfo;
    }
}
