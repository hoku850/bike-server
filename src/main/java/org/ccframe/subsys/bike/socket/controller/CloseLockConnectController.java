package org.ccframe.subsys.bike.socket.controller;

import java.util.Date;
import java.util.Map;

import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class CloseLockConnectController implements ISocketController {
	
	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.LOCKED;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(Long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.HARDWARE_CODE, lockerHardwareCode);
		if (smartLock == null) {
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"lockerHardwareCode不存在!"});
		}
		
		// 关锁成功，更新车锁状态
		String lockStatus = Byte.toString((byte)requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS));
		Integer lockBattery = (int)(byte)requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY);
		Double lng =(Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG);
		Double lat =(Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT);
		Date sysTime = (Date)requestDataMap.get(DataBlockTypeEnum.SYS_TIME);
		
		System.out.println("关锁后：");
		System.out.println("锁的状态=" + lockStatus);
		System.out.println("锁的电量=" + lockBattery);
		System.out.println("锁的经度=" + lng);
		System.out.println("锁的纬度=" + lat);
		System.out.println("系统时间=" + sysTime);
		
		//如果锁的经纬度为0.0，则使用默认经纬度
		if(lng==0.0 && lat==0.0) {
			lng = Global.BEIJING_LNG;
			lat = Global.BEIJING_LAT;
		}
		
		// 更新智能锁状态表和骑行订单
		SpringContextHelper.getBean(SmartLockStatService.class).saveOrUpdate(smartLock, lockStatus, lockBattery, lng, lat, sysTime);
		
		return requestDataMap;
	}
}