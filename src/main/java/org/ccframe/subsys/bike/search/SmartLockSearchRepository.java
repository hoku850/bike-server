package org.ccframe.subsys.bike.search;

import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SmartLockSearchRepository extends ElasticsearchRepository<SmartLock, Integer>{
}
