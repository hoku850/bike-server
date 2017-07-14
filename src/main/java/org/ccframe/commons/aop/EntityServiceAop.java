package org.ccframe.commons.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.ccframe.commons.helper.EhCacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

/**
 * 用于对BaseService的对象基本操作方法进行缓存管理操作.
 * 包括：添加、修改、删除、FindAll(这个保存一份永久的在内存)
 * @author JIM
 */
@Aspect
@Component
public class EntityServiceAop {

	@Autowired
	private EhCacheHelper ehCacheHelper;

    private static final String LIST_ALL_CACHE = "listAll";
    
    private static final String GET_ENTITIY_CLASS_METHOD = "getEntityClass";

	@Around("execution(public * org.ccframe.commons.base.BaseService.getById(..))")
    public Object aroundGetById(ProceedingJoinPoint pjp) throws Throwable{
		Object target = pjp.getTarget();
		Class<?> entityClass = (Class<?>)target.getClass().getMethod(GET_ENTITIY_CLASS_METHOD).invoke(target);
		Object id = pjp.getArgs()[0];
		
		AutoCacheConfig autoCacheConfig = entityClass.getAnnotation(AutoCacheConfig.class);
		Cache cache = ehCacheHelper.getAndCreateCache(entityClass.getSimpleName(), autoCacheConfig);
		ValueWrapper valueWrapper = cache.get(id);
		if(valueWrapper != null){
			return valueWrapper.get(); 
		}
		Object obj = pjp.proceed();
		if(obj != null){
			cache.put(id, obj);
		}
		return obj;
    }

	@Around("execution(public * org.ccframe.commons.base.BaseService.listAll(..))") //能调用这个的必须是较小的表，例如码表等，因此全部缓存
    public Object aroundListAll(ProceedingJoinPoint pjp) throws Throwable{
		Object target = pjp.getTarget();
		Class<?> entityClass = (Class<?>)target.getClass().getMethod(GET_ENTITIY_CLASS_METHOD).invoke(target);
		
		AutoCacheConfig autoCacheConfig = target.getClass().getAnnotation(AutoCacheConfig.class);
		Cache cache = ehCacheHelper.getAndCreateCache(entityClass.getSimpleName() + "_" + LIST_ALL_CACHE, autoCacheConfig);
		ValueWrapper valueWrapper = cache.get(LIST_ALL_CACHE);
		if(valueWrapper != null){
			return valueWrapper.get(); 
		}
		Object listAll = pjp.proceed();
		if(listAll != null){
			cache.put(LIST_ALL_CACHE, listAll);
		}
		ehCacheHelper.addEvictByCache(entityClass.getSimpleName(), cache);
		return listAll;
    }

    @AfterReturning(pointcut="execution(public * org.ccframe.commons.base.BaseService.save(..)) || execution(public * org.ccframe.commons.base.BaseService.saveAndFlush(..))", returning="returnValue")
    public void afterSave(JoinPoint jp, Object returnValue) throws Throwable{ //创建以自己关联对象为命名的缓存并保存数据.
		Object entity = jp.getArgs()[0];
		if(entity == null){
			return;
		}
		Object target = jp.getTarget();
		Class<?> entityClass = (Class<?>)target.getClass().getMethod(GET_ENTITIY_CLASS_METHOD).invoke(target);
		
		String simpleName = entityClass.getSimpleName();
		Object id = entityClass.getMethod("get" + simpleName + "Id").invoke(entity);
		AutoCacheConfig autoCacheConfig = entityClass.getAnnotation(AutoCacheConfig.class);
		Cache cache = ehCacheHelper.getAndCreateCache(simpleName, autoCacheConfig);
		cache.put(id, returnValue);
		ehCacheHelper.clearAssociateCache(simpleName);
    }

    @AfterReturning("execution(public * org.ccframe.commons.base.BaseService.delete(..))")
    public void afterDelete(JoinPoint jp) throws Throwable{ //尝试删除数据.
		Object entity = jp.getArgs()[0];
		if(entity == null){
			return;
		}

		Object target = jp.getTarget();
		Class<?> entityClass = (Class<?>)target.getClass().getMethod(GET_ENTITIY_CLASS_METHOD).invoke(target);
		String simpleName = entityClass.getSimpleName();
		Object id = entityClass.getMethod("get" + simpleName + "Id").invoke(entity);
		Cache cache = ehCacheHelper.getCache(simpleName);
		if(cache != null){
			cache.evict(id);
		}
		ehCacheHelper.clearAssociateCache(simpleName);
    }

    @AfterReturning("execution(public * org.ccframe.commons.base.BaseService.deleteById(..))")
    public void afterDeleteById(JoinPoint jp) throws Throwable{ //尝试删除数据.
		Object target = jp.getTarget();
		Class<?> entityClass = (Class<?>)target.getClass().getMethod(GET_ENTITIY_CLASS_METHOD).invoke(target);
		String simpleName = entityClass.getSimpleName();
		Cache cache = ehCacheHelper.getCache(simpleName);
		if(cache != null){
			cache.evict(jp.getArgs()[0]);
		}
		ehCacheHelper.clearAssociateCache(simpleName);
    }
}
