package org.ccframe.subsys.bike.socket.controller;

import java.util.Map;

import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class ImsiConnectController implements ISocketController {

	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.IMSI_INFO;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		
		System.out.println("IMSI码"+requestDataMap.get(DataBlockTypeEnum.IMSI));
		System.out.println("锁的状态"+requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS));
		System.out.println("锁的电量"+requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
		System.out.println("csp信号="+requestDataMap.get(DataBlockTypeEnum.GPS_INFO));
		
		//长连接事件，数据原封不动丢回去完事
		return requestDataMap;
	}

}
