package org.ccframe.subsys.bike.socket.controller;

import java.util.Map;

import org.ccframe.client.ResGlobal;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.commons.SmartLockChannelUtil;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

/**
 * 抓取开锁回复的指令.
 * @author JIM
 *
 */
@Component
public class OpenLockController implements ISocketController {

	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.OPEN_LOCK;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		Byte lockerErrorInfo = (byte)requestDataMap.get(DataBlockTypeEnum.LOCK_ERROR);
		if(lockerErrorInfo != null){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该单车出现故障，请更换一辆单车"});
		}

		//开锁成功，更新车锁状态，生成骑行订单

		SmartLockChannelUtil.notifyUnlocked(lockerHardwareCode);
		return null; //不回写了
	}

}
