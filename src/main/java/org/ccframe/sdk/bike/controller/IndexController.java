package org.ccframe.sdk.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_INDEX_BASE)
public class IndexController {

	@RequestMapping(value = ControllerMapping.GET_BIKE_LOCATION)
	@ResponseBody
	public AppPageDto getBikeLocation(String position) {

		return SpringContextHelper.getBean(SmartLockStatService.class).getBikeLocation(position);
	}
	
}
