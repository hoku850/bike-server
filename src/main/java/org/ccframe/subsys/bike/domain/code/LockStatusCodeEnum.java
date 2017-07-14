package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

/**
 * 锁的状态
 * @author JIM
 *
 */
public enum LockStatusCodeEnum implements ICodeEnum{

    /**
     * 不使用
     */
	UNUSED,
    /**
     * 关闭
     */
	CLOSED,
	/**
	 * 打开
	 */
	OPEN;
	
	public static LockStatusCodeEnum fromCode(String code) {
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
		for(LockStatusCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
