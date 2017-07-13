package com.ulaiber.conmon;

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
    
    //短信模板
	public final static String SMS_TEMPLATE = "【U钱包】您的验证码是#code#";
	
}
