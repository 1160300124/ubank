/** 
 * 文件： demo.java
 * 创建日期 2015年6月25日
 *
 * Copyright 2004-2012
 * Client Server International, Inc. All rights reserved.
 * 北京科蓝软件系统有限公司，保留所有版权。
**/
package com.ulaiber.web.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.rmi.CORBA.Util;
import javax.xml.crypto.dsig.TransformException;


/**
 * @description TODO(这里用一句话描述这个类的作用)
 * @date        2015年6月25日 下午6:36:51
 * @version     1.0
 * @author      asus
 *
 * Modified history
 *
 * modified date:
 * modifier user:
 * description:
**/
public class demo {
    private static Map ROOT = new HashMap();
    private static Map IASPDB = new HashMap();
    private static Map PLAIN = new HashMap();
    private static Map HEADMAP = new HashMap();
    private static Map BODYMAP = new HashMap();
    private static String SIGNATURE = "";
    
    private static final String priKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCi5H7XuvhSCUOJ\n" +
            "tBydT3NRPPIV56PoSI9+61QODZSc5BzMjGLRjx4Dpjd8H6RQUPWEYcrfczUwhhgf\n" +
            "/A6LNENyNzfDugrs0QTKWiaTCaW30VAfLOuWrTb1bwrQ3fv86Ki4pdXoCcN8+pCL\n" +
            "HUROUQ4kcpHrNAx3YhOGCXhKLYZEd0/W5bjzSQpoIfgHARN8jMYXsQMyGXRAgJ18\n" +
            "Xfim0SIqjUhk+7DIoOgHFqros797dyeOwml8rQAMxXgHoK4vUtuXVdOa+t1DCZhZ\n" +
            "kmohSkYGHV1R94ZoYatUmyWCPUCQKpHpMjR+BFra6AdTqtGTNAqdhyZSUXr3mBln\n" +
            "xKWms1RjAgMBAAECggEAbExmWJ+iQUiPcpog/CW6kaRnf1DGlMTJQqTK+Zzl1XmD\n" +
            "/CrJ+HGijuKPuSKAaDWrRhyOeQlrr5s3puw57ysMPH+hv6uNX+2HtdFCHDgpyZCM\n" +
            "KG4BI+h9xafSFRPBWWCF7F21XdQ/+HPObe9DXp5dPLY/dZThRO4b9N2O8CJcRl/N\n" +
            "R7XizMH9kLM6h0Q1f4JoRpF/Ca2CFvpfjgCqG22fKxW3JN7T1Qw3IG+JGR3WurSh\n" +
            "7cLczasrpLYrOuvI2L0TdtwMwU3HzY3YUBB8Cye9woe0rWyWuP3e1EiB6+pTfN7Y\n" +
            "QMUGvGf4AJkuxWtv0A0d4Qvc3fp05he0hGUMIUkXQQKBgQDQpV5/pHJE6LOhvQf/\n" +
            "6L52M9sX89fWgBNSOpZWjq3TkPYK7RvAUAE/omXtrMoiKGRw0t6gHiU+79W6spZR\n" +
            "fVqHC+KSbU/taG5ZpPEOurMEf8qUcHOk7QlHHhXCbAkAIeDXYNDmCq7j7gWtbMxc\n" +
            "tWQObKeLSni4dd+b9ZMtyhnR6wKBgQDH3MmNqYakTE+aE1z/6I1hEFAv2TKHnE17\n" +
            "VqXQajTG2iTMjXwB7vPcrhHEZepqHKXUErL1knfoID1RXFTo4KfoUkw+od3tphI+\n" +
            "pbnMO5WtDa6z+0nm8saLEk80Gucltuq6yyPObivRWukThgfPxcnjDibZsqiKqhGj\n" +
            "hIPcXPrxaQKBgGH2s0b1PF3UcLspGT9z1TGEqYM2j4n8OpYqQfpEJaOndY6tw5Dp\n" +
            "P5zUME2HCqU9F+PNbo/5d310eIKfZWq/k6jC0M/1mUib9uwKQNCsom8jXUeTAQlX\n" +
            "7jpBCvBtb1Brl7kokQzhyTnlwS6rXczNsjv/ki8ZeVMKrUH57O/b64GfAoGAH3v0\n" +
            "dFCEWVV8JLVwLRAhVW0QdLkucJZ6zjc0H8TbU4gBzAdtxZAy8kypl9rGNrkUlnfr\n" +
            "oSvVn8eUYUC+T4E0cjCkuikdoNZMgXnx7u5kAcLqBohLeAo+pKHZwEeMqjBKgeqG\n" +
            "VXqdhHlhxiWTDXQG/bG5BQpT6qQn6y5w50HudiECgYAOVqZhAtl9//pfnpRQkV/K\n" +
            "60vpQAapKP67U+468gnDMmtO2sxNx7D0w2XbftXXS/e7mdv34zlnvYSsrivP1HXs\n" +
            "5bqI4Hzu8kXp58FzYsBKDXNOfgjEpztYcv9KBfdvd756U3kdCak7Wc1QcNVVGNoU\n" +
            "vUWo8GnG/QoFG7ai3S7gtg==";
    private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAouR+17r4UglDibQcnU9z\n" +
            "UTzyFeej6EiPfutUDg2UnOQczIxi0Y8eA6Y3fB+kUFD1hGHK33M1MIYYH/wOizRD\n" +
            "cjc3w7oK7NEEylomkwmlt9FQHyzrlq029W8K0N37/OiouKXV6AnDfPqQix1ETlEO\n" +
            "JHKR6zQMd2IThgl4Si2GRHdP1uW480kKaCH4BwETfIzGF7EDMhl0QICdfF34ptEi\n" +
            "Ko1IZPuwyKDoBxaq6LO/e3cnjsJpfK0ADMV4B6CuL1Lbl1XTmvrdQwmYWZJqIUpG\n" +
            "Bh1dUfeGaGGrVJslgj1AkCqR6TI0fgRa2ugHU6rRkzQKnYcmUlF695gZZ8SlprNU\n" +
            "YwIDAQAB";
    
