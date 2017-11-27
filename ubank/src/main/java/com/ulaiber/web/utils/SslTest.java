package com.ulaiber.web.utils;

import org.apache.commons.io.IOUtils;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * 用户处理二类户发送请求是过滤掉证书
 * Created by daiqingwen on 2017/10/27.
 */
public class SslTest {

    public String getRequest(String url,int timeOut) throws Exception{
        URL u = new URL(url);
        if("https".equalsIgnoreCase(u.getProtocol())){
            Verifier.ignoreSsl();
        }
        URLConnection conn = u.openConnection();
        conn.setConnectTimeout(timeOut);
        conn.setReadTimeout(timeOut);
        return IOUtils.toString(conn.getInputStream());
    }
    public String postRequest(String urlAddress,String args,int timeOut) throws Exception{
        URL url = new URL(urlAddress);
        if("https".equalsIgnoreCase(url.getProtocol())){
            Verifier.ignoreSsl();
        }
        URLConnection u = url.openConnection();
        u.setDoInput(true);
        u.setDoOutput(true);
        u.setConnectTimeout(timeOut);
        u.setReadTimeout(timeOut);
        u.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
        OutputStreamWriter osw = new OutputStreamWriter(u.getOutputStream(), "UTF-8");
        osw.write(args);
        osw.flush();
        osw.close();
        u.getOutputStream();
        return IOUtils.toString(u.getInputStream());
    }

    /**
     * 不过滤证书
     * @param urlAddress 请求地址
     * @param args 参数
     * @param timeOut 请求时间
     * @return String
     * @throws Exception
     */
    public String doPostSSL(String urlAddress,String args,int timeOut)throws Exception{
        URL url = new URL(urlAddress);
        URLConnection u = url.openConnection();
        u.setDoInput(true);
        u.setDoOutput(true);
        u.setConnectTimeout(timeOut);
        u.setReadTimeout(timeOut);
        u.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
        OutputStreamWriter osw = new OutputStreamWriter(u.getOutputStream(), "UTF-8");
        osw.write(args);
        osw.flush();
        osw.close();
        u.getOutputStream();
        return IOUtils.toString(u.getInputStream());
    }
    public static void main(String[] args) {
        try {
            SslTest st = new SslTest();
            //String a = st.getRequest("https://xxx.com/login.action", 3000);
            String a = st.postRequest("https://xxx.com/login.action","", 3000);
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
