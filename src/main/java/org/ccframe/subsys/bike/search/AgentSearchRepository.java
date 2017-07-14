package org.ccframe.subsys.bike.search;

import org.ccframe.subsys.bike.domain.entity.Agent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AgentSearchRepository extends ElasticsearchRepository<Agent, Integer>{
}
