package org.ccframe.subsys.bike.socket.commons;

import java.util.Map;

import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;

public interface ISocketController {
	CommandFlagEnum getCommand();
	Map<DataBlockTypeEnum, Object> execute(String lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap);
}
