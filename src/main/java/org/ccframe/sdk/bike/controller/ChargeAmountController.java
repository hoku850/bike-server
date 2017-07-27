package org.ccframe.sdk.bike.controller;

import java.util.HashMap;
import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.service.AgentAppService;
import org.ccframe.subsys.core.service.MemberAccountSearchService;
import org.ccframe.subsys.core.service.MemberAccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_CHARGEAMOUNT_BASE)
public class ChargeAmountController {
	
	@RequestMapping(value = ControllerMapping.GET_CHARGE_AMOUNT)
	@ResponseBody
	public Map<String, String> getChargeAmount() {
		
		return SpringContextHelper.getBean(MemberAccountSearchService.class).getChargeAmount();
	}
	
	@RequestMapping(value = ControllerMapping.GET_DEPOSIT)
	@ResponseBody
	public Map<String, Object> getDeposit() {

		return SpringContextHelper.getBean(AgentAppService.class).getDeposit();
	}
	
	@RequestMapping(value = ControllerMapping.CHARGE_ACCOUNT)
	@ResponseBody
	public Map<String, Object> chargeAccount(Double chargeMoney, String payType) {
		
		return SpringContextHelper.getBean(MemberAccountService.class).chargeAccount(chargeMoney, payType);
	}
	
	@RequestMapping(value = ControllerMapping.RETURN_DEPOSIT)
	@ResponseBody
	public Map<String, Object> returnDeposit() {
		
		return SpringContextHelper.getBean(MemberAccountService.class).returnDeposit();
	}
	
	@RequestMapping(value = ControllerMapping.CHARGE_DEPOSIT)
	@ResponseBody
	public Map<String, Object> chargeDeposit(String payType) {
		
		return SpringContextHelper.getBean(MemberAccountService.class).chargeDeposit(payType);
	}

}
