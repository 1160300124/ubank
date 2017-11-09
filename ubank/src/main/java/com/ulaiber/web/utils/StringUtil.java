/**
 * 
 */
package com.ulaiber.web.utils;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

import com.ulaiber.web.SHSecondAccount.ShangHaiAccount;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.model.MSGContent;
import com.ulaiber.web.model.Message;

import com.ulaiber.web.service.LeaveService;
import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import sun.misc.BASE64Encoder;


/**
 * 一组字符类操作实用工具集合
 * @author huangguoqing
 *
 */
public class StringUtil {

	private static final Logger logger = Logger.getLogger(StringUtil.class);
	
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

	//判断对象是否为空
	public static boolean isEmpty(Object obj)
	{
		if (obj == null)
		{
			return true;
		}
		if ((obj instanceof List))
		{
			return ((List) obj).size() == 0;
		}
		if ((obj instanceof String))
		{
			return ((String) obj).trim().equals("");
		}
		return false;
	}

	/**
	 * 推送信息
	 * @param map 用户信息
	 * @param reason 备注
	 * @param mark 记录类型: 0 请假记录， 1 加班记录 , 2 报销记录,3 工资发放记录,4 补卡记录
	 */
	public static void sendMessage(Map<String,Object> map ,String reason,String mark){
		String cid  = "";
		if(!StringUtil.isEmpty(map.get("CID"))){
			cid = (String) map.get("CID");
			if(!StringUtil.isEmpty(cid)){
				String name = (String) map.get("user_name");
				int type = IConstants.PENGDING;
				String types = "";
				switch (mark){
					case "0":
						types = "请假";
						break;
					case "1":
						types = "加班";
						break;
					case "2":
						types = "报销";
						break;
					case "3":
						types = "工资发放";
						break;
					case "4":
						types = "补卡";
						break;
				}
				//消息内容
				String title = "您有个"+types+"申请待审批";
				String str = "";
				if(!StringUtil.isEmpty(reason)){
					str = reason;
				}
				String content = name + "你好，有一条请假申请需要您审批，原因是:"+ str;
				try {
					//推送审批信息致第一个审批人
					PushtoSingle.singlePush(cid,type,content,title);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(">>>>>>>>>>向"+name+"推送消息异常",e);
				}
			}

		}
	}

	/**
	 * 生成随机数字和字母
	 * @param length 随机数长度
	 * @return String
	 */
	public static String getStringRandom(int length) {
		String val = "";
		Random random = new Random();
		//参数length，表示生成几位随机数
		for(int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			//输出字母还是数字
			if( "char".equalsIgnoreCase(charOrNum) ) {
				//输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char)(random.nextInt(26) + temp);
			} else if( "num".equalsIgnoreCase(charOrNum) ) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 生成随机数字
	 * @param strLength
	 * @return
	 */
	public static String getFixLenthString(int strLength) {

		Random rm = new Random();

		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);

		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLength + 1);
	}

	/**
	 * 读取配置文件信息
	 * @return map
	 */
	public static Map<String,Object> loadConfig(){
		Map<String,Object> map = new HashMap<>();
		Properties prop = new Properties();
		InputStream in = SysConf.class.getClassLoader().getResourceAsStream("config/config.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			logger.error(">>>>>>>>加载config配置文件异常",e);
		}
		String privateKey = prop.getProperty("SH_privateKey");
		String publicKey = prop.getProperty("SH_publicKey");
		String postUrl = prop.getProperty("SH_postUrl");
		String pwd = prop.getProperty("SH_pwd");
		String username = prop.getProperty("SH_username");
		String password = prop.getProperty("SH_password");
		String host = prop.getProperty("SH_host");
		int port = Integer.parseInt(prop.getProperty("SH_port"));
		String directory = prop.getProperty("SH_directory");
		map.put("privateKey",privateKey);
		map.put("publicKey",publicKey);
		map.put("postUrl",postUrl);
		map.put("pwd",pwd);
		map.put("username",username);
		map.put("password",password);
		map.put("host",host);
		map.put("port",port);
		map.put("directory",directory);
		return map;

	}


