package org.ccframe.subsys.bike.search;

import java.util.List;

import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserToRepairRecordSearchRepository extends ElasticsearchRepository<UserToRepairRecord, Integer>{
	
	List<UserToRepairRecord> findByUserIdAndBikePlateNumberOrderByToRepairTime(Integer UserId, String bikePlateNumber);
}
