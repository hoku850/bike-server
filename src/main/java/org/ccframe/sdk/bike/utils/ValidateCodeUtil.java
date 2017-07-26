package org.ccframe.sdk.bike.utils;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class ValidateCodeUtil {

	private static Map<Integer, String> map = new HashedMap();

	public static String putValidateCode(Integer loginId) {
		String validateCode = (int)((Math.random()*9+1)*100000)+"";
		map.put(loginId, validateCode);
		return validateCode;
	}

	public static String getValidateCode(Integer loginId) {
		return map.get(loginId);
	}
}
