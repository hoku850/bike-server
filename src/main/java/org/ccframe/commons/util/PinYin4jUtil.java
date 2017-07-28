package org.ccframe.commons.util;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang3.StringUtils;

public class PinYin4jUtil {

	public static boolean isChineseChar(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        }
        return false;
    }

	public static final String toPinYinFirstCharString(String text, boolean isLowerCase){
		if(StringUtils.isBlank(text)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(char ch:text.toCharArray()){
			if(isChineseChar(ch)){
				String[] pinYinStr = PinyinHelper.toHanyuPinyinStringArray(ch);
				sb.append(pinYinStr[0].charAt(0));
			}else{
				sb.append(ch);
			}
		}
		return sb.toString().toLowerCase();
	}
	
}
