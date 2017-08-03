package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.LockSwitchStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.repository.SmartLockStatRepository;
import org.ccframe.subsys.bike.socket.commons.SmartLockChannelUtil;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartLockStatService extends BaseService<SmartLockStat,java.lang.Integer, SmartLockStatRepository>{
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public AppPageDto getBikeLocation(String position) {
		List<String> bikeLocationList = new ArrayList<String>();
		
	    String[] splitPos = PositionUtil.splitPos(position);
	    Double lng = Double.valueOf(splitPos[0]);
	    Double lat = Double.valueOf(splitPos[1]);
	    //半径为500米
	    double[] around = PositionUtil.getAround(lat, lng, Global.RADIUS);
		
		List<SmartLockStat> list = SpringContextHelper.getBean(SmartLockStatSearchService.class).
		findByLockSwitchStatCodeAndIfRepairIngAndLockLatBetweenAndLockLngBetween(
				LockSwitchStatCodeEnum.CLOCK.toCode(),BoolCodeEnum.NO.toCode(),around[0],around[2],
				around[1],around[3]);
		
		if(list!=null && list.size()>0) {
			StringBuffer posString = new StringBuffer("");
			for(SmartLockStat smartLockStat : list) {
				double distance = PositionUtil.getDistanceByLongNLat(lng, lat, smartLockStat.getLockLng(), smartLockStat.getLockLat());
				if(distance <= Global.RADIUS) {
					posString = new StringBuffer("");
					posString = posString.append("[").append(smartLockStat.getLockLng())
							.append(",").append(smartLockStat.getLockLat()).append("]");
					bikeLocationList.add(posString.toString());
				}
			}
		}
		
		/*bikeLocationList.add("[113.370900,23.132915]");
		bikeLocationList.add("[113.370650,23.133000]");
		bikeLocationList.add("[113.370700,23.133100]");*/
		
		AppPageDto appPageDto = new AppPageDto();
		appPageDto.setBikeLocationList(bikeLocationList);
		
		return appPageDto;
	}

	@Transactional
	public void updateLockBattery(Integer smartLockId, Integer battery) {
		SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatSearchService.class).getByKey(SmartLockStat.SMART_LOCK_ID, smartLockId);
		smartLockStat.setLockBattery(battery);
		SpringContextHelper.getBean(this.getClass()).save(smartLockStat);
	}

	/**
	 * 关锁时状态更新
	 * @param smartLock 
	 */
	@Transactional
	public void saveOrUpdate(SmartLock smartLock, String lockStatus, Integer lockBattery, Double lng, Double lat, Date sysTime) {
		SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatSearchService.class).getById(smartLock.getSmartLockId());
		if (smartLockStat == null) {
			smartLockStat = new SmartLockStat();
			smartLockStat.setSmartLockId(smartLock.getSmartLockId());
			smartLockStat.setOrgId(smartLock.getOrgId());
			smartLockStat.setIfRepairIng(BoolCodeEnum.NO.toCode());
		}
		smartLockStat.setLockSwitchStatCode(lockStatus);
		smartLockStat.setLockBattery(lockBattery);
		smartLockStat.setLockLat(lng);
		smartLockStat.setLockLng(lat);
		smartLockStat.setLastLocationUpdTime(sysTime);
		smartLockStat = SpringContextHelper.getBean(this.getClass()).save(smartLockStat);
		
		//更新骑行订单和智能锁状态表记录
		SpringContextHelper.getBean(CyclingOrderService.class).updateCyclingOrderAndLockStat(smartLock.getSmartLockId(), lng, lat, lockBattery);
		
	}

	/**
	 * @author zjm
	 */
	@Transactional
	public void saveSmartLockStatAndCyclingOrder(Long lockerHardwareCode,
			Map<DataBlockTypeEnum, Object> requestDataMap) {

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
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.HARDWARE_CODE, lockerHardwareCode);
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
		
	}
	
}
