/**
 * 
 */
package com.ulaiber.web.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.ulaiber.web.model.MSGContent;
import com.ulaiber.web.model.Message;

import sun.misc.BASE64Encoder;


/**
 * 一组字符类操作实用工具集合
 * @author huangguoqing
 *
 */
public class StringUtil {
	
	// 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
    private static int BEGIN = 45217;
    private static int END = 63486;

    // 按照声母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
    // i, u, v都不做声母, 自定规则跟随前面的字母
    private static char[] chartable = { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
            '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌',
            '塌', '挖', '昔', '压', '匝'};

    // 二十六个字母区间对应二十七个端点
    // GB2312码汉字区间十进制表示
    private static int[] table = new int[27];

    // 对应首字母区间表
    private static char[] initialtable = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            't', 't', 'w', 'x', 'y', 'z', };

    //入参防SQL注入关键字判断
    private static String[] inj_char = {"'","and","exec","insert","select","delete","update",
    	"count","*","chr","mid","master","truncate","char","declare",";","or","-","+",",",".."};

    /**
     * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法，思路如下：一个个字符读入、判断、输出
     * @param SourceStr 字符串 
     * @return 返回值，如输入“技术实现”，返回“jssx”
     */
    public static String cn2py(String SourceStr) {
    	for (int i = 0; i < 26; i++) {
            table[i] = gbValue(chartable[i]);// 得到GB2312码的首字母区间端点表，十进制。
        }
        table[26] = END;// 区间表结尾
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Initial(SourceStr.charAt(i));
            }
        } catch (Exception e) {
            Result = "";
        }
        return Result;
    }
    
    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0'
     *
     */
    private static char Char2Initial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z')
            return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z')
            return ch;

        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = gbValue(ch);// 汉字转换首字母
        if ((gb < BEGIN) || (gb > END))// 在码表区间之前，直接返回
            return ch;
        int i;
        for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
                if ((gb >= table[i]) && (gb < table[i+1]))
                    break;
        }
        if (gb==END) {//补上GB2312区间最右端
            i=25;
        }
        return initialtable[i]; // 在码表区间中，返回首字母
    }

    /**
     * 取出汉字的编码 cn 汉字
     */
    private static int gbValue(char ch) {// 将一个汉字（GB2312）转换为十进制表示。
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }
    

	/**
	 * 前台会员密码加密，单向方法不可逆，超长宽度，不支持彩虹表扫描，支持加盐，暂时不支持一人一密
	 * @param password
	 * @return 新密码或者为null
	 */
	public static String passwordEncode(String password){
		//私钥及临时变量定义
		String salt = "0x5f3759df",resultPassword = null,tempStr = "";
		if (ObjUtil.notEmpty(password)){
			try{
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				BASE64Encoder base64 = new BASE64Encoder();
				tempStr = StringUtil.forEach(password, 0x55).concat(salt).concat(StringUtil.forEach(password, 0x55));
				resultPassword = base64.encode(md5.digest(tempStr.getBytes("utf-8")));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resultPassword;
	}

	/**
	 * 将一个字符串循环N次
	 * @param nativeStr 字符串 
	 * @param count 循环次数
	 * @return 返回新串
	 */
	public static String forEach(String nativeStr,int count){
		StringBuilder sb = new StringBuilder();
			for(int i=0;i<count;i++)
				sb.append(nativeStr);
		return sb.toString();
	}
	
	
	/**
	 * 将HTML代码格式化<br/>
	 * 例：<html><head>hello</head></html> 转换为 &lt;html&gt;&lt;head&gt;hello&lt;/head&gt;
	 * @param html 原始HTML代码
	 * @param length 截取长度，如果为0则取所有长度
	 * @return
	 */
	public static String formatHTML(String html,int length){
		if (ObjUtil.notEmpty(html)){
			html = html.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("''", "&#039;")
				.replace(" ", "&nbsp;")
				.replace("\r\n", "<br>")
				.replace("\r", "<br>")
				.replace("\n", "<br>");
			if (length==0)
				return html;
			else
				return html.substring(0, html.length()>length?length:html.length());
		} else 
			return null;
	}


	/**
	 * 将html代码格式化为纯文本<br/>
	 * 例如：<html>hello</html> 将变为hello
	 * @param html html代码
	 * @param length 截图长度，0为全部
	 * @return
	 */
	public static String formatHTML2plaintext(String html,int length){
		if (ObjUtil.notEmpty(html)){
			String result = html.replaceAll("<[^>]*>","");
			if (length==0)
				return result;
			else 
				return result.substring(0, result.length()>length?length:result.length());
		} else 
			return null;
	}
	
	
	/**
	 * 数字采用前端补0的方式转换为固定长度的字符串<br/>
	 * 该方法不支持超过10位长度的数字转换<br/>
	 * 如果数字长度大于等于字符串长度，返回原数字对应的字符串<br/>
	 * @param num 需要转换的数字
	 * @param length 需要的字符串长度
	 * @return 定长的数字字符串
	 */
	public static String int2Str(Integer num,Integer length){
		if (length>0 && length<=10){
			String fmt = "%0"+ length + "d";
			return String.format(fmt, num);
		}
		return null;
	}
	
	/**
	 * 功能描述：判断字符串是否为整数
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 判断是否为浮点数，包括double和float
	 * @param str 传入的字符串
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 判断输入的字符串是否符合Email样式.
	 * @param email 传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.length() < 1 || email.length() > 256) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(email).matches();
	}
	
	/**
	 * 判断输入的字符串是否为纯汉字
	 * @param str 传入的字符窜
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}
	
	
	/**
	 * 防SQL注入判断，对于用户的输入参数，需要使用该方法进行校验
	 * @param str 入参
	 * @return 参数中SQL注入，返回true，反之返回false
	 */
	public static boolean isSqlinject(String str){
		for (int i=0 ; i < inj_char.length ; i++ ){
			if (str.indexOf(inj_char[i])>=0)
				return true;
		}
		return false;
	}
	
	
	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 * @author 
	 */
	public static String ToSBC(String input) { // 半角转全角：
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}
	
	/**
	 * 符号转换（半角转全角）
	 * 
	 * @param s
	 * @return
	 * @author 
	 */
	public static String validChar(String s){
		String str = "!@#$%^&*()+:\"|;'\\,/<>?";
		String t = "";
		char [] cs = new char [s.length()];
		for (int i = 0; i < s.length(); i++) {
			t= s.substring(i, i+1);
			if(str.indexOf(t)>0){
				t=ToSBC(t);
			}
			cs[i]=t.toCharArray()[0];
		}
		return new String(cs);
	}

	//解密json字符串，并解析json数据
	public static Message parserJson(String json){
		//解析响应的json数据
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(json);
		List<List<Map<String, Object>>> plain_list = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			net.sf.json.JSONObject obj = jsonArray.getJSONObject(i);
			List<List<Map<String, Object>>> plain =  (List<List<Map<String, Object>>>) obj.get("PLAIN");
			//需要解密签名
			//String signature = (String) obj.get("SIGNATURE");
			for (int j = 0; j < plain.size(); j++) {
				plain_list = (List<List<Map<String, Object>>>) plain.get(j).get(j);
			}
		}
		List<Map<String, Object>> head = (List<Map<String, Object>>) plain_list.get(0).get(0).get("HEAD");
		List<Map<String, Object>> body = (List<Map<String, Object>>) plain_list.get(0).get(1).get("BODY");
		Message msg = new Message();
		//获取head数据
		for (Map<String, Object> map : head) {
			msg.setTransId(map.get("transId").toString());
			msg.setReturnCode(map.get("returnCode").toString());
			msg.setReturnMsg(map.get("returnMsg").toString());
			msg.setTimeStamp(map.get("timeStamp").toString());
		}
		//获取body数据
		List<MSGContent> body_list = new ArrayList<>();
		MSGContent info = new MSGContent();
		for (Map<String, Object> map : body) {
			info.setUserId(map.get("userId").toString());
			info.setIdType(map.get("idType").toString());
			info.setStatus(map.get("status").toString());
			info.setOpenId(map.get("openId").toString());
			info.setReturnCode(map.get("returnCode").toString());
			info.setReturnMsg(map.get("returnMsg").toString());
		}
		body_list.add(info);
		msg.setMsgContent(body_list);
		return msg;
	}
}
