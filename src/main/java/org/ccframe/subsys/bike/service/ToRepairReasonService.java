package org.ccframe.subsys.bike.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ccframe.subsys.bike.domain.entity.ToRepairReason;
import org.ccframe.subsys.bike.repository.ToRepairReasonRepository;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;

@Service
public class ToRepairReasonService extends BaseService<ToRepairReason,java.lang.Integer, ToRepairReasonRepository>{
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public List<ToRepairReason> getFixReason(String posCode) {
		List<ToRepairReason> list = SpringContextHelper.getBean(ToRepairReasonService.class).findByKey(ToRepairReason.TO_REPAIR_LOCATION_CODE, posCode);
		
		return list;
	}
}
