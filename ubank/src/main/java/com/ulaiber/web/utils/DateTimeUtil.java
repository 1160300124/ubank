package com.ulaiber.web.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 一组关于时间工具方法集合
 * @author huangguoqing
 *
 */
public class DateTimeUtil {

	/**
	 * 定义精确到秒的标准时间格式 eg：2001-01-01 10:01:02
	 */
	public static final String DATE_FORMAT_FULLTIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 定义精确到日的标准时间格式 eg：20010101
	 */
	public static final String DATE_FORMAT_SHORTDAY = "yyyyMMdd";
	/**
	 * 定义精确到秒的标准时间格式 eg：20010101100102
	 */
	public static final String DATE_FORMAT_SIMPLEFULLTIME = "yyyyMMddHHmmss";
	/**
	 * 定义精确到毫秒的标准时间格式 eg：0101011001021234
	 */
	public static final String DATE_FORMAT_SIMPLEALLTIME = "yyMMddHHmmssSSSS";
	/**
	 * 定义精确到毫秒的标准时间格式 eg：20010101100102123
	 */
	public static final String DATE_FORMAT_ALLTIME = "yyyyMMddHHmmssSSS";
	/**
	 * 定义精确到日的标准时间格式 eg：2001-01-01
	 */
	public static final String DATE_FORMAT_DAYTIME = "yyyy-MM-dd";
	/**
	 * 定义精确到月的标准时间格式 eg：2001-01
	 */
	public static final String DATE_FORMAT_MONTHTIME = "yyyy-MM";
	/**
	 * 定义精确到分的标准时间格式 eg：2001-01-01 10:01
	 */
	public static final String DATE_FORMAT_MINUTETIME = "yyyy-MM-dd HH:mm";

