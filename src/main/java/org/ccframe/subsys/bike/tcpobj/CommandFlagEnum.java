package org.ccframe.subsys.bike.tcpobj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.BusinessTypeCodeEnum;


/**
 * 命令字
 *
 * @author jim
 */
public enum CommandFlagEnum{

    /**
     * 禁止使用(0x00)
     */
	FORBIDDEN((byte)0x00),
    /**
     * 设备主动告警(0x01)
     */
	WARNING((byte)0x01),
	/**
	 * 查询(0x02)	
	 */
	QUERY((byte)0x02),
	/**
	 * 设置(0x03)
	 */
	SETUP((byte)0x03),
	/**
	 * 设备关锁(0x80)
	 */
	LOCKED((byte)0x80);
	
	private byte value;

	private CommandFlagEnum(byte value) {
		this.value = value;
	}

	public static CommandFlagEnum fromValue(byte value) {
        for (CommandFlagEnum requestFlagEnum : CommandFlagEnum.values()) {
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
