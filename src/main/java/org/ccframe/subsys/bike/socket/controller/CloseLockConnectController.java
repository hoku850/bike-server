package org.ccframe.subsys.bike.socket.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
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
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");//小写的mm表示的是分钟
		SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatService.class).getByKey(SmartLockStat.SMART_LOCK_ID, (int)lockerHardwareCode);
		if (smartLockStat == null) {
			smartLockStat = new SmartLockStat();
		}
		smartLockStat.setSmartLockId((int)lockerHardwareCode);
		smartLockStat.setLockSwitchStatCode(Byte.toString((byte)requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS)));
		smartLockStat.setLockBattery((int)(byte)requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
		smartLockStat.setLockLat(Double.valueOf((String)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT)));
		smartLockStat.setLockLng(Double.valueOf((String)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG)));
		try {
			smartLockStat.setLastLocationUpdTime(sdf.parse((String)requestDataMap.get(DataBlockTypeEnum.SYS_TIME)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
		
		System.out.println(smartLockStat);
		
		return requestDataMap;
	}
}
