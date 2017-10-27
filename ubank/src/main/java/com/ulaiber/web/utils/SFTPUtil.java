package com.ulaiber.web.utils;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 上传图片到SFTP服务器
 * Created by daiqingwen on 2017/10/19.
 */
public class SFTPUtil {

    private static Logger log = Logger.getLogger(SFTPUtil.class);

    private ChannelSftp sftp;

    private Session session;
    /** FTP 登录用户名*/
//    private String username = "ftpuser";
//    /** FTP 登录密码*/
//    private String password = "ftpuser";
//    /** FTP 服务器地址IP地址*/
//    private String host = "119.23.247.226";
//    /** FTP 端口*/
//    private int port = 22;

    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void login(MultipartFile file, String fileName){
        try {
            //加载配置文件
            Map<String,Object> configMap = StringUtil.loadConfig();
            String username = (String) configMap.get("SH_username");
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

            //FileInputStream ins = new FileInputStream(new File("/Users/emacs/Desktop/tests.jpg"));
            InputStream ins = file.getInputStream();
            //FileInputStream ins = new FileInputStream();
            String sftpFileName = fileName;

            upload(directory,sftpFileName,ins);
            logout();
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server , Exception message is: ", e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
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
