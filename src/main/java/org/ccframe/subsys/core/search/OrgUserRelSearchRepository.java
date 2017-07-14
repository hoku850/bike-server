package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.OrgUserRel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrgUserRelSearchRepository extends ElasticsearchRepository<OrgUserRel, Integer> {
	
}
