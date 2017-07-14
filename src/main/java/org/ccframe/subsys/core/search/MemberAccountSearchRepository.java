package org.ccframe.subsys.core.search;

import java.util.List;

import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MemberAccountSearchRepository extends ElasticsearchRepository<MemberAccount, Integer> {
	
	public List<MemberAccount> findByUserIdAndOrgIdAndAccountTypeCode(Integer userId, Integer orgId, String code);
	
}
