package org.ccframe.subsys.bike.socket.commons;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBiMap;

public class SmartLockChannelUtil {
	
	private static final int MAX_CONNECTION=10000; /* 默认10000个长连接 */
	
	/**
	 * 双向map
	 */
	private static HashBiMap<Long,Channel> chanelMap = HashBiMap.<Long,Channel>create(MAX_CONNECTION);
	
	private static Logger logger = LoggerFactory.getLogger(SmartLockChannelUtil.class);
	
	synchronized public static void checkAndRegisterChannel(Long smartLockId, Channel channel){
		if(chanelMap.get(smartLockId) == null){
			chanelMap.put(smartLockId, channel);
			logger.info("connected smartLockId channel >> {}", smartLockId);
		}
	}

	synchronized public static void unRegisterChannel(Channel channel){
		logger.info("remove smartLockId >> {}", chanelMap.inverse().remove(channel));
	}
	
	public boolean isChannelActive(Long smartLockId){
		return chanelMap.get(smartLockId) != null;
	}
	
	public static void writeAndFlushBytes(Long smartLockId, byte[] bytes){
		Channel channel = chanelMap.get(smartLockId);
		if(channel != null){
			channel.writeAndFlush(bytes);
		}
	}
}
