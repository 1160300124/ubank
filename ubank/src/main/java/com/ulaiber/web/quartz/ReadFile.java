package com.ulaiber.web.quartz;

import com.ulaiber.web.controller.api.BankController;
import com.ulaiber.web.service.BankService;
import com.ulaiber.web.utils.SFTPUtil;
import com.ulaiber.web.utils.StringUtil;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取上海银行回盘文件
 * Created by daiqingwen on 2017/11/15.
 */
public class ReadFile extends QuartzJobBean {
    private static Logger logger = Logger.getLogger(BankController.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public  boolean readFile(JobExecutionContext jobContext){
        boolean flag = false;
        logger.info(">>>>>>>>>>"+sdf.format(new Date())+"开始读取回盘文件");
        /**
         * 读取文件并解析内容的步骤：
         *  1.连接sftp
         *  2.将回盘目录中所有文件下载到本地临时目录
         *  3.遍历临时目录中的文件，解析目录中的文件内容
         *  4.如果内容返回错误状态码，则推送消息给用户
         *  5.将下载的目录文件备份
         *  6.删除临时目录中的文件
         *  7.删除sftp服务器上回盘目录中的文件
         *  8.关闭sftp
         *
         */
        SFTPUtil sftpUtil = new SFTPUtil();
        try {
            //1.连接SFTP，将回盘目录中所有文件下载到本地临时目录
            Map<String,Object> map = sftpUtil.connect();
            String resDir = (String) map.get("resDir");
            String downloadDir = (String) map.get("downloadDir");
            String backupDir = (String) map.get("backupDir");
            logger.info(">>>>>>>>>>开始下载文件到本地临时目录");

            //3.遍历临时目录中的文件，解析目录中的文件内容
            logger.info(">>>>>>>>>>开始解析文件内容");
            File tempFile = new File(downloadDir);
            File[] temporary = tempFile.listFiles();
            if(temporary.length <= 0){
                return flag;
            }
            for (int j = 0 ; j < temporary.length ; j++){
                String content = StringUtil.txt2String(temporary[j]);
                String[] arr = content.split("\n");
                String SubAcctNo = "";
                boolean result = true;
                for (int i = 2 ; i < arr.length; i++){
                    if(!StringUtil.isEmpty(arr[i])){
                        //获取第一条记录的状态：S 表示处理成功  F 表示处理部分失败 E 文件或压缩包校验失败
                        String[] strs = arr[i].split("\\|");
                        SubAcctNo = strs[3]; //二类户账号
                        String status = strs[6];  //校验状态
                        String descriptions = strs[7]; //处理后的描述
                        //4.如果校验失败，则推送消息给用户
                        if(!"S".equals(status)){
                           result = false;
                        }
                    }
                }
                ApplicationContext  app = getApplicationContext(jobContext);
                BankService bankService = app.getBean(BankService.class);
                //根据二类户账号获取CID
                Map<String,Object> paraMap = bankService.queryCIdbySub(SubAcctNo);
                StringUtil.sendPictureMsg(paraMap,result);
                //更新二类户冻结状态
                int res = bankService.updateAccFreeze(SubAcctNo);
                if(res <= 0){
                    logger.error(">>>>>>>>>>定时任务-"+sdf.format(new Date())+"更新二类户冻结状态失败，二类户为：" + SubAcctNo);
                }
            }

            //5.将下载的目录文件备份
            logger.info(">>>>>>开始备份文件");
            File downloadFile = new File(downloadDir);
            File[] dFile = downloadFile.listFiles();
            for (int i = 0 ; i < dFile.length ; i++){
                logger.info(">>>>>>>>>>备份第"+(i+1)+"个文件");
                String oldFile = dFile[i].getPath();
                StringUtil.copyFile(oldFile,backupDir+"/"+ dFile[i].getName());
            }
            //6.删除临时目录中的文件
            logger.info(">>>>>>>>>>开始删除临时文件夹中的文件");
            StringUtil.delAllFile(downloadDir);
            //7.删除sftp服务器上回盘目录中的文件
            logger.info(">>>>>>>>>>开始删除sftp服务器上回盘文件");
            sftpUtil.delete(resDir);
            flag = true;
            logger.info(">>>>>>>>>>解析文件完成，关闭sftp连接");
            //关闭sftp
            sftpUtil.logout();
        }catch(Exception e){
            //关闭sftp
            sftpUtil.logout();
            logger.error(">>>>>>>>>>"+sdf.format(new Date())+"读取回盘文件失败，原因是：",e);
        }
        return flag;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        readFile(jobExecutionContext);
    }

    @SuppressWarnings("unused")
    private ApplicationContext getApplicationContext(JobExecutionContext jobContext) throws SchedulerException {
        ApplicationContext appCtx = (ApplicationContext) jobContext.getScheduler().getContext().get("applicationContext");
        if (appCtx == null) {
            throw new JobExecutionException("No application context available in scheduler context.");
        }
        return appCtx;
    }
}
