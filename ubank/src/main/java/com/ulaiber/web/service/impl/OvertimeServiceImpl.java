package com.ulaiber.web.service.impl;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.dao.LeaveAuditDao;
import com.ulaiber.web.dao.LeaveDao;
import com.ulaiber.web.dao.OvertimeDao;
import com.ulaiber.web.model.ApplyForVO;
import com.ulaiber.web.service.BaseService;
import com.ulaiber.web.service.OvertimeService;
import com.ulaiber.web.utils.PushtoSingle;
import com.ulaiber.web.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 加班业务层接口实现类
 * Created by daiqingwen on 2017/8/26.
 */
@Service
public class OvertimeServiceImpl extends BaseService implements OvertimeService{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private OvertimeDao overtimeDao;

    @Resource
    private LeaveAuditDao leaveAuditDao;

    @Resource
    private LeaveDao leaveDao;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
    public int addOvertimeRecord(ApplyForVO applyForVO) {
        //新增申请记录
        int result = overtimeDao.addRecord(applyForVO);
        applyForVO.setId(applyForVO.getId());
        //新增加班申请记录
        int result2 = overtimeDao.addOvertimeRecord(applyForVO);
        List<Map<String,Object>> list = new ArrayList<>();
        String[] auditor = applyForVO.getAuditor().split(",");
        for (int i = 0; i < auditor.length; i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",applyForVO.getUserid());
            map.put("recordNo",String.valueOf(applyForVO.getId()));
            map.put("auditor",auditor[i]);
            map.put("auditDate",sdf.format(new Date()));
            map.put("sort",i+1);
            list.add(map);
        }
        //获取第一个审批人ID
        int userid = Integer.parseInt(auditor[0]);
        int result3 = leaveAuditDao.saveAditor(list);
        //查询用户个推CID
        Map<String,Object> map2 = leaveAuditDao.queryCIDByUserid(userid);
        String cid  = (String) map2.get("CID");
        String name = (String) map2.get("user_name");
        int type = IConstants.PENGDING;
        //消息内容
        String content = name + "有一条加班申请需要您审批，原因是:" + applyForVO.getReason();
        String title = "您有一条加班申请待审批记录";
        if(!StringUtil.isEmpty(cid)){
            try {
                //推送审批信息致第一个审批人
                PushtoSingle.singlePush(cid,type,content,title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result3;
    }


}
