package org.ccframe.subsys.core.dto;

import java.util.List;

public class CacheInf {
	private String cacheName;
	private double hitRatio;
	private long hitCount;
	private long missCount;
	private long timeToLiveSeconds;
	private long size;
	private long maxSize;
	
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	/**
	 * 关联缓存，如果是对象关联查询则是查询的列表。如果是查询缓存，则是关联的对象列表
	 */
	private List<String> evictLinkCacheList;

	public List<String> getEvictLinkCacheList() {
		return evictLinkCacheList;
	}
	public void setEvictLinkCacheList(List<String> evictLinkCacheList) {
		this.evictLinkCacheList = evictLinkCacheList;
	}
	public double getHitRatio() {
		return hitRatio;
	}
	public void setHitRatio(double hitRatio) {
		this.hitRatio = hitRatio;
	}
	public long getHitCount() {
		return hitCount;
	}
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}
	public long getMissCount() {
		return missCount;
	}
	public void setMissCount(long missCount) {
		this.missCount = missCount;
	}
	public long getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}
	public void setTimeToLiveSeconds(long timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
}
