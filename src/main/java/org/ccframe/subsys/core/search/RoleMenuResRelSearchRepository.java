package org.ccframe.subsys.core.search;

import java.util.Collection;
import java.util.List;

import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoleMenuResRelSearchRepository extends ElasticsearchRepository<RoleMenuResRel, Integer> {
	
	List<RoleMenuResRel> findByRoleIdIn(Collection<Integer> roleIdList);
}
