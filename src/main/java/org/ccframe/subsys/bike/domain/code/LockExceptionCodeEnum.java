package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

/**
 * 锁的异常信息
 * @author JIM
 *
 */
public enum LockExceptionCodeEnum implements ICodeEnum{

    /**
     * 正常
     */
	SUCEED,
    /**
     * 关闭异常
     */
	CLOSED_FAILED,
    /**
     * 打开异常
     */
	OPEN_FAILED,
	/**
	 * 不在范围内
	 */
	OUT_OF_RANGE,
	/**
     * 打开超时
     */
	OPEN_TIMEOUT;
	
	public static LockExceptionCodeEnum fromCode(String code) {
		try {
			return values()[Integer.parseInt(code)];
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toCode() {
		return Integer.toString(this.ordinal());
	}

	@Override
	public List<ICodeEnum> valueList() {
		List<ICodeEnum> result = new ArrayList<ICodeEnum>();
		for(LockExceptionCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
