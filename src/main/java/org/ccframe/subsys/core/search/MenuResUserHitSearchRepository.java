package org.ccframe.subsys.core.search;

import java.util.List;

import org.ccframe.subsys.core.domain.entity.MenuResUserHit;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MenuResUserHitSearchRepository extends ElasticsearchRepository<MenuResUserHit, Integer> {
	
	List<MenuResUserHit> findByUserId(Integer userId, Sort sort);

	List<MenuResUserHit> findByUserIdAndMenuResId(Integer userId, Integer menuResId);
}
