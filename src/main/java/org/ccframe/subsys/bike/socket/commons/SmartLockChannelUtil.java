package org.ccframe.subsys.bike.socket.commons;

import com.google.gwt.thirdparty.guava.common.collect.HashBiMap;

import io.netty.channel.Channel;

public class SmartLockChannelUtil {
	
	/**
	 * 双向map
	 */
	private static HashBiMap<Long,Channel> chanelMap = HashBiMap.<Long,Channel>create();
	
	synchronized public static void checkAndRegisterChannel(Long smartLockId, Channel channel){
		if(chanelMap.get(smartLockId) != null){
			chanelMap.put(smartLockId, channel);
		}
	}

	synchronized public static void unRegisterChannel(Channel channel){
		chanelMap.inverse().remove(channel);
	}
	
	public static void writeBytes(Long smartLockId, byte[] bytes){
	}
}
