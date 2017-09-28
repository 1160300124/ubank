package com.ulaiber.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.ulaiber.web.model.BankUsers;
import org.apache.log4j.Logger;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.User;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.ObjUtil;
import com.ulaiber.web.utils.StringUtil;
import com.ulaiber.web.utils.UUIDGenerator;

/**
 * 基础控制器，所有controller都要继承此类
 * 
 * @author huangguoqing
 *
 */
public class BaseController {
	
	/**
	 * 得到后台当前登陆的账户
	 * 
	 * @param request
	 * @return
	 * @Author 
	 */
	protected User getUserFromSession(HttpServletRequest request){
		User user = null;
		if(ObjUtil.notEmpty(request)){
			user = (User) request.getSession().getAttribute(IConstants.UBANK_BACKEND_USERSESSION);
		}
		return user;
	}

	/**
	 * 是否登录
	 *	@param request
	 *	@return 
	 *  @author 
	 */
	protected boolean isLogin(HttpServletRequest request){
		return null != getUserFromSession(request);
	}

	/**
	 * 控制器级别的公共日志输出
	 * @param log 日志器
	 * @param msg 消息
	 * @author
	 */
	protected void writeLog(Logger log,String msg){
		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_FULLTIME);
		log.info("@@@@@@@@@@ ".concat(datetime).concat(",").concat(msg).concat(" @@@@@@@@@@"));
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
	
	/**
	 * Spring上传控制器的公共方法，用于将某个图片在重命名后存储于指定的目录下
	 * @param request
	 * @param basePath 要上传文件存储的基地址，参数IConstants.SYS_DIRECTORS
	 * @param bytes 文件字节，由具体控制器传入
	 * @param filename 自定义文件名
	 * @return 重命名后的文件，路径是相对地址+重命名的文件名
	 * @throws Exception
	 */
	protected String upload(HttpServletRequest request, String basePath, byte[] bytes, String filename){
		//取得项目根地址
		String realpath = getProjectAbsoluteURL(request);
        String filesavepaths = null;
        
        if (realpath.endsWith(File.separator)){
        	filesavepaths = realpath.concat(basePath);
        } else {
        	filesavepaths = realpath.concat("/").concat(basePath);
        }
        String uuid = UUIDGenerator.getUUID();
        String newFilename = uuid + filename.substring(filename.lastIndexOf("."), filename.length());
        FileOutputStream fos = null;
        try {
			
        	//目录是否存在，否则创建文档夹
        	File file = new File(filesavepaths); 
        	if (!file.exists())
        		file.mkdirs();
        	
        	fos = new FileOutputStream(new File( filesavepaths.concat(File.separator).concat(newFilename)));
        	fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally {
			try {
				if (null != fos){
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        //返回文件的相对地址
        return basePath.concat("/").concat(newFilename);
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
	 * 前台输入框（或值）防止SQL注入校验
	 * 任何用户输入的值均要进行防SQL注入漏洞校验
	 * @param obj
	 * @return true有类SQL注入信息 false无SQL注入信息，可正常使用
	 */
	public boolean sqlInjectCheck(String obj){
		return StringUtil.isSqlinject(obj);
	}
}
