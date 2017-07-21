package org.ccframe.subsys.bike.socket.tcpobj;

/**
 * 应答字
 *
 * @author jim
 */
public enum AnswerFlagEnum{

    /**
     * 成功(0x00)
     */
	SUCCESS(0x00),
    /**
     * 命令被有条件的执行(0x01)
     */
	EXECUTED(0x01),
	/**
	 * 命令编号错(0x02)	
	 */
	COMMAND_TYPE_ERROR(0x02),
	/**
	 * 长度错(0x03)
	 */
	LENTCH_ERROR(0x03),
	/**
	 * CRC校验错(0x04)
	 */
	CRC_ERROR(0x04),
	/**
	 * 其它错误(0xFE)
	 */
	OTHER_ERROR(0xfe),
	/**
	 * 该包是一个命令包(0xff)
	 */
	COMMAND(0xff);
	
	private int value;

	private AnswerFlagEnum(int value) {
		this.value = value;
	}

	public static AnswerFlagEnum fromValue(int value) {
        for (AnswerFlagEnum requestFlagEnum : AnswerFlagEnum.values()) {
            if (requestFlagEnum.value == value) {
                return requestFlagEnum;
            }
        }
        return null;
	}

	public int toValue() {
		return value;
	}
}