package com.ulaiber.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.ulaiber.web.utils.HttpsUtil;


public class MapTest {
	private static double EARTH_RADIUS = 6378.137;  //地球半径  
    //将用角度表示的角转换为近似相等的用弧度表示的角 Math.toRadians  
    private static double rad(double d)  
    {  
        return d * Math.PI / 180.0;  
    }  
    
    public static void main(String[] args){
        String start = "深圳市深大地铁站A出口";
        String end = "深圳市优融网络科技有限公司";

        String startLonLat = getLonLat(start);
        String endLonLat = getLonLat(end);
//        endLonLat +=  "|" + getLonLat("海王银河科技大厦");
//        System.out.println(startLonLat);
//        System.out.println(endLonLat);
//        Long dis = getDistance(startLonLat,endLonLat);
//        System.out.println(dis);
        
//        Distribution startdis = new Distribution();
//        startdis.setLongitude(Double.parseDouble(startLonLat.split(",")[0]));
//        startdis.setDimensionality(Double.parseDouble(startLonLat.split(",")[1]));
//        Distribution enddis = new Distribution();
//        enddis.setLongitude(Double.parseDouble(endLonLat.split(",")[0]));
//        enddis.setDimensionality(Double.parseDouble(endLonLat.split(",")[1]));
//        Distribution dist = new Distribution();
//        double distance = dist.getDistance(startdis, enddis);
//        System.out.println(distance);
        
        
//        String apiUrl1 = "http://localhost:8080/ubank/api/v1/refreshLocation";
//        Map<String, Object> params1 = new HashMap<String, Object>();//请求参数集合
//        params1.put("mobile", "15919477086");
//        params1.put("longitude", "113.944173");
//        params1.put("latitude", "22.538667");
//        String response1 = HttpsUtil.doPost1(apiUrl1, params1);
//        System.out.println(response1);
        
        String apiUrl = "http://localhost:8080/ubank/api/v1/clock";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数集合
        params.put("mobile", "15919477086");
        params.put("longitude", "113.944173");
        params.put("latitude", "22.538667");
        params.put("location", "深圳市优融网络科技有限公司M-10");
        params.put("device", "android-8.0土豪金版");
        String response = HttpsUtil.doPost1(apiUrl, params);
        System.out.println(response);
//        
//        String apiUrl2 = "http://localhost:8080/ubank/api/v1/getClockInfo";
//        Map<String, Object> params2 = new HashMap<String, Object>();//请求参数集合
//        params2.put("mobile", "15919477086");
//        params2.put("date", "2017-08-21");
//        String response2 = HttpsUtil.doGet(apiUrl2, params2);
//        System.out.println(response2);
        
//        String apiUrl3 = "http://localhost:8080/ubank/api/v1/getRecordsForMonth";
//        Map<String, Object> params3 = new HashMap<String, Object>();//请求参数集合
//        params3.put("mobile", "15919477086");
//        params3.put("month", "2017-08");
//        String response3 = HttpsUtil.doGet(apiUrl3, params3);
//        System.out.println(response3);
        
//		for (int i = 1; i<= 31; i++){
//			String sql = "insert into tbl_attendance_records(user_id,user_name,dept_num,company_num,clock_datetime,clock_date,clock_time,clock_type,clock_status,clock_location,clock_device) values(336,'黄国清',40025,20047,'2017-08-";
//			String sql1 = sql;
//			String sql2 = sql;
//			if (i < 10){
//				sql1 += "0" + i + " 09:10','2017-08-" + "0" + i + "','09:10',0,0,'广东省深圳市南山区海王大厦','安卓10.0');";
//				sql2 += "0" + i + " 20:00','2017-08-" + "0" + i + "','20:00',1,0,'广东省深圳市南山区海王大厦','安卓10.0');";
//			} else if (i > 10 && i < 13){
//				sql1 += i + " 10:20','2017-08-" + i + "','10:20',0,1,'广东省深圳市南山区海王大厦','安卓10.0');";
//				sql2 += i + " 20:00','2017-08-" + i + "','20:00',1,0,'广东省深圳市南山区海王大厦','安卓10.0');";
//			} else if (i > 13 && i < 16){
//				sql1 += i + " 09:10','2017-08-" + i + "','09:10',0,0,'广东省深圳市南山区海王大厦','安卓10.0');";
//				sql2 += i + " 17:20','2017-08-" + i + "','17:20',1,2,'广东省深圳市南山区海王大厦','安卓10.0');";
//			} else {
//				sql1 += i + " 09:10','2017-08-" + i + "','09:10',0,0,'广东省深圳市南山区海王大厦','安卓10.0');";
//				sql2 += i + " 20:00','2017-08-" + i + "','20:00',1,0,'广东省深圳市南山区海王大厦','安卓10.0');";
//			}
//			System.out.println(sql1);
//			System.out.println(sql2);
//		}
//		for (int i = 1; i<= 31; i++){
//			String sql = "insert into tbl_attendance_records(user_id,user_name,dept_num,company_num,clock_datetime,clock_date,clock_time,clock_type,clock_status,clock_location,clock_device) values(359,'焦敏',40025,20047,'2017-08-";
//			String sql1 = sql;
//			String sql2 = sql;
//			if (i < 10){
//				sql1 += "0" + i + " 09:10','2017-08-" + "0" + i + "','09:10',0,0,'广东省深圳市南山区海王大厦','iphone100000');";
//				sql2 += "0" + i + " 20:10','2017-08-" + "0" + i + "','20:10',1,0,'广东省深圳市南山区海王大厦','iphone100000');";
//			} else if (i > 10 && i < 12){
//				sql1 += i + " 10:10','2017-08-" + i + "','10:10',0,1,'广东省深圳市南山区海王大厦','iphone100000');";
//				sql2 += i + " 20:10','2017-08-" + i + "','20:10',1,0,'广东省深圳市南山区海王大厦','iphone100000');";
//			} else if (i > 13 && i < 15){
//				sql1 += i + " 09:10','2017-08-" + i + "','09:10',0,0,'广东省深圳市南山区海王大厦','iphone100000');";
//				sql2 += i + " 17:10','2017-08-" + i + "','17:10',1,2,'广东省深圳市南山区海王大厦','iphone100000');";
//			} else {
//				sql1 += i + " 09:10','2017-08-" + i + "','09:10',0,0,'广东省深圳市南山区海王大厦','iphone100000');";
//				sql2 += i + " 20:10','2017-08-" + i + "','20:10',1,0,'广东省深圳市南山区海王大厦','iphone100000');";
//			}
//			System.out.println(sql1);
//			System.out.println(sql2);
//		}
    }

