package org.ccframe.subsys.bike.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(ControllerMapping.TEST_BASE)
public class TestController {
	private int smartLockId = 1;
	private static int imeiCode = 100004004;
	private static int macAddress = 200004004;
	private static int lockerHardwareCode = 300004004;
	private static int bikePlateNumber = 400004004;
	private static Date activeDate = new Date();
	private static Date lastUseDate = new Date();
	private static int orgId = 4;
	private static int bikeTypeId = 60022;
	private static String smartLockStatCode = "3";
	
	@RequestMapping(value = ControllerMapping.TEST_SAVE_SMARTLOCK)
	public void saveOrUpdate(){
		int i = 0;
		for(i = 0; i < 2000; i++){
			SmartLock smartLock = new SmartLock();
			smartLock.setImeiCode(String.valueOf(imeiCode++));
			smartLock.setMacAddress(String.valueOf(macAddress++));
			smartLock.setLockerHardwareCode(String.valueOf(lockerHardwareCode++));
			smartLock.setBikePlateNumber(String.valueOf(bikePlateNumber++));
			smartLock.setActiveDate(new Date());
			smartLock.setLastUseDate(new Date());
			smartLock.setOrgId(orgId);
			smartLock.setBikeTypeId(bikeTypeId);
			smartLock.setSmartLockStatCode(smartLockStatCode);
			SpringContextHelper.getBean(SmartLockService.class).saveOrUpdateSmartLock(smartLock);
		}
		System.out.println("finish");
	}
	
	/**
	 * 用户表（会员） 10000条数据，内容尽量随机，要具备合理性。
	 * @throws IOException 
	 */
	@RequestMapping(value = ControllerMapping.TEST_SAVE_USER)
	public void saveUser() throws IOException{
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		PrintWriter writer = response.getWriter();
		User user;
		Date time = new Date();
		for (int i = 0; i < 10000; i++) {
			user = new User();
			user.setLoginId(String.format("%06d", i));
			user.setUserNm(String.format("%06d", i));
			user.setUserPsw("admin");
			user.setUserMobile(String.format("%011d", i));
			user.setUserEmail(String.format("%011d@qq.com", i));
			user.setIfAdmin(BoolCodeEnum.NO.toCode());
			user.setCreateDate(time);
			user.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
			SpringContextHelper.getBean(UserService.class).save(user);
		}
		writer.write("saveUser finish");
		writer.flush();
	}
}

