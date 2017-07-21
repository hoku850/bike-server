package org.ccframe.sdk.bike.controller;

import java.util.List;
import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_USINGBIKE_BASE)
public class UsingBikeController{
	
	@RequestMapping(value = "getUsingBikeData")
	@ResponseBody
	public Map<String, Object> getUsingBikeData(String meter) {
		return SpringContextHelper.getBean(CyclingOrderService.class).getUsingBikeData(meter);
	}
	
	@RequestMapping(value = "newCyclingOrder")
	@ResponseBody
	public Map<String, Object> newCyclingOrder(String startPos) {
		return SpringContextHelper.getBean(CyclingOrderService.class).newCyclingOrder(startPos);
	}
	
	@RequestMapping(value = "savePosition")
	@ResponseBody
	public String savePosition(String paths) {
		return SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).savePosition(paths);
	}
	
	@RequestMapping(value = "closeLock")
	@ResponseBody
	public String closeLock(String paths, String meter) {
		return SpringContextHelper.getBean(CyclingOrderService.class).closeLock(paths, meter);
	}
	

	
}
