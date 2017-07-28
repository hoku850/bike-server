package org.ccframe.sdk.bike.controller;

import java.util.Map;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_ORDER_PAY_BASE)
public class OrderPayController {
	// 测试用，正式使用设置debug为false
	final boolean DEBUG = false;

	@RequestMapping(value = ControllerMapping.GET_PAY_DATA)
	@ResponseBody
	public Map<String, String> getPayData() {

		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore()
				.getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		if (DEBUG) {
			if(user == null || user.getUserId() == null){
				user = new MemberUser();
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

	@RequestMapping(value = ControllerMapping.ORDER_PAY_SUBMIT)
	@ResponseBody
	public String orderPaySubmit(Integer orderId) {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore()
		.getServerValue(Global.SESSION_LOGIN_MEMBER_USER);;
		if (DEBUG) {
			if (user == null || user.getUserId() == null) {
				user = new MemberUser();
				user.setUserId(50002);
			}

		}
		return SpringContextHelper.getBean(CyclingOrderService.class).orderPay(
				orderId, user);

	}

}
