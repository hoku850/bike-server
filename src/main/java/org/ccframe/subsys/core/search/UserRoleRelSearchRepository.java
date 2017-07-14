package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRoleRelSearchRepository extends ElasticsearchRepository<UserRoleRel, Integer> {
}