	/**
	 * 给定Date对象，根据制定日期格式转换为日期字符串<br/>
	 * 如果不写值，默认长格式<br/>
	 * 示例：dateToStr(new Date(),DATE_FORMAT_FULLTIME) = 2001-01-01 10:01:02
	 * @param date  Date 日期
	 * @param format String 格式
	 * @return String 日期字符串
	 */
	public static String date2Str(Date date, String... format) {
		String result = null;
		String formatStr = DATE_FORMAT_FULLTIME;
		if (ObjUtil.notEmpty(format) && format.length >= 1)
			formatStr = format[0];
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			result = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 给定日期字符串，根据指定格式转换为date对象<br/>
	 * 如果不写格式，默认长格式yyyy-MM-dd HH:mm:ss
	 * @param dateStr String 字符型日期
	 * @param format String 格式,如果该参数不填写,默认格式为yyyy-MM-dd
	 * @return Date 日期
	 */
	public static Date str2Date(String dateStr, String... format) {
		String formatStr = DATE_FORMAT_FULLTIME;
		if (ObjUtil.notEmpty(format) && format.length >= 1)
			formatStr = format[0];
		Date date = null;
		DateFormat df = new SimpleDateFormat(formatStr, Locale.CHINA);
		try {
			date = df.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回指定日期的年份
	 * @param date  Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回指定日期的月份
	 * @param date  Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 返回指定日期的日份
	 * @param date  Date 日期
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回指定日期的小时
	 * @param date  日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 返回指定日期的分钟
	 * @param date  日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回指定日期的秒钟
	 * @param date  Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 * @param date  日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 根据传入的时间加上或减去若干天得到时间
	 * @param date  Date 日期
	 * @param day  int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 两个日期相减得到毫秒数
	 * @param date  Date 日期
	 * @param date1 Date 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 功能描述：取得指定日期当月的第一天时间
	 * @param strdate  String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate) {
		Date date = str2Date(strdate, DATE_FORMAT_MONTHTIME);
		return date2Str(date, DATE_FORMAT_MONTHTIME) + "-01";
	}

	/**
	 * 取得指定日期当月的最后一天时间
	 * @param strdate String 字符型日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String getMonthEnd(String strdate) {
		Date date = str2Date(getMonthBegin(strdate), DATE_FORMAT_DAYTIME);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return date2Str(calendar.getTime(), DATE_FORMAT_DAYTIME);
	}
	/**
	 * 功能描述：取得指定日期的第一秒时间
	 * @param strdate String 字符型日期（yyyy-mm-dd格式）
	 *@return String yyyy-MM-dd 00:00:00格式
	 */
	public static String getDayBegin(String strdate) {
		return strdate + " 00:00:00";
	}

	/**
	 * 功能描述：取得指定日期的最后一秒时间
	 * 
	 * @param strdate String 字符型日期（yyyy-mm-dd格式）
	 * @return String 日期字符串 yyyy-MM-dd 23:59:59格式
	 */
	public static String getDayEnd(String strdate) {
		return strdate + " 23:59:59";
	}

	/**
	 * 根据指定日期和天，小时，分钟数求日期
	 * @param strdate  yyyy-MM-dd HH:mm
	 * @param day 前几天或后几天
	 * @param hour 前几个小时或后几个小时
	 * @param minute 前几分钟或后几分钟
	 * @return
	 */
	public static String getfutureTime(String strdate, int day, int hour, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(str2Date(strdate, DATE_FORMAT_MINUTETIME));
		calendar.add(Calendar.DATE, day); //把日期往前减少一天，若想把日期向后推一天则将负数改为正数
		calendar.add(Calendar.HOUR, hour);
		calendar.add(Calendar.MINUTE, minute);
		return date2Str(calendar.getTime(), DATE_FORMAT_MINUTETIME);
	}

	/**
	 * 查看指定日期是星期几
	 * @param strdate  yyyy-MM-dd
	 * @return
	 */
	public static int getWeekday(String strdate){
		Calendar calendar = Calendar.getInstance();
		Date date = str2Date(strdate, DATE_FORMAT_DAYTIME);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取指定月份的天数
	 * @param month yyyy-MM
	 * @return
	 */
	public static int getNumFromMonth(String month){
		Date date = str2Date(month, DATE_FORMAT_MONTHTIME);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定时间段的天数
	 * @param dateBegin  yyyy-MM-dd
	 * @param dateEnd yyyy-MM-dd
	 * @return
	 */
	public static int getNumFromdate(String dateBegin, String dateEnd){
		Date begin = str2Date(dateBegin, DATE_FORMAT_DAYTIME);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		long beginTime = calendar.getTimeInMillis();
		Date end = str2Date(dateEnd, DATE_FORMAT_DAYTIME);
		calendar.setTime(end);
		long endTime = calendar.getTimeInMillis();
		return Integer.parseInt((endTime - beginTime) / (1000 * 3600 * 24) + "") + 1;
	}

	/**
	 * 获取指定月份的所有天数集合
	 * @param month yyyy-MM
	 * @return
	 */
	public static List<String> getDaysFromMonth(String month){
		String currentMonth = date2Str(new Date(), DATE_FORMAT_MONTHTIME);
		String currentDay = "";
		if (currentMonth.equals(month)){
			currentDay = date2Str(new Date(), DATE_FORMAT_DAYTIME);
		}
		List<String> days = new ArrayList<String>();
		int num = getNumFromMonth(month);
		for (int i = 0; i < num; i++){
			String day = "";
			if (i < 9){
				day = month + "-0" + (i + 1);
			} else {
				day = month + "-" + (i + 1);
			}

			if (currentDay.equals(day)){
				break;
			}
			days.add(day);
		}
		return days;
	}

	/**
	 * 获取指定时间段的所有天数集合
	 * @param dateBegin  yyyy-MM-dd
	 * @param dateEnd yyyy-MM-dd
	 * @return
	 */
	public static List<String> getDaysFromDate(String dateBegin, String dateEnd){
		if (dateBegin.compareTo(dateEnd) > 0){
			return null;
		}
		List<String> days = new ArrayList<String>();
		String beginMonth = dateBegin.substring(0, dateBegin.lastIndexOf("-"));
		String beginDay = dateBegin.substring(dateBegin.lastIndexOf("-") + 1, dateBegin.length());
		String endMonth = dateEnd.substring(0, dateEnd.lastIndexOf("-"));
		String endDay = dateEnd.substring(dateEnd.lastIndexOf("-") + 1, dateEnd.length());
		if (beginMonth.equals(endMonth)){
			for (int i = Integer.parseInt(beginDay); i <= Integer.parseInt(endDay); i++){
				String day = "";
				if (i < 10){
					day = beginMonth + "-0" + i;
				} else {
					day = beginMonth + "-" + i;
				}
				days.add(day);
			}
		} else {
			int num = getNumFromMonth(beginMonth);
			for (int i = Integer.parseInt(beginDay); i <= num; i++){
				String day = "";
				if (i < 10){
					day = beginMonth + "-0" + i;
				} else {
					day = beginMonth + "-" + i;
				}
				days.add(day);
			}
			for (int i = 1; i <= Integer.parseInt(endDay); i++){
				String day = "";
				if (i < 10){
					day = endMonth + "-0" + i;
				} else {
					day = endMonth + "-" + i;
				}
				days.add(day);
			}
		}

		return days;
	}

	/**
	 * 获得时间段的分钟数
	 * @param startStr yyyy-MM-dd HH:mm
	 * @param endStr  yyyy-MM-dd HH:mm
	 * @return int
	 */
	public static int getminute(String startStr, String endStr){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(str2Date(startStr, DATE_FORMAT_MINUTETIME));
		long start = calendar.getTimeInMillis();
		calendar.setTime(str2Date(endStr, DATE_FORMAT_MINUTETIME));
		long end = calendar.getTimeInMillis();
		return Math.abs(Integer.parseInt((end - start) / (1000 * 60) + ""));
	}

	/**
	 * 获得时间段的小时数
	 * @param startStr yyyy-MM-dd HH:mm
	 * @param endStr  yyyy-MM-dd HH:mm
	 * @return int
	 */
	public static double gethour(String startStr, String endStr){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(str2Date(startStr, DATE_FORMAT_MINUTETIME));
		long start = calendar.getTimeInMillis();
		calendar.setTime(str2Date(endStr, DATE_FORMAT_MINUTETIME));
		long end = calendar.getTimeInMillis();
		return new BigDecimal(Math.abs((double)(end - start) / (1000 * 60 * 60))).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
