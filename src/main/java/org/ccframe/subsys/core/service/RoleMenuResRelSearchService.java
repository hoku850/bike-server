package org.ccframe.subsys.core.service;

import java.util.Collection;
import java.util.List;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.search.RoleMenuResRelSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuResRelSearchService extends BaseSearchService<RoleMenuResRel, Integer, RoleMenuResRelSearchRepository> {

	public List<RoleMenuResRel> findByRoleIdIn(Collection<Integer> roleIdList) {
		return  getRepository().findByRoleIdIn(roleIdList);
	}

}
