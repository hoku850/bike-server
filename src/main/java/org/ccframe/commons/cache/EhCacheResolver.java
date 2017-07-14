package org.ccframe.commons.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.ccframe.commons.helper.EhCacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;

public class EhCacheResolver extends org.springframework.cache.interceptor.SimpleCacheResolver {  

	@Autowired
	private EhCacheHelper ehCacheHelper;
	
    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
		super.setCacheManager(cacheManager);
	}

	@Override
	public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
		String cacheName = context.getTarget().getClass().getSimpleName() + "_" + context.getMethod().getName();
		Set<Cache> result = new HashSet<Cache>();
		
		Cache cache = ehCacheHelper.getAndCreateCache(cacheName, context.getMethod().getAnnotation(AutoCacheConfig.class));
		result.add(cache);
		CacheEvictBy cacheEvictBy = context.getMethod().getAnnotation(CacheEvictBy.class);
		if(cacheEvictBy != null){
			for(Class<?> byClass: cacheEvictBy.value()){
				ehCacheHelper.addEvictByCache(byClass.getSimpleName(), cache);
			}
		}
		return result;
	}
}  