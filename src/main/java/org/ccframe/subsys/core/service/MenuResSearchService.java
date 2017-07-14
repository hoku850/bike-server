package org.ccframe.subsys.core.service;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.ccframe.subsys.core.search.MenuResSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuResSearchService extends BaseSearchService<MenuRes, Integer, MenuResSearchRepository> {

}
