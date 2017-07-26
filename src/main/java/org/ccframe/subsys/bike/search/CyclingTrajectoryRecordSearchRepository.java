package org.ccframe.subsys.bike.search;

import java.util.List;

import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CyclingTrajectoryRecordSearchRepository extends ElasticsearchRepository<CyclingTrajectoryRecord, Integer>{

	List<CyclingTrajectoryRecord> findByCyclingOrderIdOrderByRecordTimeAsc(Integer cyclingOrderId);
	List<CyclingTrajectoryRecord> findByCyclingOrderIdOrderByRecordTimeDesc(Integer cyclingOrderId);

}
