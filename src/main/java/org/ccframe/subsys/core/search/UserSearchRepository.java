package org.ccframe.subsys.core.search;

import java.util.List;

import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, Integer>{

	List<User> findByLoginIdAndUserPsw(String phoneNumber, String iMEI);

}
