package org.ccframe.subsys.core.service;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.search.UserRoleRelSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleRelSearchService extends BaseSearchService<UserRoleRel, Integer, UserRoleRelSearchRepository> {

	@Override
	public Integer getPriority() {
		return 0;
	}

}
