package org.ccframe.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

/**
 * JSON 转换器.(jackson) 
 * 
 * Jim
 */
public class JsonBinder {
    private static Logger logger = LoggerFactory.getLogger(JsonBinder.class);

    private ObjectMapper mapper;
    private static JsonBinder normalBinder;
    private static JsonBinder nonNullBinder;

    private JsonBinder(Include inclusion) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(inclusion);
//        mapper.getSerializationConfig().withSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 创建输出全部属性到Json字符串的Binder.
     */
    public static synchronized  JsonBinder buildNormalBinder() {
        if(normalBinder==null){
            normalBinder = new JsonBinder(Include.ALWAYS);
        }
        return normalBinder;
    }

    /**
     * 创建只输出非空属性到Json字符串的Binder.
     */
    public static synchronized  JsonBinder buildNonNullBinder() {
        if(nonNullBinder==null){
            nonNullBinder = new JsonBinder(Include.NON_NULL);
        }
        return nonNullBinder;
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null.
     * 如果JSON字符串为"[]",返回空集合.
     * 如果需要返回数组，使用数据类型如Product[].class
     * <p/>
     */
    public <T> T toBean(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 如果对象为Null,返回"null".
     * 如果集合为空集合,返回"[]".
     */
    public String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

}
