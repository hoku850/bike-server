package org.ccframe.sdk.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_LOGIN_BASE)
public class LoginController{
	
	@RequestMapping(value = "login")
	@ResponseBody
	public String login(String phoneName) {
		SpringContextHelper.getBean(UserService.class).login(phoneName);
		return "success";
	}
	
}
