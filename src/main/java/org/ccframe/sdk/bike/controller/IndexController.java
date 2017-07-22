package org.ccframe.sdk.bike.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_INDEX_BASE)
public class IndexController {

	@RequestMapping(value = "getBikeLocation")
	@ResponseBody
	public Map<String, Object> getBikeLocation(String position) {

		return SpringContextHelper.getBean(SmartLockStatService.class).getBikeLocation(position);
	}
	
}
