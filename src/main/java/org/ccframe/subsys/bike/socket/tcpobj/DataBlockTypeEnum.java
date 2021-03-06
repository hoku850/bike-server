package org.ccframe.subsys.bike.socket.tcpobj;

/**
 * 数据块类型
 *
 * @author wj
 */
public enum DataBlockTypeEnum{

	//-------------------设备只读数据----------------------
	
    /**
     * 系统保留(0x0001)
     */
	RESERVED((short)0x0001),
    /**
     * 设备厂商代码(0x0001)
     */
	FACTORY_CODE((short)0x0001),
	/**
	 * 设备类别(0x0003)	
	 */
	LOCK_TYPE((short)0x0003),
	/**
	 * 设备型号(0x0004)
	 */
	LOCK_MODEL((short)0x0004),
	/**
	 * 设备生产序号(0x0005)
	 */
	FACTORY_NUM((short)0x0005),
	/**
	 * 经度(0x0006)
	 */
	LOCK_LNG((short)0x0006),
	/**
	 * 纬度(0x0007)
	 */
	LOCK_LAT((short)0x0007),
	/**
	 * 监控版本信息(0x0008)
	 */
	SOFTWARE_VERSION((short)0x0008),
	/**
	 * IMSI码(0x0009)
	 */
	IMSI((short)0x0009),
	/**
	 * 锁的状态(0x0010)
	 */
	LOCK_STATUS((short)0x0010),
	/**
	 * 锁的异常信息(0x0050)
	 */
	LOCK_ERROR((short)0x0050),
	/**
	 * 锁的电量(0x0051)
	 */
	LOCK_BATTERY((short)0x0051),
	/**
	 * 用户ID/IC 卡 ID(0x0052)
	 */
	USER_ID((short)0x0052),
	/**
	 * GPS信息标识(0x0053)
	 */
	GPS_INFO((short)0x0053),
	/**
	 * 进入蓝牙低功耗模式电量值(0x0054)
	 */
	GPS_GATE((short)0x0054),

	//-------------------网络参数（可以设置，可以读取）----------------------

	/**
	 * 单车设备号(0x0101)
	 */
	MODEL_NUM((short)0x0101),
	
	/**
	 * 锁的开关(0x0104)
	 */
	LOCK_IF_OPEN((short)0x0104),
	
	/**
	 * 锁的MAC(0x0105)
	 */
	LOCK_MAC((short)0x0105),

	/**
	 * 开锁时长(0x0108)
	 */
	UNLOCK_TIME_DURATION((short)0x0108),
	
	/**
	 * GPRS卫星个数(0x010A)
	 */
	CPRS_SATELLITE_NUM((short)0x010a),

	/**
	 * GPRS 连接状态设置(0x010B)
	 */
	GPRS_KEEP_ALIVE((short)0x010B),
	
	/**
	 * GPS上报类型(0x0113)
	 */
	GPS_REPORT_TYPE((short)0x0113),
	
	/**
	 * 上报信号 csq(0x0114)
	 */
	CSQ((short)0x0114),
	
	/**
	 * 时间(0x0150)
	 */
	SYS_TIME((short)0x0150),
	
	//-------------------告警和状态（可上报， 可读取，不能设置）----------------------
	
	/**
	 * 告警信息(0x0301)
	 */
	WARN_INFO((short)0x0301),
	
	//-------------------升级指令----------------------
	
	/**
	 * 升级包类型(0x0901)
	 */
	UPGRADE((short)0x0901),
	
	/**
	 * 蓝牙桩类型
	 */
	BLUETOOTH_TYPE((short)1201),
	;
	
	private short value;

	private DataBlockTypeEnum(short value) {
		this.value = value;
	}

	public static DataBlockTypeEnum fromValue(short value) {
        for (DataBlockTypeEnum dataBlockType : DataBlockTypeEnum.values()) {
            if (dataBlockType.value == value) {
                return dataBlockType;
            }
        }
        return null;
	}

	public short toValue() {
		return value;
	}

}
