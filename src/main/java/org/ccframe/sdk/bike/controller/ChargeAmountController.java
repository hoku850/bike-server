package org.ccframe.sdk.bike.controller;

import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.dto.AppPageDto;
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
	public AppPageDto getChargeAmount() {
		/*AppPageDto appPageDto = new AppPageDto();
		String amount, deposit, ifChargeDeposit;
		SpringContextHelper.getBean(MemberAccountSearchService.class).getChargeAmount(amount, deposit, ifChargeDeposit);
		appPageDto.setAmount(amount);
		appPageDto.setDeposit(deposit);
		appPageDto.setIfChargeDeposit(ifChargeDeposit);
		return appPageDto;*/
		return SpringContextHelper.getBean(MemberAccountSearchService.class).getChargeAmount();
	}
	
	@RequestMapping(value = ControllerMapping.GET_DEPOSIT)
	@ResponseBody
	public AppPageDto getDeposit() {

		return SpringContextHelper.getBean(AgentAppService.class).getDeposit();
	}
	
	@RequestMapping(value = ControllerMapping.CHARGE_ACCOUNT)
	@ResponseBody
	public AppPageDto chargeAccount(Double chargeMoney, String payType) {
		
		return SpringContextHelper.getBean(MemberAccountService.class).chargeAccount(chargeMoney, payType);
	}
	
	@RequestMapping(value = ControllerMapping.RETURN_DEPOSIT)
	@ResponseBody
	public AppPageDto returnDeposit() {
		
		return SpringContextHelper.getBean(MemberAccountService.class).returnDeposit();
	}
	
	@RequestMapping(value = ControllerMapping.CHARGE_DEPOSIT)
	@ResponseBody
	public AppPageDto chargeDeposit(String payType) {
		
		return SpringContextHelper.getBean(MemberAccountService.class).chargeDeposit(payType);
	}

}
