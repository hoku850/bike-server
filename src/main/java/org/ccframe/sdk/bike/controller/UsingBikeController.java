package org.ccframe.sdk.bike.controller;

import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_USINGBIKE_BASE)
public class UsingBikeController{
	
	@RequestMapping(value = ControllerMapping.GET_USING_BIKE_DATA)
	@ResponseBody
	public Map<String, Object> getUsingBikeData() {
		return SpringContextHelper.getBean(CyclingOrderService.class).getUsingBikeData();
	}
	
	@RequestMapping(value = ControllerMapping.NEW_CYCLING_ORDER)
	@ResponseBody
	public Map<String, Object> newCyclingOrder(String nowPos) {
		return SpringContextHelper.getBean(CyclingOrderService.class).newCyclingOrder(nowPos);
	}
	
	@RequestMapping(value = ControllerMapping.SAVE_POSITION)
	@ResponseBody
	public String savePosition(String paths) {
		return SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).savePosition(paths);
	}
	
	@RequestMapping(value = ControllerMapping.CLOSE_LOCK)
	@ResponseBody
	public String closeLock(String paths) {
		return SpringContextHelper.getBean(CyclingOrderService.class).closeLock(paths);
	}
	
	@RequestMapping(value = "lunxun")
	@ResponseBody
	public String lunxun() {

		return SpringContextHelper.getBean(CyclingOrderService.class).lunxun();
	}
	
}
