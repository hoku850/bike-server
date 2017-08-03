package org.ccframe.subsys.core.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BigDecimalUtil;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.sdk.bike.utils.SaveLogUtil;
import org.ccframe.subsys.bike.domain.code.ChargeOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.PaymentTypeCodeEnum;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.service.AgentAppSearchService;
import org.ccframe.subsys.bike.service.ChargeOrderSearchService;
import org.ccframe.subsys.bike.service.ChargeOrderService;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.domain.code.ChargeOperateCodeEnum;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.MemberAccountLog;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.MemberAccountRowDto;
import org.ccframe.subsys.core.repository.MemberAccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAccountService extends BaseService<MemberAccount,java.lang.Integer, MemberAccountRepository>{

	@Transactional
	public void saveOrUpdateMemberAccount(MemberAccountRowDto memberAccountRowDto) {
		MemberAccount memberAccount = new MemberAccount();
		ChargeOperateCodeEnum chargeOperateCodeEnum = ChargeOperateCodeEnum.fromCode(memberAccountRowDto.getChargeOperateTypeCode());
		
		// 账户表
		BeanUtils.copyProperties(memberAccountRowDto, memberAccount);
		if (ChargeOperateCodeEnum.RECHARGE == chargeOperateCodeEnum) {
			memberAccount.setAccountValue(BigDecimalUtil.add(memberAccountRowDto.getAccountValue(), memberAccountRowDto.getChangeValue()).doubleValue());
		} else {
			memberAccount.setAccountValue(BigDecimalUtil.subtract(memberAccountRowDto.getAccountValue(), memberAccountRowDto.getChangeValue()).doubleValue());
		}
		SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount);
		
		// 账户日志表
		MemberAccountLog memberAccountLog = new MemberAccountLog();
		memberAccountLog.setUserId(memberAccountRowDto.getUserId());
		memberAccountLog.setOrgId(memberAccountRowDto.getOrgId());
		memberAccountLog.setMemberAccountId(memberAccountRowDto.getUserId());
		memberAccountLog.setPrevValue(memberAccountRowDto.getAccountValue());
		if (ChargeOperateCodeEnum.RECHARGE == chargeOperateCodeEnum) {
			memberAccountLog.setAfterValue(BigDecimalUtil.add(memberAccountRowDto.getAccountValue(), memberAccountRowDto.getChangeValue()).doubleValue());
			memberAccountLog.setChangeValue(memberAccountRowDto.getChangeValue());
		} else {
			memberAccountLog.setAfterValue(BigDecimalUtil.subtract(memberAccountRowDto.getAccountValue(), memberAccountRowDto.getChangeValue()).doubleValue());
			memberAccountLog.setChangeValue(0 - memberAccountRowDto.getChangeValue());
		}
		memberAccountLog.setSysTime(new Date());
		memberAccountLog.setReason(memberAccountRowDto.getReason());
		memberAccountLog.setOperationManId(memberAccountRowDto.getUserId());
		SpringContextHelper.getBean(MemberAccountLogService.class).save(memberAccountLog);
	}

	@Transactional(readOnly=true)
	public MemberAccountRowDto getMemberAccountRowDto(Integer memberAccountId) {
		MemberAccount memberAccount = SpringContextHelper.getBean(MemberAccountSearchService.class).getById(memberAccountId);
		// 用DTO传输
		MemberAccountRowDto memberAccountRowDto = new MemberAccountRowDto();
		BeanUtils.copyProperties(memberAccount, memberAccountRowDto);
		
		// 查询用户名称
		User user = SpringContextHelper.getBean(UserSearchService.class).getById(memberAccount.getUserId());
		memberAccountRowDto.setUserNm(user==null ? null : user.getUserNm());
		// 查询出机构名称
		Org org = SpringContextHelper.getBean(OrgService.class).getById(memberAccount.getOrgId());
		memberAccountRowDto.setOrgNm(org==null ? null : org.getOrgNm());
		return memberAccountRowDto;
	}
	
	/**
	 * @author zjm
	 */
	@Transactional
	public AppPageDto chargeAccount(Double chargeMoney, String payType) {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<MemberAccount> list1 = SpringContextHelper.getBean(MemberAccountSearchService.class)
				.findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.PRE_DEPOSIT.toCode());

		Double amount = 0.00;
		Double total = 0.00;
		if(list1 != null && list1.size()>0){
			
			//保存系统会员账户表
			MemberAccount memberAccount = list1.get(0);
			amount = memberAccount.getAccountValue();
			
			
			total = BigDecimalUtil.add(amount, chargeMoney);
			memberAccount.setAccountValue(total);
			SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount);
			
			//保存日志表
			Integer logId = SaveLogUtil.saveLog(memberAccount.getMemberAccountId(), amount, total, chargeMoney, "保存系统会员账户表");
			
			//保存充值订单表
			ChargeOrder chargeOrder = new ChargeOrder();
			Date nowDate = new Date();
			String chargeOrderNum = UtilDateTimeClient.convertDateTimeToString(nowDate, "yyMMddHHmmss") + (int)(Math.random() * 9000 + 1000);
			//SimpleDateFormat dateFormater = new SimpleDateFormat("yyMMddHHmmss");
			//String chargeOrderNum = dateFormater.format(nowDate) + (Math.random() * 9000 + 1000);
			chargeOrder.setChargeOrderNum(chargeOrderNum);
			chargeOrder.setUserId(user.getUserId());
			chargeOrder.setMemberAccountId(memberAccount.getMemberAccountId());
			chargeOrder.setMemberAccountLogId(logId);
			chargeOrder.setOrgId(user.getOrgId());
			chargeOrder.setPaymentTransactionalNumber(Global.PAYMENT_TRANSACTIONAL_NUMBER);
			chargeOrder.setChargeAmmount(chargeMoney);
			chargeOrder.setChargeOrderStatCode(ChargeOrderStatCodeEnum.CHARGE_SUCCESS.toCode());
			chargeOrder.setCreateTime(nowDate);
			chargeOrder.setChargeFinishTime(nowDate);

			PaymentTypeCodeEnum payTypeCode = PaymentTypeCodeEnum.fromCode(payType);
			switch (payTypeCode) {
			case ALIPAY:
				chargeOrder.setPaymentTypeCode(PaymentTypeCodeEnum.ALIPAY.toCode());
				break;
			case WECHAT:
				chargeOrder.setPaymentTypeCode(PaymentTypeCodeEnum.WECHAT.toCode());
				break;

			default:
				break;
			}
			
			SpringContextHelper.getBean(ChargeOrderService.class).save(chargeOrder);
		}
		
		AppPageDto appPageDto = new AppPageDto();

		appPageDto.setAmount(total+"");
		appPageDto.setResult(Global.SPRING_MVC_JSON_SUCCESS);
		
		return appPageDto;
	}
	
	/**
	 * @author zjm
	 */
	@Transactional
	public AppPageDto returnDeposit() {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<MemberAccount> list1 = SpringContextHelper.getBean(MemberAccountSearchService.class)
				.findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.DEPOSIT.toCode());

		Double deposit = 0.00;
		if(list1 != null && list1.size()>0){
			
			//更新账户表
			MemberAccount memberAccount = list1.get(0);
			deposit = memberAccount.getAccountValue();
			
			memberAccount.setAccountValue(0.00);
			SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount);
			
			Integer logId = SaveLogUtil.saveLog(memberAccount.getMemberAccountId(), deposit, 0.00, deposit, "更新账户表");
			
			//更新充值订单表
			List<MemberAccount> list = SpringContextHelper.getBean(MemberAccountSearchService.class)
			.findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.DEPOSIT.toCode());
			
			if(list!=null && list.size()>0) {
				MemberAccount memberAccount2 = list.get(0);
				//根据账户id查询最新的押金充值记录
				List<ChargeOrder> list2 = SpringContextHelper.getBean(ChargeOrderSearchService.class)
						.findByKey(ChargeOrder.MEMBER_ACCOUNT_ID, memberAccount2.getMemberAccountId(), new Order(Direction.DESC, ChargeOrder.CHARGE_FINISH_TIME));

				if(list2!=null && list2.size()>0) {
					ChargeOrder chargeOrder = list2.get(0);
					chargeOrder.setChargeOrderStatCode(ChargeOrderStatCodeEnum.REFUND_SUCCESS.toCode());
					chargeOrder.setMemberAccountLogId(logId);
					chargeOrder.setRefundFinishTime(new Date());
					SpringContextHelper.getBean(ChargeOrderService.class).save(chargeOrder);
				}

			}
		}
		
		AppPageDto appPageDto = new AppPageDto();
		appPageDto.setResult(Global.SPRING_MVC_JSON_SUCCESS);
		appPageDto.setDeposit(deposit+"");
		
		return appPageDto;
	}
	
	/**
	 * @author zjm
	 */
	@Transactional
	public AppPageDto chargeDeposit(String payType) {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		List<MemberAccount> list1 = SpringContextHelper.getBean(MemberAccountSearchService.class)
				.findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.DEPOSIT.toCode());

		List<AgentApp> list = SpringContextHelper.getBean(AgentAppSearchService.class).findByKey(AgentApp.ORG_ID, user.getOrgId());
		Double deposit = list.get(0).getChargeDeposit();
		MemberAccount memberAccount = null;
		if(list1 != null && list1.size()>0){
			memberAccount = list1.get(0);
			memberAccount.setAccountValue(deposit);
			SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount);
			
			Integer logId = SaveLogUtil.saveLog(memberAccount.getMemberAccountId(), 0.00, deposit, deposit, "充值押金");
			
			//保存充值订单表
			ChargeOrder chargeOrder = new ChargeOrder();
			
			Date nowDate = new Date();
			String chargeOrderNum = UtilDateTimeClient.convertDateTimeToString(nowDate, "yyMMddHHmmss") + (int)(Math.random() * 9000 + 1000);
			//SimpleDateFormat dateFormater = new SimpleDateFormat("yyMMddHHmmss");
			//String chargeOrderNum = dateFormater.format(nowDate) + (Math.random() * 9000 + 1000);
			chargeOrder.setChargeOrderNum(chargeOrderNum);
			chargeOrder.setChargeOrderNum(chargeOrderNum);
			chargeOrder.setUserId(user.getUserId());
			chargeOrder.setMemberAccountId(memberAccount.getMemberAccountId());
			chargeOrder.setMemberAccountLogId(logId);
			chargeOrder.setOrgId(user.getOrgId());
			chargeOrder.setPaymentTransactionalNumber(Global.PAYMENT_TRANSACTIONAL_NUMBER);
			chargeOrder.setChargeAmmount(deposit);
			chargeOrder.setChargeOrderStatCode(ChargeOrderStatCodeEnum.CHARGE_SUCCESS.toCode());
			chargeOrder.setCreateTime(nowDate);
			chargeOrder.setChargeFinishTime(nowDate);
			
			PaymentTypeCodeEnum payTypeCode = PaymentTypeCodeEnum.fromCode(payType);
			switch (payTypeCode) {
			case ALIPAY:
				chargeOrder.setPaymentTypeCode(PaymentTypeCodeEnum.ALIPAY.toCode());
				break;
			case WECHAT:
				chargeOrder.setPaymentTypeCode(PaymentTypeCodeEnum.WECHAT.toCode());
				break;

			default:
				break;
			}
			
			SpringContextHelper.getBean(ChargeOrderService.class).save(chargeOrder);
		}
	
		AppPageDto appPageDto = new AppPageDto();
		appPageDto.setDeposit(deposit+"");
		
		return appPageDto;
	}
}
