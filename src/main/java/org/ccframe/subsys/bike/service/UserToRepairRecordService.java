package org.ccframe.subsys.bike.service;

import java.util.Date;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.DateDistanceUtil;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.repository.UserToRepairRecordRepository;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service	
public class UserToRepairRecordService extends BaseService<UserToRepairRecord,java.lang.Integer, UserToRepairRecordRepository>{
	
	/**
	 * @author zjm
	 */
	@Transactional
	public String saveRepairRecord(String posCode, Integer reasonID, String position) {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
	
		//更新骑行订单状态
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		CyclingOrder cyclingOrder = list.get(0);
		cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.TO_BE_REPAIRED.toCode());
		Date nowDate = new Date();
		cyclingOrder.setEndTime(nowDate);
		
		//------------未完待续
		String[] splits = PositionUtil.splitPos(position);
		cyclingOrder.setEndLocationLng(Double.valueOf(splits[0]));
		cyclingOrder.setEndLocationLat(Double.valueOf(splits[1]));
		long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder.getStartTime(), new Date());
		long min = sec / 60;

		cyclingOrder.setCyclingContinousSec((int)sec);

		//更新骑行距离
		Integer meter = SpringContextHelper.getBean(CyclingOrderService.class).countMeter(cyclingOrder);
		cyclingOrder.setCyclingDistanceMeter(meter);
		
		cyclingOrder.setOrderAmmount(0.00);
		SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		
		//生成报修记录
		UserToRepairRecord userToRepairRecord = new UserToRepairRecord();
		userToRepairRecord.setSmartLockId(60001);
		userToRepairRecord.setBikePlateNumber(cyclingOrder.getBikePlateNumber());
		userToRepairRecord.setUserId(user.getUserId());
		userToRepairRecord.setOrgId(user.getOrgId());
		userToRepairRecord.setToRepairTime(nowDate);
		userToRepairRecord.setToRepairLocationLng(Double.valueOf(splits[0]));
		userToRepairRecord.setToRepairLocationLat(Double.valueOf(splits[1]));
		userToRepairRecord.setToRepairLocationCode(posCode);
		userToRepairRecord.setToRepairReasonId(reasonID);
		userToRepairRecord.setIfFinishFix(BoolCodeEnum.NO.toCode());
		
		SpringContextHelper.getBean(UserToRepairRecordService.class).save(userToRepairRecord);
		
		//更新智能锁表记录
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder.getSmartLockId());
		smartLock.setSmartLockStatCode(SmartLockStatCodeEnum.TO_FIX.toCode());
		smartLock.setLastUseDate(nowDate);
		SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
		
		//更新智能锁状态表记录
		List<SmartLockStat> statList = SpringContextHelper.getBean(SmartLockStatService.class).findByKey(SmartLockStat.SMART_LOCK_ID, cyclingOrder.getSmartLockId());
		SmartLockStat smartLockStat = statList.get(0);
		//未完待续
		//smartLockStat.setLockLng(lockLng);
		//smartLockStat.setLockLat(lockLat);
		smartLockStat.setIfRepairIng(BoolCodeEnum.YES.toCode());
		SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);

		return "success";
	}
}
