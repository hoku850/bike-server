package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MenuResSearchRepository extends ElasticsearchRepository<MenuRes, Integer>{
}
