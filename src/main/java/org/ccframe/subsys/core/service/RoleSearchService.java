package org.ccframe.subsys.core.service;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.search.RoleSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleSearchService extends BaseSearchService<Role, Integer, RoleSearchRepository> {

}
