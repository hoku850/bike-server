package org.ccframe.commons.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

public class ImportDataCheckUtil {
	
	private static Map<String, Pattern> patternPool = new HashMap<String, Pattern>();
	
	public static boolean stringCheck(String labelText, int maxLength, boolean allowBlank, String checkValue, int rowNum, int colNum, List<ExcelReaderError> errorList){
		if(StringUtils.isBlank(checkValue)){
			if(!allowBlank){
				errorList.add(new ExcelReaderError(colNum, rowNum, labelText + "不能为空"));
				return false;
			}
		}else if(checkValue.length() > maxLength){
			errorList.add(new ExcelReaderError(colNum, rowNum, labelText + "不能超过" + maxLength + "字符"));
			return false;
		}
		return true;
	}

	/**
	 * double,int,Date类型都使用此方法
	 * @param labelText
	 * @param checkValue
	 * @param rowNum
	 * @param colNum
	 * @param errorList
	 * @return
	 */
	public static boolean nullCheck(String labelText, Object checkValue, int rowNum, int colNum, List<ExcelReaderError> errorList){
		if(ObjectUtils.isEmpty(checkValue)){
			errorList.add(new ExcelReaderError(colNum, rowNum, labelText + "不能为空"));
			return false;
		}
		return true;
	}

	public static boolean patternCheck(String labelText, String regex, boolean allowBlank, String checkValue, int rowNum, int colNum, List<ExcelReaderError> errorList){
		if(StringUtils.isBlank(checkValue)){
			if(!allowBlank){
				errorList.add(new ExcelReaderError(colNum, rowNum, labelText + "不能为空"));
				return false;
			}
		}else{
			if(patternPool.get(regex) == null){
				patternPool.put(regex, Pattern.compile(regex));
			}
			if(!patternPool.get(regex).matcher(checkValue).find()){
				errorList.add(new ExcelReaderError(colNum, rowNum, labelText + "格式不正确"));
				return false;
			}
		}
		return true;
	}
	public static boolean enumCheck(String labelText, String allowedTexts, String checkValue, int rowNum, int colNum, List<ExcelReaderError> errorList){
		if(StringUtils.isBlank(checkValue)){
			errorList.add(new ExcelReaderError(colNum, rowNum, labelText + "不能为空"));
			return false;
		}else{
			if(!Arrays.asList(allowedTexts.split(",")).contains(checkValue)){
				errorList.add(new ExcelReaderError(colNum, rowNum, checkValue + "不是一个有效值"));
				return false;
			}
		}
		return true;
	}
}
