package com.ulaiber.web.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Company;
import com.ulaiber.web.model.Departments;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.attendance.Attendance;
import com.ulaiber.web.model.attendance.AttendanceRule;
import com.ulaiber.web.service.AttendanceRuleService;
import com.ulaiber.web.service.AttendanceService;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.DateTimeUtil;

/** 
 * 考勤记录api控制器
 *
 * @author  huangguoqing
 * @date 创建时间：2017年8月14日 下午12:23:46
 * @version 1.0 
 * @since 
 */
@Controller
@RequestMapping("/api/v1/")
public class AttendanceApiController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AttendanceApiController.class);
	
	@Autowired
	private AttendanceService service;
	
	@Autowired
	private AttendanceRuleService ruleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LeaveService leaveService;
	
	@RequestMapping(value = "getRecordsForMonth", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getRecordsForMonth(Long userId, String month, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (null == userId ){
			logger.error("用户ID不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户ID不能为空。");
			return info;
		}
		
		try {
			AttendanceRule rule = ruleService.getRuleByUserId(userId);
			if (null == rule){
				logger.error("用户 {" + userId + "}没有设置考勤规则，请先设置。");
				info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
				info.setMessage("用户 {" + userId + "}没有设置考勤规则，请先设置。");
				return info;
			}
			if (rule.getType() != 0){
				logger.info("用户 {" + userId + "}不参与考勤规则。");
				info.setCode(IConstants.QT_N0T_IN_ATTENDANCE_RULE);
				info.setMessage("用户 {" + userId + "}不参与考勤规则。");
				return info;
			}

			Map<String, Object> data = service.getRecordsByMonthAndUserId(month, userId, rule);
			if (null == data){
				logger.error(month.split("-")[0] + "年没有设置法定节假日，请先设置。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage(month.split("-")[0] + "年没有设置法定节假日，请先设置。");
				return info;
			}
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("用户 {" + userId + "}获取考勤记录成功。");
			info.setData(data);
		} catch (Exception e) {
			logger.error("getRecordsForMonth exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部错误。");
		}

		return info;
	}
	
	@RequestMapping(value = "getClockInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getClockInfo(Long userId, String date,HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo(IConstants.QT_CODE_OK, "获取打卡信息成功。");
		Map<String, Object> data = new HashMap<String, Object>(); 
		if (null == userId){
			logger.error("用户ID不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户ID不能为空。");
			return info;
		}
		AttendanceRule rule = ruleService.getRuleByUserId(userId);
		if (null == rule){
			logger.error("用户 {" + userId + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
			info.setMessage("用户 {" + userId + "}没有设置考勤规则，请先设置。");
			return info;
		}
		if (rule.getType() != 0){
			logger.info("用户 {" + userId + "}不参与考勤规则。");
			info.setCode(IConstants.QT_N0T_IN_ATTENDANCE_RULE);
			info.setMessage("用户 {" + userId + "}不参与考勤规则。");
			return info;
		}

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String datetime = "2017-10-09 02:20";
		String today = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		data.put("clockOnTime", rule.getClockOnTime());
		data.put("clockOffTime", rule.getClockOffTime());
		
		String clockOnTime= today + " " + rule.getClockOnTime();
		String clockOffTime = today + " " + rule.getClockOffTime();
		String dateBegin = today + " " + rule.getClockOnStartTime();
		String dateEnd = today + " " + rule.getClockOffEndTime();
		
		if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
			if (time.compareTo(rule.getClockOffEndTime()) <= 0){
				today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
				dateBegin = DateTimeUtil.getfutureTime(dateBegin, -1, 0, 0);
				clockOnTime = DateTimeUtil.getfutureTime(clockOnTime, -1, 0, 0);
				clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, -1, 0, 0);
			} else {
				dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
				if (rule.getClockOnTime().compareTo(rule.getClockOffTime()) > 0){
					clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 1, 0, 0);
				}
			}
		}
		
		//当前时间是哪天
		data.put("today", today);
		
		if (StringUtils.equals(today, date)){
			date = "";
		}
		
		if (StringUtils.isNotEmpty(date)){
			today = date;
			clockOnTime= today + " " + rule.getClockOnTime();
			clockOffTime = today + " " + rule.getClockOffTime();
			dateBegin = today + " " + rule.getClockOnStartTime();
			dateEnd = today + " " + rule.getClockOffEndTime();
			
			if (rule.getClockOffEndTime().compareTo(rule.getClockOnStartTime()) < 0){
				if (time.compareTo(rule.getClockOffEndTime()) <= 0){
					today = DateTimeUtil.getfutureTime(datetime, -1, 0, 0).split(" ")[0];
					dateBegin = DateTimeUtil.getfutureTime(dateBegin, -1, 0, 0);
					clockOnTime = DateTimeUtil.getfutureTime(clockOnTime, -1, 0, 0);
					clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, -1, 0, 0);
				} else {
					dateEnd = DateTimeUtil.getfutureTime(dateEnd, 1, 0, 0);
					if (rule.getClockOnTime().compareTo(rule.getClockOffTime()) > 0){
						clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 1, 0, 0);
					}
				}
			}
		} 
		
		
		data.put("clockDate", today);
		boolean isRestDay = service.isRestDay(today, rule);
		if (isRestDay){
			data.put("isRestDay", isRestDay);
			info.setData(data);
			return info;
		}
		
		//请假信息
		String leaveMsg = "";
		List<LeaveRecord> leaveRecords = leaveService.getLeaveRecordByUserIdAndDate(userId, today);
		if (leaveRecords != null && leaveRecords.size() > 0){
			//请假时间不可重复的情况下，一条记录时可能是全天或半天
			if (leaveRecords.size() == 1){
				LeaveRecord leaveRecord = leaveRecords.get(0);
				//当天请假
				if (StringUtils.equals(today, leaveRecord.getStartDate()) && StringUtils.equals(today, leaveRecord.getEndDate())){
					if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
						leaveMsg = "请假：上半天";
					} else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
						leaveMsg = "请假：下半天";
					} else if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "1")){
						leaveMsg = "请假：全天";
					}
				} else {
					String startDate = leaveRecord.getStartDate().split("-")[2] + "日";
					String startType = StringUtils.equals(leaveRecord.getStartType(), "0") ? "上半天" : "下半天";
					String endDate = leaveRecord.getEndDate().split("-")[2] + "日";
					String endType = StringUtils.equals(leaveRecord.getEndType(), "0") ? "上半天" : "下半天";
					leaveMsg = "请假：" + startDate + " " + startType + " - " + endDate + " " + endType;
				}
			}
			//请假时间不可重复的情况下，2条记录时只可能是上半天加下半天
			if (leaveRecords.size() == 2){
				leaveMsg = "请假：全天";
			}
		}
		data.put("leaveMsg", leaveMsg);
		
		//date不为空, 查询指定日期的打卡信息 
		if (StringUtils.isNotEmpty(date)){
			List<Attendance> records = service.getRecordsByDateAndUserId(date, date, userId);
			Attendance record = records.size() == 0 ? null : records.get(0);
			if (leaveRecords != null && leaveRecords.size() > 0){
				data = message1(leaveRecords, record, date, clockOnTime, clockOffTime, rule, data);
			} else {
				String clockMsg = clockOnTime.split(" ")[1] + "~" + clockOffTime.split(" ")[1];
				if (rule.getRestFlag() == 0){
					if (clockOnTime.split(" ")[1].compareTo(rule.getRestStartTime()) < 0 && clockOffTime.split(" ")[1].compareTo(rule.getRestEndTime()) > 0){
						clockMsg = clockOnTime.split(" ")[1] + "~" + rule.getRestStartTime() + "   " + rule.getRestEndTime() + "~" + clockOffTime.split(" ")[1];
					}
				}
				data.put("clockMsg", clockMsg);
			}			
			data.put("record", record);
			info.setData(data);
			return info;
		}
		
		//0上班卡  1下班卡  2更新 打卡  3不能打上班卡  4不能打下班卡  5全天请假了
		int type = 0;
		//查询当天的考勤记录
		List<Attendance> records = service.getRecordsByDateAndUserId(today, today, userId);
		Attendance record = records.size() == 0 ? null : records.get(0);
		//是否开启弹性时间
		if (rule.getFlexibleFlag() == 0){
			if (records != null && records.size() > 0){
				//获取当前时间>上班时间的分钟数
				int minute = records.get(0).getClockOnDateTime() == null ? 0 : DateTimeUtil.getminute(clockOnTime, records.get(0).getClockOnDateTime());
				//下班是否顺延
				if (rule.getPostponeFlag() == 0){
					//在弹性上班时间内打卡，下班时间才顺延
					if (minute > 0 && minute <= rule.getFlexibleTime()){
						clockOffTime = DateTimeUtil.getfutureTime(clockOffTime, 0, 0, minute);
					}
				}
			}
		}
		
		// 0:上半天请假   1:下半天请假   2:全天请假
		int flag = -1;
		if (record == null || record != null && !StringUtils.equals(record.getRevokeType(), "1")){
			if (leaveRecords != null && leaveRecords.size() > 0){
				if (leaveRecords.size() == 1){
					LeaveRecord leaveRecord = leaveRecords.get(0);
					String startDay = leaveRecord.getStartDate();
					String endDay = leaveRecord.getEndDate();
					//请假在同一天
					if (StringUtils.equals(startDay, today) && StringUtils.equals(endDay, today)){
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = today + " " + rule.getRestStartTime();
							String restEndTime = today + " " + rule.getRestEndTime();
							//上半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = restEndTime;
								flag = 0;
							}
							//下半天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								clockOffTime = restStartTime;
								flag = 1;
							}
							//全天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								flag = 2;
							}
						} else {
							//每天工作分钟数
							int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
							String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
							//上半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = middleTime;
								flag = 0;
							}
							//下半天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								clockOffTime = middleTime;
								flag = 1;
							}
							//全天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								flag = 2;
							}
						}
						//请假开始时间为今天
					} else if (StringUtils.equals(startDay, today)){
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = today + " " + rule.getRestStartTime();
							//下半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "1")){
								clockOffTime = restStartTime;
								flag = 1;
							}						
							//全天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "0")){
								flag = 2;
							}
							
						} else {
							//每天工作分钟数
							int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
							String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
							//下半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "1")){
								clockOffTime = middleTime;
								flag = 1;
							}
							//全天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "0")){
								flag = 2;
							}
						}
						//请假结束时间为今天
					} else if (StringUtils.equals(endDay, today)){
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restEndTime = today + " " + rule.getRestEndTime();
							//上半天请假
							if (StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = restEndTime;
								flag = 0;
							}
							//全天请假
							else if (StringUtils.equals(leaveRecord.getEndType(), "1")){
								flag = 2;
							}
						} else {
							//每天工作分钟数
							int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
							String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
							//上半天请假
							if (StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = middleTime;
								flag = 0;
							}
							//全天请假
							else if (StringUtils.equals(leaveRecord.getEndType(), "1")){
								flag = 2;
							}
						}
					}
				}
				if (leaveRecords.size() == 2){
					flag = 2;
				}
			}
		}
		
		data.put("leaveType", flag);
		String clockMsg = "";
		// 0:上半天请假   1:下半天请假   2:全天请假
		if (flag == 0){
			clockMsg = clockOnTime.split(" ")[1] + "~" + clockOffTime.split(" ")[1];
		} else if (flag == 1){
			clockMsg = clockOnTime.split(" ")[1] + "~" + clockOffTime.split(" ")[1];
		} else if (flag == 2){
			clockMsg = "";
		} else {
			if (rule.getRestFlag() == 0){
				if (clockOnTime.split(" ")[1].compareTo(rule.getRestStartTime()) < 0 && clockOffTime.split(" ")[1].compareTo(rule.getRestEndTime()) > 0){
					clockMsg = clockOnTime.split(" ")[1] + "~" + rule.getRestStartTime() + "   " + rule.getRestEndTime() + "~" + clockOffTime.split(" ")[1];
				}
			}
		}

		if (null == record){
			//如果有请假 上下班时间会变化 重新put一下
			data.put("clockOnTime", clockOnTime.split(" ")[1]);
			data.put("clockOffTime", clockOffTime.split(" ")[1]);
			//当前时间在下班时间和最晚下半时间之间
			if (datetime.compareTo(clockOffTime) >= 0 && datetime.compareTo(dateEnd) <= 0){
				type = 1;
			}
		} else {
			//销假之后按正常的上下班时间算 ,不销假按请假时间来算上下班时间
			if (!StringUtils.equals(record.getRevokeType(), "1")){
				data.put("clockOnTime", clockOnTime.split(" ")[1]);
				data.put("clockOffTime", clockOffTime.split(" ")[1]);
			}
			if (StringUtils.isEmpty(records.get(0).getClockOffDateTime())){
				type = 1;
			}
			//当前时间在上一次下班时间和最晚下半时间之间
			else if (datetime.compareTo(records.get(0).getClockOffDateTime()) >= 0 && datetime.compareTo(dateEnd) <= 0){
				type = 2;
			}
		}
		
		//当前时间<最早上班打卡时间
		if (datetime.compareTo(dateBegin) < 0){
			type = 3;
		}
		//当前时间>最晚下班打卡时间
		else if (datetime.compareTo(dateEnd) > 0){
			type = 4;
		}
		
		data.put("type", type);
		data.put("record", record);
		data.put("clockMsg", clockMsg);
		info.setData(data);
		return info;
	}
	
	private Map<String, Object> message1(List<LeaveRecord> leaveRecords, Attendance record, String date, String clockOnTime, String clockOffTime, AttendanceRule rule, Map<String, Object> data){
		// 0:上半天请假   1:下半天请假   2:全天请假
		int flag = -1;
		if (leaveRecords.size() == 1){
			LeaveRecord leaveRecord = leaveRecords.get(0);
			//请假在同一天
			if (StringUtils.equals(leaveRecord.getStartDate(), date) && StringUtils.equals(leaveRecord.getEndDate(), date)){
				//全天请假,没有打卡记录
				if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "1")){
					if (null == record){
						data.put("type", 5);
						flag = 2;
					}
				} else {
					if (record != null){
						//请半天假,有打卡记录且为不销假打卡
						if (!StringUtils.equals(record.getRevokeType(), "1")){
							//是否有休息时段
							if (rule.getRestFlag() == 0){
								String restStartTime = date + " " + rule.getRestStartTime();
								String restEndTime = date + " " + rule.getRestEndTime();
								//上半天请假
								if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
									clockOnTime = restEndTime;
									flag = 0;
								}
								//下半天请假
								else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
									clockOffTime = restStartTime;
									flag = 1;
								}
							} else {
								//每天工作分钟数
								int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
								String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
								//上半天请假
								if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
									clockOnTime = middleTime;
									flag = 0;
								}
								//下半天请假
								else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
									clockOffTime = middleTime;
									flag = 1;
								}
							}
						}
					} else {
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = date + " " + rule.getRestStartTime();
							String restEndTime = date + " " + rule.getRestEndTime();
							//上半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = restEndTime;
								flag = 0;
							}
							//下半天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								clockOffTime = restStartTime;
								flag = 1;
							}
						} else {
							//每天工作分钟数
							int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
							String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
							//上半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = middleTime;
								flag = 0;
							}
							//下半天请假
							else if (StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								clockOffTime = middleTime;
								flag = 1;
							}
						}
					}
				}
				//请假开始时间为今天
			} else if (StringUtils.equals(leaveRecord.getStartDate(), date)){
				//全天请假,没有打卡记录
				if (StringUtils.equals(leaveRecord.getStartType(), "0")){
					if (null == record){
						data.put("type", 5);
						flag = 2;
					}
				} else {
					if (record != null){
						//请半天假,有打卡记录且为不销假打卡
						if (!StringUtils.equals(record.getRevokeType(), "1")){
							//是否有休息时段
							if (rule.getRestFlag() == 0){
								String restStartTime = date + " " + rule.getRestStartTime();
								//下半天请假
								if (StringUtils.equals(leaveRecord.getStartType(), "1")){
									clockOffTime = restStartTime;
									flag = 1;
								}
							} else {
								//每天工作分钟数
								int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
								String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
								//下半天请假
								if (StringUtils.equals(leaveRecord.getStartType(), "1")){
									clockOffTime = middleTime;
									flag = 1;
								}
							}
						}
					} else {
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = date + " " + rule.getRestStartTime();
							//下半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "1")){
								clockOffTime = restStartTime;
								flag = 1;
							}
						} else {
							//每天工作分钟数
							int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
							String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
							//下半天请假
							if (StringUtils.equals(leaveRecord.getStartType(), "1")){
								clockOffTime = middleTime;
								flag = 1;
							}
						}
					}
				}
				//请假结束时间为今天
			} else if (StringUtils.equals(leaveRecord.getEndDate(), date)){
				//全天请假,没有打卡记录
				if (StringUtils.equals(leaveRecord.getEndType(), "1")){
					if (null == record){
						data.put("type", 5);
						flag = 2;
					}
				} else {
					if (record != null){
						//请半天假,有打卡记录且为不销假打卡
						if (!StringUtils.equals(record.getRevokeType(), "1")){
							//是否有休息时段
							if (rule.getRestFlag() == 0){
								String restEndTime = date + " " + rule.getRestEndTime();
								//上半天请假
								if (StringUtils.equals(leaveRecord.getEndType(), "0")){
									clockOnTime = restEndTime;
									flag = 0;
								}
							} else {
								//每天工作分钟数
								int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
								String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
								//上半天请假
								if (StringUtils.equals(leaveRecord.getEndType(), "0")){
									clockOnTime = middleTime;
									flag = 0;
								}
							}
						}
					} else {
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restEndTime = date + " " + rule.getRestEndTime();
							//上半天请假
							if (StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = restEndTime;
								flag = 0;
							}
						} else {
							//每天工作分钟数
							int workMinutes = DateTimeUtil.getminute(clockOnTime, clockOffTime);
							String middleTime = DateTimeUtil.getfutureTime(clockOnTime, 0, 0, workMinutes / 2);
							//上半天请假
							if (StringUtils.equals(leaveRecord.getEndType(), "0")){
								clockOnTime = middleTime;
								flag = 0;
							}
						}
					}
				}
				//今天在请假开始和结束时间之间
			} else if (date.compareTo(leaveRecord.getStartDate()) > 0 && date.compareTo(leaveRecord.getEndDate()) < 0){
				if (null == record){
					data.put("type", 5);
					flag = 2;
				}
			}
		}
		if (leaveRecords.size() == 2){
			if (null == record){
				data.put("type", 5);
				flag = 2;
			}
		}
		
		data.put("clockOnTime", clockOnTime.split(" ")[1]);
		data.put("clockOffTime", clockOffTime.split(" ")[1]);
		data.put("leaveType", flag);
		String clockMsg = "";
		// 0:上半天请假   1:下半天请假   2:全天请假
		if (flag == 0){
			clockMsg = clockOnTime.split(" ")[1] + "~" + clockOffTime.split(" ")[1];
		} else if (flag == 1){
			clockMsg = clockOnTime.split(" ")[1] + "~" + clockOffTime.split(" ")[1];
		} else if (flag == 2){
			clockMsg = "";
		} else {
			if (rule.getRestFlag() == 0){
				if (clockOnTime.split(" ")[1].compareTo(rule.getRestStartTime()) < 0 && clockOffTime.split(" ")[1].compareTo(rule.getRestEndTime()) > 0){
					clockMsg = clockOnTime.split(" ")[1] + "~" + rule.getRestStartTime() + "   " + rule.getRestEndTime() + "~" + clockOffTime.split(" ")[1];
				}
			}
		}
		data.put("clockMsg", clockMsg);
		return data;
	}
	
	@RequestMapping(value = "clock", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo clock(Long userId, String longitude, String latitude, String device, String location, boolean isOutClock, String remark, String revokeType,
			HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (null == userId || StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			logger.error("用户ID或经纬度不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("用户ID或经纬度不能为空。");
			return info;
		}
		
		try {
			User user = userService.getUserById(userId);
			if (null == user){
				logger.error("用户  " + userId + " 不存在。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("用户  " + userId + " 不存在。");
				return info;
			}
			
			AttendanceRule rule = ruleService.getRuleByUserId(userId);
			if (null == rule){
				logger.error("用户 {" + userId + "}没有设置考勤规则，请先设置。");
				info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
				info.setMessage("用户  " + userId + " 没有设置考勤规则，请先设置。");
				return info;
			}
			if (rule.getType() != 0){
				logger.info("用户 {" + userId + "}不参与考勤规则。");
				info.setCode(IConstants.QT_N0T_IN_ATTENDANCE_RULE);
				info.setMessage("用户 {" + userId + "}不参与考勤规则。");
				return info;
			}
			
			String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
			String today = datetime.split(" ")[0];
			String restStartTime = today + " " + rule.getRestStartTime();
			String restEndTime = today + " " + rule.getRestEndTime();
			//0:不销假正常上班打卡  1:销假打卡
			if (StringUtils.isEmpty(revokeType)){
				List<LeaveRecord> leaveRecords = leaveService.getLeaveRecordByUserIdAndDate(userId, today);
				if (leaveRecords != null && leaveRecords.size() > 0){
					//请假时间不可重复的情况下，一条记录时可能是全天或半天
					if (leaveRecords.size() == 1){
						LeaveRecord leaveRecord = leaveRecords.get(0);
						//请假开始时间和结束时间在同一天
						if (StringUtils.equals(leaveRecord.getStartDate(), today) && StringUtils.equals(leaveRecord.getEndDate(), today)){
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "1")){
								logger.info("用户" + userId + "今天请假了，要销假打卡吗？ ");
								info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
								info.setMessage("今天请假了，要销假打卡吗？");
								return info;
							}
							//上半天请假,打卡时间<=请假结束时间时提示时候销假打卡
							//下半天请假,打卡时间>=请假开始时间时提示时候销假打卡
							if (StringUtils.equals(leaveRecord.getStartType(), "0") && StringUtils.equals(leaveRecord.getEndType(), "0") && datetime.compareTo(restStartTime) <= 0
									|| StringUtils.equals(leaveRecord.getStartType(), "1") && StringUtils.equals(leaveRecord.getEndType(), "1") && datetime.compareTo(restEndTime) >= 0){
								logger.info("用户" + userId + "在请假的时间段内，要销假打卡吗？ ");
								info.setCode(IConstants.QT_IN_LEAVE_TIME);
								info.setMessage("在请假的时间段内，要销假打卡吗？");
								return info;
							}
						} 
						else if (StringUtils.equals(leaveRecord.getStartDate(), today)){
							if (StringUtils.equals(leaveRecord.getStartType(), "0")){
								logger.info("用户" + userId + "今天请假了，要销假打卡吗？ ");
								info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
								info.setMessage("今天请假了，要销假打卡吗？");
								return info;
							} else if (StringUtils.equals(leaveRecord.getStartType(), "1") && datetime.compareTo(restEndTime) >= 0){
								logger.info("用户" + userId + "在请假的时间段内，要销假打卡吗？ ");
								info.setCode(IConstants.QT_IN_LEAVE_TIME);
								info.setMessage("在请假的时间段内，要销假打卡吗？");
								return info;
							}
						} 
						else if (StringUtils.equals(leaveRecord.getEndDate(), today)){
							if (StringUtils.equals(leaveRecord.getEndType(), "1")){
								logger.info("用户" + userId + "今天请假了，要销假打卡吗？ ");
								info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
								info.setMessage("今天请假了，要销假打卡吗？");
								return info;
							} else if (StringUtils.equals(leaveRecord.getEndType(), "0") && datetime.compareTo(restStartTime) <= 0){
								logger.info("用户" + userId + "在请假的时间段内，要销假打卡吗？ ");
								info.setCode(IConstants.QT_IN_LEAVE_TIME);
								info.setMessage("在请假的时间段内，要销假打卡吗？");
								return info;
							}
						}
						else if (today.compareTo(leaveRecord.getStartDate()) > 0 && today.compareTo(leaveRecord.getEndDate()) < 0){
							logger.info("用户" + userId + "今天请假了，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
							info.setMessage("今天请假了，要销假打卡吗？");
							return info;
						}
					}
					//请假时间不可重复的情况下，2条记录时只可能是上半天加下半天
					if (leaveRecords.size() == 2){
						logger.info("用户" + userId + "今天请假了，要销假打卡吗？ ");
						info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
						info.setMessage("今天请假了，要销假打卡吗？");
						return info;
					}
				}
			}
			
			//是否外勤打卡 是则不再刷新位置
			if (!isOutClock){
				//检查打卡的位置是否在设置范围内
				ResultInfo result = service.refreshLocation(longitude, latitude, rule);
				if (result.getCode() == IConstants.QT_N0T_IN_BOUNDS){
					logger.error("不在设置的打卡范围之内，请刷新位置。");
					return result;
				}
			}

			Attendance att = new Attendance();
			att.setUserId(user.getId());
			att.setUserName(user.getUserName());
			
			Company com = new Company();
			com.setCompanyNumber(user.getCompanyId());
			att.setCompany(com);
			
			Departments dept = new Departments();
			dept.setDept_number(user.getDept_number());
			att.setDept(dept);
			att.setRevokeType(revokeType);
			info = service.save(att, device, location, isOutClock, remark, rule);
			if (info.getCode() == IConstants.QT_CODE_OK){
				logger.info("用户 {" + userId + "}打卡成功。");
				info.setMessage("用户 {" + userId + "}打卡成功。");
			}
			
		} catch (Exception e) {
			logger.error("clock exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部错误。");
		}
 
		
		return info;
	}

	@RequestMapping(value = "refreshLocation", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo refreshLocation(Long userId, String longitude, String latitude, HttpServletRequest request, HttpServletResponse response){
		logger.debug("refreshLocation start...");
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("经度或纬度不能为空。");
			return info;
		}
		
		try {
			AttendanceRule rule = ruleService.getRuleByUserId(userId);
			if (null == rule){
				logger.error("用户 {" + userId + "}没有设置考勤规则，请先设置。");
				info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
				info.setMessage("用户  " + userId + " 没有设置考勤规则，请先设置。");
				return info;
			}
			if (rule.getType() != 0){
				logger.info("用户 {" + userId + "}不参与考勤规则。");
				info.setCode(IConstants.QT_N0T_IN_ATTENDANCE_RULE);
				info.setMessage("用户 {" + userId + "}不参与考勤规则。");
				return info;
			}
			
			info = service.refreshLocation(longitude, latitude, rule);
		} catch (Exception e) {
			logger.error("refreshLocation exception: ", e);
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("系统内部错误。");
		}

		logger.debug("refreshLocation end...");
		return info;
	}
	
	@RequestMapping(value = "getHoursByDateAndMobile", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getHoursByDateAndMobile(String mobile, String startDateTime, String endDateTime, HttpServletRequest request, HttpServletResponse response){
		logger.debug("getHoursByDateAndMobile start...");
		ResultInfo info = new ResultInfo();
		if (startDateTime.compareTo(endDateTime) > 0){
			logger.error("请假开始时间不能大于结束时间。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请假开始时间不能大于结束时间。");
			return info;
		}
		
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
			info.setMessage("用户  " + mobile + " 没有设置考勤规则，请先设置。");
			return info;
		}
		//开始日期  yyyy-MM-dd
		String startDate = startDateTime.split(" ")[0];
		//结束日期  yyyy-MM-dd
		String endDate = endDateTime.split(" ")[0];
		List<String> days = DateTimeUtil.getDaysFromDate(startDate, endDate);
		if (days.size() == 1){
			//上班时间
			String clockOnTime = startDate + " " + rule.getClockOnTime();
			//下班时间
			String clockOffTime = endDate + " " + rule.getClockOffTime();
			if (startDateTime.compareTo(clockOnTime) < 0 && endDateTime.compareTo(clockOnTime) < 0
					|| startDateTime.compareTo(clockOffTime) > 0 && endDateTime.compareTo(clockOffTime) > 0){
				logger.error("请假的时间范围不在工作时间内。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("请假的时间范围不在工作时间内。");
				return info;
			}
		}
		double hours = service.getHoursByDate(startDateTime, endDateTime, rule);
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(hours);
		logger.debug("getHoursByDateAndMobile end...");
		return info;
	}
	
	/**
	 * 获取请假时长
	 * @param userId    用户id
	 * @param startDate 开始日期  yyyy-MM-dd
	 * @param startType 0：上半天   1：下半天
	 * @param endDate   结束日期  yyyy-MM-dd
	 * @param endType   0：上半天   1：下半天
	 * @param request   request
	 * @param response  response
	 * @return ResultInfo
	 */
	@RequestMapping(value = "getDaysByDate", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getDaysByDate(Long userId, String startDate, String startType, String endDate, String endType, HttpServletRequest request, HttpServletResponse response){
		logger.debug("getDaysByDate start...");
		ResultInfo info = new ResultInfo();
		if (userId == null || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(startType) || StringUtils.isEmpty(endDate) || StringUtils.isEmpty(endType)){
			logger.error("参数不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("参数不能为空。");
			return info;
		}
		if (startDate.compareTo(endDate) > 0){
			logger.error("请假开始时间不能大于结束时间。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("请假开始时间不能大于结束时间。");
			return info;
		}
		
		AttendanceRule rule = ruleService.getRuleByUserId(userId);
		if (null == rule){
			logger.error("用户 {" + userId + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
			info.setMessage("用户  " + userId + " 没有设置考勤规则，请先设置。");
			return info;
		}
		Map<String, Object> data = service.getDaysByDate(startDate, startType, endDate, endType, rule);
		info.setCode(IConstants.QT_CODE_OK);
		info.setData(data);
		logger.debug("getDaysByDate end...");
		return info;
	}
	
}
