package org.ccframe.subsys.bike.service;

import java.util.Date;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.dto.Position;
import org.ccframe.sdk.bike.utils.AppConstant;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.repository.CyclingTrajectoryRecordRepository;
import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CyclingTrajectoryRecordService extends BaseService<CyclingTrajectoryRecord,java.lang.Integer, CyclingTrajectoryRecordRepository>{

	/**
	 * @author zjm
	 */
	@Transactional
	public String savePosition(String paths) {
		List<Position> list = PositionUtil.splitPathsToList(paths);
		MemberUser user = (MemberUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		if(list!=null && list.size()>0){
			for(Position position : list) {
				CyclingTrajectoryRecord record = new CyclingTrajectoryRecord();
				List<CyclingOrder> list2 = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
				record.setCyclingOrderId(list2.get(0).getCyclingOrderId());
				record.setRecordLocationLng(position.getLng());
				record.setRecordLocationLat(position.getLat());
				record.setRecordTime(new Date());
				
				SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).save(record);
			}
		}
		return AppConstant.SUCCESS;
	}

}
