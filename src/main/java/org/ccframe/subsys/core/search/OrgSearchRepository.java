package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.Org;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrgSearchRepository extends ElasticsearchRepository<Org, Integer>{
}
