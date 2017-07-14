package com.ulaiber.web.utils;

import java.util.UUID;

public class UUIDGenerator {
	
	 public static String getUUID(){
		 
         String uuid = UUID.randomUUID().toString(); 
         //去掉“-”符号 ,获取32位的uuid
         return uuid.replaceAll("-", ""); 
     } 

}
