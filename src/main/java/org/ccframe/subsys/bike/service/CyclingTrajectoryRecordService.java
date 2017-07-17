package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.ccframe.sdk.bike.dto.Position;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.repository.CyclingTrajectoryRecordRepository;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;

@Service
public class CyclingTrajectoryRecordService extends BaseService<CyclingTrajectoryRecord,java.lang.Integer, CyclingTrajectoryRecordRepository>{

	/**
	 * @author zjm
	 */
	@Transactional
	public String savePosition(String paths) {
		List<Position> list = PositionUtil.splitPathsToList(paths);
		User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		if(list!=null && list.size()>0){
			for(Position position : list) {
				CyclingTrajectoryRecord record = new CyclingTrajectoryRecord();
				List<CyclingOrder> list2 = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), 1);
				record.setCyclingOrderId(list2.get(0).getCyclingOrderId());
				record.setRecordLocationLng(position.getLng());
				record.setRecordLocationLat(position.getLat());
				record.setRecordTime(new Date());
				
				SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).save(record);
			}
		}
		return "success";
	}

}
