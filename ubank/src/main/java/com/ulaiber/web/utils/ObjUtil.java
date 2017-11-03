package com.ulaiber.web.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一组对象操作实用工具集合
 * @author huangguoqing
 *
 */
public class ObjUtil {
	
	/**
	 * 判断是否为空， 调用示例：  if (ObjUtil.notEmpty(obj)){....} <br/>
	 * 说明：<br/>
	 * 如果对象为null，直接返回false<br/>
	 * 如果为String类型，去掉空格后值为“”则返回false<br/>
	 * 如果为数组类型，length==0 则返回false<br/>
	 * 如果为List||Collection||Map，如果size==0则返回false<br/>
	 * @author xiaokang.zh
	 * @param object 支持多种数据类型
	 * @return 为空返回false,不为空返回true
	 */
	@SuppressWarnings("unchecked")
	public static <T,E,F> boolean notEmpty(T obj){
		boolean result = true;
		//空判断
		if (obj == null)
			result = false;
		//字符串空判断
		if (obj instanceof String){
			result = String.valueOf(obj).trim().equals("")?false:true;
		}
		//数组类型判断
		if (obj instanceof Object[]){
			result = (((Object[])obj).length==0)?false:true;
		}
		//集合类型
		if (obj instanceof List) {
			result = ((List<E>)obj).isEmpty()?false:true;
		}
		if (obj instanceof Collection) {
			result = ((Collection<E>)obj).isEmpty()?false:true;
		} else if (obj instanceof Map) {
			result = ((Map<E, F>)obj).isEmpty()?false:true;
		}
		return result;
	}
	
	
	/**
	 * 将List<String>列表类型转换成String[]数组类型
	 * @param list 
	 * @return
	 */
	public static String[] listString2Array(List<String> list){
		return notEmpty(list) ? (String[])list.toArray(new String[0]) : null; 
	}

	/**
	 * 将List<Integer>列表类型转换成Integer[]数组类型
	 * @param list 
	 * @return
	 */
	public static Integer[] listInteger2Array(List<Integer> list){
		return notEmpty(list) ? (Integer[])list.toArray(new Integer[0]) : null;
	}
	
	/**
     * 获取两个List的不同元素
     * @param list1
     * @param list2
     * @return
     */
	public static List<String> getDiffrent(List<String> list1, List<String> list2) {
		List<String> diff = new ArrayList<String>();
		List<String> maxList = list1;
		List<String> minList = list2;
		if(list2.size() > list1.size()){
			maxList = list2;
			minList = list1;
		}
		Map<String,Integer> map = new HashMap<String,Integer>(maxList.size());
		for (String string : maxList) {
			map.put(string, 1);
		}
		for (String string : minList) {
			if(map.get(string) != null) {
				map.put(string, 2);
				continue;
			}
			diff.add(string);
		}
		for(Map.Entry<String, Integer> entry:map.entrySet()){
			if(entry.getValue() == 1){
				diff.add(entry.getKey());
			}
		}
		return diff;
	}
	
	
	/**
	 * 拼接Class中的属性的值(只限于vo类的get方法),可以拼接父类属性
	 * @param list
	 * @param fieldName 要拼接的属性的名称
	 * @param side 属性两边的字符
	 * @param last 每个属性的间隔字符
	 * @param flag flag等于1 查询父类中的属性 否则查找子类
	 * @return
	 * @author
	 */
	public static <T> String splicingString(List<T> list, String fieldName, String side, String last, int flag) {
		String string = "";
		side = (side == null) ? "" : side ;
		last = (last == null) ? "" : last ;
		if (ObjUtil.notEmpty(fieldName)  && ObjUtil.notEmpty(list) ) {
			for (T t : list) {
				Class<?> tempClass = t.getClass();
				tempClass = (flag==1)?tempClass.getSuperclass():tempClass;
				string = getFiledValue(fieldName, side, last, string, t,tempClass);
			}
			string = string.substring(0, string.lastIndexOf(last));
		}
		return string;
	}


	private static <T> String getFiledValue(String fieldName, String side,String last, String string, T t, Class<?> tempClass) {
		String methodNameLower;
		for (Method m : tempClass.getDeclaredMethods()) {
			methodNameLower = m.getName().toLowerCase();
			if (methodNameLower.startsWith("get") && methodNameLower.substring(3, methodNameLower.length()).equals(fieldName.toLowerCase())) {
				try {
					if (String.valueOf(m.invoke(t)) != null && String.valueOf(m.invoke(t)).length() > 0) {
						string += side + String.valueOf(m.invoke(t)) + side + last;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return string;
	}
	
	/**
	 * ","号分隔
	 * 2012-5-17 上午9:33:12
	 * @param list
	 * @param fieldName
	 * @return
	 * @author
	 */
	public static <T> String splicingString(List<T> list, String fieldName){
		return splicingString(list, fieldName, null, ",",0);
	}
	
	/**
	 * 拼接Class中的属性的值(只限于vo类的get方法)
	 * @param list
	 * @param fieldName 要拼接的属性的名称
	 * @param side 属性两边的字符
	 * @param last 每个属性的间隔字符
	 * @return
	 * @author
	 */
	public static <T> String splicingString(List<T> list, String fieldName, String side, String last) {
		return splicingString(list, fieldName, side, last,0);
	}
}
