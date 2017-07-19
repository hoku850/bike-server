package org.ccframe.sdk.bike.controller;

import org.apache.http.HttpRequest;
import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_LOGIN_BASE)
public class LoginController{
	
	@RequestMapping(value = "login")
	@ResponseBody
	public String login(String phoneName, String IMEI) {

		SpringContextHelper.getBean(UserService.class).login(phoneName, IMEI);
		return "success";
	}
	
	@RequestMapping(value = "checkState")
	@ResponseBody
	public String checkState() {
		return SpringContextHelper.getBean(UserService.class).checkState();
	}
	
}
