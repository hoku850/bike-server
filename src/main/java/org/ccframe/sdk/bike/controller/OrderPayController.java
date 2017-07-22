package org.ccframe.sdk.bike.controller;

import java.util.Date;
import java.util.Map;

import javax.jws.soap.SOAPBinding.Use;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BigDecimalUtil;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.MemberAccountLog;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.MemberAccountLogService;
import org.ccframe.subsys.core.service.MemberAccountService;
import org.ccframe.subsys.core.service.UserService;
import org.elasticsearch.monitor.jvm.JvmInfo.Mem;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.icu.math.BigDecimal;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping(ControllerMapping.MEMBER_ORDER_PAY_BASE)
public class OrderPayController {
	
	@RequestMapping(value = "getPayData")
	@ResponseBody
	public Map<String, String> getPayData(Integer orderId) {
//		System.out.println("test");
		User user = (User) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		return SpringContextHelper.getBean(CyclingOrderSearchService.class).getOrderPayDetail(user.getLoginId());		
		//测试用，正式使用时解除以上2注释并注释以下2语句
//		String loginId = "18813299774";
		
//		return SpringContextHelper.getBean(CyclingOrderSearchService.class).getOrderPayDetail(loginId);
	}
	
	@RequestMapping(value = "orderPaySubmit")
	@ResponseBody
	public String orderPaySubmit(Integer orderId) {
		String loginId= ((User) WebContextHolder.getSessionContextStore()
				.getServerValue(Global.SESSION_LOGIN_MEMBER_USER)).getLoginId();
		//测试用，正式使用时解除以上2注释并注释以下1语句
//		String loginId = "18813299774";
		
		
		return SpringContextHelper.getBean(CyclingOrderService.class).orderPay(orderId, loginId);
	}
	

}
