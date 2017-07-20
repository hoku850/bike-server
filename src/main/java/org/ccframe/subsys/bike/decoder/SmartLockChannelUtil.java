package org.ccframe.subsys.bike.decoder;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SmartLockChannelUtil {
	
	private static Map<Integer, Channel> chanelMap = new ConcurrentHashMap<>();
	
	public static void registerChannel(Integer smartLockId, Channel channel){
		chanelMap.put(smartLockId, channel);
	}
	public static void writeBytes(Integer smartLockId, byte[] bytes){
//		Channel channel = chanelMap.get(smartLockId);
		Channel channel = chanelMap.entrySet().iterator().next().getValue();
		channel.writeAndFlush(bytes);
		System.out.print("writeBytes");
	}
}
