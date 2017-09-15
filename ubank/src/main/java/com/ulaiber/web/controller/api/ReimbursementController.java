package com.ulaiber.web.controller.api;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Reimbursement;
import com.ulaiber.web.model.ReimbursementVO;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.ReimbursementService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 报销controller
 * Created by daiqingwen on 2017/9/11.
 */
@Controller
@RequestMapping("/api/v1/")
public class ReimbursementController extends BaseController {

    public static final Logger logger = Logger.getLogger(ReimbursementController.class);

    @Resource
    private ReimbursementService reimbursementService;

    /**
     * 新增报销记录
     * @param  VO 报销详细信息
     * @return ResultInfo
     */
    @RequestMapping("insertReim")
    @ResponseBody
    public ResultInfo insertReimbursement(@RequestBody ReimbursementVO VO,
                             HttpServletRequest request, HttpServletResponse response){
        logger.info(">>>>>>>>>>>>>>开始保存报销记录");
//        List<Reimbursement> list = (List<Reimbursement>) VO .getData();
//        for (int i = 0 ; i < list.size() ; i++){
//            Reimbursement re = list.get(i);
//            byte[] by = (byte[]) re.getImage();
//        }
        int result = reimbursementService.insert(VO);
        ResultInfo resultInfo = new ResultInfo();
        if(result <= 0 ){
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增报销记录失败");
            logger.info(">>>>>>>>>>>>>>新增报销记录失败");
            return resultInfo;
        }else{
            resultInfo.setCode(IConstants.QT_CODE_ERROR);
            resultInfo.setMessage("新增报销记录成功");
            logger.info(">>>>>>>>>>>>>>新增报销记录成功");
            return resultInfo;
        }
    }

    /**
     * byte数组转换图片
     * @param data 字节流
     * @param path 图片路径
     */
    public void byteToImage(byte[] data , String path){
        if(data.length < 3 || path.equals("")){
            return ;
        }
        try {
            FileImageOutputStream fio = new FileImageOutputStream(new File(path));
            fio.write(data,0,data.length);
            fio.close();
            logger.info(">>>>>>>>>>>>>>转换图片成功");
            System.out.println("转换图片成功，图片路径在>>>>>>>>>>>" + path);
        } catch (IOException e) {
            logger.info(">>>>>>>>>>>>>转换图片Exception :" + e);
            e.printStackTrace();
        }

    }
}
