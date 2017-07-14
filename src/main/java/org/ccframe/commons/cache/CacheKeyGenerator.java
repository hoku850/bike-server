package org.ccframe.commons.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.commons.util.MD5Util;
import org.springframework.cache.interceptor.KeyGenerator;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CacheKeyGenerator implements KeyGenerator {

	private final ObjectMapper objectMapper;
	
	public CacheKeyGenerator(){
		objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
    }

	@Override
	public Object generate(Object target, Method method, Object... params) {
		List<String> paramList = new ArrayList<String>();
		for(Object param: params){
			if(param != null){
				try {
					paramList.add(objectMapper.writeValueAsString(param));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
		}
		return MD5Util.encode(StringUtils.join(paramList, '\u0000'));
	}
}
