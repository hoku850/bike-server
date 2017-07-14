package org.ccframe.sdk.bike.controller;

import java.util.List;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.service.ChargeOrderService;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_TRADEDETAIL_BASE)
public class TradeDetailController{
	
	@RequestMapping(value = "getChargeDetail")
	@ResponseBody
	public List<ChargeOrder> getChargeDetail() {
		
		return SpringContextHelper.getBean(ChargeOrderService.class).getChargeDetail();
	}
	
	@RequestMapping(value = "getPayDetail")
	@ResponseBody
	public List<CyclingOrder> getPayDetail() {
		
		return SpringContextHelper.getBean(CyclingOrderService.class).getPayDetail();
	}
	
}
