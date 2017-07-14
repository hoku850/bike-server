package org.ccframe.subsys.core.service;

import java.util.List;

import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.MenuResUserHit;
import org.ccframe.subsys.core.search.MenuResUserHitSearchRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class MenuResUserHitSearchService extends BaseSearchService<MenuResUserHit, Integer, MenuResUserHitSearchRepository> {

	public List<MenuResUserHit> findFastMenuRes(Integer userId) {
		return this.getRepository().findByUserId(userId, new Sort(Direction.DESC, MenuResUserHit.LAST_HIT_TIME));
	}

	public List<MenuResUserHit> findByUserIdAndMenuResId(Integer userId, Integer menuResId) {
		return this.getRepository().findByUserIdAndMenuResId(userId, menuResId);
	}

}
