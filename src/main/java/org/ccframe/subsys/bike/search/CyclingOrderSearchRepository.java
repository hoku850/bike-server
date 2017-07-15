package org.ccframe.subsys.bike.search;

import java.util.List;

import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CyclingOrderSearchRepository extends ElasticsearchRepository<CyclingOrder, Integer>{

	List<CyclingOrder> findByUserIdAndOrgIdOrderByEndTimeDesc(Integer userId,
			Integer orgId);
	
}