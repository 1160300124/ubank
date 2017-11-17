package com.ulaiber.web.utils;

import com.jcraft.jsch.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 上传图片到SFTP服务器
 * Created by daiqingwen on 2017/10/19.
 */
public class SFTPUtil {

    private static Logger log = Logger.getLogger(SFTPUtil.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

    private ChannelSftp sftp;

    private Session session;

    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public boolean login(String path){
        boolean flag = false;
        try {
            //加载配置文件
            Map<String,Object> configMap = StringUtil.loadConfig();
            String username = (String) configMap.get("username");
            String password = (String) configMap.get("password");
            int port = (int) configMap.get("port");
            String host = (String) configMap.get("host");
            String directory = (String) configMap.get("directory");
            JSch jsch = new JSch();
            log.info("sftp connect by host:{} username:{}");

            session = jsch.getSession(username, host, port);
            log.info("Session is build");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");

            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
            //开始上传图片
            File file = new File(path);
            File[] files =  file.listFiles();
            for (int i = 0; i < files.length; i++) {
                log.info(">>>>>>>>>>上传文件第"+i+"个文件:" + files[i]);
                //System.out.println(">>>>>>>>>>上传文件第"+i+"个文件:" + files[i]);
                File mul = files[i];
                String filename =  mul.getName(); // 获取上传文件的原名
                InputStream ins = new FileInputStream(mul);
                upload(directory,filename,ins);
            }
            flag = true;
            logout();
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server , Exception message is: ", e);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            log.error(">>>>>>>>>>file not found :",e);
        } catch (SftpException e) {
            //e.printStackTrace();
            log.error(">>>>>>>>>>SftpException : " ,e);
        }
        return flag;
    }

    /**
     * 连接sftp
     * @throws JSchException
     */
    public Map<String,Object> connect() throws JSchException {
        Map<String,Object> resultMap = new HashMap<>();
        //加载配置文件
        Map<String,Object> configMap = StringUtil.loadConfig();
        String username = (String) configMap.get("username");
        String password = (String) configMap.get("password");
        int port = (int) configMap.get("port");
        String host = (String) configMap.get("host");
        String directory = (String) configMap.get("directory");
        String resDir = (String) configMap.get("resDir");
        String downloadDir = (String) configMap.get("downloadDir");
        String backup = (String) configMap.get("backup");
        resultMap.put("resDir",resDir);
        resultMap.put("downloadDir",downloadDir);
        resultMap.put("backupDir",backup);
        JSch jsch = new JSch();
        log.info("sftp connect by host:{} username:{}");

        session = jsch.getSession(username, host, port);
        log.info("Session is build");
        if (password != null) {
            session.setPassword(password);
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config);
        session.connect();
        log.info("Session is connected");

        Channel channel = session.openChannel("sftp");
        channel.connect();
        log.info("channel is connected");

        sftp = (ChannelSftp) channel;
        log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        log.info(">>>>>>>>>>开始下载文件到本地临时目录");
        download(resDir,downloadDir);
        return resultMap;
    }

    /**
     * 关闭连接 server
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     * @param directory 上传到该目录
     * @param sftpFileName  sftp端文件名
     * @param input 输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful" + sftpFileName);

    }

    /**
     * 下载文件
     * @param directory 下载目录
     * @param savepath 保存
     */
    public void download(String directory,String savepath) {
        try {
            sftp.cd(directory);
            Vector v = sftp.ls("*.txt");
            //String savepath = "/Users/emacs/Desktop/test1";
            if(v.size() > 0){
                for(int i=0;i<v.size();i++){
                    //截取需要下载的文件名
                    String str  = String.valueOf(v.get(i));
                    int index = str.indexOf("RES");
                    str = str.substring(index,str.length());
                    File file = new File(savepath + "/"+ str);
                    sftp.get(str, new FileOutputStream(file));
                }
                log.info(">>>>>>>>>>下载成功");
            }else{
                log.info(">>>>>>>>>>暂无回盘文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(">>>>>>>>>下载回盘文件失败 :",e);
        }
    }



    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     */
    public void delete(String directory) {
        try {
            sftp.cd(directory);
            Vector v = sftp.ls("*.txt");
            if(v.size() > 0){
                for(int i=0;i<v.size();i++){
                    //截取需要删除的文件名
                    String deleteFile  = String.valueOf(v.get(i));
                    int index = deleteFile.indexOf("RES");
                    deleteFile = deleteFile.substring(index,deleteFile.length());
                    sftp.rm(deleteFile);
                }
                log.info(">>>>>>>>>>删除成功");
            }else{
                log.info(">>>>>>>>>>暂无回盘文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(">>>>>>>>>删除回盘文件失败 :",e);
        }
    }

    public static void main(String[] args){
        try {
            String directory = "/var/ftp/testmulu/ftpuser";
            //String directory = prop.getProperty("directory");
            String sftpFileName = "tests.jpg";
            FileInputStream in = new FileInputStream(new File("/Users/emacs/Desktop/tests.jpg"));
            SFTPUtil sftpUtil = new SFTPUtil();
          //  sftpUtil.login();
//            sftpUtil.upload(directory,sftpFileName,in);
//            sftpUtil.logout();
        } catch (FileNotFoundException e) {
            System.out.println(">>>>>>>>>>上传文件异常：" + e );
        }
    }

}
