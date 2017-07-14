package org.ccframe.subsys.core.controller;

import java.util.Arrays;
import java.util.List;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.EhCacheHelper;
import org.ccframe.subsys.core.dto.CacheInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.ADMIN_CACHE_BASE)
public class CacheInfController {

	@Autowired
	private EhCacheHelper ehCacheHelper;
	
	@RequestMapping(value = ControllerMapping.ADMIN_CACHE_INF_LIST)
	@Transactional(readOnly=true)
	public List<CacheInf> findCacheList() {
		return ehCacheHelper.getCacheInfList();
	}

	@RequestMapping(value = ControllerMapping.ADMIN_CACHE_CLEAR_CACHES)
	public void clearCaches(String cacheNameStr){
		ehCacheHelper.clearCaches(Arrays.asList(cacheNameStr.split(Global.ENUM_TEXT_SPLIT_CHAR)));
	}
}
