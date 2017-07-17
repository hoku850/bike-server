package org.ccframe.sdk.bike.controller;

import java.util.HashMap;
import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.service.MemberAccountSearchService;
import org.ccframe.subsys.core.service.MemberAccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_CHARGEAMOUNT_BASE)
public class ChargeAmountController {
	
	@RequestMapping(value = "getChargeAmount")
	@ResponseBody
	public Map<String, String> getChargeAmount() {
		
		return SpringContextHelper.getBean(MemberAccountSearchService.class).getChargeAmount();
	}
	
	@RequestMapping(value = "getDeposit")
	@ResponseBody
	public Map<String, Object> getDeposit() {

		Double deposit = 99.00;
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("deposit", deposit);
		
		return map;
	}
	
	@RequestMapping(value = "chargeAccount")
	@ResponseBody
	public Map<String, Object> chargeAccount(Double chargeMoney, String payType) {
		
		return SpringContextHelper.getBean(MemberAccountService.class).chargeAccount(chargeMoney, payType);
	}
	
	@RequestMapping(value = "returnDeposit")
	@ResponseBody
	public Map<String, Object> returnDeposit() {
		
		return SpringContextHelper.getBean(MemberAccountService.class).returnDeposit();
	}
	
	@RequestMapping(value = "chargeDeposit")
	@ResponseBody
	public Map<String, Object> chargeDeposit(String payType) {
		
		return SpringContextHelper.getBean(MemberAccountService.class).chargeDeposit(payType);
	}

}
