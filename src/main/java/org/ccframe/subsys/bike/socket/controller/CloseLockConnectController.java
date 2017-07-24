package org.ccframe.subsys.bike.socket.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.DateDistanceUtil;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
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
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CloseLockConnectController implements ISocketController {
	
	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.LOCKED;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		//请写到一个service方法以便减少事务开销
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
			System.out.println(smartLockStat.toString());
			
//			//更新骑行订单
//			User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
//			
//			List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), 1);
//			
//			if(list!=null && list.size()>0) {
//				//更新骑行订单记录
//				CyclingOrder cyclingOrder = list.get(0);
//
//				cyclingOrder.setEndLocationLng((Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG));
//				cyclingOrder.setEndLocationLat((Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT));
//				cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode());
//				Date nowDate = new Date();
//				cyclingOrder.setEndTime(nowDate);
//				long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder.getStartTime(), nowDate);
//				long min = sec / 60;
//				System.out.println("已骑行" + min + "分钟");
//				//写死 每半小时支付0.5元
//				Integer count = (int) (min/30.0) + 1;
//				//Integer count = (int) (31/30.0);
//				BigDecimal result = new BigDecimal(0.5).multiply(new BigDecimal(count), MathContext.DECIMAL32);
//				Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//				
//				cyclingOrder.setCyclingContinousSec((int)sec);
//				cyclingOrder.setOrderAmmount(payMoney);
//				//怎么保存距离
//				//cyclingOrder.setCyclingDistanceMeter(Integer.valueOf(meter));
//				SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
//			}
		} 
		
		return requestDataMap;
	}
}
