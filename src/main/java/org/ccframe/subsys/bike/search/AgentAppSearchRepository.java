package org.ccframe.subsys.bike.search;

import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AgentAppSearchRepository extends ElasticsearchRepository<AgentApp, Integer>{
}
