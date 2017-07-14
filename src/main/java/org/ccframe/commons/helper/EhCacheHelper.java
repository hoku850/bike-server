package org.ccframe.commons.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.TransactionController;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;
import net.sf.ehcache.statistics.StatisticsGateway;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.ccframe.commons.cache.SpringTransactionAwareCacheDecorator;
import org.ccframe.subsys.core.dto.CacheInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

@Component
public class EhCacheHelper {

	protected Logger logger = Logger.getLogger(this.getClass()); //NOSONAR

	protected Map<String, Set<Cache>> cacheEvictByMap = new ConcurrentHashMap<>();
	
	@Autowired
	private CacheManager cacheManager;

	public Cache getCache(String cacheName){
		return cacheManager.getCache(cacheName);
	}

	public Map<String, Set<String>> getEvictEntityMap(){
		Map<String, Set<String>> resultMap = new HashMap<>();
		for(Entry<String,Set<Cache>> cacheSetEntry: cacheEvictByMap.entrySet()){
			for(Cache cache: cacheSetEntry.getValue()){
				String searchCacheName = cache.getName();
				Set<String> entityCacheNameSet = resultMap.get(searchCacheName);
				if(entityCacheNameSet == null){
					entityCacheNameSet = new HashSet<String>();
					resultMap.put(searchCacheName, entityCacheNameSet);
				}
				entityCacheNameSet.add(cacheSetEntry.getKey());
			}
		}
		return resultMap;
	}

	private net.sf.ehcache.CacheManager ehcache;

	@Autowired
	public void setEhcache(net.sf.ehcache.CacheManager ehcache) {
		this.ehcache = ehcache;
	}

	/**
	 * 清理多个缓存.
	 * @param cacheNameList
	 */
	public void clearCaches(List<String> cacheNameList){
		for(String cacheName: cacheNameList){
			net.sf.ehcache.Cache cache = ehcache.getCache(cacheName);
			if(cache != null){ //某些实体缓存在查询时可能没建立（没SAVE或GET过），故要添加判断
				if(cache.getCacheConfiguration().getTransactionalMode() == TransactionalMode.LOCAL){
					ehcache.getTransactionController().begin();
					cacheManager.getCache(cacheName).clear();
					ehcache.getTransactionController().commit();
				}else{
					cacheManager.getCache(cacheName).clear();
				}
			}
		}
	}
	
	public List<CacheInf> getCacheInfList(){

		//构建查询到类的反向关联map
		Map<String, Set<String>> queryCacheToEntityMap = getEvictEntityMap();

		List<CacheInf> result = new ArrayList<>();
	
		List<String> cacheNameList = Arrays.asList(((EhCacheCacheManager)cacheManager).getCacheManager().getCacheNames());
		Collections.sort(cacheNameList);

		for(String cacheName: cacheNameList){
			net.sf.ehcache.Cache ehCacheCache = (net.sf.ehcache.Cache)cacheManager.getCache(cacheName).getNativeCache();
			if("auto".equals(cacheName)){ //过滤掉auto的缓存
				continue;
			}
			CacheInf cacheInf = new CacheInf();
			
			cacheInf.setCacheName(cacheName);
			StatisticsGateway statisticsGateway = ehCacheCache.getStatistics();
			cacheInf.setHitRatio(statisticsGateway.cacheHitRatio());
			cacheInf.setMissCount(statisticsGateway.cacheMissCount());
			cacheInf.setHitCount(statisticsGateway.cacheHitCount());

			CacheConfiguration cacheConfiguration = ehCacheCache.getCacheConfiguration();
			cacheInf.setTimeToLiveSeconds(cacheConfiguration.getTimeToLiveSeconds());
			cacheInf.setMaxSize(cacheConfiguration.getMaxEntriesLocalHeap());
			cacheInf.setSize(ehCacheCache.getSize());
			
			List<String> evictLinkCacheList = new ArrayList<>();
			cacheInf.setEvictLinkCacheList(evictLinkCacheList);

			Set<Cache> entityLinkCaches = cacheEvictByMap.get(cacheName);
			if(CollectionUtils.isNotEmpty(entityLinkCaches)){ //对象关联的查询缓存
				for(Cache cache: entityLinkCaches){
					evictLinkCacheList.add(cache.getName());
				}
			}
			Set<String> queryCacheNameSet = queryCacheToEntityMap.get(cacheName);
			if(CollectionUtils.isNotEmpty(queryCacheNameSet)){
				evictLinkCacheList.addAll(queryCacheNameSet);
			}
			Collections.sort(evictLinkCacheList);

			result.add(cacheInf);
		}
		return result;
	}
	
	public void transactionCommit(){
		TransactionController controller = ((net.sf.ehcache.CacheManager)((EhCacheCacheManager)cacheManager).getCacheManager()).getTransactionController();
		if(controller.getCurrentTransactionContext()!= null){
			controller.commit();
		}
	}
	
	/**
	 * 获得缓存，没有就根据默认参数自动创建一个
	 * @param cacheName
	 * @param autoCacheConfig 
	 * @return
	 */
	public Cache getAndCreateCache(String cacheName, AutoCacheConfig autoCacheConfig){
		int cacheSize = (autoCacheConfig == null ? 100: autoCacheConfig.value());
		int timeToLive = (autoCacheConfig == null ? 120 : autoCacheConfig.timeToLive());
		TransactionalMode transactionalMode = (autoCacheConfig == null ? TransactionalMode.OFF : autoCacheConfig.transactionalMode());
		Cache cache = cacheManager.getCache(cacheName);
		if(cache == null){
			synchronized(this){ //处理并行创建的问题
				Cache innerCheckCache = cacheManager.getCache(cacheName);
				if(innerCheckCache != null){
					return innerCheckCache;
				}
				logger.warn("cache " + cacheName + " is missing, try to create a new one: cacheSize="+cacheSize+", timeToLive="+timeToLive);
				CacheConfiguration cacheConfiguration = new CacheConfiguration(cacheName, cacheSize)
					.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
					.eternal(false)
					.timeToLiveSeconds(timeToLive)
					.timeToIdleSeconds(timeToLive)
					.transactionalMode(transactionalMode)
					.overflowToDisk(false);
				cacheConfiguration.setStatistics(true);
				net.sf.ehcache.Cache newCache = new net.sf.ehcache.Cache(cacheConfiguration);
				((EhCacheCacheManager)cacheManager).getCacheManager().addCache(newCache);
				return new EhCacheCache(newCache);
			}
		}else{
			return cache;
		}
	}
	
	/**
	 * 将所有关联查询的缓存都清空
	 * @param className
	 */
	public void clearAssociateCache(String className){
		Set<Cache> cacheSet = cacheEvictByMap.get(className);
		if(cacheSet != null){
			for(Cache cache: cacheEvictByMap.get(className)){
				cache.clear();
			}
		}
	}

	/**
	 * @param className 关联的实体
	 * @param cacheName 需要清理的缓存
	 */
	public void addEvictByCache(String className,Cache cache){
		Set<Cache> cacheSet = cacheEvictByMap.get(className);
		if(cacheSet == null){
			cacheSet = new HashSet<Cache>();
		}
		Cache targetCache = cache;
		if(cache instanceof SpringTransactionAwareCacheDecorator){
			targetCache = ((SpringTransactionAwareCacheDecorator)cache).getTargetCache();
		}
		boolean found = false;
		for(Cache testCache: cacheSet){
			if(testCache.getName().equals(targetCache.getName())){
				found = true;
			}
		}
		if(!found){
			cacheSet.add(targetCache);
		}
		cacheEvictByMap.put(className, cacheSet);
	}
}
