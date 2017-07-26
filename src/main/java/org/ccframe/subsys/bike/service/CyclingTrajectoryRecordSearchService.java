package org.ccframe.subsys.bike.service;

import java.util.List;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.search.CyclingTrajectoryRecordSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class CyclingTrajectoryRecordSearchService extends BaseSearchService<CyclingTrajectoryRecord, Integer, CyclingTrajectoryRecordSearchRepository>{

	/**	
	 * @author zjm
	 */
	public List<CyclingTrajectoryRecord> findByCyclingOrderIdOrderByRecordTimeAsc(Integer cyclingOrderId){
		if(cyclingOrderId!=null) {
			
			return this.getRepository().findByCyclingOrderIdOrderByRecordTimeAsc(cyclingOrderId);
		}
		return null;
	}
	
	public List<CyclingTrajectoryRecord> findByCyclingOrderIdOrderByRecordTimeDesc(Integer cyclingOrderId){
		if(cyclingOrderId!=null) {
			
			return this.getRepository().findByCyclingOrderIdOrderByRecordTimeDesc(cyclingOrderId);
		}
		return null;
	}

}
