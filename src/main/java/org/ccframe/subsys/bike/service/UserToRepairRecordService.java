package org.ccframe.subsys.bike.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.repository.UserToRepairRecordRepository;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;

@Service
public class UserToRepairRecordService extends BaseService<UserToRepairRecord,java.lang.Integer, UserToRepairRecordRepository>{
	
	/**
	 * @author zjm
	 */
	@Transactional
	public String saveRepairRecord(String posCode, Integer reasonID, String position) {
		User user = (User) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		//更新骑行订单状态
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), 1);
		CyclingOrder cyclingOrder = list.get(0);
		cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.TO_BE_REPAIRED.toCode());
		cyclingOrder.setEndTime(new Date());
		
		//------------未完待续
		String[] splits = PositionUtil.splitPos(position);
		cyclingOrder.setEndLocationLng(Double.valueOf(splits[0]));
		cyclingOrder.setEndLocationLat(Double.valueOf(splits[1]));
		SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		
		
		//生成报修记录
		UserToRepairRecord userToRepairRecord = new UserToRepairRecord();
		userToRepairRecord.setSmartLockId(1);
		userToRepairRecord.setBikePlateNumber("123");
		userToRepairRecord.setUserId(user.getUserId());
		userToRepairRecord.setOrgId(1);
		userToRepairRecord.setToRepairTime(new Date());
		userToRepairRecord.setToRepairLocationLng(116.0);
		userToRepairRecord.setToRepairLocationLat(40.0);
		userToRepairRecord.setToRepairLocationCode(posCode);
		userToRepairRecord.setToRepairReasonId(reasonID);
		userToRepairRecord.setIfFinishFix("N");
		userToRepairRecord.setFinishFixTime(new Date());
		
		SpringContextHelper.getBean(UserToRepairRecordService.class).save(userToRepairRecord);

		return "success";
	}
}
