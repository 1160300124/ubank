package com.ulaiber.web.controller.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ulaiber.web.model.BankAccount;
import com.ulaiber.web.model.ShangHaiAcount.SHChangeCard;
import com.ulaiber.web.SHSecondAccount.SHChangeBinding;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;
import com.ulaiber.web.model.User;
import com.ulaiber.web.service.UserService;
import com.ulaiber.web.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ulaiber.web.conmon.IConstants;
import com.ulaiber.web.controller.BaseController;
import com.ulaiber.web.model.Bank;
import com.ulaiber.web.model.ResultInfo;
import com.ulaiber.web.service.BankService;

import java.util.List;

@Controller
@RequestMapping("/api/v1/")
public class BankController extends BaseController {
	
	/**
	 * 日志对象
	 */
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
	public ResultInfo queryAllBanks(String code){
		logger.debug("queryAllBanks start...");
		ResultInfo retInfo = new ResultInfo();
		//List<Bank> banks = bankservice.getAllBanks();
		//获取公司绑定的银行信息
		BankAccount bankAccount = bankservice.getBankByCode(code);
		if(StringUtil.isEmpty(bankAccount)){
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("公司没有绑定银行");
			logger.error(">>>>>>>>>>>>>>公司没有绑定银行");
			return retInfo;
		}
		//根据银行编号获取银行信息
		List<Bank> banks = bankservice.queryBanksByNumber(bankAccount.getBankNumber());
		if(StringUtil.isEmpty(banks)){
			retInfo.setCode(IConstants.QT_CODE_ERROR);
			retInfo.setMessage("查询失败");
			logger.error(">>>>>>>>>>查询银行失败");
		}
		retInfo.setCode(IConstants.QT_CODE_OK);
		retInfo.setData(banks);
		logger.debug("queryAllBanks end...");
		return retInfo;
	}

	/**
	 * 改绑（上海银行二类户）银行卡
	 * @param shCard
	 * @return ResultInfo
	 */
	@RequestMapping(value = "SHChangeBind", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo SHChangeBind(SHChangeCard shCard,User user){
		logger.info(">>>>>>>>>>开始改绑银行卡");
		ResultInfo resultInfo = new ResultInfo();
		//根据二类户账号ID和原银行卡号查询二类户信息
		long id = shCard.getId();
		SecondAcount sa = bankservice.querySecondAccount(id);
		shCard.setSubAcctNo(sa.getSubAcctNo());
		shCard.setProductCd(sa.getProductCd());
		shCard.setCustName(sa.getCustName());
		shCard.setIdNo(sa.getIdNo());
		shCard.setBindCardNo(sa.getBindCardNo());
		shCard.setReservedPhone(sa.getReservedPhone());
		shCard.setModiType("00");
		ResultInfo re = SHChangeBinding.changeBindCard(shCard);
		if(re.getCode() != 0000){
			resultInfo.setCode(Integer.parseInt(shCard.getStatusCode()));
			resultInfo.setMessage(shCard.getServerStatusCode());
			logger.info(">>>>>>>>>>>>改绑失败");
			return resultInfo;
		}
		SHChangeCard data = (SHChangeCard) re.getData();
		if(!data.getStatusCode().equals("0000")){
			resultInfo.setCode(Integer.parseInt(data.getStatusCode()));
			resultInfo.setMessage(data.getServerStatusCode());
			logger.info(">>>>>>>>>>>>改绑失败");
			return resultInfo;
		}

		String originCart = sa.getBindCardNo();  //原绑定卡号
		long userid = shCard.getUserid();	//用户ID
		long bankNo = Long.parseLong(user.getBankNo()); //银行卡编号
		String newCardNo = shCard.getNewCardNo();	//新银行卡
		//删除原绑定银行卡
		int result = bankservice.deleteOriginCart(originCart,userid,bankNo,newCardNo);
		if(result == 0){
			resultInfo.setCode(IConstants.QT_CODE_ERROR);
			resultInfo.setMessage("改绑失败");
			logger.error(">>>>>>>>>>>>改绑失败");
		}
		resultInfo.setCode(IConstants.QT_CODE_OK);
		resultInfo.setMessage("改绑成功");
		return resultInfo;
	}

}