	/**
	 * 解析xml
	 * @param xml
	 * @return map
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static Map<String, String> xmlParse(String xml) throws XmlPullParserException, IOException {
		Map<String, String> map = null;
		if (!StringUtil.isEmpty(xml)) {
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
			XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
			pullParser.setInput(inputStream, "UTF-8"); // 为xml设置要解析的xml数据
			int eventType = pullParser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						map = new HashMap<String, String>();
						break;
					case XmlPullParser.START_TAG:
						String key = pullParser.getName();
						if (key.equals("xml"))
							break;
						String value = pullParser.nextText().trim();
						map.put(key, value);
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = pullParser.next();
			}
		}
		return map;
	}

	/**
	 * 四舍五入
	 * @param val
	 * @return
	 */
	public static double round(String val) {
		BigDecimal bg = new BigDecimal(val);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 *  拼接签名数据
	 * @param map
	 * @return
	 */
	public static String jointSignature(Map<String,Object> map){
		List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
		//排序方法
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				//return (o2.getValue() - o1.getValue());
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		//排序后
//		for (int i = 0; i < infoIds.size(); i++) {
//			String id = infoIds.get(i).toString();
//			System.out.println("排序前"+id);
//		}
		StringBuffer sb = new StringBuffer();
		//排序后
		for(Map.Entry<String, Object> m : infoIds){
			//System.out.println("排序后"+m.getKey()+":"+ m.getValue());
			if(!StringUtil.isEmpty(m.getValue())){
				sb.append(m.getKey()+ "=" + m.getValue() + "&");
			}
		}
		String str = "";
		str = sb.substring(0,sb.length()-1);
		return str;
	}

	/**
	 *  拼接XML
	 * @param map
	 * @return
	 */
	public static String jointXML(Map<String,Object> map){
		Map<String,String> ma = new HashMap<>();
		ma.put("SPName","SPName");
		ma.put("RqUID","RqUID");
		ma.put("ClearDate","ClearDate");
		ma.put("TranDate","TranDate");
		ma.put("TranTime","TranTime");
		ma.put("ChannelId","ChannelId");
		List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
		//排序方法
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				//return (o2.getValue() - o1.getValue());
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		//排序后
//		for (int i = 0; i < infoIds.size(); i++) {
//			String id = infoIds.get(i).toString();
//			System.out.println("排序前"+id);
//		}
		StringBuffer sb = new StringBuffer();
		//排序后
		for(Map.Entry<String, Object> m : infoIds){
			boolean flag = true;
			//System.out.println("排序后"+m.getKey()+":"+ m.getValue());
			if(!StringUtil.isEmpty(m.getValue())){
				for(String key : ma.keySet()){
					if(m.getKey().equals(key)){
						flag = false;
						break;
					}
				}
				if(flag){
					sb.append("<"+m.getKey()+">"+m.getValue()+"</"+m.getKey()+">");
				}
			}
		}
		return sb.toString();
	}


	/**
	 * 递归删除目录中的文件
	 * @param file
	 * @return
	 */
	public static int deleteFile(File file){
		if(file.exists()){
			//判断是否文件
			if(file.isFile()){
				file.delete();
			}else if(file.isDirectory()){
				//声明目录下所有的文件 files[];
				File[] files = file.listFiles();
				//遍历目录下所有文件，用递归删除所有目录下的文件
				for (int i = 0; i < files.length; i++) {
					StringUtil.deleteFile(files[i]);
				}
				//删除所有文件后删除文件夹
				file.delete();
			}
			return IConstants.QT_CODE_OK;
		}else{
			System.out.println("文件夹不存在！");
			logger.error(">>>>>>>>>>文件夹不存在！");
			return IConstants.QT_CODE_EMPTY;
		}
	}

	/**
	 * 写入内容至txt文件
	 * @param filePath 文件路径
	 * @param content 输入的内容
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean writeToTxt( String filePath, String content) throws IOException {
		boolean flag = false;
		String temp = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			File file = new File(filePath+".txt");
			if(!file.exists()){
				file.createNewFile();
			}
			File path = new File(filePath+".txt");//文件路径(包括文件名称)
			//将文件读入输入流
			fis = new FileInputStream(path);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			//文件原有内容
			for(int i=0;(temp =br.readLine())!=null;i++){
				buffer.append(temp);
				// 行与行之间的分隔符 相当于“\n”
				buffer = buffer.append(System.getProperty("line.separator"));
			}
			buffer.append(content);
			fos = new FileOutputStream(path);
			pw = new PrintWriter(fos);
			pw.write(buffer.toString().toCharArray());
			pw.flush();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return flag;
	}

}
