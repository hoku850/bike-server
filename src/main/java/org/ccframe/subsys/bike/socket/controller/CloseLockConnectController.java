package org.ccframe.subsys.bike.socket.controller;

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
import org.springframework.stereotype.Component;

@Component
public class CloseLockConnectController implements ISocketController {
	
	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.LOCKED;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, String.valueOf(lockerHardwareCode));
		if (smartLock!= null) {
			
			//保存或更新智能锁状态表
			SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatService.class).saveOrUpdateSmartLock(smartLock, requestDataMap);
			
			if (smartLockStat != null) {
				Double lng = smartLockStat.getLockLng();
				Double lat = smartLockStat.getLockLat();
				Integer battery = smartLockStat.getLockBattery();
				//更新骑行订单和智能锁状态表记录
				SpringContextHelper.getBean(CyclingOrderService.class).updateCyclingOrderAndLockStat(smartLock.getSmartLockId(), lng, lat, battery);
			}
			
		}
		
		return requestDataMap;
	}
}