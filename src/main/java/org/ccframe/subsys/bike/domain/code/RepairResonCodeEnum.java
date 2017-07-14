package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum RepairResonCodeEnum implements ICodeEnum {
	
	/**
	 * 坐垫
	 */
	A,
	
	/**
	 * 车锁
	 */
	B,
	
	/**
	 * 链条
	 */
	C,
	
	/**
	 * 车把
	 */
	D,
	
	/**
	 * 刹车
	 */
	E,
	
	/**
	 * 轮胎
	 */
	F,
	
	/**
	 * 踏板
	 */
	G,
	
	/**
	 * 其它
	 */
	H;
	
	public static RepairResonCodeEnum fromCode(String code) {
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
		for(RepairResonCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
