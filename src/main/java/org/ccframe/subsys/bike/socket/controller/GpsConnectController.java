package org.ccframe.subsys.bike.socket.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingOrderService;
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
		Double lockLng = (Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG);
		Double lockLat = (Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT);
		Date date = (Date)requestDataMap.get(DataBlockTypeEnum.SYS_TIME);
//		Integer userId = (Integer)requestDataMap.get(DataBlockTypeEnum.USER_ID);
		
		SmartLock smartLock= SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, String.valueOf(lockerHardwareCode));
		Integer smartLockId = smartLock.getSmartLockId();
		
		
		//更新骑行订单经纬度
		List<CyclingOrder> cyclingOrderList = SpringContextHelper.getBean(CyclingOrderSearchService.class).findBySmartLockIdOrderByStartTimeDesc(smartLockId);
		CyclingOrder cyclingOrder = cyclingOrderList.get(0);
		
		if(date.getTime() - cyclingOrder.getStartTime().getTime() <= 10*1000){//10秒内有新订单
			if(lockLat != null){
				cyclingOrder.setStartLocationLat(lockLat);
				cyclingOrder.setEndLocationLat(lockLat);
			}
			if(lockLng != null){
				cyclingOrder.setStartLocationLng(lockLng);
				cyclingOrder.setEndLocationLng(lockLng);
			}
			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		}
		
		//更新智能锁状态
		if(smartLock != null){
			SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatService.class).getByKey(SmartLockStat.SMART_LOCK_ID, smartLock.getSmartLockId());
			
			if(smartLockStat != null){
				if(lockLng != null){
					smartLockStat.setLockLng(lockLng);
				}
				if(lockLat != null){
					smartLockStat.setLockLat(lockLat);
				}
				smartLockStat.setLastLocationUpdTime(date);
				SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
			}
		}
		//长连接事件，数据原封不动丢回去完事
		return requestDataMap;
	}

}
