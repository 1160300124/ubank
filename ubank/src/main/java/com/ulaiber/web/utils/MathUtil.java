package com.ulaiber.web.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 一组关于浮点数运算与金额相关的方法集合
 * @author huangguoqing
 *
 */
public class MathUtil {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	//Decimal输出时的默认精度，与数据库对应
	private static final int DEF_DECIMAL_SCALEE = 2;

	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		return v1.add(v2);
	}

	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
		return v1.subtract(v2);
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2  乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return mul(b1, b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		return v1.multiply(v2);
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2  除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * @param v1  被除数
	 * @param v2  除数
	 * @return 两个参数的商
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
		return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2  除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * @param v1  被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
		BigDecimal returnValue = null;
		try {
			returnValue = v1.divide(v2);
		} catch (Exception e) {
			returnValue = v1.divide(v2, 10, BigDecimal.ROUND_HALF_UP);
		}
		return returnValue;
	}

	/**
	 * 精确对比两个数字
	 * @param v1  需要被对比的第一个数
	 * @param v2  需要被对比的第二个数
	 * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
	 */
	public static int compareTo(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.compareTo(b2);
	}

	/**
	 * 精确对比两个数字
	 * @param v1  需要被对比的第一个数
	 * @param v2  需要被对比的第二个数
	 * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
	 */
	public static int compareTo(BigDecimal v1, BigDecimal v2) {
		return v1.compareTo(v2);
	}

	/**
	 * 返回两个数中小的一个值
	 * @param v1  需要被对比的第一个数
	 * @param v2  需要被对比的第二个数
	 * @return 返回两个数中小的一个值
	 */
	public static double returnMin(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.min(b2).doubleValue();
	}

	/**
	 * 返回两个数中小的一个值
	 * @param v1  需要被对比的第一个数
	 * @param v2  需要被对比的第二个数
	 * @return 返回两个数中小的一个值
	 */
	public static BigDecimal returnMin(BigDecimal v1, BigDecimal v2) {
		return v1.min(v2);
	}

	/**
	 * 返回两个数中大的一个值
	 * @param v1  需要被对比的第一个数
	 * @param v2  需要被对比的第二个数
	 * @return 返回两个数中大的一个值
	 */
	public static double returnMax(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.max(b2).doubleValue();
	}

	/**
	 * 返回两个数中大的一个值
	 * @param v1  需要被对比的第一个数
	 * @param v2  需要被对比的第二个数
	 * @return 返回两个数中大的一个值
	 */
	public static BigDecimal returnMax(BigDecimal v1, BigDecimal v2) {
		return v1.max(v2);
	}


	/**
	 * 功能描述：人民币转成大写
	 * @param str  人民币数字字符串
	 * @return String 人民币转换成大写后的字符串
	 */
	public static String hangeToBig(String str) {
		double value;
		try {
			value = Double.parseDouble(str.trim());
			if (value==0){
				return "零圆整";
			}
		} catch (Exception e) {
			return null;
		}
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		long midVal = (long) (value * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串

		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角"
					+ digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		char zero = '0'; // 标志'0'表示出现过0
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				zeroSerNum++; // 连续0次数递增
				if (zero == '0') { // 标志
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // 连续0次数清零
			if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0']; // 转化该数字表示
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
			}
		}

		if (prefix.length() > 0)
			prefix += '圆'; // 如果整数部分存在,则有圆的字样
		return prefix + suffix; // 返回正确表示
	}


	/**
	 * 格式化金钱的显示方式，整数部分每三位加逗号分割,小数部分不足两位补0<br/>
	 * 格式化代码：##,###,###,##0.00<br/>
	 * @param viewValue 需要进行格式化的金钱字符串
	 * @return 已经格式化后的金钱字符串
	 */
	public static String formatMoney(String viewValue) {
		return formatMoney(Double.parseDouble(viewValue));
	}

	/**
	 * 格式化金钱的显示方式，整数部分每三位加逗号分割,小数部分不足两位补0<br/>
	 * 格式化代码：##,###,###,##0.00<br/>
	 * @param viewValue 需要进行格式化的金钱字符串
	 * @return 已经格式化后的金钱字符串
	 */
	public static String formatMoney(Double viewValue) {
		DecimalFormat myformat = new DecimalFormat("##,###,###,##0.00");
		return myformat.format(viewValue);
	}

	/**
	 * 使用系统内置的精度位数-2位来格式化一个BigDecimal数字<br/>
	 * 如：123.456 , 四舍五入为123.46
	 * @param number 原BigDecimal数字
	 * @param flag 是否需要四舍五入，需要1 不需要0
	 * @return
	 */
	public static BigDecimal formatBigDecimal(BigDecimal number, int flag){
		if (flag==1)
			return number.setScale(DEF_DECIMAL_SCALEE, BigDecimal.ROUND_HALF_UP);
		else
			return number.setScale(DEF_DECIMAL_SCALEE, BigDecimal.ROUND_DOWN);
	}
}

