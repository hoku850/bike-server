package org.ccframe.subsys.bike.socket.controller;

import java.util.Date;
import java.util.Map;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
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
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, String.valueOf(lockerHardwareCode));
		if (smartLock!= null) {
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
			smartLockStat.setLockLat((Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT));
			smartLockStat.setLockLng((Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG));
			smartLockStat.setLastLocationUpdTime((Date)requestDataMap.get(DataBlockTypeEnum.SYS_TIME));
			
			smartLockStatService.save(smartLockStat);
			System.out.println(smartLockStat);
		} 
		
		return requestDataMap;
	}
}
