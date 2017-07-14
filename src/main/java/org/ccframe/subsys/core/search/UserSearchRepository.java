package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, Integer>{
}
