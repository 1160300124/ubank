package com.ulaiber.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.AttendanceDao;
import com.ulaiber.web.model.Attendance;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.utils.DateTimeUtil;
import com.ulaiber.web.utils.HttpsUtil;

/** 
 * 考勤记录业务逻辑实现类
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月11日 上午11:08:05
 * @version 1.0 
 * @since 
 */
@Service
public class AttendanceServiceImpl extends BaseService implements AttendanceService {
	
	private static Logger logger = Logger.getLogger(AttendanceServiceImpl.class);
	
	@Resource
	private AttendanceDao dao;

	@Override
	public boolean save(Attendance att) {
		String clock_on_time = "09:30";
		String clock_off_time = "18:30";
		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
		String date = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		//type：0签到，1签退   status：0正常 ，1迟到， 2早退
		if (StringUtils.equals(att.getClockType(), "0")){
			if (time.compareTo(clock_on_time) > 0){
				att.setClockStatus("1");
			} else {
				att.setClockStatus("0");
			}
		} else if (StringUtils.equals(att.getClockType(), "1")){
			if (time.compareTo(clock_off_time) < 0){
				att.setClockStatus("2");
			} else {
				att.setClockStatus("0");
			}
		}
		att.setClockDate(date);
		att.setClockTime(time);
		return dao.save(att) > 0;
	}

	@Override
	public List<Attendance> getRecordsByCond(Map<String, Object> params) {

		return dao.getRecordsByCond(params);
	}

	@Override
	public ResultInfo refreshLocation(String longitude, String latitude, HttpServletRequest request) {
		ResultInfo retInfo = new ResultInfo();
		//APP手机定位的坐标
		String mobileLocation = longitude + "," + latitude;
		//公司坐标
		String companyLocation = "113.941664,22.542380";
		String url = "http://restapi.amap.com/v3/distance?key=1f255ddf4f7ff1d1660b0417bee242e7&origins=" + mobileLocation + "&destination=" + companyLocation + "&type=0";
		String result = HttpsUtil.doGet(url);
		if (StringUtils.isNotEmpty(result)){
			//{"status":"1","info":"OK","infocode":"10000","results":[{"origin_id":"1","dest_id":"1","distance":"487","duration":"0"}]}
			JSONObject json = JSONObject.parseObject(result);
			if (!StringUtils.equals(json.getString("status"), "1")){
				String infocode = json.getString("infocode");
				String info = json.getString("info");
				logger.error("调用地图服务失败，code=" + infocode + ",info=" + info);
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("调用地图服务失败，code=" + infocode + ",info=" + info);
				return retInfo;
			}
			JSONArray array = json.getJSONArray("results");
			//打卡规则设置里设置的打卡距离
			long distance = 1000;
			long currentDistance = Long.parseLong(array.getJSONObject(0).getString("distance"));
			if (currentDistance > distance){
				logger.info("当前坐标离公司" + currentDistance + "m，超过设置的" + distance + "m");
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("当前坐标离公司" + currentDistance + "m，超过设置的" + distance + "m，在走进一点点就可以打卡了哟，么么哒...");
				return retInfo;
			}
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setMessage("当前坐标离公司" + currentDistance + "m，可以打卡。");
		}
		return retInfo;
	}

}
