package org.ccframe.subsys.bike.socket.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.search.SmartLockSearchRepository;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class GpsConnectController implements ISocketController {

	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.GPS_INFO;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		//TODO 保存锁的状态和电量
		System.out.println("经度"+requestDataMap.get(DataBlockTypeEnum.LOCK_LNG));
		System.out.println("纬度"+requestDataMap.get(DataBlockTypeEnum.LOCK_LAT));
		System.out.println("时间="+requestDataMap.get(DataBlockTypeEnum.SYS_TIME));
		System.out.println("GPS卫星个数="+requestDataMap.get(DataBlockTypeEnum.CPRS_SATELLITE_NUM));
		System.out.println("用户ID="+requestDataMap.get(DataBlockTypeEnum.USER_ID));
		System.out.println("GPS上报类型="+requestDataMap.get(DataBlockTypeEnum.GPS_REPORT_TYPE));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			System.out.println(sdf.parse(requestDataMap.get(DataBlockTypeEnum.SYS_TIME).toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		SmartLock smartLock= SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, String.valueOf(lockerHardwareCode));
		SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatService.class).getByKey(SmartLockStat.SMART_LOCK_ID, smartLock.getSmartLockId());
//		smartLockStat.setLockLng(requestDataMap.get(DataBlockTypeEnum.LOCK_LNG));
//		smartLockStat.setLockLat(requestDataMap.get(DataBlockTypeEnum.LOCK_LAT));
//		smartLockStat.setLastLocationUpdTime(requestDataMap.get(DataBlockTypeEnum.SYS_TIME));
		SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
		//长连接事件，数据原封不动丢回去完事
		return requestDataMap;
	}

}
