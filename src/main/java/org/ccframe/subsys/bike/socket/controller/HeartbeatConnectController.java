package org.ccframe.subsys.bike.socket.controller;

import java.util.Map;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatConnectController implements ISocketController {

	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.IMSI_INFO;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(Long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		
		System.out.println("锁心跳发送的数据：");
		System.out.println("IMSI码"+requestDataMap.get(DataBlockTypeEnum.IMSI));
		System.out.println("锁的状态"+Byte.toString((byte)requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS)));
		System.out.println("锁的电量"+(int)(byte)requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
		System.out.println("csp信号="+requestDataMap.get(DataBlockTypeEnum.GPS_INFO));
		System.out.println("经度"+requestDataMap.get(DataBlockTypeEnum.LOCK_LNG));
		System.out.println("纬度"+requestDataMap.get(DataBlockTypeEnum.LOCK_LAT));
		
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.HARDWARE_CODE, lockerHardwareCode);
		if (smartLock!= null) {
			if(SmartLockStatCodeEnum.fromCode(smartLock.getSmartLockStatCode()) == SmartLockStatCodeEnum.GRANTED || SmartLockStatCodeEnum.fromCode(smartLock.getSmartLockStatCode()) == SmartLockStatCodeEnum.ACTIVED){
				SpringContextHelper.getBean(SmartLockStatService.class).updateLockBattery(smartLock.getSmartLockId(), (int)(byte)requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
			}
		}
		//长连接事件，数据原封不动丢回去完事
		return requestDataMap;
	}

}
