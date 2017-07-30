package org.ccframe.subsys.bike.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.service.UserToRepairRecordService;
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
	
	
	@RequestMapping(value = ControllerMapping.TEST_SAVE_SMARTLOCK)
	public void saveSmartLock() throws IOException{
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		PrintWriter writer = response.getWriter();
		
	    int imeiCode = 100000001;
		int macAddress = 200000001;
		int lockerHardwareCode = 300000001;
		int bikePlateNumber = 400000001;
		int i = 0;
		SmartLock smartLock = new SmartLock();
		for(i = 0; i < 10000; i++){
			smartLock = new SmartLock();
			smartLock.setImeiCode(String.valueOf(imeiCode++));
			smartLock.setMacAddress(String.valueOf(macAddress++));
			smartLock.setLockerHardwareCode(String.valueOf(lockerHardwareCode++));
			smartLock.setBikePlateNumber(String.valueOf(bikePlateNumber++));
			smartLock.setActiveDate(new Date());
			smartLock.setLastUseDate(new Date());
			if(i<1000){
				smartLock.setOrgId(2);
				smartLock.setBikeTypeId(62002);
				smartLock.setSmartLockStatCode("0");
			}
			if(i>=1000 && i<2000){
				smartLock.setOrgId(2);
				smartLock.setBikeTypeId(62002);
				smartLock.setSmartLockStatCode("1");
			}
			if(i>=2000 && i<3000){
				smartLock.setOrgId(2);
				smartLock.setBikeTypeId(62003);
				smartLock.setSmartLockStatCode("2");
			}
			if(i>=3000 && i<5000){
				smartLock.setOrgId(3);
				smartLock.setBikeTypeId(63002);
				smartLock.setSmartLockStatCode("1");
			}
			if(i>=5000 && i<6000){
				smartLock.setOrgId(3);
				smartLock.setBikeTypeId(63003);
				smartLock.setSmartLockStatCode("2");
			}
			if(i>=6000 && i<8000){
				smartLock.setOrgId(4);
				smartLock.setBikeTypeId(64002);
				smartLock.setSmartLockStatCode("2");
			}
			if(i>=8000 && i<9000){
				smartLock.setOrgId(4);
				smartLock.setBikeTypeId(64003);
				smartLock.setSmartLockStatCode("3");
			}
			SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
		}
		writer.write("saveUser finish");
		writer.flush();
	}
	
	@RequestMapping(value = "saveCyclingOrder")
	public void saveCyclingOrder() throws IOException{
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		PrintWriter writer = response.getWriter();
		int userId = 60002;
		int smartLockId = 60002;
		int bikePlateNumber = 400000001;
		
		int i = 0;
		CyclingOrder cyclingOrder = new CyclingOrder();
		for(i = 0; i < 9000; i++){
			cyclingOrder = new CyclingOrder();
			
			cyclingOrder.setUserId(SpringContextHelper.getBean(UserService.class).findIdList().get(i));
			cyclingOrder.setSmartLockId(SpringContextHelper.getBean(SmartLockService.class).findIdList().get(i));
			cyclingOrder.setOrgId(SpringContextHelper.getBean(SmartLockService.class).getById(cyclingOrder.getSmartLockId()).getOrgId());
			cyclingOrder.setBikePlateNumber(SpringContextHelper.getBean(SmartLockService.class).getById(cyclingOrder.getSmartLockId()).getBikePlateNumber());
			cyclingOrder.setStartTime(new Date());
			cyclingOrder.setStartLocationLng(116.23);
			cyclingOrder.setStartLocationLat(39.54);
			cyclingOrder.setEndTime(new Date());
			cyclingOrder.setEndLocationLng(116.53);
			cyclingOrder.setEndLocationLat(39.84);
			cyclingOrder.setCyclingOrderStatCode("2");
			cyclingOrder.setCyclingDistanceMeter(10000);
			cyclingOrder.setOrderAmmount(1.0);
			cyclingOrder.setCyclingContinousSec(3800);
			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
			
			cyclingOrder = new CyclingOrder();
			cyclingOrder.setUserId(SpringContextHelper.getBean(UserService.class).findIdList().get(i));
			cyclingOrder.setSmartLockId(SpringContextHelper.getBean(SmartLockService.class).findIdList().get(i+1));
			cyclingOrder.setOrgId(SpringContextHelper.getBean(SmartLockService.class).getById(cyclingOrder.getSmartLockId()).getOrgId());
			cyclingOrder.setBikePlateNumber(SpringContextHelper.getBean(SmartLockService.class).getById(cyclingOrder.getSmartLockId()).getBikePlateNumber());
			cyclingOrder.setStartTime(new Date());
			cyclingOrder.setStartLocationLng(106.23);
			cyclingOrder.setStartLocationLat(38.54);
			cyclingOrder.setEndTime(new Date());
			cyclingOrder.setEndLocationLng(106.53);
			cyclingOrder.setEndLocationLat(39.84);
			cyclingOrder.setCyclingOrderStatCode("2");
			cyclingOrder.setCyclingDistanceMeter(9000);
			cyclingOrder.setOrderAmmount(1.0);
			cyclingOrder.setCyclingContinousSec(3800);
			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		}
		writer.write("saveUser finish");
		writer.flush();
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
	
	@RequestMapping(value = "saveUserRepairRecord")
	public void saveUserRepairRecord() throws IOException{
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		PrintWriter writer = response.getWriter();
		int smartLockId = 60001;
		int i = 0;
		UserToRepairRecord userToRepairRecord = new UserToRepairRecord();
		for(i = 0; i < 1000; i++){
			userToRepairRecord = new UserToRepairRecord();
			userToRepairRecord.setSmartLockId(SpringContextHelper.getBean(SmartLockService.class).findIdList().get(i));
			userToRepairRecord.setBikePlateNumber(SpringContextHelper.getBean(SmartLockService.class).getById(userToRepairRecord.getSmartLockId()).getBikePlateNumber());
			userToRepairRecord.setUserId(SpringContextHelper.getBean(UserService.class).findIdList().get(i));
			userToRepairRecord.setOrgId(SpringContextHelper.getBean(SmartLockService.class).getById(userToRepairRecord.getSmartLockId()).getOrgId());
			userToRepairRecord.setToRepairTime(new Date());
			userToRepairRecord.setToRepairLocationLng(116.23);
			userToRepairRecord.setToRepairLocationLat(39.54);
			userToRepairRecord.setToRepairLocationCode("1");
			userToRepairRecord.setToRepairReasonId(60001);
			userToRepairRecord.setIfFinishFix("1");
			userToRepairRecord.setFinishFixTime(new Date());
			SpringContextHelper.getBean(UserToRepairRecordService.class).save(userToRepairRecord);
		}
		writer.write("finish");
		writer.flush();
	}
}

