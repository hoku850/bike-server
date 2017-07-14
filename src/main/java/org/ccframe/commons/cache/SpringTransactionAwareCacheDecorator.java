package org.ccframe.commons.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

public class SpringTransactionAwareCacheDecorator extends TransactionAwareCacheDecorator {

	private Cache targetCache;
	
	public SpringTransactionAwareCacheDecorator(Cache targetCache) {
		super(targetCache);
		this.targetCache = targetCache;
	}

	@Override
	public void put(final Object key, final Object value) {
		this.targetCache.put(key, value);
	}

	public Cache getTargetCache(){
		return targetCache;
	}
}
