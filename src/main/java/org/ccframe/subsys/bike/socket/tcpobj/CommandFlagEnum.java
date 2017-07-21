package org.ccframe.subsys.bike.socket.tcpobj;

/**
 * 命令字
 *
 * @author jim
 */
public enum CommandFlagEnum{

    /**
     * 禁止使用(0x00)
     */
	FORBIDDEN(0x00),
    /**
     * 设备主动告警(0x01)
     */
	WARNING(0x01),
	/**
	 * 查询(0x02)	
	 */
	QUERY(0x02),
	/**
	 * 设置(0x03)
	 */
	SETUP(0x03),
	/**
	 * 开锁指令
	 */
	OPEN_LOCK(0x04),
	/**
	 * MAC信息
	 */
	MAC_INFO(0x06),
	/**
	 * IMSI信息
	 */
	IMSI_INFO(0x08),
	/**
	 * 状态信息
	 */
	STATE_INFO(0x0a),
	/**
	 * GPS信息
	 */
	GPS_INFO(0x0b),
	/**
	 * 同步时间信息
	 */
	SYN_TIME_INFO(0x10),
	/**
	 * 蓝牙桩信息
	 */
	BLUETOOTH_INFO(0x12),
	/**
	 * 蓝牙桩扫描信息
	 */
	BLUETOOTH_SCAN_INFO(0x13),
	/**
	 * 设置长连接时间信息
	 */
	LONGCONNECT_TIME_INFO(0x14),
	/**
	 * 模式信息
	 */
	MODE_INFO(0x11),
	/**
	 * BT开锁指令
	 */
	BT_OPRN_LOCK(0x15),
	/**
	 * IC卡开锁
	 */
	IC_OPEN_LOCK(0x16),
	/**
	 * 设备关锁(0x80)
	 */
	LOCKED(0x80),
	/**
	 * 升级指令
	 */
	UPGRADE_GPS(0x90),
	/**
	 * 升级完成
	 */
	UPGRADE_FINISH(0x91)
	;
	
	private int value;

	private CommandFlagEnum(int value) {
		this.value = value;
	}

	public static CommandFlagEnum fromValue(int value) {
        for (CommandFlagEnum requestFlagEnum : CommandFlagEnum.values()) {
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