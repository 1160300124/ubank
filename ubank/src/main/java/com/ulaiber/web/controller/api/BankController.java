package com.ulaiber.web.controller.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.SHSecondAccount.SHQueryBalance;
import com.ulaiber.web.SHSecondAccount.SHTradingStatus;
import com.ulaiber.web.SHSecondAccount.SHWithdraw;
import com.ulaiber.web.model.*;
import com.ulaiber.web.model.ShangHaiAcount.SHChangeCard;
import com.ulaiber.web.SHSecondAccount.SHChangeBinding;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.model.ShangHaiAcount.Withdraw;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.service.BankService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/")
public class BankController extends BaseController {

	private static Logger logger = Logger.getLogger(BankController.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private BankService bankservice;

	@Resource
	private UserService userService;
	
	@RequestMapping(value = "getBankById", method = RequestMethod.GET)
	@ResponseBody
	public Bank getBankById(String bankNo, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isEmpty(bankNo)){
			return null;
		}
		return bankservice.getBankByBankNo(bankNo);
	}

	/**
	 * 根据邀请码，查询银行
	 * @param code 邀请码
	 * @return ResultInfo
	 */
	@RequestMapping(value = "queryAllBanks", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo queryAllBanks(String code,String userId){
		logger.info(">>>>>>>>>>开始查询银行信息");
		ResultInfo retInfo = new ResultInfo();
		try{
			if(!StringUtil.isEmpty(userId)){
				long userid = Long.parseLong(userId);
				//根据用户ID获取对应公司的邀请码
				String co = bankservice.getCodeByuserid(userid);
				code = co;
			}
			//获取公司绑定的银行信息]
			BankAccount bankAccount = bankservice.getBankByCode(code);
			if(StringUtil.isEmpty(bankAccount)){
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("公司没有绑定银行");
				logger.error(">>>>>>>>>>>>>>公司没有绑定银行");
				return retInfo;
			}
			//根据银行编号获取银行信息
			String bankNumber = bankAccount.getBankNumber();
			List<Bank> banks = bankservice.queryBanksByNumber(bankNumber);
			if(StringUtil.isEmpty(banks)){
				retInfo.setCode(IConstants.QT_CODE_ERROR);
				retInfo.setMessage("查询失败");
				logger.error(">>>>>>>>>>查询银行失败");
				return retInfo;
			}
			retInfo.setCode(IConstants.QT_CODE_OK);
			retInfo.setData(banks);
			logger.info(">>>>>>>>>>查询银行成功");
		}catch(Exception e){
			logger.error(">>>>>>>>>>查询银行信息失败：" ,e);
		}

		return retInfo;
	}

	/**
	 * 改绑银行卡
	 * @param shCard
	 * @return ResultInfo
	 */
	@RequestMapping(value = "ChangeBind", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo changeBind( SHChangeCard shCard ,Bank bank){
		logger.info(">>>>>>>>>>开始改绑银行卡");
		ResultInfo resultInfo = new ResultInfo();
		String status = "";
		try{
			if(bank.getType() == 0){  //上海银行二类户
				//根据上海二类户账号查询二类户信息
				//String SubAcctNo = shCard.getSubAcctNo();
				//SecondAcount sa = bankservice.querySecondAccount(SubAcctNo);
				shCard.setSubAcctNo(shCard.getSubAcctNo());
				shCard.setProductCd(shCard.getProductCd());
				shCard.setCustName(shCard.getCustName());
				shCard.setIdNo(shCard.getIdNo());
				shCard.setBindCardNo(shCard.getBindCardNo());
				shCard.setReservedPhone(shCard.getReservedPhone());
				shCard.setModiType(shCard.getModiType());
				ResultInfo re = SHChangeBinding.changeBindCard(shCard);
				Map<String,Object> resultMap = (Map<String, Object>) re.getData();
				status = (String) resultMap.get("status");
				SHChangeCard data = (SHChangeCard) resultMap.get("SHChangeCard");
				if(!data.getStatusCode().equals("0000")){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage(data.getServerStatusCode());
					resultInfo.setData(status);
					logger.info(">>>>>>>>>>>>改绑失败");
					return resultInfo;
				}
				String originCart = shCard.getBindCardNo();  //原绑定卡号
				long userid = shCard.getUserId();	//用户ID
				//long bankNo = Long.parseLong(user.getBankNo()); //银行卡编号
				String newCardNo = data.getNewCardNo();	//新银行卡
				logger.info(">>>>>>>>>新银行卡为:" + newCardNo);
				String modiType = shCard.getModiType(); //修改类型  00:换卡 01:修改绑定卡手机号
				Map<String,Object> map = new HashMap<>();
				map.put("originCart",originCart);
				map.put("userid",userid);
				map.put("bankNo",bank.getBankNo());
				map.put("type",bank.getType());
				map.put("newCardNo",newCardNo);
				map.put("modiType",modiType);
				map.put("newReserveMobile",data.getNewReservedPhone());
				//删除原绑定银行卡
				int result = bankservice.deleteOriginCart(map);
				if(result == 0){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage("改绑失败");
					logger.error(">>>>>>>>>>>>改绑失败");
					return resultInfo;
				}
				resultInfo.setCode(IConstants.QT_CODE_OK);
				resultInfo.setData(status);
				resultInfo.setMessage("改绑成功");
				logger.info(">>>>>>>>>>>>改绑成功");
			}

		}catch(Exception e){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("改绑失败");
			logger.error(">>>>>>>>改绑失败",e);
		}
		return resultInfo;
	}

	/**
	 * 查询二类户余额
	 * @param SubAcctNo 二类户账号
	 * @return
	 */
	@RequestMapping(value = "QueryBalance", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo queryBalance(String SubAcctNo,String type){
		logger.info(SubAcctNo +">>>>>>>>>>开始查询二类户余额");
		ResultInfo resultInfo = new ResultInfo();
		String status = "";
		try{
			if(type.equals("0")){  // 上海银行二类户
				ResultInfo result = SHQueryBalance.queryBalance(SubAcctNo);
				logger.info(">>>>>>>>>> 查询上海二类户余额结果为：" + result);
				logger.info(">>>>>>>>>>返回data为：" + result.getData());
				Map<String,Object> resultMap = (Map<String, Object>) result.getData();
				logger.info(">>>>>>>>>resultMap is :" + resultMap);
				SecondAcount sa = (SecondAcount) resultMap.get("secondAccount");
				sa.setSubAcctNo(SubAcctNo);
				status = (String) resultMap.get("status");
				Map<String,Object> map = new HashMap<>();
				if(!"0000".equals(status)){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage(sa.getServerStatusCode());
					map.put("status",status);
					map.put("secondAcount","");
					resultInfo.setData(map);
					logger.info(">>>>>>>>>>"+SubAcctNo + " 查询上海二类账户余额失败");
					return resultInfo;
				}
				//更新二类账户余额
				int resu = bankservice.updateSecondAcc(sa);
				if(resu == 0){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage("查询余额失败");
					map.put("status",status);
					map.put("secondAcount","");
					resultInfo.setData(map);
					logger.info(">>>>>>>>>>"+SubAcctNo + " 更新二类户余额失败");
					return resultInfo;
				}
				map.put("status",status);
				map.put("secondAcount",sa);
				resultInfo.setCode(IConstants.QT_CODE_OK);
				resultInfo.setMessage(sa.getServerStatusCode());
				resultInfo.setData(map);
				logger.info(">>>>>>>>>>"+SubAcctNo + " 查询上海二类账户余额成功");
			}

		}catch(Exception e){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("查询余额失败");
			logger.error(SubAcctNo + ">>>>>>>>>>查询余额异常",e);
		}
		return resultInfo;
	}

	/**
	 * 二类户提现
	 * @param wid 提现信息
	 * @return resultInfo
	 */
	@RequestMapping(value = "Withdraw", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo widthdraw( Withdraw wid){
		logger.info(">>>>>>>>>>开始提现操作，银行类型为：" + wid.getType());
		ResultInfo resultInfo = new ResultInfo();
		Map<String,Object> map = new HashMap<>();
		String status = "";
		if(wid.getType().equals("0")){
			try{
				//提现
				ResultInfo result = SHWithdraw.withdraw(wid);
				logger.info(">>>>>>>>>>上海银行提现结果为：" + result);
				Map<String,Object> resultMap = (Map<String, Object>) result.getData();
				logger.info(">>>>>>>>>resultMap is :" + resultMap);
				Withdraw withd = (Withdraw) resultMap.get("withdraw");
				status = (String) resultMap.get("status");
				if(!"0000".equals(status)){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage(withd.getServerStatusCode());
					resultInfo.setData(status);
					logger.info(">>>>>>>>>>上海银行二类户提现失败，银行卡号为"+withd.getBindCardNo());
					return resultInfo;
				}
				withd.setType(wid.getType());
				String SubAcctNo = withd.getSubAcctNo();
				double amount = withd.getAmount();
				//根据二类账户号查询账户余额
				SecondAcount sa = bankservice.queryAccount(SubAcctNo);
				double WorkingBal = sa.getWorkingBal();  //余额(子账户余额)
				double FundShare = sa.getFundShare();   //基金份额(以基金公司为准，不含当日申购赎回的交易份额)
				//先将余额扣除提现金额，如果大于0，则不用继续扣除份额的金额；如果小于=0，则继续扣除份额的金额
				WorkingBal = WorkingBal - amount;
				if(WorkingBal <= 0 ){
					FundShare = FundShare + WorkingBal;
				}
				sa.setAvaiBal(WorkingBal + FundShare);
				sa.setWorkingBal(WorkingBal);
				sa.setFundShare(FundShare);
				//更新二类账户的金额
				int upResult = bankservice.updateSecondAcc(sa);
				if(upResult == 0){
					resultInfo.setCode(IConstants.INSER_DB_ERROR);
					resultInfo.setMessage("提现失败");
					logger.error(">>>>>>>>>>更新二类账户信息失败");
					return resultInfo;
				}
				withd.setCreateDate(sdf.format(new Date()));
				withd.setUpdateTime(sdf.format(new Date()));
				withd.setStatus(0);
				withd.setTrading(0);
				withd.setUserId(wid.getUserId());
				//新增提现记录
				int inResult = bankservice.insertWithdraw(withd);
				if(inResult == 0){
					resultInfo.setCode(IConstants.INSER_DB_ERROR);
					resultInfo.setMessage("提现失败");
					logger.error(">>>>>>>>>>插入提现记录失败");
					return resultInfo;
				}
				resultInfo.setCode(IConstants.QT_CODE_OK);
				resultInfo.setMessage("提现成功");
				resultInfo.setData(status);
				logger.info(">>>>>>>>>>流水号为："+withd.getRqUID()+"的上海二类户提现成功");
			}catch(Exception e){
				resultInfo.setCode(IConstants.QT_CODE_ERROR);
				resultInfo.setMessage("提现失败");
				e.printStackTrace();
				logger.error(">>>>>>>>>>提现失败",e);
			}
		}

		return resultInfo;
	}

	/**
	 * 二类账户账单（交易状态）查询
	 * @param SubAcctNo 二类户账号
	 * @param type 二类户类型。 0 上海银行二类户
	 * @param pageNum 页码
	 * @param pageSize 页大小
	 * @return
	 */
	@RequestMapping(value = "TradingQuery", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo tradingQuery(String SubAcctNo,int userId,String type,int pageNum,int pageSize){
		logger.info(">>>>>>>>>>开始二类户交易状态查询");
		ResultInfo resultInfo = new ResultInfo();
		Map<String,Object> resMap = new HashMap<>();
		String status = "";
		if("0".equals(type)){  //上海银行二类户
            try {
                pageNum = (pageNum - 1) * pageSize;
                //根据二类账户查询账单
                Map<String,Object> map = new HashMap<>();
                map.put("SubAcctNo",SubAcctNo);
                map.put("pageNum",pageNum);
                map.put("pageSize",pageSize);
                List<Bill> wi = bankservice.queryWithdraw(map);
                if(wi.size() <= 0){
                    resultInfo.setCode(IConstants.QT_CODE_OK);
                    resultInfo.setMessage("暂无数据");
                    logger.info(">>>>>>>>>>类型为"+type+"的二类户交易状态查询结果为："+ wi.size());
                    return resultInfo;
                }
				//根据用户ID，获取用户CID
				//Map<String,Object> userMap = bankservice.queryCIDByUserid(userId);
                for (int i = 0 ; i < wi.size() ; i++){
					Bill wid = wi.get(i);
                    //如果当前交易记录处于"处理中"，则请求银行的交易状态查询接口
                    if(wid.getTradingStatus() == 0){
                        String RqUID = wid.getRqUID();
                        ResultInfo result = SHTradingStatus.tradingStatus(RqUID);
                        logger.info(">>>>>>>>>>上海银行二类户交易状态查询结果为：" + result);
                        Map<String,Object> resultMap = (Map<String, Object>) result.getData();
                        logger.info(">>>>>>>>>resultMap is :" + resultMap);
                        Map<String,Object> tradingMap = (Map<String, Object>) resultMap.get("tradingSta");
                        status = (String) resultMap.get("status");
                        if(!"0000".equals(status)){
                            resultInfo.setCode(IConstants.QT_CODE_ERROR);
                            resultInfo.setMessage("查询账单失败");
                            resultInfo.setData(status);
                            logger.info(">>>>>>>>>>上海银行二类户交易状态查询失败，二类户账号为"+ SubAcctNo);
                            return resultInfo;
                        }
                        //交易状态. I 处理中,F 交易失败,S 交易成功
                        String TxnStatus = (String) tradingMap.get("TxnStatus");
                        String OrirqUID = (String) tradingMap.get("OriRqUID");
                        logger.info(">>>>>>>>>>原交易流水号为：" + OrirqUID + ",传入的交易流水号为：" + RqUID);
						int tStatus = 0;
						if("I".equals(TxnStatus)){
							tStatus = 0;
						}else if("F".equals(TxnStatus)){
							tStatus = 2;
						}else if("S".equals(TxnStatus)){
							tStatus = 1;
						}
						//如果处理成功或失败，则推送信息给用户
                        //消息类型 0 提现，1 工资转入
//                        String mark = String.valueOf(wid.getTrading());
//                        if(tStatus != 0){
//							StringUtil.sendTradingMessage(userMap,mark,tStatus);
//						}
						String date = (String) tradingMap.get("TranDate"); //处理时间
                        //更新交易记录
                        int re = bankservice.updateWithdraw(OrirqUID,tStatus,date);
                        if(re == 0 ){
                            resultInfo.setCode(IConstants.QT_CODE_ERROR);
                            resultInfo.setMessage("查询账单失败");
                            resultInfo.setData(status);
                            logger.info(">>>>>>>>>>上海银行二类户交易状态查询失败，二类户账号为"+ SubAcctNo);
                            return resultInfo;
                        }
                        wid.setTradingStatus(tStatus);
                    }else{
                        //如果当前查询的数据都为处理完的数据，则不进行接口查询
                        status = "0000";
                    }
                }
//				JSONArray json = JSONArray.fromObject(wi);
//				System.out.println(">>>>>>>>>>>json："+ json.toString());
                resultInfo.setCode(IConstants.QT_CODE_OK);
                resultInfo.setMessage("查询账单成功");
                resMap.put("tradingRecord",wi);
                resMap.put("status",status);
                resultInfo.setData(resMap);
                logger.info(">>>>>>>>>>上海银行二类户交易状态查询成功，二类户账号为"+ SubAcctNo);
            }catch(Exception e){
                resultInfo.setCode(IConstants.QT_CODE_ERROR);
                resultInfo.setMessage("查询账单失败");
                logger.error(">>>>>>>>>>类型为"+type+"的二类户交易状态查询失败：" ,e);
            }
        }

		return resultInfo;
	}

	/**
	 * 二类户交易详情查询
	 * @param SubAcctNo 二类户账号
	 * @param trading 交易类型 0 提现 1 工资转入
	 * @return resultInfo
	 */
	@RequestMapping(value = "TradingDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo tradingDetail(String SubAcctNo,String RqUID,int trading){
		logger.info(">>>>>>>>>>>开始查询交易详情");
		ResultInfo resultInfo = new ResultInfo();
		try {
			if(trading == 0){  //提现
				//根据流水号查询交易详情
				BillDetail bd = bankservice.queryWithdrawByRqUID(RqUID);
				if(StringUtil.isEmpty(bd)){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage("暂无数据");
					logger.error(">>>>>>>>>>查询提现详情失败，流水号为：" + RqUID);
					return resultInfo;
				}
				resultInfo.setCode(IConstants.QT_CODE_OK);
				resultInfo.setMessage("查询成功");
				resultInfo.setData(bd);
			}else{ //工资转入
				//根据流水号查询工资转入详情
				BillDetail billDet = bankservice.querySalariesByRqUID(RqUID);
				if(StringUtil.isEmpty(billDet)){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage("暂无数据");
					logger.error(">>>>>>>>>>查询工资转入详情失败，流水号为：" + RqUID);
					return resultInfo;
				}
				resultInfo.setCode(IConstants.QT_CODE_OK);
				resultInfo.setMessage("查询成功");
				resultInfo.setData(billDet);
			}
		}catch(Exception e){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("查询失败");
			logger.error(">>>>>>>>>>>查询交易详情失败，流水号为："+ RqUID,e);
		}
		return resultInfo;
	}


}
