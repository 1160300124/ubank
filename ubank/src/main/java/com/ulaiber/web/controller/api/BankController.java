package com.ulaiber.web.controller.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.SHSecondAccount.SHQueryBalance;
import com.ulaiber.web.SHSecondAccount.SHWithdraw;
import com.ulaiber.web.model.BankAccount;
import com.ulaiber.web.model.ShangHaiAcount.SHChangeCard;
import com.ulaiber.web.SHSecondAccount.SHChangeBinding;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.model.ShangHaiAcount.Withdraw;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Bank;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BankService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/")
public class BankController extends BaseController {

	private static Logger logger = Logger.getLogger(BankController.class);
	
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
			//获取公司绑定的银行信息
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
	 * @param SubAcctNo
	 * @return
	 */
	@RequestMapping(value = "QueryBalance", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo queryBalance(String SubAcctNo,String type){
		logger.info(SubAcctNo +">>>>>>>>>>开始查询上海二类户余额");
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
	public ResultInfo widthdraw(Withdraw wid){
		logger.info(">>>>>>>>>>开始提现操作，银行类型为：" + wid.getType());
		ResultInfo resultInfo = new ResultInfo();
		Map<String,Object> map = new HashMap<>();
		String status = "";
		if(wid.getType().equals("0")){
			try{
				//提现
				ResultInfo result = SHWithdraw.withdraw(wid);
				logger.info(">>>>>>>>>>上海银行提现结果为：" + result);
				logger.info(">>>>>>>>>>返回data为：" + result.getData());
				Map<String,Object> resultMap = (Map<String, Object>) result.getData();
				logger.info(">>>>>>>>>resultMap is :" + resultMap);
				Withdraw withd = (Withdraw) resultMap.get("withdraw");
				if(!"0000".equals(status)){
					resultInfo.setCode(IConstants.QT_CODE_ERROR);
					resultInfo.setMessage(withd.getServerStatusCode());
					map.put("status",status);
					map.put("withdraw","");
					resultInfo.setData(map);
					logger.info(">>>>>>>>>>上海银行二类户提现失败，银行卡号为"+withd.getBindCardNo());
					return resultInfo;
				}
				String SubAcctNo = withd.getSubAcctNo();
				//根据二类账户号查询账户余额
				SecondAcount sa = bankservice.queryAccount(SubAcctNo);
				double WorkingBal = 0.0;
				double FundShare = 0.0;

				//更新数据库的金额
				//bankservice.updateAccount();
			}catch(Exception e){
				resultInfo.setCode(IConstants.QT_CODE_ERROR);
				resultInfo.setMessage("提现失败");
				resultInfo.setData(status);
				e.printStackTrace();
				logger.error(">>>>>>>>>>提现失败");
			}
		}

		return resultInfo;
	}

}
