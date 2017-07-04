package com.ulaiber.service;

import java.io.File;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.model.UserOperaDetail;
import com.ulaiber.utils.DateTimeUtil;
import com.ulaiber.utils.ObjUtil;

/**
 * 基础服务类，所有service都要继承此类
 * 
 * @author huangguoqing
 *
 */
public class BaseService {
	
	/**
	 * 后台系统输出日志的标准方法
	 * (格式：时间+Message)
	 * @param log
	 * @param msg
	 */
	public void writeSLFLog(Logger log,String msg){
		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_FULLTIME);
		log.info("@@@@@@@@@@ ".concat(datetime).concat(",").concat(msg).concat(" @@@@@@@@@@"));
	}
	
	/**
	 * 格式化输出SQL，方便展示，减少拼接，特别适合于大的语句
	 * @param template 模板语句，其中%s代表字符串 %d代表数字 %f代表浮点型,特别注意，对百分号的转换需要两个百分号，即%%
	 * @param objs 对象集合，以逗号分割
	 * @return
	 */
	public String fmtStr(String template,Object... objs){
		return String.format(template, objs);
	}
	
	
	/**
	 * 设置JVM时区为ETC/GMT-8
	 * 2012-10-19 上午1:27:11
	 * BaseService.java
	 * cuiyonghao
	 */
	public void setTimeZone(){
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
	}
	
	/**
	 * 记录会员操作流水
	 * 
	 * @param request
	 * @param mod_type 流水类型
	 * @param mod_typevalue 备注
	 * @param mber_id 会员ID
	 * @return
	 * @throws Exception
	 * @author 
	 */
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public int saveMemberOperaDetail(HttpServletRequest request,int mod_type,String mod_typevalue,long user_id) throws Exception{
		int res=0;
		if(ObjUtil.notEmpty(request)){
			String ip = request.getRemoteAddr();
			UserOperaDetail opera = new UserOperaDetail();
			opera.setUser_id(user_id);
			opera.setOpera_date(DateTimeUtil.date2Str(new Date()));
			opera.setOpera_ip(ip);
			opera.setOpera_type(mod_type);
			opera.setOpera_typevalue(mod_typevalue);
		}
		return res;
	}
	
	
	/**
	 * 神器，得到TOMCAT中项目的绝对地址<br/>
	 * 注意，这个地址的最后是含"/"的，比如/a/b/c/d/，而不是/a/b/c/d
	 * @param request
	 * @return
	 */
	protected String getProjectAbsoluteURL(HttpServletRequest request){
		if (ObjUtil.notEmpty(request)){
			return request.getSession().getServletContext().getRealPath("");
		}
		return null;
	}
	
	/**
	 * 得到项目部署的路径
	 *
	 * @param request
	 * @return a/b
	 * @author 
	 */
	protected String getWarAbsoluteURL(HttpServletRequest request){
		if (ObjUtil.notEmpty(request)){
			String pro = getProjectAbsoluteURL(request);
			pro = pro.substring(0, pro.lastIndexOf(File.separator));
			return pro;
		}
		return null;
	}

}
