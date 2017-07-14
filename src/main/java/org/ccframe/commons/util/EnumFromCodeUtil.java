package org.ccframe.commons.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * JSTL的enum代码支持。支持API方法可以实现JSP页面JSTL无需使用数据库code判断，而采用ENUM名来判断，例如：
 * <c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'NOT_SUBMIT'}">未提交</c:if>
 * <c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'QUEUE'}">未处理</c:if>
 * <c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'APPROVE'}">同意</c:if>
 * <c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'DENY'}">拒绝</c:if>
 * @author JIM
 *
 */
public class EnumFromCodeUtil {
	
	public static Map<String, Class<Enum>> enumClassMap;
	
	private static synchronized void initEnumMap(){
		try{
			if(enumClassMap == null){
				enumClassMap = new HashMap<String, Class<Enum>>();
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		    	int pathLen = Thread.currentThread().getContextClassLoader().getResource(".").toURI().toString().length();
				for(Resource resources: resolver.getResources("classpath:org/ccframe/subsys/*/domain/code/*.class")){
					String uriStr = resources.getURI().toString();
					Class scanClass = Class.forName(uriStr.substring(pathLen, uriStr.length() - 6).replaceAll("/", "."));
					enumClassMap.put(scanClass.getSimpleName(), scanClass);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Enum enumFromCode(String enumClassName, String code){
		try {
			initEnumMap();
			return (Enum)enumClassMap.get(enumClassName).getMethod("fromCode", String.class).invoke(null, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
