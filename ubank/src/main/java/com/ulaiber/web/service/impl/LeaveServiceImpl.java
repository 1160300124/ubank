package com.ulaiber.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.model.AuditData;
import com.ulaiber.web.model.AuditVO;
import com.ulaiber.web.model.LeaveAudit;
import com.ulaiber.web.model.LeaveRecord;
import com.ulaiber.web.model.Remedy;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.LeaveService;
import com.ulaiber.web.utils.PushtoSingle;
import com.ulaiber.web.utils.StringUtil;

/**
 * 申请请假业务实现类
 * Created by daiqingwen on 2017/8/21.
 */
@Service
public class LeaveServiceImpl extends BaseService implements LeaveService{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private LeaveDao leaveDao;

    @Resource
    private LeaveAuditDao leaveAuditDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int saveLeaveRecord(LeaveRecord leaveRecord) {
    	//初始请假时长和实际请假时长一致，待有销假时实际请假时长=请假时长-销假时长
    	leaveRecord.setRealLeaveTime(leaveRecord.getLeaveTime());
    	//新增请假记录
        int result = leaveDao.saveLeaveRecord(leaveRecord);
        if(result == 0){
            return result;
        }
        //新增审批人记录
        List<Map<String,Object>> list = new ArrayList<>();
        String[] auditor = leaveRecord.getAuditor().split(",");
        for (int i = 0; i < auditor.length; i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",leaveRecord.getUserid());
            map.put("recordNo",String.valueOf(leaveRecord.getId()));
            map.put("auditor",auditor[i]);
            map.put("auditDate",sdf.format(new Date()));
            map.put("sort",i+1);
            list.add(map);
        }
        //获取第一个审批人ID
        int result2 = leaveAuditDao.saveAditor(list);
        int userid = Integer.parseInt(auditor[0]);
        String reason = leaveRecord.getReason(); //原因/备注
        String mark = "0";  //申请类型
        Map<String,Object> map2 = leaveAuditDao.queryCIDByUserid(userid);  //查询用户个推CID
        StringUtil.sendMessage(map2,reason,mark); //消息推送
        return result2;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int saveAditor(LeaveRecord leaveRecord) {
        List<Map<String,Object>> list = new ArrayList<>();
        String[] auditor = leaveRecord.getAuditor().split(",");
        for (int i = 0; i < auditor.length; i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",leaveRecord.getUserid());
            map.put("recordNo",String.valueOf(leaveRecord.getId()));
            map.put("auditor",auditor[i]);
            map.put("auditDate",sdf.format(new Date()));
            map.put("sort",i+1);
            list.add(map);
        }
        //获取第一个审批人ID
        int result = leaveAuditDao.saveAditor(list);
        int userid = Integer.parseInt(auditor[0]);
        String reason = leaveRecord.getReason(); //原因/备注
        String mark = "0";  //申请类型
        Map<String,Object> map2 = leaveAuditDao.queryCIDByUserid(userid);  //查询用户个推CID
        StringUtil.sendMessage(map2,reason,mark); //消息推送
        return result;
    }




    @Override
    public List<ApplyForVO> queryApplyRecord(String userId,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("userid",userId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        List<ApplyForVO> list = leaveDao.queryApplyRecord(map);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int cancelApply(String applyId) {
        int result = leaveDao.cancelApply(applyId);
        int result2 = leaveDao.cancelApplyAudit(applyId);
        return result2;
    }

    @Override
    public List<LeaveAudit> queryAuditor(int[] ids) {
        return leaveAuditDao.queryAuditor(ids);
    }

    @Override
    public List<Map<String,Object>> getLeaveRecord(String userId) {
        return leaveDao.getLeaveRecord(userId);
    }

    @Override
    public List<AuditVO> getLeaveAuditor(String userId) {
        return leaveAuditDao.getLeaveAuditor(userId);
    }

    @Override
    public List<Map<String, Object>> getPendingRecord(String userId) {
        return leaveDao.getPendingRecord(userId);
    }

    @Override
    public List<Map<String, Object>> getAlreadyRecord(String userId) {
        return leaveDao.getAlreadyRecord(userId);
    }

    @Override
    public ApplyForVO queryPeningRecord(int id,String userId) {
        Map<String , Object> map = new HashMap<>();
        map.put("id" , id);
        map.put("userId" , userId);
        return leaveDao.queryPeningRecord(map);
    }

    @Override
    public List<ApplyForVO> queryAlreadRecord(int[] ids) {
//        Map<String , Object> map = new HashMap<>();
//        map.put("id" , id);
//        map.put("userId" , userId);
        return leaveDao.queryAlreadRecord(ids);
    }

    @Override
    public List<LeaveAudit> getAuditorByUserId( String userId,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId" ,userId);
        map.put("pageNum" ,pageNum);
        map.put("pageSize" ,pageSize);
        return leaveAuditDao.getAuditorByUserId(map);
    }

    @Override
    public List<LeaveAudit> getAuditorBySort(String recordNo, int sortValue) {
        Map<String,Object> map = new HashMap<>();
        map.put("recordNo" , recordNo);
        map.put("sort" , sortValue);
        return leaveAuditDao.getAuditorBySort(map);
    }

    @Override
    public List<AuditVO> queryAuditorByRecord(String recordNo) {
        return leaveAuditDao.queryAuditorByRecord(recordNo);
    }

    @Override
    public List<LeaveAudit> queryAuditorByUserId(String userId,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId" ,userId);
        map.put("pageNum" ,pageNum);
        map.put("pageSize" ,pageSize);
        return leaveAuditDao.queryAuditorByUserId(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int confirmAudit(AuditData auditData) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId" , auditData.getUserId());
        map.put("recordNo" , auditData.getRecordNo());
        map.put("status" , auditData.getAuditorStatus());
        map.put("reason",auditData.getReason());
        map.put("date",sdf.format(new Date()));
        int result = leaveAuditDao.confirmAudit(map);
        //获取当前被审批记录的用户ID
        Map<String,Object> param = new HashMap<>();
        param.put("userId",auditData.getUserId());
        param.put("recordNo",auditData.getRecordNo());
        String resultMap = leaveAuditDao.queryUserIdByRecordNo(param);
        int userid = Integer.parseInt(resultMap);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //根据多个id获取CID
        if(auditData.getCurrentUserId().equals("")){
            int[] ids = new int[1];
            ids[0] = userid;
            list = leaveAuditDao.queryCIDByIds(ids);
        }else{
            int[] ids = new int[2];
            ids[0] = userid;
            ids[1] = Integer.parseInt(auditData.getCurrentUserId());
            list = leaveAuditDao.queryCIDByIds(ids);
        }
        String title = "";
        String content = "";
        String cid = "";
        String name = "";
        Integer user_id = 0;
        long id = 0; //内容ID，如果不需要默认为0
        String status = ""; //状态 0 失败，1 成功
        int type = 0;
        for (int i = 0 ; i < list.size() ; i++){
            Map<String,Object> map2 = list.get(i);
            user_id = Integer.parseInt(map2.get("user_id").toString());
            cid = (String) map2.get("CID");
            name = (String) map2.get("user_name");
            if(!StringUtil.isEmpty(cid)){
                type = IConstants.ALREADY;
                if(user_id == userid ){ //推送给申请记录所属用户
                    title = "您有一条已审批的记录";
                    content = name + "你好，您有一条申请已回复，理由是：" + auditData.getReason();
                    try {
                        PushtoSingle.singlePush(cid,type,content,title,id,status);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(user_id == Integer.parseInt(auditData.getCurrentUserId())){  //推送给下一个审批人
                    if(!auditData.getAuditorStatus().equals("2")){
                        type = IConstants.PENGDING;
                        title = "您有一条待审批的记录";
                        content = name + "你好，您有一个条待审批的记录需要审批，请查看";
                        try {
                            PushtoSingle.singlePush(cid,type,content,title,id,status);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

        return result;
    }

    @Override
    public int updateRecord(AuditData auditData) {
        Map<String,Object> map = new HashMap<>();
        map.put("recordNo" , auditData.getRecordNo());
        map.put("status" , auditData.getStatus());
        map.put("currentAuditor" , auditData.getCurrentAuditor());
        map.put("date",sdf.format(new Date()));
        return leaveDao.updateRecord(map);
    }

    @Override
    public Map<String,Object> queryApplyRecordById(int id) {
        return leaveDao.queryApplyRecordById(id);
    }

    @Override
    public int getUserTotalByDate(String date, String companyNumber) {
        Map<String,Object> map = new HashMap<>();
        map.put("date" , date);
        map.put("companyNumber" , companyNumber);
        return leaveDao.getUserTotalByDate(map);
    }

    @Override
    public List<User> getUserByDate(String date, String companyNumber, int pageNum, int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("date" , date);
        map.put("companyNumber" , companyNumber);
        map.put("pageNum" , pageNum);
        map.put("pageSize" , pageSize);
        return leaveDao.getUserByDate(map);
    }

    @Override
    public int updateUser(String userId, String CID) {
        Map<String,Object > map = new HashMap<>();
        map.put("userid" , userId);
        map.put("CID" , CID);
        return leaveDao.updateUser(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int insertRemedy(Remedy remedy) {
        LeaveRecord leaveRecord = new LeaveRecord();
        leaveRecord.setAuditor(remedy.getAuditor());
        leaveRecord.setUserid(remedy.getUserId());
        leaveRecord.setReason(remedy.getReason());
        leaveRecord.setCreateDate(sdf.format(new Date()));
        //新增申请记录
        int result = leaveDao.insertRemedyRecord(leaveRecord);
        if(result <= 0 ){
            return 0;
        }
        //新增审批人
        List<Map<String,Object>> list = new ArrayList<>();
        String[] auditor = leaveRecord.getAuditor().split(",");
        for (int i = 0; i < auditor.length; i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",leaveRecord.getUserid());
            map.put("recordNo",String.valueOf(leaveRecord.getId()));
            map.put("auditor",auditor[i]);
            map.put("auditDate",sdf.format(new Date()));
            map.put("sort",i+1);
            list.add(map);
        }
        int userid = Integer.parseInt(auditor[0]);
        String reason = leaveRecord.getReason(); //原因/备注
        String mark = "0";  //申请类型
        Map<String,Object> map2 = leaveAuditDao.queryCIDByUserid(userid);  //查询用户个推CID
        StringUtil.sendMessage(map2,reason,mark);  //消息推送
        //新增审批人
        int result2 = leaveAuditDao.saveAditor(list);
        if (result2 <= 0){
            return 0;
        }
        remedy.setRecordNo(leaveRecord.getId());
        //新增补卡信息
        int result3 = leaveDao.addRemedy(remedy);
        return result3;
    }

    @Override
    public Remedy getRemedyRecordByUserId(int recordNo) {
        return leaveDao.getRemedyRecordByUserId(recordNo);
    }

	@Override
	public List<Map<String, Object>> getTotalTimeByCompanyNumAndMonth(String companyNum, String type, String month) {
		return leaveDao.getTotalTimeByCompanyNumAndMonth(companyNum, type, month);
	}

    @Override
    public LeaveRecord queryApplyStatus(String recordNo) {
        int id = Integer.parseInt(recordNo);
        return leaveDao.queryApplyStatus(id);
    }

    @Override
    public int getLeaveRecordCount(long userId) {
        return leaveDao.getLeaveRecordCount(userId);
    }

    @Override
    public int getLeaveAuditorCount(long userId) {
        return leaveDao.getLeaveAuditorCount(userId);
    }

    @Override
    public List<User> getDeleteUserByDate(String date, String companyNumber, int pageNum, int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("date" , date);
        map.put("companyNumber" , companyNumber);
        map.put("pageNum" , pageNum);
        map.put("pageSize" , pageSize);
        return leaveDao.getDeleteUserByDate(map);
    }

	@Override
	public List<LeaveRecord> getLeaveRecordByUserIdAndDate(long userId, String date) {
		return leaveDao.getLeaveRecordByUserIdAndDate(userId, date);
	}

	@Override
	public List<LeaveRecord> getLeaveRecordByUserIdAndDate2(long userId, String startDate, String endDate) {
		return leaveDao.getLeaveRecordByUserIdAndDate2(userId, startDate, endDate);
	}

}
