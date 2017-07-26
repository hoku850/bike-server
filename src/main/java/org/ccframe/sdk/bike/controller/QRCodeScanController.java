package org.ccframe.sdk.bike.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.service.AgentAppService;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.socket.commons.SmartLockChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping(ControllerMapping.QR_CODE_SCAN_BASE)
public class QRCodeScanController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = Global.ID_BINDER_PATH)
	public void browserScan(@PathVariable(Global.ID_BINDER_ID) java.lang.Long lockerHardwareCode, HttpServletResponse response) {
		//测试用，转到appScan逻辑
		appScan(lockerHardwareCode);
		if(true)return;
		
		//查查锁是哪个运营商的
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, lockerHardwareCode);
		try {
			response.sendRedirect(SpringContextHelper.getBean(AgentAppService.class).getByKey(AgentApp.ORG_ID, smartLock.getOrgId()).getAndroidUrl());
		} catch (IOException e) {
			throw new BusinessException("", e);
		}
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.POST) //APP扫码采用POST方式开锁
	public String appScan(@PathVariable(Global.ID_BINDER_ID) java.lang.Long lockerHardwareCode){
		if(! SmartLockChannelUtil.isChannelActive(lockerHardwareCode)){ //锁不在线
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该单车出现故障，请更换一辆单车"});
		}
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, lockerHardwareCode);
		SmartLockStatCodeEnum smartLockStatCodeEnum = SmartLockStatCodeEnum.fromCode(smartLock.getSmartLockStatCode());
		//已发放或已激活才可骑行
		if(!( smartLockStatCodeEnum == SmartLockStatCodeEnum.GRANTED || smartLockStatCodeEnum == SmartLockStatCodeEnum.ACTIVED)){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该单车出现故障，请更换一辆单车"});
		}
		
		//如果是在骑行中，那么不能开锁
		//TODO 杰中补充完成，如果最后一个锁相关的订单是骑行中，那么不能关锁。throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该单车正在被人使用，请更换一辆单车"});
		SmartLock smartLock1 = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, lockerHardwareCode);
		List<CyclingOrder> listCyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).findBySmartLockIdOrderByStartTimeDesc(smartLock1.getSmartLockId());
		if(listCyclingOrder.size()>0&&listCyclingOrder.get(0).getCyclingOrderStatCode().equals(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode())){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该单车正在被人使用，请更换一辆单车"});
		}
		
		if(! SmartLockChannelUtil.tryUnlock(lockerHardwareCode)){ //开锁并等待完成
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"开锁信息未同步成功，如已经开锁请骑行，如未开启请重试或更换一辆单车"});
		}
		
		//扫开了，就是激活了
		smartLock.setActiveDate(new Date());
		if(smartLockStatCodeEnum == SmartLockStatCodeEnum.GRANTED){
			smartLock.setSmartLockStatCode(SmartLockStatCodeEnum.ACTIVED.toCode());
			SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
			logger.info("激活智能锁 {}", lockerHardwareCode);
		}
		logger.info("智能锁 {} 开锁成功", lockerHardwareCode);
		System.out.println("----智能锁开锁成功-----");
		
		return "success";
	}

}
