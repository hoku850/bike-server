package org.ccframe.commons.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AutoCacheConfig {

	/**
	 * 缓存大小，未指定默认是100
	 * @return
	 */
	int value() default 100;
	
	/**
	 * TTL，对象未访问的移除时间，默认是一周
	 * @return
	 */
	int timeToLive() default 7 * 24 * 3600;
	
	/**
	 * 事务模式，默认为查询缓存，无缓存事务
	 * @return
	 */
	TransactionalMode transactionalMode() default TransactionalMode.OFF;
}