    private static String getLonLat(String address){
        //返回输入地址address的经纬度信息, 格式是 经度,纬度
        String queryUrl = "http://restapi.amap.com/v3/geocode/geo?key=1f255ddf4f7ff1d1660b0417bee242e7&address="+address;
        String queryResult = HttpsUtil.doGet(queryUrl);  //高德接品返回的是JSON格式的字符串

        JSONObject jo = new JSONObject().fromObject(queryResult);
        JSONArray ja = jo.getJSONArray("geocodes");
        return new JSONObject().fromObject(ja.getString(0)).get("location").toString();
    }

    private static Long getDistance(String startLonLat, String endLonLat){
        //返回起始地startAddr与目的地endAddr之间的距离，单位：米
        Long result = new Long(0);
        String queryUrl = "http://restapi.amap.com/v3/distance?key=1f255ddf4f7ff1d1660b0417bee242e7&origins="+endLonLat+"&destination="+startLonLat+"&type=0";
        String queryResult = HttpsUtil.doGet(queryUrl);
        System.out.println(queryResult);
        JSONObject jo = new JSONObject().fromObject(queryResult);
        JSONArray ja = jo.getJSONArray("results");
        System.out.println(ja.toString());
        result = Long.parseLong(new JSONObject().fromObject(ja.getString(0)).get("distance").toString());
        return result;
//        return queryResult;
    }

    private static String getResponse(String serverUrl){
        //用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = in.readLine()) != null){
                result.append(line);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    
    /** 
     * 谷歌地图计算两个坐标点的距离 
     * @param lng1  经度1 
     * @param lat1  纬度1 
     * @param lng2  经度2 
     * @param lat2  纬度2 
     * @return 距离（千米） 
     */  
    public static double getDistance(double lng1, double lat1, double lng2, double lat2)  
    {  
        double radLat1 = Math.toRadians(lat1);  
        double radLat2 = Math.toRadians(lat2);  
        double a = radLat1 - radLat2;  
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
         Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000) / 10000;  
        return s;  
    }  
    

}

class Distribution  
{  
	// 经度  
	double longitude;  
	// 维度  
	double dimensionality;  
	
	public double getLongitude()  
	{  
		return longitude;  
	}  
	
	public void setLongitude(double longitude)  
	{  
		this.longitude = longitude;  
	}  
	
	public double getDimensionality()  
	{  
		return dimensionality;  
	}  
	
	public void setDimensionality(double dimensionality)  
	{  
		this.dimensionality = dimensionality;  
	}  
	
	/* 
	 * 计算两点之间距离 
	 *  
	 * @param start 
	 *  
	 * @param end 
	 *  
	 * @return 米 
	 */  
	public double getDistance(Distribution start, Distribution end)  
	{  
		
		double lon1 = (Math.PI / 180) * start.longitude;  
		double lon2 = (Math.PI / 180) * end.longitude;  
		double lat1 = (Math.PI / 180) * start.dimensionality;  
		double lat2 = (Math.PI / 180) * end.dimensionality;  
		
		// double Lat1r = (Math.PI/180)*(gp1.getLatitudeE6()/1E6);  
		// double Lat2r = (Math.PI/180)*(gp2.getLatitudeE6()/1E6);  
		// double Lon1r = (Math.PI/180)*(gp1.getLongitudeE6()/1E6);  
		// double Lon2r = (Math.PI/180)*(gp2.getLongitudeE6()/1E6);  
		
		// 地球半径  
		double R = 6378.137;  
		
		// 两点间距离 km，如果想要米的话，结果*1000就可以了  
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;  
		
		return d * 1000;  
	}  
}  