package org.ccframe.subsys.core.search;

import org.ccframe.subsys.core.domain.entity.TreeNode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TreeNodeSearchRepository extends ElasticsearchRepository<TreeNode, Integer> {
	
}
