package org.ccframe.subsys.bike.search;

import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BikeTypeSearchRepository extends ElasticsearchRepository<BikeType, Integer>{

}
