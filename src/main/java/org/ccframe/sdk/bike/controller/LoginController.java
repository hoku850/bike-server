package org.ccframe.sdk.bike.controller;

import javax.servlet.http.HttpServletRequest;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.utils.ValidateCodeUtil;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_LOGIN_BASE)
public class LoginController {

	@RequestMapping(value = "nofilter/login")
	@ResponseBody
	public String login(String phoneName, String IMEI, String validateCode, Integer orgId) {
		
		orgId = 1;
		SpringContextHelper.getBean(UserService.class).login(phoneName, IMEI, validateCode, orgId);
		return "success";
	}

	@RequestMapping(value = "checkState")
	@ResponseBody
	public String checkState(HttpServletRequest httpRequest) {
		return SpringContextHelper.getBean(UserService.class).checkState(httpRequest);
	}
	
	@RequestMapping(value = "nofilter/validateCode")
	@ResponseBody
	public String getValidateCode(Integer loginId) {
		System.out.println(loginId);
		String validateCode = ValidateCodeUtil.putValidateCode(loginId);
		System.out.println(validateCode);
		return validateCode;
	}
	
}
