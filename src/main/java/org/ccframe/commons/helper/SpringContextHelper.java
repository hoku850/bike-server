package org.ccframe.commons.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SpringContextHelper implements ApplicationContextAware {

    private static Log log = LogFactory.getLog(SpringContextHelper.class);
    private static ApplicationContext applicationContext;

    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext context) {
        if (this.applicationContext != null) {
            log.error("ApplicationContextHolder already holded 'applicationContext'.");
        }
        this.applicationContext = context;
        log.debug("holded applicationContext,displayName:" + applicationContext.getDisplayName());
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("'applicationContext' property is null,ApplicationContextHolder not yet init.");
        }
        return applicationContext;
    }

    private static Map<Class<?>,Object> beans = new HashMap<Class<?>, Object>();
    public static <T> T getBean(Class<T> requiredType) {
        if(beans.get(requiredType)!=null){
            return (T)beans.get(requiredType);
        } else {
            Object instance = getApplicationContext().getBean(requiredType);
            beans.put(requiredType,instance);
            return (T)instance;
        }
    }


    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    public static boolean containsBean(String beanName) {
    	return getApplicationContext().containsBean(beanName);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> requiredType){
    	return getApplicationContext().getBeansOfType(requiredType);
    }
    
    public static void cleanHolder() {
        applicationContext = null;
    }


}
