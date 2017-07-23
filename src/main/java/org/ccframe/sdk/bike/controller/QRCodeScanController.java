package org.ccframe.sdk.bike.controller;

import javax.servlet.http.HttpServletResponse;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.QR_CODE_SCAN_BASE)
public class QRCodeScanController {

	@RequestMapping(value = Global.ID_BINDER_PATH)
	public void browserScan(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer articleInfId, HttpServletResponse response) {
		//查查锁是哪个运营商的
		SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, articleInfId);
//		response.sendRedirect();
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.POST) //APP扫码采用POST方式开锁
	public void appScan(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer articleInfId){
		
	}

}
