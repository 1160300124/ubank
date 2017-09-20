package com.ulaiber.web.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 将图片上传到七牛云服务器
 * Created by daiqingwen on 2017/9/18.
 */
public class QiNiuUpload {

    public static final Logger logger = Logger.getLogger(QiNiuUpload.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "GsEHlVlmMBEt4Swq_G-A5FttePWwi1lKwodjomoB";
    String SECRET_KEY = "y3aVnN1bDCxjWd7wuFUf-aUQ0ld-8VxjBqrJcoUg";
    //要上传的空间
    String bucketname = "ubank-images1";
    //上传到七牛后保存的文件名
    String key = "";
    //上传文件的路径
    String FilePath = "/Users/emacs/Desktop/test1.jpg";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
    // Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone zone = Zone.autoZone();
    Configuration con = new Configuration(zone);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(con);

    public static void main(String args[]) throws IOException {
        new QiNiuUpload().upload();
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }


    public void upload() throws IOException {
        try {
            //上传的字节数组
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //调用put方法上传
            Response response = uploadManager.put(FilePath, key, getUpToken());
            //Response response =uploadManager.put(uploadBytes, key, getUpToken());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(">>>>>>>>>>>上传图片成功,key=:" +putRet.key);
            System.out.println(">>>>>>>>>>>上传图片成功,hash=:" +putRet.hash);
            //System.out.println(">>>>>>>>>>>上传图片成功:" + res.bodyString());
            logger.info(">>>>>>>>>>>上传图片成功:" + response.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            logger.info(">>>>>>>>>>>>>上传图片失败:" +r.toString() );
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
                logger.info(">>>>>>>>>>>>>>>响应的文本信息:" + r.bodyString());
            } catch (QiniuException e1) {
                logger.info(">>>>>>>>>>>>>QiniuException" + e1);
                e1.printStackTrace();
            }
        }
    }



}
