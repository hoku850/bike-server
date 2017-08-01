package org.ccframe.subsys.bike.socket.controller;

import java.util.Date;
import java.util.Map;

import org.ccframe.client.ResGlobal;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.ccframe.subsys.bike.service.SmartLockStatService;
import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.commons.SmartLockChannelUtil;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

/**
 * 抓取开锁回复的指令.
 * @author JIM
 *
 */
@Component
public class OpenLockController implements ISocketController {

	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.OPEN_LOCK;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(String lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		Byte lockerErrorInfo = (byte)requestDataMap.get(DataBlockTypeEnum.LOCK_ERROR);

		if(lockerErrorInfo != 0){
			System.out.println("lockerErrorInfo != 0");
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该单车出现故障，请更换一辆单车"});
		}

		//开锁成功，更新车锁状态
		int userId = (int)requestDataMap.get(DataBlockTypeEnum.USER_ID);
		byte lockIfOpen = (byte)requestDataMap.get(DataBlockTypeEnum.LOCK_IF_OPEN);
		Date date = (Date)requestDataMap.get(DataBlockTypeEnum.SYS_TIME);
		byte lockStatus = (byte)requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS);
		byte lockError = (byte)requestDataMap.get(DataBlockTypeEnum.LOCK_ERROR);
		
		System.out.println("锁的开关=" + Byte.toString(lockIfOpen));
		System.out.println("用户ID=" + userId);
		System.out.println("时间=" + date);
		System.out.println("锁的状态="+Byte.toString(lockStatus));
		System.out.println("异常信息="+Byte.toString(lockError));
		
		//更新锁状态
		Date nowDate = new Date();
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, lockerHardwareCode);
		if(smartLock != null){
			SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatService.class).getByKey(SmartLockStat.SMART_LOCK_ID, smartLock.getSmartLockId());
			if(smartLockStat != null){
				smartLockStat.setLastLocationUpdTime(nowDate);
				smartLockStat.setLockSwitchStatCode(Byte.toString(lockStatus));
				SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
			}
		}
		
		//创建新的骑行订单
		MemberUser memberUser = SmartLockChannelUtil.getLockerUser(lockerHardwareCode);

		if(memberUser != null){
			CyclingOrder cyclingOrder = new CyclingOrder();
			cyclingOrder.setUserId(memberUser.getUserId());
			cyclingOrder.setOrgId(memberUser.getOrgId());
			cyclingOrder.setSmartLockId(smartLock.getSmartLockId());
			cyclingOrder.setBikePlateNumber(smartLock.getBikePlateNumber());
			cyclingOrder.setStartTime(nowDate);
			cyclingOrder.setStartLocationLng(39.54);//天安门经纬度
			cyclingOrder.setStartLocationLat(116.23);
			cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode());
			cyclingOrder.setCyclingContinousSec(0);
			cyclingOrder.setOrderAmmount(0.00);
			cyclingOrder.setCyclingDistanceMeter(0);
			cyclingOrder.setBikeTypeId(smartLock.getBikeTypeId());

			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		}
		
		SmartLockChannelUtil.notifyUnlocked(lockerHardwareCode);
		return null; //不回写了
	}

}
