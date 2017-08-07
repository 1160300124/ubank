package com.ulaiber.web.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.utils.FileUtil;

/** 
 * <一句话概述功能>
 *
 * @author  huangguoqing
 * @date 创建时间：2017年7月31日 下午12:15:58
 * @version 1.0 
 * @since 
 */
public class ClearTempFolderTask extends QuartzJobBean{

	private static Logger logger = Logger.getLogger(ClearTempFolderTask.class);

	@Override
	protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
		logger.info("ClearTempFolderTask start...");
		String path = ClearTempFolderTask.class.getResource("/").getPath();
		String projectPath = path.substring(0, path.indexOf("/WEB-INF/"));
		if (System.getProperty("os.name").contains("Windows")){
			projectPath = projectPath.substring(1);
		}
		FileUtil.deleteAllFilesOfDir(projectPath + IConstants.ICON_TEMP_UPLOAD_PATH);
		FileUtil.deleteAllFilesOfDir(projectPath + IConstants.BANNER_TEMP_UPLOAD_PATH);
		logger.info("ClearTempFolderTask end...");
	}

	@SuppressWarnings("unused")
	private ApplicationContext getApplicationContext(JobExecutionContext jobContext) throws SchedulerException {  
		ApplicationContext appCtx = (ApplicationContext) jobContext.getScheduler().getContext().get("applicationContext");  
		if (appCtx == null) {  
			throw new JobExecutionException("No application context available in scheduler context.");  
		}  
		return appCtx;  
	}  
	
	@SuppressWarnings("unused")
	private ApplicationContext getApplicationContext1(JobExecutionContext jobContext) throws JobExecutionException{
		
		JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
		ApplicationContext ctx = (ApplicationContext)dataMap.get("applicationContext");
		if (ctx == null) {  
			throw new JobExecutionException("No application context available in job detail context.");  
		} 
		return ctx;
	}

}
