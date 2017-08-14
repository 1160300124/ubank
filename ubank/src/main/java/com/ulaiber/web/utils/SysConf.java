package com.ulaiber.web.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/** 
 * 系统的一些配置信息
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月7日 上午10:38:44
 * @version 1.0 
 * @since 
 */
public class SysConf {
	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(SysConf.class);
	
	private static Properties props = null; 
	
	private synchronized static void loadProps(){
		props = new Properties();
		InputStream is = null;
		try {
			is = SysConf.class.getClassLoader().getResourceAsStream("config/sysConf.properties");
			props.load(is);
		} catch (FileNotFoundException e) {
			logger.error("cannot find sysConf.properties", e);
		} catch (IOException e) {
			logger.error("read sysConf.properties error.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	public static String getProperty(String key) {
		if(null == props) {
			loadProps();
		}
		return props.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		if(null == props) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}
	
}
