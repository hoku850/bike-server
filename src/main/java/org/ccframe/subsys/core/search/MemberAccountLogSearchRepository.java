package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.MemberAccountLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MemberAccountLogSearchRepository extends ElasticsearchRepository<MemberAccountLog, Integer> {
	
}
