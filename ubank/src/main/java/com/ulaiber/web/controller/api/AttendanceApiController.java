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
	public ResultInfo getRecordsForMonth(String mobile, String month, HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(month)){
			logger.error("手机号或月份不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号或月份不能为空。");
			return info;
		}
		
		try {
			AttendanceRule rule = ruleService.getRuleByMobile(mobile);
			if (null == rule){
				logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
				info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
				info.setMessage("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
				return info;
			}

			Map<String, Object> data = service.getRecordsByMonthAndMobile(month, mobile, rule);
			if (null == data){
				logger.error(month.split("-")[0] + "年没有设置法定节假日，请先设置。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage(month.split("-")[0] + "年没有设置法定节假日，请先设置。");
				return info;
			}
			info.setCode(IConstants.QT_CODE_OK);
			info.setMessage("用户 {" + mobile + "}获取考勤记录成功。");
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
	public ResultInfo getClockInfo(String mobile, String date,HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo(IConstants.QT_CODE_OK, "获取打卡信息成功。");
		Map<String, Object> data = new HashMap<String, Object>(); 
		if (StringUtils.isEmpty(mobile)){
			logger.error("手机号不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号不能为空。");
			return info;
		}
		AttendanceRule rule = ruleService.getRuleByMobile(mobile);
		if (null == rule){
			logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			info.setCode(IConstants.QT_N0_ATTENDANCE_RULE);
			info.setMessage("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
			return info;
		}

		String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
//		String datetime = "2017-10-09 02:20";
		String today = datetime.split(" ")[0];
		String time = datetime.split(" ")[1];
		
		if (StringUtils.equals(today, date)){
			date = "";
		}
		
		String clockOnTime= today + " " + rule.getClockOnTime();
		String clockOffTime = today + " " + rule.getClockOffTime();
		String dateBegin = today + " " + rule.getClockOnStartTime();
		String dateEnd = today + " " + rule.getClockOffEndTime();
		
		data.put("clockOnTime", rule.getClockOnTime());
		data.put("clockOffTime", rule.getClockOffTime());
		
		if (StringUtils.isNotEmpty(date)){
			today = date;
		} else {
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
//		
		//请假信息
		String leaveMsg = "";
		LeaveRecord leaveRecord = leaveService.getLeaveRecordByMobileAndDate(mobile, today);
		if (leaveRecord != null){
			String start = leaveRecord.getStartDate().substring(leaveRecord.getStartDate().lastIndexOf("-") + 1).replace(" ", "日");
			String end = leaveRecord.getEndDate().substring(leaveRecord.getEndDate().lastIndexOf("-") + 1).replace(" ", "日");
			leaveMsg = "请假：" + start + "-" + end;
		}
		data.put("leaveMsg", leaveMsg);
		
		//date不为空, 查询指定日期的打卡信息 
		if (StringUtils.isNotEmpty(date)){
			List<Attendance> records = service.getRecordsByDateAndMobile(date, date, mobile);
			Attendance record = records.size() == 0 ? null : records.get(0);
			if (leaveRecord != null){
				data = message1(leaveRecord, record, date, clockOnTime, clockOffTime, rule, data);
			}			
			data.put("record", record);
			info.setData(data);
			return info;
		}
		
		//0上班卡  1下班卡  2更新 打卡  3不能打上班卡  4不能打下班卡  5全天请假了
		int type = 0;
		//查询当天的考勤记录
		List<Attendance> records = service.getRecordsByDateAndMobile(today, today, mobile);
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
		
		if (leaveRecord != null){
			String startDay = leaveRecord.getStartDate().split(" ")[0];
			String endDay = leaveRecord.getEndDate().split(" ")[0];
			//请假在同一天
			if (startDay.compareTo(endDay) == 0){
				if (!(leaveRecord.getStartDate().compareTo(clockOnTime) <= 0 && leaveRecord.getEndDate().compareTo(clockOffTime) >= 0)){
					if (rule.getRestFlag() == 0){
						String restStartTime = today + " " + rule.getRestStartTime();
						String restEndTime = today + " " + rule.getRestEndTime();
						//上午请假
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
							if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
								clockOnTime = restEndTime;
							}
							if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
						}
						//下午请假
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
							if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
								clockOffTime = restStartTime;
							}
							if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
						}
					} else {
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							clockOnTime = leaveRecord.getEndDate();
						}
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							clockOffTime = leaveRecord.getStartDate();
						}
					}
				}
			//请假开始时间为今天
			} else if (startDay.compareTo(today) == 0){
				//是否有休息时段
				if (rule.getRestFlag() == 0){
					String restStartTime = date + " " + rule.getRestStartTime();
					String restEndTime = date + " " + rule.getRestEndTime();
					//上午请假
					if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
						if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
							clockOnTime = leaveRecord.getEndDate();
						}
						if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
							clockOnTime = restEndTime;
						}
						if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
							clockOnTime = leaveRecord.getEndDate();
						}
					}
				} else {
					if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
						clockOnTime = leaveRecord.getEndDate();
					}
				}
			//请假结束时间为今天
			} else if (endDay.compareTo(today) == 0){
				//是否有休息时段
				if (rule.getRestFlag() == 0){
					String restStartTime = date + " " + rule.getRestStartTime();
					String restEndTime = date + " " + rule.getRestEndTime();
					//下午请假
					if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
						if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
							clockOffTime = leaveRecord.getStartDate();
						}
						if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
							clockOffTime = restStartTime;
						}
						if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
							clockOffTime = leaveRecord.getStartDate();
						}
					}
				} else {
					if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
						clockOffTime = leaveRecord.getStartDate();
					}
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
		info.setData(data);
		return info;
	}
	
	private Map<String, Object> message1(LeaveRecord leaveRecord, Attendance record, String date, String clockOnTime, String clockOffTime, AttendanceRule rule, Map<String, Object> data){
		//请假在同一天
		if (leaveRecord.getStartDate().contains(date) && leaveRecord.getEndDate().contains(date)){
			//全天请假,没有打卡记录
			if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0 && leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
				if (null == record){
					data.put("type", 5);
				}
			} else {
				if (record != null){
					//请半天假,有打卡记录且为不销假打卡
					if (StringUtils.equals(record.getRevokeType(), "0")){
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = date + " " + rule.getRestStartTime();
							String restEndTime = date + " " + rule.getRestEndTime();
							//上午请假
							if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
								if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
									clockOnTime = leaveRecord.getEndDate();
								}
								if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
									clockOnTime = restEndTime;
								}
								if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
									clockOnTime = leaveRecord.getEndDate();
								}
							}
							//下午请假
							else if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
								if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
									clockOffTime = leaveRecord.getStartDate();
								}
								if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
									clockOffTime = restStartTime;
								}
								if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
									clockOffTime = leaveRecord.getStartDate();
								}
							}
						} else {
							if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
								clockOnTime = leaveRecord.getEndDate();
							}
							else if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
								clockOffTime = leaveRecord.getStartDate();
							}
						}
					}
				} else {
					//是否有休息时段
					if (rule.getRestFlag() == 0){
						String restStartTime = date + " " + rule.getRestStartTime();
						String restEndTime = date + " " + rule.getRestEndTime();
						//上午请假
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
							if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
								clockOnTime = restEndTime;
							}
							if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
						}
						//下午请假
						else if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
							if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
								clockOffTime = restStartTime;
							}
							if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
						}
					} else {
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							clockOnTime = leaveRecord.getEndDate();
						}
						else if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							clockOffTime = leaveRecord.getStartDate();
						}
					}
				}
			}
		//请假开始时间为今天
		} else if (leaveRecord.getStartDate().contains(date)){
			//全天请假,没有打卡记录
			if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
				if (null == record){
					data.put("type", 5);
				}
			} else {
				if (record != null){
					//请半天假,有打卡记录且为不销假打卡
					if (StringUtils.equals(record.getRevokeType(), "0")){
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = date + " " + rule.getRestStartTime();
							String restEndTime = date + " " + rule.getRestEndTime();
							//上午请假
							if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
								if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
									clockOnTime = leaveRecord.getEndDate();
								}
								if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
									clockOnTime = restEndTime;
								}
								if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
									clockOnTime = leaveRecord.getEndDate();
								}
							}
						} else {
							if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
								clockOnTime = leaveRecord.getEndDate();
							}
						}
					}
				} else {
					//是否有休息时段
					if (rule.getRestFlag() == 0){
						String restStartTime = date + " " + rule.getRestStartTime();
						String restEndTime = date + " " + rule.getRestEndTime();
						//上午请假
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							if (leaveRecord.getEndDate().compareTo(restStartTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
							if (leaveRecord.getEndDate().compareTo(restStartTime) >= 0 && leaveRecord.getEndDate().compareTo(restEndTime) <= 0){
								clockOnTime = restEndTime;
							}
							if (leaveRecord.getEndDate().compareTo(restEndTime) > 0 && leaveRecord.getEndDate().compareTo(clockOffTime) < 0){
								clockOnTime = leaveRecord.getEndDate();
							}
						}
					} else {
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							clockOnTime = leaveRecord.getEndDate();
						}
					}
				}
			}
		//请假结束时间为今天
		} else if (leaveRecord.getEndDate().contains(date)){
			//全天请假,没有打卡记录
			if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
				if (null == record){
					data.put("type", 5);
				}
			} else {
				if (record != null){
					//请半天假,有打卡记录且为不销假打卡
					if (StringUtils.equals(record.getRevokeType(), "0")){
						//是否有休息时段
						if (rule.getRestFlag() == 0){
							String restStartTime = date + " " + rule.getRestStartTime();
							String restEndTime = date + " " + rule.getRestEndTime();
							//下午请假
							if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
								if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
									clockOffTime = leaveRecord.getStartDate();
								}
								if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
									clockOffTime = restStartTime;
								}
								if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
									clockOffTime = leaveRecord.getStartDate();
								}
							}
						} else {
							if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
								clockOffTime = leaveRecord.getStartDate();
							}
						}
					}
				} else {
					//是否有休息时段
					if (rule.getRestFlag() == 0){
						String restStartTime = date + " " + rule.getRestStartTime();
						String restEndTime = date + " " + rule.getRestEndTime();
						//下午请假
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							if (leaveRecord.getStartDate().compareTo(clockOnTime) > 0 && leaveRecord.getStartDate().compareTo(restStartTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
							if (leaveRecord.getStartDate().compareTo(restStartTime) >= 0 && leaveRecord.getStartDate().compareTo(restEndTime) <= 0){
								clockOffTime = restStartTime;
							}
							if (leaveRecord.getStartDate().compareTo(restEndTime) > 0 && leaveRecord.getStartDate().compareTo(clockOffTime) < 0){
								clockOffTime = leaveRecord.getStartDate();
							}
						}
					} else {
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							clockOffTime = leaveRecord.getStartDate();
						}
					}
				}
			}
		//今天在请假开始tian和结束时间之间
		} else if (date.compareTo(leaveRecord.getStartDate()) > 0 && date.compareTo(leaveRecord.getEndDate()) < 0){
			if (null == record){
				data.put("type", 5);
			}
		}
		
		data.put("clockOnTime", clockOnTime.split(" ")[1]);
		data.put("clockOffTime", clockOffTime.split(" ")[1]);
		return data;
	}
	
	@RequestMapping(value = "clock", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo clock(String mobile, String longitude, String latitude, String device, String location, boolean isOutClock, String remark, String revokeType,
			HttpServletRequest request, HttpServletResponse response){
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			logger.error("手机号或经纬度不能为空。");
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("手机号或经纬度不能为空。");
			return info;
		}
		
		try {
			AttendanceRule rule = ruleService.getRuleByMobile(mobile);
			if (null == rule){
				logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("用户  " + mobile + " 没有设置考勤规则，请先设置。");
				return info;
			}
			
			String datetime = DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_MINUTETIME);
			String today = datetime.split(" ")[0];
			String clockOnTime = today + " " + rule.getClockOnTime();
			String clockOffTime = today + " " + rule.getClockOffTime();
			//0:不销假正常上班打卡  1:销假打卡
			if (StringUtils.isEmpty(revokeType)){
				LeaveRecord leaveRecord = leaveService.getLeaveRecordByMobileAndDate(mobile, today);
				if (leaveRecord != null){
					//请假开始时间和结束时间在同一天
					if (leaveRecord.getStartDate().contains(today) && leaveRecord.getEndDate().contains(today)){
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0 && leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							logger.info(mobile + "今天请假了，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
							info.setMessage("今天请假了，要销假打卡吗？");
							return info;
						}
						//请假开始时间<=上班时间,则为上午请假,打卡时间<=请假结束时间时提示时候销假打卡
						//请假结束时间>=下班时间,则为下午请假,打卡时间>=请假开始时间时提示时候销假打卡
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0 && datetime.compareTo(leaveRecord.getEndDate()) <= 0
								|| leaveRecord.getEndDate().compareTo(clockOffTime) >= 0 && datetime.compareTo(leaveRecord.getStartDate()) >= 0){
							logger.info(mobile + "在请假的时间段内，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_TIME);
							info.setMessage("在请假的时间段内，要销假打卡吗？");
							return info;
						}
					} 
					else if (leaveRecord.getStartDate().contains(today)){
						if (leaveRecord.getStartDate().compareTo(clockOnTime) <= 0){
							logger.info(mobile + "今天请假了，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
							info.setMessage("今天请假了，要销假打卡吗？");
							return info;
						} else if (datetime.compareTo(leaveRecord.getStartDate()) >= 0){
							logger.info(mobile + "在请假的时间段内，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_TIME);
							info.setMessage("在请假的时间段内，要销假打卡吗？");
							return info;
						}
					} 
					else if (leaveRecord.getEndDate().contains(today)){
						if (leaveRecord.getEndDate().compareTo(clockOffTime) >= 0){
							logger.info(mobile + "今天请假了，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
							info.setMessage("今天请假了，要销假打卡吗？");
							return info;
						} else if (datetime.compareTo(leaveRecord.getEndDate()) <= 0){
							logger.info(mobile + "在请假的时间段内，要销假打卡吗？ ");
							info.setCode(IConstants.QT_IN_LEAVE_TIME);
							info.setMessage("在请假的时间段内，要销假打卡吗？");
							return info;
						}
					}
					else if (today.compareTo(leaveRecord.getStartDate()) > 0 && today.compareTo(leaveRecord.getEndDate()) < 0){
						logger.info(mobile + "今天请假了，要销假打卡吗？ ");
						info.setCode(IConstants.QT_IN_LEAVE_WHOLE_DAY);
						info.setMessage("今天请假了，要销假打卡吗？");
						return info;
					}
				}
			}
			
			//是否外勤打卡 是则不再刷新位置
			if (!isOutClock){
				//检查打卡的位置是否在设置范围内
				ResultInfo result = service.refreshLocation(mobile, longitude, latitude, rule);
				if (result.getCode() == IConstants.QT_N0T_IN_BOUNDS){
					logger.error("不在设置的打卡范围之内，请刷新位置。");
					return result;
				}
			}
			User user = userService.findByMobile(mobile);
			if (null == user){
				logger.error("用户  " + mobile + " 不存在，可能此手机号还没注册。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("用户  " + mobile + " 不存在，可能此手机号还没注册。");
				return info;
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
				logger.info("用户  " + mobile + " 打卡成功。");
				info.setMessage("用户  " + mobile + " 打卡成功。");
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
	public ResultInfo refreshLocation(String mobile, String longitude, String latitude, HttpServletRequest request, HttpServletResponse response){
		logger.debug("refreshLocation start...");
		ResultInfo info = new ResultInfo();
		if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
			info.setCode(IConstants.QT_CODE_ERROR);
			info.setMessage("经度或纬度不能为空。");
			return info;
		}
		
		try {
			AttendanceRule rule = ruleService.getRuleByMobile(mobile);
			if (null == rule){
				logger.error("用户 {" + mobile + "}没有设置考勤规则，请先设置。");
				info.setCode(IConstants.QT_CODE_ERROR);
				info.setMessage("用户  " + mobile + " 没有设置考勤规则，请先设置。");
				return info;
			}
			
			info = service.refreshLocation(mobile, longitude, latitude, rule);
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
			info.setCode(IConstants.QT_CODE_ERROR);
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
	
}
