package org.ccframe.subsys.core.service;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.OrgUserRel;
import org.ccframe.subsys.core.search.OrgUserRelSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class OrgUserRelSearchService extends BaseSearchService<OrgUserRel, Integer, OrgUserRelSearchRepository> {

}
