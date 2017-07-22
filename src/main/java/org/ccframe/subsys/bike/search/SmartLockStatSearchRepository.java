package org.ccframe.subsys.bike.search;

import java.util.List;

import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface SmartLockStatSearchRepository extends ElasticsearchRepository<SmartLockStat, Integer>{

	List<SmartLockStat> findByLockSwitchStatCodeAndIfRepairIngAndLockLatBetweenAndLockLngBetween(String code, String string, double d,
			double e, double f, double g);
}
