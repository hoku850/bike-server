package org.ccframe.sdk.bike.controller;

import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_ORDER_PAY_BASE)
public class OrderPayController {
	// 测试用，正式使用设置debug为false
	final boolean DEBUG = true;

	@RequestMapping(value = "getPayData")
	@ResponseBody
	public Map<String, String> getPayData(Integer orderId) {

		User user = (User) WebContextHolder.getSessionContextStore()
				.getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		if (DEBUG) {
			if(user == null || user.getUserId() == null){
				user = new User();
				user.setUserId(50002);
			}
				return SpringContextHelper.getBean(
						CyclingOrderSearchService.class).getOrderPayDetail(
						user);

		} else {
			return SpringContextHelper.getBean(CyclingOrderSearchService.class)
					.getOrderPayDetail(user);
		}

	}

	@RequestMapping(value = "orderPaySubmit")
	@ResponseBody
	public String orderPaySubmit(Integer orderId) {
		User user;
		if (DEBUG) {

			user = (User) WebContextHolder.getSessionContextStore()
					.getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
			if (user == null || user.getUserId() == null) {
				user = new User();
				user.setUserId(50002);
			}

		} else {
			user = (User) WebContextHolder.getSessionContextStore()
					.getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		}

		return SpringContextHelper.getBean(CyclingOrderService.class).orderPay(
				orderId, user);

	}

}
