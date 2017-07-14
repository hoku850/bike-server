package org.ccframe.subsys.bike.tcpobj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.BusinessTypeCodeEnum;


/**
 * 应答字
 *
 * @author jim
 */
public enum AnswerFlagEnum{

    /**
     * 成功(0x00)
     */
	SUCCESS((byte)0x00),
    /**
     * 命令被有条件的执行(0x01)
     */
	EXECUTED((byte)0x01),
	/**
	 * 命令编号错(0x02)	
	 */
	COMMAND_TYPE_ERROR((byte)0x02),
	/**
	 * 长度错(0x03)
	 */
	LENTCH_ERROR((byte)0x03),
	/**
	 * CRC校验错(0x04)
	 */
	CRC_ERROR((byte)0x04),
	/**
	 * 其它错误(0xFE)
	 */
	OTHER_ERROR((byte)0xfe),
	/**
	 * 该包是一个命令包(0xff)
	 */
	COMMAND((byte)0xff);
	
	private byte value;

	private AnswerFlagEnum(byte value) {
		this.value = value;
	}

	public static AnswerFlagEnum fromValue(byte value) {
        for (AnswerFlagEnum requestFlagEnum : AnswerFlagEnum.values()) {
            if (requestFlagEnum.value == value) {
                return requestFlagEnum;
            }
        }
        return null;
	}

	public byte toValue() {
		return value;
	}
}
