package org.ccframe.sdk.bike.controller;

import java.util.List;
import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_TRAVEL_BASE)
public class TravelController{
	
	@RequestMapping(value = ControllerMapping.GET_TRAVEL_LIST)
	@ResponseBody
	public List<CyclingOrder> getTravelList() {
		return SpringContextHelper.getBean(CyclingOrderService.class).getTravelList();
	}
	
	@RequestMapping(value = ControllerMapping.GET_TRAVEL_DETAIL)
	@ResponseBody
	public Map<String, Object> getTravelDetail(Integer cyclingOrderId) {
		return SpringContextHelper.getBean(CyclingOrderService.class).getTravelDetail(cyclingOrderId);
	}
}
