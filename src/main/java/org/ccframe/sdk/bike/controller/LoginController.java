package org.ccframe.sdk.bike.controller;

import javax.servlet.http.HttpServletRequest;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.utils.AppConstant;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_LOGIN_BASE)
public class LoginController {

	@RequestMapping(value = ControllerMapping.NOFILTER_LOGIN)
	@ResponseBody
	public String login(String phoneNum, String IMEI, String validateCode, Integer orgId) {
//		System.out.println(UserService.Validate(phoneNum, validateCode));
		switch (UserService.Validate(phoneNum, validateCode)) {
		case PASS:
			SpringContextHelper.getBean(UserService.class).login(phoneNum, IMEI, validateCode, orgId);
			return AppConstant.SUCCESS;		
		case TIMEOUT:
			return "timeout";
		default:
			System.out.println(AppConstant.FAIL);
			return AppConstant.FAIL;
		}	
	}

	@RequestMapping(value = ControllerMapping.CHECK_STATE)
	@ResponseBody
	public String checkState(HttpServletRequest httpRequest) {
		return SpringContextHelper.getBean(UserService.class).checkState(httpRequest);
	}
	
	@RequestMapping(value = ControllerMapping.NOFILTER_VALIDATECODE)
	@ResponseBody
	public String getValidateCode(String phoneNum) {
		System.out.println(phoneNum);
		String validateCode = UserService.getValidateCode(phoneNum);
		System.out.println(validateCode);
		return validateCode;
	}
	
}