    public static void main(String[] args) throws Exception {
        //head数据封装
        HEADMAP.put("version", "1.0");//协议版本号
        HEADMAP.put("orgId", "MG00");//机构号
        HEADMAP.put("subOrgId", "MG01");//子机构号
        HEADMAP.put("channel", "1");//渠道
        HEADMAP.put("jnlNo", "Y000Y021120140605");//接入机构流水号
        HEADMAP.put("timestamp", System.currentTimeMillis());//时间戳
        HEADMAP.put("certVersion", "1.0");//证书版本号
        HEADMAP.put("funcGroup", "ia00011");
        HEADMAP.put("transId", "GetFinanceUrl");
        
        //body数据封装
        BODYMAP.put("target", "test");
        BODYMAP.put("notifyUrl", "www.test.com");
        
        //组装要发送的数据
        setMap();
        
        //转换成最后的string
        String jsonStr = (String)format(ROOT);
        System.out.println("jsonstr="+jsonStr);
        
        //中间省略了http发送的部分，下面模拟接收到json数据后如何处理及验签
        //String apiUrl = "https://124.74.239.32/ia/gateway.do";
       // String response = HttpsUtil.doPost(apiUrl, jsonStr);
        //对接收到的jsonstr转换成map
        Map jsonMap = (Map)parse(jsonStr);
        
        ObjectMapper objm = new ObjectMapper();
        JsonNode node;
        node = objm.readTree(String.valueOf(jsonStr.getBytes("UTF-8")));
        String plain = node.path("IASPDB").path("PLAIN").toString();
        String sign = node.path("IASPDB").path("SIGNATURE").toString();
        plain = "\"PLAIN\":" + plain;
        
        //验签
        boolean flag = virefy(plain,sign,publicKey);
        System.out.println("验签结果：" + flag);
        
    }
    
    /**************util*******************/
    //封装map数据
    private static void setMap(){
        PLAIN.put("HEAD", HEADMAP);
        PLAIN.put("BODY", BODYMAP);
        
        IASPDB.put("PLAIN", PLAIN);
        sign();
        IASPDB.put("SIGNATURE", SIGNATURE);
        
        ROOT.put("IASPDB", IASPDB);
        System.out.println(ROOT);
    }
    //对plan数据进行签名
    private static void sign(){
        String planStr = getMapFromJson(IASPDB);
        planStr = planStr.substring(1, planStr.length()-1);//去掉前后的“{”，“}”
        System.out.println(planStr);
        try {
            SIGNATURE = SignatureUtil.sign(planStr, priKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean virefy(String plain, String sign, String pubkey) throws Exception{
       return SignatureUtil.virefy(plain, sign, pubkey);
    }
    
    public static Object format(Map map) throws TransformException {
        ObjectMapper mapper = new ObjectMapper();
        Map formatMap=map;
        try {
            return mapper.writeValueAsString(formatMap);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            throw new TransformException(
                    "Json Generation Error");
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new TransformException(
                    "Json Mapping Error");
        } catch (IOException e) {
            e.printStackTrace();
            throw new TransformException(
                    "Json IO Error");
        }
    }

    public static Object parse(Object inputStr) throws TransformException {
        if(!(inputStr instanceof String))
            throw new TransformException("only parse String");
            
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue((String)inputStr, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new TransformException(
                    "Json Parse Error");
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new TransformException(
                    "Json Mapping Error");
        } catch (IOException e) {
            e.printStackTrace();
            throw new TransformException(
                    "Json Io Error");
        }


    }

    public static String getMapFromJson(Map<?,?> map) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(map);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return jsonStr;
    }
}
