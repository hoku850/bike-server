package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoleSearchRepository extends ElasticsearchRepository<Role, Integer> {
}
