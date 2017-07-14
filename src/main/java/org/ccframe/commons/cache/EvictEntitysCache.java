package org.ccframe.commons.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于service,执行完毕批量移除指定类相关的缓存及查询缓存.
 * 用于批量上传等场景
 * @author JIM
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EvictEntitysCache {

	Class<?>[] value() default {};
}
