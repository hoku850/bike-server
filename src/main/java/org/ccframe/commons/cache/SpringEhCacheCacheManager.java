package org.ccframe.commons.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class SpringEhCacheCacheManager extends EhCacheCacheManager {

	@Override
	protected Cache decorateCache(Cache cache) {
		return (isTransactionAware() ? new SpringTransactionAwareCacheDecorator(cache) : cache);
	}
}
