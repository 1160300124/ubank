package com.ulaiber.web.conmon;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共常量类
 * @author huangguoqing
 *
 */
public class IConstants
{

    //后台用户SESSION常量名
    public final static String UBANK_BACKEND_USERSESSION = "BACKENDUSER";
    
    //成功
    public final static int QT_CODE_OK = 1000;
    
    //失败
    public final static int QT_CODE_ERROR = 1010;
    
    //密码不一致
    public final static int QT_PWD_NOT_MATCH = 1011;
    
    //验证码错误
    public final static int QT_CAPTCHA_ERROR = 1012;
    
    //验证码发送错误
    public final static int QT_CAPTCHA_SEND_ERROR = 1013;

    //手机号或密码错误
    public final static int QT_NAME_OR_PWD_OEEOR = 2010;
    
    //手机号已存在错误码
    public final static int QT_MOBILE_EXISTS = 2011;
    
    //手机号不存在错误码
    public final static int QT_MOBILE_NOT_EXISTS = 2012;
    
    //注销失败错误码
    public final static int QT_LOGOUT_ERROR = 2013;
    
    //银行开户失败错误码
    public final static int QT_OPEN_ACCOUT_ERROR = 3010; 
    
    //查询余额失败错误码
    public final static int QT_GET_BALANCE_ERROR = 3011; 
    
    //查询账单失败错误码
    public final static int QT_GET_BILL_ERROR = 3012;
    
    //提现失败错误码
    public final static int QT_GET_MONEY_ERROR = 3013; 
    
    //更改绑定银行卡失败错误码
    public final static int QT_BIND_BANKCARD_ERROR = 3014; 
    
    //不能打上班卡
    public final static int QT_CANNOT_CLOCK_ON = 4001;
    
    //不能打下班卡
    public final static int QT_CANNOT_CLOCK_OFF = 4002;
    
    //不要频繁打卡
    public final static int QT_CANNOT_CLOCK_FREQUENTLY = 4003;
    
    //休息日
    public final static int QT_REST_DAY = 4004;
    
    //短信模板
	public final static String SMS_TEMPLATE = "【优发展】您的验证码是#code#。如非本人操作，请忽略本短信";
	
	//银企直联交易状态
	public static Map<String, String> TRANS_STATUS = new HashMap<String, String>();
	static{
		TRANS_STATUS.put("0", "未处理数据文件");
		TRANS_STATUS.put("1", "正在处理数据文件");
		TRANS_STATUS.put("2", "数据文件处理成功");
		TRANS_STATUS.put("3", "数据文件有误，不能进行");
		TRANS_STATUS.put("4", "正在处理业务数据");
		TRANS_STATUS.put("5", "业务数据处理成功");
		TRANS_STATUS.put("6", "业务数据处理失败");
		TRANS_STATUS.put("7", "撤销");
		TRANS_STATUS.put("E", "通讯失败");
		TRANS_STATUS.put("G", "主机拒绝");
		TRANS_STATUS.put("H", "网银拒绝");
		TRANS_STATUS.put("I", "授权拒绝");
		TRANS_STATUS.put("J", "交易录入，待授权");
		TRANS_STATUS.put("K", "待处理");
		TRANS_STATUS.put("Y", "交易提交不成功 ");
		TRANS_STATUS.put("A", "等待进一步授权");
	}
	
	
	//上传目录
	public static String UPLOAD_PATH = "/images";
	
	//临时目录
	public static String TEMP_PATH = "/temp";
	
	//图标路径
	public static String ICON_PATH = "/icon";
	
	//banner路径
	public static String BANNER_PATH = "/banner";
	
	//上传临时目录
	public static String TEMP_UPLOAD_PATH = UPLOAD_PATH + TEMP_PATH;
	
	//图标上传临时目录
	public static String ICON_TEMP_UPLOAD_PATH = TEMP_UPLOAD_PATH + ICON_PATH;
	
	//图标上传目录
	public static String ICON_UPLOAD_PATH = UPLOAD_PATH + ICON_PATH;
	
	//banner上传临时目录
	public static String BANNER_TEMP_UPLOAD_PATH = TEMP_UPLOAD_PATH + BANNER_PATH;
	
	//banner上传目录
	public static String BANNER_UPLOAD_PATH = UPLOAD_PATH + BANNER_PATH;
	
	//文件操作的缓存大小
	public static final int FILE_OPERA_BUFFER = 1024 ;
	
	/**
	 * 工作日 1：周一  2：周二 3：周三  4：周四  5：周五  6：周六  7：周日
	 */
	public static Map<String, String> WORK_DAY = new HashMap<String, String>(); 

	static {
		WORK_DAY.put("1", "周一");
		WORK_DAY.put("2", "周二");
		WORK_DAY.put("3", "周三");
		WORK_DAY.put("4", "周四");
		WORK_DAY.put("5", "周五");
		WORK_DAY.put("6", "周六");
		WORK_DAY.put("7", "周日");
	}
}
