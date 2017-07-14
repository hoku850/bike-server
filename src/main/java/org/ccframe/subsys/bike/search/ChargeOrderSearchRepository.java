package org.ccframe.subsys.bike.search;

import java.util.List;

import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ChargeOrderSearchRepository extends ElasticsearchRepository<ChargeOrder, Integer>{

	List<ChargeOrder> findByUserIdAndOrgIdOrderByChargeFinishTimeDesc(
			Integer userId, Integer orgId);

}
