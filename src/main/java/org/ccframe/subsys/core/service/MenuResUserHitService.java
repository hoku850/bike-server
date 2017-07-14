package org.ccframe.subsys.core.service;

import java.util.Date;
import java.util.List;

import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.ccframe.subsys.core.domain.entity.MenuResUserHit;
import org.ccframe.subsys.core.repository.MenuResUserHitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuResUserHitService extends BaseService<MenuResUserHit, Integer, MenuResUserHitRepository> {

	@Transactional
	public void menuHit(Integer userId, String viewResValue) {
		MenuRes menuRes = SpringContextHelper.getBean(MenuResSearchService.class).getByKey(MenuRes.RES_URL, viewResValue);
		if(menuRes == null){
			return;
		}
		List<MenuResUserHit> menuResUserHitList = SpringContextHelper.getBean(MenuResUserHitSearchService.class).findByUserIdAndMenuResId(userId, menuRes.getMenuResId());
		if(menuResUserHitList.isEmpty()){
			MenuResUserHit menuResUserHit = new MenuResUserHit();
			menuResUserHit.setUserId(userId);
			menuResUserHit.setMenuResId(menuRes.getMenuResId());
			menuResUserHit.setLastHitTime(new Date());
			SpringContextHelper.getBean(this.getClass()).save(menuResUserHit);
		}else{
			MenuResUserHit menuResUserHit = menuResUserHitList.get(0);
			menuResUserHit.setLastHitTime(new Date());
			SpringContextHelper.getBean(this.getClass()).save(menuResUserHit);
		}
	}

}
