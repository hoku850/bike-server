package org.ccframe.commons.helper;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 系统初始化完毕后才允许执行定时器的切面.
 * @author JIM
 */
@Aspect
public class ProcessorAop {

	private static Logger logger = Logger.getLogger(ProcessorAop.class);

	@Around("execution(public void org.ccframe.subsys.*.processor.*.process(..))")
	public void aroundProcess(ProceedingJoinPoint jp) {
		if(!SysInitBeanHelper.isInited()){ //系统未初始化好，任何定时器都不执行
			return;
		}
		try {
			jp.proceed();
		} catch (Throwable tr) {
			logger.error(tr.getMessage(), tr);
		}
    }
}
