package org.ccframe.subsys.bike.socket.controller;

import java.util.Date;
import java.util.Map;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.springframework.stereotype.Component;

@Component
public class CloseLockConnectController implements ISocketController {
	
	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.LOCKED;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(String lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, lockerHardwareCode);
		if (smartLock!= null) {
			
			//保存或更新智能锁状态表
			SmartLockStatService smartLockStatService = SpringContextHelper.getBean(SmartLockStatService.class);
			SmartLockStat smartLockStat = smartLockStatService.getByKey(SmartLockStat.SMART_LOCK_ID, smartLock.getSmartLockId());
			if (smartLockStat == null) {
				smartLockStat = new SmartLockStat();
				smartLockStat.setSmartLockId(smartLock.getSmartLockId());
				smartLockStat.setOrgId(smartLock.getOrgId());
				smartLockStat.setIfRepairIng(BoolCodeEnum.NO.toCode());
			}
			smartLockStat.setLockSwitchStatCode(Byte.toString((byte)requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS)));
			smartLockStat.setLockBattery((int)(byte)requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
			Double lng =(Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG);
			Double lat =(Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT);
			smartLockStat.setLockLat(lng);
			smartLockStat.setLockLng(lat);
			smartLockStat.setLastLocationUpdTime((Date)requestDataMap.get(DataBlockTypeEnum.SYS_TIME));
			smartLockStat = smartLockStatService.save(smartLockStat);
			
			if (smartLockStat != null) {
				Integer battery = smartLockStat.getLockBattery();
				//更新骑行订单和智能锁状态表记录
				SpringContextHelper.getBean(CyclingOrderService.class).updateCyclingOrderAndLockStat(smartLock.getSmartLockId(), lng, lat, battery);
			}
		}
		return requestDataMap;
	}
}